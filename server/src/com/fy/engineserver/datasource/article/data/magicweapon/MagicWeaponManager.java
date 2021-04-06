package com.fy.engineserver.datasource.article.data.magicweapon;

import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;
import java.util.concurrent.ConcurrentHashMap;

import org.apache.poi.hssf.usermodel.HSSFCell;
import org.apache.poi.hssf.usermodel.HSSFRow;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.poifs.filesystem.POIFSFileSystem;
import org.apache.poi.ss.usermodel.Cell;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.fy.boss.authorize.exception.BillFailedException;
import com.fy.boss.authorize.exception.NoEnoughMoneyException;
import com.fy.engineserver.achievement.AchievementManager;
import com.fy.engineserver.achievement.RecordAction;
import com.fy.engineserver.activity.ActivitySubSystem;
import com.fy.engineserver.activity.activeness.ActivenessManager;
import com.fy.engineserver.activity.activeness.ActivenessType;
import com.fy.engineserver.activity.pickFlower.MagicWeaponNpc;
import com.fy.engineserver.activity.shop.ShopActivityManager;
import com.fy.engineserver.animation.AnimationManager;
import com.fy.engineserver.articleProtect.ArticleProtectDataValues;
import com.fy.engineserver.articleProtect.ArticleProtectManager;
import com.fy.engineserver.chat.ChatMessage;
import com.fy.engineserver.chat.ChatMessageService;
import com.fy.engineserver.core.CoreSubSystem;
import com.fy.engineserver.core.Game;
import com.fy.engineserver.core.res.Avata;
import com.fy.engineserver.core.res.ResourceManager;
import com.fy.engineserver.country.manager.CountryManager;
import com.fy.engineserver.datasource.article.concrete.DefaultArticleEntityManager;
import com.fy.engineserver.datasource.article.data.articles.Article;
import com.fy.engineserver.datasource.article.data.entity.ArticleEntity;
import com.fy.engineserver.datasource.article.data.entity.NewMagicWeaponEntity;
import com.fy.engineserver.datasource.article.data.magicweapon.huntLife.HuntLifeEntityManager;
import com.fy.engineserver.datasource.article.data.magicweapon.model.AdditiveAttrModel;
import com.fy.engineserver.datasource.article.data.magicweapon.model.BasicAttrRanModel;
import com.fy.engineserver.datasource.article.data.magicweapon.model.BasicDataModel;
import com.fy.engineserver.datasource.article.data.magicweapon.model.Color4DevourModel;
import com.fy.engineserver.datasource.article.data.magicweapon.model.DevourModel;
import com.fy.engineserver.datasource.article.data.magicweapon.model.HiddenAttrModel;
import com.fy.engineserver.datasource.article.data.magicweapon.model.HiddenRanProbModel;
import com.fy.engineserver.datasource.article.data.magicweapon.model.MagicWeaponAttrModel;
import com.fy.engineserver.datasource.article.data.magicweapon.model.MagicWeaponBaseModel;
import com.fy.engineserver.datasource.article.data.magicweapon.model.MagicWeaponColorUpModule;
import com.fy.engineserver.datasource.article.data.magicweapon.model.TunShiModle;
import com.fy.engineserver.datasource.article.data.magicweapon.operate.MoreMagicWeaponMess;
import com.fy.engineserver.datasource.article.data.props.Knapsack;
import com.fy.engineserver.datasource.article.manager.ArticleEntityManager;
import com.fy.engineserver.datasource.article.manager.ArticleManager;
import com.fy.engineserver.datasource.language.Translate;
import com.fy.engineserver.datasource.skill.Skill;
import com.fy.engineserver.economic.BillingCenter;
import com.fy.engineserver.economic.CurrencyType;
import com.fy.engineserver.economic.ExpenseReasonType;
import com.fy.engineserver.event.EventRouter;
import com.fy.engineserver.event.cate.EventWithObjParam;
import com.fy.engineserver.menu.MenuWindow;
import com.fy.engineserver.menu.Option;
import com.fy.engineserver.menu.Option_Cancel;
import com.fy.engineserver.menu.Option_MagicWeapon_Eat_Sure;
import com.fy.engineserver.menu.Option_MagicWeapon_Strong_Sure;
import com.fy.engineserver.menu.WindowManager;
import com.fy.engineserver.menu.magicweapon.CopyOfOption_ConfirmRefreshExtraAttr;
import com.fy.engineserver.message.CONFIRM_MAGICWEAPON_EAT_REQ;
import com.fy.engineserver.message.CONFIRM_MAGICWEAPON_EAT_RES;
import com.fy.engineserver.message.CONFIRM_SHENSHI_REQ;
import com.fy.engineserver.message.CONFIRM_SHENSHI_RES;
import com.fy.engineserver.message.GameMessageFactory;
import com.fy.engineserver.message.HINT_REQ;
import com.fy.engineserver.message.JIHUO_MAGICWEAPON_HIDDLE_PROP_REQ;
import com.fy.engineserver.message.JIHUO_MAGICWEAPON_HIDDLE_PROP_RES;
import com.fy.engineserver.message.MAGICWEAPON_STRONG_REQ;
import com.fy.engineserver.message.MAGICWEAPON_STRONG_RES;
import com.fy.engineserver.message.NOTIFY_CLIENT_MAGICWEAPON_CHANGE_REQ;
import com.fy.engineserver.message.NOTIFY_CLIENT_SUMMON_MAGICWEAPON_REQ;
import com.fy.engineserver.message.PLAY_ANIMATION_REQ;
import com.fy.engineserver.message.QUERY_ARTICLE_INFO_RES;
import com.fy.engineserver.message.QUERY_MAGICWEAPON_EAT_REQ;
import com.fy.engineserver.message.QUERY_MAGICWEAPON_EAT_RES;
import com.fy.engineserver.message.QUERY_MAGICWEAPON_HIDDLE_PROP_REQ;
import com.fy.engineserver.message.QUERY_MAGICWEAPON_HIDDLE_PROP_RES;
import com.fy.engineserver.message.QUERY_MAGICWEAPON_STRONG_REQ;
import com.fy.engineserver.message.QUERY_MAGICWEAPON_STRONG_RES;
import com.fy.engineserver.message.QUERY_SHENSHI_REQ;
import com.fy.engineserver.message.QUERY_SHENSHI_RES;
import com.fy.engineserver.message.QUERY_WINDOW_RES;
import com.fy.engineserver.message.REFRESH_MAGICWEAPON_HIDDLE_PROP_REQ;
import com.fy.engineserver.message.REFRESH_MAGICWEAPON_HIDDLE_PROP_RES;
import com.fy.engineserver.message.SYNC_MAGICWEAPON_FOR_KNAPSACK_REQ;
import com.fy.engineserver.message.SYNC_MAGICWEAPON_FOR_KNAPSACK_RES;
import com.fy.engineserver.playerAims.manager.PlayerAimManager;
import com.fy.engineserver.playerAims.tool.ReadFileTool;
import com.fy.engineserver.sprite.Player;
import com.fy.engineserver.sprite.Soul;
import com.fy.engineserver.sprite.npc.MemoryNPCManager;
import com.fy.engineserver.sprite.npc.NPC;
import com.fy.engineserver.sprite.pet2.PetGrade;
import com.fy.engineserver.stat.ArticleStatManager;

public class MagicWeaponManager {
	
	public static Logger logger = LoggerFactory.getLogger(MagicWeaponManager.class);
	
	public static MagicWeaponManager instance;
	/** 法宝基础属性配表地址 */
	private String fileName;
	/** 法宝数值配置地址 */
	private String dataFileName;
	/** key=物品前缀名称 */
	public Map<String, BasicAttrRanModel> basicAttrMap = null;
	/** key=附加技能id */
	public Map<Integer, AdditiveAttrModel> mwAxtraAttr = null;
	/** key=法宝颜色   */
	public Map<Integer, MagicWeaponBaseModel> mwBaseModel = null;
	/** 吞噬升级配置 key=法宝当前等级 */
	public Map<Integer, DevourModel> devourModelMap = null;
	/** 存储基础属性  key=法宝装备等级*/
	public Map<Integer, BasicDataModel> basicDataMap = null;
	/** 存储隐藏属性  key=法宝等级*/
	public Map<Integer, HiddenAttrModel> hiddenDataMap = null;
	/** 隐藏属性各属性随机出来的概率 */
	public List<HiddenRanProbModel> hiddenProbabblyModel = null; 
	
	public static Map<String ,String> translates = new LinkedHashMap<String, String>();
	/**  法宝升阶突破  key=法宝颜色 */
	public Map<Integer, MagicWeaponColorUpModule> colorUpMaps = new HashMap<Integer, MagicWeaponColorUpModule>();
			
	/**
	 * key:前缀名
	 * value:统计名
	 */
	public static Map<String ,String> nameStats = new LinkedHashMap<String, String>();
	
	public String [] materialnames = {Translate.重铸符,Translate.天铸符,Translate.神铸符};
	
	public void init() throws Exception {
		long startTime = System.currentTimeMillis();
		instance = this;
		initAttrTypeMapping();
		loadFile();
		loadBasicDataFile();
	}
	
	/**
	 * 穿法宝
	 * @throws Exception 
	 */
	public NewMagicWeaponEntity putOn(Player p, NewMagicWeaponEntity entity, int soulType) throws Exception {
		String result = null;
		ArticleManager am = ArticleManager.getInstance();
		if (am == null) {
			throw new NullPointerException("ArticleManager is null");
		}
		long oldMId = p.getSoul(soulType).getEc().getEquipmentIds()[11];
		if(oldMId == entity.getId()) {
			throw new Exception("[穿法宝出错] [要穿的法宝id和身上已经存在的法宝id相同] [" + p.getLogString() + "][" + entity.getId() + "]");
		}
		
		Soul soul = p.getSoul(soulType);
		boolean isCurrent = p.getCurrSoul().getSoulType() == soulType;
		if (soul == null) {
			logger.error("[玩家元神不存在] [{}] owenerId[{}] 元神类型[{}]", new Object[] { p.getName(), p.getId(), soulType });
			throw new IllegalStateException("玩家元神不存在");
		}
		Article a = am.getArticle(entity.getArticleName());
		if(a != null && a instanceof MagicWeapon) {
			MagicWeapon m = (MagicWeapon) a;
			result = m.canuse(p, soulType);
			if(result == null) {
				NewMagicWeaponEntity oldm = null;
				if (isCurrent && entity.getMdurability() <= 0) {				//当前法宝没有耐久需要减掉兽魂属性
					HuntLifeEntityManager.instance.unloadAllAttr(p);
				}
				p.getSoul(soulType).getEc().putOnMagicWeapon(entity, soulType);
				if(isCurrent && p.getActiveMagicWeaponId() > 0) {
					try {
						NPC mnpc = MemoryNPCManager.getNPCManager().getNPC(p.getActiveMagicWeaponId());
						Game game = p.getCurrentGame();
						if(mnpc != null && game != null) {
							game.removeSprite(mnpc);
						}
					} catch(Exception e) {
						logger.error("[从场景中移除法宝npc出错] [ " + p.getLogString() + "]");
					}
					p.setActiveMagicWeaponId(-1);
				}
				if(oldMId > 0) {
					ArticleEntityManager aem = ArticleEntityManager.getInstance();
					oldm = (NewMagicWeaponEntity) aem.getEntity(oldMId);
				}
				if(entity.getMdurability() > 0) {		//法宝是否有耐久度
					if(logger.isDebugEnabled()) {
						logger.debug("[穿法宝] [" + p.getLogString() + "]");
					}
					if(entity.getBasiAttr() != null) {
					//计算基础属性
						p = loadMagicWeaponPropertyValue(p, MagicWeaponConstant.basiceAttr, Arrays.asList(entity.getBasiAttr()), soulType, entity.getMcolorlevel(), true);
					}
					//计算隐藏属性
					p = loadMagicWeaponPropertyValue(p, MagicWeaponConstant.hiddenAttr, entity.getHideproterty(), soulType, entity.getMcolorlevel());
					//计算附加技能
					p = loadMagicWeaponPropertyValue(p, MagicWeaponConstant.addtiveAttr, entity.getAdditiveAttr(), soulType, entity.getMcolorlevel());
					if (isCurrent) {
						HuntLifeEntityManager.instance.loadAllAttr(p);
					}
					entity.owner = p;
				}
				if(isCurrent) {		//召唤出法宝npc
					MagicWeaponNpc mNpc = (MagicWeaponNpc) MemoryNPCManager.getNPCManager().createNPC(m.getNpcId());
					if(mNpc == null) {
						logger.error("[法宝npc配置错误] [" + p.getLogString() + "][" + entity.getArticleName() + "] [npcid:" + m.getNpcId() + "]");
					} else {
						mNpc.setSpeedA(p.getSpeed());
						mNpc.removeMoveTrace();
						mNpc.setX(p.getX() + MagicWeaponConstant.offsetX);
						mNpc.setY(p.getY() + MagicWeaponConstant.offsetY);
						if(entity.avatarRace != null && !entity.avatarRace.isEmpty()) {
							String[] ta = entity.avatarRace.split("_");
							if(ta.length == 2) {
								mNpc.setAvataRace(ta[0]);
								mNpc.setAvataSex(ta[1]);
								Avata a1 = ResourceManager.getInstance().getAvata(mNpc);
								mNpc.setAvata(a1.avata);
								mNpc.setAvataType(a1.avataType);
							} else {
								logger.warn("[法宝npc形象配置错误] [" + p.getLogString() + "] [" + entity.avatarRace + "] [" + entity.getArticleName() + "]");
							}
						}
						if(entity.particle != null && !entity.particle.isEmpty()) {
							mNpc.setParticleName(entity.particle);
						}
						p.setActiveMagicWeaponId(mNpc.getId());
						if(p.getCurrentGame() != null) {
							p.getCurrentGame().addSprite(mNpc);
							if(MagicWeaponManager.logger.isDebugEnabled()) {
								MagicWeaponManager.logger.debug("[法宝加入地图][" + p.getLogString() + "] [" + mNpc.getId() + "]");
							}
							NOTIFY_CLIENT_SUMMON_MAGICWEAPON_REQ req = new NOTIFY_CLIENT_SUMMON_MAGICWEAPON_REQ(GameMessageFactory.nextSequnceNum(), mNpc.getId());
							p.addMessageToRightBag(req);
							if(MagicWeaponManager.logger.isDebugEnabled()) {
								MagicWeaponManager.logger.debug("[通知客户端法宝id][" + p.getLogString() + "] [" + mNpc.getId() + "]");
							}
						}
					}
				}
				try {
					EventWithObjParam evt = new EventWithObjParam(com.fy.engineserver.event.Event.RECORD_PLAYER_OPT, new Object[] { p.getId(), RecordAction.装备一个法宝, 1L});
					EventRouter.getInst().addEvent(evt);
					if (entity.getMstar() >= 10) {
						EventWithObjParam evt2 = new EventWithObjParam(com.fy.engineserver.event.Event.RECORD_PLAYER_OPT, new Object[] { p.getId(), RecordAction.法宝星级, entity.getMstar()});
						EventRouter.getInst().addEvent(evt2);
					}
				} catch (Exception eex) {
					PlayerAimManager.logger.error("[目标系统] [统计玩家穿戴法宝异常] [" + p.getLogString() + "]", eex);
				}
				return oldm;
			} else {
				throw new Exception("[穿法宝数据异常] [" + p.getLogString() + "][" + result + "]");
			}
		} else {
			throw new Exception("装备实体[" + entity.getArticleName() + "]对应的装备不存在");
		}
	}
	/**
	 * 脱法宝 
	 * @param flag  标记是否为装备替换
	 * @throws Exception 
	 */
	public void putOff(Player p, NewMagicWeaponEntity entity, int soulType, boolean flag) throws Exception {
		ArticleManager am = ArticleManager.getInstance();
		if (am == null) {
			throw new NullPointerException("ArticleManager is null");
		}
		Soul soul = p.getSoul(soulType);
		boolean isCurrent = p.getCurrSoul().getSoulType() == soulType;
		if (soul == null) {
			logger.error("[玩家元神不存在] [{}] owenerId[{}] 元神类型[{}]", new Object[] { p.getName(), p.getId(), soulType });
			throw new IllegalStateException("玩家元神不存在");
		}
		if(!flag && isCurrent) {
			long npcId = p.getActiveMagicWeaponId();
			if(npcId > 0) {
				NPC npc = MemoryNPCManager.getNPCManager().getNPC(npcId);
				Game game = p.getCurrentGame();
				if(npc != null && game != null) {
					game.removeSprite(npc);
				}
			}
			p.setActiveMagicWeaponId(-1);
		}
		Article a = am.getArticle(entity.getArticleName());
		if(a != null && a instanceof MagicWeapon) {
//			MagicWeapon m = (MagicWeapon) a;
			if(!flag) {
				soul.getEc().takeOffMagicWeapon(entity, soulType);
			}
			if(entity.getMdurability() > 0) {		//法宝是否有耐久度
				if(logger.isDebugEnabled()) {
					logger.debug("[脱法宝] [" + p.getLogString() + "]");
				}
				//计算附加技能
				p = unloadMagicWeaponPropertyValue(p, MagicWeaponConstant.addtiveAttr, entity.getAdditiveAttr(), soulType, entity.getMcolorlevel());
				//计算隐藏属性
				p = unloadMagicWeaponPropertyValue(p, MagicWeaponConstant.hiddenAttr, entity.getHideproterty(), soulType, entity.getMcolorlevel());
				if(entity.getBasiAttr() != null) {
					//计算基础属性
					p = unloadMagicWeaponPropertyValue(p, MagicWeaponConstant.basiceAttr, Arrays.asList(entity.getBasiAttr()), soulType, entity.getMcolorlevel(), true);
				}
			}
		}
	}
	
	public void addWearTime(Player p, NewMagicWeaponEntity entity) throws Exception {
		if(entity == null) {
			return;
		}
		if(entity.getMdurability() <= 0) {
			return;
		}
		int tt = entity.getWearTime() + 1;
		if(tt >= MagicWeaponConstant.LASTWARNTIME) {
			tt = 0;
			int du = entity.getMdurability() - 1;
			if(du <= 0) {
				if (ArticleManager.logger.isWarnEnabled()) {
					ArticleManager.logger.warn("[法宝灵气为零前]" + p.getPlayerPropsString());
				}
				du = 0;
				//计算附加技能
				/*p = unloadMagicWeaponPropertyValue(p, MagicWeaponConstant.hiddenAttr, entity.getHideproterty(), p.getCurrSoul().getSoulType(), entity.getMcolorlevel());
				//计算隐藏属性
				p = unloadMagicWeaponPropertyValue(p, MagicWeaponConstant.addtiveAttr, entity.getAdditiveAttr(), p.getCurrSoul().getSoulType(), entity.getMcolorlevel());
				if(entity.getBasiAttr() != null) {
					//计算基础属性
					p = unloadMagicWeaponPropertyValue(p, MagicWeaponConstant.basiceAttr, Arrays.asList(entity.getBasiAttr()), p.getCurrSoul().getSoulType(), entity.getMcolorlevel());
				}*/
				//计算附加技能
				p = unloadMagicWeaponPropertyValue(p, MagicWeaponConstant.addtiveAttr, entity.getAdditiveAttr(), p.getCurrSoul().getSoulType(), entity.getMcolorlevel());
				//计算隐藏属性
				p = unloadMagicWeaponPropertyValue(p, MagicWeaponConstant.hiddenAttr, entity.getHideproterty(), p.getCurrSoul().getSoulType(), entity.getMcolorlevel());
				if(entity.getBasiAttr() != null) {
					//计算基础属性
					p = unloadMagicWeaponPropertyValue(p, MagicWeaponConstant.basiceAttr, Arrays.asList(entity.getBasiAttr()), p.getCurrSoul().getSoulType(), entity.getMcolorlevel(), true);
				}
				
				HuntLifeEntityManager.instance.unloadAllAttr(p);
				MagicWeaponManager.logger.warn("[法宝灵气为零][" + p.getLogString() + "] [法宝id:" + entity.getId() + "]");
				if (ArticleManager.logger.isWarnEnabled()) {
					ArticleManager.logger.warn("[法宝灵气为零后]" + p.getPlayerPropsString());
				}
				
			}
			entity.setMdurability(du);
			NOTIFY_CLIENT_MAGICWEAPON_CHANGE_REQ req = new NOTIFY_CLIENT_MAGICWEAPON_CHANGE_REQ(GameMessageFactory.nextSequnceNum(), entity.getId());
			p.addMessageToRightBag(req);
		}
		entity.setWearTime(tt);
	}
	
	private Player loadMagicWeaponPropertyValue(Player p, int attrAddType, List<MagicWeaponAttrModel> attrList, int soulType, int eeLevel) throws Exception {
		return loadMagicWeaponPropertyValue(p, attrAddType, attrList, soulType, eeLevel, false);
	}
	
	/**
	 * 
	 * @param p
	 * @param attrAddType	基础、隐藏属性或附加技能
	 * @param attrList	
	 * @throws Exception 
	 */
	private Player loadMagicWeaponPropertyValue(Player p, int attrAddType, List<MagicWeaponAttrModel> attrList, int soulType, int eeLevel, boolean isBasiAttr) throws Exception {
		if(p != null && attrList != null && attrList.size() > 0) {
			boolean isCurrSoul = p.isCurrSoul(soulType);
			Soul soul = p.getSoul(soulType);
			double add = isCurrSoul ? 1 : (soul.getAddPercent() + p.getUpgradeNums() * 0.01);
			for(int i=0; i<attrList.size(); i++) {
				int tempValue = attrList.get(i).getAttrNum();
				if (isBasiAttr) {
					tempValue = attrList.get(i).getNewAttrNum();
				}
				if(attrAddType == MagicWeaponConstant.addtiveAttr) {			//附加属性需要判断是否开启
					AdditiveAttrModel am = MagicWeaponManager.instance.mwAxtraAttr.get(attrList.get(i).getId());
					if(eeLevel < am.getNeedLevel()) {
						continue;
					}
				}
				int idType = attrList.get(i).getId();
				if(attrAddType == MagicWeaponConstant.addtiveAttr) {
					AdditiveAttrModel amm = MagicWeaponManager.instance.mwAxtraAttr.get(attrList.get(i).getId());
					idType = amm.getBaseAttrNumByIndex();
				}
				int addType = getAddTypeByType(attrAddType, idType);
				if(attrAddType == MagicWeaponConstant.addtiveAttr && !attrList.get(i).isAct()) {
					continue;
				}
				if(addType == MagicWeaponConstant.add_typeNum) {			//具体数值
					int value = tempValue + attrList.get(i).getExtraAddAttr();
					p = addAttr2Player(p, idType,value, add, isCurrSoul, soulType);
				} else if(addType == MagicWeaponConstant.add_typePercent4Person) {		//人物属性加成
					int value = attrList.get(i).getAttrNum();
					if(attrAddType == MagicWeaponConstant.addtiveAttr) {
						value = value / 10; 
					}
					p = addAttrP2Player(p, idType,value, add, isCurrSoul, soulType);
				} else {			//法宝基础属性加成----使用extraAttrNum加到玩家身上
					int value = attrList.get(i).getExtraAddAttr();
					p = addAttr2Player(p, idType,value, add, isCurrSoul, soulType);
				}
			}
		}
		return p;
	}
	
