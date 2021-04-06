package com.fy.engineserver.sprite;

import com.fy.engineserver.datasource.language.Translate;
import com.fy.engineserver.message.GameMessageFactory;
import com.fy.engineserver.message.NOTICE_CLIENT_READ_TIMEBAR_INTERRUPT_REQ;

/**
 *
 * 
 * @version 创建时间：Sep 19, 2011 1:42:50 PM
 * 
 */
public class TimerTask {

	public static final int type_采集 = 0;
	public static final int type_传功 = 1;
	public static final int type_元神切换 = 2;
	public static final int type_领取BUFF = 3;
	public static final int type_村庄战 = 4;
	public static final int type_骑飞行坐骑 = 5;
	public static final int type_附魔锁定 = 6;
	public static final int type_附魔特效buff = 7;
	public static final int type_宝箱发送等待 = 8;
	public static final int type_空岛大冒险 = 9;
	public static final int type_神奇大宝箱 = 10;
	public static final int type_仙灵捕捉 = 11;
	
	public static final String TYPE[] = new String[]{
		"采集", "传功", "元神切换", "领取BUFF" ,Translate.村庄战, Translate.骑飞行坐骑,Translate.锁定附魔, "特效buff" ,"宝箱发送等待", "type_空岛大冒险","神奇大宝箱"
	};
	
	public static String getTypeDesp(int type) {
		if(type >=0 && type < TYPE.length) {
			return TYPE[type];
		}
		return "未定义";
	}
	
	public static final int event_移动 = 0;
	public static final int event_被攻击 = 1;
	public static final int event_施放技能 = 2;
	public static final int event_使用道具 = 3;
	public static final int event_上马 = 4;
	public static final int event_打坐 = 5;
	public static final int event_死亡 = 6;
	public static final int event_交易 = 7;
	public static final int event_传功 = 8;
	public static final int event_元神切换 = 9;
	public static final int event_采集 = 10;
	public static final int event_传送 = 11;
	public static final int event_上下线 = 12;
	public static final int event_捕捉失败 = 13;
	
	public static final String EVENT[] = new String[]{
		"移动","被攻击","施放技能","使用道具","上马","打坐","死亡","交易","传功","元神切换","采集","被动传送","上下线","捕捉失败"
	};
	
	public static String getEventDesp(int event) {
		if(event >=0 && event < EVENT.length) {
			return EVENT[event];
		}
		return "未定义";
	}
	
	/**
	 * 打断设定
	 * 维度0-type，维度1-event
	 */
	public static int[][] BREAK_CONFIG = new int[][]{
		//采集
		{1,1,1,1,1,1,1,1,1,1,1,1,1,1},
		//传功
		{0,0,0,0,0,0,0,0,0,0,0,0,1,0},
		//元神切换
		{1,1,1,1,1,1,1,1,1,1,1,1,1,1},
		{1,1,1,1,1,1,1,1,1,1,1,1,1,1},
		{1,1,1,1,1,1,1,1,1,1,1,1,1,1},
		//骑飞行坐骑
		{1,1,1,1,1,1,1,1,1,1,1,1,1,1},
		//附魔
		{1,1,1,1,1,1,1,1,1,1,1,1,1,1},
		
		{0,0,0,0,0,0,0,0,0,0,0,0,0,0},
		{0,0,0,0,0,0,0,0,0,0,0,0,0,0},
		//空岛大冒险
		{1,0,0,0,0,0,0,0,0,0,0,0,1,0},
		{1,0,0,0,0,0,0,0,0,0,0,0,1,0},
		//仙灵捕捉
		{1,0,0,0,0,0,1,0,0,0,0,1,1,1}
	};
	
	public static int[] getBreakFlags(int type) {
		return BREAK_CONFIG[type];
	}
	
	private Callbackable callbacker;
	
	private int type;
	
	private long startTime;
	
	private long endTime;
	
	private int[] breakFlags;

	public int getType() {
		return type;
	}

	public void setType(int type) {
		this.type = type;
	}

	public Callbackable getCallbacker() {
		return callbacker;
	}

	public void setCallbacker(Callbackable callbacker) {
		this.callbacker = callbacker;
	}

	public long getStartTime() {
		return startTime;
	}

	public void setStartTime(long startTime) {
		this.startTime = startTime;
	}

	public long getEndTime() {
		return endTime;
	}

	public void setEndTime(long endTime) {
		this.endTime = endTime;
	}

	public int[] getBreakFlags() {
		return breakFlags;
	}

	public void setBreakFlags(int[] breakFlags) {
		this.breakFlags = breakFlags;
	}
	
	/**
	 * 是否打断
	 * @param actionType
	 * @return
	 */
	public boolean isBreak(int actionType) {
		try {
			return breakFlags[actionType]==1;
		} catch(Exception e) {}
		return false;
	}
	
	public void breakNoticePlayer(Player owner){
		
		owner.addMessageToRightBag(new NOTICE_CLIENT_READ_TIMEBAR_INTERRUPT_REQ(GameMessageFactory.nextSequnceNum()));
		
	}
}
