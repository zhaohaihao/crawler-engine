package com.frame.crawler.process.impl;

import java.net.URLDecoder;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.concurrent.ThreadPoolExecutor;

import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import com.frame.crawler.constants.GobalConstant;
import com.frame.crawler.model.SearchKeyword;
import com.frame.crawler.model.WebPage;
import com.frame.crawler.process.interfaces.AbstractAnalysisProcess;
import com.frame.crawler.util.ThreadPoolUtils;
import com.frame.crawler.util.UUIDUtils;

/**
 * 360搜索爬取逻辑
 * Created by zhh on 2018/04/10.
 */
@Service
public class So360AnalysisProcess extends AbstractAnalysisProcess {

	// 页面相关固定值
	private static final Integer FIXED_VALUE = 1;
	
	// 获取页面数量的url格式
	private static final String GET_PAGE_NUM_URL = "https://www.so.com/s?q=%s";
	
	// 渲染请求抓取数据的url格式
	private static final String FIXED_GRAB_URL = GET_PAGE_NUM_URL + "&pn=%d";

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
			
			for (int i = 1; i <= pageNum; i++) {
				threadPoolExecutor.execute(new GrabSearcherRunnable().setKeyWord(keyword).setPageNum(i).setFixedGrabUrl(FIXED_GRAB_URL).setFixedValue(FIXED_VALUE));
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
		
		// 获取请求url, 例：https://www.sogou.com/web?query=15022235545&page=1
		String baseUri = pageDoc.baseUri();
		// 参数串
		String paramStr = baseUri.substring(baseUri.indexOf("?") + 1);
		// 参数键值对
		String[] keyValue = paramStr.split("&");
		// 关键字需解码
		String keyWord = URLDecoder.decode(keyValue[0].split("=")[1], "UTF-8");
		Integer pageNum = Integer.valueOf(keyValue[1].split("=")[1]);
		
		// 360搜索主体内容
		Element result = pageDoc.getElementsByClass("result").get(0);
		
		// 获取主体内容各层搜索信息的 div 容器(以此作为一级元素)
		Elements containers = result.children();
		
		List<WebPage> webPages = new ArrayList<>();
		WebPage webpage;
		// 顺序号
		int sort = 0;
		String title = "";
		String targetUrl = "";
		String summary = "";
		for (Element container : containers) {
			
			// 获取标题、内容
			Elements elements = container.getElementsByClass("res-title");
			if (elements.size() == 0) {
				// 非正常内容, 继续
				continue;
			}
			
			try {
				title = elements.get(0).text();
				targetUrl = elements.get(0).getElementsByTag("a").attr("href");
				summary = elements.get(0).nextElementSibling().text();
			} catch (Exception e) {
				// 内容解析发生异常不做处理, 跳过继续执行
				continue;
			}
			
			sort++;
			// 存储结果
			webpage = new WebPage();
//			String content = HtmlParser.getHTMLContent(targetUrl);
			// 设置存储信息
			webpage.setTitle(title);
			webpage.setReqUrl(URLDecoder.decode(baseUri, "UTF-8"));
			webpage.setTargetUrl(targetUrl);
			webpage.setSummary(summary);
			// 粗略解析二级链接正文内容, 默认赋" "(一个char), 防止在text类型中保存null和""产生异常
//			webpage.setContent(StringUtils.isEmpty(content) ? " " : content);
			webpage.setContent(" ");
			webpage.setSrcType(GobalConstant.CrawleSrcType.SO_360);
			webpage.setKeyWord(keyWord);
			webpage.setPageNum(pageNum);
			webpage.setSort(sort);
			webpage.setImportDate(new Date());
			webpage.setUuid(UUIDUtils.getId());
			webpage.setFlag(GobalConstant.ReadFlag.NOT_READ);
			
			webPages.add(webpage);
			logger.info(">>> 存储webpage信息: {} >>>", webpage);
		}
		
		// 存储信息
		webPageService.save(webPages);
	}

	@Override
	protected int getAnalysisPageNum(Document pageDoc) throws Exception {
		
		int pageNum = -1;
		
		// 获取页面信息 div 层
		Element pageElement = pageDoc.getElementById("page");
		if (pageElement == null) {
			return pageNum;
		}
		Elements elements = pageElement.children();
		int size = elements.size();
		
		if (size == 0) {
			pageNum = 0;
		} else {
			// 至少两页数据项, 获取页面 div 层内子元素数量(包括了“下一页”, 需不计算在内) 
			pageNum = size - 1;
			// 查看 信息框：找到相关结果约100,000,000个，是否存在, 存在则在页面计数中减1
			Elements numsElements = pageElement.getElementsByClass("nums");
			if (numsElements.size() != 0) {
				pageNum = pageNum - 1;
			}
		}
		
		logger.info(">>> 所请求页面的页面数pageNum为: {} >>>", pageNum);
		return pageNum;
	}
}
