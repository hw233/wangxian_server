<%@page import="com.fy.engineserver.datasource.article.data.props.ArticleProperty"%>
<%@page import="java.lang.reflect.Field"%>
<%@page import="com.fy.engineserver.activity.shop.ArticleActivityOfUseAndGive"%>
<%@page import="com.fy.engineserver.util.TimeTool"%>
<%@page import="com.fy.engineserver.activity.shop.ShopActivityOfBuyAndGive"%>
<%@page import="com.fy.engineserver.activity.shop.ShopActivityManager"%>
<%@page import="com.fy.engineserver.activity.shop.ShopActivity"%>
<%@page import="com.fy.engineserver.datasource.article.data.props.RandomPackageProps"%>
<%@page import="com.fy.engineserver.platform.PlatformManager.Platform"%>
<%@page import="com.fy.engineserver.platform.PlatformManager"%>
<%@ page language="java" contentType="text/html; charset=utf-8"
    pageEncoding="utf-8"%>
<%@ page import="com.google.gson.*,java.util.*" %>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<%@page import="com.fy.engineserver.datasource.article.manager.ArticleManager"%>
<%@page import="com.fy.engineserver.datasource.article.data.articles.Article"%>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8">
<title>宝石bug </title>
<%
	if(PlatformManager.getInstance().isPlatformOf(Platform.官方,Platform.腾讯)){
		Article a = ArticleManager.getInstance().getArticle("6级宝石随机袋");
		if(a == null){
			out.print("物品 6级宝石随机袋 不存在！");
			return;
		}
		
		if(a instanceof RandomPackageProps == false){
			out.print("道具类型不符");
			return;
		}
		
		RandomPackageProps rp = (RandomPackageProps)a;
		
		ArticleProperty[] apps = rp.getApps();
		ArticleProperty[] apps_stat = rp.getApps_stat();
		for(ArticleProperty ap : apps){
			if(ap != null && ap.getArticleName().equals("宝石混沌(8级)")){
				ap.setArticleName("宝石混沌(6级)");
				ap.setArticleName_stat("宝石混沌(6级)");
				out.print("[修改成功] [宝箱："+a.getName()+"] [修改后宝石:"+ap.getArticleName()+"]");
			}
		}
		
		for(ArticleProperty ap : apps_stat){
			if(ap != null && ap.getArticleName().equals("宝石混沌(8级)")){
				ap.setArticleName("宝石混沌(6级)");
				ap.setArticleName_stat("宝石混沌(6级)");
				out.print("[修改成功stat] [宝箱："+a.getName()+"] [修改后宝石:"+ap.getArticleName()+"]");
			}
		}
	}else{
		out.print("您无权操作");
	}

	
	
%>
