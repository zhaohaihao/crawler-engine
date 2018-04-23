package com.frame.process.utils;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

/**
 * 文件操作工具类
 * Created by zhh on 2017/04/23.
 */
public class FileUtils {
	
	/**
	 * 获取文件后缀名
	 * @param file 文件对象
	 * @return	      文件后缀名
	 */
	public static String getSuffix(File file) {
		return getSuffix(file.getName());
	}
	
	/**
	 * 获取文件后缀名
	 * @param fileName 文件名
	 * @return		      文件后缀名
	 */
	public static String getSuffix(String fileName) {
		if (fileName == null) {
			return "";
		}
		if (fileName.contains(".")) {
			return fileName.substring(fileName.lastIndexOf(".") + 1);
		}
		return "";
	}
	
	/**
	 * 移除文件后缀名
	 * @param file 文件对象
	 * @return	      移除后缀的文件名
	 */
	public static String removeSuffix(File file) {
		return removeSuffix(file.getName());
	}
	
	/**
	 * 移除文件后缀名
	 * @param fileName 文件名
	 * @return		      移除后缀的文件名
	 */
	public static String removeSuffix(String fileName) {
		if (fileName == null) {
			return "";
		}
		if (fileName.contains(".")) {
			return fileName.substring(0, fileName.lastIndexOf("."));
		}
		return "";
	}	
	
	/**
	 * 获取文件当前所在文件路径
	 * @param filePath
	 * @return
	 */
	public static String getFatherFilePath(String filePath) {
		if (filePath == null) {
			return "";
		}
		int pos = filePath.lastIndexOf("/");
		if (pos == -1) {
			return "";
		}
		return filePath.substring(0, pos);
	}

	/**
	 * 根据路径删除指定的目录或者文件, 无论存在与否
	 * @param sPath 要删除的目录或者文件
	 * @return		删除成功返回true, 否则返回false
	 */
	public static boolean deleteFolder(String sPath) {
		boolean flag = false;
		File file = new File(sPath);
		if (!file.exists()) {
			return flag;
		} else {
			if (file.isFile()) {
				return deleteFile(sPath);
			} else {
				return deleteDirectory(sPath);
			}
		}
	}
	
	/**
	 * 获得 指定文件或者指定文件夹下所有文件的路径
	 * @param filePath 文件或者文件夹路径
	 * @param pathList 文件的路径列表
	 */
	public static void getAllFilePath(String filePath, List<String> pathList) {
		if (pathList == null) {
			pathList = new ArrayList<>();
		}
		if (StringUtils.isNullOrEmpty(filePath)){
			return;
		}
		File file = new File(filePath);
		if (file.isDirectory()) {
			File[] childFiles = file.listFiles();
			if (childFiles != null) {
				for (File f : childFiles) {
					if (f.isDirectory()) {
						getAllFilePath(f.getPath(), pathList);
					} else {
						/* 
						 * 关于文件路径分隔符:
						 * Windows 下是"\",  unix|linux 下是"/"
						 * 考虑到程序的可移植性
						 * 对文件路径做统一替换处理
						 * "/"或 File.separator均可
						 */
						filePath = f.getAbsolutePath().replace("\\", "/");
						pathList.add(filePath);
					}
				}
			}
		} else {
			filePath = filePath.replace("\\", "/");
			pathList.add(filePath);
		}
	}
	
	/**
	 * 删除单个文件
	 * @param sPath 要删除的文件名
	 * @return		删除成功返回true, 否则返回false
	 */
	public static boolean deleteFile(String sPath) {
		boolean flag = false;
		File file = new File(sPath);
		if (file.isFile() && file.exists()) {
			file.delete();
			flag = true;
		}
		return flag;
	}
	
	/**
	 * 删除目录(文件夹)以及目录下的文件
	 * @param sPath	要删除目录的文件路径
	 * @return		删除成功返回true, 否则返回false
	 */
	public static boolean deleteDirectory(String sPath){
		 if(!sPath.endsWith(File.separator)){
			 sPath = sPath + File.separator;
		 }
		 
		 File dirFile = new File(sPath);
		 if(!dirFile.exists() || !dirFile.isDirectory()){
			 return false;
		 }
		 boolean flag = true;
		 
		 File[] files = dirFile.listFiles();
		 for(int i = 0; i < files.length ; i++){
			 if(files[i].isFile()){
				 flag = deleteFile(files[i].getAbsolutePath());
				 if(!flag) break;
			 }else {
				 flag =deleteDirectory(files[i].getAbsolutePath());
				 if(!flag) break;
			 }
		 }
		 if(!flag) return false;
		 if(dirFile.delete()){
			 return true;
		 }else {
			 return false;
		 }
	 }
}
