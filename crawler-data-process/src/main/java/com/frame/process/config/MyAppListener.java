package com.frame.process.config;

import java.util.HashMap;
import java.util.Map;

import org.springframework.context.ApplicationListener;
import org.springframework.context.event.ContextRefreshedEvent;
import org.springframework.stereotype.Component;

import com.frame.process.annotation.ExportService;
import com.frame.process.core.Service;
import com.frame.process.process.interfaces.CommonProcess;

/**
 * 启动时监听服务
 * Created by zhh on 2018/04/21.
 */
@Component
public class MyAppListener implements ApplicationListener<ContextRefreshedEvent> {

	@SuppressWarnings({ "unchecked", "rawtypes" })
	@Override
	public void onApplicationEvent(ContextRefreshedEvent contextRefreshedEvent) {
		// 获取包含 @ExportService 注解的服务类
		Map<String, Object> beansWithAnnotation = contextRefreshedEvent.getApplicationContext().getBeansWithAnnotation(ExportService.class);
	
		Map<String, Service> categoryMap = new HashMap<String, Service>();
		
		beansWithAnnotation.forEach((key, value) -> {
			Class clazz = value.getClass();
			ExportService annotation = (ExportService) clazz.getAnnotation(ExportService.class);
			// 获取服务分类
			String type = annotation.type();
			categoryMap.put(type, (Service) value);
			System.out.println("=== 服务类type: " + type + " , 服务类: " + value + " ===");
		});
		
		// map 传值
		CommonProcess.setCategoryMap(categoryMap);
	}

}
