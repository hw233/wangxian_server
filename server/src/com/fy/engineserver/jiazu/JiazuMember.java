package com.fy.engineserver.jiazu;

import java.util.Calendar;
import java.util.GregorianCalendar;

import com.fy.engineserver.achievement.RecordAction;
import com.fy.engineserver.core.JiazuSubSystem;
import com.fy.engineserver.economic.BillingCenter;
import com.fy.engineserver.economic.CurrencyType;
import com.fy.engineserver.economic.SavingReasonType;
import com.fy.engineserver.event.EventRouter;
import com.fy.engineserver.event.cate.EventWithObjParam;
import com.fy.engineserver.jiazu.service.JiazuManager;
import com.fy.engineserver.playerAims.manager.PlayerAimManager;
import com.fy.engineserver.sprite.Player;
import com.fy.engineserver.sprite.PlayerManager;
import com.fy.engineserver.sprite.concrete.GamePlayerManager;
import com.xuanzhi.tools.simplejpa.annotation.SimpleColumn;
import com.xuanzhi.tools.simplejpa.annotation.SimpleEntity;
import com.xuanzhi.tools.simplejpa.annotation.SimpleId;
import com.xuanzhi.tools.simplejpa.annotation.SimpleIndex;
import com.xuanzhi.tools.simplejpa.annotation.SimpleIndices;
import com.xuanzhi.tools.simplejpa.annotation.SimplePostLoad;
import com.xuanzhi.tools.simplejpa.annotation.SimpleVersion;

/**
 * 
 * 家族成员
 * 
 * 
 * 
 * 
 */
@SimpleEntity
@SimpleIndices({ @SimpleIndex(members = { "jiazuID", "playerID" }), @SimpleIndex(name = "idTitle",
	members = { "jiazuID", "titleIndex" }) })
public class JiazuMember {
	/** 家族ID */
	private long jiazuID;
	/** 家族成员ID */
	@SimpleId
	private long jiazuMemID;
	@SimpleVersion
	private int version;

	/** 家族成员职务 */
	private transient JiazuTitle title;
	/** 家族职务ID */
	@SimpleColumn(saveInterval = 30)
	private int titleIndex;
	/** 玩家的id */
	private long playerID;
	/** 家族工资 * 总工资 */
	@SimpleColumn(saveInterval = 120)
	private long jiazuSalary;
	/** 领取工资历史记录 */
	@SimpleColumn(saveInterval = 120)
	private long totalJiazuSalary;
	/** 上一次领取工资 */
	@SimpleColumn(saveInterval = 120)
	private long lastSalary;
	/** 本周领取工资 */
	@SimpleColumn(saveInterval = 120)
	private long currentSalary;
	/** 最后一次领取工资的时间 */
	@SimpleColumn(saveInterval = 120)
	private long lastPaidDay;
	/** 个人总贡献度 */
	@SimpleColumn(saveInterval = 120)
	private long totalcontribution;
	/** 本周的贡献值 */
	@SimpleColumn(saveInterval = 120)
	private int currentWeekContribution;
	/** 个人上周总贡献度 */
	@SimpleColumn(saveInterval = 120)
	private long lastWeekContribution;
	/** 是否已经发工资 */
	@SimpleColumn(saveInterval = 120)
	private boolean isPaid;
	/** 捐献的钱,累计 */
	private long contributeMoney;
	/** 本周能领取的工资上限 */
	private long currentWeekMaxSalary;
	/** 最后一次完成建设任务时间 */
	private long lastDeliverBuildingTaskTime;
	/** 完成建设任务时间 */
	private int deliverBuildingTaskNum;
	/** 最后一次离线时间 */
	private long lastOffLineTime;

	public long getLastPaidDay() {
		return lastPaidDay;
	}

	public void setLastPaidDay(long lastPaidDay) {
		this.lastPaidDay = lastPaidDay;
		notifyFieldChange("lastPaidDay");
	}

	public long getLastSalary() {
		return lastSalary;
	}

	public void setLastSalary(long lastSalary) {
		this.lastSalary = lastSalary;
		notifyFieldChange("lastSalary");
	}

