<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<%@page import="com.fy.engineserver.datasource.article.data.entity.PetEggPropsEntity"%>
<%@page import="com.fy.engineserver.datasource.article.data.entity.PetPropsEntity"%>
<%@page import="com.fy.engineserver.sprite.npc.CityFightNPC"%>
<%@page import="com.fy.engineserver.cityfight.CityFightManager"%>
<%@page import="com.fy.engineserver.sprite.PropsUseRecord"%>
<%@page
	import="com.fy.engineserver.datasource.article.data.equipextra.costEnum.AddLuckyCostEnum"%>
<%@page import="com.fy.engineserver.message.GameMessageFactory"%>
<%@page import="com.fy.engineserver.message.QUERY_ARTICLE_INFO_RES"%>
<%@page
	import="com.fy.engineserver.datasource.article.data.equipextra.instance.StarInfo"%>
<%@page
	import="com.fy.engineserver.datasource.article.data.equipextra.instance.EquipExtraData"%>
<%@page
	import="com.fy.engineserver.datasource.article.data.equipextra.EquipStarManager"%>
<%@page import="com.fy.engineserver.sprite.Soul"%>
<%@page import="com.fy.engineserver.sprite.horse.HorseManager"%>
<%@page import="com.fy.engineserver.sprite.horse.Horse"%>
<%@page import="com.fy.engineserver.datasource.language.Translate"%>
<%@page
	import="com.fy.engineserver.playerTitles.PlayerTitlesManager"%>
<%@page import="com.fy.engineserver.sprite.PlayerSimpleInfoManager"%>
<%@page import="com.fy.engineserver.sprite.PlayerSimpleInfo"%>
<%@page import="com.fy.engineserver.marriage.MarriageInfo"%>
<%@page
	import="com.fy.engineserver.marriage.manager.MarriageManager"%>
<%@page import="com.fy.engineserver.society.SocialManager"%>
<%@page import="com.fy.engineserver.society.Relation"%>
<%@page import="java.io.Serializable"%>
<%@page
	import="com.fy.engineserver.activity.wafen.instacne.WaFenInstance4Private"%>
<%@page
	import="com.fy.engineserver.activity.wafen.manager.WaFenManager"%>
<%@page
	import="com.fy.engineserver.activity.loginActivity.ActivityManagers"%>
<%@page import="java.util.Random"%>
<%@page import="com.fy.engineserver.util.ProbabilityUtils"%>
<%@page
	import="com.fy.engineserver.datasource.article.data.props.ArticleProperty"%>
<%@page
	import="com.fy.engineserver.datasource.article.data.props.RandomPackageProps"%>
<%@page import="com.fy.engineserver.newtask.service.TaskManager"%>
<%@page import="com.fy.engineserver.newtask.Task"%>
<%@page import="com.fy.engineserver.sprite.pet.PetManager"%>
<%@page import="com.fy.engineserver.sprite.pet.Pet"%>
<%@page import="com.fy.engineserver.core.CoreSubSystem"%>
<%@page
	import="com.fy.engineserver.datasource.article.concrete.DefaultArticleEntityManager"%>
<%@page
	import="com.fy.engineserver.datasource.article.data.entity.EquipmentEntity"%>
<%@page
	import="com.fy.engineserver.datasource.article.data.props.Cell"%>
<%@page
	import="com.fy.engineserver.datasource.article.data.props.Knapsack"%>
<%@page
	import="com.fy.engineserver.carbon.devilSquare.model.DsPlayerDataModel"%>
<%@page import="com.fy.engineserver.sprite.monster.Monster"%>
<%@page
	import="com.fy.engineserver.datasource.article.manager.ArticleManager"%>
<%@page import="com.fy.engineserver.core.res.ResourceManager"%>
<%@page import="com.fy.engineserver.gametime.SystemTime"%>
<%@page import="com.fy.engineserver.sprite.npc.NPCManager"%>
<%@page
	import="com.fy.engineserver.datasource.article.manager.ArticleEntityManager"%>
<%@page
	import="com.fy.engineserver.datasource.article.data.articles.Article"%>
<%@page
	import="com.fy.engineserver.datasource.article.data.entity.ArticleEntity"%>
<%@page import="com.fy.engineserver.sprite.npc.FlopCaijiNpc"%>
<%@page import="java.util.ArrayList"%>
<%@page import="java.util.List"%>
<%@page import="com.fy.engineserver.core.g2d.Point"%>
<%@page import="com.fy.engineserver.sprite.Sprite"%>
<%@page import="java.sql.Timestamp"%>
<%@page
	import="com.fy.engineserver.carbon.devilSquare.DevilSquareConstant"%>
<%@page import="com.fy.engineserver.sprite.npc.MemoryNPCManager"%>
<%@page import="com.fy.engineserver.core.GameManager"%>
<%@page import="com.fy.engineserver.sprite.npc.NPC"%>
<%@page import="com.fy.engineserver.core.Game"%>
<%@page
	import="com.fy.engineserver.util.server.TestServerConfigManager"%>
<%@page import="java.util.Iterator"%>
<%@page
	import="com.fy.engineserver.carbon.devilSquare.instance.DevilSquare"%>
<%@page
	import="com.fy.engineserver.sprite.concrete.GamePlayerManager"%>
