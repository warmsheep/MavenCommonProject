package org.warmsheep.util.ftp;

import java.io.BufferedReader;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.SocketException;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import jxl.common.Logger;

import org.apache.commons.net.ftp.FTPClient;
import org.apache.commons.net.ftp.FTPFile;
import org.apache.commons.net.ftp.FTPReply;
import org.warmsheep.util.DateUtils;
import org.warmsheep.util.StringUtils;

/**
 * FTP处理工具类
 */
public class FtpUtil {

	private static Logger logger = Logger.getLogger(FtpUtil.class);
	private FTPClient ftp;
	public static final String ACTIVE_MODE = "1";
	public static final String PASSIVE_MODE = "2";
	private String host;
	private int port;
	private String username;
	private String password;
	private String ftpTransferMode;
	private String path;
	
	public FtpUtil(){
		
	}
	
	public FtpUtil(String host, int port, String username, String password,String path,String ftpTransferMode){
		this.path = path;
		this.host = host;
		this.port = port;
		this.username = username;
		this.password = password;
		this.ftpTransferMode = ftpTransferMode;
	}
	
	
	/**
	 * 链接FTP服务器
	 * @throws Exception
	 */
	private void connect() throws Exception {
		connect(path, host, port, username, password, ftpTransferMode);
	}

	/**
	 * 链接FTP服务器
	 * @param path FTP文件存放相对路径
	 * @param host FTP服务器hostname
	 * @param port FTP端口号
	 * @param username FTP用户名
	 * @param password FTP密码
	 * @return
	 * @throws Exception
	 */
	private void connect(String path, String host, int port, String username, String password,String ftpTransferMode) throws Exception {
		int reply;
		ftp = new FTPClient();
		//链接
		ftp.connect(host, port);
		//检验是否连接成功
		reply = ftp.getReplyCode();
		if (!FTPReply.isPositiveCompletion(reply)) {
			logger.error("####ERROR: Connect FTP service failure! [hostname:" + host + ", port:" + port+"]");
			//链接失败
			ftp.disconnect();
			throw new Exception("Connect FTP service failure!");
		}
		//登陆
		ftp.login(username, password);
		//每次数据连接之前,ftpClient告诉ftpServer开通一个端口来传输数据
		//是否被动模式
		if(PASSIVE_MODE.equals(ftpTransferMode)){
			ftp.enterLocalPassiveMode();
		}
		//转移工作目录至指定目录下
		if (StringUtils.isNotBlank(path))
			ftp.makeDirectory(path);
		ftp.changeWorkingDirectory(path);
		
		//文件读取类型（二进制）
		ftp.setFileType(FTPClient.BINARY_FILE_TYPE);
		//设置传输超时时间为30分钟
		ftp.setDataTimeout(1800000);
		//连接超时为30分钟
		ftp.setConnectTimeout(1800000);
		
		logger.info("####SUCCESS: Connect FTP service success! [hostname:" + host + ", port:" + port+", path:" + path + "]");
	}

	/**
	 * 关闭FTP服务
	 * @version 1.0,2016年1月4日 上午11:54:03,Liugl,Ins
	 */
	public void closeServer() throws Exception {
		try {
			ftp.logout();
		} catch (IOException e) {
			logger.error("####ERROR: Disconnect FTP service failure! " + e);
			throw new IOException("Disconnect FTP service failure!", e);
		} finally {
			if(ftp.isConnected()) {
				try {
					ftp.disconnect();
					logger.info("####SUCCESS: Disconnect FTP service success!");
				} catch (IOException e) {
					throw new IOException("Disconnect FTP service failure!", e);
				}
			}
		}
	}

	/**
	 * 上传的文件或文件夹
	 * @throws Exception
	 */
	private void upload(File file) throws Exception {
		if (file.isDirectory()) {
			ftp.makeDirectory(file.getName());
			ftp.changeWorkingDirectory(file.getName());
			String[] files = file.list();
			for (int i = 0; i < files.length; i++) {
				File file1 = new File(file.getPath() + "\\" + files[i]);
				if (file1.isDirectory()) {
					upload(file1);
					ftp.changeToParentDirectory();
				} else {
					File file2 = new File(file.getPath() + "\\" + files[i]);
					FileInputStream input = new FileInputStream(file2);
					ftp.storeFile(file2.getName(), input);
					input.close();
				}
			}
		} else {
			File file2 = new File(file.getPath());
			FileInputStream input = new FileInputStream(file2);
			ftp.storeFile(file2.getName(), input);
			input.close();
		}
	}
	
