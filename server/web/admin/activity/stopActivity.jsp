<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<
<%@page import="java.util.Calendar"%>
<%@page import="com.fy.engineserver.activity.ActivityManager"%>
<%@page import="com.fy.engineserver.gametime.SystemTime"%>
<%@page import="com.fy.engineserver.activity.fireActivity.FireManager"%>
<%@page import="com.fy.engineserver.activity.ActivityThread"%>
<%@page import="com.fy.engineserver.newBillboard.monitorLog.LogRecordManager"%>
<%@page import="java.util.Date"%><html>
	<head>
		<title>停止活动线程 </title>
	</head>
	
	<body>
	
		<%
			String st = request.getParameter("id");
		
			if(st != null && !st.equals("")){
				ActivityThread at = ActivityThread.getInstance();
				at.running = false;
				
			}
		%>
		
		<form action="">
			停止:<input type="text" name="id" />
			<input type="submit" value="submit" />
		
		</form>
		
	</body>
</html>