<%@ page language="java" contentType="text/html; charset=utf-8"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<%
String path = request.getContextPath();
String basePath = request.getScheme()+"://"+request.getServerName()+":"+request.getServerPort()+path+"/";
%>


<%@page import="com.fy.engineserver.activity.quiz.QuizManager"%>
<%@page import="com.fy.engineserver.activity.quiz.Quiz"%>
<%@page import="java.util.Map"%>
<%@page import="java.util.List"%>
<%@page import="java.util.Map.Entry"%>
<%@page import="com.fy.engineserver.sprite.PlayerManager"%>
<%@page import="com.fy.engineserver.sprite.Player"%><html>
<head>
	<meta http-equiv="Content-Type" content="text/html; charset=utf-8">
	<title>查看答题人</title>
	<link rel="stylesheet" type="text/css" href="../css/common.css" />
</head>

<body>
	<%
		Quiz quiz =  QuizManager.getInstance().getQz();
		PlayerManager pm = PlayerManager.getInstance();
		if(quiz != null){
			Map<Byte, List<Long>> map =  quiz.getOneQuizRecord();
			for(Map.Entry<Byte,List<Long>> en : map.entrySet()){
				out.print(en.getKey());
				List<Long> list= en.getValue();
				for(long id :list){
					Player p =  pm.getPlayer(id);
					out.print(p.getName());
				}
				out.print("<br/>"+"*************************");
			}
		}
	
	%>
	
</body>
</html>
