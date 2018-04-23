package com.frame.crawler.service;

import java.util.List;

import com.frame.crawler.core.Service;
import com.frame.crawler.model.NetworkProxyInfo;
import com.frame.crawler.model.page.PageInfo;
import com.frame.crawler.model.query.BaseQuery;

/**
 * 
 * Created by zhh on 2018/04/02.
 */
public interface NetworkProxyInfoService extends Service<NetworkProxyInfo> {
	
	/**
	 * 插入数据
	 * @param networkProxyInfo 代理ip信息
	 * @return 主键
	 */
	Integer insert(NetworkProxyInfo networkProxyInfo);
	
	/**
	 * 查找所有的代理ip信息, 格式-59.110.221.78:80
	 * @return
	 */
	List<String> getProxyInfoString();
	
	/**
	 * 查找有效的代理ip信息
	 * @param isValid 是否有效，0.有效，1.无效；默认0
	 * @return
	 */
	List<NetworkProxyInfo> getAllNetworkProxyInfoByIsValid(Integer isValid);
	
	/**
	 * 分页查找有效的代理ip信息
	 * @param isValid 是否有效，0.有效，1.无效；默认0
	 * @return
	 */
	PageInfo<NetworkProxyInfo> getLimitNetworkProxyInfoByIsValid(Integer isValid, BaseQuery baseQuery);
	
	/**
	 * 将从文本中获取的内容插入
	 * @param contents 代理信息字符串
	 */
	void insertInfoFromTxt(List<String> contents);
	
}
