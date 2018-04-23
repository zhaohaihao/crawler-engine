package com.frame.crawler.util;

import java.util.UUID;

/**
 * UUID相关工具类
 * Created by zhh on 2018/04/23.
 */
public class UUIDUtils {
	
	private UUIDUtils() {}
	
	/**
	 * 获取uuid (eg: 90225dac-9687-4f3c-b54c-d247872a76be)
	 * @return
	 */
	public static String getId() {
		return UUID.randomUUID().toString();
	}
	
	/**
	 * 获取uuid (eg: 90225dac96874f3cb54cd247872a76be)
	 * @return
	 */
	public static String getIdWithNoBars() {
		return getId().replace("-", "");
	}
}
