package com.fy.engineserver.jiazu2.manager;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Hashtable;
import java.util.Map;

import org.slf4j.Logger;

import com.fy.engineserver.core.JiazuSubSystem;
import com.fy.engineserver.datasource.language.Translate;
import com.fy.engineserver.jiazu.Jiazu;
import com.fy.engineserver.jiazu.JiazuFunction;
import com.fy.engineserver.jiazu.JiazuMember;
import com.fy.engineserver.jiazu.JiazuTitle;
import com.fy.engineserver.jiazu.service.JiazuManager;
import com.fy.engineserver.jiazu2.instance.BiaoCheEntity;
import com.fy.engineserver.jiazu2.model.BiaoCheQianghuaModel;
import com.fy.engineserver.sprite.Player;
import com.fy.engineserver.sprite.concrete.GamePlayerManager;
import com.fy.engineserver.sprite.npc.BiaoCheNpc;
import com.xuanzhi.tools.cache.LruMapCache;
import com.xuanzhi.tools.simplejpa.SimpleEntityManager;
import com.xuanzhi.tools.simplejpa.SimpleEntityManagerFactory;

public class BiaocheEntityManager {
	
	public static BiaocheEntityManager instance;
	
	public static Logger logger = JiazuSubSystem.logger;
	
	public static SimpleEntityManager<BiaoCheEntity> em;
	
	public LruMapCache cache = new LruMapCache(10240, 60 * 60 * 1000, false, "jiazuBiaoche_entity");
	
	public Map<Long, BiaoCheEntity> tempCache = new Hashtable<Long, BiaoCheEntity>();
	
	public static int 镖车强化家族等级 = 5;
	public static int 镖车强化最大等级 = 10;
	
	public static final byte 强化血量 = 1;
	public static final byte 强化双防  = 2;
	public static final int 强化最大进度 = 10;
	public static final int 每次强化进度 = 1;
	
	public static final int infoShowNumRate = 2;	//除以
	
	public void init() {
		instance = this;
		em = SimpleEntityManagerFactory.getSimpleEntityManager(BiaoCheEntity.class);
	}
	
