package com.xuanzhi.tools.text;

/**
 *
 * 
 */
public class PasswordGenerator {
	public static void main(String args[]) {
		for(int i=0; i<100; i++) {
			System.out.println(StringUtil.randomString(16));
		}
	}
}
