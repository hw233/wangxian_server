<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@page import="com.fy.engineserver.datasource.skill.Skill"%>
<%@page import="com.fy.engineserver.sprite.Player"%>
<%@page import="com.fy.engineserver.sprite.PlayerManager"%>
<%@page import="com.fy.engineserver.jsp.propertyvalue.SkillManager"%>
<%@page import="com.fy.engineserver.datasource.career.SkillInfo"%>
<%@page import="com.fy.engineserver.sprite.monster.MonsterManager"%>
<%@page import="com.fy.engineserver.activity.monsterDrop.MonsterDropActivityManager"%>
<%@page import="java.util.ArrayList"%>
<%@page import="com.fy.engineserver.activity.monsterDrop.MonsterDropInfo"%>
<%@page import="com.fy.engineserver.datasource.article.manager.ArticleManager"%>
<%@page import="com.fy.engineserver.datasource.article.data.articles.Article"%>
<%@page import="java.util.List"%>
<%@page import="com.fy.engineserver.sprite.monster.Monster"%>
<%@page import="com.fy.engineserver.sprite.monster.MemoryMonsterManager"%>
<%@page import="java.util.Map"%>
<%@page import="java.util.LinkedHashMap"%>
<%@page import="com.fy.engineserver.sprite.monster.FlopSet"%>
<%@page import="java.util.Collection"%>
<%@page import="java.util.Iterator"%>
<%@page import="java.util.Arrays"%>
<%@page import="java.util.Set"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<%
	MemoryMonsterManager mm = (MemoryMonsterManager)MemoryMonsterManager.getMonsterManager();
	Map<Long, Monster> monsterMap = mm.monsterMap;
	ArrayList<String> errorMonster = new ArrayList<String>();
	Map<String,String> errorlevel = new HashMap<String,String>();
	Map<String,String> errorDropArticles = new HashMap<String,String>();
	
	/*LinkedHashMap<int[], FlopSet[]> level2FlopListMap = mm.getLevel2FlopListMap();
	Collection<FlopSet[]> c1 = level2FlopListMap.values();
	Iterator<FlopSet[]> itor1 = c1.iterator();
	while(itor1.hasNext()){
		FlopSet[] fs = itor1.next();
		for(int i=0;i<fs.length;i++){
			String articles[] = fs[i].articles;
			for(int j=0;j<articles.length;j++){
				Article article = ArticleManager.getInstance().getArticle(articles[j]);
				if (article == null) {
					errorDropArticles.add(articles[j]);
				}
			}
		}
	}*/
	
	LinkedHashMap<int[], FlopSet[]> level2FlopListMap = mm.getLevel2FlopListMap();
	Set<int[]> levelSet = level2FlopListMap.keySet();
	Iterator<int[]> itor1 = levelSet.iterator();
	while(itor1.hasNext()){
		int[] level = itor1.next();
		for(int i=0;i<level2FlopListMap.get(level).length;i++){
			String articles[] = level2FlopListMap.get(level)[i].articles;
			for(int j=0;j<articles.length;j++){
				Article article = ArticleManager.getInstance().getArticle(articles[j]);
				if (article == null) {
					errorlevel.put(Arrays.toString(level),articles[j]);
				}
			}
		}
	}
	
	/*Collection<FlopSet[]> c2 = level2FlopListMap.values();
	Iterator<FlopSet[]> itor2 = c2.iterator();
	while(itor2.hasNext()){
		FlopSet[] fs = itor2.next();
		for(int i=0;i<fs.length;i++){
			String articles[] = fs[i].articles;
			for(int j=0;j<articles.length;j++){
				Article article = ArticleManager.getInstance().getArticle(articles[j]);
				if (article == null) {
					errorDropArticles.add(articles[j]);
				}
			}
		}
	}*/
	LinkedHashMap<String, FlopSet[]> map2FlopListMap = mm.getMap2FlopListMap();
	Set<String> monsterNames = map2FlopListMap.keySet();
	for(String name:monsterNames){
		Collection<Monster> c3 = monsterMap.values();
		Iterator<Monster> itor3 = c3.iterator();
		boolean find = false;
		while(itor3.hasNext()){
			Monster mon = itor3.next();
			if(name.equals(mon.getName())){
				find = true;
				break;
			}
		}
		if(!find){
			errorDropArticles.put(name,"没有该怪物");
		}else{
			FlopSet[] fs = map2FlopListMap.get(name);
			for(int i=0;i<fs.length;i++){
				String articles[] = fs[i].articles;
				for(int j=0;j<articles.length;j++){
					Article article = ArticleManager.getInstance().getArticle(articles[j]);
					if (article == null) {
						errorDropArticles.put(name,"没有该物品:"+articles[j]);
					}
				}
			}
		}
		
	}
%>
 




<%@page import="java.util.HashMap"%><html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title>怪物检查</title>
<link rel="stylesheet" type="text/css" href="../css/table.css" />
<style type="text/css">

</style>
</head>
<body><div>
	<div style="float:left">
	<table style="font-size: 12px;" border="1">
	<tr style="background-color: #83AAEB;font-weight: bold;"><td>等级</td><td>不存在的物品</td></tr>

	<%
	Set<String> ss = errorlevel.keySet();
	Iterator<String> it = ss.iterator();
	while(it.hasNext()){
		String level = it.next();
		out.print("<tr><td>"+level+"</td><td>"+errorlevel.get(level)+"</td></tr>");
	}
	%>
	</table>
	</div>
	<div style="float:left;margin-left:20px;">
	<table style="font-size: 12px;" border="1">
	<tr style="background-color: #83AAEB;font-weight: bold;"><td>怪物</td><td>问题</td></tr>

	<%
	Set<String> ss2 = errorDropArticles.keySet();
	Iterator<String> it2 = ss.iterator();
	while(it.hasNext()){
		String name = it.next();
		out.print("<tr><td>"+name+"</td><td>"+errorDropArticles.get(name)+"</td></tr>");
	}
	%>
	</table>
	</div>
</div></body>
</html>