	private Player unloadMagicWeaponPropertyValue(Player p, int attrAddType, List<MagicWeaponAttrModel> attrList, int soulType, int eeLevel) throws Exception {
		return unloadMagicWeaponPropertyValue(p, attrAddType, attrList, soulType, eeLevel, false);
	}
	/**
	 * 卸载法宝数值
	 * @param p
	 * @param attrAddType
	 * @param attrList
	 * @param soulType
	 * @param eeLevel
	 * @return
	 * @throws Exception
	 */
	private Player unloadMagicWeaponPropertyValue(Player p, int attrAddType, List<MagicWeaponAttrModel> attrList, int soulType, int eeLevel, boolean isBasiAttr) throws Exception {
		if(p != null && attrList != null && attrList.size() > 0) {
			boolean isCurrSoul = p.isCurrSoul(soulType);
			Soul soul = p.getSoul(soulType);
			double add = isCurrSoul ? 1 : (soul.getAddPercent() + p.getUpgradeNums() * 0.01);
			for(int i=0; i<attrList.size(); i++) {
				int tempValue = attrList.get(i).getAttrNum();
				if (isBasiAttr) {
					tempValue = attrList.get(i).getNewAttrNum();
				}
				if(attrAddType == MagicWeaponConstant.addtiveAttr) {			//附加属性需要判断是否开启
					AdditiveAttrModel am = MagicWeaponManager.instance.mwAxtraAttr.get(attrList.get(i).getId());
					if(eeLevel < am.getNeedLevel()) {
						continue;
					}
				}
				int idType = attrList.get(i).getId();
				if(attrAddType == MagicWeaponConstant.addtiveAttr) {
					AdditiveAttrModel amm = MagicWeaponManager.instance.mwAxtraAttr.get(attrList.get(i).getId());
					idType = amm.getBaseAttrNumByIndex();
				}
				int addType = getAddTypeByType(attrAddType, idType);
				if(attrAddType == MagicWeaponConstant.addtiveAttr && !attrList.get(i).isAct()) {
					continue;
				}
				if(addType == MagicWeaponConstant.add_typeNum) {			//具体数值
					int value = tempValue + attrList.get(i).getExtraAddAttr();
					p = addAttr2Player(p, idType,-value, add, isCurrSoul, soulType);
				} else if(addType == MagicWeaponConstant.add_typePercent4Person) {		//人物属性加成
					int value = attrList.get(i).getAttrNum();
					if(attrAddType == MagicWeaponConstant.addtiveAttr) {
						value = value / 10; 
					}
					p = addAttrP2Player(p, idType,-value, add, isCurrSoul, soulType);
				} else {			//法宝基础属性加成----使用extraAttrNum加到玩家身上
					int value = attrList.get(i).getExtraAddAttr();
					p = addAttr2Player(p, idType,-value, add, isCurrSoul, soulType);
				}
			}
		}
		return p;
	}
	/**
	 * 给玩家加百分比数值
	 * @param p
	 * @param aaType--属性type
	 * @param value
	 * @param add
	 * @param isCurrSoul
	 * @param soulType
	 * @return
	 */
	private Player addAttrP2Player(Player p, int aaType, int value, double add, boolean isCurrSoul, int soulType) {
		Soul soul = p.getSoul(soulType);
		switch (aaType) {
		case MagicWeaponConstant.mpNum:
			if(isCurrSoul) {
				p.setMaxMPC(p.getMaxMPC() + value);
			}
			soul.setMaxMpC(soul.getMaxMpC() + value);
			break;
		case MagicWeaponConstant.打怪经验:			
			if(isCurrSoul) {
				p.setExpPercent(p.getExpPercent() + value);
			}
			break;
		case MagicWeaponConstant.法宝吞噬经验:		//player中加字段
			if(isCurrSoul) {
				p.setMagicWeaponDevourPercent((p.getMagicWeaponDevourPercent() + value));
			}
			break;
		case MagicWeaponConstant.宠物打怪经验:		//pet中设置?
			if(isCurrSoul) {
				p.setPetExpPercent(p.getPetExpPercent() + value);
			}
			break;
		case MagicWeaponConstant.破甲:
			if(isCurrSoul) {
				p.setBreakDefenceRateOther(p.getBreakDefenceRateOther() + value);
				p.setBreakDefence(p.getBreakDefence());
			}
			break;
		case MagicWeaponConstant.hitNum:
			if(isCurrSoul) {
				p.setHitRateOther(p.getHitRateOther() + value);
				p.setHit(p.getHit());
			}
			break;
		case MagicWeaponConstant.dodgeNum:
			if(isCurrSoul) {
				p.setDodgeRateOther(p.getDodgeRateOther() + value);
				p.setDodge(p.getDodge());
			}
			break;
		case MagicWeaponConstant.火炕:
			if(isCurrSoul) {
				p.setFireDefenceRateOther(p.getFireDefenceRateOther() + value);
				p.setFireDefence(p.getFireDefence());
			}
			break;
		case MagicWeaponConstant.冰抗:
			if(isCurrSoul) {
				p.setBlizzardDefenceRateOther(p.getBlizzardDefenceRateOther() + value);
				p.setBlizzardDefence(p.getBlizzardDefence());
			}
			break;
		case MagicWeaponConstant.风抗:
			if(isCurrSoul) {
				p.setWindDefenceRateOther(p.getWindDefenceRateOther() + value);
				p.setWindDefence(p.getWindDefence());
			}
			break;
		case MagicWeaponConstant.雷抗:
			if(isCurrSoul) {
				p.setThunderDefenceRateOther(p.getThunderDefenceRateOther() + value);
				p.setThunderDefence(p.getThunderDefence());
			}
			break;
		case MagicWeaponConstant.火减抗:
			if(isCurrSoul) {
				p.setFireIgnoreDefenceRateOther(p.getFireIgnoreDefenceRateOther() + value);
				p.setFireIgnoreDefence(p.getFireIgnoreDefence());
			}
			break;
		case MagicWeaponConstant.冰减抗:
			if(isCurrSoul) {
				p.setBlizzardIgnoreDefenceRateOther(p.getBlizzardIgnoreDefenceRateOther() + value);
				p.setBlizzardIgnoreDefence(p.getBlizzardIgnoreDefence());
			}
			break;
		case MagicWeaponConstant.风减抗:
			if(isCurrSoul) {
				p.setWindIgnoreDefenceRateOther(p.getWindIgnoreDefenceRateOther() + value);
				p.setWindIgnoreDefence(p.getWindIgnoreDefence());
			}
			break;
		case MagicWeaponConstant.雷减抗:
			if(isCurrSoul) {
				p.setThunderIgnoreDefenceRateOther(p.getThunderIgnoreDefenceRateOther() + value);
				p.setThunderIgnoreDefence(p.getThunderIgnoreDefence());
			}
			break;
		case MagicWeaponConstant.买药打折:
			if (isCurrSoul) {
				p.setMedicineDiscount(p.getMedicineDiscount() + value);
			}
			break;
		case MagicWeaponConstant.回城复活血量增加:
			if (isCurrSoul) {
				p.setAliveHpPercent(p.getAliveHpPercent() + value);
			}
			break;
		case MagicWeaponConstant.修理装备打折:
			if (isCurrSoul) {
				p.setRepairDiscount(p.getRepairDiscount() + value);
			}
			break;
		case MagicWeaponConstant.兽魂属性:
			if (isCurrSoul) {
				boolean b = false;
				if (p.getHuntLifr() != null) {
					b = p.getHuntLifr().isHasLoadAllAttr();
					HuntLifeEntityManager.instance.unloadAllAttr(p);
				}
				p.setShouhunAttr(p.getShouhunAttr() + value);
				if (b) {
					HuntLifeEntityManager.instance.loadAllAttr(p);
				}
			}
			break;
		} 
		return p;
	}
	
	/**
	 * 增加具体数值给玩家
	 * @param p
	 * @param aaType--属性type
	 * @param value
	 * @param add
	 * @param isCurrSoul
	 * @param soulType
	 */
	public Player addAttr2Player(Player p, int aaType, int value, double add, boolean isCurrSoul, int soulType) {
		Soul soul = p.getSoul(soulType);
		switch (aaType) {
		case MagicWeaponConstant.hpNum:
			if (isCurrSoul) {
				p.setMaxHPB(p.getMaxHPB() + value);
			} else {
				p.setMaxHPX(p.getMaxHPX() + (int) (value * add));
			}
			soul.setMaxHpB(soul.getMaxHpB() + value);
			break;
		case MagicWeaponConstant.physicAttNum:
			if (isCurrSoul) {
				p.setPhyAttackB(p.getPhyAttackB() + value);
			} else {
				p.setPhyAttackX(p.getPhyAttackX() + (int) (value * add));
			}
			soul.setPhyAttackB(soul.getPhyAttackB() + value);
			break;
		case MagicWeaponConstant.magicAttNum:
			if (isCurrSoul) {
				p.setMagicAttackB(p.getMagicAttackB() + value);
			} else {
				p.setMagicAttackX(p.getMagicAttackX() + (int) (value * add));
			}
			soul.setMagicAttackB(soul.getMagicAttackB() + value);
			break;
		case MagicWeaponConstant.physicDefanceNum:
			if (isCurrSoul) {
				p.setPhyDefenceB(p.getPhyDefenceB() + value);
			} else {
				p.setPhyDefenceX(p.getPhyDefenceX() + (int) (value * add));
			}
			soul.setPhyDefenceB(soul.getPhyDefenceB() + value);
			break;
		case MagicWeaponConstant.magicDefanceNum:
			if (isCurrSoul) {
				p.setMagicDefenceB(p.getMagicDefenceB() + value);
			} else {
				p.setMagicDefenceX(p.getMagicDefenceX() + (int) (value * add));
			}
			soul.setMagicDefenceB(soul.getMagicDefenceB() + value);
			break;
		case MagicWeaponConstant.mpNum:
			if (isCurrSoul) {
				p.setMaxMPB(p.getMaxMPB() + value);
			} else {
				p.setMaxMPX(p.getMaxMPX() + (int) (value * add));
			}
			soul.setMaxMpB(soul.getMaxMpB() + value);
			break;
		case MagicWeaponConstant.精准:
			if (isCurrSoul) {
				p.setAccurateB(p.getAccurateB() + value);
			} else {
				p.setAccurateX(p.getAccurateX() + (int) (value * add));
			}
			soul.setAccurateX(soul.getAccurateX() + value);
			break;
		case MagicWeaponConstant.cirtNum:
			if (isCurrSoul) {
				p.setCriticalHitB(p.getCriticalHitB() + value);
			} else {
				p.setCriticalHitX(p.getCriticalHitX() + (int) (value * add));
			}
			soul.setCriticalHitX(soul.getCriticalHitX() + value);
			break;
		case MagicWeaponConstant.免爆:
			if (isCurrSoul) {
				p.setCriticalDefenceB(p.getCriticalDefenceB() + value);
			} else {
				p.setCriticalDefenceX(p.getCriticalDefenceX() + (int) (value * add));
			}
			soul.setCriticalDefenceX(soul.getCriticalDefenceX() + value);
			break;
		case MagicWeaponConstant.火攻:
			if (isCurrSoul) {
				p.setFireAttackB(p.getFireAttackB() + value);
			} else {
				p.setFireAttackX(p.getFireAttackX() + (int) (value * add));
			}
			soul.setFireAttackX(soul.getFireAttackX() + value);
			break;
		case MagicWeaponConstant.风攻:
			if (isCurrSoul) {
				p.setWindAttackB(p.getWindAttackB() + value);
			} else {
				p.setWindAttackX(p.getWindAttackX() + (int) (value * add));
			}
			soul.setWindAttackX(soul.getWindAttackX() + value);
			break;
		case MagicWeaponConstant.雷攻:
			if (isCurrSoul) {
				p.setThunderAttackB(p.getThunderAttackB() + value);
			} else {
				p.setThunderAttackX(p.getThunderAttackX() + (int) (value * add));
			}
			soul.setThunderAttackX(soul.getThunderAttackX() + value);
			break;
		case MagicWeaponConstant.冰攻:
			if (isCurrSoul) {
				p.setBlizzardAttackB(p.getBlizzardAttackB() + value);
			} else {
				p.setBlizzardAttackX(p.getBlizzardAttackX() + (int) (value * add));
			}
			soul.setBlizzardAttackX(soul.getBlizzardAttackX() + value);
			break;
		case MagicWeaponConstant.hitNum:
			if (isCurrSoul) {
				p.setHitB(p.getHitB() + value);
			} else {
				p.setHitX(p.getHitX() + (int) (value * add));
			}
			soul.setHitB(soul.getHitB() + value);
			break;
		case MagicWeaponConstant.dodgeNum:
			if (isCurrSoul) {
				p.setDodgeB(p.getDodgeB() + value);
			} else {
				p.setDodgeX(p.getDodgeX() + (int) (value * add));
			}
			soul.setDodgeB(soul.getDodgeB() + value);
			break;
		}
		return p;
	}
	/**
	 * 根据基本、隐藏、附加技能获得增加的数值类型
	 * @param type
	 * @return
	 * @throws Exception 
	 */
	public int getAddTypeByType(int type, int attrType) throws Exception {
		int result = -1;
		switch (type) {
		case MagicWeaponConstant.addtiveAttr:
			result = AdditiveAttrModel.getAttrAddType(attrType);
			break;
		case MagicWeaponConstant.basiceAttr:
			result = BasicDataModel.getAttrAddType(attrType);
			break;
		case MagicWeaponConstant.hiddenAttr:
			result = HiddenAttrModel.getAttrAddType(attrType);
			break;
		}
		return result;
	}
	
	public static long getRefreshCost(int refreshTimes) {
		if (refreshTimes <= 0) {
			return 0;
		}
		int tempNum = refreshTimes ;
		if (tempNum >= 5) {
			tempNum = 5;
		}
		return (long) ((7 * tempNum + Math.pow(tempNum, 3)) * 45000);
	}
	
	public String refreshExtraAttr(Player player, long id, boolean confirm) {
		synchronized (player) {
			ArticleEntity ae = ArticleEntityManager.getInstance().getEntity(id);
			Knapsack bag = player.getKnapsack_common();
			if (!bag.contains(ae)) {
				return "error";
			}
			if (ae instanceof NewMagicWeaponEntity) {
				long cost = getRefreshCost(((NewMagicWeaponEntity)ae).getIllusionLevel());
				if (cost > 0) {
					if (player.getSilver() < cost) {
						return Translate.银子不足;
					}
					if (!confirm) {			//加二次确认
						MenuWindow mw=WindowManager.getInstance().createTempMenuWindow(300);
						mw.setDescriptionInUUB(String.format(Translate.法宝唤虚提示, BillingCenter.得到带单位的银两(cost)));
						CopyOfOption_ConfirmRefreshExtraAttr option = new CopyOfOption_ConfirmRefreshExtraAttr();
						option.setText(Translate.text_125);
						option.setArticleId(id);
						Option_Cancel oc=new Option_Cancel();
						oc.setText(Translate.text_126);
						oc.setColor(0xffffff);
						mw.setOptions(new Option[]{option,oc});
						
						QUERY_WINDOW_RES res = new QUERY_WINDOW_RES(GameMessageFactory.nextSequnceNum(),mw,mw.getOptions());
						player.addMessageToRightBag(res);
						return "";
					}
					try {	
						BillingCenter.getInstance().playerExpense(player, cost, CurrencyType.YINZI, ExpenseReasonType.刷新法宝附加技能, "刷新法宝附加技能");
					} catch (Exception e) {
						return Translate.银子不足;
					}
				}
				return refreshExtraAttr(player, ((NewMagicWeaponEntity)ae));
			}
		}
		return Translate.服务器出现错误;
	}
	
	/**
	 * 刷法宝附加属性
	 * @param p
	 * @param entity
	 * @return
	 */
	private String refreshExtraAttr(Player player, NewMagicWeaponEntity entity) {
		try {
			MagicWeaponBaseModel mbm = mwBaseModel.get(entity.getColorType());
			int additiveNum = player.random.nextInt((mbm.getMaxAdditiveAttr() - mbm.getMinAdditiveAttr()) + 1) + mbm.getMinAdditiveAttr();
			List<MagicWeaponAttrModel> additiveAttr = new ArrayList<MagicWeaponAttrModel>();
			StringBuffer sb = new StringBuffer();
			int tempId = 0;
			int ranNum = 0;
			for (int j = 0; j < mbm.getAdditiveSkillList().size(); j++) {
				ranNum += mbm.getAdditiveSkillList().get(j).getProbbly();
			}
			for (int i = 0; i < additiveNum; i++) {
				int randomNum = 0;
				if (player != null) {
					randomNum = player.random.nextInt(ranNum);
				} else {
					randomNum = new Random().nextInt(ranNum);
				}
				int oldProbabbly = 0;
				for (int j = 0; j < mbm.getAdditiveSkillList().size(); j++) {
					oldProbabbly += mbm.getAdditiveSkillList().get(j).getProbbly();
					if (oldProbabbly >= randomNum) {
						tempId = mbm.getAdditiveSkillList().get(j).getSkillId();
						AdditiveAttrModel tempSkModel = this.mwAxtraAttr.get(tempId);
						int[] result = tempSkModel.getAttrNumByType(tempSkModel.getBaseAttrNumByIndex());
						MagicWeaponAttrModel temp = new MagicWeaponAttrModel(tempId, result[1], result[0], tempSkModel.getDescreption());
						additiveAttr.add(temp);
						sb.append(temp);
						if (MagicWeaponManager.logger.isDebugEnabled() && player != null) {
							MagicWeaponManager.logger.debug("[刷法宝附加属性] [根据概率随机获得附加技能] [" + (player != null ? player.getLogString() : "null") + "] [随机数" + randomNum + "获得技能id:" + tempId + "]");
						}
						break;
					}
				}
			}
			entity.setIllusionLevel(entity.getIllusionLevel() + 1);		//以前没用字段，现用来记录玩家刷新法宝附加技能的次数
			entity.setAdditiveAttr(additiveAttr);
			entity.initAttrNum();
			if (logger.isWarnEnabled()) {
				logger.warn("[刷法宝附加属性] [成功] ["+player.getLogString()+"] [aeId:"+entity.getId()	+"] [附加属性:" + sb.toString() + "]");
			}
			return null;
		} catch (Exception e) {
			logger.warn("[刷法宝附加属性] [异常] [" + player.getLogString() + "] [aeId:" + entity.getId() + "]", e);
		}
		return Translate.服务器出现错误;
	}
	
	/**
	 * 鉴定一件法宝
	 * @param entity
	 */
	public String appraisal(Player p, NewMagicWeaponEntity entity,boolean isRefreshHiddleProp) {
		String result = null;
		if(entity.getBasicpropertyname() != null && !entity.getBasicpropertyname().isEmpty() && entity.getBasiAttr() != null && !isRefreshHiddleProp) {
			result = "已鉴定过";
		} else  {
			try{
				int len = MagicWeaponConstant.pre_nameList.size();
				String title = MagicWeaponConstant.pre_nameList.get(p.random.nextInt(len));
				BasicAttrRanModel bm = this.basicAttrMap.get(title);
				MagicWeapon article = (MagicWeapon) ArticleManager.getInstance().getArticle(entity.getArticleName());
				if(bm == null || article == null) {
					logger.error("[鉴定法宝出错，没有得到配置属性] [" + p.getLogString() + "] [" + title + "]");
					result = "服务器出错";
				} else {
					//鉴定获得基础属性
					MagicWeaponAttrModel[] basicAttrs = new MagicWeaponAttrModel[bm.getAttrList().size()];
					for(int i=0; i<basicAttrs.length; i++) {
						int attrType = bm.getAttrList().get(i);
						int[] resultNum = this.getAttrNumByLevelAndType(p, article.getDataLevel(), MagicWeaponConstant.basiceAttr, attrType, entity.getColorType());
						basicAttrs[i] = new MagicWeaponAttrModel(attrType, resultNum[1], resultNum[0], "基础属性值");
					}
					entity.setBasicpropertyname(title);
					entity.setBasicpropertyname_stat(nameStats.get(title));
					entity.setBasiAttr(basicAttrs);
					this.recordMagicOpt(p, RecordAction.法宝神识, article.getLevellimit());
					//鉴定获得隐藏属性，有可能一个都不获得
					if(!isRefreshHiddleProp){
						MagicWeaponBaseModel mm = mwBaseModel.get(entity.getColorType());	
						if(mm == null) {
							throw new Exception("[鉴定法宝出错,没有获取到隐藏属性model] [" + p.getLogString() + "] [color:" + entity.getColorType() + "]");
						}
						if(mm.getHiddenAttrNum() > 0 && mm.getAppraisalNum() > 0) {			//判断是否有且可以在鉴定时获取到隐藏属性才做处理
							int hiddenNum = p.random.nextInt(mm.getAppraisalNum());
							if(hiddenNum > 0) {
								for(int i=0; i<hiddenNum; i++) {
									if(entity.getHideproterty().size() < mm.getHiddenAttrNum()) {		//隐藏属性上限
										MagicWeaponAttrModel mwa = getHiddenAttr(p, article.getDataLevel(), entity.getColorType());
										List<MagicWeaponAttrModel> tempH = entity.getHideproterty();
										tempH.add(mwa);
										entity.setHideproterty(tempH);
									}
								}
							}
						}
					}
				}
			}catch(Exception e) {
				logger.error("[鉴定法宝出错] [" + p.getLogString() + "]", e);
				result = "服务器出错";
			}
		}
		return result;
	}
	/**
	 * 
	 * @param type RecordAction中的type
	 * @param mLevel	法宝level
	 */
	private void recordMagicOpt(Player player, RecordAction recordAction, int mLevel) {
		try {
			AchievementManager.getInstance().record(player, recordAction);;
		} catch (Exception e) {
			PlayerAimManager.logger.error("[目标系统] [记录法宝操作数据异常] [" + recordAction.getName() + "] [mlevel:" + mLevel + "][" + player.getLogString() + "]", e);
		}
	}
	/**
	 * 随机获取一条隐藏属性（激活隐藏属性时候用）
	 * @param level 法宝携带等级
	 * @return
	 * @throws Exception 
	 */
	public MagicWeaponAttrModel getHiddenAttr(Player p, int level, int colorType) throws Exception {
		int ranNum = p.random.nextInt(MagicWeaponConstant.tenThousand);
		int oldProbabbly = 0;
		int attrType = -1;
		for(HiddenRanProbModel hm : hiddenProbabblyModel) {
			oldProbabbly += hm.getProbabbly();
			if(oldProbabbly >= ranNum) {
				attrType = hm.getAttrType();
				break;
			}
		}
		if(attrType == -1) {
			throw new Exception("[随机获取隐藏属性出错] [没有获取到隐藏属性，随机数:" + ranNum + "][配表读出概率总值:" + oldProbabbly + "]");
		}
		HiddenAttrModel ham = hiddenDataMap.get(level);
		if(ham == null) {
			throw new Exception("[随机获取隐藏属性出错] [配表中没有找到对应等级数值] [level:" + level + "]");
		}
		int[] resultNum = getAttrNumByLevelAndType(p, level, MagicWeaponConstant.hiddenAttr, attrType, colorType);
		MagicWeaponAttrModel mwa = new MagicWeaponAttrModel(attrType, resultNum[1], resultNum[0], "法宝隐藏属性");
		return mwa;
	}
	/**
	 * 基本、隐藏属性具体值获取方法
	 * @param level			携带等级
	 * @param addType		基本、隐藏属性			
	 * @param attrType		属性类型（气血等，减magicweaponconstant）
	 * @return		result[0]=属性增加类型（数值、千分比等）  result[1]=属性增加具体数值
	 * @throws Exception 
	 */
	public int[] getAttrNumByLevelAndType(Player p, int level, int addType, int attrType, int colorType) throws Exception {
		int[] result = new int[2];
		if(addType == MagicWeaponConstant.basiceAttr) {				//基本属性
			BasicDataModel bm = basicDataMap.get(level);
			if(bm == null) {
				throw new Exception("[没获取到法宝基础属性model] [level:" + level + "]");
			}
			int[] tempresult = bm.getAttrNumByType(attrType);
			result[0] = tempresult[0];
			if(result[0] == MagicWeaponConstant.add_typeNum) {				
				result[1] = getInitalNum(p, tempresult[1], colorType);
			} else {							
				result[1] = getInitalP(p, tempresult[1]);
			}
		} else if(addType == MagicWeaponConstant.hiddenAttr) {		//隐藏属性
			HiddenAttrModel hm = hiddenDataMap.get(level);
			if(hm == null) {
				throw new Exception("[没获取到法宝隐藏属性model] [level:" + level + "]");
			}
			int[] tempresult = hm.getAttrNumByType(attrType);
			result = tempresult;
			logger.debug("[隐藏属性数值] [" + result[1] + "][类型:" + result[0] + "]");
			if(result[0] == MagicWeaponConstant.add_typeNum) {				
				result[1] = getHiddenNum(p, tempresult[1], colorType);
			} else {							
				result[1] = getHiddenP(p, tempresult[1], colorType);
			}
			logger.debug("[计算后隐藏属性数值] [" + result[1] + "][类型:" + result[0] + "]");
		}
		return result;
	}
	/**
	 * 获得隐藏属性最大值。。显示用
	 * @param tempNum			配表中数值
	 * @param colorType			法宝颜色
	 * @param addType			增加类型，百分比还是具体数值
	 * @return
	 */

	public int getHiddenMaxAttr(int tempNum, int colorType, int addType) {
		int result = 0;
		if(addType == MagicWeaponConstant.add_typeNum) {
			result = (int) ((tempNum * (MagicWeaponConstant.colorWeight[colorType] / 100) * (MagicWeaponConstant.hiddenWeight / 100))) / 5;
		} else if(addType == MagicWeaponConstant.add_typePercent4Person) {
			result = (int) (tempNum / 5L * MagicWeaponConstant.colorWeight4H[colorType] / 100);
		}
		return result;
	}
	
	/**
	 * 隐藏属性百分比计算
	 * @param p
	 * @param tempNum
	 * @return
	 * @throws Exception 
	 */
	public int getHiddenP(Player p, int tempNum, int colorType) throws Exception {
		if(colorType > MagicWeaponConstant.colorWeight4H.length) {
			throw new Exception("[计算法宝数值错误] [配置颜色不正确:" + colorType + "]");
		}
		int result = 0;
		result = (int) (tempNum / 5L * MagicWeaponConstant.colorWeight4H[colorType] / 100);
		int prob = p.random.nextInt(MagicWeaponConstant.tenThousand);
		int index = 4;
		int tempP = 0;
		for(int i=0; i<MagicWeaponConstant.deductProblem.length; i++) {
			tempP += MagicWeaponConstant.deductProblem[i];
			if(prob <= tempP) {
				index = i;
				break;
			}
		}
		result -= result * MagicWeaponConstant.deductHiddenP[index] / 100;					//向下递减
		return result;
	}
	/**
	 *  隐藏属性数值计算
	 * @param p
	 * @param tempNum
	 * @return
	 * @throws Exception 
	 */
	public int getHiddenNum(Player p, int tempNum, int colorType) throws Exception {	
		if(colorType > MagicWeaponConstant.colorWeight.length) {
			throw new Exception("[计算法宝数值错误] [配置颜色不正确:" + colorType + "]");
		}
		int result = 0;
		result = (int) ((tempNum * (MagicWeaponConstant.colorWeight[colorType] / 100) * (MagicWeaponConstant.hiddenWeight / 100))) / 5;
		int prob = p.random.nextInt(MagicWeaponConstant.tenThousand);
		int index = 4;
		int tempP = 0;
		for(int i=0; i<MagicWeaponConstant.deductProblem.length; i++) {
			tempP += MagicWeaponConstant.deductProblem[i];
			if(prob <= tempP) {
				index = i;
				break;
			}
		}
		result -= result * MagicWeaponConstant.deductHiddenP[index] / 100;					//向下递减
		return result;
	}
	
	/**
	 * 获得影藏属性条颜色
	 * @param attrNum
	 * @param maxNum
	 * @return
	 */
	public int getHiddenAttrColor(int attrNum, int maxNum) {
		int tempNum = 0;
		int tempNum1 = 0;
		int index = 0;
		if(attrNum == maxNum) {
			index = 4;
		} else {
			for(int i=1; i<MagicWeaponConstant.deductHiddenP.length; i++) {
				tempNum = maxNum * MagicWeaponConstant.deductHiddenP[i-1] / 100;
				tempNum1 = maxNum * MagicWeaponConstant.deductHiddenP[i] / 100;
				if(tempNum <= attrNum) {
					index = 3- (i-1);
					break;
				} else if(tempNum1 <= attrNum) {
					index = 3 - i;
					break;
				}
			}
		}
		return index;
	}
	
