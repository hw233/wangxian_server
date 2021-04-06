<%@page import="com.fy.engineserver.jiazu2.manager.JiazuEntityManager2"%>
<%@page import="com.fy.engineserver.jiazu2.instance.JiazuMember2"%>
<%@page import="com.fy.engineserver.datasource.article.data.horseInlay.HorseEquInlayEntityManager"%>
<%@page import="com.fy.engineserver.datasource.article.data.horseInlay.instance.HorseEquInlay"%>
<%@page import="com.fy.engineserver.datasource.article.manager.ArticleManager"%>
<%@page import="com.fy.engineserver.activity.xianling.XianLingManager"%>
<%@page import="com.fy.engineserver.activity.xianling.PlayerXianLingData"%>
<%@page import="com.fy.engineserver.sprite.pet.suit.PetSuitArticleEntity"%>
<%@page import="com.fy.engineserver.soulpith.instance.SoulPithEntity"%>
<%@page import="com.fy.engineserver.datasource.article.data.entity.HunshiEntity"%>
<%@page import="com.fy.engineserver.soulpith.SoulPithManager"%>
<%@page import="com.fy.engineserver.soulpith.SoulPithEntityManager"%>
<%@page import="com.fy.engineserver.soulpith.instance.SoulPithAeData"%>
<%@page import="com.fy.engineserver.datasource.article.data.entity.SoulPithArticleEntity"%>
<%@page import="com.fy.engineserver.seal.SealManager"%>
<%@page import="com.fy.engineserver.activity.fairyRobbery.FairyRobberyEntityManager"%>
<%@page import="com.fy.engineserver.activity.fairyRobbery.instance.FairyRobberyEntity"%>
<%@page import="com.fy.engineserver.talent.FlyTalentManager"%>
<%@page import="com.fy.engineserver.talent.TalentData"%>
<%@page import="com.fy.engineserver.sprite.pet.PetFlyState"%>
<%@page import="com.fy.engineserver.activity.TransitRobbery.TransitRobberyManager"%>
<%@page import="java.io.BufferedReader"%>
<%@page import="java.io.FileInputStream"%>
<%@page import="java.io.InputStreamReader"%>
<%@page import="java.io.File"%>
<%@page import="java.util.Arrays"%>
<%@page import="com.fy.engineserver.datasource.article.data.entity.BaoShiXiaZiData"%>
<%@page import="com.fy.engineserver.articleEnchant.EnchantEntityManager"%>
<%@page import="com.fy.engineserver.articleEnchant.EnchantData"%>
<%@page import="com.fy.engineserver.datasource.article.data.magicweapon.huntLife.instance.HuntArticleExtraData"%>
<%@page import="com.fy.engineserver.datasource.article.concrete.DefaultArticleEntityManager"%>
<%@page import="com.fy.engineserver.datasource.article.data.entity.HuntLifeArticleEntity"%>
<%@page import="com.fy.engineserver.datasource.article.data.magicweapon.huntLife.HuntLifeEntityManager"%>
<%@page import="com.fy.engineserver.datasource.article.data.magicweapon.huntLife.instance.HuntLifeEntity"%>
<%@page import="com.xuanzhi.boss.game.GameConstants"%>
<%@page import="com.fy.engineserver.articleProtect.ArticleProtectManager"%>
<%@page import="com.fy.engineserver.sprite.PlayerManager"%>
<%@page import="com.fy.engineserver.datasource.article.data.entity.EquipmentEntity"%>
<%@page import="com.fy.engineserver.datasource.article.data.entity.BiWuArticleEntity"%>
<%@page import="com.fy.engineserver.datasource.article.data.entity.BottlePropsEntity"%>
<%@page import="com.fy.engineserver.datasource.article.data.entity.ExchangeArticleEntity"%>
<%@page import="com.fy.engineserver.datasource.article.data.entity.FateActivityPropsEntity"%>
<%@page import="com.fy.engineserver.datasource.article.data.entity.MagicWeaponEntity"%>
<%@page import="com.fy.engineserver.datasource.article.data.entity.PetEggPropsEntity"%>
<%@page import="com.fy.engineserver.datasource.article.data.entity.PetPropsEntity"%>
<%@page import="com.fy.engineserver.datasource.article.data.entity.PropsEntity"%>
<%@page import="com.fy.engineserver.datasource.article.data.entity.Special_1EquipmentEntity"%>
<%@page import="com.fy.engineserver.datasource.article.data.entity.Special_2EquipmentEntity"%>
<%@page import="com.fy.engineserver.activity.explore.ExploreResourceMapEntity"%>
<%@page import="com.fy.engineserver.sprite.pet.SingleForPetPropsEntity"%>
<%@page import="com.fy.engineserver.datasource.article.data.entity.QiLingArticleEntity"%>
<%@page import="com.fy.engineserver.datasource.article.data.entity.NewMagicWeaponEntity"%>
<%@page import="com.fy.engineserver.activity.dig.DigPropsEntity"%>
<%@page import="com.fy.engineserver.datasource.article.data.entity.YinPiaoEntity"%>
<%@page import="java.net.URLDecoder"%>
<%@page import="com.xuanzhi.tools.simplejpa.SimpleEntityManagerFactory"%>
<%@page import="com.xuanzhi.tools.simplejpa.annotation.SimpleVersion"%>
<%@page import="java.util.Iterator"%>
<%@page import="java.lang.reflect.Field"%>
<%@page import="com.fy.engineserver.uniteserver.UnitedServerManager2"%>
<%@page import="com.fy.engineserver.unite.UniteManager"%>
<%@page import="com.fy.engineserver.wing.WingManager"%>
<%@page import="com.fy.engineserver.wing.WingPanel"%>
<%@page import="com.fy.engineserver.gametime.SystemTime"%>
<%@page import="com.fy.engineserver.homestead.faery.service.FaeryManager"%>
<%@page import="com.fy.engineserver.playerAims.manager.PlayerAimeEntityManager"%>
<%@page import="com.fy.engineserver.sprite.horse2.manager.Horse2EntityManager"%>
<%@page import="com.fy.engineserver.newtask.NewDeliverTaskManager"%>
<%@page import="com.fy.engineserver.buffsave.BufferSaveManager"%>
<%@page import="com.fy.engineserver.articleProtect.PlayerArticleProtectData"%>
<%@page import="com.fy.engineserver.activity.TransitRobbery.TransitRobberyEntityManager"%>
<%@page import="com.fy.engineserver.datasource.skill.master.SkEnhanceManager"%>
<%@page import="com.fy.engineserver.minigame.MiniGameEntityManager"%>
<%@page import="com.fy.engineserver.activity.activeness.ActivenessManager"%>
<%@page import="com.fy.engineserver.sprite.pet2.Pet2Manager"%>
<%@page import="com.fy.engineserver.sprite.pet.PetManager"%>
<%@page import="com.fy.engineserver.sprite.horse.HorseManager"%>
<%@page import="com.fy.engineserver.hotspot.HotspotManager"%>
<%@page import="com.fy.engineserver.achievement.AchievementManager"%>
<%@page import="java.util.Collection"%>
<%@page import="com.fy.engineserver.newtask.TaskEntityManager"%>
<%@page import="com.fy.engineserver.newtask.service.DeliverTaskManager"%>
<%@page import="com.fy.engineserver.sprite.Soul"%>
<%@page import="com.fy.engineserver.qiancengta.QianCengTa_Ceng"%>
<%@page import="com.fy.engineserver.qiancengta.QianCengTa_Dao"%>
<%@page import="com.fy.engineserver.datasource.article.manager.ArticleEntityManager"%>
<%@page import="com.xuanzhi.tools.simplejpa.SimpleEntityManager"%>
<%@page import="com.fy.engineserver.homestead.cave.Cave"%>
<%@page import="com.fy.engineserver.playerAims.instance.PlayerAimsEntity"%>
<%@page import="com.fy.engineserver.sprite.horse2.instance.Horse2RelevantEntity"%>
<%@page import="com.fy.engineserver.newtask.NewDeliverTask"%>
<%@page import="com.fy.engineserver.buffsave.BuffSave"%>
<%@page import="com.fy.engineserver.activity.TransitRobbery.TransitRobberyEntity"%>
<%@page import="com.fy.engineserver.datasource.skill.master.SkBean"%>
<%@page import="com.fy.engineserver.articleProtect.ArticleProtectData"%>
<%@page import="com.fy.engineserver.minigame.MiniGameEntity"%>
<%@page import="com.fy.engineserver.activity.activeness.PlayerActivenessInfo"%>
<%@page import="com.fy.engineserver.sprite.pet2.PetsOfPlayer"%>
<%@page import="com.fy.engineserver.sprite.pet.Pet"%>
<%@page import="com.fy.engineserver.sprite.horse.Horse"%>
<%@page import="com.fy.engineserver.hotspot.HotspotInfo"%>
<%@page import="com.fy.engineserver.achievement.AchievementEntity"%>
<%@page import="com.fy.engineserver.achievement.GameDataRecord"%>
<%@page import="com.fy.engineserver.newtask.TaskEntity"%>
<%@page import="com.fy.engineserver.newtask.DeliverTask"%>
<%@page import="java.util.List"%>
<%@page import="com.fy.engineserver.datasource.article.data.entity.ArticleEntity"%>
<%@page import="java.util.ArrayList"%>
<%@page import="com.fasterxml.jackson.core.type.TypeReference"%>
<%@page import="com.fy.engineserver.sprite.concrete.GamePlayerManager"%>
<%@page import="com.fy.engineserver.qiancengta.QianCengTaManager"%>
<%@page import="com.fy.engineserver.qiancengta.QianCengTa_Ta"%>
<%@page import="com.fy.engineserver.sprite.Player"%>
<%@page import="com.xuanzhi.tools.servlet.HttpUtils"%>
<%@page import="com.xuanzhi.tools.servlet.HttpUtil"%>
<%@page import="java.net.URL"%>
<%@page import="java.util.LinkedHashMap"%>
<%@page import="com.xuanzhi.tools.text.JsonUtil"%>
<%@page import="org.apache.commons.lang.StringUtils"%>
<%@page import="com.xuanzhi.tools.text.ParamUtils"%>
<%@page import="java.util.Map"%>
<%@page import="java.util.HashMap"%>
<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%!
		
		public static Object convertToSpecialedType(Object o, Class t) throws Exception
		{
			 return JsonUtil.objectFromJson( JsonUtil.jsonFromObject(o),t);
		}
		
		public static <T> T convertToTypeReferenceType(Object o, TypeReference<T> t) throws Exception
		{
			 return JsonUtil.objectFromJson( JsonUtil.jsonFromObject(o),t);
		}
		
		
