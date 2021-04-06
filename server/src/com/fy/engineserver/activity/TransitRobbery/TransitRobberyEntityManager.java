package com.fy.engineserver.activity.TransitRobbery;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;

import org.slf4j.Logger;

import com.fy.engineserver.achievement.RecordAction;
import com.fy.engineserver.activity.TransitRobbery.model.RobberyConstant;
import com.fy.engineserver.core.ExperienceManager;
import com.fy.engineserver.core.res.Constants;
import com.fy.engineserver.datasource.language.Translate;
import com.fy.engineserver.event.Event;
import com.fy.engineserver.event.EventRouter;
import com.fy.engineserver.event.cate.EventWithObjParam;
import com.fy.engineserver.message.NOTICE_CLIENT_COUNTDOWN_REQ;
import com.fy.engineserver.newBillboard.BillboardsManager;
import com.fy.engineserver.newBillboard.IBillboardPlayerInfo;
import com.fy.engineserver.seal.SealManager;
import com.fy.engineserver.seal.data.Seal;
import com.fy.engineserver.sprite.Player;
import com.fy.engineserver.sprite.Soul;
import com.fy.engineserver.sprite.concrete.GamePlayerManager;
import com.fy.engineserver.util.ServiceStartRecord;
import com.xuanzhi.tools.cache.LruMapCache;
import com.xuanzhi.tools.simplejpa.SimpleEntityManager;
import com.xuanzhi.tools.simplejpa.SimpleEntityManagerFactory;

public class TransitRobberyEntityManager {
	public static Logger logger = TransitRobberyManager.logger;
	
	public static SimpleEntityManager<TransitRobberyEntity> em;
	private static TransitRobberyEntityManager instance;
	/** 正在渡劫的玩家列表 */
	private Set<Long> robberingPlayer;
	/** 记录玩家渡劫中杀死的怪物id和数量*/
	private Map<Long, ArrayList<Integer[]>> killedMonsters = new HashMap<Long, ArrayList<Integer[]>>();
	/** 使用护身罡气和护身真气的玩家list */
	private Set<Long> list = new HashSet<Long>();
	/** 使用火神玄魄玩家map  key=playerId, value=结束时间 */
	private Map<Long, Long> huoMap = new ConcurrentHashMap<Long, Long>();
	
	public LruMapCache cache = new LruMapCache(10240,60 * 60 * 1000,false, "渡劫");
	
	private TransitRobberyEntityManager(){
		instance = this;
	}
	
	public static TransitRobberyEntityManager getInstance(){
		return instance;
	}
	
