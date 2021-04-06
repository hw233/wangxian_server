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
<title>修改物品描述</title>
<%
	if(PlatformManager.getInstance().isPlatformOf(Platform.台湾)){
		Article a = ArticleManager.getInstance().getArticle("雙蛋秘寶");
		if(a instanceof RandomPackageProps){
			RandomPackageProps p = (RandomPackageProps)a;
			out.print("《修改前》宝箱："+a.getName()+"--开启需要消耗的道具："+p.getCostName()+"<br>");
			p.setCostName("諸天寶箱");
			out.print("《修改后》宝箱："+a.getName()+"--开启需要消耗的道具："+p.getCostName()+"<br>");
		}
	}

	out.print("<hr>");
	List<ShopActivity> shopActivities = ShopActivityManager.getInstance().getShopActivities();
	for(int i=0;i<shopActivities.size();i++){
		ShopActivity sa = shopActivities.get(i);
		if(sa instanceof ShopActivityOfBuyAndGive){
			ShopActivityOfBuyAndGive ss = (ShopActivityOfBuyAndGive)sa;
			ss.setEndTime(TimeTool.formatter.varChar19.parse("2013-12-31 23:59:59"));
			out.print(ss.getShopName()+"--"+TimeTool.formatter.varChar19.format(ss.getStartTime())+"--"+TimeTool.formatter.varChar19.format(ss.getEndTime())+"<br>");
		}else if(sa instanceof ArticleActivityOfUseAndGive){
			ArticleActivityOfUseAndGive as = (ArticleActivityOfUseAndGive)sa;
			as.setEndTime(TimeTool.formatter.varChar19.parse("2013-12-31 23:59:59"));
			out.print(as.getRepayType()+"--"+TimeTool.formatter.varChar19.format(as.getStartTime())+"--"+TimeTool.formatter.varChar19.format(as.getEndTime())+"<br>");
		}
	}
	
	
%>