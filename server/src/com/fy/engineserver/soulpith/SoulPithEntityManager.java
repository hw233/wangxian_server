package com.fy.engineserver.soulpith;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.slf4j.Logger;

import com.fy.engineserver.articleEnchant.EnchantEntityManager;
import com.fy.engineserver.articleProtect.ArticleProtectDataValues;
import com.fy.engineserver.articleProtect.ArticleProtectManager;
import com.fy.engineserver.datasource.article.data.articles.Article;
import com.fy.engineserver.datasource.article.data.entity.ArticleEntity;
import com.fy.engineserver.datasource.article.data.entity.SoulPithArticleEntity;
import com.fy.engineserver.datasource.article.data.props.Knapsack;
import com.fy.engineserver.datasource.article.data.soulpith.GourdArticle;
import com.fy.engineserver.datasource.article.data.soulpith.SoulPithArticle;
import com.fy.engineserver.datasource.article.data.soulpith.SoulPithArticleLevelData;
import com.fy.engineserver.datasource.article.manager.ArticleEntityManager;
import com.fy.engineserver.datasource.article.manager.ArticleManager;
import com.fy.engineserver.datasource.language.Translate;
import com.fy.engineserver.economic.BillingCenter;
import com.fy.engineserver.economic.CurrencyType;
import com.fy.engineserver.economic.ExpenseReasonType;
import com.fy.engineserver.menu.MenuWindow;
import com.fy.engineserver.menu.Option;
import com.fy.engineserver.menu.Option_Cancel;
import com.fy.engineserver.menu.WindowManager;
import com.fy.engineserver.menu.soulpith.Option_confirmartifice;
import com.fy.engineserver.menu.soulpith.Option_confirmdevour;
import com.fy.engineserver.message.CONFIRM_WINDOW_REQ;
import com.fy.engineserver.message.GameMessageFactory;
import com.fy.engineserver.message.NEW_QUERY_ARTICLE_INFO_RES;
import com.fy.engineserver.minigame.MinigameConstant;
import com.fy.engineserver.soulpith.instance.PlayerSoulpithInfo;
import com.fy.engineserver.soulpith.instance.SoulPithAeData;
import com.fy.engineserver.soulpith.instance.SoulPithEntity;
import com.fy.engineserver.soulpith.module.ArtificeModule;
import com.fy.engineserver.soulpith.module.SoulLevelupExpModule;
import com.fy.engineserver.soulpith.module.SoulPithExtraAttrModule;
import com.fy.engineserver.soulpith.module.SoulPithLevelModule;
import com.fy.engineserver.soulpith.property.AddPropertyTypes;
import com.fy.engineserver.soulpith.property.PropertyManager;
import com.fy.engineserver.soulpith.property.PropertyModule;
import com.fy.engineserver.sprite.Player;
import com.fy.engineserver.sprite.Soul;
import com.xuanzhi.tools.simplejpa.SimpleEntityManager;
import com.xuanzhi.tools.simplejpa.SimpleEntityManagerFactory;
/**
 * 灵根
 * 
 * @date on create 2016年3月22日 下午2:17:58
 */
public class SoulPithEntityManager {
	
	public static SoulPithEntityManager inst;
	
	public static Logger logger = SoulPithManager.logger;
	
	public static SimpleEntityManager<SoulPithEntity> em;
	
	public static SimpleEntityManager<SoulPithAeData> em_ae;
	
	public static Map<Long, Object> playerLocks = new HashMap<Long, Object>();
	public static Object lock = new Object();
	
	public static SoulPithEntityManager getInst() {
		return inst;
	}
	
	public void init() {
		inst = this;
		em = SimpleEntityManagerFactory.getSimpleEntityManager(SoulPithEntity.class);
		em_ae = SimpleEntityManagerFactory.getSimpleEntityManager(SoulPithAeData.class);
		logger.warn("["+this.getClass().getSimpleName()+"] [启动成功]");
	}
	
	public void destory() {
		try {
			em.destroy();
			em_ae.destroy();
		} catch (Exception e) {
			logger.warn("["+this.getClass().getSimpleName()+"] [执行destr异常]", e);
		}
	}
	
