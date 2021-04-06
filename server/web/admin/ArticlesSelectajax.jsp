<%@ page language="java" contentType="text/html; charset=utf-8"
    pageEncoding="utf-8"%>
<%@ page import="com.fy.engineserver.datasource.props.*,com.google.gson.*,java.util.*,com.fy.engineserver.sprite.*,
com.fy.engineserver.sprite.concrete.*" %>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<%@include file="IPManager.jsp" %><html>
<head>
<title>物品一览</title>
<%

String errorMessage = null;
ArticleManager am = ArticleManager.getInstance();
ArticleEntityManager aem = ArticleEntityManager.getInstance();
PropsCategory pcs[] = am.getAllPropsCategory();
HashMap<String, Article> hm = am.getArticles();

	String articleName = request.getParameter("articleName");
	if(articleName == null || articleName.trim().length() == 0){ 
		errorMessage = "要使用的物品不能为空";
	}
	if(errorMessage == null){
		if(hm == null){
			errorMessage = "没有物品";
		}
		//Knapsack knapsack = player.getKnapsack();
		Article ae = hm.get(articleName);
		if(ae instanceof Equipment){
			String s = AritcleInfoHelper.generate((Equipment)ae);
			s = s.replaceAll("\\[/color\\]","</font>");
			s = s.replaceAll("\\[color=(.*)\\]","<font color='$1'>");
			
			s = s.replaceAll("\\[icon\\](.*)\\[/icon\\]","<img src='/icons/$1'>");
			out.println("<pre>"+s+"</pre>");
		}else if(ae instanceof Props){
			String s =AritcleInfoHelper.generate((Props)ae);
			s = s.replaceAll("\\[/color\\]","</font>");
			s = s.replaceAll("\\[color=(.*)\\]","<font color='$1'>");
			s = s.replaceAll("\\[icon\\](.*)\\[/icon\\]","<img src='/icons/$1'>");
			out.println("<pre>"+s+"</pre>");
		
		}else if(ae instanceof Article){
			String s = AritcleInfoHelper.generate((Article)ae);
			s = s.replaceAll("\\[/color\\]","</font>");
			s = s.replaceAll("\\[color=(.*)\\]","<font color='$1'>");
			s = s.replaceAll("\\[icon\\](.*)\\[/icon\\]","<img src='/icons/$1'>");
			out.println("<pre>"+s+"</pre>");
		
		}

	}

%>
<head></head>
<body></body>
</html>
