<%@ page language="java" contentType="text/html; charset=utf-8"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<%
String path = request.getContextPath();
String basePath = request.getScheme()+"://"+request.getServerName()+":"+request.getServerPort()+path+"/";
%>

<%@page import="com.fy.engineserver.sprite.PlayerManager"%>
<%@page import="com.fy.engineserver.sprite.Player"%>
<%@page import="com.fy.engineserver.activity.quiz.QuizManager"%>


<html>
<head>
	<meta http-equiv="Content-Type" content="text/html; charset=utf-8">
	<title>答题用帮助</title>
	<link rel="stylesheet" type="text/css" href="../css/common.css" />
</head>

<body>
	
	<%
	
	String ID = request.getParameter("playerId");
	if(ID == null){
		out.print("没有id");
	}else{
		PlayerManager pm = PlayerManager.getInstance();
		Player player = pm.getPlayer(Long.parseLong(ID));
		
		QuizManager qm = QuizManager.getInstance();
		int num = qm.getQz().getNextNum();
		if(qm.helpAnswer(num,player)){
			out.print("回答完毕");
		}else{
			out.print("回答出错");
		}
	}
	%>
	
	
</body>
</html>
