package com.fy.engineserver.sprite.horse2.manager;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Hashtable;
import java.util.List;
import java.util.Map;
import java.util.Random;

import org.slf4j.Logger;

import com.fy.engineserver.achievement.AchievementManager;
import com.fy.engineserver.achievement.RecordAction;
import com.fy.engineserver.activity.ActivitySubSystem;
import com.fy.engineserver.activity.shop.ShopActivityManager;
import com.fy.engineserver.bourn.BournCfg;
import com.fy.engineserver.bourn.BournManager;
import com.fy.engineserver.datasource.article.concrete.DefaultArticleEntityManager;
import com.fy.engineserver.datasource.article.data.articles.Article;
import com.fy.engineserver.datasource.article.data.entity.ArticleEntity;
import com.fy.engineserver.datasource.article.data.equipments.HorseEquipmentColumn;
import com.fy.engineserver.datasource.article.data.magicweapon.MagicWeaponConstant;
import com.fy.engineserver.datasource.article.data.props.HorseProps;
import com.fy.engineserver.datasource.article.data.props.Knapsack;
import com.fy.engineserver.datasource.article.manager.ArticleEntityManager;
import com.fy.engineserver.datasource.article.manager.ArticleManager;
import com.fy.engineserver.datasource.language.Translate;
import com.fy.engineserver.economic.BillingCenter;
import com.fy.engineserver.economic.CurrencyType;
import com.fy.engineserver.economic.ExpenseReasonType;
import com.fy.engineserver.event.Event;
import com.fy.engineserver.event.EventProc;
import com.fy.engineserver.event.EventRouter;
import com.fy.engineserver.event.cate.EventOfPlayer;
import com.fy.engineserver.event.cate.EventWithObjParam;
import com.fy.engineserver.mail.service.MailManager;
import com.fy.engineserver.menu.MenuWindow;
import com.fy.engineserver.menu.Option;
import com.fy.engineserver.menu.Option_Cancel;
import com.fy.engineserver.menu.WindowManager;
import com.fy.engineserver.menu.horse.Option_ConfirmReplaceHighSkill;
import com.fy.engineserver.menu.horse.Option_confirmRankStronger;
import com.fy.engineserver.message.CONFIRM_WINDOW_REQ;
import com.fy.engineserver.message.GameMessageFactory;
import com.fy.engineserver.message.NOTICE_PARTICLE_REQ;
import com.fy.engineserver.minigame.MinigameConstant;
import com.fy.engineserver.playerAims.manager.PlayerAimManager;
import com.fy.engineserver.seal.SealManager;
import com.fy.engineserver.shop.Goods;
import com.fy.engineserver.shop.Shop;
import com.fy.engineserver.shop.ShopManager;
import com.fy.engineserver.sprite.Player;
import com.fy.engineserver.sprite.Soul;
import com.fy.engineserver.sprite.concrete.GamePlayerManager;
import com.fy.engineserver.sprite.horse.Horse;
import com.fy.engineserver.sprite.horse.HorseManager;
import com.fy.engineserver.sprite.horse.HorseOtherData;
import com.fy.engineserver.sprite.horse2.instance.Horse2RelevantEntity;
import com.fy.engineserver.sprite.horse2.model.BloodStarModel;
import com.fy.engineserver.sprite.horse2.model.HorseAttrModel;
import com.fy.engineserver.sprite.horse2.model.HorseBaseModel;
import com.fy.engineserver.sprite.horse2.model.HorseColorModel;
import com.fy.engineserver.sprite.horse2.model.HorseRankModel;
import com.fy.engineserver.sprite.horse2.model.HorseSkillModel;
import com.fy.engineserver.sprite.pet2.Pet2Manager;
import com.fy.engineserver.sprite.pet2.PetsOfPlayer;
import com.xuanzhi.tools.cache.LruMapCache;
import com.xuanzhi.tools.simplejpa.SimpleEntityManager;
import com.xuanzhi.tools.simplejpa.SimpleEntityManagerFactory;

public class Horse2EntityManager implements EventProc{
	
	public static SimpleEntityManager<Horse2RelevantEntity> em;
	
	public static Logger logger = HorseManager.logger;
	
	public static Horse2EntityManager instance;
	
	public LruMapCache cache = new LruMapCache(10240, 60 * 60 * 1000, false, "horse2_entity");
	
	public static final int MAX_RANK_STAR = 80;
	
	public static final int MAX_BLOOD_STAR = 20;
	
	public static final int MAX_COLOR_TYPE = 4;
	/** 对应阶坐骑给予的物品数 */
	public static final int[] horseChangePropNums = new int[]{0,10,20,30};
	/** 物品名 */
	public static String horseChangePropName = Translate.坐骑碎片;
	
	public Map<Long, Horse2RelevantEntity> tempCache = new Hashtable<Long, Horse2RelevantEntity>();
	
	public void init(){
		instance = this;
		em = SimpleEntityManagerFactory.getSimpleEntityManager(Horse2RelevantEntity.class);
		this.doReg();
	}
	/**
	 * 获取刷新出来的临时技能id
	 * @param player
	 * @param horseId
	 * @return
	 */
	public int getTempSkillIdById(Player player, long horseId) {
		Horse2RelevantEntity entity = this.getEntity(player.getId());
		if (entity == null) {
			return -1;
		}
		int tempId = -1;
		if (entity.getTempHorseId() != null && entity.getTempHorseId().size() > 0) {
			for (int i=0; i<entity.getTempHorseId().size(); i++) {
				if (entity.getTempHorseId().get(i) == horseId) {
					tempId = entity.getTempSkillList().get(i);
					break;
				}
			}
		}
		return tempId;
	}
	
