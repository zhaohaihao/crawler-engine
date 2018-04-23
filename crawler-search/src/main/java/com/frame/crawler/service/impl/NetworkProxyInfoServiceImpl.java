package com.frame.crawler.service.impl;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;
import org.springframework.util.StringUtils;

import com.frame.crawler.constants.GobalConstant;
import com.frame.crawler.core.AbstractService;
import com.frame.crawler.mapper.NetworkProxyInfoMapper;
import com.frame.crawler.model.NetworkProxyInfo;
import com.frame.crawler.model.page.PageInfo;
import com.frame.crawler.model.query.BaseQuery;
import com.frame.crawler.service.NetworkProxyInfoService;
import com.github.pagehelper.PageHelper;

@Service
public class NetworkProxyInfoServiceImpl extends AbstractService<NetworkProxyInfo> implements NetworkProxyInfoService {
	
	private Logger logger = LoggerFactory.getLogger(getClass());
	
	@Autowired
	private NetworkProxyInfoMapper networkProxyInfoMapper;
	
	@Override
	public Integer insert(NetworkProxyInfo networkProxyInfo) {
		saveReturnGeneratedKeys(networkProxyInfo);
		return networkProxyInfo.getId();
	}

	@Override
	public List<String> getProxyInfoString() {
		return networkProxyInfoMapper.selectProxyInfoString();
	}
	
	@Override
	public List<NetworkProxyInfo> getAllNetworkProxyInfoByIsValid(Integer isValid) {
		return findListBy("isValid", isValid);
	}

	@Override
	public PageInfo<NetworkProxyInfo> getLimitNetworkProxyInfoByIsValid(Integer isValid, BaseQuery baseQuery) {
		PageHelper.startPage(baseQuery.getPageNum(), baseQuery.getPageSize());
		List<NetworkProxyInfo> networkProxyInfos = getAllNetworkProxyInfoByIsValid(isValid);
		return new PageInfo<NetworkProxyInfo>(networkProxyInfos);
	}

	@Override
	public void insertInfoFromTxt(List<String> contents) {
		
		logger.info(">>> 代理ip信息入库操作开始执行 >>>");
		
		if (CollectionUtils.isEmpty(contents)) {
			// 内容为空, 不执行后面操作
			logger.info(">>> 读取的代理ip信息列表大小为空, 不执行后续操作 >>>");
			return;
		}

		// 获取数据库已经存在的代理ip信息, 格式-59.110.221.78:80
		List<String> dbProxyInfoStringList = getProxyInfoString();
		
		logger.info(">>> 读取的代理ip信息列表大小为: {} >>>", contents.size());
		
		// 内部信息去重
		List<String> newContents = contents.stream().distinct().collect(Collectors.toList());
		
		String txtProxyInfoString;
		// 检重, 剔除数据库内已经存在的数据
		for (int i = 0; i < newContents.size(); i++) {
			
			for (String dbProxyInfoString : dbProxyInfoStringList) {
				
				txtProxyInfoString = newContents.get(i);
				if (dbProxyInfoString.equals(txtProxyInfoString)) {
					// 移除当前已存在数据
					newContents.remove(i);
					// 移除后, 列表大小变化, 需改变索引, 防止后面异常
					i--;
					break;
				}
			}
		}
		
		List<NetworkProxyInfo> insertIntoDBInfos = new ArrayList<>();
		NetworkProxyInfo networkProxyInfo;
		// 去重, 检重后可完全入库的代理信息, 格式：59.110.221.78:80
		for (String content : newContents) {
			
			if (StringUtils.isEmpty(content) || !content.contains(":")) {
				// 内容为空, 格式不符合规范忽略后续操作
				continue;
			}
			
			String[] contentArr = content.split(":");
			String ip = contentArr[0];
			String port = contentArr[1];
			
			// 设置存储信息
			networkProxyInfo = new NetworkProxyInfo();
			networkProxyInfo.setProxyIp(ip);
			networkProxyInfo.setNetworkUrl("");
			networkProxyInfo.setProxyPort(port);
			networkProxyInfo.setIsValid(GobalConstant.ProxyValid.VALID);
			networkProxyInfo.setUserName("");
			networkProxyInfo.setPwd("");
			networkProxyInfo.setNetType(GobalConstant.NetType.HTTP);
			
//			logger.info(">>> 需要入库的代理ip信息:{} >>>", networkProxyInfo);
			insertIntoDBInfos.add(networkProxyInfo);
		}
		
		// 存储信息
		save(insertIntoDBInfos);
		logger.info(">>> 代理ip信息入库操作执行完毕 >>>");
	}

}
