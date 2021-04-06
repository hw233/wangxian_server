<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<
<%@page import="java.util.Calendar"%>
<%@page import="com.fy.engineserver.activity.ActivityManager"%>
<%@page import="com.fy.engineserver.gametime.SystemTime"%>
<%@page import="com.fy.engineserver.activity.fireActivity.FireManager"%>
<%@page import="com.fy.engineserver.activity.ActivityThread"%>
<%@page import="com.fy.engineserver.newBillboard.monitorLog.LogRecordManager"%>
<%@page import="java.util.Date"%>
<%@page import="com.fy.engineserver.sprite.Player"%>
<%@page import="com.fy.engineserver.mail.service.concrete.DefaultMailManager.PlayerMailRequest"%>
<%@page import="com.fy.engineserver.sprite.PlayerManager"%>
<%@page import="com.fy.engineserver.activity.PlayerActivityRecord"%><html>
	<head>
		<title>查询活动的通知时间 </title>
	</head>
	
	<body>
	
		<%
			String st = request.getParameter("id");
		
			if(st != null && !st.equals("")){
				
				ActivityThread at = ActivityThread.getInstance();
				
				int[][] i1 = at.指定时间;
				boolean[] b2 = at.指定时间生效;
				for(boolean b:b2){
					out.print("是否失效"+b+"<br/>");
				}
				
				for(int[] int1 :i1){
					out.print("指定时间:"+int1[0]+"   分钟:"+int1[1]+"<br/>");
				}
				
				
			}
		%>
		
		<form action="">
			查看活动通知时间，状态:<input type="text" name="id" />
			<input type="submit" value="submit" />
		
		</form>
		
	</body>
</html>