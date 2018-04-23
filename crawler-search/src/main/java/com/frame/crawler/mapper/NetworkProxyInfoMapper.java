package com.frame.crawler.mapper;

import java.util.List;

import com.frame.crawler.core.MyMapper;
import com.frame.crawler.model.NetworkProxyInfo;

/**
 * 
 * Created on zhh 2018/04/02.
 */
public interface NetworkProxyInfoMapper extends MyMapper<NetworkProxyInfo> {

	/**
	 * 查询所有的代理ip信息, 格式-59.110.221.78:80
	 * @return
	 */
	List<String> selectProxyInfoString();
}