	/**
	 * 强化镖车
	 * @param player
	 * @param upgradeType  1血量强化  2双防强化
	 * @return 0强化失败   1强化成功未升级   2强化成功且升级
	 */
	public byte upgragdeBiaoche(Player player, byte upgradeType) {
		try {
			if (upgradeType != 强化血量 && upgradeType != 强化双防) {
				return 0;
			}
			if (player.getJiazuId() <= 0) {
				player.sendError(Translate.您还没有家族);
				return 0;
			}
			Jiazu jiazu = JiazuManager.getInstance().getJiazu(player.getJiazuId());
			if (jiazu == null) {
				player.sendError(Translate.您还没有家族);
				return 0;
			}
			if (jiazu.getLevel() < 镖车强化家族等级) {
				player.sendError(Translate.家族等级太低不能强化镖车);
				return 0;
			}
			
			JiazuMember jm = JiazuManager.getInstance().getJiazuMember(player.getId(), player.getJiazuId());
			if (jm == null) {
				player.sendError(Translate.您还没有家族);
				return 0;
			}
			boolean perMission = JiazuTitle.hasPermission(jm.getTitle(), JiazuFunction.stronger_BIAOCHE);
			if (!perMission) {
				player.sendError(Translate.权限不足不能使用此功能);
				return 0;
			}
			if (jiazu.getJiazuStatus() == JiazuManager2.封印家族功能) {
				player.sendError(Translate.家族资金不足封印);
				return 0;
			}
			synchronized (jiazu) {
				BiaoCheEntity entity = this.getEntity(player.getJiazuId());
				byte[] types = entity.getStrongerType();
				int[] levels = entity.getStrongerLevel();
				int[] proces = entity.getProcess();
				int oldLevel = 0;
				byte result = 1;
				for (int i=0; i<types.length; i++) {
					if (types[i] == upgradeType) {
						if (levels[i] < 镖车强化最大等级) {
//							oldpro = proces[i];
							oldLevel = levels[i];
							proces[i] += 每次强化进度;
							if (proces[i] >= 强化最大进度) {
								proces[i] = 0;
								levels[i]+=1;
								result = 2;
							}
//							flag = true;
						} else {
							player.sendError(Translate.镖车强化已达上限);
							return 0;
						}
						break;
					}
				}
				BiaoCheQianghuaModel bqm = JiazuManager2.instance.biaocheMap.get(upgradeType).get(oldLevel + 1);
				if (jiazu.getLevel() < bqm.getNeedJiazuLevel()) {
					player.sendError(Translate.家族等级太低不能强化镖车);
					return 0;
				}
				long costLingmai = bqm.getCostLingmai() / 强化最大进度;
				long costMoney = bqm.getCostJiazuMoney() / 强化最大进度;
				//扣除消耗
				if (costMoney > 0 && jiazu.getJiazuMoney() < costMoney) {
					player.sendError(Translate.text_jiazu_107);
					return 0;
				}
				if (costLingmai > 0 && jiazu.getConstructionConsume() < costLingmai) {
					player.sendError(Translate.家族资源不足);
					return 0;
				}
				
				if (costMoney > 0) {
					if (!jiazu.consumeJiazuMoneyAndConstructionConsume(costMoney, 0)) {
						player.sendError(Translate.text_jiazu_107);
						return 0;
					}
//					jiazu.setJiazuMoney(jiazu.getJiazuMoney() - costMoney);
//					try {
//						if (JiazuManager2.instance.noticeNum.get(jiazu.getLevel()) != null) {
//							int num = JiazuManager2.instance.noticeNum.get(jiazu.getLevel());
//							if (jiazu.getJiazuMoney() < num) {
//								JiazuManager2.instance.noticeJizuMondeyNotEnough(jiazu);
//							}
//						}
//					} catch (Exception e) {
//						JiazuManager2.logger.error("[新家族] [发送家族资金不足提醒] [异常] [" + jiazu.getLogString() + "]", e);
//						return 0;
//					}
				}
				if (costLingmai > 0) {
					if (!jiazu.consumeJiazuMoneyAndConstructionConsume(0, costLingmai)) {
						player.sendError(Translate.家族资源不足);
						return 0;
					}
//					jiazu.setConstructionConsume(jiazu.getConstructionConsume() - costLingmai);
				}
				entity.setProcess(proces);
				//增加强化等级
				entity.setStrongerLevel(levels);
				logger.warn("[新家族] [强化家族镖车] [成功] [" + player.getJiazuLogString() + "] [upgradeType : " + upgradeType + "] [强化类型: "+Arrays.toString(entity.getStrongerType())+"] [强化等级:" + Arrays.toString(entity.getStrongerLevel()) + "] [进度:"+Arrays.toString(entity.getProcess())+"] [消耗家族资金: "
						+ costMoney + "] [消耗家族灵脉: " + costLingmai + "] [剩余家族资金: " + jiazu.getJiazuMoney() + 
						"] [剩余家族灵脉: " + jiazu.getConstructionConsume() + "]" );
//				if (flag) {			//已经召唤出来的镖车不管？
//					upgradeBiaocheAttr(player);
//				}
				return result;
			}
		} catch (Exception e) {
			logger.error("[新家族] [镖车强化] [异常] [" + player.getJiazuLogString() + "] [type : " + upgradeType + "]", e);
		}
		return 0;
	}
	
	public void destory() {
		if (em != null) {
			em.destroy();
			logger.info("**************************************[服务器关闭][家族镖车调用destory]*********************************************");
		}
	}
	
