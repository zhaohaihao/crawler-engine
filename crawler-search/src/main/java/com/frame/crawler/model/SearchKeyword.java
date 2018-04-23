package com.frame.crawler.model;

import java.io.Serializable;
import java.util.Date;

import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;

import com.alibaba.fastjson.JSON;

/**
 * 搜索关键字
 * Created by zhh on 2018/04/08.
 */
public class SearchKeyword implements Serializable {

	private static final long serialVersionUID = -2950523042539979766L;
	
	/**
	 * ID
	 */
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Integer id;
	
	/**
	 * 关键字
	 */
	private String keyword;
	
	/**
	 * 关键词分类，1.诈骗；默认1.诈骗
	 */
	private Integer keywordType;

	/**
	 * 搜索方式，0.全查，1.百度，2.搜狗，3.360；默认0.全查
	 */
	private Integer searchType;
	
	/**
	 * 爬取的次数
	 */
	private Integer count;
	
	/**
	 * 最近一次爬取的时间
	 */
	private Date lastTime;
	
	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public String getKeyword() {
		return keyword;
	}

	public void setKeyword(String keyword) {
		this.keyword = keyword;
	}

	public Integer getKeywordType() {
		return keywordType;
	}

	public void setKeywordType(Integer keywordType) {
		this.keywordType = keywordType;
	}

	public Integer getSearchType() {
		return searchType;
	}

	public void setSearchType(Integer searchType) {
		this.searchType = searchType;
	}

	public Integer getCount() {
		return count;
	}

	public void setCount(Integer count) {
		this.count = count;
	}

	public Date getLastTime() {
		return lastTime;
	}

	public void setLastTime(Date lastTime) {
		this.lastTime = lastTime;
	}

	@Override
	public String toString() {
		return JSON.toJSONString(this);
	}
}
