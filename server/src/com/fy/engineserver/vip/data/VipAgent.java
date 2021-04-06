package com.fy.engineserver.vip.data;

import java.util.Calendar;

import com.fy.engineserver.platform.PlatformManager;
import com.fy.engineserver.platform.PlatformManager.Platform;
import com.fy.engineserver.sprite.Player;
import com.fy.engineserver.vip.VipManager;
import com.fy.engineserver.vip.vipinfo.VipInfoRecordManager;
import com.xuanzhi.boss.game.GameConstants;
import com.xuanzhi.tools.simplejpa.annotation.SimpleEmbeddable;

/**
 * 角色身上的VIP代理,存储在player中
 */
@SimpleEmbeddable
public class VipAgent {

	private transient Player owner;

	/** 最后一次记录时间 */
	private long lastRecordtime = 0L;
	/** 最后一次抽奖时间 */
	private long lastLotterytime = 0L;

	public VipAgent() {
		this.setLastLotterytime(0L);
		this.setLastRecordtime(0L);
	}

	public long getLastRecordtime() {
		return lastRecordtime;
	}

	public void setLastRecordtime(long lastRecordtime) {
		this.lastRecordtime = lastRecordtime;
	}

	public long getLastLotterytime() {
		return lastLotterytime;
	}

	public void setLastLotterytime(long lastLotterytime) {
		this.lastLotterytime = lastLotterytime;
	}

	public boolean hasRecorded() {
		return this.lastRecordtime > 0;
	}

	/**
	 * 是否可以抽奖
	 * @return
	 */
	public boolean canLottery(Calendar now) {
		if (owner.getVipLevel() < VipManager.recordVipLevel) {
			return false;
		}
		Calendar lastLottery = Calendar.getInstance();
		lastLottery.setTimeInMillis(lastLotterytime);
		if (now.before(lastLottery)) {
			return false;
		}
		if (now.get(Calendar.YEAR) < lastLottery.get(Calendar.YEAR)) {
			return false;
		}
		if (now.get(Calendar.YEAR) == lastLottery.get(Calendar.YEAR)) {
			if (now.get(Calendar.MONTH) <= lastLottery.get(Calendar.MONTH)) {
				return false;
			}
		}
		if (now.get(Calendar.YEAR) > lastLottery.get(Calendar.YEAR)) {
			return true;
		}
		return true;
	}

	public Player getOwner() {
		return owner;
	}

	public void setOwner(Player owner) {
		this.owner = owner;
	}

	/**
	 * 是否需要录入信息
	 * 之前没有录入过的
	 * @return
	 */
	public boolean needRecord() {
		if (!PlatformManager.getInstance().isPlatformOf(Platform.官方)) {
			return false;
		}
		if(GameConstants.getInstance().getServerName().equals("客户端测试") || GameConstants.getInstance().getServerName().equals("飘渺寻仙") ){
			return VipInfoRecordManager.isOpenRecord && lastRecordtime <= 0 && owner.getVipLevel() >= VipManager.recordVipLevel2;
			
		}else{
			
			return VipInfoRecordManager.isOpenRecord && lastRecordtime <= 0 && owner.getVipLevel() >= VipManager.recordVipLevel;
		}
	}

	/**
	 * 是否有权限录入信息
	 * 1)主设备
	 * 2)授权超过30天
	 * @return
	 */
	public boolean hasAuthority() {

		if (!PlatformManager.getInstance().isPlatformOf(Platform.官方)) {
			return false;
		}

		long startTime = System.currentTimeMillis();
		if (VipManager.isTest) {
			return true;
		}
		String clientId = owner.getClientId();
		if (clientId == null || "".equals(clientId.trim())) {
			if (VipManager.logger.isInfoEnabled()) {
				VipManager.logger.info("[VIP] [查询是否有权限修改信息] [不可以] [" + owner.getUsername() + "] [clientId:" + clientId + "]");
			}
			return false;
		}
		boolean res = owner.modifyVipInfo(clientId);
		if (VipManager.logger.isInfoEnabled()) {
			VipManager.logger.info("[VIP] [查询是否有权限修改信息] [网关结果] [" + owner.getUsername() + "] [" + clientId + "] [" + res + "] [cost:" + (System.currentTimeMillis() - startTime) + "ms]");
		}
		return res;
	}

	@Override
	public String toString() {
		return "VipAgent [lastRecordtime=" + lastRecordtime + ", lastLotterytime=" + lastLotterytime + ", getLastRecordtime()=" + getLastRecordtime() + ", getLastLotterytime()=" + getLastLotterytime() + ", getOwner()=" + getOwner() + ", needRecord()=" + needRecord() + "]";
	}
}