	private void notifyFieldChange(String fieldName) {
		JiazuManager.memberEm.notifyFieldChange(this, fieldName);
	}

	/**
	 * 返回这周是否已经发了工资了
	 * 
	 * @return
	 */
	public boolean isPaidthisweek() {
		if (!isPaid) return false;
		else if (this.lastPaidDay == 0) return false;
		else {
			Calendar last = new GregorianCalendar();
			last.setTimeInMillis(this.lastPaidDay);
			Calendar now = new GregorianCalendar();
			if (last.get(Calendar.WEEK_OF_YEAR) != now.get(Calendar.WEEK_OF_YEAR)) {
				return false;
			}
		}
		return true;
	}

	@SimplePostLoad
	public void loadOver() {
		JiazuTitle jiazuTitle = JiazuManager.jiazuTitles.get(titleIndex);
		setTitle(jiazuTitle);
		if (jiazuTitle == null) {
			JiazuManager.logger.error("[从dbload] [家族职务异常] [null] [titleIndex:{}]", new Object[] { titleIndex });
		}
	}

	public int getVersion() {
		return version;
	}

	public void setVersion(int version) {
		this.version = version;
	}

	public int getCurrentWeekContribution() {
		return currentWeekContribution;
	}

	public void setCurrentWeekContribution(int currentWeekContribution) {
		this.currentWeekContribution = currentWeekContribution;
		notifyFieldChange("currentWeekContribution");
		{
			// 设置人身上的贡献度
			if (GamePlayerManager.getInstance().isOnline(playerID)) {
				Player player = null;
				try {
					player = GamePlayerManager.getInstance().getPlayer(playerID);
				} catch (Exception e) {
					e.printStackTrace();
				}
				if (player != null) {
					player.setGangContribution(this.getCurrentWeekContribution());
				}
			}
		}
	}

	public long getTotalcontribution() {
		return totalcontribution;
	}

	public void setTotalcontribution(long totalcontribution) {
		this.totalcontribution = totalcontribution;
		notifyFieldChange("totalcontribution");
	}

	public long getLastWeekContribution() {
		return lastWeekContribution;
	}

	public void setLastWeekContribution(long lastWeekContribution) {
		this.lastWeekContribution = lastWeekContribution;
		notifyFieldChange("lastWeekContribution");
	}

	public long getJiazuSalary() {
		return jiazuSalary;
	}

	public void setJiazuSalary(long jiazuSalary) {
		this.jiazuSalary = jiazuSalary;
		notifyFieldChange("jiazuSalary");
	}

	public long getCurrentSalary() {
		return currentSalary;
	}

	public void setCurrentSalary(long currentSalary) {
		this.currentSalary = currentSalary;
		notifyFieldChange("currentSalary");
	}

	public long getTotalJiazuSalary() {
		return totalJiazuSalary;
	}

	public void setTotalJiazuSalary(long totalJiazuSalary) {
		this.totalJiazuSalary = totalJiazuSalary;
		notifyFieldChange("totalJiazuSalary");
	}

	public int getTitleIndex() {
		return titleIndex;
	}

	public void setTitleIndex(int titleIndex) {
		this.titleIndex = titleIndex;
		notifyFieldChange("titleIndex");
	}

	/**
	 * 接收本周的薪水
	 * @param salary
	 */
	/**
	 * @param salary
	 */
	public synchronized void addSalary(long salary) {
		try {
			this.setPaid(true);
			this.setJiazuSalary(this.jiazuSalary + salary);
			this.setTotalJiazuSalary(this.totalJiazuSalary + salary);
			this.setCurrentSalary(this.getCurrentSalary() + salary);
			// 发薪的时间
			this.setLastPaidDay(com.fy.engineserver.gametime.SystemTime.currentTimeMillis());
			Player player = PlayerManager.getInstance().getPlayer(getPlayerID());
			BillingCenter.getInstance().playerSaving(player, salary, CurrencyType.GONGZI, SavingReasonType.JIAZU_GONGZI, "工资");
			// player.setWage(player.getWage() + salary);
			try {
				EventWithObjParam evt3 = new EventWithObjParam(com.fy.engineserver.event.Event.RECORD_PLAYER_OPT, new Object[] { player.getId(), RecordAction.领取家族工资次数, 1L});
				EventRouter.getInst().addEvent(evt3);
		} catch (Exception eex) {
			PlayerAimManager.logger.error("[目标系统] [统计玩家领取家族工资异常] [" + player.getLogString() + "]", eex);
		}
		} catch (Exception e) {
			JiazuSubSystem.logger.error("[给家族成员加工资异常] [角色ID:{}] [工资:{}]", new Object[] { getPlayerID(), salary }, e);
		}
	}

