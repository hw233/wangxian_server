<%@page import="com.fy.engineserver.marriage.manager.MarriageManager"%>
<%@page import="com.fy.engineserver.playerTitles.PlayerTitlesManager"%>
<%@page import="com.fy.engineserver.datasource.language.Translate"%>
<%@page import="com.fy.engineserver.society.Relation"%>
<%@page import="com.fy.engineserver.society.SocialManager"%>
<%@page import="com.fy.engineserver.sprite.PlayerManager"%>
<%@page import="com.fy.engineserver.sprite.Player"%>
<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<%@page import="com.fy.engineserver.newtask.service.TaskManager"%>
<%@page import="java.util.List"%>
<%@page import="com.fy.engineserver.newtask.Task"%><html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title>Insert title here</title>
</head>
<body>
<%
	String action = request.getParameter("action");
	if (action != null) {
		if (action.equals("setMarriage")) {
		String name = request.getParameter("playerName");
		try{
			Player player = PlayerManager.getInstance().getPlayer(name);
			if (player != null) {
				Relation relation = SocialManager.getInstance().getRelationById(player.getId());
				relation.setMarriageId(-1);
				PlayerTitlesManager.getInstance().removeTitle(player, Translate.text_marriage_结婚);
			}
		}catch(Exception e) {
			
		}
		}else if (action.equals("setTextTime")) {
			MarriageManager.ONE_HOUR = 5 * 60*1000L;
			MarriageManager.LIHUN_SPACETIME = 60 * 1000L;
			MarriageManager.hunli_time = new long[]{3*60*1000, 60*1000, 0};
		}
	}
%>
	
	<form name="f1">
		<input type="hidden" name="action" value="setMarriage">
		玩家名字<input name="playerName">
		<input type="submit" value="清除结婚关系">
	</form>
	<br>
	<form name="f2">
		<input type="hidden" name="action" value="setTextTime">
		<input type="submit" value="把结婚时间设置成测试">
	</form>
</body>
</html>
