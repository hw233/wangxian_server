package com.fy.engineserver.datasource.article.data.magicweapon.huntLife;

import org.slf4j.Logger;

import com.fy.engineserver.articleEnchant.EnchantEntityManager;
import com.fy.engineserver.datasource.article.concrete.DefaultArticleEntityManager;
import com.fy.engineserver.datasource.article.data.articles.Article;
import com.fy.engineserver.datasource.article.data.entity.ArticleEntity;
import com.fy.engineserver.datasource.article.data.entity.HuntLifeArticleEntity;
import com.fy.engineserver.datasource.article.data.entity.NewMagicWeaponEntity;
import com.fy.engineserver.datasource.article.data.magicweapon.huntLife.instance.HuntArticleExtraData;
import com.fy.engineserver.datasource.article.data.magicweapon.huntLife.instance.HuntLifeEntity;
import com.fy.engineserver.datasource.article.data.magicweapon.huntLife.model.ShouHunModel;
import com.fy.engineserver.datasource.article.manager.ArticleManager;
import com.fy.engineserver.datasource.language.Translate;
import com.fy.engineserver.message.GameMessageFactory;
import com.fy.engineserver.message.QUERY_ARTICLE_INFO_RES;
import com.fy.engineserver.sprite.Player;
import com.fy.engineserver.sprite.concrete.GamePlayerManager;
import com.xuanzhi.tools.simplejpa.SimpleEntityManager;
import com.xuanzhi.tools.simplejpa.SimpleEntityManagerFactory;

public class HuntLifeEntityManager {

	public static Logger logger = HuntLifeManager.logger;

	public static HuntLifeEntityManager instance;

	public static SimpleEntityManager<HuntLifeEntity> em;
	public static SimpleEntityManager<HuntArticleExtraData> em_ae;

	public static final int 气血 = 0;
	public static final int 攻击 = 1;
	public static final int 外防 = 2;
	public static final int 内防 = 3;
	public static final int 暴击 = 4;
	public static final int 命中 = 5;
	public static final int 闪避 = 6;
	public static final int 破甲 = 7;
	public static final int 精准 = 8;
	public static final int 免暴 = 9;

	public void init() {
		instance = this;
		em = SimpleEntityManagerFactory.getSimpleEntityManager(HuntLifeEntity.class);
		em_ae = SimpleEntityManagerFactory.getSimpleEntityManager(HuntArticleExtraData.class);
		try {
			GamePlayerManager manag = (GamePlayerManager) GamePlayerManager.getInstance();
			if (manag.namePlayerMap.size() > 0 || manag.idPlayerMap.size() > 0) {
				Player[] players = (Player[]) manag.namePlayerMap.values().toArray();
				for (Player p : players) {
					p.modifyShouhun();
				}
				Player[] players2 = (Player[]) manag.idPlayerMap.values().toArray();
				for (Player p : players2) {
					p.modifyShouhun();
				}
			}
		} catch (Exception e) {
			logger.warn("[重新初始化兽魂属性] [异常]", e);
		}
	}

	/**
	 * 摘取兽魂
	 * @return
	 */
	public boolean takeShouhun(Player player, long articleId) {
		ArticleEntity ae = DefaultArticleEntityManager.getInstance().getEntity(articleId);
		if (articleId <= 0) {
			return false;
		}
		if (ae == null) {
			logger.warn("[摘取兽魂] [失败] [不存在物品] [物品id:" + articleId + "] [" + player.getLogString() + "]");
		}
		HuntLifeEntity entity = this.getHuntLifeEntity(player);
		if (entity == null) {
			logger.warn("[摘取兽魂] [失败] [HuntLifeEntity为空] [" + player.getLogString() + "]");
			return false;
		}
		Article a = ArticleManager.getInstance().getArticle(ae.getArticleName());
		if (!(a instanceof HuntLifeArticle)) {
			player.sendError(Translate.摘取失败);
			logger.warn("[摘取兽魂] [失败] [摘取的物品不是兽魂] [" + player.getLogString() + "] [articleId:" + articleId + "]");
			return false;
		}
		if(player.countEmptyCell4ShouhunKnap() <=0 ){
			return false;
		}
		
		int type = ((HuntLifeArticle)a).getAddAttrType();
		if (entity.getHuntDatas()[type] != articleId) {
			player.sendError(Translate.摘取失败);
			logger.warn("[摘取兽魂] [失败] [物品id与同属性位置镶嵌id不一样] [articleId:"+articleId+"] [entity.getHuntDatas()[type]:" +entity.getHuntDatas()[type]+ "] [" + player.getLogString() + "] [articleId:" + articleId + "]");
			return false;
		}
//		if (entity.getHuntDatas()[type] > 0) {
		this.unloadHuntLIfeAttr(player, type);
//		}
		entity.getHuntDatas()[type] = -1;
		em.notifyFieldChange(entity, "huntDatas");
		boolean b = player.putArticle2ShouhunKnap((HuntLifeArticleEntity) ae);
		player.sendError(Translate.摘取成功);
		logger.warn("[摘取兽魂] [成功] [" + player.getLogString4Knap() + "] [aeId:" + ae.getId() + "] [aeName:"+ae.getArticleName()+"] [放入背包:" + b + "] ");
		return b;
	}

