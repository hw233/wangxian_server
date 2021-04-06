<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Frameset//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-frameset.dtd">
<%@page import="com.fy.engineserver.sprite.monster.FlopSet"%>
<%@page import="com.fy.engineserver.sprite.monster.MemoryMonsterManager"%>
<%@page import="com.fy.engineserver.datasource.language.Translate"%>
<%@page import="com.fy.engineserver.datasource.article.data.equipments.Equipment"%>
<%@page import="com.fy.engineserver.datasource.article.data.props.Props"%>
<%@page import="com.fy.engineserver.newtask.prizes.TaskPrizeOfArticle"%>
<%@page import="com.fy.engineserver.newtask.Task"%>
<%@page import="com.fy.engineserver.newtask.service.TaskManager"%>
<%@page import="com.fy.engineserver.newtask.prizes.TaskPrize"%>
<%@page import="com.fy.engineserver.datasource.article.data.props.PackageProps"%>
<%@page import="com.fy.engineserver.datasource.article.data.props.RandomPackageProps"%>
<%@page import="com.fy.engineserver.datasource.article.data.articles.Article"%>
<%@page import="com.fy.engineserver.datasource.article.manager.ArticleManager"%>
<%@ page language="java" contentType="text/html; charset=utf-8" pageEncoding="utf-8"%>
<%@page import="java.util.*"%>
<title>比较怪物等级掉落不存在</title>
</head>
<body>
<form method="post" action="?isstart=true"> 
	<%
		MemoryMonsterManager mm = (MemoryMonsterManager)MemoryMonsterManager.getMonsterManager();	
		LinkedHashMap<int[], FlopSet[]> map = mm.getLevel2FlopListMap();
		Iterator<int[]> it = map.keySet().iterator();
		int count = 0;
		int articleLeveluseLimit = 0;
		int propscount = 0;
		int equcount = 0;
		int 最低级别 = 0;
		while(it.hasNext()){
			int[] levels = (int[])it.next();
			FlopSet[] fss = null;
			try{
				fss = (FlopSet[])map.get(levels);
			}catch(Exception e){
				e.printStackTrace();
				out.print("异常："+levels+"=============================="+e);
			}
			
			最低级别 = levels[0];
			if(fss!=null && fss.length>0){
				for(FlopSet fs : fss){
					String names [] = fs.getArticles();
					if(names!=null && names.length>0){
						for(String name:names){
							Article a = ArticleManager.getInstance().getArticle(name);
							if(a!=null){
								count++;
								if(a instanceof Props){
									propscount++;
									Props p = (Props)a;
									//玩家等级限制
									articleLeveluseLimit = p.getLevelLimit();
									if(articleLeveluseLimit/20==最低级别/20){
// 										out.print("<font color='green'>《么错误道具》</font>--掉落物品："+name+"--物品最低级别："+最低级别+"--玩家等级限制："+articleLeveluseLimit+"<br>");
									}else{
										out.print("<font color='red'>《有错误道具》</font>--掉落物品："+name+"--物品最低级别："+最低级别+"--(道具)玩家等级限制："+articleLeveluseLimit+"<br>");
									}
								}else if(a instanceof Equipment){
									equcount++;
									Equipment e = (Equipment)a;
									articleLeveluseLimit = e.getPlayerLevelLimit();
									if(articleLeveluseLimit/20==最低级别/20){
// 										out.print("<font color='green'>《么错误装备》</font>--掉落物品："+name+"--物品最低级别："+最低级别+"--玩家等级限制："+articleLeveluseLimit+"<br>");
									}else{
										out.print("<font color='red'>《有错误装备》</font>--掉落物品："+name+"--物品最低级别："+最低级别+"--(装备)玩家级别限制："+articleLeveluseLimit+"<br>");
									}
								}	
								
							}
						}
					}
				}	
			}
		}
		out.print("总数："+count);
		out.print("道具总数："+propscount);
		out.print("道具总数："+equcount);
	%>
</form>
</body>
</html>
