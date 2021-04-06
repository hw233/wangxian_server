package com.fy.engineserver.articleProtect;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.fy.engineserver.datasource.article.data.articles.Article;
import com.fy.engineserver.datasource.article.data.articles.InlayArticle;
import com.fy.engineserver.datasource.article.data.entity.ArticleEntity;
import com.fy.engineserver.datasource.article.data.entity.BaoShiXiaZiData;
import com.fy.engineserver.datasource.article.data.entity.EquipmentEntity;
import com.fy.engineserver.datasource.article.data.entity.PetEggPropsEntity;
import com.fy.engineserver.datasource.article.data.entity.PetPropsEntity;
import com.fy.engineserver.datasource.article.data.entity.Special_1EquipmentEntity;
import com.fy.engineserver.datasource.article.manager.ArticleEntityManager;
import com.fy.engineserver.datasource.article.manager.ArticleManager;
import com.fy.engineserver.datasource.language.Translate;
import com.fy.engineserver.menu.MenuWindow;
import com.fy.engineserver.menu.Option;
import com.fy.engineserver.menu.Option_Cancel;
import com.fy.engineserver.menu.WindowManager;
import com.fy.engineserver.menu.articleProtect.Option_ArticleProtectBlock;
import com.fy.engineserver.menu.articleProtect.Option_ArticleProtectUnBlock;
import com.fy.engineserver.message.ARTICLEPROTECT_BLOCK_REQ;
import com.fy.engineserver.message.ARTICLEPROTECT_MSG_RES;
import com.fy.engineserver.message.ARTICLEPROTECT_OTHERMSG_RES;
import com.fy.engineserver.message.ARTICLEPROTECT_UNBLOCK_REQ;
import com.fy.engineserver.message.CONFIRM_WINDOW_REQ;
import com.fy.engineserver.message.GameMessageFactory;
import com.fy.engineserver.message.QUERY_ARTICLE_INFO_RES;
import com.fy.engineserver.sprite.Player;
import com.fy.engineserver.sprite.PlayerManager;
import com.fy.engineserver.sprite.pet.Pet;
import com.fy.engineserver.sprite.pet.PetManager;
import com.fy.engineserver.util.ServiceStartRecord;
import com.xuanzhi.tools.simplejpa.SimpleEntityManager;
import com.xuanzhi.tools.simplejpa.SimpleEntityManagerFactory;


/**
 *	物品保护manager
 */
public class ArticleProtectManager implements Runnable {
	public static boolean isRun = true;
	public static long runTime = 1000L * 10;

	public static Logger logger = LoggerFactory.getLogger(ArticleProtectManager.class);
	
	public HashMap<Long, PlayerArticleProtectData> playerDatas = new HashMap<Long, PlayerArticleProtectData>();
	
	public SimpleEntityManager<ArticleProtectData> em;
	
	public static ArticleProtectManager instance;
	
	public static String COMMON_PROP_NAME = Translate.锁魂符;
	public static String HIGH_PROP_NAME = Translate.高级锁魂符;
	
	public static String UNBLOCK_PROP_NAME = Translate.解魂石;
	
	public static long UNBLOCK_TIME = 1000L * 60 * 60 * 24;
//	public static long UNBLOCK_TIME = 1000L * 60 * 6;
	
	public static long CATCH_REMOVE_TIME = 1000L * 60 * 60 * 2;
	
	public void init() {
		
		instance = this;
		
		em = SimpleEntityManagerFactory.getSimpleEntityManager(ArticleProtectData.class);
		PlayerArticleProtectData.em = em;
		new Thread(this, "ArticleProtect").start();
		ServiceStartRecord.startLog(this);
	}
	
	public void destroy() {
		em.destroy();
	}
	
	public ArticleProtectData removeBlock(Player player, long entityID) {
		PlayerArticleProtectData data = getPlayerData(player);
		ArticleProtectData da = data.getProtectData(ArticleEntityManager.getInstance().getEntity(entityID));
		if (da != null) {
			try{
				em.remove(da);
				data.getDatas().remove(da);
				logger.warn("[删除block记录] [{}] [{}] [{}]", new Object[]{player.getLogString(), entityID, da.getLogString(false)});
				return da;
			}catch(Exception e) {
				logger.error("删除锁定记录出错:" + player.getLogString() + "~" + entityID, e);
			}
		}
		return null;
	}
	
