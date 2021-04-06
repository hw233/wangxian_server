package com.fy.engineserver.hunshi;

import com.fy.engineserver.util.SimpleKey;

public class HunshiPropValue {
	@SimpleKey
	private int propId;
	private int[] value0; // 白色魂石/套装魂石的属性值区间
	private int[] value1;
	private int[] value2;
	private int[] value3;
	private int[] value4;
	private int[] value5;// 金色魂石/套装魂石的属性值区间

	public HunshiPropValue(int propId, int[] value0, int[] value1, int[] value2, int[] value3, int[] value4, int[] value5) {
		this.propId = propId;
		this.value0 = value0;
		this.value1 = value1;
		this.value2 = value2;
		this.value3 = value3;
		this.value4 = value4;
		this.value5 = value5;
	}
	
	public int[] getPropValue(int color){
		int[] propRange = new int[2];
		switch (color) {
		case 0:
			propRange = value0;
			break;
		case 1:
			propRange = value1;
			break;
		case 2:
			propRange = value2;
			break;
		case 3:
			propRange = value3;
			break;
		case 4:
			propRange = value4;
			break;
		case 5:
			propRange = value5;
			break;
		default:
			propRange = value0;
			break;
		}
		return propRange;
	}

	public HunshiPropValue() {
	}

	public int getPropId() {
		return propId;
	}

	public void setPropId(int propId) {
		this.propId = propId;
	}

	public int[] getValue0() {
		return value0;
	}

	public void setValue0(int[] value0) {
		this.value0 = value0;
	}

	public int[] getValue1() {
		return value1;
	}

	public void setValue1(int[] value1) {
		this.value1 = value1;
	}

	public int[] getValue2() {
		return value2;
	}

	public void setValue2(int[] value2) {
		this.value2 = value2;
	}

	public int[] getValue3() {
		return value3;
	}

	public void setValue3(int[] value3) {
		this.value3 = value3;
	}

	public int[] getValue4() {
		return value4;
	}

	public void setValue4(int[] value4) {
		this.value4 = value4;
	}

	public int[] getValue5() {
		return value5;
	}

	public void setValue5(int[] value5) {
		this.value5 = value5;
	}

}
