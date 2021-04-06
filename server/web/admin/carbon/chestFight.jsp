<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<%@page import="com.fy.engineserver.util.server.TestServerConfigManager"%>
<%@page import="java.sql.Timestamp"%>
<%@page import="java.util.Calendar"%>
<%@page import="com.fy.engineserver.activity.chestFight.ChestFightManager"%>
<%@page import="com.fy.engineserver.activity.chestFight.model.ChestBaseModel"%>
<%@ page language="java" contentType="text/html; charset=utf-8"
	pageEncoding="utf-8"%>
<head>
</head>
<body>
	<%
		String action = request.getParameter("action");
		String playerId = request.getParameter("playerId");
		String openDay = request.getParameter("openDay");
		String openHour = request.getParameter("openHour");
		String openMin = request.getParameter("openMin");
		action = action == null ? "" : action;//1
		playerId = playerId == null ? "" :playerId;
		openDay = openDay == null ? "" :openDay;
		openHour = openHour == null ? "" :openHour;
		openMin = openMin == null ? "" :openMin;
		
		
	%>
	<form action="chestFight.jsp" method="post">
		<input type="hidden" name="action" value="checkChest" />
		<input type="submit" value="查看宝箱争夺信息" />
	</form>
	<br />
	<form action="chestFight.jsp" method="post">
		<input type="hidden" name="action" value="setOpenTime" /> 开启周(1周天 )  :
		<input type="text" name="openDay" value="<%=openDay%>" />小时  :
		<input type="text" name="openHour" value="<%=openHour%>" />分  :
		<input type="text" name="openMin" value="<%=openMin%>" />
		<input type="submit" value="修改开启时间" />
	</form>
	<br />
	
	<%
	if("checkChest".equals(action)) {
		ChestBaseModel baseModel = ChestFightManager.inst.baseModel;
		for (int i=0; i<baseModel.getOpenDayOfWeek().length; i++) {
			Calendar cal = Calendar.getInstance();
			cal.set(Calendar.DAY_OF_WEEK, baseModel.getOpenDayOfWeek()[i]);
			cal.set(Calendar.HOUR_OF_DAY, baseModel.getOpenHourOfDay()[i]);
			cal.set(Calendar.MINUTE, baseModel.getOpenMinitOfHour()[i]);
			cal.set(Calendar.SECOND, 0);
			cal.set(Calendar.MILLISECOND, 0);
			long openTime = cal.getTimeInMillis();
			out.println("[开启时间] [" + new Timestamp(openTime) + "]<br>");
		}
		out.println("<br><br>");
		out.println(ChestFightManager.inst.fight.getLogString());
	} else if ("setOpenTime".equals(action)) {
		if(!TestServerConfigManager.isTestServer()) {
			out.println("不是测试服，不允许修改");
			return;
		}
		int day = Integer.parseInt(openDay);
		int hour = Integer.parseInt(openHour);
		int min = Integer.parseInt(openMin);
		ChestFightManager.inst.baseModel.setOpenDayOfWeek(new int[]{day});
		ChestFightManager.inst.baseModel.setOpenHourOfDay(new int[]{hour});
		ChestFightManager.inst.baseModel.setOpenMinitOfHour(new int[]{min});
		out.println("设置成功!");
	}
			
	%>
</body>
</html>
