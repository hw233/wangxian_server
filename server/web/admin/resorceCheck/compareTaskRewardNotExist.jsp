<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Frameset//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-frameset.dtd">
<%@page import="com.fy.engineserver.newtask.service.TaskConfig.PrizeType"%>
<%@page import="com.fy.engineserver.datasource.article.data.props.ArticleProperty"%>
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
<title>比较物品不存在</title>
</head>
<body>
<form method="post" action="?isstart=true"> 
	<%!
		public int getIndex(int level1){
			return (level1-1)/20;
		}
	%>
	
	<%
		//任务奖励装备等级 与 任务等级
		TaskPrize[] tempPrizeList = null;			
		TaskManager tm = TaskManager.getInstance();
		ArticleManager am = ArticleManager.getInstance();
		HashMap<Long, Task> taskIdMap = tm.getTaskIdMap();
		Iterator it = taskIdMap.values().iterator();
		String[] prizeName = null;
		int taskLevel = 0;
		int minGradeLimit = 0;
		int articleLeveluseLimit = 0;
		int propscount = 0;
		int escount = 0;
		int packetcount = 0;
		while(it.hasNext()){
			Task task = (Task)it.next();
			tempPrizeList = task.getPrizes();
			if(tempPrizeList!=null && tempPrizeList.length>0){
				for(TaskPrize prize:tempPrizeList){
					if(prize.getPrizeType().equals(PrizeType.CAREER_ARTICLE)){
						prizeName = prize.getPrizeName();
						for(String name:prizeName){
							Article a = am.getArticle(name);
							if(a!=null){
								 if(a instanceof Equipment){
									Equipment e = (Equipment)a;
									escount++;
									articleLeveluseLimit = e.getPlayerLevelLimit();
									taskLevel = task.getGrade();
									if(getIndex(articleLeveluseLimit)==getIndex(taskLevel)){
// 										out.print("<font color='green'>《么错误装备》</font>任务："+task.getName()+"--奖励名称："+name+"--任务等级："+taskLevel+"--奖励物品等级限制："+articleLeveluseLimit+"<br>");
									}else{
										out.print("<font color='red'>《有错误装备》</font>任务："+task.getName()+"--奖励名称："+name+"--任务等级："+taskLevel+"--奖励物品等级限制："+articleLeveluseLimit+"<br>");
									}
								}
							}else{
								out.print("《物品不存在！》物品名：<font color='red'>"+name+"</font><br>");
							}
						}
					}
					
					
					if(prize instanceof TaskPrizeOfArticle){
						prizeName = prize.getPrizeName();		
						for(String name:prizeName){
							Article a = am.getArticle(name);
							if(a!=null){
								if(a instanceof Props){
									propscount++;
									Props p = (Props)a;
									//玩家等级限制
									articleLeveluseLimit = p.getLevelLimit();
									taskLevel = task.getGrade();
									if(getIndex(articleLeveluseLimit)==getIndex(taskLevel)){
// 										out.print("<font color='green'>《么错误道具》</font>任务："+task.getName()+"--奖励名称："+name+"--任务等级："+taskLevel+"--奖励物品等级限制："+articleLeveluseLimit+"<br>");
									}else{
										out.print("<font color='red'>《有错误道具》</font>任务："+task.getName()+"--奖励名称："+name+"--任务等级："+taskLevel+"--奖励物品等级限制："+articleLeveluseLimit+"<br>");
									}
								}else if(a instanceof PackageProps){
									PackageProps p = (PackageProps)a;
									ArticleProperty articlenames[]= p.getArticleNames();
									if(articlenames!=null && articlenames.length>0){
										packetcount++;
										for(ArticleProperty ap:articlenames){
											if(ap.getArticleName()!=null){
												Article app = am.getArticle(ap.getArticleName());
												if(app!=null){
													if(app instanceof Props){
														propscount++;
														Props pp = (Props)app;
														//玩家等级限制
														articleLeveluseLimit = pp.getLevelLimit();
														taskLevel = task.getGrade();
														if(getIndex(articleLeveluseLimit)==getIndex(taskLevel)){
//					 										out.print("<font color='green'>《么错误道具》</font>任务："+task.getName()+"--奖励名称："+name+"--任务等级："+taskLevel+"--奖励物品等级限制："+articleLeveluseLimit+"<br>");
														}else{
															out.print("<font color='red'>《有错误背包道具》</font>任务："+task.getName()+"--奖励名称："+name+"--任务等级："+taskLevel+"--奖励物品等级限制："+articleLeveluseLimit+"<br>");
														}
													}else if(app instanceof Equipment){
														Equipment ee = (Equipment)app;
														articleLeveluseLimit = ee.getPlayerLevelLimit();
														taskLevel = task.getGrade();
														if(getIndex(articleLeveluseLimit)==getIndex(taskLevel)){
//					 										out.print("<font color='green'>《么错误装备》</font>任务："+task.getName()+"--奖励名称："+name+"--任务等级："+taskLevel+"--奖励物品等级限制："+articleLeveluseLimit+"<br>");
														}else{
															out.print("<font color='red'>《有错误背包装备》</font>任务："+task.getName()+"--奖励名称："+name+"--任务等级："+taskLevel+"--奖励物品等级限制："+articleLeveluseLimit+"<br>");
														}
													}
													
												}
											}
										}	
									}
								}
							}else{
								out.print("《物品不存在！》物品名：<font color='red'>"+name+"</font><br>");
							}
							
						}
					}
				}
			}
			
		}
		out.print("道具总数："+propscount+"<br>");
		out.print("装备总数："+escount+"<br>");
		out.print("背包道具总数："+packetcount+"<br>");
	%>
</form>
</body>
</html>