	public int isPlayerCanUpgradeHorseFree(Player player) {
		try {
			Soul soul = player.getCurrSoul();
			if (soul.getHorseArr() == null || soul.getHorseArr().size() <= 0) {
				return 0;
			}
			boolean temp = false;
			Horse horse = null;
			for (long horseId : soul.getHorseArr()) {
				Horse hh = HorseManager.getInstance().getHorseById(horseId, player);
				if (hh != null && !hh.isFly()) {	
					horse = hh;
					temp = true;
					break;
				} 
			}
			if (!temp) {
				return 0;
			}
			Horse2RelevantEntity entity = this.getEntity(player.getId());
			if (entity == null) {
				return 0;
			}
			HorseBaseModel bm = Horse2Manager.instance.baseModel;
			Article a = ArticleManager.getInstance().getArticleByCNname(bm.getNeedArticle4Rank());
			Knapsack bag = player.getKnapsack_common();
//			if (player.countArticleInKnapsacksByName(a.getName()) > 0) {
//				return 4;
//			}
			if (bag.countArticle(a.getName()) > 0) {
				return 4;
			}
			if (horse.getRank() < 80 && entity.getCultureTime4Free() < bm.getFreeTimes4Rank()) {
				return 4;
			}
			if (entity.getRefreshSkillTime() < bm.getFreeTimes4Skill()) {
				return 5;
			}
		} catch (Exception e) {
			
		}
		return 0;
	}
	/**
	 * 加载坐骑基础数值
	 * @param horse
	 * @param natural
	 * @param colorType
	 * @param rankStar
	 * @param bloodStar
	 * @param skills
	 * @param skillLevels
	 */
	public void initHorseBasicAttr(Horse horse, Player player, byte natural, int colorType, int rankStar, int bloodStar, int[] skills, int[] skillLevels) {
		try {
			if (horse.isFly()) {
				return;
			}
			if (logger.isInfoEnabled()) {
				logger.info("[initHorseBasicAttr] [" + player.getLogString() + "] [是否乘骑状态:" + player.isIsUpOrDown() + "] [坐骑是否已初始化:" + horse.isInited() + "]");
			}
			HorseAttrModel model = Horse2Manager.instance.getNewHorseAttrModel(horse.getHorseId(), natural, colorType, rankStar, bloodStar, skills, skillLevels);
			if (logger.isDebugEnabled()) {
				logger.debug("[新坐骑系统] [加载坐骑基础属性] [" + player.getLogString() + "] [horseId :" + horse.getHorseId() + "] [" + model + "]");
			}
			if (model != null) {
				boolean bln = player.isIsUpOrDown() && player.getRidingHorseId() == horse.getHorseId() && horse.isInited();	//是否当前乘坐的坐骑	2014年12月11日18:58:50 初始化时不用给玩家加属性
				if (horse.getOldBasicAttr() != null) {
					List<Integer[]> attrList = horse.getOldBasicAttr().getAttrNumAndType();
					for (int i=0; i<attrList.size(); i++) {
						if (attrList.get(i)[1] == 0) {
							continue;
						}
						this.addAttr2Horse(horse, player, attrList.get(i)[0], -attrList.get(i)[1], bln);
						if (logger.isDebugEnabled()) {
							logger.debug("[新坐骑系统] [去除原有基础属性] [" + Arrays.toString(attrList.get(i))+ "] [" + player.getLogString() + "]");
						}
					}
				}
				horse.setOldBasicAttr(model);
				List<Integer[]> attrList = model.getAttrNumAndType();
				for (int i=0; i<attrList.size(); i++) {
					if (attrList.get(i)[1] == 0) {
						continue;
					}
					this.addAttr2Horse(horse, player, attrList.get(i)[0], attrList.get(i)[1], bln);
					if (logger.isDebugEnabled()) {
						logger.debug("[新坐骑系统] [加载坐骑基础属性] [" + Arrays.toString(attrList.get(i))+ "] [" + player.getLogString() + "]");
					}
				}
			}
		} catch (Exception e) {
			logger.error("[新坐骑系统] [初始化坐骑基本属性异常] [坐骑id:" + horse.getHorseId() + "]", e);
		}
	}
	/**
	 * 增加坐骑属性
	 * @param horse
	 * @param attrType
	 * @param attrNum
	 */
	private void addAttr2Horse(Horse horse, Player player, int attrType, int attrNum, boolean add2Player) {
		float index = (float) (horse.getLastEnergyIndex() * 0.1);
		switch (attrType) {
		case MagicWeaponConstant.hpNum:		//血
			if (add2Player) {
				player.setMaxHPB(player.getMaxHPB() - (int) (horse.getMaxHP() * index));
				horse.setMaxHPB(horse.getMaxHPB() + attrNum);
				player.setMaxHPB(player.getMaxHPB() + (int) (horse.getMaxHP() * index));
			} else {
				horse.setMaxHPB(horse.getMaxHPB() + attrNum);
			}
			break;
		case MagicWeaponConstant.physicAttNum: //外攻
			if (add2Player) {
				player.setPhyAttackB(player.getPhyAttackB() - (int) (horse.getPhyAttackB() * index));
				horse.setPhyAttackB(horse.getPhyAttackB() + attrNum);
				player.setPhyAttackB(player.getPhyAttackB() + (int) (horse.getPhyAttackB() * index));
			} else {
				horse.setPhyAttackB(horse.getPhyAttackB() + attrNum);
			}
			break;
		case MagicWeaponConstant.physicDefanceNum: //外防
			if (add2Player) {
				player.setPhyDefenceB(player.getPhyDefenceB() - (int) (horse.getPhyDefenceB() * index));
				horse.setPhyDefenceB(horse.getPhyDefenceB() + attrNum);
				player.setPhyDefenceB(player.getPhyDefenceB() + (int) (horse.getPhyDefenceB() * index));
			} else {
				horse.setPhyDefenceB(horse.getPhyDefenceB() + attrNum);
			}
			break;
		case MagicWeaponConstant.破甲: //破甲
			if (add2Player) {
				player.setBreakDefenceB(player.getBreakDefenceB() - (int) (horse.getBreakDefenceB() * index));
				horse.setBreakDefenceB(horse.getBreakDefenceB() + attrNum);
				player.setBreakDefenceB(player.getBreakDefenceB() + (int) (horse.getBreakDefenceB() * index));
			} else {
				horse.setBreakDefenceB(horse.getBreakDefenceB() + attrNum);
			}
			break;
		case MagicWeaponConstant.dodgeNum: //闪避
			if (add2Player) {
				player.setDodgeB(player.getDodgeB() - (int) (horse.getDodgeB() * index));
				horse.setDodgeB(horse.getDodgeB() + attrNum);
				player.setDodgeB(player.getDodgeB() + (int) (horse.getDodgeB() * index));
			} else {
				horse.setDodgeB(horse.getDodgeB() + attrNum);
			}
			break;
		case MagicWeaponConstant.cirtNum: //暴击
			if (add2Player) {
				player.setCriticalHitB(player.getCriticalHitB() - (int) (horse.getCriticalHitB() * index));
				horse.setCriticalHitB(horse.getCriticalHitB() + attrNum);
				player.setCriticalHitB(player.getCriticalHitB() + (int) (horse.getCriticalHitB() * index));
			} else {
				horse.setCriticalHitB(horse.getCriticalHitB() + attrNum);
			}
			break;
		case MagicWeaponConstant.mpNum: //仙法
			if (add2Player) {
				player.setMaxMPB(player.getMaxMPB() - (int) (horse.getMaxMPB() * index));
				horse.setMaxMPB(horse.getMaxMPB() + attrNum);
				player.setMaxMPB(player.getMaxMPB() + (int) (horse.getMaxMPB() * index));
			} else {
				horse.setMaxMPB(horse.getMaxMPB() + attrNum);
			}
			break;
		case MagicWeaponConstant.magicAttNum: //法攻
			if (add2Player) {
				player.setMagicAttackB(player.getMagicAttackB() - (int) (horse.getMagicAttackB() * index));
				horse.setMagicAttackB(horse.getMagicAttackB() + attrNum);
				player.setMagicAttackB(player.getMagicAttackB() + (int) (horse.getMagicAttackB() * index));
			} else {
				horse.setMagicAttackB(horse.getMagicAttackB() + attrNum);
			}
			break;
		case MagicWeaponConstant.magicDefanceNum: //法防
			if (add2Player) {
				player.setMagicDefenceB(player.getMagicDefenceB() - (int) (horse.getMagicDefenceB() * index));
				horse.setMagicDefenceB(horse.getMagicDefenceB() + attrNum);
				player.setMagicDefenceB(player.getMagicDefenceB() + (int) (horse.getMagicDefenceB() * index));
			} else {
				horse.setMagicDefenceB(horse.getMagicDefenceB() + attrNum);
			}
			break;
		case MagicWeaponConstant.hitNum: //命中
			if (add2Player) {
				player.setHitB(player.getHitB() - (int) (horse.getHitB() * index));
				horse.setHitB(horse.getHitB() + attrNum);
				player.setHitB(player.getHitB() + (int) (horse.getHitB() * index));
			} else {
				horse.setHitB(horse.getHitB() + attrNum);
			}
			break;
		case MagicWeaponConstant.精准: //精准
			if (add2Player) {
				player.setAccurateB(player.getAccurateB() - (int) (horse.getAccurateB() * index));
				horse.setAccurateB(horse.getAccurateB() + attrNum);
				player.setAccurateB(player.getAccurateB() + (int) (horse.getAccurateB() * index));
			} else {
				horse.setAccurateB(horse.getAccurateB() + attrNum);
			}
			break;
		case MagicWeaponConstant.免爆: //免爆
			if (add2Player) {
				player.setCriticalDefenceB(player.getCriticalDefenceB() - (int) (horse.getCriticalDefenceB() * index));
				horse.setCriticalDefenceB(horse.getCriticalDefenceB() + attrNum);
				player.setCriticalDefenceB(player.getCriticalDefenceB() + (int) (horse.getCriticalDefenceB() * index));
			} else {
				horse.setCriticalDefenceB(horse.getCriticalDefenceB() + attrNum);
			}
			break;
		case MagicWeaponConstant.火攻: //火攻
			if (add2Player) {
				player.setFireAttackB(player.getFireAttackB() - (int) (horse.getFireAttackB() * index));
				horse.setFireAttackB(horse.getFireAttackB() + attrNum);
				player.setFireAttackB(player.getFireAttackB() + (int) (horse.getFireAttackB() * index));
			} else {
				horse.setFireAttackB(horse.getFireAttackB() + attrNum);
			}
			break;
		case MagicWeaponConstant.火炕: //火炕
			if (add2Player) {
				player.setFireDefenceB(player.getFireDefenceB() - (int) (horse.getFireDefenceB() * index));
				horse.setFireDefenceB(horse.getFireDefenceB() + attrNum);
				player.setFireDefenceB(player.getFireDefenceB() + (int) (horse.getFireDefenceB() * index));
			} else {
				horse.setFireDefenceB(horse.getFireDefenceB() + attrNum);
			}
			break;
		case MagicWeaponConstant.火减抗: //外攻
			if (add2Player) {
				player.setFireIgnoreDefenceB(player.getFireIgnoreDefenceB() - (int) (horse.getFireIgnoreDefenceB() * index));
				horse.setFireIgnoreDefenceB(horse.getFireIgnoreDefenceB() + attrNum);
				player.setFireIgnoreDefenceB(player.getFireIgnoreDefenceB() + (int) (horse.getFireIgnoreDefenceB() * index));
			} else {
				horse.setFireIgnoreDefenceB(horse.getFireIgnoreDefenceB() + attrNum);
			}
			break;
		case MagicWeaponConstant.风攻: //外攻
			if (add2Player) {
				player.setWindAttackB(player.getWindAttackB() - (int) (horse.getWindAttackB() * index));
				horse.setWindAttackB(horse.getWindAttackB() + attrNum);
				player.setWindAttackB(player.getWindAttackB() + (int) (horse.getWindAttackB() * index));
			} else {
				horse.setWindAttackB(horse.getWindAttackB() + attrNum);
			}
			break;
		case MagicWeaponConstant.风抗: //风抗
			if (add2Player) {
				player.setWindDefenceB(player.getWindDefenceB() - (int) (horse.getWindDefenceB() * index));
				horse.setWindDefenceB(horse.getWindDefenceB() + attrNum);
				player.setWindDefenceB(player.getWindDefenceB() + (int) (horse.getWindDefenceB() * index));
			} else {
				horse.setWindDefenceB(horse.getWindDefenceB() + attrNum);
			}
			break;
		case MagicWeaponConstant.风减抗: //风减抗
			if (add2Player) {
				player.setWindIgnoreDefenceB(player.getWindIgnoreDefenceB()- (int) (horse.getWindIgnoreDefenceB() * index));
				horse.setWindIgnoreDefenceB(horse.getWindIgnoreDefenceB() + attrNum);
				player.setWindIgnoreDefenceB(player.getWindIgnoreDefenceB() + (int) (horse.getWindIgnoreDefenceB() * index));
			} else {
				horse.setWindIgnoreDefenceB(horse.getWindIgnoreDefenceB() + attrNum);
			}
			break;
		case MagicWeaponConstant.冰攻: //冰攻
			if (add2Player) {
				player.setBlizzardAttackB(player.getBlizzardAttackB()- (int) (horse.getBlizzardAttackB() * index));
				horse.setBlizzardAttackB(horse.getBlizzardAttackB() + attrNum);
				player.setBlizzardAttackB(player.getBlizzardAttackB() + (int) (horse.getBlizzardAttackB() * index));
			} else {
				horse.setBlizzardAttackB(horse.getBlizzardAttackB() + attrNum);
			}
			break;
		case MagicWeaponConstant.冰抗: //冰抗
			if (add2Player) {
				player.setBlizzardDefenceB(player.getBlizzardDefenceB()- (int) (horse.getBlizzardDefenceB() * index));
				horse.setBlizzardDefenceB(horse.getBlizzardDefenceB() + attrNum);
				player.setBlizzardDefenceB(player.getBlizzardDefenceB() + (int) (horse.getBlizzardDefenceB() * index));
			} else {
				horse.setBlizzardDefenceB(horse.getBlizzardDefenceB() + attrNum);
			}
			break;
		case MagicWeaponConstant.冰减抗: //冰减抗
			if (add2Player) {
				player.setBlizzardIgnoreDefenceB(player.getBlizzardIgnoreDefenceB() - (int) (horse.getBlizzardIgnoreDefenceB() * index));
				horse.setBlizzardIgnoreDefenceB(horse.getBlizzardIgnoreDefenceB() + attrNum);
				player.setBlizzardIgnoreDefenceB(player.getBlizzardIgnoreDefenceB() + (int) (horse.getBlizzardIgnoreDefenceB() * index));
			} else {
				horse.setBlizzardIgnoreDefenceB(horse.getBlizzardIgnoreDefenceB() + attrNum);
			}
			break;
		case MagicWeaponConstant.雷攻: //雷攻
			if (add2Player) {
				player.setThunderAttackB(player.getThunderAttackB() - (int) (horse.getThunderAttackB() * index));
				horse.setThunderAttackB(horse.getThunderAttackB() + attrNum);
				player.setThunderAttackB(player.getThunderAttackB() + (int) (horse.getThunderAttackB() * index));
			} else {
				horse.setThunderAttackB(horse.getThunderAttackB() + attrNum);
			}
			break;
		case MagicWeaponConstant.雷抗: //雷抗
			if (add2Player) {
				player.setThunderDefenceB(player.getThunderDefenceB() - (int) (horse.getThunderDefenceB() * index));
				horse.setThunderDefenceB(horse.getThunderDefenceB() + attrNum);
				player.setThunderDefenceB(player.getThunderDefenceB() + (int) (horse.getThunderDefenceB() * index));
			} else {
				horse.setThunderDefenceB(horse.getThunderDefenceB() + attrNum);
			}
			break;
		case MagicWeaponConstant.雷减抗: //雷减抗
			if (add2Player) {
				player.setThunderIgnoreDefenceB(player.getThunderIgnoreDefenceB() - (int) (horse.getThunderIgnoreDefenceB() * index));
				horse.setThunderIgnoreDefenceB(horse.getThunderIgnoreDefenceB() + attrNum);
				player.setThunderIgnoreDefenceB(player.getThunderIgnoreDefenceB() + (int) (horse.getThunderIgnoreDefenceB() * index));
			} else {
				horse.setThunderIgnoreDefenceB(horse.getThunderIgnoreDefenceB() + attrNum);
			}
			break;

		default:
			break;
		}
	}
	
