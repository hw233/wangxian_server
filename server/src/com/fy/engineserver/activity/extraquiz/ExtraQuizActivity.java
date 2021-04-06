package com.fy.engineserver.activity.extraquiz;

import java.util.Arrays;

import com.fy.engineserver.activity.BaseActivityInstance;
import com.fy.engineserver.util.CompoundReturn;
/**
 * 答题活动（配表控制可增加每日答题次数、题目数量以及答题奖励--只针对活动新开启的答题，对原有答题活动无影响）
 *
 */
public class ExtraQuizActivity extends BaseActivityInstance{
	/** 额外答题开是时间(小时) */
	private int[] beginHours;
	/** 额外答题开始时间（分） **/
	private int[] beginMinits;
	/** 此次答题活动开启题目数量 */
	private int[] quizNums;
	/**  答对多少道题以上给奖励 */
	private int[] rightNums;

	private RewardArticles[] rewardArticleInfos;
	
	@Override
	public String toString() {
		return "ExtraQuizActivity [beginHours=" + Arrays.toString(beginHours) + ", beginMinits=" + Arrays.toString(beginMinits) + ", quizNums=" + Arrays.toString(quizNums) + ", rightNums=" + Arrays.toString(rightNums) + ", rewardArticleInfos=" + Arrays.toString(rewardArticleInfos) + "]";
	}
	public ExtraQuizActivity(String startTime, String endTime, String platForms, String openServerNames, String notOpenServers) throws Exception {
		super(startTime, endTime, platForms, openServerNames, notOpenServers);
		// TODO Auto-generated constructor stub
	}
	/**
	 * 
	 * @param openTimes   格式： HH:MM|HH2:MM2
	 * @param quizNums		
	 * @param rightNums
	 * @param rewardArticles 格式：articleCNName1.num1;articleCNName2.num2|articleCNName1.num1:articleCNName2.num2
	 * @throws Exception 
	 */
	public void initOtherVar(String openTimes, String quizNums, String rightNums, String rewardArticles) throws Exception {
		if (openTimes == null || openTimes.isEmpty()) {
			return ;
		}
		String[] ots = openTimes.split("\\|");
		String[] quizs = quizNums.split(",");
		String[] rNums = rightNums.split(",");
		String[] ats = rewardArticles.split("\\|");
		this.beginHours = new int[ots.length];
		this.beginMinits = new int[ots.length];
		this.quizNums = new int[ots.length];
		this.rightNums = new int[ots.length];
		this.rewardArticleInfos = new RewardArticles[ots.length];
		for (int i=0; i<ots.length; i++) {
			String[] t = ots[i].split(":");
			if (t.length != 2) {
				throw new Exception("[时间格式错误] [" + openTimes + "]");
			}
			this.beginHours[i] = Integer.parseInt(t[0]);
			this.beginMinits[i] = Integer.parseInt(t[1]);
			this.quizNums[i] = Integer.parseInt(quizs[i]);
			this.rightNums[i] = Integer.parseInt(rNums[i]);
			this.rewardArticleInfos[i] = new RewardArticles(ats[i]);
		}
	}
	
	/**
	 * 额外活动是否开启
	 * @param currentHour
	 * @param currentMinit
	 * @return
	 */
	public CompoundReturn getExtraQuizInfo (int currentHour, int currentMinit) {
		CompoundReturn cr = new CompoundReturn();
		cr.setBooleanValue(false);
		if (beginHours == null || beginHours.length <= 0) {
			return cr;
		}
		for (int i=0; i<beginHours.length; i++) {
			if (currentHour == beginHours[i] && (currentMinit - beginMinits[i] <= 2 && currentMinit - beginMinits[i] >= 0)) {
				cr.setBooleanValue(true).setIntValue(quizNums[i]).setObjValue(rewardArticleInfos[i]).setLongValue(rightNums[i]);
				return cr;
			}
		}
		return cr;
	}
	
	@Override
	public String getInfoShow() {
		// TODO Auto-generated method stub
		StringBuffer sb = new StringBuffer();
		sb.append("[额外答题活动]<br>");
		if (beginHours != null && beginHours.length > 0) {
			sb.append("[开启时间:");
			for (int i=0; i<beginHours.length; i++) {
				sb.append(beginHours[i] + ":" + beginMinits[i]);
				sb.append("]");
				sb.append("[题目数量:"+quizNums[i]+"]");
				sb.append("[需要答对题目数量:"+rightNums[i]+"]");
				sb.append("[奖励物品:"+rewardArticleInfos[i]+"]");
				if (i < beginHours.length) {
					sb.append("<br>");
				}
			}
		}
		return sb.toString();
	}

	public int[] getBeginHours() {
		return beginHours;
	}

	public void setBeginHours(int[] beginHours) {
		this.beginHours = beginHours;
	}

	public int[] getBeginMinits() {
		return beginMinits;
	}

	public void setBeginMinits(int[] beginMinits) {
		this.beginMinits = beginMinits;
	}

	public int[] getRightNums() {
		return rightNums;
	}

	public void setRightNums(int[] rightNums) {
		this.rightNums = rightNums;
	}

	public int[] getQuizNums() {
		return quizNums;
	}

	public void setQuizNums(int[] quizNums) {
		this.quizNums = quizNums;
	}
	public RewardArticles[] getRewardArticleInfos() {
		return rewardArticleInfos;
	}
	public void setRewardArticleInfos(RewardArticles[] rewardArticleInfos) {
		this.rewardArticleInfos = rewardArticleInfos;
	}

	

}
