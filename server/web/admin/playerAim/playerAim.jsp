<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<%@page import="com.fy.engineserver.talent.FlyTalentManager"%>
<%@page import="com.fy.engineserver.talent.TalentData"%>
<%@page import="com.fy.engineserver.core.CoreSubSystem"%>
<%@page import="com.xuanzhi.boss.game.GameConstants"%>
<%@page import="com.fy.engineserver.sprite.PropsUseRecord"%>
<%@page import="com.fy.engineserver.tournament.manager.TournamentManager"%>
<%@page import="java.util.Calendar"%>
<%@page import="com.fy.engineserver.movedata.moveArticle.MoveArticleManager"%>
<%@page import="com.fy.engineserver.datasource.article.data.entity.PetEggPropsEntity"%>
<%@page import="com.xuanzhi.tools.simplejpa.annotation.SimpleVersion"%>
<%@page import="com.fy.engineserver.movedata.DataMoveManager"%>
<%@page import="java.lang.reflect.Field"%>
<%@page import="com.fy.engineserver.sprite.pet.Pet"%>
<%@page import="com.fy.engineserver.sprite.pet.PetManager"%>
<%@page import="com.fy.engineserver.datasource.article.data.entity.PetPropsEntity"%>
<%@page import="com.xuanzhi.tools.simplejpa.impl.DefaultSimpleEntityManagerFactory"%>1
<%@page import="com.xuanzhi.tools.simplejpa.SimpleEntityManager"%>
<%@page import="com.fy.engineserver.datasource.article.data.props.Knapsack"%>
<%@page import="com.fy.engineserver.achievement.RecordAction"%>
<%@page import="com.fy.engineserver.achievement.AchievementManager"%>
<%@page import="com.fy.engineserver.achievement.GameDataRecord"%>
<%@page import="com.fy.engineserver.datasource.article.data.entity.EquipmentEntity"%>
<%@page import="com.fy.engineserver.sprite.Soul"%>
<%@page import="com.fy.engineserver.articleProtect.ArticleProtectData"%>
<%@page import="com.fy.engineserver.articleProtect.ArticleProtectManager"%>
<%@page import="com.fy.engineserver.articleProtect.PlayerArticleProtectData"%>
<%@page import="com.fy.engineserver.newtask.service.TaskManager"%>
<%@page import="com.fy.engineserver.newtask.Task"%>
<%@page import="com.fy.engineserver.mail.service.MailManager"%>
<%@page import="java.util.Arrays"%>
<%@page import="com.fy.engineserver.event.EventRouter"%>
<%@page import="com.fy.engineserver.event.cate.EventWithObjParam"%>
<%@page import="com.fy.engineserver.datasource.article.concrete.DefaultArticleEntityManager"%>
<%@page import="java.text.DecimalFormat"%>
<%@page import="com.fy.engineserver.activity.wafen.model.FenMuModel"%>
<%@page import="com.fy.engineserver.activity.wafen.instacne.model.FenDiModel"%>
<%@page import="java.util.Map"%>
<%@page import="com.fy.engineserver.activity.wafen.instacne.WaFenInstance4Private"%>
<%@page import="com.fy.engineserver.activity.wafen.manager.WaFenManager"%>
<%@page import="com.fy.engineserver.playerAims.model.ChapterModel"%>
<%@page import="com.fy.engineserver.playerAims.instance.PlayerChapter"%>
<%@page import="com.fy.engineserver.playerAims.instance.PlayerAim"%>
<%@page import="com.fy.engineserver.util.CompoundReturn"%>
<%@page import="com.fy.engineserver.playerAims.model.PlayerAimModel"%>
<%@page import="com.fy.engineserver.playerAims.manager.PlayerAimeEntityManager"%>
<%@page import="com.fy.engineserver.playerAims.manager.PlayerAimManager"%>
<%@page import="com.fy.engineserver.playerAims.instance.PlayerAimsEntity"%>
<%@page import="com.fy.engineserver.carbon.devilSquare.model.DsPlayerDataModel"%>
<%@page import="com.fy.engineserver.sprite.monster.Monster"%>
<%@page import="com.fy.engineserver.datasource.article.manager.ArticleManager"%>
<%@page import="com.fy.engineserver.core.res.ResourceManager"%>
<%@page import="com.fy.engineserver.gametime.SystemTime"%>
<%@page import="com.fy.engineserver.sprite.npc.NPCManager"%>
<%@page import="com.fy.engineserver.datasource.article.manager.ArticleEntityManager"%>
<%@page import="com.fy.engineserver.datasource.article.data.articles.Article"%>
<%@page import="com.fy.engineserver.datasource.article.data.entity.ArticleEntity"%>
<%@page import="com.fy.engineserver.sprite.npc.FlopCaijiNpc"%>
<%@page import="java.util.ArrayList"%>
<%@page import="java.util.List"%>
<%@page import="com.fy.engineserver.core.g2d.Point"%>
<%@page import="com.fy.engineserver.sprite.Sprite"%>
<%@page import="java.sql.Timestamp"%>
<%@page import="com.fy.engineserver.carbon.devilSquare.DevilSquareConstant"%>
<%@page import="com.fy.engineserver.sprite.npc.MemoryNPCManager"%>
<%@page import="com.fy.engineserver.core.GameManager"%>
<%@page import="com.fy.engineserver.sprite.npc.NPC"%>
<%@page import="com.fy.engineserver.core.Game"%>
<%@page import="com.fy.engineserver.util.server.TestServerConfigManager"%>
<%@page import="java.util.Iterator"%>
<%@page import="com.fy.engineserver.carbon.devilSquare.instance.DevilSquare"%>
<%@page import="com.fy.engineserver.sprite.concrete.GamePlayerManager"%>
<%@page import="com.fy.engineserver.sprite.Player"%>
<%@page import="com.fy.engineserver.carbon.devilSquare.DevilSquareManager"%>
<%@page import="java.util.HashMap"%>
<%@page
	import="com.fy.engineserver.sprite.monster.MemoryMonsterManager"%>
