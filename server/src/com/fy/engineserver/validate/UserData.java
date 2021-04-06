package com.fy.engineserver.validate;

import java.util.Calendar;

public class UserData {
	// user对应的数据，记录都是当天的数据

	public String userName;
	public long playerId;
	public String playerName;

	public int answerRightTimes = 0;			//答题正确次数
	public int answerWrongTimes = 0;			//答题错误次数
	public int takeTaskCount = 0;				//接取任务次数
	public int requestBuySaleCount = 0;

	public int _needAnswerTimes = 0;
	public int _answerRightTimes = 0;
	public int _answerWrongTimes = 0;
	
	//直接通过检查次数
	public int directPassCount = 0;

	// 当前正在回答的题，可以是null，非NULL标识正在答题
	public ValidateAsk currentAsk;
	public String reason = "";

	public long lastModifyTime = 0;

	public boolean update() {
		long now = System.currentTimeMillis();

		Calendar cal = Calendar.getInstance();
		cal.setTimeInMillis(lastModifyTime);
		int y1 = cal.get(Calendar.YEAR);
		int d1 = cal.get(Calendar.DAY_OF_YEAR);

		cal.setTimeInMillis(now);
		int y2 = cal.get(Calendar.YEAR);
		int d2 = cal.get(Calendar.DAY_OF_YEAR);

		if (y1 != y2 || d1 != d2) {
			answerRightTimes--;
			if(answerRightTimes < 0) answerRightTimes = 0;
			directPassCount = 0;
			answerWrongTimes = 0;
			takeTaskCount = 0;
			requestBuySaleCount = 0;
		}

		if (d1 + 2 < d2) return true;
		return false;
	}
}
