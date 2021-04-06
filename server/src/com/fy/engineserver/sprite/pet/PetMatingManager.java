package com.fy.engineserver.sprite.pet;

import java.util.Collections;
import java.util.LinkedList;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.fy.engineserver.achievement.AchievementManager;
import com.fy.engineserver.achievement.RecordAction;
import com.fy.engineserver.articleProtect.ArticleProtectDataValues;
import com.fy.engineserver.articleProtect.ArticleProtectManager;
import com.fy.engineserver.datasource.article.data.articles.Article;
import com.fy.engineserver.datasource.article.data.entity.ArticleEntity;
import com.fy.engineserver.datasource.article.data.entity.PetEggPropsEntity;
import com.fy.engineserver.datasource.article.data.entity.PetPropsEntity;
import com.fy.engineserver.datasource.article.data.props.PetProps;
import com.fy.engineserver.datasource.article.manager.ArticleEntityManager;
import com.fy.engineserver.datasource.article.manager.ArticleManager;
import com.fy.engineserver.datasource.language.Translate;
import com.fy.engineserver.jiazu.petHouse.PetHouseManager;
import com.fy.engineserver.sprite.Player;
import com.fy.engineserver.sprite.PlayerManager;
import com.fy.engineserver.stat.ArticleStatManager;
import com.fy.engineserver.util.ServiceStartRecord;


/**
 * 宠物繁殖管理器
 * 预计正在繁殖和繁殖完成的实例不会很多，故设计在服务器启动后即加载所有正在繁殖和繁殖完成的实例，过期实例不加载
 * 
 *
 */
public  class PetMatingManager implements  Runnable {
	
public	static Logger logger = LoggerFactory.getLogger(PetManager.class.getName());
	
	protected static PetMatingManager self;
	
	/**
	 * 玩家的宠物繁殖实体列表，玩家可以同时繁殖多个宠物
	 */
	protected List<PetMating> petMatingList = Collections.synchronizedList( new LinkedList<PetMating>() );
	
	protected ArticleEntityManager articleEntityManager;
	
	protected ArticleManager articleManager;
	
	protected PlayerManager playerManager;
	
	protected PetManager petManager;
	
	public static long 会话时间 = 5 * 60 * 1000;
	
	protected static final Object lock = new Object() {};
	
	public static long temp_seq_id = 0;
	
	public synchronized static long nextId() {
		return temp_seq_id++;
	}
	
	public static PetMatingManager getInstance() {
		return self;
	}
	
	public void init() {
		
		long now = com.fy.engineserver.gametime.SystemTime.currentTimeMillis();
		self = this;
		Thread t = new Thread(this, "宠物繁殖管理");
		t.start();
		ServiceStartRecord.startLog(this);
	}
	
	public void destroy() {
		
	}
	
	public void run() {
		
		PetMating mating = null;
		while(true) {
			try {
				Thread.sleep(5*1000);
				
				if(!com.fy.engineserver.util.ContextLoadFinishedListener.isLoadFinished()){
					continue;
				}
				
				long now  =com.fy.engineserver.gametime.SystemTime.currentTimeMillis();
				synchronized(this) {
					for(int i=petMatingList.size()-1; i>=0; i--) {
						mating = petMatingList.get(i);
						long playerAId = mating.getPlayerAId();
						long playerBId = mating.getPlayerBId();
						
						if(!playerManager.isOnline(playerAId) || !playerManager.isOnline(playerBId)){
							petMatingList.remove(mating);
							continue;
						}

						if(now - mating.getCreateTime() > 5*60*1000) {
							petMatingList.remove(mating);
						}
					}
				}
			} catch(Throwable e) {
				PetManager.logger.error("[宠物繁殖心跳]",e);
			}
		}
	}
	
	public void setArticleEntityManager(ArticleEntityManager articleEntityManager) {
		this.articleEntityManager = articleEntityManager;
	}

	public void setArticleManager(ArticleManager articleManager) {
		this.articleManager = articleManager;
	}

	public void setPlayerManager(PlayerManager playerManager) {
		this.playerManager = playerManager;
	}

