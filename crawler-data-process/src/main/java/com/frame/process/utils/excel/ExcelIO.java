package com.frame.process.utils.excel;

import java.io.InputStream;
import java.util.List;
import java.util.Map;

import com.frame.process.utils.excel.support.CommonExcelReader;
import com.frame.process.utils.excel.wrapper.BeanWrapper;
import com.frame.process.utils.excel.wrapper.ExcelReaderWrapper;

/**
 * 
 * Created by zhh on 2018/04/21.
 */
public class ExcelIO {
	
	/**
     * 读取excel为javaBean
     *
     * @param inputStream  excel输入流
     * @param headerMapper 表头与字段映射配置
     * @param <T>          bean泛型
     * @param tClass       javaBean类型
     * @return bean集合
     * @throws Exception 读取异常
     */
    public static <T> List<T> read2Bean(InputStream inputStream, Map<String, String> headerMapper, Class<T> tClass) throws Exception {
        BeanWrapper wrapper = new BeanWrapper<T>();
        wrapper.setType(tClass);
        wrapper.setHeaderNameMapper(headerMapper);
        return read(inputStream, wrapper);
    }
    
    /**
     * 自定义包装器读取excel为集合
     *
     * @param inputStream excel输入流
     * @param wrapper     包装器
     * @param <T>         读取结果泛型
     * @return 读取结果集合
     * @throws Exception 读取异常
     */
    public static <T> List<T> read(InputStream inputStream, ExcelReaderWrapper<T> wrapper) throws Exception {
        CommonExcelReader reader = new CommonExcelReader();
        reader.setWrapper(wrapper);
        return reader.readExcel(inputStream);
    }
}
