<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">

<%@page import="com.fy.engineserver.activity.ActivityManager"%>
<%@page import="com.fy.engineserver.sprite.Player"%>
<%@page import="com.fy.engineserver.sprite.PlayerManager"%>
<%@page import="com.xuanzhi.tools.cache.diskcache.DiskCache"%>
<%@page import="com.fy.engineserver.activity.PlayerActivityRecord"%><html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title>腾讯在线设置上次获得时间</title>
</head>
<body>

	<%
		String ex = request.getParameter("server");
		if(ex != null && !ex.equals("")){
			Player player = PlayerManager.getInstance().getPlayer(ex);
			
			DiskCache cache = ActivityManager.getInstance().disk;
			Object o = cache.get(player.getId());
			if(o != null){
				PlayerActivityRecord pr = (PlayerActivityRecord) o;
				pr.setLastOnlineReceiveTime(System.currentTimeMillis() - 29*60*1000);
				
				out.print(pr.isFirstReceive()+"<br/>");
				out.print(pr.isSecondReceive()+"<br/>");
				out.print(pr.isThirdReceive()+"<br/>");
				out.print(pr.isFourthReceive()+"<br/>");
			}else{
				out.print("没有实体");
			}
			out.print("设置成功<br/>");
			return;
		}
	%>

	<form action="">
		设置登录时间提前29分钟:<input type="text" name="server" >
	
		<input type="submit" value="submit">
	
	</form>

</body>
</html>