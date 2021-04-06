package com.fy.engineserver.activity;

import java.util.ArrayList;
import java.util.HashMap;

@CheckAttribute("测试 ")
public class TestCheck {
	@CheckAttribute("简单MAP")
	private HashMap<Integer, String> simpleMap = new HashMap<Integer, String>();
	@CheckAttribute("简单Lisst ")
	private ArrayList<String> simpleList = new ArrayList<String>();
	@CheckAttribute("二维数组")
	private String[][] array2 = new String[2][];
	@CheckAttribute("一维数组")
	private String[] array1 = new String[2];
	@CheckAttribute("字符串")
	private String stringValue;
	@CheckAttribute("Integer")
	private int intValue;
	@CheckAttribute("数组为值的MAP")
	private HashMap<String, String[]> arrayValueMap = new HashMap<String, String[]>();
	@CheckAttribute("double数组")
	private double[] doubleArr1 ;
	@CheckAttribute("double二维数组")
	private double[][] doubleArr2 ;

	public void init() {
		simpleMap.put(1, "Map一");
		simpleMap.put(2, "Map二");
		simpleMap.put(3, "Map三");
		simpleMap.put(4, "Map亖");
		simpleList.add("List1");
		simpleList.add("List2");
		simpleList.add("List3");
		simpleList.add("List4");
		array2[0] = new String[] { "array2_01", "array2_02" };
		array2[1] = new String[] { "array2_11", "array2_12" };

		array1[0] = "array1_0";
		array1[1] = "array1_1";

		stringValue = "I'm stringValue";
		intValue = 1;

		arrayValueMap.put("第一个", new String[] { "第一个A", "第一个B" });
		arrayValueMap.put("第二个", new String[] { "第二个A", "第二个B" });
		arrayValueMap.put("第三个", new String[] { "第三个A", "第三个B" });
		
		doubleArr1 = new double []{0.3,0.6};
		doubleArr2 = new double [2][];
		doubleArr2[0] = new double[]{0.1,0.2,0.3};
		doubleArr2[1] = new double[]{1.1,1.2,1.3};
	}
}
