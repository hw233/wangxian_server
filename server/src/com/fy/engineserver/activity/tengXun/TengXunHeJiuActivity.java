package com.fy.engineserver.activity.tengXun;

import com.fy.engineserver.activity.base.ExpAddAbstract;
import com.fy.engineserver.activity.base.ExpAddManager;
import com.fy.engineserver.activity.base.ExpAddTengXunHeJiu;

public class TengXunHeJiuActivity extends TengXunActivity {

	private int activityid;
	
	private int bili;
	
	private TengXunHeJiuActivity(long startTime_Long, long endTime_Long, String activityMsg) {
		super(startTime_Long, endTime_Long, activityMsg);
	}
	
	public TengXunHeJiuActivity(long startTime_Long, long endTime_Long, String activityMsg, int activityid, int bili) {
		super(startTime_Long, endTime_Long, activityMsg);
		this.activityid = activityid;
		this.bili = bili;
	}

	@Override
	protected void onStart() {
		ExpAddTengXunHeJiu activty = new ExpAddTengXunHeJiu(activityid, getStartTime_Long(), getEndTime_Long(), ExpAddAbstract.EXP_ADD_TYPE1, bili, 2);
		ExpAddManager.instance.addActivity(activty);
	}

	@Override
	protected void onEnd() {
		ExpAddManager.instance.removeActivity(activityid);
	}

	public void setBili(int bili) {
		this.bili = bili;
	}

	public int getBili() {
		return bili;
	}

}
