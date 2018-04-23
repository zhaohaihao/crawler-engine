package com.frame.crawler;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.web.servlet.ServletComponentScan;

import com.frame.crawler.core.MyMapper;

import tk.mybatis.spring.annotation.MapperScan;

/**
 * 启动项
 * Created by zhh on 2018/03/30.
 */
@MapperScan(basePackages = {"com.frame.crawler.mapper"}, markerInterface = MyMapper.class)
@ServletComponentScan	// 为了能让 spring 能够扫描到自己编写的 servlet 和 filter
@SpringBootApplication
public class CrawlerSearchApp {
	
	public static void main(String[] args) {
		
		SpringApplication.run(CrawlerSearchApp.class, args);
	}
}
