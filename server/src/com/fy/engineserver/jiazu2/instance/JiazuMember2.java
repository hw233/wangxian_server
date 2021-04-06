package com.fy.engineserver.jiazu2.instance;

import java.util.ArrayList;
import java.util.List;

import com.fy.engineserver.jiazu2.manager.JiazuEntityManager2;
import com.fy.engineserver.jiazu2.manager.JiazuManager2;
import com.fy.engineserver.sprite.Player;
import com.fy.engineserver.sprite.concrete.GamePlayerManager;
import com.fy.engineserver.util.TimeTool;
import com.fy.engineserver.util.TimeTool.TimeDistance;
import com.xuanzhi.tools.cache.CacheListener;
import com.xuanzhi.tools.cache.Cacheable;
import com.xuanzhi.tools.simplejpa.annotation.SimpleEntity;
import com.xuanzhi.tools.simplejpa.annotation.SimpleId;
import com.xuanzhi.tools.simplejpa.annotation.SimplePostPersist;
import com.xuanzhi.tools.simplejpa.annotation.SimpleVersion;
/**
 * 家族修改新加 
 * 
 *
 */
@SimpleEntity
public class JiazuMember2 implements Cacheable, CacheListener{
	@SimpleId
	private long id;		//playerId
	@SimpleVersion
	private int version;
	/** 家族修炼值 */
	private long practiceValue;
	/** 个人家族修炼具体 */
	private List<JiazuXiulian> xiulian = new ArrayList<JiazuXiulian>();
	/** 最后一次重置祈福次数时间*/
	private long lastResetTime;
	/** 当日祈福次数 每天清 */
	private int qifuTimes;
	/** 当日玩家击杀或助攻敌国其他家族镖车次数(每天固定前N次有奖励，每天清) */		//只有击杀敌国家族镖车才有奖励
	private int killBiaocheTimes;
	
	public static final long ONE_DAY = 60 * 60 * 24 * 1000L;
	/**
	 * 记录上次增加的属性，下次增加时需要先减掉
	 */
	private transient List<JiazuXiulian> oldXiuLian = null;
	
	public JiazuMember2() {
	}
	
	public JiazuMember2(long id) {
		super();
		this.id = id;
	}
	/**
	 * 每天初始化
	 */
	public synchronized void reset() {
		long now = System.currentTimeMillis();
		boolean result = TimeTool.instance.isSame(now, lastResetTime, TimeDistance.DAY);
		if(!result) {
//		if(now - lastResetTime >= ONE_DAY) {
			JiazuEntityManager2.logger.warn("[新坐骑系统] [重置玩家培养次数] [成功] [playerId:" + id + "] [上次重置时间:" + lastResetTime + "]");
			this.qifuTimes = 0;
			this.killBiaocheTimes = 0;
			lastResetTime = now;
			JiazuEntityManager2.em.notifyFieldChange(this, "qifuTimes");
			JiazuEntityManager2.em.notifyFieldChange(this, "lastResetTime");
			JiazuEntityManager2.em.notifyFieldChange(this, "killBiaocheTimes");
		} 
//		else {
//			if (JiazuEntityManager2.logger.isDebugEnabled()) {
//				JiazuEntityManager2.logger.debug("[新坐骑系统] [重置玩家培养次数失败] [playerId:" + id + "] [上次重置时间:" + lastResetTime + "]");
//			}
//		}
	}

	/**
	 * 
	 * @param skillId
	 * @return   null代表从来没有进行过对应技能的修炼
 	 */
	public JiazuXiulian getJiazuXiulianBySkillId(int skillId) {
		for (JiazuXiulian temp : xiulian) {
			if (temp.getSkillId() == skillId) {
				return temp;
			}
		}
		return null;
	}

	@Override
	public void remove(int paramInt) {
		// TODO Auto-generated method stub
		if (paramInt == CacheListener.LIFT_TIMEOUT) {
			JiazuEntityManager2.instance.notifyRemoveFromCache(this);
		}
	}
	
	@SimplePostPersist
	public void saved() {
		JiazuMember2 dt = JiazuEntityManager2.instance.tempCache.remove(this.getId());
		if(JiazuManager2.logger.isDebugEnabled()) {
			JiazuManager2.logger.debug("[移除JiazuMember2] [" + dt + "] [" + this + "]");
		}
	}

	@Override
	public int getSize() {
		// TODO Auto-generated method stub
		return 1;
	}

	public int getQifuTimes() {
		return qifuTimes;
	}

	public void setQifuTimes(int qifuTimes) {
		this.qifuTimes = qifuTimes;
		JiazuEntityManager2.em.notifyFieldChange(this, "qifuTimes");
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

	public long getPracticeValue() {
		return practiceValue;
	}

	public void setPracticeValue(long practiceValue) {
		this.practiceValue = practiceValue;
		JiazuEntityManager2.em.notifyFieldChange(this, "practiceValue");
		try {
			Player plo = GamePlayerManager.getInstance().getPlayer(this.getId());
			plo.setPracticeValue();
		} catch (Exception e) {
			
		}
	}

	public List<JiazuXiulian> getXiulian() {
		return xiulian;
	}

	public void setXiulian(List<JiazuXiulian> xiulian) {
		this.xiulian = xiulian;
		JiazuEntityManager2.em.notifyFieldChange(this, "xiulian");
	}

	public long getLastResetTime() {
		return lastResetTime;
	}

	public void setLastResetTime(long lastResetTime) {
		this.lastResetTime = lastResetTime;
	}

	public List<JiazuXiulian> getOldXiuLian() {
		return oldXiuLian;
	}

	public void setOldXiuLian(List<JiazuXiulian> oldXiuLian) {
		this.oldXiuLian = oldXiuLian;
	}

	public int getKillBiaocheTimes() {
		return killBiaocheTimes;
	}

	public void setKillBiaocheTimes(int killBiaocheTimes) {
		this.killBiaocheTimes = killBiaocheTimes;
		JiazuEntityManager2.em.notifyFieldChange(this, "killBiaocheTimes");
	}


}
