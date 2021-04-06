<%@page import="com.xuanzhi.boss.game.GameConstants"%>
<%@page import="com.fy.engineserver.enterlimit.EnterLimitManager"%>
<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<%@page import="com.fy.engineserver.newtask.service.TaskManager"%>
<%@page import="java.util.List"%>
<%@page import="com.fy.engineserver.newtask.Task"%>
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title>Insert title here</title>
</head>
<body>
	<%
	String action = request.getParameter("action");
	if (action != null) {
		if (action.equals("jieError")) {
			String userS = request.getParameter("userS");
			String[] us = userS.split(",");
			EnterLimitManager manager = EnterLimitManager.getInstance();
			out.print(GameConstants.getInstance().getServerName());
			out.print("<br>");
			for (int i = 0 ; i < us.length; i++) {
				if (manager.inEnterLimit(us[i],null)) {
					out.print("找到["+us[i]+"]");
				}else {
					out.print("未["+us[i]+"]");
				}
			}
			manager.removeFromlimit(us);
			out.print("<br>");
			out.print("操作完成");
		}
	}
	%>
</body>
</html>