	/**
	 * 飞升，一辈子只需要调用一次
	 * @param player
	 */
	public String feiSheng(Player player){
		String str = TransitRobberyManager.getInstance().feishengPanduan(player);
		if(str != null){
			return str;
		}
		TransitRobberyEntity entity = this.getTransitRobberyEntity(player.getId());
		entity.updateFeiSheng();
		player.setAvataRace(Constants.race_human_new);
		player.initAvta();
		{					//升级
			ExperienceManager em = ExperienceManager.getInstance();
			if (player.getCurrSoul().getSoulType() == Soul.SOUL_TYPE_BASE) {			//本尊飞升
				player.playerLevelUpByClick();							
				if (logger.isInfoEnabled()) {
					logger.info("[飞升] [扣除经验] [本尊升级] [剩余经验:" + player.getExp() + "] [" + player.getLogString() + "]" );
				}
				Soul soul = player.getSoul(Soul.SOUL_TYPE_SOUL);
				int oldLevel = soul.getGrade();
				int newLevel = oldLevel + 1;
				long newExp = em.calculateOneLevelLeftExp(oldLevel, player.getExp());
				player.setExp(newExp);
				soul.setGrade(newLevel);
				if (logger.isInfoEnabled()) {
					logger.info("[飞升] [扣除经验] [元神升级] [剩余经验:" + player.getExp() + "] [" + player.getLogString() + "]" );
				}
			} else {										//元神飞升
				Soul soul = player.getSoul(Soul.SOUL_TYPE_BASE);
				int oldLevel = soul.getGrade();
				int newLevel = oldLevel + 1;
				long newExp = em.calculateOneLevelLeftExp(oldLevel, player.getExp());
				player.setExp(newExp);
				if (logger.isInfoEnabled()) {
					logger.info("[飞升] [扣除经验] [本尊升级] [剩余经验:" + newExp + "] [" + player.getLogString() + "]" );
				}
				soul.setGrade(newLevel);
				player.getMainSoul().setGrade(newLevel);
				player.setLevel(newLevel);
				player.playerLevelUpByClick();	
				if (logger.isInfoEnabled()) {
					logger.info("[飞升] [扣除经验] [元神升级] [剩余经验:" + player.getExp() + "] [" + player.getLogString() + "]" );
				}
			}
			player.setDirty(true, "soulLevel");
			player.setDirty(true, "currSoul");
			player.setDirty(true, "unusedSoul");
			player.changeSoulLv();
		}
		try {
			if (!SealManager.hasUpdateSeal) {
				long c = ((GamePlayerManager)GamePlayerManager.getInstance()).em.count(Player.class, "level >= 221");
				if (c > 0) {
					SealManager.hasUpdateSeal = true;
				} else {
					Seal s = SealManager.getInstance().getSeal();
					if (s.getSealLevel() == 260) {
						s.setSealCanOpenTime(System.currentTimeMillis() + 365 * 24 * 60 * 60 * 1000L,"飞升");
					}
					SealManager.getInstance().sealMap.put(260, s);
					SealManager.getInstance().saveSeal(true);
					SealManager.hasUpdateSeal = true;
					logger.warn("[修改260封印时间] [成功] [" + player.getLogString() + "]");
				}
			}
		}catch (Exception e) {
			logger.warn("[修改260封印时间] [异常] ", e);
		}
		logger.info("飞升====飞升成功==" + player.getLogString());
		return null;
	}
	public String feiSheng(Player player, boolean flag){
		TransitRobberyEntity entity = this.getTransitRobberyEntity(player.getId());
		entity.updateFeiSheng();
		player.setAvataRace(Constants.race_human_new);
		player.initAvta();
		return null;
	}
	
	public void init(){
		
		try{
			em = SimpleEntityManagerFactory.getSimpleEntityManager(TransitRobberyEntity.class);
			robberingPlayer = new HashSet<Long>();
		}catch(Exception e){
			logger.error("[渡劫entity][初始化数据库失败][e:" + e + "]");
			return; 
		}
		ServiceStartRecord.startLog(this);
	}
	/**
	 * 屏蔽散仙
	 * @param playerId
	 * @param amount		屏蔽个数
	 */
	public void 使用护身罡气(long playerId, int amount){
		//判断当前是否有散仙--有则直接灭掉，没有的话加入list下次保证不刷新
		if(!TransitRobberyManager.getInstance().killAllImmortal(playerId, amount)){
			list.add(playerId);
		}
	}
	
	public void 使用火神玄魄(long playerId, long invalidTime) {
		long time = System.currentTimeMillis() + invalidTime * 1000;
		huoMap.put(playerId, time);
		try {
			NOTICE_CLIENT_COUNTDOWN_REQ resp = new NOTICE_CLIENT_COUNTDOWN_REQ();
			resp.setCountdownType((byte)50);
			resp.setLeftTime((int)invalidTime);
			resp.setDescription(Translate.火神玄魄倒计时);
			Player player = GamePlayerManager.getInstance().getPlayer(playerId);
			player.addMessageToRightBag(resp);
			if(logger.isInfoEnabled()) {
				logger.info("[渡劫][使用火神玄魄][" + player.getLogString() + "][持续时间:"+invalidTime + "][失效时间:" + time + "]");
			}
		} catch (Exception e) {
			logger.error("[渡劫 ] [使用火神玄魄] [异常] [playerId : " + playerId + "]", e);
		}
	}
	
