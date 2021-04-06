package com.fy.engineserver.sprite.horse2.instance;

import java.util.ArrayList;
import java.util.List;

import com.fy.engineserver.sprite.horse2.manager.Horse2EntityManager;
import com.fy.engineserver.sprite.horse2.manager.Horse2Manager;
import com.fy.engineserver.util.TimeTool;
import com.fy.engineserver.util.TimeTool.TimeDistance;
import com.xuanzhi.tools.cache.CacheListener;
import com.xuanzhi.tools.cache.Cacheable;
import com.xuanzhi.tools.simplejpa.annotation.SimpleEntity;
import com.xuanzhi.tools.simplejpa.annotation.SimpleId;
import com.xuanzhi.tools.simplejpa.annotation.SimplePostPersist;
import com.xuanzhi.tools.simplejpa.annotation.SimpleVersion;

/**
 * 新坐骑相关数据
 * @author Administrator
 *
 */
@SimpleEntity
public class Horse2RelevantEntity  implements Cacheable, CacheListener{
	@SimpleId
	private long id;		//playerId
	@SimpleVersion
	private int version;
	/** 当天刷新技能次数(每天重置) */
	private int refreshSkillTime;
	/** 当天花钱阶培养次数(每天重置) */
	private int cultureTime;
	/** 当天免费阶培养次数(每天重置) */
	private int cultureTime4Free;
	/** 临时技能（刷出来没替换的）list */
	private List<Integer> tempSkillList = new ArrayList<Integer>();
	/** 对应坐骑id(与临时技能对应) */
	private List<Long> tempHorseId = new ArrayList<Long>();
	/** 上次重置时间 */
	private long lastInitTime = -1;
	/** 容错处理，两次重置时间间隔不能少于24小时 */
	public static long intevelTime = 24 * 60 * 60 * 1000;
	public Horse2RelevantEntity(){}
	
	public Horse2RelevantEntity(long playerId) {
		this.id = playerId;
	}
	
	/**
	 * 每天定点初始化
	 */
	public synchronized void initData() {
		long now = System.currentTimeMillis();
		boolean result = TimeTool.instance.isSame(now, lastInitTime, TimeDistance.DAY, 1);
		if(!result) {
			this.refreshSkillTime = 0;
			this.cultureTime = 0;
			cultureTime4Free = 0;
			lastInitTime = now;
			Horse2EntityManager.em.notifyFieldChange(this, "cultureTime4Free");
			Horse2EntityManager.em.notifyFieldChange(this, "refreshSkillTime");
			Horse2EntityManager.em.notifyFieldChange(this, "cultureTime");
			Horse2EntityManager.em.notifyFieldChange(this, "lastInitTime");
			Horse2EntityManager.logger.warn("[新坐骑系统] [重置玩家培养次数] [成功] [playerId:" + id + "] [上次重置时间:" + lastInitTime + "]");
		} else {
			if (Horse2EntityManager.logger.isDebugEnabled()) {
				Horse2EntityManager.logger.debug("[新坐骑系统] [重置玩家培养次数失败] [playerId:" + id + "] [上次重置时间:" + lastInitTime + "]");
			}
		}
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
	}

	@Override
	public void remove(int arg0) {
		// TODO Auto-generated method stub
		if (arg0 == CacheListener.LIFT_TIMEOUT) {
			Horse2EntityManager.instance.notifyRemoveFromCache(this);
		}
	}

	@Override
	public int getSize() {
		// TODO Auto-generated method stub
		return 1;
	}

	public int getRefreshSkillTime() {
		return refreshSkillTime;
	}
	
	@SimplePostPersist
	public void saved() {
		Horse2RelevantEntity dt = Horse2EntityManager.instance.tempCache.remove(this.getId());
		if(Horse2Manager.logger.isDebugEnabled()) {
			Horse2Manager.logger.debug("[移除Horse2RelevantEntity] [" + dt + "] [" + this + "]");
		}
	}

	public void setRefreshSkillTime(int refreshSkillTime) {
		this.refreshSkillTime = refreshSkillTime;
		Horse2EntityManager.em.notifyFieldChange(this, "refreshSkillTime");
	}

	public int getCultureTime() {
		return cultureTime;
	}

	public void setCultureTime(int cultureTime) {
		this.cultureTime = cultureTime;
		Horse2EntityManager.em.notifyFieldChange(this, "cultureTime");
	}

	public List<Integer> getTempSkillList() {
		return tempSkillList;
	}

	public void setTempSkillList(List<Integer> tempSkillList) {
		this.tempSkillList = tempSkillList;
		Horse2EntityManager.em.notifyFieldChange(this, "tempSkillList");
	}

	public long getLastInitTime() {
		return lastInitTime;
	}

	public void setLastInitTime(long lastInitTime) {
		this.lastInitTime = lastInitTime;
	}

	public List<Long> getTempHorseId() {
		return tempHorseId;
	}

	public void setTempHorseId(List<Long> tempHorseId) {
		this.tempHorseId = tempHorseId;
		Horse2EntityManager.em.notifyFieldChange(this, "tempHorseId");
	}

	public int getCultureTime4Free() {
		return cultureTime4Free;
	}

	public void setCultureTime4Free(int cultureTime4Free) {
		this.cultureTime4Free = cultureTime4Free;
		Horse2EntityManager.em.notifyFieldChange(this, "cultureTime4Free");
	}

}
