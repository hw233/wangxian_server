<%@ page language="java" contentType="text/html; charset=utf-8"
    pageEncoding="utf-8"%>
<%@ page import="com.google.gson.*,java.util.*,com.fy.engineserver.sprite.*,
com.fy.engineserver.sprite.concrete.*" %>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<%@include file="IPManager.jsp" %>
<%@page import="com.fy.engineserver.datasource.article.manager.ArticleManager"%>
<%@page import="com.fy.engineserver.datasource.article.data.props.PropsCategory"%>
<%@page import="com.fy.engineserver.datasource.article.data.articles.Article"%>
<%@page import="com.fy.engineserver.datasource.article.data.props.Knapsack"%>
<%@page import="com.fy.engineserver.datasource.article.data.entity.ArticleEntity"%>
<%@page import="com.fy.engineserver.datasource.article.data.entity.EquipmentEntity"%>
<%@page import="com.fy.engineserver.datasource.article.manager.AritcleInfoHelper"%>
<%@page import="com.fy.engineserver.datasource.article.data.entity.PropsEntity"%><html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8"> 
<title>物品</title>
<%
long playerId = -1;
String action = request.getParameter("action");
String errorMessage = null;
PlayerManager pm = PlayerManager.getInstance();
Player player = null;
ArticleManager am = ArticleManager.getInstance();
PropsCategory pcs[] = am.getAllPropsCategory();
HashMap<String, Article> hm = am.getArticles();
if(action != null && action.equals("seeArticle")){

	String cellIndex = request.getParameter("cellIndex1");
	try{
		playerId = Long.parseLong(request.getParameter("playerid"));
		if(cellIndex == null || cellIndex.trim().length() == 0){
			errorMessage = "要使用的物品不能为空";
		}
	}catch(Exception e){
		errorMessage = "玩家ID必须是数字，不能含有非数字的字符";
	}
	if(errorMessage == null){
		player = pm.getPlayer(playerId);
		if(player == null){
			errorMessage = "ID为"+playerId+"的玩家不存在！";
		}
	}
	if(errorMessage == null){
		if(hm == null){
			errorMessage = "没有物品";
		}
		Knapsack knapsack = player.getKnapsack();
		ArticleEntity ae = knapsack.getArticleEntityByCell(new Integer(cellIndex).intValue());
		out.println("id:"+ae.getId());
		if(ae instanceof EquipmentEntity){
			String s = AritcleInfoHelper.generate(player,(EquipmentEntity)ae);
			s = s.replaceAll("\\[/color\\]","</font>");
			s = s.replaceAll("\\[color=(.*)\\]","<font color='$1'>");
			
			s = s.replaceAll("\\[icon\\](.*)\\[/icon\\]","<img src='/icons/$1'>");
			out.println("<pre>"+s+"</pre><br>");
		}else if(ae instanceof PropsEntity){
			String s =AritcleInfoHelper.generate(player,(PropsEntity)ae);
			s = s.replaceAll("\\[/color\\]","</font>");
			s = s.replaceAll("\\[color=(.*)\\]","<font color='$1'>");
			s = s.replaceAll("\\[icon\\](.*)\\[/icon\\]","<img src='/icons/$1'>");
			out.println("<pre>"+s+"</pre><br>");
		
		}else if(ae instanceof ArticleEntity){
			String s = AritcleInfoHelper.generate(player,(ArticleEntity)ae);
			s = s.replaceAll("\\[/color\\]","</font>");
			s = s.replaceAll("\\[color=(.*)\\]","<font color='$1'>");
			s = s.replaceAll("\\[icon\\](.*)\\[/icon\\]","<img src='/icons/$1'>");
			out.println("<pre>"+s+"</pre><br>");
		
		}

	}
}

%>
</head>
<body>
</body>
</html>
