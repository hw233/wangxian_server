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
<title>查看玩家身上的寻宝实体</title>
</head>
<body>

		<%
		String name = request.getParameter("playerName");
		if(name == null){
			%>
			<form action="" name="showExploreEntity">
				用户名：<input type="text" name="playerName" />
				<input type="submit" value="提交"/>
			</form>
			<%
		}else{
			PlayerManager pm = PlayerManager.getInstance();
			try{
				Player player =  pm.getPlayer(name);
				
				ExploreEntity ee = player.getExploreEntity();
				if(ee == null){
					out.print("玩家没有寻宝任务");
				}else{
					out.print("寻宝地图"+ee.getMapName()+"<br/>");
					out.print("寻宝地图的某一个片段"+ee.getMapSegmentName()+"<br/>");
					out.print("寻宝地图的x"+ee.getPoint().getX()+"<br/>");
					out.print("寻宝地图的y"+ee.getPoint().getY()+"<br/>");
					out.print("寻宝地图的范围"+ee.getRange()+"<br/>");
					out.print("寻宝地图道具id"+ee.getPropsId()+"<br/>");
					
				}
			}catch(Exception e){
				out.print("没有这个玩家" + name);
			}
		}
	
	%>


</body>
</html>