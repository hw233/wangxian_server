<%@page import="com.fy.engineserver.constants.GameConstant"%>
<%@page import="com.fy.engineserver.activity.ActivityIntroduce"%>
<%@page import="java.util.List"%>
<%@page import="com.fy.engineserver.activity.ActivityManager"%>
<%@page import="com.xuanzhi.boss.game.GameConstants"%>
<%@page
	import="com.fy.engineserver.activity.chongzhiActivity.ChongZhiServerConfig"%>
<%@page
	import="com.fy.engineserver.activity.chongzhiActivity.ChongZhiActivity"%>
<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<%
	out.print("<H1>" + GameConstants.getInstance().getServerName() + "</H1>");
	List<ActivityIntroduce> list = ActivityManager.getInstance().getActivityIntroduces();
	for (ActivityIntroduce ai : list) {
		if (ai.getId() == 4) {
			String s = ai.getDes().replace("0点","维护后");
			ai.setDes(s);
			break;
		}
	}
	out.print("<H2>修改完成</H2>");
%>
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title>Insert title here</title>
</head>
<body>

</body>
</html>