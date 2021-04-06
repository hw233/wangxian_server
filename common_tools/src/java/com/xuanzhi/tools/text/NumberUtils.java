package com.xuanzhi.tools.text;

import java.math.BigDecimal;

public class NumberUtils {
	
	/**
	 * 截取小数
	 * @param f
	 * @param num
	 * @return
	 */
	public static float cutFloat(float f, int num) {
		int scale = num;  
		int roundingMode = 4;  
		BigDecimal bd = new BigDecimal((double)f);  
		bd = bd.setScale(scale,roundingMode);  
		f = bd.floatValue(); 
		return f;
	}
	
	/**
	 * 四舍五入
	 * @param v
	 * @param scale 保留的小数点位数
	 * @return
	 */
	public static float round(float v, int scale) {
		if (scale < 0)
			return v;
		String temp = "#####0.";
		for (int i = 0; i < scale; i++) {
			temp += "0";
		}
		return Float.valueOf(new java.text.DecimalFormat(temp).format(v));
	}
	
	public static void main(String args[]) {
		long startPrice = 10;
		long count = 30;
		int oddprice = (int)NumberUtils.round(new Float(startPrice)/new Float(count), 0);
		System.out.println(oddprice);
	}
	
	/**
	 * 获得两个值间的随机值
	 * @param v1
	 * @param v2
	 * @param open 全开还是前开后闭
	 * @return
	 */
	public static int getRandomRangeValue(int v1, int v2, boolean open) {
		if(v1 == v2) {
			return v1;
		} else if(v1 > v2) {
			int m = v1;
			v1 = v2;
			v2 = m;
		}
		if(open) {
			return v1 + new java.util.Random().nextInt(v2-v1+1);
		} else {
			return v1 + new java.util.Random().nextInt(v2-v1);
		}
	}
}