	public void blockOne(Player player, ArticleEntity entity, int state, int type) {
		PlayerArticleProtectData data = getPlayerData(player);
		int protect = getBlockState(player, entity.getId());
		if (protect < 0) {
			data.addOne(player, type, entity.getId(), state);
		}else {
			data.blockOne(player, type, entity.getId(), state);
		}
		send_ARTICLEPROTECT_MSG_RES(player);
		logger.warn("[关联锁定] [{}] [{}] [{}] [{}] [{}] [{}]", new Object[]{player.getLogString(), data.getLogString(), entity.getId(), entity.getArticleName(), state, type});
	}
	
	public int getBlockState(Player player, long entityID) {
		PlayerArticleProtectData data = getPlayerData(player);
		ArticleProtectData da = data.getProtectData(ArticleEntityManager.getInstance().getEntity(entityID));
		if (da == null) {
			return -1;
		}else {
			return da.getState();
		}
	}
	
	/**
	 * 普通锁魂解魂状态下是否可以操作
	 * @param player
	 * @param entityID
	 * @return
	 */
	public boolean isCanDo4UnBlock(Player player, long entityID) {
		try {
			PlayerArticleProtectData data = getPlayerData(player);
			int state_1 = -1;
			long articleID = -1;
			int articleType = -1;
			long removeTime = -1;
			ArticleEntity entity = ArticleEntityManager.getInstance().getEntity(entityID);
			if (entity == null) {
				return true;
			}
			Article a = ArticleManager.getInstance().getArticle(entity.getArticleName());
			if (a.get物品一级分类().equals(ArticleManager.物品一级分类类名[ArticleManager.物品一级分类_宝石类])) {
				articleType = ArticleProtectDataValues.ArticleProtect_ArticleType_Gem;
				articleID = entityID;
			}
			for (int i = 0; i < data.getDatas().size(); i++) {
				ArticleProtectData d = data.getDatas().get(i);
				if (d.getArticleType() == articleType && d.getArticleID() == articleID) {
					state_1 = d.getState();
					removeTime = d.getRemoveTime();
				}
			}
			if (state_1 == ArticleProtectDataValues.ArticleProtect_State_Common) {
				if (removeTime > 0) {
					return false;
				}
				return true;
			}
		} catch (Exception e) {
			logger.error("isCanDo4UnBlock出错", e);
		}
		return true;
	}
	
//	public boolean isXiaZiCanDo(Player player, int state, long entityID){
//		boolean isRealCanDo = true;
//		ArticleEntity entity = ArticleEntityManager.getInstance().getEntity(entityID);
//		if(entity != null){
//			Article a = ArticleManager.getInstance().getArticle(entity.getArticleName());
//			if(a != null && a instanceof InlayArticle){
//				if(((InlayArticle)a).getInlayType() > 1){
//					BaoShiXiaZiData xiazidata = ArticleManager.getInstance().getxiLianData(entityID);
//					if(xiazidata != null){
//						long ids[] = xiazidata.getIds();
//						for(long id : ids){
//							boolean cando = isCanDo(player, state, id);
//							if(cando == false){
//								isRealCanDo = false;
//								break;
//							}
//						}
//					}
//				}
//			}
//		}
//		return isRealCanDo;
//	}
	
