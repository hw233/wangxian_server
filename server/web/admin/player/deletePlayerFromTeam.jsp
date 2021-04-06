<%@ page language="java" contentType="text/html; charset=utf-8"
	pageEncoding="utf-8"%>
<%@page import="java.util.*"%>

<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<%@page
	import="com.fy.engineserver.datasource.article.data.articles.Article"%>
<%@page import="com.fy.engineserver.sprite.pet.PetManager"%>
<%@page import="com.fy.engineserver.sprite.pet.Pet"%>
<%@page import="com.fy.engineserver.sprite.Player"%>
<%@page import="com.fy.engineserver.sprite.PlayerManager"%>
<%@page
	import="com.fy.engineserver.datasource.article.data.entity.PetPropsEntity"%>
<%@page
	import="com.fy.engineserver.datasource.article.manager.ArticleEntityManager"%>
<%@page
	import="com.fy.engineserver.datasource.article.data.entity.PetEggPropsEntity"%>
<%@page
	import="com.fy.engineserver.datasource.article.data.entity.ArticleEntity"%>
<%@page import="com.fy.engineserver.mail.service.MailManager"%>
<%@page
	import="com.fy.engineserver.datasource.article.manager.ArticleManager"%>
<%@page import="com.fy.engineserver.sprite.Team"%>
<%@page import="com.fy.engineserver.sprite.TeamSubSystem"%><html>
<head>
<title>帮玩家退出队伍</title>
</head>
<body>

<%
	String playerName = request.getParameter("playerName");
	if (playerName == null || playerName.equals("")) {

	} else {
		Player player = null;
		try {
			player = PlayerManager.getInstance().getPlayer(playerName);
			if (player != null) {
				Team team = player.getTeam();
				if (team != null) {
					out.print("teamId:" + team.getId() + "<br/>");
					List<Player> members = team.getMembers();
					if (members != null) {
						out.print("members.size:"+members.size()+"<br>");
						Player ps[] = members.toArray(new Player[0]);
						team.removeMember(player, 0);
						//让客户端清空费队友
						if (ps != null) {
							for (int i = 0; i < ps.length; i++) {
								if (ps[i] != null) {
									out.print("test4<br>");
									if (ps[i].getAroundNotifyPlayerNum() == 0) {
										ps[i].setNewlySetAroundNotifyPlayerNum(true);
									}
								} else {
									out.print("玩家不存在，index：" + i);
								}
							}
							if (TeamSubSystem.logger.isWarnEnabled()) {
								TeamSubSystem.logger.warn("[后台帮助玩家主动离队] [成功] [玩家主动离队] [" + player.getLogString() + "]");
							}
							out.print("已成功帮玩家离开队伍！");
						} else {
							out.print("ps=null<br>");
						}
					} else {
						out.print("没有队员");
					}
				} else {
					out.print("该玩家没有队伍！");
				}
			} else {
				out.print("无此玩家，请检查玩家角色名是否正确！");
			}
		} catch (Exception e) {
			out.print(e);
			TeamSubSystem.logger.error("后台帮玩家离开队伍异常", e);
		}
	}
%>

<form action="">角色名:<input type="text" name="playerName"
	/ value="<%=playerName%>"><br />
<input type="submit" value="submit" /></form>



</body>
</html>