	/**
	 * 文件上传
	 * @param fileName
	 * @param input
	 * @throws Exception
	 */
	public void upload(String fileName, InputStream input) throws Exception{
		upload(path, host, port, username, password, fileName, input, ftpTransferMode);
	}

	/**
	 * 文件上传
	 * @param path FTP文件存放相对路径
	 * @param host FTP服务器地址
	 * @param port 端口
	 * @param username 用户名
	 * @param password 密码
	 * @param fileName 保存到服务器的文件名
	 * @param input 文件流
	 * @throws Exception
	 * @version 1.0,2015年11月27日 下午5:49:06,Liugl,Ins
	 */
	public void upload(String path, String host, int port, String username, String password, String fileName, InputStream input,String ftpTransferMode)
			throws Exception {
		try {
			// 连接服务
			connect(path, host, port, username, password,ftpTransferMode);
			// 存储文件
			ftp.storeFile(fileName, input);
		} catch (Exception ex) {
			throw ex;
		} finally {
			if (null != input) {
				input.close();
				input = null;
			}
			// 关闭链接
			closeServer();
		}
	}
	
	/**
	 * 读取FTP服务器制定目录下文件内容
	 * @param path FTP文件存储相对目录
	 * @param host 服务器地址
	 * @param port 端口
	 * @param username 用户名
	 * @param password 密码
	 * @param fileName 文件路径
	 * @param chartCode 字符编码
	 * @return
	 * @throws Exception
	 * @version 1.0,2016年1月4日 上午11:33:54,Liugl,Ins
	 */
	public List<String> readFile(String path, String host, int port,
			String username, String password, String fileName, String chartCode,String ftpTransferMode) throws Exception {
		List<String> array = new ArrayList<String>();
		InputStream input = null;
		BufferedReader reader = null;
		try {
			//连接服务
			connect(path, host, port, username, password,ftpTransferMode);
			//读取文件内容
			input = ftp.retrieveFileStream(fileName);
			reader = new BufferedReader(new InputStreamReader(input, chartCode));
			String line = null;
			while (StringUtils.isNotBlank(line = reader.readLine()))
				array.add(line);
			//主动调用一次getReply()把接下来的226消费掉. 这样做是可以解决这个返回null问题 
			ftp.getReply();
			//END
		} catch (Exception ex) {
			throw new Exception("Read file does not exist!", ex);
		} finally {
			if (null != input) {
				input.close();
				input = null;
			}
			if (null != reader) {
				reader.close();
				reader = null;
			}
			// 关闭链接
			closeServer();
		}
		return array;
	}

