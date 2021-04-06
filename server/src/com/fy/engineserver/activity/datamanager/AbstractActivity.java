package com.fy.engineserver.activity.datamanager;

import java.util.List;
/**
 * 需要在新活动界面显示的活动需要实现此接口
 */
public interface AbstractActivity {
	/**
	 * 活动名称，用于作为主键获取相关显示配置信息
	 * @return
	 */
	public String getActivityName();
	/**
	 * 获取活动开启时间  
	 * @return  null代表当天不开启此活动 
	 */
	public List<String> getActivityOpenTime(long now);
	/**
	 * 是否开启
	 */
	public boolean isActivityTime(long now);
}
