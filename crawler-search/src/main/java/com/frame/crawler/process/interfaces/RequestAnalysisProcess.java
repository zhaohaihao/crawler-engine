package com.frame.crawler.process.interfaces;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.URL;
import java.util.List;
import java.util.Random;

import org.jsoup.Connection;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.frame.crawler.model.NetworkProxyInfo;

/**
 * 请求处理类
 * Created by zhh on 2018/04/17.
 */
public class RequestAnalysisProcess {
	
	public Logger logger = LoggerFactory.getLogger(getClass());
	
	// 代理ip列表
	public static List<NetworkProxyInfo> NETWORK_PROXY_LIST = null;
	
	// 用户代理列表
	public static List<String> USER_AGENTS_LIST = null;
	
//	private int timeOut = 700;
	
	/*===================================请求页面方式处理区域===================================*/
	
	/**
	 * 通过Jsoup的方式得到地址对应document对象
	 * @param url 请求地址
	 */
	public Document getDocumentByJsoup(String url) {
		try {
//			setProxyIp();
			Connection conn = Jsoup.connect(url).header("Accept", "*/*")
					.header("Accept-Encoding", "gzip, deflate, sdch")
					.header("Accept-Language", "zh-CN,zh;q=0.8,en;q=0.6")
					.header("Cache-Control", "max-age=0")
					.header("User-Agent", setUserAgents());
//					.timeout(timeOut);
			Document document = conn.get();
			// TODO: 先顶替ip代理设置, 暂停2秒, 防止爬取过快ip被封
			try {
				Thread.sleep(2000);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
			
			return document;
		} catch (IOException e) { // 链接超时等其他情况
			e.printStackTrace();
			logger.info(">>>>>>>>>>>>>>出现链接超时等其他情况>>>>>>>>>>>>>");
			getDocumentByJsoup(url);// 继续爬取网页
		}
		return getDocumentByJsoup(url);
		
	}
	
	/**
	 * 通过URL的方式得到地址对应document对象(解决Jsoup无法处理的动态页面)
	 * @param url 请求地址
	 * @return
	 */
	public Document getDocumentByURL(String url) {
		try {
			setProxyIp();
			URL urlObj = new URL(url);
			BufferedReader buff = new BufferedReader(new InputStreamReader(urlObj.openStream()));
			StringBuilder sb = new StringBuilder();
			String s = null;
			while ((s = buff.readLine()) != null) {
				sb.append(s + "\n");
			}
			String html = sb.toString();
			Document document = Jsoup.parse(html);
			// 传递url的值, 后面解析需要用到
			document.setBaseUri(url);
			return document;
		} catch (Exception e) {
			e.printStackTrace();
			logger.info(">>> 链接读取出现异常 >>>");
		}
		return getDocumentByURL(url);
	}
	
	// TODO: 后面有其他的请求处理方式, 在这里添加
	
	/*===================================请求页面方式处理区域===================================*/
	
	
	/**
	 * 设置代理ip
	 * @throws IOException
	 */
	private void setProxyIp() {
		String ip = "";
		try {
//			System.out.println("datalist=========" + datalist);
			Random random = new Random();
			int randomInt = random.nextInt(NETWORK_PROXY_LIST.size());
			NetworkProxyInfo proxyIpInfo = NETWORK_PROXY_LIST.get(randomInt);
			String proxyIp = proxyIpInfo.getProxyIp();
			String proxyPort = proxyIpInfo.getProxyPort();
			ip = proxyIp + ":" + proxyPort;
			System.setProperty("http.maxRedirects", "50");
			System.getProperties().setProperty("proxySet", "true");
			System.getProperties().setProperty("http.proxyHost", proxyIp);
			System.getProperties().setProperty("http.proxyPort", proxyPort);

			System.out.println("设置代理ip为：" + proxyIp + "端口号为：" + proxyPort);
		} catch (Exception e) {
			System.out.println("重新设置代理ip失败:" + ip);
			setProxyIp();
		}
	}
	
	/**
	 * 设置用户代理
	 * @return
	 */
	private String setUserAgents() {
		Random random = new Random();
		int index = random.nextInt(USER_AGENTS_LIST.size());
		String userAgent = USER_AGENTS_LIST.get(index);
		
		System.out.println(">>> 当前用户代理User-Agent为："+ userAgent +" >>>");
		return userAgent;
	}
}