	public Object getPlayerLock(Player player) {
		Object o = playerLocks.get(player.getId());
		if (o != null) {
			return o;
		}
		synchronized (lock) {
			o = playerLocks.get(player.getId());
			if (o == null) {
				o = new Object();
				playerLocks.put(player.getId(), o);
			}
			return o;
		}
	}
	/**
	 * 计算灵根给玩家附加的属性(元神和本尊的属性都需要计算)
	 * @param player
	 */
	public void initSoulPithAttr(Player player) {
		//减去之前灵根加的属性
		SoulPithEntity entity = this.getEntity(player);
		if (entity == null) {
			return ;
		}
		if (entity.getOldAddProps() != null) {
			PropertyManager.getInst().deductAttrs(player, entity.getOldAddProps(), logger);
		}
		//加上现在灵根的属性
		PropertyModule pm = new PropertyModule();
		Map<SoulPithTypes, Integer> soulNums = new HashMap<SoulPithTypes, Integer>();
		SoulPithLevelModule splm = SoulPithManager.getInst().levelModules.get(SoulPithManager.getPlayerLevel(player, player.getCurrSoul().getSoulType()));
		int[] careerSouls = splm.getCareerBaseSoulNum(player.getCurrSoul().getCareer());
		for (int i=0; i<careerSouls.length; i++) {
			soulNums.put(SoulPithTypes.valueOf(i), careerSouls[i]);
		}
		
		for (Soul s : player.getSouls()) {		//当前元神镶嵌灵髓附加的属性
			PlayerSoulpithInfo info = entity.getPlayerSoulpithInfo(s.getSoulType());
			for (int i=0; i<info.getPiths().length; i++) {
				long id = info.getPiths()[i];
				if (id > 0) {
					SoulPithArticleEntity ae = (SoulPithArticleEntity) ArticleEntityManager.getInstance().getEntity(id);
					SoulPithArticle a = (SoulPithArticle) ArticleManager.getInstance().getArticle(ae.getArticleName());
					SoulPithArticleLevelData data = a.getLevelDatas().get(ae.getLevel());
					SoulPithArticleLevelData data2 = a.getLevelDatas().get(ae.getLevel()+1);
					SoulLevelupExpModule sem = SoulPithManager.getInst().soulLevelModules.get(ae.getLevel());
					for (int j=0; j<data.getProperTypes().length; j++) {
						int num = (int) (data.getProperNums()[j] * SoulPithConstant.COLOR_RATE[ae.getColorType()]);			
						AddPropertyTypes addType = AddPropertyTypes.ADD_B_NUM;			//当前元神增加B值，未使用的元神增加X值
						if (player.getCurrSoul().getSoulType() != s.getSoulType()) {
							addType = AddPropertyTypes.ADD_X_NUM;
						}
						pm.addNewProperty(addType, data.getProperTypes()[j], num);
					}
					if (s.getSoulType() == player.getCurrSoul().getSoulType()) {			//只计算当前元神镶嵌的灵髓点数
						for (int j=0; j<data.getTypes().length; j++) {
							int num = data.getSoulNums()[j];
							if (soulNums.containsKey(data.getTypes()[j])) {
								num = soulNums.get(data.getTypes()[j]) + data.getSoulNums()[j];
							}
							soulNums.put(data.getTypes()[j], num);
						}
					}
					if (ae.getExp() > 0 && data2 != null) {
						for (int j=0; j<data2.getProperTypes().length; j++) {
							int num = (int) (data.getProperNums()[j] * SoulPithConstant.COLOR_RATE[ae.getColorType()]);			
							int num2 = (int) (data2.getProperNums()[j] * SoulPithConstant.COLOR_RATE[ae.getColorType()]);			
							AddPropertyTypes addType = AddPropertyTypes.ADD_B_NUM;			//当前元神增加B值，未使用的元神增加X值
							if (player.getCurrSoul().getSoulType() != s.getSoulType()) {
								addType = AddPropertyTypes.ADD_X_NUM;
							}
							float rate = (float)ae.getExp() / (float)sem.getNeedExp();
							int addNum = (int)((num2 - num) * rate);
							pm.addNewProperty(addType, data2.getProperTypes()[j], addNum);
						}
					}
				}
			}
		}
		{		//额外激活的属性
			for (int i=0; i<SoulPithManager.getInst().extraAttrs.size(); i++) {
				SoulPithExtraAttrModule module = SoulPithManager.getInst().extraAttrs.get(i);
				if (module.canAdd(soulNums)) {
					for (int j=0; j<module.getAddTypes().length; j++) {
						pm.addNewProperty(module.getAddTypes()[j], module.getAttrTypes()[j], module.getAttrNums()[j]);
					}
				}
			}
		}
		entity.setOldAddProps(pm);
		PropertyManager.getInst().addAttrs(player, pm, logger);
	}
	
