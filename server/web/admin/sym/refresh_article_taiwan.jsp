<%@page import="java.util.Map"%>
<%@page import="java.util.Hashtable"%>
<%@page import="java.lang.reflect.Field"%>
<%@page
	import="com.fy.engineserver.datasource.article.data.articles.Article"%>
<%@page import="java.util.Arrays"%>
<%@page import="java.util.ArrayList"%>
<%@page import="java.util.List"%>
<%@page
	import="com.fy.engineserver.datasource.article.data.props.RandomPackageProps"%>
<%@page
	import="com.fy.engineserver.datasource.buff.BuffTemplate_addDamage"%>
<%@page
	import="com.fy.engineserver.datasource.buff.BuffTemplate_ZengShu"%>
<%@page
	import="com.fy.engineserver.datasource.buff.BuffTemplate_JiangDiZhiLiao"%>
<%@page
	import="com.fy.engineserver.datasource.buff.BuffTemplateManager"%>
<%@page
	import="com.fy.engineserver.datasource.buff.BuffTemplate_Silence"%>
<%@page
	import="com.fy.engineserver.datasource.article.data.props.PetEggProps"%>
<%@page
	import="com.fy.engineserver.datasource.article.manager.ArticleManager"%>
<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%
	PackageProps[] allPackageProps = ArticleManager.getInstance().getAllPackageProps();

	for (PackageProps pr : allPackageProps) {
		if (pr.getName_stat().equals("端午福利礼包")) {
			pr.setBindStyle((byte) 3);
			pr.setOpenBindType((byte) 0);
			out.print("刷新随机礼包中的:"+pr.getName()+"<br>");
		}
		if (pr.getName().equals("端午回饋錦囊")) {
			pr.setOpenBindType((byte) 0);
			out.print("刷新随机礼包中的:"+pr.getName()+"<br>");
		}
	}
	
	Article[] allArticles=ArticleManager.getInstance().getAllArticles();
	for(Article a:allArticles){
		if(a instanceof PackageProps){
			if (a.getName_stat().equals("端午福利礼包")) {
				a.setBindStyle((byte) 3);
				((PackageProps)a).setOpenBindType((byte) 0);
				out.print("刷新所有物品中的:"+a.getName()+"<br>");
			}
			if (a.getName().equals("端午回饋錦囊")) {
				((PackageProps)a).setOpenBindType((byte) 0);
				out.print("刷新所有物品中的:"+a.getName()+"<br>");
			}
		}
	}
	out.print("刷新完毕");
%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">

<%@page
	import="com.fy.engineserver.datasource.article.data.props.PackageProps"%><html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title>台湾添加新的物品类型</title>
</head>
<body>

</body>
</html>