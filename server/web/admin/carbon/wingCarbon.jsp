<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<%@page import="java.util.Random"%>
<%@page import="com.fy.engineserver.platform.PlatformManager.Platform"%>
<%@page import="com.fy.engineserver.platform.PlatformManager"%>
<%@page import="com.fy.engineserver.sprite.npc.WingCarbonNPC"%>
<%@page import="com.fy.engineserver.carbon.instance.CarBonEntityModel"%>
<%@page import="com.fy.engineserver.carbon.CarbonEntityManager"%>
<%@page import="com.fy.engineserver.carbon.instance.CarBonEntity"%>
<%@page import="java.util.Arrays"%>
<%@page import="com.fy.engineserver.carbon.wingCarbon.instance.WingCarbon"%>
<%@page import="com.fy.engineserver.carbon.wingCarbon.model.RewardRuleModel"%>
<%@page import="com.fy.engineserver.carbon.wingCarbon.WingCarbonManager"%>
<%@page import="com.fy.engineserver.carbon.wingCarbon.model.WingCarbonBaseModel"%>
<%@page import="com.fy.engineserver.datasource.article.data.props.Cell"%>
<%@page import="com.fy.engineserver.datasource.article.data.props.Knapsack"%>
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
		String carbonLevel = request.getParameter("carbonLevel");
		String cateid = request.getParameter("cateid");
		action = action == null ? "" : action;//1
		playerId = playerId == null ? "" :playerId;
		cateid = cateid == null ? "" :cateid;
		carbonLevel = carbonLevel== null ? "" : carbonLevel;
		
		
	%>
	<form action="wingCarbon.jsp" method="post">
		<input type="hidden" name="action" value="testDrop" />副本难度   :
		<input type="text" name="playerId" value="<%=playerId%>" />副本玩家数量:
		<input type="text" name="carbonLevel" value="<%=carbonLevel%>" />
		<input type="submit" value="测试副本掉率" />
	</form>
	<br />
	<form action="wingCarbon.jsp" method="post">
		<input type="hidden" name="action" value="resetEnterTimes" />角色名   :
		<input type="text" name="playerId" value="<%=playerId%>" />
		<input type="submit" value="重置进入副本次数" />
	</form>
	<form action="wingCarbon.jsp" method="post">
		<input type="hidden" name="action" value="resetWUDITimes" />角色名   :
		<input type="text" name="playerId" value="<%=playerId%>" />
		<input type="submit" value="清除雷震子无敌使用次数" />
	</form>
	
	<%
	if("testDrop".equals(action)) {
		WingCarbonBaseModel model = WingCarbonManager.instance.carbonMaps.get(Integer.parseInt(playerId));
		if (model == null) {
			out.println("副本等级不对！");
			return ;
		}
		List<Long> playerList = new ArrayList<Long>();
		for (int ii=0; ii<Integer.parseInt(carbonLevel); ii++) {
			playerList.add(Long.parseLong((ii+1)+""));
		}
		for (int i=0; i<=model.getMaxRefreshTimes(); i++) {
			RewardRuleModel reModel = model.getRewardRuleModelByRefreshTimes(i, WingCarbon.ran);
			if (reModel != null) {
				out.println(notifyReward(reModel, playerList, i));
				out.println("<br>");
			}
		}
	} else if ("resetWUDITimes".equals(action)) {
		if(!TestServerConfigManager.isTestServer()) {
			out.println("不是测试服，不允许修改");
			return;
		}
		Player player = GamePlayerManager.getInstance().getPlayer(playerId);
		WingCarbon carbon = WingCarbonManager.instance.getWingCarbonByPlayerId(player.getId());
		if (carbon == null) {
			out.println("玩家不在副本内！");
			return ;
		}
		if (!(carbon.getNpc() instanceof WingCarbonNPC)) {
			return ;
		}
		carbon.isUseWudi = false;
		carbon.isUseReHp = false;
		out.println("修改成功！");
	} else if ("resetEnterTimes".equals(action)) {
		if(!TestServerConfigManager.isTestServer()) {
			out.println("不是测试服，不允许修改");
			return;
		}
		Player player = GamePlayerManager.getInstance().getPlayer(playerId);
		CarBonEntity entity = CarbonEntityManager.instance.getEntity(player.getId());
		List<CarBonEntityModel> lists = entity.getCarBonList();
		for (CarBonEntityModel cm : lists) {
			cm.setCycleCounts(0);
		}
		entity.setCarBonList(lists);
		out.println("重置成功！！");
	}
			
	%>
	<%!
	private String notifyReward(RewardRuleModel reModel, List<Long> playerList, int refreshTimes) {
		if (playerList.size() <= 0) {
			return "";
		}
		StringBuffer sb = new StringBuffer();
		sb.append("[第"+(refreshTimes)+"波怪物奖励]<br>");
		try {
			int ranIndex = WingCarbon.ran.nextInt(playerList.size());
			long chiBang = 0;
			if (reModel.getWingArticle() != null && reModel.getWingArticle().length > 0) {
				
				chiBang = playerList.get(ranIndex);
				sb.append("[玩家:" + chiBang + "] [获得翅膀: ");
				Player player = null;
				try {
				if (PlatformManager.getInstance().isPlatformOf(Platform.官方)) {
					player = GamePlayerManager.getInstance().getPlayer("qwer");
				} else if (PlatformManager.getInstance().isPlatformOf(Platform.腾讯)) {
					player = GamePlayerManager.getInstance().getPlayer("qwe");
				}
				}catch (Exception e) {}
				
				int ran = 0;
				if (player != null) {
					ran = player.random.nextInt(100);
				} else {
					ran = new Random().nextInt(100);
				}
				int tempProb = 0;
				int wingIndex = 0;
				for (int i=0; i<reModel.getWingProbs().length; i++) {
					tempProb += reModel.getWingProbs()[i]; 
					if (ran <= tempProb) {
						wingIndex = i;
						break;
					}
				}
				sb.append(reModel.getWingArticle()[wingIndex] + "]<br>");
			}
			for (long pId : playerList) {
				if (pId == chiBang) {
					continue;
				}
				Player plo = null;
				try {
				if (PlatformManager.getInstance().isPlatformOf(Platform.官方)) {
					plo = GamePlayerManager.getInstance().getPlayer("qwer");
				} else if (PlatformManager.getInstance().isPlatformOf(Platform.腾讯)) {
					plo = GamePlayerManager.getInstance().getPlayer("qwe");
				}
				}catch (Exception e) {}
				
				int ran = 0;
				if (plo != null) {
					ran = plo.random.nextInt(100);
				} else {
					ran = new Random().nextInt(100);
				}
				if (reModel.getArticleNames() != null && reModel.getArticleNames().length > 0) {
					
					int totalProb = 0;
					for (int i=0; i<reModel.getArticleProb().length; i++) {
						totalProb += reModel.getArticleProb()[i];
					}
					//int ran = plo.random.nextInt(100);
					int tempProb = 0;
					int gainTimesIndex = 0;
					for (int kk=0; kk<reModel.getProbabbly().length; kk++) {
						tempProb += reModel.getProbabbly()[kk];
						if (ran <= tempProb) {
							gainTimesIndex = kk;
							break;
						}
					}
					sb.append("[玩家 :" + pId + "] [获得物品次数:" + reModel.getGainCounts()[gainTimesIndex] + "]");
					for (int ii=0; ii<reModel.getGainCounts()[gainTimesIndex]; ii++) {
						int ran2 = plo.random.nextInt(totalProb);
						int tempRan = 0;
						int rewardIndex = 0;
						for (int kk=0; kk<reModel.getArticleProb().length; kk++) {
							tempRan += reModel.getArticleProb()[kk];
							if (ran2 <= tempRan) {
								rewardIndex = kk;
								break;
							}
						}
						String articleCnnName = reModel.getArticleNames()[rewardIndex];
						if (ii == 0) {
							sb.append("[获得物品:" + articleCnnName + "]");
						} else {
							sb.append( articleCnnName + "]");
						}
					}
					sb.append("<br>");
				}
			}
		} catch (Exception e) {
			WingCarbonManager.logger.warn("[翅膀副本] [发奖励] [异常] [翅膀名:" + reModel + "] " , e);
		}
		return sb.toString();
	}
	%>>
</body>
</html>
