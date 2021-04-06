	<%@page import="java.util.Date"%>
<%@page import="java.text.SimpleDateFormat"%>
<%@page import="com.fy.engineserver.vip.data.VipAgent"%>
	<%@page import="com.fy.engineserver.sprite.concrete.GamePlayerManager"%>
	<%@page import="com.fy.engineserver.sprite.Player"%>
	<%@ page language="java" contentType="text/html; charset=UTF-8"
	    pageEncoding="UTF-8"%>
	<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
	<html>
	<%
		String name = request.getParameter("name");
		if (name == null) {
			name = "";
		}
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		if (!"".equals(name)) {
			Player p = GamePlayerManager.getInstance().getPlayer(name);
			if (p != null) {
				VipAgent agent = p.getVipAgent();
				String option = request.getParameter("option");
				if ("query".equals(option)) {
					out.print(agent.toString());
					out.print("<hr>");
					out.print("上次摇奖时间:" + sdf.format(new Date( agent.getLastLotterytime())));
					out.print("上次记录时间:" + sdf.format(new Date( agent.getLastRecordtime())));
					
				} else if ("clean".equals(option)){
					if ("666".equals(name)) {
						p.getVipAgent().setLastLotterytime(sdf.parse("2016-04-20 00:00:00").getTime());
					} else {
						p.getVipAgent().setLastLotterytime(0L);
						p.getVipAgent().setLastRecordtime(0L);
						p.setVipAgent(p.getVipAgent());
					}
					out.print("信息清除成功");
				}
			} else {
				out.print("角色不存在");
			}
		} else {
			out.print("输入角色名<BR>");
		}
	%>
	<head>
	<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
	<title>Insert title here</title>
	</head>
	<body>
	<form action="./vip.jsp" method="post">
	角色名:<input type="text" name="name" value="<%=name%>" >
	<input type="submit" value="check">
	<input type="hidden" value="query" name="option">
	</form>
	
	<form action="./vip.jsp" method="post">
		<input type="hidden" value="clean" name="option">
		<input type="hidden" value="<%=name %>" name="name">
		<input type="submit" value="清除信息">
	</form>
	</body>
	</html>