	public synchronized boolean 火神玄魄生效(long playerId) {
		if(huoMap.keySet().contains(playerId)) {
			long currentTime = System.currentTimeMillis();
			if(currentTime > huoMap.get(playerId)) {
				删除火神玄魄效果(playerId);
				TransitRobberyManager.getInstance().删除火神玄魄效果(playerId);
				return false;
			} else {
				return true;
			}
		}
		return false;
	}
	public void 删除火神玄魄效果(long playerId) {
		huoMap.remove(playerId);
		if(logger.isInfoEnabled()) {
			logger.info("[渡劫][火神玄魄失效][playerId:" + playerId + "]");
		}
	}
	
	/**
	 * 方便测试加的方法--逻辑中不用
	 * @param playerId
	 * @param lev
	 */
	public String setCurrentRobberyLevel(long playerId, int lev){
		if(lev < 0 || lev > 9){
			return "填写的等级不对";
		}
		TransitRobberyEntity entity = TransitRobberyEntityManager.getInstance().getTransitRobberyEntity(playerId);
		if(entity == null){
			return "本人等级低于110";
		}
		entity.setCurrentLevel(lev);
		em.notifyFieldChange(entity, "currentLevel");
		return "成功";
	}
	
	public String setCurrentPassLayer(long playerId, int lev) {
		if(lev < 0 || lev > 7) {
			return "层数填写不对";
		}
		TransitRobberyEntity entity = TransitRobberyEntityManager.getInstance().getTransitRobberyEntity(playerId);
		if(entity == null){
			return "本人等级低于110";
		}
		entity.setPassLayer(lev);
		em.notifyFieldChange(entity, "passLayer");
		return "成功";
	}
	
	/**
	 * 
	 * @param playerId
	 * @return  true 屏蔽散仙
	 */
	public boolean 护身罡气生效(long playerId){
		return list.remove(new Long(playerId));
	}
	/**
	 * 清空杀怪记录
	 * */
	public void clearKilledList(long playerId){
		ArrayList<Integer[]> list = killedMonsters.get(playerId);
		if(list != null) {
			list.clear();
			killedMonsters.put(playerId, list);
		}
	}
	/**
	 * 更新玩家在渡劫中杀死的怪物
	 * @param playerId
	 * @param monsterId
	 */
	public void add2KilledList(long playerId, int monsterId){
		if(isPlayerInRobbery(playerId)){
			logger.info("[渡劫][杀死怪物，怪物id=" + monsterId + "][playerId=" + playerId + "]");
			ArrayList<Integer[]> list = killedMonsters.get(playerId);
			boolean exist = false;
			if(list == null){
				list = new ArrayList<Integer[]>();
			}
			for(int i=0; i<list.size(); i++){
				if(list.get(i)[0] == monsterId){
					list.get(i)[1] += 1;
					exist = true;
				}
			}
			if(!exist){
				Integer[] aa = new Integer[2];
				aa[0] = monsterId;
				aa[1] = 1;
				list.add(aa);
			}
			killedMonsters.put(playerId, list);
		}
	}
	/**
	 * 判断玩家是否在渡劫中杀死目标怪物
	 * @param playerId
	 * @param monsterId
	 * @return
	 */
	public boolean isKilledMoster(long playerId, int monsterId, int amount){
		ArrayList<Integer[]> list = killedMonsters.get(playerId);
		if(list == null){
			return false;
		}
		if(monsterId == -999){
			int count = 0;
			for(int i=1;i<RobberyConstant.BOSS_MODEL_IDS.length; i++){
				for(Integer[] a : list){
					if(a[0] == RobberyConstant.BOSS_MODEL_IDS[i]){
						count += a[1];
					}
				}
			}
			if(count >= amount){					//杀死四种人型怪总数大于等于目标数算通过
				return true;
			}
		} else {
			for(Integer[] a : list){
				if(a[0] == monsterId && a[1] >= amount){
					return true;
				}
			}
		}
		return false;
	}
	/**
	 * 检测玩家是否在幻象劫
	 * @param playerId
	 * @return
	 */
	public boolean isPlayerInPhantomRobbery(long playerId, int level){
		if(!isPlayerInRobbery(playerId)){
			return false;
		}
		TransitRobberyEntity entity = this.getTransitRobberyEntity(playerId);
		if(entity != null && (entity.getCurrentLevel() + 1) == level){
			return true;
		}
		return false;
	}
	/**
	 * 检测玩家是否正在渡劫。。用于对玩家换装、召唤宠物、切换元神做限制
	 * @param playerId
	 * @return
	 */
	public boolean isPlayerInRobbery(long playerId){
		boolean result =  robberingPlayer.contains(new Long(playerId));
		return result;
	}
	
