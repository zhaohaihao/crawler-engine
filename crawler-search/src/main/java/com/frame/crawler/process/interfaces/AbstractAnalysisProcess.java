package com.frame.crawler.process.interfaces;

import javax.annotation.PostConstruct;

import org.jsoup.nodes.Document;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.CollectionUtils;

import com.frame.crawler.constants.GobalConstant;
import com.frame.crawler.service.NetworkProxyInfoService;
import com.frame.crawler.service.WebPageService;
import com.frame.crawler.util.TxtUtils;

public abstract class AbstractAnalysisProcess extends RequestAnalysisProcess implements AnalysisInter {
	
	@Autowired
	protected WebPageService webPageService;
	
	@Autowired
	protected NetworkProxyInfoService networkProxyInfoService;
	
	@PostConstruct
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
	
	/**
	 * 通过地址得到document对象
	 * @param url
	 */
	public Document getDocument(String url) {
		return super.getDocumentByJsoup(url);
	}
	
	/**
	 * 根据自己的业务需求解析网页内容
	 * @param pageDoc
	 */
	protected abstract void analysisDocument(Document pageDoc) throws Exception;

	/**
	 * 根据解析网页获取页数(最大获取10页)
	 * @param pageDoc
	 * @return
	 * @throws Exception
	 */
	protected abstract int getAnalysisPageNum(Document pageDoc) throws Exception;
	
	@Override
	public void executeGrab(String url) throws Exception {
		Document pageDoc = getDocument(url);
		this.analysisDocument(pageDoc);
	}

	@Override
	public int getPageNum(String url) throws Exception {
		Document pageDoc = getDocument(url);
		return this.getAnalysisPageNum(pageDoc);
	}
	
	// 任务线程
	public class GrabSearcherRunnable implements Runnable {
		
		private String keyWord;
		
		private Integer pageNum;
		
		private Integer fixedValue;
		
		private String fixedGrabUrl;
		
		public String getKeyWord() {
			return keyWord;
		}

		public GrabSearcherRunnable setKeyWord(String keyWord) {
			this.keyWord = keyWord;
			return this;
		}

		public Integer getPageNum() {
			return pageNum;
		}

		public GrabSearcherRunnable setPageNum(Integer pageNum) {
			this.pageNum = pageNum;
			return this;
		}
		
		public Integer getFixedValue() {
			return fixedValue;
		}

		public GrabSearcherRunnable setFixedValue(Integer fixedValue) {
			this.fixedValue = fixedValue;
			return this;
		}

		public String getFixedGrabUrl() {
			return fixedGrabUrl;
		}

		public GrabSearcherRunnable setFixedGrabUrl(String fixedGrabUrl) {
			this.fixedGrabUrl = fixedGrabUrl;
			return this;
		}

		@Override
		public void run() {
			// 请求完整 url
			String reqUrl = String.format(fixedGrabUrl, keyWord, pageNum * fixedValue);
			System.out.println(">>> " + reqUrl + " >>>");
			try {
				// 抓取数据
				executeGrab(reqUrl);
			} catch (Exception e) {
				logger.debug(">>> 当前线程出现异常, 请求url地址为: {} >>>", reqUrl);
				e.printStackTrace();
			}
		}
	}
}
