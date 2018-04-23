package com.frame.process.core;

import tk.mybatis.mapper.common.BaseMapper;
import tk.mybatis.mapper.common.IdsMapper;
import tk.mybatis.mapper.common.special.InsertListMapper;
import tk.mybatis.mapper.common.special.InsertUseGeneratedKeysMapper;

/**
 * 通用 Mapper, 如果被扫描到会报错
 * Created by zhh on 2018/04/19.
 */
public interface MyMapper<T> extends BaseMapper<T>, IdsMapper<T>, InsertListMapper<T>, InsertUseGeneratedKeysMapper<T> {

}
