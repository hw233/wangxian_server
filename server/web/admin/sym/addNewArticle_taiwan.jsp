<%@page import="java.util.Map"%>
<%@page import="java.util.Hashtable"%>
<%@page import="java.lang.reflect.Field"%>
<%@page import="com.fy.engineserver.datasource.article.data.articles.Article"%>
<%@page import="java.util.Arrays"%>
<%@page import="java.util.ArrayList"%>
<%@page import="java.util.List"%>
<%@page import="com.fy.engineserver.datasource.article.data.props.RandomPackageProps"%>
<%@page import="com.fy.engineserver.datasource.buff.BuffTemplate_addDamage"%>
<%@page import="com.fy.engineserver.datasource.buff.BuffTemplate_ZengShu"%>
<%@page import="com.fy.engineserver.datasource.buff.BuffTemplate_JiangDiZhiLiao"%>
<%@page import="com.fy.engineserver.datasource.buff.BuffTemplateManager"%>
<%@page import="com.fy.engineserver.datasource.buff.BuffTemplate_Silence"%>
<%@page import="com.fy.engineserver.datasource.article.data.props.PetEggProps"%>
<%@page import="com.fy.engineserver.datasource.article.manager.ArticleManager"%>
<%@ page language="java" contentType="text/html; charset=UTF-8"
        pageEncoding="UTF-8"%>
<%
	RandomPackageProps prop = null;
	RandomPackageProps[] allRandomPackageProps = ArticleManager.getInstance().getAllRandomPackageProps();
	
	for(RandomPackageProps pr :allRandomPackageProps ){
		if(pr.getName_stat().equals("活跃密钥")){
			prop = new RandomPackageProps();
			prop.setName("活躍金鑰");
			prop.setName_stat("活跃金钥");
			prop.setIconId(pr.getIconId());
			prop.setKnapsackType(pr.getKnapsackType());
			prop.setBindStyle(pr.getBindStyle());
			prop.setColorType(pr.getColorType());
			prop.setOverlap(pr.isOverlap());
			prop.setOverLapNum(pr.getOverLapNum());
			prop.set物品一级分类(pr.get物品一级分类());
			prop.set物品二级分类(pr.get物品二级分类());
			prop.setDescription(pr.getDescription());
			prop.setUseMethod(pr.getUseMethod());
			prop.setGetMethod(pr.getGetMethod());
			prop.setHaveValidDays(pr.isHaveValidDays());
			prop.setActivationOption(pr.getActivationOption());
			prop.setMaxValidDays(pr.getMaxValidDays());
			prop.setSailFlag(pr.isSailFlag());
			prop.setPrice(pr.getPrice());
			prop.setFlopNPCAvata(pr.getFlopNPCAvata());
			prop.setCategoryName(pr.getCategoryName());
			prop.setUsingTimesLimit(pr.isUsingTimesLimit());
			prop.setMaxUsingTimes(pr.getMaxUsingTimes());
			prop.setUsedUndisappear(pr.isUsedUndisappear());
			prop.setLevelLimit(pr.getLevelLimit());
			prop.setClassLimit(pr.getClassLimit());
			prop.setFightStateLimit(pr.isFightStateLimit());
			prop.setApps(pr.getApps());
			prop.setApps_stat(pr.getApps_stat());
			prop.setOpenBindType(pr.getOpenBindType());
			prop.setCostNum(pr.getCostNum());
			prop.setCostColor(pr.getCostColor());
			prop.setCostName(pr.getCostName());
			prop.setCostName_stat(pr.getCostName_stat());
			prop.setSendMessageArticles(pr.getSendMessageArticles());
			break;
		}
	}
	if(prop!=null){
		List<RandomPackageProps> list = new ArrayList<RandomPackageProps>();
		List<Article> articles = new ArrayList<Article>();
		Article[] allArticles = ArticleManager.getInstance().getAllArticles();
		list.addAll(Arrays.asList(allRandomPackageProps));
		articles.addAll(Arrays.asList(allArticles));
		list.add(prop);
		articles.add(prop);
		ArticleManager.getInstance().setAllArticles(articles.toArray(new Article[]{}));
		Class c = ArticleManager.class;
		ArticleManager a = ArticleManager.getInstance();
		Field f = c.getDeclaredField("allRandomPackageProps");
		Field f2 = c.getDeclaredField("allArticleCNNameMap");
		Field f3 = c.getDeclaredField("allArticleNameMap");
		f.setAccessible(true);
		f2.setAccessible(true);
		f3.setAccessible(true);
		Map<String, Article> allArticleNameMap = new Hashtable<String, Article>();
		Map<String, Article> allArticleCNNameMap = new Hashtable<String, Article>();
		for(int i=0;i<articles.size();i++){
			allArticleCNNameMap.put(articles.get(i).getName_stat(), articles.get(i));
			allArticleNameMap.put(articles.get(i).getName(), articles.get(i));
		}
		f2.set(a, allArticleCNNameMap);
		f.set(a, list.toArray(new RandomPackageProps[]{}));
		f3.set(a, allArticleNameMap);
		
		out.print("[添加成功] [添加之前数量："+allRandomPackageProps.length+"] [添加后数量:"+list.size()+"]");
	}
	
%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title>台湾添加新的物品类型</title>
</head>
<body>

</body>
</html>