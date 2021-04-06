<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Frameset//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-frameset.dtd">
<%@page import="com.fy.engineserver.datasource.article.data.articles.Article"%>
<%@page import="com.fy.engineserver.datasource.article.manager.ArticleManager"%>
<%@page import="com.fy.engineserver.newtask.targets.TaskTargetOfTalkToNPC"%>
<%@page import="com.fy.engineserver.sprite.npc.MemoryNPCManager"%>
<%@page import="com.fy.engineserver.activity.peoplesearch.PeopleSearchManager"%>
<%@page import="com.fy.engineserver.activity.peoplesearch.CountryNpc"%>
<%@page import="com.fy.engineserver.sprite.npc.NPCManager"%>
<%@page import="com.fy.engineserver.sprite.npc.NPC"%>
<%@page import="com.fy.engineserver.newtask.TaskEntity"%>
<%@page import="com.fy.engineserver.sprite.Player"%>
<%@page import="com.fy.engineserver.sprite.PlayerManager"%>
<%@page import="com.fy.engineserver.platform.PlatformManager.Platform"%>
<%@page import="com.fy.engineserver.platform.PlatformManager"%>
<%@page import="com.fy.engineserver.country.manager.CountryManager"%>
<%@page import="com.fy.engineserver.core.GameManager"%>
<%@page import="com.fy.engineserver.util.CompoundReturn"%>
<%@page import="com.fy.engineserver.newtask.service.TaskConfig.TargetType"%>
<%@page import="com.fy.engineserver.newtask.targets.TaskTarget"%>
<%@page import="com.fy.engineserver.newtask.TaskGivenArticle"%>
<%@page import="com.fy.engineserver.newtask.service.TaskManager"%>
<%@page import="com.fy.engineserver.newtask.Task"%>
<%@ page language="java" contentType="text/html; charset=utf-8" pageEncoding="utf-8"%>
<%@page import="java.util.*"%>
<title>修复物品描述</title>
</head>
<body>
<form method="post" action="?isstart=true"> 
	<%
		String needModifyNames[] = {"春夏秋冬","梅兰竹菊","梅兰竹菊—梅","梅兰竹菊—兰","梅兰竹菊—竹","梅兰竹菊—菊"};
		String needModifyMess[] = {"【春夏秋冬】可到【秋香】处换取湛天礼包(3级)*1、墨轮礼包(3级)*1、竹青礼包(3级)*1、枳香礼包(3级)*1、幽橘礼包(3级)*1，每个宝石礼包可以开出4颗同类的宝石；集齐3套成语字牌可在【唐伯虎】处换取四季至尊锦囊（魔域狼蛛蛋*1，红朝天龙*1）",
				"【梅兰竹菊】可到【秋香】处换取绝品精魄碎片*600；集齐3套成语字牌在【唐伯虎】处可换取四季至尊锦囊（魔域狼蛛蛋*1，红朝天龙*1）",
				"【梅】字可到【秋香】处换取绝品圣宠精魄*2，集齐一套字牌可到【唐伯虎】处换取梅兰竹菊",
				"【兰】字可到【秋香】处换取绝品圣宠精魄*3，集齐一套字牌可到【唐伯虎】处换取梅兰竹菊",
				"【竹】字可到【秋香】处换取绝品圣宠精魄*4，集齐一套字牌可到【唐伯虎】处换取梅兰竹菊",
				"【菊】字可到【秋香】处换取绝品圣宠精魄*150，集齐一套字牌可到【唐伯虎】处换取梅兰竹菊"};
		
		String needModifyNames_tw[] = {"梅蘭竹菊—梅","梅蘭竹菊—蘭","梅蘭竹菊—竹","梅蘭竹菊—菊"};
		String needModifyMess_tw[] = {
				"【梅】字可到【秋香】處換取絕品聖寵精魄*2，集齊一套字牌可到【唐伯虎】處換取梅蘭竹菊",
				"【蘭】字可到【秋香】處換取絕品聖寵精魄*3，集齊一套字牌可到【唐伯虎】處換取梅蘭竹菊",
				"【竹】字可到【秋香】處換取絕品聖寵精魄*4，集齊一套字牌可到【唐伯虎】處換取梅蘭竹菊",
				"【菊】字可到【秋香】處換取絕品聖寵精魄*150，集齊一套字牌可到【唐伯虎】處換取梅蘭竹菊"};
		if(PlatformManager.getInstance().isPlatformOf(Platform.官方)){
			for(int i=0;i<needModifyNames.length;i++){
				Article a = ArticleManager.getInstance().getArticle(needModifyNames[i]);
				if(a!=null){
					out.print("官方《修改前》物品名："+needModifyNames[i]+"--物品描述："+a.getDescription()+"<br>");
					a.setDescription(needModifyMess[i]);
					out.print("<font color='red'>《修改后》</font>物品名："+needModifyNames[i]+"--物品描述："+a.getDescription()+"<br>");
				}else{
					out.print("修复物品描述出错："+needModifyNames[i]+"不存在!");
				}
			}
		}else if(PlatformManager.getInstance().isPlatformOf(Platform.台湾)){
			for(int i=0;i<needModifyNames_tw.length;i++){
				Article a = ArticleManager.getInstance().getArticle(needModifyNames_tw[i]);
				if(a!=null){
					out.print("官方《修改前》物品名："+needModifyNames_tw[i]+"--物品描述："+a.getDescription()+"<br>");
					a.setDescription(needModifyMess_tw[i]);
					out.print("<font color='red'>《修改后》</font>物品名："+needModifyNames_tw[i]+"--物品描述："+a.getDescription()+"<br>");
				}else{
					out.print("修复物品描述出错："+needModifyNames_tw[i]+"不存在!");
				}
			}
		}
		
		
		
		
		
	
	
	%>
</form>
</body>
</html>
