package com.fy.engineserver.newBillboard;

import java.io.Serializable;


public class BillboardDate implements Serializable{

	//数据类型
	public static int 玩家 = 0;
	public static int 宠物 = 1;
	public static int 家族 = 2;
	public static int 洞府 = 3;
	public static int 国家 = 4;
	public static int 其他 = 10;
	
	//类型玩家  宠物，家族
	private int type;
	//各种id 玩家，宠物，家族
	private long dateId;
	
	//于title对应
	private String[] dateValues = new String[]{};

	private long rewardHonorTime;
	
	/*******************get set****************************/
	public int getType() {
		return type;
	}

	public void setType(int type) {
		this.type = type;
	}

	public long getDateId() {
		return dateId;
	}

	public void setDateId(long dateId) {
		this.dateId = dateId;
	}

	public String[] getDateValues() {
		return dateValues;
	}

	public void setDateValues(String[] dateValues) {
		this.dateValues = dateValues;
	}

	public long getRewardHonorTime() {
		return rewardHonorTime;
	}

	public void setRewardHonorTime(long rewardHonorTime) {
		this.rewardHonorTime = rewardHonorTime;
	}

}
