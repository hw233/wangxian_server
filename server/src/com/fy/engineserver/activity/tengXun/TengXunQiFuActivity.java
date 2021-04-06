package com.fy.engineserver.activity.tengXun;

import com.fy.engineserver.activity.ActivitySubSystem;
import com.fy.engineserver.activity.base.TimesActivityManager;
import com.fy.engineserver.activity.base.TimesActivityTengXun;
import com.fy.engineserver.util.TimeTool;

public class TengXunQiFuActivity extends TengXunActivity {

	private int addNum;
	
	private TengXunQiFuActivity(long startTime_Long, long endTime_Long, String activityMsg) {
		super(startTime_Long, endTime_Long, activityMsg);
	}
	
	public TengXunQiFuActivity(long startTime_Long, long endTime_Long, String activityMsg, int num) {
		super(startTime_Long, endTime_Long, activityMsg);
		this.addNum = num;
	}

	@Override
	protected void onStart() {
//		TimesActivityTengXun tengxun = new TimesActivityTengXun(100000, getStartTime_Long(), getEndTime_Long(), addNum, TimesActivityManager.QIFU_ACTIVITY);
//		TimesActivityManager.instacen.addActivity(tengxun);
		String st = TimeTool.formatter.varChar19.format(getStartTime_Long());
		String et = TimeTool.formatter.varChar19.format(getEndTime_Long());
		TimesActivityTengXun tengxun;
		try {
			tengxun = new TimesActivityTengXun(st, et, "tencent", "", "");
			tengxun.setOtherVar(100000, addNum, TimesActivityManager.QIFU_ACTIVITY, 0);
			TimesActivityManager.instacen.addActivity(tengxun);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			ActivitySubSystem.logger.error("[腾讯活动] [TengXunHeJiuTimesActivity报错]", e);
		}
	}

	@Override
	protected void onEnd() {
		TimesActivityManager.instacen.removeActivity(100000);
	}

	public void setAddNum(int addNum) {
		this.addNum = addNum;
	}

	public int getAddNum() {
		return addNum;
	}

}
