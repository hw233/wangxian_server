<%@page import="com.fy.engineserver.core.Game"%>
<%@page import="com.fy.engineserver.sprite.Player"%>
<%@page import="com.fy.engineserver.sprite.PlayerManager"%>
<%@page import="com.fy.engineserver.chat.ChatMessageService"%>
<%@page import="com.fy.engineserver.sprite.Soul"%>
<%@page import="com.fy.engineserver.newtask.Task"%>
<%@page import="com.fy.engineserver.newtask.service.TaskManager"%>
<%@page import="java.lang.reflect.Field"%>
<%@page import="com.fy.engineserver.activity.farming.FarmingTaskEvent"%>
<%@page import="com.fy.engineserver.activity.farming.FarmingManager"%>
<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title>Insert title here</title>
</head>
<body>
<%
	String action = request.getParameter("action");
	if (action != null) {
		String pName = request.getParameter("pName");
		Game.logger.warn("[沉默操作] [action:"+action+"] [pName:"+pName+"]");
// 		if ("silnce".equals(action)) {
// 			int hour = Integer.parseInt(request.getParameter("hour"));
// 			String reason = request.getParameter("reason");
// 			int sLevel = Integer.parseInt(request.getParameter("sLevel"));
// 			Player p = PlayerManager.getInstance().getPlayer(pName);
// 			ChatMessageService.getInstance().silencePlayer(p.getId(), hour, reason, sLevel);
// 		}else if ("unSilnce".equals(action)) {
// 			Player p = PlayerManager.getInstance().getPlayer(pName);
// 			ChatMessageService.getInstance().unSilencePlayer(p.getId());
// 		}
	}
%>
<form>
	<input type="hidden" name="action" value="silnce">
	角色名字<input type="text" name="pName">
	时间(小时)<input type="text" name="hour">
	原因<input type="text" name="reason">
	级别(1是沉默，2是封交易包括邮件)<input type="text" name="sLevel">
	<input type="submit" value="确定">
</form>
<br>
<br>
清除沉默
<form>
	<input type="hidden" name="action" value="unSilnce">
	角色名字<input type="text" name="pName">
	<input type="submit" value="确定">
</form>
</body>

</html>