	/**
	 *  镶嵌灵髓
	 * @param player
	 * @param aeId  灵髓道具id
	 * @param index 镶嵌的位置
	 */
	public void inlay(Player player, int soulType, long aeId, int index) {
		if (index < 0 || index >= SoulPithConstant.MAX_FILL_NUM) {
			if (logger.isInfoEnabled()) {
				logger.info("[镶嵌灵髓] [失败] [客户端发过来要镶嵌的位置id错误] [" + player.getLogString() + "] [index:" + index + "]");
			}
			return ;
		}
		ArticleEntity ae = ArticleEntityManager.getInstance().getEntity(aeId);
		if (ae instanceof SoulPithArticleEntity) {
			SoulPithArticleEntity spae = (SoulPithArticleEntity) ae;
			Object playerLock = this.getPlayerLock(player);
			synchronized (playerLock) {
				SoulPithEntity entity = this.getEntity(player);
				if (entity == null) {
					player.sendError(Translate.等级不足);
					return ;
				}
				String result = this.check4Inlay(player, entity, spae, soulType, index);
				if (result != null) {
					if (logger.isInfoEnabled()) {
						logger.info("[镶嵌灵髓] [失败] [reason:" + result + "] [" + player.getLogString() + "] [index:" + index + "]");
					}
					player.sendError(result);
					return ;
				}
				Knapsack bag = player.getKnapsack_common();
				ArticleEntity aee = bag.removeByArticleId(ae.getId(), "灵髓镶嵌", false);
				if (aee == null) {
					if (logger.isInfoEnabled()) {
						logger.info("[镶嵌灵髓] [失败] [删除物品失败] [" + player.getLogString() + "] [aeId:" + aeId + "]");
					}
					return ;
				}
				PlayerSoulpithInfo info = entity.getPlayerSoulpithInfo(soulType);
				long oldId = info.getPiths()[index];
				info.getPiths()[index] = ae.getId();
				em.notifyFieldChange(entity, "pithInfos");
				logger.warn("[镶嵌灵髓] [成功] [" + player.getLogString() + "] [index:" + index + "] [" + spae.getLogString() + "]");
				if (oldId > 0) {
					try {
						ArticleEntity oldAe = ArticleEntityManager.getInstance().getEntity(oldId);
						boolean b = bag.put(oldAe, "灵髓取出");
						logger.warn("[系统取出灵髓] [成功] [" + player.getLogString() + "] [soulType:"+soulType+"] [id:" + oldAe.getId() + "] [name:" + oldAe.getArticleName() + "] [取出结果:" + b + "]");
					} catch (Exception e) {
						logger.warn("[取出之前镶嵌的灵髓] [异常] [" + player.getLogString() + "] [soulType:"+soulType+"] [oldAeid:" + oldId + "]", e);
					}
				}
				if (ArticleManager.logger.isWarnEnabled()) {
					ArticleManager.logger.warn("[镶嵌灵髓前]" + player.getPlayerPropsString());
				}
				this.initSoulPithAttr(player);
				if (ArticleManager.logger.isWarnEnabled()) {
				}
			}
		} else {
			if (logger.isInfoEnabled()) {
				logger.info("[镶嵌灵髓] [失败] [镶嵌的物品不是灵髓宝石] [" + player.getLogString() + "] [soulType:"+soulType+"] [aeId:" + aeId + "] [index:" + index + "]");
			}
		}
	}
	/**
	 * 镶嵌灵髓检查
	 * @param player
	 * @param entity
	 * @param spae
	 * @return
	 */
	public String check4Inlay(Player player, SoulPithEntity entity, SoulPithArticleEntity spae, int soulType, int index) {
		Soul s = player.getSoul(soulType);
		if (s == null) {
			return "元神未开启";
		}
		int maxOpenNum = SoulPithManager.getPlayerFillNum(player, soulType);
		if (index >= maxOpenNum) {
			return "尚未开启对应镶嵌位置!";
		}
		SoulPithLevelModule module = SoulPithManager.getInst().levelModules.get(SoulPithManager.getPlayerLevel(player, soulType));
//		int[] careerSouls = module.getCareerBaseSoulNum(player.getCareer());
		int[] currSouls ;
		int[] maxSouls = module.getMaxSoulNums();
		PlayerSoulpithInfo info = entity.getPlayerSoulpithInfo(soulType);
//		currSouls = Arrays.copyOf(careerSouls, careerSouls.length);
		currSouls = new int[SoulPithTypes.values().length];
		for (int i=0; i<info.getPiths().length; i++) {
			long id = info.getPiths()[i];
			if (id > 0) {
				SoulPithArticleEntity ae = (SoulPithArticleEntity) ArticleEntityManager.getInstance().getEntity(id);
				SoulPithArticle a = (SoulPithArticle) ArticleManager.getInstance().getArticle(ae.getArticleName());
				SoulPithArticleLevelData data = a.getLevelDatas().get(ae.getLevel());
				for (int j=0; j<data.getTypes().length; j++) {
					currSouls[data.getTypes()[j].getId()] += data.getSoulNums()[j];
				}
			}
		}
		SoulPithArticle article = (SoulPithArticle) ArticleManager.getInstance().getArticle(spae.getArticleName());
		SoulPithArticleLevelData data = article.getLevelDatas().get(spae.getLevel());
		for (int i=0; i<data.getTypes().length; i++) {
			int tempIndex = data.getTypes()[i].getId();
			int temp = currSouls[tempIndex] + data.getSoulNums()[i];
			if (temp > maxSouls[tempIndex]) {
				return String.format(SoulPithManager.getInst().getTranslate("inlay_fail"), maxSouls[tempIndex] + "");
			}
		}
		return null;
	}
	