	/**
	 * 是否可以执行操作，锁魂
	 * @param player
	 * @param state
	 * @param articleType
	 * @param articleID
	 * @return
	 */
	public boolean isCanDo(Player player, int state, long entityID) {
		try{
			PlayerArticleProtectData data = getPlayerData(player);
			int state_1 = -1;
			long articleID = -1;
			int articleType = -1;
			long ids [] = null;//匣子处理
			ArticleEntity entity = ArticleEntityManager.getInstance().getEntity(entityID);
			if (entity == null) {
				return true;
			}else {
				Article a = ArticleManager.getInstance().getArticle(entity.getArticleName());
				if (a.get物品一级分类().equals(ArticleManager.物品一级分类类名[ArticleManager.物品一级分类_角色装备类]) || a.get物品一级分类().equals(ArticleManager.物品一级分类类名[ArticleManager.物品一级分类_马匹装备类])) {
					articleType = ArticleProtectDataValues.ArticleProtect_ArticleType_Equpment;
					articleID = entityID;
				}else if (a.get物品一级分类().equals(ArticleManager.物品一级分类类名[ArticleManager.物品一级分类_宝石类])) {
					articleType = ArticleProtectDataValues.ArticleProtect_ArticleType_Gem;
					articleID = entityID;
				}else if (a.get物品一级分类().equals(ArticleManager.物品一级分类类名[ArticleManager.物品一级分类_宠物蛋]) || a.get物品一级分类().equals(ArticleManager.物品一级分类类名[ArticleManager.物品一级分类_宠物类])) {
					articleType = ArticleProtectDataValues.ArticleProtect_ArticleType_Pet;
					if (entity instanceof PetEggPropsEntity) {
						articleID = ((PetEggPropsEntity)entity).getPetId();
					}else if (entity instanceof PetPropsEntity) {
						articleID = ((PetPropsEntity)entity).getPetId();
					}
				} else if (a.get物品一级分类().equals(ArticleManager.物品一级分类类名[ArticleManager.物品一级分类_角色法宝类])) {
					articleType = ArticleProtectDataValues.ArticleProtect_ArticleType_Equpment;
					articleID = entity.getId();
				} 
				if(a != null && a instanceof InlayArticle){
					if(((InlayArticle)a).getInlayType() > 1){
						BaoShiXiaZiData xiazidata = ArticleManager.getInstance().getxiLianData(player,entityID);
						if(xiazidata != null){
							ids = xiazidata.getIds();
						}
					}
				}
			}
			
			if(ids != null && ids.length > 0){		//匣子处理
				for(long id : ids){
					for (int i = 0; i < data.getDatas().size(); i++) {
						ArticleProtectData d = data.getDatas().get(i);
						if (d.getArticleType() == articleType && d.getArticleID() == id) {
							if(state_1 < d.getState()){
								state_1 = d.getState();
							}
						}
					}
				}
			}else{
				for (int i = 0; i < data.getDatas().size(); i++) {
					ArticleProtectData d = data.getDatas().get(i);
					if (d.getArticleType() == articleType && d.getArticleID() == articleID) {
						state_1 = d.getState();
					}
				}
			}
			logger.warn("[检查是否可以操作] [{}] [{}] [是否宝石匣子:{}] [name:{}] [aeId:{}] [{}] [{}] [{}]", new Object[]{player.getLogString(), state,(ids==null?"否":"是"), entity.getArticleName(), entityID, state_1, articleType, articleID});
			if (state_1 == ArticleProtectDataValues.ArticleProtect_State_Common) {
				if (state == ArticleProtectDataValues.ArticleProtect_Common) {
					return false;
				}
				return true;
			}else if (state_1 == ArticleProtectDataValues.ArticleProtect_State_High) {
				return false;
			}
		}catch(Exception e) {
			logger.error("isCanDo出错", e);
		}
		return true;
	}
	
	public void send_ARTICLEPROTECT_OTHERMSG_RES(Player player) {
		try{
			String suohunMsg = Translate.普通锁魂说明;
			String jiehunMsg = Translate.解魂说明;
			String suohunPropName1 = COMMON_PROP_NAME;
			String suohunPropName2 = HIGH_PROP_NAME;
			String jiehunPropName = UNBLOCK_PROP_NAME;
			ARTICLEPROTECT_OTHERMSG_RES res = new ARTICLEPROTECT_OTHERMSG_RES(GameMessageFactory.nextSequnceNum(), suohunMsg, jiehunMsg, suohunPropName1, suohunPropName2, jiehunPropName);
			player.addMessageToRightBag(res);
		}catch(Exception e) {
			logger.error("send_ARTICLEPROTECT_OTHERMSG_RES出错", e);
		}
	}
	
