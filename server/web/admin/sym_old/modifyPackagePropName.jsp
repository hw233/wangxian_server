<%@page import="com.fy.engineserver.platform.PlatformManager.Platform"%>
<%@page import="com.fy.engineserver.platform.PlatformManager"%>
<%@page
	import="com.fy.engineserver.datasource.article.data.props.ArticleProperty"%>
<%@page
	import="com.fy.engineserver.datasource.article.data.props.PackageProps"%>
<%@page
	import="com.fy.engineserver.datasource.article.manager.ArticleManager"%>
<%@page
	import="com.fy.engineserver.datasource.article.data.articles.Article"%>
<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<%
	if(PlatformManager.getInstance().isPlatformOf(Platform.官方)==false){
		out.print("无权操作");
		return;
	}

	String names [] = {"第四日礼包","第五日礼包","第六日礼包","第七日礼包","第八日礼包","第九日礼包","第十日礼包","第十一日礼包","第十二日礼包","第十三日礼包"
			,"第十四日礼包","第十五日礼包","第十六日礼包","第十七日礼包","第十八日礼包","第十九日礼包","第二十日礼包"};
	
	for(String name:names){
		Article a = ArticleManager.getInstance().getArticle(name);
		if(a!=null){
			if(a instanceof PackageProps){
				PackageProps p = (PackageProps)a;
				p.setLevelLimit(70);
				out.print("[设置成功] [物品："+a.getName()+"] [等级限制："+p.getLevelLimit()+"]<br>");
			}else{
				out.print("不是宝箱");
			}
		}else{
			out.print("物品不存在");
		}
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