	/**
	 * 取出对应位置的灵髓
	 * @param player
	 * @param index
	 */
	public void takeOut(Player player, int soulType, int index) {
		if (index < 0 || index >= SoulPithConstant.MAX_FILL_NUM) {
			if (logger.isInfoEnabled()) {
				logger.info("[取出灵髓] [失败] [客户端发过来要镶嵌的位置id错误] [" + player.getLogString() + "] [index:" + index + "]");
			}
			return ;
		}
		Object playerLock = this.getPlayerLock(player);
		synchronized (playerLock) {
			SoulPithEntity entity = this.getEntity(player);
			if (entity == null) {
				player.sendError(Translate.等级不足);
				return ;
			}
			PlayerSoulpithInfo info = entity.getPlayerSoulpithInfo(soulType);
			if (info.getPiths()[index] <= 0) {
				if (logger.isDebugEnabled()) {
					logger.debug("[取出灵髓] [失败] [对应位置没有镶嵌灵髓] [" + player.getLogString() + "] [soulType:"+soulType+"] [index:" + index + "] [" + Arrays.toString(info.getPiths()) + "]");
				}
				return ;
			}
			Knapsack bag = player.getKnapsack_common();
			if (bag.getEmptyNum() <= 0) {
				player.sendError(Translate.背包空间不足);
				return ;
			}
			long oldId = info.getPiths()[index];
			info.getPiths()[index] = 0;
			em.notifyFieldChange(entity, "pithInfos");
			try {
				ArticleEntity oldAe = ArticleEntityManager.getInstance().getEntity(oldId);
				boolean b = bag.put(oldAe, "灵髓取出");
				logger.warn("[手动取出灵髓] [成功] [" + player.getLogString() + "] [id:" + oldAe.getId() + "] [soulType:"+soulType+"] [name:" + oldAe.getArticleName() + "] [取出结果:" + b + "]");
			} catch (Exception e) {
				logger.warn("[取出灵髓] [异常] [" + player.getLogString() + "] [soulType:"+soulType+"] [oldAeid:" + oldId + "]", e);
			}
			if (ArticleManager.logger.isWarnEnabled()) {
				ArticleManager.logger.warn("[取出灵髓前]" + player.getPlayerPropsString());
			}
			this.initSoulPithAttr(player);
			if (ArticleManager.logger.isWarnEnabled()) {
				ArticleManager.logger.warn("[取出灵髓后]" + player.getPlayerPropsString());
			}
		}
	}

