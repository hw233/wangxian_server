package com.fy.engineserver.activity.base;

import java.text.SimpleDateFormat;
import java.util.ArrayList;

import com.fy.engineserver.activity.ActivitySubSystem;
import com.fy.engineserver.core.ExperienceManager;
import com.fy.engineserver.sprite.Player;

public class ExpAddManager {

	public static ExpAddManager instance = new ExpAddManager();
	
	public ExpAddManager() {
		SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		try{
			//喝酒活动
			long id = 1000;
//			String startTime = "2013-01-20 00:00:00";
//			String endTime = "2013-01-28 23:59:00";
			String startTime = "2013-02-07 00:00:00";
			String endTime = "2013-02-11 23:59:59";
			int addType = ExpAddAbstract.EXP_ADD_TYPE1;
			int addParameter = 2000;
			ExpAddHeJiu base1 = new ExpAddHeJiu(id, format.parse(startTime).getTime(), 
					format.parse(endTime).getTime(), addType, addParameter, 2);
			addActivity(base1);
		}catch(Exception e) {
			ActivitySubSystem.logger.error("活动创建失败", e);
		}
		try{
			//封魔录活动
			long id = 1001;
//			String startTime = "2013-01-20 00:00:00";
//			String endTime = "2013-01-28 23:59:00";
			String startTime = "2013-02-07 00:00:00";
			String endTime = "2013-02-11 23:59:59";
			int addType = ExpAddAbstract.EXP_ADD_TYPE1;
			int addParameter = 2000;
			ExpAddTuMoTie base1 = new ExpAddTuMoTie(id, format.parse(startTime).getTime(), 
					format.parse(endTime).getTime(), addType, addParameter, 2);
			addActivity(base1);
		}catch(Exception e) {
			ActivitySubSystem.logger.error("活动创建失败", e);
		}
		try{
			long id = 1002;
			String startTime = "2013-01-20 00:00:00";
			String endTime = "2013-01-20 14:59:00";
			int addReason = ExperienceManager.ADDEXP_REASON_FIGHTING;
			int addType = ExpAddAbstract.EXP_ADD_TYPE1;
			int addParameter = 10000;
			ExpAddBase base1 = new ExpAddBase(id, format.parse(startTime).getTime(), 
					format.parse(endTime).getTime(), addReason, addType, addParameter);
			addActivity(base1);
		}catch(Exception e) {
			ActivitySubSystem.logger.error("活动创建失败", e);
		}
	}
	
	public ArrayList<ExpAddAbstract> expAdds = new ArrayList<ExpAddAbstract>();
	
	
	/**
	 * 外部活动添加经验活动用这个方法
	 * @param base
	 */
	public void addActivity(ExpAddAbstract base) {
		for ( int i = 0; i < expAdds.size(); i++) {
			ExpAddAbstract expadd = expAdds.get(i);
			if (expadd.activityId == base.activityId) {
				expAdds.set(i, base);
				return;
			}
		}
		expAdds.add(base);
	}
	
	public void removeActivity(int activityID) {
		for ( int i = 0; i < expAdds.size(); i++) {
			ExpAddAbstract expadd = expAdds.get(i);
			if (expadd.activityId == activityID) {
				expAdds.remove(i);
				break;
			}
		}
	}
	
	
	/**
	 * 这个方法在player 的 addexp方法调用
	 * @param player
	 * @param exp
	 * @param reason
	 * @return
	 */
	public long doAddExp(Player player, long exp, int reason, Object...objects) {
		long oldExp = exp;
		for (ExpAddAbstract expadd : expAdds) {
			exp = expadd.doExpAdd(player, exp, reason, objects);
		}
		if (exp != oldExp) {
			ActivitySubSystem.logger.warn("[经验活动] [{}] [o={}] [n={}] [r={}]", new Object[]{player.getLogString(), oldExp, exp, reason});
		}
		return exp;
	}
}
