<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Frameset//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-frameset.dtd">
<%@ page language="java" contentType="text/html; charset=utf-8" pageEncoding="utf-8"%>
<%@page import="java.util.*"%>
<%@page import="com.fy.engineserver.sprite.Player"%>
<%@page import="com.fy.engineserver.sprite.PlayerManager"%>

<html>
<head>
<title>查询一个人的传功状态</title>
</head>
<body>

<%
	String set = request.getParameter("set");

	if(set != null && !set.equals("")){
		
		Player[] ps = PlayerManager.getInstance().getOnlinePlayers();
		for(Player p : ps){
			if(p.isChuangonging()){
				out.print("正在进行传功:"+p.getName()+"<br/>");
			}
		}
		return;
	}else{
		String name = request.getParameter("name");
	
		if(name != null && !name.equals("")){
			Player player = PlayerManager.getInstance().getPlayer(name);
			player.setChuangonging(false);
			out.print("success");
			return;
		}
	}
%>


<form action="">
	
	不设置:<input type="text" name="set"/>
	name:<input type="text" name="name"/>
	<input type="submit" value="submit"/>
</form>



</body>

</html>
