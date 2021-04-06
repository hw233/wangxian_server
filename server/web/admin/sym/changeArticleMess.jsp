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
<title>修改物品描述</title>
<%
	if(PlatformManager.getInstance().isPlatformOf(Platform.官方)){
		Article a = ArticleManager.getInstance().getArticle("西红柿洋葱券");
		if(a == null){
			out.print("物品 西红柿洋葱券 不存在！");
			return;
		}
		
		out.print("《修改前》名字："+a.getName()+"--统计名："+a.getName_stat()+"<br>");
		a.setName("番茄洋葱券");
		a.setName_stat("番茄洋葱券");
		out.print("《修改后》名字："+a.getName()+"--统计名："+a.getName_stat()+"<br>");
		
		Class<?> c = ArticleManager.class;
		Field f_allArticleNameMap = c.getDeclaredField("allArticleNameMap");
		f_allArticleNameMap.setAccessible(true);
		Field f_allArticleCNNameMap = c.getDeclaredField("allArticleCNNameMap");
		f_allArticleCNNameMap.setAccessible(true);

		Map<String, Article> allArticleNameMap = (Map<String, Article>) f_allArticleNameMap.get(ArticleManager.getInstance());
		Map<String, Article> allArticleCNNameMap = (Map<String, Article>) f_allArticleCNNameMap.get(ArticleManager.getInstance());
		allArticleNameMap.put(a.getName(), a);
		allArticleCNNameMap.put(a.getName(), a);
		out.print("修改完毕...");
		
	}else{
		out.print("您无权操作");
	}

// 	out.print("<hr>");
// 	List<ShopActivity> shopActivities = ShopActivityManager.getInstance().getShopActivities();
// 	for(int i=0;i<shopActivities.size();i++){
// 		ShopActivity sa = shopActivities.get(i);
// 		if(sa instanceof ShopActivityOfBuyAndGive){
// 			ShopActivityOfBuyAndGive ss = (ShopActivityOfBuyAndGive)sa;
// 			ss.setEndTime(TimeTool.formatter.varChar19.parse("2013-12-31 23:59:59"));
// 			out.print(ss.getShopName()+"--"+TimeTool.formatter.varChar19.format(ss.getStartTime())+"--"+TimeTool.formatter.varChar19.format(ss.getEndTime())+"<br>");
// 		}else if(sa instanceof ArticleActivityOfUseAndGive){
// 			ArticleActivityOfUseAndGive as = (ArticleActivityOfUseAndGive)sa;
// 			as.setEndTime(TimeTool.formatter.varChar19.parse("2013-12-31 23:59:59"));
// 			out.print(as.getRepayType()+"--"+TimeTool.formatter.varChar19.format(as.getStartTime())+"--"+TimeTool.formatter.varChar19.format(as.getEndTime())+"<br>");
// 		}
// 	}
	
	
%>
