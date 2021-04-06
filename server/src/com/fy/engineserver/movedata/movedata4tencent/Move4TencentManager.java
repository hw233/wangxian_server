package com.fy.engineserver.movedata.movedata4tencent;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.slf4j.Logger;

import com.fy.engineserver.achievement.AchievementManager;
import com.fy.engineserver.achievement.GameDataRecord;
import com.fy.engineserver.activity.TransitRobbery.TransitRobberyEntity;
import com.fy.engineserver.activity.TransitRobbery.TransitRobberyEntityManager;
import com.fy.engineserver.activity.TransitRobbery.TransitRobberyManager;
import com.fy.engineserver.activity.activeness.ActivenessManager;
import com.fy.engineserver.activity.activeness.PlayerActivenessInfo;
import com.fy.engineserver.datasource.article.concrete.DefaultArticleEntityManager;
import com.fy.engineserver.datasource.article.data.entity.ArticleEntity;
import com.fy.engineserver.datasource.article.data.entity.EquipmentEntity;
import com.fy.engineserver.datasource.article.data.entity.PetEggPropsEntity;
import com.fy.engineserver.datasource.article.data.entity.PetPropsEntity;
import com.fy.engineserver.datasource.article.data.props.Knapsack;
import com.fy.engineserver.datasource.skill.master.SkBean;
import com.fy.engineserver.datasource.skill.master.SkEnhanceManager;
import com.fy.engineserver.homestead.cave.Cave;
import com.fy.engineserver.homestead.faery.service.FaeryManager;
import com.fy.engineserver.hotspot.HotspotInfo;
import com.fy.engineserver.hotspot.HotspotManager;
import com.fy.engineserver.movedata.DataMoveManager;
import com.fy.engineserver.newtask.DeliverTask;
import com.fy.engineserver.newtask.NewDeliverTask;
import com.fy.engineserver.newtask.NewDeliverTaskManager;
import com.fy.engineserver.newtask.TaskEntity;
import com.fy.engineserver.newtask.TaskEntityManager;
import com.fy.engineserver.newtask.service.DeliverTaskManager;
import com.fy.engineserver.playerAims.instance.PlayerAimsEntity;
import com.fy.engineserver.playerAims.manager.PlayerAimeEntityManager;
import com.fy.engineserver.qiancengta.QianCengTaManager;
import com.fy.engineserver.qiancengta.QianCengTa_Ceng;
import com.fy.engineserver.qiancengta.QianCengTa_Dao;
import com.fy.engineserver.qiancengta.QianCengTa_Ta;
import com.fy.engineserver.sprite.Player;
import com.fy.engineserver.sprite.Soul;
import com.fy.engineserver.sprite.concrete.GamePlayerManager;
import com.fy.engineserver.sprite.horse.Horse;
import com.fy.engineserver.sprite.horse.HorseManager;
import com.fy.engineserver.sprite.horse2.instance.Horse2RelevantEntity;
import com.fy.engineserver.sprite.horse2.manager.Horse2EntityManager;
import com.fy.engineserver.sprite.pet.Pet;
import com.fy.engineserver.sprite.pet.PetManager;
import com.fy.engineserver.sprite.pet2.Pet2Manager;
import com.fy.engineserver.sprite.pet2.PetsOfPlayer;
import com.xuanzhi.tools.mail.JavaMailUtils;
import com.xuanzhi.tools.simplejpa.SimpleEntityManager;
import com.xuanzhi.tools.simplejpa.annotation.SimpleVersion;
import com.xuanzhi.tools.simplejpa.impl.DefaultSimpleEntityManagerFactory;
import com.xuanzhi.tools.text.DateUtil;

public class Move4TencentManager {
	public static Logger logger = TransitRobberyManager.logger;
	/**  有用的需要转移的playerid */
	private List<Long> playerIds = new ArrayList<Long>();
	
	public static Move4TencentManager inst;
	/** 需要转移的数据库配置(将此库中部分数据转移到本服中) */
	private DefaultSimpleEntityManagerFactory factory;
	
	public static final long MONTH = 30L * 24 * 60 * 60 * 1000;
	public static long now = System.currentTimeMillis();
	
	public static String sql = "RMB > 0 or quitGameTime >" + (now - 6 * MONTH) + " or level > 110";
	
	public List<Move4TencentThread> threads = new ArrayList<Move4TencentThread>();
	
	/**
	 * 由于腾讯创建em速度非常慢  全部提取出来
	 */
	public SimpleEntityManager<DeliverTask> delvPem;
	public SimpleEntityManager<Player> newPem;
	public SimpleEntityManager<ArticleEntity> aeEm;
	public SimpleEntityManager<TaskEntity> teEm;
	public SimpleEntityManager<NewDeliverTask> ndtEm;
	public SimpleEntityManager<GameDataRecord> gdrEm;
	public SimpleEntityManager<Horse> hrEm;
	public SimpleEntityManager<Pet> pEm;
	public SimpleEntityManager<Horse2RelevantEntity> hr2Em;
	public SimpleEntityManager<HotspotInfo> hsiEm;
	public SimpleEntityManager<PetsOfPlayer> popEm;
	public SimpleEntityManager<PlayerActivenessInfo> paiEm;
	public SimpleEntityManager<SkBean> skEm;
	public SimpleEntityManager<TransitRobberyEntity> tranEm;
	public SimpleEntityManager<PlayerAimsEntity> paeEm;
	public SimpleEntityManager<Cave> caveEm;
	public SimpleEntityManager<QianCengTa_Ta> taEm;
	
