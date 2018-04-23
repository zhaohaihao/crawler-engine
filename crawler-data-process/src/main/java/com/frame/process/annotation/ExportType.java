package com.frame.process.annotation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * 用于文件导入时类型判断
 * Created by zhh on 2018/04/20.
 */
@Target({ElementType.TYPE})
@Retention(RetentionPolicy.RUNTIME)
public @interface ExportType {
	
	/**
	 * Excel表别名
	 * @return
	 */
	String value() default "";
}
