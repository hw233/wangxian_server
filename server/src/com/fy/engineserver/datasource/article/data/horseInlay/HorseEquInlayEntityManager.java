package com.fy.engineserver.datasource.article.data.horseInlay;

import java.util.Arrays;

import org.slf4j.Logger;

import com.fy.engineserver.articleProtect.ArticleProtectDataValues;
import com.fy.engineserver.articleProtect.ArticleProtectManager;
import com.fy.engineserver.core.CoreSubSystem;
import com.fy.engineserver.datasource.article.data.articles.Article;
import com.fy.engineserver.datasource.article.data.articles.InlayArticle;
import com.fy.engineserver.datasource.article.data.entity.ArticleEntity;
import com.fy.engineserver.datasource.article.data.entity.EquipmentEntity;
import com.fy.engineserver.datasource.article.data.equipments.Equipment;
import com.fy.engineserver.datasource.article.data.horseInlay.instance.HorseEquInlay;
import com.fy.engineserver.datasource.article.data.horseInlay.module.InlayModule;
import com.fy.engineserver.datasource.article.data.props.Knapsack;
import com.fy.engineserver.datasource.article.manager.ArticleEntityManager;
import com.fy.engineserver.datasource.article.manager.ArticleManager;
import com.fy.engineserver.datasource.language.Translate;
import com.fy.engineserver.menu.MenuWindow;
import com.fy.engineserver.menu.Option;
import com.fy.engineserver.menu.Option_Cancel;
import com.fy.engineserver.menu.WindowManager;
import com.fy.engineserver.menu.horse.Option_ConfirmReplaceHorseEquStrong;
import com.fy.engineserver.menu.horse.Option_ConfirmReplaceInlayBinded;
import com.fy.engineserver.message.CONFIRM_WINDOW_REQ;
import com.fy.engineserver.message.GameMessageFactory;
import com.fy.engineserver.message.QUERY_ARTICLE_INFO_RES;
import com.fy.engineserver.message.QUERY_ARTICLE_RES;
import com.fy.engineserver.minigame.MinigameConstant;
import com.fy.engineserver.sprite.Player;
import com.xuanzhi.tools.simplejpa.SimpleEntityManager;
import com.xuanzhi.tools.simplejpa.SimpleEntityManagerFactory;

public class HorseEquInlayEntityManager {
	private static HorseEquInlayEntityManager instance;
	
	public static Logger logger = HorseEquInlayManager.logger;
	
	public static final int 开孔 = 1;
	public static final int 洗孔  = 2;
	
	public static SimpleEntityManager<HorseEquInlay> em;
	
	public void init() {
		instance = this;
		em = SimpleEntityManagerFactory.getSimpleEntityManager(HorseEquInlay.class);
	}
	
	public void destroy() {
		try {
			em.destroy();
		} catch (Exception e) {
			logger.warn("[destory] [异常]", e);
		}
	}
	
	public static HorseEquInlayEntityManager getInst() {
		return instance;
	}
	/**
	 * 取出坐骑装备中的神匣
	 * @param player
	 * @param eeId
	 * @param index
	 * @return
	 */
	public String takeOff(Player player, long eeId, int index) {
		try {
			ArticleEntity ae = ArticleEntityManager.getInstance().getEntity(eeId);
			if (ae instanceof EquipmentEntity) {
				EquipmentEntity ee = (EquipmentEntity) ae;
				synchronized (player) {
					HorseEquInlay inlay = this.getEntity(ee);
					if (inlay == null) {
						return HorseEquInlayManager.getInst().getTranslate("punch_first");
					}
					if (index < 0 || index >= inlay.getInlayColorType().length) {
						return HorseEquInlayManager.getInst().getTranslate("wrong_inlay_index");
					}
					long oldId = inlay.getInlayArticleIds()[index];
					if (oldId <= 0) {
						return HorseEquInlayManager.getInst().getTranslate("no_xiazi_inlay");
					}
					if (!ArticleProtectManager.instance.isCanDo(player, ArticleProtectDataValues.ArticleProtect_High, ae.getId())) {
						player.sendError(Translate.高级锁魂物品不能做此操作);
						return Translate.高级锁魂物品不能做此操作;
					}
					Knapsack bag = player.getKnapsack_common();
					if (bag.isFull()) {
						return Translate.背包已满;
					}
					inlay.getInlayArticleIds()[index] = -1;
					em.notifyFieldChange(inlay, "inlayArticleIds");
					ArticleEntity oldAe = ArticleEntityManager.getInstance().getEntity(oldId);
					boolean b = bag.put(oldAe, "取出坐骑装备神匣");
					QUERY_ARTICLE_INFO_RES res = new QUERY_ARTICLE_INFO_RES(GameMessageFactory.nextSequnceNum(), eeId, ae.getInfoShow(player));
					player.addMessageToRightBag(res);
					logger.warn("[取出坐骑装备神匣] [成功] [" + player.getLogString() + "] [aeId:" + ee.getId() + "] [index:" + index + "] [baoshiId:" + oldId + "] [放入背包结果:" + b + "]");
				}
			} else {
				return HorseEquInlayManager.getInst().getTranslate("wrong_article");
			}
		} catch (Exception e) {
			logger.warn("[取出神匣] [异常] [" + player.getLogString() + "] [eeId:" + eeId + "] [index:" + index + "]", e);
		}
		return Translate.服务器出现错误;
	}
	