	public static String tempFileName = "";
	
	public static String 服务器名 = "战无不胜";
	
	public List<Long> temp = new ArrayList<Long>();
	/** 宠物id替换 */
	public Map<Long, Long> petMap = new HashMap<Long, Long>();
	
	public void init(String emPath, String serverName) throws Exception {
		inst = this;
		服务器名 = serverName;
		initAllSimpleEMF(emPath);
		collectIds();
	}
	
	public void notifyThreadFinish(Move4TencentThread thread) {
		try {
			boolean allFinish = true;
			for (Move4TencentThread mtt : threads) {
				if (!mtt.finish) {
					allFinish = false;
					break;
				}
			}
			if (allFinish) {
				sendMail(tempFileName + "-转移数据结束",  tempFileName + "-转移数据结束");
			}
			if (logger.isInfoEnabled()) {
				logger.info("[腾讯导数据] [" + thread.threadName + "] [执行结束] [转移player数量:" + thread.getPlayerIds().size() + "] [耗时:" + (thread.endTime - thread.startTime) + "ms");
			}
		} catch (Exception e) {
			logger.warn("[腾讯导数据] [线程执行结束检测是否全部完毕] [异常] ", e);
		}
	}
	
	public static String[] mailAddress = new String[]{"wtx062@126.com"};
	
