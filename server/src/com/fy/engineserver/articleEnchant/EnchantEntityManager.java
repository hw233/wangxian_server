package com.fy.engineserver.articleEnchant;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import org.slf4j.Logger;

import com.fy.engineserver.achievement.AchievementManager;
import com.fy.engineserver.achievement.RecordAction;
import com.fy.engineserver.articleEnchant.model.EnchantArticle;
import com.fy.engineserver.articleEnchant.model.EnchantModel;
import com.fy.engineserver.articleEnchant.model.EnchantTempModel;
import com.fy.engineserver.core.CoreSubSystem;
import com.fy.engineserver.datasource.article.concrete.DefaultArticleEntityManager;
import com.fy.engineserver.datasource.article.data.articles.Article;
import com.fy.engineserver.datasource.article.data.entity.ArticleEntity;
import com.fy.engineserver.datasource.article.data.entity.EquipmentEntity;
import com.fy.engineserver.datasource.article.data.equipments.Equipment;
import com.fy.engineserver.datasource.article.data.props.Knapsack;
import com.fy.engineserver.datasource.article.manager.ArticleManager;
import com.fy.engineserver.datasource.buff.Buff;
import com.fy.engineserver.datasource.language.Translate;
import com.fy.engineserver.economic.BillingCenter;
import com.fy.engineserver.economic.CurrencyType;
import com.fy.engineserver.economic.ExpenseReasonType;
import com.fy.engineserver.mail.service.MailManager;
import com.fy.engineserver.menu.MenuWindow;
import com.fy.engineserver.menu.Option;
import com.fy.engineserver.menu.Option_Cancel;
import com.fy.engineserver.menu.Option_ConfirmLockEnchant;
import com.fy.engineserver.menu.WindowManager;
import com.fy.engineserver.message.CONFIRM_WINDOW_REQ;
import com.fy.engineserver.message.GameMessageFactory;
import com.fy.engineserver.message.NOTICE_CLIENT_READ_TIMEBAR_REQ;
import com.fy.engineserver.message.QUERY_ARTICLE_INFO_RES;
import com.fy.engineserver.minigame.MinigameConstant;
import com.fy.engineserver.playerAims.manager.PlayerAimeEntityManager;
import com.fy.engineserver.sprite.Player;
import com.fy.engineserver.sprite.TimerTask;
import com.fy.engineserver.stat.ArticleStatManager;
import com.sqage.stat.client.StatClientService;
import com.sqage.stat.model.FuMoFlow;
import com.xuanzhi.boss.game.GameConstants;
import com.xuanzhi.tools.simplejpa.SimpleEntityManager;
import com.xuanzhi.tools.simplejpa.SimpleEntityManagerFactory;

public class EnchantEntityManager {
	
	public static EnchantEntityManager instance;
	
	public static SimpleEntityManager<EnchantData> em;
	
	public static Logger logger = EnchantManager.logger;
	
	public static final int 附魔类型_属性 = 1;
	public static final int 附魔类型_触发buff = 2;
	public static final int 附魔类型_触发减免效果 = 3;
	
	public static final int 外攻 = 1;
	public static final int 内攻 = 2;
	public static final int 气血 = 3;
	public static final int 暴击 = 4;
	public static final int 命中 = 5;
	public static final int 精准 = 6;
	public static final int 破甲 = 7;
	public static final int 减少控制技能时间 = 8;
	public static final int 双防 = 9;
	
	
	public static final byte 主动攻击 = 1;
	public static final byte 受到控制类技能 = 2;
	public static final byte 被攻击 = 3;
	
	public static final long 触发间隔 = 1000;
	
	public void init() {
		instance = this;
		em = SimpleEntityManagerFactory.getSimpleEntityManager(EnchantData.class);
	}
	
	public void destory() {
		if (em != null) {
			em.destroy();
		}
	}
	
	public static int 附魔开放等级 = 110;
	/**
	 * 检测控制类技能概率免疫
	 * @param player
	 * @param buff
	 * @return
	 */
	public boolean checkControlBuff(Player player, Buff buff) {
		if (buff instanceof ControlBuff) {
			boolean b = player.checkPassiveEnchant(EnchantEntityManager.受到控制类技能) == 100;
			return b;
		}
		return false;
	}
	