	/**
	 * 强化镖车消耗二次确认，加不加再看
	 * @param p
	 * @param costType
	 * @param costNum
	 */
	private void popConfirmWindow(Player p, byte costType, long costNum) {
//		WindowManager wm = WindowManager.getInstance();
//		MenuWindow mw = wm.createTempMenuWindow(600);
//		Option_Jiazu_Lingkuang_Notice option1 = new Option_Jiazu_Lingkuang_Notice();
//		option1.setText(MinigameConstant.CONFIRM);
//		Option_Cancel option2 = new Option_Cancel();
//		option2.setText(MinigameConstant.CANCLE);
//		Option[] options = new Option[] {option1, option2};
//		mw.setOptions(options);
//		String msg = Translate.灵矿战开始;
//		mw.setDescriptionInUUB(msg);
//		CONFIRM_WINDOW_REQ creq = new CONFIRM_WINDOW_REQ(GameMessageFactory.nextSequnceNum(), mw.getId(), mw.getDescriptionInUUB(), options);
//		p.addMessageToRightBag(creq);
	}
	
	/**
	 * 给镖车加属性
	 * @param player
	 */
	public void addAttr2Biaoche(Player player) {
		try {
			if (player.getJiazuId() <= 0) {
				return ;
			}
			Jiazu jiazu = JiazuManager.getInstance().getJiazu(player.getJiazuId());
			if (jiazu == null) {
				return;
			}
			if (player.getFollowableNPC() == null || !(player.getFollowableNPC() instanceof BiaoCheNpc) || !((BiaoCheNpc)player.getFollowableNPC()).isJiazuCar()) {
				return ;
			}
			BiaoCheEntity entity = this.getEntity(player.getJiazuId());
			BiaoCheQianghuaModel xue = null;
			BiaoCheQianghuaModel fang = null;
			for (int i=0; i<entity.getStrongerType().length; i++) {
				if (entity.getStrongerType()[i] == 强化血量) {
					xue = JiazuManager2.instance.biaocheMap.get(强化血量).get(entity.getStrongerLevel()[i]);
				} else if (entity.getStrongerType()[i] == 强化双防) {
					fang = JiazuManager2.instance.biaocheMap.get(强化双防).get(entity.getStrongerLevel()[i]);
				}
			}
			if (xue != null) {
				int oldMaxHp = player.getFollowableNPC().getMaxHP();
				float f = (100+xue.getAddNum()) / 100f;
				int resultHp = (int) (oldMaxHp * f) ;
				if (resultHp <= 0) {
					resultHp = 1500000000;
				}
				player.getFollowableNPC().setMaxHP(resultHp);
//				player.getFollowableNPC().setMaxHPC(xue.getAddNum());
				player.getFollowableNPC().setHp(player.getFollowableNPC().getMaxHP());
				if (logger.isDebugEnabled()) {
					logger.debug("[新家族] [给家族镖车加属性] [增加血百分之 : " + xue.getAddNum() + "] [" + player.getLogString() + "] [oldMaxHp:" + oldMaxHp + "] [new:" + player.getFollowableNPC().getMaxHP() + "]");
				}
			}
			if (fang != null) {
				int oldPhyDefRate = player.getFollowableNPC().getPhyDefenceRateOther();
				int oldMagDefRate = player.getFollowableNPC().getMagicDefenceRateOther();
				int newPhyDefRate = oldPhyDefRate + fang.getAddNum()*10;
				int newMagDefRate = oldMagDefRate + fang.getAddNum()*10;
				player.getFollowableNPC().setPhyDefenceRateOther(newPhyDefRate);
				player.getFollowableNPC().setMagicDefenceRateOther(newMagDefRate);
				if (logger.isDebugEnabled()) {
					logger.debug("[新家族] [给家族镖车加属性] [增加双防百分之 : " + xue.getAddNum() + "] [" + player.getLogString() + "] [oldPhyDef:" + oldPhyDefRate + "] [new:" + player.getFollowableNPC().getPhyDefenceRateOther() 
							+ "] [oldMagDef:" + oldMagDefRate + "] [new:" + player.getFollowableNPC().getMagicDefenceRateOther() + "]");
				}
			}
		} catch (Exception e) {
			logger.error("[新家族] [给家族镖车加属性] [异常] [" + player.getLogString() + "]", e);
		}
	}
	
