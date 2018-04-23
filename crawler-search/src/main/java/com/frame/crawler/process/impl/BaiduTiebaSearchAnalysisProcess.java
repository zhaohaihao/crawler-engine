package com.frame.crawler.process.impl;

import java.net.URLDecoder;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ThreadPoolExecutor;

import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import com.frame.crawler.constants.GobalConstant;
import com.frame.crawler.model.BaiduTiebaSearch;
import com.frame.crawler.model.SearchKeyword;
import com.frame.crawler.process.interfaces.AbstractAnalysisProcess;
import com.frame.crawler.service.BaiduTiebaSearchService;
import com.frame.crawler.util.ThreadPoolUtils;
import com.frame.crawler.util.UUIDUtils;
/**
 * 百度贴吧搜索爬取逻辑
 * Created by zhh on 2018/04/13.
 */
@Service
public class BaiduTiebaSearchAnalysisProcess extends AbstractAnalysisProcess {

	// 页面相关固定值
	private static final Integer FIXED_VALUE = 50;
	
	// 贴吧原始网址
	private static final String ORIGINAL_URL = "https://tieba.baidu.com";
	
	// 获取页面数量的url格式
	private static final String GET_PAGE_NUM_URL = "https://tieba.baidu.com/f?kw=%s";
	
	// 渲染请求抓取数据的url格式
	private static final String FIXED_GRAB_URL = GET_PAGE_NUM_URL + "&pn=%d";
	
	@Autowired
	private BaiduTiebaSearchService baiduTiebaSearchService;
	
	@Autowired
	private BaiduTiebaContentAnalysisProcess baiduTiebaContentAnalysisProcess;
	
	@Override
	public Document getDocument(String url) {
		return super.getDocumentByURL(url);
	}

	@Override
	public boolean threadExecuteGrab(List<SearchKeyword> searchKeywordList) throws Exception {
		
		if (CollectionUtils.isEmpty(searchKeywordList)) {
			// 关键词集合为空, 不执行以下操作
			return true;
		}
		
		/*========================== 线程执行操作 ==============================*/
		// 创建线程池
		ThreadPoolExecutor threadPoolExecutor = ThreadPoolUtils.getThreadPoolInstance();
		
		for (SearchKeyword searchKeyword : searchKeywordList) {
			String keyword = searchKeyword.getKeyword();
			String getPageNumUrl = String.format(GET_PAGE_NUM_URL, keyword);
			// 读取页面数
			Integer pageNum = getPageNum(getPageNumUrl);
			
			switch (pageNum) {
			case 0:
				// 单页数据
				threadPoolExecutor.execute(new GrabSearcherRunnable().setKeyWord(keyword).setPageNum(pageNum).setFixedGrabUrl(FIXED_GRAB_URL).setFixedValue(FIXED_VALUE));
				break;
			default:
				// 无数据忽略 和 读取页面有信息
				for (int i = 0; i < pageNum; i++) {
					threadPoolExecutor.execute(new GrabSearcherRunnable().setKeyWord(keyword).setPageNum(i).setFixedGrabUrl(FIXED_GRAB_URL).setFixedValue(FIXED_VALUE));
				}
				break;
			}
		}
		/*========================== 线程执行操作 ==============================*/
		
		while (true) {
			// 判断所有多线程任务是否完成
			if (threadPoolExecutor.getActiveCount() == 0) {
				return true;
			}
		}
	}