	/**
	 * 法宝初值百分比计算
	 * @param p
	 * @param tempNum
	 * @return
	 */
	public int getInitalP(Player p, int tempNum) {
		int result = 0;
		result = tempNum - p.random.nextInt(5);	//向下减少千分之5
		return result;
	}
	/**
	 * 根据基本属性值与强化级别计算出强化后应该额外增加的属性值
	 * @param baseNum
	 * @param aidLevel
	 * @return
	 */
	public int getAidNum(int baseNum, int aidLevel) {
		int result = 0;
		if(aidLevel > 0 && aidLevel <= MagicWeaponConstant.starMaxValue) {
			result += baseNum * MagicWeaponConstant.strongValues[aidLevel - 1] / 100;  
		}
		return result;
	}

	public static int getLbyLevel(int level) {
		int result = level / 10;
		if(level > 0 && level % 10 == 0) {
			result -= 1;
		}
		return result;
	}
	/**
	 * 获取法宝1级属性
	 * @param tempNum  配表中数值
	 * @return
	 * @throws Exception 
	 */
	public int getInitalNum(Player p, int tempNum, int colorType) throws Exception {
		int result = 0;
		float numerator = tempNum * 80f / 100f;		//分子		80%为基础数值权重
		float denominator = (100 + MagicWeaponConstant.aidWeight) / 100;
		result = (int) (numerator / denominator * 5f / 100);		//5%为初值权重
		result -= result * new Random().nextInt(500) / MagicWeaponConstant.tenThousand;		//数值向下浮动百分之5
		result = getInitAttrDevelNum(result, colorType, 1);
		return result;
	}
	private int getInitAttrDevelNum(float baseNum, int colorType, int level) throws Exception {
		if(colorType > MagicWeaponConstant.newColorWeight.length) {
			throw new Exception("[计算法宝数值错误] [配置颜色不正确:" + colorType + "]");
		}
		int result = 0;
		float ff = level;
		int tl = getLbyLevel(level);
		if(tl >= MagicWeaponConstant.newLevelWeight.length) {
			tl = MagicWeaponConstant.newLevelWeight.length - 1;
		}
		float numerator = baseNum * (1f + MagicWeaponConstant.newColorWeight[colorType] / 100 + MagicWeaponConstant.newLevelWeight[tl] / 100f + ff / 32f);
		result = (int) numerator;
		return result;
	}
	/**
	 * 获得法宝升级后属性
	 * @param attrNum
	 * @param colorType
	 * @param level
	 * @return
	 * @throws Exception 
	 */
	public int getDevelAttrNum(int attrNum, int colorType, int level, int oldLevel) throws Exception {
		int result = 0;
		float initAttr = getInitAttrByAttrNum(attrNum, colorType, oldLevel);
		
		result = (int) getInitAttrDevelNum(initAttr, colorType, level);
		
		return result;
	}
	/**
	 * 计算法宝属性
	 * @param attrNum
	 * @param colorType
	 * @param level
	 * @return
	 */
	public int calcMagicweaponAttrNum(int attrNum, int colorType, int level) {
		int result = 0;
		float ff = level;
		float initAttr = getInitAttrByAttrNum(attrNum, colorType, level);
		int tl = getLbyLevel(level);
		if(tl >= MagicWeaponConstant.newLevelWeight_new.length) {
			tl = MagicWeaponConstant.newLevelWeight_new.length - 1;
		}
		float numerator = initAttr * (1f + MagicWeaponConstant.newColorWeight[colorType] / 100 + MagicWeaponConstant.newLevelWeight_new[tl] / 100f + ff / 32f);
		result = (int) numerator;
		return result;
	}
	
	public static void main(String[] args) {
		System.out.println("1");
	}
	/**
	 * 根据法宝当前数值计算初值
	 * @param baseNum
	 * @param colorType
	 * @param level
	 * @return
	 */
	public float getInitAttrByAttrNum(float attrNum, int colorType, int level) {
		float result = 0;
		float ff = level;
		int tl = getLbyLevel(level);
		if(tl >= MagicWeaponConstant.newLevelWeight.length) {
			tl = MagicWeaponConstant.newLevelWeight.length - 1;
		}
		float numerator = attrNum / (1f + MagicWeaponConstant.newColorWeight[colorType] / 100 + MagicWeaponConstant.newLevelWeight[tl] / 100f + ff / 32f);
		result = numerator;
		return result;
	}
	
//	public static int getInitAttrDevelNum1(float baseNum, int colorType, int level) {
//		int result = 0;
//		float ff = level;
//		int tl = getLbyLevel(level);
//		if(tl >= MagicWeaponConstant.newLevelWeight.length) {
//			tl = MagicWeaponConstant.newLevelWeight.length - 1;
//		}
//		float numerator = baseNum * (1f + MagicWeaponConstant.newColorWeight[colorType] / 100 + MagicWeaponConstant.newLevelWeight[tl] / 100f + ff / 32f);
//		result = (int) numerator;
//		return result;
//	}
//	public static float getInitAttrDevelBNum1(int baseNum, int colorType, int level) {
//		float result = 0;
//		float ff = level;
//		int tl = getLbyLevel(level);
//		if(tl >= MagicWeaponConstant.newLevelWeight.length) {
//			tl = MagicWeaponConstant.newLevelWeight.length - 1;
//		}
//		float numerator = baseNum / (1f + MagicWeaponConstant.newColorWeight[colorType] / 100 + MagicWeaponConstant.newLevelWeight[tl] / 100f + ff / 32f);
//		result = numerator;
//		return result;
//	}
//	
//	public static int getInitalNum1(int tempNum) throws Exception {
//		int result = 0;
//		float numerator = tempNum * 80f / 100f;		//分子
//		float denominator = (100 + MagicWeaponConstant.aidWeight) / 100;
//		result = (int) (numerator / denominator * 10f / 100);
//		result -= result * new Random().nextInt(500) / MagicWeaponConstant.tenThousand;		//数值向下浮动百分之5
//		return result;
//	}
//	
//	public static void main(String[] args) throws Exception {
//		int test = getInitalNum1(144000);
//		System.out.println("[初值][" + test + "]");
//		for(int i=1; i<=80; i++) {
//			int test2 = getInitAttrDevelNum1(test, 0, i);
//			float test3 = getInitAttrDevelBNum1(test2, 0, i);
//			int test4 = getInitAttrDevelNum1(test3, 0, i);
//			System.out.println("[白]["+i+"]" + test2 + "["+test4+"]");
//		}
//	}
	
	/**
	 * 获取法宝成长属性
	 * @param colorType
	 * @return
	 */
	public int getInitAttrDevelNum(int baseNum, int colorType) {
		int result = 0;
		float numerator = baseNum * 100 / MagicWeaponConstant.initalWeight;		//分子
		float denominator = numerator * MagicWeaponConstant.growWeight / 100;
		result = (int) (denominator / (getMaxP(colorType) - 1));
		return result;
	}
	
	public void initAttrTypeMapping() {
		MagicWeaponConstant.mappingKeyValue.put(MagicWeaponConstant.hpNum, "气血");
		MagicWeaponConstant.mappingKeyValue.put(MagicWeaponConstant.physicAttNum, "外攻");
		MagicWeaponConstant.mappingKeyValue.put(MagicWeaponConstant.magicAttNum, "内攻");
		MagicWeaponConstant.mappingKeyValue.put(MagicWeaponConstant.hitNum, "命中");
		MagicWeaponConstant.mappingKeyValue.put(MagicWeaponConstant.dodgeNum, "闪避");
		MagicWeaponConstant.mappingKeyValue.put(MagicWeaponConstant.physicDefanceNum, "外防");
		MagicWeaponConstant.mappingKeyValue.put(MagicWeaponConstant.magicDefanceNum, "内防");
		MagicWeaponConstant.mappingKeyValue.put(MagicWeaponConstant.cirtNum, "暴击");
		MagicWeaponConstant.mappingKeyValue.put(MagicWeaponConstant.mpNum, "仙法");
		MagicWeaponConstant.mappingKeyValue.put(MagicWeaponConstant.打怪经验, "打怪经验加成");
		MagicWeaponConstant.mappingKeyValue.put(MagicWeaponConstant.掉宝率, "掉落几率加成");
		MagicWeaponConstant.mappingKeyValue.put(MagicWeaponConstant.法宝掉落, "掉落法宝几率加成");
		MagicWeaponConstant.mappingKeyValue.put(MagicWeaponConstant.法宝吞噬经验, "法宝吞噬经验加成");
		MagicWeaponConstant.mappingKeyValue.put(MagicWeaponConstant.宠物打怪经验, "宠物打怪经验加成");
		MagicWeaponConstant.mappingKeyValue.put(MagicWeaponConstant.精准, "精准");
		MagicWeaponConstant.mappingKeyValue.put(MagicWeaponConstant.免爆, "免暴");
		MagicWeaponConstant.mappingKeyValue.put(MagicWeaponConstant.火攻, "火攻");
		MagicWeaponConstant.mappingKeyValue.put(MagicWeaponConstant.冰攻, "冰攻");
		MagicWeaponConstant.mappingKeyValue.put(MagicWeaponConstant.雷攻, "雷攻");
		MagicWeaponConstant.mappingKeyValue.put(MagicWeaponConstant.风攻, "风攻");
		MagicWeaponConstant.mappingKeyValue.put(MagicWeaponConstant.破甲, "破甲");
		MagicWeaponConstant.mappingKeyValue.put(MagicWeaponConstant.火炕, "火抗");
		MagicWeaponConstant.mappingKeyValue.put(MagicWeaponConstant.冰抗, "冰抗");
		MagicWeaponConstant.mappingKeyValue.put(MagicWeaponConstant.雷抗, "雷抗");
		MagicWeaponConstant.mappingKeyValue.put(MagicWeaponConstant.风抗, "风抗");
		MagicWeaponConstant.mappingKeyValue.put(MagicWeaponConstant.火减抗, "减火抗");
		MagicWeaponConstant.mappingKeyValue.put(MagicWeaponConstant.冰减抗, "减冰抗");
		MagicWeaponConstant.mappingKeyValue.put(MagicWeaponConstant.雷减抗, "减雷抗");
		MagicWeaponConstant.mappingKeyValue.put(MagicWeaponConstant.风减抗, "减风抗");
		MagicWeaponConstant.mappingKeyValue.put(MagicWeaponConstant.祈福冰凤, "祈福抽取冰凤几率");
		MagicWeaponConstant.mappingKeyValue.put(MagicWeaponConstant.紫色果实, "增加庄园种植紫色果实产出几率");
		MagicWeaponConstant.mappingKeyValue.put(MagicWeaponConstant.橙色果实, "增加庄园种植橙色果实产出几率");
		MagicWeaponConstant.mappingKeyValue.put(MagicWeaponConstant.买药打折, "购买红药和蓝药费用减少");
		MagicWeaponConstant.mappingKeyValue.put(MagicWeaponConstant.回城复活血量增加, "复活时血量多");
		MagicWeaponConstant.mappingKeyValue.put(MagicWeaponConstant.修理装备打折, "装备修理费用减少");
		MagicWeaponConstant.mappingKeyValue.put(MagicWeaponConstant.兽魂属性, "兽魂属性增加");
		MagicWeaponConstant.mappingKeyValue.put(MagicWeaponConstant.法宝基础属性, "基础属性增加");
		
		
		
		MagicWeaponConstant.mappingValueKey.put("血量", MagicWeaponConstant.hpNum);
		MagicWeaponConstant.mappingValueKey.put("物攻", MagicWeaponConstant.physicAttNum);
		MagicWeaponConstant.mappingValueKey.put("法攻", MagicWeaponConstant.magicAttNum);
		MagicWeaponConstant.mappingValueKey.put("命中", MagicWeaponConstant.hitNum);
		MagicWeaponConstant.mappingValueKey.put("闪避", MagicWeaponConstant.dodgeNum);
		MagicWeaponConstant.mappingValueKey.put("物防", MagicWeaponConstant.physicDefanceNum);
		MagicWeaponConstant.mappingValueKey.put("法防", MagicWeaponConstant.magicDefanceNum);
		MagicWeaponConstant.mappingValueKey.put("暴击", MagicWeaponConstant.cirtNum);
		MagicWeaponConstant.mappingValueKey.put("仙法", MagicWeaponConstant.mpNum);
		MagicWeaponConstant.mappingValueKey.put("打怪经验加成", MagicWeaponConstant.打怪经验);
		MagicWeaponConstant.mappingValueKey.put("掉落几率加成", MagicWeaponConstant.掉宝率);
		MagicWeaponConstant.mappingValueKey.put("掉落法宝几率加成", MagicWeaponConstant.法宝掉落);
		MagicWeaponConstant.mappingValueKey.put("法宝吞噬经验加成", MagicWeaponConstant.法宝吞噬经验);
		MagicWeaponConstant.mappingValueKey.put("宠物打怪经验加成", MagicWeaponConstant.宠物打怪经验);
		MagicWeaponConstant.mappingValueKey.put("精准", MagicWeaponConstant.精准);
		MagicWeaponConstant.mappingValueKey.put("免暴", MagicWeaponConstant.免爆);
		MagicWeaponConstant.mappingValueKey.put("金攻", MagicWeaponConstant.火攻);
		MagicWeaponConstant.mappingValueKey.put("水攻", MagicWeaponConstant.冰攻);
		MagicWeaponConstant.mappingValueKey.put("火攻", MagicWeaponConstant.雷攻);
		MagicWeaponConstant.mappingValueKey.put("木攻", MagicWeaponConstant.风攻);
		MagicWeaponConstant.mappingValueKey.put("破甲", MagicWeaponConstant.破甲);
		MagicWeaponConstant.mappingValueKey.put("金抗", MagicWeaponConstant.火炕);
		MagicWeaponConstant.mappingValueKey.put("水抗", MagicWeaponConstant.冰抗);
		MagicWeaponConstant.mappingValueKey.put("火抗", MagicWeaponConstant.雷抗);
		MagicWeaponConstant.mappingValueKey.put("木抗", MagicWeaponConstant.风抗);
		MagicWeaponConstant.mappingValueKey.put("减金抗", MagicWeaponConstant.火减抗);
		MagicWeaponConstant.mappingValueKey.put("减水抗", MagicWeaponConstant.冰减抗);
		MagicWeaponConstant.mappingValueKey.put("减火抗", MagicWeaponConstant.雷减抗);
		MagicWeaponConstant.mappingValueKey.put("减木抗", MagicWeaponConstant.风减抗);
		MagicWeaponConstant.mappingValueKey.put("祈福抽取冰凤几率", MagicWeaponConstant.祈福冰凤);
		MagicWeaponConstant.mappingValueKey.put("增加庄园种植紫色果实产出几率", MagicWeaponConstant.紫色果实);
		MagicWeaponConstant.mappingValueKey.put("增加庄园种植橙色果实产出几率", MagicWeaponConstant.橙色果实);
		MagicWeaponConstant.mappingValueKey.put("购买红药和蓝药费用减少", MagicWeaponConstant.买药打折);
		MagicWeaponConstant.mappingValueKey.put("复活时血量多", MagicWeaponConstant.回城复活血量增加);
		MagicWeaponConstant.mappingValueKey.put("装备修理费用减少", MagicWeaponConstant.修理装备打折);
		MagicWeaponConstant.mappingValueKey.put("兽魂属性增加", MagicWeaponConstant.兽魂属性);
		MagicWeaponConstant.mappingValueKey.put("基础属性增加", MagicWeaponConstant.法宝基础属性);
		
	}
	
	private void loadBasicDataFile() throws Exception {
		File f = new File(dataFileName);
		if(!f.exists()){
			throw new Exception(dataFileName + " 配表不存在! " + f.getAbsolutePath());
		}
		InputStream is = new FileInputStream(f);
		POIFSFileSystem pss = new POIFSFileSystem(is);
		HSSFWorkbook workbook = new HSSFWorkbook(pss);
		
		basicDataMap = new ConcurrentHashMap<Integer, BasicDataModel>();
		hiddenDataMap = new ConcurrentHashMap<Integer, HiddenAttrModel>();
		hiddenProbabblyModel = new ArrayList<HiddenRanProbModel>(); 
		
		HSSFSheet sheet = workbook.getSheetAt(0);				//基础属性数值
		int rows = sheet.getPhysicalNumberOfRows();
		for(int i=1; i<rows; i++){
			HSSFRow row = sheet.getRow(i);
			if(row == null){
				continue;
			}
			try{
				BasicDataModel temp = getBasicDataModel(row);
				basicDataMap.put(temp.getLevel(), temp);
			}catch(Exception e) {
				throw new Exception(fileName + "第" + (i+1) + "行异常！！", e);
			}
		}
		
		sheet = workbook.getSheetAt(1);				//隐藏属性数值
		rows = sheet.getPhysicalNumberOfRows();
		for(int i=1; i<rows; i++){
			HSSFRow row = sheet.getRow(i);
			if(row == null){
				continue;
			}
			try{
				HiddenAttrModel temp = getHiddenAttrModel(row);
				hiddenDataMap.put(temp.getLevel(), temp);
			}catch(Exception e) {
				throw new Exception(fileName + "第" + (i+1) + "行异常！！", e);
			}
		}
		
		sheet = workbook.getSheetAt(2);				//法宝附加、隐藏属性配置
		rows = sheet.getPhysicalNumberOfRows();
		for(int i=1; i<rows; i++){
			HSSFRow row = sheet.getRow(i);
			if(row == null){
				continue;
			}
			try{
				HiddenRanProbModel temp = getHiddenRanProbModel(row);
				hiddenProbabblyModel.add(temp);
			}catch(Exception e) {
				throw new Exception(fileName + "第" + (i+1) + "行异常！！", e);
			}
		}
		
	}
	
	private HiddenRanProbModel getHiddenRanProbModel(HSSFRow row) {
		HiddenRanProbModel hrm = new HiddenRanProbModel();
		int rowNum = 0;
		hrm.setAttrStr(PetGrade.getString(row, rowNum++, logger));
		hrm.setProbabbly(getInt(row, rowNum++));
		return hrm;
	}
	private HiddenAttrModel getHiddenAttrModel(HSSFRow row) {
		HiddenAttrModel ham = new HiddenAttrModel();
		int rowNum = 0;
		ham.setLevel(getInt(row, rowNum++));
		ham.setMp(getInt(row, rowNum++));
		ham.setActp(getInt(row, rowNum++));
		ham.setCirt(getInt(row, rowNum++));
		ham.setDchp(getInt(row, rowNum++));
		ham.setFireAttc(getInt(row, rowNum++));
		ham.setIceAttc(getInt(row, rowNum++));
		ham.setWindAttc(getInt(row, rowNum++));
		ham.setThundAttc(getInt(row, rowNum++));
		ham.setDac(getInt(row, rowNum++));
		ham.setHitP(getInt(row, rowNum++));
		ham.setDodgeP(getInt(row, rowNum++));
		ham.setFrt(getInt(row, rowNum++));
		ham.setIrt(getInt(row, rowNum++));
		ham.setWrt(getInt(row, rowNum++));
		ham.setTrt(getInt(row, rowNum++));
		ham.setDfrt(getInt(row, rowNum++));
		ham.setDirt(getInt(row, rowNum++));
		ham.setDwrt(getInt(row, rowNum++));
		ham.setDtrt(getInt(row, rowNum++));
		ham.setHp(getInt(row, rowNum++));
		ham.setAttact(getInt(row, rowNum++));
		return ham;
	}
	
	private BasicDataModel getBasicDataModel(HSSFRow row) {
		BasicDataModel bdm = new BasicDataModel();
		int rowNum = 0;
		bdm.setLevel(getInt(row, rowNum++));
		bdm.setHpNum(getInt(row, rowNum++));
		bdm.setAttackNum(getInt(row, rowNum++));
		bdm.setDefanceNum(getInt(row, rowNum++));
		bdm.setHitP(getInt(row, rowNum++));
		bdm.setDodgeP(getInt(row, rowNum++));
		bdm.setCirtP(getInt(row, rowNum++));
		return bdm;
	}
	
	public static String getString(HSSFRow row, int i, Logger logger){
		HSSFCell cell = row.getCell(i);
		if(cell == null){
			logger.error("单元格是null，页签{} 行{} 列{} ",
					new Object[]{row.getSheet().getSheetName(), row.getRowNum(), i});
			return "";
		}
		int type = cell.getCellType();
		if(type == Cell.CELL_TYPE_STRING){
			return cell.getStringCellValue();
		}else if(type == Cell.CELL_TYPE_FORMULA){
			int typeF = cell.getCachedFormulaResultType();
			if(typeF == Cell.CELL_TYPE_STRING){
				return cell.getStringCellValue();
			}
		}
		String ret = String.valueOf(cell.getNumericCellValue());
		if(ret.equals("0.0")){
			ret = "";
		}
		return ret;
	}
	
	private void loadFile() throws Exception {
		File f = new File(fileName);
		if(!f.exists()){
			throw new Exception(fileName + " 配表不存在! " + f.getAbsolutePath());
		}
		InputStream is = new FileInputStream(f);
		POIFSFileSystem pss = new POIFSFileSystem(is);
		HSSFWorkbook workbook = new HSSFWorkbook(pss);
		
		mwBaseModel = new ConcurrentHashMap<Integer, MagicWeaponBaseModel>();
		basicAttrMap = new ConcurrentHashMap<String, BasicAttrRanModel>();
		mwAxtraAttr = new ConcurrentHashMap<Integer, AdditiveAttrModel>();
		devourModelMap = new ConcurrentHashMap<Integer, DevourModel>();
		
		HSSFSheet sheet = workbook.getSheetAt(5);			//初始化mapping
		int rows = sheet.getPhysicalNumberOfRows();
		for(int i=1; i<rows; i++) {
			HSSFRow row = sheet.getRow(i);
			if(row == null){
				continue;
			}
			try{
				int tt = getInt(row, 0);
				String ss = getString(row, 1, logger);
				if(ss != null && !ss.isEmpty()) {
					MagicWeaponConstant.mappingKeyValue2.put(tt, ss);
				} else {
					logger.warn("[初始化法宝mnager] [初始化mapping错误，描述有空," + ss + "] [" + tt + "]");
				}
			}catch(Exception e) {
				throw new Exception(fileName + "第" + (i+1) + "行异常！！", e);
			}
		}
		
		
		sheet = workbook.getSheetAt(0);				//法宝品质相关基础配置
		rows = sheet.getPhysicalNumberOfRows();
		for(int i=1; i<rows; i++){
			HSSFRow row = sheet.getRow(i);
			if(row == null){
				continue;
			}
			try{
				MagicWeaponBaseModel temp = getMagicWeaponBaseModel(row);
				mwBaseModel.put(temp.getColorType(), temp);
			}catch(Exception e) {
				throw new Exception(fileName + "第" + (i+1) + "行异常！！", e);
			}
		}
		
		sheet = workbook.getSheetAt(1);				//基础属性名称相关配置
		rows = sheet.getPhysicalNumberOfRows();
		for(int i=1; i<rows; i++){
			HSSFRow row = sheet.getRow(i);
			if(row == null){
				continue;
			}
			try{
				BasicAttrRanModel temp = getBasicAttrModel(row);
				basicAttrMap.put(temp.getPre_name(), temp);
				if(!MagicWeaponConstant.pre_nameList.contains(temp.getPre_name())) {
					MagicWeaponConstant.pre_nameList.add(temp.getPre_name());
				}
			}catch(Exception e) {
				throw new Exception(fileName + "第" + (i+1) + "行异常！！", e);
			}
		}
		
		sheet = workbook.getSheetAt(2);				//法宝附加、隐藏属性配置
		rows = sheet.getPhysicalNumberOfRows();
		for(int i=1; i<rows; i++){
			HSSFRow row = sheet.getRow(i);
			if(row == null){
				continue;
			}
			try{
				AdditiveAttrModel temp = getMagicWeaponSkillModel(row);
				mwAxtraAttr.put(temp.getId(), temp);
			}catch(Exception e) {
				throw new Exception(fileName + "第" + (i+1) + "行异常！！", e);
			}
		}
		
		sheet = workbook.getSheetAt(3);				//法宝升级、吞噬相关配置
		rows = sheet.getPhysicalNumberOfRows();
		for(int i=1; i<rows; i++){
			HSSFRow row = sheet.getRow(i);
			if(row == null){
				continue;
			}
			try{
				DevourModel temp = getDevourModel(row);
				devourModelMap.put(temp.getLevel(), temp);
			}catch(Exception e) {
				throw new Exception(fileName + "第" + (i+1) + "行异常！！", e);
			}
		}
		
		initTranslate(workbook);
		
		sheet = workbook.getSheetAt(6);				//法宝吞噬突破
		rows = sheet.getPhysicalNumberOfRows();
		for (int i=1; i<rows; i++) {
			HSSFRow row = sheet.getRow(i);
			if(row == null){
				continue;
			}
			try {
				int index = 0;
				MagicWeaponColorUpModule module = new MagicWeaponColorUpModule();
				module.setColorType(ReadFileTool.getInt(row, index++, logger));
				int maxTimes = ReadFileTool.getInt(row, index++, logger);
				int[] mwLvs = new int[maxTimes];
				String[] costAeNames = new String[maxTimes];
				int[] costNums = new int[maxTimes];
				int[] probs = new int[maxTimes];
				for (int j=0; j<maxTimes; j++) {
					String str = ReadFileTool.getString(row, index++, logger);
					String[] tempStr = str.split(",");
					mwLvs[j] = Integer.parseInt(tempStr[0]);
					costAeNames[j] = tempStr[1];
					costNums[j] = Integer.parseInt(tempStr[2]);
					probs[j] = Integer.parseInt(tempStr[3]);
				}
				module.setArticleCNNNames(costAeNames);
				module.setCostNums(costNums);
				module.setMagicWeaponLvs(mwLvs);
				module.setProbabblys(probs);
				colorUpMaps.put(module.getColorType(), module);
			} catch (Exception e) {
				throw new Exception(fileName + "第" + (i+1) + "行异常！！] [" + sheet.getSheetName() + "]", e);
			}
		}
	}
	
	private DevourModel getDevourModel(HSSFRow row) {
		DevourModel dm = new DevourModel();
		int rowNum = 0;
		dm.setLevel(this.getInt(row, rowNum++));
		for(int i=0; i<=MagicWeaponConstant.maxColor; i++) {
			Color4DevourModel cm = new Color4DevourModel();
			cm.setColor(i);
			cm.setExp4Levelup(this.getInt(row, rowNum++));
			cm.setCostSiliver(this.getInt(row, rowNum++));
			dm.addColorList(cm);
		}
		return dm;
	}
	
