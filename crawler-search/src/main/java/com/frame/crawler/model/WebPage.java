package com.frame.crawler.model;

import java.io.Serializable;
import java.util.Date;

import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

import com.alibaba.fastjson.JSON;

/**
 * 爬取的页面的内容
 * Created by zhh on 2018/03/30.
 */
@Table(name = "webpage")
public class WebPage implements Serializable {
	
	private static final long serialVersionUID = 8244758142724688624L;

	/**
	 * ID
	 */
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Integer id;
	
	/**
	 * 标题
	 */
	private String title;
	
	/**
	 * 请求链接
	 */
	private String reqUrl;
	
	/**
	 * 目标链接
	 */
	private String targetUrl;
	
	/**
	 * 简介
	 */
    private String summary;
    
    /**
     * 正文内容
     */
    private String content;
    
    /**
     * 来源类型
     * 1. 百度，2.搜狗，3.360；
     */
    private Integer srcType;
    
    /**
     * 搜索关键字
     */
    private String keyWord;
    
    /**
     * 所处页数, 默认1
     */
    private Integer pageNum;
    
    /**
     * 顺序, 默认1
     */
    private Integer sort;
    
    /**
     * 读取标志；0.未读，1.已读；默认0.未读
     */
    private Integer flag;
    
    /**
     * 导入日期
     */
    private Date importDate;

    /**
     * 关联ID
     */
    private String uuid;
    
	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public String getReqUrl() {
		return reqUrl;
	}

	public void setReqUrl(String reqUrl) {
		this.reqUrl = reqUrl;
	}

	public String getTargetUrl() {
		return targetUrl;
	}

	public void setTargetUrl(String targetUrl) {
		this.targetUrl = targetUrl;
	}

	public String getSummary() {
		return summary;
	}

	public void setSummary(String summary) {
		this.summary = summary;
	}

	public String getContent() {
		return content;
	}

	public void setContent(String content) {
		this.content = content;
	}

	public Integer getSrcType() {
		return srcType;
	}

	public void setSrcType(Integer srcType) {
		this.srcType = srcType;
	}

	public String getKeyWord() {
		return keyWord;
	}

	public void setKeyWord(String keyWord) {
		this.keyWord = keyWord;
	}

	public Integer getPageNum() {
		return pageNum;
	}

	public void setPageNum(Integer pageNum) {
		this.pageNum = pageNum;
	}

	public Integer getSort() {
		return sort;
	}

	public void setSort(Integer sort) {
		this.sort = sort;
	}

	public Date getImportDate() {
		return importDate;
	}

	public void setImportDate(Date importDate) {
		this.importDate = importDate;
	}
	
	public Integer getFlag() {
		return flag;
	}

	public void setFlag(Integer flag) {
		this.flag = flag;
	}

	public String getUuid() {
		return uuid;
	}

	public void setUuid(String uuid) {
		this.uuid = uuid;
	}

	@Override
	public String toString() {
		return JSON.toJSONString(this);
	}
	
}
