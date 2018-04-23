package com.frame.process.utils.excel.wrapper;



import java.util.HashMap;
import java.util.Map;

/**
 * 
 * Created by zhh on 2018/04/21.
 */
public abstract class AbstractWrapper<T> implements ExcelReaderWrapper<T> {
    protected boolean shutdown;
    protected Map<String, String> headerNameMapper = new HashMap<>();

    @Override
    public boolean isShutdown() {
        return shutdown;
    }

    @Override
    public void shutdown() {
        shutdown = true;
    }


    @Override
    public void wrapperDone(T instance) {

    }

    protected String headerMapper(String old) {
        String newHeader = headerNameMapper.get(old);
        if (newHeader == null) return old;
        else return newHeader;
    }

    public Map<String, String> getHeaderNameMapper() {
        return headerNameMapper;
    }

    public void setHeaderNameMapper(Map<String, String> headerNameMapper) {
        this.headerNameMapper = headerNameMapper;
    }
}
