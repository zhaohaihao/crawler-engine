package com.frame.process;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.web.servlet.ServletComponentScan;

import com.frame.process.core.MyMapper;

import tk.mybatis.spring.annotation.MapperScan;

/**
 * 启动项
 * Created by zhh on 2018/04/19.
 */
@MapperScan(basePackages = {"com.frame.process.mapper"}, markerInterface = MyMapper.class)
@ServletComponentScan
@SpringBootApplication
public class CrawlerDataProcessApp {
	
	public static void main(String[] args) {
		
		SpringApplication.run(CrawlerDataProcessApp.class, args);
	}
}
