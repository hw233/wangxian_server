package com.fy.engineserver.soulpith.module;

public class SoulInfo4Client {
	/** 灵髓宝石类型 */
	private String soulType;
	/** 宝石等级 */
	private int soulLevel;
	/** 属性类型 */
	private int[] propTypes;
	/** 属性值 */
	private int[] propNums;
	/** 增加的灵髓点 */
	private int[] soulNums;
	
	public String getSoulType() {
		return soulType;
	}
	public void setSoulType(String soulType) {
		this.soulType = soulType;
	}
	public int getSoulLevel() {
		return soulLevel;
	}
	public void setSoulLevel(int soulLevel) {
		this.soulLevel = soulLevel;
	}
	public int[] getPropTypes() {
		return propTypes;
	}
	public void setPropTypes(int[] propTypes) {
		this.propTypes = propTypes;
	}
	public int[] getPropNums() {
		return propNums;
	}
	public void setPropNums(int[] propNums) {
		this.propNums = propNums;
	}
	public int[] getSoulNums() {
		return soulNums;
	}
	public void setSoulNums(int[] soulNums) {
		this.soulNums = soulNums;
	}
	
}