	private void initTranslate(HSSFWorkbook workbook) throws Exception{
		HSSFSheet sheet = workbook.getSheetAt(MagicWeaponConstant.翻译描述_所在sheet);
		int rows = sheet.getPhysicalNumberOfRows();
		String key = "";
		String value = "";
		for(int i=0;i<rows;i++){
			HSSFRow row = sheet.getRow(i);
			if(row!=null){
				try{
					key = row.getCell(0).getStringCellValue();
					value = row.getCell(1).getStringCellValue();
					translates.put(key, value);
				}catch(Exception e){
					throw new Exception("[法宝翻译加载] [异常] [行："+i+"]",e);
				}
			}
		}
	}
	
	private String getTranslate(String key,String tvalue){
		String value = translates.get(key);
		if(value!=null)
			return String.format(value, tvalue);
		return null;
	}
	
	
	private AdditiveAttrModel getMagicWeaponSkillModel(HSSFRow row) {
		AdditiveAttrModel mkm = new AdditiveAttrModel();
		int rowNum = 0;
		mkm.setId(this.getInt(row, rowNum++));
		mkm.setAttrType(PetGrade.getString(row, rowNum++, logger));
		mkm.setAttrNum(this.getInt(row, rowNum++));
		mkm.setNeedLevel(this.getInt(row, rowNum++));
		mkm.setDescreption(PetGrade.getString(row, rowNum++, logger));
		return mkm;
	}
	
	private BasicAttrRanModel getBasicAttrModel(HSSFRow row) throws Exception {
		BasicAttrRanModel ba = new BasicAttrRanModel();
		int rowNum = 0;
		String name = PetGrade.getString(row, rowNum++, logger);
		ba.setPre_name(name);
		String name_stat = PetGrade.getString(row, rowNum++, logger);
		nameStats.put(name, name_stat);
		String skillNames = PetGrade.getString(row, rowNum++, logger);
		ba.setProbabbly(this.getInt(row, rowNum++));
		String[] temp = skillNames.split(",");
		for(int i=0; i<temp.length; i++) {
			ba.add2AttrList(temp[i]);
		}
		return ba;
		
	}
	
	private MagicWeaponBaseModel getMagicWeaponBaseModel(HSSFRow row) throws Exception {
		MagicWeaponBaseModel bm = new MagicWeaponBaseModel();
		int rowNum = 0;
		
		bm.setColorType(getInt(row,rowNum++));
		bm.setMaxLevel(getInt(row,rowNum++));
		bm.setName(PetGrade.getString(row, rowNum++, logger));
		bm.setMinAdditiveAttr(getInt(row,rowNum++));
		bm.setMaxAdditiveAttr(getInt(row,rowNum++));
		String additiveIdList = PetGrade.getString(row, rowNum++, logger);
		String additiveIP = PetGrade.getString(row, rowNum++, logger);
		bm.setHiddenAttrNum(getInt(row,rowNum++));
		bm.setAppraisalNum(getInt(row,rowNum++));
		bm.setPropForAppraisal(PetGrade.getString(row, rowNum++, logger));
		bm.setPropColorType(getInt(row,rowNum++));
		bm.setUsePropNum(getInt(row,rowNum++));
		bm.setCostSiliverNum(getInt(row,rowNum++));
		
		String[] temp1 = additiveIdList.split(",");
		String[] temp2 = additiveIP.split(",");
		if(temp1.length != temp2.length) {
			throw new Exception("配表错");
		}
		for(int i=0; i<temp1.length; i++) {
			bm.add2AdditiveSkillList(Integer.parseInt(temp1[i]), Integer.parseInt(temp2[i]));
		}
		
		return bm;
	}
	
	int getInt(HSSFRow row, int cellIdx) {
		HSSFCell cell = row.getCell(cellIdx);
		if (cell == null) {
			logger.error("单元格是null " + cellIdx + " 行 " + row.getRowNum() + " at " + row.getSheet().getSheetName());
			return 0;
		}
		int type = cell.getCellType();
		if (type == HSSFCell.CELL_TYPE_NUMERIC) {
			return (int) cell.getNumericCellValue();
		}
		if(cell.getStringCellValue().trim().length()>0){
			return Integer.parseInt(cell.getStringCellValue());
		}
		return 0;
	}
	
	byte getByte(HSSFRow row, int cellIdx) {
		HSSFCell cell = row.getCell(cellIdx);
		if (cell == null) {
			logger.error("单元格是null " + cellIdx + " 行 " + row.getRowNum() + " at " + row.getSheet().getSheetName());
			return 0;
		}
		int type = cell.getCellType();
		if (type == HSSFCell.CELL_TYPE_NUMERIC) {
			return (byte) cell.getNumericCellValue();
		}
		if(cell.getStringCellValue().trim().length()>0){
			return Byte.parseByte(cell.getStringCellValue());
		}
		return 0;
	}
	/**
	 *  获得品阶
	 * @param level
	 * @return
	 */
	public static String getJieJiMess(int level) {
		String mess = "";
		int p = level % 10 - 1;
		if(p < 0) {
			p = 9;
		}
		int j = (level - 1) / 10;
		mess = MagicWeaponConstant.品[j] + MagicWeaponConstant.阶[p];
		return mess;
	}
	
	/**
	 * 获得法宝阶级描述
	 * level 来获取不同级别的描述
	 * @param id
	 * @return
	 */
	public String getJieJiMess(long id,int level,int colorlevel){
		String mess = "";
		if(id>0){
			ArticleEntity ae = DefaultArticleEntityManager.getInstance().getEntity(id);
			if(ae!=null && ae instanceof NewMagicWeaponEntity){
				NewMagicWeaponEntity me = (NewMagicWeaponEntity)ae;
				if(level<0){
					level = me.getMcolorlevel();
				}
				if(level >= getMaxP(colorlevel)){
					level = getMaxP(colorlevel);
				}
				int p = level % 10 - 1;
				if(p < 0) {
					p = 9;
				}
				int j = (level - 1) / 10;
				
				mess = MagicWeaponConstant.品[j] + MagicWeaponConstant.阶[p];
			}
		}
		return mess;
	}
	/**
	 * 是否需要特殊道具才能继续升阶
	 * @param colorLevel
	 * @return
	 */
	public String needSpecialProp(int colorLevel, int mwLv) {
		if (colorLevel >= 3) {			//紫色以上法宝才可以突破升阶上限
			MagicWeaponColorUpModule module = colorUpMaps.get(colorLevel);
			if (module != null) {
				for (int i=0; i<module.getMagicWeaponLvs().length; i++) {
					if (mwLv == module.getMagicWeaponLvs()[i]) {
						return module.getArticleCNNNames()[i];
					}
				}
			}
		}
		return null;
	}
	
	/**
	 * 获得可以达到的最大品
	 * @return
	 */
	public int getMaxP(int colorlevel){
		switch (colorlevel) {
		case 0:
			return 30;
		case 1:
			return 40;
		case 2:
			return 50;
		case 3:
			if (法宝阶飞升是否开启) {
				return 120;
			} else {
				return 60;
			}
		case 4:
			if (法宝阶飞升是否开启) {
				return 130;
			} else {
				return 70;
			}
		case 5:
			if (法宝阶飞升是否开启) {
				return 140;
			} else {
				return 80;
			}
		default:
			break;
		}
		return 30;
	}
	/**
	 * 获得额外的法宝信息
	 * @param player
	 * @param req
	 * @return
	 */
	public SYNC_MAGICWEAPON_FOR_KNAPSACK_RES checkMagicWeaponMess(Player player , SYNC_MAGICWEAPON_FOR_KNAPSACK_REQ req){
		if(req == null || player == null){
			logger.warn("["+this.getClass().getSimpleName()+"] [checkMagicWeaponMess] [error] [req:"+(req==null?"reqisnull":req.getTypeDescription())+"] [player:"+(player==null?"playerisnull":player.getLogString())+"] [req or player error]");
			return null;
		}
		long ids[] = req.getIds();
		Skill.logger.warn("[checksync] ["+(ids==null?"null":ids.length)+"] ["+player.getLogString()+"]");
		if(ids!=null && ids.length>0){
			List<MoreMagicWeaponMess> list = new ArrayList<MoreMagicWeaponMess>();
			for(int i=0,len=ids.length;i<len;i++){
				long id = ids[i];
				if(id>0){
					ArticleEntity ae = DefaultArticleEntityManager.getInstance().getEntity(id);
					if(ae!=null){
						if(ae instanceof NewMagicWeaponEntity){
							MoreMagicWeaponMess mess = new MoreMagicWeaponMess();
							NewMagicWeaponEntity me = (NewMagicWeaponEntity)ae;
							mess.setId(id);
							if(me.getBasicpropertyname()==null || me.getBasicpropertyname().isEmpty()){
								mess.setShenShi(false);	
							}else{
								mess.setShenShi(true);
							}
							if(me.getBasicpropertyname()!=null){
								mess.setBasicpropertyname(me.getBasicpropertyname());
							}else{
								mess.setBasicpropertyname("");
							}
							mess.setMstar(me.getMstar());
							mess.setMess3("");
							mess.setMess4("");
							list.add(mess);
						}else{
							logger.warn("["+this.getClass().getSimpleName()+"] [checkMagicWeaponMess] [errorType] [magicweaponid:"+id+"] ["+player.getLogString()+"]");
						}
					}
				}
			}
			logger.warn("["+this.getClass().getSimpleName()+"] [checkMagicWeaponMess] [ok] [ids:"+Arrays.toString(ids)+"] ["+player.getLogString()+"]");
			SYNC_MAGICWEAPON_FOR_KNAPSACK_RES res = new SYNC_MAGICWEAPON_FOR_KNAPSACK_RES(req.getSequnceNum(), list.toArray(new MoreMagicWeaponMess[]{}));
			return res;
		}
		return null;
	}
	
	/**
	 * 请求神识
	 * @param player
	 * @param req
	 * @return
	 */
	public QUERY_SHENSHI_RES queryShenShi(Player player , QUERY_SHENSHI_REQ req){
		if(req == null || player == null){
			logger.warn("["+this.getClass().getSimpleName()+"] [queryShenShi] [error] [req:"+(req==null?"reqisnull":req.getTypeDescription())+"] [player:"+(player==null?"playerisnull":player.getLogString())+"] [req or player error]");
			return null;
		}
		
		long id = req.getId();
		
		ArticleEntity ae = DefaultArticleEntityManager.getInstance().getEntity(id);
		if(ae!=null){
			if(ae instanceof NewMagicWeaponEntity){
				NewMagicWeaponEntity me = (NewMagicWeaponEntity)ae;
				String jieji = getJieJiMess(id,me.getMcolorlevel(),me.getColorType());
				String basicmess = Translate.神识说明;
				
				String costname = "";
				int costsilver = 0;
				int costnum = 0;
				
				MagicWeaponBaseModel mbm = mwBaseModel.get(me.getColorType());
				if(mbm!=null){
					costname = mbm.getPropForAppraisal();
					costnum = mbm.getUsePropNum();
					costsilver = mbm.getCostSiliverNum();
				}else{
					logger.warn("["+this.getClass().getSimpleName()+"] [queryShenShi] [error] [mbm==null] [magicweaponid:"+id+"] ["+player.getLogString()+"]");
					return null;
				}
				
				if(costsilver==0 || costnum==0 || costname==null || costname.isEmpty()){
					logger.warn("["+this.getClass().getSimpleName()+"] [queryShenShi] [error] [costsilver==0] [costnum==0] [costname==null || costname.isEmpty] [magicweaponid:"+id+"] ["+player.getLogString()+"]");
					return null;
				}
				
				String costmess = Translate.translateString(Translate.神识消耗说明, new String[][] { { Translate.STRING_1, (int)(costsilver/1000)+"" } });
				
				logger.warn("["+this.getClass().getSimpleName()+"] [queryShenShi] [ok] ["+costname+"*"+costnum+"] [costsilver:"+costsilver+"] [magicweaponid:"+id+"] [name:"+me.getArticleName()+"] [jieji:"+jieji+"] ["+player.getLogString()+"]");
				QUERY_SHENSHI_RES res = new QUERY_SHENSHI_RES(req.getSequnceNum(), id, jieji, costname,costnum,basicmess, costmess);
				return res;
			}else{
				player.sendError(Translate.请放入法宝);
				logger.warn("["+this.getClass().getSimpleName()+"] [queryShenShi] [error] [errorType] [magicweaponid:"+id+"] ["+player.getLogString()+"]");
			}
		}else{
			player.sendError(Translate.背包中没有该法宝);
			logger.warn("["+this.getClass().getSimpleName()+"] [queryShenShi] [error] [ae==null] [magicweaponid:"+id+"] ["+player.getLogString()+"]");
		}
		return null;
	}
	
	/**
	 * 确认神识
	 * @param player
	 * @param req
	 */
	public void confirmShenShi(Player player , CONFIRM_SHENSHI_REQ req){
		if(req == null || player == null){
			logger.warn("["+this.getClass().getSimpleName()+"] [confirmShenShi] [error] [req:"+(req==null?"reqisnull":req.getTypeDescription())+"] [player:"+(player==null?"playerisnull":player.getLogString())+"] [req or player error]");
			player.addMessageToRightBag(new CONFIRM_SHENSHI_RES(req.getSequnceNum(), false));
			return;
		}
		
		long magicweaponid = req.getMagicweaponid();
		long materialids [] = req.getIds();
		int nums [] = req.getNums();
		int usesilver = req.getUsesilver();
		long start = com.fy.engineserver.gametime.SystemTime.currentTimeMillis();
		
		ArticleEntity ae = DefaultArticleEntityManager.getInstance().getEntity(magicweaponid);
		if(ae!=null){
			
			if (!ArticleProtectManager.instance.isCanDo(player, ArticleProtectDataValues.ArticleProtect_High, ae.getId())) {
				player.sendError(Translate.高级锁魂物品不能做此操作);
				return;
			}
			
			if(ae instanceof NewMagicWeaponEntity == false){
				player.sendError(Translate.请放入法宝);
				player.addMessageToRightBag(new CONFIRM_SHENSHI_RES(req.getSequnceNum(), false));
				return;
			}else{
				NewMagicWeaponEntity me = (NewMagicWeaponEntity)ae;
				
				if(mwBaseModel==null){
					logger.warn("[法宝神识确认] [失败] [mwBaseModel==null]");
					return;
				}
				
				if(mwBaseModel.get(ae.getColorType())==null){
					logger.warn("[法宝神识确认] [失败] [mwBaseModel.get(ae.getColorType())==null]");
					return;
				}
				
				if(usesilver==1){
					if(mwBaseModel.get(ae.getColorType()).getCostSiliverNum()<=0){
						logger.warn("[法宝神识确认] [失败] [costmoney==0]");
						return;
					}
					
					if(player.getSilver() + player.getShopSilver()< mwBaseModel.get(ae.getColorType()).getCostSiliverNum()){
						
						//银子不足，消耗材料
						String resultmess = "o";
						if(materialids!=null && materialids.length>0){
							boolean isshenshifu = true;
							int materialnums = 0;
							ArticleEntity materialEntity = null;
							List<Long> list = new ArrayList<Long>();
							int needCostNums = mwBaseModel.get(ae.getColorType()).getUsePropNum(); 
							String costname = mwBaseModel.get(ae.getColorType()).getPropForAppraisal();
							if(needCostNums <= 0 || costname==null || costname.isEmpty()){
								logger.warn("[法宝神识确认] [失败] [needCostNums <= 0] [costname==null]");
								player.addMessageToRightBag(new CONFIRM_SHENSHI_RES(req.getSequnceNum(), false));
								return;
							}
							
							for(int i=0,len=materialids.length;i<len;i++){
								materialnums += nums[i];
								ArticleEntity material = DefaultArticleEntityManager.getInstance().getEntity(materialids[i]);
								if(material!=null){
									if(material.getArticleName().equals(costname)==false){
										isshenshifu = false;
										break;
									}else{
										materialEntity = material;
									}
								}
								for(int j=0;j<nums[i];j++){
									if(list.size()<needCostNums){
										list.add(materialids[i]);
									}else{
										break;
									}
								}
							}
							
							if(!isshenshifu || materialEntity==null){
								resultmess = Translate.请放入法宝鉴定符;
//								player.sendError(Translate.请放入法宝鉴定符);
								logger.warn("[法宝神识确认] [失败] [clientsenderror] [isshenshifu:"+isshenshifu+"] [materialEntity:"+(materialEntity==null)+"] ["+player.getLogString()+"]");
//								player.addMessageToRightBag(new CONFIRM_SHENSHI_RES(req.getSequnceNum(), false));
//								return;
							}
							
							boolean hasStrongMaterial = false;
							Knapsack[] knapsacks = player.getKnapsacks_common();
							
							if(knapsacks==null){
								logger.warn("[法宝神识确认] [失败] [knapsacks==null] ["+player.getLogString()+"]");
								player.addMessageToRightBag(new CONFIRM_SHENSHI_RES(req.getSequnceNum(), false));
								return;
							}
							
							for (Knapsack knapsack : knapsacks) {
								if (knapsack != null) {
									int index = knapsack.indexOf(materialEntity);
									if (index != -1) {
										hasStrongMaterial = true;
										break;
									}
								}
							}
							
							if (!hasStrongMaterial) {
								resultmess = Translate.背包没有法宝鉴定符;
//								player.sendError(Translate.背包没有法宝鉴定符);
//								player.addMessageToRightBag(new CONFIRM_SHENSHI_RES(req.getSequnceNum(), false));
//								return;
							}
							
							
							if(materialnums < needCostNums){
								resultmess = Translate.法宝鉴定符数量不足;
//								player.sendError(Translate.法宝鉴定符数量不足);
//								player.addMessageToRightBag(new CONFIRM_SHENSHI_RES(req.getSequnceNum(), false));
//								return;
							}
							
							for (long strongMaterialId : list) {
								if (strongMaterialId > 0) {
									ArticleEntity aee = player.removeArticleEntityFromKnapsackByArticleId(strongMaterialId, "神识删除", true);
									if (aee == null) {
										String description = Translate.删除物品不成功;
										HINT_REQ hreq = new HINT_REQ(GameMessageFactory.nextSequnceNum(), (byte) 0, description);
										player.addMessageToRightBag(hreq);
										if (logger.isWarnEnabled()) logger.warn("[法宝神识确认] [失败] [{}] [{}] [{}] [{}] [{}] [{}] [{}mss]", new Object[] { player.getUsername(), player.getId(), player.getName(), (ae != null ? ae.getArticleName() : Translate.空白), (ae != null ? ae.getId() : Translate.空白), description, com.fy.engineserver.gametime.SystemTime.currentTimeMillis() - start });
										player.addMessageToRightBag(new CONFIRM_SHENSHI_RES(req.getSequnceNum(), false));
										return;
									} else {
										// 统计
										ArticleStatManager.addToArticleStat(player, null, aee, ArticleStatManager.OPERATION_物品获得和消耗, 0, ArticleStatManager.YINZI, 1, "强化删除", null);
									}
								}
							}
							
							String result = this.appraisal(player, me,false);
							if(result==null){
								resultmess = "ok";
								me.initAttrNum();
								player.sendError(Translate.神识成功);
								player.addMessageToRightBag(new CONFIRM_SHENSHI_RES(req.getSequnceNum(), true));
								logger.warn("[法宝神识确认] [成功] [扣除物品] ["+ae.getArticleName()+"] [list:"+list.size()+"] [需要数量:"+needCostNums+"] [实际数量："+materialnums+"] ["+player.getLogString()+"]");
							}else{
								resultmess = Translate.神识失败;
//								player.sendError(Translate.神识失败);
//								player.addMessageToRightBag(new CONFIRM_SHENSHI_RES(req.getSequnceNum(), false));
							}
						}else{
							resultmess = Translate.请放入法宝鉴定符;
//							player.sendError(Translate.请放入法宝鉴定符);
							logger.warn("[法宝神识确认] [错误] [没有材料] ["+ae.getArticleName()+"] [消耗法宝鉴定符："+materialids.length+"] ["+player.getLogString()+"]");
//							player.addMessageToRightBag(new CONFIRM_SHENSHI_RES(req.getSequnceNum(), false));
						}
						
						if(resultmess.equals("ok")==false){
							player.sendError(Translate.银子不足+resultmess);
							player.addMessageToRightBag(new CONFIRM_SHENSHI_RES(req.getSequnceNum(), false));
							logger.warn("[confirmShenShi] [error] [needsilve:"+mwBaseModel.get(ae.getColorType()).getCostSiliverNum()+"] ["+player.getLogString()+"]");
						}
						return;
					}
					
					BillingCenter bc = BillingCenter.getInstance();
					try {
						
						String result = this.appraisal(player, me,false);
						if(result==null){
							me.initAttrNum();
							bc.playerExpense(player, mwBaseModel.get(ae.getColorType()).getCostSiliverNum(), CurrencyType.SHOPYINZI, ExpenseReasonType.法宝神识, "法宝神识--"+ae.getColorType());
							player.sendError(Translate.神识成功);
							player.addMessageToRightBag(new CONFIRM_SHENSHI_RES(req.getSequnceNum(), true));
							logger.warn("[法宝神识确认] [成功] [扣除银子] ["+ae.getArticleName()+"] [消耗银子："+mwBaseModel.get(ae.getColorType()).getCostSiliverNum()+"] ["+player.getLogString()+"]");
						}else{
							player.sendError(Translate.神识失败);
							logger.warn("[法宝神识确认] [失败] [result:"+result+"] ["+ae.getArticleName()+"] [消耗银子："+mwBaseModel.get(ae.getColorType()).getCostSiliverNum()+"] ["+player.getLogString()+"]");
							player.addMessageToRightBag(new CONFIRM_SHENSHI_RES(req.getSequnceNum(), false));
							return;
						}
					} catch (NoEnoughMoneyException e) {
						e.printStackTrace();
						logger.warn("[法宝神识确认] [扣费异常] ["+e+"]");
						player.addMessageToRightBag(new CONFIRM_SHENSHI_RES(req.getSequnceNum(), false));
						return;
					} catch (BillFailedException e) {
						logger.warn("[法宝神识确认] [扣费异常] ["+e+"]");
						e.printStackTrace();
						player.addMessageToRightBag(new CONFIRM_SHENSHI_RES(req.getSequnceNum(), false));
						return;
					}
				}else{
					if(materialids!=null && materialids.length>0){
						boolean isshenshifu = true;
						int materialnums = 0;
						ArticleEntity materialEntity = null;
						List<Long> list = new ArrayList<Long>();
						int needCostNums = mwBaseModel.get(ae.getColorType()).getUsePropNum(); 
						String costname = mwBaseModel.get(ae.getColorType()).getPropForAppraisal();
						if(needCostNums <= 0 || costname==null || costname.isEmpty()){
							logger.warn("[法宝神识确认] [失败] [needCostNums <= 0] [costname==null]");
							player.addMessageToRightBag(new CONFIRM_SHENSHI_RES(req.getSequnceNum(), false));
							return;
						}
						
						for(int i=0,len=materialids.length;i<len;i++){
							materialnums += nums[i];
							ArticleEntity material = DefaultArticleEntityManager.getInstance().getEntity(materialids[i]);
							if(material!=null){
								if(material.getArticleName().equals(costname)==false){
									isshenshifu = false;
									break;
								}else{
									materialEntity = material;
								}
							}
							for(int j=0;j<nums[i];j++){
								if(list.size()<needCostNums){
									list.add(materialids[i]);
								}else{
									break;
								}
							}
						}
						
						if(!isshenshifu || materialEntity==null){
							player.sendError(Translate.请放入法宝鉴定符);
							logger.warn("[法宝神识确认] [失败] [clientsenderror] [isshenshifu:"+isshenshifu+"] [materialEntity:"+(materialEntity==null)+"] ["+player.getLogString()+"]");
							player.addMessageToRightBag(new CONFIRM_SHENSHI_RES(req.getSequnceNum(), false));
							return;
						}
						
						boolean hasStrongMaterial = false;
						Knapsack[] knapsacks = player.getKnapsacks_common();
						
						if(knapsacks==null){
							logger.warn("[法宝神识确认] [失败] [knapsacks==null] ["+player.getLogString()+"]");
							player.addMessageToRightBag(new CONFIRM_SHENSHI_RES(req.getSequnceNum(), false));
							return;
						}
						
						for (Knapsack knapsack : knapsacks) {
							if (knapsack != null) {
								int index = knapsack.indexOf(materialEntity);
								if (index != -1) {
									hasStrongMaterial = true;
									break;
								}
							}
						}
						
						if (!hasStrongMaterial) {
							player.sendError(Translate.背包没有法宝鉴定符);
							player.addMessageToRightBag(new CONFIRM_SHENSHI_RES(req.getSequnceNum(), false));
							return;
						}
						
						
						if(materialnums < needCostNums){
							player.sendError(Translate.法宝鉴定符数量不足);
							player.addMessageToRightBag(new CONFIRM_SHENSHI_RES(req.getSequnceNum(), false));
							return;
						}
						
						for (long strongMaterialId : list) {
							if (strongMaterialId > 0) {
								ArticleEntity aee = player.removeArticleEntityFromKnapsackByArticleId(strongMaterialId, "神识删除", true);
								if (aee == null) {
									String description = Translate.删除物品不成功;
									HINT_REQ hreq = new HINT_REQ(GameMessageFactory.nextSequnceNum(), (byte) 0, description);
									player.addMessageToRightBag(hreq);
									if (logger.isWarnEnabled()) logger.warn("[法宝神识确认] [失败] [{}] [{}] [{}] [{}] [{}] [{}] [{}mss]", new Object[] { player.getUsername(), player.getId(), player.getName(), (ae != null ? ae.getArticleName() : Translate.空白), (ae != null ? ae.getId() : Translate.空白), description, com.fy.engineserver.gametime.SystemTime.currentTimeMillis() - start });
									player.addMessageToRightBag(new CONFIRM_SHENSHI_RES(req.getSequnceNum(), false));
									return;
								} else {
									// 统计
									ArticleStatManager.addToArticleStat(player, null, aee, ArticleStatManager.OPERATION_物品获得和消耗, 0, ArticleStatManager.YINZI, 1, "强化删除", null);
								}
							}
						}
						
						String result = this.appraisal(player, me,false);
						if(result==null){
							me.initAttrNum();
							player.sendError(Translate.神识成功);
							player.addMessageToRightBag(new CONFIRM_SHENSHI_RES(req.getSequnceNum(), true));
							logger.warn("[法宝神识确认] [成功] [扣除物品] ["+ae.getArticleName()+"] [list:"+list.size()+"] [需要数量:"+needCostNums+"] [实际数量："+materialnums+"] ["+player.getLogString()+"]");
						}else{
							player.sendError(Translate.神识失败);
							player.addMessageToRightBag(new CONFIRM_SHENSHI_RES(req.getSequnceNum(), false));
						}
					}else{
						player.sendError(Translate.请放入法宝鉴定符);
						logger.warn("[法宝神识确认] [错误] [没有材料] ["+ae.getArticleName()+"] [消耗法宝鉴定符："+materialids.length+"] ["+player.getLogString()+"]");
						player.addMessageToRightBag(new CONFIRM_SHENSHI_RES(req.getSequnceNum(), false));
					}
				}
			}
		}else{
			player.sendError(Translate.请放入法宝);
			player.addMessageToRightBag(new CONFIRM_SHENSHI_RES(req.getSequnceNum(), false));
			logger.warn("[法宝神识确认] [错误] [ae==null] ["+player.getLogString()+"]");
		}
	}
	
