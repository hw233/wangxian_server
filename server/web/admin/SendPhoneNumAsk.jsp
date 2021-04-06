<%@page import="com.fy.engineserver.enterlimit.PhoneNumMananger"%>
<%@page import="com.fy.engineserver.activity.chongzhiActivity.ChongZhiSpecialActivity"%>
<%@page import="java.text.SimpleDateFormat"%>
<%@page import="com.sqage.stat.commonstat.entity.ChongZhi"%>
<%@page import="com.fy.engineserver.sprite.PlayerManager"%>
<%@page import="com.fy.engineserver.sprite.Player"%>
<%@page import="com.xuanzhi.boss.game.GameConstants"%>
<%@page import="com.fy.engineserver.activity.chongzhiActivity.ChongZhiServerConfig"%>
<%@page import="java.util.HashMap"%>
<%@page import="com.fy.engineserver.activity.chongzhiActivity.ChongZhiActivity"%>
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
			if (action.equals("text_send")) {
				String name = request.getParameter("playerName");
				Player player = PlayerManager.getInstance().getPlayer(name);
				PhoneNumMananger.getInstance().send_PHONENUM_ASK_RES(player);
			}
		}
	%>
	<form name="f1">
		<input type="hidden" name="action" value="text_send">
		玩家名字<input name="playerName">
		<input type="submit" value="确定">
	</form>
</body>
</html>
