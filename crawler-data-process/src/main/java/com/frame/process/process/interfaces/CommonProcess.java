package com.frame.process.process.interfaces;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import javax.annotation.PostConstruct;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.util.Assert;

import com.frame.process.annotation.ExportType;
import com.frame.process.core.Service;
import com.frame.process.utils.PackageUtils;
import com.frame.process.utils.UUIDUtils;
/**
 * 公共核心处理类
 * Created by zhh on 2018/04/20.
 */
@Component
public class CommonProcess {
	
	public Logger logger = LoggerFactory.getLogger(getClass());
	
	/** 换行符 **/
	protected final static String LINE_BREAK = "\n";
	
	/** 下划线 **/
	protected final static String UNDER_LINE = "_";
	
	// 要获取的实体类的包
	@Value("${commonProcess.modelPackage}")
	private String modelPackage;
	
	// 文件打包存放、解析读取的位置
	@Value("${commonProcess.fileLocation}")
	protected String fileLocation;
	
	// 初始类别表 <导出Excel表别名, 实体类>
	protected static Map<String, Class<?>> initialCategoriesMap = new HashMap<>();
	
	// 初始服务表 <导出Excel表别名, Service类>
	protected static Map<String, Service> categoryMap = new HashMap<>();
	
	public static Map<String, Service> getCategoryMap() {
		return categoryMap;
	}

	public static void setCategoryMap(Map<String, Service> categoryMap) {
		CommonProcess.categoryMap = categoryMap;
	}

	/**
	 * 获取初始类别表列表
	 * @return
	 */
	public static List<String> getInitialCategoriesKeys() {
		return initialCategoriesMap.keySet().stream().collect(Collectors.toList());
	}
	
	/**
	 * 获取文件名(命名规则: {tableType}_UUID.{保存类型})
	 * @param tableType 表类别
	 * @return
	 */
	public static String getFileName(String tableType) {
		return (tableType + UNDER_LINE + UUIDUtils.getIdWithNoBars());
	}
	
	/**
	 * 获取表类型
	 * @param fileName 文件名(例: ../test/TieBaContent_83af473342a0475ab5f98435b62faa35.xlsx)
	 * @return
	 */
	public static String getTableType(String filePath) {
		Assert.notNull(filePath, "文件路径异常!");
		String tableType = filePath;
		int pos = tableType.lastIndexOf("/");
		if (pos != -1) {
			tableType = tableType.substring(pos + 1);
		}
		pos = tableType.indexOf(UNDER_LINE);
		if (pos != -1) {
			tableType = tableType.substring(0, pos);
		}
		return tableType;
	}
	
	// 初始化执行
	@PostConstruct
	public void init() {
		/*
		 *  实体 POJO 未注入到 Spring 中, 采用这种方式;
		 *  Service类 可参考 com.frame.process.config.MyAppListener;
		 */
		if (initialCategoriesMap.isEmpty()) {
			try {
				// 获取指定包下的实体类
				List<Class<?>> clazzList = PackageUtils.getClass(modelPackage, true);
				for (Class<?> clazz : clazzList) {
					// 类上是否含指定注解
					boolean hasAnno = clazz.isAnnotationPresent(ExportType.class);
					if (hasAnno) {
						// 获取类上的注解
						ExportType annotation = clazz.getAnnotation(ExportType.class);
						// 获取注解当中所需要的属性
						String value = annotation.value();
						System.out.println("=== 初始化获取 POJO类 ：" + clazz + ", 所包含注解 @ExportType 中的值为：" + value + " ===");
						initialCategoriesMap.put(value, clazz);
					}
				}
			} catch (Exception e) {
				logger.info("=== 列表初始化异常, 请检查相关配置! ===");
				e.printStackTrace();
			}
		}
	}
	
	
}
