<%@page import="com.fy.engineserver.datasource.article.manager.ArticleManager"%>
<%@page import="com.fy.engineserver.datasource.article.data.articles.Article"%>
<%@page import="com.fy.engineserver.datasource.article.manager.ArticleEntityManager"%>
<%@page import="com.fy.engineserver.activity.wafen.manager.WaFenManager"%>
<%@ page language="java" contentType="text/html; charset=UTF-8"
        pageEncoding="UTF-8"%>
<%
	//if (WaFenManager.instance.fenmuMap.get("昆天圣域").getArticleMap().get(3036).getArticleCNNName().equals("紫色魂石礼包")) {
		WaFenManager.instance.fenmuMap.get("昆天圣域").getArticleMap().get(3036).setArticleCNNName("紫色坐骑魂石锦囊");
		Article a = ArticleManager.getInstance().getArticle("紫色坐骑魂石锦囊");
		long aa = ArticleEntityManager.getInstance().createTempEntity(a, false, 1, null, a.getColorType()).getId();
		WaFenManager.instance.fenmuMap.get("昆天圣域").getArticleMap().get(3036).setTempArticleEntityId(aa);
		out.println("ok!!");
	//}
%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title>修正白格</title>
</head>
<body>

</body>
</html>