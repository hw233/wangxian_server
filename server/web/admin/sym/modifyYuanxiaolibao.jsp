<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">

<%@page
	import="com.fy.engineserver.datasource.article.manager.ArticleManager"%>
<%@page import="java.lang.reflect.Field"%>
<%@page
	import="com.fy.engineserver.datasource.article.data.props.RandomPackageProps"%>
<%@page
	import="com.fy.engineserver.datasource.article.data.props.ArticleProperty"%><html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title>元宵礼包中50两工资卡从橙色改为紫色</title>
</head>
<body>
<%
	RandomPackageProps[] allRandomPackageProps = ArticleManager.getInstance().getAllRandomPackageProps();
	for (RandomPackageProps rpp : allRandomPackageProps) {
		if (rpp.getName().equals("元宵好礼")) {
			ArticleProperty[] apps = rpp.getApps();
			for (ArticleProperty ap : apps) {
				if (ap.getArticleName().equals("50两工资转换卡") && ap.getColor() == 4) {
					ap.setColor(3);
					out.print(ap.getArticleName() + "颜色修复：" + ap.getColor() + "<br>");
				}
			}
			ArticleProperty[] apps_stat = rpp.getApps_stat();
			for (ArticleProperty ap : apps_stat) {
				if (ap.getArticleName().equals("50两工资转换卡") && ap.getColor() == 4) {
					ap.setColor(3);
					out.print(ap.getArticleName() + "颜色修复：" + ap.getColor() + "<br>");
				}
			}
		}
	}
%>
</body>
</html>