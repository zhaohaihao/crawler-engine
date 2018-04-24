package com.frame.process.subscribe;

import java.util.ArrayList;
import java.util.List;

import com.frame.process.process.interfaces.ProcessInter;

/**
 * 文件数量订阅
 * Created by zhh on 2018/04/24.
 */
public class FileSubscriber {
	
	private static FileSubscriber instance = null;
	
	private static List<String> filePaths = new ArrayList<>();
	
	private static int count = 0;
	
	private FileSubscriber() {}
	
	/**
	 * 懒汉式单例, 这里用双重锁定避免额外开销和多线程访问的单例不达标
	 * @return
	 */
	public static FileSubscriber getInstance() {
		if (instance == null) {
			synchronized (FileSubscriber.class) {
				if (instance == null) {
					instance = new FileSubscriber();
				}
			}
		}
		return instance;
	}
	
	/**
	 * 统计文件是否达到包符合数量
	 * @param filePath
	 */
	public synchronized void countFileNum(ProcessInter processInter, String filePath) {
		if (filePath == null) {
			return;
		}
		if (count < 5) {
			// 如果数目小于当前打包数量范围
			count = count + 1;
			filePaths.add(filePath);
		} else {
			// 通知打包方法, 清除原始数据
			processInter.packFileWithFixedNum(filePaths);
			count = 0;
			filePaths.clear();
		}
	}
}