	/**
	 * 升级坐骑技能
	 * @param player
	 * @param horseId
	 * @param skillIndex
	 * @param skillId
	 * @return
	 */
	public boolean upGradeSkill(Player player, long horseId, int skillIndex, int skillId) {
		synchronized (player) {
			Horse horse = HorseManager.getInstance().getHorseById(horseId, player);
			if(horse == null) {
				logger.warn("[新坐骑系统] [获取玩家当前元神坐骑异常] [" + player.getLogString() + "] [" + horseId + "]");
				return false;
			}
			if (horse.isFly()) {
				player.sendError(Translate.飞行坐骑不可进行此操作);
				return false;
			}
			HorseOtherData hod = horse.getOtherData();
			if (hod.getSkillList().length <= skillIndex) {
				logger.warn("[新坐骑系统] [坐骑升级技能] [失败] [" + player.getLogString() + "] [坐骑技能:" + Arrays.toString(hod.getSkillList()) + "] [发过来的index:" + skillIndex + "] [skillId :" + skillId + "] [horseId:" + horseId + "]");
				return false;
			}
			if (hod.getSkillList()[skillIndex] != skillId) {
				logger.warn("[新坐骑系统] [坐骑升级技能] [失败] [" + player.getLogString() + "] [坐骑技能:" + Arrays.toString(hod.getSkillList()) + "] [发过来的index:" + skillIndex + "] [skillId :" + skillId + "] [horseId:" + horseId + "]");
				return false;
			}
			HorseSkillModel hsm = Horse2Manager.instance.horseSkillMap.get(skillId);
			ArrayList<ArticleEntity> articles = new ArrayList<ArticleEntity>();
			int oldLevel = hod.getSkillLevel()[skillIndex];
			if (hsm != null) {
				if (oldLevel >= hsm.getMaxLevel() - 1) {
					player.sendError(Translate.此技能已经达到最高级);
					return false;
				}
				String aName = hsm.getNeedArticleName()[oldLevel];
				int num = hsm.getNeedArticleNum()[oldLevel];
				int probabbly = hsm.getUpGradeRate()[oldLevel];
				Article a = ArticleManager.getInstance().getArticleByCNname(aName);
				if (a == null) {
					logger.error("[新坐骑系统] [升级坐骑技能失败] [物品模板不存在] [" + player.getLogString() + "] [物品统计名:" + aName + "]");
					player.sendError(Translate.出现异常);
					return false;
				}
				Knapsack kp = player.getKnapsack_common();
//				int count = player.countArticleInKnapsacksByName(a.getName());
				int count = kp.countArticle(a.getName());
				if (count < num) {
					player.sendError(Translate.text_feed_silkworm_006);
					return false;
				}
				for (int i=0; i<num; i++) {
					ArticleEntity ae = player.removeArticle(a.getName(), "升级坐骑技能","");
					if (ae == null) {
						logger.error("[新坐骑系统] [升级坐骑技能] [失败] [删除物品不成功] [" + player.getLogString() + "] [articleName:" + a.getName() + "] [已删除个数：" + i + "]");
						player.sendError(Translate.删除物品失败);
						return false;
					} else {
						articles.add(ae);
					}
				}
				int ran = player.random.nextInt(1000);
				if (logger.isDebugEnabled()) {
					logger.debug("[新坐骑系统] [升级技能] [升级概率:" + probabbly + "] [随机数:" + ran + "] [skillId:" + skillId + "] [" + player.getLogString() + "]");
				}
				if (ran <= probabbly) {
					int newLevel = oldLevel + 1;
					hod.getSkillLevel()[skillIndex] = newLevel;
					horse.setOtherData(hod);
					horse.initBasicAttr();
//					player.sendError("升级坐骑技能成功，技能id：" + hod.getSkillList()[skillIndex] + "==升级后技能等级:" + (hod.getSkillLevel()[skillIndex]+1));
					try {
						if (newLevel >= 1) {
							int lv2Num = 0;
							for (int i=0; i<hod.getSkillLevel().length; i++) {
								if (hod.getSkillLevel()[i] > 0) {
									lv2Num ++ ;
								}
							}
							AchievementManager.getInstance().record(player, RecordAction.坐骑拥有2级技能个数, lv2Num);
							if (newLevel >= 2) {
								int lv3Num = 0;
								for (int i=0; i<hod.getSkillLevel().length; i++) {
									if (hod.getSkillLevel()[i] > 1) {
										lv3Num ++ ;
									}
								}
								AchievementManager.getInstance().record(player, RecordAction.坐骑拥有3级技能个数, lv3Num);
							}
						}
					} catch (Exception e) {
						PlayerAimManager.logger.error("[目标系统] [统计坐骑技能升级] [异常] [" + player.getLogString() + "]", e);
					}
					if (logger.isInfoEnabled()) {
						logger.info("[新坐骑系统] [升级坐骑技能] [成功] [" + player.getLogString() + "] [horseId :" + horseId + "] [skills:" + Arrays.toString(hod.getSkillList()) + "] [skillLevel:" + Arrays.toString(hod.getSkillLevel()) + "]");
					}
					player.sendError(Horse2Manager.instance.translate.get(14));
					try {
						ShopActivityManager.getInstance().noticeUseSuccess(player, articles);
					} catch (Exception e) {
						ActivitySubSystem.logger.error("[使用赠送活动] [异常] [" + player.getLogString() + "]", e);
					}
					return true;
				} else {
					if (logger.isInfoEnabled()){
						logger.info("[新坐骑系统] [升级坐骑技能] [失败] [随机数大于概率] [" + player.getLogString() + "]");
					}
					player.sendError(Horse2Manager.instance.translate.get(13));
					try {
						ShopActivityManager.getInstance().noticeUseSuccess(player, articles);
					} catch (Exception e) {
						ActivitySubSystem.logger.error("[使用赠送活动] [异常] [" + player.getLogString() + "]", e);
					}
				}
			} else {
				logger.error("[新坐骑系统] [玩家坐骑拥有的技能配表中没找到] [" + player.getLogString() + "] [skillId :" + skillId + "]");
			}
			
		}
		return false;
	}
	
