package com.fy.engineserver.activity.jiazu;

import com.fy.engineserver.activity.BaseActivityInstance;

public class RefreshTaskActivity extends BaseActivityInstance{
	
	private long costMoney;

	public RefreshTaskActivity(String startTime, String endTime, String platForms, String openServerNames, String notOpenServers) throws Exception {
		super(startTime, endTime, platForms, openServerNames, notOpenServers);
		// TODO Auto-generated constructor stub
	}
	
	
	@Override
	public String toString() {
		return "RefreshTaskActivity [costMoney=" + costMoney + "]";
	}



	public void initOtherVar(String cost) {
		this.costMoney = Long.parseLong(cost);
	}

	@Override
	public String getInfoShow() {
		// TODO Auto-generated method stub
		StringBuffer sb = new StringBuffer();
		sb.append("[刷新家族任务动] [每次刷新消耗银子:" + costMoney + "]");	
		return sb.toString();
	}

	public long getCostMoney() {
		return costMoney;
	}

	public void setCostMoney(long costMoney) {
		this.costMoney = costMoney;
	}

}
