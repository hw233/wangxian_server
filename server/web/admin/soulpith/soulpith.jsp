<%@page import="com.fy.engineserver.soulpith.module.SoulpithInfo4Client"%>
<%@page import="com.fy.engineserver.datasource.article.manager.ArticleEntityManager"%>
<%@page import="com.fy.engineserver.datasource.article.data.entity.SoulPithArticleEntity"%>
<%@page import="com.fy.engineserver.soulpith.instance.PlayerSoulpithInfo"%>
<%@page import="com.fy.engineserver.sprite.Soul"%>
<%@page import="com.fy.engineserver.soulpith.SoulPithConstant"%>
<%@page import="com.fy.engineserver.soulpith.SoulPithEntityManager"%>
<%@page import="com.fy.engineserver.sprite.concrete.GamePlayerManager"%>
<%@page import="com.fy.engineserver.sprite.Player"%>
<%@page import="com.fy.engineserver.soulpith.instance.SoulPithEntity"%>
<%@page import="java.util.Arrays"%>
<%@page import="java.sql.Timestamp"%>
<%@page import="com.fy.engineserver.activity.disaster.DisasterManager"%>
<%@page import="com.fy.engineserver.activity.disaster.module.DisasterOpenModule"%>
<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">

<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title>Insert title here</title>
</head>
<body>
	<%
		String playerId = request.getParameter("playerId");
		String action = request.getParameter("action");
		String startHour = request.getParameter("startHour");
		playerId = playerId == null ? "" : playerId;
		action = action == null ? "" : action;
		startHour = startHour == null ? "" : startHour;
		if ("checkPlayerSoulpith".equals(action)) {
			Player player = GamePlayerManager.getInstance().getPlayer(playerId);
			SoulPithEntity entity = SoulPithEntityManager.getInst().getEntity(player);
			if (entity == null) {
				out.println("[玩家等级太低没开灵根] ["+player.getLogString()+"] [灵根开启需要等级" + SoulPithConstant.openLevel + "]<br>");
				return ;
			}
			for (Soul s : player.getSouls()) {
				PlayerSoulpithInfo info = entity.getPlayerSoulpithInfo(s.getSoulType());
				out.println("元神类型:" +( s.getSoulType() == Soul.SOUL_TYPE_BASE ? "本尊" : "元神") + "<br>");
				for (int i=0; i<info.getPiths().length; i++) {
					SoulPithArticleEntity ae = null;
					long id = info.getPiths()[i];
					if (id > 0) {
						ae = (SoulPithArticleEntity)ArticleEntityManager.getInstance().getEntity(id);
					}
					out.println("[格子位置:"+i+"] [镶嵌道具id:" + id + "] [灵髓:" + (ae != null ? ae.getLogString() : "") + "]<br>");
				}
			}
			out.println("<br>");
			out.println("灵根附加属性");
			out.println("<br>");
			if (entity.getOldAddProps() != null) {
				for (int i=0; i<entity.getOldAddProps().getAddTypes().size(); i++) {
					out.println("[" + entity.getOldAddProps().getAddTypes().get(i).getName() + "," + entity.getOldAddProps().getPropertys().get(i).getName() + "," + entity.getOldAddProps().getPropertyNums().get(i) + "]<br>");
				}
			}
			SoulpithInfo4Client cc = new SoulpithInfo4Client(player, player.getCurrSoul().getSoulType());
			out.println("soulNums:" + Arrays.toString(cc.getSoulNums()) + "<br>");
			out.println("des:" + Arrays.toString(cc.getDescription()) + "<br>");
		} else if ("checksoulinfo".equals(action)) {
			Player player = GamePlayerManager.getInstance().getPlayer(playerId);
			SoulpithInfo4Client cc = new SoulpithInfo4Client(player, Integer.parseInt(startHour));
			out.println("soulNums:" + Arrays.toString(cc.getSoulNums()) + "<br>");
			out.println("inlayInfos:" + Arrays.toString(cc.getInlayInfos()) + "<br>");
			out.println("description:" + Arrays.toString(cc.getDescription()) + "<br>");
		}
	%>

	<form action="soulpith.jsp" method="post">
		<input type="hidden" name="action" value="checkPlayerSoulpith" />角色名:
		<input type="text" name="playerId" value="<%=playerId%>" />
		<input type="submit" value="查看玩家灵根镶嵌及激活属性" />
	</form>
	<br />
	<form action="soulpith.jsp" method="post">
		<input type="hidden" name="action" value="checksoulinfo" />角色名:
		<input type="text" name="playerId" value="<%=playerId%>" />元神类型:
		<input type="text" name="startHour" value="<%=startHour%>" />
		<input type="submit" value="查看元神信息" />
	</form>
	<br />
</body>
</html>