	/**
	 * 
	 * @param player
	 * @param equipmentId  装备id
	 * @param knapeIndex   附魔道具背包索引
	 * @param confirm  是否确定替换原有
	 */
	public boolean enchant(Player player, long equipmentId, int knapeIndex, boolean confirm) {
		try {
			if (player.getLevel() < 附魔开放等级) {
				player.sendError(Translate.等级不足不开放);
				return false;
			}
			Knapsack bag = player.getKnapsack_common();
			if (bag == null) {
				player.sendError(Translate.服务器物品出现错误);
				return false;
			}
			ArticleEntity ae = bag.getArticleEntityByCell(knapeIndex);
			if (ae == null) {
				player.sendError(String.format(Translate.附魔材料不存在, knapeIndex + ""));
				return false;
			}
			Article a = ArticleManager.getInstance().getArticle(ae.getArticleName());
			if (a == null || !(a instanceof EnchantArticle)) {
				player.sendError(Translate.不是附魔道具);
				return false;
			}
			EnchantArticle ea = (EnchantArticle) a;
			EnchantModel model = EnchantManager.instance.modelMap.get(ea.getEnchantId());	//附魔模板
			if (model == null) {
				player.sendError(Translate.服务器出现错误);
				return false;
			}
			ArticleEntity equipt = DefaultArticleEntityManager.getInstance().getEntity(equipmentId);			
			if (!bag.contains(equipt)) {
				player.sendError(Translate.附魔装备不在背包中);
				return false;
			}
			if (equipt == null || !(equipt instanceof EquipmentEntity)) {
				player.sendError(Translate.附魔装备不在背包中);
				return false;
			}
			Article ee = ArticleManager.getInstance().getArticle(equipt.getArticleName());
			if (ee == null || !(ee instanceof Equipment)) {
				player.sendError(Translate.只有装备可以附魔);
				return false;
			}
			if (model.getEquiptLevelLimit() > ((Equipment)ee).getPlayerLevelLimit()) {
				player.sendError(Translate.装备等级太低);
				return false;
			}
			int eeType = ((Equipment)ee).getEquipmentType();
			if (!EnchantManager.instance.isOpenEnchant(eeType)) {
				player.sendError(Translate.此类装备未开放附魔功能);
				return false;
			}
			if (eeType != ea.getEquipmentType() || eeType != model.getEquiptmentType()) {
				player.sendError(Translate.此装备不能使用这种附魔);
				return false;
			}
			// 判断物品上是否已经有附魔，有则需要提示确认
			EnchantData entity = getEntity((EquipmentEntity) equipt);
			if (entity == null) {		//创建一个新的附魔
				ArticleEntity removeAe = bag.removeByArticleId(ae.getId(), "附魔删除", true);
				if (removeAe != null) {
					entity = new EnchantData();
					entity.setId(equipt.getId());
					EnchantAttr eattr = createNewEnchant(model, player.random, ea);
					entity.getEnchants().add(eattr);
					em.notifyNewObject(entity);
				} else {
					logger.warn("[附魔] [失败] [删除物品不成功] [" + player.getLogString4Knap() + "] [equiptId :" + equipmentId + "] [附魔道具背包id:" + knapeIndex + "]");
					player.sendError(Translate.删除物品不成功);
					return false;
				}
			} else if (entity.getEnchants().size() <= 0) {		
				ArticleEntity removeAe = bag.removeByArticleId(ae.getId(), "附魔删除", true);
				if (removeAe != null) {
					EnchantAttr eattr = createNewEnchant(model, player.random, ea);
					entity.getEnchants().add(eattr);
					em.notifyFieldChange(entity, "enchants");
					sendArticleStat(player, removeAe, "附魔删除");
				} else {
					logger.warn("[附魔] [失败] [删除物品不成功] [" + player.getLogString4Knap() + "] [equiptId :" + equipmentId + "] [附魔道具背包id:" + knapeIndex + "]");
					player.sendError(Translate.删除物品不成功);
					return false;
				}
			} else {
				if (!confirm) {
					//提示替换确认
					popConfirmWindow2(player, equipmentId, knapeIndex);
					return false;
				}
				ArticleEntity removeAe = bag.removeByArticleId(ae.getId(), "附魔删除", true);
				if (removeAe != null) {
//					try {
//						EnchantAttr eatt = entity.getEnchants().get(0);
//						EnchantModel tempModel = EnchantManager.instance.modelMap.get(eatt.getEnchantId());
//						sendEnchantDataStat(player, tempModel.getEnchatName(), equipt.getId(), 2);
//					} catch (Exception e) {
//						EnchantEntityManager.logger.warn("[发送统计] [异常]", e);
//					}
//					unLoadEnchantAttr(player, (EquipmentEntity) equipt);
					EnchantAttr eattr = createNewEnchant(model, player.random, ea);
					entity.getEnchants().clear();
					entity.getEnchants().add(eattr);
					em.notifyFieldChange(entity, "enchants");
					sendArticleStat(player, removeAe, "附魔删除");
				} else {
					logger.warn("[附魔] [失败] [删除物品不成功] [" + player.getLogString4Knap() + "] [equiptId :" + equipmentId + "] [附魔道具背包id:" + knapeIndex + "]");
					player.sendError(Translate.删除物品不成功);
					return false;
				}
			}
			((EquipmentEntity) equipt).setEnchantData(entity);
			sendEnchantDataStat(player, ae.getArticleName(), equipt.getId(), 1, eeType);
//			loadEnchantAttr(player, (EquipmentEntity) equipt);			//此时不用加属性。。已经改为脱装备附魔了
			logger.warn("[附魔] [成功] ["+player.getLogString4Knap()+"] [equiptId:" + equipt.getId() + "] [eName:" + equipt.getArticleName() + "] [" + entity.getLogString() + "]");
			QUERY_ARTICLE_INFO_RES res = new QUERY_ARTICLE_INFO_RES(GameMessageFactory.nextSequnceNum(), equipt.getId(), equipt.getInfoShow(player));
			player.addMessageToRightBag(res);
			try {
				RecordAction action1 = EnchantManager.actions[eeType];
				if (action1 != null) {
					AchievementManager.getInstance().record(player, action1);
				}
				boolean max = model.getAttrNums()[model.getAttrNums().length - 1] == entity.getEnchants().get(0).getAttrNum();
				if (max) {
					RecordAction action2= EnchantManager.actionMax[eeType];
					if (action2 != null) {
						AchievementManager.getInstance().record(player, action2);
					}
				}
			} catch (Exception e) {
				PlayerAimeEntityManager.logger.warn("[目标系统] [统计装备附魔] [异常] [" + player.getLogString() + "] ", e);
			}
			player.sendError(Translate.附魔成功);
			return true;
		} catch (Exception e) {
			logger.warn("[附魔] [异常] [" + player.getLogString4Knap() + "] [equiptId :" + equipmentId + "] [附魔道具背包id:" + knapeIndex + "]", e);
		}
		return false;
	}
	
