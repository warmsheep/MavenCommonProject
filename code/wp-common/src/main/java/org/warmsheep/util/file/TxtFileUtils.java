package org.warmsheep.util.file;

import java.io.BufferedOutputStream;
import java.io.BufferedReader;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.RandomAccessFile;

import org.apache.commons.lang.StringUtils;

/**
 * Txt格式文件读写帮助类
 */
public class TxtFileUtils {

	/**
	 * 创建文件目录
	 * @param path 文件路径
	 * @throws IOException
	 * @version 1.0,2015年11月23日 下午5:19:24,Liugl,Ins
	 */
	public static void createDir(String path) throws IOException {
		if (StringUtils.isNotBlank(path)) {
			File file = new File(path);
			file.mkdirs();
		} else {
			throw new IOException("文件路径错误");
		}
	}

	/**
	 * 创建文本文件
	 * @param file
	 * @throws IOException
	 * @version 1.0,2015年11月23日 下午5:29:12,Liugl,Ins
	 */
	public static void creatTxtFile(File file) throws IOException {
		if (!file.exists()) {
			file.createNewFile();
			System.err.println(file + "已创建！");
		}
	}

	/**
	 * 读取文本文件
	 * @param file
	 * @return
	 * @version 1.0,2015年11月23日 下午5:29:37,Liugl,Ins
	 */
	public static String readTxtFile(File file) {
		StringBuilder readStr = new StringBuilder();
		FileReader fileread = null;
		BufferedReader bufread = null;
		try {
			fileread = new FileReader(file);
			bufread = new BufferedReader(fileread);
			String read = "";
			while ((read = bufread.readLine()) != null)
				readStr.append(read).append("\r\n");
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
		return readStr.toString();
	}

	/**
	 * 写文件
	 * @param newStr
	 * @param file
	 * @throws IOException
	 * @version 1.0,2015年11月23日 下午5:29:44,Liugl,Ins
	 */
	public static void writeTxtFile(String newStr, File file)
			throws IOException {
		// 先读取原有文件内容，然后进行写入操作
		final String readStr = readTxtFile(file);
		final String filein = newStr + "\r\n" + readStr + "\r\n";
		RandomAccessFile mm = null;
		try {
			mm = new RandomAccessFile(file, "rw");
			mm.writeBytes(filein);
		} catch (IOException e1) {
			e1.printStackTrace();
		} finally {
			if (mm != null) {
				try {
					mm.close();
				} catch (IOException e2) {
					e2.printStackTrace();
				}
			}
		}
	}

	/**
	 * 文件转byte[]
	 * @param filePath
	 * @return
	 * @version 1.0,2015年11月26日 下午7:48:39,Liugl,Ins
	 */
	public static byte[] File2byte(String filePath) {
		byte[] buffer = null;
		try {
			File file = new File(filePath);
			FileInputStream fis = new FileInputStream(file);
			ByteArrayOutputStream bos = new ByteArrayOutputStream();
			byte[] b = new byte[1024];
			int n;
			while ((n = fis.read(b)) != -1) {
				bos.write(b, 0, n);
			}
			fis.close();
			bos.close();
			buffer = bos.toByteArray();
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
		return buffer;
	}

	/**
	 * byte[]转文件
	 * @param buf
	 * @param filePath
	 * @param fileName
	 * @version 1.0,2015年11月26日 下午7:49:03,Liugl,Ins
	 */
	public static void byte2File(byte[] buf, String filePath, String fileName) {
		BufferedOutputStream bos = null;
		FileOutputStream fos = null;
		File file = null;
		try {
			File dir = new File(filePath);
			if (!dir.exists() && dir.isDirectory()) {
				dir.mkdirs();
			}
			file = new File(filePath + File.separator + fileName);
			fos = new FileOutputStream(file);
			bos = new BufferedOutputStream(fos);
			bos.write(buf);
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			if (bos != null) {
				try {
					bos.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
			if (fos != null) {
				try {
					fos.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		}
	}

	// 测试
	public static void main(String[] s) throws IOException {
		String path = "E:\\aaa.txt";
		byte[] b = File2byte(path);
		InputStreamReader isr = new InputStreamReader(new ByteArrayInputStream(b));
		BufferedReader br = new BufferedReader(isr);
		try {
			while (br.ready()) {
				String str = br.readLine();
				System.out.println("-------|"+ str);
			}
		} catch (IOException e) {
		}
//		File file = new File(path + "aa.txt");
//		TxtFileUtils.createDir("D:/logs/");
//		TxtFileUtils.creatTxtFile(file);
//		TxtFileUtils.writeTxtFile("########", file);// 结束标记
//		int i = 0;
//		while (i < 10) {
//			TxtFileUtils.writeTxtFile("abcdefghijklmnopqrstuvwxyz_0000" + i, file);
//			i++;
//		}
//		TxtFileUtils.writeTxtFile("P|3000|8000000", file);// 结束标记
	}
}
