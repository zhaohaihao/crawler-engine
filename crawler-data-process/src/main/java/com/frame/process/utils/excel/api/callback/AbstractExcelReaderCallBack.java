package com.frame.process.utils.excel.api.callback;

/**
 * 抽象回掉，实现手动结束读取的功能
 * Created by zhh on 2018/04/21.
 */
public abstract class AbstractExcelReaderCallBack implements ExcelReaderCallBack {

    protected boolean shutdown;

    @Override
    public boolean isShutdown() {
        return shutdown;
    }

    @Override
    public void shutdown() {
        shutdown = true;
    }

    @Override
    public void done(Object workBook) {

    }

    @Override
    public void sheetDone(Object sheet) throws Exception {

    }
}
