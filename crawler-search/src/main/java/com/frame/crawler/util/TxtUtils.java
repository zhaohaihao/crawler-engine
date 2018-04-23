package com.frame.crawler.util;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;

/**
 * 操作txt文件工具类
 * Created by zhh on 2018/04/11.
 */
public class TxtUtils {
	
	// 私有构造
	private TxtUtils() {}
	
	/**
	 * 从文件读取关键字列表, </br>
	 * 		文本格式：</br>
	 * 			词语1 </br>
	 * 			词语2 </br>
	 * 			词语3 </br
	 * 参照资源文件夹下 keyword.txt(关键词文件)、proxypool.txt(代理ip文件)、user-agents.txt(用户代理文件)
	 * @return
	 * @throws IOException 
	 */
	public static List<String> getContentList(String... fileNames) throws IOException {
		
		List<String> contents = new ArrayList<>();
		
		if (fileNames == null || fileNames.length == 0) {
			// TODO: 异常处理(没有待读取的文件)
			return contents;
		}
		
		// 动态加载配置文件(可多个)
		ClassLoader classLoader = Thread.currentThread().getContextClassLoader();
		for (String fileName : fileNames) {
			try(InputStream inputStream = classLoader.getResourceAsStream(fileName)) {
				try(BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(inputStream, "UTF-8"))) {
					String line = null;
					while ((line = bufferedReader.readLine()) != null){  
						contents.add(line.trim());
					}
				}
			} 
		}
		return contents;
	}
}