	public long getPlayerID() {
		return playerID;
	}

	public void setPlayerID(long playerID) {
		this.playerID = playerID;
		notifyFieldChange("playerID");
	}

	public static long count = 1;

	public JiazuMember() {

	}

	public long getJiazuID() {
		return jiazuID;
	}

	public void setJiazuID(long jiazuID) {
		this.jiazuID = jiazuID;
		notifyFieldChange("jiazuID");
	}

	public long getJiazuMemID() {
		return jiazuMemID;
	}

	public void setJiazuMemID(long jiazuMemID) {
		this.jiazuMemID = jiazuMemID;
	}

	public JiazuTitle getTitle() {
		return title;
	}

	public void setTitle(JiazuTitle title) {
		this.title = title;
		setTitleIndex(title.ordinal());
	}

	public boolean isPaid() {
		return isPaid;
	}

	public void setPaid(boolean isPaid) {
		this.isPaid = isPaid;
		notifyFieldChange("isPaid");
	}

	public boolean equals(JiazuMember mem) {
		if (mem.getJiazuMemID() == this.jiazuMemID) return true;
		return false;
	}

	public int hashCode() {
		return (int) (this.getJiazuMemID());
	}

	public long getContributeMoney() {
		return contributeMoney;
	}

	public void setContributeMoney(long contributeMoney) {
		this.contributeMoney = contributeMoney;
		notifyFieldChange("contributeMoney");
	}

	public long getCurrentWeekMaxSalary() {
		return currentWeekMaxSalary;
	}

	public void setCurrentWeekMaxSalary(long currentWeekMaxSalary) {
		this.currentWeekMaxSalary = currentWeekMaxSalary;
		notifyFieldChange("currentWeekMaxSalary");
	}

	public long getLastDeliverBuildingTaskTime() {
		return lastDeliverBuildingTaskTime;
	}

	public void setLastDeliverBuildingTaskTime(long lastDeliverBuildingTaskTime) {
		this.lastDeliverBuildingTaskTime = lastDeliverBuildingTaskTime;
		notifyFieldChange("lastDeliverBuildingTaskTime");
	}

	public int getDeliverBuildingTaskNum() {
		return deliverBuildingTaskNum;
	}

	public void setDeliverBuildingTaskNum(int deliverBuildingTaskNum) {
		this.deliverBuildingTaskNum = deliverBuildingTaskNum;
		notifyFieldChange("deliverBuildingTaskNum");
	}

	public long getLastOffLineTime() {
		return lastOffLineTime;
	}

	public void setLastOffLineTime(long lastOffLineTime) {
		this.lastOffLineTime = lastOffLineTime;
		notifyFieldChange("lastOffLineTime");
	}

	@Override
	public String toString() {
		return "JiazuMember [jiazuID=" + jiazuID + ", jiazuMemID=" + jiazuMemID + ", version=" + version + ", titleIndex=" + titleIndex + ", playerID=" + playerID + ", jiazuSalary=" + jiazuSalary + ", totalJiazuSalary=" + totalJiazuSalary + ", lastSalary=" + lastSalary + ", currentSalary=" + currentSalary + ", lastPaidDay=" + lastPaidDay + ", totalcontribution=" + totalcontribution + ", currentWeekContribution=" + currentWeekContribution + ", lastWeekContribution=" + lastWeekContribution + ", isPaid=" + isPaid + ", contributeMoney=" + contributeMoney + "]";
	}

}
