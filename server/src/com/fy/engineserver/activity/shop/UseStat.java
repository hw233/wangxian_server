package com.fy.engineserver.activity.shop;

import java.io.Serializable;

public class UseStat implements Serializable{
	public int [] colorvalue;
	public UseStat(){}
	public UseStat(int []colorvalue){
		this.colorvalue = colorvalue;
	}
	public int[] getColorvalue() {
		return colorvalue;
	}
	public void setColorvalue(int[] colorvalue) {
		this.colorvalue = colorvalue;
	}
}
