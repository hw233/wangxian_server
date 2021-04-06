<%@page import="com.fy.engineserver.datasource.article.concrete.DefaultArticleEntityManager"%>
<%@page import="java.util.LinkedList"%>
<%@page import="com.sun.swing.internal.plaf.synth.resources.synth"%>
<%@page import="com.fy.engineserver.articleProtect.ArticleProtectManager"%>
<%@page import="java.util.Collection"%>
<%@page import="com.fy.engineserver.datasource.article.data.entity.EquipmentEntity"%>
<%@page import="com.fy.engineserver.datasource.article.data.entity.BiWuArticleEntity"%>
<%@page import="com.fy.engineserver.datasource.article.data.entity.BottlePropsEntity"%>
<%@page import="com.fy.engineserver.datasource.article.data.entity.ExchangeArticleEntity"%>
<%@page import="com.fy.engineserver.datasource.article.data.entity.FateActivityPropsEntity"%>
<%@page import="com.fy.engineserver.datasource.article.data.entity.MagicWeaponEntity"%>
<%@page import="com.fy.engineserver.datasource.article.data.entity.PropsEntity"%>
<%@page import="com.fy.engineserver.datasource.article.data.entity.Special_1EquipmentEntity"%>
<%@page import="com.fy.engineserver.datasource.article.data.entity.Special_2EquipmentEntity"%>
<%@page import="com.fy.engineserver.activity.explore.ExploreResourceMapEntity"%>
<%@page import="com.fy.engineserver.sprite.pet.SingleForPetPropsEntity"%>
<%@page import="com.fy.engineserver.datasource.article.data.entity.QiLingArticleEntity"%>
<%@page import="com.fy.engineserver.datasource.article.data.entity.YinPiaoEntity"%>
<%@page import="com.fy.engineserver.activity.dig.DigPropsEntity"%>
<%@page import="com.xuanzhi.tools.simplejpa.SimpleEntityManager"%>
<%@page import="com.fy.engineserver.datasource.article.data.entity.NewMagicWeaponEntity"%>
<%@page import="com.fy.engineserver.datasource.article.data.entity.PetEggPropsEntity"%>
<%@page import="com.fy.engineserver.datasource.article.data.entity.PetPropsEntity"%>
<%@page import="com.xuanzhi.tools.simplejpa.annotation.SimpleVersion"%>
<%@page import="java.lang.reflect.Field"%>
<%@page import="com.fy.engineserver.uniteserver.UnitedServerManager2"%>
<%@page import="org.apache.commons.lang.StringUtils"%>
<%@page import="java.net.URLDecoder"%>
<%@page import="com.fasterxml.jackson.core.type.TypeReference"%>
<%@page import="com.fy.engineserver.movedata.DataMoveManager"%>
<%@page import="java.util.Queue"%>
<%@page import="com.fy.engineserver.homestead.faery.service.QuerySelect"%>
<%@page import="com.xuanzhi.tools.simplejpa.impl.DefaultSimpleEntityManagerFactory"%>
<%@page import="com.fy.engineserver.sprite.concrete.GamePlayerManager"%>
<%@page import="com.fy.engineserver.core.Game"%>
<%@page import="com.fy.engineserver.wing.WingManager"%>
<%@page import="com.fy.engineserver.wing.WingPanel"%>
<%@page import="com.fy.engineserver.homestead.faery.service.FaeryManager"%>
<%@page import="com.fy.engineserver.homestead.cave.Cave"%>
<%@page import="com.fy.engineserver.playerAims.instance.PlayerAimsEntity"%>
<%@page import="com.fy.engineserver.playerAims.manager.PlayerAimeEntityManager"%>
<%@page import="com.fy.engineserver.sprite.horse2.manager.Horse2EntityManager"%>
<%@page import="com.fy.engineserver.sprite.horse2.instance.Horse2RelevantEntity"%>
<%@page import="com.fy.engineserver.newtask.NewDeliverTaskManager"%>
<%@page import="com.fy.engineserver.newtask.NewDeliverTask"%>
<%@page import="com.fy.engineserver.buffsave.BufferSaveManager"%>
<%@page import="com.fy.engineserver.buffsave.BuffSave"%>
<%@page import="com.fy.engineserver.activity.TransitRobbery.TransitRobberyEntityManager"%>
<%@page import="com.fy.engineserver.activity.TransitRobbery.TransitRobberyEntity"%>
<%@page import="com.fy.engineserver.datasource.skill.master.SkEnhanceManager"%>
<%@page import="com.fy.engineserver.datasource.skill.master.SkBean"%>
<%@page import="com.fy.engineserver.articleProtect.PlayerArticleProtectData"%>
<%@page import="com.fy.engineserver.articleProtect.ArticleProtectData"%>
<%@page import="com.fy.engineserver.minigame.MiniGameEntityManager"%>
<%@page import="com.fy.engineserver.minigame.MiniGameEntity"%>
<%@page import="com.fy.engineserver.activity.activeness.ActivenessManager"%>
<%@page import="com.fy.engineserver.activity.activeness.PlayerActivenessInfo"%>
<%@page import="com.fy.engineserver.sprite.pet2.Pet2Manager"%>
<%@page import="com.fy.engineserver.sprite.pet2.PetsOfPlayer"%>
<%@page import="com.fy.engineserver.activity.worldBoss.WorldBossRankManager"%>
<%@page import="com.fy.engineserver.activity.worldBoss.WBossBean"%>
<%@page import="com.fy.engineserver.sprite.pet.Pet"%>
<%@page import="com.fy.engineserver.sprite.pet.PetManager"%>
<%@page import="com.xuanzhi.tools.text.JsonUtil"%>
<%@page import="com.fy.engineserver.sprite.horse.Horse"%>
<%@page import="com.fy.engineserver.sprite.horse.HorseManager"%>
<%@page import="com.fy.engineserver.hotspot.HotspotManager"%>
<%@page import="com.fy.engineserver.hotspot.HotspotInfo"%>
<%@page import="com.fy.engineserver.hotspot.Hotspot"%>
<%@page import="java.util.List"%>
<%@page import="com.fy.engineserver.achievement.AchievementEntity"%>
<%@page import="java.util.HashMap"%>
<%@page import="com.fy.engineserver.achievement.AchievementManager"%>
<%@page import="com.fy.engineserver.achievement.GameDataRecord"%>
<%@page import="com.fy.engineserver.newtask.TaskEntityManager"%>
<%@page import="com.fy.engineserver.newtask.TaskEntity"%>
<%@page
	import="com.fy.engineserver.newtask.service.DeliverTaskManager"%>
<%@page import="com.fy.engineserver.newtask.DeliverTask"%>
<%@page
	import="com.fy.engineserver.datasource.article.manager.ArticleEntityManager"%>
<%@page
	import="com.fy.engineserver.datasource.article.data.entity.ArticleEntity"%>
<%@page import="com.fy.engineserver.sprite.Soul"%>
<%@page import="com.fy.engineserver.qiancengta.QianCengTa_Ceng"%>
<%@page import="com.fy.engineserver.qiancengta.QianCengTa_Dao"%>
<%@page import="com.fy.engineserver.qiancengta.QianCengTaManager"%>
<%@page import="com.fy.engineserver.qiancengta.QianCengTa_Ta"%>
<%@page import="java.util.HashSet"%>
<%@page import="java.util.ArrayList"%>
<%@page import="com.fy.engineserver.sprite.PlayerManager"%>
<%@page import="com.fy.engineserver.sprite.Player"%>
<%@page import="com.xuanzhi.tools.text.ParamUtils"%>
<%@page import="java.util.LinkedHashMap"%>
<%@page
	import="org.apache.xmlbeans.impl.jam.annotation.LineDelimitedTagParser"%>
<%@page import="java.util.Map"%>
<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<% 
	String ceshi = "/data/home/cp_mqq/resin_server_2/webapps/game_server/WEB-INF/spring_config/simpleEMF.xml";
	String zhengshi = "/data/home/cp_mqq/resin_server_2/webapps/game_server/WEB-INF/spring_config/simpleEMF_zhuanyi.xml";
	/* String path2 = "";
	String path3 = "";
	String path4 = ""; */

	DefaultSimpleEntityManagerFactory zfFactory = new DefaultSimpleEntityManagerFactory(zhengshi);
	DefaultSimpleEntityManagerFactory csFactory = new DefaultSimpleEntityManagerFactory(ceshi);
	/*DefaultSimpleEntityManagerFactory factory2 = new DefaultSimpleEntityManagerFactory(mainPath);
	DefaultSimpleEntityManagerFactory factory3 = new DefaultSimpleEntityManagerFactory(mainPath);
	DefaultSimpleEntityManagerFactory factory4 = new DefaultSimpleEntityManagerFactory(mainPath); */
	
	Queue<String> queue = new LinkedList<String>();
	List<Long> list = new ArrayList<Long>();
	list.add(1200000000000048131L);		
	//list.add(1200000000000048132L);		
	CollectThread ct = new CollectThread(list, queue, csFactory);
	Thread thrad1 = new Thread(ct);
	thrad1.setName("转服数据收集");
	thrad1.start();
	List<CollectThread> collectThreads = new  ArrayList<CollectThread>();;
	collectThreads.add(ct);
	Thread thrad2 = new Thread(new MoveThread(queue, collectThreads, zfFactory));
	thrad2.setName("转移数据");
	thrad2.start();  
	out.println("==============================");

%>

<%!

public class MoveThread implements Runnable{
	Queue<String> in;
	
	List<CollectThread> collectThreads;
	
	DefaultSimpleEntityManagerFactory factory;			//需要存储到的库的factory
	
	
	public MoveThread(Queue<String> in, List<CollectThread> collectThreads, DefaultSimpleEntityManagerFactory factory) {
		this.in = in;
		this.collectThreads = collectThreads;
		this.factory = factory;
	}
	
	public void run() {
		boolean allDone = false;
		while (!allDone || in.size() > 0) {
			boolean temp = true;
			for (CollectThread ct : collectThreads) {
				if (!ct.finished) {
					temp = false;
					break;
				}
			}
			allDone = temp;
			try {
				Thread.sleep(100);
			} catch (Exception e) {
				
			}
			
			List<String> onceCove = new ArrayList<String>();
			synchronized(in) {
				int leftNum = 1000;
				if (leftNum > 0 && in.size() > 0) {
					onceCove.add(in.poll());
					leftNum -- ;
				}
			}
			if (onceCove.size() <= 0) {
				continue;
			}
			for (String str : onceCove) {
				try {
					long startTime = System.currentTimeMillis();
					String result = reiceivePlayer(str, factory, true);	
					long endTime = System.currentTimeMillis();
					ArticleEntityManager.log.warn("[接收数据] [耗时:" + (endTime - startTime));
					if (result == null) {
						ArticleEntityManager.log.warn("[接收数据] [失败] [转移不成功] [str:" + str + "]");
					} else {
						ArticleEntityManager.log.warn("[接收数据] [成功] [str:" + str + "]");
					}
				} catch (Exception e) {
					ArticleEntityManager.log.warn("[接收数据] [异常] [str:" + str + "]", e);
				}
			}
		}
	}
}

/** 收集数据的线程 */
public class CollectThread implements Runnable{
	
	List<Long> playerIds ;
	
	Queue<String> out;
	
	
	DefaultSimpleEntityManagerFactory factory;			//需要查找数据的factory
	
	public boolean finished = false;
	
	public CollectThread(){}
	public CollectThread(List<Long> playerIds, Queue<String> out, DefaultSimpleEntityManagerFactory factory) {
		this.playerIds = playerIds;
		this.out = out;
		this.factory = factory;
	}
	
