package com.fy.engineserver.activity.base;

import java.util.Arrays;
import java.util.Calendar;

import com.fy.engineserver.activity.BaseActivityInstance;
import com.fy.engineserver.activity.RefreshSpriteManager;

public class GodDownActivityConfig extends BaseActivityInstance{


	private String stime = "";						//只是用来toString
	private String etime = "";
	private String rewardNames= "";					//箱子
	private boolean hasFlowerDown;					//是否有粒子效果
	private String liZiName = "";					//粒子名
	private int starttime[] = new int[0];
	private int endtime[] = new int[0];
	
	public GodDownActivityConfig(String startTime, String endTime, String platForms, String openServerNames, String notOpenServers) throws Exception {
		super(startTime, endTime, platForms, openServerNames, notOpenServers);
		this.stime = startTime;
		this.etime = endTime;
	}
	
	public void setStartTimes (String timeHours,String rewardnames,boolean hasFlowerDown) throws Exception{
		String[] tempP1 = timeHours.split(",");
		starttime =  RefreshSpriteManager.parse2Int(tempP1);
		if(starttime != null && starttime.length > 0){
			endtime = new int[starttime.length];
			for(int i=0;i<starttime.length;i++){
				if(starttime[i] > 0){
					endtime[i] = starttime[i] + 2;
				}
			}
		}
		this.hasFlowerDown = hasFlowerDown;
		this.rewardNames = rewardnames;
	}
	

	public int isEffectActivity(){
		Calendar calendar = Calendar.getInstance();
		int result = -1;
		if(this.isThisServerFit() == null){
			for (int i=0;i<starttime.length;i++) {
				int hour = calendar.get(Calendar.HOUR_OF_DAY);
				int minute = calendar.get(Calendar.MINUTE);
				int currMinutes = hour * 60 + minute;
				int startMinutes = starttime[i] * 60;
				int endMinutes = endtime[i] * 60;
				if(currMinutes >= startMinutes && currMinutes < endMinutes){
					result = starttime[i];
					break;
				}
			}
		}
		return result;
	}
	
	public String getRewardNames() {
		return this.rewardNames;
	}

	public void setRewardNames(String rewardNames) {
		this.rewardNames = rewardNames;
	}

	public boolean isHasFlowerDown() {
		return this.hasFlowerDown;
	}
	public void setHasFlowerDown(boolean hasFlowerDown) {
		this.hasFlowerDown = hasFlowerDown;
	}
	public String getStime() {
		return this.stime;
	}
	public void setStime(String stime) {
		this.stime = stime;
	}
	public String getEtime() {
		return this.etime;
	}
	public void setEtime(String etime) {
		this.etime = etime;
	}
	public String getLiZiName() {
		return this.liZiName;
	}
	public void setLiZiName(String liZiName) {
		this.liZiName = liZiName;
	}

	@Override
	public String getInfoShow() {
		StringBuffer sb = new StringBuffer();
		sb.append("[活动类型 :飘箱子]");
		sb.append("[活动生效时间 :");
		sb.append(stime).append("---").append(etime);
		sb.append("] [箱子名 : " + rewardNames + "]").append("[开始时间点:").append(Arrays.toString(starttime)).append("] [结束时间点:")
		.append(Arrays.toString(endtime)).append("]").append(" [是否有粒子效果:").append(hasFlowerDown).append("]");
		return sb.toString();
	}
}
