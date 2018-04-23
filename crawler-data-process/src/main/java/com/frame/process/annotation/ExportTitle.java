package com.frame.process.annotation;

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * 用于导出文件标明列明
 * Created by zhh on 2018/04/19.
 */
@Target({ElementType.FIELD})
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface ExportTitle {
	
	/**
	 * 导出Excel表头名
	 * @return
	 */
	String value() default "";
}
