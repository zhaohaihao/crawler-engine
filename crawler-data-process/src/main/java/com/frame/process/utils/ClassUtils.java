package com.frame.process.utils;

import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;

/**
 * 类工具类
 * Created by zhh on 2018/04/21.
 */
public class ClassUtils {
	
	/**
     * 获取一个类的第一个泛型的类型
     *
     * @param clazz 要获取的类
     * @return 泛型
     */
    public static Class<?> getGenericType(Class clazz) {
        return getGenericType(clazz, 0);
    }
    
    /**
     * 获取一个类的泛型类型,如果未获取到返回Object.class
     *
     * @param clazz 要获取的类
     * @param index 泛型索引
     * @return 泛型
     */
    public static Class<?> getGenericType(Class clazz, int index) {
        Type genType = clazz.getGenericSuperclass();
        if (!(genType instanceof ParameterizedType)) {
            return Object.class;
        }
        Type[] params = ((ParameterizedType) genType).getActualTypeArguments();
        if (index >= params.length || index < 0) {
            throw new RuntimeException("Index outof bounds");
        }
        if (!(params[index] instanceof Class)) {
            return Object.class;
        }
        return (Class) params[index];
    }
}
