package com.frame.process.utils.excel.api;




import java.io.InputStream;

import com.frame.process.utils.excel.api.callback.ExcelReaderCallBack;

/**
 * excel操作的API接口
 * Created by zhh on 2018/04/21.
 */
public interface ExcelApi {

    /**
     * 基于回掉的excel读取
     *
     * @param inputStream excel文件输入流
     * @param callBack    excel读取回掉接口
     * @throws Exception 读取异常
     */
    void read(InputStream inputStream, ExcelReaderCallBack callBack) throws Exception;

}
