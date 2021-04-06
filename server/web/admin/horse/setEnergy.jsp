<%@ page language="java" contentType="text/html; charset=utf-8"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<%
String path = request.getContextPath();
String basePath = request.getScheme()+"://"+request.getServerName()+":"+request.getServerPort()+path+"/";
%>
<%@page import="com.fy.engineserver.sprite.horse.*"%>
<%@page import="com.fy.engineserver.sprite.*"%>
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8">
</head>
<body>
	<% 
	String name = request.getParameter("name");
	String ids = request.getParameter("id");
		if(name == null || name.equals("")){
		%>
		<form action="">
			name:<input type="text" name="name"/>
			id:<input type="text" name="id"/>
			<input type="submit" value="submit"/>
		</form>
		
		<%
		}else{
			
			long id = Long.parseLong(ids);
			Player player = PlayerManager.getInstance().getPlayer(name);
			Horse horse = HorseManager.getInstance().getHorseById(id,player);
			horse.setEnergy(1);
			out.print("设置成功");
		}
	
%>
	
</body>
</html>