<%@ page language="java" contentType="text/html; charset=utf-8"
	pageEncoding="utf-8"%>
<head>
</head>
<body>
	<%
		String action = request.getParameter("action");
		String playerId = request.getParameter("playerId");
		String aimId = request.getParameter("aimId");
		String chapterName = request.getParameter("chapterName");
		String vipreceive = request.getParameter("vipreceive");
		action = action == null ? "" : action;
		playerId = playerId == null ? "" :playerId;
		aimId = aimId == null ? "" :aimId;
		chapterName = chapterName == null ? "" :chapterName;
		vipreceive = vipreceive == null ? "0" :vipreceive;
		
		
	%>
	<%
	if("cleanReciveStatus".equals(action)) {
		if(!TestServerConfigManager.isTestServer()) {
			out.println("不是测试服，此功能不可用<br>");
			return;
		}
		if(!playerId.isEmpty()) {
			Player player = GamePlayerManager.getInstance().getPlayer(playerId);
			PlayerAimsEntity entity = PlayerAimeEntityManager.instance.getEntity(player.getId());
			List<PlayerAim> amL = entity.getAimList();
			List<PlayerChapter> cList = entity.getChapterList();
			for (PlayerAim pa : amL) {
				pa.setReveiveStatus((byte)0);
			}
			if (cList != null) {
				for (PlayerChapter pc : cList) {
					if (pc.getReceiveType() > 0) {
						pc.setReceiveType((byte)0);
					}
				}
			}
			entity.setAimList(amL);
			entity.setChapterList(cList);
			out.println("重置领取状态成功！！");
		}
	} else if ("receiveReward".equals(action)) {
		if(!TestServerConfigManager.isTestServer()) {
			out.println("不是测试服，此功能不可用<br>");
			return;
		}
		if(!playerId.isEmpty()) {
			Player player = GamePlayerManager.getInstance().getPlayer(playerId);
			boolean isVipReveive = !vipreceive.equals("0");
			CompoundReturn cr = PlayerAimeEntityManager.instance.receiveReward(player, Integer.parseInt(aimId), chapterName, isVipReveive);
			if(cr.getBooleanValue()) {
				out.println("[领取成功!]<br>");
			} else {
				out.println("[领取失败] [原因 :" + cr.getStringValue() + "]<br>");
			}
		}
	} else if ("checkYuhua".equals(action)) {
		Player player = GamePlayerManager.getInstance().getPlayer(playerId);
		int tempNum = 0;
		for (Soul soul : player.getSouls()) {
			long[] eids = soul.getEc().getEquipmentIds();
			for (long id : eids) {
				if (id > 0) {
					ArticleEntity ae = DefaultArticleEntityManager.getInstance().getEntity(id);
					if (ae instanceof EquipmentEntity) {
						if (((EquipmentEntity)ae).getStar() > 20 && ((EquipmentEntity) ae).isInscriptionFlag()) {
							tempNum += ((EquipmentEntity)ae).getStar() - 20;
						}
					}
				}
			}
		}
		if (tempNum > 0) {
			GameDataRecord gdr = AchievementManager.getInstance().getPlayerDataRecord(player, RecordAction.羽化装备成功次数);
			if (gdr == null) {
				out.println("异常！！");
				/* for (int i=0; i<start20Nums; i++) {
					AchievementManager.getInstance().record(ep.player, RecordAction.羽化装备成功次数);
				} */
			} else if(tempNum > gdr.getNum()) {
				int temp = (int) (tempNum - gdr.getNum());
				for (int i=0; i<temp; i++) {
					AchievementManager.getInstance().record(player, RecordAction.羽化装备成功次数);
				}
			}
		}
		out.println("总羽化星级:" + tempNum + "<br>");
	} else if ("lookarticleInfo".equals(action)) {
		Player plo = GamePlayerManager.getInstance().getPlayer(playerId);
		ArticleEntity ae = DefaultArticleEntityManager.getInstance().getEntity(Long.parseLong(aimId));
		out.println("物品颜色:" + ae.getColorType() + "<br><br>");
		out.println(ae.getInfoShow(plo).replaceAll("\n", "<br>"));
	} else if ("checkCompletedAim".equals(action)) {
		Player player = GamePlayerManager.getInstance().getPlayer(playerId);
		EventWithObjParam evt = new EventWithObjParam(com.fy.engineserver.event.Event.CHECK_PLAYER_AIMS, new Object[] { player.getId()});
		EventRouter.getInst().addEvent(evt);
		out.println("操作完毕!");
	} else if ("autoCompleteAll".equals(action)) {
		if(!TestServerConfigManager.isTestServer()) {
			out.println("不是测试服，此功能不可用<br>");
			return;
		}
		if(!playerId.isEmpty()) {
			Player player = GamePlayerManager.getInstance().getPlayer(playerId);
			PlayerAimsEntity entity = PlayerAimeEntityManager.instance.getEntity(player.getId());
			ChapterModel cm = PlayerAimManager.instance.chapterMaps.get(aimId);
			if (cm == null) {
				out.println("章节名输入错误！！");
				return ;
			}
			for (int i=0; i<cm.getAimsList().size(); i++) {
				PlayerAimModel pam = PlayerAimManager.instance.aimMaps.get(cm.getAimsList().get(i).getId());
				long now = System.currentTimeMillis();
				PlayerAim aim = new PlayerAim();
				aim.setAimId(pam.getId());
				aim.setCompletTime(now);
				aim.setReveiveStatus((byte) 0);
				boolean has = false;
				List<PlayerAim> aimList = entity.getAimList();
				for (PlayerAim paa : aimList) {
					if (paa.getAimId() == aim.getAimId()) {
						has = true;
						break;
					}
				}
				if (!has) {
					aimList.add(aim);
					entity.setAimList(aimList);
					//ChapterModel cm = PlayerAimManager.instance.chapterMaps.get(pam.getChapterName());
					if(cm != null) {
						entity.updateChapterScore(cm, true);
					}
					out.println("完成目标:" + pam.getAimName() + "<br>");
				}
			}
		}
	} else if ("autoComplete".equals(action)) {
		if(!TestServerConfigManager.isTestServer()) {
			out.println("不是测试服，此功能不可用<br>");
			return;
		}
		if(!playerId.isEmpty()) {
			Player player = GamePlayerManager.getInstance().getPlayer(playerId);
			PlayerAimsEntity entity = PlayerAimeEntityManager.instance.getEntity(player.getId());
			for (PlayerAim pa1 : entity.getAimList()) {
				if (pa1.getAimId() == Integer.parseInt(aimId)) {
					out.println("已经完成了此目标！！！");
					return;
				}
			}			
			PlayerAimModel pam = PlayerAimManager.instance.aimMaps.get(Integer.parseInt(aimId));
			long now = System.currentTimeMillis();
			PlayerAim aim = new PlayerAim();
			aim.setAimId(pam.getId());
			aim.setCompletTime(now);
			aim.setReveiveStatus((byte) 0);
			List<PlayerAim> aimList = entity.getAimList();
			aimList.add(aim);
			entity.setAimList(aimList);
			ChapterModel cm = PlayerAimManager.instance.chapterMaps.get(pam.getChapterName());
			if(cm != null) {
				entity.updateChapterScore(cm, true);
			} else {
			}
			out.println("完成目标:" + pam.getAimName() + "<br>");
		}
	} else if ("checkChapterComplate".equals(action)) {
		Player player = GamePlayerManager.getInstance().getPlayer(playerId);
		PlayerAimsEntity entity = PlayerAimeEntityManager.instance.getEntity(player.getId());
		for (PlayerChapter pct : entity.getChapterList()) {
			ChapterModel cm = PlayerAimManager.instance.chapterMaps.get(pct.getChapterName()) ;
			if (cm == null) {
				continue;
			}
			List<Integer> aimIds = new ArrayList<Integer>();
			for (PlayerAimModel pams : cm.getAimsList()) {
				for (PlayerAim pas : entity.getAimList()) {
					if (pams.getId() == pas.getAimId() && !aimIds.contains(pams.getId())) {
						aimIds.add(pams.getId());
					}
				}
			}
			out.println("[章节名:" + pct.getChapterName() + "] [领取状态:" + pct.getReceiveType() + "] [获得积分:" + entity.updateChapterScore(cm, true) + "][此章已经完成的目标:"+ aimIds +"]<br>");
		}
	} else if ("checkXYLevel".equals(action)) {
		Player player = GamePlayerManager.getInstance().getPlayer(playerId);
		TalentData data = FlyTalentManager.getInstance().getTalentData(player.getId());
		if (data != null && data.getXylevel() > 0) {
			AchievementManager.getInstance().record(player, RecordAction.元婴修炼等级, data.getXylevel());
		}
		out.println("ok");
	} else if ("checkHigherTask".equals(action)) {
		Player player = GamePlayerManager.getInstance().getPlayer(playerId);
		List<Long> tempList = (List<Long>) PlayerAimManager.instance.disk.get(PlayerAimManager.renwuKey + "_" + player.getId());
		for (int i=0; i<PlayerAimManager.极限任务1Id.length; i++) {
			Task task = TaskManager.getInstance().getTask(PlayerAimManager.极限任务1Id[i]);
			if (player.getTaskStatus(task) == 3 && !tempList.contains(task.getId())) {	//测试
				out.println("新增未完成的任务进仙录:" + task.getName_stat() + "<br>");
				PlayerAimManager.instance.notifyCompleteTask(task, player);
			}
		}
		for (int i=0; i<PlayerAimManager.极限任务2Id.length; i++) {
			Task task = TaskManager.getInstance().getTask(PlayerAimManager.极限任务2Id[i]);
			if (player.getTaskStatus(task) == 3 && !tempList.contains(task.getId())) {	//测试
				out.println("新增未完成的任务进仙录:" + task.getName_stat() + "<br>");
				PlayerAimManager.instance.notifyCompleteTask(task, player);
			}
		}
		for (int i=0; i<PlayerAimManager.极限任务3Id.length; i++) {
			Task task = TaskManager.getInstance().getTask(PlayerAimManager.极限任务3Id[i]);
			if (player.getTaskStatus(task) == 3 && !tempList.contains(task.getId())) {	//测试
				out.println("新增未完成的任务进仙录:" + task.getName_stat() + "<br>");
				PlayerAimManager.instance.notifyCompleteTask(task, player);
			}
		}
		for (int i=0; i<PlayerAimManager.极限任务4Id.length; i++) {
			Task task = TaskManager.getInstance().getTask(PlayerAimManager.极限任务4Id[i]);
			if (player.getTaskStatus(task) == 3 && !tempList.contains(task.getId())) {	//测试
				out.println("新增未完成的任务进仙录:" + task.getName_stat() + "<br>");
				PlayerAimManager.instance.notifyCompleteTask(task, player);
			}
		}
		for (int i=0; i<PlayerAimManager.极限任务5Id.length; i++) {
			Task task = TaskManager.getInstance().getTask(PlayerAimManager.极限任务5Id[i]);
			if (player.getTaskStatus(task) == 3 && !tempList.contains(task.getId())) {	//测试
				out.println("新增未完成的任务进仙录:" + task.getName_stat() + "<br>");
				PlayerAimManager.instance.notifyCompleteTask(task, player);
			}
		}
		for (int i=0; i<PlayerAimManager.极限任务6Id.length; i++) {
			Task task = TaskManager.getInstance().getTask(PlayerAimManager.极限任务6Id[i]);
			if (player.getTaskStatus(task) == 3 && !tempList.contains(task.getId())) {	//测试
				out.println("新增未完成的任务进仙录:" + task.getName_stat() + "<br>");
				PlayerAimManager.instance.notifyCompleteTask(task, player);
			}
		}
		out.println("[处理玩家极限任务完成仙录未完成情况结束！] [" + player.getLogString() + "]<br>");
	} else if ("checkPlayerHidden".equals(action)) {
		Player player = GamePlayerManager.getInstance().getPlayer(playerId);
		out.println("屏蔽本国玩家开关:" + player.hiddenSameCountryPlayer + "<br>");
		out.println("屏蔽聊天开关:" + player.hiddenChatMessage + "<br>");
		out.println("屏蔽所有玩家开关:" + player.hiddenAllPlayer + "<br>");
	} else if ("checkSoulEqu".equals(action)) {
		Player player = GamePlayerManager.getInstance().getPlayer(playerId);
		if (player.getUnusedSoul() != null && player.getUnusedSoul().size() > 0) {
			out.println("装备id:" + Arrays.toString(player.getUnusedSoul().get(0).getEc().getEquipmentIds()) + "<br>");
		}
	} else if ("giveBindArticle".equals(action)) {
		if(!TestServerConfigManager.isTestServer()) {
			out.println("不是测试服，此功能不可用<br>");
			return;
		}
		Player player = GamePlayerManager.getInstance().getPlayer(playerId);
		Article a = ArticleManager.getInstance().getArticle(aimId);
		int num = Integer.parseInt(chapterName);
		int colorType = a.getColorType();
		if (!vipreceive.isEmpty()) {
			colorType = Integer.parseInt(vipreceive);
		}
		for (int i=0; i<num; i++) {
			ArticleEntity ae = DefaultArticleEntityManager.getInstance().createEntity(a, true, ArticleEntityManager.CREATE_REASON_后台, player, colorType, 
					num, true);
			MailManager.getInstance().sendMail(player.getId(), new ArticleEntity[] {ae}, new int[] { 1 }, "后台", "后台", 0L, 0L, 0L, "后台发放");
		}
		out.println("成功！！查收邮件");
	} else if ("resetHejiu".equals(action)) {
		if(!TestServerConfigManager.isTestServer()) {
			out.println("不是测试服，此功能不可用<br>");
			return;
		}
		Player player = GamePlayerManager.getInstance().getPlayer(playerId);
		Map<String, PropsUseRecord> map = player.getPropsUseRecordMap();
		PropsUseRecord pr = map.get("酒");
		if (pr != null) {
			pr.setLastUseTime(0L);
			out.println("ok!");
		}
	}else if ("enchantRate".equals(action)) {
				if(!TestServerConfigManager.isTestServer()) {
				out.println("不是测试服，此功能不可用<br>");
					return;
				}
				ArticleEntity ae = DefaultArticleEntityManager.getInstance().getEntity(Long.parseLong(chapterName));
				((EquipmentEntity)ae).getEnchantData().getEnchants().get(0).setAttrNum(1000);
				out.println("ok");
	} else if ("deductEnchantDura".equals(action)) {
		if(!TestServerConfigManager.isTestServer() && !"ceshia".equals(GameConstants.getInstance().getServerName())) {
			out.println("不是测试服，此功能不可用<br>");
				return;
		}
		Player player = GamePlayerManager.getInstance().getPlayer(playerId);
		EquipmentEntity ee = (EquipmentEntity)DefaultArticleEntityManager.getInstance().getEntity((Long.parseLong(chapterName)));
		int aa = Integer.parseInt(aimId);
		if (ee.getEnchantData() == null || ee.getEnchantData().getEnchants().size() <= 0) {
			out.println("装备没附魔！！");
			return ;
		}
		for (int i=0; i<aa; i++) {
			ee.getEnchantData().lostDurable(player, ee, 1);
		}
		CoreSubSystem.notifyEquipmentChange(player, new EquipmentEntity[] { ee });
	} else if ("resetTumotie".equals(action)) {
		if(!TestServerConfigManager.isTestServer()) {
			out.println("不是测试服，此功能不可用<br>");
			return;
		}
		Player player = GamePlayerManager.getInstance().getPlayer(playerId);
		Map<String, PropsUseRecord> map = player.getPropsUseRecordMap();
		PropsUseRecord pr = map.get("屠魔帖");
		if (pr != null) {
			pr.setLastUseTime(0L);
			out.println("ok!");
		}
	} else if ("startTournment".equals(action)) {
		if(!TestServerConfigManager.isTestServer()) {
			out.println("不是测试服，此功能不可用<br>");
			return;
		}
		Calendar calendar = Calendar.getInstance();
		int hour = calendar.get(Calendar.HOUR_OF_DAY);
		int minute = calendar.get(Calendar.MINUTE);
		minute = 60 * hour + minute;
		TournamentManager.每天比武开启时间 = new int[]{minute + 1, minute + 5, minute + 10};
		out.println("ok!");
	} else if ("recoverTencent".equals(action)) {
		Player player = GamePlayerManager.getInstance().getPlayer(playerId);
		String emPath = "/data/home/cp_mqq/bushuju/simpleEMF_zhanwubusheng.xml";
		SimpleEntityManager<ArticleEntity> aeEm = null;
		SimpleEntityManager<Pet> pEm = null;
		Field petField = null;
		Field aeField = null;
		{				//当前背包
			Knapsack bag = player.getKnapsack_common();
			for (int i=0; i<bag.getCells().length; i++) {
				long aeId = bag.getCells()[i].getEntityId();
				if (aeId > 0) {
					long ct = DefaultArticleEntityManager.getInstance().em.count(ArticleEntity.class, "id="+aeId);
					if (ct <= 0) {
						if (aeEm == null) {
							DefaultSimpleEntityManagerFactory factory = new DefaultSimpleEntityManagerFactory(emPath);
							aeEm = factory.getSimpleEntityManager(ArticleEntity.class);
							pEm = factory.getSimpleEntityManager(Pet.class);
							petField = DataMoveManager.getFieldByAnnotation(Pet.class, SimpleVersion.class);
							aeField = DataMoveManager.getFieldByAnnotation(ArticleEntity.class, SimpleVersion.class);
							petField.setAccessible(true);
							aeField.setAccessible(true);
						}
						ArticleEntity ae = aeEm.find(aeId);
						if (ae != null) {
							try {
								if (ae instanceof PetPropsEntity) {
									PetPropsEntity pp = (PetPropsEntity) ae;
									long oldPetId = pp.getPetId();
									long newPetId = PetManager.em.nextId();
									Pet pet = pEm.find(oldPetId);
									petField.set(pet, 0);
									pet.setId(newPetId);
									pet.setPetPropsId(ae.getId());
									((PetPropsEntity) ae).setPetId(newPetId);
									PetManager.em.flush(pet);
								} else if (ae instanceof PetEggPropsEntity) {
									PetEggPropsEntity ppe = (PetEggPropsEntity) ae;
									long oldPetId = ppe.getPetId();
									if (oldPetId > 0) {
										long newPetId = PetManager.em.nextId();
										Pet pet = pEm.find(oldPetId);
										petField.set(pet, 0);
										pet.setId(newPetId);
										pet.setPetPropsId(ae.getId());
										PetManager.em.flush(pet);
										((PetEggPropsEntity) ae).setPetId(newPetId);
									}
								}
							} catch (Exception e) {
								MoveArticleManager.logger.warn("[腾讯数据转移] [转移宠物道具] [" + ae.getId() + "]", e);
							}
							aeField.set(ae, 0);
							long newId = DefaultArticleEntityManager.getInstance().em.nextId();
							ae.setId(newId);
							DefaultArticleEntityManager.getInstance().em.flush(ae);
							bag.getCells()[i].setEntityId(newId);
						}
					}
				}
			}
			bag.setModified(true);
		}
		{				//防爆包
			Knapsack bag = player.getKnapsack_fangbao();
			for (int i=0; i<bag.getCells().length; i++) {
				long aeId = bag.getCells()[i].getEntityId();
				if (aeId > 0) {
					long ct = DefaultArticleEntityManager.getInstance().em.count(ArticleEntity.class, "id="+aeId);
					if (ct <= 0) {
						if (aeEm == null) {
							DefaultSimpleEntityManagerFactory factory = new DefaultSimpleEntityManagerFactory(emPath);
							aeEm = factory.getSimpleEntityManager(ArticleEntity.class);
							pEm = factory.getSimpleEntityManager(Pet.class);
							petField = DataMoveManager.getFieldByAnnotation(Pet.class, SimpleVersion.class);
							aeField = DataMoveManager.getFieldByAnnotation(ArticleEntity.class, SimpleVersion.class);
							petField.setAccessible(true);
							aeField.setAccessible(true);
						}
						ArticleEntity ae = aeEm.find(aeId);
						if (ae != null) {
							try {
								if (ae instanceof PetPropsEntity) {
									PetPropsEntity pp = (PetPropsEntity) ae;
									long oldPetId = pp.getPetId();
									long newPetId = PetManager.em.nextId();
									Pet pet = pEm.find(oldPetId);
									petField.set(pet, 0);
									pet.setId(newPetId);
									pet.setPetPropsId(ae.getId());
									((PetPropsEntity) ae).setPetId(newPetId);
									PetManager.em.flush(pet);
								} else if (ae instanceof PetEggPropsEntity) {
									PetEggPropsEntity ppe = (PetEggPropsEntity) ae;
									long oldPetId = ppe.getPetId();
									if (oldPetId > 0) {
										long newPetId = PetManager.em.nextId();
										Pet pet = pEm.find(oldPetId);
										petField.set(pet, 0);
										pet.setId(newPetId);
										pet.setPetPropsId(ae.getId());
										PetManager.em.flush(pet);
										((PetEggPropsEntity) ae).setPetId(newPetId);
									}
								}
							} catch (Exception e) {
								MoveArticleManager.logger.warn("[腾讯数据转移] [转移宠物道具] [" + ae.getId() + "]", e);
							}
							aeField.set(ae, 0);
							long newId = DefaultArticleEntityManager.getInstance().em.nextId();
							ae.setId(newId);
							DefaultArticleEntityManager.getInstance().em.flush(ae);
							bag.getCells()[i].setEntityId(newId);
						}
					}
				}
			}
			bag.setModified(true);
		}
		{
			Knapsack bag = player.getPetKnapsack();
			for (int k=0; k<bag.getCells().length; k++) {
				long petAeId = bag.getCells()[k].getEntityId();
				if (petAeId > 0) {
					ArticleEntity ae = DefaultArticleEntityManager.getInstance().em.find(petAeId);
					if (ae instanceof PetPropsEntity) {
						PetPropsEntity pp = (PetPropsEntity)ae;
						long petId = pp.getPetId();
						Pet pet = PetManager.em.find(petId);
						if (pet == null) {
							out.println("新库不存在! " + petId + "<br>");
						} else {
							pet.setOwnerId(player.getId());
							pet.setPetPropsId(ae.getId());
						}
					}
				}
			}
			bag.setModified(true);
		}
		{				//仓库
			Knapsack bag = player.getKnapsacks_cangku();
			for (int i=0; i<bag.getCells().length; i++) {
				long aeId = bag.getCells()[i].getEntityId();
				if (aeId > 0) {
					long ct = DefaultArticleEntityManager.getInstance().em.count(ArticleEntity.class, "id="+aeId);
					if (ct <= 0) {
						if (aeEm == null) {
							DefaultSimpleEntityManagerFactory factory = new DefaultSimpleEntityManagerFactory(emPath);
							aeEm = factory.getSimpleEntityManager(ArticleEntity.class);
							pEm = factory.getSimpleEntityManager(Pet.class);
							petField = DataMoveManager.getFieldByAnnotation(Pet.class, SimpleVersion.class);
							aeField = DataMoveManager.getFieldByAnnotation(ArticleEntity.class, SimpleVersion.class);
						}
						ArticleEntity ae = aeEm.find(aeId);
						if (ae != null) {
							try {
								if (ae instanceof PetPropsEntity) {
									PetPropsEntity pp = (PetPropsEntity) ae;
									long oldPetId = pp.getPetId();
									long newPetId = PetManager.em.nextId();
									Pet pet = pEm.find(oldPetId);
									petField.set(pet, 0);
									pet.setId(newPetId);
									((PetPropsEntity) ae).setPetId(newPetId);
									PetManager.em.flush(pet);
								} else if (ae instanceof PetEggPropsEntity) {
									PetEggPropsEntity ppe = (PetEggPropsEntity) ae;
									long oldPetId = ppe.getPetId();
									if (oldPetId > 0) {
										long newPetId = PetManager.em.nextId();
										Pet pet = pEm.find(oldPetId);
										petField.set(pet, 0);
										pet.setId(newPetId);
										PetManager.em.flush(pet);
										((PetEggPropsEntity) ae).setPetId(newPetId);
									}
								}
							} catch (Exception e) {
								MoveArticleManager.logger.warn("[腾讯数据转移] [转移宠物道具] [" + ae.getId() + "]", e);
							}
							aeField.set(ae, 0);
							long newId = DefaultArticleEntityManager.getInstance().em.nextId();
							ae.setId(newId);
							DefaultArticleEntityManager.getInstance().em.flush(ae);
							bag.getCells()[i].setEntityId(newId);
						}
					}
				}
			}
			bag.setModified(true);
		}
		out.println("ok");
	} else if ("changeSuoHun".equals(action)) {
		if(!TestServerConfigManager.isTestServer()) {
			out.println("不是测试服，此功能不可用<br>");
			return;
		}
		Player player = GamePlayerManager.getInstance().getPlayer(playerId);
		PlayerArticleProtectData data = ArticleProtectManager.instance.getPlayerData(player);	
		long articleId = Long.parseLong(chapterName);
		long times = Timestamp.valueOf(aimId).getTime();
		for (ArticleProtectData data2 : data.getDatas()) {
			if (articleId == data2.getArticleID()) {
				data2.setRemoveTime(times);
				out.println("[修改物品解魂时间成功] [物品Id:" + articleId + "] [解魂时间:" + (new Timestamp(times)) + "]<br>");
				return;
			}
		}
		out.println("[修改物品解魂时间] [失败] [物品没锁魂，或者不是此玩家物品]<br>");
	} else if ("zhounianqing".equals(action)) {
		Player player = GamePlayerManager.getInstance().getPlayer(playerId);
		WaFenInstance4Private wp = WaFenManager.instance.privateMaps.get(player.getId());
		if (wp == null) {
			out.println("没挖过坟的不要！");
			return ;
		}
		DecimalFormat df = new DecimalFormat( "0.00 ");
		Iterator<String> ite = wp.getOpenFendiMaps().keySet().iterator();
		while (ite.hasNext()) {
			String key = ite.next();
			Map<String, Integer> tempMap = new HashMap<String, Integer>();
			Map<String, Integer> tempMap2 = new HashMap<String, Integer>();
			List<FenDiModel> list = wp.getOpenFendiMaps().get(key);
			FenMuModel fmm = WaFenManager.instance.fenmuMap.get(key);
			int count1 = 0;
			int count2 = 0;
			if (list != null && list.size() > 0) {
				for (int i=0; i<list.size(); i++) {
					String articleName = fmm.getArticleMap().get(list.get(i).getArticleId()).getArticleCNNName();
					int colorType = fmm.getArticleMap().get(list.get(i).getArticleId()).getColorType();
					String key1 = articleName + "__" + colorType;
					if (!list.get(i).isBind()) {
						if (tempMap.get(key1) != null) {
							int tempp = tempMap.get(key1);
							tempMap.put(key1, tempp+1);
							count1++;
						} else {
							tempMap.put(key1, 1);
							count1++;
						}
					} else {
						if (tempMap2.get(key1) != null) {
							int tempp = tempMap2.get(key1);
							tempMap2.put(key1, tempp+1);
							count2++;
						} else {
							tempMap2.put(key1, 1);
							count2++;
						}
					}
				}
			}
			out.println("<br><br>*******************<font color='red'>" + key + "</font>************************************<br>");
			if (count1 > 0) {
			out.println("<font color='#FF00FF'>-----------------[金铲子产出] [个数:" + count1 + "]------------------</font><br>");
			Iterator<String> ite2 = tempMap.keySet().iterator();
			while (ite2.hasNext()) {
				String key2 = ite2.next();
				int value2 = tempMap.get(key2);
				String[] ttt = key2.split("__");
				out.println("[<font color='red'>" + ttt[0] + "</font>] & [<font color='red'>"+ttt[1]+"</font>] & [<font color='blue'>" + value2 + "</font>] & [<font color='blue'>"+df.format((((double)value2/(double)count1) * 100d))+"%</font>]<br>");
			}
			}
			if (count2 > 0) {
			out.println("<font color='#FF00FF'>[-----------------银铲子产出] [个数:" + count2 + "]------------------</font><br>");
			Iterator<String> ite3 = tempMap2.keySet().iterator();
			while (ite3.hasNext()) {
				String key3 = ite3.next();
				int value3 = tempMap2.get(key3);
				String[] ttt = key3.split("__");
				out.println("[<font color='red'>" + ttt[0] + "</font>] & [<font color='red'>"+ttt[1]+"</font>] & [<font color='blue'>" + value3 + "</font>] & [<font color='blue'>"+ df.format((((double)value3/(double)count2) * 100d))+"%</font>]<br>");
			}
			}
			
		}
		
	}
	%>
	<form action="playerAim.jsp" method="post">
		<input type="hidden" name="action" value="receiveReward" />角色名   :
		<input type="text" name="playerId" value="<%=playerId%>" />目标id   :
		<input type="text" name="aimId" value="<%=aimId%>" />章节名   :
		<input type="text" name="chapterName" value="<%=chapterName%>" />vip领取（填1）   :
		<input type="text" name="vipreceive" value="<%=vipreceive%>" />
		<input type="submit" value="领取成就奖励" />
	</form>
	<br />
	<form action="playerAim.jsp" method="post">
		<input type="hidden" name="action" value="checkYuhua" />角色名   :
		<input type="text" name="playerId" value="<%=playerId%>" />
		<input type="submit" value="重新检测完成羽化装备成就" />
	</form>
	<br />
	<form action="playerAim.jsp" method="post">
		<input type="hidden" name="action" value="cleanReciveStatus" />角色名   :
		<input type="text" name="playerId" value="<%=playerId%>" />
		<input type="submit" value="重置领取状态" />
	</form>
	<br />
	<form action="playerAim.jsp" method="post">
		<input type="hidden" name="action" value="autoComplete" />角色名   :
		<input type="text" name="playerId" value="<%=playerId%>" />目标id:
		<input type="text" name="aimId" value="<%=aimId%>" />
		<input type="submit" value="完成某个目标" />
	</form>
	<br />
	<form action="playerAim.jsp" method="post">
		<input type="hidden" name="action" value="autoCompleteAll" />角色名   :
		<input type="text" name="playerId" value="<%=playerId%>" />章节名:
		<input type="text" name="aimId" value="<%=aimId%>" />
		<input type="submit" value="完成某张所有目标" />
	</form>
	<br />
	<form action="playerAim.jsp" method="post">
		<input type="hidden" name="action" value="checkCompletedAim" />角色名   :
		<input type="text" name="playerId" value="<%=playerId%>" />
		<input type="submit" value="检测并完成玩家已完成目标" />
	</form>
	<br />
	<form action="playerAim.jsp" method="post">
		<input type="hidden" name="action" value="checkHigherTask" />角色名   :
		<input type="text" name="playerId" value="<%=playerId%>" />
		<input type="submit" value="检测玩家极限任务" />
	</form>
	<br />
	<form action="playerAim.jsp" method="post">
		<input type="hidden" name="action" value="checkXYLevel" />角色名   :
		<input type="text" name="playerId" value="<%=playerId%>" />
		<input type="submit" value="检测玩家仙婴等级" />
	</form>
	<br />
	<form action="playerAim.jsp" method="post">
		<input type="hidden" name="action" value="checkChapterComplate" />角色名   :
		<input type="text" name="playerId" value="<%=playerId%>" />
		<input type="submit" value="查看玩家章节完成及领取状态" />
	</form>
	<br />
	<form action="playerAim.jsp" method="post">
		<input type="hidden" name="action" value="lookarticleInfo" />角色名   :
		<input type="text" name="playerId" value="<%=playerId%>" />物品id:
		<input type="text" name="aimId" value="<%=aimId%>" />
		<input type="submit" value="查看物品属性" />
	</form>
	<br />
	<form action="playerAim.jsp" method="post">
		<input type="hidden" name="action" value="zhounianqing" />角色名   :
		<input type="text" name="playerId" value="<%=playerId%>" />
		<input type="submit" value="周年庆活动产出物品" />
	</form>
	<br />
	<form action="playerAim.jsp" method="post">
		<input type="hidden" name="action" value="checkPlayerHidden" />角色名   :
		<input type="text" name="playerId" value="<%=playerId%>" />
		<input type="submit" value="查看玩家屏蔽信息" />
	</form>
	<br />
	<form action="playerAim.jsp" method="post">
		<input type="hidden" name="action" value="checkSoulEqu" />角色名   :
		<input type="text" name="playerId" value="<%=playerId%>" />
		<input type="submit" value="查看玩家另一元神装备" />
	</form>
	<br />
	<form action="playerAim.jsp" method="post">
		<input type="hidden" name="action" value="giveBindArticle" />角色名   :
		<input type="text" name="playerId" value="<%=playerId%>" />物品名:
		<input type="text" name="aimId" value="<%=aimId%>" />物品颜色:
		<input type="text" name="vipreceive" value="<%=vipreceive%>" />物品个数 :
		<input type="text" name="chapterName" value="<%=chapterName%>" />
		<input type="submit" value="发送绑定物品" />
	</form>
	<br />
	<form action="playerAim.jsp" method="post">
		<input type="hidden" name="action" value="changeSuoHun" />角色名:
		<input type="text" name="playerId" value="<%=playerId%>" />物品Id :
		<input type="text" name="chapterName" value="<%=chapterName%>" />解魂时间(格式例：2015-01-14 10:34:13)：
		<input type="text" name="aimId" value="<%=aimId%>" />
		<input type="submit" value="修改物品锁魂时间" />
	</form>
	<br />
	<form action="playerAim.jsp" method="post">
		<input type="hidden" name="action" value="recoverTencent" />角色名:
		<input type="text" name="playerId" value="<%=playerId%>" />
		<input type="submit" value="腾讯修复转服空格子问题" />
	</form>
	<br />
	<form action="playerAim.jsp" method="post">
		<input type="hidden" name="action" value="startTournment" />
		<input type="submit" value="开比武" />
	</form>
	<br />
	<form action="playerAim.jsp" method="post">
		<input type="hidden" name="action" value="resetHejiu" />角色名:
		<input type="text" name="playerId" value="<%=playerId%>" />
		<input type="submit" value="清喝酒次数" />
	</form>
	<br />
	<form action="playerAim.jsp" method="post">
		<input type="hidden" name="action" value="resetTumotie" />角色名:
		<input type="text" name="playerId" value="<%=playerId%>" />
		<input type="submit" value="清使用屠魔贴次数" />
	</form>
	<br />
	<form action="playerAim.jsp" method="post">
		<input type="hidden" name="action" value="enchantRate" />角色名:
		<input type="text" name="playerId" value="<%=playerId%>" />物品Id :
		<input type="text" name="chapterName" value="<%=chapterName%>" />
		<input type="submit" value="调整装备附魔触发概率" />
	</form>
	<br />
	<form action="playerAim.jsp" method="post">
		<input type="hidden" name="action" value="deductEnchantDura" />角色名:
		<input type="text" name="playerId" value="<%=playerId%>" />装备Id :
		<input type="text" name="chapterName" value="<%=chapterName%>" />消耗耐久度数 :
		<input type="text" name="aimId" value="<%=aimId%>" />
		<input type="submit" value="消耗装备附魔耐久度" />
	</form>
	<br />
</body>
</html>