	public void setPetManager(PetManager petManager) {
		this.petManager = petManager;
	}

	
	/**
	 * 和另一个玩家创建一个繁殖的会话
	 * @param playerA
	 * @param entityA
	 * @param playerB
	 * @param entityB
	 * @return
	 */
	public synchronized PetMating createNewMatingSession(Player playerA, Player playerB){
		PetMating matingA = this.getPlayerMating(playerA);
		PetMating matingB = this.getPlayerMating(playerB);
		if(matingA != null && com.fy.engineserver.gametime.SystemTime.currentTimeMillis() - matingA.getCreateTime() < 会话时间 ) {
			logger.error("[发起繁殖会话失败] [失败：你已经在一个繁殖会话中] [playerA:" + playerA.getLogString() + "] [playerB:" + playerB.getLogString() + "]");
			playerA.sendError(Translate.你已经在一个繁殖会话中);
			return null;
			
		}
		if(matingB != null && com.fy.engineserver.gametime.SystemTime.currentTimeMillis() - matingB.getCreateTime() < 会话时间  ) {
			logger.error("[发起繁殖会话失败] [失败：对方已经在一个繁殖会话中] [playerA:" + playerA.getLogString() + "] [playerB:" + playerB.getLogString() + "]");
			playerA.sendError(Translate.对方已经在一个繁殖会话中);
			return null;
		}
		
		
		
		PetMating mating = new PetMating(playerA, null, playerB, null);
		mating.setId(nextId());
		mating.setCreateTime(com.fy.engineserver.gametime.SystemTime.currentTimeMillis());
		petMatingList.add(mating);
		return mating;
	}
	
	/**
	 * 刷新繁殖会话的状态，如果双方都已经放入繁殖宠物物品，那么创建这个繁殖
	 * @param player
	 * @param entity
	 * @return
	 */
	public synchronized PetMating refreshMatingSession(Player player, PetPropsEntity entity, PetMating mating){
		
		if(mating.getPlayerAId() == player.getId()) {
			mating.setPetEntityAId(entity.getId());
			mating.setPlayerBConfirmed(false);
		} else if(mating.getPlayerBId() == player.getId()) {
			mating.setPetEntityBId(entity.getId());
			mating.setPlayerAConfirmed(false);
		}
		return mating;
	}
	
	/**
	 * 玩家确认(俩个人的繁殖)
	 * @param player
	 * @param matingId
	 */
	public synchronized String confirmMating(Player player, long matingId){
		PetMating mating = this.getPlayerMating(matingId);
		if(mating == null) {
			logger.error("[繁殖确认] [失败：繁殖会话不存在] [" + player.getLogString() + "] [" + matingId + "]");
			return Translate.text_pet_18;
		}
		if(mating.getPlayerAId() == player.getId()) {
			mating.setPlayerAConfirmed(true);
		} else if(mating.getPlayerBId() == player.getId()) {
			mating.setPlayerBConfirmed(true);
		}
		return "";
	}
	
	/**
	 * 取消繁殖
	 * @param player
	 * @param matingId
	 */
	public synchronized boolean cancelMating(Player player, long matingId) {
		PetMating mating = this.getPlayerMating(matingId);
		if(mating == null) {
			return false;
		}
		if(mating.getPlayerAId() == player.getId() || mating.getPlayerBId() == player.getId()) {
			this.petMatingList.remove(mating);
			logger.warn("[删除会话成功] ["+player.getLogString()+"] ["+matingId+"]");
			return true;
		}
		return false;
	}

