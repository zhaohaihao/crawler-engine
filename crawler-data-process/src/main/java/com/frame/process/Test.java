package com.frame.process;

public class Test {
	
	public static void main(String[] args) {
		String fileName = "C:/Users/Administrator/Desktop/test/帖子详情内容表_83af4733-42a0-475a-b5f9-8435b62faa35.xlsx";
		int pos = fileName.lastIndexOf("/");
		if (pos != -1) {
			fileName = fileName.substring(pos + 1);
		}
		pos = fileName.indexOf("_");
		if (pos != -1) {
			fileName = fileName.substring(0, pos);
		}
		System.out.println(fileName);
	}
}