	public void 从渡劫中剔除(long playerId) {
		TransitRobberyManager.getInstance().removeRobbery(playerId);	//渡劫线程中删除此人信息
		this.removeFromRobbering(playerId);
	}
	/**
	 * 判断玩家是否在第四劫的最后一小关--------不允许吃药
	 * @param player
	 * @return
	 */
	public boolean isPlayerInSixiang(Player player){
		try{
			if(isPlayerInRobbery(player.getId())) {
				if(TransitRobberyManager.getInstance().是否在四相劫最后一关(player)) {
					return true;
				}
			}
			return false;
		}catch(Exception e) {
			logger.error("[限制使用药品出错][" + player.getLogString() + "]{}",e);
			return false;
		}
	}
	
	public void add2Robbering(long playerId){
		robberingPlayer.add(playerId);
	}
	
	public void removeFromRobbering(long playerId){
		boolean result = robberingPlayer.remove(new Long(playerId));
		killedMonsters.remove(new Long(playerId));			//清楚玩家渡劫中的杀怪统计
		logger.info("[移除玩家正在渡劫的状态][result = " + result + "][playerId:" + playerId + "]");
	}
	
	/**
	 * 
	 * @param playerId
	 * @param type 1-更新开始渡劫时间     2更新渡劫结束时间  3更新渡劫重数(会去检测是否开启下重天劫强制拉人)
	 * @param leftTime 只有更新lefttime时用到。 如果是玩家自己进入或者已经执行了强制拉入。则传-1
	 */
	public void updateTransitRobberyEntity(long playerId, int type, int lefttime){
		TransitRobberyEntity entity = this.getTransitRobberyEntity(playerId);
		if(entity == null){
			return;
		}
		switch (type) {
		case RobberyConstant.UPDATE_START_TIME:
			entity.updateStartTime();
			break;
		case RobberyConstant.UPDATE_END_TIME:
			entity.updateEndTime();
			break;
		case RobberyConstant.UPDATE_DOBBERY_LEVEL:
			if(entity.getCurrentLevel() < 9){
				entity.updateCurrentLevel();
				entity.updateEndTime();
				TransitRobberyManager.getInstance().check4NextRobberyLevel(playerId);
				entity.updateLeftTime(-100);
				Object[] param = new Object[]{playerId, entity.getCurrentLevel()};
				EventRouter.getInst().addEvent(new EventWithObjParam(Event.PLAYER_DU_JIE, param));
				EventWithObjParam evt = new EventWithObjParam(com.fy.engineserver.event.Event.RECORD_PLAYER_OPT, new Object[] { playerId, RecordAction.渡劫重数, entity.getCurrentLevel()});
				EventRouter.getInst().addEvent(evt);
			} else {
				logger.error("[渡劫][出现异常，玩家已经达到最高劫可是还是渡劫成功了][playerId=" + playerId + "]");
			}
			break;
		case RobberyConstant.INIT_FORCE_PULL_TIME:
			entity.initForceLeftTime();
			break;
		case RobberyConstant.UPDATE_FORCE_PULL_TIME:
			entity.updateLeftTime(lefttime);
			if(lefttime < 0){
				TransitRobberyManager.getInstance().remove2CountdownHeartBeat(playerId);
			}
			break;
		case RobberyConstant.UPDATE_PASSLAYER:
			logger.info("[更新通过层数]{"+lefttime+"}{"+playerId+"}");
			entity.updatePassLayer(lefttime);
			break;
		}
	}
	/**
	 * 方便测试用----可以重置天劫
	 * @param playerId
	 */
	public void resetRobberyEntity(long playerId){
		TransitRobberyEntity entity = this.getTransitRobberyEntity(playerId);
		if(entity == null){
			return;
		}
		entity.resetRobbery();
	}
	
