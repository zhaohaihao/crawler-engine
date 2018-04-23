package com.frame.crawler.config;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

import com.frame.crawler.model.SearchKeyword;
import com.frame.crawler.process.impl.BaiduAnalysisProcess;
import com.frame.crawler.process.impl.BaiduTiebaSearchAnalysisProcess;
import com.frame.crawler.process.impl.So360AnalysisProcess;
import com.frame.crawler.process.impl.SogouAnalysisProcess;
import com.frame.crawler.service.BaiduTiebaImageService;
import com.frame.crawler.service.NetworkProxyInfoService;
import com.frame.crawler.service.SearchKeywordService;
import com.frame.crawler.util.TxtUtils;

/**
 * 
 * Created by zhh on 2018/04/02.
 */
@Component
public class MyApplicationRunner implements CommandLineRunner {

	protected Logger logger = LoggerFactory.getLogger(getClass());
	
	// 搜索关键字信息列表
	private static List<SearchKeyword> SEARCH_KEYWORDS = new ArrayList<>();
	
	@Autowired
	private MyContext myContext;
	
	@Autowired
	private BaiduAnalysisProcess baiduAnalysisProcess;
	
	@Autowired
	private SogouAnalysisProcess sogouAnalysisProcess;
	
	@Autowired
	private So360AnalysisProcess so360AnalysisProcess;
	
	@Autowired
	private BaiduTiebaSearchAnalysisProcess baiduTiebaSearchAnalysisProcess;
	
	@Autowired
	private SearchKeywordService searchKeywordService;
	
	@Autowired
	private NetworkProxyInfoService networkProxyInfoService;
	
	@Autowired
	private BaiduTiebaImageService baiduTiebaImageService;
	
	@Override
	public void run(String... arg0) throws Exception {

		// 代理ip信息入库
//		insertProxyInfoIntoDB("proxypool.txt");
		
		// 执行爬虫
//		executeGrab(true, true, true);
		
//		getKeyWordListFromDB(7);
		
		baiduTiebaImageService.downloadImagesToLocal(baiduTiebaImageService.getNotDownloadImagesByLimit(1000));
		
//		SearchKeyword searchKeyword = new SearchKeyword();
//		searchKeyword.setKeyword("哈哈");
//		SEARCH_KEYWORDS.add(searchKeyword);
//		boolean flag = baiduTiebaSearchAnalysisProcess.threadExecuteGrab(SEARCH_KEYWORDS);
		/*if (flag) {
			// 执行完任务关闭容器
			myContext.showdown();
			logger.info(">>> 爬虫爬取网页内容结束!!! >>>");
		}*/
//		executeGrab(true, true, true);
	}

	/**
	 * 爬虫操作
	 * @param baiduFlag 百度开关
	 * @param sogouFlag 搜狗开关
	 * @param so360Flag 360开关
	 * @throws Exception
	 */
	private void executeGrab(boolean baiduFlag, boolean sogouFlag, boolean so360Flag) throws Exception {
		
		logger.info(">>> 爬虫爬取网页内容开始... >>>");
		
		SearchKeyword searchKeyword = new SearchKeyword();
		searchKeyword.setKeyword("哈哈");
		SEARCH_KEYWORDS.add(searchKeyword);
		
		// 从数据获取满足条件关键字列表
//		getKeyWordListFromDB(7);
		
		/*=============================主要抓取业务逻辑=============================*/
		
		// 提示开关赋默认值
		boolean baiduCompleted = true;
		boolean sogouCompleted = true;
		boolean so360Completed = true;
		
		if (baiduFlag) {
			// 百度爬取
			baiduCompleted = baiduAnalysisProcess.threadExecuteGrab(SEARCH_KEYWORDS);
		}
		
		if (sogouFlag) {
			// 搜狗爬取
			baiduCompleted = sogouAnalysisProcess.threadExecuteGrab(SEARCH_KEYWORDS);
		}
		
		if (so360Flag) {
			// 360爬取
			baiduCompleted = so360AnalysisProcess.threadExecuteGrab(SEARCH_KEYWORDS);
		}

		/*=============================主要抓取业务逻辑=============================*/
		
		if (baiduCompleted && sogouCompleted && so360Completed) {
			// 执行完任务关闭容器
			myContext.showdown();
			logger.info(">>> 爬虫爬取网页内容结束!!! >>>");
		}
	}
	
	/**
	 * 从数据库获取搜索关键字列表
	 * @param timeInterval 时间间隔, 单位: 天
	 * @return
	 */
	private void getKeyWordListFromDB(Integer timeInterval) {
		
		SEARCH_KEYWORDS = searchKeywordService.getMatchConditionKeyWordList(timeInterval);
	}
	
	/**
	 * 代理ip信息读取入库
	 * @param fileNames
	 * @throws IOException
	 */
	private void insertProxyInfoIntoDB(String... fileNames) throws IOException {
		List<String> contentList = TxtUtils.getContentList(fileNames);
		networkProxyInfoService.insertInfoFromTxt(contentList);
	}
}