	public static void sendArticleStat(Player player, ArticleEntity ae , String reason) {
		try {
			ArticleStatManager.addToArticleStat(player, null, ae, ArticleStatManager.OPERATION_物品获得和消耗, 0, ArticleStatManager.ARTICLES, 1, reason, null);
		} catch (Exception e) {
			
		}
	}
	
	private void popConfirmWindow2(Player p, long equiptIndex, int knapeIndex ) {
		WindowManager wm = WindowManager.getInstance();
		MenuWindow mw = wm.createTempMenuWindow(600);
		Option_confirmEnchant option1 = new Option_confirmEnchant();
		option1.setText(MinigameConstant.CONFIRM);
		option1.setEquiptIndex(equiptIndex);
		option1.setKnapeIndex(knapeIndex);
		Option_Cancel option2 = new Option_Cancel();
		option2.setText(MinigameConstant.CANCLE);
		Option[] options = new Option[] {option1, option2};
		mw.setOptions(options);
		mw.setDescriptionInUUB(Translate.确认替换原有附魔);
		CONFIRM_WINDOW_REQ creq = new CONFIRM_WINDOW_REQ(GameMessageFactory.nextSequnceNum(), mw.getId(), mw.getDescriptionInUUB(), options);
		p.addMessageToRightBag(creq);
	}
	
