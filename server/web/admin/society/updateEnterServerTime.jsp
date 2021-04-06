<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Frameset//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-frameset.dtd">
<%@ page language="java" contentType="text/html; charset=utf-8" pageEncoding="utf-8"%>
<%@page import="java.util.*"%>
<%@page import="com.fy.engineserver.sprite.Player"%>
<%@page import="com.fy.engineserver.sprite.PlayerManager"%>


<%@page import="com.fy.engineserver.society.Relation"%>
<%@page import="com.fy.engineserver.society.SocialManager"%>
<%@page import="com.fy.engineserver.unite.Unite"%>
<%@page import="com.fy.engineserver.unite.UniteManager"%><html>
<head>
<title>更新玩家的登陆时间</title>
</head>
<body>

<%

		String name = request.getParameter("name");
		String times = request.getParameter("time");
	
		if(name != null && !name.equals("") && times != null && !times.equals("")){
			Player player = PlayerManager.getInstance().getPlayer(name);
			
			int timeI = Integer.parseInt(times);
			
			long time = 1l*timeI*60*1000;
			
			player.setEnterServerTime(System.currentTimeMillis() - time);
			
			SocialManager.logger.error("[后台修改登陆时间] ["+player.getLogString()+"] [时间:"+player.getEnterServerTime()+"]");
			
			return;
		}
	
%>


<form action="">
	name:<input type="text" name="name"/><br/>
	提前时间(分钟10,20,60,120):<input type="text" name="time"/><br/>
	<input type="submit" value="submit"/>
</form>



</body>

</html>
