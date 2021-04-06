package com.fy.engineserver.util;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

/**
 * 
 * @version 创建时间：Mar 22, 2011 6:57:54 PM
 * 
 */
public class Test {
	private String good = "";

	public String getGood() {
		return good;
	}

	public void setGood(String good) {
		this.good = good;
	}
	
	public void exec(Test test) {
//		System.out.println(test.getGood());
	}
	
	public static void main(String args[]) {
		TestA ta = new TestA();
		ta.setGood("OK");
		Test test = new Test();
		Class clazz = test.getClass();
		try {
			Method m1 = clazz.getDeclaredMethod("exec", Test.class);
			m1.invoke(test, ta);
		} catch (SecurityException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (NoSuchMethodException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IllegalArgumentException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IllegalAccessException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (InvocationTargetException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
	}
}
