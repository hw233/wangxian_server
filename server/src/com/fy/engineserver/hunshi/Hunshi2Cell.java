package com.fy.engineserver.hunshi;

import com.fy.engineserver.datasource.language.Translate;
import com.fy.engineserver.economic.BillingCenter;

/**
 * 套装魂石面板上的格子
 * 
 * 
 */
public class Hunshi2Cell {
	private int index;// 格子位置0-15
	private long hunshiId;// 镶嵌套装石的id，未镶嵌的时候为-1
	private int openType;// 格子开启的条件类型：0-默认开启，1-玩家等级，2-玩家等级+消耗银子
	private boolean open;// 是否已开启
	private String des;// 提示开启条件的描述

	public Hunshi2Cell() {
	}

	public Hunshi2Cell(int index) {
		this.index = index;
		this.hunshiId = -1;
		HunshiManager hm = HunshiManager.getInstance();
		HunshiXiangQian xq = hm.openHole2.get(index);
		if (xq == null) {
			return;
		}
		if (index < 4) {
			this.openType = 0;
			this.open = true;
			this.des = "不需要描述";
		}  else if (index >= 4 && index <= 15) {
			this.openType = 2;
			this.open = false;
			this.des = Translate.translateString(Translate.人物N级付费开启, new String[][] { { Translate.STRING_1, xq.getPlayerLevel() + "" }, { Translate.STRING_2, BillingCenter.得到带单位的银两(xq.getNeedSilver()) } });
		} else {
			this.des = "不需要描述1";
		}
	}

	public long getIndex() {
		return index;
	}

	public void setIndex(int index) {
		this.index = index;
	}

	public long getHunshiId() {
		return hunshiId;
	}

	public void setHunshiId(long hunshiId) {
		this.hunshiId = hunshiId;
	}

	public int getOpenType() {
		return openType;
	}

	public void setOpenType(int openType) {
		this.openType = openType;
	}

	public boolean isOpen() {
		return open;
	}

	public void setOpen(boolean open) {
		this.open = open;
	}

	public String getDes() {
		return des;
	}

	public void setDes(String des) {
		this.des = des;
	}

}
