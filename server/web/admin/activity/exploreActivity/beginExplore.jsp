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
<title>开始寻宝</title>
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
				
				ExploreEntity ee = player.getExploreEntity();
				if(ee != null){
					out.print("玩家正在进行寻宝任务");
				}else{
					ExploreManager em = ExploreManager.getInstance();
					em.randomGetExploreEntity((byte)0,player);
					
				}
			}catch(Exception e){
				e.printStackTrace();
				out.print("没有这个玩家" + name);
			}
		}
	
	%>

	
</body>
</html>