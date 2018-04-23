package com.frame.process.utils.excel.support;


import java.util.List;

import com.frame.process.utils.excel.api.callback.ExcelReaderCallBack.CellContent;
import com.frame.process.utils.excel.wrapper.ExcelReaderWrapper;

/**
 * 通用的excel读取器,使用指定的包装器，将excel数据包装为对象，因此必须指定一个包装器
 * Created by zhh on 2018/04/21.
 */
public class CommonExcelReader<T> extends AbstractExcelReader<T> {

    protected ExcelReaderWrapper<T> wrapper = null;

    @Override
    public ExcelReaderWrapper<T> getWrapper() {
        return wrapper;
    }

    public void setWrapper(ExcelReaderWrapper<T> wrapper) {
        this.wrapper = wrapper;
    }

	protected boolean isHeader(CellContent content, List header) {
		// TODO Auto-generated method stub
		return false;
	}
}
