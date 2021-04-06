<%@page
	import="com.fy.engineserver.activity.feedsilkworm.FeedSilkwormManager"%>
<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%
	FeedSilkwormManager fm = FeedSilkwormManager.getInstance();
	fm.setDoTaskTime(1000 * 900);
	fm.setWormLife(1000 * 300);
	fm.setTreeLife(1000 * 300);
%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title>Insert title here</title>
</head>
<body>

</body>
</html>