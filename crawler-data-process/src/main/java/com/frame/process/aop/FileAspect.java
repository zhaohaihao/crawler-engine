package com.frame.process.aop;

import java.util.List;

import org.apache.commons.lang3.ArrayUtils;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import com.frame.process.constants.GobalConstant;
import com.frame.process.utils.FileUtils;
import com.frame.process.utils.Zip4jUtils;
import com.frame.process.utils.ZipUtils;

/**
 * 文件处理切面
 * Created by zhh on 2018/04/23.
 */
@Aspect
@Component
public class FileAspect {
	
	// 文件加解密密码
	@Value("${file.secretKey}")
	protected String secretKey;
	
	/**
	 * 加密切入点
	 */
//	@Pointcut("execution(* com.frame.process.process.interfaces.ProcessInter.packFileWithFixedNum(..))")
	public void encrypt() {}
	
	/**
	 * 解密切入点
	 */
	@Pointcut("execution(* com.frame.process.process.interfaces.ProcessInter.filesToDatasConverterThreadOption(..))")
	public void decrypt() {}
	
	/**
	 * 压缩、加密文件(文件处理之后统一压缩、加密)
	 */
//	@AfterReturning(returning = "ret", pointcut = "encrypt()")
	public void encryptFile(List<String> ret) {
		System.out.println("=== 执行了该操作 ===");
	}
	
	/**
	 * 解密、解压文件(文件处理之前统一解密、解压缩)
	 */
	@Around("decrypt()")
	public void decryptFile(ProceedingJoinPoint proceedingJoinPoint) {
		// 获取传入方法的参数值
		Object[] args = proceedingJoinPoint.getArgs();
		if (ArrayUtils.isEmpty(args)) {
			return;
		}
		// 获得到需要解密的文件的路径
		String filePath = String.valueOf(args[0]);
		if (!filePath.contains(GobalConstant.ENCRYPT_FLAG) || !filePath.endsWith(GobalConstant.FileType.ZIP)) {
			// 不是压缩包不执行以下操作
			return;
		}
		// 解密
		String dstPath = filePath.replace(GobalConstant.ENCRYPT_FLAG, "");
		int pos = dstPath.lastIndexOf("/");
		Zip4jUtils.decrypt(filePath, dstPath.substring(0, pos), secretKey);
		FileUtils.deleteFolder(filePath);
		try {
			// 获取解压文件夹路径
			String unCompressFilePath = ZipUtils.unCompress(dstPath);
			args[0] = unCompressFilePath;
			// 传入变换后的参数
			proceedingJoinPoint.proceed(args);
		} catch (Throwable e) {
			e.printStackTrace();
			return;
		}
	}
}
