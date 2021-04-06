package com.fy.engineserver.sprite.pet;

import java.util.Arrays;

public class PetEatProp2Rule {

	private String propName;
	private int basicAddPoint;
	private int [] baoJiAddPoints;
	private int [] baoJiNums;
	
	public String getPropName() {
		return propName;
	}
	public void setPropName(String propName) {
		this.propName = propName;
	}
	public int getBasicAddPoint() {
		return basicAddPoint;
	}
	public void setBasicAddPoint(int basicAddPoint) {
		this.basicAddPoint = basicAddPoint;
	}
	public int[] getBaoJiAddPoints() {
		return baoJiAddPoints;
	}
	public void setBaoJiAddPoints(int[] baoJiAddPoints) {
		this.baoJiAddPoints = baoJiAddPoints;
	}
	public int[] getBaoJiNums() {
		return baoJiNums;
	}
	public void setBaoJiNums(int[] baoJiNums) {
		this.baoJiNums = baoJiNums;
	}
	
	@Override
	public String toString() {
		return "PetEatProp2Rule [baoJiAddPoints="
				+ Arrays.toString(baoJiAddPoints) + ", baoJiNums="
				+ Arrays.toString(baoJiNums) + ", basicAddPoint="
				+ basicAddPoint + ", propName=" + propName + "]";
	}
	
}
