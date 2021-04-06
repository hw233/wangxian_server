package com.fy.engineserver.activity.wafen.model;

import com.fy.engineserver.activity.BaseActivityInstance;

public class WaFenActivity extends BaseActivityInstance{

	public WaFenActivity(String startTime, String endTime, String platForms, String openServerNames, String notOpenServers) throws Exception {
		super(startTime, endTime, platForms, openServerNames, notOpenServers);
		// TODO Auto-generated constructor stub
	}
	

	@Override
	public String getInfoShow() {
		// TODO Auto-generated method stub
		StringBuffer sb = new StringBuffer();
		sb.append("挖坟");
		return sb.toString();
	}

}