	public void send_ARTICLEPROTECT_MSG_RES(Player player) {
		try{
			PlayerArticleProtectData data = getPlayerData(player);
			ARTICLEPROTECT_MSG_RES req = new ARTICLEPROTECT_MSG_RES(GameMessageFactory.nextSequnceNum(), data.getInfos(player));
			player.addMessageToRightBag(req);
		}catch(Exception e) {
			logger.error("send_ARTICLEPROTECT_MSG_RES出错", e);
		}
	}
	
	public boolean isCanBlock(Player player, long entityID, int entityIndex, long propID, int propIndex) {
		try{
			ArticleEntity knEntity = player.getKnapsack_common().getArticleEntityByCell(entityIndex);
			ArticleEntity knProp = player.getKnapsack_common().getArticleEntityByCell(propIndex);
			if (knEntity == null || knProp == null) {
				player.sendError(Translate.您要锁魂的物品或锁魂符不存在);
				return false;
			}
			if (knEntity.getId() != entityID || knProp.getId() != propID) {
				player.sendError(Translate.您要锁魂的物品或锁魂符不存在);
				return false;
			}
			boolean isCommon = knProp.getArticleName().equals(COMMON_PROP_NAME);
			boolean isHigh = knProp.getArticleName().equals(HIGH_PROP_NAME);
			if (!isCommon && !isHigh) {
				player.sendError(Translate.您放入的不是锁魂符);
				return false;
			}
			Article a = ArticleManager.getInstance().getArticle(knEntity.getArticleName());
			if (a == null) {
				logger.error("[锁魂物品模板不存在] [{}] [{}]", new Object[]{player.getLogString(), knEntity.getId()});
				return false;
			}
			if (knEntity instanceof Special_1EquipmentEntity) {
				player.sendError(Translate.万灵榜装备不能锁魂);
				return false;
			}
			PlayerArticleProtectData data = getPlayerData(player);
			if (!data.canBlock(knEntity, isHigh)) {
				player.sendError(Translate.您放入物品不能锁魂);
				return false;
			}
			boolean isCanBlock = false;
			if (a.get物品一级分类().equals(ArticleManager.物品一级分类类名[ArticleManager.物品一级分类_角色装备类])) {
				isCanBlock = true;
			}else if (a.get物品一级分类().equals(ArticleManager.物品一级分类类名[ArticleManager.物品一级分类_马匹装备类])) {
				isCanBlock = true;
			}else if (a.get物品一级分类().equals(ArticleManager.物品一级分类类名[ArticleManager.物品一级分类_宠物蛋]) || a.get物品一级分类().equals(ArticleManager.物品一级分类类名[ArticleManager.物品一级分类_宠物类])) {
				Pet pet = null;
				if (knEntity instanceof PetEggPropsEntity) {
					long petID = ((PetEggPropsEntity)knEntity).getPetId();
					if (petID > 0) {
						pet = PetManager.getInstance().getPet(petID);
						if (pet != null) {
							if (pet.getRarity() >= 2) {
								isCanBlock = true;
							}
						}
					}
				}else if (knEntity instanceof PetPropsEntity) {
					long petID = ((PetPropsEntity)knEntity).getPetId();
					if (petID > 0) {
						pet = PetManager.getInstance().getPet(petID);
						if (pet != null) {
							if (pet.getRarity() == 2) {
								isCanBlock = true;
							}
						}
					}
				}
			}else if (a.get物品一级分类().equals(ArticleManager.物品一级分类类名[ArticleManager.物品一级分类_宝石类])) {
				if (a.getArticleLevel() >= 4) {
					isCanBlock = true;
				}
				//2014年12月22日15:57:28    修改法宝可以锁魂
			} else if (a.get物品一级分类().equals(ArticleManager.物品一级分类类名[ArticleManager.物品一级分类_角色法宝类])) {
				isCanBlock = true;
			}
//			else if(a != null && a instanceof InlayArticle){
//				if(((InlayArticle)a).getInlayType() > 1){
//					if (a.getArticleLevel() >= 4) {
//						isCanBlock = true;
//					}
//				}
//			}
			if (!isCanBlock) {
				player.sendError(Translate.您放入物品不能锁魂);
				return false;
			}
			return true;
		}catch(Exception e){
			logger.error("isCanBlock出错", e);
		}
		return false;
	}
	