	/**
	 * 灵髓吞噬
	 * @param player
	 * @param mainId
	 * @param meterials
	 */
	public boolean devour(Player player, long mainId, long meterials, boolean confirm) {
		if (mainId <= 0 || meterials <= 0) {
			player.sendError(SoulPithManager.getInst().getTranslate("incorrect_articleId"));
			return false;
		}
		if (!ArticleProtectManager.instance.isCanDo(player, ArticleProtectDataValues.ArticleProtect_Common, meterials)) {
			player.sendError(Translate.锁魂物品不能做此操作);
			return false;
		}
		Object playerLock = this.getPlayerLock(player);
		synchronized (playerLock) {
			Knapsack bag = player.getKnapsack_common();
			ArticleEntity ae1 = ArticleEntityManager.getInstance().getEntity(mainId);
			ArticleEntity ae2 = ArticleEntityManager.getInstance().getEntity(meterials);
			if (ae1 instanceof SoulPithArticleEntity && ae2 instanceof SoulPithArticleEntity) {
				long costBind = this.getDevourCosy((SoulPithArticleEntity) ae2);
				if (player.getBindSilver() + player.getShopSilver() + player.getSilver() < costBind) {
					player.sendError(Translate.金币不足);
					return false;
				}
				if (bag.contains(ae1) && bag.contains(ae2)) {
					SoulPithArticleEntity soulAe = (SoulPithArticleEntity) ae1;
					if (soulAe.getLevel() >= SoulPithConstant.SOUL_MAX_LEVEL) {
						player.sendError(SoulPithManager.getInst().getTranslate("reach_max_level"));
						return false;
					}
					if (!confirm && check4DevourConfirm(player, (SoulPithArticleEntity)ae1,(SoulPithArticleEntity) ae2, costBind)) {
						return false;
					}
					try {
						BillingCenter.getInstance().playerExpense(player, costBind, CurrencyType.BIND_YINZI, ExpenseReasonType.灵髓吞噬, "灵髓吞噬");
					} catch (Exception e) {
						player.sendError(Translate.金币不足);
						return false;
					}
					ArticleEntity aee = bag.removeByArticleId(ae2.getId(), "灵髓吞噬", false);
					if (aee == null) {
						logger.warn("[灵髓吞噬] [删除物品不成功] [" + player.getLogString() + "] [mainId:" + mainId + "] [meterials:" + meterials + "]");
						return false;
					}
					try {
						EnchantEntityManager.sendArticleStat(player, aee, "灵髓吞噬删除");
					} catch (Exception e){}
					long oldExp = ((SoulPithArticleEntity)aee).getExtraData().getExps();
					int tempLv = ((SoulPithArticleEntity)aee).getLevel() - 1; 
					if (tempLv >= 1) {
						for (int i=tempLv; i>0; i--) {
							SoulLevelupExpModule oldLevelModule = SoulPithManager.getInst().soulLevelModules.get(i);
							oldExp += oldLevelModule.getNeedExp();
						}
					}
					long[] result = this.calculateLevel(soulAe.getLevel(), soulAe.getExp(), oldExp);
					soulAe.getExtraData().setExps(result[1]);
					if (soulAe.getExtraData().getPithLevel() < result[0]) {
						soulAe.getExtraData().setPithLevel((int) result[0]);
					}
					
					Article article = ArticleManager.getInstance().getArticle(soulAe.getArticleName());
					int tttll = soulAe.getLevel();
					String tempIc = ((SoulPithArticle)article).getLevelDatas().get(tttll).getCorner();
					String iconId = article.getIconId() + "," + tempIc;
					NEW_QUERY_ARTICLE_INFO_RES res = new NEW_QUERY_ARTICLE_INFO_RES(GameMessageFactory.nextSequnceNum(), soulAe.getId(), iconId,soulAe.getInfoShow(player));
					player.addMessageToRightBag(res);
					logger.warn("[灵髓吞噬] [成功] [" + player.getLogString() + "] [灵髓:" + soulAe.getLogString() + "] [被吞噬的灵髓:" + ((SoulPithArticleEntity)aee).getLogString() + "]");
					return true;
				} else {
					player.sendError(SoulPithManager.getInst().getTranslate("article_not_in_bag"));
				}
			} else {
				if (logger.isInfoEnabled()) {
					logger.info("[灵髓吞噬] [放入物品不对] [mainId:" + mainId + "] [meterialsId:" + meterials + "] [" + player.getLogString() + "]");
				}
				player.sendError(SoulPithManager.getInst().getTranslate("incorrect_articleId"));
			}
			return false;
		}
	}
	
	public long getDevourCosy(SoulPithArticleEntity se) {
		/*for (DevourCostModule module : SoulPithManager.getInst().devourCostList) {
			if (module.getColorType() == se.getColorType() && module.getSoulLevel() == se.getLevel()) {
				return module.getBindSilver();
			}
		}
		return SoulPithConstant.DEVOUR_COST;*/
		int level = se.getLevel();
		int colorType = se.getColorType();
		if (level <= 0) {
			level = 1;
		}
		if (colorType <= 0) {
			colorType = 1;
		}
		return SoulPithConstant.DEVOUR_COST * level * colorType;
	}
	
	public long getArtificeCost() {
		return SoulPithConstant.ARTIFICE_COST;
	}
	
	
	
	/**
	 * 灵髓吞噬二次确认检查并弹出确认框
	 * @param player
	 * @return
	 */
	public boolean check4DevourConfirm(Player player, SoulPithArticleEntity mainAe, SoulPithArticleEntity meterial, long costBind) {
		long tempSilver = costBind - player.getBindSilver();
		if (tempSilver > 0) {
			WindowManager wm = WindowManager.getInstance();
			MenuWindow mw = wm.createTempMenuWindow(600);
			Option_confirmdevour option1 = new Option_confirmdevour();
			option1.setText(MinigameConstant.CONFIRM);
			option1.setMeterials(meterial.getId());
			option1.setMainId(mainAe.getId());
			Option_Cancel option2 = new Option_Cancel();
			option2.setText(MinigameConstant.CANCLE);
			Option[] options = new Option[] {option1, option2};
			mw.setOptions(options);
			mw.setDescriptionInUUB(SoulPithManager.getInst().getTranslate("devour_confirm_des", BillingCenter.得到带单位的银两(tempSilver)));
			CONFIRM_WINDOW_REQ creq = new CONFIRM_WINDOW_REQ(GameMessageFactory.nextSequnceNum(), mw.getId(), mw.getDescriptionInUUB(), options);
			player.addMessageToRightBag(creq);
			
			
			return true;
		}
		return false;
	}
	
