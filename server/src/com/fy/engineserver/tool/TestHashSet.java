package com.fy.engineserver.tool;

import java.util.HashSet;

/**
 *
 * 
 * @version 创建时间：Mar 6, 2012 8:11:04 PM
 * 
 */
public class TestHashSet {
	public static void main(String args[]) {
		int num = 200000;
		HashSet<String> set = new HashSet<String>();
		for(int i=0; i<500; i++) {
			set.add("ooooooooooooooooooooooooooooooooooooooo-"+i);
		}
		long now = System.currentTimeMillis();
		for(int i=0; i<num; i++) {
			if(set.contains("bb")) {
				//
			}
		}
//		System.out.println("执行contains方法" + num + "次，消耗" + (System.currentTimeMillis()-now)+"ms");
	}
}
