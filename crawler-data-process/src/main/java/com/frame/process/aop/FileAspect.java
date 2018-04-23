package com.frame.process.aop;

import org.apache.commons.lang3.ArrayUtils;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.AfterReturning;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.springframework.stereotype.Component;

import com.frame.process.constants.GobalConstant;
import com.frame.process.utils.ZipUtils;

/**
 * 文件处理切面
 * Created by zhh on 2018/04/23.
 */
@Aspect
@Component
public class FileAspect {
	
	/**
	 * 加密切入点
	 */
	@Pointcut("execution(* com.frame.process.process.interfaces.ProcessInter.datasToFilesConverter(..))")
	public void encrypt() {}
	
	/**
	 * 解密切入点
	 */
	@Pointcut("execution(* com.frame.process.process.interfaces.ProcessInter.filesToDatasConverterThreadOption(..))")
	public void decrypt() {}
	
	/**
	 * 压缩、加密文件(文件处理之后统一压缩、加密)
	 */
	@AfterReturning(returning = "ret", pointcut = "encrypt()")
	public void encryptFile(String ret) {
		System.out.println("hah" + ret);
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
		if (!filePath.endsWith(GobalConstant.FileType.ZIP)) {
			// 不是压缩包不执行以下操作
			return;
		}
		
		// TODO: 压缩包解密
		
		try {
			// 获取解压文件夹路径
			String unCompressFilePath = ZipUtils.unCompress(filePath);
			args[0] = unCompressFilePath;
			// 传入变换后的参数
			proceedingJoinPoint.proceed(args);
		} catch (Throwable e) {
			e.printStackTrace();
			return;
		}
	}
}
