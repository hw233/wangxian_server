package com.fy.engineserver.util;
import java.util.HashMap;

import com.xuanzhi.tools.text.FileUtils;


public class ArticleMap {
	public static void main(String args[]) {
		String newOld = "d:/new_old.txt";
		String newId = "d:/newIds.txt";
		String s = FileUtils.readFile(newOld);
		String newOlds[] = s.split("\n");
		HashMap<String,String> m1 = new HashMap<String,String>();
		for(int i=0; i<newOlds.length; i++) {
			String ss[] = newOlds[i].split(",");
			if(ss.length >= 2) {
				m1.put(ss[0], ss[1]);
				if(i % 100 == 0) {
//					System.out.println("*" + i);
				}
			}
		}
		String newF = FileUtils.readFile(newId);
		String newIds[] = newF.split("\n");
		StringBuffer sb = new StringBuffer();
		for(int i=0; i<newIds.length; i++) {
			String c = m1.get(newIds[i]);
			if(c != null) {
//				System.out.println(c+","+newIds[i]);
				sb.append(c+","+newIds[i]+"\n");
			}
		}
		FileUtils.writeFile(sb.toString(), "d:/maping.txt");
	}
}
