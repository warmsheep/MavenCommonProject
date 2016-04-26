package org.warmsheep.util.rar;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.zip.GZIPInputStream;

import org.apache.commons.compress.archivers.ArchiveException;
import org.apache.commons.compress.archivers.ArchiveInputStream;
import org.apache.commons.compress.archivers.ArchiveStreamFactory;
import org.apache.commons.compress.archivers.tar.TarArchiveEntry;

/**
 * @description 解压tar.gz文件包
 */
public class GZip {

	private BufferedOutputStream bufferedOutputStream;
	String zipfileName = null;

	public GZip(String fileName) {
		this.zipfileName = fileName;
	}

	/**
	 * 文件解压
	 * @param rarFileName 需要解压的文件路径(具体到文件)
	 * @param destDir 解压目标路径
	 * @throws Exception
	 */
	public static void unTargzFile(String rarFileName, String destDir) throws Exception {
		GZip gzip = new GZip(rarFileName);
		File file = new File(destDir);
		if (!file.exists())
			file.mkdirs();
		// 解压文件到制定目录
		gzip.unzipOarFile(destDir);
	}
	
	/**
	 * 解压文件
	 * @param outputDirectory
	 * @throws Exception
	 */
	private void unzipOarFile(String outputDirectory) throws Exception {
		FileInputStream fis = null;
		ArchiveInputStream in = null;
		BufferedInputStream bufferedInputStream = null;
		try {
			fis = new FileInputStream(zipfileName);
			GZIPInputStream is = new GZIPInputStream(new BufferedInputStream(fis));
			in = new ArchiveStreamFactory().createArchiveInputStream("tar", is);
			bufferedInputStream = new BufferedInputStream(in);
			TarArchiveEntry entry = (TarArchiveEntry) in.getNextEntry();
			while (entry != null) {
				String name = entry.getName();
				String[] names = name.split("/");
				String fileName = outputDirectory;
				for (int i = 0; i < names.length; i++) {
					String str = names[i];
					fileName = fileName + File.separator + str;
				}
				if (name.endsWith("/")) {
					mkFolder(fileName);
				} else {
					File file = mkFile(fileName);
					bufferedOutputStream = new BufferedOutputStream(new FileOutputStream(file));
					int b;
					while ((b = bufferedInputStream.read()) != -1) {
						bufferedOutputStream.write(b);
					}
					bufferedOutputStream.flush();
					bufferedOutputStream.close();
				}
				entry = (TarArchiveEntry) in.getNextEntry();
			}
		} catch (FileNotFoundException e) {
			throw e;
		} catch (IOException e) {
			throw e;
		} catch (ArchiveException e) {
			throw e;
		} finally {
			try {
				if (bufferedInputStream != null) {
					bufferedInputStream.close();
				}
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	}
	
	/**
	 * 新建文件目录
	 * @param fileName
	 */
	private void mkFolder(String fileName) {
		File f = new File(fileName);
		if (!f.exists())
			f.mkdir();
	}
	/**
	 * 新建文件
	 * @param fileName
	 * @return
	 * @throws IOException
	 */
	private File mkFile(String fileName) throws IOException {
		File f = new File(fileName);
		try {
			f.createNewFile();
		} catch (IOException e) {
			throw e;
		}
		return f;
	}
	
	public static void main(String[] args) throws Exception {
		GZip.unTargzFile("D:\\ftpFile\\20160327\\00012210MTDFile20160324.tar.Z", "D:\\");
	}
}
