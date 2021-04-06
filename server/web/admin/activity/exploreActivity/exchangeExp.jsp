<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">

<%@page import="com.fy.engineserver.sprite.PlayerManager"%>
<%@page import="com.fy.engineserver.sprite.Player"%>
<%@page import="com.fy.engineserver.activity.explore.ExploreEntity"%>
<%@page import="com.fy.engineserver.activity.explore.ExploreManager"%>

<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title>兑换经验</title>
</head>
<body>
	<%
		String name = request.getParameter("playerName");
		if(name == null){
			%>
			<form action="" name="beginExplore">
				用户名：<input type="text" name="playerName" />
				<input type="submit" value="提交"/>
			</form>
			<%
		}else{
			PlayerManager pm = PlayerManager.getInstance();
			try{
				Player player =  pm.getPlayer(name);
				
				ExploreManager em = ExploreManager.getInstance();
				em.convertFromArticle(player);
				out.print("转化成功");
			}catch(Exception e){
				e.printStackTrace();
				out.print("没有这个玩家" + name);
			}
		}
	
	%>

	
</body>
</html>