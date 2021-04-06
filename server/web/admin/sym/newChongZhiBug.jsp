<%@page import="java.util.List"%>
<%@ page language="java" contentType="text/html; charset=UTF-8"
        pageEncoding="UTF-8"%>

<%@page import="com.fy.engineserver.gm.newfeedback.NewFeedbackSubSystem"%>
<%@page import="com.fy.engineserver.activity.newChongZhiActivity.NewChongZhiActivityManager"%><html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title>Insert title here</title>
</head>
<body>
<%
try{
NewChongZhiActivityManager.instance.LoadWeekChongZhiActivity();
out.print(NewChongZhiActivityManager.instance.weekCZs.size());
}catch(Exception e){
	out.print(e);
}
%>
</body>
</html>