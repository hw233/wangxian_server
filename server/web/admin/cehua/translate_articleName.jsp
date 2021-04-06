<%@page import="com.fy.engineserver.datasource.article.data.articles.ComposeOnlyChangeColorArticle"%>
<%@page import="com.fy.engineserver.datasource.article.data.equipments.Equipment"%>
<%@page import="com.fy.engineserver.datasource.article.data.articles.InlayArticle"%>
<%@page import="com.fy.engineserver.datasource.article.data.articles.Article"%>
<%@page import="com.fy.engineserver.datasource.article.manager.ArticleManager"%>
<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<%
	String stat_names = request.getParameter("stat_names");
	String option = request.getParameter("option");
	StringBuffer articleRealNames = new StringBuffer();
	StringBuffer articleColoes = new StringBuffer();
	String [] articleStatNames = null;
	int notExist = 0;
	String notExistNames = "";
	if (stat_names == null || "null".equals(stat_names)) {
		stat_names = "";
	}
	if ("query".equals(option)) {
		articleStatNames = stat_names.split("\r\n");
		ArticleManager articleManager = ArticleManager.getInstance();
		for(int i = 0; i < articleStatNames.length; i ++){
			String stat = articleStatNames[i];
			Article article = articleManager.getArticleByCNname(stat);
			if (article != null) {
				articleRealNames.append(article.getName());
				if (article instanceof ComposeOnlyChangeColorArticle || article instanceof Equipment) {
					articleColoes.append("可变色");
				} else {
					articleColoes.append(article.getColorType());
				}
			} else {
				articleRealNames.append("#物品不存在#");
				articleColoes.append("#物品不存在#");
				notExist++;
				notExistNames += stat + "<HR>";
			}
			if (i != articleStatNames.length - 1) {
				articleRealNames.append("\r\n");
				articleColoes.append("\r\n");
			}
		}		
		
	}
%>
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<style type="text/css">
textarea {
	display: inline;
	background: #b7fff8;
	font-size: 12px;
	border-radius: 10px;
	/** border: solid 2px black;*/
	line-height: 1.5;
}
input {
	border-radius: 10px;
}
td {
	border-radius: 10px;
}
textarea[id^="r_"] {
	background-color: #56ffa7;
}
div[id=res] {
	color: red;
	font-size: 12px;
	font-weight: bolder;
	border-radius: 10px;
	display:inline;
	float: left;
	width: 30%;
}
body {
       -webkit-animation-name:fadein;
       -webkit-animation-duration:2s;
       -webkit-animation-timing-function:linear;
       -webkit-animation-iteration-count:1;
       font-weight: bold;
   }
@-webkit-keyframes fadein {
    0% {
        opacity: 0;
        background-color: black;
    }
    100% {
        opacity: 1;
        background-color: white;
    }
}
</style>
<title>物品名称翻译</title>
</head>
<body >
	<div align="center">
		<form action="./translate_articleName.jsp" method="post">
			<table style="font-size: 12px;text-align: center;display: inline;float: left;" align="center">
				<tr>
					<td style="background-color: #b7fff8">物品统计名</td>
					<td style="background-color: #56ffa7">物品翻译名</td>
					<td style="background-color: #56ffa7">物品颜色</td>
				</tr>
				<tr>
					<td><textarea id="cnName" rows="<%=articleStatNames == null ? "30" : articleStatNames.length %>" cols="30" name="stat_names"><%=stat_names %></textarea></td>
					<td><textarea id="r_realName" rows="<%=articleStatNames == null ? "30" : articleStatNames.length %>" cols="30" ><%=articleRealNames %></textarea></td>
					<td><textarea id="r_color" rows="<%=articleStatNames == null ? "30" : articleStatNames.length %>" cols="30"><%=articleColoes %></textarea></td>
				</tr>
				<tr>
					<td colspan="3">
						<input type="hidden" name="option" value="query"> 
						<input type="submit" value="查询" style="background-color: red;color: white;font-weight: bolder;">
					</td>
				</tr>
			</table>
			<%if ("query".equals(option)) { %>
				<div id="res">查询物品数量:<%=articleStatNames.length %>个.<br/><%=notExist==0 ? "":"其中不存在物品" + notExist + "个:<HR>" + notExistNames %></div>
			<%} %>
		</form>
	</div>
</body>
</html>