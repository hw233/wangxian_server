package com.fy.engineserver.activity.base;

import com.fy.engineserver.sprite.Player;

public abstract class ExpAddAbstract {
	
	public static final int EXP_ADD_TYPE1 = 0;			//比例
	public static final int EXP_ADD_TYPE2 = 1;			//定值
	
	public static int ADD_RATIO = 10000;

	public long activityId;			//这个ID在活动中是唯一，要不会覆盖
	
	public long startTime;			//开始时间
	public long endTime;			//结束时间
	
	public int addReason = -1;		//经验加原因   ExperienceManager 中配置的ADD类型
	
	public int addType;				//比例0或定值1
	public int addParameter;		//参数		比例的话这个是万分之
	
	public ExpAddAbstract(long id, long startTime, long endTime, int addReason, int addType, int addParameter){
		this.activityId = id;
		this.startTime = startTime;
		this.endTime = endTime;
		this.addReason = addReason;
		this.addType = addType;
		this.addParameter = addParameter;
	}

	public long doExpAdd(Player player, long exp, int addReason, Object...is) {
		long now = System.currentTimeMillis();
		if (now >= startTime && now <= endTime) {
			if (canAdd(player, addReason, is)) {
				if (addType == EXP_ADD_TYPE1) {
					exp += exp * addParameter / ADD_RATIO;
				}else if (addType == EXP_ADD_TYPE2) {
					exp += addParameter;
				}
			}
		}
		return exp;
	}
	
	/**
	 * 判断是否可以加经验
	 * @param player
	 * @param addReason
	 * @return
	 */
	public abstract boolean canAdd(Player player, int addReason, Object...is);
	
	public abstract String getParmeter();
}
