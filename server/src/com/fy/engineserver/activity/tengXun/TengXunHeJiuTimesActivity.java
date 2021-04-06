package com.fy.engineserver.activity.tengXun;

import com.fy.engineserver.activity.ActivitySubSystem;
import com.fy.engineserver.activity.base.TimesActivityManager;
import com.fy.engineserver.activity.base.TimesActivityTengXun;
import com.fy.engineserver.util.TimeTool;

public class TengXunHeJiuTimesActivity extends TengXunActivity {

	private int addNum;
	
	private TengXunHeJiuTimesActivity(long startTime_Long, long endTime_Long, String activityMsg) {
		super(startTime_Long, endTime_Long, activityMsg);
	}
	
	public TengXunHeJiuTimesActivity(long startTime_Long, long endTime_Long, String activityMsg, int num) {
		super(startTime_Long, endTime_Long, activityMsg);
		this.addNum = num;
	}

	@Override
	protected void onStart() {
		String st = TimeTool.formatter.varChar19.format(getStartTime_Long());
		String et = TimeTool.formatter.varChar19.format(getEndTime_Long());
		TimesActivityTengXun tengxun;
		try {
			tengxun = new TimesActivityTengXun(st, et, "tencent", "", "");
			tengxun.setOtherVar(110000, addNum, TimesActivityManager.HEJIU_ACTIVITY, 0);
			TimesActivityManager.instacen.addActivity(tengxun);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			ActivitySubSystem.logger.error("[腾讯活动] [TengXunHeJiuTimesActivity报错]", e);
		}
//		TimesActivityTengXun tengxun = new TimesActivityTengXun(110000, getStartTime_Long(), getEndTime_Long(), addNum, TimesActivityManager.HEJIU_ACTIVITY);
	}

	@Override
	protected void onEnd() {
		TimesActivityManager.instacen.removeActivity(110000);
	}

	public void setAddNum(int addNum) {
		this.addNum = addNum;
	}

	public int getAddNum() {
		return addNum;
	}

}
