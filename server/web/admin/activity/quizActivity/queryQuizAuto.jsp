<%@ page language="java" contentType="text/html; charset=utf-8"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<%
String path = request.getContextPath();
String basePath = request.getScheme()+"://"+request.getServerName()+":"+request.getServerPort()+path+"/";
%>


<%@page import="com.fy.engineserver.activity.quiz.QuizManager"%>
<%@page import="com.fy.engineserver.activity.quiz.*"%>
<%@page import="com.fy.engineserver.gametime.*"%>

<html>
<head>
	<meta http-equiv="Content-Type" content="text/html; charset=utf-8">
	<title>查询答题自动开始</title>
	<link rel="stylesheet" type="text/css" href="../css/common.css" />
</head>

<body>
	<%
		out.print("查看今天以后答题活动能不能自动开始,默认是能,可以设置不能<br/>");
		
		boolean open = QuizManager.getInstance().open;
		if(open){
			out.print("能自动开始<br/>");
			out.print("<a href=\"setQuizAuto.jsp?auto=1\">设置不能自动开始</a>");
		}else{
			out.print("不能自动开始<br/>");
			out.print("<a href=\"setQuizAuto.jsp?auto=0\">设置能自动开始</a>");
		}
	
	%>
	
</body>
</html>
