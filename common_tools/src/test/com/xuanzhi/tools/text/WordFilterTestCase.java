package com.xuanzhi.tools.text;

import junit.framework.TestCase;

public class WordFilterTestCase extends TestCase{

	public static void main(String args[]) throws Exception{
		WordFilterTestCase w = new WordFilterTestCase();
		w.testA();
	}
	
	public void testA() throws Exception{
		WordFilter wf = new WordFilter();
		wf.setFilterFile(System.getProperty("user.dir")+"/conf/testfilter2.txt");
		wf.initialize();
		String content = "法轮功sdfa法轮功";
		String c = wf.nvalidAndReplace(content, 1, "@#\\$%^\\&\\*");
		
		System.out.println("["+content+"]->["+c+"]");
	}
}