	public void handle_ARTICLEPROTECT_BLOCK_REQ(Player player, ARTICLEPROTECT_BLOCK_REQ req) {
		try{
			long entityID = req.getEntityID();
			int entityIndex = req.getEntityIndex();
			long propID = req.getPropID();
			int propIndex = req.getPropIndex();
			
			if (!isCanBlock(player, entityID, entityIndex, propID, propIndex)) {
				return ;
			}
			
			ArticleEntity entity = ArticleEntityManager.getInstance().getEntity(entityID);
			
			ArticleEntity prop = ArticleEntityManager.getInstance().getEntity(propID);
			boolean isCommon = prop.getArticleName().equals(COMMON_PROP_NAME);
			boolean isHigh = prop.getArticleName().equals(HIGH_PROP_NAME);
			if (isCommon) {
				boolean b = isCanDo(player, ArticleProtectDataValues.ArticleProtect_High, entityID);
				if (!b) {
					player.sendError(Translate.锁魂物品不能做此操作);
					return ;
				}
			}
			MenuWindow mw = WindowManager.getInstance().createTempMenuWindow(600);
			mw.setTitle(Translate.确定锁魂);
			if (isCommon) {
				mw.setDescriptionInUUB(Translate.translateString(Translate.确定锁魂普通, new String[][]{{Translate.STRING_1, entity.getArticleName()}}));
			}
			if (isHigh) {
				mw.setDescriptionInUUB(Translate.translateString(Translate.确定锁魂高级, new String[][]{{Translate.STRING_1, entity.getArticleName()}}));
			}
			Option_ArticleProtectBlock opt1 = new Option_ArticleProtectBlock();
			opt1.setText(Translate.确定);
			opt1.setEntityID(entityID);
			opt1.setEntityIndex(entityIndex);
			opt1.setPropID(propID);
			opt1.setPropIndex(propIndex);
			Option_Cancel opt2 = new Option_Cancel();
			opt2.setText(Translate.取消);
			Option[] opts = new Option[]{opt1, opt2};
			mw.setOptions(opts);
			
			CONFIRM_WINDOW_REQ windowreq = new CONFIRM_WINDOW_REQ(GameMessageFactory.nextSequnceNum(), mw.getId(), mw.getDescriptionInUUB(), opts);
			player.addMessageToRightBag(windowreq);
		}catch(Exception e) {
			logger.error("handle_ARTICLEPROTECT_BLOCK_REQ出错", e);
		}
	}
	
