package com.fy.engineserver.validate;

import java.io.Serializable;
import java.util.Arrays;
import java.util.Calendar;

public class UserAskData implements Serializable {

	private static final long serialVersionUID = 201307221041L;
	
	private long pid;				//玩家ID
	private String pName;			//名字
	private String uName;			//用户名
	private int level;				//级别
	
	//答题次数
	private int[] answerTimes = new int[10];
	//答题回答正确次数
	private int[] answerRightTimes = new int[10];
	//答题回答错误次数
	private int[] answerWrongTimes = new int[10];
	//上次答题连续错误次数
	private int lastanswerWrongTimes;
	//上次答题的状态
	private int lastValidateState = 0;
	//上次答题时间
	private long lastValidateTime;
	//直接通过检查次数
	private int directPassCount = 0;
	//连续直接通过检查次数
	private int lastDirectPassCount = 0;
	//今天答题数目
	private int todayRihgtTimes;

	//最后访问时间
	private long lastGetTime; 
	
	@Override
	public String toString() {
		StringBuffer sb = new StringBuffer("");
		sb.append("[").append(pid).append("] ");
		sb.append("[").append(pName).append("] ");
		sb.append("[").append(uName).append("] ");
		sb.append("[").append(level).append("] ");
		sb.append("[").append(Arrays.toString(answerTimes)).append("] ");
		sb.append("[对:").append(Arrays.toString(answerRightTimes)).append("] ");
		sb.append("[错:").append(Arrays.toString(answerWrongTimes)).append("] ");
		sb.append("[当前状态:").append(lastValidateState).append("] ");
		sb.append("[连续错:").append(lastanswerWrongTimes).append("] ");
		sb.append("[pass:").append(directPassCount).append("] ");
		sb.append("[今天对的:").append(todayRihgtTimes).append("] ");
		return sb.toString();
	}
	
	/**
	 * 得到总的错误数目
	 * @return
	 */
	public int getAllWrongTimes () {
		int num = 0;
		for (int i = 0; i < answerWrongTimes.length; i++) {
			num = num + answerWrongTimes[i];
		}
		return num;
	}
	
	public int getAllRightTimes() {
		int num = 0;
		for (int i = 0; i < answerRightTimes.length; i++) {
			if (i == OtherValidateManager.ASK_TYPE_PLAYER_ENTER) {
				continue;
			}
			num = num + answerRightTimes[i];
		}
		return num;
	}
	
	//今天答题是否正确过2次
	public boolean isTodayRightEnough () {
		Calendar cal = Calendar.getInstance();
		cal.setTimeInMillis(lastValidateTime);
		int d1 = cal.get(Calendar.DAY_OF_YEAR);

		cal.setTimeInMillis(System.currentTimeMillis());
		int d2 = cal.get(Calendar.DAY_OF_YEAR);
		if (d1 != d2) {
			todayRihgtTimes = 0;
		}
		if (todayRihgtTimes >= OtherValidateManager.TODAY_MAX_RIGHT_TIME) {
			return true;
		}
		return false;
	}
	
	public void answerRight (int askType) {
		answerRightTimes[askType]++;
		if (askType != OtherValidateManager.ASK_TYPE_PLAYER_ENTER) {
			todayRihgtTimes++;
		}
		lastanswerWrongTimes = 0;
		lastValidateState = ValidateManager.VALIDATE_STATE_通过;
		lastValidateTime = System.currentTimeMillis();
	}
	
	public void answerWrong (int askType) {
		answerWrongTimes[askType]++;
		lastanswerWrongTimes++;
		lastValidateState = ValidateManager.VALIDATE_STATE_需要答题;
		lastValidateTime = System.currentTimeMillis();
	}
	
	/**
	 * 加答题次数，更新上次pass次数为0
	 * @param askType
	 */
	public void addAnswerTimes (int askType) {
		lastDirectPassCount = 0;
		answerTimes[askType]++;
		lastValidateState = ValidateManager.VALIDATE_STATE_需要答题;
		lastValidateTime = System.currentTimeMillis();
	}
	
	public void addPassCount() {
		directPassCount++;
		lastDirectPassCount++;
	}
	
	public void setPid(long pid) {
		this.pid = pid;
	}
	public long getPid() {
		return pid;
	}
	public void setpName(String pName) {
		this.pName = pName;
	}
	public String getpName() {
		return pName;
	}
	public void setuName(String uName) {
		this.uName = uName;
	}
	public String getuName() {
		return uName;
	}
	public void setLevel(int level) {
		this.level = level;
	}
	public int getLevel() {
		return level;
	}
	public void setAnswerRightTimes(int[] answerRightTimes) {
		this.answerRightTimes = answerRightTimes;
	}
	public int[] getAnswerRightTimes() {
		return answerRightTimes;
	}
	public void setAnswerWrongTimes(int[] answerWrongTimes) {
		this.answerWrongTimes = answerWrongTimes;
	}
	public int[] getAnswerWrongTimes() {
		return answerWrongTimes;
	}
	public void setLastValidateState(int lastValidateState) {
		this.lastValidateState = lastValidateState;
	}
	public int getLastValidateState() {
		return lastValidateState;
	}
	public void setLastValidateTime(long lastValidateTime) {
		this.lastValidateTime = lastValidateTime;
	}
	public long getLastValidateTime() {
		return lastValidateTime;
	}
	public void setDirectPassCount(int directPassCount) {
		this.directPassCount = directPassCount;
	}
	public int getDirectPassCount() {
		return directPassCount;
	}
	public void setAnswerTimes(int[] answerTimes) {
		this.answerTimes = answerTimes;
	}
	public int[] getAnswerTimes() {
		return answerTimes;
	}

	public void setLastanswerWrongTimes(int lastanswerWrongTimes) {
		this.lastanswerWrongTimes = lastanswerWrongTimes;
	}

	public int getLastanswerWrongTimes() {
		return lastanswerWrongTimes;
	}

	public void setLastDirectPassCount(int lastDirectPassCount) {
		this.lastDirectPassCount = lastDirectPassCount;
	}

	public int getLastDirectPassCount() {
		return lastDirectPassCount;
	}

	public void setLastGetTime(long lastGetTime) {
		this.lastGetTime = lastGetTime;
	}

	public long getLastGetTime() {
		return lastGetTime;
	}

	public int getTodayRihgtTimes() {
		return todayRihgtTimes;
	}

	public void setTodayRihgtTimes(int todayRihgtTimes) {
		this.todayRihgtTimes = todayRihgtTimes;
	}
	
}
