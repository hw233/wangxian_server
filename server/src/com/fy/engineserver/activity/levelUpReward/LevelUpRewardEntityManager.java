package com.fy.engineserver.activity.levelUpReward;

import java.util.Hashtable;
import java.util.Map;

import org.slf4j.Logger;

import com.fy.engineserver.activity.levelUpReward.instance.LevelUpRewardEntity;
import com.fy.engineserver.activity.levelUpReward.instance.RewardInfo;
import com.fy.engineserver.sprite.Player;
import com.xuanzhi.tools.cache.LruMapCache;
import com.xuanzhi.tools.simplejpa.SimpleEntityManager;
import com.xuanzhi.tools.simplejpa.SimpleEntityManagerFactory;

public class LevelUpRewardEntityManager {
	public static Logger logger = LevelUpRewardManager.logger;
	
	public static LevelUpRewardEntityManager instance;
	
	public LruMapCache cache = new LruMapCache(10240, 60 * 60 * 1000, false, "LevelUpRewardEntityManager");
	
	public static SimpleEntityManager<LevelUpRewardEntity> em;
	
	public Map<Long, LevelUpRewardEntity> tempCache = new Hashtable<Long, LevelUpRewardEntity>();
	
	public void init(){
		instance = this;
		em = SimpleEntityManagerFactory.getSimpleEntityManager(LevelUpRewardEntity.class);
	}
	/**
	 * 购买冲级返利
	 * @param player
	 * @param rewardType
	 * @return
	 */
	protected String buyLevelUpReward(Player player, int rewardType) {
		LevelUpRewardEntity entity = this.getEntity(player.getId());
		if (entity.getRewardInfo() != null && entity.getRewardInfo().size() > 0) {
			for (RewardInfo info : entity.getRewardInfo()) {
				if (info.getType() == rewardType) {
					if (logger.isWarnEnabled()) {
						logger.warn("[购买冲级返利] [异常] [" + player.getLogString() + "] [type:" + rewardType + "] [已经购买过当前等级对应冲级奖励商品]");
					}
					return "已经购买过当前等级对应冲级奖励商品";
				}
			}
		}
		RewardInfo info = new RewardInfo();
		info.setReceiveTimes(0);
		info.setType(rewardType);
		entity.getRewardInfo().add(info);
		em.notifyFieldChange(entity, "rewardInfo");
		LevelUpRewardManager.instance.autoReceiveReward(player);		//购买成功后先调用一次自动返利，防止玩家等级已经超过之前的返利等级
		if (logger.isWarnEnabled()) {
			logger.warn("[购买冲级返利] [成功] [" + player.getLogString() + "] [rewardType:" + rewardType + "]");
		}
		return null;
	} 
	
	public LevelUpRewardEntity getEntity(long id) {
		LevelUpRewardEntity entity = (LevelUpRewardEntity) cache.get(id);
		if (entity == null) {
			try {
				entity = em.find(id);
				if (entity == null) {
					entity = new LevelUpRewardEntity();
					entity.setId(id);
					cache.put(id, entity);
				}
			} catch (Exception e) {
				logger.warn("["+this.getClass().getSimpleName()+"] [getEntity] [异常] [playerId:" + id + "]", e);
			}
		}
		if (!tempCache.containsKey(id)) {
			tempCache.put(id, entity);
		}
		return entity;
	}
	
	public void notifyRemoveFromCache(LevelUpRewardEntity entity) {
		try {
			em.save(entity);
		} catch (Exception e) {
			logger.error("[冲击红利] [异常] [冲击红利] [移除保存错误] [" + entity.getId() + "]", e);
		}
	}
	
	public void destory() {
		if (em != null) {
			em.destroy();
		}
	}

}