	/**
	 * 设置家族镖车属性
	 * @param player
	 */
	@SuppressWarnings("unused")
	private void upgradeBiaocheAttr(Player player) {
		Jiazu jiazu = JiazuManager.getInstance().getJiazu(player.getJiazuId());
		if (jiazu == null) {
			return;
		}
		//加强已经出来的家族镖车
		ArrayList<JiazuMember> list = JiazuManager.getInstance().getJiazuMember(jiazu.getJiazuID(), JiazuTitle.master, JiazuTitle.vice_master);
		if (list == null || list.size() <= 0) {
//			player.sendError(Translate.家族镖车不存在);
			return ;
		}
		for (int i=0; i<list.size(); i++) {			//正在押镖，增加镖车属性  用不用再看
			if (!GamePlayerManager.getInstance().isOnline(list.get(i).getJiazuMemID())) {
				continue;
			}
			try {
				Player p = GamePlayerManager.getInstance().getPlayer(list.get(i).getJiazuMemID());
				if (p.getFollowableNPC() != null && p.getFollowableNPC() instanceof BiaoCheNpc && ((BiaoCheNpc)p.getFollowableNPC()).isJiazuCar()) {
					//随便写的。。测试用，等表出来修改
					int tempAdd = 10;			//百分之10  随便写的
					int oldMHp = ((BiaoCheNpc)p.getFollowableNPC()).getMaxHP();
					((BiaoCheNpc)p.getFollowableNPC()).setMaxHPC(((BiaoCheNpc)p.getFollowableNPC()).getMaxHPC() + tempAdd);
					int newMhp = ((BiaoCheNpc)p.getFollowableNPC()).getMaxHP();
					((BiaoCheNpc)p.getFollowableNPC()).setHp(((BiaoCheNpc)p.getFollowableNPC()).getHp() + (newMhp-oldMHp));
					logger.warn("[新家族] [强化家族镖车] [成功] [正在家族押镖] [增加镖车属性] [成功] [" + player.getLogString() + "] [强化后镖车最大血量:" + newMhp + "] [强化前镖车最大血量:" + oldMHp + "]");
					break;
				}
			} catch (Exception e) {
				logger.error("[新家族] [通过jiazumemberId获取玩家] [异常] [" + list.get(i).getJiazuMemID() + "]", e);
			}
		}
	}
	
	public void notifyRemoveFromCache(BiaoCheEntity entity) {
		try {
			em.save(entity);
		} catch (Exception e) {
			logger.error("[新家族] [异常] [镖车] [移除保存错误] [" + entity.getId() + "]", e);
		}
	}
	
	public BiaoCheEntity getEntity(long jiazuId) {
		BiaoCheEntity entity = (BiaoCheEntity) cache.get(jiazuId);
		if(entity == null) {
			try {
				entity = em.find(jiazuId);
				if(entity == null) {
					entity = new BiaoCheEntity(jiazuId);
					entity.setId(jiazuId);
					entity.setStrongerType(new byte[]{1,2});		//目前默认强化血和双防
					entity.setStrongerLevel(new int[]{0,0});
					entity.setProcess(new int[]{0,0});
					em.notifyNewObject(entity);
					cache.put(jiazuId, entity);
					logger.error("[新家族] [从数据库加载镖车强化数据] [成功] [jiazuId : " + jiazuId + "]");
				}
				if (logger.isDebugEnabled()) {
					logger.debug("[新家族] [从数据库加载镖车强化数据] [成功] [jiazuId : " + jiazuId + "]");
				}
			} catch (Exception e) {
				logger.error("[新家族] [从数据库加载镖车强化数据异常] [jiazuId : " + jiazuId + "]", e);
			}
		}
		if (!tempCache.containsKey(jiazuId)) {
			tempCache.put(jiazuId, entity);
		}
		return entity;
	}
	
}
