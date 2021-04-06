<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Frameset//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-frameset.dtd">
<%@page import="com.fy.engineserver.datasource.article.data.props.PackageProps"%>
<%@page import="com.fy.engineserver.datasource.article.data.props.RandomPackageProps"%>
<%@page import="com.fy.engineserver.datasource.article.data.articles.Article"%>
<%@page import="com.fy.engineserver.datasource.article.manager.ArticleManager"%>
<%@ page language="java" contentType="text/html; charset=utf-8" pageEncoding="utf-8"%>
<%@page import="java.util.*"%>
<title>比较物品不存在</title>
</head>
<body>
<form method="post" action="?isstart=true"> 
	<%
		//宝箱类道具,,随机宝箱类道具
		ArticleManager am = ArticleManager.getInstance();
		RandomPackageProps[] allRandomPackageProps = am.getAllRandomPackageProps();
		int errorCount = 0;
		int rightCount = 0;
		for(RandomPackageProps props:allRandomPackageProps){
			Article a = am.getArticle(props.getName());
			if(a==null){
				out.print("<font color='red'>《随机宝箱类道具物品不存在》</font>--物品："+props.getName()+"<br>");
				errorCount++;
			}else{
				rightCount++;
			}
		}	
		out.print("=====随机宝箱类道具,检查完毕。总数："+(rightCount+errorCount)+"--错误的数量："+errorCount+"--正确的数量："+rightCount+"=====<br>");
		
		PackageProps[] allPackageProps = am.getAllPackageProps();
		errorCount = 0;
		rightCount = 0;
		for(PackageProps props:allPackageProps){
			Article a = am.getArticle(props.getName());
			if(a==null){
				out.print("<font color='red'>《宝箱类道具物品不存在》</font>--物品："+props.getName()+"<br>");
				errorCount++;
			}else{
				rightCount++;
			}
		}	
		out.print("=====宝箱类道具,检查完毕。总数："+(rightCount+errorCount)+"--错误的数量："+errorCount+"--正确的数量："+rightCount+"=====");
		
		
	
	%>
</form>
</body>
</html>
