<%@page import="com.fy.engineserver.datasource.article.data.entity.HunshiEntity"%>
<%@page import="com.fy.engineserver.soulpith.SoulPithEntityManager"%>
<%@page import="com.fy.engineserver.soulpith.instance.SoulPithEntity"%>
<%@page import="com.fy.engineserver.soulpith.instance.SoulPithAeData"%>
<%@page import="com.fy.engineserver.datasource.article.data.entity.SoulPithArticleEntity"%>
<%@page import="java.io.OutputStream"%>
<%@page import="java.net.URLEncoder"%>
<%@page import="com.xuanzhi.boss.game.GameConstants"%>
<%@page import="com.fy.engineserver.activity.TransitRobbery.TransitRobberyManager"%>
<%@page import="com.fy.engineserver.sprite.pet.PetFlyState"%>
<%@page
	import="com.fy.engineserver.activity.fairyRobbery.FairyRobberyEntityManager"%>
<%@page
	import="com.fy.engineserver.activity.fairyRobbery.instance.FairyRobberyEntity"%>
<%@page import="com.fy.engineserver.talent.FlyTalentManager"%>
<%@page import="com.fy.engineserver.talent.TalentData"%>
<%@page
	import="com.fy.engineserver.datasource.article.data.articles.InlayArticle"%>
<%@page
	import="com.fy.engineserver.datasource.article.manager.ArticleManager"%>
<%@page
	import="com.fy.engineserver.datasource.article.data.articles.Article"%>
<%@page
	import="com.fy.engineserver.datasource.article.data.entity.BaoShiXiaZiData"%>
<%@page
	import="com.fy.engineserver.datasource.article.data.magicweapon.huntLife.instance.HuntArticleExtraData"%>
<%@page
	import="com.fy.engineserver.articleEnchant.EnchantEntityManager"%>
<%@page import="com.fy.engineserver.articleEnchant.EnchantData"%>
<%@page
	import="com.fy.engineserver.datasource.article.concrete.DefaultArticleEntityManager"%>
<%@page
	import="com.fy.engineserver.datasource.article.data.magicweapon.huntLife.HuntLifeEntityManager"%>
<%@page
	import="com.fy.engineserver.datasource.article.data.magicweapon.huntLife.instance.HuntLifeEntity"%>
<%@page
	import="com.fy.engineserver.datasource.article.data.entity.HuntLifeArticleEntity"%>
<%@page import="com.xuanzhi.tools.simplejpa.SimpleEntityManager"%>
<%@page import="java.util.Iterator"%>
<%@page
	import="com.fy.engineserver.datasource.article.data.entity.NewMagicWeaponEntity"%>
<%@page import="com.fy.engineserver.activity.dig.DigPropsEntity"%>
<%@page
	import="com.fy.engineserver.datasource.article.data.entity.YinPiaoEntity"%>
<%@page
	import="com.fy.engineserver.datasource.article.data.entity.QiLingArticleEntity"%>
<%@page
	import="com.fy.engineserver.sprite.pet.SingleForPetPropsEntity"%>
<%@page
	import="com.fy.engineserver.activity.explore.ExploreResourceMapEntity"%>
<%@page
	import="com.fy.engineserver.datasource.article.data.entity.Special_2EquipmentEntity"%>
<%@page
	import="com.fy.engineserver.datasource.article.data.entity.Special_1EquipmentEntity"%>
<%@page
	import="com.fy.engineserver.datasource.article.data.entity.PropsEntity"%>
<%@page
	import="com.fy.engineserver.datasource.article.data.entity.PetPropsEntity"%>
<%@page
	import="com.fy.engineserver.datasource.article.data.entity.PetEggPropsEntity"%>
<%@page
	import="com.fy.engineserver.datasource.article.data.entity.MagicWeaponEntity"%>
<%@page
	import="com.fy.engineserver.datasource.article.data.entity.FateActivityPropsEntity"%>
<%@page
	import="com.fy.engineserver.datasource.article.data.entity.ExchangeArticleEntity"%>
<%@page
	import="com.fy.engineserver.datasource.article.data.entity.BottlePropsEntity"%>
<%@page
	import="com.fy.engineserver.datasource.article.data.entity.BiWuArticleEntity"%>
<%@page
	import="com.fy.engineserver.datasource.article.data.entity.EquipmentEntity"%>
<%@page
	import="com.xuanzhi.tools.simplejpa.impl.SimpleEntityManagerFactoryImpl"%>
<%@page import="com.xuanzhi.tools.simplejpa.SimpleEntityManagerFactory"%>
<%@page import="com.fy.engineserver.wing.WingManager"%>
<%@page import="com.fy.engineserver.wing.WingPanel"%>
<%@page
	import="com.fy.engineserver.homestead.faery.service.FaeryManager"%>
