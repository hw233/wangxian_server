package com.fy.engineserver.activity.quiz;

import java.util.Comparator;
import java.util.Map.Entry;

import com.fy.engineserver.achievement.AchievementManager;
import com.fy.engineserver.achievement.RecordAction;
import com.fy.engineserver.sprite.Player;

public class AnswerRecord {

	
	public int getCulture() {
		return culture;
	}

	public void setCulture(int culture) {
		this.culture = culture;
	}


	public static final int amplifierNumLimit = 3;
	public static final int helpNumLimit = 3;
	
	long playerId;
	
	int culture;
	int score;
	long costTime;
	
	int helpNum = 3;
	int amplifierNum = 3;
	
	byte lastResult = -1;
	long lastCostTime;
	
	
	public void addTempRecord(byte result,long costTime){
		lastResult = result;
		lastCostTime = costTime;
	}
	
	public void addTempRecord(byte result){
		lastResult = result;
	}
	
	public void settle(Subject subject,Player p){
		if(subject.isRight(lastResult)){
			++score;
			++culture;
			p.addCulture(1);
			AchievementManager.getInstance().record(p, RecordAction.文采值累计);
			this.costTime += lastCostTime;
		}
		this.lastResult = -1;
		this.lastCostTime = 0;
	}
	
	
	public void addHelp(){
		--helpNum;
	}
	public void addAmplifier(){
		--amplifierNum;
	}
	
	public boolean isAddAmplifier(){
		return amplifierNum >0 ? true:false;
	}
	
	public boolean isAddHelp(){
		return helpNum > 0 ? true:false;
	}
	
	
	/**************************************************************************/
	
	
	public int getScore() {
		return score;
	}
	public long getPlayerId() {
		return playerId;
	}

	public void setPlayerId(long playerId) {
		this.playerId = playerId;
	}

	public void setScore(int score) {
		this.score = score;
	}
	public long getCostTime() {
		return costTime;
	}
	public void setCostTime(long costTime) {
		this.costTime = costTime;
	}

	public int getHelpNum() {
		return helpNum;
	}

	public void setHelpNum(int helpNum) {
		this.helpNum = helpNum;
	}

	public int getAmplifierNum() {
		return amplifierNum;
	}

	public void setAmplifierNum(int amplifierNum) {
		this.amplifierNum = amplifierNum;
	}

	public byte getLastResult() {
		return lastResult;
	}

	public void setLastResult(byte lastResult) {
		this.lastResult = lastResult;
	}

	public long getLastCostTime() {
		return lastCostTime;
	}

	public void setLastCostTime(long lastCostTime) {
		this.lastCostTime = lastCostTime;
	}

	
	static class RecordComparator implements Comparator<Entry<Long, AnswerRecord>> {
		
		@Override
		public int compare(Entry<Long, AnswerRecord> e1, Entry<Long, AnswerRecord> e2) {
			
			AnswerRecord o1 = e1.getValue();
			AnswerRecord o2 = e2.getValue();
			
			int score1 = o1.getScore();
			int score2 = o2.getScore();
			if(score2 > score1){
				return 1;
			}
			if(score2 == score1){
				long costTime1 = o1.getCostTime();
				long costTime2 = o2.getCostTime();
				if(costTime2 >= costTime1){
					return 1;
				}
			}
			return -1;
		}
		
	}

}