	public void option_block(Player player, long entityID, int entityIndex, long propID, int propIndex) {
		try{
			if (!isCanBlock(player, entityID, entityIndex, propID, propIndex)) {
				return ;
			}
			ArticleEntity knEntity = player.getKnapsack_common().getArticleEntityByCell(entityIndex);
			ArticleEntity knProp = player.getKnapsack_common().getArticleEntityByCell(propIndex);
			player.getKnapsack_common().clearCell(propIndex, 1,"锁魂删除", true);
			PlayerArticleProtectData data = getPlayerData(player);
			boolean isCommon = knProp.getArticleName().equals(COMMON_PROP_NAME);
			boolean isHigh = knProp.getArticleName().equals(HIGH_PROP_NAME);
			int state = -1;
			if (isCommon) {
				state = ArticleProtectDataValues.ArticleProtect_State_Common;
			}
			if (isHigh) {
				state = ArticleProtectDataValues.ArticleProtect_State_High;
			}
			int articleType = -1;
			long articleID = -1;
			if (knEntity instanceof PetPropsEntity ) {
				articleType = ArticleProtectDataValues.ArticleProtect_ArticleType_Pet;
				articleID = ((PetPropsEntity)knEntity).getPetId();
			}else if (knEntity instanceof PetEggPropsEntity) {
				articleType = ArticleProtectDataValues.ArticleProtect_ArticleType_Pet;
				articleID = ((PetEggPropsEntity)knEntity).getPetId();
			}else {
				Article a = ArticleManager.getInstance().getArticle(knEntity.getArticleName());
				if (a.get物品一级分类().equals(ArticleManager.物品一级分类类名[ArticleManager.物品一级分类_宝石类])){
					articleType = ArticleProtectDataValues.ArticleProtect_ArticleType_Gem;
					articleID = knEntity.getId();
				}else {
					articleType = ArticleProtectDataValues.ArticleProtect_ArticleType_Equpment;
					articleID = knEntity.getId();
				}
			}
			ArticleProtectData da = data.getProtectData(knEntity);
			int blockState = -1;
			if (da != null) {
				blockState = da.getState();
			}
			if (blockState > 0) {
				boolean block = data.blockOne(player, articleType, articleID, state);
				if (block) {
					logger.warn("[继续锁魂成功] [{}] [{}] [{}] [{}]", new Object[]{player.getLogString(), data.getLogString(), knEntity.getId() + "-" + knEntity.getArticleName(), knProp.getArticleName()});
				} else {
					logger.warn("[继续锁魂失败] [{}] [{}] [{}] [{}]", new Object[]{player.getLogString(), data.getLogString(), knEntity.getId() + "-" + knEntity.getArticleName(), knProp.getArticleName()});
				}
			}else {
				boolean add = data.addOne(player, articleType, articleID, state);
				if (add) {
					logger.warn("[新加锁魂成功] [{}] [{}] [{}] [{}]", new Object[]{player.getLogString(), data.getLogString(), knEntity.getId() + "-" + knEntity.getArticleName(), knProp.getArticleName()});
				}else {
					logger.error("[新加锁魂失败] [{}] [{}] [{}] [{}]", new Object[]{player.getLogString(), data.getLogString(), knEntity.getId() + "-" + knEntity.getArticleName(), knProp.getArticleName()});
				}
			}
			send_ARTICLEPROTECT_MSG_RES(player);
			QUERY_ARTICLE_INFO_RES res = new QUERY_ARTICLE_INFO_RES(GameMessageFactory.nextSequnceNum(), articleID, knEntity.getInfoShow(player));
			player.addMessageToRightBag(res);
		}catch(Exception e) {
			logger.error("option_block出错", e);
		}
	}
	
	public boolean isCanUnBlock(Player player, long entityID, int entityIndex, long propID, int propIndex) {
		try{
			ArticleEntity knEntity = player.getKnapsack_common().getArticleEntityByCell(entityIndex);
			ArticleEntity knProp = player.getKnapsack_common().getArticleEntityByCell(propIndex);
			if (knEntity == null || knProp == null) {
				player.sendError(Translate.您要解魂的物品或解魂石不存在);
				return false;
			}
			if (knEntity.getId() != entityID || knProp.getId() != propID) {
				player.sendError(Translate.您要解魂的物品或解魂石不存在);
				return false;
			}
//			if (knEntity instanceof EquipmentEntity) {
//				EquipmentEntity ep = (EquipmentEntity) knEntity;
//				long[] baoshiIds = ep.getInlayArticleIds();
//				if (baoshiIds != null && baoshiIds.length > 0) {
//					for (int i=0; i<baoshiIds.length; i++) {
//						if (baoshiIds[i] > 0) {
//							boolean cando = this.isCanDo(player, ArticleProtectDataValues.ArticleProtect_High, entityID);
//							if (cando) {
//								cando = this.isCanDo(player, ArticleProtectDataValues.ArticleProtect_Common, entityID);
//							}
//							if (!cando) {
//								player.sendError(Translate.您要解魂的装备上有锁魂物品);
//								return false;
//							}
//						}
//					}
//				}
//			}
			boolean isUnBlockProp = knProp.getArticleName().equals(UNBLOCK_PROP_NAME);
			if (!isUnBlockProp) {
				player.sendError(Translate.您放入的不是解魂石);
				return false;
			}
			Article a = ArticleManager.getInstance().getArticle(knEntity.getArticleName());
			if (a == null) {
				logger.error("[解魂物品模板不存在] [{}] [{}]", new Object[]{player.getLogString(), knEntity.getId()});
				return false;
			}
			PlayerArticleProtectData data = getPlayerData(player);
			if (!data.canUnBlock(knEntity)) {
				player.sendError(Translate.您放入物品不能解魂);
				return false;
			}
			return true;
		}catch(Exception e) {
			logger.error("isCanUnBlock出错", e);
		}
		return false;
	}
	