	/**
	 * 法宝强化请求
	 * @param player
	 * @param req
	 * @return
	 */
	public QUERY_MAGICWEAPON_STRONG_RES queryMagicWeaponStrong(Player player, QUERY_MAGICWEAPON_STRONG_REQ req) {
		if(req == null || player == null){
			logger.warn("["+this.getClass().getSimpleName()+"] [queryMagicWeaponStrong] [error] [req:"+(req==null?"reqisnull":req.getTypeDescription())+"] [player:"+(player==null?"playerisnull":player.getLogString())+"] [req or player error]");
			return null;
		}
		
		ArticleEntityManager aem = ArticleEntityManager.getInstance();
		ArticleManager am = ArticleManager.getInstance();
		if (player != null && req != null && aem != null && am != null) {
			long magicweaponId = req.getMagicweaponId();
			ArticleEntity ae = aem.getEntity(magicweaponId);
			if (ae!=null && ae instanceof NewMagicWeaponEntity) {
				NewMagicWeaponEntity me = (NewMagicWeaponEntity)ae;
				int currstar = me.getMstar();
				Article a = am.getArticle(ae.getArticleName());
				
				String mess = "";
				String strongMaterialName[] = new String[]{};
				String otherStrongMaterialName = "";
				int otherStrongMaterialNum = 0;
				int otherStrongMaterialluck = 0;
				int[] strongMaterialLuck = ArticleManager.getInstance().根据装备强化等级得到不同颜色强化符的成功率分子值_new(me.getMstar());
				
				if(a==null){
					logger.warn("[法宝强化请求] [物品不存在："+ae.getArticleName()+"] ["+player.getLogString()+"] []");
					return null;
				}
				if(a instanceof MagicWeapon == false){
					logger.warn("[法宝强化请求] [物品不是法宝类型："+ae.getArticleName()+"] ["+player.getLogString()+"] []");
					return null;
				}
				
				if(currstar<MagicWeaponConstant.法宝羽化等级){
					strongMaterialName = new String[]{Translate.法宝强化石白,Translate.法宝强化石绿,Translate.法宝强化石蓝,Translate.法宝强化石紫,Translate.法宝强化石橙};
					mess = Translate.translateString(Translate.请求强化描述, new String[][] { { Translate.STRING_1, Translate.法宝强化石 } });
					
				}
//				else if(currstar==MagicWeaponConstant.法宝羽化等级){
//					MagicWeapon m = (MagicWeapon)a;
//					if(m.getDataLevel()<MagicWeaponConstant.法宝羽化等级限制){		//2014年10月22日    羽化需要等级改为树脂等级
//						player.sendError(Translate.法宝不能羽化携带等级不够);
//						return null;
//					}
//					mess = Translate.translateString(Translate.请求强化描述, new String[][] { { Translate.STRING_1, Translate.羽化石 } });
//					otherStrongMaterialName = Translate.羽化石;
//					otherStrongMaterialNum =  ArticleManager.每个等级消耗羽化石的数量[me.getMstar()];
//					otherStrongMaterialluck = (int) (ArticleManager.TOTAL_LUCK_VALUE * 0.3);
//				}else if(currstar>MagicWeaponConstant.法宝羽化等级){
//					strongMaterialName = new String[]{Translate.法宝强化石白,Translate.法宝强化石绿,Translate.法宝强化石蓝,Translate.法宝强化石紫,Translate.法宝强化石橙};
//					mess = Translate.translateString(Translate.请求强化描述, new String[][] { { Translate.STRING_1, Translate.法宝强化石+","+Translate.精华羽化石 } });
//					otherStrongMaterialName = Translate.精华羽化石;
//					otherStrongMaterialNum =  ArticleManager.每个等级消耗羽化石的数量[me.getMstar()];
//				}
//				
				mess = "<f size='28' color='0x78f4ff'>" + mess + "</f>";
				
				if (me.getMstar() < MagicWeaponConstant.starMaxValue) {
					QUERY_MAGICWEAPON_STRONG_RES res = new QUERY_MAGICWEAPON_STRONG_RES(req.getSequnceNum(), magicweaponId, strongConsumeMoney(me), mess, strongMaterialName,strongMaterialLuck ,otherStrongMaterialName,otherStrongMaterialNum,otherStrongMaterialluck);
					logger.warn("[法宝强化请求] [成功] ["+ae.getArticleName()+"] [currstar:"+currstar+"] [costsilver:"+strongConsumeMoney(me)+"] [costmarterial:"+(strongMaterialName!=null?Arrays.toString(strongMaterialName):"null")+"] [luck:"+(strongMaterialLuck!=null?Arrays.toString(strongMaterialLuck):"null")+"] 【otherName:"+otherStrongMaterialName+"] [otherNum:"+otherStrongMaterialNum+"] [otherluck:"+otherStrongMaterialluck+"】 ["+player.getName()+"]");
					return res;
				} else {
					String description = Translate.法宝已经强化到了上限;
					HINT_REQ hreq = new HINT_REQ(GameMessageFactory.nextSequnceNum(), (byte) 0, description);
					player.addMessageToRightBag(hreq);
				}
			} else {
				String description = Translate.空白;
				description = Translate.请放入法宝;
				HINT_REQ hreq = new HINT_REQ(GameMessageFactory.nextSequnceNum(), (byte) 0, description);
				player.addMessageToRightBag(hreq);
			}
		}
		return null;
	}
	
	public void magicWeaponStrong(Player player, MAGICWEAPON_STRONG_REQ req) {
		magicWeaponStrong(player, req, false);
	}
	
	public void magicWeaponStrong(Player player, MAGICWEAPON_STRONG_REQ req,boolean isstrong) {
		if(req == null || player == null){
			logger.warn("["+this.getClass().getSimpleName()+"] [magicWeaponStrong] [error] [req:"+(req==null?"reqisnull":req.getTypeDescription())+"] [player:"+(player==null?"playerisnull":player.getLogString())+"] [req or player error]");
			return;
		}
		logger.warn("[法宝强化申请] [测试] [0]");
		long start = com.fy.engineserver.gametime.SystemTime.currentTimeMillis();
		ArticleEntityManager aem = ArticleEntityManager.getInstance();
		ArticleManager am = ArticleManager.getInstance();
		if (player != null && aem != null && am != null && req != null) {
			long magicweaponId = req.getMagicweaponId();
			long[] strongMaterialIds = req.getStrongMaterialID();
			int strongType = req.getStrongType();
			long[] otherids = req.getOtherStrongMaterialID();
			int[] nums = req.getOtherStrongMaterialNum();
			ArticleEntity ae = aem.getEntity(magicweaponId);
			if(ae==null){
				player.sendError(Translate.物品不存在);
				if (logger.isWarnEnabled()) logger.warn("[法宝强化申请] [失败] [ae is null] ["+player.getLogString()+"]");
				return;
			}
			
			if(ae instanceof NewMagicWeaponEntity == false){
				player.sendError(Translate.请放入法宝);
				if (logger.isWarnEnabled()) logger.warn("[法宝强化申请] [失败] [ae not magicweapon] ["+player.getLogString()+"]");
				return;
			}
			
			NewMagicWeaponEntity me = (NewMagicWeaponEntity)ae;
			
			MagicWeapon m = (MagicWeapon) ArticleManager.getInstance().getArticle(me.getArticleName());
			if(me.getMstar() == 20 && m.getDataLevel()<MagicWeaponConstant.法宝羽化等级限制){		//2014年10月22日    羽化需要等级改为树脂等级
				player.sendError(Translate.法宝不能羽化携带等级不够);
				logger.warn("[法宝强化确认] [失败] [" + player.getLogString() + "] [数值等级不足220:" + me.getArticleName() + "]");
				return;
			}
					
			MagicWeaponAttrModel[] basiAttr = me.getBasiAttr();
			if(basiAttr==null || basiAttr.length<=0){
				player.sendError(Translate.未神识探查);
				return;
			}
			
			if (me.getMstar() >= MagicWeaponConstant.starMaxValue) {
				player.sendError(Translate.法宝已经强化到了上限);
				return;
			}
			
			boolean 我是20星 = false;
			int 强化需要羽化石数量 = ArticleManager.每个等级消耗羽化石的数量[me.getMstar()];
			int 实际羽化石数量 = 0;
			
			if (nums != null && nums.length > 0) {
				for (int num : nums) {
					实际羽化石数量 += num;
				}
			}

			if (实际羽化石数量 < 强化需要羽化石数量) {
				player.sendError(Translate.text_trade_006);
				if (logger.isWarnEnabled()) logger.warn("[法宝强化申请] [失败] [原因：放入格子中的羽化石数量不足,需要" + 强化需要羽化石数量 + "] [实际羽化石数量:" + 实际羽化石数量 + "] [start:" + me.getMstar() + "] [" + player.getLogString() + "]");
				return;
			}
			
			ArticleEntity needArticle = null;
			if (otherids != null && otherids.length > 0) {
				needArticle = aem.getEntity(otherids[0]);
			}
			
			if (me.getMstar() == MagicWeaponConstant.法宝羽化等级) {
				strongMaterialIds = new long[] { -1, -1, -1, -1 };
				我是20星 = true;
				if (needArticle != null) {
					if (!needArticle.getArticleName().equals(Translate.羽化石)) {
						player.sendError(Translate.请放入羽化石);
						return;
					}
				}

				if (player.getKnapsack_common().getArticleCellCount_重叠(Translate.羽化石) < 强化需要羽化石数量) {
					player.sendError(Translate.text_trade_006);
					if (logger.isWarnEnabled()) logger.warn("[法宝强化申请] [失败] [原因： 背包中羽化石石数量不足,需要" + 强化需要羽化石数量 + "] [实际羽化石数量:" + 实际羽化石数量 + "] [start:" + me.getMstar() + "] [" + player.getLogString() + "]");
					return;
				}
			}

			
			if (strongMaterialIds == null && !我是20星) {
				String description = Translate.空白;
				description = Translate.请放入法宝强化石;
				HINT_REQ hreq = new HINT_REQ(GameMessageFactory.nextSequnceNum(), (byte) 0, description);
				player.addMessageToRightBag(hreq);
				if (logger.isWarnEnabled()) logger.warn("[法宝强化申请] [失败] [{}] [{}] [{}] [{}] [{}] [{},{}的物品为空] [{}ms]", new Object[] { player.getUsername(), player.getId(), player.getName(), (ae != null ? ae.getArticleName() : Translate.空白), (ae != null ? ae.getId() : Translate.空白), description, strongMaterialIds, com.fy.engineserver.gametime.SystemTime.currentTimeMillis() - start });
				return;
			}
			
			if (me.getMstar() > MagicWeaponConstant.法宝羽化等级) {
				if (needArticle != null) {
					if (!needArticle.getArticleName().equals(Translate.精华羽化石)) {
						player.sendError(Translate.请放入精华羽化石);
						return;
					}
				}

				if (player.getKnapsack_common().getArticleCellCount_重叠(Translate.精华羽化石) < 强化需要羽化石数量) {
					player.sendError(Translate.text_trade_006);
					if (logger.isWarnEnabled()) logger.warn("[法宝强化申请] [失败] [原因： 背包中精华羽化石石数量不足,需要" + 强化需要羽化石数量 + "] [实际羽化石数量:" + 实际羽化石数量 + "] [start:" + me.getMstar() + "] [" + player.getLogString() + "]");
					return;
				}
			}

			if (strongType == 0) {
				if (!player.bindSilverEnough(strongConsumeMoney(me))) {
					String description = Translate.金币不足;
					try {
						description = Translate.translateString(Translate.绑银不足提示, new String[][] { { Translate.COUNT_1, BillingCenter.得到带单位的银两(strongConsumeMoney(me)) } });
					} catch (Exception ex) {
						ex.printStackTrace();
					}
					HINT_REQ hreq = new HINT_REQ(GameMessageFactory.nextSequnceNum(), (byte) 0, description);
					player.addMessageToRightBag(hreq);
					if (logger.isWarnEnabled()) logger.warn("[法宝强化申请] [失败] [{}] [{}] [{}] [{}] [{}] [{},{}的物品为空] [{}ms]", new Object[] { player.getUsername(), player.getId(), player.getName(), (ae != null ? ae.getArticleName() : Translate.空白), (ae != null ? ae.getId() : Translate.空白), description, strongMaterialIds, com.fy.engineserver.gametime.SystemTime.currentTimeMillis() - start });
					return;
				}
			} else if (strongType == 1) {
				if (player.getSilver()+player.getShopSilver() < strongConsumeMoney(me)) {
					String description = Translate.元宝不足;
					try {
						description = Translate.translateString(Translate.银子不足提示, new String[][] { { Translate.COUNT_1, BillingCenter.得到带单位的银两(strongConsumeMoney(me)) } });
					} catch (Exception ex) {
						ex.printStackTrace();
					}
					HINT_REQ hreq = new HINT_REQ(GameMessageFactory.nextSequnceNum(), (byte) 0, description);
					player.addMessageToRightBag(hreq);
					if (logger.isWarnEnabled()) logger.warn("[法宝强化申请] [失败] [{}] [{}] [{}] [{}] [{}] [{},{}的物品为空] [{}ms]", new Object[] { player.getUsername(), player.getId(), player.getName(), (ae != null ? ae.getArticleName() : Translate.空白), (ae != null ? ae.getId() : Translate.空白), description, strongMaterialIds, com.fy.engineserver.gametime.SystemTime.currentTimeMillis() - start });
					return;
				}
			}

			if (strongMaterialIds.length != MagicWeaponConstant.strongMaterialMaxNumber && !我是20星) {
				String description = Translate.空白;
				description = Translate.请输入正确数量;
				HINT_REQ hreq = new HINT_REQ(GameMessageFactory.nextSequnceNum(), (byte) 0, description);
				player.addMessageToRightBag(hreq);
				if (logger.isWarnEnabled()) logger.warn("[法宝强化申请] [失败] [{}] [{}] [{}] [{}] [{}] [{},{}!={}] [{}ms]", new Object[] { player.getUsername(), player.getId(), player.getName(), (ae != null ? ae.getArticleName() : Translate.空白), (ae != null ? ae.getId() : Translate.空白), description, strongMaterialIds.length, MagicWeaponConstant.strongMaterialMaxNumber, com.fy.engineserver.gametime.SystemTime.currentTimeMillis() - start });
				return;
			}
			
			{
//				MagicWeapon e = (MagicWeapon) am.getArticle(ae.getArticleName());
				int[] strongMaterialColorLuck = ArticleManager.getInstance().根据装备强化等级得到不同颜色强化符的成功率分子值_new(me.getMstar());
				Knapsack[] knapsacks = player.getKnapsacks_common();
				ArrayList<ArticleEntity> strongMaterialEntitys = new ArrayList<ArticleEntity>();
				for (long id : strongMaterialIds) {
					if (id != -1) {
						ArticleEntity strongMaterialEntity = aem.getEntity(id);
						if (strongMaterialEntity == null) {
							String description = Translate.空白;
							description = Translate.请放入法宝强化石;
							HINT_REQ hreq = new HINT_REQ(GameMessageFactory.nextSequnceNum(), (byte) 0, description);
							player.addMessageToRightBag(hreq);
							if (logger.isWarnEnabled()) logger.warn("[法宝强化申请] [失败] [{}] [{}] [{}] [{}] [{}] [{}id为{}的物品为空] [{}ms]", new Object[] { player.getUsername(), player.getId(), player.getName(), (ae != null ? ae.getArticleName() : Translate.空白), (ae != null ? ae.getId() : Translate.空白), description, id, com.fy.engineserver.gametime.SystemTime.currentTimeMillis() - start });
							return;
						}
						boolean hasStrongMaterial = false;
						if (knapsacks != null) {
							for (Knapsack knapsack : knapsacks) {
								if (knapsack != null) {
									int index = knapsack.indexOf(strongMaterialEntity);
									if (index != -1) {
										hasStrongMaterial = true;
										break;
									}
								}
							}
						}
						if (!hasStrongMaterial) {
							String description = Translate.空白;
							description = Translate.请放入法宝强化石;
							HINT_REQ hreq = new HINT_REQ(GameMessageFactory.nextSequnceNum(), (byte) 0, description);
							player.addMessageToRightBag(hreq);
							if (logger.isWarnEnabled()) logger.warn("[法宝强化申请] [失败] [2] [{}] [{}] [{}] [{}] [{}] [{}背包中没有id为{}的物品] [{}ms]", new Object[] { player.getUsername(), player.getId(), player.getName(), (ae != null ? ae.getArticleName() : Translate.空白), (ae != null ? ae.getId() : Translate.空白), description, strongMaterialEntity.getId(), com.fy.engineserver.gametime.SystemTime.currentTimeMillis() - start });
							return;
						}
						
						strongMaterialEntitys.add(strongMaterialEntity);
					}
				}
				if (strongMaterialEntitys.isEmpty() && !我是20星) {
					String description = Translate.空白;
					description = Translate.请放入法宝强化石;
					HINT_REQ hreq = new HINT_REQ(GameMessageFactory.nextSequnceNum(), (byte) 0, description);
					player.addMessageToRightBag(hreq);
					if (logger.isWarnEnabled()) logger.warn("[法宝强化申请] [失败] [{}] [{}] [{}] [{}] [{}] [{}] [{}ms]", new Object[] { player.getUsername(), player.getId(), player.getName(), (ae != null ? ae.getArticleName() : Translate.空白), (ae != null ? ae.getId() : Translate.空白), description, com.fy.engineserver.gametime.SystemTime.currentTimeMillis() - start });
					return;
				}
				
				if(me.getMstar() < MagicWeaponConstant.法宝羽化等级){
					boolean isregihtmaterial = true;
					for(int i=0;i<strongMaterialEntitys.size();i++){
						if(strongMaterialEntitys.get(i).getArticleName().startsWith(Translate.法宝强化石)==false){
							isregihtmaterial = false;
							break;
						}
					}
					if(!isregihtmaterial){
						player.sendError(Translate.translateString(Translate.没有炼器所需材料, new String[][] { { Translate.STRING_1, Translate.法宝强化石 } }));
						return;
					}
				}
				
				boolean hasMagicWeapon = false;
				if (knapsacks != null) {
					for (Knapsack knapsack : knapsacks) {
						if (knapsack != null) {
							if (!hasMagicWeapon) {
								int index1 = knapsack.hasArticleEntityByArticleId(magicweaponId);
								if (index1 != -1) {
									hasMagicWeapon = true;
									break;
								}
							}
						}
					}
				}
				if (hasMagicWeapon) {
					boolean bindedTip = false;
					if (!me.isBinded()) {	//如果有材料是绑定的，法宝是绑定的
						for (ArticleEntity strongMaterialEntity : strongMaterialEntitys) {
							if (strongMaterialEntity != null && strongMaterialEntity.isBinded()) {
								bindedTip = true;
								break;
							}
						}
					}
					
					if(!bindedTip){
						if(otherids != null && otherids.length>0){
							for(long id : otherids){
								ArticleEntity otherAE = ArticleEntityManager.getInstance().getEntity(id);
								if(otherAE != null && otherAE.isBinded()){
									bindedTip = true;
									break;
								}
							}
						}
					}
					
					if(bindedTip && isstrong==false){//绑定材料
						//TODO
						MenuWindow mw=WindowManager.getInstance().createTempMenuWindow(300);
						mw.setDescriptionInUUB(Translate.法宝强化材料绑定提示);
						Option_MagicWeapon_Strong_Sure option = new Option_MagicWeapon_Strong_Sure();
						option.setText(Translate.text_125);
						option.setReq(req);
						Option_Cancel oc=new Option_Cancel();
						oc.setText(Translate.text_126);
						oc.setColor(0xffffff);
						mw.setOptions(new Option[]{option,oc});
						
						QUERY_WINDOW_RES res = new QUERY_WINDOW_RES(GameMessageFactory.nextSequnceNum(),mw,mw.getOptions());
						player.addMessageToRightBag(res);
						return;
					}
					
					int totalLuckValue = 0;
					for (ArticleEntity strongMaterialEntity : strongMaterialEntitys) {
						if (strongMaterialEntity != null) {
							try {
								totalLuckValue += strongMaterialColorLuck[strongMaterialEntity.getColorType()];
							} catch (Exception ex) {
								ex.printStackTrace();
							}
						}
					}
					double percent = totalLuckValue * 1.0 / MagicWeaponConstant.TOTAL_LUCK_VALUE;

					if (percent > 1) {
						percent = 1;
					}
					if (我是20星) {
						percent = 0.3;
						totalLuckValue = (int) (MagicWeaponConstant.TOTAL_LUCK_VALUE * 0.3);
					}
					logger.warn("[法宝强化申请] [测试] [strongType:"+strongType+"] [1]");
					try {
						confirmMagicWeaponStrong(player, req, 我是20星);
					} catch (Exception ex) {
						ex.printStackTrace();
					}
					logger.warn("[法宝强化申请] [测试] [strongType:"+strongType+"] [11]");
					return;
				} else {
					String description = Translate.请放入正确物品;
					try {
						description = Translate.translateString(Translate.背包中没有该物品, new String[][] { { Translate.ARTICLE_NAME_1, (ae != null ? ae.getArticleName() : Translate.该物品) } });
					} catch (Exception ex) {
						ex.printStackTrace();
					}
					HINT_REQ hreq = new HINT_REQ(GameMessageFactory.nextSequnceNum(), (byte) 0, description);
					player.addMessageToRightBag(hreq);
					if (logger.isWarnEnabled()) logger.warn("[法宝强化申请] [失败] [{}] [{}] [{}] [{}] [{}] [{}] [{}ms]", new Object[] { player.getUsername(), player.getId(), player.getName(), (ae != null ? ae.getArticleName() : Translate.空白), (ae != null ? ae.getId() : Translate.空白), description, com.fy.engineserver.gametime.SystemTime.currentTimeMillis() - start });
					return;
				}
			}
		}
	}
	
