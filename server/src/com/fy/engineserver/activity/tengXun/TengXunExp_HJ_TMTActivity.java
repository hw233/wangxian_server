package com.fy.engineserver.activity.tengXun;

import com.fy.engineserver.activity.base.ExpAddAbstract;
import com.fy.engineserver.activity.base.ExpAddManager;
import com.fy.engineserver.activity.base.ExpAddTengXunHeJiu;
import com.fy.engineserver.activity.base.ExpAddTuMoTieTengXun;

public class TengXunExp_HJ_TMTActivity extends TengXunActivity {

	private int activityid1;
	private int activityid2;
	
	private int bili1;
	private int bili2;
	
	private TengXunExp_HJ_TMTActivity(long startTime_Long, long endTime_Long, String activityMsg) {
		super(startTime_Long, endTime_Long, activityMsg);
	}
	
	public TengXunExp_HJ_TMTActivity(long startTime_Long, long endTime_Long, String activityMsg, int activityid1, int bili1, int activityid2, int bili2) {
		super(startTime_Long, endTime_Long, activityMsg);
		this.activityid1 = activityid1;
		this.bili1 = bili1;
		this.activityid2 = activityid2;
		this.bili2 = bili2;
	}

	@Override
	protected void onStart() {
		ExpAddTengXunHeJiu activty = new ExpAddTengXunHeJiu(activityid1, getStartTime_Long(), getEndTime_Long(), ExpAddAbstract.EXP_ADD_TYPE1, bili1, 2);
		ExpAddManager.instance.addActivity(activty);
		ExpAddTuMoTieTengXun expActivity = new ExpAddTuMoTieTengXun(activityid2, getStartTime_Long(), getEndTime_Long(), ExpAddAbstract.EXP_ADD_TYPE1, bili2, 2);
		ExpAddManager.instance.addActivity(expActivity);
	}

	@Override
	protected void onEnd() {
		ExpAddManager.instance.removeActivity(activityid1);
		ExpAddManager.instance.removeActivity(activityid2);
	}

	public void setActivityid1(int activityid1) {
		this.activityid1 = activityid1;
	}

	public int getActivityid1() {
		return activityid1;
	}

	public void setActivityid2(int activityid2) {
		this.activityid2 = activityid2;
	}

	public int getActivityid2() {
		return activityid2;
	}

	public void setBili1(int bili1) {
		this.bili1 = bili1;
	}

	public int getBili1() {
		return bili1;
	}

	public void setBili2(int bili2) {
		this.bili2 = bili2;
	}

	public int getBili2() {
		return bili2;
	}

}