	/**
	 * 加载附魔属性
	 */
	public void loadEnchantAttr(Player player, EquipmentEntity ee) {
		EnchantData data = this.getEntity(ee);
		if (data == null || data.getEnchants().size() <= 0) {
			return ;
		}
		if (data.isLock()) {
			if (logger.isDebugEnabled()) {
				logger.debug("[加载附魔属性] [失败] [附魔效果已锁定] [" + data.getEnchants().get(0).getEnchantId() + "] [" + player.getLogString() + "]");
			}
			return ;
		}
		EnchantModel model = EnchantManager.instance.modelMap.get(data.getEnchants().get(0).getEnchantId());
		if (model == null) {
			logger.warn("[加载附魔属性] [异常] [不存在附魔id:" + data.getEnchants().get(0).getEnchantId() + "] [" + player.getLogString() + "]");
			return ;
		}
		int addValue = data.getEnchants().get(0).getAttrNum();
		if (logger.isDebugEnabled()) {
			logger.debug("[加载附魔属性] [" + player.getLogString() + "] [" + model + "] [addValue:" + addValue + "]");
		}
		if (model.getType() == 附魔类型_属性) {
			for (int i=0; i<model.getAddAttrTypes().length; i++) {
				switch (model.getAddAttrTypes()[i]) {
				case 外攻: player.setPhyAttackY(player.getPhyAttackY() + addValue);
					break;
				case 内攻:player.setMagicAttackY(player.getMagicAttackY() + addValue);
					break;
				case 气血:player.setMaxHpY(player.getMaxHpY() + addValue);
					break;
				case 暴击:player.setCriticalHitY(player.getCriticalHitY() + addValue);
					break;
				case 命中:player.setHitY(player.getHitY() + addValue);
					break;
				case 精准:player.setAccurateY(player.getAccurateY() + addValue);
					break;
				case 破甲:player.setBreakDefenceY(player.getBreakDefenceY() + addValue);
					break;
				case 减少控制技能时间:player.decreaseConTimeRate += addValue;
								player.decreaseArticleId = ee.getId();
					break;
				case 双防:
					player.setPhyDefenceY(player.getPhyDefenceY() + addValue);
					player.setMagicDefenceY(player.getMagicDefenceY() + addValue);
					break;
				default:
					break;
				}
			}
		} else if (model.getType() == 附魔类型_触发buff || model.getType() == 附魔类型_触发减免效果) {
			for (EnchantTempModel em : player.passiveEnchants) {
				if (em.getId() == model.getId()) {
					return ;
				}
			}
			player.passiveEnchants.add(new EnchantTempModel(model.getId(), ee.getId()));
		}
	}
	
	/**
	 * 减少控制时间掉耐久
	 */
	public void notifyCheckEnchant(Player player) {
		try {
			if (player.decreaseArticleId <= 0) {
				if (logger.isInfoEnabled()) {
					logger.info("[触发减少控制技能时间] [减少灵性失败] [" + player.getLogString() + "] [" + player.decreaseArticleId + "]");
				}
				return ;
			}
			EquipmentEntity ee = (EquipmentEntity) DefaultArticleEntityManager.getInstance().getEntity(player.decreaseArticleId);
			if (!ee.getEnchantData().lostDurable(player, ee, 1)) {			//附魔耐久消耗完移除附魔效果
				//EnchantEntityManager.instance.unLoadEnchantAttr(player, ee);
				try {
					String des = String.format(Translate.附魔消失邮件标题, ee.getArticleName());
					MailManager.getInstance().sendMail(player.getId(), new ArticleEntity[0], new int[0], Translate.附魔消失, des, 0L, 0L, 0L, "附魔消失");
					player.sendError(des);
				} catch (Exception e) {
					EnchantManager.logger.warn("[checkPassiveEnchant] [附魔消失邮件通知] [异常] [" + player.getLogString() + "]", e);
				}
			}
			try {
				if (ee.getEnchantData() != null && ee.getEnchantData() .getEnchants().size() > 0 && ee.getEnchantData().getEnchants().get(0).getDurable() < EnchantManager.耐久) {
					player.sendError(String.format(Translate.低于10点通知, ee.getArticleName()));
				}
			} catch (Exception e) {
				EnchantManager.logger.warn("[checkPassiveEnchant] [低于10点通知] [异常] [" + player.getLogString() + "]", e);
			}
			CoreSubSystem.notifyEquipmentChange(player, new EquipmentEntity[] { ee });
		} catch (Exception e) {
			logger.warn("[notifyCheckEnchant] [异常] [" + player.getLogString() + "]", e);
		}
	}
	