<%@page import="com.fy.engineserver.sprite.Player"%>
<%@page
	import="com.fy.engineserver.carbon.devilSquare.DevilSquareManager"%>
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
		String carbonLevel = request.getParameter("carbonLevel");
		String cateid = request.getParameter("cateid");
		String deletepwd = request.getParameter("deletepwd");
		action = action == null ? "" : action;//1
		deletepwd = deletepwd == null ? "" : deletepwd;//1
		playerId = playerId == null ? "" : playerId;
		cateid = cateid == null ? "" : cateid;
		carbonLevel = carbonLevel == null ? "" : carbonLevel;
		/* Iterator<String> ite11 = DevilSquareManager.instance.map.keySet().iterator();
		while(ite11.hasNext()) {
			String key = ite11.next();
			int numm = DevilSquareManager.instance.map.get(key);
			String[] ss = key.split("_");
			out.println("[物品名:" + ss[0] + "] [颜色:" + ss[1] + "] [数量:" + numm + "]<BR>");
		} */
		//

		if ("testRandomPackage".equals(action)) {
			Player p = GamePlayerManager.getInstance().getPlayer(playerId);
			Article a = ArticleManager.getInstance().getArticle(carbonLevel);
			if (a instanceof RandomPackageProps) {
				int tempCount = Integer.parseInt(cateid);
				for (int kk = 0; kk < tempCount; kk++) {
					RandomPackageProps rpp = (RandomPackageProps) a;
					ArticleProperty[] apps = rpp.getApps();
					double[] probabilitys = new double[apps.length];
					double countValue = 0;
					for (int i = 0; i < apps.length; i++) {
						if (apps[i] != null) {
							countValue += apps[i].getProb();
						}
					}
					for (int i = 0; i < apps.length; i++) {
						if (apps[i] != null) {
							probabilitys[i] = apps[i].getProb() * 1.0 / countValue;
						}
					}
					int index = ProbabilityUtils.randomProbability(p.random, probabilitys);
					ArticleProperty appTemp = apps[index];
					if (appTemp != null) {
						String s = appTemp.getArticleName_stat();
						int maxOutNums = ActivityManagers.getInstance().getMaxOutNumsByArticleName(rpp.getName(), s);
						if (maxOutNums > 0) {
							int hasOutNums = ActivityManagers.getInstance().getHasOutNumsByArticleName(s);
							if (hasOutNums >= maxOutNums) {
								s = ActivityManagers.LAST_ARTICLE_NAME;
							}
						}
						Article resultA = ArticleManager.getInstance().getArticleByCNname(s);
						out.println("[第" + kk + "次] [开出物品:" + resultA.getName() + "] [颜色:" + appTemp.getColor() + "] [数量:" + appTemp.getCount() + "] [获取概率:" + appTemp.getProb() + "]<br>");
					}
				}
			} else {
				out.println("输入的物品名错误！！" + carbonLevel + " == " + a + "<br>");
			}
		}
	%>
	<form action="devilSquare.jsp" method="post">
		<input type="hidden" name="action" value="enterCarbon" />角色 id : <input
			type="text" name="playerId" value="<%=playerId%>" />进入副本等级: <input
			type="text" name="carbonLevel" value="<%=carbonLevel%>" /> <input
			type="submit" value="进入恶魔广场" />
	</form>
	<br />
	<form action="devilSquare.jsp" method="post">
		<input type="hidden" name="action" value="changecarbonstatus" />副本等级:
		<input type="text" name="playerId" value="<%=playerId%>" />修改状态为 : <input
			type="text" name="carbonLevel" value="<%=carbonLevel%>" /> <input
			type="submit" value="修改副本状态" />
	</form>
	<br />
	<form action="devilSquare.jsp" method="post">
		<input type="hidden" name="action" value="testkillmonster" />
		人物id(等级得在怪物掉落范围内):<input type="text" name="playerId"
			value="<%=playerId%>" /> 怪物id:<input type="text" name="cateid"
			value="<%=cateid%>" /> 怪物数量:<input type="text" name="carbonLevel"
			value="<%=carbonLevel%>" /> <input type="submit" value="测试怪物掉率" />
	</form>
	<br />
	<form action="devilSquare.jsp" method="post">
		<input type="hidden" name="action" value="showDetailPlayers" />副本等级:
		<input type="text" name="playerId" value="<%=playerId%>" /> <input
			type="submit" value="查看副本中玩家信息" />
	</form>
	<br />
	<form action="devilSquare.jsp" method="post">
		<input type="hidden" name="action" value="checkcarbonstatus" /> <input
			type="submit" value="查看各副本状态" />
	</form>
	<br />
	<form action="devilSquare.jsp" method="post">
		<input type="hidden" name="action" value="refreashpotal" /> <input
			type="submit" value="刷出传送门" />
	</form>
	<form action="devilSquare.jsp" method="post">
		<input type="hidden" name="action" value="cleanpotal" /> <input
			type="submit" value="清除传送门" />
	</form>
	<form action="devilSquare.jsp" method="post">
		<input type="hidden" name="action" value="checkseal" /> <input
			type="submit" value="查看各等级副本解封时间" />
	</form>

	<form action="devilSquare.jsp" method="post">
		<input type="hidden" name="action" value="cleanBag" /> 角色名: <input
			type="text" name="playerId" value="<%=playerId%>" /> <input
			type="submit" value="清除当前背包物品" />
	</form>

	<form action="devilSquare.jsp" method="post">
		<input type="hidden" name="action" value="setEnchant" />装备id: <input
			type="text" name="playerId" value="<%=playerId%>" /> 灵性:<input
			type="text" name="cateid" value="<%=cateid%>" /> 角色名:<input
			type="text" name="carbonLevel" value="<%=carbonLevel%>" /> <input
			type="submit" value="设置附魔灵性" />
	</form>
	<br />
	<form action="devilSquare.jsp" method="post">
		<input type="hidden" name="action" value="changeDeleteFlag" /> 宠物Id:
		<input type="text" name="playerId" value="<%=playerId%>" /> <input
			type="submit" value="修改宠物删除标志位" />
	</form>
	<br />
	<form action="devilSquare.jsp" method="post">
		<input type="hidden" name="action" value="changepetstate2" /> 宠物Id: <input
			type="text" name="playerId" value="<%=playerId%>" />基础技能(多个用,分隔): <input
			type="text" name="cateid" value="<%=cateid%>" />天赋技能(多个用,分隔): <input
			type="text" name="carbonLevel" value="<%=carbonLevel%>" /> <input
			type="submit" value="修改宠物技能" />
	</form>
	<br />
	<form action="devilSquare.jsp" method="post">
		<input type="hidden" name="action" value="changepetstate3" /> 宠物Id: <input
			type="text" name="playerId" value="<%=playerId%>" />
			type="submit" value="清空宠物技能" />
	</form>
	<br />
	<br />
	<form action="devilSquare.jsp" method="post">
		<input type="hidden" name="action" value="testRandomPackage" />角色名: <input
			type="text" name="playerId" value="<%=playerId%>" />随机宝箱道具名: <input
			type="text" name="carbonLevel" value="<%=carbonLevel%>" />开启次数: <input
			type="text" name="cateid" value="<%=cateid%>" /> <input
			type="submit" value="测试随机宝箱开出物品概率" />
	</form>
	<br />
	<form action="devilSquare.jsp" method="post">
		<input type="hidden" name="action" value="deletechanzi" />角色名: <input
			type="text" name="playerId" value="<%=playerId%>" />清除的铲子类型(0银铲子,1金铲子,2琉璃铲子):
		<input type="text" name="carbonLevel" value="<%=carbonLevel%>" />清除个数:
		<input type="text" name="cateid" value="<%=cateid%>" />密码: <input
			type="password" name="deletepwd" value="<%=deletepwd%>" /> <input
			type="submit" value="删除玩家铲子" />
	</form>
	<br />
	<form action="devilSquare.jsp" method="post">
		<input type="hidden" name="action" value="cleanmarrigeinfo" />角色名: <input
			type="text" name="playerId" value="<%=playerId%>" /> <input
			type="submit" value="清除无效的结婚状态" />
	</form>
	<br />
	<form action="devilSquare.jsp" method="post">
		<input type="hidden" name="action" value="cleaninvalidhorse" />角色名: <input
			type="text" name="playerId" value="<%=playerId%>" /> <input
			type="submit" value="修复看不到NPC问题" />
	</form>
	<br />
	<form action="devilSquare.jsp" method="post">
		<input type="hidden" name="action" value="viewEquLuckNum" />装备id: <input
			type="text" name="playerId" value="<%=playerId%>" /> <input
			type="submit" value="查看装备幸运值" />
	</form>
	<br />
	<form action="devilSquare.jsp" method="post">
		<input type="hidden" name="action" value="setequipStar" />角色名: <input
			type="text" name="playerId" value="<%=playerId%>" />装备id: <input
			type="text" name="carbonLevel" value="<%=carbonLevel%>" />星级: <input
			type="text" name="cateid" value="<%=cateid%>" /> <input
			type="submit" value="设置装备强化等级" />
	</form>
	<br />
	<form action="devilSquare.jsp" method="post">
		<input type="hidden" name="action" value="setEquLuckStar" />装备id: <input
			type="text" name="playerId" value="<%=playerId%>" />消耗数组: <input
			type="text" name="carbonLevel" value="<%=carbonLevel%>" />星级: <input
			type="text" name="cateid" value="<%=cateid%>" /> <input
			type="submit" value="设置装备幸运值" />
	</form>
	<br />
	<form action="devilSquare.jsp" method="post">
		<input type="hidden" name="action" value="cleanJiuNums" />角色名: <input
			type="text" name="playerId" value="<%=playerId%>" /> <input
			type="submit" value="清除喝酒使用次数" />
	</form>
	<br />
	<form action="devilSquare.jsp" method="post">
		<input type="hidden" name="action" value="cleanTieNums" />角色名: <input
			type="text" name="playerId" value="<%=playerId%>" /> <input
			type="submit" value="清除屠魔贴使用次数" />
	</form>
	<br />
	<form action="devilSquare.jsp" method="post">
		<input type="hidden" name="action" value="refreshChengzhu" />角色名: <input
			type="text" name="playerId" value="<%=playerId%>" />等级: <input
			type="text" name="carbonLevel" value="<%=carbonLevel%>" /> <input
			type="submit" value="刷出对应级别城主" />
	</form>
	<br />
	<form action="devilSquare.jsp" method="post">
		<input type="hidden" name="action" value="modifyPetsuit" />角色名: <input
			type="text" name="playerId" value="<%=playerId%>" /> 
			<input type="submit" value="修正宠物饰品丢失问题(只修复宠物背包内宠物)" />
	</form>
	<br />

	<%
		if ("enterCarbon".equals(action)) {
			out.println("=============================");
			Player p = GamePlayerManager.getInstance().getPlayer(Long.parseLong(playerId));
			int cbLevel = Integer.parseInt(carbonLevel);
			DevilSquareManager dsm = DevilSquareManager.instance;
			dsm.notifyEnterCarbon(p, cbLevel, true);
			out.println("======");
		} else if ("modifyPetsuit".equals(action)) {
			Player player = GamePlayerManager.getInstance().getPlayer(playerId);
			Knapsack bag = player.getPetKnapsack();
			boolean b = false;
			for (int i=0; i<bag.getCells().length; i++) {
				if (bag.getCells()[i] != null && bag.getCells()[i].getEntityId() > 0) {
					ArticleEntity ae = ArticleEntityManager.getInstance().getEntity(bag.getCells()[i].getEntityId());
					long petId = -1L;
					if (ae instanceof PetPropsEntity) {
						petId = ((PetPropsEntity)ae).getPetId();
					} else if (ae instanceof PetEggPropsEntity) {
						petId = ((PetEggPropsEntity)ae).getId();
					}
					Pet pet = PetManager.getInstance().getPet(petId);
					if (pet.getOrnaments() != null && pet.getOrnaments().length > 0 && pet.getOrnaments()[0] > 0) {
						ArticleEntity aee = ArticleEntityManager.getInstance().getEntity(pet.getOrnaments()[0]) ;
						if (aee != null) {
							b = true;
							pet.init();
							out.println("[修正宠物饰品丢失问题成功] [宠物id:" + pet.getId() + "] [饰品id:"+pet.getOrnaments()[0]+"] [饰品名:"+aee.getArticleName()+"]<br>");
						} else {
							out.println("不存在饰品id:" + pet.getOrnaments()[0] + "<br>");
						}
					}
				}
			}
			if (!b) {
				out.println("此玩家宠物背包内没有需要处理的宠物！");
			}
		} else if ("refreshChengzhu".equals(action)) {
			if (!TestServerConfigManager.isTestServer()) {
				out.println("只有测试服可以用！");
				return;
			}
			Player player = GamePlayerManager.getInstance().getPlayer(playerId);
			int lv = Integer.parseInt(carbonLevel);
			int nnId = CityFightManager.npcId;
			if (lv >= 260) {
				nnId = CityFightManager.npcId2;
			}
			if (lv >= 280) {
				nnId = CityFightManager.npcId3;
			}
			NPC np = MemoryNPCManager.getNPCManager().createNPC(nnId);
			np.setX(player.getX());
			np.setLevel(lv);
			np.setY(player.getY());
			((CityFightNPC) np).bornTime = System.currentTimeMillis();
			np.setBornPoint(new Point(np.getX(), np.getY()));
			((MemoryNPCManager) MemoryNPCManager.getNPCManager()).设置sprite属性值(np);
			player.getCurrentGame().addSprite(np);
			out.println(np.getId() + " = " + np.getMaxHP() + " = b :" + np.getMaxHPB() + ", a:" + np.getMaxHPA() + ", c:" + np.getMaxHPC() + "<br>");
			byte career = np.getCareer();
		} else if ("viewEquLuckNum".equals(action)) {
			ArticleEntity ae = ArticleEntityManager.getInstance().getEntity(Long.parseLong(playerId));
			if (ae instanceof EquipmentEntity) {
				EquipmentEntity ee = (EquipmentEntity) ae;
				EquipExtraData data = EquipStarManager.getInst().getEntity(ee);
				for (StarInfo info : data.getStarInfos()) {
					out.println("[星:" + info.getEquipStar() + "(" + ArticleManager.获得对应的羽化描述.get(info.getEquipStar()) + ")] [幸运值:" + info.countLuckNum(ee) + "]<br>");
				}
			} else {
				out.println("不是装备！！");
			}
		} else if ("cleanJiuTieNums".equals(action)) {
			if (!TestServerConfigManager.isTestServer()) {
				out.println("只有测试服可以用！");
				return;
			}
			Player player = GamePlayerManager.getInstance().getPlayer(playerId);
			player.getPropsUseRecordMap().clear();
			player.setDirty(true, "propsUseRecordMap");
			out.println("ok!");
		} else if ("cleanTieNums".equals(action)) {
			if (!TestServerConfigManager.isTestServer()) {
				out.println("只有测试服可以用！");
				return;
			}
			Player player = GamePlayerManager.getInstance().getPlayer(playerId);
			PropsUseRecord p = player.getPropsUseRecordMap().get("屠魔帖");
			PropsUseRecord p2 = new PropsUseRecord("屠魔帖", System.currentTimeMillis(), (byte) 0);
			player.getPropsUseRecordMap().put("屠魔帖", p2);
			player.setDirty(true, "propsUseRecordMap");
			out.println("ok!");
		} else if ("cleanJiuNums".equals(action)) {
			if (!TestServerConfigManager.isTestServer()) {
				out.println("只有测试服可以用！");
				return;
			}
			Player player = GamePlayerManager.getInstance().getPlayer(playerId);
			PropsUseRecord p = player.getPropsUseRecordMap().get("酒");
			PropsUseRecord p2 = new PropsUseRecord("酒", System.currentTimeMillis(), (byte) 0);
			player.getPropsUseRecordMap().put("酒", p2);
			player.setDirty(true, "propsUseRecordMap");
			out.println("ok!");
		} else if ("setEquLuckStar".equals(action)) {
			if (!TestServerConfigManager.isTestServer()) {
				out.println("只有测试服可以用！");
				return;
			}
			ArticleEntity ae = ArticleEntityManager.getInstance().getEntity(Long.parseLong(playerId));
			if (ae instanceof EquipmentEntity) {
				EquipmentEntity ee = (EquipmentEntity) ae;
				EquipExtraData data = EquipStarManager.getInst().getEntity(ee);
				if (data == null) {
					out.println("此装备没开启幸运值，需要到羽化5以后才能用！");
					return;
				}
				int star = Integer.parseInt(cateid);
				String[] sss = carbonLevel.split(",");
				if (sss.length != AddLuckyCostEnum.values().length) {
					out.println("消耗数组必须是" + AddLuckyCostEnum.values().length + "位，用,分隔开!");
					return;
				}
				int[] aaa = new int[sss.length];
				for (int i = 0; i < aaa.length; i++) {
					aaa[i] = Integer.parseInt(sss[i]);
				}
				boolean exist = false;
				for (StarInfo info : data.getStarInfos()) {
					if (info.getEquipStar() == ee.getStar()) {
						info.setCostNums(aaa);
						exist = true;
						break;
					}
				}
				if (!exist) {
					StarInfo info = new StarInfo(ee.getStar());
					info.setCostNums(aaa);
					data.getStarInfos().add(info);
				}
				EquipStarManager.em.notifyFieldChange(data, "starInfos");
				out.println("设置成功!!<br>");
				for (StarInfo info : data.getStarInfos()) {
					out.println("[星:" + info.getEquipStar() + "(" + ArticleManager.获得对应的羽化描述.get(info.getEquipStar()) + ")] [幸运值:" + info.countLuckNum(ee) + "]<br>");
				}
			}
		} else if ("setequipStar".equals(action)) {
			if (!TestServerConfigManager.isTestServer()) {
				out.println("只有测试服可以用！");
				return;
			}
			Player player = GamePlayerManager.getInstance().getPlayer(playerId);
			long eeId = Long.parseLong(carbonLevel);
			int eestar = Integer.parseInt(cateid);
			ArticleEntity ae = ArticleEntityManager.getInstance().getEntity(eeId);
			if (ae instanceof EquipmentEntity) {
				EquipmentEntity ee = (EquipmentEntity) ae;
				if (eestar > 0 && eestar <= 31) {
					ee.setStar(eestar);
					out.println("设置成功");
				} else {
					out.println("设置失败！");
				}
				if (player.isOnline()) {
					QUERY_ARTICLE_INFO_RES qres = new QUERY_ARTICLE_INFO_RES(GameMessageFactory.nextSequnceNum(), ee.getId(), ee.getInfoShow(player));
					player.addMessageToRightBag(qres);
				}
			}
		} else if ("cleaninvalidhorse".equals(action)) {
			Player player = GamePlayerManager.getInstance().getPlayer(playerId);
			ArrayList<Long> horseIdlist = player.getHorseIdList();
			List<Long> tempList = new ArrayList<Long>();
			for (Long horseId : horseIdlist) {
				Horse h = HorseManager.getInstance().getHorseById(horseId, player);
				if (h == null) {
					//Horse h2 = HorseManager.em.find(horseId);
					//if (h2 == null) {
					tempList.add(horseId);
					//}
				}
			}
			/* for (Soul s:player.getSouls()) {
				for (long id : s.getHorseArr()) {
					if (id > 0) {
						Horse h2 = HorseManager.em.find(id);
						if (h2 == null) {
							tempList.add(id);
						}
					}
				}
			} */
			for (long id : tempList) {
				player.getHorseIdList().remove(id);
				out.println("[临时清除玩家无效坐骑] [成功] [" + player.getLogString() + "] [horseId:" + id + "]<br>");
				/* for (Soul s : player.getSouls()) {
					boolean b = s.getHorseArr().remove(id);
					if (b) {
						player.setDirty(true, "currSoul");
						player.setDirty(true, "unusedSoul"); 
						HorseManager.logger.warn("[清除玩家无效坐骑] [成功] [" + player.getLogString() + "] [horseId:" + id + "]");
					}
				} */
			}
		} else if ("cleanmarrigeinfo".equals(action)) {
			Player player = GamePlayerManager.getInstance().getPlayer(playerId);
			Relation relation = SocialManager.getInstance().getRelationById(player.getId());
			if (relation.getMarriageId() <= 0) {
				out.println("此人没有结婚！<br>");
				return;
			}
			MarriageInfo info = MarriageManager.getInstance().getMarriageInfoById(relation.getMarriageId());
			long tempId = info.getHoldA() == player.getId() ? info.getHoldB() : info.getHoldA();
			PlayerSimpleInfo playerInfo = PlayerSimpleInfoManager.getInstance().getInfoById(tempId);
			if (playerInfo != null) {
				out.println("此人婚姻状态正常!<br>");
				return;
			}
			info.setState(MarriageInfo.STATE_LIHUN);
			PlayerTitlesManager.getInstance().removeTitle(player, Translate.text_marriage_结婚);
			relation.setMarriageId(-1);
			SocialManager.em.notifyFieldChange(relation, "marriageId");
			out.println(tempId + "<br>");
			out.println("清除玩家结婚状态成功！<br>");
			MarriageManager.logger.warn("[后台清除玩家无效的结婚状态] [成功] [" + player.getLogString() + "]");
		} else if ("modifyPlayerTili".equals(action)) {
			Task task = TaskManager.getInstance().getTask(1273);
			Player player = GamePlayerManager.getInstance().getPlayer(playerId);
			if (player.isNeedAddVitality()) {
				out.println("玩家体力恢复正常！");
				return;
			}
			if (player.inDeliver(task)) {
				player.setNeedAddVitality(true);
				out.println("修改成功!!");
			} else {
				out.println("玩家没完成开启体力任务!!");
			}
		} else if ("deletechanzi".equals(action)) {
			if ("deletetempdata".equals(deletepwd)) {
				Player player = GamePlayerManager.getInstance().getPlayer(playerId);
				WaFenInstance4Private wfip = WaFenManager.instance.privateMaps.get(player.getId());
				byte type = Byte.parseByte(carbonLevel);
				int num = Integer.parseInt(cateid);
				int tempNum = wfip.getChanziNum(type) >= num ? num : wfip.getChanziNum(type);
				for (int i = 0; i < tempNum; i++) {
					wfip.expenseChanZi(type, 1);
				}
				WaFenManager.instance.privateMaps.put(player.getId(), wfip);
				WaFenManager.instance.disk.put(WaFenManager.privateKey, (Serializable) WaFenManager.instance.privateMaps);
				WaFenManager.logger.warn("[后台删除成功] [" + player.getLogString() + "] [删除铲子类型:" + type + "] [删除个数:" + num + "] [实际删除个数:" + tempNum + "] [剩余银铲子数量:" + wfip.getChanziNum(WaFenManager.yinChanZi) + "] [剩余金铲子数量:" + wfip.getChanziNum(WaFenManager.jinChanZi) + "] [剩余琉璃铲子数量:" + wfip.getChanziNum(WaFenManager.liuliChanZi) + "]");
				out.println("[删除成功] [" + player.getLogString() + "] [删除铲子类型:" + type + "] [删除个数:" + num + "] [实际删除个数:" + tempNum + "] [剩余银铲子数量:" + wfip.getChanziNum(WaFenManager.yinChanZi) + "] [剩余金铲子数量:" + wfip.getChanziNum(WaFenManager.jinChanZi) + "] [剩余琉璃铲子数量:" + wfip.getChanziNum(WaFenManager.liuliChanZi) + "]<br>");

			} else {
				out.println("不知道密码别乱点！！");
			}
		} else if ("changepetstate3".equals(action)) {
			if (!TestServerConfigManager.isTestServer()) {
				out.println("不是测试服，不允许修改");
				return;
			}
			Pet pet = PetManager.getInstance().getPet(Long.parseLong(playerId));
			pet.setSkillIds(new int[0]);
			pet.setActiveSkillLevels(new int[0]);
			pet.init();
			out.println("ok!");
		} else if ("changepetstate2".equals(action)) {
			if (!TestServerConfigManager.isTestServer()) {
				out.println("不是测试服，不允许修改");
				return;
			}
			Pet pet = PetManager.getInstance().getPet(Long.parseLong(playerId));
			if (!cateid.isEmpty()) {
				if (cateid.contains(",")) {
					String[] aa = cateid.split(",");
					int[] bb = new int[aa.length];
					int[] cc = new int[aa.length];
					for (int i = 0; i < bb.length; i++) {
						bb[i] = Integer.parseInt(aa[i]);
						cc[i] = 1;
					}
					pet.setSkillIds(bb);
					pet.setActiveSkillLevels(cc);
				} else {
					pet.setSkillIds(new int[] { Integer.parseInt(cateid) });
					pet.setActiveSkillLevels(new int[] { 1 });
				}
			}
			if (!carbonLevel.isEmpty()) {
				if (carbonLevel.contains(",")) {
					String[] aa = carbonLevel.split(",");
					int[] bb = new int[aa.length];
					int[] cc = new int[aa.length];
					for (int i = 0; i < bb.length; i++) {
						bb[i] = Integer.parseInt(aa[i]);
						cc[i] = 1;
					}
					pet.setTianFuSkIds(bb);
					pet.setTianFuSkIvs(cc);
				} else {
					pet.setTianFuSkIds(new int[] { Integer.parseInt(carbonLevel) });
					pet.setTianFuSkIvs(new int[] { 1 });
				}
			}
			pet.init();
			out.println("ok!");
		} else if ("changeDeleteFlag".equals(action)) {
			if (playerId == null || playerId.isEmpty()) {
				out.println("输入宠物id！！");
				return;
			}
			long petId = Long.parseLong(playerId);
			if (petId > 0) {
				Pet pet = PetManager.getInstance().getPet(petId);
				pet.setDelete(false);
				out.println("修改成功！！");
			}
		} else if ("setEnchant".equals(action)) {
			if (!TestServerConfigManager.isTestServer()) {
				out.println("不是测试服，不允许修改");
				return;
			}
			EquipmentEntity ee = (EquipmentEntity) DefaultArticleEntityManager.getInstance().getEntity(Long.parseLong(playerId));
			ee.getEnchantData().getEnchants().get(0).setDurable(Integer.parseInt(cateid));
			Player player = GamePlayerManager.getInstance().getPlayer(carbonLevel);
			CoreSubSystem.notifyEquipmentChange(player, new EquipmentEntity[] { ee });
			out.println("设置成功!");
		} else if ("cleanBag".equals(action)) {
			if (!TestServerConfigManager.isTestServer()) {
				out.println("不是测试服，不允许修改");
				return;
			}
			if (playerId == null || playerId.isEmpty()) {
				out.println("输入角色名!");
				return;
			}
			Player player = GamePlayerManager.getInstance().getPlayer(playerId);
			Knapsack bag = player.getKnapsack_common();
			for (int i = 0; i < bag.getCells().length; i++) {
				if (bag.getCells()[i] != null && bag.getCells()[i].getEntityId() > 0) {
					int count = bag.getCells()[i].count;
					for (int ii = 0; ii < count; ii++) {
						bag.remove(i, "测试服后台页面删除", true);
					}
				}
			}
			out.println("清除完毕！");
		} else if ("showDetailPlayers".equals(action)) {
			DevilSquareManager dsm = DevilSquareManager.instance;
			int carbonId = Integer.parseInt(playerId);
			DevilSquare ds = dsm.dsInst.get(carbonId);
			if (ds == null) {
				out.println("没有次等级副本：" + carbonId + "<br>");
				return;
			}
			for (long pId : ds.playerList) {
				Player ploe = GamePlayerManager.getInstance().getPlayer(pId);
				DsPlayerDataModel dpdm = ds.limitMap.get(pId);
				out.println("[" + ploe.getLogString() + "][已经复活次数:" + dpdm.getReliveCount() + "][当前积分:" + dpdm.getCarbonScores() + "]<br>");
			}
		} else if ("testkillmonster".equals(action)) {
			if (!TestServerConfigManager.isTestServer()) {
				out.println("不是测试服，不允许修改");
				return;
			}
			int num = Integer.parseInt(carbonLevel);
			Player p = GamePlayerManager.getInstance().getPlayer(Long.parseLong(playerId));
			Game game = GameManager.getInstance().getGameByDisplayName("飘渺王城", 1);
			DevilSquareManager.testMonsterId = Integer.parseInt(cateid);
			DevilSquareManager.instance.map.clear();
			for (int i = 0; i < num; i++) {
				try {
					Sprite sprite = null;
					sprite = MemoryMonsterManager.getMonsterManager().createMonster(Integer.parseInt(cateid));
					if (sprite != null && sprite instanceof Monster) {
						sprite.setX(100);
						sprite.setY(200);
						((Monster) sprite).setOwner(p);
						sprite.setBornPoint(new Point(sprite.getX(), sprite.getY()));
						game.addSprite(sprite);
						sprite.setHp(0);
					} else {
					}
				} catch (Exception e) {
					out.println(e);
				}
			}
			Iterator<String> ite = DevilSquareManager.instance.map.keySet().iterator();
			while (ite.hasNext()) {
				String key = ite.next();
				int numm = DevilSquareManager.instance.map.get(key);
				String[] ss = key.split("_");
				out.println("[物品名:" + ss[0] + "] [颜色:" + ss[1] + "] [数量:" + numm + "]<BR>");
			}
		} else if ("refreashpotal".equals(action)) {
			if (!TestServerConfigManager.isTestServer()) {
				out.println("不是测试服，不允许修改");
				return;
			}
			DevilSquareManager inst = DevilSquareManager.instance;
			DevilSquareManager.instance.portal = inst.refreshNPC(inst.potalRule.getTransMap(), inst.potalRule.getTransNPCId(), inst.potalRule.getTransPoints().get(0));
			out.println("刷出传送门成功");
		} else if ("checkseal".equals(action)) {
			Iterator<Integer> ite = DevilSquareManager.instance.dsInst.keySet().iterator();
			while (ite.hasNext()) {
				int key = ite.next();
				boolean result = DevilSquareManager.instance.isCarbonSeal(key);
				out.println("[" + key + "级恶魔城堡] [是否处于封印状态 : " + result + " ]");
				if (result) {
					out.println("[解封日期:" + new Timestamp(DevilSquareManager.instance.carbonSealTimes.get(key)) + "]");
				}
				out.println("<br>");
			}
		} else if ("cleanpotal".equals(action)) {
			if (!TestServerConfigManager.isTestServer()) {
				out.println("不是测试服，不允许修改");
				return;
			}
			DevilSquareManager inst = DevilSquareManager.instance;
			inst.removeTransNPC(inst.potalRule.getTransMap(), inst.portal);
			DevilSquareManager.instance.portal = null;
			out.println("清除传送门成功");
		} else if ("testrefreashbox".equals(action)) {
			if (!TestServerConfigManager.isTestServer()) {
				out.println("不是测试服，不允许修改");
				return;
			}
			Player p = GamePlayerManager.getInstance().getPlayer(Long.parseLong(playerId));

			Article at = ArticleManager.getInstance().getArticle("岁末礼包");
			if (at == null) {
				return;
			}
			List<Long> ll = new ArrayList<Long>();
			ll.add(Long.parseLong(playerId));
			FlopCaijiNpc npc = getFlopCaijiNpc(DevilSquareConstant.boxCateId, at, 5 * 60, ll);
			npc.setX(p.getX());
			npc.setY(p.getY());
			p.getCurrentGame().addSprite(npc);
			out.println("设置成功111111111");

		} else if ("checkpoints".equals(action)) {
			Player p = GamePlayerManager.getInstance().getPlayer(Long.parseLong(playerId));
			out.println(p.getX() + "&&&&" + p.getY());
		} else if ("refreashNPC".equals(action)) {
			if (!TestServerConfigManager.isTestServer()) {
				out.println("不是测试服，不允许修改");
				return;
			}
			NPC npc = null;
			Game game = null;
			for (int i = 0; i <= 3; i++) {
				game = GameManager.getInstance().getGameByName(playerId, i);
				//			game = GameManager.getInstance().getGameByDisplayName(mapName, i);
				if (game == null) {
					out.println("*****没有找到game***************<BR>");
					continue;
				}
				try {
					npc = MemoryNPCManager.getNPCManager().createNPC(Integer.parseInt(carbonLevel));
					Player p = GamePlayerManager.getInstance().getPlayer("qwer");
					if (npc != null) {
						npc.setX(p.getX() + 30);
						npc.setY(p.getY() + 50);
						game.addSprite(npc);
						out.println("****刷新成功&&" + i + "*******<BR>");
					} else {
						out.println("****刷新失败&&" + i + "*******<BR>");
					}
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
			out.println("**********************");
		} else if ("checkcarbonstatus".equals(action)) {
			List<Integer> oot = DevilSquareManager.instance.getActHour();
			for (Integer it : oot) {
				out.println("恶魔广场开放时间: " + it + " 点<BR>");
			}
			out.println("<BR> <BR>");
			DevilSquareManager dsm = DevilSquareManager.instance;
			Iterator<Integer> ite = dsm.dsInst.keySet().iterator();
			while (ite.hasNext()) {
				int key = ite.next();
				DevilSquare ds = dsm.dsInst.get(key);
				out.println("[" + key + "级副本状态为：" + ds.getStatus() + "]**[当前副本人数为：" + ds.playerList.size() + "]<BR>");
				out.println("[最后更改状态时间:" + new Timestamp(ds.lastStatusChangeT) + "]****[当前刷怪波数：" + ds.refreashTimes + "]<BR>");
				out.println("[副本当前怪物数量" + ds.game.getMonsterNum() + "]<BR>");
				out.println("[刷宝箱次数:" + ds.boxRefreashTimes + "&& 刷宝箱标记:" + ds.isBoxAct + "]<BR>");
				out.println("副本开启时间:" + new Timestamp(ds.openTime) + "<br>");
				out.println("副本持续时间:" + ds.carbonLastTime + "<br>");
				out.println("副本踢人时间:" + ds.kickoutTime + "<br>");
				out.println("副本是否pvp限制:" + ds.game.gi.isLimitPVP() + "<BR><BR>");
			}
			for (String ss : DevilSquareManager.carbonMaps) {
				out.println("[恶魔广场副本地图有][" + ss + "]<BR>");
			}
		} else if ("changecarbonstatus".equals(action)) {
			if (!TestServerConfigManager.isTestServer()) {
				out.println("不是测试服，不允许修改");
				return;
			}
			DevilSquareManager dsm = DevilSquareManager.instance;
			//dsm.setCarbonStatus(Integer.parseInt(playerId), Byte.parseByte(carbonLevel));
			if (dsm.dsInst.get(Integer.parseInt(playerId)) != null) {
				dsm.dsInst.get(Integer.parseInt(playerId)).setStatus(Byte.parseByte(carbonLevel));
				if (Byte.parseByte(carbonLevel) == DevilSquareConstant.PREPARE) {
					dsm.dsInst.get(Integer.parseInt(playerId)).openTime = System.currentTimeMillis();
					dsm.dsInst.get(Integer.parseInt(playerId)).refreashTimes = 0;
					out.println("****" + dsm.dsInst.get(Integer.parseInt(playerId)).lastTime + "******");
				}
			}
			out.println("设置成功");
		}
	%>
	<%!private FlopCaijiNpc getFlopCaijiNpc(int categoryId, Article article, int boxCleanTime, List<Long> playerList) {
		ArticleEntity ae = null;
		try {
			ae = ArticleEntityManager.getInstance().createEntity(article, true, ArticleEntityManager.活动, null, article.getColorType(), 1, true);
		} catch (Exception e) {
			return null;
		}
		NPCManager nm = MemoryNPCManager.getNPCManager();
		FlopCaijiNpc fcn = (FlopCaijiNpc) nm.createNPC(MemoryNPCManager.掉落NPC的templateId);
		fcn.setStartTime(com.fy.engineserver.gametime.SystemTime.currentTimeMillis());
		fcn.setAllCanPickAfterTime(1000);
		fcn.setEndTime(SystemTime.currentTimeMillis() + boxCleanTime * 1000);
		ResourceManager.getInstance().getAvata(fcn);
		fcn.setPlayersList(playerList);
		fcn.setAe(ae);
		fcn.setNameColor(ArticleManager.getColorValue(article, article.getColorType()));
		fcn.setCount(1);
		fcn.setName(article.getName());
		Article at = ArticleManager.getInstance().getArticle(ae.getArticleName());
		if (at.getFlopNPCAvata() != null) {
			fcn.setAvataSex(at.getFlopNPCAvata());
			if (article.getFlopNPCAvata().equals("yinyuanbao")) {
				fcn.setAvataSex("baoxiang");
			}
			ResourceManager.getInstance().getAvata(fcn);
		}
		return fcn;
	}%>>
</body>
</html>
