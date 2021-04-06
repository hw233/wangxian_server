package com.fy.engineserver.quiz;

import java.util.ArrayList;
import java.util.LinkedHashMap;

import com.fy.engineserver.menu.question.Question;
import com.fy.engineserver.sprite.Player;

public class AnswerRecord {
	
	//Player player;
	long playerId;
	String playerName;
	int playerLevel;
	/**
	 * 答对次数
	 */
	int rightNum;
	
	/**
	 * 答错次数
	 */
	int wrongNum;
	
	/**
	 * 得分
	 */
	int points;
	
	/**
	 * 消耗的时间
	 */
	long spendTime;
	
	/**
	 * 回答过的问题
	 */
	ArrayList<Integer> questionIds;
	
	/**
	 * 回答问题的状态，key是问题ID，value是问题回答的状态：0未回答，1正确，2错误
	 */
	LinkedHashMap<Integer, Integer> questionStatus;
	
	/**
	 * 奖励金钱
	 */
	long rewardMoney;
	
	/**
	 * 奖励经验
	 */
	long rewardExp;
	
	/**
	 * 当前问题的ID
	 */
	int currentQuestionId;
	
	/**
	 * 所有的问题
	 */
	ArrayList<Question> allQuestions;
	
	
	public AnswerRecord(Player player){
		this.playerId = player.getId();
		this.playerName = player.getName();
		this.playerLevel = player.getLevel();
		this.questionIds=new ArrayList<Integer>();
		this.questionStatus=new LinkedHashMap<Integer, Integer>();
		this.allQuestions=new ArrayList<Question>();
	}


	public long getPlayerId() {
		return playerId;
	}


	public void setPlayerId(long playerId) {
		this.playerId = playerId;
	}


	public String getPlayerName() {
		return playerName;
	}


	public void setPlayerName(String playerName) {
		this.playerName = playerName;
	}


	public int getRightNum() {
		return rightNum;
	}

	public void setRightNum(int rightNum) {
		this.rightNum = rightNum;
	}

	public int getWrongNum() {
		return wrongNum;
	}

	public void setWrongNum(int wrongNum) {
		this.wrongNum = wrongNum;
	}

	public int getPoints() {
		return points;
	}

	public void setPoints(int points) {
		this.points = points;
	}
	
	public void addQuestionId(int questionId){
		if(!this.questionIds.contains(questionIds)){
			this.questionIds.add(questionId);
		}
	}
	
	/**
	 * 更新所答题目的状态
	 * @param questionId 问题ID
	 * @param status 0未回答，1正确，2错误
	 */
	public void updateQuestionStatus(int questionId,int status){
		this.questionStatus.put(questionId, status);
	}
	
	public int getQuestionStatus(int questionId){
		if(this.questionStatus.containsKey(questionId)){
			return this.questionStatus.get(questionId);
		}
		return -1;
	}
	
	/**
	 * 这个问题是否回答过了
	 * @param questionId
	 * @return
	 */
	public boolean isAnswered(int questionId){
		return this.questionIds.contains(questionIds);
	}

	public long getSpendTime() {
		return spendTime;
	}

	public void setSpendTime(long spendTime) {
		this.spendTime = spendTime;
	}

	public long getRewardMoney() {
		return rewardMoney;
	}

	public void setRewardMoney(long rewardMoney) {
		this.rewardMoney = rewardMoney;
	}

	public long getRewardExp() {
		return rewardExp;
	}

	public void setRewardExp(long rewardExp) {
		this.rewardExp = rewardExp;
	}


	public int getCurrentQuestionId() {
		return currentQuestionId;
	}


	public void setCurrentQuestionId(int currentQuestionId) {
		this.currentQuestionId = currentQuestionId;
	}


	public int getPlayerLevel() {
		return playerLevel;
	}


	public void setPlayerLevel(int playerLevel) {
		this.playerLevel = playerLevel;
	}
}
