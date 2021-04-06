<%@page import="java.util.HashMap"%>
<%@page import="com.fy.engineserver.enterlimit.Player_Process"%>
<%@page import="com.fy.engineserver.enterlimit.EnterLimitManager"%>
<%@page import="com.fy.engineserver.datasource.article.data.entity.ArticleEntity"%>
<%@page import="com.fy.engineserver.activity.newChongZhiActivity.RmbRewardData"%>
<%@page import="com.fy.engineserver.activity.newChongZhiActivity.TotalChongZhiActivity"%>
<%@page import="com.fy.engineserver.activity.newChongZhiActivity.SingleChongZhiActivity"%>
<%@page import="com.fy.engineserver.activity.newChongZhiActivity.FanLi4LongTimeChongZhiActivity"%>
<%@page import="com.fy.engineserver.activity.newChongZhiActivity.FirstChongZhiActivity"%>
<%@page import="java.util.Arrays"%>
<%@page import="com.fy.engineserver.sprite.PlayerManager"%>
<%@page import="com.fy.engineserver.activity.newChongZhiActivity.NewChongZhiActivityManager"%>
<%@page import="com.fy.engineserver.sprite.Player"%>
<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<%@page import="com.fy.engineserver.newtask.service.TaskManager"%>
<%@page import="java.util.List"%>
<%@page import="com.fy.engineserver.newtask.Task"%>
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title>Insert title here</title>
</head>
<body>
	<%
		int jincheng2 = 0;
		int jincheng10 = 0;
		int noHaveWangXian = 0;
		int jinchengIOS = 0;
		int jincengAndroid = 0;
		int showJinCengNum = 0;
		String nn = request.getParameter("sh");
		if (nn != null) {
			showJinCengNum = Integer.parseInt(nn);
		}
		HashMap<String, Integer> pss = new HashMap<String, Integer>();
		for (Long key : EnterLimitManager.player_process.keySet()) {
			Player_Process process = EnterLimitManager.player_process.get(key);
			if (process.getPlatform().equals("Android")) {
				jincengAndroid++;
				if (process.getAndroidProcesss().length == 2) {
					jincheng2++;
				}else if (process.getAndroidProcesss().length <= 10) {
					jincheng10++;
				}
				boolean isHave = false;
				for (int i = 0; i < process.getAndroidProcesss().length; i++) {
					if (process.getAndroidProcesss()[i].indexOf("飘渺寻仙曲") >= 0) {
						isHave = true;
					}
					
				}
				if (!isHave) {
					noHaveWangXian++;
				}else {
					for (int i = 0; i < process.getAndroidProcesss().length; i++) {
						Integer num = pss.get(process.getAndroidProcesss()[i]);
						if (num == null) {
							num = new Integer(0);
						}
						num = num.intValue() + 1;
						pss.put(process.getAndroidProcesss()[i], num);
					}
				}
			}else if (process.getPlatform().equals("IOS")) {
				jinchengIOS++;
				
			}
		}
	
	%>
	<br>
	玩家数目:<%=EnterLimitManager.player_process.size() %>
	<br>
	IOS数目:<%=jinchengIOS %>
	<br>
	Android数目:<%=jincengAndroid %>
	<br>
	进程数目为2的:<%=jincheng2 %>
	<br>
	进程数目为3到10的:<%=jincheng10 %>
	<br>
	进程中未有飘渺寻仙曲的:<%=noHaveWangXian %>
	<br>
	<br>
	<br>
	<%=Arrays.toString(Player_Process.moniqiArr) %>
	<br>
	<br>
	<%
		for (String key : pss.keySet()) {
			int num = pss.get(key).intValue();
			if (num >= showJinCengNum) {
				out.print("进程名字["+key+"] ["+num+"]<br>");
			}
		}
	%>
	
</body>
</html>
