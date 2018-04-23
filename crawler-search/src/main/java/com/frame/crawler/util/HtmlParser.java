package com.frame.crawler.util;
/**
 * 搜索结果链接页面解析
 * Created by zhh on 2018/03/30.
 */

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.URL;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.util.StringUtils;

public class HtmlParser {
	
	private static final Logger logger = LoggerFactory.getLogger(HtmlParser.class);
	//
	private static List<String> LINES = new ArrayList<>();
	//
	private final static int BLOCKS_WIDTH = 3;
	// 网页HTML字符串
	private static String HTML = "";
	// 是否为主题类型标志
	private static boolean FLAG = false;
	//
	private static ArrayList<Integer> INDEX_DISTRIBUTION = new ArrayList<>();
	// 
	private static StringBuilder TEXT = new StringBuilder();
	//
	private static int START;
	//
	private static int END;
	/*
	 * 阈值, 
	 * 当待抽取的网页正文中遇到成块的新闻标题未剔除时, 只要增大此阈值即可;
	 * 阈值增大，准确率提升，召回率下降;
	 * 值变小，噪声会大，但可以保证抽到只有一句话的正文
	 */
	private static int THRESHOLD = 86;
	// 引号
	private static final String QUOTE = "\"";
	// 编码
	private static final String ENCODING = "UTF-8";
	
	/**
	 * 获取 HTML 页面内容
	 * @param url 页面 url 地址
	 * @return
	 */
	public static String getHTMLContent(String url) {
        return getHTMLContent(url, ENCODING);
    }

	/**
	 * 获取 HTML 页面内容
	 * @param url 页面 url 地址
	 * @param encoding 页面编码规则
	 * @return
	 */
    public static String getHTMLContent(String url, String encoding) {
        try {
        	// 页面编码
        	String pageEncoding = encoding;
        	// 获取页面编码
            BufferedReader reader = new BufferedReader(new InputStreamReader(new URL(url).openStream()));
            String line = reader.readLine();
            while (line != null) {
            	if (line.contains("charset")) {
            		pageEncoding = cleanCharSet(getCharSet(line));
            		break;
            	}
                line = reader.readLine();
            }
            
            logger.info(">>> 当前页面的编码: {} >>>", pageEncoding);
            
            // 重新读取
            reader = new BufferedReader(new InputStreamReader(new URL(url).openStream(), pageEncoding));
            StringBuilder html = new StringBuilder();
            line = reader.readLine();
            while (line != null) {
            	html.append(line).append("\n");
                line = reader.readLine();
            }
            String content = parse(html.toString());
            return content;
        } catch (Exception e) {
        	logger.info(">>> 所解析二级url失败, 失败url地址: {} >>>", url);
        	logger.debug(">>> 解析url异常: {} >>>", e);
        }
        return " ";
    }
	
	/**
	 * 抽取网页的正文内容 (忽略目录类型)
	 * @param html 网页 HTML 字符串
	 * @return 网页正文
	 */
	public static String parse(String html) {
		return parse(html, false);
	}
	
	/**
	 * 判断传入HTML, 若是主题类网页, 则抽取正文; 否则输出<b>"unkown"</b>。
	 * @param html 网页 HTML 字符串
	 * @param flag true进行主题类判断, 默认为false
	 * @return 网页正文
	 */
	public static String parse(String html, boolean flag) {
		
//		logger.info(">>> 预处理前HTML字符串内容：{} >>>", html);
		logger.info(">>> HTML页面预处理开始! >>>");
		HTML = html;
		FLAG = flag;
		preProcess();
		logger.info(">>> HTML页面预处理结束! >>>");
//		logger.info(">>> 预处理后HTML字符串内容：{} >>>", html);
		
		return getText();
	}
	
