package com.fy.engineserver.soulpith;

import java.util.Arrays;

/**
 * 灵髓静态数据
 * 
 * @date on create 2016年3月16日 下午3:11:26
 */
public class SoulPithConstant {
	/** 灵髓功能开启等级 */
	public static int openLevel = 150;
	/** 灵髓最大镶嵌个数 */
	public static final int MAX_FILL_NUM = 24;
	/** 炼化消耗银子数量 */
	public static long ARTIFICE_COST = 100000;
	/** 吞噬消耗银两 */
	public static long DEVOUR_COST = 400000;
	/** 等级对应的灵髓颜色 */
	public static int[] leve_color_type = new int[]{0,1,2,3,3,3,4,4,4,4};
	/** 灵髓最大等级 */
	public static final int SOUL_MAX_LEVEL = 9;
	/** 炼化需要消耗的灵髓个数 */
	public static final int ARTIFICE_NEED_NUM = 4;
	
	public static String[] artifice_articleCNNName = new String[]{"天灵髓", "地灵髓", "仙灵髓", "神灵髓", "人灵髓", "魔灵髓", "妖灵髓", "鬼灵髓"};
	/** 基础数值对应的颜色加成比例 */
	public static final double[] COLOR_RATE = new double[]{0.5, 0.5, 0.7, 1, 1.5, 1.5, 1.5, 1.5};
	/** 仙气葫芦道具名 */
	public static String[] gourdNames = new String[0];
	
	public static int[] playerLevels = new int[]{190, 200, 210, 220, 230, 240};
	public static int[] openSoulNums = new int[]{4, 8, 12, 16, 20, MAX_FILL_NUM};
	
	public static void main(String[] args) {
		int[] aa = new int[SoulPithConstant.COLOR_RATE.length];
		for (int i=0; i<aa.length; i++) {
			aa[i] = (int) (SoulPithConstant.COLOR_RATE[i] * 100);
		}
		System.out.println(Arrays.toString(aa));
	}
}
