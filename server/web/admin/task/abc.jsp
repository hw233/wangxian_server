<%@page import="com.fy.engineserver.newtask.Task"%>
<%@page import="com.fy.engineserver.newtask.service.TaskManager"%>
<%@page import="com.fy.engineserver.sprite.concrete.GamePlayerManager"%>
<%@page import="com.fy.engineserver.sprite.Player"%>
<%@page import="com.fy.engineserver.newtask.service.DeliverTaskManager"%>
<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<%
	DeliverTaskManager dt = DeliverTaskManager.getInstance();
	if (dt == null) {
		out.print("DT NULL");
	} else {
		//Player player = GamePlayerManager.getInstance().getPlayer("wsy")
		//if (player == null) {
		//	out.print("player NULL");
		//} else {
		//	Task task = TaskManager.getInstance().getTask("采集冬梅花")
		//	dt.isDelived(player,task);
		//}
	}
%>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title>Insert title here</title>
</head>
<body>

</body>
</html>