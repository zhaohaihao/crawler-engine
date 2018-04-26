package com.frame.process.core;

import java.util.List;

/**
 * Service 层 基础接口，其他Service 接口 请继承该接口
 * Created by zhh on 2018/04/19.
 */
public interface Service<T> {
	
	/**
	 * 插入数据
	 * @param model
	 */
    void save(T model);
    
    /**
     * 插入数据返回带主键实体
     * @param model
     * @return
     */
    void saveReturnGeneratedKeys(T model);
    
    /**
     * 批量插入数据
     * @param models
     */
    void save(List<T> models);
    
    /**
     * 通过主鍵刪除
     * @param id
     */
    void deleteById(Integer id);
    
    /**
     * 批量刪除 
     * @param ids eg：ids -> "1,2,3,4"
     */
    void deleteByIds(String ids);
    
    /**
     * 更新
     * @param model
     */
    void update(T model);
    
    /**
     * 批量更新
     * @param models
     * @param flag
     */
    void updateBatch(List<T> models, Integer flag);
    
    /**
     * 通过ID查找
     * @param id
     * @return
     */
    T findById(Integer id);
    
    /**
     * 通过Model中某个成员变量名称（非数据表中column的名称）查找,value需符合unique约束
     * @param fieldName 属性名
     * @param value
     * @return
     */
    T findOneBy(String fieldName, Object value);
    
    /**
     * 通过Model中某个成员变量名称（非数据表中column的名称）查找
     * @param fieldName 属性名
     * @param value
     * @return
     */
    List<T> findListBy(String fieldName, Object value);
    
    /**
     * 通过多个ID查找
     * @param ids eg：ids -> "1,2,3,4"
     * @return
     */
    List<T> findByIds(String ids);
    
    /**
     * 根据条件查找
     * @param model
     * @return
     */
    List<T> findBy(T model);
    
    /**
     * 获取所有
     * @return
     */
    List<T> findAll();
    
    /**
     * 获取限定数量数据
     * @param startPos 起始位
     * @param limit 限定数目
     * @param flag 已经标志
     * @return
     */
    List<T> findDatasByLimit(Integer startPos, Integer limit, Integer flag);
}
