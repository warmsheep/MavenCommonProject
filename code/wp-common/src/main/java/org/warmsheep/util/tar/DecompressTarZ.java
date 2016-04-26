package org.warmsheep.util.tar;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;

import org.apache.commons.compress.archivers.tar.TarArchiveEntry;
import org.apache.commons.compress.archivers.tar.TarArchiveInputStream;
import org.apache.commons.compress.compressors.z.ZCompressorInputStream;

/**
 * 解压tar.Z文件
 * @description
 * @author 刘高林
 * @version 1.0,2016年3月29日 下午10:32:38,刘高林,Ins
 */
public class DecompressTarZ {

	/**
	 * tar.Z文件解压
	 * @param file 需要解压tar.Z的文件
	 * @return
	 */
	public static File deCompressTZFile(String file) {
		return deCompressZFile(new File(file));
	}

	/**
	 * 解压.Z文件
	 * @param file
	 * @return
	 */
	private static File deCompressZFile(File file) {
		FileOutputStream out = null;
		ZCompressorInputStream zIn = null;
		try {
			//生成的tar临时文件
			final String tmpFileName = file.getName().substring(0, file.getName().indexOf(".")) + ".tar";
			//
			FileInputStream fin = new FileInputStream(file);
			BufferedInputStream in = new BufferedInputStream(fin);
			File outFile = new File(file.getParent() + File.separator + tmpFileName);
			out = new FileOutputStream(outFile);
			zIn = new ZCompressorInputStream(in);
			final byte[] buffer = new byte[2048];
			int n = 0;
			while (-1 != (n = zIn.read(buffer))) {
				out.write(buffer, 0, n);
			}
			return outFile;
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		} finally {
			try {
				out.close();
				zIn.close();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	}

	/**
	 * 解压.tar文件
	 * @param file
	 * @param basePath 解压后文件存放目录(绝对路径)
	 */
	public static void deCompressTARFile(File file, String basePath) throws Exception {
		TarArchiveInputStream is = null;
		try {
			is = new TarArchiveInputStream(new FileInputStream(file));
			while (true) {
				TarArchiveEntry entry = is.getNextTarEntry();
				if (entry == null) {
					break;
				}
				if (entry.isDirectory()) {// 这里貌似不会运行到，跟ZipEntry有点不一样
					new File(basePath + entry.getName()).mkdirs();
				} else {
					FileOutputStream os = null;
					try {
						File f = new File(basePath + entry.getName());
						if (!f.getParentFile().exists()) {
							f.getParentFile().mkdirs();
						}
						if (!f.exists()) {
							f.createNewFile();
						}
						os = new FileOutputStream(f);
						byte[] bs = new byte[2048];
						int len = -1;
						while ((len = is.read(bs)) != -1) {
							os.write(bs, 0, len);
						}
						os.flush();
					} catch (Exception e) {
						e.printStackTrace();
					} finally {
						os.close();
					}
				}
			}
		} catch (Exception e) {
			throw e;
		} finally {
			try {
				is.close();
				file.delete();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	}
	
	//测试
	public static void main(String[] args) {
		String file = "F:\\易联众\\商户信息\\增量00012210MTDFile20160314.tar.Z";
		File f = DecompressTarZ.deCompressTZFile(file);
		try {
			DecompressTarZ.deCompressTARFile(f, "F:/易联众/商户信息/untargz/merchant/");
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

}