	/**
	 * 镶嵌神匣
	 * @param player
	 * @param eeId
	 * @param index
	 * @param inlayId
	 * @return
	 */
	public String inlay(Player player, long eeId, int index, long inlayId, boolean confirm) {
		try {
			ArticleEntity ae = ArticleEntityManager.getInstance().getEntity(eeId);
			if (ae instanceof EquipmentEntity) {
				EquipmentEntity ee = (EquipmentEntity) ae;
				HorseEquInlay inlay = this.getEntity(ee);
				if (inlay == null) {
					return HorseEquInlayManager.getInst().getTranslate("punch_first");
				}
				if (index < 0 || index >= inlay.getInlayColorType().length) {
					return HorseEquInlayManager.getInst().getTranslate("wrong_inlay_index");
				}
				if (inlay.getInlayColorType()[index] < 0) {
					return HorseEquInlayManager.getInst().getTranslate("punch_first");
				}
				ArticleEntity baoshi = ArticleEntityManager.getInstance().getEntity(inlayId);
				boolean needBind = false;
				if (!ee.isBinded()) {
					needBind = baoshi.isBinded() || baoshi.isRealBinded();
					if (!confirm && needBind) {
						popConfirmWindow1(player, eeId, index, inlayId);
						return "";
					}
				}
				Article a = ArticleManager.getInstance().getArticle(baoshi.getArticleName());
				if (a instanceof InlayArticle) {
					InlayArticle aa = (InlayArticle) a;
					if (aa.getInlayType() <= 1) {
						return HorseEquInlayManager.getInst().getTranslate("xiazi_only");
					}
					synchronized (player) {
						HorseEquInlay entity = this.getEntity(ee);
						if (entity.getInlayColorType()[index] != aa.getInlayColorType()){
							return HorseEquInlayManager.getInst().getTranslate("wrong_xiazi_color");
						}
						long oldId = entity.getInlayArticleIds()[index];
						Knapsack bag = player.getKnapsack_common();
						if (bag.contains(ae) && bag.contains(baoshi)) {
							ArticleEntity tempAe = bag.removeByArticleId(baoshi.getId(), "镶嵌神匣到坐骑装备", false);
							if (tempAe == null) {
								return Translate.删除物品不成功;
							}
							entity.getInlayArticleIds()[index] = baoshi.getId();
							em.notifyFieldChange(inlay, "inlayArticleIds");
							logger.warn("[坐骑装备镶嵌神匣] [成功] [" + player.getLogString() + "] [eeId:" + ee.getId() + "] [index:" + index + "] [baoshiId:" + baoshi.getId() + "] [原位置id:" + oldId + "]");
							if (oldId > 0) {
								ArticleEntity oldAe = ArticleEntityManager.getInstance().getEntity(oldId);
								bag.put(oldAe, "取出坐骑装备神匣");
							}
							if (needBind) {
								ee.setBinded(true);
							}
							QUERY_ARTICLE_RES res = new QUERY_ARTICLE_RES(GameMessageFactory.nextSequnceNum(), new com.fy.engineserver.datasource.article.entity.client.ArticleEntity[0], new com.fy.engineserver.datasource.article.entity.client.PropsEntity[0], new com.fy.engineserver.datasource.article.entity.client.EquipmentEntity[] { CoreSubSystem.translate(ee) });
							player.addMessageToRightBag(res);
							
//							QUERY_ARTICLE_INFO_RES res = new QUERY_ARTICLE_INFO_RES(GameMessageFactory.nextSequnceNum(), eeId, ae.getInfoShow(player));
//							player.addMessageToRightBag(res);
							return null;
						} else {
							return Translate.物品不存在;
						}
					}
				} else {
					return HorseEquInlayManager.getInst().getTranslate("wrong_article");
				}
			} else {
				return HorseEquInlayManager.getInst().getTranslate("wrong_article");
			}
		} catch (Exception e) {
			logger.warn("[镶嵌神匣] [异常] [" + player.getLogString() + "] [eeId:" + eeId + "] [index:" + index + "] [inlayId:" + inlayId + "]", e);
		}
		return Translate.服务器出现错误;
	}
	