	/**
	 * 卸载附魔属性
	 */
	public void unLoadEnchantAttr(Player player, EquipmentEntity ee) {
		EnchantData data = this.getEntity(ee);
		if (data == null || data.getEnchants().size() <= 0) {
			return ;
		}
		if (data.isLock()) {
			if (logger.isDebugEnabled()) {
				logger.debug("[卸载附魔属性] [失败] [附魔效果已锁定] [" + data.getEnchants().get(0).getEnchantId() + "] [" + player.getLogString() + "]");
			}
			return ;
		}
		EnchantModel model = EnchantManager.instance.modelMap.get(data.getEnchants().get(0).getEnchantId());
		if (model == null) {
			logger.warn("[卸载附魔属性] [异常] [不存在附魔id:" + data.getEnchants().get(0).getEnchantId() + "] [" + player.getLogString() + "]");
			return ;
		}
		
		int addValue = data.getEnchants().get(0).getAttrNum();
		if (logger.isDebugEnabled()) {
			logger.debug("[卸载附魔属性] [" + player.getLogString() + "] [" + model + "] [addValue:" + addValue + "]");
		}
		if (model.getType() == 附魔类型_属性) {
			for (int i=0; i<model.getAddAttrTypes().length; i++) {
				switch (model.getAddAttrTypes()[i]) {
				case 外攻: player.setPhyAttackY(player.getPhyAttackY() - addValue);
					break;
				case 内攻:player.setMagicAttackY(player.getMagicAttackY() - addValue);
					break;
				case 气血:player.setMaxHpY(player.getMaxHpY() - addValue);
					break;
				case 暴击:player.setCriticalHitY(player.getCriticalHitY() - addValue);
					break;
				case 命中:player.setHitY(player.getHitY() - addValue);
					break;
				case 精准:player.setAccurateY(player.getAccurateY() - addValue);
					break;
				case 破甲:player.setBreakDefenceY(player.getBreakDefenceY() - addValue);
					break;
				case 减少控制技能时间:player.decreaseConTimeRate = player.decreaseConTimeRate - addValue;
								player.decreaseArticleId = -1;
					break;
				case 双防:
					player.setPhyDefenceY(player.getPhyDefenceY() - addValue);
					player.setMagicDefenceY(player.getMagicDefenceY() - addValue);
					break;
				default:
					break;
				}
			}
		} else if (model.getType() == 附魔类型_触发buff || model.getType() == 附魔类型_触发减免效果) {
			List<EnchantTempModel> needRemove = new ArrayList<EnchantTempModel>();
			for (int i=0; i<player.passiveEnchants.size(); i++) {
				if (player.passiveEnchants.get(i).getEquiptId() == ee.getId()) {
					needRemove.add(player.passiveEnchants.get(i));
				}
			}
			for (EnchantTempModel em : needRemove) {
				player.passiveEnchants.remove(em);
			}
		}
	}
	/**
	 * 
	 * @param player
	 * @param index
	 * @param articleId
	 * @param type					1 为附魔单个装备   2为全部附魔
	 * @param confirm
	 */
	public void openLockTimerTask(Player player, int index, long articleId, int type,boolean confirm) {
		int num = countNeedLockNum(player);
		if (num <= 0) {
			player.sendError("没有需要锁定的装备");
			return ;
		}
		if (!confirm) {
			WindowManager wm = WindowManager.getInstance();
			MenuWindow mw = wm.createTempMenuWindow(600);
			Option_ConfirmLockEnchant option1 = new Option_ConfirmLockEnchant();
			option1.setText(MinigameConstant.CONFIRM);
			option1.setEnchantType(type);
			option1.setIndex(index);
			option1.setArticleId(articleId);
			Option_Cancel option2 = new Option_Cancel();
			option2.setText(MinigameConstant.CANCLE);
			Option[] options = new Option[] {option1, option2};
			mw.setOptions(options);
			String costs = "";
			if (type == 1) {
				costs = BillingCenter.得到带单位的银两(EnchantManager.锁定附魔消耗);
			} else {
				long ct = EnchantManager.锁定附魔消耗 * num;			//查询一次锁定了几件装备
				costs = BillingCenter.得到带单位的银两(ct);			//全部锁定消耗
			}
			mw.setDescriptionInUUB(String.format(Translate.锁定附魔消耗, costs));			//全部锁定消耗
			CONFIRM_WINDOW_REQ creq = new CONFIRM_WINDOW_REQ(GameMessageFactory.nextSequnceNum(), mw.getId(), mw.getDescriptionInUUB(), options);
			player.addMessageToRightBag(creq);
			return ;
		}
		player.getTimerTaskAgent().createTimerTask(new EnchantTimerTask(player, index, articleId, type), EnchantManager.锁定读条时间,TimerTask.type_附魔锁定);
		player.addMessageToRightBag(new NOTICE_CLIENT_READ_TIMEBAR_REQ(GameMessageFactory.nextSequnceNum(), EnchantManager.锁定读条时间, Translate.锁定附魔));
	}
	