	/**
	 * 替换技能
	 * @param player
	 * @param horseId
	 * @return
	 */
	public boolean learnSkill(Player player, long horseId, int skillIndex, int oldSkillId, boolean confirm) {
		synchronized (player) {
			Horse horse = HorseManager.getInstance().getHorseById(horseId, player);
			if(horse == null) {
				logger.warn("[新坐骑系统] [获取玩家当前元神坐骑异常] [" + player.getLogString() + "] [" + horseId + "]");
				return false;
			}
			if (horse.isFly()) {
				player.sendError(Translate.飞行坐骑不可进行此操作);
				return false;
			}
			if (oldSkillId <= 0 && horse.getSkillNum() <= skillIndex) {
				player.sendError(Translate.该技能格还未开启);
				return false;
			}
			Horse2RelevantEntity entity = this.getEntity(player.getId());
			if (entity != null) {
				List<Long> tempHorseIds = entity.getTempHorseId();
				List<Integer> list = entity.getTempSkillList();
				if (tempHorseIds.contains(horseId)) {
					int tempId = -1;
					for (int i=0; i<tempHorseIds.size(); i++) {
						if (tempHorseIds.get(i) == horseId) {
							tempId = list.get(i);
							break;
						}
					}
					if (tempId <= 0 ) {
						player.sendError(Translate.该技能格还未开启);
						return false;
					}
					HorseOtherData hod = horse.getOtherData();
					if (this.containsInt(hod.getSkillList(), tempId)) {
						player.sendError(Translate.该坐骑已经有此技能);
						return false;
					}
					int[] newSkill = hod.getSkillList();
					int[] newLevel = hod.getSkillLevel();
					if (oldSkillId <= 0) {
						list.remove((Integer)tempId);
						tempHorseIds.remove(horseId);
						newSkill = Arrays.copyOf(hod.getSkillList(), hod.getSkillList().length + 1);
						newLevel = Arrays.copyOf(hod.getSkillLevel(), hod.getSkillLevel().length + 1);
						newSkill[newSkill.length - 1] = tempId;
						newLevel[newLevel.length - 1] = 0;
						entity.setTempSkillList(list);
						entity.setTempHorseId(tempHorseIds);
						hod.setSkillList(newSkill);
						hod.setSkillLevel(newLevel);
						horse.setOtherData(hod);
					} else {
						if(newSkill.length > skillIndex && newSkill[skillIndex] == oldSkillId) {
//							HorseSkillModel tempHsm = Horse2Manager.instance.horseSkillMap.get(oldSkillId);
							if (!confirm/* && (newLevel[skillIndex] > 0 || tempHsm.getSkillType() > 0)*/) {
								this.popConfirmWindow2(player, horseId, skillIndex, oldSkillId, newLevel[skillIndex], tempId);
								return false;
							}
//							int oldSkId = newSkill[skillIndex];
//							int oldSkLevel = newLevel[skillIndex];
							list.remove((Integer)tempId);
							tempHorseIds.remove(horseId);
							newSkill[skillIndex] = tempId;
							newLevel[skillIndex] = 0;
							entity.setTempSkillList(list);
							entity.setTempHorseId(tempHorseIds);
							hod.setSkillList(newSkill);
							hod.setSkillLevel(newLevel);
							horse.setOtherData(hod);
						} else {
							logger.warn("[新坐骑系统] [坐骑学习技能] [失败] [客户端发送过来的index和技能id不匹配] [" + player.getLogString() + "] [skillIndex:" + skillIndex + "]"
									+ "] [oldSkillId:" + oldSkillId + "] [原技能:" + Arrays.toString(newSkill) + "] [horseId :" + horseId + "]");
							player.sendError(Translate.替换技能失败);
							return false;
						}
					}
					horse.initBasicAttr();
					logger.warn("[新坐骑系统] [坐骑学习技能] [成功] [" + player.getLogString() + "] [horseId :" + horseId + "] [skillList:" + Arrays.toString(newSkill) + "]");
					return true;
				} else {
					player.sendError(Translate.没有临时技能);
					return false;
				}
			}
		}
		return false;
	}
	/**
	 * 替换技能二次确认
	 * @param p
	 * @param horseId
	 * @param cost
	 */
	private void popConfirmWindow2(Player p, long horseId, int skillIndex, int oldSkillId, int oldSkillLevel,  int newSkillId) {
		WindowManager wm = WindowManager.getInstance();
		MenuWindow mw = wm.createTempMenuWindow(600);
		Option_ConfirmReplaceHighSkill option1 = new Option_ConfirmReplaceHighSkill();
		option1.setText(MinigameConstant.CONFIRM);
		option1.setHorseId(horseId);
		option1.setSkillIndex(skillIndex);
		option1.setSkillId(oldSkillId);
		Option_Cancel option2 = new Option_Cancel();
		option2.setText(MinigameConstant.CANCLE);
		Option[] options = new Option[] {option1, option2};
		mw.setOptions(options);
		String msg = Horse2Manager.instance.translate.get(9);		//0xff8400   0x5aff00
		HorseSkillModel hsm1 = Horse2Manager.instance.horseSkillMap.get(newSkillId);
		HorseSkillModel hsm2 = Horse2Manager.instance.horseSkillMap.get(oldSkillId);
		String str1 = hsm1.getName() + "Lv.1";
		String str2 = hsm2.getName() + "Lv." + (oldSkillLevel + 1);
		String str3 = "0x5aff00";
		String str4 = "0x5aff00";
		if (hsm1.getSkillType() != 0) {
			str3 = "0xff8400";
		}
		if (hsm2.getSkillType() != 0) {
			str4 = "0xff8400";
		}
		msg = String.format(msg, str3, str1, str4, str2);
		mw.setDescriptionInUUB(msg);
		CONFIRM_WINDOW_REQ creq = new CONFIRM_WINDOW_REQ(GameMessageFactory.nextSequnceNum(), mw.getId(), mw.getDescriptionInUUB(), options);
		p.addMessageToRightBag(creq);
	}
	/**
	 * 刷坐骑技能
	 * @param player
	 * @param refreshType
	 * @return  刷出来的技能id [1]剩余免费刷新次数 [2]粒子位置
	 */
	public int[] refreshSkill(Player player, int refreshType, long horseId) {
		synchronized (player) {
			int[] resultL = new int[3];
			Horse horse = HorseManager.getInstance().getHorseById(horseId, player);
			if(horse == null) {
				logger.warn("[新坐骑系统] [获取玩家当前元神坐骑异常] [" + player.getLogString() + "] [" + horseId + "]");
				return new int[]{-1,0,-1};
			}
			if (horse.isFly()) {
				player.sendError(Translate.飞行坐骑不可进行此操作);
				return  new int[]{-1,0,-1};
			}
			int result = -1;
	//	if (entity.getTempSkillList() != null && entity.getTempSkillList().size() > 0) {		//原来有技能是否给提示
	//		return -1;
	//	}
			Horse2RelevantEntity entity = this.getEntity(player.getId());
			HorseBaseModel bm = Horse2Manager.instance.baseModel;
			if (entity != null && bm != null) {
				boolean flag = false;
				if (refreshType == 0) {
					if(entity.getRefreshSkillTime() >= bm.getFreeTimes4Skill()) {		//还有免费次数可以用
						player.sendError(Translate.技能刷新免费次数用完);
						return new int[]{-1,0,-1};
					} 
					flag = true;
				} else {
					String articleCNNName = bm.getUseArticleName(refreshType);
					Article a = ArticleManager.getInstance().getArticleByCNname(articleCNNName);
					/*if (a != null && player.removeArticle(a.getName(), "坐骑刷技能扣除")) {
						flag = true;
					} else {
						player.sendError(Translate.刷新技能物品不足);
						return new int[]{-1,0, -1};
					}*/
					if (a != null) {
						ArticleEntity ae = player.removeArticle(a.getName(), "坐骑刷技能扣除","");
						if (ae != null) {
							flag = true;
							try {
								ArrayList<ArticleEntity> strongMaterialEntitys = new ArrayList<ArticleEntity>();
								strongMaterialEntitys.add(ae);
								ShopActivityManager.getInstance().noticeUseSuccess(player, strongMaterialEntitys);
							} catch (Exception e) {
								ActivitySubSystem.logger.error("[使用赠送活动] [领悟坐骑技能] [" + player.getLogString() + "]", e);
							}
						} else {
							player.sendError(Translate.刷新技能物品不足);
							return new int[]{-1,0, -1};
						}
					} else {
						player.sendError(Translate.刷新技能物品不足);
						return new int[]{-1,0, -1};
					}
				}
				if (refreshType == 0) {
					entity.setRefreshSkillTime(entity.getRefreshSkillTime() + 1);
				}
				resultL[1] = bm.getFreeTimes4Skill() - entity.getRefreshSkillTime();
				if (resultL[1] < 0) {
					resultL[1] = 0;
				}
				boolean isLearn = false;
				if (flag) {
					result = getSkillId(refreshType, player.random);
					while(containsInt(horse.getOtherData().getSkillList(), result)) {
						result = getSkillId(refreshType, player.random);
					}
					List<Integer> list = entity.getTempSkillList();
					List<Long> tempHorseId = entity.getTempHorseId();
					boolean isExist = false;
					for (int i=0; i<tempHorseId.size(); i++) {
						if (tempHorseId.get(i) == horseId) {
							list.set(i, result);
							isExist = true;
							break;
						}
					}
					HorseOtherData hod = horse.getOtherData();
					if (horse.getSkillNum() > hod.getSkillList().length) {
						if (isExist) {
							tempHorseId.remove(horseId);
							list.remove(new Integer(result));
						}
						int[] newSkill = Arrays.copyOf(hod.getSkillList(), hod.getSkillList().length + 1);
						int[] newLevel = Arrays.copyOf(hod.getSkillLevel(), hod.getSkillLevel().length + 1);
						newSkill[newSkill.length - 1] = result;
						newLevel[newLevel.length - 1] = 0;
						resultL[2] = newSkill.length - 1;
						hod.setSkillList(newSkill);
						hod.setSkillLevel(newLevel);
						horse.setOtherData(hod);
						horse.initBasicAttr();
						isLearn = true;
						if (logger.isWarnEnabled()) {
							logger.warn("[新坐骑系统] [刷新技能] [坐骑自动学习技能] [坐骑阶:" + horse.getRank() + "] [技能:" + Arrays.toString(hod.getSkillList()) + "] [技能等级：" + Arrays.toString(hod.getSkillLevel()) + "] [horseId :" + horseId + "] [原来是否有临时技能:"+isExist+"] [" + player.getLogString() + "]");
						}
					} else {
						if (!isExist) {
							list.add(result);
							tempHorseId.add(horseId);
							if(logger.isWarnEnabled()) {
								logger.warn("[新坐骑系统] [刷新技能] [类型：" + refreshType + "] [获得技能id:" + result + "] [" + player.getLogString() + "] [horseId :" + horseId + "] [刷新次数：" + entity.getRefreshSkillTime() + "] [原来是否有临时技能:"+isExist+"] [" + player.getLogString() + "]");
							}
						} 
						entity.setTempSkillList(list);
						entity.setTempHorseId(tempHorseId);
						resultL[2] = -1;
					}
				}
				String str = Horse2Manager.instance.translate.get(17);
				if (isLearn) {
					str = Translate.自动学习坐骑技能;
				}
				HorseSkillModel hs = Horse2Manager.instance.horseSkillMap.get(result);
				String str1 = "0x5aff00";
				if (hs.getSkillType() != 0) {
					str1 = "0xff8400";
				}
				str = String.format(str, str1, hs.getName());
				player.sendError(str);
			}
			resultL[0] = result;
			return resultL;
		}
	}
	
