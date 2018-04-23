package com.frame.process.process.interfaces;

import java.io.FileInputStream;
import java.io.InputStream;
import java.lang.reflect.Field;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Queue;
import java.util.concurrent.LinkedBlockingQueue;

import org.apache.commons.lang3.ArrayUtils;

import com.frame.process.annotation.ExportTitle;
import com.frame.process.core.Service;
import com.frame.process.utils.DateTimeUtils;
import com.frame.process.utils.excel.ExcelIO;

/**
 * 核心处理类的抽象类
 * Created by zhh on 2018/04/19.
 */
public abstract class AbstractProcess extends CommonProcess implements ProcessInter {
	
	/**
	 * 保存文件
	 * @param fileName 文件名
	 * @param datas 需要保持的数据
	 * @return
	 * @throws Exception 
	 */
	public abstract String saveFile(String fileName, Queue<String> datas) throws Exception;
	
	/**
	 * 反射获取所需导出数据库数据, 分表
	 * @param tableType 表类别
	 * @return
	 * @throws Exception 
	 */
	public Queue<String> getDBDatas(String tableType) throws Exception {
		// 整理后的数据
		Queue<String> datas = new LinkedBlockingQueue<>();
		// 获取对应的服务类
		Service serviceBean = categoryMap.get(tableType);
		if (serviceBean == null) {
			// 如果当前无该服务, 跳过后续执行
			return datas;
		}
		// 获取对应的实体类
		Class pojoClazz = initialCategoriesMap.get(tableType);
		// 获取的数据
		List<?> findDatas = serviceBean.findDatasByLimit(0, 50000);

		// 数据信息
		Field[] clazzFields = pojoClazz.getDeclaredFields();
		String[] nameArray = saveHeaders(datas, clazzFields);
		saveBodys(datas, findDatas, clazzFields, nameArray);
		return datas;
	}
	
	/**
	 * 获取文件数据并转成对应实体类
	 * @param filePaths 待解析文件路径列表
	 * @param tableType 表类别
	 * @return
	 * @throws Exception 
	 */
	public List getFileDatas(String filePath, String tableType) throws Exception {
		Class clazz = initialCategoriesMap.get(tableType);
		if (clazz == null) {
			logger.info("=== 无此类型POJO类型, 请检查文件名是否正确! ===");
			return null;
		}
		
		InputStream in = new FileInputStream(filePath);
		// 读取到的实体类
		return ExcelIO.read2Bean(in, getHeaderMapper(clazz), clazz);
	}
	
	/**
	 * 保存文件的数据至对应的表
	 * @param fileDatas 文件解包出来的数据
	 * @param tableType 对应的表类别
	 */
	public void saveFileDatas(List fileDatas, String tableType) throws Exception {
		// 获取对应的服务类
		Service serviceBean = categoryMap.get(tableType);
		if (serviceBean == null) {
			// 如果当前无该服务, 跳过后续执行
			return;
		}
		// 保存数据
		serviceBean.save(fileDatas);
	}
	
	/**
	 * 获取实体类表头和字段的的映射
	 * @param clazz
	 * @return
	 */
	protected Map<String, String> getHeaderMapper(Class<?> clazz) {
		Map<String, String> headerMapper = new HashMap<>();
		Field[] clazzFields = clazz.getDeclaredFields();
		for (int i = 0; i < clazzFields.length; i++) {
			// 当存在 @ExportTitle 才保存
			if (clazzFields[i].isAnnotationPresent(ExportTitle.class)) {
				// 列名
				ExportTitle title = clazzFields[i].getAnnotation(ExportTitle.class);
				// 实体类属性名
				String name = clazzFields[i].getName();
				headerMapper.put(title.value(), name);
			}
		}
		return headerMapper;
	}
	
	/**
	 * 保存数据内容
	 * @param datas 保存数据队列
	 * @param collects 原始查询数据
	 * @param fields 对应实体属性
	 * @param nameArray 列头
	 * @throws Exception 
	 */
	private void saveBodys(Queue<String> datas, List<?> collects, Field[] fields, String[] nameArray) throws Exception {
		for (int i = 0; i < collects.size(); i++) {
			Object object = collects.get(i);
			// 反射获取对应的属性值
			for (int j = 0; j < fields.length; j++) {
				String name = fields[j].getName();
				// 判断当前的属性是否是选中的属性
				int pos = ArrayUtils.indexOf(nameArray, name);
				if (pos == ArrayUtils.INDEX_NOT_FOUND) {
					continue;
				}
				// 允许获取私有属性的值
				fields[j].setAccessible(true);
				// 获取当前对象对应的属性值
				Object value = fields[j].get(object);
				value = value == null ? "" : value;
				// TODO: 个别字段映射处理
				
				if (value instanceof Date) {
					value = DateTimeUtils.format((Date) value, DateTimeUtils.YEAR_MONTH_DAY_HOUR_MINUTE_SECOND);
				}
				// 保存数据
				datas.offer(value.toString());
			}
			datas.offer(LINE_BREAK);
		}
	}
	
	/**
	 * 保存列头
	 * @param datas 数据
	 * @param fields 类属性
	 * @return
	 */
	private String[] saveHeaders(Queue<String> datas, Field[] fields) {
		String[] nameArray = new String[] {};
		for (int i = 0; i < fields.length; i++) {
			// 当存在 @ExportTitle 才保存
			if (fields[i].isAnnotationPresent(ExportTitle.class)) {
				// 存下表头
				String name = fields[i].getName();
				nameArray = ArrayUtils.add(nameArray, name);
				
				ExportTitle title = fields[i].getAnnotation(ExportTitle.class);
				datas.offer(title.value());
			}
		}
		datas.offer(LINE_BREAK);
		return nameArray;
	}
}
