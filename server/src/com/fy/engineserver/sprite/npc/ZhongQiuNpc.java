package com.fy.engineserver.sprite.npc;

import java.util.Date;

import com.fy.engineserver.core.Game;
import com.xuanzhi.tools.text.DateUtil;

/**
 * 中秋NPC
 * 
 * 当第一个人答题时，设置一个状态，该状态表示已经有人在答题了。当处于答题状态时，其他人的答题请求
 * 会被拒绝。答题状态持续一定时间，然后状态变成可答题状态，这时候，其他人可以答题。
 * 
 * 
 * 
 *
 */
public class ZhongQiuNpc extends NPC implements Cloneable{

	/**
	 * 
	 */
	private static final long serialVersionUID = -7453166717555642290L;
	/////////////////////////////////////////////////////////////////////
	// 以下为中秋NPC内部数据

//	/**
//	 * 答题状态，true表示有人正在答题，其他人不能答题
//	 */
//	private boolean questionState;
//	
//	/**
//	 * 正在答题的人
//	 */
//	private String questionPlayerName;
//	
//	/**
//	 * 最后一次答题时间点
//	 */
//	private long lastQuestionTime = com.fy.engineserver.gametime.SystemTime.currentTimeMillis();
	
	private int questionId = -1;

//	public boolean isQuestionState() {
//		return questionState;
//	}
//	public void setQuestionState(boolean questionState) {
//		if(questionState){
//			lastQuestionTime = com.fy.engineserver.gametime.SystemTime.currentTimeMillis();
//		}
//		this.questionState = questionState;
//	}
//	public String getQuestionPlayerName() {
//		return questionPlayerName;
//	}
//	public void setQuestionPlayerName(String questionPlayerName) {
//		this.questionPlayerName = questionPlayerName;
//	}
//
//	public long getLastQuestionTime() {
//		return lastQuestionTime;
//	}
//	public void setLastQuestionTime(long lastQuestionTime) {
//		this.lastQuestionTime = lastQuestionTime;
//	}

	public int getQuestionId() {
		return questionId;
	}
	public void setQuestionId(int questionId) {
		this.questionId = questionId;
	}
	public void heartbeat(long heartBeatStartTime, long interval, Game game){
		super.heartbeat(heartBeatStartTime, interval, game);
		
//		if(questionState && (heartBeatStartTime - lastQuestionTime) > 30000){
//			this.questionState = false;
//		}
		
	}
	public static boolean isValidTime(){
		boolean validTime = false;
		String date = DateUtil.formatDate(new Date(com.fy.engineserver.gametime.SystemTime.currentTimeMillis()), "yyyyMMddHH");
		String date1 = date.substring(0,date.length() - 2);
		String date2 = date.substring(date.length() - 2);
		if("20100922".equals(date1) || "20100923".equals(date1) || "20100924".equals(date1)){
			int intDate = Integer.parseInt(date2);
			if(intDate >= 18 && intDate < 22){
				validTime = true;
			}
		}
//		if(true){
//			int intDate = Integer.parseInt(date2);
//			if(intDate >= 10 && intDate <= 18){
//				validTime = true;
//			}
//		}
		return validTime;
	}

	/**
	 * clone出一个对象
	 */
	public Object clone() {
		ZhongQiuNpc p = new ZhongQiuNpc();
		p.cloneAllInitNumericalProperty(this);
		
		p.country = country;
		
		p.setnPCCategoryId(this.getnPCCategoryId());
		
		p.windowId = windowId;
		
		return p;
	}

}