	/**
	 * 镶嵌兽魂
	 * @return
	 */
	public boolean inlay(Player player, long articleId, boolean tips) {
		ArticleEntity ae = DefaultArticleEntityManager.getInstance().getEntity(articleId);
		if (!player.isShouhunInKnap(articleId)) {
			player.sendError(Translate.物品不在兽魂仓库);
			return false;
		}
		if (!(ae instanceof HuntLifeArticleEntity)) {
			player.sendError("镶嵌物品不是兽魂");
			return false;
		}
		HuntLifeArticle a = (HuntLifeArticle) ArticleManager.getInstance().getArticle(ae.getArticleName());
		HuntLifeEntity entity = this.getHuntLifeEntity(player);
		if (entity == null) {
			player.sendError(Translate.等级不足);
			return false;
		}
		if (a.getAddAttrType() >= entity.getHuntDatas().length) {
			player.sendError("镶嵌位置错误！" + a.getAddAttrType());
			return false;
		}
		
		if (!player.removeShouhun(articleId)) {
			player.sendError(Translate.删除物品不成功);
			return false;
		}
		if (entity.getHuntDatas()[a.getAddAttrType()] > 0) {
			this.unloadHuntLIfeAttr(player, a.getAddAttrType());			//卸载属性
			ArticleEntity backAe = DefaultArticleEntityManager.getInstance().getEntity((entity.getHuntDatas()[a.getAddAttrType()]));
			player.putArticle2ShouhunKnap((HuntLifeArticleEntity) backAe);
		}
		entity.getHuntDatas()[a.getAddAttrType()] = articleId;
		em.notifyFieldChange(entity, "huntDatas");
		this.loadHuntLifeAttr(player, a.getAddAttrType());
		if (logger.isWarnEnabled()) {
			logger.warn("[镶嵌兽魂] [成功] [" + player.getLogString4Knap() + "] [兽魂id:" + articleId + "] [兽魂名:" + ae.getArticleName() + "] [兽魂等级:" + ((HuntLifeArticleEntity)ae).getLevel() + "]");
		}
		if (tips) {
			player.sendError(Translate.镶嵌成功);
		}
		return true;
	}