	public void confirmMagicWeaponStrong(Player player, MAGICWEAPON_STRONG_REQ req, boolean 是20星) {
		logger.warn("[法宝强化申请] [测试] [2]");
		long magicweaponId = req.getMagicweaponId();
		byte strongType = req.getStrongType();
		long[] strongMaterialIds = req.getStrongMaterialID();
		long[] otherids = req.getOtherStrongMaterialID();
		int[] nums = req.getOtherStrongMaterialNum();
		if (otherids.length != nums.length) {
			logger.warn("[法宝强化确认] [失败] [原因：羽化石id长度和个数长度不一致] [" + player.getLogString() + "]  ");
		}
		long start = com.fy.engineserver.gametime.SystemTime.currentTimeMillis();
		ArticleEntityManager aem = ArticleEntityManager.getInstance();
		ArticleManager am = ArticleManager.getInstance();
		logger.warn("[法宝强化申请] [测试] [22]");
		if (player != null && aem != null && am != null) {
			ArticleEntity ae = aem.getEntity(magicweaponId);

			if(ae==null){
				player.sendError(Translate.物品不存在);
				if (logger.isWarnEnabled()) logger.warn("[法宝强化确认] [失败] [ae is null] ["+player.getLogString()+"]");
				return;
			}
			
			if(ae instanceof NewMagicWeaponEntity == false){
				player.sendError(Translate.请放入法宝);
				if (logger.isWarnEnabled()) logger.warn("[法宝强化确认] [失败] [ae not magicweapon] ["+player.getLogString()+"]");
				return;
			}
			
			if (!ArticleProtectManager.instance.isCanDo(player, ArticleProtectDataValues.ArticleProtect_High, ae.getId())) {
				player.sendError(Translate.高级锁魂物品不能做此操作);
				return ;
			}
			
			NewMagicWeaponEntity me = (NewMagicWeaponEntity)ae;
			logger.warn("[法宝强化申请] [测试] [3]");
			if (strongType == 0) {
				if (!player.bindSilverEnough(strongConsumeMoney(me))) {
					String description = Translate.金币不足;
					try {
						description = Translate.translateString(Translate.绑银不足提示, new String[][] { { Translate.COUNT_1, BillingCenter.得到带单位的银两(strongConsumeMoney(me)) } });
					} catch (Exception ex) {
						ex.printStackTrace();
					}
					HINT_REQ hreq = new HINT_REQ(GameMessageFactory.nextSequnceNum(), (byte) 0, description);
					player.addMessageToRightBag(hreq);
					if (logger.isWarnEnabled()) logger.warn("[法宝强化确认] [失败] [{}] [{}] [{}] [{}] [{}] [{},{}的物品为空] [{}ms]", new Object[] { player.getUsername(), player.getId(), player.getName(), (ae != null ? ae.getArticleName() : Translate.空白), (ae != null ? ae.getId() : Translate.空白), description, strongMaterialIds, com.fy.engineserver.gametime.SystemTime.currentTimeMillis() - start });
					return;
				}
			} else if (strongType == 1) {
				if (player.getSilver() +player.getShopSilver()< strongConsumeMoney(me)) {
					String description = Translate.元宝不足;
					try {
						description = Translate.translateString(Translate.银子不足提示, new String[][] { { Translate.COUNT_1, BillingCenter.得到带单位的银两(strongConsumeMoney(me)) } });
					} catch (Exception ex) {
						ex.printStackTrace();
					}
					HINT_REQ hreq = new HINT_REQ(GameMessageFactory.nextSequnceNum(), (byte) 0, description);
					player.addMessageToRightBag(hreq);
					if (logger.isWarnEnabled()) logger.warn("[法宝强化确认] [失败] [{}] [{}] [{}] [{}] [{}] [{},{}的物品为空] [{}ms]", new Object[] { player.getUsername(), player.getId(), player.getName(), (ae != null ? ae.getArticleName() : Translate.空白), (ae != null ? ae.getId() : Translate.空白), description, strongMaterialIds, com.fy.engineserver.gametime.SystemTime.currentTimeMillis() - start });
					return;
				}
			}
			
			if (me.getMstar() >= 20) {
				MagicWeapon m = (MagicWeapon) ArticleManager.getInstance().getArticle(me.getArticleName());
				if(m.getDataLevel()<MagicWeaponConstant.法宝羽化等级限制){		//2014年10月22日    羽化需要等级改为树脂等级
					player.sendError(Translate.法宝不能羽化携带等级不够);
					logger.warn("[法宝强化确认] [失败] [" + player.getLogString() + "] [数值等级不足220:" + me.getArticleName() + "]");
					return;
				}
			}
			
			if (strongMaterialIds == null) {
				String description = Translate.空白;
				description = Translate.请放入法宝强化石;
				HINT_REQ hreq = new HINT_REQ(GameMessageFactory.nextSequnceNum(), (byte) 0, description);
				player.addMessageToRightBag(hreq);
				if (logger.isWarnEnabled()) logger.warn("[法宝强化确认] [失败] [{}] [{}] [{}] [{}] [{}] [{},{}的物品为空] [{}ms]", new Object[] { player.getUsername(), player.getId(), player.getName(), (ae != null ? ae.getArticleName() : Translate.空白), (ae != null ? ae.getId() : Translate.空白), description, strongMaterialIds, com.fy.engineserver.gametime.SystemTime.currentTimeMillis() - start });
				return;
			}
			
			if (strongMaterialIds.length != MagicWeaponConstant.strongMaterialMaxNumber && !是20星) {
				String description = Translate.空白;
				description = Translate.请输入正确数量;
				HINT_REQ hreq = new HINT_REQ(GameMessageFactory.nextSequnceNum(), (byte) 0, description);
				player.addMessageToRightBag(hreq);
				if (logger.isWarnEnabled()) logger.warn("[法宝强化确认] [失败] [{}] [{}] [{}] [{}] [{}] [{},{}!={}] [{}ms]", new Object[] { player.getUsername(), player.getId(), player.getName(), (ae != null ? ae.getArticleName() : Translate.空白), (ae != null ? ae.getId() : Translate.空白), description, strongMaterialIds.length, MagicWeaponConstant.strongMaterialMaxNumber, com.fy.engineserver.gametime.SystemTime.currentTimeMillis() - start });
				return;
			}
			
			logger.warn("[法宝强化申请] [测试] [4]");
			if (ae instanceof NewMagicWeaponEntity) {
				if (me.getMstar() < MagicWeaponConstant.starMaxValue) {
//					MagicWeapon e = (MagicWeapon) am.getArticle(ae.getArticleName());
					int[] strongMaterialColorLuck = ArticleManager.getInstance().根据装备强化等级得到不同颜色强化符的成功率分子值_new(me.getMstar());
					Knapsack[] knapsacks = player.getKnapsacks_common();
					ArrayList<ArticleEntity> strongMaterialEntitys = new ArrayList<ArticleEntity>();
					ArrayList<ArticleEntity> strongMaterialEntitys2 = new ArrayList<ArticleEntity>();
					if (是20星) {
						strongMaterialIds = new long[] { -1, -1, -1, -1 };
					}
					for (long id : strongMaterialIds) {
						if (id != -1) {
							ArticleEntity strongMaterialEntity = aem.getEntity(id);
							if (strongMaterialEntity == null) {
								String description = Translate.空白;
								description = Translate.请放入法宝强化物品;
								HINT_REQ hreq = new HINT_REQ(GameMessageFactory.nextSequnceNum(), (byte) 0, description);
								player.addMessageToRightBag(hreq);
								if (logger.isWarnEnabled()) logger.warn("[法宝强化确认] [失败] [{}] [{}] [{}] [{}] [{}] [{}id为{}的物品为空] [{}ms]", new Object[] { player.getUsername(), player.getId(), player.getName(), (ae != null ? ae.getArticleName() : Translate.空白), (ae != null ? ae.getId() : Translate.空白), description, id, com.fy.engineserver.gametime.SystemTime.currentTimeMillis() - start });
								return;
							}
							
							boolean hasStrongMaterial = false;
							if (knapsacks != null) {
								for (Knapsack knapsack : knapsacks) {
									if (knapsack != null) {
										int index = knapsack.indexOf(strongMaterialEntity);
										if (index != -1) {
											hasStrongMaterial = true;
											break;
										}
									}
								}
							}
							if (!hasStrongMaterial) {
								String description = Translate.空白;
								description = Translate.请放入法宝强化石;
								HINT_REQ hreq = new HINT_REQ(GameMessageFactory.nextSequnceNum(), (byte) 0, description);
								player.addMessageToRightBag(hreq);
								if (logger.isWarnEnabled()) logger.warn("[法宝强化确认] [失败] [{}] [{}] [{}] [{}] [{}] [{}背包中没有id为{}的物品] [{}ms]", new Object[] { player.getUsername(), player.getId(), player.getName(), (ae != null ? ae.getArticleName() : Translate.空白), (ae != null ? ae.getId() : Translate.空白), description, strongMaterialEntity.getId(), com.fy.engineserver.gametime.SystemTime.currentTimeMillis() - start });
								return;
							}
							strongMaterialEntitys.add(strongMaterialEntity);
						}
					}
					if (strongMaterialEntitys.isEmpty() && !是20星) {
						String description = Translate.空白;
						description = Translate.请放入法宝强化石;
						HINT_REQ hreq = new HINT_REQ(GameMessageFactory.nextSequnceNum(), (byte) 0, description);
						player.addMessageToRightBag(hreq);
						if (logger.isWarnEnabled()) logger.warn("[法宝强化确认] [失败] [{}] [{}] [{}] [{}] [{}] [{}] [{}ms]", new Object[] { player.getUsername(), player.getId(), player.getName(), (ae != null ? ae.getArticleName() : Translate.空白), (ae != null ? ae.getId() : Translate.空白), description, com.fy.engineserver.gametime.SystemTime.currentTimeMillis() - start });
						return;
					}
					
					if(me.getMstar() < MagicWeaponConstant.法宝羽化等级){
						boolean isregihtmaterial = true;
						for(int i=0;i<strongMaterialEntitys.size();i++){
							if(strongMaterialEntitys.get(i).getArticleName().startsWith(Translate.法宝强化石)==false){
								isregihtmaterial = false;
								break;
							}
						}
						if(!isregihtmaterial){
							player.sendError(Translate.translateString(Translate.没有炼器所需材料, new String[][] { { Translate.STRING_1, Translate.法宝强化石 } }));
							return;
						}
					}
					
					boolean hasEquipment = false;
					if (knapsacks != null) {
						for (Knapsack knapsack : knapsacks) {
							if (knapsack != null) {
								if (!hasEquipment) {
									int index1 = knapsack.hasArticleEntityByArticleId(magicweaponId);
									if (index1 != -1) {
										hasEquipment = true;
										break;
									}
								}
							}
						}
					}
					
					if (hasEquipment) {
						boolean bindedTip = false;
						if (!me.isBinded()) {
							for (ArticleEntity strongMaterialEntity : strongMaterialEntitys) {
								if (strongMaterialEntity != null && strongMaterialEntity.isBinded()) {
									bindedTip = true;
									break;
								}
							}
						}
						if(!bindedTip){
							if(otherids != null && otherids.length>0){
								for(long id : otherids){
									ArticleEntity otherAE = ArticleEntityManager.getInstance().getEntity(id);
									if(otherAE != null && otherAE.isBinded()){
										bindedTip = true;
										break;
									}
								}
							}
						}
						int totalLuckValue = 0;
						for (ArticleEntity strongMaterialEntity : strongMaterialEntitys) {
							if (strongMaterialEntity != null) {
								try {
									totalLuckValue += strongMaterialColorLuck[strongMaterialEntity.getColorType()];
								} catch (Exception ex) {
									ex.printStackTrace();
								}
							}
						}
						double percent = totalLuckValue * 1.0 / MagicWeaponConstant.TOTAL_LUCK_VALUE;
						if (percent > 1) {
							percent = 1;
						}
						
						if (是20星) {
							percent = 0.3;
							totalLuckValue = (int) (MagicWeaponConstant.TOTAL_LUCK_VALUE * 0.3);
						}
						
						if (knapsacks != null) {
							
							if (me.getMstar() >= 20 && otherids != null && otherids.length > 0) {
								int 强化需要羽化石数量 = ArticleManager.每个等级消耗羽化石的数量[me.getMstar()];
								long id = -1;
								if (otherids.length > 0) {
									id = otherids[0];
								}

								if (id != -1) {
									for (int j = 0; j < 强化需要羽化石数量; j++) {
										ArticleEntity aee1 = player.removeArticleEntityFromKnapsackByArticleId(id, "法宝加持删除", true);
										if (aee1 == null) {
											String description = Translate.删除物品不成功;
											HINT_REQ hreq = new HINT_REQ(GameMessageFactory.nextSequnceNum(), (byte) 0, description);
											player.addMessageToRightBag(hreq);
											if (logger.isWarnEnabled()) logger.warn("[法宝强化确认] [大于20星]  [删除额外的] [id:" + id + "] [失败] [{}] [{}] [{}] [{}] [{}] [{}] [{}ms2]", new Object[] { player.getUsername(), player.getId(), player.getName(), (ae != null ? ae.getArticleName() : Translate.空白), (ae != null ? ae.getId() : Translate.空白), description, com.fy.engineserver.gametime.SystemTime.currentTimeMillis() - start });
											return;
										} else {
											// 统计
											strongMaterialEntitys2.add(aee1);
											ArticleStatManager.addToArticleStat(player, null, aee1, ArticleStatManager.OPERATION_物品获得和消耗, 0, ArticleStatManager.ARTICLES, 1, "法宝加持删除", null);
										}
									}
								}

							}

							if (!是20星) {
								for (long strongMaterialId : strongMaterialIds) {
									if (strongMaterialId != -1) {
										ArticleEntity aee1 = player.removeArticleEntityFromKnapsackByArticleId(strongMaterialId, "法宝加持删除", true);
										if (aee1 == null) {
											String description = Translate.删除物品不成功;
											HINT_REQ hreq = new HINT_REQ(GameMessageFactory.nextSequnceNum(), (byte) 0, description);
											player.addMessageToRightBag(hreq);
											if (logger.isWarnEnabled()) logger.warn("[法宝强化确认] [失败] [{}] [{}] [{}] [{}] [{}] [{}] [{}ms]", new Object[] { player.getUsername(), player.getId(), player.getName(), (ae != null ? ae.getArticleName() : Translate.空白), (ae != null ? ae.getId() : Translate.空白), description, com.fy.engineserver.gametime.SystemTime.currentTimeMillis() - start });
											return;
										} else {
											// 统计
											ArticleStatManager.addToArticleStat(player, null, aee1, ArticleStatManager.OPERATION_物品获得和消耗, 0, ArticleStatManager.ARTICLES, 1, "法宝加持删除", null);
											// 活跃度统计
											ActivenessManager.getInstance().record(player, ActivenessType.强化装备);
											strongMaterialEntitys2.add(aee1);
										}
									}
								}
							}
						}

						// 扣费
						try {
							if (strongType == 0) {
								BillingCenter bc = BillingCenter.getInstance();
								bc.playerExpense(player, strongConsumeMoney(me), CurrencyType.BIND_YINZI, ExpenseReasonType.法宝, "绑银法宝加持");
							} else if (strongType == 1) {
								BillingCenter bc = BillingCenter.getInstance();
								bc.playerExpense(player, strongConsumeMoney(me), CurrencyType.SHOPYINZI, ExpenseReasonType.法宝, "银子法宝加持");
							}
							this.recordMagicOpt(player, RecordAction.法宝加持, 0);
							if(strongMaterialEntitys2 != null && strongMaterialEntitys2.size() > 0) {
								try {
									ShopActivityManager.getInstance().noticeUseSuccess(player, strongMaterialEntitys2);
								} catch (Exception e2) {
									ActivitySubSystem.logger.error("[使用赠送活动报错]",e2);
								}
							}
							
						} catch (Exception ex) {
							ex.printStackTrace();
							if (logger.isWarnEnabled()) logger.warn("[法宝强化确认] [失败] [" + player.getUsername() + "] [" + player.getId() + "] [" + player.getName() + "] [" + (ae != null ? ae.getArticleName() : Translate.空白) + "] [" + (ae != null ? ae.getId() : Translate.空白) + "]", ex);
							return;
						}
						int resultValue = player.random.nextInt(MagicWeaponConstant.TOTAL_LUCK_VALUE) + 1;
						boolean success = false;
						if (totalLuckValue >= resultValue) {
							success = true;
						}
						int oldStar = me.getMstar();
						if (success) {
							me.setMstar(oldStar + 1);
							try {
								AchievementManager.getInstance().record(player, RecordAction.法宝星级, me.getMstar());
							} catch (Exception e) {
								PlayerAimManager.logger.error("[目标系统] [统计法宝星级] [异常] [" + player.getLogString() + "]", e);
							}
							if (me.getMstar() >= MagicWeaponConstant.starMaxValue) {
								me.setMstar((int)MagicWeaponConstant.starMaxValue);
							}
							
							{
								int a = me.getMstar() / 2;
								int b = me.getMstar() % 2;
								if(me.getMstar() <= MagicWeaponConstant.法宝羽化等级){
									if (a >= 5 && b == 0) {
										try {
											ChatMessageService cms = ChatMessageService.getInstance();
											ChatMessage msg = new ChatMessage();
											// 恭喜！<国家>的<玩家姓名>经历千辛万苦将<装备名>炼到<星级>。（世界）
											String descri = Translate.translateString(Translate.装备强化系统广播详细, new String[][] { { Translate.STRING_1, CountryManager.得到国家名(player.getCountry()) }, { Translate.PLAYER_NAME_1, player.getName() }, { Translate.STRING_2, me.getArticleName() }, { Translate.COUNT_1, me.getMstar() + "" } });
											if (me.getMstar() <= MagicWeaponConstant.法宝羽化等级) {
												if (a == 5) {
													descri = "<f color='" + ArticleManager.COLOR_WHITE + "'>" + descri + "</f>";
												} else if (a == 6) {
													descri = "<f color='" + ArticleManager.COLOR_GREEN + "'>" + descri + "</f>";
												} else if (a == 7) {
													descri = "<f color='" + ArticleManager.COLOR_BLUE + "'>" + descri + "</f>";
												} else if (a == 8) {
													descri = "<f color='" + ArticleManager.COLOR_PURPLE + "'>" + descri + "</f>";
												} else if (a == 9) {
													descri = "<f color='" + ArticleManager.COLOR_ORANGE + "'>" + descri + "</f>";
												} else if (a == 10) {
													descri = "<f color='" + ArticleManager.COLOR_ORANGE + "'>" + descri + "</f>";
												}
											}

											msg.setMessageText(descri);
											cms.sendMessageToSystem(msg);
										} catch (Exception ex) {
											ex.printStackTrace();
										}
									}
								}else{
									try {
										ChatMessageService cms = ChatMessageService.getInstance();
										ChatMessage msg = new ChatMessage();
										String descri = Translate.translateString(Translate.装备强化系统广播详细_羽化, new String[][] { { Translate.STRING_1, CountryManager.得到国家名(player.getCountry()) }, { Translate.PLAYER_NAME_1, player.getName() }, { Translate.STRING_2, me.getArticleName() }, { Translate.STRING_3, ArticleManager.获得对应的羽化描述.get(new Integer(me.getMstar())) } });
										descri = "<f color='" + ArticleManager.COLOR_ORANGE_2 + "'>" + descri + "</f>";
										msg.setMessageText(descri);
										cms.sendMessageToSystem(msg);
										try {
											AchievementManager.getInstance().record(player, RecordAction.法宝羽化成功次数);
										} catch (Exception e) {
											PlayerAimManager.logger.error("[目标系统] [统计法宝羽化成功次数] [异常] [" + player.getLogString() + "]", e);
										}
									} catch (Exception e1) {
										e1.printStackTrace();
										logger.error("21星以上发送世界公告出错");
									}
								}
							}
							String description = Translate.恭喜法宝升级成功;
							try {
								description = Translate.translateString(Translate.装备强化成功, new String[][] { { Translate.ARTICLE_NAME_1, me.getArticleName() }, new String[] { Translate.LEVEL_1, me.getMstar() + "" } });
								if (me.getMstar() > 20) {
									description = Translate.translateString(Translate.装备羽化成功, new String[][] { { Translate.ARTICLE_NAME_1, me.getArticleName() }, new String[] { Translate.STRING_1, ArticleManager.获得对应的羽化描述.get(new Integer(me.getMstar())) } });
								}
							} catch (Exception ex) {
								ex.printStackTrace();
							}
							HINT_REQ hreq = new HINT_REQ(GameMessageFactory.nextSequnceNum(), (byte) 5, description);
							player.addMessageToRightBag(hreq);
							QUERY_ARTICLE_INFO_RES qres = new QUERY_ARTICLE_INFO_RES(GameMessageFactory.nextSequnceNum(), me.getId(), me.getInfoShow(player));
							player.addMessageToRightBag(qres);
							PLAY_ANIMATION_REQ pareq = AnimationManager.组织播放动画协议(AnimationManager.装备升级成功, (byte) 1, AnimationManager.动画播放位置类型_武器强化中间孔, 0, 0);
							if (pareq != null) {
								player.addMessageToRightBag(pareq);
							}
							boolean canStrong = false;
							if(me.getMstar()<MagicWeaponConstant.starMaxValue){
								canStrong = true;
							}
							MAGICWEAPON_STRONG_RES res2 = new MAGICWEAPON_STRONG_RES(req.getSequnceNum(), me.getId(), (short)me.getMstar(), canStrong);
							player.addMessageToRightBag(res2);
//							QUERY_ARTICLE_RES res = new QUERY_ARTICLE_RES(GameMessageFactory.nextSequnceNum(), new com.fy.engineserver.datasource.article.entity.client.ArticleEntity[0], new com.fy.engineserver.datasource.article.entity.client.PropsEntity[0], new com.fy.engineserver.datasource.article.entity.client.EquipmentEntity[] { CoreSubSystem.translate(ae) });
//							player.addMessageToRightBag(res);
							logger.warn("[法宝强化确认] [炼星成功。。。。。。。。。。。。。。。。。。] [" + me.getArticleName() + "] [" + me.getMstar() + "] [CoreSubSystem.translate(ee):" + CoreSubSystem.translate(me).getIconId() + "]");
							player.successStrongStarCount = player.successStrongStarCount + 1;
							player.failStrongStarCount = 0;
							if (AchievementManager.getInstance() != null) {
//								AchievementManager.getInstance().record(player, RecordAction.连续炼星成功次数, player.successStrongStarCount);
//								AchievementManager.getInstance().record(player, RecordAction.全身装备最大星级, me.getMstar());
							}
						} else {
							if (20 <= me.getMstar() && me.getMstar() < 26) {
								String description = Translate.很遗憾法宝羽化失败;
								try {
									description = Translate.translateString(Translate.装备强化失败, new String[][] { { Translate.ARTICLE_NAME_1, me.getArticleName() }, new String[] { Translate.LEVEL_1, me.getMstar() + "" } });
									if (me.getMstar() > 20) {
										description = Translate.translateString(Translate.装备羽化失败, new String[][] { { Translate.ARTICLE_NAME_1, me.getArticleName() }, new String[] { Translate.STRING_1, ArticleManager.获得对应的羽化描述.get(new Integer(me.getMstar())) } });
									}
								} catch (Exception ex) {
									ex.printStackTrace();
								}
								HINT_REQ hreq = new HINT_REQ(GameMessageFactory.nextSequnceNum(), (byte) 5, description);
								player.addMessageToRightBag(hreq);
								PLAY_ANIMATION_REQ pareq = AnimationManager.组织播放动画协议(AnimationManager.装备升级失败, (byte) 1, AnimationManager.动画播放位置类型_武器强化中间孔, 0, 0);
								if (pareq != null) {
									player.addMessageToRightBag(pareq);
								}
								logger.error("[炼星20星] [不扣星] [name:" + player.getName() + "] [ee.getStar():" + me.getMstar() + "] [description:" + description + "] [=======]");
								return;
							}
							// 强化失败降级
							int dropStar = ArticleManager.getInstance().强化失败后降级();
							if (me.getMstar() <= dropStar) {
								me.setMstar(0);
							} else {
								me.setMstar(me.getMstar()-dropStar);
							}
							String description = Translate.很遗憾法宝升级失败;
							try {
								description = Translate.translateString(Translate.装备强化失败, new String[][] { { Translate.ARTICLE_NAME_1, me.getArticleName() }, new String[] { Translate.LEVEL_1, me.getMstar() + "" } });
								if (me.getMstar() > 20) {
									description = Translate.translateString(Translate.装备羽化失败, new String[][] { { Translate.ARTICLE_NAME_1, me.getArticleName() }, new String[] { Translate.STRING_1, ArticleManager.获得对应的羽化描述.get(new Integer(me.getMstar())) } });
								}
							} catch (Exception ex) {
								ex.printStackTrace();
							}
							HINT_REQ hreq = new HINT_REQ(GameMessageFactory.nextSequnceNum(), (byte) 5, description);
							player.addMessageToRightBag(hreq);
							PLAY_ANIMATION_REQ pareq = AnimationManager.组织播放动画协议(AnimationManager.装备升级失败, (byte) 1, AnimationManager.动画播放位置类型_武器强化中间孔, 0, 0);
							if (pareq != null) {
								player.addMessageToRightBag(pareq);
							}
							boolean canStrong = false;
							if(me.getMstar()<MagicWeaponConstant.starMaxValue){
								canStrong = true;
							}
							MAGICWEAPON_STRONG_RES res2 = new MAGICWEAPON_STRONG_RES(req.getSequnceNum(), me.getId(), (short)me.getMstar(), canStrong);
							player.addMessageToRightBag(res2);
							player.successStrongStarCount = 0;
							player.failStrongStarCount = player.failStrongStarCount + 1;
							if (AchievementManager.getInstance() != null) {
//								AchievementManager.getInstance().record(player, RecordAction.连续炼星失败次数, player.failStrongStarCount);
							}
						}
						if (bindedTip) {
							me.setBinded(true);
						}
						if (strongType == 0 && !me.isBinded()) {
							me.setBinded(true);
						}
//						QUERY_MAGICWEAPON_STRONG_RES qres2 = new QUERY_MAGICWEAPON_STRONG_RES(GameMessageFactory.nextSequnceNum(), magicweaponId, strongConsumeMoney(me), Translate.空白, new String[]{Translate.法宝强化石}, ArticleManager.getInstance().根据装备强化等级得到不同颜色强化符的成功率分子值_new(me.getMstar()),"",1,1);
//						player.addMessageToRightBag(qres2);
//						NEW_EQUIPMENT_STRONG_RES res = new NEW_EQUIPMENT_STRONG_RES(seqNum, magicweaponId, (short) me.getMstar(), canStrong);
//						player.addMessageToRightBag(res);
//						NOTIFY_EQUIPMENT_CHANGE_REQ nreq = new NOTIFY_EQUIPMENT_CHANGE_REQ(GameMessageFactory.nextSequnceNum(), new com.fy.engineserver.datasource.article.entity.client.EquipmentEntity[] { CoreSubSystem.translate(ee) });
//						player.addMessageToRightBag(nreq);
						if (logger.isWarnEnabled()) logger.warn("[法宝强化确认] [成功] [{}] [{}] [{}] [{}] [{}] [oldstar:{}] [newstar:{}] [totle:{}] [random:{}] [{}ms]", new Object[] { player.getUsername(), player.getId(), player.getName(), (ae != null ? ae.getArticleName() : Translate.空白), (ae != null ? ae.getId() : Translate.空白), oldStar, me.getMstar(), totalLuckValue, resultValue, com.fy.engineserver.gametime.SystemTime.currentTimeMillis() - start });
						return;
					} else {
						String description = Translate.请放入正确物品;
						try {
							description = Translate.translateString(Translate.背包中没有该物品, new String[][] { { Translate.ARTICLE_NAME_1, (ae != null ? ae.getArticleName() : Translate.该物品) } });
						} catch (Exception ex) {
							ex.printStackTrace();
						}
						HINT_REQ hreq = new HINT_REQ(GameMessageFactory.nextSequnceNum(), (byte) 0, description);
						player.addMessageToRightBag(hreq);
						if (logger.isWarnEnabled()) logger.warn("[法宝强化确认] [失败] [{}] [{}] [{}] [{}] [{}] [{}] [{}ms]", new Object[] { player.getUsername(), player.getId(), player.getName(), (ae != null ? ae.getArticleName() : Translate.空白), (ae != null ? ae.getId() : Translate.空白), description, com.fy.engineserver.gametime.SystemTime.currentTimeMillis() - start });
						return;
					}
				} else {
					String description = Translate.法宝已经强化到了上限;
					try {
						description = Translate.translateString(Translate.装备已经强化到了顶级, new String[][] { { Translate.ARTICLE_NAME_1, ae.getArticleName() } });
					} catch (Exception ex) {
						ex.printStackTrace();
					}
					HINT_REQ hreq = new HINT_REQ(GameMessageFactory.nextSequnceNum(), (byte) 0, description);
					player.addMessageToRightBag(hreq);
					if (logger.isWarnEnabled()) logger.warn("[法宝强化确认] [失败] [{}] [{}] [{}] [{}] [{}] [{}] [{}ms]", new Object[] { player.getUsername(), player.getId(), player.getName(), (ae != null ? ae.getArticleName() : Translate.空白), (ae != null ? ae.getId() : Translate.空白), description, com.fy.engineserver.gametime.SystemTime.currentTimeMillis() - start });
				}
			} else {
				String description = Translate.空白;
				description = Translate.请放入法宝;
				HINT_REQ hreq = new HINT_REQ(GameMessageFactory.nextSequnceNum(), (byte) 0, description);
				player.addMessageToRightBag(hreq);
				if (logger.isWarnEnabled()) logger.warn("[法宝强化确认] [失败] [{}] [{}] [{}] [{}] [{}] [{}] [{}ms]", new Object[] { player.getUsername(), player.getId(), player.getName(), (ae != null ? ae.getArticleName() : Translate.空白), (ae != null ? ae.getId() : Translate.空白), description, com.fy.engineserver.gametime.SystemTime.currentTimeMillis() - start });
			}
		}
	}
	
