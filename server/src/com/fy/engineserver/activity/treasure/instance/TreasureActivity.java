package com.fy.engineserver.activity.treasure.instance;

import com.fy.engineserver.activity.BaseActivityInstance;

/**
 * 极地寻宝获得 (此处只管活动开启结束时间，具体逻辑单独做)
 * @author Administrator
 *
 */
public class TreasureActivity extends BaseActivityInstance{

	public TreasureActivity(String startTime, String endTime, String platForms, String openServerNames, String notOpenServers) throws Exception {
		super(startTime, endTime, platForms, openServerNames, notOpenServers);
		// TODO Auto-generated constructor stub
	}

	@Override
	public String getInfoShow() {
		// TODO Auto-generated method stub
		return null;
	}

}