	public boolean containsInt(int[] arr, int tempId) {
		if (arr.length <= 0) {
			return false;
		}
		for(int i=0; i<arr.length; i++) {
			if(arr[i] == tempId) {
				return true;
			}
		}
		return false;
	}
	
	public int getSkillId(int type, Random ran) {
		double tempNum = 0;
		int ranNum = ran.nextInt(1000);
		for(HorseSkillModel hsm : Horse2Manager.instance.horseSkillMap.values()) {
			tempNum += hsm.getProbabblyByType(type);
			if(ranNum <= tempNum) {
				if (HorseManager.logger.isDebugEnabled()) {
					HorseManager.logger.debug("[新坐骑系统] [根据概率获得随机出来的技能] [随机数:" + ranNum + "] [获得的技能id:" + hsm.getId() + "]");
				}
				return hsm.getId();
			}
		}
		return 1;
	}
	
	/**
	 * 升级坐骑颜色
	 * @param player
	 * @param horseId
	 * @return
	 */
	public boolean colorStrong(Player player, long horseId) {
		synchronized (player) {
			Horse horse = HorseManager.getInstance().getHorseById(horseId, player);
			if(horse == null) {
				logger.warn("[新坐骑系统] [获取玩家当前元神坐骑异常] [" + player.getLogString() + "] [" + horseId + "]");
				return false;
			}
			if (horse.isFly()) {
				player.sendError(Translate.飞行坐骑不可进行此操作);
				return false;
			}
			HorseColorModel hcm = Horse2Manager.instance.naturalRateMap.get(horse.getColorType());
			if(hcm != null) {
				HorseOtherData hod = horse.getOtherData();
				int newColor = hod.getColorType() + 1;
				if(newColor > MAX_COLOR_TYPE) {
					player.sendError(Horse2Manager.instance.translate.get(19));
					return false;
				}
				for (int i=0; i<Horse2Manager.levelLimit4Color.length; i++) {
					if (player.getLevel() <= Horse2Manager.levelLimit4Color[i] && horse.getOtherData().getColorType() >= Horse2Manager.maxColor[i]) {
						String str = Horse2Manager.instance.translate.get(12);
						try {
							str = String.format(str, (Horse2Manager.levelLimit4Color[i] + 1));
						} catch (Exception e){}
						player.sendError(str);
						return false;
					}
				}
				boolean removeResult = true;
				Knapsack bag = player.getKnapsack_common();
				if(bag != null) {
					Article a = ArticleManager.getInstance().getArticleByCNname(hcm.getArticleCNNName());
					if (a != null) {
						int count = bag.countArticle(a.getName());
//						int count = player.countArticleInKnapsacksByName(a.getName());
						if (count < hcm.getNeedNum()) {
							player.sendError(Translate.物品不足);
							return false;
						}
						for(int i=0; i<hcm.getNeedNum(); i++) {
							removeResult = player.removeArticle(a.getName(), "坐骑升级颜色扣除");
							if (!removeResult) {
								logger.warn("[新坐骑系统] [升级坐骑颜色失败] [删除物品不成功] [已删除数量:" + i + "] [" + player.getLogString() + "]");
								return false;
							}
						}
						hod.setColorType(newColor);
						horse.setColorType(newColor);
						horse.setOtherData(hod);
						horse.initBasicAttr();
						player.sendError(Horse2Manager.instance.translate.get(20));
						logger.warn("[新坐骑系统] [升级坐骑颜色成功] [" + player.getLogString() + "] [坐骑id:" + horseId + "] [颜色:" + horse.getColorType() + "]");
						return true;
					}
				} else {
					logger.warn("[新坐骑系统] [升级坐骑颜色失败] [获取背包为空] [" + player.getLogString() + "]");
				}
			}
		}
		return false;
	}
	