	/**
	 * 法宝吞噬请求
	 * @param player
	 * @param req
	 * @return
	 */
	public QUERY_MAGICWEAPON_EAT_RES queryMagicWeaponEat(Player player , QUERY_MAGICWEAPON_EAT_REQ req){
		if(req == null || player == null){
			logger.warn("["+this.getClass().getSimpleName()+"] [queryMagicWeaponEat] [error] [req:"+(req==null?"reqisnull":req.getTypeDescription())+"] [player:"+(player==null?"playerisnull":player.getLogString())+"] [req or player error]");
			return null;
		}
		
		long ids [] = req.getIds();
		if(ids!=null){
			if(ids.length<1){
				player.sendError(Translate.吞噬法宝不存在);
				return null;
			}
			long mainId = ids[0];
			if(mainId>0){
				ArticleEntity ae = DefaultArticleEntityManager.getInstance().getEntity(mainId);
				if(ae!=null && ae instanceof NewMagicWeaponEntity){
					NewMagicWeaponEntity me = (NewMagicWeaponEntity)ae;
					StringBuffer sb2 = new StringBuffer();
					MagicWeaponAttrModel[] basiAttr = me.getBasiAttr();
					if(basiAttr==null || basiAttr.length<=0){
						player.sendError(Translate.不能吞噬);
						return null;
					}
					
					String costHeadMess[] = new String[basiAttr.length]; 
					long basicNums [] = new long[basiAttr.length]; 
					long addNums [] = new long[basiAttr.length]; 
					
					if(basiAttr!=null && basiAttr.length>0){
						for(int i=0;i<basiAttr.length;i++) {
							MagicWeaponAttrModel attr = basiAttr[i];
							if(attr != null) {
								costHeadMess[i] = MagicWeaponConstant.mappingKeyValue.get(attr.getId());
								basicNums[i] = attr.getAttrNum();
							} 
						}
					}
					
					long addexps = 0;
					long aexps = me.getMagicWeaponExp();
					long aexps2 = me.getMagicWeaponExp();
					
					int maxlevel = getMaxP(me.getColorType());
					long upgradeexps = 0;
					
					for(int i=me.getMcolorlevel()+1;i<maxlevel;i++){
						upgradeexps+=getUpgradeExps(i,me.getColorType());
					}
					
					long upgradeSilvers = getUpgradeSilvers(me.getMcolorlevel()+1,me.getColorType());
					
					upgradeSilvers = upgradeSilvers*(ids.length-1);
					
					List<Prop4MagicWeaponEat> list = new ArrayList<Prop4MagicWeaponEat>();
					
					for(int i=1;i<ids.length;i++){
						ArticleEntity aee = DefaultArticleEntityManager.getInstance().getEntity(ids[i]);
						Article a = ArticleManager.getInstance().getArticle(aee.getArticleName());
						
						if(a!=null && a instanceof Prop4MagicWeaponEat){
							if(aee instanceof NewMagicWeaponEntity) {
								addexps += addExps((NewMagicWeaponEntity)aee,player);
								aexps+= addExps((NewMagicWeaponEntity)aee,player);
								aexps2+= addExps((NewMagicWeaponEntity)aee,player);
								list.add((Prop4MagicWeaponEat)a);
							} else {
								addexps += ((Prop4MagicWeaponEat) a).getAddExp(player);
								aexps+= ((Prop4MagicWeaponEat) a).getAddExp(player);
								aexps2+= ((Prop4MagicWeaponEat) a).getAddExp(player);
								list.add((Prop4MagicWeaponEat)a);
							}
						}
					}
					
					int oldlevel = me.getMcolorlevel();
					long exp = upgradeexps-aexps2;
					long upexp = getUpgradeExps(oldlevel+1,me.getColorType());
					if(exp<0){
						exp = 0;
					}
					
					if(me.getMcolorlevel()>=getMaxP(me.getColorType())){
						aexps2 = getUpgradeExps(oldlevel,me.getColorType());;
					}
					
					if(devourModelMap!=null && devourModelMap.size()>0){
//						sb2.append(Translate.消耗+"：\n");
						sb2.append("<f color='0xFFFFFF'>").append(Translate.消耗经验).append(":").append(addexps).append("</f>\n");
						sb2.append("<f color='0xFFFFFF'>").append(Translate.升级所需经验).append(":").append(exp).append("</f>\n");
						sb2.append("<f color='0xFFFFFF'>").append(Translate.银两消耗).append(":").append((upgradeSilvers/1000)>0?upgradeSilvers/1000+Translate.两:"").append(upgradeSilvers%1000+Translate.文).append("</f>\n");
					}
					
					
					int level = me.getMcolorlevel();
					
					while(aexps>0){
						long nextUpgrageExp = getUpgradeExps(level+1,me.getColorType());
						if(nextUpgrageExp>0 && aexps-nextUpgrageExp>0){
							aexps-=nextUpgrageExp;
							level++;
						}else{
							break;
						}
					}
					
					//
					int temp = level - oldlevel;
					if(temp>0) {
						if(basiAttr != null && basiAttr.length > 0) {
							for(int i=0; i<basiAttr.length; i++) {
								if(basiAttr[i] != null) {
									try {
										addNums[i] = MagicWeaponManager.instance.getDevelAttrNum(basiAttr[i].getAttrNum(), me.getColorType(), level, oldlevel) - basiAttr[i].getAttrNum();
									} catch (Exception e) {
										// TODO Auto-generated catch block
										e.printStackTrace();
									}
								} 
							}
						} 
					}
					
					String currjiejimess = getJieJiMess(me.getId(),oldlevel,me.getColorType());
					String nextjiejimess = getJieJiMess(me.getId(),oldlevel,me.getColorType());
					
					if(list.size()>0 && addexps>0){
						nextjiejimess = getJieJiMess(me.getId(),level,me.getColorType());
					}
					
					if(nextjiejimess.equals(currjiejimess)==false){
						nextjiejimess = "<f color='0xff00ff00'>"+nextjiejimess+"</f>";
					}

					//升最大级剩余经验 = 升最大级所需经验 - 已有经验 - 增量 ;  经验对比
					logger.warn("[法宝吞噬请求] ["+Arrays.toString(costHeadMess)+"] [mcolorlevel:"+me.getMcolorlevel()+"] ["+Arrays.toString(basicNums)+"] ["+Arrays.toString(addNums)+"] [ids:"+ids.length+"] [currjiejimess:"+currjiejimess+"] [nextjiejimess:"+nextjiejimess+"] [等级:"+oldlevel+"-->"+level+"] [me.getColorType():"+me.getColorType()+"] [upgradeexps:"+upgradeexps+"] [addexps:"+addexps+" + hasexp:"+me.getMagicWeaponExp()+"] [exp1:"+upexp+"--exp2:"+aexps2+"] [upgradeSilvers:"+upgradeSilvers+"] ["+player.getLogString()+"]");
					QUERY_MAGICWEAPON_EAT_RES res = new QUERY_MAGICWEAPON_EAT_RES(req.getSequnceNum(), sb2.toString(), costHeadMess,basicNums,addNums, new String[]{currjiejimess,nextjiejimess},aexps2, upexp);
					
					//
					return res;
				}else{
					player.sendError(Translate.请放入法宝);
				}
			}else{
				player.sendError(Translate.背包中没有该法宝);
				logger.warn("[法宝吞噬请求] [出错] [mainid<=0]");
			}
		}else{
			logger.warn("[法宝吞噬请求] [出错] [吞噬id集为空] [ids:"+(ids==null?"null":ids.length)+"] ["+player.getLogString()+"] ");
		}
		return null;
	}
	
	public long getUpgradeExps(int level,int color){
		if(level>=80){
			level = 80;
		}
		long upgradeexps = 0;
		DevourModel dm = devourModelMap.get(level);
		if(dm!=null){
			List<Color4DevourModel> colorList = dm.getColorList();
			if(colorList!=null && colorList.size()>0){
				Color4DevourModel cModel = colorList.get(color);//检查一下是否会越界
				if(cModel!=null){
					upgradeexps = cModel.getExp4Levelup();
				}
			}
		}
		return upgradeexps;
	}
	
	public long getUpgradeSilvers(int level,int color){
		int lev = level;
		long costsilver = 0;
		if(color==0){
			if(lev>=10){
				lev = 10;
			}
		}else if(color==1){
			if(lev>=15){
				lev = 15;
			}
		}else if(color==2){
			if(lev>=20){
				lev = 20;
			}
		}else if(color==3){
			if(lev>=25){
				lev = 25;
			}
		}else if(color==4){
			if(lev>=30){
				lev = 30;
			}
		}else if(color==5){
			if(lev>=35){
				lev = 35;
			}
		}
		
		DevourModel dm = devourModelMap.get(lev);
		if(dm!=null){
			List<Color4DevourModel> colorList = dm.getColorList();
			if(colorList!=null && colorList.size()>0){
				Color4DevourModel cModel = colorList.get(color);//检查一下是否会越界
				if(cModel!=null){
					costsilver = cModel.getCostSiliver();
				}
			}
		}
		
		return costsilver;
	}
	/**
	 * 法宝突破吞噬等级
	 * @param player
	 * @param mwId
	 * @return
	 */
	public boolean magicWeaponEatBreak(Player player, long mwId) {
		if (!法宝阶飞升是否开启) {
			return false;
		}
		if (mwId <= 0) {
			logger.warn("[法宝突破吞噬等级] [失败] [法宝ID错误:" + mwId + "] [" + player.getLogString() + "]");
			return false;
		}
		ArticleEntity ae = ArticleEntityManager.getInstance().getEntity(mwId);
		if (ae instanceof NewMagicWeaponEntity) {
			NewMagicWeaponEntity mw = (NewMagicWeaponEntity) ae;
			if (mw.getColorType() < 3) {
				player.sendError(Translate.紫色以上法宝才能操作);
				return false;
			}
			MagicWeaponColorUpModule module = colorUpMaps.get(mw.getColorType());
			if (module == null) {
				logger.warn("[法宝突破吞噬等级] [失败] [颜色模板不存在:" + mwId + "] [" + player.getLogString() + "] [颜色:" + mw.getColorType() + "]");
				return false;
			}
			String aeCnnName = "";
			int costNum = 0;
			int prob = 0;
			for (int i=0; i<module.getMagicWeaponLvs().length; i++) {
				if (mw.getMcolorlevel() == module.getMagicWeaponLvs()[i]) {
					aeCnnName = module.getArticleCNNNames()[i];
					costNum = module.getCostNums()[i];
					prob = module.getProbabblys()[i];
					break;
				}
			}
			if (aeCnnName == null || aeCnnName.isEmpty() || costNum <= 0) {
				logger.warn("[法宝突破吞噬等级] [失败] [没找到对应法宝等级消耗] [" + player.getLogString() + "] [" + mwId + "] [" + mw.getMcolorlevel() + "]");
				return false;
			}
			Article a = ArticleManager.getInstance().getArticleByCNname(aeCnnName);
			Knapsack bag = player.getKnapsack_common();
			int count = bag.getArticleCellCount(a.getName());
			if (count < costNum) {
				player.sendError(Translate.物品个数不足);
				logger.warn("[法宝突破吞噬等级] [失败] [背包物品不足] [" + player.getLogString() + "] [" + mwId + "] [" + mw.getMcolorlevel() + "]");
				return false;
			}
			for (int i=0; i<costNum; i++) {
				ArticleEntity aa = player.removeArticle(a.getName(), "法宝突破吞噬等级", "");
				if (aa == null) {
					logger.warn("[法宝突破吞噬等级] [失败] [删除道具失败] [已删除数量:" + i + "] [" + player.getLogString() + "] [" + mwId + "] [" + mw.getMcolorlevel() + "]");
					return false;
				}
			}
			int ran = player.random.nextInt(10000);
			if (ran <= prob) {
				int oldLv = mw.getMcolorlevel();
				mw.setMcolorlevel(mw.getMcolorlevel() + 1);
				mw.saveAutoAddBasicData(oldLv);
				logger.warn("[法宝突破吞噬等级] [成功] [" + mw.getId() + "] [" + player.getLogString() + "]");
				return true;
			}
			logger.warn("[法宝突破吞噬等级] [失败] [随机数:" + ran + "] [概率:" + prob + "] [" + mw.getId() + "] [" + player.getLogString() + "]");
			return false;
 		} else {
			logger.warn("[法宝突破吞噬等级] [失败] [传入id对应物品不是法宝:" + mwId + "] [" + player.getLogString() + "]");
		}
		return false;
	}
	
	public static boolean 法宝阶飞升是否开启 = false;
	
	/**
	 * 确认吞噬
	 * @param player
	 * @param req
	 */
	public CONFIRM_MAGICWEAPON_EAT_RES confirmMagicWeaponEat(Player player , CONFIRM_MAGICWEAPON_EAT_REQ req,boolean issure){
		if(req == null || player == null){
			logger.warn("["+this.getClass().getSimpleName()+"] [confirmMagicWeaponEat] [error] [req:"+(req==null?"reqisnull":req.getTypeDescription())+"] [player:"+(player==null?"playerisnull":player.getLogString())+"] [req or player error]");
			return new CONFIRM_MAGICWEAPON_EAT_RES(req.getSequnceNum(), false,new TunShiModle[0]);
		}
		
		long ids[] = req.getIds();
		
		long magicweaponId = 0;
		List<Long> materialids = new ArrayList<Long>();
		if(ids!=null && ids.length>0){
			magicweaponId = ids[0];
			for(int i=1,len=ids.length;i<len;i++){
				if(ids[i]>0){
					materialids.add(ids[i]);
				}
			}
		}
		
		if(magicweaponId<=0){
			player.sendError(Translate.吞噬法宝不存在);
			return new CONFIRM_MAGICWEAPON_EAT_RES(req.getSequnceNum(), false,new TunShiModle[0]);
		}
		
		if(materialids.size()==0){
			player.sendError(Translate.被吞噬法宝不存在);
			return new CONFIRM_MAGICWEAPON_EAT_RES(req.getSequnceNum(), false,new TunShiModle[0]);
		}
		
		ArticleEntity ae = DefaultArticleEntityManager.getInstance().getEntity(magicweaponId);
		if(ae==null){
			player.sendError(Translate.吞噬法宝不存在);
			return new CONFIRM_MAGICWEAPON_EAT_RES(req.getSequnceNum(), false,new TunShiModle[0]);
		}
		
		if(ae instanceof NewMagicWeaponEntity == false){
			player.sendError(Translate.请放入法宝);
			return new CONFIRM_MAGICWEAPON_EAT_RES(req.getSequnceNum(), false,new TunShiModle[0]);
		}
		
		NewMagicWeaponEntity me = (NewMagicWeaponEntity)ae;
		Knapsack knapsack = player.getKnapsack_common();
		
		MagicWeaponAttrModel[] basiAttr = me.getBasiAttr();
		if(basiAttr==null || basiAttr.length<=0){
			player.sendError(Translate.未神识探查);
			return new CONFIRM_MAGICWEAPON_EAT_RES(req.getSequnceNum(), false,new TunShiModle[0]);
		}
		
		int maxlv = getMaxP(me.getColorType());
		if(me.getMcolorlevel()>=maxlv){
			player.sendError(Translate.已经吞噬最大);
			return new CONFIRM_MAGICWEAPON_EAT_RES(req.getSequnceNum(), false,new TunShiModle[0]); 
		}
		if (法宝阶飞升是否开启) {
			String needAeName = this.needSpecialProp(me.getColorType(), me.getMcolorlevel());
			if (needAeName != null) {
				player.sendError(Translate.需要法宝进阶后才能继续);
				return new CONFIRM_MAGICWEAPON_EAT_RES(req.getSequnceNum(), false,new TunShiModle[0]); 
			}
		}
		
		if(knapsack!=null){
			int index1 = knapsack.hasArticleEntityByArticleId(magicweaponId);
			if (index1 == -1) {
				player.sendError(Translate.背包中没有该法宝);
				return new CONFIRM_MAGICWEAPON_EAT_RES(req.getSequnceNum(), false,new TunShiModle[0]);
			}
			
			for(int i=0;i<materialids.size();i++){
				int index2 = knapsack.hasArticleEntityByArticleId(magicweaponId);
				if (index2 == -1) {
					player.sendError(Translate.背包中没有该法宝+Translate.text_3788);
					return new CONFIRM_MAGICWEAPON_EAT_RES(req.getSequnceNum(), false,new TunShiModle[0]);
				}
			}
			if (!ArticleProtectManager.instance.isCanDo(player, ArticleProtectDataValues.ArticleProtect_High, magicweaponId)) {
				player.sendError(Translate.高级锁魂物品不能做此操作);
				return new CONFIRM_MAGICWEAPON_EAT_RES(req.getSequnceNum(), false,new TunShiModle[0]);
			}
			
			for(long id:materialids){
				ArticleEntity ace = DefaultArticleEntityManager.getInstance().getEntity(id);
				Article a = ArticleManager.getInstance().getArticle(ace.getArticleName());
				
				if(!(a instanceof Prop4MagicWeaponEat)) {
					player.sendError(Translate.放入吞噬材料不对);
					return new CONFIRM_MAGICWEAPON_EAT_RES(req.getSequnceNum(), false, new TunShiModle[0]);
				}
				if(ace instanceof NewMagicWeaponEntity) {
					NewMagicWeaponEntity nm = (NewMagicWeaponEntity) ace;
					if (!ArticleProtectManager.instance.isCanDo(player, ArticleProtectDataValues.ArticleProtect_High, nm.getId())) {
						player.sendError(Translate.高级锁魂物品不能做此操作);
						return new CONFIRM_MAGICWEAPON_EAT_RES(req.getSequnceNum(), false,new TunShiModle[0]);
					}
					if(nm!=null){
						if(nm.getColorType()>=2 && !issure){
							MenuWindow mw=WindowManager.getInstance().createTempMenuWindow(300);
							mw.setDescriptionInUUB(Translate.是否要吞噬高颜色的法宝);
							Option_MagicWeapon_Eat_Sure sure = new Option_MagicWeapon_Eat_Sure();
							sure.setText(Translate.text_125);
							sure.setReq(req);
							Option_Cancel oc=new Option_Cancel();
							oc.setText(Translate.text_126);
							oc.setColor(0xffffff);
							mw.setOptions(new Option[]{sure,oc});
							
							QUERY_WINDOW_RES res = new QUERY_WINDOW_RES(GameMessageFactory.nextSequnceNum(),mw,mw.getOptions());
							player.addMessageToRightBag(res);
							return new CONFIRM_MAGICWEAPON_EAT_RES(req.getSequnceNum(), false,new TunShiModle[0]);
						}
					}
				}
			}
			
				long needcostsiver = getUpgradeSilvers(me.getMcolorlevel()+1, me.getColorType());
				needcostsiver = needcostsiver*materialids.size();
				if(needcostsiver<=0){
					logger.warn("[法宝吞噬] [消耗银子配置错误] [needcostsiver:"+needcostsiver+"] [name:"+me.getArticleName()+"] [color:"+me.getColorType()+"] ["+player.getLogString()+"]");
					return new CONFIRM_MAGICWEAPON_EAT_RES(req.getSequnceNum(), false,new TunShiModle[0]);
				}
				
				if(player.getSilver()+player.getShopSilver() < needcostsiver){
					player.sendError(Translate.余额不足);
					return new CONFIRM_MAGICWEAPON_EAT_RES(req.getSequnceNum(), false,new TunShiModle[0]);
				}
				
				long addexps = me.getMagicWeaponExp();
				for(int i=0;i<materialids.size();i++){
					ArticleEntity aee = DefaultArticleEntityManager.getInstance().getEntity(materialids.get(i));
					Article a = ArticleManager.getInstance().getArticle(aee.getArticleName());
					
					if(a!=null && a instanceof Prop4MagicWeaponEat){
						if(aee instanceof NewMagicWeaponEntity) {
							addexps += addExps((NewMagicWeaponEntity)aee,player);
						} else {
							addexps += ((Prop4MagicWeaponEat) a).getAddExp(player);
						}
					}
				}
				
				if(addexps<=0){
					player.sendError(Translate.吞噬失败);
					logger.warn("[法宝吞噬] [addexps<=0] [materialids:"+materialids.size()+"] ["+player.getLogString()+"]");
					return new CONFIRM_MAGICWEAPON_EAT_RES(req.getSequnceNum(), false,new TunShiModle[0]);
				}
				
				//扣费
				for (int j = 0; j < materialids.size(); j++) {
					ArticleEntity aee1 = player.removeArticleEntityFromKnapsackByArticleId(materialids.get(j), "法宝吞噬删除", true);
					if (aee1 == null) {
						String description = Translate.删除物品不成功;
						HINT_REQ hreq = new HINT_REQ(GameMessageFactory.nextSequnceNum(), (byte) 0, description);
						player.addMessageToRightBag(hreq);
						if (logger.isWarnEnabled()) logger.warn("[法宝吞噬扣费] [扣除物品] [id:" + materialids.get(j) + "] [成功] [{}] [{}] [{}] [{}] [{}] [{}]", new Object[] { player.getUsername(), player.getId(), player.getName(), (ae != null ? ae.getArticleName() : Translate.空白), (ae != null ? ae.getId() : Translate.空白), description});
						return new CONFIRM_MAGICWEAPON_EAT_RES(req.getSequnceNum(), false,new TunShiModle[0]);
					} else {
						// 统计
						ArticleStatManager.addToArticleStat(player, null, aee1, ArticleStatManager.OPERATION_物品获得和消耗, 0, ArticleStatManager.YINZI, 1, "法宝吞噬删除", null);
					}
				}
				
				try {
					BillingCenter bc = BillingCenter.getInstance();
					bc.playerExpense(player, needcostsiver, CurrencyType.SHOPYINZI, ExpenseReasonType.法宝吞噬, "法宝吞噬扣去手续费");
					if (logger.isWarnEnabled()) logger.warn("[法宝吞噬扣费] [扣除银子] [成功] [needcostsiver:{}] [{}] [{}] [{}] [{}] [{}] [{}]", new Object[] { needcostsiver,player.getUsername(), player.getId(), player.getName(), (ae != null ? ae.getArticleName() : Translate.空白), (ae != null ? ae.getId() : Translate.空白), "法宝吞噬扣去手续费"});
				} catch (Exception ex) {
					ex.printStackTrace();
					if (logger.isWarnEnabled()) logger.warn("[法宝吞噬扣费] [失败] [" + player.getUsername() + "] [" + player.getId() + "] [" + player.getName() + "] [" + (ae != null ? ae.getArticleName() : Translate.空白) + "] [" + (ae != null ? ae.getId() : Translate.空白) + "]", ex);
					return new CONFIRM_MAGICWEAPON_EAT_RES(req.getSequnceNum(), false,new TunShiModle[0]);
				}
				
				CONFIRM_MAGICWEAPON_EAT_RES result = me.addExp(req,player, addexps);
				if(result==null){
					player.sendError(Translate.吞噬失败);
					if (logger.isWarnEnabled()) logger.warn("[法宝吞噬扣费] [添加经验] [失败] [addexps:"+addexps+"] [result:"+result+"] ["+player.getLogString()+"]");
					return new CONFIRM_MAGICWEAPON_EAT_RES(req.getSequnceNum(), false,new TunShiModle[0]);
				}
				
				logger.warn("[法宝吞噬] [吞噬成功] [result:"+(result==null?"null":result.getTypeDescription()+"--"+result.getType())+"] [法宝id:"+me.getId()+"] [level:"+me.getMcolorlevel()+"] [name:"+me.getArticleName()+"] [addexps:"+addexps+"] [color:"+me.getColorType()+"] ["+player.getLogString()+"]");
				player.sendError(Translate.吞噬成功);
				player.addMessageToRightBag(result);
		}
		return new CONFIRM_MAGICWEAPON_EAT_RES(req.getSequnceNum(), false,new TunShiModle[0]);
	}
	
	public void issure(Player p,CONFIRM_MAGICWEAPON_EAT_REQ req){
		confirmMagicWeaponEat(p, req,true);
	}
	
