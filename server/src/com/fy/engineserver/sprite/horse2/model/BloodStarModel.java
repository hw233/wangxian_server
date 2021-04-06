package com.fy.engineserver.sprite.horse2.model;

public class BloodStarModel {
	/** 星级 (0开始)*/
	private int startLevel;
	/** 血脉数值比率(血脉星数值=坐骑完美规划数值*10%*次数) */
	private double bloodDataRate;
	/** 升级所需血脉值 */
	private int needBloodNum;
	
	@Override
	public String toString() {
		return "BloodStarModel [startLevel=" + startLevel + ", bloodDataRate=" + bloodDataRate + ", needBloodNum=" + needBloodNum + "]";
	}
	public int getStartLevel() {
		return startLevel;
	}
	public void setStartLevel(int startLevel) {
		this.startLevel = startLevel;
	}
	public int getNeedBloodNum() {
		return needBloodNum;
	}
	public void setNeedBloodNum(int needBloodNum) {
		this.needBloodNum = needBloodNum;
	}
	public double getBloodDataRate() {
		return bloodDataRate;
	}
	public void setBloodDataRate(double bloodDataRate) {
		this.bloodDataRate = bloodDataRate;
	}
	
	

}