	public int countNeedLockNum(Player player) {
		int result = 0;
		for (int i=0; i<EnchantManager.开放附魔的位置.length; i++) {
			EquipmentEntity ae = (EquipmentEntity) player.getEquipmentColumns().get(EnchantManager.开放附魔的位置[i]);
			if (ae != null && ae.getEnchantData() != null && ae.getEnchantData().getEnchants().size() > 0 && !ae.getEnchantData().isLock()) {
				result++;
			}
		}
		return result;
	}
	
	/**
	 * 一键锁定
	 * @param player
	 * @param lockType   1为锁定   2为解锁
 	 * @return
	 */
	public boolean lockAllEnchant(Player player, int lockType) {
		if (lockType == 1) {
			int result = countNeedLockNum(player);
			if (result <= 0 ) {
				return false;
			}
			try {
				BillingCenter.getInstance().playerExpense(player, EnchantManager.锁定附魔消耗 * result, CurrencyType.BIND_YINZI, ExpenseReasonType.锁定附魔消耗, "附魔锁定消耗");
			} catch(Exception e) {
				player.sendError(Translate.金币不足);
				return false;
			}
			int tempResult = 0;
			for (int i=0; i<EnchantManager.开放附魔的位置.length; i++) {
				EquipmentEntity ae = (EquipmentEntity) player.getEquipmentColumns().get(EnchantManager.开放附魔的位置[i]);
				if (ae != null && ae.getEnchantData() != null && ae.getEnchantData().getEnchants().size() > 0 && !ae.getEnchantData().isLock()) {
					this.unLoadEnchantAttr(player, ae);
					ae.getEnchantData().setLock(true);
					CoreSubSystem.notifyEquipmentChange(player, new EquipmentEntity[] { ae });
					tempResult++;
				}
			}
			player.sendError(Translate.锁定成功);
			if (logger.isWarnEnabled()) {
				logger.warn("[一键锁定附魔] [成功] [锁定个数:" + result + "] [实际成果结果:"+tempResult+"] [ " + player.getLogString() + "]");
			}
			return true;
		} else {
			for (int i=0; i<EnchantManager.开放附魔的位置.length; i++) {
				boolean temp = i == 0;
				this.unlockEnchant(player, EnchantManager.开放附魔的位置[i], temp);
			}
			return true;
		}
	}
	
	public boolean unlockEnchant(Player player, int equiptIndex){
		if (unlockEnchant(player, equiptIndex, false)) {
			player.sendError(Translate.解锁附魔成功);
			return true;
		}
		return false;
	}
	
