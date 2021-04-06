package com.fy.engineserver.activity.base;

import com.fy.engineserver.activity.BaseActivityInstance;
import com.fy.engineserver.sprite.Player;

public class TimesActivity extends BaseActivityInstance{ 

	public int activityType;
	public int activityID;
	
	private int addNum;
	//玩家等级限制
	private int levellimit;
	
	public String getType() {
		if(activityType == 1) {
			return "祈福";
		} else if(activityType == 2) {
			return "喝酒";
		} else if(activityType == 3) {
			return "封魔录";
		}
		return "配置错误！";
	}
	
	public TimesActivity(String startTime, String endTime, String platForms, String openServerNames, String notOpenServers) throws Exception {
		super(startTime, endTime, platForms, openServerNames, notOpenServers);
	}
	
	public void setOtherVar(int activityID, int addNum, int activityType, int levellimit) {
		this.activityID = activityID;
		this.addNum = addNum;
		this.levellimit = levellimit;
		this.activityType = activityType;
	}
	
	public int getAddNum(Player player) {
		if (CanAdd(player)) {
			return addNum(player);
		}
		return 0;
	}
	
	public boolean CanAdd(Player player) {
		if(player.getLevel() < levellimit){
			return false;
		}
		return (this.isThisServerFit()==null);
	}
	
	protected int addNum(Player player){
		return getAddNum();
	}

	public int getLevellimit() {
		return levellimit;
	}

	public void setLevellimit(int levellimit) {
		this.levellimit = levellimit;
	}

	public int getAddNum() {
		return addNum;
	}
	public void setAddNum(int addNum) {
		this.addNum = addNum;
	}

	@Override
	public String getInfoShow() {
		// TODO Auto-generated method stub
		StringBuffer sb = new StringBuffer();
		sb.append("[活动类型:使用次数增加]");
		sb.append("[活动内容:" + this.getType() + " 次数增加 " + addNum + " 次] [等级限制:" + levellimit + "]");
		return sb.toString();
	}
	
}
