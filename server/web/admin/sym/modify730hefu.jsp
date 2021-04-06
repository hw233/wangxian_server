<%@page import="com.fy.engineserver.util.TimeTool"%>
<%@page import="java.sql.Timestamp"%>
<%@page import="com.fy.engineserver.uniteserver.UnitServerActivity"%>
<%@page import="com.fy.engineserver.uniteserver.UnitedServerManager"%>
<%@ page language="java" contentType="text/html; charset=utf-8"
	pageEncoding="utf-8"%>
</html>
<head>
<title>test</title>
</head>
<body>

<%
	long startTime = TimeTool.formatter.varChar19.parse("2015-07-30 14:45:00");
	long endTime = TimeTool.formatter.varChar19.parse("2015-08-03 00:00:00");
	long endTime2 = TimeTool.formatter.varChar19.parse("2015-08-03 23:59:59");
	UnitedServerManager inst = UnitedServerManager.getInstance();	
	for (UnitServerActivity ac : inst.getUnitServerActivities()) {
		if (ac.getActivityName().equals("20150730合服前领取合服锦囊") && ac.getStartTime() == startTime && ac.getEndTime() == endTime) {
			ac.setEndTime(endTime2);
 			out.println(ac.getActivityName() + " = " + new Timestamp(ac.getStartTime()) + " = " + new Timestamp(ac.getEndTime()) + "<br>");
		}
		if (ac.getActivityName().equals("勇夺飘渺王城报名") && ac.getStartTime() == startTime && ac.getEndTime() == endTime) {
			ac.setEndTime(endTime2);
 			out.println(ac.getActivityName() + " = " + new Timestamp(ac.getStartTime()) + " = " + new Timestamp(ac.getEndTime()) + "<br>");
		}
	}
	out.println("ok!!!");

%>
</table>
</body>
</html>
