<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Frameset//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-frameset.dtd">
<%@ page language="java" contentType="text/html; charset=utf-8" pageEncoding="utf-8"%>
<%@page import="java.util.*"%>
<%@page import="com.fy.engineserver.sprite.Player"%>
<%@page import="com.fy.engineserver.sprite.PlayerManager"%>


<%@page import="com.fy.engineserver.society.SocialManager"%>
<%@page import="com.fy.engineserver.society.Relation"%><html>
<head>
<title>查询一个人的结义</title>
</head>
<body>

<%
	String set = request.getParameter("set");

	if(set != null && !set.equals("")){
		
		String name = request.getParameter("name");
		
		if(name != null && !name.equals("")){
			Player player = PlayerManager.getInstance().getPlayer(name);
			Relation re = SocialManager.getInstance().getRelationById(player.getId());
			out.print("结义id:"+re.getUniteId());
			out.print("success");
			return;
		}else{
			out.print("输入名字");
		}
		return;
	}else{
		String name = request.getParameter("name");
	
		if(name != null && !name.equals("")){
			Player player = PlayerManager.getInstance().getPlayer(name);
			Relation re = SocialManager.getInstance().getRelationById(player.getId());
			re.setUniteId(-1);
			out.print("success");
			return;
		}
	}
%>


<form action="">
	
	不设置只是查询:<input type="text" name="set"/>
	name:<input type="text" name="name"/>
	<input type="submit" value="submit"/>
</form>



</body>

</html>
