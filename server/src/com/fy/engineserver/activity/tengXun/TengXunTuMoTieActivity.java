package com.fy.engineserver.activity.tengXun;

import com.fy.engineserver.activity.base.ExpAddAbstract;
import com.fy.engineserver.activity.base.ExpAddManager;
import com.fy.engineserver.activity.base.ExpAddTuMoTieTengXun;

public class TengXunTuMoTieActivity extends TengXunActivity {

	private int activityid;
	
	private int bili;
	
	private TengXunTuMoTieActivity(long startTime_Long, long endTime_Long, String activityMsg) {
		super(startTime_Long, endTime_Long, activityMsg);
	}
	
	public TengXunTuMoTieActivity(long startTime_Long, long endTime_Long, String activityMsg, int activityid, int bili) {
		super(startTime_Long, endTime_Long, activityMsg);
		this.activityid = activityid;
		this.bili = bili;
	}

	@Override
	protected void onStart() {
		ExpAddTuMoTieTengXun expActivity = new ExpAddTuMoTieTengXun(activityid, getStartTime_Long(), getEndTime_Long(), ExpAddAbstract.EXP_ADD_TYPE1, bili, 2);
		ExpAddManager.instance.addActivity(expActivity);
	}

	@Override
	protected void onEnd() {
		ExpAddManager.instance.removeActivity(activityid);
	}

	public void setActivityid(int activityid) {
		this.activityid = activityid;
	}

	public int getActivityid() {
		return activityid;
	}

	public void setBili(int bili) {
		this.bili = bili;
	}

	public int getBili() {
		return bili;
	}

}