	public static List<IBillboardPlayerInfo> getBillboardPlayerInfo(long ids){
		long[] idss = new long[1];
		idss[0] = ids;
		
		SimpleEntityManager<Player> em = SimpleEntityManagerFactory.getSimpleEntityManager(Player.class);
		if(idss != null && idss.length > 0){
			List<IBillboardPlayerInfo> list = null;
			try {
				list = em.queryFields(IBillboardPlayerInfo.class, idss);
				List<IBillboardPlayerInfo> sortList = new ArrayList<IBillboardPlayerInfo>();
				for(long id : idss){
					for(IBillboardPlayerInfo ib : list){
						if(id == ib.getId()){
							sortList.add(ib);
							break;
						}
					}
				}
				return sortList;
			} catch (Exception e) {
				BillboardsManager.logger.error("[查询榜单玩家数据异常] ["+idss[0]+"]",e);
			}
		}else{
			BillboardsManager.logger.error("[查询榜单玩家数据错误] [没有记录] ["+ids+"]");
		}
		return null;
	}
	
	/**
	 * 通过任务id获取渡劫信息
	 * @param playerId
	 * @return
	 */
	public TransitRobberyEntity getTransitRobberyEntity(long playerId){
		try {
			List<IBillboardPlayerInfo> list = getBillboardPlayerInfo(playerId);
			int level = 0;
			if(list == null || list.size() <= 0) {
				Player p = GamePlayerManager.getInstance().getPlayer(playerId);
				level = p.getLevel();
			} else {
				level = list.get(0).getLevel();
			}
			if(level < TransitRobberyManager.openLevel){
				return null;
			}
		} catch (Exception e1) {
			// TODO Auto-generated catch block
			logger.error("entityManager(264行)，获取player出错,e:" , e1);
			return null;
		}
		TransitRobberyEntity bean = (TransitRobberyEntity) cache.get(playerId);
		if(bean != null){
			return bean;
		}
		try {
			bean = em.find(playerId);
			if(bean == null){
				bean = new TransitRobberyEntity();
				bean.setRobberyStartTime(0L);
				bean.setRobberyEndTime(0L);
				bean.setId(playerId);
				bean.setCurrentLevel(0);
				bean.setRobberyLeftTime(-100);
				bean.setFeisheng((byte) 0);
				em.notifyNewObject(bean);
			}
			if(bean != null){
				cache.put(playerId, bean);
			}
		} catch (Exception e) {
			// TODO Auto-generated catch block
			logger.error("[渡劫][创建数据失败][playerId=" + playerId + "]");
			e.printStackTrace();
		}
		return bean;
	}
	
	public void notifyRemoveFromCache(TransitRobberyEntity mge) {
		try {
			em.save(mge);

		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	public void destory() {
		if (em != null) {
			em.destroy();
			logger.info("**************************************[服务器关闭][调用destory]*********************************************");
		}
	}
}