	/**
	 * 计算灵髓等级
	 * @param oldLevel
	 * @param oldExp
	 * @param addExp
	 * @return  [0]加经验后的等级  [1]剩余经验
	 */
	public long[] calculateLevel(int oldLevel, long oldExp, long addExp) {
		long exp = oldExp + addExp;
		long resultLevel = oldLevel;
		for (int i=oldLevel; i<=SoulPithConstant.SOUL_MAX_LEVEL; i++) {
			SoulLevelupExpModule module = SoulPithManager.getInst().soulLevelModules.get(i);
			if (module.getNeedExp() <= 0) {
				break;
			}
			if (exp < module.getNeedExp()) {
				break;
			} else {
				if (resultLevel < SoulPithConstant.SOUL_MAX_LEVEL) {
					exp -= module.getNeedExp();
					resultLevel += 1;
				} else {
					break;
				}
			}
		}
		return new long[]{resultLevel, exp};
	}
	/**
	 * 计算灵髓宝石到满级所需经验
	 * @param se
	 * @return
	 */
	public long calculateMaxNeedExp(SoulPithArticleEntity se) {
		int tempLv = se.getLevel();
		long result = 0;
		for (int i=tempLv; i<SoulPithConstant.SOUL_MAX_LEVEL; i++) {
			SoulLevelupExpModule module = SoulPithManager.getInst().soulLevelModules.get(i);
			result += module.getNeedExp();
		}
		result -= se.getExp();
		return result;
	}
	
