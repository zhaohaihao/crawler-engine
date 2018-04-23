package com.frame.process.utils.excel.support;

import java.io.InputStream;
import java.util.List;

/**
 * Excel读取器接口,用于将excel文件读取为java对象
 * Created by zhh on 2018/04/21.
 */
public interface ExcelReader<T> {
    /**
     * 读取excel为java对象集合
     *
     * @param inputStream excel输入流
     * @return 读取结果
     * @throws Exception 读取异常
     */
    List<T> readExcel(InputStream inputStream) throws Exception;
}
