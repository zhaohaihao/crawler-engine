package com.frame.crawler.service;

import java.util.List;

import com.frame.crawler.core.Service;
import com.frame.crawler.model.BaiduTiebaImage;

/**
 * 
 * Created by zhh on 2018/04/13.
 */
public interface BaiduTiebaImageService extends Service<BaiduTiebaImage> {
	
	/**
	 * 获取未下载的图片
	 * @param limit 限制条数
	 * @return
	 */
	List<BaiduTiebaImage> getNotDownloadImagesByLimit(Integer limit);
	
	/**
	 * 下载图片保存至本地
	 * @param baiduTiebaImages 图片信息集
	 */
	void downloadImagesToLocal(List<BaiduTiebaImage> baiduTiebaImages);
}