	@Override
	protected void analysisDocument(Document pageDoc) throws Exception {
		
		// 获取请求url, 例：https://tieba.baidu.com/f?kw=nba&pn=50
		String baseUri = pageDoc.baseUri();
		// 参数串
		String paramStr = baseUri.substring(baseUri.indexOf("?") + 1);
		// 参数键值对
		String[] keyValue = paramStr.split("&");
		// 关键字需解码
		String keyWord = URLDecoder.decode(keyValue[0].split("=")[1], "UTF-8");
		Integer pageNum = Integer.valueOf(keyValue[1].split("=")[1]);
		
		// 百度贴吧搜索主体内容
		Element threadList = pageDoc.getElementById("thread_list");
		
		// 获取主体内容各个帖子的信息 (忽略置顶帖子, class: thread_top_list_folder)
		Elements contents = threadList.getElementsByClass(" j_thread_list clearfix");
		
		List<BaiduTiebaSearch> baiduTiebaSearchs = new ArrayList<>();
		BaiduTiebaSearch baiduTiebaSearch;
		
		// 顺序号
		int sort = 0;
		String title = "";
		String titleUrl = "";
		String postUser = "";
		String lastReplyUser = "";
		String lastReplyTime = "";
		
		// 获取贴吧内容的参数条件(帖子链接, uuid)
		Map<String, String> contentParams = new HashMap<>();
		
		for (Element content : contents) {
			
			try {
				// 获取帖子标题和帖子内容的 url
				Element titleElement = null;
				// 解决两种不同类名的处理
				try {
					titleElement = content.getElementsByClass("threadlist_title pull_left j_th_tit ").get(0);
				} catch (Exception e) {
					titleElement = content.getElementsByClass("threadlist_title pull_left j_th_tit \n").get(0);
				}
				Elements titleAs = titleElement.getElementsByTag("a");
				titleUrl = titleAs.attr("href");
				title = titleAs.text();
				// 获取发帖用户
				postUser = content.getElementsByClass("frs-author-name-wrap").text();
				// 获取最后一次回帖用户
				lastReplyUser = content.getElementsByClass("tb_icon_author_rely j_replyer").text();
				// 获取最后一次回帖时间
				lastReplyTime = content.getElementsByClass("threadlist_reply_date pull_right j_reply_data").text();
			} catch (Exception e) {
				// 内容解析发生异常不做处理, 跳过继续执行
				continue;
			}
			
			// 帖子详细内容链接
			String contentUrl = ORIGINAL_URL + titleUrl;
			
			sort++;
			// 存储结果
			baiduTiebaSearch = new BaiduTiebaSearch();
			String uuid = UUIDUtils.getId();
			// 设置存储信息
			baiduTiebaSearch.setReqUrl(URLDecoder.decode(baseUri, "UTF-8"));
			baiduTiebaSearch.setTargetUrl(contentUrl);
			baiduTiebaSearch.setTitle(title);
			baiduTiebaSearch.setPostUser(postUser);
			baiduTiebaSearch.setLastReplyUser(lastReplyUser);
			baiduTiebaSearch.setLastReplyTime(lastReplyTime);
			baiduTiebaSearch.setKeyWord(keyWord);
			baiduTiebaSearch.setPageNum(pageNum / FIXED_VALUE + 1);
			baiduTiebaSearch.setSort(sort);
			baiduTiebaSearch.setImportDate(new Date());
			baiduTiebaSearch.setUuid(uuid);
			baiduTiebaSearch.setFlag(GobalConstant.ReadFlag.NOT_READ);
			
//			baiduTiebaSearchService.insert(baiduTiebaSearch);
			baiduTiebaSearchs.add(baiduTiebaSearch);
			logger.info(">>> 存储baiduTiebaSearch信息: {} >>>", baiduTiebaSearch);
			
			// 存储符合的条件
			contentParams.put(contentUrl, uuid);
		}
		
		// 存储信息
		if (baiduTiebaSearchs.size() != 0) {
			baiduTiebaSearchService.save(baiduTiebaSearchs);
		}
		
		// 请求处理帖子详情内容
		baiduTiebaContentAnalysisProcess.threadExecuteGrab(contentParams);
	}

	@Override
	protected int getAnalysisPageNum(Document pageDoc) throws Exception {
		
		/*
		 * 注：贴吧搜索会产生以下两种页面搜索结果
		 * 	1. https://tieba.baidu.com/f?kw=nba&ie=utf-8
		 *	2. http://tieba.baidu.com/f/search/res?qw=18868568888 (这种情况暂时不处理, 忽略)
		 */
		int pageNum = -1;
		
		// 获取页面信息 div 层
		Element pageElement = pageDoc.getElementById("frs_list_pager");
		if (pageElement == null) {
			return pageNum;
		}
		
		Elements elements = pageElement.children();
		int size = elements.size();
		
		if (size == 0) {
			pageNum = 0;
		} else {
			// 至少两页数据项, 获取页面 div 层内子元素数量(包括了“下一页”、“尾页”, 需不计算在内) 
			pageNum = size - 2;
		}
		
		logger.info(">>> 所请求页面的页面数pageNum为: {} >>>", pageNum);
		return pageNum;
	}
}
