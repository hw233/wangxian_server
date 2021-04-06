package com.fy.engineserver.jiazu;

import com.fy.engineserver.core.JiazuSubSystem;
import com.fy.engineserver.jiazu.service.JiazuManager;
import com.fy.engineserver.sprite.PlayerManager;
import com.fy.engineserver.sprite.PlayerSimpleInfo;
import com.fy.engineserver.sprite.PlayerSimpleInfoManager;
import com.fy.engineserver.sprite.concrete.GamePlayerManager;

/**
 * 发送给客户端的家族成员
 * 
 * 
 */
public class JiazuMember4Client implements Comparable<JiazuMember4Client> {

	private String playerName = "";

	private long playerId;

	private int title;

	private int playerLevel;

	private byte sex;
	/** 捐献的钱总数 */
	private long contributeMoney;
	/** 贡献值 */
	private int contributionSalary;
	/** 境界 */
	private int classLevel;
	/** 职业(本尊) */
	private int career;
	/** 是否在线 */
	private boolean onLine;

	private JiazuMember jiazuMember;

	public JiazuMember4Client() {
		// TODO Auto-generated constructor stub
	}

	public JiazuMember4Client(JiazuMember jiazuMember) {
		PlayerSimpleInfo ps = null;
		try {
			ps = PlayerSimpleInfoManager.getInstance().getInfoById(jiazuMember.getPlayerID());
		} catch (Exception e) {
			JiazuManager.logger.error("[初始化jiazuMember=" + jiazuMember + "] [异常] [PlayerManager.getInstance()=" + PlayerManager.getInstance() + "]", e);
		}
		if (ps != null) {
			this.playerId = jiazuMember.getPlayerID();
			this.playerLevel = ps.getLevel();
			this.playerName = ps.getName();
			this.title = jiazuMember.getTitleIndex();
			this.sex = ps.getSex();
			this.contributionSalary = jiazuMember.getCurrentWeekContribution();
			this.career = ps.getCareer();
			this.classLevel = ps.getClassLevel();
			this.onLine = GamePlayerManager.getInstance().isOnline(jiazuMember.getPlayerID());
			this.contributeMoney = jiazuMember.getContributeMoney();
			this.jiazuMember = jiazuMember;
		} else {
			JiazuSubSystem.logger.error(jiazuMember.getPlayerID() + "[初始化JiazuMember4Client异常] [PlayerSimpleInfo=null]");
		}
	}

	@Override
	public int compareTo(JiazuMember4Client o) {
		return this.getTitle() - o.getTitle();
	}

	public int getPlayerLevel() {
		return playerLevel;
	}

	public void setPlayerLevel(int playerLevel) {
		this.playerLevel = playerLevel;
	}

	public String getPlayerName() {
		return playerName;
	}

	public void setPlayerName(String playerName) {
		this.playerName = playerName;
	}

	public long getPlayerId() {
		return playerId;
	}

	public void setPlayerId(long playerId) {
		this.playerId = playerId;
	}

	public int getTitle() {
		return title;
	}

	public void setTitle(int title) {
		this.title = title;
	}

	public byte getSex() {
		return sex;
	}

	public void setSex(byte sex) {
		this.sex = sex;
	}

	public int getContributionSalary() {
		return contributionSalary;
	}

	public void setContributionSalary(int contributionSalary) {
		this.contributionSalary = contributionSalary;
	}

	public int getClassLevel() {
		return classLevel;
	}

	public void setClassLevel(int classLevel) {
		this.classLevel = classLevel;
	}

	public int getCareer() {
		return career;
	}

	public void setCareer(int career) {
		this.career = career;
	}

	public boolean isOnLine() {
		return onLine;
	}

	public void setOnLine(boolean onLine) {
		this.onLine = onLine;
	}

	public long getContributeMoney() {
		return contributeMoney;
	}

	public void setContributeMoney(long contributeMoney) {
		this.contributeMoney = contributeMoney;
	}

	public JiazuMember getJiazuMember() {
		return jiazuMember;
	}

	public void setJiazuMember(JiazuMember jiazuMember) {
		this.jiazuMember = jiazuMember;
	}

	@Override
	public String toString() {
		return "JiazuMember4Client [playerName=" + playerName + ", playerId=" + playerId + ", title=" + title + ", playerLevel=" + playerLevel + ", sex=" + sex + ", contributeMoney=" + contributeMoney + ", contributionSalary=" + contributionSalary + ", classLevel=" + classLevel + ", career=" + career + ", onLine=" + onLine + "]";
	}
}
