package com.frame.process.core;

import java.lang.reflect.Field;
import java.lang.reflect.ParameterizedType;
import java.util.List;

import org.mybatis.spring.MyBatisSystemException;
import org.springframework.beans.factory.annotation.Autowired;

/**
 * 基于通用MyBatis Mapper插件的Service接口的实现
 * Created by zhh on 2018/04/19.
 */
public abstract class AbstractService<T> {

    @Autowired
    protected MyMapper<T> mapper;

    // 当前泛型真实类型的Class
    private Class<T> modelClass;    

    @SuppressWarnings("unchecked")
	public AbstractService() {
        ParameterizedType pt = (ParameterizedType) this.getClass().getGenericSuperclass();
        modelClass = (Class<T>) pt.getActualTypeArguments()[0];
    }

    public void save(T model) {
        mapper.insertSelective(model);
    }
    
    public void saveReturnGeneratedKeys(T model) {
    	mapper.insertUseGeneratedKeys(model);
    }

    public void save(List<T> models) {
        mapper.insertList(models);
    }

    public void deleteById(Integer id) {
        mapper.deleteByPrimaryKey(id);
    }

    public void deleteByIds(String ids) {
        mapper.deleteByIds(ids);
    }

    public void update(T model) {
        mapper.updateByPrimaryKeySelective(model);
    }

    public T findById(Integer id) {
        return mapper.selectByPrimaryKey(id);
    }

	public T findOneBy(String fieldName, Object value) {
        try {
            T model = modelClass.newInstance();
            Field field = modelClass.getDeclaredField(fieldName);
            field.setAccessible(true);
            field.set(model, value);
            return mapper.selectOne(model);
        } catch (ReflectiveOperationException e) {
        	// TODO 查询反射操作异常
        } catch (MyBatisSystemException e) {
        	// TODO 查询返回结果不唯一
        }
        return null;
    }
	
	public List<T> findListBy(String fieldName, Object value) {
        try {
            T model = modelClass.newInstance();
            Field field = modelClass.getDeclaredField(fieldName);
            field.setAccessible(true);
            field.set(model, value);
            return mapper.select(model);
        } catch (ReflectiveOperationException e) {
        	// TODO 查询反射操作异常
        	return null;
        }
    }

    public List<T> findByIds(String ids) {
        return mapper.selectByIds(ids);
    }

    public List<T> findBy(T model) {
        return mapper.select(model);
    }

    public List<T> findAll() {
        return mapper.selectAll();
    }
    
}
