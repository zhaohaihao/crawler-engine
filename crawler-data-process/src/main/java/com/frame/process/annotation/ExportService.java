package com.frame.process.annotation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * 导出服务
 * Created by zhh on 2018/04/21.
 */
@Target({ElementType.TYPE})
@Retention(RetentionPolicy.RUNTIME)
public @interface ExportService {
	
	/**
	 * Excel表类别
	 * @return
	 */
	String type() default "";
	
	/**
	 * 服务注入 spring 的 beanId
	 * @return
	 */
	String beanId() default "";
}