	public void aa (List<Long> playerIds) {
		this.playerIds = playerIds;
	}
	public void setVar(List<Long> playerIds, Queue<String> out, DefaultSimpleEntityManagerFactory factory) {
		this.playerIds = playerIds;
		this.out = out;
		this.factory = factory;
	}
	
	public void run() {
		List<String> resList = new ArrayList<String>();
		for (int i=0; i<playerIds.size(); i++) {
			long playerId = playerIds.get(i);
			if (playerId > 0) {
				String result = null;
				try {
					long startTime = System.currentTimeMillis();
					result = getPlayerCovertInfo(playerId, factory);
					long endTime = System.currentTimeMillis();
					ArticleEntityManager.log.warn("[转移角色数据] [收集数据] [耗时:" + (endTime -startTime) + "ms");
				} catch (Exception e) {
					ArticleEntityManager.log.warn("[转移角色数据] [异常] [id:" + playerId + "]", e);
				}
				if (result != null) {
					if (resList == null) {
						resList = new ArrayList<String>();
					}
					resList.add(result);
					if (resList.size() > 1000 || i == (playerIds.size()-1)) {
						synchronized(out) {
							out.addAll(resList);
						}
						resList = null;
					}
				} else {
					ArticleEntityManager.log.warn("[转移角色数据] [失败] [result==null] [id:" + playerId + "]");
				}
				try {
					Thread.sleep(100);
				} catch (Exception e) {
					
				}
			}
		}
		finished = true;
	}
}
%>
	
<%!
	//long playerId = ParamUtils.getLongParameter(request, "pid", -1);