%>

<%
String ctt = request.getParameter("ctt");
String action = request.getParameter("action");
action = action == null ? "" : action;
ctt = ctt == null ? "" : ctt;
String fileName = request.getParameter("fileName");
	if (action.equals("resetTumotie")) {

		File f = new File(fileName);
		 InputStreamReader read = new InputStreamReader(
                new FileInputStream(f),"utf-8");//考虑到编码格式
		 BufferedReader bufferedReader = new BufferedReader(read);
		 String lineTxt = null;
		 StringBuffer sb = new StringBuffer();
        while((lineTxt = bufferedReader.readLine()) != null){
        	if (lineTxt.length() > 100) {
        		sb.append(lineTxt);
        	}
        }
        read.close();
        out.println("*******************<br>");
	String json = sb.toString();
	if (json == null) {
		out.println("*&*&*&*&*&*&*&&*&*<br>");
		return ;
	}
	
	

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
			 
			 
			out.println("aaaaaaaaaaaaaaaaaaaa<br>");
		
			
			Player player =  (Player)  convertToSpecialedType (linkedHashMap.get("player"),Player.class);
			
			out.println("aaaaaaaaaaaaaaaaaaaa<br>");
	
			if(player != null)
			{
				if (SealManager.getInstance().getSeal().getSealLevel() < player.getLevel()) {
					out.println("*****************************************************************************<br>");
					out.println("*****************************************************************************<br>");
					out.println("*****************************************************************************<br>");
					out.println("*******************************转服失败*****************************************<br>");
					out.println("****************************玩家等级超出本服封印等级**********************************<br>");
					out.println("服务器封印等级为:" + SealManager.getInstance().getSeal().getSealLevel() + "<br>");
					out.println("*****************************************************************************<br>");
					out.println("*****************************************************************************<br>");
					out.println("*****************************************************************************<br>");
					return ;
				}
			out.println("aaaaaaaaaaaaaaaaaaaa<br>");
				long oldId = player.getId();
				long newId = ((GamePlayerManager) GamePlayerManager.getInstance()).em.nextId();
				player.setId(newId);
				player.setCaveId(-1);
				player.setJiazuId(0);
				player.setJiazuName(null);
				player.setFaeryId(-1);
				try
				{
					if(PlayerManager.getInstance().getPlayer(player.getName()) != null)
					{
						String serverName = GameConstants.getInstance().getServerName();
						long tempId = newId % 1000;
						player.setName(player.getName()+"@" + tempId);
						out.println(player.getName() + "<br>");
					}
				}
				catch(Exception e)
				{
					
				}
				
				Field versionField = UnitedServerManager2.getFieldByAnnotation(Player.class, SimpleVersion.class);
				versionField.setAccessible(true);
				versionField.set(player, 0);
				player.getBuffs().clear();
				((GamePlayerManager) GamePlayerManager.getInstance()).em.flush(player);
				GamePlayerManager.logger.warn("[角色转服] [原角色id:"+oldId+"] [新角色id:"+newId+"]");
				oldIdToNewIdMap.put(oldId, newId);
				
			}
			{
				/* if(linkedHashMap.get("cave") != null)
				{
					Cave cave = (Cave)convertToSpecialedType( linkedHashMap.get("cave"),Cave.class);
					if (cave != null) {
						long newcid = FaeryManager.caveEm.nextId();
						cave.setId(newcid);
						cave.setOwnerId(player.getId());
						cave.setStatus(Cave.CAVE_STATUS_KHATAM);
						cave.setKhatamTime(System.currentTimeMillis());
						player.setCaveId(cave.getId());
						FaeryManager.caveEm.flush(cave);
						FaeryManager.getInstance().getKhatamMap().put(cave.getOwnerId(), cave.getId());
					}
				} */
			}
			
			{
				if(linkedHashMap.get("flytalent") != null){    //仙婴
					TalentData talent = (TalentData)convertToSpecialedType( linkedHashMap.get("flytalent"),TalentData.class);
					if (talent != null) {
						talent.setId(player.getId());
						Field versionField = UnitedServerManager2.getFieldByAnnotation(TalentData.class, SimpleVersion.class);
						versionField.setAccessible(true);
						versionField.set(talent, 0);
						FlyTalentManager.em.flush(talent);
					}
				}
			}
			{
				if(linkedHashMap.get("fairyrobbery") != null){    //仙界渡劫
					FairyRobberyEntity talent = (FairyRobberyEntity)convertToSpecialedType( linkedHashMap.get("fairyrobbery"),FairyRobberyEntity.class);
					if (talent != null) {
						talent.setId(player.getId());
						Field versionField = UnitedServerManager2.getFieldByAnnotation(FairyRobberyEntity.class, SimpleVersion.class);
						versionField.setAccessible(true);
						versionField.set(talent, 0);
						FairyRobberyEntityManager.em.flush(talent);
					}
				}
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
							 
							 QianCengTaManager.getInstance().em.flush(qianCengTa_Ta);
							 
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
						SimpleEntityManager entityManager = ArticleEntityManager.getInstance().em;
						long newId = entityManager.nextId();
						long oldId = articleEntity.getId();
						articleEntity.setId(newId);
						
						Field versionField = UnitedServerManager2.getFieldByAnnotation(ArticleEntity.class, SimpleVersion.class);
						versionField.setAccessible(true);
						versionField.set(articleEntity, 0);
						
						entityManager.flush(articleEntity);
						
						ArticleEntityManager.log.warn("[角色转服] [原NewMagicWeaponEntityid:"+oldId+"] [新NewMagicWeaponEntityid:"+newId+"]");
						oldIdToNewIdMap.put(oldId, newId);
						
					}
				}
				{
					ArrayList<PetSuitArticleEntity> articles = (ArrayList<PetSuitArticleEntity>)convertToTypeReferenceType(linkedHashMap.get(PetSuitArticleEntity.class.getName()), new TypeReference<ArrayList<PetSuitArticleEntity>>(){});	
					SimpleEntityManager entityManager = ArticleEntityManager.getInstance().em;
					for (PetSuitArticleEntity articleEntity : articles) {
						long newId = entityManager.nextId();
						long oldId = articleEntity.getId();
						articleEntity.setId(newId);
						
						Field versionField = UnitedServerManager2.getFieldByAnnotation(ArticleEntity.class, SimpleVersion.class);
						versionField.setAccessible(true);
						versionField.set(articleEntity, 0);
						
						entityManager.flush(articleEntity);
						
						ArticleEntityManager.log.warn("[角色转服] [原petSuitId:"+oldId+"] [新petSuitId:"+newId+"]");
						oldIdToNewIdMap.put(oldId, newId);
					}
				}
				{
					ArrayList<DigPropsEntity> articles = (ArrayList<DigPropsEntity>)convertToTypeReferenceType(linkedHashMap.get(DigPropsEntity.class.getName()), new TypeReference<ArrayList<DigPropsEntity>>(){});
					
					for(DigPropsEntity articleEntity : articles)
					{
						SimpleEntityManager entityManager = ArticleEntityManager.getInstance().em;
						long newId = entityManager.nextId();
						long oldId = articleEntity.getId();
						articleEntity.setId(newId);
						
						Field versionField = UnitedServerManager2.getFieldByAnnotation(ArticleEntity.class, SimpleVersion.class);
						versionField.setAccessible(true);
						versionField.set(articleEntity, 0);
						
						entityManager.flush(articleEntity);
						
						ArticleEntityManager.log.warn("[角色转服] [原DigPropsEntity:"+oldId+"] [新DigPropsEntity:"+newId+"]");
						oldIdToNewIdMap.put(oldId, newId);
						
					}
				}
				{
					ArrayList<YinPiaoEntity> articles = (ArrayList<YinPiaoEntity>)convertToTypeReferenceType(linkedHashMap.get(YinPiaoEntity.class.getName()), new TypeReference<ArrayList<YinPiaoEntity>>(){});
					
					for(YinPiaoEntity articleEntity : articles)
					{
						SimpleEntityManager entityManager = ArticleEntityManager.getInstance().em;
						long newId = entityManager.nextId();
						long oldId = articleEntity.getId();
						articleEntity.setId(newId);
						
						Field versionField = UnitedServerManager2.getFieldByAnnotation(ArticleEntity.class, SimpleVersion.class);
						versionField.setAccessible(true);
						versionField.set(articleEntity, 0);
						
						entityManager.flush(articleEntity);
						
						
						ArticleEntityManager.log.warn("[角色转服] [原YinPiaoEntity:"+oldId+"] [新YinPiaoEntity:"+newId+"]");
						oldIdToNewIdMap.put(oldId, newId);
						
					}
				}
				{
					ArrayList<QiLingArticleEntity> articles = (ArrayList<QiLingArticleEntity>)convertToTypeReferenceType(linkedHashMap.get(QiLingArticleEntity.class.getName()), new TypeReference<ArrayList<QiLingArticleEntity>>(){});
					
					for(QiLingArticleEntity articleEntity : articles)
					{
						SimpleEntityManager entityManager = ArticleEntityManager.getInstance().em;
						long newId = entityManager.nextId();
						long oldId = articleEntity.getId();
						articleEntity.setId(newId);
						
						Field versionField = UnitedServerManager2.getFieldByAnnotation(ArticleEntity.class, SimpleVersion.class);
						versionField.setAccessible(true);
						versionField.set(articleEntity, 0);
						
						entityManager.flush(articleEntity);
						
						ArticleEntityManager.log.warn("[角色转服] [原QiLingArticleEntity:"+oldId+"] [新QiLingArticleEntity:"+newId+"]");
						oldIdToNewIdMap.put(oldId, newId);
						
					}
				}
				{
					ArrayList<SingleForPetPropsEntity> articles = (ArrayList<SingleForPetPropsEntity>)convertToTypeReferenceType(linkedHashMap.get(SingleForPetPropsEntity.class.getName()), new TypeReference<ArrayList<SingleForPetPropsEntity>>(){});
					
					for(SingleForPetPropsEntity articleEntity : articles)
					{
						SimpleEntityManager entityManager = ArticleEntityManager.getInstance().em;
						long newId = entityManager.nextId();
						long oldId = articleEntity.getId();
						articleEntity.setId(newId);
						
						Field versionField = UnitedServerManager2.getFieldByAnnotation(ArticleEntity.class, SimpleVersion.class);
						versionField.setAccessible(true);
						versionField.set(articleEntity, 0);
						
						entityManager.flush(articleEntity);
						ArticleEntityManager.log.warn("[角色转服] [原SingleForPetPropsEntity:"+oldId+"] [新SingleForPetPropsEntity:"+newId+"]");
						oldIdToNewIdMap.put(oldId, newId);
						
					}
				}
				
				{
					ArrayList<ExploreResourceMapEntity> articles = (ArrayList<ExploreResourceMapEntity>)convertToTypeReferenceType(linkedHashMap.get(ExploreResourceMapEntity.class.getName()), new TypeReference<ArrayList<ExploreResourceMapEntity>>(){});
					
					for(ExploreResourceMapEntity articleEntity : articles)
					{
						SimpleEntityManager entityManager = ArticleEntityManager.getInstance().em;
						long newId = entityManager.nextId();
						long oldId = articleEntity.getId();
						articleEntity.setId(newId);
						
						Field versionField = UnitedServerManager2.getFieldByAnnotation(ArticleEntity.class, SimpleVersion.class);
						versionField.setAccessible(true);
						versionField.set(articleEntity, 0);
						
						entityManager.flush(articleEntity);
						ArticleEntityManager.log.warn("[角色转服] [原ExploreResourceMapEntity:"+oldId+"] [新ExploreResourceMapEntity:"+newId+"]");
						oldIdToNewIdMap.put(oldId, newId);
						
					}
				}
				{
					ArrayList<Special_2EquipmentEntity> articles = (ArrayList<Special_2EquipmentEntity>)convertToTypeReferenceType(linkedHashMap.get(Special_2EquipmentEntity.class.getName()), new TypeReference<ArrayList<Special_2EquipmentEntity>>(){});
					
					for(Special_2EquipmentEntity articleEntity : articles)
					{
						SimpleEntityManager entityManager = ArticleEntityManager.getInstance().em;
						long newId = entityManager.nextId();
						long oldId = articleEntity.getId();
						articleEntity.setId(newId);
						
						Field versionField = UnitedServerManager2.getFieldByAnnotation(ArticleEntity.class, SimpleVersion.class);
						versionField.setAccessible(true);
						versionField.set(articleEntity, 0);
						
						articleEntity.setOwnerId(player.getId());
						
						entityManager.flush(articleEntity);
						ArticleEntityManager.log.warn("[角色转服] [原Special_2EquipmentEntity:"+oldId+"] [新Special_2EquipmentEntity:"+newId+"]");
						oldIdToNewIdMap.put(oldId, newId);
						
					}
				}
				{
					ArrayList<Special_1EquipmentEntity> articles = (ArrayList<Special_1EquipmentEntity>)convertToTypeReferenceType(linkedHashMap.get(Special_1EquipmentEntity.class.getName()), new TypeReference<ArrayList<Special_1EquipmentEntity>>(){});
					
					for(Special_1EquipmentEntity articleEntity : articles)
					{
						SimpleEntityManager entityManager = ArticleEntityManager.getInstance().em;
						long newId = entityManager.nextId();
						long oldId = articleEntity.getId();
						articleEntity.setId(newId);
						
						Field versionField = UnitedServerManager2.getFieldByAnnotation(ArticleEntity.class, SimpleVersion.class);
						versionField.setAccessible(true);
						versionField.set(articleEntity, 0);
						
						articleEntity.setPlayerId(player.getId());
						entityManager.flush(articleEntity);
						
						ArticleEntityManager.log.warn("[角色转服] [原Special_1EquipmentEntity:"+oldId+"] [新Special_1EquipmentEntity:"+newId+"]");
						oldIdToNewIdMap.put(oldId, newId);
						
					}
				}
				{
					ArrayList<PropsEntity> articles = (ArrayList<PropsEntity>)convertToTypeReferenceType(linkedHashMap.get(PropsEntity.class.getName()), new TypeReference<ArrayList<PropsEntity>>(){});
					
					for(PropsEntity articleEntity : articles)
					{
						SimpleEntityManager entityManager = ArticleEntityManager.getInstance().em;
						long newId = entityManager.nextId();
						long oldId = articleEntity.getId();
						articleEntity.setId(newId);
						
						Field versionField = UnitedServerManager2.getFieldByAnnotation(ArticleEntity.class, SimpleVersion.class);
						versionField.setAccessible(true);
						versionField.set(articleEntity, 0);
						
						entityManager.flush(articleEntity);
						ArticleEntityManager.log.warn("[角色转服] [原PropsEntity:"+oldId+"] [新PropsEntity:"+newId+"]");
						oldIdToNewIdMap.put(oldId, newId);
						
					}
				}
				
				
				
				{
					ArrayList<PetPropsEntity> articles = (ArrayList<PetPropsEntity>)convertToTypeReferenceType(linkedHashMap.get(PetPropsEntity.class.getName()), new TypeReference<ArrayList<PetPropsEntity>>(){});
					
					for(PetPropsEntity articleEntity : articles)
					{
						SimpleEntityManager entityManager = ArticleEntityManager.getInstance().em;
						long newId = entityManager.nextId();
						long oldId = articleEntity.getId();
						articleEntity.setId(newId);
						
						
						Field versionField = UnitedServerManager2.getFieldByAnnotation(ArticleEntity.class, SimpleVersion.class);
						versionField.setAccessible(true);
						versionField.set(articleEntity, 0);
						
						entityManager.flush(articleEntity);
						ArticleEntityManager.log.warn("[角色转服] [原PetPropsEntity:"+oldId+"] [新PetPropsEntity:"+newId+"]");
						oldIdToNewIdMap.put(oldId, newId);
						morePetPropsEntitys.add(articleEntity);
					}
				}
				
				
				{
					ArrayList<PetEggPropsEntity> articles = (ArrayList<PetEggPropsEntity>)convertToTypeReferenceType(linkedHashMap.get(PetEggPropsEntity.class.getName()), new TypeReference<ArrayList<PetEggPropsEntity>>(){});
					
					for(PetEggPropsEntity articleEntity : articles)
					{
						SimpleEntityManager entityManager = ArticleEntityManager.getInstance().em;
						long newId = entityManager.nextId();
						long oldId = articleEntity.getId();
						articleEntity.setId(newId);
						Field versionField = UnitedServerManager2.getFieldByAnnotation(ArticleEntity.class, SimpleVersion.class);
						versionField.setAccessible(true);
						versionField.set(articleEntity, 0);
						
						entityManager.flush(articleEntity);
						ArticleEntityManager.log.warn("[角色转服] [原PetEggPropsEntity:"+oldId+"] [新PetEggPropsEntity:"+newId+"]");
						oldIdToNewIdMap.put(oldId, newId);
						morePetEggPropsEntitys.add(articleEntity);
					}
				}
				{
					ArrayList<MagicWeaponEntity> articles = (ArrayList<MagicWeaponEntity>)convertToTypeReferenceType(linkedHashMap.get(MagicWeaponEntity.class.getName()), new TypeReference<ArrayList<MagicWeaponEntity>>(){});
					
					for(MagicWeaponEntity articleEntity : articles)
					{
						SimpleEntityManager entityManager = ArticleEntityManager.getInstance().em;
						long newId = entityManager.nextId();
						long oldId = articleEntity.getId();
						articleEntity.setId(newId);
						
						Field versionField = UnitedServerManager2.getFieldByAnnotation(ArticleEntity.class, SimpleVersion.class);
						versionField.setAccessible(true);
						versionField.set(articleEntity, 0);
						
						entityManager.flush(articleEntity);
						ArticleEntityManager.log.warn("[角色转服] [原MagicWeaponEntity:"+oldId+"] [新MagicWeaponEntity:"+newId+"]");
						oldIdToNewIdMap.put(oldId, newId);
						
					}
				}
				{
					ArrayList<FateActivityPropsEntity> articles = (ArrayList<FateActivityPropsEntity>)convertToTypeReferenceType(linkedHashMap.get(FateActivityPropsEntity.class.getName()), new TypeReference<ArrayList<FateActivityPropsEntity>>(){});
					
					for(FateActivityPropsEntity articleEntity : articles)
					{
						SimpleEntityManager entityManager = ArticleEntityManager.getInstance().em;
						long newId = entityManager.nextId();
						long oldId = articleEntity.getId();
						articleEntity.setId(newId);
						
						Field versionField = UnitedServerManager2.getFieldByAnnotation(ArticleEntity.class, SimpleVersion.class);
						versionField.setAccessible(true);
						versionField.set(articleEntity, 0);
						
						entityManager.flush(articleEntity);
						ArticleEntityManager.log.warn("[角色转服] [原FateActivityPropsEntity:"+oldId+"] [新FateActivityPropsEntity:"+newId+"]");
						oldIdToNewIdMap.put(oldId, newId);
						
					}
				}
				
				ArrayList<ExchangeArticleEntity> exchangeArticleEntities = new ArrayList<ExchangeArticleEntity>();
				{
					ArrayList<ExchangeArticleEntity> articles = (ArrayList<ExchangeArticleEntity>)convertToTypeReferenceType(linkedHashMap.get(ExchangeArticleEntity.class.getName()), new TypeReference<ArrayList<ExchangeArticleEntity>>(){});
					
					for(ExchangeArticleEntity articleEntity : articles)
					{
						SimpleEntityManager entityManager = ArticleEntityManager.getInstance().em;
						long newId = entityManager.nextId();
						long oldId = articleEntity.getId();
						articleEntity.setId(newId);
						
						Field versionField = UnitedServerManager2.getFieldByAnnotation(ArticleEntity.class, SimpleVersion.class);
						versionField.setAccessible(true);
						versionField.set(articleEntity, 0);
						
						entityManager.flush(articleEntity);
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
						SimpleEntityManager entityManager = ArticleEntityManager.getInstance().em;
						long newId = entityManager.nextId();
						long oldId = articleEntity.getId();
						articleEntity.setId(newId);
						
						Field versionField = UnitedServerManager2.getFieldByAnnotation(ArticleEntity.class, SimpleVersion.class);
						versionField.setAccessible(true);
						versionField.set(articleEntity, 0);
						
						entityManager.flush(articleEntity);
						oldIdToNewIdMap.put(oldId, newId);
						
						ArticleEntityManager.log.warn("[角色转服] [原BottlePropsEntity:"+oldId+"] [新BottlePropsEntity:"+newId+"]");
						
					}
				}
				{
				
					ArrayList<BiWuArticleEntity> articles = (ArrayList<BiWuArticleEntity>)convertToTypeReferenceType(linkedHashMap.get(BiWuArticleEntity.class.getName()), new TypeReference<ArrayList<BiWuArticleEntity>>(){});
					
					for(BiWuArticleEntity articleEntity : articles)
					{
						SimpleEntityManager entityManager = ArticleEntityManager.getInstance().em;
						long newId = entityManager.nextId();
						long oldId = articleEntity.getId();
						articleEntity.setId(newId);
						
						Field versionField = UnitedServerManager2.getFieldByAnnotation(ArticleEntity.class, SimpleVersion.class);
						versionField.setAccessible(true);
						versionField.set(articleEntity, 0);
						
						entityManager.flush(articleEntity);
						oldIdToNewIdMap.put(oldId, newId);
						ArticleEntityManager.log.warn("[角色转服] [原BiWuArticleEntity:"+oldId+"] [新BiWuArticleEntity:"+newId+"]");
					}
				}
				
				{
					ArrayList<ArticleEntity> articles = (ArrayList<ArticleEntity>)convertToTypeReferenceType(linkedHashMap.get(ArticleEntity.class.getName()), new TypeReference<ArrayList<ArticleEntity>>(){});
					ArrayList<BaoShiXiaZiData> baoshixiazis = (ArrayList<BaoShiXiaZiData>)convertToTypeReferenceType(linkedHashMap.get(BaoShiXiaZiData.class.getName()), new TypeReference<ArrayList<BaoShiXiaZiData>>(){});
					Field baoshiVer = UnitedServerManager2.getFieldByAnnotation(BaoShiXiaZiData.class, SimpleVersion.class);
					baoshiVer.setAccessible(true);
					for(ArticleEntity articleEntity : articles)
					{
						SimpleEntityManager entityManager = ArticleEntityManager.getInstance().em;
						long newId = entityManager.nextId();
						long oldId = articleEntity.getId();
						articleEntity.setId(newId);
						
						Field versionField = UnitedServerManager2.getFieldByAnnotation(ArticleEntity.class, SimpleVersion.class);
						versionField.setAccessible(true);
						versionField.set(articleEntity, 0);
						
						entityManager.flush(articleEntity);
						oldIdToNewIdMap.put(oldId, newId);
						ArticleEntityManager.log.warn("[角色转服] [原ArticleEntity:"+oldId+"] [新ArticleEntity:"+newId+"]");
					}
					for (BaoShiXiaZiData bxd : baoshixiazis) {
						long oldId = bxd.getId();
						if (oldIdToNewIdMap.containsKey(oldId)) {
							long newId = oldIdToNewIdMap.get(bxd.getId());
							baoshiVer.set(bxd, 0);
							bxd.setId(newId);
							for (int k=0; k<bxd.getIds().length; k++) {
								if (bxd.getIds()[k] > 0) {
									Long newBaoshiId = oldIdToNewIdMap.get(bxd.getIds()[k]);
									if (newBaoshiId != null && newBaoshiId > 0) {
										bxd.getIds()[k] = newBaoshiId;
									}
								}
							}
							ArticleEntityManager.baoShiXiLianEM.flush(bxd);
						} else {
							ArticleEntityManager.log.warn("[角色转服] [宝石匣子转移失败] [宝石匣子id没找到对应新的id] [oldId:" + oldId + "] [" + player.getLogString() + "]");
						}
					}
				}
				
				{
					ArrayList<EquipmentEntity> articles = (ArrayList<EquipmentEntity>)convertToTypeReferenceType(linkedHashMap.get(EquipmentEntity.class.getName()), new TypeReference<ArrayList<EquipmentEntity>>(){});
					ArrayList<EnchantData> enchants = (ArrayList<EnchantData>)convertToTypeReferenceType(linkedHashMap.get(EnchantData.class.getName()), new TypeReference<ArrayList<EnchantData>>(){});
					ArrayList<HorseEquInlay> horseInlays = (ArrayList<HorseEquInlay>)convertToTypeReferenceType(linkedHashMap.get(HorseEquInlay.class.getName()), new TypeReference<ArrayList<HorseEquInlay>>(){});
					Field enVersion = UnitedServerManager2.getFieldByAnnotation(EnchantData.class, SimpleVersion.class);
					Field heiVersion = UnitedServerManager2.getFieldByAnnotation(HorseEquInlay.class, SimpleVersion.class);
					enVersion.setAccessible(true);
					heiVersion.setAccessible(true);
					for(EquipmentEntity articleEntity : articles)
					{
						SimpleEntityManager entityManager = ArticleEntityManager.getInstance().em;
						long newId = entityManager.nextId();
						long oldId = articleEntity.getId();
						articleEntity.setId(newId);
						
						for (EnchantData en : enchants) {
							if (en.getId() == oldId) {
								enVersion.set(en, 0);
								en.setId(newId);
								EnchantEntityManager.em.flush(en);
							}
						}
						if (horseInlays != null) {
							for (HorseEquInlay hei : horseInlays) {
								if (hei.getId() == oldId) {
									heiVersion.set(hei, 0);
									hei.setId(newId);
									long[] horseInlayIds = hei.getInlayArticleIds();
									if (horseInlayIds == null) {
										TransitRobberyManager.logger.warn("[转角色] [坐骑装备镶嵌属性为空:" + hei.getId() + "<br>");
										continue;
									}
									for (int kk=0; kk<horseInlayIds.length; kk++) {
										Long ll = oldIdToNewIdMap.get(horseInlayIds[kk]);
										if (ll == null) {
											TransitRobberyManager.logger.warn("[转角色] [坐骑装备镶嵌匣子id不存在:" + hei.getId() + "] ["+horseInlayIds[kk]+"]<br>");
											continue;
										}
										horseInlayIds[kk] = oldIdToNewIdMap.get(horseInlayIds[kk]);
									}
									hei.setInlayArticleIds(horseInlayIds);
									HorseEquInlayEntityManager.em.flush(hei);
								}	
							}
						}
						
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
						
						entityManager.flush(articleEntity);
						oldIdToNewIdMap.put(oldId, newId);
						ArticleEntityManager.log.warn("[角色转服] [原EquipmentEntity:"+oldId+"] [新EquipmentEntity:"+newId+"]");
					}
				}
				
				Map<Long,Long> linsuimap = new HashMap<Long,Long>();
				{			//灵髓道具
					if (linkedHashMap.get(SoulPithArticleEntity.class.getName()) != null) {
						ArrayList<SoulPithArticleEntity> huntLifeAes = (ArrayList<SoulPithArticleEntity>)convertToTypeReferenceType(linkedHashMap.get(SoulPithArticleEntity.class.getName()), new TypeReference<ArrayList<SoulPithArticleEntity>>(){});
						ArrayList<SoulPithAeData> extraData = (ArrayList<SoulPithAeData>)convertToTypeReferenceType(linkedHashMap.get(SoulPithAeData.class.getName()), new TypeReference<ArrayList<SoulPithAeData>>(){});
						for  (SoulPithArticleEntity ae : huntLifeAes) {
							long oldId = ae.getId();
							long newId = DefaultArticleEntityManager.getInstance().em.nextId();
							Field versionField = UnitedServerManager2.getFieldByAnnotation(ArticleEntity.class, SimpleVersion.class);
							versionField.setAccessible(true);
							Field versionField2 = UnitedServerManager2.getFieldByAnnotation(SoulPithAeData.class, SimpleVersion.class);
							versionField2.setAccessible(true);
							versionField.set(ae, 0);
							for (SoulPithAeData spad : extraData) {
								long oldHid = spad.getId();
								if (oldHid == oldId) {
									versionField2.set(spad, 0);
									spad.setId(newId);
									SoulPithEntityManager.em_ae.flush(spad);
									break;
								}
							}
							ae.setId(newId);
							DefaultArticleEntityManager.getInstance().em.flush(ae);
							linsuimap.put(oldId, newId);
							oldIdToNewIdMap.put(oldId, newId);
							SoulPithEntityManager.logger.warn("[转移灵髓道具] [oldId:" + oldId + "] [newId:" + newId + "]");
						}
					}
				}
				
				{		//灵髓面板
					if (linkedHashMap.get("soulpithentity") != null) {
						SoulPithEntity entity = (SoulPithEntity)convertToSpecialedType( linkedHashMap.get("soulpithentity"),SoulPithEntity.class);
						for (int i=0; i<entity.getPithInfos().size(); i++) {
							for (int ii=0; ii<entity.getPithInfos().get(i).getPiths().length; ii++) {
								long id = entity.getPithInfos().get(i).getPiths()[ii];
								if (id > 0) {
									if (oldIdToNewIdMap.containsKey(id)) {
										entity.getPithInfos().get(i).getPiths()[ii] = oldIdToNewIdMap.get(id);
										SoulPithEntityManager.logger.warn("[转移灵髓道具] [改变道具id] [成功] [oldId:" + id + "] [newId:" + entity.getPithInfos().get(i).getPiths()[ii] + "]");
									} else {
										SoulPithEntityManager.logger.warn("[转移灵髓道具] [改变道具id] [失败] [oldId:" + id + "]");
									}
								}
							}
						}
						Field versionField = UnitedServerManager2.getFieldByAnnotation(SoulPithEntity.class, SimpleVersion.class);
						versionField.setAccessible(true);
						versionField.set(entity, 0);
						entity.setId(player.getId());
						SoulPithEntityManager.em.flush(entity);
					}
				}
				{		//"xianlingData"
					if (linkedHashMap.get("xianlingData") != null) {
						PlayerXianLingData entity = (PlayerXianLingData)convertToSpecialedType( linkedHashMap.get("xianlingData"),PlayerXianLingData.class);
						Field versionField = UnitedServerManager2.getFieldByAnnotation(PlayerXianLingData.class, SimpleVersion.class);
						versionField.setAccessible(true);
						versionField.set(entity, 0);
						entity.setId(player.getId());
						XianLingManager.xianlingEm.flush(entity);
					}
				}
				{		//仙灵
					
				}
				
				Map<Long,Long> hunshimap = new HashMap<Long,Long>();
				{		//魂石道具
					if (linkedHashMap.get(HunshiEntity.class.getName()) != null) {
						ArrayList<HunshiEntity> hunshiaes = (ArrayList<HunshiEntity>)convertToTypeReferenceType(linkedHashMap.get(HunshiEntity.class.getName()), new TypeReference<ArrayList<HunshiEntity>>(){});
						for (HunshiEntity ae : hunshiaes) {
							long oldId = ae.getId();
							long newId = DefaultArticleEntityManager.getInstance().em.nextId();
							Field versionField = UnitedServerManager2.getFieldByAnnotation(ArticleEntity.class, SimpleVersion.class);
							versionField.setAccessible(true);
							versionField.set(ae, 0);
							ae.setId(newId);
							DefaultArticleEntityManager.getInstance().em.flush(ae);
							hunshimap.put(oldId, newId);
							oldIdToNewIdMap.put(oldId, newId);
							HuntLifeEntityManager.logger.warn("[转移魂石道具] [oldId:" + oldId + "] [newId:" + newId + "]");
						}
					}
				}
				
				QianCengTaManager qianCengTaManager = QianCengTaManager
						.getInstance();
			
				QianCengTa_Ta qianCengTa_Ta = qianCengTaManager.em
						.find(player.getId());
			
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
					
					QianCengTaManager.getInstance().em.flush(qianCengTa_Ta);
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
				if (player.getKnapsacks_warehouse() != null) {
					for (int i = 0; i < player.getKnapsacks_warehouse().size(); i++) {
						try {
							if (player.getKnapsacks_warehouse().getCell(i) != null) {
								if (player.getKnapsacks_warehouse().getCell(i)
										.getEntityId() > 0) {
									if(oldIdToNewIdMap.get(player.getKnapsacks_warehouse().getCell(i)
											.getEntityId())!=null)
									{
										long newId = oldIdToNewIdMap.get(player.getKnapsacks_warehouse().getCell(i)
												.getEntityId());
										player.getKnapsacks_warehouse().getCell(i).setEntityId(newId);
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
				

				
				
				((GamePlayerManager) GamePlayerManager.getInstance()).em.flush(player);
					
			}
			
			{
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
			}
			
			{
				if(linkedHashMap.get("taskentitys") != null)
				{
					List<TaskEntity> taskEntitys  = (List<TaskEntity>)convertToTypeReferenceType(linkedHashMap.get("taskentitys"), new TypeReference<List<TaskEntity>>(){});
					for(TaskEntity taskEntity : taskEntitys)
					{
						SimpleEntityManager entityManager = TaskEntityManager.getInstance().em;
						long newId = entityManager.nextId();
						long oldId = taskEntity.getId();
						taskEntity.setId(newId);
						taskEntity.setPlayerId(player.getId());
						
						Field versionField = UnitedServerManager2.getFieldByAnnotation(TaskEntity.class, SimpleVersion.class);
						versionField.setAccessible(true);
						versionField.set(taskEntity, 0);
						
						entityManager.flush(taskEntity);
						oldIdToNewIdMap.put(oldId, newId);
						DeliverTaskManager.logger.warn("[角色转服] [原TaskEntity:"+oldId+"] [新TaskEntity:"+newId+"]");
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
						SimpleEntityManager entityManager = AchievementManager.getInstance().gameDREm;
						long newId = entityManager.nextId();
						long oldId = gameDataRecord.getId();
						gameDataRecord.setId(newId);
						gameDataRecord.setPlayerId(player.getId());
						
						
						Field versionField = UnitedServerManager2.getFieldByAnnotation(GameDataRecord.class, SimpleVersion.class);
						versionField.setAccessible(true);
						versionField.set(gameDataRecord, 0);
						
						entityManager.flush(gameDataRecord);
						oldIdToNewIdMap.put(oldId, newId);
						AchievementManager.logger.warn("[角色转服] [原GameDataRecord:"+oldId+"] [新GameDataRecord:"+newId+"]");
					}
					
				}
			}
			
			{
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
			}
			
			
			{
				if(linkedHashMap.get("hotspotinfos") != null)
				{
					List<HotspotInfo>  list = (List<HotspotInfo> )convertToTypeReferenceType(linkedHashMap.get("hotspotinfos"), new TypeReference<List<HotspotInfo>  >(){});
					for(HotspotInfo hotspotInfo : list)
					{
						SimpleEntityManager entityManager = HotspotManager.getInstance().em;
						long newId = entityManager.nextId();
						long oldId = hotspotInfo.getId();
						hotspotInfo.setId(newId);
						hotspotInfo.setPlayerID(player.getId());
						
						Field versionField = UnitedServerManager2.getFieldByAnnotation(HotspotInfo.class, SimpleVersion.class);
						versionField.setAccessible(true);
						versionField.set(hotspotInfo, 0);
						
						entityManager.flush(hotspotInfo);
						oldIdToNewIdMap.put(oldId, newId);
						
						HotspotManager.logger.warn("[角色转服] [原HotspotInfo:"+oldId+"] [新HotspotInfo:"+newId+"]");
						
					}
				}
			}
			
			{
				if(linkedHashMap.get("pets") != null)
				{
					List<PetFlyState> petFlys = null;
					if (linkedHashMap.get("PetFlyState") != null) {
						petFlys = (List<PetFlyState> )convertToTypeReferenceType(linkedHashMap.get("PetFlyState"), new TypeReference<List<PetFlyState> >(){});
					}
					List<Pet>  list = (List<Pet> )convertToTypeReferenceType(linkedHashMap.get("pets"), new TypeReference<List<Pet> >(){});
					for(Pet pet : list)
					{
						SimpleEntityManager entityManager = PetManager.getInstance().em;
						long newId = entityManager.nextId();
						long oldId = pet.getId();
						pet.setId(newId);
						
						if(pet.getOwnerId() > 0)
							pet.setOwnerId(player.getId());
						
						long oldPropsId = pet.getPetPropsId();
						long curPropsId =  0;
						
						if (petFlys != null && petFlys.size() > 0) {
							for (int i=0; i<petFlys.size(); i++) {
								if (PetManager.em_state.count(Pet.class, "id=?", new Object[]{oldId}) > 0) {
									continue;
								}
								if (oldId == petFlys.get(i).getId()) {
									petFlys.get(i).setId(newId);
									Field versionField = UnitedServerManager2.getFieldByAnnotation(PetFlyState.class, SimpleVersion.class);
									versionField.setAccessible(true);
									versionField.set(petFlys.get(i), 0);
									PetManager.em_state.flush(petFlys.get(i));
								}
							}
						}
						
						if(oldIdToNewIdMap.get(oldPropsId) != null)
						{
							curPropsId = oldIdToNewIdMap.get(oldPropsId);
						}
						
						if(curPropsId > 0)
						{
							PropsEntity petPropsEntity =(PropsEntity)ArticleEntityManager.getInstance().getEntity(curPropsId);
							
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
						for (int k=0; k<pet.getOrnaments().length; k++) {
							long oId = pet.getOrnaments()[k];
							if (oId > 0) {
								Long ddd = oldIdToNewIdMap.get(oId);
								if (ddd != null && ddd > 0) {
									pet.getOrnaments()[k] = ddd;
								}
								ArticleManager.logger.warn("[转角色] [宠物饰品id:" + oId + " -> " + ddd + "] [petId:" + pet.getId() + "]");
							}
						}
						
						Field versionField = UnitedServerManager2.getFieldByAnnotation(Pet.class, SimpleVersion.class);
						versionField.setAccessible(true);
						versionField.set(pet, 0);
						
						entityManager.flush(pet);
						oldIdToNewIdMap.put(oldId, newId);
						PetManager.logger.warn("[角色转服] [原Pet:"+oldId+"] [新Pet:"+newId+"]");
					}
				
					
					
				}
				
				
				{
					for(PetPropsEntity articleEntity : morePetPropsEntitys)
					{
						SimpleEntityManager entityManager = ArticleEntityManager.getInstance().em;
						if(articleEntity.getPetId() > 0)
						{
							if(oldIdToNewIdMap.get(articleEntity.getPetId()) != null)
							{
								long oldId = articleEntity.getPetId();
								long newId = oldIdToNewIdMap.get(oldId);
								articleEntity.setPetId(newId);
								
								PetManager.logger.warn("[角色转服] [设置petid] [id:"+articleEntity.getId()+"] [oldid:"+oldId+"] [newId:"+newId+"]");
							}
						}
						entityManager.flush(articleEntity);
					}
				}
				
				
				{
					for(PetEggPropsEntity articleEntity : morePetEggPropsEntitys)
					{
						SimpleEntityManager entityManager = ArticleEntityManager.getInstance().em;
						if(articleEntity.getPetId() > 0)
						{
							if(oldIdToNewIdMap.get(articleEntity.getPetId()) != null)
							{
								long oldId = articleEntity.getPetId();
								long newId = oldIdToNewIdMap.get(oldId);
								articleEntity.setPetId(newId);
								PetManager.logger.warn("[角色转服] [设置petid] [id:"+articleEntity.getId()+"] [oldid:"+oldId+"] [newId:"+newId+"]");
							}
						}
						entityManager.flush(articleEntity);
					}
				}
				
				
			}
			
			{
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
							 
							 Pet2Manager.inst.petsOfPlayerBeanEm.flush(petsOfPlayer);
							 
						 }
						 
					 }
				}
			} 
			
			{
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
						 
						 ActivenessManager.getInstance().em.flush(playerActivenessInfo);
						 
					 }
				}
			} 
			
			{
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
			} 
			
			{
				if(linkedHashMap.get("articleprotectdatas") != null)
				{
					List<ArticleProtectData> list = (List<ArticleProtectData> )convertToTypeReferenceType(linkedHashMap.get("articleprotectdatas"), new TypeReference<List<ArticleProtectData>  >(){});
					for(ArticleProtectData articleProtectData : list)
					{
						SimpleEntityManager entityManager = PlayerArticleProtectData.em;
						long newId = entityManager.nextId();
						long oldId = articleProtectData.getId();
						articleProtectData.setId(newId);
						articleProtectData.setPlayerID(player.getId());
						
						Field versionField = UnitedServerManager2.getFieldByAnnotation(ArticleProtectData.class, SimpleVersion.class);
						versionField.setAccessible(true);
						versionField.set(articleProtectData, 0);
						
						entityManager.flush(articleProtectData);
						oldIdToNewIdMap.put(oldId, newId);
						ArticleProtectManager.logger.warn("[角色转服] [原ArticleProtectData:"+oldId+"] [新ArticleProtectData:"+newId+"]");
					}
				}
			}
			
			{
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
						 
						 SkEnhanceManager.getInst().em.flush(skBean);
					 }
				}
			} 
			{
				if (linkedHashMap.get("JiazuMember2") != null) {
					JiazuMember2 jm2 = (JiazuMember2)convertToSpecialedType( linkedHashMap.get("JiazuMember2"),JiazuMember2.class);
					if(oldIdToNewIdMap.get(jm2.getId()) != null)
					 {
						long newPid = oldIdToNewIdMap.get(jm2.getId());
						Field versionField = UnitedServerManager2.getFieldByAnnotation(JiazuMember2.class, SimpleVersion.class);
						versionField.setAccessible(true);
						versionField.set(jm2, 0);
						jm2.setId(newPid);
						JiazuEntityManager2.em.flush(jm2);
					 }
				}
			}
			
			{
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
						 
						 TransitRobberyEntityManager.getInstance().em.flush(transitRobberyEntity);
						 
					 }
				}
			} 
			
			{
				if(linkedHashMap.get("buffsaves") != null)
				{
					List<BuffSave> list = (List<BuffSave> )convertToTypeReferenceType(linkedHashMap.get("buffsaves"), new TypeReference<List<BuffSave>  >(){});
					for(BuffSave buffSave : list)
					{
						SimpleEntityManager entityManager = BufferSaveManager.getInstance().bem;
						long newId = entityManager.nextId();
						long oldId = buffSave.getSaveID();
						buffSave.setSaveID(newId);
						buffSave.setPid(player.getId());
						
						Field versionField = UnitedServerManager2.getFieldByAnnotation(BuffSave.class, SimpleVersion.class);
						versionField.setAccessible(true);
						versionField.set(buffSave, 0);
						
						entityManager.flush(buffSave);
						oldIdToNewIdMap.put(oldId, newId);
						BufferSaveManager.log.warn("[角色转服] [原BuffSave:"+oldId+"] [新BuffSave:"+newId+"]");
					}
				}
			}
			
			{
				if(linkedHashMap.get("newdelivertasks") != null)
				{
					List<NewDeliverTask> newDeliverTasks = (List<NewDeliverTask> )convertToTypeReferenceType(linkedHashMap.get("newdelivertasks"), new TypeReference<List<NewDeliverTask>>(){});
					for(NewDeliverTask newDeliverTask : newDeliverTasks)
					{
						SimpleEntityManager entityManager = NewDeliverTaskManager.getInstance().em;
						
						long oldId = newDeliverTask.getId();
						NewDeliverTask newDeliverTask2 = new NewDeliverTask(player.getId(),newDeliverTask.getTaskId());
						long newId = newDeliverTask2.getId();
						
						Field versionField = UnitedServerManager2.getFieldByAnnotation(NewDeliverTask.class, SimpleVersion.class);
						versionField.setAccessible(true);
						versionField.set(newDeliverTask2, 0);
						
						entityManager.flush(newDeliverTask2);
						oldIdToNewIdMap.put(oldId, newId);
						NewDeliverTaskManager.logger.warn("[角色转服] [原NewDeliverTask:"+oldId+"] [新NewDeliverTask:"+newId+"]");
					}
				}
			}
			
			{
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
						 
						 Horse2EntityManager.instance.em.flush(horse2RelevantEntity);
					 }
				}
			} 
			
			{
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
						 
						 PlayerAimeEntityManager.instance.em.flush(playerAimsEntity);
						 
					 }
				}
			} 
			
			Map<Long,Long> shouhunmap = new HashMap<Long,Long>();
			{			//兽魂道具
				if (linkedHashMap.get(HuntLifeArticleEntity.class.getName()) != null) {
					ArrayList<HuntLifeArticleEntity> huntLifeAes = (ArrayList<HuntLifeArticleEntity>)convertToTypeReferenceType(linkedHashMap.get(HuntLifeArticleEntity.class.getName()), new TypeReference<ArrayList<HuntLifeArticleEntity>>(){});
					ArrayList<HuntArticleExtraData> extraData = (ArrayList<HuntArticleExtraData>)convertToTypeReferenceType(linkedHashMap.get(HuntArticleExtraData.class.getName()), new TypeReference<ArrayList<HuntArticleExtraData>>(){});
					for (HuntLifeArticleEntity ae : huntLifeAes) {
						long oldId = ae.getId();
						long newId = DefaultArticleEntityManager.getInstance().em.nextId();
						Field versionField = UnitedServerManager2.getFieldByAnnotation(ArticleEntity.class, SimpleVersion.class);
						versionField.setAccessible(true);
						Field versionField2 = UnitedServerManager2.getFieldByAnnotation(HuntArticleExtraData.class, SimpleVersion.class);
						versionField2.setAccessible(true);
						versionField.set(ae, 0);
						for (HuntArticleExtraData hed : extraData) {
							long oldHid = hed.getId();
							if (oldHid == oldId) {
								versionField2.set(hed, 0);
								hed.setId(newId);
								HuntLifeEntityManager.em_ae.flush(hed);
								break;
							}
						}
						ae.setId(newId);
						DefaultArticleEntityManager.getInstance().em.flush(ae);
						shouhunmap.put(oldId, newId);
						HuntLifeEntityManager.logger.warn("[转移兽魂道具] [oldId:" + oldId + "] [newId:" + newId + "]");
					}
				}
			}
			{
				long[] shouhunids = player.getShouhunKnap();
				for (int i=0; i<shouhunids.length; i++) {
					if (shouhunids[i] > 0) {
						long old = shouhunids[i];
						if (shouhunmap.containsKey(old)) {
							shouhunids[i] = shouhunmap.get(old);
							HuntLifeEntityManager.logger.warn("[转移兽魂道具] [oldId:" + old + "] [newId:" + shouhunids[i] + "]");
						}
					}
				}
			}
			{
				if (linkedHashMap.get("shouhun") != null) {
					HuntLifeEntity he = (HuntLifeEntity)convertToSpecialedType( linkedHashMap.get("shouhun"),HuntLifeEntity.class);
					if (he != null) {
						Field versionField = UnitedServerManager2.getFieldByAnnotation(HuntLifeEntity.class, SimpleVersion.class);
						long newId = HuntLifeEntityManager.em.nextId();
						he.setId(newId);
						versionField.setAccessible(true);
						versionField.set(he, 0);
						long[] datas = he.getHuntDatas();
						for (int i=0; i<datas.length; i++) {
							long old = datas[i];
							if (old > 0) {
								if (shouhunmap.containsKey(old)) {
									datas[i] = shouhunmap.get(old);
									HuntLifeEntityManager.logger.warn("[转移兽魂道具222] [oldId:" + old + "] [newId:" + datas[i] + "]");
								}
							}
						}
						HuntLifeEntityManager.logger.warn("[兽魂道具] ["+Arrays.toString(datas)+"]");
						he.setHuntDatas(datas);
						he.setId(player.getId());
						HuntLifeEntityManager.em.flush(he);
						player.setHuntLifr(HuntLifeEntityManager.instance.getHuntLifeEntity(player));
						//player.setHuntLifr(he);
					}
				}
			}
			{
				if(linkedHashMap.get("horses") != null)
				{
					ArrayList<Horse>  list = (ArrayList<Horse> )convertToTypeReferenceType(linkedHashMap.get("horses"), new TypeReference<ArrayList<Horse> >(){});
					for(Horse horse : list)
					{
						SimpleEntityManager entityManager = HorseManager.getInstance().em;
						long newId = entityManager.nextId();
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
						for (int i=0; i<horse.getHunshiArray().length; i++) {
							long id = horse.getHunshiArray()[i];
							if (id > 0) {
								if (oldIdToNewIdMap.containsKey(id)) {
									horse.getHunshiArray()[i] = oldIdToNewIdMap.get(id);
									HorseManager.logger.warn("[转服魂石道具] [转换坐骑身上魂石id] [成功] [oldId:" + id + "] [newId:"+ horse.getHunshiArray()[i] +"] [" + player.getLogString() + "]");
								} else {
									HorseManager.logger.warn("[转服魂石道具] [转换坐骑身上魂石id] [失败] [oldId:" + id + "] [" + player.getLogString() + "]");
								}
							}
						}
						for (int i=0; i<horse.getHunshi2Array().length; i++) {
							long id = horse.getHunshi2Array()[i];
							if (id > 0) {
								if (oldIdToNewIdMap.containsKey(id)) {
									horse.getHunshi2Array()[i] = oldIdToNewIdMap.get(id);
									HorseManager.logger.warn("[转服魂石道具2] [转换坐骑身上魂石id] [成功] [oldId:" + id + "] [newId:"+ horse.getHunshi2Array()[i] +"] [" + player.getLogString() + "]");
								} else {
									HorseManager.logger.warn("[转服魂石道具2] [转换坐骑身上魂石id] [失败] [oldId:" + id + "] [" + player.getLogString() + "]");
								}
							}
						}
						
						horse.getEquipmentColumn().setEquipmentIds(newHorseEquIDs);
						
						
						Field versionField = UnitedServerManager2.getFieldByAnnotation(Horse.class, SimpleVersion.class);
						versionField.setAccessible(true);
						versionField.set(horse, 0);
						
						entityManager.flush(horse);
						oldIdToNewIdMap.put(oldId, newId);
						
						HorseManager.logger.warn("[角色转服] [原Horse:"+oldId+"] [新Horse:"+newId+"]");
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
					((GamePlayerManager) GamePlayerManager.getInstance()).em.flush(player);
				}
			}
			
			{
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
						
						WingManager.em.flush(wp);
					}
					
				}
			}
			
			
			
			
			String username = ParamUtils.getParameter(request, "username", "");
			player.setNeedAddVitality(true);
			if(!StringUtils.isEmpty(username))
			{
				player.setUsername(username);
			}
			if (ctt != null && !ctt.isEmpty()) {
				player.setUsername(ctt);
			}
			((GamePlayerManager) GamePlayerManager.getInstance()).em.flush(player);
			TransitRobberyManager.logger.warn("[转移角色] [成功] [" + player.getLogString() + "]");
			
			out.print("success");
	}
	else
	{
		out.print("error");
	}
	}

%>
<form action="reiceiveplayer2.jsp" method="post">
		<input type="hidden" name="action" value="resetTumotie" />角色名:
		<input type="text" name="ctt" value="<%=ctt%>" />文件地址:
		<input type="text" name="fileName" value="<%=fileName%>" />
		<input type="submit" value="qwe123" />
	</form>
	<br />