	public void handle_ARTICLEPROTECT_UNBLOCK_REQ(Player player, ARTICLEPROTECT_UNBLOCK_REQ req) {
		try{
			long entityID = req.getEntityID();
			int entityIndex = req.getEntityIndex();
			long propID = req.getPropID();
			int propIndex = req.getPropIndex();
			if (!isCanUnBlock(player, entityID, entityIndex, propID, propIndex)) {
				return;
			}
			
			MenuWindow mw = WindowManager.getInstance().createTempMenuWindow(600);
			mw.setTitle(Translate.确定解魂);
			mw.setDescriptionInUUB(Translate.您确定解魂);
			Option_ArticleProtectUnBlock opt1 = new Option_ArticleProtectUnBlock();
			opt1.setText(Translate.确定);
			opt1.setEntityID(entityID);
			opt1.setEntityIndex(entityIndex);
			opt1.setPropID(propID);
			opt1.setPropIndex(propIndex);
			Option_Cancel opt2 = new Option_Cancel();
			opt2.setText(Translate.取消);
			Option[] opts = new Option[]{opt1, opt2};
			mw.setOptions(opts);
			
			CONFIRM_WINDOW_REQ windowreq = new CONFIRM_WINDOW_REQ(GameMessageFactory.nextSequnceNum(), mw.getId(), mw.getDescriptionInUUB(), opts);
			player.addMessageToRightBag(windowreq);
			
		}catch(Exception e) {
			logger.error("handle_ARTICLEPROTECT_UNBLOCK_REQ出错", e);
		}
	}
	
	public void option_unBlock(Player player, long entityID, int entityIndex, long propID, int propIndex) {
		try{
			if (!isCanUnBlock(player, entityID, entityIndex, propID, propIndex)) {
				return;
			}
			ArticleEntity knEntity = player.getKnapsack_common().getArticleEntityByCell(entityIndex);
			ArticleEntity knProp = player.getKnapsack_common().getArticleEntityByCell(propIndex);
			player.getKnapsack_common().clearCell(propIndex, 1,"解魂删除", true);
			PlayerArticleProtectData data = getPlayerData(player);
			long articleID = -1;
			int articleType = -1;
			List<Long> otherIds = new ArrayList<Long>();
			if (knEntity instanceof PetEggPropsEntity) {
				articleType = ArticleProtectDataValues.ArticleProtect_ArticleType_Pet;
				articleID = ((PetEggPropsEntity)knEntity).getPetId();
			}else if (knEntity instanceof PetPropsEntity) {
				articleType = ArticleProtectDataValues.ArticleProtect_ArticleType_Pet;
				articleID = ((PetPropsEntity)knEntity).getPetId();
			}else{
				Article a = ArticleManager.getInstance().getArticle(knEntity.getArticleName());
				if (a.get物品一级分类().equals(ArticleManager.物品一级分类类名[ArticleManager.物品一级分类_宝石类])){
					articleType = ArticleProtectDataValues.ArticleProtect_ArticleType_Gem;
					articleID = knEntity.getId();
				}else {
					articleType = ArticleProtectDataValues.ArticleProtect_ArticleType_Equpment;
					articleID = knEntity.getId();
					if (knEntity instanceof EquipmentEntity) {
						EquipmentEntity ee = (EquipmentEntity) knEntity;
						if (ee.getInlayArticleIds() != null && ee.getInlayArticleIds().length > 0) {
							for (int k=0; k<ee.getInlayArticleIds().length; k++) {
								if (ee.getInlayArticleIds()[k] > 0) {
									otherIds.add(ee.getInlayArticleIds()[k]);
								}
							}
						}
						if (ee.getInlayQiLingArticleIds() != null && ee.getInlayQiLingArticleIds().length > 0) {
							for (int k=0; k<ee.getInlayQiLingArticleIds().length; k++) {
								if (ee.getInlayQiLingArticleIds()[k] > 0) {
									otherIds.add(ee.getInlayQiLingArticleIds()[k]);
								}
							}
						}
					}
				}
			}
			boolean isUnBlock = data.unBlockOne(player, articleType, articleID);
			if (isUnBlock) {
				if (otherIds.size() > 0) {
					for (int i=0; i<otherIds.size(); i++) {
						data.unBlockOne(player, articleType, otherIds.get(i));
					}
				}
				/*ArticleEntity mainAe = DefaultArticleEntityManager.getInstance().getEntity(entityID);
				if (mainAe != null) {
					QUERY_ARTICLE_INFO_RES res = new QUERY_ARTICLE_INFO_RES(GameMessageFactory.nextSequnceNum(), mainAe.getId(), mainAe.getInfoShow(player));
					player.addMessageToRightBag(res);
				}*/
				logger.warn("[解魂成功] [{}] [{}] [{}]", new Object[]{player.getLogString(), data.getLogString(), knEntity.getId() + "-" + knEntity.getArticleName()});
			}else {
				logger.warn("[解魂失败] [{}] [{}] [{}]", new Object[]{player.getLogString(), data.getLogString(), knEntity.getId() + "-" + knEntity.getArticleName()});
			}
			send_ARTICLEPROTECT_MSG_RES(player);
			QUERY_ARTICLE_INFO_RES res = new QUERY_ARTICLE_INFO_RES(GameMessageFactory.nextSequnceNum(), articleID, knEntity.getInfoShow(player));
			player.addMessageToRightBag(res);
		}catch(Exception e) {
			logger.error("option_unBlock出错", e);
		}
	}
	
