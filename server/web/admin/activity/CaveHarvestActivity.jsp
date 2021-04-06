<%@page import="java.util.Date"%>
<%@page import="java.text.SimpleDateFormat"%>
<%@page import="com.fy.engineserver.menu.cave.activity.CaveHarvestActivityManager"%>
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
		SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		if (action != null) {
			if ("setting".equals(action)) {
				String startTime = request.getParameter("startTime");
				String endTime = request.getParameter("endTime");
				CaveHarvestActivityManager.startTime = format.parse(startTime).getTime();
				CaveHarvestActivityManager.endTime = format.parse(endTime).getTime();
			}
		}
		
	%>
	活动开始时间   <%=format.format(new Date(CaveHarvestActivityManager.startTime))%>
	<br>
	活动结束时间	<%=format.format(new Date(CaveHarvestActivityManager.endTime))%>
	
	<br>
	<form name="f4">
		设置剪刀活动时间
		<input type="hidden" name="action" value="setting">
		开始<input name="startTime" type="text" value="2013-01-18 17:00:00">
		结束<input name="endTime" type="text" value="2013-01-18 17:00:00">
		<input type="submit" value="设置">
	</form>
	
</body>
</html>
