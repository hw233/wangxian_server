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
		<title>查询各种活动时间 </title>
	</head>
	
	<body>
	
		<%
			String st = request.getParameter("id");
		
			if(st != null && !st.equals("")){
				
				out.print("在线活动时间");
				
				//在线活动
				ActivityManager am = ActivityManager.getInstance();
				int[][] ints = am.指定日期;
				out.print("年:"+ints[0][0] + "月:"+ints[0][1] +"日:"+ints[0][2]+"<br/>");
				

				out.print("家族活动时间");
				int[][] int2 = FireManager.指定日期;
				out.print("年:"+int2[0][0] + "月:"+int2[0][1] +"日:"+int2[0][2]+"<br/>");
				
				
				ActivityThread at = ActivityThread.getInstance();
				out.print("打印时间:");
				out.print(at.dateString[0]+"<br/>");
				
				out.print("滚屏日期:");
				out.print(at.指定日期[0][0] +"  "+at.指定日期[0][1]+"  "+at.指定日期[0][2]+"<br/>");
				out.print("滚屏时间:");
				out.print(at.指定时间[0][0]+"  "+at.指定时间[0][1]+"<br/>");
				
				return;
			}
		%>
		
		<form action="">
			查看(在线活动，大奉送活动，家族经验活动，飞行坐骑活动):<input type="text" name="id" />
			<input type="submit" value="submit" />
		
		</form>
		
	</body>
</html>