	/**
	 * 俩个玩家都确认繁殖后
	 * @param playerA
	 * @param petEntityA
	 * @param playerB
	 * @param petEntityB
	 * @return
	 */
	public synchronized String finishPetMating(long matingId){
		
		long start = com.fy.engineserver.gametime.SystemTime.currentTimeMillis();
		PetMating mating = this.getPlayerMating(matingId);
		
		Player playerA = null;
		Player playerB = null;
		try {
			playerA = PlayerManager.getInstance().getPlayer(mating.getPlayerAId());
			playerB = PlayerManager.getInstance().getPlayer(mating.getPlayerBId());
		} catch (Exception e1) {
			PetManager.logger.error("[宠物繁殖]",e1);
			return null;
		}
		
		PetPropsEntity petEntityA = (PetPropsEntity)ArticleEntityManager.getInstance().getEntity(mating.getPetEntityAId());
		PetPropsEntity petEntityB = (PetPropsEntity)ArticleEntityManager.getInstance().getEntity(mating.getPetEntityBId());
		
		if (!ArticleProtectManager.instance.isCanDo(playerA, ArticleProtectDataValues.ArticleProtect_High, petEntityA.getId())){
			return Translate.高级锁魂物品不能做此操作;
		}
		if (!ArticleProtectManager.instance.isCanDo(playerB, ArticleProtectDataValues.ArticleProtect_High, petEntityB.getId())){
			return Translate.锁魂物品不能做此操作;
		}
		
		PetManager pm = PetManager.getInstance();
		Pet petA = pm.getPet(petEntityA.getPetId());
		Pet petB = pm.getPet(petEntityB.getPetId());
		
		for(long pid : playerA.getPetCell()){
			if(pid == petA.getId()){
				playerA.sendError(Translate.助战中的宠物不能封印);
				return null;
			}
		}
		if(PetHouseManager.getInstance().petIsStore(playerA, petA)){
			playerA.sendError(Translate.挂机中的宠物不能封印);
			return null; 
		}
		
		for(long pid : playerB.getPetCell()){
			if(pid == petB.getId()){
				playerB.sendError(Translate.助战中的宠物不能封印);
				return null;
			}
		}
		if(PetHouseManager.getInstance().petIsStore(playerB, petB)){
			playerB.sendError(Translate.挂机中的宠物不能封印);
			return null; 
		}
		
		if(petA.getBreedTimes() <= 0 || petB.getBreedTimes() <= 0) {
			logger.error("[俩人宠物繁殖] [失败:玩家繁殖次数错误]  [playerA:" + playerA.getLogString() + "] [petA:" + petEntityA.getPetId() + "] [petB" + petEntityB.getPetId() + "]");
			return Translate.text_pet_15;
		}
		
		if(petA.getSex() + petB.getSex() != 1) {
			logger.error("[俩人宠物繁殖] [失败:宠物不是一公一母]  [playerA:" + playerA.getLogString() + "] [petA:" + petEntityA.getPetId() + "] [petB" + petEntityB.getPetId() + "]");
			return Translate.text_pet_13;
		}
		
		
		if(!petEntityA.getArticleName().equals(petEntityB.getArticleName())) {
			logger.error("[俩人宠物繁殖] [失败:宠物不是同类宠物]  [playerA:" + playerA.getLogString() + "] [[petA:" + petEntityA.getPetId() + "] [petB" + petEntityB.getPetId() + "]");
			return Translate.text_pet_14;
		}
		
		if(petA == null || petA.getVariation() != 0 || petB == null || petB.getVariation() != 0) {
			logger.error("[俩人宠物繁殖] [失败:宠物不是非变异宠]  [player:" + playerA.getLogString() + "] [petA:" + petEntityA.getPetId() + "] [petB" + petEntityB.getPetId() + "]");
			return Translate.text_pet_12;
		}
		
		if(petA == null || petA.getGeneration() != 0 || petB == null || petB.getGeneration() != 0) {
			logger.error("[俩人宠物繁殖] [失败:宠物不是一代宠]  [player:" + playerA.getLogString() + "] [petA:" + petEntityA.getPetId() + "] [petB" + petEntityB.getPetId() + "]");
			return Translate.宠物不是一代宠不能繁殖;
		}
		
//		if(petA == null || petA.getGeneration() != 0 || petA.getVariation() != 0 || petB == null || petB.getGeneration() != 0 || petB.getVariation() != 0) {
//			logger.error("[俩人宠物繁殖] [失败:宠物不是一代非变异宠]  [playerA:" + playerA.getLogString() + "] [petA:" + petEntityA.getPetId() + "] [petB" + petEntityB.getPetId() + "]");
//			return Translate.text_pet_12;
//		}
		
		if(!petA.isIdentity() || !petB.isIdentity()){
			logger.error("[俩人宠物繁殖] [失败:宠物必须要鉴定]  [playerA:" + playerA.getLogString() + "] [petA:" + petEntityA.getPetId() + "] [petB" + petEntityB.getPetId() + "]");
			return Translate.宠物繁殖必须要鉴定;
		}
		
		if(petA.isSummoned() || petB.isSummoned()) {
			logger.error("[俩人宠物繁殖] [失败:宠物不能在出战状态下]  [playerA:" + playerA.getLogString() + "] [petA:" + petEntityA.getPetId() + "] [petB" + petEntityB.getPetId() + "]");
			return Translate.text_pet_16;
		}

		if(playerA.getKnapsack_common().isFull() || playerB.getKnapsack_common().isFull() ) {
			logger.error("[个人宠物繁殖] [失败:没有剩余空间]  [playerA:" + playerA.getLogString() + "] [petA:" + petEntityA.getPetId() + "] [petB" + petEntityB.getPetId() + "] ");
			return Translate.背包空间不足;
		} 
		
		ArticleEntity aeA = playerA.getArticleEntity(petEntityA.getId());
		ArticleEntity aeB = playerB.getArticleEntity(petEntityB.getId());
		if(aeA == null || aeB == null) {
			logger.error("[个人宠物繁殖] [失败:没有宠物道具物品]  [playerA:" + playerA.getLogString() + "] [petA:" + petEntityA.getPetId() + "] [petB" + petEntityB.getPetId() + "] ");
			return Translate.玩家背包中没有宠物物品;
		}
		
		Pet childA = null;
		Pet childB = null;
		
		Article petArticle = ArticleManager.getInstance().getArticle(aeA.getArticleName());
		if(petArticle instanceof PetProps) {
			try {
//				PetPropsEntity ppe = (PetPropsEntity) ArticleEntityManager.getInstance().createEntity(petArticle, false, ArticleEntityManager.CREATE_REASON_PET_BREED, playerA, 0);
				//其次，创建一个宠物实体
				childA = petManager.createPetByParent(petA, petB);
				if(childA != null) {
					
					String eggArticle = ((PetProps)petArticle).getEggAticleName();
					Article a = ArticleManager.getInstance().getArticle(eggArticle);
					PetEggPropsEntity pep = (PetEggPropsEntity)ArticleEntityManager.getInstance().createEntity(a, false, ArticleEntityManager.CREATE_REASON_PET_MATING, playerA, 0, 1, true);
					
					childA.setName(petArticle.getName());
					pep.setPetId(childA.getId());
					pep.setColorType(childA.getQuality());
					playerA.putToKnapsacks(pep,"宠物繁殖");
					ArticleStatManager.addToArticleStat(playerA, null, pep, ArticleStatManager.OPERATION_物品获得和消耗, 0, (byte)0, 1, "宠物繁殖获得", null);
					petA.setBreedTimes((byte)(petA.getBreedTimes()-1));
					petA.setBreededTimes((byte)(petA.getBreededTimes() + 1));
					
					AchievementManager.getInstance().record(playerA, RecordAction.宠物繁殖次数);
					if(AchievementManager.logger.isWarnEnabled()){
						AchievementManager.logger.warn("[成就统计宠物繁殖次数] ["+playerA.getLogString()+"]");
					}
					
//					if (logger.isWarnEnabled()) {
//						logger.warn("[俩人宠物繁殖成功] [A方:] ["+playerA.getLogString()+"] [道具名:"+petArticle.getName()+"]");
//					}
					if (logger.isWarnEnabled()){
						logger.warn("[俩人宠物繁殖成功] [A方:] [{}] {} [道具名:{}]",new Object[]{playerA.getLogString4Knap(),childA.getLogOfInterest(), petArticle.getName()});
					}
				}
			} catch(Exception e) {
				logger.error("[完成繁殖宠物] [异常] ["+playerA.getId()+"] ["+playerA.getName()+"] ["+playerA.getUsername()+"]", e);
			}
		} else {
//			logger.error("[完成繁殖宠物] [失败：宠物物种有误]  ["+playerA.getId()+"] ["+playerA.getName()+"] ["+playerA.getUsername()+"]", start);
			logger.error("[完成繁殖宠物] [失败：宠物物种有误]  [{}] [{}] [{}] [{}ms]", new Object[]{playerA.getId(),playerA.getName(),playerA.getUsername(),com.fy.engineserver.gametime.SystemTime.currentTimeMillis()-start});
		}
		if(playerB != null) {
			petArticle = ArticleManager.getInstance().getArticle(aeA.getArticleName());
			if(petArticle instanceof PetProps) {
				try {
//					PetPropsEntity ppe = (PetPropsEntity) ArticleEntityManager.getInstance().createEntity(petArticle, false, ArticleEntityManager.CREATE_REASON_PET_BREED, playerA, 0);
					//其次，创建一个宠物实体
//					if(childA != null) {
//						childB = petManager.createPetByClone(childA);
//					}
					childB = petManager.createPetByParent(petA, petB);
					if(childB != null) {
						childB.setName(petArticle.getName());
						String eggArticle = ((PetProps)petArticle).getEggAticleName();
						Article a = ArticleManager.getInstance().getArticle(eggArticle);
						PetEggPropsEntity pep = (PetEggPropsEntity)ArticleEntityManager.getInstance().createEntity(a, false, ArticleEntityManager.CREATE_REASON_PET_MATING, playerB, 0, 1, true);
						
						pep.setPetId(childB.getId());
						pep.setColorType(childB.getQuality());
						playerB.putToKnapsacks(pep,"宠物繁殖");
						ArticleStatManager.addToArticleStat(playerB, null, pep, ArticleStatManager.OPERATION_物品获得和消耗, 0, (byte)0, 1, "宠物繁殖获得", null);
						petB.setBreedTimes((byte)(petB.getBreedTimes()-1));
						petB.setBreededTimes((byte)(petB.getBreededTimes() + 1));
						
						AchievementManager.getInstance().record(playerB, RecordAction.宠物繁殖次数);
						if(AchievementManager.logger.isWarnEnabled()){
							AchievementManager.logger.warn("[成就统计宠物繁殖次数] ["+playerB.getLogString()+"]");
						}
						
//						if (logger.isWarnEnabled()){
//							logger.warn("[俩人宠物繁殖成功] [B方:] ["+playerB.getLogString()+"] [道具名:"+petArticle.getName()+"]");
//						}
						if (logger.isWarnEnabled()){
							logger.warn("[俩人宠物繁殖成功] [B方:] [{}] {} [道具名:{}]",new Object[]{playerB.getLogString4Knap(),childB.getLogOfInterest(), petArticle.getName()});
						}
					}
				} catch(Exception e) {
					logger.error("[完成繁殖宠物] [异常] ["+playerB.getId()+"] ["+playerB.getName()+"] ["+playerB.getUsername()+"]", e);
				}
			} else {
//				logger.error("[完成繁殖宠物] [失败：宠物物种有误] ["+playerB.getId()+"] ["+playerB.getName()+"] ["+playerB.getUsername()+"]", start);
				logger.error("[完成繁殖宠物] [失败：宠物物种有误] [{}] [{}] [{}] [{}ms]", new Object[]{playerB.getId(),playerB.getName(),playerB.getUsername(),com.fy.engineserver.gametime.SystemTime.currentTimeMillis()-start});
			}
		}
		return "";
	}
	