<%@page import="com.fy.engineserver.homestead.cave.Cave"%>
<%@page
	import="com.fy.engineserver.playerAims.instance.PlayerAimsEntity"%>
<%@page
	import="com.fy.engineserver.playerAims.manager.PlayerAimeEntityManager"%>
<%@page
	import="com.fy.engineserver.sprite.horse2.manager.Horse2EntityManager"%>
<%@page
	import="com.fy.engineserver.sprite.horse2.instance.Horse2RelevantEntity"%>
<%@page import="com.fy.engineserver.newtask.NewDeliverTaskManager"%>
<%@page import="com.fy.engineserver.newtask.NewDeliverTask"%>
<%@page import="com.fy.engineserver.buffsave.BufferSaveManager"%>
<%@page import="com.fy.engineserver.buffsave.BuffSave"%>
<%@page
	import="com.fy.engineserver.activity.TransitRobbery.TransitRobberyEntityManager"%>
<%@page
	import="com.fy.engineserver.activity.TransitRobbery.TransitRobberyEntity"%>
<%@page
	import="com.fy.engineserver.datasource.skill.master.SkEnhanceManager"%>
<%@page import="com.fy.engineserver.datasource.skill.master.SkBean"%>
<%@page
	import="com.fy.engineserver.articleProtect.PlayerArticleProtectData"%>
<%@page
	import="com.fy.engineserver.articleProtect.ArticleProtectData"%>
<%@page import="com.fy.engineserver.minigame.MiniGameEntityManager"%>
<%@page import="com.fy.engineserver.minigame.MiniGameEntity"%>
<%@page
	import="com.fy.engineserver.activity.activeness.ActivenessManager"%>
<%@page
	import="com.fy.engineserver.activity.activeness.PlayerActivenessInfo"%>
<%@page import="com.fy.engineserver.sprite.pet2.Pet2Manager"%>
<%@page import="com.fy.engineserver.sprite.pet2.PetsOfPlayer"%>
<%@page
	import="com.fy.engineserver.activity.worldBoss.WorldBossRankManager"%>
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
<%@page import="java.util.Map"%>
<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>


<%
	String pId = request.getParameter("pId");
	String psd = request.getParameter("psd");
	/* if (pId != null && psd != null) {
	if ("zhuanfumima".equals(psd)) {
		try {
			long playerId = Long.parseLong(pId);
			String serverName = GameConstants.getInstance().getServerName();
			
			String targetFile = URLEncoder.encode(serverName, "utf-8") + "_" + playerId + ".txt"; 
			response.setContentType("text/x-msdownload");  
			response.addHeader("Content-Disposition", 
		            "attachment;   filename=\"" + targetFile + "\""); 
			OutputStream os = response.getOutputStream(); 
			String str = queryPlayerInfos(playerId);
			
			os.write(str.getBytes());
			os.close();
			response.flushBuffer(); 
		} catch (Exception e) {
			e.printStackTrace();
			TransitRobberyManager.logger.warn("[转移角色异常] [Pid:" + pId + "]", e);
			out.println("转服页面出现异常<br>");
		}
	} else {
		out.println("不知道密码别乱点！<br>");
	}
	} */
	if (pId != null && psd != null) {
		String herf = "realconvertplayerInfos.jsp?pId=" + pId + "&psd=" + psd;
		out.println(herf + "<br>");
		response.sendRedirect(herf);
	} else {
		out.println("请输入角色Id和密码！<br>");
	}
	
%>

<form action="convertplayerInfos.jsp" method="post">
	<input type="hidden" name="action" value="enterCarbon" />角色 id: <input
		type="text" name="pId" value="<%=pId%>" />密码: <input type="password"
		name="psd" value="<%=psd%>" /> 
		<input type="submit" value="下载转服角色信息" >
</form>
<br />

