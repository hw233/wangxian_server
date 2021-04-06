package com.fy.engineserver.soulpith.module;
/**
 * 吞噬消耗银子数量
 * 
 * @date on create 2016年4月26日 下午4:11:08
 */
public class DevourCostModule {
	private int soulLevel;
	
	private int colorType;
	
	private long bindSilver;

	public int getSoulLevel() {
		return soulLevel;
	}

	public void setSoulLevel(int soulLevel) {
		this.soulLevel = soulLevel;
	}

	public int getColorType() {
		return colorType;
	}

	public void setColorType(int colorType) {
		this.colorType = colorType;
	}

	public long getBindSilver() {
		return bindSilver;
	}

	public void setBindSilver(long bindSilver) {
		this.bindSilver = bindSilver;
	}
}
