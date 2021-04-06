<%@page import="com.fy.engineserver.datasource.article.data.props.ArticleProperty"%>
<%@page import="com.fy.engineserver.datasource.article.data.props.RandomPackageProps"%>
<%@page import="com.fy.engineserver.datasource.article.manager.ArticleManager"%>
<%@page import="com.fy.engineserver.chat.ChatMessageService"%>
<%@page import="com.fy.engineserver.playerTitles.PlayerTitlesManager"%>
<%@page import="com.fy.engineserver.datasource.buff.BuffTemplate"%>
<%@page import="com.fy.engineserver.datasource.buff.Buff"%>
<%@page import="com.fy.engineserver.datasource.buff.BuffTemplateManager"%>
<%@ page language="java" contentType="text/html; charset=utf-8"
	pageEncoding="utf-8"%>
<%@page import="com.fy.engineserver.sprite.PlayerManager"%>
<%@page import="java.util.*"%>
<%@page import="com.fy.engineserver.sprite.Player"%>
<%@page import="com.xuanzhi.tools.text.StringUtil"%>
<%@page import="com.fy.engineserver.stat.StatData"%>
<%@page import="com.xuanzhi.tools.text.DateUtil"%>
<%@page import="org.apache.log4j.Logger"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
	<head>
		<meta http-equiv="Content-Type"
			content="text/html; charset=utf-8">
		<title>修改随机宝箱物品开启几率</title>
		<link rel="stylesheet" href="gm/style.css" />
		<script type="text/javascript">
	
</script>
	</head>
	<body>
		<br> 
		<%
			RandomPackageProps[] allRandomPackageProps = ArticleManager.getInstance().getAllRandomPackageProps();
			for(RandomPackageProps rp:allRandomPackageProps){
				if(rp.getName().equals("月團錦囊")){
					ArticleProperty[] apps = rp.getApps();
					for(ArticleProperty a : apps){
						if(a.getArticleName().equals("打折券禮包")){
							out.print("月團錦囊---"+a.getArticleName()+"修改之前的几率："+a.getProb()+"<br>");
							a.setProb(50);
							out.print("月團錦囊---"+a.getArticleName()+"修改之后的几率："+a.getProb()+"<br>");
						}else if(a.getArticleName().equals("月團餡")){
							out.print("月團錦囊---"+a.getArticleName()+"修改之前的几率："+a.getProb()+"<br>");
							a.setProb(400);
							out.print("月團錦囊---"+a.getArticleName()+"修改之后的几率："+a.getProb()+"<br>");
						}
					}
				}
			}
		
		%>

	</body>
</html>
