package com.frame.crawler.model.page;

import java.io.Serializable;
import java.util.Collection;
import java.util.List;

import com.github.pagehelper.Page;

/**
 * 分页返回结果
 * Created by zhh on 2018/04/02.
 */
public class PageInfo<T> implements Serializable {

	private static final long serialVersionUID = 6101700245197848996L;
	
	/**
	 * 总页数
	 */
	private int pages;
	
	/**
	 * 总记录数
	 */
	private long total;
	
	/**
	 * 结果集
	 */
	private List<T> list;
	
	/**
	 * 包装 Page 对象
	 * @param list 结果集
	 */
	@SuppressWarnings({ "rawtypes", "unchecked" })
	public PageInfo(List<T> list) {
		if (list instanceof Page) {
			Page page = (Page) list;
			this.pages = page.getPages();
			this.list = page;
			this.total = page.getTotal();
		} else if (list instanceof Collection) {
			this.pages = 1;
			this.list = list;
			this.total = (long) list.size();
		}
	}

	public int getPages() {
		return pages;
	}

	public void setPages(int pages) {
		this.pages = pages;
	}

	public long getTotal() {
		return total;
	}

	public void setTotal(long total) {
		this.total = total;
	}

	public List<T> getList() {
		return list;
	}

	public void setList(List<T> list) {
		this.list = list;
	}
	
}
