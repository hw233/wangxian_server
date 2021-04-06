package com.fy.engineserver.activity.bloodrate;

import java.util.Arrays;

import com.fy.engineserver.activity.BaseActivityInstance;

public class BloodRateAct extends BaseActivityInstance{
	/** 放生宠物稀有度 */
	private byte[] rarity;
	/** 对应增加倍数 */
	private double[] muls;
	
	public BloodRateAct(String startTime, String endTime, String platForms, String openServerNames, String notOpenServers) throws Exception {
		super(startTime, endTime, platForms, openServerNames, notOpenServers);
		// TODO Auto-generated constructor stub
	}
	
	public double getRate(byte rarit) {
		if (rarity == null || rarity.length <= 0) {
			return 0;
		}
		for (int i=0; i<rarity.length; i++) {
			if (rarity[i] == rarit) {
				return muls[i];
			}
		}
		return 0;
	}
	
	@Override
	public String toString() {
		return "BloodRateAct [rarity=" + Arrays.toString(rarity) + ", muls=" + Arrays.toString(muls) + "]";
	}
	
	public void setOtherVar(byte[] rarity, double[] muls) {
		this.rarity = rarity;
		this.muls = muls;
	}

	@Override
	public String getInfoShow() {
		// TODO Auto-generated method stub
		StringBuffer sb = new StringBuffer();
		sb.append("[放生宠物获得血脉增加活动]<br>");
		for (int i=0; i<rarity.length; i++) {
			sb.append("[放生宠物稀有度:" + rarity[i] + "，获得血脉额外倍数:" + muls[i] + "]<br>");
		}
		return sb.toString();
	}

	public byte[] getRarity() {
		return rarity;
	}

	public void setRarity(byte[] rarity) {
		this.rarity = rarity;
	}

	public double[] getMuls() {
		return muls;
	}

	public void setMuls(double[] muls) {
		this.muls = muls;
	}

	
}
