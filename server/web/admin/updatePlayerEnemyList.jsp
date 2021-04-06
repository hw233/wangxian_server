<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<!DOCTYPE html PUBLIC "-//W3C//Dth HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dth">

<%@page import="com.fy.engineserver.sprite.Player"%>
<%@page import="com.fy.engineserver.sprite.Player.*"%>


<%@page import="com.fy.engineserver.sprite.PlayerManager"%><html>
<head>
<title>修改玩家的敌人列表</title>
</head>
<body>


	<%
		String name = request.getParameter("name");
		if(name != null && !name.equals("")){
			
			Player player = PlayerManager.getInstance().getPlayer(name);
			player.enemyList = new EnemyList();
			out.print("设置成功");
			
			return;
		}
	%>

	<form action="">
		玩家名称:<input type="text" name="name"><br/>
		<input type="submit" value="submit">
	</form>

</body>
</html>
