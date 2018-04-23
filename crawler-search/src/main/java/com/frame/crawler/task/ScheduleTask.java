package com.frame.crawler.task;

import java.util.ArrayList;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;

import com.frame.crawler.model.SearchKeyword;
import com.frame.crawler.process.impl.BaiduAnalysisProcess;
import com.frame.crawler.process.impl.BaiduTiebaSearchAnalysisProcess;
import com.frame.crawler.process.impl.So360AnalysisProcess;
import com.frame.crawler.process.impl.SogouAnalysisProcess;
import com.frame.crawler.process.interfaces.AnalysisInter;
import com.frame.crawler.service.BaiduTiebaImageService;
import com.frame.crawler.service.NetworkProxyInfoService;
import com.frame.crawler.service.SearchKeywordService;

/**
 * 定时任务
 * Created by zhh on 2018/04/18.
 */
//@Configuration
//@EnableScheduling
public class ScheduleTask {
	
	private Logger logger = LoggerFactory.getLogger(getClass());
	
	// 搜索关键字信息列表
	private static List<SearchKeyword> SEARCH_KEYWORDS = new ArrayList<>();
	
	@Autowired
	private AnalysisInter baiduAnalysisProcess ;
	
	@Autowired
	private SogouAnalysisProcess sogouAnalysisProcess;
	
	@Autowired
	private So360AnalysisProcess so360AnalysisProcess;
	
	@Autowired
	private SearchKeywordService searchKeywordService;
	
	@Autowired
	private NetworkProxyInfoService networkProxyInfoService;
	
	@Autowired
	private BaiduTiebaSearchAnalysisProcess baiduTiebaSearchAnalysisProcess;
	
	@Autowired
	private BaiduTiebaImageService baiduTiebaImageService;
	
	/**
	 * 图片下载定时任务
	 */
//	@Scheduled()
	public void downloadImages() {
		
	}
}