	/**
	 * 升级坐骑血脉星级
	 * @param player
	 * @param horseId
	 * @return
	 */
	public boolean bloodStarStrong(Player player, long horseId) {
		synchronized (player) {
			Horse horse = HorseManager.getInstance().getHorseById(horseId, player);
			if(horse == null) {
				logger.warn("[新坐骑系统] [获取玩家当前元神坐骑异常] [" + player.getLogString() + "] [" + horseId + "]");
				return false;
			}
			if (horse.isFly()) {
				player.sendError(Translate.飞行坐骑不可进行此操作);
				return false;
			}
			BloodStarModel bsm = Horse2Manager.instance.bloodStarMap.get(horse.getBloodStar());
			HorseColorModel hcm = Horse2Manager.instance.naturalRateMap.get(horse.getColorType());
			PetsOfPlayer bean0 = Pet2Manager.getInst().findPetsOfPlayer(player);
			if (bsm != null && bean0 != null) {
				if (bean0.getXueMai() < bsm.getNeedBloodNum()) {
					player.sendError(Translate.血脉不足);
					return false;
				}
				HorseOtherData hod = horse.getOtherData();
				int newLevel = hod.getBloodStar() + 1;
				if (newLevel > MAX_BLOOD_STAR) {
					player.sendError(Translate.已升至最大);
					return false;
				}
				if(newLevel > hcm.getMaxStar()) {
					player.sendError(Translate.颜色太低);
					return false;
				}
				bean0.setXueMai(bean0.getXueMai() - bsm.getNeedBloodNum());
				hod.setBloodStar(newLevel);
				horse.setBloodStar(newLevel);
				horse.setOtherData(hod);
				horse.initBasicAttr();
				String str = String.format(Horse2Manager.instance.translate.get(15), bsm.getNeedBloodNum());
				player.sendError(str);
				try {
					EventWithObjParam evt3 = new EventWithObjParam(com.fy.engineserver.event.Event.RECORD_PLAYER_OPT, new Object[] { player.getId(), RecordAction.坐骑升级最大血脉阶, newLevel});
					EventRouter.getInst().addEvent(evt3);
				} catch (Exception eex) {
					PlayerAimManager.logger.error("[目标系统] [统计宠物坐骑血脉最大阶数异常][" + player.getLogString() + "]", eex);
				}
				return true;
			}
		}
		return false;
	}
	/**
	 * 坐骑阶升星
	 * @param playerId
	 * @param type  培养类型(1为免费 3 道具培养   2为花费银子培养)
	 * @return
	 */
	public byte rankStarStrong(Player player, long horseId, byte type, boolean confirm) {
		synchronized (player) {
			Horse horse = HorseManager.getInstance().getHorseById(horseId, player);
			if(horse == null) {
				Horse2Manager.logger.warn("[新坐骑系统] [获取玩家当前元神坐骑异常] [" + player.getLogString() + "] [" + horseId + "]");
				return -1;
			}
			if (horse.isFly()) {
				player.sendError(Translate.飞行坐骑不可进行此操作);
				return -1;
			}
			if (horse.getRank() >= MAX_RANK_STAR) {
				player.sendError(Translate.已升至最大);
				return -1;
			}
			/**
			 * 暂时屏蔽扩阶系统
			 */
//			if (horse.getRank() >= (MAX_RANK_STAR + horse.getOtherData().getUpLevel())) {
//				String articleName = ArticleManager.upRankLevelNeedArticle.get(horse.getRank());
//				if (articleName != null) {
//					player.sendError(Translate.translateString(Translate.继续升阶需要, new String[][] { { Translate.STRING_1, articleName } }));
//				} else {
//					player.sendError(Translate.已升至最大);
//				}
//				player.sendError(Translate.已升至最大);
//				return -1;
//			}
			for (int i=0; i<Horse2Manager.levelLimit4Rank.length; i++) {
				if (player.getSoulLevel() <= Horse2Manager.levelLimit4Rank[i] && horse.getRank() >= Horse2Manager.maxRank[i]) {
					String str = Horse2Manager.instance.translate.get(11);
					try {
						str = String.format(str, (Horse2Manager.levelLimit4Rank[i] + 1));
					} catch (Exception e){}
					player.sendError(str);
					return -1;
				}
			}
			HorseRankModel hrm = Horse2Manager.instance.rankModelMap.get(horse.getRank());
			HorseBaseModel bm = Horse2Manager.instance.baseModel;
			Horse2RelevantEntity entity = this.getEntity(player.getId());
			if (type != 1 && type !=2 ) {
				Horse2Manager.logger.warn("[新坐骑系统] [未知的培养类型] [" + player.getLogString() + "] [" + horseId + "][培养类型:" + type + "]");
				return -1;
			}
			if(hrm != null && entity != null) {
				int[] problems = new int[]{0,0,0};		//免费阶培养或花钱阶培养暴击概率
				int[] muls = new int[]{1,1,1};		//免费阶培养或花钱阶培养暴击倍数
				int free2Next = 0;		//直接升级概率
				boolean useArticle = false;
				boolean isuseArti = false;
				if (type == 1) {
					if (entity.getCultureTime4Free() < bm.getFreeTimes4Rank()) {
						problems = hrm.getFreeMulProbabbly();
						muls = hrm.getFreeMul();
						free2Next = hrm.getFree4Next();
					} else {
						player.sendError(Horse2Manager.instance.translate.get(16));
						return -1;
						/*Knapsack bag = player.getKnapsack_common();
						if(bag != null) {
							Article a = ArticleManager.getInstance().getArticleByCNname(bm.getNeedArticle4Rank());
							if(a != null) {
								boolean removeResult = player.removeArticle(a.getName(),"坐骑升阶扣除");
								if(!removeResult) {
									player.sendError(Translate.text_feed_silkworm_006);
									return -1;
								}
								useArticle = true;
//								entity.setCultureTime(entity.getCultureTime()+1);
								problems = hrm.getFreeMulProbabbly();
								muls = hrm.getFreeMul();
								free2Next = hrm.getFree4Next();
							} else {
								logger.warn("[新坐骑系统] [免费阶培养需要道具配表没找到] [配置的物品统计名为:" + bm.getNeedArticle4Rank() + "]");
								player.sendError(Translate.text_feed_silkworm_006);
								return -1;
							}
						} else {
							logger.warn("[新坐骑系统] [玩家背包为空] [" + player.getLogString() + "]");
							player.sendError(Translate.text_feed_silkworm_006);
							return -1;
						}
//						player.sendError(Translate.免费次数已用尽);
//						return -1;
*/					}
				} else if (type == 3) {
					Knapsack bag = player.getKnapsack_common();
					if(bag != null) {
						Article a = ArticleManager.getInstance().getArticleByCNname(bm.getNeedArticle4Rank());
						if(a != null) {
							if (!confirm) {
								//弹确认框
								popConfirmWindow(player,horseId, -1, type, a.getName());
								return -2;
							}
							boolean removeResult = player.removeArticle(a.getName(),"坐骑升阶扣除");
							if(!removeResult) {
								player.sendError(Translate.text_feed_silkworm_006);
								return -1;
							}
							useArticle = true;
//							entity.setCultureTime(entity.getCultureTime()+1);
							problems = hrm.getFreeMulProbabbly();
							muls = hrm.getFreeMul();
							free2Next = hrm.getFree4Next();
						} else {
							logger.warn("[新坐骑系统] [免费阶培养需要道具配表没找到] [配置的物品统计名为:" + bm.getNeedArticle4Rank() + "]");
							player.sendError(Translate.text_feed_silkworm_006);
							return -1;
						}
					} else {
						logger.warn("[新坐骑系统] [玩家背包为空] [" + player.getLogString() + "]");
						player.sendError(Translate.text_feed_silkworm_006);
						return -1;
					}
//					player.sendError(Translate.免费次数已用尽);
//					return -1;
				} else if (type == 2) {
					Knapsack bag = player.getKnapsack_common();
					if(bag != null) {
						Article a = ArticleManager.getInstance().getArticleByCNname(bm.getNeedArticle4Rank());
						if(a != null) {
							isuseArti = player.removeArticle(a.getName(),"坐骑升阶扣除");
						}
					}
					if (!isuseArti) {
						int tempTimes = entity.getCultureTime();
						long cost = bm.getCost4RankByTimes(tempTimes) * 1000;
						if (!confirm) {
							//弹确认框
							popConfirmWindow(player, horseId, cost/1000, (byte)2, "");
							return -2;
						}
						BillingCenter bc = BillingCenter.getInstance();
						if ((player.getSilver() + player.getShopSilver()) >= cost) {
							try {
								bc.playerExpense(player, cost, CurrencyType.SHOPYINZI, ExpenseReasonType.坐骑阶培养, "新坐骑系统");
								player.sendError(String.format(Translate.坐骑培养扣除银子, (int)(cost / 1000)));
							} catch (Exception e) {
								logger.error("[新坐骑系统] [对宠物进行阶培养,扣费失败] [失败] [" + player.getLogString() + "] [今天已培养次数:" + entity.getCultureTime() + "]", e);
								player.sendError(Translate.元宝不足);
								return -1;
							}
						} else {
							player.sendError(Translate.元宝不足);
							return -1;
						}
					}
					problems = hrm.getCostMulProbabbly();
					muls = hrm.getCostMul();
					free2Next = hrm.getCost4Next();
				} else {
					logger.error("[新坐骑系统] [未知的培养类型] [" + player.getLogString() + "] [今天已培养次数:" + entity.getCultureTime() + "] [类型:" + type + "]");
					return -1;
				}
				if (type == 1) {
					entity.setCultureTime4Free(entity.getCultureTime4Free()+1);
				} else if (type == 2 && !isuseArti) {
					entity.setCultureTime(entity.getCultureTime()+1);
				}
				int mul = 1;
				int addExp = bm.getExp4Rank();
				int ranNum = player.random.nextInt(1000);
				for(int i=0; i<problems.length; i++) {
					if(ranNum <= problems[i] && mul < muls[i]) {
						mul = muls[i];
					}
				}
				boolean toNextLevel = false;
				if(ranNum < free2Next) {
					toNextLevel = true;
				}
				
				HorseOtherData hod = horse.getOtherData();
				int rankLevel = hod.getRankStar();
				int oldLevel = rankLevel;
				if(toNextLevel) {
					hod.setRankStar(rankLevel + 1);
					horse.setRank(hod.getRankStar());
					hod.setTrainExp(0);
					horse.setOtherData(hod);
				} else {
					addExp *= mul;
					long tempExp = hod.getTrainExp() + addExp;
					HorseRankModel tempHrm = Horse2Manager.instance.rankModelMap.get(rankLevel);
					while (/* (tempExp - tempHrm.getNeedExp()) >= 0 && */rankLevel < (MAX_RANK_STAR + horse.getOtherData().getUpLevel())) {
						tempHrm = Horse2Manager.instance.rankModelMap.get(rankLevel);
						if ((tempExp - tempHrm.getNeedExp()) >= 0) {
							tempExp -= tempHrm.getNeedExp();
							rankLevel++;
						} else {
							break;
						}
					}
					HorseRankModel hrm2 = Horse2Manager.instance.rankModelMap.get(rankLevel);
					if (hrm2.getOpenSkillNum() > horse.getSkillNum()) {
//						player.sendError(String.format(Translate.开启新技能格, hrm2.getOpenSkillNum()));
//						player.sendError(Translate.translateString(Translate.开启新技能格, new String[][] { { Translate.COUNT_1, hrm2.getOpenSkillNum()+"" }}));
					}
					hod.setTrainExp(tempExp);
					hod.setRankStar(rankLevel);
					horse.setRank(rankLevel);
					horse.setOtherData(hod);
				}
				if (rankLevel > oldLevel) {
					horse.initBasicAttr();
					try {
						EventWithObjParam evt3 = new EventWithObjParam(com.fy.engineserver.event.Event.RECORD_PLAYER_OPT, new Object[] { player.getId(), RecordAction.坐骑升级最大阶数, rankLevel});
						EventRouter.getInst().addEvent(evt3);
					} catch (Exception eex) {
						PlayerAimManager.logger.error("[目标系统] [统计宠物最大炼妖值异常][" + player.getLogString() + "]", eex);
					}
				}
				logger.warn("[新坐骑系统] [阶培养] [成功] [暴击倍数：" + mul + "] [oldLevel:"+oldLevel+"] [随机数:" + ranNum + "] [free2Next:"+free2Next+"] [增加经验:" + addExp + "] [是否直接升级:"+toNextLevel+"] [" + player.getLogString() + "] [horseId :" + horseId + "] [阶星级:" + rankLevel + "] [经验:"+ horse.getOtherData().getTrainExp() + "] [培养类型:" + type + "] [是否使用道具:" + useArticle + "]");
				return (byte) mul;
			}
			logger.warn("[新坐骑系统] [通过坐骑等阶没有取到相应model] [" + player.getLogString() + "] [" + horse.getRank() + "] [" + entity + "]");
		}
		return -1;
	}
	
