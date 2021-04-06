<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">

<%@page import="com.fy.engineserver.activity.godDown.GodDwonManager"%>
<%@page import="java.util.Calendar"%>
<%@page import="com.fy.engineserver.sprite.Player"%>
<%@page import="com.fy.engineserver.sprite.PlayerManager"%>
<%@page import="com.fy.engineserver.activity.godDown.GodDownInfo"%>
<%@page import="com.fy.engineserver.util.Utils"%>
<%@page import="com.fy.engineserver.activity.godDown.GodDownFlushEntity"%><html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title>查看刷新实体</title>
</head>
<body>

	<%
	String set = request.getParameter("set");
	if(set != null && !set.equals("")){
		
		GodDownFlushEntity entity = GodDwonManager.getInstance().flushEntity;
		if(entity != null){
			
			out.print("今天第几次:"+entity.dayNum+"<br/>");
			out.print("第几个npc:"+entity.npcNum+"<br/>");
			out.print("地图:"+entity.nowGame.getGameInfo().getName()+"<br/>");
			out.print("x:"+entity.nowNpc.getX()+"y:"+entity.nowNpc.getY()+"<br/>");
			out.print("国家:"+entity.nowGame.country);
			
		}else{
		out.print("flush entity null");	
		}
		
		return;
	}
	%>


	<form action="">
		playerName:<input type="text" name="set"/ >
		<input type="submit" value="submit"> 
	</form>

</body>
</html>