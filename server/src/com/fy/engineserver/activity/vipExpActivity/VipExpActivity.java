package com.fy.engineserver.activity.vipExpActivity;

import com.fy.engineserver.sprite.Player;
import com.fy.engineserver.util.config.ServerFit4Activity;

public class VipExpActivity {
	/** 只判断服务器是否开放  */
	private ServerFit4Activity serverfit;
	/** vip等级限制 */
	private int vipLevelLimit;
	/** 开始时间 */
	private long startTime;
	/** 结束时间 */
	private long endTime;
	/** 增加倍数   具体增加数值为(1+multiple)*baseNum */
	private double multiple;
	/** 类型（国服没用，韩服用来判断消费方式----消费方式如果为所有会顶替其他单个的消费方式） 
	 * 1:所有系统收取的银子vip经验都翻倍
	 * 2：商城消耗   3：圣兽阁  4：进入福地洞天   5：进入圣兽岛    6：祈福   7：宠物炼妖（消耗银子）
	 * */
	private int expenseType;
	
	@Override
	public String toString() {
		return "VipExpActivity [serverfit=" + serverfit + ", vipLevelLimit=" + vipLevelLimit + ", startTime=" + startTime + ", endTime=" + endTime + ", multiple=" + multiple + ", expenseType=" + expenseType + "]";
	}
	/**
	 * 
	 * @param player
	 * @return    null代表玩家符合活动规则
	 */
	public boolean isServerFit(Player player) {
		long currentTime = System.currentTimeMillis();
		if(currentTime < startTime || currentTime > endTime) {
			return false;
		}
		if(player.getVipLevel() < vipLevelLimit) {
			return false;
		}
		if(!serverfit.thiserverFit()) {
			return false;
		}
		return true;
	}
	
	public ServerFit4Activity getServerfit() {
		return serverfit;
	}
	public void setServerfit(ServerFit4Activity serverfit) {
		this.serverfit = serverfit;
	}
	public int getVipLevelLimit() {
		return vipLevelLimit;
	}
	public void setVipLevelLimit(int vipLevelLimit) {
		this.vipLevelLimit = vipLevelLimit;
	}
	public long getStartTime() {
		return startTime;
	}
	public void setStartTime(long startTime) {
		this.startTime = startTime;
	}
	public long getEndTime() {
		return endTime;
	}
	public void setEndTime(long endTime) {
		this.endTime = endTime;
	}
	public double getMultiple() {
		return multiple;
	}
	public void setMultiple(double multiple) {
		this.multiple = multiple;
	}
	public int getExpenseType() {
		return expenseType;
	}
	public void setExpenseType(int expenseType) {
		this.expenseType = expenseType;
	}
	
	
}