	/**
	 * 删除FTP服务器上文件
	 * @param fileName 文件路径
	 * @version 1.0,2016年1月4日 上午11:48:39,Liugl,Ins
	 */
	public void deleteFile(String fileName) {
		try {
			ftp.deleteFile(fileName);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	/**
	 * 读取FTP指定目录下所有文件名
	 * @param path FTP文件存储相对目录
	 * @return
	 * @version 1.0,2016年1月4日 上午11:50:14,Liugl,Ins
	 */
	public List<String> getFileNameList(String path) throws IOException {
		List<String> fileLists = new ArrayList<String>();
		FTPFile[] ftpFiles	= ftp.listFiles(path);
		for (int i = 0; ftpFiles != null && i < ftpFiles.length; i++) {
			FTPFile file = ftpFiles[i];
			if (file.isFile()) {
				fileLists.add(file.getName());
			}
		}
		return fileLists;
	}
	
	/**
	 * 读取FTP指定目录下所有文件内容
	 * @return
	 * @throws Exception
	 */
	public Map<String, byte[]> readAllFile() throws Exception {
		return readAllFile(path, host, port, username, password, ftpTransferMode);
	}
	
	/**
	 * 读取FTP指定目录下所有文件内容
	 * @param path 文件存放地址
	 * @param host FTP服务器地址
	 * @param port FTP服务器端口
	 * @param username FTP登录账号
	 * @param password FTP登录密码
	 * @param ftpTransferMode FTP传输模式 1、主动 2、被动
	 * @return Map<String,byte[]>{文件名,文件内容byte[]}
	 * @throws Exception
	 * @version 1.0,2016年1月12日 上午11:42:15,Liugl,Ins
	 */
	public Map<String, byte[]> readAllFile(String path, String host, int port, String username, String password,String ftpTransferMode) throws Exception {
		Map<String, byte[]> hash = new HashMap<String, byte[]>();
		InputStream input = null;
		byte[] bytes = null;
//		InputStreamReader isr = null;
//		BufferedReader br = null;
		try {
			//连接服务
			connect(path, host, port, username, password, ftpTransferMode);
			//文件读取
			FTPFile[] ftpFiles = ftp.listFiles(path);
			for (int i = 0; ftpFiles != null && i < ftpFiles.length; i++) {
				FTPFile file = ftpFiles[i];
				if (file.isFile()) {
					input = ftp.retrieveFileStream(file.getName());
					if (null != input) {
						//数据格式转换
						bytes = input2byte(input);
						//内容转换为String
//						isr = new InputStreamReader(new ByteArrayInputStream(bytes), "GBK");
//						br = new BufferedReader(isr);
//						while (br.ready())
//							list.add(br.readLine());
						//保存文件内容，key=文件名
						hash.put(file.getName(), bytes);
						//主动调用一次getReply()把接下来的226消费掉，这样做是可以解决这个返回null问题 
						ftp.getReply();
					}
				}
			}
		} catch (Exception ex) {
			throw new Exception("Read file does not exist!", ex);
		} finally {
			if (null != input)
				input.close();
			if (null != bytes)
				bytes.clone();
			// 关闭链接
			closeServer();
		}
		return hash;
	}
	
	/**
	 * 读取FTP服务器制定目录下的text文件内容
	 * @param fileName
	 * @return
	 * @throws Exception
	 */
	public byte[] readFileToBytes(String fileName) throws Exception {
		return readFileToBytes(path, host, port, username, password, fileName, ftpTransferMode);
	}
	
	/**
	 * 读取FTP服务器制定目录下的text文件内容
	 * @param path ftp子目录
	 * @param host 服务器地址
	 * @param port 端口
	 * @param username 用户名
	 * @param password 密码
	 * @param fileName 文件路径
	 * @param chartCode 字符编码
	 * @param ftpTransferMode FTP传输模式 1主动 2被动
	 * @return
	 * @throws Exception
	 * @version 1.0,2016年1月4日 上午11:33:54,Liugl,Ins
	 */
	public byte[] readFileToBytes(String path, String host, int port, String username, String password, String fileName,String ftpTransferMode) throws Exception {
		byte[] bytes = null;
		InputStream input = null;
		try {
			//连接服务
			connect(path, host, port, username, password,ftpTransferMode);
			//读取文件内容
			input = ftp.retrieveFileStream(fileName);
			if (null != input)
				bytes = input2byte(input);
			//主动调用一次getReply()把接下来的226消费掉. 这样做是可以解决这个返回null问题 
			ftp.getReply();
			//END
		} catch (Exception ex) {
			throw new Exception("Read file does not exist", ex);
		} finally {
			if (null != input) {
				input.close();
				input = null;
			}
			// 关闭链接
			closeServer();
		}
		return bytes;
	}
	
	/**
	 * 从FTP服务器下载文件到制定目录
	 * @param fileNameMap
	 * @param localPath
	 * @return
	 * @throws Exception
	 */
	public List<String> downloadFtpFileToLocal(Map<String, String> fileNameMap, String localPath) throws Exception {
		return downloadFtpFileToLocal(host, port, username, password, path, fileNameMap, localPath, ftpTransferMode, 0);
	}
	
	/**
	 * 从FTP服务器下载文件到制定目录
	 * @param fileNameMap
	 * @param localPath
	 * @return
	 * @throws Exception
	 */
	public List<String> downloadFtpFileToLocal(Map<String, String> fileNameMap, String localPath,int downloadMode) throws Exception {
		return downloadFtpFileToLocal(host, port, username, password, path, fileNameMap, localPath, ftpTransferMode, downloadMode);
	}

	/**
	 * 从FTP服务器下载文件到制定目录
	 * @param hostname FTP服务器地址
	 * @param port FTP服务器端口
	 * @param username FTP登录账号
	 * @param password FTP登录密码
	 * @param remotePath FTP服务器上的相对路径
	 * @param fileNameMap HashMap<'文件名','文件名'> 需要过滤掉的文件
	 * @param localPath 本地文件保存路径
	 * @param ftpTransferMode FTP传输模式1、主动 2、被动
	 * @return 下载文件记录信息
	 */
	public List<String> downloadFtpFileToLocal(String hostname, int port, String username, String password, String remotePath,
			Map<String, String> fileNameMap, String localPath,String ftpTransferMode,int downloadMode) throws Exception {
		List<String> result = new ArrayList<String>();
		try {
			//连接FTP服务
			connect(remotePath, hostname, port, username, password,ftpTransferMode);
			int replyCode = ftp.getReplyCode();
			if (replyCode == 550) {
				ftp.makeDirectory(remotePath);
			} else {
				logger.info("FTP Server getReplyCode() : " + replyCode);
				if (!FTPReply.isPositiveCompletion(replyCode)) {
					logger.info("FTP connection unexpectedly closed!");
					closeServer();//关闭FTP服务
					return null;
				}
			}
			//转移到FTP服务器目录
			ftp.changeWorkingDirectory(remotePath);
			
			//判断本地保存路径否存在，不存在，则创建目录
			File file = new File(localPath);
			if (!file.exists())
				file.mkdirs();
			
			//批量文件下载
			FTPFile[] fs = ftp.listFiles();
			for (FTPFile ff : fs) {
				File localFile = null;
				OutputStream outStream = null;
				//当为文件，且文件不存在已经处理过的Hash中时，则进行文件下载
				if (ff.isFile() && !fileNameMap.containsKey(ff.getName())) {
					//下载模式7，下载清算加密文件
					if(downloadMode == 7){
						//不下载清算原文件（只下载清算加密文件）
						if (ff.getName().indexOf("enc_ylz_remit") < 0){
							continue;
						}
					}
					localFile = new File(localPath + "/" + ff.getName());
					outStream = new FileOutputStream(localFile);
					ftp.retrieveFile(ff.getName(), outStream);
					if (null != outStream) {
						outStream.close();
						outStream = null;
					}
					//下载完成，添加到记录中
					result.add(ff.getName());
				}//if end
			}//for end
		} catch (Exception e) {
			logger.error("#ERROR: FTP file download failed!" + e);
			result = null;
		} finally {
			try {
				//关闭FTP服务
				closeServer();
			} catch (Exception e) {
				throw new Exception(e);
			}
			//返回结果
			return result;
		}
	}

	/**
	 * 文件转成 byte[]
	 * @param inStream
	 * @return
	 * @throws IOException
	 */
	public static byte[] input2byte(InputStream inStream) throws IOException {
		ByteArrayOutputStream swapStream = new ByteArrayOutputStream();
		byte[] buff = new byte[100];
		int rc = 0;
		while ((rc = inStream.read(buff, 0, 100)) > 0) {
			swapStream.write(buff, 0, rc);
		}
		byte[] in2b = swapStream.toByteArray();
		if (null != swapStream)
			swapStream.close();
		if (null != inStream)
			inStream.close();
		if (null != buff)
			buff.clone();
		return in2b;
	}
	
	/**
	 * 获取ftp上文件的最后修改时间
	 * @param host	 ftp主机地址 e.g. "127.0.0.1"
	 * @param username 用户名		e.g. "username"
	 * @param password 密码		e.g. "passowrd"
	 * @param path	 文件路径	e.g. "ftputil/test.txt"
	 * @return Date 如果连接成功返回java.util.Date时间，如果连接失败返回null
	 * @throws IOException 
	 * @throws SocketException 
	 */
	public Date getFileLastModifiedTime(String host,String username,String password,String path) throws SocketException, IOException{
		Date lastModifiedDate = null;
		FTPClient ftpClient = new FTPClient();
		ftpClient.connect(host);
		boolean isLogin = ftpClient.login(username, password);
		//连接ftp失败返回null
		if(!isLogin){
			ftpClient.disconnect();
			ftpClient = null;
			return null;
		}
		//获取ftp上path路径下的文件
		FTPFile[] fileList = ftpClient.listFiles(path);
		for (int i = 0; i < fileList.length; i++) {
			lastModifiedDate = fileList[i].getTimestamp().getTime();
		}
		return lastModifiedDate;
	}
	
	/**
	 * 获得ftp路径下面所有文件的修改时间
	 * @param host	 	ftp主机地址
	 * @param port		ftp端口号
	 * @param username	用户名
	 * @param password	密码
	 * @param path		文件路径
	 * @return java.util.Map 连接成功返回一个key是文件名，value是最后修改时间(java.util.Date)的一个map;连接失败返回null
	 * @throws SocketException
	 * @throws IOException
	 */
	public Map<String, String> getAllFileLastModifiedTime(String host, int port, String username, String password, String path,String ftpTransferMode) throws Exception{
		Date lastModifiedDate = null;
		//连接FTP
		this.connect(path, host, port, username, password,ftpTransferMode);
		
		//获取ftp上path路径下的文件
		FTPFile[] fileList = ftp.listFiles(path);
		//HashMap容量：fileList.length
		Map<String, String> map = new HashMap<String, String>(fileList.length);
		for (int i = 0; i < fileList.length; i++) {
			lastModifiedDate = fileList[i].getTimestamp().getTime();
			//key文件名；value修改时间
			map.put(fileList[i].getName(), DateUtils.formatDate(lastModifiedDate, "yyyyMMdd"));
		}
		return map;
	}
	
	/**
	 * 获取FTP下所有的文件名
	 * @return
	 * @throws Exception
	 */
	public FTPFile[] getAllFileName() throws Exception{
		return getAllFileName(host, port, username, password, path, ftpTransferMode);
	}
	/**
	 * 获取FTP下所有的文件名
	 * @param host
	 * @param port
	 * @param username
	 * @param password
	 * @param path
	 * @param ftpTransferMode
	 * @return
	 * @throws Exception
	 */
	public FTPFile[] getAllFileName(String host, int port, String username, String password, String path,String ftpTransferMode) throws Exception{
		FTPFile[] fileList = null;
		try {
			//连接FTP
			this.connect(path, host, port, username, password,ftpTransferMode);
			
			//获取ftp上path路径下的文件
			fileList = ftp.listFiles(path);
		} catch (Exception e) {
			e.printStackTrace();
			logger.error("#ERROR: FTP file download failed!" + e);
		} finally {
			closeServer();
		}
		return fileList;
	}
	
	public void moveFiles(List<String> origFilePath,String targetRootDir,String targetFileDir) throws Exception{
		moveFiles(host, port, username, password, path, ftpTransferMode, origFilePath, targetRootDir, targetFileDir);
	}
	
	public void moveFiles(String host, int port, String username, String password, String path,String ftpTransferMode,List<String> origFilePath,String targetRootDir,String targetFileDir) throws Exception{
		try {
			//连接FTP
			this.connect(path, host, port, username, password,ftpTransferMode);
			ftp.mkd(targetRootDir);
			ftp.changeWorkingDirectory(targetRootDir);
			ftp.mkd(targetFileDir);
			ftp.changeWorkingDirectory("/");
			for(int i = 0; i < origFilePath.size(); i++ ){
				logger.info("move origFilePath:" + targetRootDir + "/" + targetFileDir + "/" + origFilePath.get(i));
				ftp.rename(origFilePath.get(i), targetRootDir + "/" + targetFileDir + "/" + origFilePath.get(i));
			}
		} catch (Exception e) {
			e.printStackTrace();
			logger.error("#ERROR: FTP file download failed!" + e);
		} finally {
			closeServer();
		}
	}
	
}