	public static void sendMail(String title, String content) {
		StringBuffer sb = new StringBuffer();
		sb.append(content);
		sb.append("<br>" + DateUtil.formatDate(new Date(), "yyyy-MM-dd HH:mm:ss"));
		ArrayList<String> args = new ArrayList<String>();
		args.add("-username");
		args.add("sqage_wx_restart");
		args.add("-password");
		args.add("2wsxcde3");

		args.add("-smtp");
		args.add("smtp.163.com");
		args.add("-from");
		args.add("wtx062@126.com");
		args.add("-to");
		// TODO mailAddress 修改邮件
		String address_to = "";

		for (String address : mailAddress) {
			address_to += address + ",";
		}

		if (!"".equals(address_to)) {
			args.add(address_to);
			args.add("-subject");
			args.add(title);
			args.add("-message");
			args.add(sb.toString());
			args.add("-contenttype");
			args.add("text/html;charset=utf-8");
			try {
				JavaMailUtils.sendMail(args.toArray(new String[0]));
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
	}
	
	public void initAllSimpleEMF(String emPath) {
		factory = new DefaultSimpleEntityManagerFactory(emPath);
		newPem = factory.getSimpleEntityManager(Player.class);
		aeEm = factory.getSimpleEntityManager(ArticleEntity.class);
		teEm = factory.getSimpleEntityManager(TaskEntity.class);
		ndtEm = factory.getSimpleEntityManager(NewDeliverTask.class);
		gdrEm = factory.getSimpleEntityManager(GameDataRecord.class);
		hrEm = factory.getSimpleEntityManager(Horse.class);
		pEm = factory.getSimpleEntityManager(Pet.class);
		hr2Em = factory.getSimpleEntityManager(Horse2RelevantEntity.class);
		hsiEm = factory.getSimpleEntityManager(HotspotInfo.class);
		popEm = factory.getSimpleEntityManager(PetsOfPlayer.class);
		paiEm = factory.getSimpleEntityManager(PlayerActivenessInfo.class);
		skEm = factory.getSimpleEntityManager(SkBean.class);
		tranEm = factory.getSimpleEntityManager(TransitRobberyEntity.class);
		paeEm = factory.getSimpleEntityManager(PlayerAimsEntity.class);
		caveEm = factory.getSimpleEntityManager(Cave.class);
		taEm = factory.getSimpleEntityManager(QianCengTa_Ta.class);
		delvPem = factory.getSimpleEntityManager(DeliverTask.class);
		petField = DataMoveManager.getFieldByAnnotation(Pet.class, SimpleVersion.class);
		
		newPem.setReadOnly(true);
		aeEm.setReadOnly(true);
		teEm.setReadOnly(true);
		ndtEm.setReadOnly(true);
		gdrEm.setReadOnly(true);
		hrEm.setReadOnly(true);
		pEm.setReadOnly(true);
		hr2Em.setReadOnly(true);
		hsiEm.setReadOnly(true);
		popEm.setReadOnly(true);
		paiEm.setReadOnly(true);
		skEm.setReadOnly(true);
		tranEm.setReadOnly(true);
		paeEm.setReadOnly(true);
		caveEm.setReadOnly(true);
		taEm.setReadOnly(true);
		delvPem.setReadOnly(true);
		delvPem.setReadOnly(true);
		
		petField.setAccessible(true);
	}
	
	
	
	/**
	 * 收集有用的playerId
	 * @return
	 * @throws Exception 
	 */
	public List<Long> collectIds() throws Exception {
		long startTime = System.currentTimeMillis();
		long count = newPem.count(Player.class, sql);
		long[] tempId = newPem.queryIds(Player.class, sql, "", 1, count + 1);
		for (long id : tempId) {
			if (!playerIds.contains(id) && !temp.contains(id)) {
				playerIds.add(id);
			}
		}
		long endTime = System.currentTimeMillis();
		logger.warn("[腾讯转移数据] [收集有用id] [收集总数:" + count + "] [实际数:" + playerIds.size() + "] [耗时：" + (endTime - startTime) + "ms]");
		return playerIds;
	}
	
	
	public void move(long playerId) {
		try {
			long startTime = System.currentTimeMillis();
			Player player = newPem.find(playerId);
			if (player == null) {
				logger.warn("[腾讯数据] [没有找到角色] [" + playerId + "]");
				return ;
			}
			List<Long> articleIds = new ArrayList<Long>();
			Set<Long> temp = collectPlayerArticle(player);
			long newPId = ((GamePlayerManager)GamePlayerManager.getInstance()).em.nextId();
			if (temp != null && temp.size() > 0) {
				articleIds.addAll(temp);
			}
			Field versionField = DataMoveManager.getFieldByAnnotation(ArticleEntity.class, SimpleVersion.class);
			if (versionField == null) {
				throw new IllegalStateException("ArticleEntity's versionField not found");
			}
			versionField.setAccessible(true);
			if (articleIds.size() > 0) {
				try {
					Map<Long, Long> replaceIds = new HashMap<Long, Long>();			//物品id替换
					String sq = "";
					for (int i = 0; i < articleIds.size(); i++) {
						if (i == articleIds.size() - 1) {
							sq += "id=" + articleIds.get(i);
						} else {
							sq += "id=" + articleIds.get(i) + " or ";
						}
					}
					List<ArticleEntity> entitys = aeEm.query(ArticleEntity.class, sq, "", 1, articleIds.size() + 1);
					if (logger.isInfoEnabled()) {
						logger.info("[收集玩家物品] [" + player.getLogString() + "] [articleIds:" + articleIds.size() + "]");
					}
					if (logger.isDebugEnabled()) {
						logger.debug("[测试sql] [" + player.getLogString() + "] [" + sq + "]");
					}
					for (ArticleEntity ae : entitys) {				//所有查出来的物品
						replaceEquiptOtherIds(ae, aeEm, versionField);
						long oldId = ae.getId();
						long id = DefaultArticleEntityManager.getInstance().em.nextId();
						ae.setId(id);
						versionField.set(ae, 0);
						replactPetEntity(ae, newPId);
						DefaultArticleEntityManager.getInstance().em.flush(ae);
						replaceIds.put(oldId, id);
					}
					replacePlayerArticle(player, replaceIds, versionField);
				} catch (Exception e) {
					logger.warn("[腾讯转移数据] [异常] [" + player.getLogString() + "]", e);
				}
			}
			long articleTime = System.currentTimeMillis();
			try {
				replaceAndSaveTaskEntity(player, newPId);
			} catch (Exception e) {
				logger.warn("[腾讯转移数据] [异常] [" + player.getLogString() + "]", e);
			}
			long taskEntityTime = System.currentTimeMillis();
			try {
				replaceAndSaveNewTask(player, newPId);
			} catch (Exception e) {
				logger.warn("[腾讯转移数据] [异常] [" + player.getLogString() + "]", e);
			}
			try {
				replaceAndDeliverTask(player, newPId);
			} catch (Exception e) {
				logger.warn("[腾讯转移数据] [异常] [" + player.getLogString() + "]", e);
			}
			long newTaskEntityTime = System.currentTimeMillis();
			try {
				replaceAndSaveHorse(player, newPId, versionField);
			} catch (Exception e) {
				logger.warn("[腾讯转移数据] [异常] [" + player.getLogString() + "]", e);
			}
			try {
				replactQianCengTa(player, newPId, versionField);
			} catch (Exception e) {
				
			}
			long horseTaskEntityTime = System.currentTimeMillis();
			try {
				replaceAndSavePet(player, newPId);
			} catch (Exception e) {
				logger.warn("[腾讯转移数据] [异常] [" + player.getLogString() + "]", e);
			}
			long petTaskEntityTime = System.currentTimeMillis();
			try {
				replaceAndSaveHorse2(player, newPId);
			} catch (Exception e) {
				logger.warn("[腾讯转移数据] [异常] [" + player.getLogString() + "]", e);
			}
			long horse2TaskEntityTime = System.currentTimeMillis();
			try {
				replaceAndSaveGdr(player, newPId);
			} catch (Exception e) {
				logger.warn("[腾讯转移数据] [异常] [" + player.getLogString() + "]", e);
			}
			long gdrTaskEntityTime = System.currentTimeMillis();
			try {
				replaceAndSaveHsi(player, newPId);
			} catch (Exception e) {
				logger.warn("[腾讯转移数据] [异常] [" + player.getLogString() + "]", e);
			}
			long hsiTaskEntityTime = System.currentTimeMillis();
			try {
				replaceAndSavePop(player, newPId);
			} catch (Exception e) {
				logger.warn("[腾讯转移数据] [异常] [" + player.getLogString() + "]", e);
			}
			long popTaskEntityTime = System.currentTimeMillis();
			try {
				replaceAndSavePai(player, newPId);
			} catch (Exception e) {
				logger.warn("[腾讯转移数据] [异常] [" + player.getLogString() + "]", e);
			}
			long paiTaskEntityTime = System.currentTimeMillis();
			try {
				replaceAndSaveSk(player, newPId);
			} catch (Exception e) {
				logger.warn("[腾讯转移数据] [异常] [" + player.getLogString() + "]", e);
			}
			long skTaskEntityTime = System.currentTimeMillis();
			try {
				replaceAndSaveTrans(player, newPId);
			} catch (Exception e) {
				logger.warn("[腾讯转移数据] [异常] [" + player.getLogString() + "]", e);
			}
			long tranTaskEntityTime = System.currentTimeMillis();
			try {
				replaceAndSavePae(player, newPId);
			} catch (Exception e) {
				logger.warn("[腾讯转移数据] [异常] [" + player.getLogString() + "]", e);
			}
			long paeTaskEntityTime = System.currentTimeMillis();
			try {
				replaceAndSaveCave(player, newPId);
			} catch (Exception e) {
				logger.warn("[腾讯转移数据] [异常] [" + player.getLogString() + "]", e);
			}
			long caveTaskEntityTime = System.currentTimeMillis();
			
			
			long oldPId = player.getId();
			player.setId(newPId);
			player.setJiazuId(-1);
			player.setJiazuName(null);
			player.setFaeryId(-1);				//设置player的version
			List<Player> pList = ((GamePlayerManager)GamePlayerManager.getInstance()).em.query(Player.class, "name=?", new Object[] { player.getName() }, "", 1, 10);
			if(pList != null && pList.size() > 0)
			{
				player.setName(player.getName()+"@" + 服务器名);
			}
			Field playerVersion = DataMoveManager.getFieldByAnnotation(Player.class, SimpleVersion.class);
			playerVersion.setAccessible(true);
			playerVersion.set(player, 0);
			((GamePlayerManager)GamePlayerManager.getInstance()).em.flush(player);
			long endTime = System.currentTimeMillis();
			if (logger.isInfoEnabled()) {
				logger.info("[腾讯转移数据] [成功] [" + player.getLogString() + "] [oldPId:" + oldPId + "] [总耗时:" + (endTime-startTime) + "]");
			}
		} catch (Exception e) {
			// TODO Auto-generated catch block
			logger.warn("[腾讯转移数据] [异常] [playerId:" + playerId + "]", e);
		}
	}
	/** taskEntity
	 * @throws Exception */
	private void replaceAndSaveTaskEntity(Player player, long newPid) throws Exception {
		String where = "playerId = ? and (showType >=1 and showType <= 3  or status < 3)";
		List<TaskEntity> taskEntitys = teEm.query(TaskEntity.class, where,new Object[] {player.getId()}, "id desc", 1, 9999);
		Field versionField = DataMoveManager.getFieldByAnnotation(TaskEntity.class, SimpleVersion.class);
		versionField.setAccessible(true);
		for (TaskEntity te : taskEntitys) {
			versionField.set(te, 0);
			long newId = TaskEntityManager.em.nextId();
			te.setPlayerId(newPid);
			te.setId(newId);
			TaskEntityManager.em.flush(te);
		}
	}
	/** NewDeliverTask
	 * @throws Exception */
	private void replaceAndSaveNewTask(Player player, long newPid) throws Exception {
		List<NewDeliverTask> list = ndtEm.query(NewDeliverTask.class, "playerId=?", new Object[]{player.getId()},"", 1, 9999);
		Field versionField = DataMoveManager.getFieldByAnnotation(NewDeliverTask.class, SimpleVersion.class);
		versionField.setAccessible(true);
		for (NewDeliverTask ndt : list) {
			versionField.set(ndt, 0);
			ndt.setPlayerId(newPid);
			long newId = NewDeliverTaskManager.getInstance().em.nextId();
			ndt.setId(newId);
			NewDeliverTaskManager.getInstance().em.flush(ndt);
		}
	}
	private void replaceAndDeliverTask(Player player, long newPid) throws Exception {
		List<DeliverTask> list = delvPem.query(DeliverTask.class, "playerId = ?", new Object[]{player.getId()},"", 1, 2000);
//		List<NewDeliverTask> list = ndtEm.query(NewDeliverTask.class, "playerId=?", new Object[]{player.getId()},"", 1, 9999);
		Field versionField = DataMoveManager.getFieldByAnnotation(DeliverTask.class, SimpleVersion.class);
		versionField.setAccessible(true);
		for (DeliverTask ndt : list) {
			versionField.set(ndt, 0);
			ndt.setPlayerId(newPid);
			long newId = DeliverTaskManager.getInstance().em.nextId();
			ndt.setId(newId);
			DeliverTaskManager.getInstance().em.flush(ndt);
		}
	}
	
	/** Horse */
	private void replaceAndSaveHorse(Player player, long newPid, Field aeField)  throws Exception{ 
		Field versionField = DataMoveManager.getFieldByAnnotation(Horse.class, SimpleVersion.class);
		versionField.setAccessible(true);
		for (Soul soul : player.getSouls()) {
			if (soul == null) {
				continue;
			}
			ArrayList<Long> list = soul.getHorseArr();
			ArrayList<Long> resultList = new ArrayList<Long>();
			if (list != null && list.size() > 0) {
				for (long id : list) {
					Horse horse = hrEm.find(id);
					if (horse == null) {
						continue;
					}
					horse.setOwnerId(newPid);
					versionField.set(horse, 0);
					long newId = HorseManager.em.nextId();
					horse.setHorseId(newId);
					if (!horse.isFly()) {
						long[] horseEquIDs = horse.getEquipmentColumn().getEquipmentIds();
						for (int k=0; k<horseEquIDs.length; k++) {
							long horseAeId = horseEquIDs[k];
							if (horseAeId > 0) {
								ArticleEntity ae = aeEm.find(horseAeId);
								if (ae != null) {
									long newAeId = DefaultArticleEntityManager.getInstance().em.nextId();
									ae.setId(newAeId);
									aeField.set(ae, 0);
									horseEquIDs[k] = newAeId;
									DefaultArticleEntityManager.getInstance().em.flush(ae);
								}
							}
						}
					}
					resultList.add(newId);
					HorseManager.em.flush(horse);
				}
				soul.setHorseArr(resultList);
			}
		}
	}
	/** Pet */									//需要修改。根据玩家背包及物品中的petProp来导入宠物
	private void replaceAndSavePet(Player player, long newPid)  throws Exception{
		/*List<Pet> list = new ArrayList<Pet>();
		Field versionField = DataMoveManager.getFieldByAnnotation(Pet.class, SimpleVersion.class);
		versionField.setAccessible(true);
		long count = pEm.count(Pet.class, "ownerId=" + player.getId() + " and delete = 'F' or (ownerId = "+ player.getId() +" and delete='T' and rarity >= 2)");
		list.addAll(pEm.query(Pet.class, "ownerId=" + player.getId() + " and delete = 'F' or (ownerId = "+ player.getId() +" and delete='T' and rarity >= 2)", "", 1, count + 1));
		for(Pet pet : list)
		{
			versionField.set(pet, 0);
			long oldId = pet.getId();
			pet.setOwnerId(newPid);
			long newId = PetManager.em.nextId();
			pet.setId(newId);
			PetManager.em.flush(pet);
			petMap.put(oldId, newId);
		}*/
	}
	/** Horse2RelevantEntity */
	private void replaceAndSaveHorse2(Player player, long newPid)  throws Exception{
		Horse2RelevantEntity hr = hr2Em.find(player.getId());
		if (hr == null) {
			return ;
		}
		Field versionField = DataMoveManager.getFieldByAnnotation(Horse2RelevantEntity.class, SimpleVersion.class);
		versionField.setAccessible(true);
		versionField.set(hr, 0);
		hr.setId(newPid);
		Horse2EntityManager.em.flush(hr);
	}
	/** GameDataRecord */
	private void replaceAndSaveGdr(Player player, long newPid)  throws Exception{
		List<GameDataRecord> list = gdrEm.query(GameDataRecord.class, "playerId=" + player.getId(), "", 1, 1000);
		Field versionField = DataMoveManager.getFieldByAnnotation(GameDataRecord.class, SimpleVersion.class);
		versionField.setAccessible(true);
		for (GameDataRecord gdr : list) {
			versionField.set(gdr, 0);
			gdr.setPlayerId(newPid);
			long newId = AchievementManager.gameDREm.nextId();
			gdr.setId(newId);
			AchievementManager.gameDREm.flush(gdr);
		}
	}
	/** HotspotInfo */
	private void replaceAndSaveHsi(Player player, long newPid)  throws Exception{
		List<HotspotInfo> list = hsiEm.query(HotspotInfo.class, "playerID=" + player.getId(), "", 1, 2000);
		Field versionField = DataMoveManager.getFieldByAnnotation(HotspotInfo.class, SimpleVersion.class);
		versionField.setAccessible(true);
		for (HotspotInfo hsi : list) {
			versionField.set(hsi, 0);
			hsi.setPlayerID(newPid);
			long newId = HotspotManager.getInstance().em.nextId();
			hsi.setId(newId);
			HotspotManager.getInstance().em.flush(hsi);
		}
	}
	/** PetsOfPlayer */
	private void replaceAndSavePop(Player player, long newPid)  throws Exception{
		PetsOfPlayer petsOfPlayer = popEm.find(player.getId());
		if (petsOfPlayer == null) {
			return ;
		}
		Field versionField = DataMoveManager.getFieldByAnnotation(PetsOfPlayer.class, SimpleVersion.class);
		versionField.setAccessible(true);
		versionField.set(petsOfPlayer, 0);
		petsOfPlayer.setPid(newPid);
		Pet2Manager.getInst().petsOfPlayerBeanEm.flush(petsOfPlayer);
	}
	/** PlayerActivenessInfo */
	private void replaceAndSavePai(Player player, long newPid)  throws Exception{
		PlayerActivenessInfo playerActivenessInfo  = paiEm.find(player.getId());
		if (playerActivenessInfo == null) {
			return ;
		}
		Field versionField = DataMoveManager.getFieldByAnnotation(PlayerActivenessInfo.class, SimpleVersion.class);
		versionField.setAccessible(true);
		versionField.set(playerActivenessInfo, 0);
		playerActivenessInfo.setId(newPid);
		ActivenessManager.em.flush(playerActivenessInfo);
	}
	/** SkBean */
	private void replaceAndSaveSk(Player player, long newPid)  throws Exception{
		SkBean bean = skEm.find(player.getId());
		if (bean != null) {
			Field versionField = DataMoveManager.getFieldByAnnotation(SkBean.class, SimpleVersion.class);
			versionField.setAccessible(true);
			versionField.set(bean, 0);
			bean.setPid(newPid);
			SkEnhanceManager.em.flush(bean);
		}
	}
	/** TransitRobberyEntity */
	private void replaceAndSaveTrans(Player player, long newPid)  throws Exception{
		TransitRobberyEntity entity = tranEm.find(player.getId());
		if (entity != null) {
			Field versionField = DataMoveManager.getFieldByAnnotation(TransitRobberyEntity.class, SimpleVersion.class);
			versionField.setAccessible(true);
			versionField.set(entity, 0);
			entity.setId(newPid);
			TransitRobberyEntityManager.em.flush(entity);
		}
	}
	/** BuffSave *//*			//喝酒buff存储  不管了
	private void replaceAndSaveBs(Player player, long newPid)  throws Exception{
		
	}*/
	/** PlayerAimsEntity */
	private void replaceAndSavePae(Player player, long newPid)  throws Exception{
		PlayerAimsEntity playerAimsEntity = paeEm.find(player.getId());
		if (playerAimsEntity != null) {
			Field versionField = DataMoveManager.getFieldByAnnotation(PlayerAimsEntity.class, SimpleVersion.class);
			versionField.setAccessible(true);
			versionField.set(playerAimsEntity, 0);
			playerAimsEntity.setId(newPid);
			PlayerAimeEntityManager.em.flush(playerAimsEntity);
		}
	}
	
	private void replactQianCengTa(Player player, long newPid, Field aeVersion) {
		try {
			Field versionField = DataMoveManager.getFieldByAnnotation(QianCengTa_Ta.class, SimpleVersion.class);
			versionField.setAccessible(true);
			QianCengTa_Ta ta = taEm.find(player.getId());
			if (ta != null) {
				ta.setPlayerId(newPid);
				versionField.set(ta, 0);
				if (ta != null) {
					for (int i = 0; i < ta.getDaoList().size(); i++) {
						QianCengTa_Dao dao = ta.getDaoList().get(i);
						for (int j = 0; j < dao.getCengList().size(); j++) {
							QianCengTa_Ceng ceng = dao.getCengList().get(j);
							for (int k = 0; k < ceng.getRewards().size(); k++) {
								if (ceng.getRewards().get(k).getEntityId() > 0) {
									long oldId = ceng.getRewards().get(k).getEntityId();
									if (oldId > 0) {
										ArticleEntity ae = aeEm.find(oldId);
										if (ae != null) {
											aeVersion.set(ae, 0);
											long newAeId = DefaultArticleEntityManager.getInstance().em.nextId();
											ae.setId(newAeId);
											ceng.getRewards().get(k).setEntityId(newAeId);
											DefaultArticleEntityManager.getInstance().em.flush(ae);
										}
									}
								}
							}
						}
					}
				}
				QianCengTaManager.getInstance().em.flush(ta);
			}
		} catch (Exception e) {
			
		}
		
	}
	
	/** Cave */
	private void replaceAndSaveCave(Player player, long newPid)  throws Exception{
		if (player.getCaveId() > 0) {
			Cave cave = caveEm.find(player.getCaveId());
			Field versionField = DataMoveManager.getFieldByAnnotation(Cave.class, SimpleVersion.class);
			versionField.setAccessible(true);
			cave.setOwnerId(newPid);
			cave.setStatus(Cave.CAVE_STATUS_KHATAM);
			long newId = FaeryManager.caveEm.nextId();
			cave.setId(newId);
			versionField.set(cave, 0);
			FaeryManager.caveEm.flush(cave);
			player.setCaveId(newId);
		}
	}
	/**
	 * 
	 * @param oldId
	 * @return
	 */
	public long replaceOldId(long oldId, Map<Long, Long> tempMap, Field versionField) {
		if (tempMap.containsKey(oldId)) {
			return  tempMap.get(oldId);
		}
		if (oldId > 0) {
			try {
				ArticleEntity ae = aeEm.find(oldId);
				long newId = DefaultArticleEntityManager.getInstance().em.nextId();
				ae.setId(newId);
				versionField.set(ae, 0);
				DefaultArticleEntityManager.getInstance().em.flush(ae);
				return newId;
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		return oldId;
	}
	
	/**
	 * 替换物品id
	 * @param player
	 * @param tempMap
	 * @throws Exception
	 */
	public void replacePlayerArticle(Player player, Map<Long, Long> tempMap, Field versionField) throws Exception {
		{		//收集装备
			for (int i=0; i<player.getSouls().length; i++) {
				Soul soul = player.getSouls()[i];
				if (soul == null) {
					continue;
				}
				for (int j=0; j<soul.getEc().getEquipmentIds().length; j++) {
					if (soul.getEc().getEquipmentIds()[j] > 0) {
						long oldId = soul.getEc().getEquipmentIds()[j];
						if (oldId > 0) {
							soul.getEc().getEquipmentIds()[j] = replaceOldId(oldId, tempMap, versionField);
						}
					}
				}
			}
		}
		{		//背包 (包括宠物背包)
			Knapsack bag1 = player.getKnapsack_common();			//普通背包
			if (bag1 != null) {
				for (int i=0; i<bag1.getCells().length; i++) {
					if (bag1.getCells()[i].getEntityId() > 0) {
						long oldId = bag1.getCells()[i].getEntityId();
						if (oldId > 0) {
							bag1.getCells()[i].setEntityId(replaceOldId(oldId, tempMap, versionField));
						}
					}
				}
			}
			Knapsack bag2 = player.getPetKnapsack();				//宠物背包
			if (bag2 != null) {
				for (int i=0; i<bag2.getCells().length; i++) {
					if (bag2.getCells()[i].getEntityId() > 0) {
						long oldId = bag2.getCells()[i].getEntityId();
						if (oldId > 0) {
							bag2.getCells()[i].setEntityId(replaceOldId(oldId, tempMap, versionField));
						}
					}
				}
			}
		}
		{		//仓库
			Knapsack bag = player.getKnapsacks_cangku();
			if (bag != null) {
				for (int i=0; i<bag.getCells().length; i++) {
					if (bag.getCells()[i].getEntityId() > 0) {
						long oldId = bag.getCells()[i].getEntityId();
						if (oldId > 0) {
							bag.getCells()[i].setEntityId(replaceOldId(oldId, tempMap, versionField));
						}
					}
				}
			}
		}
		{		//器灵仓库
			Knapsack bag = player.getKnapsacks_QiLing();
			if (bag != null) {
				for (int i=0; i<bag.getCells().length; i++) {
					if (bag.getCells()[i].getEntityId() > 0) {
						long oldId = bag.getCells()[i].getEntityId();
						if (oldId > 0) {
							bag.getCells()[i].setEntityId(replaceOldId(oldId, tempMap, versionField));
						}
					}
				}
			}
		}
		{		//防爆包  （防爆包本身也是个道具）
			Knapsack bag = player.getKnapsack_fangbao();
			if (bag != null) {
				for (int i=0; i<bag.getCells().length; i++) {
					if (bag.getCells()[i].getEntityId() > 0) {
						long oldId = bag.getCells()[i].getEntityId();
						if (oldId > 0) {
							bag.getCells()[i].setEntityId(replaceOldId(oldId, tempMap, versionField));
						}
					}
				}
			}
		}
		{		//千层塔
//			QianCengTa_Ta q = taEm.find(player.getId());
//			if (q != null) {
//				for (int i = 0; i < q.getDaoList().size(); i++) {
//					QianCengTa_Dao dao = q.getDaoList().get(i);
//					for (int j = 0; j < dao.getCengList().size(); j++) {
//						QianCengTa_Ceng ceng = dao.getCengList().get(j);
//						for (int k = 0; k < ceng.getRewards().size(); k++) {
//							if (ceng.getRewards().get(k).getEntityId() > 0) {
//								long oldId = ceng.getRewards().get(k).getEntityId();
//								if (oldId > 0) {
//									ceng.getRewards().get(k).setEntityId(replaceOldId(oldId, tempMap, versionField));
//								}
//							}
//						}
//					}
//				}
//			}
		}
		/*{		//坐骑装备
			for (Soul soul : player.getSouls()) {
				if (soul == null) {
					continue;
				}
				for (long horseId : soul.getHorseArr()) {
					Horse horse = hrEm.find(horseId);
					if (horse != null && !horse.isFly()) {
						long[] horseEquIDs = horse.getEquipmentColumn().getEquipmentIds();
						for (int i=0; i<horseEquIDs.length; i++) {
							long oldId = horseEquIDs[i];
							if (oldId > 0) {
								horseEquIDs[i] = replaceOldId(oldId, tempMap, versionField);
							}
						}
					}
				}
			}
		}*/
	}
	
	
	/**
	 * 没有收集装备上的宝石和麒麟，需要再查出物品实体时判断是否为装备再收集查找
	 * @param player
	 * @return
	 * @throws Exception
	 */
	public Set<Long> collectPlayerArticle(Player player) throws Exception {
		Set<Long> resultList = new HashSet<Long>();
		{		//收集装备
			for (int i=0; i<player.getSouls().length; i++) {
				Soul soul = player.getSouls()[i];
				if (soul == null) {
					continue;
				}
				for (int j=0; j<soul.getEc().getEquipmentIds().length; j++) {
					if (soul.getEc() != null && soul.getEc().getEquipmentIds()[j] > 0) {
						resultList.add(soul.getEc().getEquipmentIds()[j]);
					}
				}
			}
		}
		{		//背包 (包括宠物背包)
			Knapsack bag1 = player.getKnapsack_common();			//普通背包
			if (bag1 != null) {
				for (int i=0; i<bag1.getCells().length; i++) {
					if (bag1.getCells()[i] != null && bag1.getCells()[i].getEntityId() > 0) {
						resultList.add(bag1.getCells()[i].getEntityId());
					}
				}
			}
			Knapsack bag2 = player.getPetKnapsack();				//宠物背包
			if (bag2 != null) {
				for (int i=0; i<bag2.getCells().length; i++) {
					if (bag2.getCells()[i] != null && bag2.getCells()[i].getEntityId() > 0) {
						resultList.add(bag2.getCells()[i].getEntityId());
					}
				}
			}
		}
		{		//仓库
			Knapsack bag = player.getKnapsacks_cangku();
			if (bag != null) {
				for (int i=0; i<bag.getCells().length; i++) {
					if (bag.getCells()[i] != null && bag.getCells()[i].getEntityId() > 0) {
						resultList.add(bag.getCells()[i].getEntityId());
					}
				}
			}
		}
		{		//器灵仓库
			Knapsack bag = player.getKnapsacks_QiLing();
			if (bag != null) {
				for (int i=0; i<bag.getCells().length; i++) {
					if (bag.getCells()[i] != null && bag.getCells()[i].getEntityId() > 0) {
						resultList.add(bag.getCells()[i].getEntityId());
					}
				}
			}
		}
		try{		//防爆包  （防爆包本身也是个道具）
			Knapsack bag = player.getKnapsack_fangbao();
			if (bag != null) {
				for (int i=0; i<bag.getCells().length; i++) {
					if (bag.getCells()[i] != null && bag.getCells()[i].getEntityId() > 0) {
						resultList.add(bag.getCells()[i].getEntityId());
					}
				}
			}
		}catch (Exception e) {
			
		}
		try{		//千层塔
			QianCengTa_Ta q = taEm.find(player.getId());
			if (q != null) {
				for (int i = 0; i < q.getDaoList().size(); i++) {
					QianCengTa_Dao dao = q.getDaoList().get(i);
					for (int j = 0; j < dao.getCengList().size(); j++) {
						QianCengTa_Ceng ceng = dao.getCengList().get(j);
						for (int k = 0; k < ceng.getRewards().size(); k++) {
							if (ceng.getRewards().get(k).getEntityId() > 0) {
								resultList.add(ceng.getRewards().get(k).getEntityId());
							}
						}
					}
				}
			}
		}catch(Exception e) {}
		try{		//坐骑装备
			for (Soul soul : player.getSouls()) {
				if (soul == null) {
					continue;
				}
				for (long horseId : soul.getHorseArr()) {
					Horse horse = hrEm.find(horseId);
					if (horse != null && !horse.isFly()) {
						long[] horseEquIDs = horse.getEquipmentColumn().getEquipmentIds();
						for (long i : horseEquIDs) {
							if (i > 0) {
								resultList.add(i);
							}
						}
					}
				}
			}
		}catch(Exception e){
			
		}
		return resultList;
	}
	Field petField = null;
	
	public void replactPetEntity(ArticleEntity ae, long ownerId) {
		try {
			if (ae instanceof PetPropsEntity) {
				PetPropsEntity pp = (PetPropsEntity) ae;
				long oldPetId = pp.getPetId();
				long newPetId = PetManager.em.nextId();
				Pet pet = pEm.find(oldPetId);
				petField.set(pet, 0);
				pet.setId(newPetId);
				pet.setOwnerId(ownerId);
				pet.setPetPropsId(ae.getId());
				((PetPropsEntity) ae).setPetId(newPetId);
				PetManager.em.flush(pet);
			} else if (ae instanceof PetEggPropsEntity) {
				PetEggPropsEntity ppe = (PetEggPropsEntity) ae;
				long oldPetId = ppe.getPetId();
				if (oldPetId > 0) {
					long newPetId = PetManager.em.nextId();
					Pet pet = pEm.find(oldPetId);
					pet.setPetPropsId(ae.getId());
					petField.set(pet, 0);
					pet.setId(newPetId);
					pet.setOwnerId(ownerId);
					PetManager.em.flush(pet);
					((PetEggPropsEntity) ae).setPetId(newPetId);
				}
			}
		} catch (Exception e) {
			logger.warn("[腾讯数据转移] [转移宠物道具] [" + ae.getId() + "]", e);
		}
	}
	
	/**
	 * 替换装备上的宝石和器灵id
	 * @param ae
	 * @return
	 * @throws Exception 
	 */
	public void replaceEquiptOtherIds(ArticleEntity ae, SimpleEntityManager<ArticleEntity> articleEm, Field versionField) throws Exception {
		if (ae instanceof EquipmentEntity) {
			EquipmentEntity ee = (EquipmentEntity) ae;
			if (ee.getInlayArticleIds() != null && ee.getInlayArticleIds().length > 0) {
				for (int i=0; i<ee.getInlayArticleIds().length; i++) {
					long baoshiId = ee.getInlayArticleIds()[i];
					if (baoshiId > 0) {
						ArticleEntity baoshi = articleEm.find(baoshiId);
						long newId = DefaultArticleEntityManager.getInstance().em.nextId();
						baoshi.setId(newId);
						versionField.set(baoshi, 0);
						ee.getInlayArticleIds()[i] = newId;
						DefaultArticleEntityManager.getInstance().em.flush(baoshi);
					}
				}
			}
			if (ee.getInlayQiLingArticleIds() != null && ee.getInlayQiLingArticleIds().length > 0) {
				for (int i=0; i<ee.getInlayQiLingArticleIds().length; i++) {
					long qilingId = ee.getInlayQiLingArticleIds()[i];
					if (qilingId > 0) {
						ArticleEntity qilin = articleEm.find(qilingId);
						long newId = DefaultArticleEntityManager.getInstance().em.nextId();
						qilin.setId(newId);
						versionField.set(qilin, 0);
						ee.getInlayQiLingArticleIds()[i] = newId;
						DefaultArticleEntityManager.getInstance().em.flush(qilin);
					}
				}
			}
		}
	}
	
	public List<Long> getPlayerIds() {
		return playerIds;
	}
	public void setPlayerIds(List<Long> playerIds) {
		this.playerIds = playerIds;
	}
	public DefaultSimpleEntityManagerFactory getFactory() {
		return factory;
	}
	public void setFactory(DefaultSimpleEntityManagerFactory factory) {
		this.factory = factory;
	}
	
	
}
