<%@ page language="java" contentType="text/html; charset=utf-8"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<%
String path = request.getContextPath();
String basePath = request.getScheme()+"://"+request.getServerName()+":"+request.getServerPort()+path+"/";

String close = request.getParameter("close");
System.out.println("close:"+ close);
%>


<%@page import="com.fy.engineserver.activity.quiz.QuizManager"%>
<%@page import="com.fy.engineserver.activity.quiz.*"%>
<%@page import="com.fy.engineserver.gametime.*"%>

<html>
<head>
	<meta http-equiv="Content-Type" content="text/html; charset=utf-8">
	<title>设置答题开始</title>
	<link rel="stylesheet" type="text/css" href="../css/common.css" />
</head>

<body>
	<%
	if ("close".equals(close)) {
		QuizManager.getInstance().setQz(null);
	}
	QuizManager.beginHour1 = 14;
	QuizManager.beginMinute1 =  5;
	
	if(true){
		return;
	}
		QuizManager qm = QuizManager.getInstance();
		Quiz qz  = qm.load();
		qz.setQs(QuizState.开始);
		qz.setRunningTime(SystemTime.currentTimeMillis()+QuizManager.多长时间后开始);
		out.print(qz.getRunningTime() - SystemTime.currentTimeMillis()+"<br/>");
		qz.setEndTime(SystemTime.currentTimeMillis() +QuizManager.多长时间后开始 + (QuizManager.ANSWER_INTERVAL+QuizManager.WAIT_CHECK_INTERVAL)*20 );
		qm.setQz(qz);
		out.print("开始");
	%>
	
</body>
</html>
