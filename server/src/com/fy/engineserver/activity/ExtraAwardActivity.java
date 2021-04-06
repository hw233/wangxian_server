package com.fy.engineserver.activity;

import java.util.Arrays;
import java.util.LinkedList;
import java.util.List;

import com.fy.engineserver.activity.shop.ActivityProp;
import com.fy.engineserver.sprite.Player;

public class ExtraAwardActivity extends BaseActivityInstance {
	private String type;
	private String[] name;
	private String[] name_stat;
	private boolean needScore;
	private int score;
	private boolean exact;

	private int levelLimit;

	private ActivityProp[] props;
	private String mailTitle;
	private String mailContent;

	public ExtraAwardActivity(String startTime, String endTime, String platForms, String openServerNames, String notOpenServers) throws Exception {
		super(startTime, endTime, platForms, openServerNames, notOpenServers);
	}

	public void setOtherVar(String type, String[] name, String[] nameStat, boolean needScore, int score, boolean exact, int levelLimit, ActivityProp[] props, String mailTitle, String mailContent) {
		this.type = type;
		this.name = name;
		this.name_stat = nameStat;
		this.needScore = needScore;
		this.score = score;
		this.exact = exact;
		this.levelLimit = levelLimit;
		this.props = props;
		this.mailTitle = mailTitle;
		this.mailContent = mailContent;
	}

	public void doAward(Player p) {
		List<Player> players = new LinkedList<Player>();
		players.add(p);
		ActivityManager.sendMailForActivity(players, props, mailTitle, mailContent, "日常任务额外奖励活动");
	}

	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}

	public String[] getName() {
		return name;
	}

	public void setName(String[] name) {
		this.name = name;
	}

	public String[] getName_stat() {
		return name_stat;
	}

	public void setName_stat(String[] nameStat) {
		name_stat = nameStat;
	}

	public boolean isNeedScore() {
		return needScore;
	}

	public void setNeedScore(boolean needScore) {
		this.needScore = needScore;
	}

	public int getScore() {
		return score;
	}

	public void setScore(int score) {
		this.score = score;
	}

	public boolean isExact() {
		return exact;
	}

	public void setExact(boolean exact) {
		this.exact = exact;
	}

	public int getLevelLimit() {
		return levelLimit;
	}

	public void setLevelLimit(int levelLimit) {
		this.levelLimit = levelLimit;
	}

	public ActivityProp[] getProps() {
		return props;
	}

	public void setProps(ActivityProp[] props) {
		this.props = props;
	}

	public String getMailTitle() {
		return mailTitle;
	}

	public void setMailTitle(String mailTitle) {
		this.mailTitle = mailTitle;
	}

	public String getMailContent() {
		return mailContent;
	}

	public void setMailContent(String mailContent) {
		this.mailContent = mailContent;
	}

	@Override
	public String getInfoShow() {
		// TODO Auto-generated method stub
		String award="";
		for(ActivityProp ap:props){
			award+="[{"+ap.getArticleCNName()+"}{color:"+ap.getArticleColor()+"}{num:"+ap.getArticleNum()+"}{"+ap.isBind()+"}]";
		}
		return "任务额外奖励-"+Arrays.toString(name_stat)+award;
	}
}
