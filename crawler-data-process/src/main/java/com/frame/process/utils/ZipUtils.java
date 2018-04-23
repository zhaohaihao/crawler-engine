package com.frame.process.utils;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.Enumeration;

import org.apache.commons.lang3.ArrayUtils;
import org.apache.tools.zip.ZipEntry;
import org.apache.tools.zip.ZipFile;
import org.apache.tools.zip.ZipOutputStream;

import com.frame.process.constants.GobalConstant.FileType;

/**
 * zip压缩文件工具类
 * Created by zhh on 2017/04/23.
 */
public class ZipUtils {
	
	private ZipUtils() {}
	
//	private static Logger log = LoggerFactory.getLogger(ZipUtils.class);
	
	/** 避免 (压缩|解压) 中文文件名乱码 */
	private static final String CHINESE_CHARSET = "GBK";
	
	/** 文件读取的缓冲区的大小 */
	private static final int CACHE_SIZE = 1024;
	
	/** 默认后缀名 **/
	private static final String suffix = FileType.ZIP;
	
	/**
	 * 压缩文件至当前目录
	 * @param srcFilePaths 所需压缩文件路径
	 * @return
	 */
	public static boolean compress(String... srcFilePaths) {
		return compress(null, srcFilePaths);
	}
	
	/**
	 * 解压压缩文件至当前目录
	 * @param srcZipPath 压缩文件路径
	 * @return
	 */
	public static String unCompress(String srcZipPath) {
		return unCompress(srcZipPath, null);
	}
	
	/**
	 * 
	 * @param dstZipPath 压缩文件存放指定目录
	 * @param srcFilePaths 所需压缩文件路径
	 * @return
	 */
	public static boolean compress(String dstZipPath, String... srcFilePaths) {
		if (ArrayUtils.isEmpty(srcFilePaths)) {
			return false;
		}
		String targetFilePath = dstZipPath;
		if (dstZipPath == null) {
			dstZipPath = srcFilePaths[0];
			targetFilePath = FileUtils.getFatherFilePath(dstZipPath);
		}
		
		ZipOutputStream out = null;
		try {
			// 目标文件
			File targetFile = new File(targetFilePath);
			// 如果目标文件路径不存在, 则新创建
			if (!targetFile.exists()) {
				targetFile.mkdirs();
			}
			// 压缩包名
			String targetFileName = RandomUtils.genFileName() + suffix;
			targetFilePath = targetFilePath + "/" + targetFileName;
			FileOutputStream fos = new FileOutputStream(targetFilePath);
			out = new ZipOutputStream(new BufferedOutputStream(fos));
			// 不加压缩文件会报异常无法打开
			out.setEncoding(System.getProperty("sun.jnu.encoding"));
			for (String srcFilePath : srcFilePaths) {
				// 源文件
				File srcFile = new File(srcFilePath);
				String targetName = srcFile.getName();
				System.out.println(targetName);
				
				createCompressedFile(out, srcFile, targetName);
			}
			return true;
		} catch (Exception e) {
			e.printStackTrace();
			return false;
		} finally {
			if (out != null) {
				try {
					out.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		}
	}
	
	public static void main(String[] args) {
		String[] file = {"C:/Users/Administrator/Desktop/excel/TieBaContent_4ea476ce30a847bdaae64c5f42f41b3d.xlsx"};
		// 压缩
		compress("C:/Users/Administrator/Desktop/zip", file);
//		unCompress("C:/Users/Administrator/Desktop/zip/File_20180423172325187411.zip");
	}
	
	/**
	 * 解压压缩文件至指定目录
	 * @param srcZipPath 压缩文件路径
	 * @param dstZipPath 解压文件指定目录
	 * @return
	 */
	public static String unCompress(String srcZipPath, String dstZipPath) {
		if (srcZipPath == null) {
			return null;
		}
		if (dstZipPath == null) {
			dstZipPath = FileUtils.removeSuffix(srcZipPath);
			// log.info("dstZipPath: " + dstZipPath);
		}

		File file = new File(dstZipPath);
		if (file.exists()) {
			FileUtils.deleteFolder(dstZipPath);
		}
		file.mkdirs();

		ZipFile zipFile = null;
		File f = null;
		try {
			zipFile = new ZipFile(srcZipPath, CHINESE_CHARSET);
			f = new File(srcZipPath);

			String strPath, gbkPath, strtemp;
			File tempFile = new File(dstZipPath);
			strPath = tempFile.getAbsolutePath();
			// log.info("strPath: " + strPath);
			Enumeration<?> e = zipFile.getEntries();
			while (e.hasMoreElements()) {
				ZipEntry zipEntry = (ZipEntry) e.nextElement();
				gbkPath = zipEntry.getName();
				// log.info("gbkPath: " + gbkPath);
				if (zipEntry.isDirectory()) {
					strtemp = strPath + File.separator + gbkPath;
					// log.info("strtemp: " + strtemp);
					File dir = new File(strtemp);
					dir.mkdirs();
					continue;
				} else {
					// 读写文件
					InputStream is = zipFile.getInputStream(zipEntry);
					BufferedInputStream bis = new BufferedInputStream(is);
					gbkPath = zipEntry.getName();
					strtemp = strPath + File.separator + gbkPath;
					// 建目录
					String strsubdir = gbkPath;
					for (int i = 0; i < strsubdir.length(); i++) {
						if (strsubdir.substring(i, i + 1).equalsIgnoreCase("/")) {
							String temp = strPath + File.separator + strsubdir.substring(0, i);
							File subdir = new File(temp);
							if (!subdir.exists())
								subdir.mkdir();
						}
					}
					FileOutputStream fos = new FileOutputStream(strtemp);
					BufferedOutputStream bos = new BufferedOutputStream(fos);
					int c;
					byte[] b = new byte[CACHE_SIZE];
					while ((c = bis.read(b, 0, CACHE_SIZE)) != -1) {
						bos.write(b, 0, c);
					}
					bos.flush();
					bos.close();
					fos.close();
					bis.close();
					is.close();
				}
			}
			return dstZipPath;
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		} finally {
			if (zipFile != null) {
				try {
					zipFile.close(); // 压缩包被占用, 需关闭后才能删除
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
			if (f.exists())
				FileUtils.deleteFolder(srcZipPath);
		}
	}
	
	private static void createCompressedFile(ZipOutputStream out, File file, String dir) throws Exception{  
		// 如果当前的是文件夹，则进行进一步处理
		if (file.isDirectory()) {
			// 得到文件列表信息
			File[] files = file.listFiles();
			// 将文件夹添加到下一级打包目录
			out.putNextEntry(new ZipEntry(dir + "/"));

			dir = dir.length() == 0 ? "" : dir + "/";

			// 循环将文件夹中的文件打包
			for (int i = 0; i < files.length; i++) {
				// 递归处理
				createCompressedFile(out, files[i], dir + files[i].getName()); 
			}
		} else {
			// 当前的是文件，打包处理
			// 文件输入流
			FileInputStream fis = new FileInputStream(file);

			out.putNextEntry(new ZipEntry(dir));
			// 进行写操作
			int j = 0;
			byte[] buffer = new byte[CACHE_SIZE];
			while ((j = fis.read(buffer)) > 0) {
				out.write(buffer, 0, j);
			}
			// 关闭输入流
			fis.close();
		}
    }  
}
