<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">

<%@page
	import="com.fy.engineserver.datasource.article.manager.ArticleManager"%>
<%@page
	import="com.fy.engineserver.datasource.article.data.articles.Article"%>
<%@page
	import="com.fy.engineserver.datasource.article.manager.ArticleEntityManager"%>
<%@page import="com.fy.engineserver.sprite.PlayerManager"%>
<%@page
	import="com.fy.engineserver.datasource.article.data.entity.HunshiEntity"%>
<%@page import="com.fy.engineserver.hunshi.HunshiManager"%>
<%@page import="com.fy.engineserver.sprite.Player"%>
<%@page
	import="com.fy.engineserver.datasource.article.data.entity.ArticleEntity"%><html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title>模拟魂石鉴定</title>
</head>
<body>

<%
	String articleName = request.getParameter("articleName");
	String color = request.getParameter("color");
	String pName = request.getParameter("pName");
	String num = request.getParameter("num");
	if (articleName != null && !"".equals(articleName) && color != null && !"".equals(color) && pName != null && !"".equals(pName) && num != null && !"".equals(num)) {
		Player p = PlayerManager.getInstance().getPlayer(pName);
		if (p != null) {
			for (int i = 0; i < Integer.valueOf(num); i++) {
				Article a = ArticleManager.getInstance().getArticle(articleName);
				if (a != null) {
					try {
						ArticleEntity ae = ArticleEntityManager.getInstance().createTempEntity(a, true, 1, p, Integer.valueOf(color));
						if (ae != null && ae instanceof HunshiEntity) {
							HunshiEntity he = (HunshiEntity) ae;
							HunshiManager.getInstance().jianDingConfirm(p, he, null, true, 1);
							out.print(HunshiManager.getInstance().getPercent(he.getRongHeZhi()) + "<br>");
						}
					} catch (Exception e) {
						out.print("异常");
					}
				}
			}
		}

	}
%>
<form action="">角色名：<input type="text" name="pName" value="" /><br>
魂石名：<input type="text" name="articleName" value="" /><br>
颜色：<input type="text" name="color" value="" /><br>
个数：<input type="text" name="num" value="" /> <br>
<input type="submit" /></form>
已默认扣银子鉴定，注意补充银子
</body>
</html>