<%@page
	import="com.fy.engineserver.datasource.article.data.equipments.Equipment"%>
<%@page import="com.fy.engineserver.datasource.language.Translate"%>
<%@page
	import="com.fy.engineserver.datasource.article.data.articles.Article"%>
<%@page
	import="com.fy.engineserver.datasource.article.manager.ArticleManager"%>
<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<%
	int[] low = new int[] { 1, 21, 41, 61, 81, 101, 121, 141, 161, 181 };
	int[] up = new int[] { 10, 30, 50, 70, 90, 110, 130, 150, 170, 190 };
	int[] classlevel = new int[] { 0, 1, 2, 3, 4, 5, 6, 7, 8, 9 };

	ArticleManager am = ArticleManager.getInstance();
	Article[] as = am.getAllArticles();
	int num = 0;
	for (Article a : as) {
		if (a.get物品一级分类().equals(Translate.角色装备类)) {
			Equipment e = (Equipment) a;
			if (e.getClassLimit() == 10) {
				int levelLimit = e.getPlayerLevelLimit();
				for (int i = 0; i < low.length; i++) {
					if (low[i] <= levelLimit && levelLimit <= up[i]) {
						e.setClassLimit(classlevel[i]);
						out.print("修改了:" + e.getName() + "<BR/>");
						num++;
					}
				}
			}
		}
	}
	out.print("<HR>");
	out.print("总数量:"+num + "<BR/>");
	out.print("<HR>");
%>
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title>Insert title here</title>
</head>
<body>

</body>
</html>