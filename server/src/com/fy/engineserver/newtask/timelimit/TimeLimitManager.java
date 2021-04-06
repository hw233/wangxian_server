package com.fy.engineserver.newtask.timelimit;

import java.util.ArrayList;
import java.util.Hashtable;
import java.util.List;
import java.util.Map;

import com.fy.engineserver.event.Event;
import com.fy.engineserver.event.EventProc;
import com.fy.engineserver.event.EventRouter;
import com.fy.engineserver.event.cate.EventPlayerLogin;
import com.fy.engineserver.newtask.TaskEntity;
import com.fy.engineserver.newtask.service.TaskManager;
import com.fy.engineserver.sprite.Player;

/**
 * 存储时间对接取交付有日期限制的任务模板--做通知
 * @author Administrator
 *
 */
public class TimeLimitManager implements EventProc{
	private static TimeLimitManager instance = new TimeLimitManager();
	/** 固定周几不开放 */
	public static final byte type_of_weed = 1;		
	/** 固定时间不开放 */
	public static final byte type_of_day = 2;		
	public Map<Byte,List<TimeLimit4Accept>> timeLimitMap = new Hashtable<Byte,List<TimeLimit4Accept>>();
	/** 存储接取切没有完成有接取交付时限的玩家列表  */
	public Map<Byte,List<Long>> playerMap = new Hashtable<Byte,List<Long>>();
	
	private TimeLimitManager() {
		instance = this;
	}
	public static TimeLimitManager getInst() {
		return instance;
	}
	/**
	 * 将有时间限制接取交付的任务加入map中
	 * @param limitType
	 * @param base
	 */
	public void add2AllActMap(Byte limitType, TimeLimit4Accept base) {
		if(timeLimitMap.get(limitType) == null) {
			List<TimeLimit4Accept> list = new ArrayList<TimeLimit4Accept>();
			timeLimitMap.put(limitType, list);
		} 
		List<TimeLimit4Accept> list = timeLimitMap.get(limitType);
		if(!list.contains(base)) {
			list.add(base);
		}
		timeLimitMap.put(limitType, list);
	}
	/**
	 * 玩家接取任务或完成任务时维护map
	 * @param player
	 * @param limitType
	 * @param isAccept	接取true  交付false
	 */
	public void modifyPlayerMap(Player player, Byte limitType, boolean isAccept) {
		if(playerMap.get(limitType) == null) {
			List<Long> list = new ArrayList<Long>();
			playerMap.put(limitType, list);
		}
		List<Long> list = playerMap.get(limitType);
		if(isAccept) {
			if(!list.contains(player.getId())) {
				list.add(player.getId());
			}
		} else {
			if(list.contains(player.getId())) {
				list.remove(player.getId());
			} else {
				if(TaskManager.logger.isWarnEnabled()) {
					TaskManager.logger.warn("[时限接取任务][完成任务维护playerMap出错][playerMap中没有玩家信息][" + player.getLogString() + "][limitType:" + limitType + "]");
				}
			}
		}
		playerMap.put(limitType, list);
	}
	/**
	 * 检测玩家已接取任务，检测是否有时限任务。判断给玩家自动放弃或者在map中加入玩家信息
	 * @param p
	 */
//	private void checkAndAdd2Map(Player p) {
//		List<TaskEntity> allTask = p.getAllTask();
//		List<Long> list = null;
//		for(TaskEntity et : allTask) {
//			if(et.getTask() != null && et.getTask().getLimitType() > 0) {		//时限任务
//				list = new ArrayList<Long>();
//				list.add(p.getId());
//				et.getTask().do4Player(list, et.getTask().getActType());
//			}
//		}
//	}
	
	@Override
	public void proc(Event evt) {
		// TODO Auto-generated method stub
		if(TaskManager.logger.isDebugEnabled()) {
			TaskManager.logger.debug("[时限接取任务][收到系统时间通知][" + evt.id + "]");
		}
		List<TimeLimit4Accept> list = null;
		List<Long> list2 = null;
		byte type = 0;
		switch (evt.id) {
		case Event.SERVER_DAY_CHANGE:
			type = type_of_weed;
			break;
		case Event.SERVER_MINU_CHANGE:
			type = type_of_day;
			break;
		case Event.PLAYER_LOGIN:
//			Player p = ((EventPlayerLogin) evt).player;
//			checkAndAdd2Map(p);
			break;
		default:
			break;
		}
		list = timeLimitMap.get(type);
		list2 = playerMap.remove(type);
		if(list != null && list2 != null) {
			if(TaskManager.logger.isDebugEnabled()) {
				TaskManager.logger.debug("[时限接取任务][找到player和任务][" + evt.id + "]");
			}
			for(TimeLimit4Accept ta : list) {
				ta.doAct(list2);
			}
		}
	}
	/**
	 * 读表结束后调用此方法
	 */
	@Override
	public void doReg() {
		// TODO Auto-generated method stub
		List<TimeLimit4Accept> list = timeLimitMap.get(type_of_weed);
		if(list != null && list.size() > 0) {
			EventRouter.register(Event.SERVER_DAY_CHANGE, this);
			TaskManager.logger.info("[时限接取任务][注册系统跨天通知事件]");
		}
		List<TimeLimit4Accept> list2 = timeLimitMap.get(type_of_day);
		if(list2 != null && list2.size() > 0) {
			EventRouter.register(Event.SERVER_MINU_CHANGE, this);
			TaskManager.logger.info("[时限接取任务][注册系统分钟变化事件]");
		}
		EventRouter.register(Event.PLAYER_LOGIN, this);
	}
}