public String getPlayerCovertInfo(long playerId, DefaultSimpleEntityManagerFactory mainFactory) throws Exception{
	//long playerId = ParamUtils.getLongParameter(request, "pid", -1);

	Player player = mainFactory.getSimpleEntityManager(Player.class).find(playerId);
	
	if(player != null)
	{
		Map returnHashMap = new LinkedHashMap();
		
		returnHashMap.put("player",player);
		//获取所有的道具，然后放入到一个list中，再放入returnList

		 
		ArrayList<ArticleEntity> articleList =	new ArrayList<ArticleEntity>();
		ArrayList<EquipmentEntity>equipmentList = new ArrayList<EquipmentEntity>();
		ArrayList<BiWuArticleEntity> biWuList =	new ArrayList<BiWuArticleEntity>();
		ArrayList<BottlePropsEntity> bottlePropsList =	new ArrayList<BottlePropsEntity>();
		ArrayList<ExchangeArticleEntity> exchangeArticleList =	new ArrayList<ExchangeArticleEntity>();
		ArrayList<FateActivityPropsEntity> fateActivityPropsList =	 new ArrayList<FateActivityPropsEntity>();
		ArrayList<MagicWeaponEntity> magicWeaponList =	 new ArrayList<MagicWeaponEntity>();
		ArrayList<PetEggPropsEntity> petEggPropsList =	new ArrayList<PetEggPropsEntity>();
		ArrayList<PetPropsEntity> petPropsList =	 new ArrayList<PetPropsEntity>();
		ArrayList<PropsEntity> propsEntityList =	  new ArrayList<PropsEntity>();
		ArrayList<Special_1EquipmentEntity> special_1EquipmentList =	 new ArrayList<Special_1EquipmentEntity>();
		ArrayList<Special_2EquipmentEntity> special_2EquipmentList =new ArrayList<Special_2EquipmentEntity>();
		ArrayList<ExploreResourceMapEntity> exploreResourceMapList =	new ArrayList<ExploreResourceMapEntity>();
		ArrayList<SingleForPetPropsEntity> singleForPetPropsList =	new ArrayList<SingleForPetPropsEntity>();
		ArrayList<QiLingArticleEntity> qiLingArticleList =	new ArrayList<QiLingArticleEntity>();
		ArrayList<YinPiaoEntity> yinPiaoList =	new ArrayList<YinPiaoEntity>();
		ArrayList<DigPropsEntity> digPropsList =		new ArrayList<DigPropsEntity>();
		ArrayList<NewMagicWeaponEntity> newMagicWeaponList =		new ArrayList<NewMagicWeaponEntity>();
		HashSet<Long> articleIds = new HashSet<Long>();
	
		{
			
		
			//获取以playerId为主键的千层塔的相关道具
		
			QianCengTa_Ta qianCengTa_Ta = mainFactory.getSimpleEntityManager(QianCengTa_Ta.class).find(playerId);
			
			
			WingPanel wp = mainFactory.getSimpleEntityManager(WingPanel.class).find(playerId);
			if(wp != null)
			{
				long brightInlayId = wp.getBrightInlayId();
				if(brightInlayId > 0){
					articleIds.add(brightInlayId);
				}
				//镶嵌的宝石
				long[] inlayArticleIds = wp.getInlayArticleIds();
				if(inlayArticleIds != null){
					for(long id : inlayArticleIds){
						if(id > 0){
							articleIds.add(id);
						}
					}
				}
			}
			
		
		
			if(qianCengTa_Ta != null)
			{
				for (int i = 0; i < qianCengTa_Ta.getDaoList().size(); i++) {
					QianCengTa_Dao dao = qianCengTa_Ta.getDaoList().get(i);
					for (int j = 0; j < dao.getCengList().size(); j++) {
						QianCengTa_Ceng ceng = dao.getCengList().get(j);
						for (int k = 0; k < ceng.getRewards().size(); k++) {
							if (ceng.getRewards().get(k).getEntityId() > 0) {
								articleIds.add(ceng.getRewards().get(k)
										.getEntityId());
							}
						}
					}
				}
			}
		
			//获取player身上的道具
			// 防暴包
			if (player.getKnapsack_fangBao_Id() > 0) {
				articleIds.add(player.getKnapsack_fangBao_Id());
			}
			if (player.getKnapsack_fangbao() != null) {
				for (int i = 0; i < player.getKnapsack_fangbao().size(); i++) {
					try {
						if (player.getKnapsack_fangbao().getCell(i) != null) {
							if (player.getKnapsack_fangbao().getCell(i)
									.getEntityId() > 0) {
								articleIds.add(player
										.getKnapsack_fangbao()
										.getCell(i).getEntityId());
							}
						}
					} catch (Exception e) {
					}
				}
			}
			if (player.getKnapsack_common() != null) {
				// 背包
				for (int i = 0; i < player.getKnapsack_common().size(); i++) {
					try {
						if (player.getKnapsack_common().getCell(i) != null) {
							if (player.getKnapsack_common().getCell(i)
									.getEntityId() > 0) {
								articleIds.add(player
										.getKnapsack_common()
										.getCell(i).getEntityId());
							}
						}
					} catch (Exception e) {
					}
				}
			}
			if (player.getKnapsacks_cangku() != null) {
				// 仓库
				for (int i = 0; i < player.getKnapsacks_cangku().size(); i++) {
					try {
						if (player.getKnapsacks_cangku().getCell(i) != null) {
							if (player.getKnapsacks_cangku().getCell(i)
									.getEntityId() > 0) {
								articleIds.add(player
										.getKnapsacks_cangku()
										.getCell(i).getEntityId());
							}
						}
					} catch (Exception e) {
					}
				}
			}
			if (player.getSouls() != null) {
				// 装备
				for (int i = 0; i < player.getSouls().length; i++) {
					try {
						Soul soul = player.getSouls()[i];
						if (soul == null || soul.getEc() == null) {
							continue;
						}
						for (int j = 0; j < soul.getEc()
								.getEquipmentIds().length; j++) {
							if (soul.getEc().getEquipmentIds()[j] > 0) {
								articleIds.add(soul.getEc()
										.getEquipmentIds()[j]);
							}
						}
					} catch (Exception e) {
					}
				}
			}
			if (player.getKnapsacks_QiLing() != null) {
				// 器灵
				for (int i = 0; i < player.getKnapsacks_QiLing().size(); i++) {
					try {
						if (player.getKnapsacks_QiLing().getCell(i) != null) {
							if (player.getKnapsacks_QiLing().getCell(i)
									.getEntityId() > 0) {
								articleIds.add(player
										.getKnapsacks_QiLing()
										.getCell(i).getEntityId());
							}
						}
					} catch (Exception e) {
					}
				}
			}
			

		
		}
	
	
	/* //获取和player相关的delivertask
	{
		List<DeliverTask> deliverTasks  = DeliverTaskManager.getInstance().loadAllDeliverTask(playerId);
		if(deliverTasks != null)
			returnHashMap.put("delivertasks",deliverTasks);
	} */
	//获取和player相关的taskEntitys
	{
		String where = "playerId = ? and (showType >=1 and showType <= 3  or status < 3)";
		List<TaskEntity> taskEntitys = mainFactory.getSimpleEntityManager(TaskEntity.class).query(TaskEntity.class, where,new Object[] {playerId}, "id desc", 1, 9999);
		if(taskEntitys != null)
		{
			returnHashMap.put("taskentitys",taskEntitys);
		}
	}
	
	//获取玩家的仙府 这里的仙府摆放什么的有疑问，暂时先不处理
	
	//获取玩家的gamedata
	{
		QuerySelect<GameDataRecord> querySelect = new QuerySelect<GameDataRecord>();
		List<GameDataRecord> queryList = querySelect.selectAll(GameDataRecord.class, "playerId = ?", new Object[] { player.getId() }, null, mainFactory.getSimpleEntityManager(GameDataRecord.class));
		if (queryList == null) {
			queryList = new ArrayList<GameDataRecord>();
		}
		HashMap<Integer, GameDataRecord> playerRecords = new HashMap<Integer, GameDataRecord>();
		for (GameDataRecord gdr : queryList) {
			playerRecords.put(gdr.getDataType(), gdr);
		}
		if(playerRecords != null)
		{
			returnHashMap.put("playerrecords",playerRecords);
		}
	}
	
	
	//获取玩家的千层塔
	{
		 QianCengTa_Ta qianCengTa_Ta = mainFactory.getSimpleEntityManager(QianCengTa_Ta.class).find(playerId);
		 if(qianCengTa_Ta != null)
		 {
			 returnHashMap.put("qiancengta_ta",qianCengTa_Ta);
		 }
	}
	
	//获取玩家的HotSpot
	{
		List<HotspotInfo> list = new ArrayList<HotspotInfo>();
		long count = mainFactory.getSimpleEntityManager(HotspotInfo.class).count(HotspotInfo.class, "playerID=" + player.getId());
		list.addAll(mainFactory.getSimpleEntityManager(HotspotInfo.class).query(HotspotInfo.class, "playerID=" + player.getId(), "", 1, count + 1));
		
		returnHashMap.put("hotspotinfos",list);
	}
	
	//获取玩家的Horse
	/**
		玩家有两个元神 上面有自己的horseAttr 这才是真正玩家的坐骑
	*/
	{
		ArrayList<Long> horseIds =  new ArrayList<Long>();
		

		// 装备
		for (int i = 0; i < player.getSouls().length; i++) {
			try {
				Soul soul = player.getSouls()[i];
				if (soul == null ) {
					continue;
				}
				for (long id : soul.getHorseArr()) {
					if (!horseIds.contains(id)) {
						horseIds.add(id);
					}
				}
				//horseIds.addAll(soul.getHorseArr());
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
	
		
		
		if(horseIds != null && horseIds.size() > 0)
		{
			ArrayList<Horse> horses = new ArrayList<Horse>();
			SimpleEntityManager<Horse> em = mainFactory.getSimpleEntityManager(Horse.class);
			for(Long horseId : horseIds)
			{
				Horse horse = em.find(horseId);
				if(horse != null)
				{
					horses.add(horse);
					
					long[] horseEquIDs = horse.getEquipmentColumn().getEquipmentIds();
					for (long i : horseEquIDs) {
						if (i > 0) {
							articleIds.add(i);
						}
					}
					
				}
				
			}
			returnHashMap.put("horses",horses);
		}
	}
	
	//获取玩家的Pet
	{
		List<Pet> list = new ArrayList<Pet>();
		long count = mainFactory.getSimpleEntityManager(Pet.class).count(Pet.class, "ownerId=" + player.getId() + " and delete = 'F'");
		list.addAll(mainFactory.getSimpleEntityManager(Pet.class).query(Pet.class, "ownerId=" + player.getId()+ " and delete = 'F'", "", 1, count + 1));
		for(Pet pet : list)
		{
			long petPropId = pet.getPetPropsId();
			if(petPropId > 0)
			{
				articleIds.add(petPropId);
			}
			
		}
		
		returnHashMap.put("pets",list);
	}
	/*
	{
		List<WBossBean> list = new ArrayList<WBossBean>();
		long count = WorldBossRankManager.getInst().beanEm.count(Pet.class, "ownerId=" + player.getId());
		list.addAll(PetManager.getInstance().em.query(Pet.class, "ownerId=" + player.getId(), "", 1, count + 1));
		returnHashMap.put("pets",list);
	} */
	
	//获取玩家获得过的宠物记录
	{
		PetsOfPlayer petsOfPlayer = mainFactory.getSimpleEntityManager(PetsOfPlayer.class).find(playerId);
		if(petsOfPlayer != null)
			returnHashMap.put("petsofplayer",petsOfPlayer);
	}
	
	//获取玩家活跃度
	{
		PlayerActivenessInfo playerActivenessInfo  = mainFactory.getSimpleEntityManager(PlayerActivenessInfo.class).find(playerId);
		returnHashMap.put("playeractivenessinfo",playerActivenessInfo);
	}
	
	
	//获取玩家的锁魂记录
	{
		List<ArticleProtectData> list = new ArrayList<ArticleProtectData>();
		long count =mainFactory.getSimpleEntityManager(ArticleProtectData.class).count(ArticleProtectData.class, "playerID=" + player.getId());
		list.addAll(mainFactory.getSimpleEntityManager(ArticleProtectData.class).query(ArticleProtectData.class, "playerID=" + player.getId(), "", 1, count + 1));
		returnHashMap.put("articleprotectdatas",list);
	}
	
	//获取玩家大师技能
	{
		SkBean skBean = mainFactory.getSimpleEntityManager(SkBean.class).find(playerId);
		if (skBean != null)	
			returnHashMap.put("skbean",skBean);
	}
	
	//获取玩家渡劫信息
	{
		TransitRobberyEntity transitRobberyEntity = mainFactory.getSimpleEntityManager(TransitRobberyEntity.class).find(playerId);
		if(transitRobberyEntity != null)	
			returnHashMap.put("transitrobberyentity",transitRobberyEntity);
	}
	
	//获取玩家buff
	{
		List<BuffSave> list = mainFactory.getSimpleEntityManager(BuffSave.class).query(BuffSave.class, " pid = "+playerId, "", 1, 100);
		if(list != null)	
			returnHashMap.put("buffsaves",list);
	}
	
	//获取玩家newdelivertask
	{
		List<NewDeliverTask> newDeliverTasks = mainFactory.getSimpleEntityManager(NewDeliverTask.class).query(NewDeliverTask.class, "playerId=?", new Object[]{playerId},"", 1, 9999);
		if(newDeliverTasks != null)
		{
			returnHashMap.put("newdelivertasks",newDeliverTasks);
		}
	}
	
	//获取玩家新宠物数据信息
	{
		Horse2RelevantEntity  horse2RelevantEntity = mainFactory.getSimpleEntityManager(Horse2RelevantEntity.class).find(playerId);
		if(horse2RelevantEntity != null)		
			returnHashMap.put("horse2relevantentity",horse2RelevantEntity);
	}
	
	//获取玩家目标实体
	{
		PlayerAimsEntity playerAimsEntity = mainFactory.getSimpleEntityManager(PlayerAimsEntity.class).find(playerId);
		if(playerAimsEntity != null)		
			returnHashMap.put("playeraimsentity",playerAimsEntity);
	}
	
	//获取仙府
	{
		Cave cave = mainFactory.getSimpleEntityManager(Cave.class).find(player.getCaveId());
		if(cave != null)
		{	
			cave.setStatus(Cave.CAVE_STATUS_KHATAM);
			returnHashMap.put("cave",cave);
		}
		
	}
	
	//获取翅膀
	{
		
		WingPanel wingPanel = mainFactory.getSimpleEntityManager(WingPanel.class).find(playerId);
		if(wingPanel != null)
		{
			returnHashMap.put("wingpanel",wingPanel);
		}
	}
	
	
	//将articleid转成article 放入articleList 再放入returnList当中
	
	HashSet<Long> moreArticleIds = new HashSet<Long>();
	
	for(Long articleId : articleIds)
	{
		ArticleEntity  articleEntity = mainFactory.getSimpleEntityManager(ArticleEntity.class).find(articleId);

		

		if(articleEntity.getClass().equals(NewMagicWeaponEntity.class))
		{
			newMagicWeaponList.add((NewMagicWeaponEntity)articleEntity);
		}
		else if(articleEntity.getClass().equals(DigPropsEntity.class))
		{
			digPropsList.add((DigPropsEntity)articleEntity);
		}
		else if(articleEntity.getClass().equals(YinPiaoEntity.class))
		{
			yinPiaoList.add((YinPiaoEntity)articleEntity);
		}
		else if(articleEntity.getClass().equals(QiLingArticleEntity.class))
		{
			qiLingArticleList.add((QiLingArticleEntity)articleEntity);
		}
		else if(articleEntity.getClass().equals(SingleForPetPropsEntity.class))
		{
			singleForPetPropsList.add((SingleForPetPropsEntity)articleEntity);
		}
		else if(articleEntity.getClass().equals(ExploreResourceMapEntity.class))
		{
			exploreResourceMapList.add((ExploreResourceMapEntity)articleEntity);
		}
		else if(articleEntity.getClass().equals(Special_2EquipmentEntity.class))
		{
			special_2EquipmentList.add((Special_2EquipmentEntity)articleEntity);
		}
		else if(articleEntity.getClass().equals(Special_1EquipmentEntity.class))
		{
			special_1EquipmentList.add((Special_1EquipmentEntity)articleEntity);
		}
		else if(articleEntity.getClass().equals(PropsEntity.class))
		{
			propsEntityList.add((PropsEntity)articleEntity);
		}
		else if(articleEntity.getClass().equals(PetPropsEntity.class))
		{
			petPropsList.add((PetPropsEntity)articleEntity);
			
			PetPropsEntity petPropsEntity = (PetPropsEntity)articleEntity;
			if(petPropsEntity.getPetId() > 0)
			{
				Pet pet =  PetManager.getInstance().getPet(petPropsEntity.getPetId());
				if(pet != null)
				{
					if(returnHashMap.get("pets") != null)
					{
						List<Pet> pets = (List)returnHashMap.get("pets");
						boolean hasPet = false;
						for(Pet p : pets )
						{
							if(p.getId() == pet.getId())
							{
								hasPet = true;
								break;
							}
						}
						if(!hasPet)
						{
							pets.add(pet);
							returnHashMap.put("pets", pets);
						}
					}
				}
			}
			
		}
		else if(articleEntity.getClass().equals(PetEggPropsEntity.class))
		{
			petEggPropsList.add((PetEggPropsEntity)articleEntity);
			
			PetEggPropsEntity petPropsEntity = (PetEggPropsEntity)articleEntity;
			if(petPropsEntity.getPetId() > 0)
			{
				Pet pet =  PetManager.getInstance().getPet(petPropsEntity.getPetId());
				if(pet != null)
				{
					if(returnHashMap.get("pets") != null)
					{
						List<Pet> pets = (List)returnHashMap.get("pets");
						boolean hasPet = false;
						for(Pet p : pets )
						{
							if(p.getId() == pet.getId())
							{
								hasPet = true;
								break;
							}
						}
						if(!hasPet)
						{
							pets.add(pet);
							returnHashMap.put("pets", pets);
						}
					}
				}
			}
			
		}
		else if(articleEntity.getClass().equals(MagicWeaponEntity.class))
		{
			magicWeaponList.add((MagicWeaponEntity)articleEntity);
		}
		else if(articleEntity.getClass().equals(FateActivityPropsEntity.class))
		{
			fateActivityPropsList.add((FateActivityPropsEntity)articleEntity);
		}
		else if(articleEntity.getClass().equals(ExchangeArticleEntity.class))
		{
			exchangeArticleList.add((ExchangeArticleEntity)articleEntity);
		}
		else if(articleEntity.getClass().equals(BottlePropsEntity.class))
		{
			bottlePropsList.add((BottlePropsEntity)articleEntity);
		}
		else if(articleEntity.getClass().equals(BiWuArticleEntity.class))
		{
			biWuList.add((BiWuArticleEntity)articleEntity);
		}
		else if(articleEntity.getClass().equals(EquipmentEntity.class))
		{
			equipmentList.add((EquipmentEntity)articleEntity);
			
			EquipmentEntity equipmentEntity =	(EquipmentEntity)articleEntity;
			
			long[] inlayArticleIds = equipmentEntity.getInlayArticleIds();
			long[] inlayQiLingArticleIds  = equipmentEntity.getInlayQiLingArticleIds();
			
			if(inlayArticleIds.length > 0)
			{
				for(int i = 0; i < inlayArticleIds.length; i++)
				{
					moreArticleIds.add(inlayArticleIds[i]);
				}
				
			}
			
			if(inlayQiLingArticleIds.length > 0)
			{
				for(int i = 0; i < inlayQiLingArticleIds.length; i++)
				{
					moreArticleIds.add(inlayQiLingArticleIds[i]);
				}
				
			}
			
			
		}
		else if(articleEntity.getClass().equals(ArticleEntity.class))
		{
			articleList.add(articleEntity);
		}
		
	}
	
	
	
	for(Long articleId : moreArticleIds)
	{
		ArticleEntity  articleEntity = ArticleEntityManager.getInstance().getEntity(articleId);

		
		if(articleEntity != null)
		{
			if(articleEntity.getClass().equals(NewMagicWeaponEntity.class))
			{
				newMagicWeaponList.add((NewMagicWeaponEntity)articleEntity);
			}
			else if(articleEntity.getClass().equals(DigPropsEntity.class))
			{
				digPropsList.add((DigPropsEntity)articleEntity);
			}
			else if(articleEntity.getClass().equals(YinPiaoEntity.class))
			{
				yinPiaoList.add((YinPiaoEntity)articleEntity);
			}
			else if(articleEntity.getClass().equals(QiLingArticleEntity.class))
			{
				qiLingArticleList.add((QiLingArticleEntity)articleEntity);
			}
			else if(articleEntity.getClass().equals(SingleForPetPropsEntity.class))
			{
				singleForPetPropsList.add((SingleForPetPropsEntity)articleEntity);
			}
			else if(articleEntity.getClass().equals(ExploreResourceMapEntity.class))
			{
				exploreResourceMapList.add((ExploreResourceMapEntity)articleEntity);
			}
			else if(articleEntity.getClass().equals(Special_2EquipmentEntity.class))
			{
				special_2EquipmentList.add((Special_2EquipmentEntity)articleEntity);
			}
			else if(articleEntity.getClass().equals(Special_1EquipmentEntity.class))
			{
				special_1EquipmentList.add((Special_1EquipmentEntity)articleEntity);
			}
			else if(articleEntity.getClass().equals(PropsEntity.class))
			{
				propsEntityList.add((PropsEntity)articleEntity);
			}
			else if(articleEntity.getClass().equals(PetPropsEntity.class))
			{
				petPropsList.add((PetPropsEntity)articleEntity);
			}
			else if(articleEntity.getClass().equals(PetEggPropsEntity.class))
			{
				petEggPropsList.add((PetEggPropsEntity)articleEntity);
			}
			else if(articleEntity.getClass().equals(MagicWeaponEntity.class))
			{
				magicWeaponList.add((MagicWeaponEntity)articleEntity);
			}
			else if(articleEntity.getClass().equals(FateActivityPropsEntity.class))
			{
				fateActivityPropsList.add((FateActivityPropsEntity)articleEntity);
			}
			else if(articleEntity.getClass().equals(ExchangeArticleEntity.class))
			{
				exchangeArticleList.add((ExchangeArticleEntity)articleEntity);
			}
			else if(articleEntity.getClass().equals(BottlePropsEntity.class))
			{
				bottlePropsList.add((BottlePropsEntity)articleEntity);
			}
			else if(articleEntity.getClass().equals(BiWuArticleEntity.class))
			{
				biWuList.add((BiWuArticleEntity)articleEntity);
			}
			else if(articleEntity.getClass().equals(EquipmentEntity.class))
			{
				equipmentList.add((EquipmentEntity)articleEntity);
				
				EquipmentEntity equipmentEntity =	(EquipmentEntity)articleEntity;
				
				long[] inlayArticleIds = equipmentEntity.getInlayArticleIds();
				long[] inlayQiLingArticleIds  = equipmentEntity.getInlayQiLingArticleIds();
			}
			else if(articleEntity.getClass().equals(ArticleEntity.class))
			{
				articleList.add(articleEntity);
			}
		}
		
	}
	
	
	
	returnHashMap.put(NewMagicWeaponEntity.class.getName(),newMagicWeaponList);
	returnHashMap.put(DigPropsEntity.class.getName(),digPropsList);
	returnHashMap.put(YinPiaoEntity.class.getName(),yinPiaoList);
	returnHashMap.put(QiLingArticleEntity.class.getName(),qiLingArticleList);
	returnHashMap.put(SingleForPetPropsEntity.class.getName(),singleForPetPropsList);
	returnHashMap.put(ExploreResourceMapEntity.class.getName(),exploreResourceMapList);
	returnHashMap.put(Special_2EquipmentEntity.class.getName(),special_2EquipmentList);
	returnHashMap.put(Special_1EquipmentEntity.class.getName(),special_1EquipmentList);
	returnHashMap.put(PropsEntity.class.getName(),propsEntityList);
	returnHashMap.put(PetPropsEntity.class.getName(),petPropsList);
	returnHashMap.put(PetEggPropsEntity.class.getName(),petEggPropsList);
	returnHashMap.put(MagicWeaponEntity.class.getName(),magicWeaponList);
	returnHashMap.put(FateActivityPropsEntity.class.getName(),fateActivityPropsList);
	returnHashMap.put(ExchangeArticleEntity.class.getName(),exchangeArticleList);
	returnHashMap.put(BottlePropsEntity.class.getName(),bottlePropsList);
	returnHashMap.put(BiWuArticleEntity.class.getName(),biWuList);
	returnHashMap.put(EquipmentEntity.class.getName(),equipmentList);
	returnHashMap.put(ArticleEntity.class.getName(),articleList);
	
	
	return JsonUtil.jsonFromObject(returnHashMap);
}
else
{
	return null;
}
}
	
%>
<%!
public String reiceivePlayer(String jsonRes, DefaultSimpleEntityManagerFactory factory, boolean currentServer) throws Exception {
String json = jsonRes;
	

	StringBuffer tempBuffer = new StringBuffer();
	int incrementor = 0;
	int dataLength = json.length();
	while (incrementor < dataLength) {
	   char charecterAt = json.charAt(incrementor);
	   if (charecterAt == '%') {
	      tempBuffer.append("<percentage>");
	   } else if (charecterAt == '+') {
	      tempBuffer.append("<plus>");
	   } else {
	      tempBuffer.append(charecterAt);
	   }
	   incrementor++;
	}
	json = tempBuffer.toString();
	json = URLDecoder.decode(json, "utf-8");
	json = json.replaceAll("<percentage>", "%");
	json = json.replaceAll("<plus>", "+");
	

	if(!StringUtils.isEmpty(json))
	{
			
			Map<Long,Long> oldIdToNewIdMap = new HashMap<Long,Long>();
			Map<Object,Class<?>> o2class = new LinkedHashMap<Object,Class<?>>();
			
			 Map linkedHashMap =JsonUtil.objectFromJson(json, Map.class);
			 
			 
	
		
			
			Player player =  (Player)  convertToSpecialedType (linkedHashMap.get("player"),Player.class);
			
			SimpleEntityManager<Player> playerEntityManager = null;		
			SimpleEntityManager<ArticleEntity> articleEntityManager = null;
			SimpleEntityManager<QianCengTa_Ta> qiancengEntityManager = null;
			SimpleEntityManager<TaskEntity> taskEntityManager = null;
			SimpleEntityManager<NewDeliverTask> newdEntityManager = null;
			SimpleEntityManager<WingPanel> wingEntityManager = null;
			SimpleEntityManager<Horse> horseEntityManager = null;
			SimpleEntityManager<Pet> petEntityManager = null;
			SimpleEntityManager<Horse2RelevantEntity> horse2EntityManager = null;
			SimpleEntityManager<GameDataRecord> gdrEntityManager = null;			
			SimpleEntityManager<HotspotInfo> hpiEntityManager = null;		
			SimpleEntityManager<PetsOfPlayer> popEntityManager = null;		  
			SimpleEntityManager<PlayerActivenessInfo> paiEntityManager = null;		   
			SimpleEntityManager<SkBean> skEntityManager = null;		   
			SimpleEntityManager<TransitRobberyEntity> trEntityManager = null;		   
			SimpleEntityManager<BuffSave> bsEntityManager = null;		   
			SimpleEntityManager<PlayerAimsEntity> pamEntityManager = null;		   
			
			
			if (!currentServer) {
				playerEntityManager = factory.getSimpleEntityManager(Player.class);
				articleEntityManager = factory.getSimpleEntityManager(ArticleEntity.class);
				qiancengEntityManager = factory.getSimpleEntityManager(QianCengTa_Ta.class);
				taskEntityManager = factory.getSimpleEntityManager(TaskEntity.class);
				newdEntityManager = factory.getSimpleEntityManager(NewDeliverTask.class);
				wingEntityManager = factory.getSimpleEntityManager(WingPanel.class);
				horseEntityManager = factory.getSimpleEntityManager(Horse.class);
				petEntityManager = factory.getSimpleEntityManager(Pet.class);
				horse2EntityManager = factory.getSimpleEntityManager(Horse2RelevantEntity.class);
				gdrEntityManager = factory.getSimpleEntityManager(GameDataRecord.class);
				hpiEntityManager = factory.getSimpleEntityManager(HotspotInfo.class);
				popEntityManager = factory.getSimpleEntityManager(PetsOfPlayer.class);
				paiEntityManager = factory.getSimpleEntityManager(PlayerActivenessInfo.class);
				skEntityManager = factory.getSimpleEntityManager(SkBean.class);
				trEntityManager = factory.getSimpleEntityManager(TransitRobberyEntity.class);
				bsEntityManager = factory.getSimpleEntityManager(BuffSave.class);
				pamEntityManager = factory.getSimpleEntityManager(PlayerAimsEntity.class);
			} else {
				playerEntityManager = ((GamePlayerManager)GamePlayerManager.getInstance()).em;
				articleEntityManager = DefaultArticleEntityManager.getInstance().em;
				qiancengEntityManager = QianCengTaManager.getInstance().em;
				taskEntityManager = TaskEntityManager.em;
				newdEntityManager = NewDeliverTaskManager.getInstance().em;
				wingEntityManager = WingManager.em;
				horseEntityManager = HorseManager.em;
				petEntityManager = PetManager.em;
				horse2EntityManager = Horse2EntityManager.em;
				gdrEntityManager = AchievementManager.gameDREm;
				hpiEntityManager = HotspotManager.getInstance().em;
				popEntityManager = Pet2Manager.getInst().petsOfPlayerBeanEm;
				paiEntityManager = ActivenessManager.em;
				skEntityManager = SkEnhanceManager.em;
				trEntityManager = TransitRobberyEntityManager.em;
				bsEntityManager = BufferSaveManager.bem;
				pamEntityManager = PlayerAimeEntityManager.em;
			}
	
			if(player != null)
			{
				long oldId = player.getId();
				long newId = playerEntityManager.nextId();
				player.setId(newId);
				player.setCaveId(-1);
				player.setJiazuId(0);
				player.setJiazuName(null);
				player.setFaeryId(-1);
				try
				{
					long[] ids = playerEntityManager.queryIds(Player.class, "name=" + player.getName());
					if(ids != null && ids.length > 0)
					{
						//String serverName = GameConstants.getInstance().getServerName();
						player.setName(player.getName()+"@" + "步罡踏斗");
					}
				}
				catch(Exception e)
				{
					
				}
				
				Field versionField = UnitedServerManager2.getFieldByAnnotation(Player.class, SimpleVersion.class);
				versionField.setAccessible(true);
				versionField.set(player, 0);
				
				playerEntityManager.flush(player);
				ArticleEntityManager.log.warn("[角色转服] [原角色id:"+oldId+"] [新角色id:"+newId+"]");
				oldIdToNewIdMap.put(oldId, newId);
				
			}
			
			
			{
				if(linkedHashMap.get("qiancengta_ta") != null)
				{
					 QianCengTa_Ta qianCengTa_Ta = (QianCengTa_Ta)convertToSpecialedType( linkedHashMap.get("qiancengta_ta"),QianCengTa_Ta.class);
					 if(qianCengTa_Ta != null)
					 {
						
						 if(oldIdToNewIdMap.get(qianCengTa_Ta.getPlayerId()) != null)
						 {
							 long newQiancengTaId = oldIdToNewIdMap.get(qianCengTa_Ta.getPlayerId());
							 qianCengTa_Ta.setPlayerId(newQiancengTaId);
							 
							Field versionField = UnitedServerManager2.getFieldByAnnotation(QianCengTa_Ta.class, SimpleVersion.class);
							versionField.setAccessible(true);
							versionField.set(qianCengTa_Ta, 0);
							 
							qiancengEntityManager.flush(qianCengTa_Ta);
							 
						 }
						 
					 }
				}
			} 
			ArrayList<PetPropsEntity> morePetPropsEntitys = new ArrayList<PetPropsEntity>();
			ArrayList<PetEggPropsEntity> morePetEggPropsEntitys = new ArrayList<PetEggPropsEntity>();
			
			{
				
				{
					ArrayList<NewMagicWeaponEntity> articles = (ArrayList<NewMagicWeaponEntity>)convertToTypeReferenceType(linkedHashMap.get(NewMagicWeaponEntity.class.getName()), new TypeReference<ArrayList<NewMagicWeaponEntity>>(){});
					
					for(NewMagicWeaponEntity articleEntity : articles)
					{
						long newId = articleEntityManager.nextId();
						long oldId = articleEntity.getId();
						articleEntity.setId(newId);
						
						Field versionField = UnitedServerManager2.getFieldByAnnotation(ArticleEntity.class, SimpleVersion.class);
						versionField.setAccessible(true);
						versionField.set(articleEntity, 0);
						
						articleEntityManager.flush(articleEntity);
						
						ArticleEntityManager.log.warn("[角色转服] [原NewMagicWeaponEntityid:"+oldId+"] [新NewMagicWeaponEntityid:"+newId+"]");
						oldIdToNewIdMap.put(oldId, newId);
						
					}
				}
				{
					ArrayList<DigPropsEntity> articles = (ArrayList<DigPropsEntity>)convertToTypeReferenceType(linkedHashMap.get(DigPropsEntity.class.getName()), new TypeReference<ArrayList<DigPropsEntity>>(){});
					
					for(DigPropsEntity articleEntity : articles)
					{
						long newId = articleEntityManager.nextId();
						long oldId = articleEntity.getId();
						articleEntity.setId(newId);
						
						Field versionField = UnitedServerManager2.getFieldByAnnotation(ArticleEntity.class, SimpleVersion.class);
						versionField.setAccessible(true);
						versionField.set(articleEntity, 0);
						
						articleEntityManager.flush(articleEntity);
						
						ArticleEntityManager.log.warn("[角色转服] [原DigPropsEntity:"+oldId+"] [新DigPropsEntity:"+newId+"]");
						oldIdToNewIdMap.put(oldId, newId);
						
					}
				}
				{
					ArrayList<YinPiaoEntity> articles = (ArrayList<YinPiaoEntity>)convertToTypeReferenceType(linkedHashMap.get(YinPiaoEntity.class.getName()), new TypeReference<ArrayList<YinPiaoEntity>>(){});
					
					for(YinPiaoEntity articleEntity : articles)
					{
						long newId = articleEntityManager.nextId();
						long oldId = articleEntity.getId();
						articleEntity.setId(newId);
						
						Field versionField = UnitedServerManager2.getFieldByAnnotation(ArticleEntity.class, SimpleVersion.class);
						versionField.setAccessible(true);
						versionField.set(articleEntity, 0);
						
						articleEntityManager.flush(articleEntity);
						
						
						ArticleEntityManager.log.warn("[角色转服] [原YinPiaoEntity:"+oldId+"] [新YinPiaoEntity:"+newId+"]");
						oldIdToNewIdMap.put(oldId, newId);
						
					}
				}
				{
					ArrayList<QiLingArticleEntity> articles = (ArrayList<QiLingArticleEntity>)convertToTypeReferenceType(linkedHashMap.get(QiLingArticleEntity.class.getName()), new TypeReference<ArrayList<QiLingArticleEntity>>(){});
					
					for(QiLingArticleEntity articleEntity : articles)
					{
						long newId = articleEntityManager.nextId();
						long oldId = articleEntity.getId();
						articleEntity.setId(newId);
						
						Field versionField = UnitedServerManager2.getFieldByAnnotation(ArticleEntity.class, SimpleVersion.class);
						versionField.setAccessible(true);
						versionField.set(articleEntity, 0);
						
						articleEntityManager.flush(articleEntity);
						
						ArticleEntityManager.log.warn("[角色转服] [原QiLingArticleEntity:"+oldId+"] [新QiLingArticleEntity:"+newId+"]");
						oldIdToNewIdMap.put(oldId, newId);
						
					}
				}
				{
					ArrayList<SingleForPetPropsEntity> articles = (ArrayList<SingleForPetPropsEntity>)convertToTypeReferenceType(linkedHashMap.get(SingleForPetPropsEntity.class.getName()), new TypeReference<ArrayList<SingleForPetPropsEntity>>(){});
					
					for(SingleForPetPropsEntity articleEntity : articles)
					{
						long newId = articleEntityManager.nextId();
						long oldId = articleEntity.getId();
						articleEntity.setId(newId);
						
						Field versionField = UnitedServerManager2.getFieldByAnnotation(ArticleEntity.class, SimpleVersion.class);
						versionField.setAccessible(true);
						versionField.set(articleEntity, 0);
						
						articleEntityManager.flush(articleEntity);
						ArticleEntityManager.log.warn("[角色转服] [原SingleForPetPropsEntity:"+oldId+"] [新SingleForPetPropsEntity:"+newId+"]");
						oldIdToNewIdMap.put(oldId, newId);
						
					}
				}
				
				{
					ArrayList<ExploreResourceMapEntity> articles = (ArrayList<ExploreResourceMapEntity>)convertToTypeReferenceType(linkedHashMap.get(ExploreResourceMapEntity.class.getName()), new TypeReference<ArrayList<ExploreResourceMapEntity>>(){});
					
					for(ExploreResourceMapEntity articleEntity : articles)
					{
						long newId = articleEntityManager.nextId();
						long oldId = articleEntity.getId();
						articleEntity.setId(newId);
						
						Field versionField = UnitedServerManager2.getFieldByAnnotation(ArticleEntity.class, SimpleVersion.class);
						versionField.setAccessible(true);
						versionField.set(articleEntity, 0);
						
						articleEntityManager.flush(articleEntity);
						ArticleEntityManager.log.warn("[角色转服] [原ExploreResourceMapEntity:"+oldId+"] [新ExploreResourceMapEntity:"+newId+"]");
						oldIdToNewIdMap.put(oldId, newId);
						
					}
				}
				{
					ArrayList<Special_2EquipmentEntity> articles = (ArrayList<Special_2EquipmentEntity>)convertToTypeReferenceType(linkedHashMap.get(Special_2EquipmentEntity.class.getName()), new TypeReference<ArrayList<Special_2EquipmentEntity>>(){});
					
					for(Special_2EquipmentEntity articleEntity : articles)
					{
						long newId = articleEntityManager.nextId();
						long oldId = articleEntity.getId();
						articleEntity.setId(newId);
						
						Field versionField = UnitedServerManager2.getFieldByAnnotation(ArticleEntity.class, SimpleVersion.class);
						versionField.setAccessible(true);
						versionField.set(articleEntity, 0);
						
						articleEntity.setOwnerId(player.getId());
						
						articleEntityManager.flush(articleEntity);
						ArticleEntityManager.log.warn("[角色转服] [原Special_2EquipmentEntity:"+oldId+"] [新Special_2EquipmentEntity:"+newId+"]");
						oldIdToNewIdMap.put(oldId, newId);
						
					}
				}
				{
					ArrayList<Special_1EquipmentEntity> articles = (ArrayList<Special_1EquipmentEntity>)convertToTypeReferenceType(linkedHashMap.get(Special_1EquipmentEntity.class.getName()), new TypeReference<ArrayList<Special_1EquipmentEntity>>(){});
					
					for(Special_1EquipmentEntity articleEntity : articles)
					{
						long newId = articleEntityManager.nextId();
						long oldId = articleEntity.getId();
						articleEntity.setId(newId);
						
						Field versionField = UnitedServerManager2.getFieldByAnnotation(ArticleEntity.class, SimpleVersion.class);
						versionField.setAccessible(true);
						versionField.set(articleEntity, 0);
						
						articleEntity.setPlayerId(player.getId());
						articleEntityManager.flush(articleEntity);
						
						ArticleEntityManager.log.warn("[角色转服] [原Special_1EquipmentEntity:"+oldId+"] [新Special_1EquipmentEntity:"+newId+"]");
						oldIdToNewIdMap.put(oldId, newId);
						
					}
				}
				{
					ArrayList<PropsEntity> articles = (ArrayList<PropsEntity>)convertToTypeReferenceType(linkedHashMap.get(PropsEntity.class.getName()), new TypeReference<ArrayList<PropsEntity>>(){});
					
					for(PropsEntity articleEntity : articles)
					{
						long newId = articleEntityManager.nextId();
						long oldId = articleEntity.getId();
						articleEntity.setId(newId);
						
						Field versionField = UnitedServerManager2.getFieldByAnnotation(ArticleEntity.class, SimpleVersion.class);
						versionField.setAccessible(true);
						versionField.set(articleEntity, 0);
						
						articleEntityManager.flush(articleEntity);
						ArticleEntityManager.log.warn("[角色转服] [原PropsEntity:"+oldId+"] [新PropsEntity:"+newId+"]");
						oldIdToNewIdMap.put(oldId, newId);
						
					}
				}
				
				
				
				{
					ArrayList<PetPropsEntity> articles = (ArrayList<PetPropsEntity>)convertToTypeReferenceType(linkedHashMap.get(PetPropsEntity.class.getName()), new TypeReference<ArrayList<PetPropsEntity>>(){});
					
					for(PetPropsEntity articleEntity : articles)
					{
						long newId = articleEntityManager.nextId();
						long oldId = articleEntity.getId();
						articleEntity.setId(newId);
						
						
						Field versionField = UnitedServerManager2.getFieldByAnnotation(ArticleEntity.class, SimpleVersion.class);
						versionField.setAccessible(true);
						versionField.set(articleEntity, 0);
						
						articleEntityManager.flush(articleEntity);
						ArticleEntityManager.log.warn("[角色转服] [原PetPropsEntity:"+oldId+"] [新PetPropsEntity:"+newId+"]");
						oldIdToNewIdMap.put(oldId, newId);
						morePetPropsEntitys.add(articleEntity);
					}
				}
				
				
				{
					ArrayList<PetEggPropsEntity> articles = (ArrayList<PetEggPropsEntity>)convertToTypeReferenceType(linkedHashMap.get(PetEggPropsEntity.class.getName()), new TypeReference<ArrayList<PetEggPropsEntity>>(){});
					
					for(PetEggPropsEntity articleEntity : articles)
					{
						long newId = articleEntityManager.nextId();
						long oldId = articleEntity.getId();
						articleEntity.setId(newId);
						Field versionField = UnitedServerManager2.getFieldByAnnotation(ArticleEntity.class, SimpleVersion.class);
						versionField.setAccessible(true);
						versionField.set(articleEntity, 0);
						
						articleEntityManager.flush(articleEntity);
						ArticleEntityManager.log.warn("[角色转服] [原PetEggPropsEntity:"+oldId+"] [新PetEggPropsEntity:"+newId+"]");
						oldIdToNewIdMap.put(oldId, newId);
						morePetEggPropsEntitys.add(articleEntity);
					}
				}
				{
					ArrayList<MagicWeaponEntity> articles = (ArrayList<MagicWeaponEntity>)convertToTypeReferenceType(linkedHashMap.get(MagicWeaponEntity.class.getName()), new TypeReference<ArrayList<MagicWeaponEntity>>(){});
					
					for(MagicWeaponEntity articleEntity : articles)
					{
						long newId = articleEntityManager.nextId();
						long oldId = articleEntity.getId();
						articleEntity.setId(newId);
						
						Field versionField = UnitedServerManager2.getFieldByAnnotation(ArticleEntity.class, SimpleVersion.class);
						versionField.setAccessible(true);
						versionField.set(articleEntity, 0);
						
						articleEntityManager.flush(articleEntity);
						ArticleEntityManager.log.warn("[角色转服] [原MagicWeaponEntity:"+oldId+"] [新MagicWeaponEntity:"+newId+"]");
						oldIdToNewIdMap.put(oldId, newId);
						
					}
				}
				{
					ArrayList<FateActivityPropsEntity> articles = (ArrayList<FateActivityPropsEntity>)convertToTypeReferenceType(linkedHashMap.get(FateActivityPropsEntity.class.getName()), new TypeReference<ArrayList<FateActivityPropsEntity>>(){});
					
					for(FateActivityPropsEntity articleEntity : articles)
					{
						long newId = articleEntityManager.nextId();
						long oldId = articleEntity.getId();
						articleEntity.setId(newId);
						
						Field versionField = UnitedServerManager2.getFieldByAnnotation(ArticleEntity.class, SimpleVersion.class);
						versionField.setAccessible(true);
						versionField.set(articleEntity, 0);
						
						articleEntityManager.flush(articleEntity);
						ArticleEntityManager.log.warn("[角色转服] [原FateActivityPropsEntity:"+oldId+"] [新FateActivityPropsEntity:"+newId+"]");
						oldIdToNewIdMap.put(oldId, newId);
						
					}
				}
				
				ArrayList<ExchangeArticleEntity> exchangeArticleEntities = new ArrayList<ExchangeArticleEntity>();
				{
					ArrayList<ExchangeArticleEntity> articles = (ArrayList<ExchangeArticleEntity>)convertToTypeReferenceType(linkedHashMap.get(ExchangeArticleEntity.class.getName()), new TypeReference<ArrayList<ExchangeArticleEntity>>(){});
					
					for(ExchangeArticleEntity articleEntity : articles)
					{
						long newId = articleEntityManager.nextId();
						long oldId = articleEntity.getId();
						articleEntity.setId(newId);
						
						Field versionField = UnitedServerManager2.getFieldByAnnotation(ArticleEntity.class, SimpleVersion.class);
						versionField.setAccessible(true);
						versionField.set(articleEntity, 0);
						
						articleEntityManager.flush(articleEntity);
						oldIdToNewIdMap.put(oldId, newId);
						
						if(articleEntity.getTaskId() > 0)
						{
							exchangeArticleEntities.add(articleEntity);
						}
						
						ArticleEntityManager.log.warn("[角色转服] [原ExchangeArticleEntity:"+oldId+"] [新ExchangeArticleEntity:"+newId+"]");
						
					}
				}
				{
					ArrayList<BottlePropsEntity> articles = (ArrayList<BottlePropsEntity>)convertToTypeReferenceType(linkedHashMap.get(BottlePropsEntity.class.getName()), new TypeReference<ArrayList<BottlePropsEntity>>(){});
					
					for(BottlePropsEntity articleEntity : articles)
					{
						long newId = articleEntityManager.nextId();
						long oldId = articleEntity.getId();
						articleEntity.setId(newId);
						
						Field versionField = UnitedServerManager2.getFieldByAnnotation(ArticleEntity.class, SimpleVersion.class);
						versionField.setAccessible(true);
						versionField.set(articleEntity, 0);
						
						articleEntityManager.flush(articleEntity);
						oldIdToNewIdMap.put(oldId, newId);
						
						ArticleEntityManager.log.warn("[角色转服] [原BottlePropsEntity:"+oldId+"] [新BottlePropsEntity:"+newId+"]");
						
					}
				}
				{
				
					ArrayList<BiWuArticleEntity> articles = (ArrayList<BiWuArticleEntity>)convertToTypeReferenceType(linkedHashMap.get(BiWuArticleEntity.class.getName()), new TypeReference<ArrayList<BiWuArticleEntity>>(){});
					
					for(BiWuArticleEntity articleEntity : articles)
					{
						long newId = articleEntityManager.nextId();
						long oldId = articleEntity.getId();
						articleEntity.setId(newId);
						
						Field versionField = UnitedServerManager2.getFieldByAnnotation(ArticleEntity.class, SimpleVersion.class);
						versionField.setAccessible(true);
						versionField.set(articleEntity, 0);
						
						articleEntityManager.flush(articleEntity);
						oldIdToNewIdMap.put(oldId, newId);
						ArticleEntityManager.log.warn("[角色转服] [原BiWuArticleEntity:"+oldId+"] [新BiWuArticleEntity:"+newId+"]");
					}
				}
				
				{
					ArrayList<ArticleEntity> articles = (ArrayList<ArticleEntity>)convertToTypeReferenceType(linkedHashMap.get(ArticleEntity.class.getName()), new TypeReference<ArrayList<ArticleEntity>>(){});
					
					for(ArticleEntity articleEntity : articles)
					{
						long newId = articleEntityManager.nextId();
						long oldId = articleEntity.getId();
						articleEntity.setId(newId);
						
						Field versionField = UnitedServerManager2.getFieldByAnnotation(ArticleEntity.class, SimpleVersion.class);
						versionField.setAccessible(true);
						versionField.set(articleEntity, 0);
						
						articleEntityManager.flush(articleEntity);
						oldIdToNewIdMap.put(oldId, newId);
						ArticleEntityManager.log.warn("[角色转服] [原ArticleEntity:"+oldId+"] [新ArticleEntity:"+newId+"]");
					}
				}
				
				{
					ArrayList<EquipmentEntity> articles = (ArrayList<EquipmentEntity>)convertToTypeReferenceType(linkedHashMap.get(EquipmentEntity.class.getName()), new TypeReference<ArrayList<EquipmentEntity>>(){});
					
					for(EquipmentEntity articleEntity : articles)
					{
						long newId = articleEntityManager.nextId();
						long oldId = articleEntity.getId();
						articleEntity.setId(newId);
						
						long[] inlayArticleIds = articleEntity.getInlayArticleIds();
						long[] inlayQiLingArticleIds  = articleEntity.getInlayQiLingArticleIds();
						
						if(inlayArticleIds.length > 0)
						{
							for(int i = 0; i < inlayArticleIds.length; i++)
							{
								if(oldIdToNewIdMap.get(inlayArticleIds[i]) != null)
								{
									inlayArticleIds[i] = oldIdToNewIdMap.get(inlayArticleIds[i]);
								}
							}
							
							articleEntity.setInlayArticleIds(inlayArticleIds);
						}
						
						if(inlayQiLingArticleIds.length > 0)
						{
							for(int i = 0; i < inlayQiLingArticleIds.length; i++)
							{
								if(oldIdToNewIdMap.get(inlayQiLingArticleIds[i]) != null)
								{
									inlayQiLingArticleIds[i] = oldIdToNewIdMap.get(inlayQiLingArticleIds[i]);
								}
							}
							
							articleEntity.setInlayQiLingArticleIds(inlayQiLingArticleIds);
						}
						
						
						Field versionField = UnitedServerManager2.getFieldByAnnotation(ArticleEntity.class, SimpleVersion.class);
						versionField.setAccessible(true);
						versionField.set(articleEntity, 0);
						
						articleEntityManager.flush(articleEntity);
						oldIdToNewIdMap.put(oldId, newId);
						ArticleEntityManager.log.warn("[角色转服] [原EquipmentEntity:"+oldId+"] [新EquipmentEntity:"+newId+"]");
					}
				}
				
				//QianCengTaManager qianCengTaManager = QianCengTaManager.getInstance();
			
				QianCengTa_Ta qianCengTa_Ta = qiancengEntityManager.find(player.getId());    
			
				if(qianCengTa_Ta != null)
				{
					for (int i = 0; i < qianCengTa_Ta.getDaoList().size(); i++) {
						QianCengTa_Dao dao = qianCengTa_Ta.getDaoList().get(i);
						for (int j = 0; j < dao.getCengList().size(); j++) {
							QianCengTa_Ceng ceng = dao.getCengList().get(j);
							for (int k = 0; k < ceng.getRewards().size(); k++) {
								if (ceng.getRewards().get(k).getEntityId() > 0) {
									if(oldIdToNewIdMap.get(ceng.getRewards().get(k).getEntityId())!=null)
									{
										long newId = oldIdToNewIdMap.get(ceng.getRewards().get(k).getEntityId());
										ceng.getRewards().get(k).setEntityId(newId);
									}
								}
							}
						}
					}
					
					factory.getSimpleEntityManager(QianCengTa_Ta.class).flush(qianCengTa_Ta);
				}
			
				if (player.getKnapsack_fangBao_Id() > 0) {
					if(oldIdToNewIdMap.get(player.getKnapsack_fangBao_Id())!=null)
					{
						long newId = oldIdToNewIdMap.get(player.getKnapsack_fangBao_Id());
						player.setKnapsack_fangBao_Id(newId);
					}
				}
				if (player.getKnapsack_fangbao() != null) {
					for (int i = 0; i < player.getKnapsack_fangbao().size(); i++) {
						try {
							if (player.getKnapsack_fangbao().getCell(i) != null) {
								if (player.getKnapsack_fangbao().getCell(i)
										.getEntityId() > 0) {
									if(oldIdToNewIdMap.get(player.getKnapsack_fangbao().getCell(i).getEntityId())!=null)
									{
										long newId = oldIdToNewIdMap.get(player.getKnapsack_fangbao().getCell(i).getEntityId());
										player.getKnapsack_fangbao().getCell(i).setEntityId(newId);
									}
									
								}
							}
						} catch (Exception e) {
						}
					}
				}
				if (player.getKnapsack_common() != null) {
					for (int i = 0; i < player.getKnapsack_common().size(); i++) {
						try {
							if (player.getKnapsack_common().getCell(i) != null) {
								if (player.getKnapsack_common().getCell(i)
										.getEntityId() > 0) {
									if(oldIdToNewIdMap.get(player.getKnapsack_common().getCell(i)
											.getEntityId())!=null)
									{
										long newId = oldIdToNewIdMap.get(player.getKnapsack_common().getCell(i)
												.getEntityId());
										player.getKnapsack_common().getCell(i).setEntityId(newId);
									}
									
								}
							}
						} catch (Exception e) {
						}
					}
				}
				if (player.getPetKnapsack() != null) {
					for (int i = 0; i < player.getPetKnapsack().size(); i++) {
						try {
							if (player.getPetKnapsack().getCell(i) != null) {
								if (player.getPetKnapsack().getCell(i)
										.getEntityId() > 0) {
									if(oldIdToNewIdMap.get(player.getPetKnapsack().getCell(i)
											.getEntityId())!=null)
									{
										long newId = oldIdToNewIdMap.get(player.getPetKnapsack().getCell(i)
												.getEntityId());
										player.getPetKnapsack().getCell(i).setEntityId(newId);
									}
									
								}
							}
						} catch (Exception e) {
						}
					}
				}
				
				
				
				if (player.getKnapsacks_cangku() != null) {
					for (int i = 0; i < player.getKnapsacks_cangku().size(); i++) {
						try {
							if (player.getKnapsacks_cangku().getCell(i) != null) {
								if (player.getKnapsacks_cangku().getCell(i)
										.getEntityId() > 0) {
									if(oldIdToNewIdMap.get(player.getKnapsacks_cangku().getCell(i)
											.getEntityId())!=null)
									{
										long newId = oldIdToNewIdMap.get(player.getKnapsacks_cangku().getCell(i)
												.getEntityId());
										player.getKnapsacks_cangku().getCell(i).setEntityId(newId);
									}
								}
							}
						} catch (Exception e) {
						}
					}
				}
				if (player.getSouls() != null) {
					for (int i = 0; i < player.getSouls().length; i++) {
						try {
							Soul soul = player.getSouls()[i];
							if (soul == null || soul.getEc() == null) {
								continue;
							}
							for (int j = 0; j < soul.getEc()
									.getEquipmentIds().length; j++) {
								if (soul.getEc().getEquipmentIds()[j] > 0) {
									if(oldIdToNewIdMap.get(soul.getEc().getEquipmentIds()[j])!=null)
									{
										
										
										long newId = oldIdToNewIdMap.get(soul.getEc().getEquipmentIds()[j]);
										soul.getEc().getEquipmentIds()[j] = newId;
									}
								}
							}
						} catch (Exception e) {
						}
					}
				}
				if (player.getKnapsacks_QiLing() != null) {
					for (int i = 0; i < player.getKnapsacks_QiLing().size(); i++) {
						try {
							if (player.getKnapsacks_QiLing().getCell(i) != null) {
								if (player.getKnapsacks_QiLing().getCell(i)
										.getEntityId() > 0) {
									if(oldIdToNewIdMap.get(player.getKnapsacks_QiLing().getCell(i)
											.getEntityId())!=null)
									{
										long newId = oldIdToNewIdMap.get(player.getKnapsacks_QiLing().getCell(i)
												.getEntityId());
										player.getKnapsacks_QiLing().getCell(i)
										.setEntityId(newId);
									}
									
								}
							}
						} catch (Exception e) {
						}
					}
				}
				

				
				
				playerEntityManager.flush(player);
					
			}
			
			/* {
				if(linkedHashMap.get("delivertasks") != null)
				{
					List<DeliverTask> deliverTasks  = (List<DeliverTask>)convertToTypeReferenceType(linkedHashMap.get("delivertasks"), new TypeReference<List<DeliverTask>>(){});
					for(DeliverTask deliverTask : deliverTasks)
					{
						SimpleEntityManager entityManager = DeliverTaskManager.getInstance().em;
						long newId = entityManager.nextId();
						long oldId = deliverTask.getId();
						deliverTask.setId(newId);
						deliverTask.setPlayerId(player.getId());
						
						Field versionField = UnitedServerManager2.getFieldByAnnotation(DeliverTask.class, SimpleVersion.class);
						versionField.setAccessible(true);
						versionField.set(deliverTask, 0);
						
						entityManager.flush(deliverTask);
						oldIdToNewIdMap.put(oldId, newId);
						
						DeliverTaskManager.logger.warn("[角色转服] [原DeliverTask:"+oldId+"] [新DeliverTask:"+newId+"]");
						
					}
				}
			} */
		
			{
				if(linkedHashMap.get("taskentitys") != null)
				{
					List<TaskEntity> taskEntitys  = (List<TaskEntity>)convertToTypeReferenceType(linkedHashMap.get("taskentitys"), new TypeReference<List<TaskEntity>>(){});
					for(TaskEntity taskEntity : taskEntitys)
					{
						long newId = taskEntityManager.nextId();
						long oldId = taskEntity.getId();
						taskEntity.setId(newId);
						taskEntity.setPlayerId(player.getId());
						
						Field versionField = UnitedServerManager2.getFieldByAnnotation(TaskEntity.class, SimpleVersion.class);
						versionField.setAccessible(true);
						versionField.set(taskEntity, 0);
						
						taskEntityManager.flush(taskEntity);
						oldIdToNewIdMap.put(oldId, newId);
						ArticleEntityManager.log.warn("[角色转服] [原TaskEntity:"+oldId+"] [新TaskEntity:"+newId+"]");
					}
				}
			}
				
			{
				if(linkedHashMap.get("playerrecords") != null)
				{
					HashMap<Integer, GameDataRecord> playerRecords = (HashMap<Integer, GameDataRecord>)convertToTypeReferenceType(linkedHashMap.get("playerrecords"), new TypeReference<HashMap<Integer, GameDataRecord>>(){});
					Collection<GameDataRecord> gameDataRecords = playerRecords.values();
					for(GameDataRecord gameDataRecord : gameDataRecords)
					{
						long newId = gdrEntityManager.nextId();
						long oldId = gameDataRecord.getId();
						gameDataRecord.setId(newId);
						gameDataRecord.setPlayerId(player.getId());
						
						
						Field versionField = UnitedServerManager2.getFieldByAnnotation(GameDataRecord.class, SimpleVersion.class);
						versionField.setAccessible(true);
						versionField.set(gameDataRecord, 0);
						
						gdrEntityManager.flush(gameDataRecord);
						oldIdToNewIdMap.put(oldId, newId);
						ArticleEntityManager.log.warn("[角色转服] [原GameDataRecord:"+oldId+"] [新GameDataRecord:"+newId+"]");
					}
					
				}
			}
			
			/* {
				if(linkedHashMap.get("achievements") != null)
				{
					HashMap<Integer, AchievementEntity>  achievementMap = (HashMap<Integer, AchievementEntity> )convertToTypeReferenceType(linkedHashMap.get("achievements"), new TypeReference<HashMap<Integer, AchievementEntity>  >(){});
					Collection<AchievementEntity> achievementEntitys = achievementMap.values();
					for(AchievementEntity achievementEntity : achievementEntitys)
					{
						SimpleEntityManager entityManager = AchievementManager.getInstance().aeEm;
						long newId = entityManager.nextId();
						long oldId = achievementEntity.getId();
						achievementEntity.setId(newId);
						achievementEntity.setPlayerId(player.getId());
						
						Field versionField = UnitedServerManager2.getFieldByAnnotation(AchievementEntity.class, SimpleVersion.class);
						versionField.setAccessible(true);
						versionField.set(achievementEntity, 0);
						
						entityManager.flush(achievementEntity);
						oldIdToNewIdMap.put(oldId, newId);
						AchievementManager.logger.warn("[角色转服] [原AchievementEntity:"+oldId+"] [新AchievementEntity:"+newId+"]");
					}
				}
			} */
			
			
			{
				if(linkedHashMap.get("hotspotinfos") != null)
				{
					List<HotspotInfo>  list = (List<HotspotInfo> )convertToTypeReferenceType(linkedHashMap.get("hotspotinfos"), new TypeReference<List<HotspotInfo>  >(){});
					for(HotspotInfo hotspotInfo : list)
					{
						long newId = hpiEntityManager.nextId();
						long oldId = hotspotInfo.getId();
						hotspotInfo.setId(newId);
						hotspotInfo.setPlayerID(player.getId());
						
						Field versionField = UnitedServerManager2.getFieldByAnnotation(HotspotInfo.class, SimpleVersion.class);
						versionField.setAccessible(true);
						versionField.set(hotspotInfo, 0);
						
						hpiEntityManager.flush(hotspotInfo);
						oldIdToNewIdMap.put(oldId, newId);
						
						ArticleEntityManager.log.warn("[角色转服] [原HotspotInfo:"+oldId+"] [新HotspotInfo:"+newId+"]");
						
					}
				}
			}
			
			{
				if(linkedHashMap.get("horses") != null)
				{
					ArrayList<Horse>  list = (ArrayList<Horse> )convertToTypeReferenceType(linkedHashMap.get("horses"), new TypeReference<ArrayList<Horse> >(){});
					for(Horse horse : list)
					{
						long newId = horseEntityManager.nextId();
						long oldId = horse.getHorseId();
						horse.setHorseId(newId);
						horse.setOwnerId(player.getId());
						
						long[] horseEquIDs = horse.getEquipmentColumn().getEquipmentIds();
						long[] newHorseEquIDs = new long[horseEquIDs.length];
						
						for(int i=0; i < horseEquIDs.length; i++)
						{
							long id = horseEquIDs[i];
							if(oldIdToNewIdMap.get(id) != null)
							{
								newHorseEquIDs[i] = oldIdToNewIdMap.get(id);
							}
						}
						
						horse.getEquipmentColumn().setEquipmentIds(newHorseEquIDs);
						
						
						Field versionField = UnitedServerManager2.getFieldByAnnotation(Horse.class, SimpleVersion.class);
						versionField.setAccessible(true);
						versionField.set(horse, 0);
						
						horseEntityManager.flush(horse);
						oldIdToNewIdMap.put(oldId, newId);
						
						ArticleEntityManager.log.warn("[角色转服] [原Horse:"+oldId+"] [新Horse:"+newId+"]");
					}
					

					for (int i = 0; i < player.getSouls().length; i++) {
						try {
							Soul soul = player.getSouls()[i];
							if (soul == null ) {
								continue;
							}
							
							ArrayList<Long> horseIdList =  soul.getHorseArr();
							
							for (int j = 0; j <horseIdList.size(); j++) {
								if(oldIdToNewIdMap.get(horseIdList.get(j))!=null)
								{
									long newId = oldIdToNewIdMap.get(horseIdList.get(j));
									horseIdList.set(j, newId);
								}
							}
						} catch (Exception e) {
						}
					}
					playerEntityManager.flush(player);
				}
			}
			
			try{
				if(linkedHashMap.get("pets") != null)
				{
					List<Pet>  list = (List<Pet> )convertToTypeReferenceType(linkedHashMap.get("pets"), new TypeReference<List<Pet> >(){});
					for(Pet pet : list)
					{
						long newId = petEntityManager.nextId();
						long oldId = pet.getId();
						pet.setId(newId);
						
						if(pet.getOwnerId() > 0)
							pet.setOwnerId(player.getId());
						
						long oldPropsId = pet.getPetPropsId();
						long curPropsId =  0;
						
						if(oldIdToNewIdMap.get(oldPropsId) != null)
						{
							curPropsId = oldIdToNewIdMap.get(oldPropsId);
						}
						
						if(curPropsId > 0)
						{
							PropsEntity petPropsEntity = (PropsEntity)factory.getSimpleEntityManager(ArticleEntity.class).find(curPropsId);
							
							if(petPropsEntity != null)
							{
								if(petPropsEntity instanceof PetEggPropsEntity)
								{
									PetEggPropsEntity eggPropsEntity =  (PetEggPropsEntity) petPropsEntity;
									eggPropsEntity.setPetId(newId);
									ArticleEntityManager.getInstance().em.flush(eggPropsEntity);
									pet.setPetPropsId(eggPropsEntity.getId());
								}
								else if(petPropsEntity instanceof PetPropsEntity)
								{
									PetPropsEntity propsEntity =  	(PetPropsEntity) petPropsEntity;
									propsEntity.setPetId(newId);
									ArticleEntityManager.getInstance().em.flush(propsEntity);
									pet.setPetPropsId(propsEntity.getId());
								}
								
								
							}
						}
						
						Field versionField = UnitedServerManager2.getFieldByAnnotation(Pet.class, SimpleVersion.class);
						versionField.setAccessible(true);
						versionField.set(pet, 0);
						
						petEntityManager.flush(pet);
						oldIdToNewIdMap.put(oldId, newId);
						ArticleEntityManager.log.warn("[角色转服] [原Pet:"+oldId+"] [新Pet:"+newId+"]");
					}
				
					
					
				}
				
				
				try{
					for(PetPropsEntity articleEntity : morePetPropsEntitys)
					{
						if(articleEntity.getPetId() > 0)
						{
							if(oldIdToNewIdMap.get(articleEntity.getPetId()) != null)
							{
								long oldId = articleEntity.getPetId();
								long newId = oldIdToNewIdMap.get(oldId);
								articleEntity.setPetId(newId);
								
								ArticleEntityManager.log.warn("[角色转服] [设置petid] [id:"+articleEntity.getId()+"] [oldid:"+oldId+"] [newId:"+newId+"]");
							}
						}
						articleEntityManager.flush(articleEntity);
					}
				} catch(Exception e) {
					ArticleEntityManager.log.warn("[角色转服] [设置petid] [异常]", e);
				}
				
				
				{
					for(PetEggPropsEntity articleEntity : morePetEggPropsEntitys)
					{
						if(articleEntity.getPetId() > 0)
						{
							if(oldIdToNewIdMap.get(articleEntity.getPetId()) != null)
							{
								long oldId = articleEntity.getPetId();
								long newId = oldIdToNewIdMap.get(oldId);
								articleEntity.setPetId(newId);
								ArticleEntityManager.log.warn("[角色转服] [设置petid] [id:"+articleEntity.getId()+"] [oldid:"+oldId+"] [newId:"+newId+"]");
							}
						}
						articleEntityManager.flush(articleEntity);
					}
				} 
				
				
			} catch (Exception e) {
				ArticleEntityManager.log.warn("[角色转服] [pets] [异常]", e);
			}
			
			try{
				if(linkedHashMap.get("petsofplayer") != null)
				{
					PetsOfPlayer petsOfPlayer = (PetsOfPlayer)convertToSpecialedType( linkedHashMap.get("petsofplayer"),PetsOfPlayer.class);
					 if(petsOfPlayer != null)
					 {
						
						 if(oldIdToNewIdMap.get(petsOfPlayer.getPid()) != null)
						 {
							 long newPid = oldIdToNewIdMap.get(petsOfPlayer.getPid());
							 petsOfPlayer.setPid(newPid);
							 
							Field versionField = UnitedServerManager2.getFieldByAnnotation(PetsOfPlayer.class, SimpleVersion.class);
							versionField.setAccessible(true);
							versionField.set(petsOfPlayer, 0);
							 
							popEntityManager.flush(petsOfPlayer);
							 
						 }
						 
					 }
				}
			} catch (Exception e) {
				ArticleEntityManager.log.warn("[角色转服] [petsofplayer] [异常]", e);
			}
			
			try{
				if(linkedHashMap.get("playeractivenessinfo") != null)
				{
					PlayerActivenessInfo playerActivenessInfo = (PlayerActivenessInfo)convertToSpecialedType( linkedHashMap.get("playeractivenessinfo"),PlayerActivenessInfo.class);
					
					 if(oldIdToNewIdMap.get(playerActivenessInfo.getId()) != null)
					 {
						 long newPid = oldIdToNewIdMap.get(playerActivenessInfo.getId());
						 playerActivenessInfo.setId(newPid);
						 
						Field versionField = UnitedServerManager2.getFieldByAnnotation(PlayerActivenessInfo.class, SimpleVersion.class);
						versionField.setAccessible(true);
						versionField.set(playerActivenessInfo, 0);
						 
						paiEntityManager.flush(playerActivenessInfo);
						 
					 }
				}
			} catch (Exception e) {
				ArticleEntityManager.log.warn("[角色转服] [playeractivenessinfo] [异常]", e);
			}
			
			/* {
				if(linkedHashMap.get("minigameentity") != null)
				{
					MiniGameEntity miniGameEntity = (MiniGameEntity)convertToSpecialedType( linkedHashMap.get("minigameentity"),MiniGameEntity.class);
					 if(oldIdToNewIdMap.get(miniGameEntity.getId()) != null)
					 {
						 long newPid = oldIdToNewIdMap.get(miniGameEntity.getId());
						 miniGameEntity.setId(newPid);
						 
						Field versionField = UnitedServerManager2.getFieldByAnnotation(MiniGameEntity.class, SimpleVersion.class);
						versionField.setAccessible(true);
						versionField.set(miniGameEntity, 0);
						 
						 MiniGameEntityManager.getInstance().em.flush(miniGameEntity);
						 
					 }
				}
			} */ 
			
			/* {
				if(linkedHashMap.get("articleprotectdatas") != null)
				{
					List<ArticleProtectData> list = (List<ArticleProtectData> )convertToTypeReferenceType(linkedHashMap.get("articleprotectdatas"), new TypeReference<List<ArticleProtectData>  >(){});
					for(ArticleProtectData articleProtectData : list)
					{
						SimpleEntityManager<ArticleProtectData> entityManager = factory.getSimpleEntityManager(ArticleProtectData.class);
						long newId = entityManager.nextId();
						long oldId = articleProtectData.getId();
						articleProtectData.setId(newId);
						articleProtectData.setPlayerID(player.getId());
						
						Field versionField = UnitedServerManager2.getFieldByAnnotation(ArticleProtectData.class, SimpleVersion.class);
						versionField.setAccessible(true);
						versionField.set(articleProtectData, 0);
						
						entityManager.flush(articleProtectData);
						oldIdToNewIdMap.put(oldId, newId);
						ArticleEntityManager.log.warn("[角色转服] [原ArticleProtectData:"+oldId+"] [新ArticleProtectData:"+newId+"]");
					}
				}
			} */
			
			try{
				if(linkedHashMap.get("skbean") != null)
				{
					SkBean skBean = (SkBean)convertToSpecialedType( linkedHashMap.get("skbean"),SkBean.class);
					 if(oldIdToNewIdMap.get(skBean.getPid()) != null)
					 {
						 long newPid = oldIdToNewIdMap.get(skBean.getPid());
						 skBean.setPid(newPid);
						 
						Field versionField = UnitedServerManager2.getFieldByAnnotation(SkBean.class, SimpleVersion.class);
						versionField.setAccessible(true);
						versionField.set(skBean, 0);
						 
						skEntityManager.flush(skBean);
					 }
				}
			} catch (Exception e) {
				ArticleEntityManager.log.warn("[角色转服] [skbean] [异常]", e);
			}
			
			try{
				if(linkedHashMap.get("transitrobberyentity") != null)
				{
					TransitRobberyEntity transitRobberyEntity = (TransitRobberyEntity)convertToSpecialedType( linkedHashMap.get("transitrobberyentity"),TransitRobberyEntity.class);
					 if(oldIdToNewIdMap.get(transitRobberyEntity.getId()) != null)
					 {
						 long newPid = oldIdToNewIdMap.get(transitRobberyEntity.getId());
						 transitRobberyEntity.setId(newPid);
						 
						Field versionField = UnitedServerManager2.getFieldByAnnotation(TransitRobberyEntity.class, SimpleVersion.class);
						versionField.setAccessible(true);
						versionField.set(transitRobberyEntity, 0);
						 
						trEntityManager.flush(transitRobberyEntity);
						 
					 }
				}
			} catch (Exception e) {
				ArticleEntityManager.log.warn("[角色转服] [transitrobberyentity] [异常]", e);
			}
			
			try{
				if(linkedHashMap.get("buffsaves") != null)
				{
					List<BuffSave> list = (List<BuffSave> )convertToTypeReferenceType(linkedHashMap.get("buffsaves"), new TypeReference<List<BuffSave>  >(){});
					for(BuffSave buffSave : list)
					{
						long newId = bsEntityManager.nextId();
						long oldId = buffSave.getSaveID();
						buffSave.setSaveID(newId);
						buffSave.setPid(player.getId());
						
						Field versionField = UnitedServerManager2.getFieldByAnnotation(BuffSave.class, SimpleVersion.class);
						versionField.setAccessible(true);
						versionField.set(buffSave, 0);
						
						bsEntityManager.flush(buffSave);
						oldIdToNewIdMap.put(oldId, newId);
						ArticleEntityManager.log.warn("[角色转服] [原BuffSave:"+oldId+"] [新BuffSave:"+newId+"]");
					}
				}
			} catch (Exception e) {
				ArticleEntityManager.log.warn("[角色转服] [buffsaves] [异常]", e);
			}
			
			try{
				if(linkedHashMap.get("newdelivertasks") != null)
				{
					List<NewDeliverTask> newDeliverTasks = (List<NewDeliverTask> )convertToTypeReferenceType(linkedHashMap.get("newdelivertasks"), new TypeReference<List<NewDeliverTask>>(){});
					for(NewDeliverTask newDeliverTask : newDeliverTasks)
					{
						
						long oldId = newDeliverTask.getId();
						NewDeliverTask newDeliverTask2 = new NewDeliverTask(player.getId(),newDeliverTask.getTaskId());
						long newId = newDeliverTask2.getId();
						
						Field versionField = UnitedServerManager2.getFieldByAnnotation(NewDeliverTask.class, SimpleVersion.class);
						versionField.setAccessible(true);
						versionField.set(newDeliverTask2, 0);
						
						newdEntityManager.flush(newDeliverTask2);
						oldIdToNewIdMap.put(oldId, newId);
						ArticleEntityManager.log.warn("[角色转服] [原NewDeliverTask:"+oldId+"] [新NewDeliverTask:"+newId+"]");
					}
				}
			}catch(Exception e) {
				ArticleEntityManager.log.warn("[角色转服] [newdelivertasks] [异常]", e);
			}
			
			try{
				if(linkedHashMap.get("horse2relevantentity") != null)
				{
					Horse2RelevantEntity  horse2RelevantEntity = (Horse2RelevantEntity)convertToSpecialedType( linkedHashMap.get("horse2relevantentity"),Horse2RelevantEntity.class);
					 if(oldIdToNewIdMap.get(horse2RelevantEntity.getId()) != null)
					 {
						 long newPid = oldIdToNewIdMap.get(horse2RelevantEntity.getId());
						 horse2RelevantEntity.setId(newPid);
						 
						Field versionField = UnitedServerManager2.getFieldByAnnotation(Horse2RelevantEntity.class, SimpleVersion.class);
						versionField.setAccessible(true);
						versionField.set(horse2RelevantEntity, 0);
						 
						horse2EntityManager.flush(horse2RelevantEntity);
					 }
				}
			} catch (Exception e) {
				ArticleEntityManager.log.warn("[角色转服] [horse2relevantentity] [异常]", e);
			}
			//到这儿
			try{
				if(linkedHashMap.get("playeraimsentity") != null)
				{
					PlayerAimsEntity playerAimsEntity = (PlayerAimsEntity)convertToSpecialedType( linkedHashMap.get("playeraimsentity"),PlayerAimsEntity.class);
					 if(oldIdToNewIdMap.get(playerAimsEntity.getId()) != null)
					 {
						 long newPid = oldIdToNewIdMap.get(playerAimsEntity.getId());
						 playerAimsEntity.setId(newPid);
						 
						Field versionField = UnitedServerManager2.getFieldByAnnotation(PlayerAimsEntity.class, SimpleVersion.class);
						versionField.setAccessible(true);
						versionField.set(playerAimsEntity, 0);
						 
						pamEntityManager.flush(playerAimsEntity);
						 
					 }
				}
			} catch (Exception e) {
				ArticleEntityManager.log.warn("[角色转服] [playeraimsentity] [异常]", e);
			}
			

			
			try{
				if(linkedHashMap.get("wingpanel") != null)
				{
				
					WingPanel wp = (WingPanel)convertToSpecialedType( linkedHashMap.get("wingpanel"),WingPanel.class);
					if(wp != null)
					{
						long brightInlayId = wp.getBrightInlayId();
						
						
						if(brightInlayId > 0){
							long newId = oldIdToNewIdMap.get(brightInlayId);
							
							if(newId > 0)
								wp.setBrightInlayId(newId);
						}
						long[] inlayArticleIds = wp.getInlayArticleIds();
						long[] newInlayArticleIds = new long[inlayArticleIds.length];
						if(inlayArticleIds != null){
							for(int i=0;i < inlayArticleIds.length;i++){
								if(inlayArticleIds[i] > 0){
									long newId = oldIdToNewIdMap.get(inlayArticleIds[i]);
									if(newId > 0)
										newInlayArticleIds[i] = newId;
										
								}
							}
						}
						
						wp.setInlayArticleIds(newInlayArticleIds);
						
						long newWpId = player.getId();
						wp.setId(newWpId);
						
						Field versionField = UnitedServerManager2.getFieldByAnnotation(WingPanel.class, SimpleVersion.class);
						versionField.setAccessible(true);
						versionField.set(wp, 0);
						
						wingEntityManager.flush(wp);
					}
					
				}
			}catch (Exception e) {
				ArticleEntityManager.log.warn("[角色转服] [wingpanel] [异常]", e);
			}
			
			
			
			
			/* String username = ParamUtils.getParameter(request, "username", "");
			if(!StringUtils.isEmpty(username))
			{
				player.setUsername(username);
			}
			
			((GamePlayerManager) GamePlayerManager.getInstance()).em.flush(player);
			
			out.print("success"); */
			return "sucess";
	}
	else
	{
		
		return null;
	}
}
public static Object convertToSpecialedType(Object o, Class t) throws Exception
{
	 return JsonUtil.objectFromJson( JsonUtil.jsonFromObject(o),t);
}

public static <T> T convertToTypeReferenceType(Object o, TypeReference<T> t) throws Exception
{
	 return JsonUtil.objectFromJson( JsonUtil.jsonFromObject(o),t);
}

%>