	/**
	 * 锁定附魔（处于锁定状态的附魔不掉耐久）
	 * @param player
	 * @param equiptIndex
	 */
	public boolean lockEnchant(Player player, int equiptIndex) {
		ArticleEntity equipt = player.getEquipmentColumns().get(equiptIndex);			//对应位置装备
		if (equipt == null || !(equipt instanceof EquipmentEntity)) {
			return false;
		}
		try {
			boolean tips = player.getBindSilver() >= ExpenseReasonType.锁定附魔消耗;
			BillingCenter.getInstance().playerExpense(player, EnchantManager.锁定附魔消耗, CurrencyType.BIND_YINZI, ExpenseReasonType.锁定附魔消耗, "附魔消耗");
			if (tips) {
				String description = Translate.translateString(Translate.装备绑定花费, new String[][] { { Translate.COUNT_1, BillingCenter.得到带单位的银两(EnchantManager.锁定附魔消耗) } });
				player.sendError(description);
			}
		} catch (Exception e) {
			player.sendError(Translate.金币不足);
			return false;
		}
		
		EnchantData entity = this.getEntity((EquipmentEntity) equipt);
		if (entity != null) {
			if (ArticleManager.logger.isWarnEnabled()) {
				ArticleManager.logger.warn("[锁定附魔前]" + player.getPlayerPropsString());
			}
			unLoadEnchantAttr(player, (EquipmentEntity) equipt);
			entity.setLock(true);
			CoreSubSystem.notifyEquipmentChange(player, new EquipmentEntity[] { (EquipmentEntity) equipt });
			logger.warn("[锁定附魔效果] [成功] [" + player.getLogString4Knap() + "] [equiptId:" + equipt.getId() + "] [eName:" + equipt.getArticleName() + "]");
			player.sendError(Translate.锁定成功);
			if (ArticleManager.logger.isWarnEnabled()) {
				ArticleManager.logger.warn("[锁定附魔后]" + player.getPlayerPropsString());
			}
			return true;
		}
		return false;
	}
	/**
	 * 解除附魔锁定效果
	 * @param player
	 * @param equiptIndex
	 */
	public boolean unlockEnchant(Player player, int equiptIndex, boolean temp) {
		ArticleEntity equipt = player.getEquipmentColumns().get(equiptIndex);			//对应位置装备
		if (equipt == null || !(equipt instanceof EquipmentEntity)) {
			return false;
		}
		EnchantData entity = this.getEntity((EquipmentEntity) equipt);
		if (entity != null && entity.isLock()) {
			entity.setLock(false);
			loadEnchantAttr(player, (EquipmentEntity) equipt);
			CoreSubSystem.notifyEquipmentChange(player, new EquipmentEntity[] { (EquipmentEntity) equipt });
			logger.warn("[解除附魔锁定效果] [成功] [" + player.getLogString4Knap() + "] [equiptId:" + equipt.getId() + "] [eName:" + equipt.getArticleName() + "]");
			if (temp) {
				player.sendError(Translate.解锁附魔成功);
			}
			return true;
		}
		return false;
	}
	
	
	public EnchantAttr createNewEnchant(EnchantModel model, Random random, EnchantArticle ea) {
		int addNum = -1;
//		if (model.getType() == 附魔类型_属性) {
		int totalNum = 0;
		for (int i=0; i< model.getFirstProb().length; i++) {
			totalNum += model.getFirstProb()[i];
		}
		int ran1 = random.nextInt(totalNum);
		int index = 0;
		int tempNum = 0;
		for (int i=0; i<model.getFirstProb().length; i++) {
			tempNum += model.getFirstProb()[i];
			if (ran1 <= tempNum) {
				index = i;
				break;
			}
		}
		int tt = 0;
		if (model.getAttrNums()[index+1] - model.getAttrNums()[index] > 0) {
			tt = random.nextInt(model.getAttrNums()[index+1] - model.getAttrNums()[index]);
		}
		addNum = model.getAttrNums()[index] + tt;
//		}
		EnchantAttr eattr = new EnchantAttr(ea.getEnchantId(), model.getDurable(), addNum);
		return eattr;
	}
	/**
	 * 
	 * @param player
	 * @param articleName			//附魔道具名
	 * @param aeId					//装备id
	 * @param type					//类型   1附魔  2结束
	 */
	public void sendEnchantDataStat(Player player, String articleName, long aeId, int type, int equipType) {
		try {
			String serverName = GameConstants.getInstance().getServerName();
			FuMoFlow stat = new FuMoFlow();
			stat.setColumn1(aeId + "");
			stat.setColumn2(equipType + "");
			stat.setCreateTime(System.currentTimeMillis());
			stat.setFenQu(serverName);
			stat.setFoMoWuPinName(articleName);
			stat.setType(type+"");
			stat.setUserName(player.getName());
			StatClientService.getInstance().sendFuMoFlow("",stat);
		} catch (Exception e) {
			logger.warn("[发送附魔统计] [异常]", e);
		}
	}
	
	/**
	 * 获得装备上的附魔
	 * @param ee
	 * @return  null 代表此装备没有过附魔
	 */
	public EnchantData getEntity(EquipmentEntity ee) {
		if (ee.getEnchantData() != null) {
			return ee.getEnchantData();
		}
		Article a = ArticleManager.getInstance().getArticle(ee.getArticleName());
		
		if (!(a instanceof Equipment) || !EnchantManager.instance.isOpenEnchant(((Equipment)a).getEquipmentType())) {
			return null;
		}
		EnchantData entity = null;
		try {
			entity = em.find(ee.getId());
			if (entity != null) {
				ee.setEnchantData(entity);
			}
		} catch (Exception e) {
			// TODO Auto-generated catch block
			logger.warn("[家族装备附魔] [异常] [" + ee.getId() + "]", e );
		}
		return entity;
	}
}