	/**
	 * 坐骑装备开孔、洗孔
	 * @param player
	 * @param eeId  坐骑装备id
	 * @param index	开孔索引
	 * @param opt  操作类型   0 开孔  1洗孔
	 * @return
	 */
	public String punch(Player player, long eeId, int index, int opt, long[] costIds1, int[] costNums2, boolean confirm) {
		try {
			ArticleEntity ae = ArticleEntityManager.getInstance().getEntity(eeId);
			if (ae instanceof EquipmentEntity) {
				EquipmentEntity ee = (EquipmentEntity) ae;
				if (ee.getStar() < HorseEquInlayManager.getInst().baseModule.getStarLimit() || ee.getColorType() < ArticleManager.equipment_color_完美橙) {
					return HorseEquInlayManager.getInst().getTranslate("invalid_star");
				}
				Equipment ep = (Equipment) ArticleManager.getInstance().getArticle(ae.getArticleName());
				if (ep.getEquipmentType() < 10 || ep.getEquipmentType() > 14) {
					return HorseEquInlayManager.getInst().getTranslate("wrong_article");
				}
				if (ep.getPlayerLevelLimit() < HorseEquInlayManager.getInst().baseModule.getEquLvLimit()) {
					return HorseEquInlayManager.getInst().getTranslate("invalid_equ_level");
				}
				synchronized (player) {
					Knapsack bag = player.getKnapsack_common();
					if (!bag.contains(ae)) {
						return HorseEquInlayManager.getInst().getTranslate("not_in_bag");
					}
					String needArticle = "";
					int needNum = 100;   // 需要重新做验证，根据客户端发过来的物品id做验证
					InlayModule m = HorseEquInlayManager.getInst().getModuleByIndex(index);
					if (m == null) {
						return HorseEquInlayManager.getInst().getTranslate("invalid_index");
					}
					String[] tempStr = m.getCostByType(opt);
					if (tempStr == null) {
						return HorseEquInlayManager.getInst().getTranslate("invalid_opt");
					}
					needArticle = tempStr[0];
					needNum = Integer.parseInt(tempStr[1]);
					int count = bag.countArticle(needArticle);
					
					if (count < needNum) {
						return Translate.text_299;
					}
					HorseEquInlay inlay = this.getEntity(ee);
					if (inlay == null) {
						inlay = new HorseEquInlay();
						inlay.setId(ee.getId());
						Arrays.fill(inlay.getInlayArticleIds(), -1);
						Arrays.fill(inlay.getInlayColorType(), -1);
						em.notifyNewObject(inlay);
						if (logger.isInfoEnabled()) {
							logger.info("[创建坐骑装备孔数据] [成功] [" + player.getLogString() +" ] [ " + ee.getId() + "]");
						}
					}
					if (index > 0 && inlay.getInlayColorType()[index-1] < 0) {
						return HorseEquInlayManager.getInst().getTranslate("invalid_index");
					}
					if (opt == 开孔 && inlay.getInlayColorType()[index] >= 0) {
						return HorseEquInlayManager.getInst().getTranslate("invalid_opt");
					}
					if (inlay.getInlayArticleIds()[index] > 0) {
						return HorseEquInlayManager.getInst().getTranslate("need_clear_inlay");
					}
					if (!confirm && opt == 洗孔) {
						//二次确认
						popConfirmWindow2(player, eeId, index, opt, costIds1, costNums2);
						return "";
					}
					for (int i=0; i<needNum; i++) {
						ArticleEntity aeee = player.removeArticle(tempStr[0], "坐骑装备开孔", "");
						if (aeee == null) {
							logger.warn("[坐骑装备开孔] [失败] [删除物品不成功] [" + player.getLogString() + "] [删除物品失败]");
							return Translate.删除物品不成功;
						}
					}
					int ran = player.random.nextInt(10000);
					int temp = 0;
					int resultIndex = -1;
					int[] oldInlayColors = Arrays.copyOf(inlay.getInlayColorType(), inlay.getInlayColorType().length);
					for (int i=0; i<m.getColorProb().length; i++) {
						temp += m.getColorProb()[i];
						if (ran <= temp) {
							resultIndex = i;
							break;
						}
					}
					if (logger.isInfoEnabled()) {
						logger.info("[坐骑装备开孔] [" + player.getLogString() + "] [eeid:" + eeId + "] [孔:" + index + "] [resultIndex:" + resultIndex + "] [ran:" + ran + "]");
					}
					if (resultIndex < 0) {
						resultIndex = 0;
						logger.warn("[坐骑装备开孔] [没有随机到孔颜色] [" + player.getLogString() + "] [eeid:" + eeId + "] [孔:" + index + "] [ran:" + ran + "]");
					}
					inlay.getInlayColorType()[index] = resultIndex;
					em.notifyFieldChange(inlay, "inlayColorType");
					logger.warn("[坐骑装备开孔] [成功] [" + player.getLogString() + "] [eeId:" + eeId + "] [" + Arrays.toString(oldInlayColors) + " --->" + Arrays.toString(inlay.getInlayColorType()) + "]");
					QUERY_ARTICLE_INFO_RES res = new QUERY_ARTICLE_INFO_RES(GameMessageFactory.nextSequnceNum(), eeId, ae.getInfoShow(player));
					player.addMessageToRightBag(res);
					player.sendError(Translate.操作成功);
					return null;
				}
			}
			return HorseEquInlayManager.getInst().getTranslate("wrong_article");
		} catch (Exception e) {
			logger.warn("[坐骑装备开孔] [异常] [" + player.getLogString() + "] [eeId:" + eeId + "] [index:" + index +"] [opt:" +opt + "]", e);
		}
		return Translate.服务器出现错误;
	}
	
