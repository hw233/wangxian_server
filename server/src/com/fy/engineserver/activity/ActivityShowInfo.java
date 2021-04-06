package com.fy.engineserver.activity;

import java.util.Arrays;

import com.fy.engineserver.activity.loginActivity.ActivityManagers;
import com.xuanzhi.boss.game.GameConstants;

/**
 * 活动展示信息
 * 
 *
 */
public class ActivityShowInfo {

	public int activityId;
	public int[] weeks;
	public int tempId;
	public String tempIcon;
	public int buttonType;
	public int buttonFunction;
	public String functionArgs;
	public String activityName;
	public String activityName_stat;
	public String activityName_color;
	public String activityTitle;
	public String activityContent;
	public long startShowTime;
	public long endShowTime;
	public String names[]=new String[0];
	public int colors[] = new int[0];
	public int nums[]= new int[0];
	public int lizis[]= new int[0];
	public String openservers[];
	public String limitservers[];
	public ActivityShowInfo(){}

	public ActivityShowInfo(int activityId, int[] weeks, long startShowTime,long endShowTime,int tempId, String tempIcon, int buttonType, int buttonFunction, String functionArgs, String activityName, String activityTitle, String activityContent) {
		super();
		this.activityId = activityId;
		this.weeks = weeks;
		this.tempId = tempId;
		this.tempIcon = tempIcon;
		this.buttonType = buttonType;
		this.buttonFunction = buttonFunction;
		this.functionArgs = functionArgs;
		this.activityName = activityName;
		this.activityTitle = activityTitle;
		this.activityContent = activityContent;
		this.startShowTime = startShowTime;
		this.endShowTime = endShowTime;
	}

	@Override
	public String toString() {
		return "ActivityShowInfo [activityId=" + activityId + ", weeks=" + Arrays.toString(weeks) + ", tempId=" + tempId + ", tempIcon=" + tempIcon + ", buttonType=" + buttonType + ", buttonFunction=" + buttonFunction + ", functionArgs=" + functionArgs + ", activityName=" + activityName + ", startShowTime=" + startShowTime + ", endShowTime=" + endShowTime + ", names=" + Arrays.toString(names) + ", colors=" + Arrays.toString(colors) + ", nums=" + Arrays.toString(nums) + "]";
	}

	public boolean isEffect(){
		if(System.currentTimeMillis()>endShowTime || System.currentTimeMillis()<startShowTime){
//			ActivityManagers.logger.warn("[展示信息] [时间不符] ["+(System.currentTimeMillis()>endShowTime)+"] ["+(System.currentTimeMillis()<startShowTime)+"]");
			return false;
		}
		
		String servername = GameConstants.getInstance().getServerName();
		
		if(limitservers!=null && limitservers.length>0){
			for(String name:limitservers){
				if(name.equals(servername)){
//					ActivityManagers.logger.warn("[展示信息] [限制服务器] ["+(Arrays.toString(limitservers))+"]");
					return false;
				}
			}
		}
		
		int servernums = 0;
		if(openservers!=null && openservers.length>0){
			for(String name:openservers){
				if(name!=null && name.length()>0){
					servernums++;
					if(name.equals(servername)){
						return true;
					}
				}
			}
		}
		
		if(openservers==null || servernums==0){
			return true;
		}
		
//		ActivityManagers.logger.warn("[展示信息] [条件不符] ["+(Arrays.toString(limitservers))+"]  ["+(Arrays.toString(openservers))+"]");
		return false;
	}
	
}
