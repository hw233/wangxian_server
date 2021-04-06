package com.fy.engineserver.carbon.devilSquare.activity;

import java.util.Arrays;

import com.fy.engineserver.activity.BaseActivityInstance;

public class ExtraDevilOpenTimesActivity extends BaseActivityInstance{
	
	private int[] addTimeHours;

	public ExtraDevilOpenTimesActivity(String startTime, String endTime, String platForms, String openServerNames, String notOpenServers) throws Exception {
		super(startTime, endTime, platForms, openServerNames, notOpenServers);
		// TODO Auto-generated constructor stub
	}
	
	public void setOtherVar(int[] addTimeHours) {
		this.addTimeHours = addTimeHours;
	}

	@Override
	public String toString() {
		return "ExtraDevilOpenTimesActivity [addTimeHours=" + Arrays.toString(addTimeHours) + "]";
	}

	@Override
	public String getInfoShow() {
		// TODO Auto-generated method stub
		StringBuffer sb = new StringBuffer();
		sb.append("[额外增加恶魔城堡开放]");
		sb.append("[额外增加次数:" + getAddTimeHours().length + "]");
		sb.append("[额外开放时间 :");
		for(int i=0; i<getAddTimeHours().length; i++) {
			sb.append(getAddTimeHours()[i] + "点");
			if(i < (getAddTimeHours().length - 1)) {
				sb.append("，");
			}
		}
		return sb.toString();
	}

	public int[] getAddTimeHours() {
		return addTimeHours;
	}

	public void setAddTimeHours(int[] addTimeHours) {
		this.addTimeHours = addTimeHours;
	}


}