	/**
	 * 吞噬
	 * @param player
	 * @param mainId
	 *            //主兽魂id
	 * @param materiIds
	 *            //被吞噬的兽魂id
	 * @return
	 */
	public boolean tunshi(Player player, long mainId, long[] materiIds) {
		if (player.getHuntLifr() != null) {
			HuntLifeArticleEntity mainAe = (HuntLifeArticleEntity) DefaultArticleEntityManager.getInstance().getEntity(mainId);
			HuntLifeArticle a = (HuntLifeArticle) ArticleManager.getInstance().getArticle(mainAe.getArticleName());
			HuntLifeEntity entity = this.getHuntLifeEntity(player);
			if (entity.getHuntDatas()[a.getAddAttrType()] != mainId) {
				player.sendError(Translate.主兽魂不在装备上);
				return false;
			}
			if (mainAe.getExtraData().getLevel() >= HuntLifeManager.兽魂满级) {
				player.sendError(Translate.兽魂已到满级);
				return false;
			}
			long levelExp = HuntLifeManager.instance.getExpByLevel(mainAe.getExtraData().getLevel());
			long leftNeedExp = HuntLifeManager.instance.allNeedExp - levelExp - mainAe.getExtraData().getExps();
			if (logger.isDebugEnabled()) {
				logger.debug("[兽魂吞噬] [满经验:" + HuntLifeManager.instance.allNeedExp + "] [levelExp:" + levelExp + "] [oldExp:" + mainAe.getExtraData().getExps() + "] [leftNeedExp:" + leftNeedExp  + "]");
			}
			long addExp = 0;
			for (int i=0; i<materiIds.length; i++) {				//先检测客户端发过来的材料id是否可以被吞噬，之后再删除加经验
				if (!player.isShouhunInKnap(materiIds[i])) {
					player.sendError(Translate.物品不在兽魂仓库);
					return false;
				}
				ArticleEntity ae = DefaultArticleEntityManager.getInstance().getEntity(materiIds[i]);
				if (ae instanceof HuntLifeArticleEntity) {
					HuntLifeArticle ma = (HuntLifeArticle) ArticleManager.getInstance().getArticle(ae.getArticleName());
					if (ma.getAddAttrType() != a.getAddAttrType()) {
						player.sendError(Translate.只能吞噬同类型兽魂);
						return false;
					}
					if (((HuntLifeArticleEntity) ae).getExtraData().getLevel() >= HuntLifeManager.兽魂满级) {
						player.sendError(Translate.满级兽魂不能被吞噬);
						return false;
					}
				} else {
					player.sendError("只能吞噬兽魂");
					return false;
				}
			}
			for (int i=0; i<materiIds.length; i++) {
				boolean result = player.removeShouhun(materiIds[i]);
				if (!result) {
					player.sendError(Translate.删除物品不成功);
					return false;
				}
				ArticleEntity ae = DefaultArticleEntityManager.getInstance().getEntity(materiIds[i]);
				EnchantEntityManager.sendArticleStat(player, ae, "兽魂吞噬删除");
				HuntArticleExtraData extraData = ((HuntLifeArticleEntity)ae).getExtraData();
				int level = 1;
				long extraExp = 0;
				if (extraData != null) {
					level = extraData.getLevel();
					extraExp = extraData.getExps();
				}
				ShouHunModel model = HuntLifeManager.instance.shouhunModels.get(level);
				addExp = addExp + extraExp + model.getExp4Give();
				if (logger.isInfoEnabled()) {
					logger.info("[兽魂吞噬] [aeId:" + materiIds[i] + "] [level:" + level + "] [extraExp:" + extraExp + "] [" + player.getLogString() + "]");
				}
				if (addExp >= leftNeedExp) {
					break;
				}
			}
			long tempExp = mainAe.getExtraData().getExps() + addExp;
			int tempLevel = mainAe.getExtraData().getLevel();
			for (int i=mainAe.getExtraData().getLevel(); i<(HuntLifeManager.兽魂满级); i++) {
				ShouHunModel model = HuntLifeManager.instance.shouhunModels.get(i);
				if (tempExp >= model.getExp4Up()) {
					tempLevel = model.getLevel()+1;
					tempExp -= model.getExp4Up();
				} else {
					break;
				}
			}
			mainAe.getExtraData().setExps(tempExp);
			if (mainAe.getExtraData().getLevel() < tempLevel) {
				this.unloadHuntLIfeAttr(player, a.getAddAttrType());
				mainAe.getExtraData().setLevel(tempLevel);
				this.loadHuntLifeAttr(player, a.getAddAttrType());
			}
			QUERY_ARTICLE_INFO_RES res = new QUERY_ARTICLE_INFO_RES(GameMessageFactory.nextSequnceNum(), mainAe.getId(), mainAe.getInfoShow(player));
			player.addMessageToRightBag(res);
			logger.warn("[兽魂吞噬] [成功] [aeId :" + mainAe.getId() + "] ["+mainAe.getArticleName()+"] [" + mainAe.getExtraData().getlogString() + "] [" + player.getLogString() + "]" );
			return true;
		}
		return false;
	}
	
	public long getMateriaAddExps(long[] materiaIds) {
		long addExp = 0;
		for (int i=0; i<materiaIds.length; i++) {
			ArticleEntity ae = DefaultArticleEntityManager.getInstance().getEntity(materiaIds[i]);
			HuntArticleExtraData extraData = ((HuntLifeArticleEntity)ae).getExtraData();
			int level = 1;
			long extraExp = 0;
			if (extraData != null) {
				level = extraData.getLevel();
				extraExp = extraData.getExps();
			}
			ShouHunModel model = HuntLifeManager.instance.shouhunModels.get(level);
			addExp = addExp + extraExp + model.getExp4Give();
		}
		return addExp;
	}

