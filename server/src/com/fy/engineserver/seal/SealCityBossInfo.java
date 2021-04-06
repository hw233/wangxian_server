package com.fy.engineserver.seal;

import java.util.Arrays;

public class SealCityBossInfo {

	public int bossId;
	public int [] xyPoints;
	public int sealLevel;	//封印任务等级
	public int sealLayer;	//封印任务阶段
	public long cutSealTime; //缩短封印的时间
	public String needMaterialName;	//上交的材料名
	public String resultMess;	//帮助信息
	public long allPoints;	
	public int bossids[];	//220级第2阶段，需要根据捐献材料的多少分配不同的boss
	public int buffid;	//根据捐献材料多少分配不同buff
	public String title;
	public String content;
	public SealRewardArticleInfo articleInfo;
	public SealCityBossInfo(){}
	public SealCityBossInfo(int bossId,int [] xyPoints,int sealLevel,int sealLayer,long cutSealTime){
		this.bossId = bossId;
		this.xyPoints = xyPoints;
		this.sealLevel = sealLevel;
		this.sealLayer = sealLayer;
		this.cutSealTime = cutSealTime;
	}
	@Override
	public String toString() {
		return "SealCityBossInfo [bossId=" + bossId + ", xyPoints=" + Arrays.toString(xyPoints) + ", sealLevel=" + sealLevel + ", sealLayer=" + sealLayer + "]";
	}
}
