package com.fy.engineserver.event;

public abstract class Event {
	//
	public int id;
	public Event(){
		initId();
	}
	public abstract void initId();
	//
	static final int EVT_ADD_PROC = 1;
	//
	public static final int SERVER_STARTED = 		9;
	public static final int SERVER_MONTH_CHANGE = 10;
	public static final int SERVER_DAY_CHANGE = 11;
	public static final int SERVER_HOUR_CHANGE = 12;
	public static final int SERVER_30MINU_CHANGE = 13;
	public static final int SERVER_10MINU_CHANGE = 14;
	public static final int SERVER_MINU_CHANGE = 15;
	public static final int SERVER_30SEC_CHANGE = 16;
	public static final int SERVER_10SEC_CHANGE = 17;
	public static final int SERVER_SECOND_CHANGE = 18;
	public static final int SERVER_5MINU_CHANGE = 19;
	//
	public static final int PLAYER_LOGIN = 1000;
	/**
	 * 玩家进入场景（地图）
	 */
	public static final int PLAYER_ENTER_SCENE 	= 1005;
	public static final int PLAYER_LEAVE_SCENE 	= 1006;
	/**
	 * 玩家下线。
	 */
	public static final int PLAYER_EXIT			= 1007;
	public static final int PLAYER_KILL_MONSTER = 1010;
	public static final int PLAYER_DU_JIE		 = 1015;
	public static final int PLAYER_CHARGE		= 1020;
	public static final int PLAYER_SPEND_MONEY	= 1030;
	public static final int PLAYER_LEVEL_CHANGE	= 1040;
	public static final int PLAYER_GAIN_PET		= 1050;
	/**
	 * 玩家放生宠物.
	 */
	public static final int PLAYER_DROP_PET		= 1051;
	public static final int PET_LEVEL_UP		= 1052;
	public static final int PET_SCORE_CHANGE	= 1053;
	/**
	 * 参数是 new Object[]{player, pet}
	 */
	public static final int PET_JIN_JIE			= 1054;
	/**
	 * 仙尊挑战结果   参数是 new Object[]{player, result, soultype}  (byte result = 1(挑战成功)   = -1(挑战失败))
	 */
	public static final int FAIRY_CHALLENGE_RESULT	= 1055;
	/** 杀死怪物
	 *  参数是 new Object[]{monster}
	 *  */
//	public static final int MONSTER_KILLED = 1056;
	/** 触发事件监测玩家数据  自动完成玩家已完成过的目标  
	 * 	参数是 new Object[]{playerId}
	 * */
	public static final int CHECK_PLAYER_AIMS = 1057;
	/**
	 * 记录玩家某种操作   
	 * 参数是 new Object[]{playerId, RecordAction, num}
	 */
	public static final int RECORD_PLAYER_OPT = 1058;
	/***
	 * 摘取果实
	 * 参数  new Object[]{player, fruitColor}
	 */
	public static final int PICK_FRUIT = 1059;
	/** 怪物死亡--之发送简单信息 
	 * 参数 new Object[]{simpleMonster}
	 * */
	public static final int MONSTER_KILLED_Simple = 1060;
	/**
	 * 自动喂养坐骑   参数是new Object[]{playerId, horseId}
	 */
	public static final int AUTO_FEED_HORSE = 1061;
	/**
	 * 仙灵挑战结果  参数是new Object[]{player, result}
	 */
	public static final int XIANLING_CHALLENGE_RESULT = 1062;
}
