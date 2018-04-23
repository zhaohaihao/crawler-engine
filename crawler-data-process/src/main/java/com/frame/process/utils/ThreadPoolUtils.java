package com.frame.process.utils;

import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

/**
 * 线程池工具
 * Created by zhh on 2018/04/23.
 */
public class ThreadPoolUtils {
	
	private ThreadPoolUtils() {
		
	}
	
	// 任务执行线程池
	private static ThreadPoolExecutor THREAD_POOL_EXECUTOR;
	
	/**
	 * 单例获取线程池(通用)
	 * @return
	 */
	public static ThreadPoolExecutor getThreadPoolInstance() {
		if (THREAD_POOL_EXECUTOR == null) {
			synchronized (ThreadPoolUtils.class) {
				if (THREAD_POOL_EXECUTOR == null) {
					// 线程核心池大小
					int corePoolSize = 10;
					// 线程池最大线程数
					int maximumPoolSize = 20;
					// 空闲线程生存时间
					long keepAliveTime = 30L;
					// 创建线程池
					THREAD_POOL_EXECUTOR = new ThreadPoolExecutor(corePoolSize, maximumPoolSize, keepAliveTime, TimeUnit.SECONDS, new LinkedBlockingQueue<>());
					// 允许核心线程超时退出
					THREAD_POOL_EXECUTOR.allowCoreThreadTimeOut(true);
				}
			}
		}
		return THREAD_POOL_EXECUTOR;
	}
}
