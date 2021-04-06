<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">

<%@page import="com.fy.engineserver.activity.godDown.GodDwonManager"%>
<%@page import="java.util.Calendar"%>
<%@page import="com.fy.engineserver.sprite.Player"%>
<%@page import="com.fy.engineserver.sprite.PlayerManager"%>
<%@page import="com.fy.engineserver.activity.godDown.GodDownInfo"%>
<%@page import="com.fy.engineserver.util.Utils"%><html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title>查看某人奖励</title>
</head>
<body>

	<%
	String set = request.getParameter("set");
	if(set != null && !set.equals("")){
		Player player = PlayerManager.getInstance().getPlayer(set);
		
		Object object = GodDwonManager.getInstance().getCache().get(player.getId());
		GodDownInfo info = null;
		if(object != null){
			info = (GodDownInfo) object;
		}else{
			info = new GodDownInfo();
		}
		out.print("上次奖励时间:"+Utils.formatTimeDisplay(info.lastReceiveTime)+"<br/>");
		StringBuffer sb = new StringBuffer();
		for(int i : info.receiveNpcList){
			sb.append("  id:"+i);
		}
		out.print(sb.toString()+"<br/>");
		return;
	}
	%>


	<form action="">
		playerName:<input type="text" name="set"/ >
		<input type="submit" value="submit"> 
	</form>

</body>
</html>