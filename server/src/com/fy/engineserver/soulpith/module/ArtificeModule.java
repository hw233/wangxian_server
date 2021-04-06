package com.fy.engineserver.soulpith.module;

import com.fy.engineserver.util.SimpleKey;

/**
 * 灵髓炼化
 * 
 * @date on create 2016年3月23日 上午10:58:13
 */
public class ArtificeModule {
	@SimpleKey
	private int colortype;
	
	private int[] resultColor;
	
	private int[] probablitys;

	public int getColortype() {
		return colortype;
	}

	public void setColortype(int colortype) {
		this.colortype = colortype;
	}

	public int[] getProbablitys() {
		return probablitys;
	}

	public void setProbablitys(int[] probablitys) {
		this.probablitys = probablitys;
	}

	public int[] getResultColor() {
		return resultColor;
	}

	public void setResultColor(int[] resultColor) {
		this.resultColor = resultColor;
	}
}
