package org.warmsheep.util.rar;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.zip.GZIPInputStream;
import java.util.zip.GZIPOutputStream;

import org.apache.commons.lang3.StringUtils;

/**
 * GZIP工具
 */
public abstract class GZipUtils {

	public static final int BUFFER = 1024;
	public static final String EXT = ".gz";

	/**
	 * 数据压缩
	 * @param data
	 * @return
	 * @throws Exception
	 */
	public static byte[] compress(byte[] data) throws Exception {
		ByteArrayInputStream bais = new ByteArrayInputStream(data);
		ByteArrayOutputStream baos = new ByteArrayOutputStream();
		// 压缩
		compress(bais, baos);
		byte[] output = baos.toByteArray();
		
		baos.flush();
		baos.close();
		bais.close();

		return output;
	}

	/**
	 * 文件压缩
	 * @param file
	 * @param delete 是否删除原始文件
	 * @throws Exception
	 */
	public static void compress(File file, boolean delete) throws Exception {
		FileInputStream fis = new FileInputStream(file);
		FileOutputStream fos = new FileOutputStream(file.getPath() + EXT);

		compress(fis, fos);

		fis.close();
		fos.flush();
		fos.close();

		if (delete) {
			file.delete();
		}
	}

	/**
	 * 数据压缩
	 * @param is
	 * @param os
	 * @throws Exception
	 */
	public static void compress(InputStream is, OutputStream os) throws Exception {
		GZIPOutputStream gos = new GZIPOutputStream(os);
		int count;
		byte data[] = new byte[BUFFER];
		while ((count = is.read(data, 0, BUFFER)) != -1) {
			gos.write(data, 0, count);
		}
		gos.finish();
		gos.flush();
		gos.close();
	}

	/**
	 * 文件压缩
	 * @param path
	 * @param delete 是否删除原始文件
	 * @throws Exception
	 */
	public static void compress(String path, boolean delete) throws Exception {
		File file = new File(path);
		compress(file, delete);
	}

	/**
	 * 数据解压缩
	 * @param data
	 * @return
	 * @throws Exception
	 */
	public static byte[] decompress(byte[] data) throws Exception {
		ByteArrayInputStream bais = new ByteArrayInputStream(data);
		ByteArrayOutputStream baos = new ByteArrayOutputStream();
		// 解压缩
		decompress(bais, baos);

		data = baos.toByteArray();
		baos.flush();
		baos.close();
		bais.close();
		return data;
	}

	/**
	 * 文件解压缩
	 * @param file
	 * @param localPath 解压文件存放目标路径
	 * @param delete 是否删除原始文件
	 * @throws Exception
	 */
	public static void decompress(File file, String localPath, boolean delete) throws Exception {
		if (StringUtils.isBlank(localPath))
			throw new Exception("解压文件目标存放路径为空");
		//创建目录
		mkFolder(localPath);
		
		FileInputStream fis = new FileInputStream(file);
		FileOutputStream fos = new FileOutputStream(localPath + File.separator + file.getName().replace(EXT, ""));
		decompress(fis, fos);
		fis.close();
		fos.flush();
		fos.close();
		if (delete) {
			file.delete();
		}
	}

	/**
	 * 数据解压缩
	 * @param is
	 * @param os
	 * @throws Exception
	 */
	public static void decompress(InputStream is, OutputStream os) throws Exception {
		GZIPInputStream gis = new GZIPInputStream(is);
		int count;
		byte data[] = new byte[BUFFER];
		while ((count = gis.read(data, 0, BUFFER)) != -1) {
			os.write(data, 0, count);
		}
		gis.close();
	}

	/**
	 * 文件解压缩
	 * @param path 需要解压的文件路径
	 * @param localPath 解压文件存放目标路径
	 * @param delete 是否删除原始文件
	 * @throws Exception
	 */
	public static void decompress(String path, String localPath, boolean delete) throws Exception {
		File file = new File(path);
		decompress(file, localPath, delete);
	}
	
	/**
	 * 新建文件目录
	 * @param fileName
	 */
	private static void mkFolder(String fileName) {
		File f = new File(fileName);
		if (!f.exists())
			f.mkdirs();
	}
	
	public static void main(String[] args) throws Exception {
		decompress("F:/易联众/商户信息/全量文件0800012210_mchnt_S.txt.gz", "F:/易联众/商户信息/全量文件0800012210_mchnt_S/aaa/bb", false);
		System.out.println("------");
	}

}
