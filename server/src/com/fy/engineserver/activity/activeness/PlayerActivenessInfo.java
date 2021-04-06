package com.fy.engineserver.activity.activeness;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Collections;
import java.util.Comparator;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;

import com.fy.engineserver.sprite.Player;
import com.fy.engineserver.sprite.PlayerManager;
import com.xuanzhi.tools.simplejpa.annotation.SimpleEntity;
import com.xuanzhi.tools.simplejpa.annotation.SimpleId;
import com.xuanzhi.tools.simplejpa.annotation.SimpleIndex;
import com.xuanzhi.tools.simplejpa.annotation.SimpleIndices;
import com.xuanzhi.tools.simplejpa.annotation.SimpleVersion;

@SimpleEntity
@SimpleIndices( { @SimpleIndex(members = { "dayActiveness" }) })
public class PlayerActivenessInfo {
	@SimpleId
	private long id;// PLAYERED
	@SimpleVersion
	private int version;
	private int dayActiveness; // 每日刷新
	// private int lastTotalActiveness;//记录当天零点刷新后的玩家总活跃度
	private int totalActiveness; // 活跃度每次变化都记录
	private boolean gotten[]; // 是否已领取
	private long lastGetTime; // 上次领奖时间
	private int hasLottery; // 每日刷新
	private int canLottery; // 每日刷新
	private long lastLotteryTime; // 上次抽奖时间
	private Map<Integer, Integer> doneNum; // <活动id,完成次数>
	private boolean hasSign[]; // 是否已签到
	private boolean hasGotSign[]; // 是否已领取签到奖励
	private long lastGetSignTime; // 上次领取签到奖励时间

	public PlayerActivenessInfo() {
	}

	public long getId() {
		return id;
	}

	public void setId(long id) {
		this.id = id;
	}

	public int getVersion() {
		return version;
	}

	public void setVersion(int version) {
		this.version = version;
		ActivenessManager.em.notifyFieldChange(this, "version");
	}

	public int getDayActiveness() {
		return dayActiveness;
	}

	public void setDayActiveness(int dayActiveness) {
		this.dayActiveness = dayActiveness;
		ActivenessManager.em.notifyFieldChange(this, "dayActiveness");
	}

	public int getTotalActiveness() {
		return totalActiveness;
	}

	public void setTotalActiveness(int totalActiveness) {
		if(totalActiveness>100000000){
			totalActiveness=100000000;
		}
		this.totalActiveness = totalActiveness;
		ActivenessManager.em.notifyFieldChange(this, "totalActiveness");
	}

	public boolean[] getGotten() {
		return gotten;
	}

	public void setGotten(boolean[] gotten) {
		this.gotten = gotten;
		ActivenessManager.em.notifyFieldChange(this, "gotten");
	}

	public long getLastGetTime() {
		return lastGetTime;
	}

	public void setLastGetTime(long lastGetTime) {
		this.lastGetTime = lastGetTime;
		ActivenessManager.em.notifyFieldChange(this, "lastGetTime");
	}

	public int getHasLottery() {
		return hasLottery;
	}

	public void setHasLottery(int hasLottery) {
		this.hasLottery = hasLottery;
		ActivenessManager.em.notifyFieldChange(this, "hasLottery");
	}

	public int getCanLottery() {
		return canLottery;
	}

	public void setCanLottery(int canLottery) {
		this.canLottery = canLottery;
		ActivenessManager.em.notifyFieldChange(this, "canLottery");
	}

	public long getLastLotteryTime() {
		return lastLotteryTime;
	}

	public void setLastLotteryTime(long lastLotteryTime) {
		this.lastLotteryTime = lastLotteryTime;
		ActivenessManager.em.notifyFieldChange(this, "lastLotteryTime");
	}

	public Map<Integer, Integer> getDoneNum() {
		return doneNum;
	}

	public void setDoneNum(Map<Integer, Integer> doneNum) {
		this.doneNum = doneNum;
		ActivenessManager.em.notifyFieldChange(this, "doneNum");
	}

	public boolean[] getHasSign() {
		return hasSign;
	}

	public void setHasSign(boolean[] hasSign) {
		this.hasSign = hasSign;
		ActivenessManager.em.notifyFieldChange(this, "hasSign");
	}

	public boolean[] getHasGotSign() {
		return hasGotSign;
	}

	public void setHasGotSign(boolean[] hasGotSign) {
		this.hasGotSign = hasGotSign;
		ActivenessManager.em.notifyFieldChange(this, "hasGotSign");
	}

	public long getLastGetSignTime() {
		return lastGetSignTime;
	}

	public void setLastGetSignTime(long lastGetSignTime) {
		this.lastGetSignTime = lastGetSignTime;
		ActivenessManager.em.notifyFieldChange(this, "lastGetSignTime");
	}

	@Override
	public String toString() {
		return "PlayerActivenessInfo [canLottery=" + canLottery + ", dayActiveness=" + dayActiveness + ", doneNum=" + doneNum + ", gotten=" + Arrays.toString(gotten) + ", hasGotSign=" + Arrays.toString(hasGotSign) + ", hasLottery=" + hasLottery + ", hasSign=" + Arrays.toString(hasSign) + ", id=" + id + ", lastGetSignTime=" + lastGetSignTime + ", lastGetTime=" + lastGetTime + ", lastLotteryTime=" + lastLotteryTime + ", totalActiveness=" + totalActiveness + ", version=" + version + "]";
	}

}