	private void popConfirmWindow(Player p, long horseId, long cost, byte peiyangType, String articleName) {
		WindowManager wm = WindowManager.getInstance();
		MenuWindow mw = wm.createTempMenuWindow(600);
		Option_confirmRankStronger option1 = new Option_confirmRankStronger();
		option1.setText(MinigameConstant.CONFIRM);
		option1.setHorseId(horseId);
		option1.setPeiyangType(peiyangType);
		Option_Cancel option2 = new Option_Cancel();
		option2.setText(MinigameConstant.CANCLE);
		Option[] options = new Option[] {option1, option2};
		mw.setOptions(options);
		String msg = "";
		if (cost > 0) {
			msg = String.format(Horse2Manager.instance.translate.get(8), cost);
		} else {
			msg = String.format(Horse2Manager.instance.translate.get(18), articleName);
		}
		mw.setDescriptionInUUB(msg);
		CONFIRM_WINDOW_REQ creq = new CONFIRM_WINDOW_REQ(GameMessageFactory.nextSequnceNum(), mw.getId(), mw.getDescriptionInUUB(), options);
		p.addMessageToRightBag(creq);
	}
	
	public Horse2RelevantEntity getEntity(long playerId) {
		Horse2RelevantEntity entity = (Horse2RelevantEntity) cache.get(playerId);
		if(entity == null) {
			try {
				entity = em.find(playerId);
				if(entity == null) {
					entity = new Horse2RelevantEntity(playerId);
					em.notifyNewObject(entity);
					cache.put(playerId, entity);
					if (logger.isDebugEnabled()) {
						logger.debug("[新坐骑系统] [从数据库加载数据] [成功] [playerId : " + playerId + "]");
					}
				}
				if (logger.isDebugEnabled()) {
					logger.debug("[新坐骑系统] [从数据库加载数据] [成功] [playerId : " + playerId + "]");
				}
			} catch (Exception e) {
				logger.error("[新坐骑系统] [从数据库加载数据异常] [playerId : " + playerId + "]", e);
			}
		}
		if (!tempCache.containsKey(playerId)) {
			tempCache.put(playerId, entity);
		}
		return entity;
	}
	
	public void notifyRemoveFromCache(Horse2RelevantEntity entity) {
		try {
			em.save(entity);
		} catch (Exception e) {
			logger.error("[新坐骑系统] [异常] [移除保存错误] [" + entity.getId() + "]", e);
		}
	}
	/**
	 * 每天零点清除所有在线玩家培养、刷新技能次数
	 */
	private void resetHorseRelevantData(Player player) {
		Horse2RelevantEntity entity = this.getEntity(player.getId());
		if(entity != null) {
			entity.initData();
		}
	}
	/**
	 * 检查玩家已有的坐骑--将高于一阶的非飞行坐骑转换成随便发放给玩家(检测玩家坐骑身上的装备，也需要发放给玩家)
	 * @param player
	 */
	public void checkPlayerHorse(Player player) {
		try {
			synchronized (player) {
				Soul soul = player.getSoul(Soul.SOUL_TYPE_BASE);
				Soul soul2 = player.getSoul(Soul.SOUL_TYPE_SOUL);
				if (soul != null) { 
					transHorse2Pipe(player, soul);			//本尊	
					if (soul2 != null) {
						transHorse2Pipe(player, soul2);			//元神
					}
				} else {
					logger.error("[新坐骑系统] [玩家本尊为null] [坐骑转换] [失败] [" + player.getLogString() + "]");
				}
			}
		} catch (Exception e) {
			logger.error("[新坐骑系统] [检测并删除玩家现有坐骑异常] [" + player.getLogString() + "]", e);
		}
	}
	
