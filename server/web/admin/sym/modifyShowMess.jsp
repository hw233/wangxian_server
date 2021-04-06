<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">

<%@page
	import="com.fy.engineserver.activity.loginActivity.ActivityManagers"%>
<%@page import="java.util.List"%>
<%@page import="com.fy.engineserver.activity.ActivityShowInfo"%>
<%@page import="com.fy.engineserver.util.TimeTool"%>
<%@page import="java.text.SimpleDateFormat"%>
<%@page import="java.util.Date"%><html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title>Insert title here</title>
</head>
<body>

<%
	SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
	List<ActivityShowInfo> infos = ActivityManagers.getInstance().infos;
	for (ActivityShowInfo info : infos) {
		if (info.activityId >= 54 && info.activityId <= 60) {
			out.print("活动id:" + info.activityId + "  修改前结束时间：" + sdf.format(new Date(info.endShowTime)) + "<br>");
			info.endShowTime = TimeTool.formatter.varChar19.parse("2016-02-28 23:59:59");
			out.print("活动id:" + info.activityId + "  修改后结束时间：" + sdf.format(new Date(info.endShowTime)) + "<hr>");
		}
	}
%>

</body>
</html>