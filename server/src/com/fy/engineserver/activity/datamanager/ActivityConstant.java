package com.fy.engineserver.activity.datamanager;

import java.util.ArrayList;
import java.util.List;

public class ActivityConstant {
	public static final String 斩妖除魔 = "斩妖除魔";
	public static final String 采花大盗 = "采花大盗";
	public static final String 恶魔城堡 = "恶魔城堡";
	public static final String 空岛大冒险 = "空岛大冒险";
	public static final String 异界入侵 = "异界入侵";		
	public static final String 骰子仙居 = "骰子仙居";		
	public static final String 五方圣兽 = "五方圣兽";
	public static final String 神魂大乱斗 = "神魂大乱斗";
	
	public static int 按钮显示等级 = 0;
	
	public static void main(String[] args) {
		int[][] aa = new int[][]{{11,30},{11,00}};
		List<String> result = new ArrayList<String>();
		for (int i=0; i<aa.length; i++) {
			result.add(aa[i][0] + ":" + aa[i][1]);
		}
		System.out.println(result);
	}
}
