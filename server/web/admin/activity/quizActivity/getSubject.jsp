<%@ page language="java" contentType="text/html; charset=utf-8"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<%
String path = request.getContextPath();
String basePath = request.getScheme()+"://"+request.getServerName()+":"+request.getServerPort()+path+"/";
%>


<%@page import="com.fy.engineserver.activity.quiz.QuizManager"%>
<%@page import="com.fy.engineserver.activity.quiz.Quiz"%>
<%@page import="com.fy.engineserver.activity.quiz.QuizState"%>
<%@page import="com.fy.engineserver.activity.quiz.Subject"%>
<%@page import="com.fy.engineserver.sprite.Player"%>
<%@page import="com.fy.engineserver.sprite.PlayerManager"%><html>
<head>
	<meta http-equiv="Content-Type" content="text/html; charset=utf-8">
	<title>取得题</title>
	<link rel="stylesheet" type="text/css" href="../css/common.css" />
</head>

<body>
	
	<%
	String name = request.getParameter("playerName");
	if(name == null){
		%>
		<form action="" name="getSubject">
			用户名：<input type="text" name="playerName" />
			<input type="submit" value="提交"/>
		</form>
		<%
	}else{
		PlayerManager pm = PlayerManager.getInstance();
		Player player = pm.getPlayer(name);
		
		QuizManager qm = QuizManager.getInstance();
		Quiz qz = qm.getQz();
		if(qz != null){
			if(qz.getQs() == QuizState.运行 || qz.getQs() == QuizState.答题后){
				int num = qz.getNextNum();
				Subject subject = qz.getSubjectList().get(num);
				out.print("题干"+subject.getTrunk()+"<br/>");
				out.print("答案A"+subject.getBranchA()+"<br/>");
				out.print("答案B"+subject.getBranchB()+"<br/>");
				out.print("答案C"+subject.getBranchC()+"<br/>");
				out.print("答案D"+subject.getBranchD()+"<br/>");
				out.print("正确答案"+subject.getRightAnswer()+"<br/>");
				
				%>
				<a href="agree.jsp?playerId = <%=player.getId() %>&key=1">答题</a>
				<a href="agreeWithAmplifier.jsp?playerId =<%=player.getId() %>">放大镜</a>
				<a href="agreeHelp.jsp?playerId =<%=player.getId() %>">求助</a>
				
				<%
			}else{
				out.print("不能答题，状态是"+qz.getQs()+"<br/>");
			}
		}
		
	}
	
	%>
	
</body>
</html>