	private void popConfirmWindow1(Player p, long horseEquId, int inlayIndex, long baoshiId) {
		WindowManager wm = WindowManager.getInstance();
		MenuWindow mw = wm.createTempMenuWindow(600);
		Option_ConfirmReplaceInlayBinded option1 = new Option_ConfirmReplaceInlayBinded();
		option1.setText(MinigameConstant.CONFIRM);
		option1.setHorseEquId(horseEquId);
		option1.setInlayIndex(inlayIndex);
		option1.setBaoshiId(baoshiId);
		Option_Cancel option2 = new Option_Cancel();
		option2.setText(MinigameConstant.CANCLE);
		Option[] options = new Option[] {option1, option2};
		mw.setOptions(options);
		String msg = HorseEquInlayManager.getInst().getTranslate("confirm_inlay_binded");		//0xff8400   0x5aff00
		mw.setDescriptionInUUB(msg);
		CONFIRM_WINDOW_REQ creq = new CONFIRM_WINDOW_REQ(GameMessageFactory.nextSequnceNum(), mw.getId(), mw.getDescriptionInUUB(), options);
		p.addMessageToRightBag(creq);
	}	
	
	private void popConfirmWindow2(Player p, long eeId, int index, int opt, long[] costIds1, int[] costNums2) {
		WindowManager wm = WindowManager.getInstance();
		MenuWindow mw = wm.createTempMenuWindow(600);
		Option_ConfirmReplaceHorseEquStrong option1 = new Option_ConfirmReplaceHorseEquStrong();
		option1.setText(MinigameConstant.CONFIRM);
		option1.setEeId(eeId);
		option1.setIndex(index);
		option1.setOpt(opt);
		option1.setCostIds1(costIds1);
		option1.setCostNums2(costNums2);
		
		Option_Cancel option2 = new Option_Cancel();
		option2.setText(MinigameConstant.CANCLE);
		Option[] options = new Option[] {option1, option2};
		mw.setOptions(options);
		String msg = HorseEquInlayManager.getInst().getTranslate("confirm_punch");		//0xff8400   0x5aff00
		mw.setDescriptionInUUB(msg);
		CONFIRM_WINDOW_REQ creq = new CONFIRM_WINDOW_REQ(GameMessageFactory.nextSequnceNum(), mw.getId(), mw.getDescriptionInUUB(), options);
		p.addMessageToRightBag(creq);
	}
	
	
	/**
	 * 
	 * @param ee
	 * @return 不满足开孔条件的装备返回null
	 */
	public HorseEquInlay getEntity(EquipmentEntity ee) {
		Equipment e = (Equipment) ArticleManager.getInstance().getArticle(ee.getArticleName());
		if (e.getEquipmentType() < 10 || e.getEquipmentType() > 14) {			//只针对坐骑装备
			return null;
		}
		if (ee.getStar() < HorseEquInlayManager.getInst().baseModule.getStarLimit()) {
			return null;
		}
		if (ee.getHorseInlay() != null) {
			return ee.getHorseInlay();
		}
		try {
			HorseEquInlay inlay = em.find(ee.getId());
			if (inlay != null) {
				ee.setHorseInlay(inlay);
			}
		} catch (Exception ex) {
			logger.warn("[获取坐骑装备开孔] [异常] [" + ee.getId() + "]", ex);
		}
		return ee.getHorseInlay();
	}
	
}