	/**
	 * 注灵
	 * @param player
	 * @param meterialIds
	 * @param nums
	 */
	public boolean pourIn(Player player, long soulId, String[] meterialIds, int[] nums) {
		if (soulId <= 0 || meterialIds.length != nums.length) {
			if (logger.isInfoEnabled()) {
				logger.info("[灵髓注灵] [失败] [传过来的物品id或者材料数组有误] [soulId:" + soulId + "] [meterials:" + Arrays.toString(meterialIds) + ",nums:" + Arrays.toString(nums) + "] [" + player.getLogString() + "]");
			}
			return false;
		}
		ArticleEntity ae = ArticleEntityManager.getInstance().getEntity(soulId);
		if (ae instanceof SoulPithArticleEntity) {
			SoulPithArticleEntity soul = (SoulPithArticleEntity) ae;
			if (soul.getLevel() >= SoulPithConstant.SOUL_MAX_LEVEL) {
				player.sendError(SoulPithManager.getInst().getTranslate("reach_max_level"));
				return false;
			}
			String[] needCostIds = new String[0];
			int[] needCostNum = new int[0];
			long maxNeedExp = this.calculateMaxNeedExp(soul);
			long exp = 0;
			boolean enough = false;
			for (int i=0; i<meterialIds.length; i++) {
				needCostIds = Arrays.copyOf(needCostIds, needCostIds.length + 1);
				needCostIds[needCostIds.length - 1] = meterialIds[i];
				needCostNum = Arrays.copyOf(needCostNum, needCostNum.length + 1);
				Article a2 = ArticleManager.getInstance().getArticle(meterialIds[i]);
				if (a2 instanceof GourdArticle) {
					for (int j=0; j<nums[i]; j++) {
						needCostNum[i] ++;
						exp += ((GourdArticle)a2).getAddExp();
						if (exp >= maxNeedExp) {
							enough = true;
							break;
						}
					}
					if (enough) {
						break;
					}
				} else {
					player.sendError(SoulPithManager.getInst().getTranslate("wrong_meterial"));
					return false;
				}
			}
			Object playerLock = this.getPlayerLock(player);
			synchronized (playerLock) {
				Knapsack bag = player.getKnapsack_common();
				for (int i=0; i<needCostIds.length; i++) {
					int count = bag.countArticle(needCostIds[i]);
					if (count < needCostNum[i]) {
						player.sendError(Translate.text_feed_silkworm_006);
						return false;
					}
				}
				if (!bag.contains(ae)) {
					return false;
				}
				for (int i=0; i<needCostIds.length; i++) {
					for (int j=0; j<needCostNum[i]; j++) {
						ArticleEntity aee = bag.remove(bag.indexOf(needCostIds[i]), "灵髓注灵", true);
						if (aee == null) {
							logger.warn("[灵髓注灵] [删除物品失败] [" + player.getLogString() + "] [soulId:" + soulId + "] [meterials:" + Arrays.toString(needCostIds) + ",nums:" + Arrays.toString(needCostNum) + "]"
									+ "[i" + i + "] [j:" + j + "]");
							player.sendError(Translate.删除物品失败);
							return false;
						}
						try {
							EnchantEntityManager.sendArticleStat(player, aee, "灵髓注灵删除");
						} catch (Exception e){}
					}
				}
				long[] result = this.calculateLevel(soul.getLevel(), soul.getExp(), exp);
				soul.getExtraData().setExps(result[1]);
				if (soul.getExtraData().getPithLevel() < result[0]) {
					soul.getExtraData().setPithLevel((int) result[0]);
				}
				Article article = ArticleManager.getInstance().getArticle(soul.getArticleName());
				int tttll = soul.getLevel();
				String tempIc = ((SoulPithArticle)article).getLevelDatas().get(tttll).getCorner();
				String iconId = article.getIconId() + "," + tempIc;
				NEW_QUERY_ARTICLE_INFO_RES res = new NEW_QUERY_ARTICLE_INFO_RES(GameMessageFactory.nextSequnceNum(), soul.getId(), iconId,soul.getInfoShow(player));
				player.addMessageToRightBag(res);
				logger.warn("[灵髓注灵] [成功] [" + player.getLogString() + "] [" + soul.getLogString() + "]");
				return true;
			}
		}
		return false;
		
	}
	/**
	 * 炼化
	 * @param player
	 * @param meterialIds
	 */
	public long artifice(Player player, long[] meterialIds, boolean confirm) {
		if (meterialIds.length < SoulPithConstant.ARTIFICE_NEED_NUM) {
			player.sendError(SoulPithManager.getInst().getTranslate("incorrect_artifice_article"));
			return -1;
		}
		Knapsack bag = player.getKnapsack_common();
		List<SoulPithArticleEntity> list = new ArrayList<SoulPithArticleEntity>();
		Map<Long, Integer> tempMap = new HashMap<Long, Integer>();
		for (long id : meterialIds) {
			int old = 0;
			if (tempMap.containsKey(id)) {
				old = tempMap.get(id);
			}
			old += 1;
			tempMap.put(id, old);
		}
		
		for (int i=0; i<meterialIds.length; i++) {
			ArticleEntity ae = ArticleEntityManager.getInstance().getEntity(meterialIds[i]);
			if (ae instanceof SoulPithArticleEntity) {
				int num = tempMap.get(ae.getId());
				long ct = bag.getArticleCellCount(ae.getId());
				if (!bag.contains(ae) || ct < num) {
					player.sendError(SoulPithManager.getInst().getTranslate("article_not_in_bag"));
					return -1;
				}
				list.add((SoulPithArticleEntity) ae);
			} else {
				player.sendError(SoulPithManager.getInst().getTranslate("incorrect_artifice_article"));
				return -1;
			}
		}
		//检查玩家银子是否够（需要确定消耗的是银子还是绑银）
		long cost = this.getArtificeCost();
		if (player.getBindSilver() + player.getSilver() + player.getShopSilver() < cost) {
			player.sendError(Translate.金币不足);
			return -1;
		}
		if (!confirm && check4ArtificeConfirm(player, list, meterialIds, cost)) {
			return -1;
		}
		Object playerLock = this.getPlayerLock(player);
		synchronized (playerLock) {
			try {
				BillingCenter.getInstance().playerExpense(player, cost, CurrencyType.BIND_YINZI, ExpenseReasonType.灵髓炼化, "灵髓炼化");
			} catch (Exception e) {
				player.sendError(Translate.金币不足);
				return -1;
			}
			
			int tempColorType = -1;			//保底颜色
			boolean binded = false;
			for (int i=0; i<meterialIds.length; i++) {
				ArticleEntity ae = bag.removeByArticleId(meterialIds[i], "炼化灵髓", false);
				if (ae == null) {
					player.sendError(Translate.删除物品失败);
					return -1;
				}
				try {
					EnchantEntityManager.sendArticleStat(player, ae, "炼化灵髓删除");
				} catch (Exception e) {
					
				}
				if (tempColorType < 0 || tempColorType > ae.getColorType()) {
					tempColorType = ae.getColorType();
				}
//				if (ae.isBinded()) {
//					binded = true;
//				}
			}
			int ranNum = player.random.nextInt(10000);
			ArtificeModule am = SoulPithManager.getInst().articleModules.get(tempColorType);
			int tempProb = 0;
			int resultColor = 0;
			for (int i=0; i<am.getProbablitys().length; i++) {
				tempProb += am.getProbablitys()[i];
				if (ranNum <= tempProb) {
					resultColor = am.getResultColor()[i];
					break;
				}
			}
			String articleName = SoulPithConstant.artifice_articleCNNName[player.random.nextInt(SoulPithConstant.artifice_articleCNNName.length)];
			Article a = ArticleManager.getInstance().getArticleByCNname(articleName);
			try {
				ArticleEntity ae = ArticleEntityManager.getInstance().createEntity(a, binded, ArticleEntityManager.灵髓炼化, player, resultColor, 1, true);
				boolean b = bag.put(ae, "灵髓炼化");
				try {
					EnchantEntityManager.sendArticleStat(player, ae, "炼化灵髓增加");
				} catch (Exception e){}
				logger.warn("[灵髓炼化] [成功] [" + player.getLogString() + "] [" + ((SoulPithArticleEntity)ae).getLogString() + "] [放入背包结果:" + b + "]");
				return ae.getId();
			} catch (Exception e) {
				// TODO Auto-generated catch block
				logger.warn("[灵髓炼化] [成功] [在放入背包是出现异常] [" + player.getLogString() + "] [articleName:" + articleName + "] [color:" + resultColor + "]", e);
			}
			return -1;
		}
	}
	/**
	 * 炼化二次确认
	 * @param player
	 * @return
	 */
	public boolean check4ArtificeConfirm(Player player, List<SoulPithArticleEntity> list, long[] meterialIds, long costSilver) {
		boolean b = false;
		for (SoulPithArticleEntity se : list) {
			if (se.getLevel() > 1 || se.getColorType() > 1) {
				b = true;
				break;
			}
		}
		String str = SoulPithManager.getInst().getTranslate("artifice_confirm_des");
		long tempSilver = costSilver - player.getBindSilver();
		if (!b && tempSilver > 0) {
			str = SoulPithManager.getInst().getTranslate("devour_confirm_des", BillingCenter.得到带单位的银两(tempSilver));
			b = true;
		}
		if (b) {
			WindowManager wm = WindowManager.getInstance();
			MenuWindow mw = wm.createTempMenuWindow(600);
			Option_confirmartifice option1 = new Option_confirmartifice();
			option1.setText(MinigameConstant.CONFIRM);
			option1.setMeterialIds(meterialIds);
			Option_Cancel option2 = new Option_Cancel();
			option2.setText(MinigameConstant.CANCLE);
			Option[] options = new Option[] {option1, option2};
			mw.setOptions(options);
			mw.setDescriptionInUUB(str);
			CONFIRM_WINDOW_REQ creq = new CONFIRM_WINDOW_REQ(GameMessageFactory.nextSequnceNum(), mw.getId(), mw.getDescriptionInUUB(), options);
			player.addMessageToRightBag(creq);
			return true;
		}
		return false;
	}
	/**
	 * 获取玩家灵根面板
	 * @param player
	 * @return null代表玩家等级不足未开启或者报错
	 */
	public SoulPithEntity getEntity(Player player) {
		if (player.getLevel() < SoulPithConstant.openLevel) {
			return null;
		}
		if (player.getSoulPith() != null) {
			return player.getSoulPith();
		}
		SoulPithEntity entity = null;
		try {
			entity = em.find(player.getId());
			if (entity == null) {
				entity = new SoulPithEntity();
				entity.setId(player.getId());
				entity.getPithInfos().add(new PlayerSoulpithInfo(Soul.SOUL_TYPE_BASE));
				entity.getPithInfos().add(new PlayerSoulpithInfo(Soul.SOUL_TYPE_SOUL));
				em.notifyNewObject(entity);
			}
			if (entity.getPithInfos().size() <= 0) {
				entity.getPithInfos().add(new PlayerSoulpithInfo(Soul.SOUL_TYPE_BASE));
				entity.getPithInfos().add(new PlayerSoulpithInfo(Soul.SOUL_TYPE_SOUL));
				em.notifyFieldChange(entity, "pithInfos");
			}
			player.setSoulPith(entity);
		} catch (Exception e) {
			logger.warn("[getEntity] [异常] [" + player.getLogString() + "]", e);
		}
		return entity;
	}
	/**
	 * 获取灵髓物品其他属性
	 * @param ae
	 * @return
	 */
	public SoulPithAeData getArticleExtraData(SoulPithArticleEntity ae) {
		if (ae.getExtraData() != null) {
			return ae.getExtraData();
		}
		SoulPithAeData entity = null;
		try {
			 entity = em_ae.find(ae.getId());
			 if (entity == null) {
				 entity = new SoulPithAeData();
				 entity.setId(ae.getId());
				 entity.setPithLevel(1);
				 entity.setExps(1);
				 em_ae.notifyNewObject(entity);
				 logger.warn("[getArticleExtraData] [创建entity] [" + ae.getId() + "]");
			 }
		} catch (Exception e) {
			logger.warn("[getArticleExtraData] [异常] [" + ae.getId() + "]", e);
		}
		ae.setExtraData(entity);
		return entity;
	}
	public SoulPithAeData getArticleExtraData(SoulPithArticleEntity ae, boolean isTemp) {
		if (ae.getExtraData() != null) {
			return ae.getExtraData();
		}
		SoulPithAeData entity = null;
		try {
			entity = em_ae.find(ae.getId());
			if (entity == null) {
				entity = new SoulPithAeData();
				entity.setId(ae.getId());
				entity.setPithLevel(1);
				entity.setExps(1);
				if (!isTemp) {
					em_ae.notifyNewObject(entity);
				}
				logger.warn("[getArticleExtraData] [创建entity] [" + ae.getId() + "] [isTemp:" + isTemp + "]");
			}
		} catch (Exception e) {
			logger.warn("[getArticleExtraData] [异常] [" + ae.getId() + "]", e);
		}
		ae.setExtraData(entity);
		return entity;
	}
}
