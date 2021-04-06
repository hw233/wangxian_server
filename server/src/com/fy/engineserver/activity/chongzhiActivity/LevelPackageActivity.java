package com.fy.engineserver.activity.chongzhiActivity;


import java.util.Arrays;

import com.fy.engineserver.activity.BaseActivityInstance;
import com.fy.engineserver.sprite.Player;

public class LevelPackageActivity extends BaseActivityInstance{

	private String shopName;
	private int lowLevel = 0;
	private int maxLevel = 0;
	private String [] goodsname;	
	private int[] moneys;
	
	public LevelPackageActivity(String startTime, String endTime, String platForms, String openServerNames, String notOpenServers) throws Exception {
		super(startTime, endTime, platForms, openServerNames, notOpenServers);
	}
	
	public void setArgs(String shopName,int lowLevel,int maxLevel,String [] goodsname,int[] moneys){
		this.shopName = shopName;
		this.lowLevel = lowLevel;
		this.maxLevel = maxLevel;
		this.goodsname = goodsname;
		this.moneys = moneys;
	}
	
	public boolean isEffectActivity(Player player){
		if(this.isThisServerFit() == null){
			return true;
		}
		return false;
	}

	@Override
	public String getInfoShow() {
		return "";
	}

	public String getShopName() {
		return this.shopName;
	}

	public void setShopName(String shopName) {
		this.shopName = shopName;
	}

	public int getLowLevel() {
		return this.lowLevel;
	}

	public void setLowLevel(int lowLevel) {
		this.lowLevel = lowLevel;
	}

	public int getMaxLevel() {
		return this.maxLevel;
	}

	public void setMaxLevel(int maxLevel) {
		this.maxLevel = maxLevel;
	}

	public String[] getGoodsname() {
		return this.goodsname;
	}

	public void setGoodsname(String[] goodsname) {
		this.goodsname = goodsname;
	}

	public int[] getMoneys() {
		return this.moneys;
	}

	public void setMoneys(int[] moneys) {
		this.moneys = moneys;
	}

	@Override
	public String toString() {
		return "LevelPackageActivity [shopName=" + this.shopName + ", lowLevel=" + this.lowLevel + ", maxLevel=" + this.maxLevel + ", goodsname=" + Arrays.toString(this.goodsname) + ", moneys=" + Arrays.toString(this.moneys) +  "]";
	}

}