	/**
	 * 检测是否可以给玩家加载命格面板属性 (需要判断是否穿着法宝等)
	 * @param player
	 * @param type  镶嵌属性类型
	 * @return
	 */
	public boolean check(Player player, int type) {
		HuntLifeEntity he = this.getHuntLifeEntity(player);
		if (he != null && he.getHuntDatas()[type] > 0) {
			ArticleEntity ae = player.getEquipmentColumns().get(11);
			if (ae != null && ae instanceof NewMagicWeaponEntity && ((NewMagicWeaponEntity)ae).getMdurability() > 0) {
				return true;
			}
		}
		return false;
	}
	/**
	 * 当前装备法宝是否有耐久，没有耐久不能卸载属性
	 * @param player
	 * @return
	 */
	public boolean isMaigicDurable(Player player) {
		ArticleEntity ae = player.getEquipmentColumns().get(11);
		if (ae != null && ae instanceof NewMagicWeaponEntity && ((NewMagicWeaponEntity)ae).getMdurability() > 0) {
			return true;
		}
		return false;
	}
	
	public void loadAllAttr(Player player) {
		if (player.getHuntLifr() != null) {
			if (player.getHuntLifr().isHasLoadAllAttr()) {
				if (logger.isDebugEnabled()) {
					logger.debug("[loadAllAttr] [已经加载过所有属性] [" + player.getLogString() + "]");
				}
				return ;
			}
			for (int i=0; i<player.getHuntLifr().getHuntDatas().length; i++) {
				long id = player.getHuntLifr().getHuntDatas()[i];
				if (id > 0) {
					loadHuntLifeAttr(player, i);
				}
			}
			player.getHuntLifr().setHasLoadAllAttr(true);
		}
	}
	
	public void unloadAllAttr(Player player) {
		if (player.getHuntLifr() != null && isMaigicDurable(player)) {
			if (!player.getHuntLifr().isHasLoadAllAttr()) {
				if (logger.isDebugEnabled()) {
					logger.debug("[loadAllAttr] [已经卸载过所有属性] [" + player.getLogString() + "]");
				}
				return ;
			}
			for (int i=0; i<player.getHuntLifr().getHuntDatas().length; i++) {
				long id = player.getHuntLifr().getHuntDatas()[i];
				if (id > 0) {
					unloadHuntLIfeAttr(player, i);
				}
			}
			player.getHuntLifr().setHasLoadAllAttr(false);
		}
	}

	public void loadHuntLifeAttr(Player player, int type) {
		try {
			if (check(player, type)) {
				HuntLifeEntity entity = getHuntLifeEntity(player);
				if (entity.getHuntDatas()[type] <= 0) {
					return ;
				}
				HuntLifeArticleEntity ae = (HuntLifeArticleEntity) DefaultArticleEntityManager.getInstance().getEntity(entity.getHuntDatas()[type]);
				int lv = ae.getExtraData().getLevel();
				ShouHunModel model = HuntLifeManager.instance.shouhunModels.get(lv);
				int addNum = model.getAddAttrNums()[type];
				int extraNum = 0;
				if (player.getShouhunAttr() > 0) {
					extraNum = (int) ((player.getShouhunAttr() / 100f) * addNum);
					addNum += extraNum;
				}
				if (logger.isDebugEnabled()) {
					logger.debug("[测试加载属性] [type:" + type + "] [addNum:" + addNum + "] [extraNum:"+extraNum+"] [" + player.getLogString() + "] [player.getShouhunAttr():" +player.getShouhunAttr() + "]");
				}
				switch (type) {
				case 气血: player.setMaxHPB(player.getMaxHPB() + addNum); break;
				case 攻击:
					if (player.getCareer() == 1 || player.getCareer() == 2) {
						player.setPhyAttackB(player.getPhyAttackB() + addNum);
					} else {
						player.setMagicAttackB(player.getMagicAttackB() + addNum);
					}
					break;
				case 外防:player.setPhyDefenceB(player.getPhyDefenceB() + addNum); break;
				case 内防:player.setMagicDefenceB(player.getMagicDefenceB() + addNum);break;
				case 暴击:player.setCriticalHitB(player.getCriticalHitB() + addNum);break;
				case 命中:player.setHitB(player.getHitB() + addNum);break;
				case 闪避:player.setDodgeB(player.getDodgeB() + addNum);break;
				case 破甲:player.setBreakDefenceB(player.getBreakDefenceB() + addNum);break;
				case 精准:player.setAccurateB(player.getAccurateB() + addNum);break;
				case 免暴:player.setCriticalDefenceB(player.getCriticalDefenceB() + addNum);break;
				default:
					break;
				}
			}
		} catch (Exception e) {
			logger.warn("[加载兽魂属性] [异常] [" + player.getLogString() + "] [" + type + "]", e);
		}
	}
	
