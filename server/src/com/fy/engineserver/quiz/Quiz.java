/**
 * 
 */
package com.fy.engineserver.quiz;

import java.util.ArrayList;
import java.util.Hashtable;

import com.fy.engineserver.menu.question.Question;
import com.fy.engineserver.sprite.Player;

/**
 * @author Administrator
 *
 */
public abstract class Quiz {
	
	String name;
	
	byte status;
	
	static final byte STATUS_ENROLL=1;
	
	static final byte STATUS_START=2;
	
	static final byte STATUS_END=3;
	
	static final byte STATUS_WAITING=4;
	
	/**
	 * 答题的等级限制
	 */
	int levelLimit;
	
	/**
	 * 参与者
	 */
	Hashtable<Long, AnswerRecord> participants;
	
	/**
	 * 领奖记录
	 */
	ArrayList<Integer> takeRewardRecord;
	
	public Quiz(String name){
		this.name=name;
		this.participants=new Hashtable<Long, AnswerRecord>();
		this.takeRewardRecord=new ArrayList<Integer>();
	}
	
	public abstract void heartbeat()throws Exception;
	
	/**
	 * 参加答题
	 * @param playerId
	 */
	public abstract void enroll(Player player);
	
	/**
	 * 退出答题
	 * @param playerId
	 */
	public abstract void quit(long playerId);
	
	/**
	 * 答题
	 * @param playerId
	 * @param selectionIndex 答案选项
	 * @param questionIndex 问题序号
	 * @return
	 */
	public abstract boolean answer(long playerId,Question question, int selectionIndex);
	
	/**
	 * 是否是答题时间
	 * @return
	 */
	public abstract boolean isAnswerTime();
	
	/**
	 * 是否是报名时间
	 * @return
	 */
	public abstract boolean isEnrollTime();
	
	
	
	/**
	 * 是否是参与者
	 * @param playerId
	 * @return
	 */
	public abstract boolean isParticipant(long playerId);

	public byte getStatus() {
		return status;
	}

	public void setStatus(byte status) {
		this.status = status;
	}

	public String getName() {
		return name;
	}
	
	public AnswerRecord getAnswerRecord(long playerId){
		if(this.participants.containsKey(playerId)){
			return this.participants.get(playerId);
		}
		return null;
	}
	
	public void removeParticipant(long playerId){
		if(this.participants.containsKey(playerId)){
			this.participants.remove(playerId);
		}
	}
	
	/**
	 * 添加领奖记录
	 * @param playerId
	 */
	public void addTakeRewardRecord(int playerId){
		if(!this.takeRewardRecord.contains(playerId)){
			this.takeRewardRecord.add(playerId);
		}
	}

	public int getLevelLimit() {
		return levelLimit;
	}

	public void setLevelLimit(int levelLimit) {
		this.levelLimit = levelLimit;
	}
}