	/**
	 * 请求隐藏属性
	 * @param player
	 * @param req
	 * @return
	 * @throws Exception 
	 */
	public QUERY_MAGICWEAPON_HIDDLE_PROP_RES queryHiddleProp(Player player,QUERY_MAGICWEAPON_HIDDLE_PROP_REQ req) throws Exception{
		if(req == null || player == null){
			logger.warn("["+this.getClass().getSimpleName()+"] [queryHiddleProp] [error] [req:"+(req==null?"reqisnull":req.getTypeDescription())+"] [player:"+(player==null?"playerisnull":player.getLogString())+"] [req or player error]");
			return null;
		}
		
		long magicweaponId = req.getId();
		
		if(magicweaponId>0){
			ArticleEntity  ae = DefaultArticleEntityManager.getInstance().getEntity(magicweaponId);
			if(ae!=null && ae instanceof NewMagicWeaponEntity){
				NewMagicWeaponEntity me = (NewMagicWeaponEntity)ae;
				MagicWeaponBaseModel mm = mwBaseModel.get(me.getColorType());	
				MagicWeapon mw = (MagicWeapon)ArticleManager.getInstance().getArticle(me.getArticleName());
				if(mm!=null && mw!=null){
					List<String> list = new ArrayList<String>();
					long list2 []= new long[me.getHideproterty().size()];
					long list3 []= new long[me.getHideproterty().size()];
					if(me.getHideproterty()!=null && me.getHideproterty().size()>0){
						HiddenAttrModel hm = hiddenDataMap.get(mw.getDataLevel());
						for(int i=0;i<me.getHideproterty().size();i++){
							MagicWeaponAttrModel mm2 = me.getHideproterty().get(i);
							if(mm2!=null){
								String descString = "";
								MagicWeaponAttrModel attr = me.getHideproterty().get(i);
								if(attr!=null){
									descString = MagicWeaponConstant.mappingKeyValue2.get(attr.getId());
								}
								list3[i] = getHiddenMaxAttr(hm.getAttrNumByType(mm2.getId())[1], me.getColorType(), hm.getAttrNumByType(mm2.getId())[0]);
								list2[i] = mm2.getAttrNum();
								if(descString==null){
									descString = "";
								} else {
									int index = getHiddenAttrColor((int)list2[i], (int)list3[i]);
									String tds = list2[i] + "";
									if(getAddTypeByType(MagicWeaponConstant.hiddenAttr, mm2.getId()) == MagicWeaponConstant.add_typePercent4Person) {
										tds = (float)list2[i] / 10f + "%";
									}
									descString = descString.replace("(", "").replace(")", "").replace("&", "") + ":"  + tds;
									descString = NewMagicWeaponEntity.hiddenColorStart[index] + descString + "</f>";
								}
								list.add(descString);
								logger.warn("[请求隐藏属性] [属性："+descString+"] [值："+mm2.getAttrNum()+"-->"+(hm.getAttrNumByType(mm2.getId())[1])+"]");
							}
						}
					}																
					QUERY_MAGICWEAPON_HIDDLE_PROP_RES res = new QUERY_MAGICWEAPON_HIDDLE_PROP_RES(req.getSequnceNum(), list.toArray(new String[]{}), list2, list3,materialnames,Translate.如果没有天铸符,getJieJiMess(magicweaponId,me.getMcolorlevel(),me.getColorType()),mm.getHiddenAttrNum());
					return res;
				}else{
					player.sendError(Translate.该法宝没有隐藏属性);
					logger.warn("[请求隐藏属性] [错误] [原因：没有激活的隐藏属性] [magicweaponId:"+magicweaponId+"] [req:"+req.getTypeDescription()+"] [player："+player.getLogString()+"]");
				}
			}else{
				logger.warn("[请求隐藏属性] [错误] [原因：ae==null or ae is not magiceweapon] [magicweaponId:"+magicweaponId+"] [req:"+req.getTypeDescription()+"] [player："+player.getLogString()+"]");
			}
		}else{
			player.sendError(Translate.背包中没有该法宝);
			logger.warn("[请求隐藏属性] [错误] [原因：req.getId()<=0] [req:"+req.getTypeDescription()+"] [player："+player.getLogString()+"]");
		}
		
		return null;
	}
	
	/**
	 * 激活隐藏属性
	 * @param p
	 * @param rq
	 * @return
	 * @throws Exception 
	 */
	public JIHUO_MAGICWEAPON_HIDDLE_PROP_RES jihuoMagicWeapon(Player player,JIHUO_MAGICWEAPON_HIDDLE_PROP_REQ req,boolean issure) throws Exception{
		if(req == null || player == null){
			logger.warn("["+this.getClass().getSimpleName()+"] [jihuoMagicWeapon] [error] [req:"+(req==null?"reqisnull":req.getTypeDescription())+"] [player:"+(player==null?"playerisnull":player.getLogString())+"] [req or player error]");
			return null;
		}
		
		long magicweaponId = req.getId();
		
		if(magicweaponId>0){
			
			ArticleEntity  ae = DefaultArticleEntityManager.getInstance().getEntity(magicweaponId);
			if(ae!=null && ae instanceof NewMagicWeaponEntity){
				
				if (!ArticleProtectManager.instance.isCanDo(player, ArticleProtectDataValues.ArticleProtect_High, ae.getId())) {
					player.sendError(Translate.高级锁魂物品不能做此操作);
					return new JIHUO_MAGICWEAPON_HIDDLE_PROP_RES(req.getSequnceNum(), false);
				}
				
				NewMagicWeaponEntity me = (NewMagicWeaponEntity)ae;
				MagicWeapon article = (MagicWeapon) ArticleManager.getInstance().getArticle(me.getArticleName());
				if(article==null){
					player.sendError(Translate.translateString(Translate.物品不存在提示, new String[][]{{Translate.STRING_1,me.getArticleName()}}));
					return new JIHUO_MAGICWEAPON_HIDDLE_PROP_RES(req.getSequnceNum(), false);
				}
				
				MagicWeaponAttrModel[] basiAttr = me.getBasiAttr();
				if(basiAttr==null || basiAttr.length<=0){
					player.sendError(Translate.未神识探查);
					return new JIHUO_MAGICWEAPON_HIDDLE_PROP_RES(req.getSequnceNum(), false);
				}
				
				MagicWeaponAttrModel mwa = getHiddenAttr(player, article.getDataLevel(), me.getColorType());
				List<MagicWeaponAttrModel> tempH = me.getHideproterty();
				int index = tempH.size();
				if(index>=MagicWeaponConstant.刷新隐藏属性激活所需费用.length){
					index = MagicWeaponConstant.刷新隐藏属性激活所需费用.length-1;
				}
				int needcostsilver = MagicWeaponConstant.刷新隐藏属性激活所需费用[index];
				
				if(player.getSilver() + player.getShopSilver()<needcostsilver){
					player.sendError(Translate.余额不足);
					return new JIHUO_MAGICWEAPON_HIDDLE_PROP_RES(req.getSequnceNum(), false);
				}
				
				try {
					MagicWeaponBaseModel mbm = MagicWeaponManager.instance.mwBaseModel.get(me.getColorType());
					if(mbm != null && mbm.getHiddenAttrNum() > tempH.size()) {
						if(issure){
							BillingCenter bc = BillingCenter.getInstance();
							bc.playerExpense(player, needcostsilver, CurrencyType.SHOPYINZI, ExpenseReasonType.EQUIPMENT_UPGRADE, "激活隐藏属性费用");
							if (logger.isWarnEnabled()) logger.warn("[激活隐藏属性扣费] [扣除银子] [成功] [{}] [{}] [{}] [{}] [{}] [{}] [{}]", new Object[] { player.getUsername(), player.getId(), player.getName(), (ae != null ? ae.getArticleName() : Translate.空白), (ae != null ? ae.getId() : Translate.空白), "法宝激活隐藏属性扣去手续费",needcostsilver});
							tempH.add(mwa);
							me.setHideproterty(tempH);
							player.sendError(Translate.激活成功);
							me.initHiddenAttr();
							this.recordMagicOpt(player, RecordAction.激活法宝隐藏属性, article.getLevellimit());
							return new JIHUO_MAGICWEAPON_HIDDLE_PROP_RES(req.getSequnceNum(), true);
						}else{
							MenuWindow mw=WindowManager.getInstance().createTempMenuWindow(300);
							mw.setDescriptionInUUB(Translate.translateString(Translate.您是否要花费钱激活属性, new String[][]{{Translate.STRING_1,(int)(needcostsilver/1000)+""}}));
							Option_MagicWeapon_Eat_Sure sure = new Option_MagicWeapon_Eat_Sure();
							sure.setText(Translate.text_125);
							sure.setReq2(req);
							sure.setType(1);
							Option_Cancel oc=new Option_Cancel();
							oc.setText(Translate.text_126);
							oc.setColor(0xffffff);
							mw.setOptions(new Option[]{sure,oc});
							
							QUERY_WINDOW_RES res = new QUERY_WINDOW_RES(GameMessageFactory.nextSequnceNum(),mw,mw.getOptions());
							player.addMessageToRightBag(res);
						}
					}else{
						player.sendError(Translate.已达激活上限);
					}
				} catch (Exception ex) {
					ex.printStackTrace();
					if (logger.isWarnEnabled()) logger.warn("[激活隐藏属性扣费] [失败] [" + player.getUsername() + "] [" + player.getId() + "] [" + player.getName() + "] [" + (ae != null ? ae.getArticleName() : Translate.空白) + "] [" + (ae != null ? ae.getId() : Translate.空白) + "]", ex);
					return new JIHUO_MAGICWEAPON_HIDDLE_PROP_RES(req.getSequnceNum(), false);
				}
			}else{
				player.sendError(Translate.请放入法宝);
			}
		}else{
			player.sendError(Translate.背包中没有该法宝);
		}
		
		return new JIHUO_MAGICWEAPON_HIDDLE_PROP_RES(req.getSequnceNum(), false);
	}
	
	/**
	 * 刷新隐藏属性
	 * @param player
	 * @param req
	 * @return
	 * @throws Exception 
	 */
	public REFRESH_MAGICWEAPON_HIDDLE_PROP_RES refrshMagicWeaponProp(Player player,REFRESH_MAGICWEAPON_HIDDLE_PROP_REQ req,boolean issure) throws Exception{
		if(req == null || player == null){
			logger.warn("["+this.getClass().getSimpleName()+"] [refrshMagicWeaponProp] [error] [req:"+(req==null?"reqisnull":req.getTypeDescription())+"] [player:"+(player==null?"playerisnull":player.getLogString())+"] [req or player error]");
			return null;
		}
		
		long magicweaponId = req.getId();
		int [] indexs = req.getIndexs();
		long  materialids = req.getMaterialids();
		int usetype = req.getMaterialtype();//0:重铸符；1:天铸符；2:神铸符
		int usesilver = req.getUsesilver();
		
		if(magicweaponId>0){
			ArticleEntity  ae = DefaultArticleEntityManager.getInstance().getEntity(magicweaponId);
			if(ae!=null && ae instanceof NewMagicWeaponEntity){
				if (!ArticleProtectManager.instance.isCanDo(player, ArticleProtectDataValues.ArticleProtect_High, ae.getId())) {
					player.sendError(Translate.高级锁魂物品不能做此操作);
					return new REFRESH_MAGICWEAPON_HIDDLE_PROP_RES(req.getSequnceNum(), false);
				}
				
				NewMagicWeaponEntity me = (NewMagicWeaponEntity) DefaultArticleEntityManager.getInstance().getEntity(magicweaponId);
				List<MagicWeaponAttrModel> hideproterty = me.getHideproterty();
				
				if(hideproterty==null || hideproterty.size()<=0){
					player.sendError(Translate.请先激活再刷新);
					return new REFRESH_MAGICWEAPON_HIDDLE_PROP_RES(req.getSequnceNum(), false);
				}
				
				if(indexs==null || indexs.length<=0){
					player.sendError(Translate.请先选择要刷新的属性);
					return new REFRESH_MAGICWEAPON_HIDDLE_PROP_RES(req.getSequnceNum(), false);
				}
				
				if(hideproterty.size() < indexs.length){
					logger.warn("[刷新隐藏属性] [错误] [数据不一致] [name:"+ae.getArticleName()+"] ["+hideproterty.size()+"] ["+indexs.length+"] ["+player.getLogString()+"]");
					return new REFRESH_MAGICWEAPON_HIDDLE_PROP_RES(req.getSequnceNum(), false);
				}
				
				boolean playersure = issure;
				if(!playersure && isnotice(indexs,me)){
					MenuWindow mw=WindowManager.getInstance().createTempMenuWindow(300);
					mw.setDescriptionInUUB(Translate.是否刷星高颜色的属性);
					Option_MagicWeapon_Eat_Sure sure = new Option_MagicWeapon_Eat_Sure();
					sure.setText(Translate.text_125);
					sure.setReq3(req);
					Option_Cancel oc=new Option_Cancel();
					oc.setText(Translate.text_126);
					oc.setColor(0xffffff);
					mw.setOptions(new Option[]{sure,oc});
					
					QUERY_WINDOW_RES res = new QUERY_WINDOW_RES(GameMessageFactory.nextSequnceNum(),mw,mw.getOptions());
					player.addMessageToRightBag(res);
					return null;
					
				}	
				
				
				boolean issucc = false;	//扣费成功
				
				if(usesilver==1){	//银子
					if(player.getSilver()+player.getShopSilver()<200000){
						player.sendError(Translate.余额不足);
						return new REFRESH_MAGICWEAPON_HIDDLE_PROP_RES(req.getSequnceNum(), false);
					}
					
					try {
						BillingCenter bc = BillingCenter.getInstance();
						bc.playerExpense(player, 200000, CurrencyType.SHOPYINZI, ExpenseReasonType.法宝激活属性, "法宝隐藏属性刷新");
						issucc = true;
						usetype=1;
						if (logger.isWarnEnabled()) logger.warn("[刷新隐藏属性扣费] [扣除银子] [成功] [{}] [{}] [{}] [{}] [{}] [{}]", new Object[] { player.getUsername(), player.getId(), player.getName(), (ae != null ? ae.getArticleName() : Translate.空白), (ae != null ? ae.getId() : Translate.空白), "法宝隐身属性扣去手续费"});
					} catch (Exception ex) {
						ex.printStackTrace();
						if (logger.isWarnEnabled()) logger.warn("[刷新隐藏属性扣费] [失败] [" + player.getUsername() + "] [" + player.getId() + "] [" + player.getName() + "] [" + (ae != null ? ae.getArticleName() : Translate.空白) + "] [" + (ae != null ? ae.getId() : Translate.空白) + "]", ex);
						return new REFRESH_MAGICWEAPON_HIDDLE_PROP_RES(req.getSequnceNum(), false);
					}
					
				}else{
					if(materialids>0){
						Knapsack knapsack = player.getKnapsack_common();
						
						boolean istmaterial = true;
						boolean isnull = false;
						boolean isexit = true;
						
						ArticleEntity aee = DefaultArticleEntityManager.getInstance().getEntity(materialids);
						if(aee!=null){
							if(!MagicWeaponConstant.刷新隐藏属性所需材料.contains(aee.getArticleName())){
								istmaterial = false;
							}
							int index2 = knapsack.hasArticleEntityByArticleId(materialids);
							if (index2 == -1) {
								isexit = false;
							}
						}else{
							isnull = true;
						}
						
						if(!istmaterial || isnull){
							player.sendError(Translate.请放入隐藏属性材料);
							logger.warn("[刷新隐藏属性] [失败] [materialids:"+materialids+"] [istmaterial:"+istmaterial+"] [isnull:"+isnull+"] ["+player.getName()+"]");
							return new REFRESH_MAGICWEAPON_HIDDLE_PROP_RES(req.getSequnceNum(), false);
						}
						
						if(!isexit){
							player.sendError(Translate.背包没有天铸符);
							return new REFRESH_MAGICWEAPON_HIDDLE_PROP_RES(req.getSequnceNum(), false);
						}
						
						String rightname = MagicWeaponConstant.刷新隐藏属性所需材料.get(usetype);
						if(rightname==null || rightname.equals(aee.getArticleName())==false){
							player.sendError(Translate.translateString(Translate.刷新需要, new String[][] { { Translate.STRING_1, rightname } }));
							return new REFRESH_MAGICWEAPON_HIDDLE_PROP_RES(req.getSequnceNum(), false);
						}
						
						ArticleEntity aee1 = player.removeArticleEntityFromKnapsackByArticleId(aee.getId(), "法宝刷新隐藏属性删除", true);
						if (aee1 == null) {
							String description = Translate.删除物品不成功;
							HINT_REQ hreq = new HINT_REQ(GameMessageFactory.nextSequnceNum(), (byte) 0, description);
							player.addMessageToRightBag(hreq);
							if (logger.isWarnEnabled()) logger.warn("[法宝刷新隐藏属性删除扣费] [扣除物品] [id:" + aee.getId() + "] [成功] [{}] [{}] [{}] [{}] [{}] [{}]", new Object[] { player.getUsername(), player.getId(), player.getName(), (ae != null ? ae.getArticleName() : Translate.空白), (ae != null ? ae.getId() : Translate.空白), description});
							return new REFRESH_MAGICWEAPON_HIDDLE_PROP_RES(req.getSequnceNum(), false);
						} else {
							// 统计
							issucc = true;
							ArticleStatManager.addToArticleStat(player, null, aee1, ArticleStatManager.OPERATION_物品获得和消耗, 0, ArticleStatManager.YINZI, 1, "刷新隐藏属性", null);
							try {
								ArrayList<ArticleEntity> strongMaterialEntitys = new ArrayList<ArticleEntity>();
								strongMaterialEntitys.add(aee1);
								ShopActivityManager.getInstance().noticeUseSuccess(player, strongMaterialEntitys);
							} catch (Exception e) {
								ActivitySubSystem.logger.error("[使用赠送活动] [刷新法宝隐藏属性] [" + player.getLogString() + "]", e);
							}
						}
					}else{
						player.sendError(Translate.请放入隐藏属性材料);
						logger.warn("[刷新隐藏属性] [失败] [materialids:"+materialids+"] [] ["+player.getName()+"]");
					}
				}
				
				if(issucc){
					MagicWeapon article = (MagicWeapon) ArticleManager.getInstance().getArticle(ae.getArticleName());
					if(article==null) {
						logger.warn("[刷新隐藏属性] [失败] ["+(article==null)+"] ["+player.getLogString()+"]");
						return new REFRESH_MAGICWEAPON_HIDDLE_PROP_RES(req.getSequnceNum(), false);
					}
					if(usetype==0){
						List<MagicWeaponAttrModel> list = me.getHideproterty();
						List<MagicWeaponAttrModel> tlist = new ArrayList<MagicWeaponAttrModel>();
						for(int i=0; i<list.size(); i++) {
							MagicWeaponAttrModel m = getHiddenAttr(player, article.getDataLevel(), me.getColorType());
							tlist.add(m);
							if(logger.isDebugEnabled()) {
								logger.debug("[刷新隐藏属性11] [" + player.getLogString() + "] [" + m + "]");
							}
						}
						me.setHideproterty(tlist);
						me.initHiddenAttr();
						this.recordMagicOpt(player, RecordAction.刷新法宝隐藏属性次数, article.getLevellimit());
					}else if(usetype==1){
						
						List<MagicWeaponAttrModel> list = me.getHideproterty();
						for(int i=0; i<list.size(); i++) {
							for(int tt : indexs) {
								if(tt == i) {
									MagicWeaponAttrModel m = getHiddenAttr(player, article.getDataLevel(), me.getColorType());
									list.set(i, m);
									if(logger.isDebugEnabled()) {
										logger.debug("[刷新隐藏属性22] [" + player.getLogString() + "] [" + m + "]");
									}
									break;
								}
							}
						}
						
						me.setHideproterty(list);
						me.initHiddenAttr();
						this.recordMagicOpt(player, RecordAction.刷新法宝隐藏属性次数, article.getLevellimit());
					}else if(usetype==2){
						player.sendError("还为开放");
						return new REFRESH_MAGICWEAPON_HIDDLE_PROP_RES(req.getSequnceNum(), true);
					}
					
					logger.warn("[刷新隐藏属性扣费] [成功] [{}] [{}] [{}] [{}] [{}] [{}] [材料:{}]", new Object[] { player.getUsername(), player.getId(), player.getName(), (ae != null ? ae.getArticleName() : Translate.空白), (ae != null ? ae.getId() : Translate.空白), "刷新隐藏属性",MagicWeaponConstant.刷新隐藏属性所需材料.get(usetype)});
					player.sendError(Translate.属性重铸成功);
					return new REFRESH_MAGICWEAPON_HIDDLE_PROP_RES(req.getSequnceNum(), true);
				}
			}else{
				player.sendError(Translate.请放入法宝);
			}
		}else{
			player.sendError(Translate.请放入法宝);
		}
		return new REFRESH_MAGICWEAPON_HIDDLE_PROP_RES(req.getSequnceNum(), false);
		
	}
	
	public boolean isnotice(int[] indexs,NewMagicWeaponEntity me){
		MagicWeaponBaseModel mbm = mwBaseModel.get(me.getColorType());
		if(mbm != null && mbm.getHiddenAttrNum() > 0) {
			MagicWeapon mw = (MagicWeapon) ArticleManager.getInstance().getArticle(me.getArticleName());
			if (mw != null) {
				for(int i=0; i<mbm.getHiddenAttrNum(); i++) {
					for(int sel:indexs){
						if(sel==i){
							MagicWeaponAttrModel attr = me.getHideproterty().get(i);
							HiddenAttrModel hm = MagicWeaponManager.instance.hiddenDataMap.get(mw.getDataLevel());
							int maxAttrNum = getHiddenMaxAttr(hm.getAttrNumByType(attr.getId())[1], me.getColorType(), hm.getAttrNumByType(attr.getId())[0]);
							int colorvalue = MagicWeaponManager.instance.getHiddenAttrColor(attr.getAttrNum(),maxAttrNum);
							if(colorvalue>=3){
								return true;
							}
						}
					}
				}
			}
		}
		return false;
	}
	
	/**
	 * 法宝炼星手续费=法宝境界*炼星等级*500文
	 * @return
	 */
	public int strongConsumeMoney(NewMagicWeaponEntity ee) {
		int count = 100000;
		ArticleManager am = ArticleManager.getInstance();
		Article a = am.getArticle(ee.getArticleName());
		if (a instanceof MagicWeapon) {
			count = (((MagicWeapon) a).getClasslimit() + 1) * (ee.getMstar() + 1) * 500;
		}
		return count;
	}
	
	/**
	 * 被吞噬经验=颜色基础吞噬经验*（1+品阶LV*等级系数+加持LV*加持系数）
	 * @return
	 */
	public long addExps(NewMagicWeaponEntity ee ,Player p){
		long exp = 0;
		if(ee!=null){
			try{
				exp = (long)(MagicWeaponConstant.basicEap[ee.getColorType()]*(1 + ee.getMcolorlevel()*MagicWeaponConstant.等级系数 + ee.getMstar()*MagicWeaponConstant.加持系数));
				exp = exp*(p.getMagicWeaponDevourPercent()+100)/100;
				if(logger.isDebugEnabled()){
					logger.debug("[法宝加经验] [法宝："+ee.getArticleName()+"] [法宝id:"+ee.getId()+"] [exp:"+exp+"] [经验加成系数："+p.getMagicWeaponDevourPercent()+"] ["+p.getLogString()+"]");
				}
			}catch(Exception e){
				e.printStackTrace();
			}
		}
		return exp;
	}
	
	public boolean supplementLingqi(Player player, long magicweaponId, long materiaId, int num) {
		if(magicweaponId < 0 || materiaId < 0) {
			player.sendError(Translate.请放入正确物品);
			return false;
		}
		Knapsack bag = player.getKnapsack_common();
		if(bag == null){
			logger.error("取得的玩家背包是null {}", player.getName());
			return false;
		}
		ArticleEntity ae1 = DefaultArticleEntityManager.getInstance().getEntity(magicweaponId);
		ArticleEntity ae2 = DefaultArticleEntityManager.getInstance().getEntity(materiaId);
		if(ae1 == null || !(ae1 instanceof NewMagicWeaponEntity)) {
			player.sendError(Translate.请放入法宝);
			return false;
		}
		if(ae2 == null) {
			player.sendError(Translate.请放入材料);
			return false;
		}
		
		if (!ArticleProtectManager.instance.isCanDo(player, ArticleProtectDataValues.ArticleProtect_High, ae1.getId())) {
			player.sendError(Translate.高级锁魂物品不能做此操作);
			return false;
		}
		
		NewMagicWeaponEntity me = (NewMagicWeaponEntity) DefaultArticleEntityManager.getInstance().getEntity(magicweaponId);//法宝
		Article ac = ArticleManager.getInstance().getArticle(me.getArticleName());
		if(ac == null || !(ac instanceof MagicWeapon)) {
			logger.error("[获取物品模板错误][" + player.getLogString() + "] [" + me.getArticleName() + "]");
			return false;
		}
		MagicWeapon mw = (MagicWeapon) ac;
		if(me.getMdurability() >= mw.getNaijiudu()) {
			player.sendError(Translate.无需补充灵气);
			return false;
		}
		int index1 = bag.indexOf(ae1);
		int index2 = bag.indexOf(ae2);
		if(index1 < 0) {
			player.sendError(Translate.背包中没有该法宝);
			return false;
		}
		if(index2 < 0) {
			player.sendError(Translate.背包中没有该材料);
			return false;
		}
		int index = -1;
		for(int i=0; i<MagicWeaponConstant.lingqiProps.length; i++) {
			if(MagicWeaponConstant.lingqiProps[i].equals(ae2.getArticleName())) {
				index = i;
				break;
			}
		}
		if(index < 0) {
			player.sendError(Translate.请放入正确物品);
			return false;
		}
		ArticleEntity ae = bag.remove(index2, "补充法宝灵气", true);
		if (ae != null) {
			logger.info("remove player [{}] tp item [{}]",
					player.getName(), ae2.getArticleName() + " color ["+ ae2.getColorType() +"]");
		}else{
			logger.error("格子号正确，但移除失败.player [{}],item [{}]",
					player.getName(), ae2.getArticleName() + " color ["+ ae2.getColorType() +"]");
			return false;
		}
		int du = me.getMdurability() + MagicWeaponConstant.lingqiNum[index];
		if(du > mw.getNaijiudu()) {
			du = mw.getNaijiudu();
		}
		me.setMdurability(du);
		if(logger.isDebugEnabled()) {
			logger.debug("[补充法宝灵气成功][" + player.getLogString() + "][" + magicweaponId + "] [addnum:" + MagicWeaponConstant.lingqiNum[index] + "][resultNum:" + me.getMdurability() + "]");
		}
		return true;
	}
	
	
	public String getFileName() {
		return fileName;
	}
	public void setFileName(String fileName) {
		this.fileName = fileName;
	}
	public String getDataFileName() {
		return dataFileName;
	}
	public void setDataFileName(String dataFileName) {
		this.dataFileName = dataFileName;
	}
}