	private void transHorse2Pipe(Player player, Soul soul) {
		ArrayList<Long> horses = soul.getHorseArr();
		List<Long> tempList = new ArrayList<Long>();
		int givePropNum = 0;
		for (int i=0; i<horses.size(); i++) {
			Horse horse = HorseManager.getInstance().getHorseByHorseId(player, horses.get(i), soul);
			if (horse != null) {
				Article article = ArticleManager.getInstance().getArticle(horse.getHorseName());
				if (article != null && article instanceof HorseProps && !((HorseProps)article).isFly() && ((HorseProps)article).getRank() > 0) {
					HorseEquipmentColumn ec = horse.getEquipmentColumn();
					tempList.add(horses.get(i));
					try {
						if (ec != null && ec.getEquipmentIds() != null) {
							List<ArticleEntity> laes = new ArrayList<ArticleEntity>();
							for (long l : ec.getEquipmentIds()) {
								if (l > 0) {
									ArticleEntity ae = DefaultArticleEntityManager.getInstance().getEntity(l);
									if (ae != null) {
										laes.add(ae);
									}
								}
							}
							if (laes.size() > 0) {
								int[] tempNum = new int[laes.size()];
								for (int ii=0; ii<tempNum.length;ii++) {
									tempNum[ii] = 1;
								}
								MailManager.getInstance().sendMail(player.getId(), laes.toArray(new ArticleEntity[0]), tempNum, Translate.系统, Translate.坐骑转换, 0L, 0L, 0L, "坐骑转换获得");
								if (logger.isWarnEnabled()) {
									logger.warn("[新坐骑系统] [老坐骑转换发送邮件] [成功] [邮件物品:" + laes + "] [" + player.getLogString() + "]");
								}
							}
						}
					} catch (Exception e) {
						logger.error("[新坐骑系统] [转换玩家现有坐骑异常] [" + player.getLogString() + "]", e);
					}
					givePropNum += horseChangePropNums[((HorseProps)article).getRank()];
				}
			}
		}
		for (long ids : tempList) {
			boolean result = horses.remove(ids);
			if (player.isIsUpOrDown() && player.getRidingHorseId() == ids) {
				player.downFromHorse();
			}
			if (player.getHorseArr() != null) {
				player.getHorseArr().remove(ids);
			}
			if (player.getHorseIdList() != null) {
				player.getHorseIdList().remove(ids);
			}
			if (logger.isWarnEnabled()) {
				logger.warn("[新坐骑系统] [删除玩家身上坐骑] [结果:" + result + "] [坐骑id:" + ids + "] [" + player.getLogString() + "]");
			}
		}
		if (soul.getSoulType() == player.getCurrSoul().getSoulType()) {
			player.setHorseIdList(horses);
		} else {
			player.setHorseIdList4UnusedSoul(horses);
		}
		if (givePropNum > 0) {			//坐骑兑换的碎片发放
			try {
				Article a = ArticleManager.getInstance().getArticle(horseChangePropName);
				if (a != null) {
					ArticleEntity ae = DefaultArticleEntityManager.getInstance().createEntity(a, true, ArticleEntityManager.新坐骑碎片转换, player, a.getColorType(), 1, true);
					MailManager.getInstance().sendMail(player.getId(), new ArticleEntity[]{ae}, new int[]{givePropNum}, Translate.系统, Translate.坐骑转换, 0L, 0L, 0L, "坐骑转换获得");
					if (logger.isWarnEnabled()) {
						logger.warn("[新坐骑系统] [老坐骑转换发送邮件] [成功] [邮件物品:" + ae.getId() + "] [" + player.getLogString() + "]");
					}
				} else {
					logger.warn("[新坐骑系统] [转换玩家现有坐骑成碎片] [失败] [碎片物品不存在] [" + player.getLogString() + "] [物品名：" + horseChangePropName + "]");
				}
			} catch (Exception e) {
				logger.error("[新坐骑系统] [转换玩家现有坐骑异常] [" + player.getLogString() + "]", e);
			}
		}
	}

	@Override
	public void proc(Event evt) {
		// TODO Auto-generated method stub
		try {
			switch (evt.id) {
			case Event.SERVER_DAY_CHANGE:
				Player[] players = GamePlayerManager.getInstance().getOnlinePlayers();
				for(int i=0; i<players.length; i++) {
					resetHorseRelevantData(players[i]);
				}
				break;
			case Event.PLAYER_LOGIN:
				if(evt instanceof EventOfPlayer) {
					EventOfPlayer e = (EventOfPlayer) evt;
					this.resetHorseRelevantData(e.player);
					int id = this.isPlayerCanUpgradeHorseFree(e.player);
					if (id > 0) {
						NOTICE_PARTICLE_REQ req = new NOTICE_PARTICLE_REQ(GameMessageFactory.nextSequnceNum(), 1, id);
						e.player.addMessageToRightBag(req);
					}
					BournCfg bournCfg = BournManager.getInstance().getBournCfg(e.player.getClassLevel());
					try {
						if (e.player.getBournExp() >= bournCfg.getExp()) {
							NOTICE_PARTICLE_REQ req2 = new NOTICE_PARTICLE_REQ(GameMessageFactory.nextSequnceNum(), 1, 7);
							e.player.addMessageToRightBag(req2);
						}
					} catch (Exception e2) {
						
					}
				}
				break;
			case Event.PICK_FRUIT:		//摘取果实伴生
				EventWithObjParam eo = (EventWithObjParam) evt;
				Object[] oj = (Object[]) eo.param;
				Player player = (Player) oj[0];
				int outputColor = Integer.parseInt(oj[1]+"");
				if (outputColor >= 3) {
					player.notifyPickFruit();
				}
				//3个捐献阶段开启的时候产出对应的材料
				SealManager.getInstance().sealArticleOutPut(player, outputColor);
				
				if (logger.isDebugEnabled()) {
					logger.debug("[偷取果实buff] [偷取果实颜色:" + outputColor + "] [" + player.getLogString() + "]");
				}
				break;
			case Event.AUTO_FEED_HORSE:
				EventWithObjParam eo1 = (EventWithObjParam) evt;
				Object[] oj1 = (Object[]) eo1.param;
				if (logger.isDebugEnabled()) {
					logger.debug("[收到喂养坐骑事件] [" + Arrays.toString(oj1) + "]");
				}
				this.notifyAutoFeedHorse(Long.parseLong(oj1[0] + ""), Long.parseLong(oj1[1] + ""));
				break;
			default:
				break;
			}
		} catch (Exception e) {
			logger.error("[horse2Entitymanager] [处理事件异常] [事件id:" + evt.id + "]", e);
		}
	}
	
	private void notifyAutoFeedHorse(long playerId, long horseId) {
		try {
			Player player = GamePlayerManager.getInstance().getPlayer(playerId);
			if (logger.isDebugEnabled()) {
				logger.debug("[喂养坐骑] [自动喂养坐骑] [失败] [自动喂养开关:" + player.autoFeedHorse + "] [喂养最低限:" + player.autoFeedLine + "] [喂养物品:" + player.feedArticleName + "] [是否自动购买:" + player.autoBuyArticle + "]");
			}
			if (player.feedArticleName == null || player.feedArticleName.isEmpty()) {
				logger.warn("[喂养坐骑] [自动喂养坐骑] [失败] [自动喂养开关:" + player.autoFeedHorse + "] [喂养最低限:" + player.autoFeedLine + "] [喂养物品:" + player.feedArticleName + "] [是否自动购买:" + player.autoBuyArticle + "]");
				return ;
			}
			int count = player.countArticleInKnapsacksByName(player.feedArticleName);
			boolean buyResult = false;
			if (count <= 0) {
				if (!player.autoBuyArticle) {
					return ;
				}
				String shopName = ShopManager.得到随身商店名字(player.getLevel());
				Shop shop = ShopManager.getInstance().getShop(shopName, player);
				int goodsId = shop.getGoodsIdByGoodName(player.feedArticleName);
				Goods gs = shop.getGoodsByGoodName(player.feedArticleName);
//				if (player.getBindSilver() < gs.getPrice() * 5) {
//					return ;
//				}
				if (goodsId >= 0) {
					if (gs != null && !player.isUseSiliver && player.getBindSilver() < gs.getPrice() * 5) {
						return ;
					} else if (gs != null && player.isUseSiliver && (player.getShopSilver() + player.getBindSilver() + player.getSilver()) >= gs.getPrice() * 5) {
						buyResult = shop.buyGoods(player, goodsId, 5, 0);
					}
				}
			}
			Knapsack bag = player.getKnapsack_common();
			int idx = bag.indexOf(player.feedArticleName);
			if (logger.isDebugEnabled()) {
				logger.debug("[新坐骑] [自动喂养坐骑] [" + player.getLogString() + "] [buyResult:" + buyResult + "] [articleName:" + player.feedArticleName + "] [bagIdx:" + idx + "]");
			}
			if (idx >= 0) {
				ArticleEntity ae = player.getArticleEntity(player.feedArticleName);
				if (ae != null) {
					Horse horse = HorseManager.getInstance().getHorseById(horseId, player);
					if (horse.getEnergy() < player.autoFeedLine) {
						player.feedHorse(ae.getId(), horse);
					}
				}
			}
		} catch (Exception e) {
			logger.error("[新坐骑] [自动喂养坐骑] [异常] [playerId : " + playerId + "] [horseId : " + horseId + "]" );
		}
		
	}
	
	
	public void destory() {
		if (em != null) {
			em.destroy();
			logger.info("**************************************[服务器关闭][horse2entityManager调用destory]*********************************************");
		}
	}

	@Override
	public void doReg() {
		// TODO Auto-generated method stub
		EventRouter.register(Event.PLAYER_LOGIN, this);
		EventRouter.register(Event.SERVER_DAY_CHANGE, this);
		EventRouter.register(Event.PICK_FRUIT, this);
		EventRouter.register(Event.AUTO_FEED_HORSE, this);
	}

}