<%!public String queryPlayerInfos(long playerId) throws Exception {
		//long playerId = ParamUtils.getLongParameter(request, "pid", -1);

		Player player = PlayerManager.getInstance().getPlayer(playerId);

		if (player != null) {
			Map returnHashMap = new LinkedHashMap();

			returnHashMap.put("player", player);
			//获取所有的道具，然后放入到一个list中，再放入returnList

			ArrayList<ArticleEntity> articleList = new ArrayList<ArticleEntity>();
			ArrayList<EquipmentEntity> equipmentList = new ArrayList<EquipmentEntity>();
			ArrayList<BiWuArticleEntity> biWuList = new ArrayList<BiWuArticleEntity>();
			ArrayList<BottlePropsEntity> bottlePropsList = new ArrayList<BottlePropsEntity>();
			ArrayList<ExchangeArticleEntity> exchangeArticleList = new ArrayList<ExchangeArticleEntity>();
			ArrayList<FateActivityPropsEntity> fateActivityPropsList = new ArrayList<FateActivityPropsEntity>();
			ArrayList<MagicWeaponEntity> magicWeaponList = new ArrayList<MagicWeaponEntity>();
			ArrayList<PetEggPropsEntity> petEggPropsList = new ArrayList<PetEggPropsEntity>();
			ArrayList<PetPropsEntity> petPropsList = new ArrayList<PetPropsEntity>();
			ArrayList<PropsEntity> propsEntityList = new ArrayList<PropsEntity>();
			ArrayList<Special_1EquipmentEntity> special_1EquipmentList = new ArrayList<Special_1EquipmentEntity>();
			ArrayList<Special_2EquipmentEntity> special_2EquipmentList = new ArrayList<Special_2EquipmentEntity>();
			ArrayList<ExploreResourceMapEntity> exploreResourceMapList = new ArrayList<ExploreResourceMapEntity>();
			ArrayList<SingleForPetPropsEntity> singleForPetPropsList = new ArrayList<SingleForPetPropsEntity>();
			ArrayList<QiLingArticleEntity> qiLingArticleList = new ArrayList<QiLingArticleEntity>();
			ArrayList<YinPiaoEntity> yinPiaoList = new ArrayList<YinPiaoEntity>();
			ArrayList<DigPropsEntity> digPropsList = new ArrayList<DigPropsEntity>();
			ArrayList<NewMagicWeaponEntity> newMagicWeaponList = new ArrayList<NewMagicWeaponEntity>();
			ArrayList<HuntLifeArticleEntity> huntLifeAes = new ArrayList<HuntLifeArticleEntity>(); //兽魂物品
			ArrayList<HuntArticleExtraData> huntLifeExtra = new ArrayList<HuntArticleExtraData>(); //兽魂其他信息
			
			ArrayList<SoulPithArticleEntity> soulpithaes = new ArrayList<SoulPithArticleEntity>(); //灵髓物品
			ArrayList<SoulPithAeData> soulpithExtra = new ArrayList<SoulPithAeData>(); //灵髓其他
			
			ArrayList<HunshiEntity> hunshi = new ArrayList<HunshiEntity>(); //魂石
			
			List<Long> petIds = new ArrayList<Long>();
			
			

			ArrayList<EnchantData> enchantDatas = new ArrayList<EnchantData>(); //附魔道具额外属性
			ArrayList<BaoShiXiaZiData> baoshixiazis = new ArrayList<BaoShiXiaZiData>(); //宝石匣子
			HashSet<Long> articleIds = new HashSet<Long>();

			{

				//获取以playerId为主键的千层塔的相关道具
				QianCengTaManager qianCengTaManager = QianCengTaManager.getInstance();

				QianCengTa_Ta qianCengTa_Ta = qianCengTaManager.em.find(playerId);

				WingPanel wp = WingManager.getInstance().getWingPanle(player);
				if (wp != null) {
					long brightInlayId = wp.getBrightInlayId();
					if (brightInlayId > 0) {
						articleIds.add(brightInlayId);
					}
					//镶嵌的宝石
					long[] inlayArticleIds = wp.getInlayArticleIds();
					if (inlayArticleIds != null) {
						for (long id : inlayArticleIds) {
							if (id > 0) {
								articleIds.add(id);
							}
						}
					}
				}

				if (qianCengTa_Ta != null) {
					for (int i = 0; i < qianCengTa_Ta.getDaoList().size(); i++) {
						QianCengTa_Dao dao = qianCengTa_Ta.getDaoList().get(i);
						for (int j = 0; j < dao.getCengList().size(); j++) {
							QianCengTa_Ceng ceng = dao.getCengList().get(j);
							for (int k = 0; k < ceng.getRewards().size(); k++) {
								if (ceng.getRewards().get(k).getEntityId() > 0) {
									articleIds.add(ceng.getRewards().get(k).getEntityId());
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
								if (player.getKnapsack_fangbao().getCell(i).getEntityId() > 0) {
									articleIds.add(player.getKnapsack_fangbao().getCell(i).getEntityId());
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
								if (player.getKnapsack_common().getCell(i).getEntityId() > 0) {
									articleIds.add(player.getKnapsack_common().getCell(i).getEntityId());
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
								if (player.getKnapsacks_cangku().getCell(i).getEntityId() > 0) {
									articleIds.add(player.getKnapsacks_cangku().getCell(i).getEntityId());
								}
							}
						} catch (Exception e) {
						}
					}
				}
				if (player.getKnapsacks_warehouse() != null) {
					for (int i = 0; i < player.getKnapsacks_warehouse().size(); i++) {		//2号仓库
						try {
							if (player.getKnapsacks_warehouse().getCell(i) != null) {
								if (player.getKnapsacks_warehouse().getCell(i).getEntityId() > 0) {
									articleIds.add(player.getKnapsacks_warehouse().getCell(i).getEntityId());
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
							for (int j = 0; j < soul.getEc().getEquipmentIds().length; j++) {
								if (soul.getEc().getEquipmentIds()[j] > 0) {
									articleIds.add(soul.getEc().getEquipmentIds()[j]);
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
								if (player.getKnapsacks_QiLing().getCell(i).getEntityId() > 0) {
									articleIds.add(player.getKnapsacks_QiLing().getCell(i).getEntityId());
								}
							}
						} catch (Exception e) {
						}
					}
				}

			}

			//获取和player相关的delivertask
			{
				List<DeliverTask> deliverTasks = DeliverTaskManager.getInstance().loadAllDeliverTask(playerId);
				if (deliverTasks != null) returnHashMap.put("delivertasks", deliverTasks);
			}
			//获取和player相关的taskEntitys
			{
				List<TaskEntity> taskEntitys = TaskEntityManager.getInstance().getPlayerTaskEntities(player);
				if (taskEntitys != null) {
					returnHashMap.put("taskentitys", taskEntitys);
				}
			}

			//获取玩家的仙府 这里的仙府摆放什么的有疑问，暂时先不处理

			//获取玩家的gamedata
			{
				HashMap<Integer, GameDataRecord> playerRecords = AchievementManager.getInstance().getPlayerDataRecords(player);
				if (playerRecords != null) {
					returnHashMap.put("playerrecords", playerRecords);
				}
			}

			//AchievementEntity
			{
				HashMap<Integer, AchievementEntity> achievementMap = AchievementManager.getInstance().getPlayerAchievementEntitys(player);

				if (achievementMap != null) {
					returnHashMap.put("achievements", achievementMap);
				}
			}

			//获取玩家的千层塔
			{
				QianCengTa_Ta qianCengTa_Ta = QianCengTaManager.getInstance().getTa(playerId);
				if (qianCengTa_Ta != null) {
					returnHashMap.put("qiancengta_ta", qianCengTa_Ta);
				}
			}
			
			{	//获取玩家灵髓
				SoulPithEntity soulpithentity = SoulPithEntityManager.getInst().getEntity(player);	
				if (soulpithentity != null) {
					returnHashMap.put("soulpithentity", soulpithentity);
				}
			}

			//获取玩家的HotSpot
			{
				List<HotspotInfo> list = new ArrayList<HotspotInfo>();
				long count = HotspotManager.getInstance().em.count(HotspotInfo.class, "playerID=" + player.getId());
				list.addAll(HotspotManager.getInstance().em.query(HotspotInfo.class, "playerID=" + player.getId(), "", 1, count + 1));

				returnHashMap.put("hotspotinfos", list);
			}

			//获取玩家的Horse
			/**
				玩家有两个元神 上面有自己的horseAttr 这才是真正玩家的坐骑
			 */
			{
				ArrayList<Long> horseIds = new ArrayList<Long>();

				// 装备
				for (int i = 0; i < player.getSouls().length; i++) {
					try {
						Soul soul = player.getSouls()[i];
						if (soul == null) {
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

				if (horseIds != null && horseIds.size() > 0) {
					ArrayList<Horse> horses = new ArrayList<Horse>();
					SimpleEntityManager<Horse> em = SimpleEntityManagerFactory.getSimpleEntityManager(Horse.class);
					for (Long horseId : horseIds) {
						Horse horse = em.find(horseId);
						if (horse != null) {
							horses.add(horse);

							long[] horseEquIDs = horse.getEquipmentColumn().getEquipmentIds();
							for (long i : horseEquIDs) {
								if (i > 0) {
									articleIds.add(i);
								}
							}
							for (long i : horse.getHunshiArray()) {
								if (i > 0) {
									articleIds.add(i);
								}
							}
							for (long i : horse.getHunshi2Array()) {
								if (i > 0) {
									articleIds.add(i);
								}
							}

						}

					}
					returnHashMap.put("horses", horses);
				}
			}

			//获取玩家的Pet
			{
				List<Pet> list = new ArrayList<Pet>();
				/* long count = PetManager.getInstance().em.count(Pet.class, "ownerId=" + player.getId() + " and delete='F' or (delete='T' and rarity >= 3)");
				list.addAll(PetManager.getInstance().em.query(Pet.class, "ownerId=" + player.getId() + " and delete='F' or (delete='T' and rarity >= 3)", "", 1, count + 1));
				for (Pet pet : list) {
					long petPropId = pet.getPetPropsId();
					if (petPropId > 0) {
						articleIds.add(petPropId);
					}

				} */
				
				returnHashMap.put("pets", list);
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
				PetsOfPlayer petsOfPlayer = Pet2Manager.getInst().findPetsOfPlayer(player);
				if (petsOfPlayer != null) returnHashMap.put("petsofplayer", petsOfPlayer);
			}

			//获取玩家活跃度
			{
				PlayerActivenessInfo playerActivenessInfo = ActivenessManager.getInstance().getPlayerActivenessInfoFromDB(player);
				returnHashMap.put("playeractivenessinfo", playerActivenessInfo);
			}

			//获取MINIGAME
			{
				MiniGameEntity miniGameEntity = MiniGameEntityManager.getInstance().getMiniGameEntity(player);
				if (miniGameEntity != null) returnHashMap.put("minigameentity", miniGameEntity);
			}

			//获取玩家的锁魂记录
			{
				List<ArticleProtectData> list = new ArrayList<ArticleProtectData>();
				long count = PlayerArticleProtectData.em.count(ArticleProtectData.class, "playerID=" + player.getId());
				list.addAll(PlayerArticleProtectData.em.query(ArticleProtectData.class, "playerID=" + player.getId(), "", 1, count + 1));
				returnHashMap.put("articleprotectdatas", list);
			}

			//获取玩家大师技能
			{
				SkBean skBean = SkEnhanceManager.getInst().findSkBean(player);
				if (skBean != null) returnHashMap.put("skbean", skBean);
			}

			//获取玩家渡劫信息
			{
				TransitRobberyEntity transitRobberyEntity = TransitRobberyEntityManager.getInstance().getTransitRobberyEntity(playerId);
				if (transitRobberyEntity != null) returnHashMap.put("transitrobberyentity", transitRobberyEntity);
			}

			//获取玩家buff
			{
				List<BuffSave> list = BufferSaveManager.getInstance().getbuff(playerId);
				if (list != null) returnHashMap.put("buffsaves", list);
			}

			//获取玩家newdelivertask
			{
				List<NewDeliverTask> newDeliverTasks = NewDeliverTaskManager.getInstance().em.query(NewDeliverTask.class, "playerId=?", new Object[] { playerId }, "", 1, 9999);
				if (newDeliverTasks != null) {
					returnHashMap.put("newdelivertasks", newDeliverTasks);
				}
			}

			//获取玩家新宠物数据信息
			{
				Horse2RelevantEntity horse2RelevantEntity = Horse2EntityManager.instance.getEntity(playerId);
				if (horse2RelevantEntity != null) returnHashMap.put("horse2relevantentity", horse2RelevantEntity);
			}

			//获取玩家目标实体
			{
				PlayerAimsEntity playerAimsEntity = PlayerAimeEntityManager.instance.getEntity(playerId);
				if (playerAimsEntity != null) returnHashMap.put("playeraimsentity", playerAimsEntity);
			}

			//获取仙府
			if (player.getCaveId() > 0) {
				Cave cave = FaeryManager.caveEm.find(player.getCaveId());
				if (cave != null) {
					returnHashMap.put("cave", cave);
				}

			}
			if (player.getLevel() > 220) { //仙婴
				TalentData talent = FlyTalentManager.em.find(player.getId());
				if (talent != null) {
					returnHashMap.put("flytalent", talent);
				}
			}
			if (player.getLevel() > 220) { //仙婴
				FairyRobberyEntity fre = FairyRobberyEntityManager.em.find(player.getId());
				if (fre != null) {
					returnHashMap.put("fairyrobbery", fre);
				}
			}

			//获取翅膀
			{

				WingPanel wingPanel = WingManager.getInstance().getWingPanle(player);
				if (wingPanel != null) {
					returnHashMap.put("wingpanel", wingPanel);
				}
			}

			{ //获取兽魂面板
				HuntLifeEntity he = HuntLifeEntityManager.em.find(player.getId());
				if (he != null) {
					for (long id : he.getHuntDatas()) {
						if (id > 0) {
							HuntLifeArticleEntity entity = (HuntLifeArticleEntity) DefaultArticleEntityManager.getInstance().getEntity(id);
							huntLifeAes.add(entity);
							HuntArticleExtraData data = HuntLifeEntityManager.em_ae.find(id);
							if (data != null) {
								huntLifeExtra.add(data);
							}
						}
					}
					returnHashMap.put("shouhun", he);
				}
			}

			{ //收集兽魂道具
				if (player.getShouhunKnap() != null) {
					for (long id : player.getShouhunKnap()) {
						if (id > 0) {
							HuntLifeArticleEntity entity = (HuntLifeArticleEntity) DefaultArticleEntityManager.getInstance().getEntity(id);
							huntLifeAes.add(entity);
							HuntArticleExtraData data = HuntLifeEntityManager.em_ae.find(id);
							if (data != null) {
								huntLifeExtra.add(data);
							}
						}
					}
				}
			}

			//将articleid转成article 放入articleList 再放入returnList当中

			HashSet<Long> moreArticleIds = new HashSet<Long>();

			for (Long articleId : articleIds) {
				ArticleEntity articleEntity = ArticleEntityManager.getInstance().getEntity(articleId);
				if (articleEntity == null) {
					continue;
				}

				if (articleEntity.getClass().equals(NewMagicWeaponEntity.class)) {
					newMagicWeaponList.add((NewMagicWeaponEntity) articleEntity);
				} else if (articleEntity.getClass().equals(DigPropsEntity.class)) {
					digPropsList.add((DigPropsEntity) articleEntity);
				} else if (articleEntity.getClass().equals(YinPiaoEntity.class)) {
					yinPiaoList.add((YinPiaoEntity) articleEntity);
				} else if (articleEntity.getClass().equals(QiLingArticleEntity.class)) {
					qiLingArticleList.add((QiLingArticleEntity) articleEntity);
				} else if (articleEntity.getClass().equals(SingleForPetPropsEntity.class)) {
					singleForPetPropsList.add((SingleForPetPropsEntity) articleEntity);
				} else if (articleEntity.getClass().equals(ExploreResourceMapEntity.class)) {
					exploreResourceMapList.add((ExploreResourceMapEntity) articleEntity);
				} else if (articleEntity.getClass().equals(Special_2EquipmentEntity.class)) {
					special_2EquipmentList.add((Special_2EquipmentEntity) articleEntity);
				} else if (articleEntity.getClass().equals(Special_1EquipmentEntity.class)) {
					special_1EquipmentList.add((Special_1EquipmentEntity) articleEntity);
				} else if (articleEntity.getClass().equals(PropsEntity.class)) {
					propsEntityList.add((PropsEntity) articleEntity);
				} else if (articleEntity.getClass().equals(HunshiEntity.class)) {
					hunshi.add((HunshiEntity)articleEntity);
				} else if (articleEntity.getClass().equals(SoulPithArticleEntity.class)) {
					soulpithaes.add((SoulPithArticleEntity)articleEntity);
					if (((SoulPithArticleEntity)articleEntity).getExtraData() != null) {
						soulpithExtra.add(((SoulPithArticleEntity)articleEntity).getExtraData());
					}
				} else if (articleEntity.getClass().equals(PetPropsEntity.class)) {
					petPropsList.add((PetPropsEntity) articleEntity);

					PetPropsEntity petPropsEntity = (PetPropsEntity) articleEntity;
					if (petPropsEntity.getPetId() > 0) {
						Pet pet = PetManager.getInstance().getPet(petPropsEntity.getPetId());
						if (pet != null) {
							if (returnHashMap.get("pets") != null) {
								List<Pet> pets = (List) returnHashMap.get("pets");
								boolean hasPet = false;
								for (Pet p : pets) {
									if (p.getId() == pet.getId()) {
										hasPet = true;
										break;
									}
								}
								if (!hasPet) {
									pets.add(pet);
									returnHashMap.put("pets", pets);
								}
							}
						}
					}

				} else if (articleEntity.getClass().equals(PetEggPropsEntity.class)) {
					petEggPropsList.add((PetEggPropsEntity) articleEntity);

					PetEggPropsEntity petPropsEntity = (PetEggPropsEntity) articleEntity;
					if (petPropsEntity.getPetId() > 0) {
						Pet pet = PetManager.getInstance().getPet(petPropsEntity.getPetId());
						if (pet != null) {
							if (returnHashMap.get("pets") != null) {
								List<Pet> pets = (List) returnHashMap.get("pets");
								boolean hasPet = false;
								for (Pet p : pets) {
									if (p.getId() == pet.getId()) {
										hasPet = true;
										break;
									}
								}
								if (!hasPet) {
									pets.add(pet);
									returnHashMap.put("pets", pets);
								}
							}
						}
					}

				} else if (articleEntity.getClass().equals(MagicWeaponEntity.class)) {
					magicWeaponList.add((MagicWeaponEntity) articleEntity);
				} else if (articleEntity.getClass().equals(FateActivityPropsEntity.class)) {
					fateActivityPropsList.add((FateActivityPropsEntity) articleEntity);
				} else if (articleEntity.getClass().equals(ExchangeArticleEntity.class)) {
					exchangeArticleList.add((ExchangeArticleEntity) articleEntity);
				} else if (articleEntity.getClass().equals(BottlePropsEntity.class)) {
					bottlePropsList.add((BottlePropsEntity) articleEntity);
				} else if (articleEntity.getClass().equals(BiWuArticleEntity.class)) {
					biWuList.add((BiWuArticleEntity) articleEntity);
				} else if (articleEntity.getClass().equals(EquipmentEntity.class)) {
					equipmentList.add((EquipmentEntity) articleEntity);

					EquipmentEntity equipmentEntity = (EquipmentEntity) articleEntity;

					long[] inlayArticleIds = equipmentEntity.getInlayArticleIds();
					long[] inlayQiLingArticleIds = equipmentEntity.getInlayQiLingArticleIds();

					if (inlayArticleIds.length > 0) {
						for (int i = 0; i < inlayArticleIds.length; i++) {
							moreArticleIds.add(inlayArticleIds[i]);
						}

					}

					if (inlayQiLingArticleIds.length > 0) {
						for (int i = 0; i < inlayQiLingArticleIds.length; i++) {
							moreArticleIds.add(inlayQiLingArticleIds[i]);
						}

					}
					{ //检测附魔
						EnchantData data = EnchantEntityManager.em.find(articleEntity.getId());
						if (data != null) {
							enchantDatas.add(data);
						}
					}

				} else if (articleEntity.getClass().equals(ArticleEntity.class)) {
					articleList.add(articleEntity);
					Article a = ArticleManager.getInstance().getArticle(articleEntity.getArticleName());
					if (a != null && a instanceof InlayArticle) {
						InlayArticle baoshi = (InlayArticle) a;
						int inlayType = baoshi.getInlayType();
						if (inlayType > 1) {
							BaoShiXiaZiData data = ArticleEntityManager.baoShiXiLianEM.find(articleEntity.getId());
							if (data != null) {
								baoshixiazis.add(data);
								for (long baoshiId : data.getIds()) {
									if (baoshiId > 0) {
										ArticleEntity baoshi1 = ArticleEntityManager.getInstance().getEntity(baoshiId);
										if (baoshi1 != null) {
											articleList.add(baoshi1);
										}
									}
								}
							}
						}
					}
				}

			}
			List<PetFlyState> petFlys = new ArrayList<PetFlyState>();
			List<Pet> pets = (List) returnHashMap.get("pets");
			if (pets != null) {
				for (Pet pet : pets) {
					PetFlyState stat = PetManager.em_state.find(pet.getId());
					if (stat != null) {
						petFlys.add(stat);
					}
				}
			}
			returnHashMap.put("PetFlyState", petFlys);

			for (Long articleId : moreArticleIds) {
				ArticleEntity articleEntity = ArticleEntityManager.getInstance().getEntity(articleId);

				if (articleEntity != null) {
					if (articleEntity.getClass().equals(NewMagicWeaponEntity.class)) {
						newMagicWeaponList.add((NewMagicWeaponEntity) articleEntity);
					} else if (articleEntity.getClass().equals(DigPropsEntity.class)) {
						digPropsList.add((DigPropsEntity) articleEntity);
					} else if (articleEntity.getClass().equals(YinPiaoEntity.class)) {
						yinPiaoList.add((YinPiaoEntity) articleEntity);
					} else if (articleEntity.getClass().equals(QiLingArticleEntity.class)) {
						qiLingArticleList.add((QiLingArticleEntity) articleEntity);
					} else if (articleEntity.getClass().equals(SingleForPetPropsEntity.class)) {
						singleForPetPropsList.add((SingleForPetPropsEntity) articleEntity);
					} else if (articleEntity.getClass().equals(ExploreResourceMapEntity.class)) {
						exploreResourceMapList.add((ExploreResourceMapEntity) articleEntity);
					} else if (articleEntity.getClass().equals(Special_2EquipmentEntity.class)) {
						special_2EquipmentList.add((Special_2EquipmentEntity) articleEntity);
					} else if (articleEntity.getClass().equals(Special_1EquipmentEntity.class)) {
						special_1EquipmentList.add((Special_1EquipmentEntity) articleEntity);
					} else if (articleEntity.getClass().equals(PropsEntity.class)) {
						propsEntityList.add((PropsEntity) articleEntity);
					} else if (articleEntity.getClass().equals(PetPropsEntity.class)) {
						petPropsList.add((PetPropsEntity) articleEntity);
					} else if (articleEntity.getClass().equals(PetEggPropsEntity.class)) {
						petEggPropsList.add((PetEggPropsEntity) articleEntity);
					} else if (articleEntity.getClass().equals(MagicWeaponEntity.class)) {
						magicWeaponList.add((MagicWeaponEntity) articleEntity);
					} else if (articleEntity.getClass().equals(FateActivityPropsEntity.class)) {
						fateActivityPropsList.add((FateActivityPropsEntity) articleEntity);
					} else if (articleEntity.getClass().equals(ExchangeArticleEntity.class)) {
						exchangeArticleList.add((ExchangeArticleEntity) articleEntity);
					} else if (articleEntity.getClass().equals(BottlePropsEntity.class)) {
						bottlePropsList.add((BottlePropsEntity) articleEntity);
					} else if (articleEntity.getClass().equals(BiWuArticleEntity.class)) {
						biWuList.add((BiWuArticleEntity) articleEntity);
					} else if (articleEntity.getClass().equals(EquipmentEntity.class)) {
						equipmentList.add((EquipmentEntity) articleEntity);

						EquipmentEntity equipmentEntity = (EquipmentEntity) articleEntity;

						long[] inlayArticleIds = equipmentEntity.getInlayArticleIds();
						long[] inlayQiLingArticleIds = equipmentEntity.getInlayQiLingArticleIds();
					} else if (articleEntity.getClass().equals(ArticleEntity.class)) {
						articleList.add(articleEntity);
						Article a = ArticleManager.getInstance().getArticle(articleEntity.getArticleName());
						if (a != null && a instanceof InlayArticle) {
							InlayArticle baoshi = (InlayArticle) a;
							int inlayType = baoshi.getInlayType();
							if (inlayType > 1) {
								BaoShiXiaZiData data = ArticleEntityManager.baoShiXiLianEM.find(articleEntity.getId());
								if (data != null) {
									baoshixiazis.add(data);
									for (long baoshiId : data.getIds()) {
										if (baoshiId > 0) {
											ArticleEntity baoshi1 = ArticleEntityManager.getInstance().getEntity(baoshiId);
											if (baoshi1 != null) {
												articleList.add(baoshi1);
											}
										}
									}
								}
							}
						}
					}
				}

			}

			returnHashMap.put(NewMagicWeaponEntity.class.getName(), newMagicWeaponList);
			returnHashMap.put(DigPropsEntity.class.getName(), digPropsList);
			returnHashMap.put(YinPiaoEntity.class.getName(), yinPiaoList);
			returnHashMap.put(QiLingArticleEntity.class.getName(), qiLingArticleList);
			returnHashMap.put(SingleForPetPropsEntity.class.getName(), singleForPetPropsList);
			returnHashMap.put(ExploreResourceMapEntity.class.getName(), exploreResourceMapList);
			returnHashMap.put(Special_2EquipmentEntity.class.getName(), special_2EquipmentList);
			returnHashMap.put(Special_1EquipmentEntity.class.getName(), special_1EquipmentList);
			returnHashMap.put(PropsEntity.class.getName(), propsEntityList);
			returnHashMap.put(PetPropsEntity.class.getName(), petPropsList);
			returnHashMap.put(PetEggPropsEntity.class.getName(), petEggPropsList);
			returnHashMap.put(MagicWeaponEntity.class.getName(), magicWeaponList);
			returnHashMap.put(FateActivityPropsEntity.class.getName(), fateActivityPropsList);
			returnHashMap.put(ExchangeArticleEntity.class.getName(), exchangeArticleList);
			returnHashMap.put(BottlePropsEntity.class.getName(), bottlePropsList);
			returnHashMap.put(BiWuArticleEntity.class.getName(), biWuList);
			returnHashMap.put(EquipmentEntity.class.getName(), equipmentList);
			returnHashMap.put(ArticleEntity.class.getName(), articleList);
			returnHashMap.put(HuntLifeArticleEntity.class.getName(), huntLifeAes);
			returnHashMap.put(HuntArticleExtraData.class.getName(), huntLifeExtra);
			returnHashMap.put(EnchantData.class.getName(), enchantDatas);
			returnHashMap.put(BaoShiXiaZiData.class.getName(), baoshixiazis);
			returnHashMap.put(HunshiEntity.class.getName(), hunshi);
			returnHashMap.put(SoulPithArticleEntity.class.getName(), soulpithaes);
			returnHashMap.put(SoulPithAeData.class.getName(), soulpithExtra);
			

			return JsonUtil.jsonFromObject(returnHashMap);
			//out.print(JsonUtil.jsonFromObject(returnHashMap));
		} else {
			return "errortransmit";
			//out.print("errortransmit");
		}
	}%>
