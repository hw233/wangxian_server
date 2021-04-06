package com.xuanzhi.tools.test;

import com.xuanzhi.tools.text.StringUtil;

/**
 *
 * 
 */
public class CloneTest implements Cloneable {
	private long id;
	
	private String name;
	
	public CloneTest(long id, String name) {
		this.id = id;
		this.name = name;
	}

	public long getId() {
		return id;
	}

	public void setId(long id) {
		this.id = id;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}
	
	public static void main(String args[]) {
//		CloneTest t = new CloneTest(1, "1");
//		CloneTest t2 = null;
//		try {
//			t2 = (CloneTest)t.clone();
//			t2.setId(2);
//			t2.setName("2");
//		} catch (CloneNotSupportedException e) {
//			// TODO Auto-generated catch block
//			e.printStackTrace();
//		}
//		System.out.println("t:{"+t.getId()+"}{"+t.getName()+"}   t2:{"+t2.getId()+"}{"+t2.getName()+"}");
//		
		long now = System.currentTimeMillis();
		int num = 1000000;
		for(int i=0; i<num; i++) {
			CloneTest t = new CloneTest(i, "" + i);
		}
		System.out.println("直接new"+num+"个对象，耗费时间:" + (System.currentTimeMillis()-now));
		now = System.currentTimeMillis();
		CloneTest t = new CloneTest(0, "" + 0);
		for(int i=0; i<num; i++) {
			try {
				CloneTest t2 = (CloneTest)t.clone();
				t2.setId(i);
				t2.setName(""+i);
			} catch (CloneNotSupportedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		System.out.println("使用clone方法得到"+num+"个对象，耗费时间:" + (System.currentTimeMillis()-now));
		System.out.println(StringUtil.hash("123401b31d9c43c8d8d7ebf655b6ab96f5c09"));
	}
}