	public void unloadHuntLIfeAttr(Player player, int type) {
		try {
			if (check(player, type)) {
				HuntLifeEntity entity = getHuntLifeEntity(player);
				if (entity.getHuntDatas()[type] <= 0) {
					return ;
				}
				HuntLifeArticleEntity ae = (HuntLifeArticleEntity) DefaultArticleEntityManager.getInstance().getEntity(entity.getHuntDatas()[type]);
				int lv = ae.getExtraData().getLevel();
				ShouHunModel model = HuntLifeManager.instance.shouhunModels.get(lv);
				int addNum = model.getAddAttrNums()[type];
				int extraNum = 0;
				if (player.getShouhunAttr() > 0) {
					extraNum = (int) ((player.getShouhunAttr() / 100f) * addNum);
					addNum += extraNum;
				}
				if (logger.isDebugEnabled()) {
					logger.debug("[测试卸载属性] [type:" + type + "] [addNum:" + addNum + "] [extraNum:"+extraNum+"] [player.getShouhunAttr():"+player.getShouhunAttr()+"] [" + player.getLogString() + "]");
				}
				switch (type) {
				case 气血: player.setMaxHPB(player.getMaxHPB() - addNum); break;
				case 攻击:
					if (player.getCareer() == 1 || player.getCareer() == 2) {
						player.setPhyAttackB(player.getPhyAttackB() - addNum);
					} else {
						player.setMagicAttackB(player.getMagicAttackB() - addNum);
					}
					break;
				case 外防:player.setPhyDefenceB(player.getPhyDefenceB() - addNum); break;
				case 内防:player.setMagicDefenceB(player.getMagicDefenceB() - addNum);break;
				case 暴击:player.setCriticalHitB(player.getCriticalHitB() - addNum);break;
				case 命中:player.setHitB(player.getHitB() - addNum);break;
				case 闪避:player.setDodgeB(player.getDodgeB() - addNum);break;
				case 破甲:player.setBreakDefenceB(player.getBreakDefenceB() - addNum);break;
				case 精准:player.setAccurateB(player.getAccurateB() - addNum);break;
				case 免暴:player.setCriticalDefenceB(player.getCriticalDefenceB() - addNum);break;
				default:
					break;
				}
			}
		} catch (Exception e) {
			logger.warn("[卸载兽魂属性] [异常] [" + player.getLogString() + "] [" + type + "]", e);
		}
	}
	
	public HuntLifeEntity getHuntLifeEntity(Player player) {
//		if (player.getLevel() < 110) {			//110级前不开放此功能
//			return null;
//		}
		if (player.getHuntLifr() != null) {
			return player.getHuntLifr();
		}
		HuntLifeEntity entity = null;
		try {
			entity = em.find(player.getId());
			if (entity == null) {
				entity = new HuntLifeEntity();
				entity.setId(player.getId());
				entity.setLastTaskFreeTime(System.currentTimeMillis());
				em.notifyNewObject(entity);
			}
			player.setHuntLifr(entity);
		} catch (Exception e) {
			logger.warn("[getHuntLifeEntity] [异常] [" + player.getLogString() + "]", e);
		}
		return entity;
	}
	/**
	 * 
	 * @param ae
	 * @return  
	 */
	public HuntArticleExtraData getHuntArticleExtraData(HuntLifeArticleEntity ae) {
		if (ae.getExtraData() != null) {
			return ae.getExtraData();
		}
		HuntArticleExtraData entity = null;
		try {
			 entity = em_ae.find(ae.getId());
			 if (entity == null) {
				 entity = new HuntArticleExtraData();
				 entity.setId(ae.getId());
				 entity.setLevel(1);
				 em_ae.notifyNewObject(entity);
				 logger.warn("[HuntArticleExtraData] [创建entity] [" + ae.getId() + "]");
			 }
		} catch (Exception e) {
			logger.warn("[HuntArticleExtraData] [异常] [" + ae.getId() + "]", e);
		}
		ae.setExtraData(entity);
		return entity;
	}
	
	public void destory() {
		if (em != null) {
			em.destroy();
			em_ae.destroy();
		}
	}
	
	public void createNewData(HuntLifeArticleEntity ae, int level) {
		if (ae.getExtraData() != null) {
			return ;
		}
		HuntArticleExtraData entity = null;
		try {
			 entity = em_ae.find(ae.getId());
			 if (entity == null) {
				 entity = new HuntArticleExtraData();
				 entity.setId(ae.getId());
				 if (level > 0) {
					 entity.setLevel(level);
				 } else {
					 entity.setLevel(1);
				 }
				 em_ae.notifyNewObject(entity);
			 }
		} catch (Exception e) {
			logger.warn("[HuntArticleExtraData] [异常] [" + ae.getId() + "]", e);
		}
		ae.setExtraData(entity);
	}
}
