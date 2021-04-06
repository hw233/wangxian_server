<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">

<%@page import="com.fy.engineserver.sprite.PlayerManager"%>
<%@page import="com.fy.engineserver.sprite.Player"%>
<%@page import="com.fy.engineserver.datasource.language.Translate"%>
<%@page
	import="com.fy.engineserver.playerTitles.PlayerTitlesManager"%>
<%@page import="com.fy.engineserver.society.SocialManager"%><html>
<%
	String name1 = request.getParameter("name1");
	String name2 = request.getParameter("name2");
	if (name1 != null && name2 != null && !"".equals(name1) && !"".equals(name2)) {

		Player player = PlayerManager.getInstance().getPlayer(name1);
		Player other = PlayerManager.getInstance().getPlayer(name2);
		if (player != null && other != null) {
			if (SocialManager.getInstance().getRelationA2B(player.getId(), other.getId()) != 4) {
				out.print("这两人不是道侣");
				return;
			}
			String title0 = "";
			String title1 = "";
			if (player.getSex() == 0) {
				title0 = player.getName() + Translate.text_marriage_105;
				title1 = other.getName() + Translate.text_marriage_105;
				PlayerTitlesManager.getInstance().addSpecialTitle(player, Translate.text_marriage_结婚, title1, true);
				PlayerTitlesManager.getInstance().addSpecialTitle(other, Translate.text_marriage_结婚, title0, true);
			} else {
				title0 = other.getName() + Translate.text_marriage_105;
				title1 = player.getName() + Translate.text_marriage_105;
				PlayerTitlesManager.getInstance().addSpecialTitle(player, Translate.text_marriage_结婚, title0, true);
				PlayerTitlesManager.getInstance().addSpecialTitle(other, Translate.text_marriage_结婚, title1, true);
			}
			out.print("添加称号成功:<br>" + title0 + "<br>" + title1 + "<br>");
		} else {
			out.print("玩家不存在,请确认输入的id是否正确");
		}
	}
%>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title>为玩家补发结婚称号</title>
</head>
<body>
<form action="">请输入玩家角色名: <input type="text" name="name1"
	value="" /> <input type="text" name="name2" value="" /> <input
	type="submit" value="提交" /></form>
</body>
</html>