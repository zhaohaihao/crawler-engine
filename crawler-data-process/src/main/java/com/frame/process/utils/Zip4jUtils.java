package com.frame.process.utils;

import java.io.File;

import net.lingala.zip4j.core.ZipFile;
import net.lingala.zip4j.exception.ZipException;
import net.lingala.zip4j.model.ZipParameters;
import net.lingala.zip4j.util.Zip4jConstants;

/**
 * zip文件加解密
 * Created by zhh on 2018/04/24.
 */
public class Zip4jUtils {
	
	private Zip4jUtils() {}

    /**
     * 加密
     * 支持将某个文件或某个目录下所有的文件加密.
     * 1.某个文件:D:\\test\\src.zip.
     * 2.某个目录:D:\\test\\src
     * @return boolean
     */
	public static boolean encrypt(String srcPath, String dstPath, String secretKey) {
		try {
			if (!new File(srcPath).exists()) {
				System.out.println("源路径不存在 " + srcPath);
				return false;
			}
			ZipParameters parameters = new ZipParameters();
			parameters.setEncryptFiles(true);
			parameters.setEncryptionMethod(Zip4jConstants.ENC_METHOD_AES);
			parameters.setAesKeyStrength(Zip4jConstants.AES_STRENGTH_256);
			parameters.setPassword(secretKey.toCharArray());
			File srcFile = new File(srcPath);
			ZipFile destFile = new ZipFile(dstPath);
			if (srcFile.isDirectory()) {
				destFile.addFolder(srcFile, parameters);
			} else {
				destFile.addFile(srcFile, parameters);
			}
			System.out.println("成功加密文件！");
			return true;
		} catch (Exception e) {
			System.out.println("加密文件发生异常:" + e);
			return false;
		}
	}

    /**
     * 解密
     * 支持将某个加密文件解压缩到某个指定目录下面.
     * @return boolean
     */
	public static boolean decrypt(String srcPath, String dstPath, String secretKey) {
		try {
			if (!new File(srcPath).exists()) {
				System.out.println("源路径不存在 " + srcPath);
				return false;
			}
			ZipFile srcFile = new ZipFile(srcPath);
			srcFile.setFileNameCharset("GBK");
			srcFile.setPassword(secretKey.toCharArray());
			srcFile.extractAll(dstPath);
			System.out.println("成功解密文件！");
			return true;
		} catch (ZipException e) {
			System.out.println("解密文件发生异常:" + e);
			return false;
		}
	}
}
