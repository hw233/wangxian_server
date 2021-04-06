<%@page import="com.fy.engineserver.datasource.article.data.props.PetEggProps"%>
<%@page import="java.util.*"%>
<%@page import="com.fy.engineserver.datasource.article.manager.ArticleManager"%>
<%@page import="com.fy.engineserver.datasource.article.data.articles.Article"%>
<%@page import="java.lang.reflect.Field"%>
<%@page import="com.fy.engineserver.datasource.article.data.props.LevelRandomPackage"%>
<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<%

	Field f = LevelRandomPackage.class.getDeclaredField("petPinzhiMap");
	f.setAccessible(true);
	LevelRandomPackage article = (LevelRandomPackage)ArticleManager.getInstance().getArticle("仙宠宝箱");
	Map<Integer, List<Article>> petPinzhiMap  = (Map<Integer, List<Article>>)f.get(article);
	out.print("petPinzhiMap.size = " + petPinzhiMap.size() + "<BR/>");
	for (Iterator<Integer> itor = petPinzhiMap.keySet().iterator();itor.hasNext();) {
		int l = itor.next();
		 List<Article> list = petPinzhiMap.get(l);
		 out.print(l + "," + list.size());
		for (Article a : list) {
			if (a instanceof PetEggProps) {
				PetEggProps aa = (PetEggProps) a;
				if (aa.getName().contains("FGT")) {
					out.print(aa.getName());
				}
			}
		}
		out.print("<HR>");
	}
%>
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title>Insert title here</title>
</head>
<body>

</body>
</html>