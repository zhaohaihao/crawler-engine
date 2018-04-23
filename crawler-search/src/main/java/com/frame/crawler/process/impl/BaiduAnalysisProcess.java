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
 * 百度搜索爬取逻辑
 * Created by zhh on 2018/04/10.
 */
@Service
public class BaiduAnalysisProcess extends AbstractAnalysisProcess {
	
	// 页面相关固定值
	private static final Integer FIXED_VALUE = 10;
	
	// 获取页面数量的url格式
	private static final String GET_PAGE_NUM_URL = "https://www.baidu.com/s?wd=%s";
	
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
		
//		System.out.println(pageDoc.text());
		
		// 获取请求url, 例：https://www.baidu.com/s?wd=15022235545&pn=10
		String baseUri = pageDoc.baseUri();
		// 参数串
		String paramStr = baseUri.substring(baseUri.indexOf("?") + 1);
		// 参数键值对
		String[] keyValue = paramStr.split("&");
		// 关键字需解码
		String keyWord = URLDecoder.decode(keyValue[0].split("=")[1], "UTF-8");
		Integer pageNum = Integer.valueOf(keyValue[1].split("=")[1]);
		
		// 百度搜索主体内容
		Element contentLeft = pageDoc.getElementById("content_left");
		
		// 获取主体内容各层搜索信息div容器 (以此作为一级元素)
		Elements containers = contentLeft.getElementsByClass("c-container");
		
		List<WebPage> webPages = new ArrayList<>();
		WebPage webpage;
		// 顺序号
		int sort = 0;
		String title = "";
		String targetUrl = "";
		String summary = "";
		for (Element container : containers) {
			
			// 子节点元素(只包含二级元素)
			Elements childElements = container.children();
			int size = childElements.size();
			
			try {
				// TODO: 后期根据不同的解析规则添加, 目前暂时这么处理
				if ("se_st_single_video_zhanzhang".equals(container.attr("tpl"))) {
					// 爱奇艺部分div单独处理
					for (int i = 2; i < size; i++) {
						Element childElement = childElements.get(i);
						switch (i) {
						case 2: // 对应百度搜索每个结果div层中 h3 标签元素
							title = childElement.text();
							targetUrl = childElement.getElementsByTag("a").attr("href");
							break;
						case 3: // 对应百度搜索每个结果div层中摘要内容div
							summary = childElement.text();
							break;
						default:
							// TODO: 数据二级元素结构异常! (异常处理)
							break;
						}
					}
				} else {
					// 其余默认格式部分div处理, tpl: se_com_default
					for (int i = 0; i < size; i++) {
						Element childElement = childElements.get(i);
						switch (i) {
						case 0: // 对应百度搜索每个结果div层中 h3 标签元素
							title = childElement.text();
							targetUrl = childElement.getElementsByTag("a").attr("href");
							break;
						case 1: // 对应百度搜索每个结果div层中摘要内容div
							summary = childElement.text();
							break;
						case 2: // 对应百度搜索每个结果div层中信息脚div
							break;
						default:
							// TODO: 数据二级元素结构异常! (异常处理)
							break;
						}
					}
				}
			} catch (Exception e) {
				// 内容解析发生异常不做处理, 跳过继续执行
				continue;
			}
			
			sort++;
			// 存储结果
			webpage = new WebPage();
//			String content = HtmlParser.getHTMLContent(url);
			// 设置存储信息
			webpage.setTitle(title);
			webpage.setReqUrl(URLDecoder.decode(baseUri, "UTF-8"));
			webpage.setTargetUrl(targetUrl);
			webpage.setSummary(summary);
			// 粗略解析二级链接正文内容, 默认赋" "(一个char), 防止在text类型中保存null和""产生异常
//			webpage.setContent(StringUtils.isEmpty(content) ? " " : content);
			webpage.setContent(" ");
			webpage.setSrcType(GobalConstant.CrawleSrcType.BAI_DU);
			webpage.setKeyWord(keyWord);
			webpage.setPageNum(pageNum / FIXED_VALUE + 1);
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
			// 以 百度搜索 关键词 "@Jacky-鴿咯咯咯赵海豪" 为例, 无数据
			return pageNum;
		}
		Elements elements = pageElement.children();
		int size = elements.size();
		
		if (size == 0) {
			// 以 百度搜索 关键词 "@Jacky-鴿咯赵海" 为例, 只有一页数据项
			pageNum = 0;
		} else {
			// 至少两页数据项, 获取页面 div 层内子元素数量(包括了“下一页”, 需不计算在内) 
			pageNum = size - 1;
		}
		
		logger.info(">>> 所请求页面的页面数pageNum为: {} >>>", pageNum);
		return pageNum;
	}
}