	/**
	 * 预处理页面 HTML 字符串
	 */
	private static void preProcess() {
		HTML = HTML.replaceAll("(?is)<!DOCTYPE.*?>", "");
		// 剔除 HTML 声明
		HTML = HTML.replaceAll("(?is)<!--.*?-->", "");
		// 剔除 相关 JS
		HTML = HTML.replaceAll("(?is)<script.*?>.*?</script>", "");
		// 剔除 相关 CSS
		HTML = HTML.replaceAll("(?is)<style.*?>.*?</style>", "");
		// 剔除 特殊字符
		// <!--[if !IE]>|xGv00|9900d21eb16fa4350a3001b3974a9415<![endif]-->
		HTML = HTML.replaceAll("&.{2,5};|&#.{2,5};", " ");
		HTML = HTML.replaceAll("(?is)<.*?>", "");
	}
	
	/**
	 * 获取正文内容
	 * @return
	 */
	private static String getText() {
		
		LINES = Arrays.asList(HTML.split("\n"));
		INDEX_DISTRIBUTION.clear();
		
		for (int i = 0; i < LINES.size() - BLOCKS_WIDTH; i++) {
			int wordsNum = 0;
			for (int j = i; j < i + BLOCKS_WIDTH; j++) {
				LINES.set(j, LINES.get(j).replaceAll("\\s+", ""));
				wordsNum += LINES.get(j).length();
			}
			INDEX_DISTRIBUTION.add(wordsNum);
//			logger.info(">>> 获取到的内容的总长度wordsNum为: {} >>>", wordsNum);
		}
		
		START = -1;
		END = -1;
		boolean boolstart = false, boolend = false;
		TEXT.setLength(0);
		
		for (int i = 0; i < INDEX_DISTRIBUTION.size() - 1; i++) {
			if (INDEX_DISTRIBUTION.get(i) > THRESHOLD && !boolstart) {
				if (INDEX_DISTRIBUTION.get(i + 1).intValue() != 0 || INDEX_DISTRIBUTION.get(i + 2).intValue() != 0
						|| INDEX_DISTRIBUTION.get(i + 3).intValue() != 0) {
					boolstart = true;
					START = i;
					continue;
				}
			}
			if (boolstart) {
				if (INDEX_DISTRIBUTION.get(i).intValue() == 0 || INDEX_DISTRIBUTION.get(i + 1).intValue() == 0) {
					END = i;
					boolend = true;
				}
			}
			StringBuilder sb = new StringBuilder();
			if (boolend) {
				logger.info(">>> START: {} \t\t END: {} >>>", START + 1, END + 1);
				for (int ii = START; ii <= END; ii++) {
					if (LINES.get(ii).length() < 5) {
						continue;
					}
					sb.append(LINES.get(ii)).append("\n");
				}
				String str = sb.toString();
				if (str.contains("Copyright") || str.contains("版权所有")) {
					continue;
				}
				TEXT.append(str);
				boolstart = boolend = false;
			}
		}
		return TEXT.toString();
	}
	
	/**  
	 * 正则动态获取页面字符编码  
	 * @param content 页面内容
	 * @return 返回 charset 编码(不规则, 需要清洗)
	 */    
	private static String getCharSet(String content){    
	    String regex = ".*charset=([^;].*).*";
	    Pattern pattern = Pattern.compile(regex);
	    Matcher matcher = pattern.matcher(content);
	    if(matcher.find()) {
	    	return matcher.group(1);
	    }
        return "";
	}
	
	/**
	 * 清洗不规则编码
	 * @param charset 字符编码
	 */
	private static String cleanCharSet(String charset) {
		
		if (StringUtils.isEmpty(charset)) {
			// 默认赋值 utf-8
			return ENCODING;
		}
		/**
		 * "utf-8">
		 * gb2312'>
		 * utf-8" />
		 * utf-8">
		 * gb2312" /><meta name="ROBOTS" content="NOODP">
		 */
		// TODO 目前只有上述情况(有新规则自行添加)
		try {
			String newCharset = charset.replaceAll("'", QUOTE);
			String[] charsetArr = newCharset.split(QUOTE);
			if (newCharset.startsWith(QUOTE)) {
				return charsetArr[1];
			} else {
				return charsetArr[0];
			}
		} catch (Exception e) {
			// 异常赋值默认编码
			return ENCODING;
		}
	}
}
