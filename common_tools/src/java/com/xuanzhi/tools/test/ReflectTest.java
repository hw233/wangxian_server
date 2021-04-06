package com.xuanzhi.tools.test;

import java.lang.reflect.Method;
import java.util.HashMap;

/**
 * 
 */
public class ReflectTest {
	
	private long id;
	
	private String name;
	
	public ReflectTest(long id, String name) {
		this.id = id;
		this.name = name;
	}
	
	@Override
	public String toString() {
		return id + "_" + name;
	}
	
	public static void main(String args[]) {
		int num = 1000000;
		ReflectTest t = new ReflectTest(0, "1");
		long now = System.currentTimeMillis();
		for(int i=0; i<num; i++) {
			if(i != -1) {
				t.toString();
			}
		}
		System.out.println("直接调用方法耗时," + (System.currentTimeMillis()-now) + "ms");
		now = System.currentTimeMillis();
		try {
			Class clazz = t.getClass();
			Method m1 = clazz.getDeclaredMethod("toString");
			HashMap<String, Method> map = new HashMap<String, Method>();
			map.put("", m1);
			for(int i=0; i<num; i++) {
				Method m = map.get("");
				m.invoke(t);
			}
		} catch(Exception e) {e.printStackTrace();}
		System.out.println("调用反射方法耗时," + (System.currentTimeMillis()-now) + "ms");
	}
}
