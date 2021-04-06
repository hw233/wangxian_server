<%@ page language="java" contentType="text/html; charset=utf-8"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<%
String path = request.getContextPath();
String basePath = request.getScheme()+"://"+request.getServerName()+":"+request.getServerPort()+path+"/";
%>


<%@page import="com.fy.engineserver.activity.pickFlower.PickFlowerManager"%>
<%@page import="com.fy.engineserver.activity.pickFlower.PickFlowerEntity"%>
<%@page import="com.fy.engineserver.sprite.Player"%>
<%@page import="com.fy.engineserver.sprite.PlayerManager"%>
<%@page import="com.fy.engineserver.gametime.SystemTime"%><html>
<head>
	<meta http-equiv="Content-Type" content="text/html; charset=utf-8">
	<title>设置玩家的上次采花时间</title>
</head>

<body>
	
	
	
	<%
	
		String name = request.getParameter("name");
		if(name != null && !name.equals("") ){
			Player player = PlayerManager.getInstance().getPlayer(name);
			player.setLastCaihuaTime(SystemTime.currentTimeMillis() - 1l*24*60*60*1000);
			out.print("success");
			return;
		}
		
	%>
	
	<form action="">

		name:<input type="text" name="name"/>
		<input type="submit" value="submit">
	
	</form>
</body>
</html>
