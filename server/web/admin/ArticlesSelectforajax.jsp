<%@ page language="java" contentType="text/html; charset=utf-8"
    pageEncoding="utf-8"%>
<%@ page import="com.google.gson.*,java.util.*,com.fy.engineserver.sprite.*,
com.fy.engineserver.sprite.concrete.*" %>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">

<%@page import="com.fy.engineserver.datasource.article.manager.*"%>
<%@page import="com.fy.engineserver.datasource.article.data.props.*"%>
<%@page import="com.fy.engineserver.datasource.article.data.articles.*"%>
<%@page import="com.fy.engineserver.datasource.article.data.entity.*"%><%@include file="IPManager.jsp" %><html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8">
<title>物品</title>
<%
long playerId = -1;
String errorMessage = null;
PlayerManager pm = PlayerManager.getInstance();
Player player = null;
ArticleManager am = ArticleManager.getInstance();
ArticleEntityManager aem = ArticleEntityManager.getInstance();
PropsCategory pcs[] = am.getAllPropsCategory();

	String cellIndex = request.getParameter("articleid");
	try{
		String playerIdStr = request.getParameter("playerid");
		playerIdStr = playerIdStr.substring(1,playerIdStr.length() - 1);
		playerId = Long.parseLong(playerIdStr);
		System.out.println("["+playerId+"]");
		if(cellIndex == null || cellIndex.trim().length() == 0){
			errorMessage = "要使用的物品不能为空";
		}
	}catch(Exception e){
		errorMessage = "玩家ID必须是数字，不能含有非数字的字符";
		System.out.println("["+playerId+"]");
	}
	if(errorMessage == null){
		player = pm.getPlayer(playerId);
		if(player == null){
			errorMessage = "ID为"+playerId+"的玩家不存在！";
		}
	}
	if(errorMessage == null){

		//Knapsack knapsack = player.getKnapsack();
		ArticleEntity ae = aem.getEntity(new Long(cellIndex).longValue());
		if(ae instanceof EquipmentEntity){
			String s = AritcleInfoHelper.generate(player,(EquipmentEntity)ae);
			s = s.replaceAll("\\[/color\\]","</font>");
			s = s.replaceAll("\\[color=(.*)\\]","<font color='$1'>");
			
			s = s.replaceAll("\\[icon\\](.*)\\[/icon\\]","<img src='/icons/$1'>");
			out.println("<pre>"+ae.getId()+"\n"+s+"</pre><br>");
		}else if(ae instanceof PropsEntity){
			String s =AritcleInfoHelper.generate(player,(PropsEntity)ae);
			s = s.replaceAll("\\[/color\\]","</font>");
			s = s.replaceAll("\\[color=(.*)\\]","<font color='$1'>");
			s = s.replaceAll("\\[icon\\](.*)\\[/icon\\]","<img src='/icons/$1'>");
			out.println("<pre>"+ae.getId()+"\n"+s+"</pre><br>");
		
		}else if(ae instanceof ArticleEntity){
			String s = AritcleInfoHelper.generate(player,(ArticleEntity)ae);
			s = s.replaceAll("\\[/color\\]","</font>");
			s = s.replaceAll("\\[color=(.*)\\]","<font color='$1'>");
			s = s.replaceAll("\\[icon\\](.*)\\[/icon\\]","<img src='/icons/$1'>");
			out.println("<pre>"+ae.getId()+"\n"+s+"</pre><br>");
		
		}

	}

%>
</head>
<body>
</body>
</html>
