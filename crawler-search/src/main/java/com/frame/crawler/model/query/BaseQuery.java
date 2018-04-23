package com.frame.crawler.model.query;

import java.io.Serializable;

/**
 * 分页查找 Bean
 * Created by zhh on 2018/01/23.
 */
public class BaseQuery implements Serializable {

	private static final long serialVersionUID = -2859004024695946153L;

	/**
	 * 当前页 默认初始为1
	 */
	private Integer pageNum = 1;

	/**
	 * 页面条数 默认初始为10
	 */
	private Integer pageSize = 10;

	public Integer getPageNum() {
		return pageNum;
	}

	public void setPageNum(Integer pageNum) {
		if (pageNum < 1){

            pageNum = 1;
        }
        this.pageNum = pageNum;
	}

	public Integer getPageSize() {
		return pageSize;
	}

	public void setPageSize(Integer pageSize) {
		this.pageSize = pageSize;
	}

}
