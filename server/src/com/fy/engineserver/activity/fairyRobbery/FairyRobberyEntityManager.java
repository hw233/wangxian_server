package com.fy.engineserver.activity.fairyRobbery;

import java.util.Hashtable;
import java.util.Map;

import org.slf4j.Logger;

import com.fy.engineserver.activity.fairyRobbery.instance.FairyRobberyEntity;
import com.fy.engineserver.sprite.Player;
import com.xuanzhi.tools.simplejpa.SimpleEntityManager;
import com.xuanzhi.tools.simplejpa.SimpleEntityManagerFactory;

public class FairyRobberyEntityManager {
	
	public static Logger logger = FairyRobberyManager.logger;
	
	public static FairyRobberyEntityManager inst;
	
	public static SimpleEntityManager<FairyRobberyEntity> em;
	
	public Map<Long, FairyRobberyEntity> cache = new Hashtable<Long, FairyRobberyEntity>();
	
	public void init() {
		inst = this;
		em = SimpleEntityManagerFactory.getSimpleEntityManager(FairyRobberyEntity.class);
	}
	
	public FairyRobberyEntity getEntity(Player player) {
		if (player.getLevel() < 221) {				//只有飞升玩家才开
			return null;
		}
		FairyRobberyEntity entity = cache.get(player.getId());
		if (entity == null) {
			try {
				entity = em.find(player.getId());
			} catch (Exception e) {
				// TODO Auto-generated catch block
				logger.warn("[仙界渡劫] [从数据库获取仙界渡劫信息] [异常] [" + player.getLogString() + "]", e);
			}
			if (entity == null) {
				entity = new FairyRobberyEntity();
				entity.setId(player.getId());
				entity.setPassLevel(0);
				em.notifyNewObject(entity);
			}
			if (entity != null) {
				cache.put(player.getId(), entity);
			}
		}
		return entity;
	}
	
	public void destory() {
		if (em != null) {
			em.destroy();
		}
	}
	
}
