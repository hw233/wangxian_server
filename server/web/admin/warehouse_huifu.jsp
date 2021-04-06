<%@ page language="java" contentType="text/html; charset=utf-8"%>
<%@ page import="com.xuanzhi.tools.transaction.*,com.fy.engineserver.warehouse.service.*,com.fy.engineserver.warehouse.*,com.fy.engineserver.datasource.props.*,com.google.gson.*,java.util.*,com.fy.engineserver.sprite.*,com.xuanzhi.gameresource.*,
com.fy.engineserver.sprite.concrete.*,com.fy.engineserver.datasource.career.*,com.fy.engineserver.mail.*,com.fy.engineserver.mail.service.*,java.sql.Connection,java.sql.*,java.io.*,com.xuanzhi.boss.game.*" %>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">

<%@page import="com.fy.engineserver.datasource.props.Knapsack.Cell"%>
<%@include file="IPManager.jsp" %><html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8">
<title>人物所持物品</title>
<link rel="stylesheet" type="text/css" href="../css/common.css" />
</head>
<%
String articles = request.getParameter("articles");
String message = null;
if(articles != null) {
	PlayerManager pm = PlayerManager.getInstance();
	WareHouseManager wm = WareHouseManager.getInstance();
	ArticleManager am = ArticleManager.getInstance();
	ArticleEntityManager aem = ArticleEntityManager.getInstance();
	int playerid = Integer.parseInt(request.getParameter("playerid"));
	Player player = pm.getPlayer(playerid);
	if(player != null) {
		WareHouse w = wm.getPlayerWareHouse(player.getId());
		if(w != null) {
			String args[] = articles.split("\n");
			StringBuffer sb = new StringBuffer();
			for(String art : args) {
				String article = art.trim();
				int count = 1;
				if(article.indexOf("(") != -1 && article.endsWith(")")) {
					String num = article.substring(article.indexOf("(") + 1, article.length()-1);
					if(num.matches("\\d+?")) {
						count = Integer.parseInt(num);
						article = article.substring(0, article.indexOf("("));
					}
				}
				Article oart = am.getArticle(article);
				if(oart != null) {
					ArticleEntity entity = null;
					if(oart instanceof Equipment) {
						entity = aem.createEntity((Equipment)oart, (oart.getBindStyle() == Article.BINDTYPE_PICKUP), ArticleEntityManager.CREATE_REASON_OTHER, player);
					} else if(oart instanceof Props) {
						entity = aem.createEntity((Props)oart, (oart.getBindStyle() == Article.BINDTYPE_PICKUP), ArticleEntityManager.CREATE_REASON_OTHER, player);
					} else if(oart instanceof Article) {
						entity = aem.createEntity(oart, (oart.getBindStyle() == Article.BINDTYPE_PICKUP), ArticleEntityManager.CREATE_REASON_OTHER, player);
					}
					if(entity != null) {
						for(int i=0; i<count; i++) {
							w.put(entity);
						}
						sb.append(entity.getArticleName() + "/" + count + ",");
					}
				}
			}
			message = sb.toString();
			if(message.length() > 0) {
				message = "给玩家仓库补了以下物品：" + message.substring(0, message.length()-1);
			}
		}
	}
}

%>
<body>
<%
if(message != null) {
	out.println("<h2>"+message+"</h2><br><br>");
}
%>
<form action="warehouse_huifu.jsp" method="post">
物品：<textarea name=articles cols=50 rows=10></textarea><br>每行一个物品: 物品名称 or 物品名称(数量)
玩家id:<input type="text" size=20 name="playerid"/>
<input type=submit name=sub1 value="提交">
</form>
</body>
</script>
</html>
