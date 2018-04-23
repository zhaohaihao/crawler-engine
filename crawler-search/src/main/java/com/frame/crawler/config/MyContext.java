package com.frame.crawler.config;

import org.springframework.beans.BeansException;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.stereotype.Component;

/**
 * 
 * Created by zhh on 2018/04/10.
 */
@Component
public class MyContext implements ApplicationContextAware {
	
	private ConfigurableApplicationContext context;

	@Override
	public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
		
		if (applicationContext instanceof ConfigurableApplicationContext) {
            this.context =  (ConfigurableApplicationContext) applicationContext;
        }
	}
	
	/**
	 * 关闭 spring 容器
	 */
    public void showdown(){
    	
        if (null != context){
            context.close();
        }
    }

}
