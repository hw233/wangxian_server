package com.fy.engineserver.newtask;
import junit.framework.TestCase;

import com.xuanzhi.tools.text.JsonUtil;
public class TaskTestCase extends TestCase{

	public void testXXXXX() throws Exception{
		TaskEntity te = new TaskEntity();
		
		String s = JsonUtil.jsonFromObject(te);
		
		System.out.println("len=" + s.length() + "," + s);
	}
}
