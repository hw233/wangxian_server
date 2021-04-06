<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<%@page import="java.util.*"%>
<%@page import="com.xuanzhi.boss.game.GameConstants"%>
<%@page import="com.fy.engineserver.activity.loginActivity.LoginActivityArticle"%>
<%@page import="com.fy.engineserver.activity.loginActivity.ContinueLoginActivity"%>
<%@page import="com.fy.engineserver.activity.loginActivity.Activity"%>
<%@page import="java.util.List"%>
<%@page import="com.fy.engineserver.activity.loginActivity.ActivityManagers"%>
<%@page import="com.fy.boss.authorize.model.Passport"%>
<%@page import="com.fy.engineserver.sprite.Player"%>
<%@ page language="java" contentType="text/html; charset=utf-8"%>
<%@page import="com.fy.engineserver.sprite.PlayerManager"%>


<%@page import="com.xuanzhi.tools.text.StringUtil"%><html>


<head>
<title>test</title>
</head>
<body>
	<%
		List<Activity> activitys = ActivityManagers.getInstance().getActivitys(); 		
		String servername = GameConstants.getInstance().getServerName();
		Set<String> set = new HashSet<String>();
		set.add("天下无双");
		set.add("海纳百川");
		set.add("琼楼金阙");
		set.add("飘渺仙道");
		set.add("万里苍穹");
		set.add("盛世欢歌");
		set.add("修罗转生");
		if(set.contains(servername)){
			for(Activity a:activitys){
				if(a.getName().equals("ContinueLoginActivity20130406绿色服")){
					ContinueLoginActivity ca = (ContinueLoginActivity)a;
					List<LoginActivityArticle> articles = ca.getArticles();
					for(LoginActivityArticle l:articles){
							l.setShowMess("银块x20");
							l.setRewardMess("恭喜您获得了银块x20");
							l.setPnums(20);
							l.setRewardNum(20);
					}
					out.print("<"+servername+">修改成功");
				}
			}
		}else{
			if(!"桃源仙境".equals(servername)){
				for(Activity a:activitys){
					if(a.getName().equals("ContinueLoginActivity20130406非绿色服")){
						ContinueLoginActivity ca = (ContinueLoginActivity)a;
						List<LoginActivityArticle> articles = ca.getArticles();
						for(LoginActivityArticle l:articles){
								l.setShowMess("连登令旗x4");
								l.setRewardMess("恭喜您获得了连登令旗x4");
								l.setPnums(4);
								l.setRewardNum(4);
						}
						out.print("<"+servername+">修改成功");
					}
				}
			}else{
				out.print("桃源仙境不能刷==========================");
			}
		}
	
		
	%>
	

</body>
</html>

