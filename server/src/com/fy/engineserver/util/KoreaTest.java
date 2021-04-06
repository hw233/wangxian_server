package com.fy.engineserver.util;

import com.xuanzhi.tools.text.FileUtils;

/**
 *
 * 
 * @version 创建时间：Apr 27, 2013 6:42:42 PM
 * 
 */
public class KoreaTest {
	public static void main(String args[]) {
		String f1 = args[0];
		String f2 = args[1];
		String c1 = FileUtils.readFile(f1);
		String c2 = FileUtils.readFile(f2);
		String str1[] = c1.split("\n");
		String str2[] = c2.split("\n");
		for(int i=0; i<str1.length; i++) {
			for(int j=0; j<str2.length; j++) {
				if(str1[i].trim().equals(str2[j].trim())) {
					System.out.println(str1[i].trim() + " " + str2[j]);
				}
			}
		}
	}
}