	/**
	 * 玩家自己创建一个繁殖，并且马上完成
	 * @param player
	 * @param petEntityIdA
	 * @param petEntityIdB
	 * @return
	 */
	public synchronized String createAndFinishMating(Player player, PetPropsEntity petEntityA, PetPropsEntity petEntityB){
		long start = com.fy.engineserver.gametime.SystemTime.currentTimeMillis();
		PetManager pm = PetManager.getInstance();
		Pet petA = pm.getPet(petEntityA.getPetId());
		Pet petB = pm.getPet(petEntityB.getPetId());
		if (!ArticleProtectManager.instance.isCanDo(player, ArticleProtectDataValues.ArticleProtect_High, petEntityA.getId())){
			return Translate.高级锁魂物品不能做此操作;
		}
		if (!ArticleProtectManager.instance.isCanDo(player, ArticleProtectDataValues.ArticleProtect_High, petEntityB.getId())){
			return Translate.锁魂物品不能做此操作;
		}
		
		if(petA.getBreedTimes() <= 0 || petB.getBreedTimes() <= 0) {
			logger.error("[个人宠物繁殖] [失败:玩家繁殖次数错误]  [player:" + player.getLogString() + "] [petA:" + petEntityA.getPetId() + "] [petB" + petEntityB.getPetId() + "]");
			return Translate.text_pet_15;
		}
		
		
		if(petA.getSex() + petB.getSex() != 1) {
			logger.error("[个人宠物繁殖] [失败:宠物不是一公一母]  [player:" + player.getLogString() + "] [petA:" + petEntityA.getPetId() + "] [petB" + petEntityB.getPetId() + "]");
			return Translate.text_pet_13;
		}
		
		if(!petEntityA.getArticleName().equals(petEntityB.getArticleName())) {
			logger.error("[个人宠物繁殖] [失败:宠物不是同类宠物]  [player:" + player.getLogString() + "] [[petA:" + petEntityA.getPetId() + "] [petB" + petEntityB.getPetId() + "]");
			return Translate.text_pet_14;
		}
		
		if(petA == null || petA.getVariation() != 0 || petB == null || petB.getVariation() != 0) {
			logger.error("[个人宠物繁殖] [失败:宠物不是非变异宠]  [player:" + player.getLogString() + "] [petA:" + petEntityA.getPetId() + "] [petB" + petEntityB.getPetId() + "]");
			return Translate.text_pet_12;
		}
		
		if(petA == null || petA.getGeneration() != 0 || petB == null || petB.getGeneration() != 0) {
			logger.error("[个人宠物繁殖] [失败:宠物不是一代宠]  [player:" + player.getLogString() + "] [petA:" + petEntityA.getPetId() + "] [petB" + petEntityB.getPetId() + "]");
			return Translate.宠物不是一代宠不能繁殖;
		}

		if(petA.isSummoned() || petB.isSummoned()) {
			logger.error("[个人宠物繁殖] [失败:宠物不能在出战状态下]  [player:" + player.getLogString() + "] [petA:" + petEntityA.getPetId() + "] [petB" + petEntityB.getPetId() + "]");
			return Translate.text_pet_16;
		}
		
		if(!petA.isIdentity() || !petB.isIdentity()){
			logger.error("[个人宠物繁殖] [失败:宠物必须要鉴定]  [player:" + player.getLogString() + "] [petA:" + petEntityA.getPetId() + "] [petB" + petEntityB.getPetId() + "]");
			return Translate.宠物繁殖必须要鉴定;
		}
		if(player.getKnapsack_common().isFull()) {
			logger.error("[个人宠物繁殖] [失败:没有剩余空间]  [player:" + player.getLogString() + "] [petA:" + petEntityA.getPetId() + "] [petB" + petEntityB.getPetId() + "] ");
			return Translate.背包空间不足;
		} 
		
		if(petA.getHookInfo() != null || petB.getHookInfo() != null){
			return Translate.所选宠物有正在宠物房挂机;
		}
		
		
		for(long pid : player.getPetCell()){
			if(pid == petA.getId()){
				player.sendError(Translate.助战中的宠物不能封印);
				return null;
			}
		}
		if(PetHouseManager.getInstance().petIsStore(player, petA)){
			player.sendError(Translate.挂机中的宠物不能封印);
			return null; 
		}
		
		for(long pid : player.getPetCell()){
			if(pid == petB.getId()){
				player.sendError(Translate.助战中的宠物不能封印);
				return null;
			}
		}
		if(PetHouseManager.getInstance().petIsStore(player, petB)){
			player.sendError(Translate.挂机中的宠物不能封印);
			return null; 
		}
		
		ArticleEntity aeA = player.getArticleEntity(petEntityA.getId());
		ArticleEntity aeB = player.getArticleEntity(petEntityB.getId());
		if(aeA == null || aeB == null) {
			logger.error("[个人宠物繁殖] [失败:没有宠物道具物品]  [player:" + player.getLogString() + "] [petA:" + petEntityA.getPetId() + "] [petB" + petEntityB.getPetId() + "] ");
			return Translate.玩家背包中没有宠物物品;
		}
		
		Pet childA = null;
		Article petArticle = ArticleManager.getInstance().getArticle(aeA.getArticleName());
		if(petArticle instanceof PetProps) {
			try {
//				PetPropsEntity ppe = (PetPropsEntity) ArticleEntityManager.getInstance().createEntity(petArticle, false, ArticleEntityManager.CREATE_REASON_PET_BREED, player, 0);
				String eggArticle = ((PetProps)petArticle).getEggAticleName();
				Article a = ArticleManager.getInstance().getArticle(eggArticle);
				PetEggPropsEntity pep = (PetEggPropsEntity)ArticleEntityManager.getInstance().createEntity(a, false, ArticleEntityManager.CREATE_REASON_PET_MATING, player, 0, 1, true);
				//其次，创建一个宠物实体
				childA = petManager.createPetByParent(petA, petB);
				
				if(childA != null) {
//					ppe.setPetId(childA.getId());
//					ppe.setPetColorType(childA.getQuality());
//					childA.setPetPropsId(ppe.getId());
					childA.setName(petArticle.getName());
					pep.setPetId(childA.getId());
					player.putToKnapsacks(pep,"宠物繁殖");
					ArticleStatManager.addToArticleStat(player, null, pep, ArticleStatManager.OPERATION_物品获得和消耗, 0, (byte)0, 1, "宠物繁殖获得", null);
					petA.setBreedTimes((byte)(petA.getBreedTimes()-1));
					petA.setBreededTimes((byte)(petA.getBreededTimes() + 1));
					petB.setBreedTimes((byte)(petB.getBreedTimes()-1));
					petB.setBreededTimes((byte)(petB.getBreededTimes() + 1));
					
					AchievementManager.getInstance().record(player, RecordAction.宠物繁殖次数);
					if(AchievementManager.logger.isWarnEnabled()){
						AchievementManager.logger.warn("[成就统计宠物繁殖次数] ["+player.getLogString()+"]");
					}
					
//					logger.info("[玩家独自繁殖宠物] [成功] ["+player.getLogString()+"] [parentArticle:"+petEntityA.getId()+"/"+petEntityB.getId()+"] [childArticle:"+pep.getId()+"]");
//					if(logger.isWarnEnabled()){
//						logger.warn("[玩家独自繁殖宠物] [成功] [{}] [parentArticle:{}{}/{}{}] [childArticle:{}]", new Object[]{player.getLogString(),petEntityA.getArticleName(), petEntityA.getId(),petEntityB.getArticleName(), petEntityB.getId(),childA.getName(), pep.getId()});
						if(logger.isWarnEnabled()){
							logger.warn("[玩家独自繁殖宠物] [成功] [{}] {} [parentArticle:{}{}/{}{}] [childArticle:{}]", new Object[]{player.getLogString(),childA.getLogOfInterest(), petEntityA.getArticleName(), petEntityA.getId(),petEntityB.getArticleName(), petEntityB.getId(),childA.getName(), pep.getId()});
					}
					return "";
				}
			} catch(Exception e) {
				logger.error("[完成繁殖宠物] [异常] ["+player.getLogString()+"]", e);
			}
		} else {
//			logger.error("[完成繁殖宠物] [失败：宠物物种有误]  ["+player.getLogString()+"]", start);
			logger.error("[完成繁殖宠物] [失败：宠物物种有误]  [{}] [{}ms]", new Object[]{player.getLogString(),com.fy.engineserver.gametime.SystemTime.currentTimeMillis()-start});
		}
		return null;
	}
	
	/**
	 * 得到玩家目前的繁殖会话
	 * @param player
	 * @return
	 */
	public PetMating getPlayerMating(Player player) {
		for(PetMating mating : petMatingList) {
			if(mating.getPlayerAId() == player.getId() || mating.getPlayerBId() == player.getId()) {
				return mating;
			}
		}
		return null;
	}
	
	public PetMating getPlayerMating(long matingId) {
		for(PetMating mating : petMatingList) {
			if(mating.getId() == matingId) {
				return mating;
			}
		}
		return null;
	} 

	public List<PetMating> getMatingList() {
		return petMatingList;
	}
	
}
