package com.frame.process.aop;

import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.After;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.aspectj.lang.annotation.Pointcut;
import org.springframework.stereotype.Component;

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
	@Pointcut("execution(* com.frame.process.process.interfaces.ProcessInter.filesToDatasConverter(..))")
	public void decrypt() {}
	
	/**
	 * 加密文件(文件处理之后统一压缩、加密)
	 */
	@After("encrypt()")
	public void encryptFile(JoinPoint joinPoint) {
		
	}
	
	/**
	 * 解密文件(文件处理之前统一解密、解压缩)
	 */
	@Before("decrypt()")
	public void decryptFile(JoinPoint joinPoint) {
		
	}
}
