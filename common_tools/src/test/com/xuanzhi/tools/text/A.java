package com.xuanzhi.tools.text;

import java.util.HashMap;

/**
 *
 * 
 */
public class A {
	public static void main(String args[]) {
		long start = System.currentTimeMillis();
		String f1 = "d:/a1.txt";
		String f2 = "d:/a2.txt";
		String out = "d:/f.txt";
		String c = FileUtils.readFile(f1);
		String str[] = c.split("\n");
		HashMap<String,String> m1 = new HashMap<String,String>();
		for(String s : str) {
			String a[] = s.split(",");
			m1.put(a[0], s);
		}
		c = FileUtils.readFile(f2);
		str = c.split("\n");
		StringBuffer sb = new StringBuffer();
		for(String s : str) {
			String a[] = s.split(",");
			if(m1.get(a[0]) != null) {
				sb.append(m1.get(a[0]) + "," + a[1] + "\n");
			}
		}
		FileUtils.writeFile(sb.toString(), out);
		System.out.println("处理完成,耗时:"+(System.currentTimeMillis()-start)+"ms.");
	}
}
