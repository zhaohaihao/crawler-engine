package com.frame.crawler.util;

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
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.CollectionUtils;

import com.frame.crawler.constants.GobalConstant;
import com.frame.crawler.model.NetworkProxyInfo;
import com.frame.crawler.service.NetworkProxyInfoService;

public class WebPageReqTool {
	
	protected Logger logger = LoggerFactory.getLogger(WebPageReqTool.class);
	
	private static WebPageReqTool webPageReqTool;
	
	@Autowired
	protected NetworkProxyInfoService networkProxyInfoService;
	
	// 代理ip列表
	private static List<NetworkProxyInfo> NETWORK_PROXY_LIST = null;
	
	// 用户代理列表
	private static List<String> USER_AGENTS_LIST = null;
	
	private WebPageReqTool()
	{
		init();
	}
	
	public static WebPageReqTool getWebPageReqTool(){
        if (webPageReqTool == null) {
            synchronized (WebPageReqTool.class) {
                if (webPageReqTool == null) {
                	webPageReqTool = new WebPageReqTool();
                }
            }
        }
        return webPageReqTool;
    }
	
	public void init() {
		try {
			// 初始化代理ip列表
			if (CollectionUtils.isEmpty(NETWORK_PROXY_LIST)) {
				NETWORK_PROXY_LIST = networkProxyInfoService.getAllNetworkProxyInfoByIsValid(GobalConstant.ProxyValid.VALID);
			}
			// 初始化用户代理
			if (CollectionUtils.isEmpty(USER_AGENTS_LIST)) {
				USER_AGENTS_LIST = TxtUtils.getContentList("user-agents.txt");
			}
		} catch (Exception e) {
			// TODO: 异常, 中断操作
		}
	}

	public Document getDocByJsonp(String url)
	{
		try {
//			setProxyIp();
			Connection conn = Jsoup.connect(url).header("Accept", "*/*")
					.header("Accept-Encoding", "gzip, deflate, sdch")
					.header("Accept-Language", "zh-CN,zh;q=0.8,en;q=0.6")
					.header("Cache-Control", "max-age=0")
					.header("User-Agent", setUserAgents());
//					.timeout(timeOut);
			Document document = conn.get();
			/*if (document == null || document.toString().trim().equals("")) {// 表示ip被拦截或者其他情况
				logger.info(">>>>>>>>>>>>出现ip被拦截或者其他情况>>>>>>>>>>>>");
				setProxyIp();
				getDocument(url);
			}*/
			
			// 获取信息为空（当前ip被拦截时返回标识1）
			if (document == null || document.toString().trim().equals("")) {// 表示ip被拦截或者其他情况
				logger.info(">>>>>>>>>>>>出现ip被拦截或者其他情况>>>>>>>>>>>>");
				setProxyIp();
				getDocByJsonp(url);
			}// 判断通过jsonp请求时内容无法获取，返回标识2
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
//			setProxyIp();// 换代理ip
			getDocByJsonp(url);// 继续爬取网页
		}
		return getDocByJsonp(url);
	}
	
	public Document getDocByNetUrl(String url) {
		try {
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
			logger.info(">>> 链接读取出现异常 >>>");
		}
		return getDocByJsonp(url);
	}
	
	/**
	 * 设置代理ip
	 * @throws IOException
	 */
	private static void setProxyIp() {
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
	private static String setUserAgents() {
		Random random = new Random();
		int index = random.nextInt(USER_AGENTS_LIST.size());
		String userAgent = USER_AGENTS_LIST.get(index);
		
		System.out.println(">>> 当前用户代理User-Agent为："+ userAgent +" >>>");
		return userAgent;
	}
	
}