	public PlayerArticleProtectData getPlayerData(Player player) {
		PlayerArticleProtectData data = playerDatas.get(player.getId());
		if (data == null) {
			List<ArticleProtectData> list = loadPlayerArticleProtect(player.getId());
			data = new PlayerArticleProtectData();
			data.setPlayerID(player.getId());
			data.getDatas().addAll(list);
			playerDatas.put(player.getId(), data);
			logger.warn("[载入玩家锁魂数据] [{}] [{}]", new Object[]{player.getLogString(), data.getLogString()});
		}
		data.setLastTime(System.currentTimeMillis());
		return data;
	}

	public List<ArticleProtectData> loadPlayerArticleProtect(long pid) {
		List<ArticleProtectData> list = new ArrayList<ArticleProtectData>();
		try{
			long count = em.count(ArticleProtectData.class, "playerID=" + pid);
			list.addAll(em.query(ArticleProtectData.class, "playerID=" + pid, "", 1, count + 1));
		}catch(Exception e) {
			logger.error("从数据库载入数据仍出错",e );
		}
		return list;
	}
	
	@Override
	public void run() {
		while(isRun) {
			try{
				long startTime = System.currentTimeMillis();
				PlayerArticleProtectData[] playerDatas = this.playerDatas.values().toArray(new PlayerArticleProtectData[0]);
				for (PlayerArticleProtectData data : playerDatas) {
					boolean isUnBlock = data.relUnBlock();
					if (isUnBlock) {
						boolean isOnline = PlayerManager.getInstance().isOnline(data.getPlayerID());
						if (isOnline) {
							Player player = null;
							try{
								player = PlayerManager.getInstance().getPlayer(data.getPlayerID());
							}catch(Exception e) {
								
							}
							if (player != null) {
								send_ARTICLEPROTECT_MSG_RES(player);
								player.sendError(Translate.您有物品解魂成功注意查看);
//								QUERY_ARTICLE_INFO_RES res = new QUERY_ARTICLE_INFO_RES(GameMessageFactory.nextSequnceNum(), articleID, knEntity.getInfoShow(player));
//								player.addMessageToRightBag(res);
							}
						}
					}
				}
				
				playerDatas = this.playerDatas.values().toArray(new PlayerArticleProtectData[0]);
				for (PlayerArticleProtectData data : playerDatas) {
					if (System.currentTimeMillis() - data.getLastTime() > CATCH_REMOVE_TIME) {
						this.playerDatas.remove(data.getPlayerID());
						data.saveDatas();
					}
				}
				
				long now = System.currentTimeMillis();
				if (now - startTime > runTime) {
					logger.error("心跳超时: " + (now - startTime) + "ms");
				}else {
					try{
						Thread.sleep(runTime - now + startTime);
					}catch(Exception e) {
						logger.error("线程sleep出错", e);
					}
				}
			}catch(Exception e) {
				logger.error("心跳出错:", e);
			}
		}
	}
	
}
