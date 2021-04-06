package com.fy.engineserver.activity.chongzhiActivity;

import java.util.Arrays;

import com.fy.engineserver.activity.BaseActivityInstance;
import com.fy.engineserver.sprite.Player;

public class MonthCardActivity extends BaseActivityInstance{

	private String cardName;
	private String cardId;
	private int chargeMoney;
	private String [] chargeRewardNames;	//立返奖励名
	private int [] chargeRewardColors;		
	private int [] chargeRewardCounts;		
	private String [] dayRewardNames;		//日返奖励名
	private int [] dayRewardColors;		
	private int [] dayRewardCounts;		
	private String dayRewardInfo;
	private String getRewardSuccInfo;		//领取日返成功后的提示
	private int levelLimit;
	private int lastDays;					//该卡可持续天数
	
	
	public MonthCardActivity(String startTime, String endTime, String platForms, String openServerNames, String notOpenServers) throws Exception {
		super(startTime, endTime, platForms, openServerNames, notOpenServers);
	}
	
	public void setArgs(String cardName,String cardId,int chargeMoney,String [] chargeRewardNames,int[] chargeRewardColors,int [] chargeRewardCounts,String [] dayRewardNames,int [] dayRewardColors,int [] dayRewardCounts,String dayRewardInfo,String getRewardSuccInfo,int levelLimit){
		this.cardName = cardName;
		this.cardId = cardId;
		this.chargeMoney = chargeMoney;
		this.chargeRewardNames = chargeRewardNames;
		this.chargeRewardColors = chargeRewardColors;
		this.chargeRewardCounts = chargeRewardCounts;
		this.dayRewardNames = dayRewardNames;
		this.dayRewardInfo = dayRewardInfo;
		this.getRewardSuccInfo = getRewardSuccInfo;
		this.dayRewardColors = dayRewardColors;
		this.dayRewardCounts = dayRewardCounts;
		this.levelLimit  = levelLimit;
	}
	
	public boolean isEffectActivity(Player player){
		if(this.isThisServerFit() == null && player.getLevel() >= levelLimit){
			return true;
		}
		return false;
	}

	@Override
	public String getInfoShow() {
		return this.toString();
	}

	public String getCardName() {
		return this.cardName;
	}

	public void setCardName(String cardName) {
		this.cardName = cardName;
	}

	public int getChargeMoney() {
		return this.chargeMoney;
	}

	public void setChargeMoney(int chargeMoney) {
		this.chargeMoney = chargeMoney;
	}

	public String[] getChargeRewardNames() {
		return this.chargeRewardNames;
	}

	public void setChargeRewardNames(String[] chargeRewardNames) {
		this.chargeRewardNames = chargeRewardNames;
	}

	public int[] getChargeRewardColors() {
		return this.chargeRewardColors;
	}

	public void setChargeRewardColors(int[] chargeRewardColors) {
		this.chargeRewardColors = chargeRewardColors;
	}

	public int[] getChargeRewardCounts() {
		return this.chargeRewardCounts;
	}

	public void setChargeRewardCounts(int[] chargeRewardCounts) {
		this.chargeRewardCounts = chargeRewardCounts;
	}

	public String[] getDayRewardNames() {
		return this.dayRewardNames;
	}

	public void setDayRewardNames(String[] dayRewardNames) {
		this.dayRewardNames = dayRewardNames;
	}

	public String getDayRewardInfo() {
		return this.dayRewardInfo;
	}

	public void setDayRewardInfo(String dayRewardInfo) {
		this.dayRewardInfo = dayRewardInfo;
	}

	public String getGetRewardSuccInfo() {
		return this.getRewardSuccInfo;
	}

	public int[] getDayRewardColors() {
		return this.dayRewardColors;
	}

	public void setDayRewardColors(int[] dayRewardColors) {
		this.dayRewardColors = dayRewardColors;
	}

	public int[] getDayRewardCounts() {
		return this.dayRewardCounts;
	}

	public void setDayRewardCounts(int[] dayRewardCounts) {
		this.dayRewardCounts = dayRewardCounts;
	}

	public String getCardId() {
		return this.cardId;
	}

	public void setCardId(String cardId) {
		this.cardId = cardId;
	}

	public int getLevelLimit() {
		return this.levelLimit;
	}

	public void setLevelLimit(int levelLimit) {
		this.levelLimit = levelLimit;
	}

	public int getLastDays() {
		return this.lastDays;
	}

	public void setLastDays(int lastDays) {
		this.lastDays = lastDays;
	}

	public void setGetRewardSuccInfo(String getRewardSuccInfo) {
		this.getRewardSuccInfo = getRewardSuccInfo;
	}
//	{
//		//datarecord	
//		long pid;
//		String cardName;
//		String cardName_stat;
//		long lastgettime;
//		
//		//客户端请求界面信息
//		String cardname = req.getCardName();
//		if(cardname == null || cardname.isEmpty()){
//			//获取玩家有剩余天数的卡
//			//有的话获取cardname,获取config
//			//没有的话从有效的配置里随便获取一条给客户端
//			
//		}
//		
//		if(有剩余天数){
//			//返回数据
//			
//		}else {
//			if(this.isEffectActivity()){
//				//返回数据
//				
//			}else{
//				//活动还没开启
//			}
//		}
//		//领取奖励
//		
//		//购买
//		
//	}

	@Override
	public String toString() {
		return "MonthCardActivity [cardName=" + this.cardName + ", cardId=" + this.cardId + ", chargeMoney=" + this.chargeMoney + ", chargeRewardNames=" + Arrays.toString(this.chargeRewardNames) + ", chargeRewardColors=" + Arrays.toString(this.chargeRewardColors) + ", chargeRewardCounts=" + Arrays.toString(this.chargeRewardCounts) + ", dayRewardNames=" + Arrays.toString(this.dayRewardNames) + ", dayRewardColors=" + Arrays.toString(this.dayRewardColors) + ", dayRewardCounts=" + Arrays.toString(this.dayRewardCounts) + ", dayRewardInfo=" + this.dayRewardInfo + ", getRewardSuccInfo=" + this.getRewardSuccInfo + ", levelLimit=" + this.levelLimit + "]";
	}


}
