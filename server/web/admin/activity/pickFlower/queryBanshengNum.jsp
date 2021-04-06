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
<%@page import="com.fy.engineserver.gametime.SystemTime"%>
<%@page import="java.util.List"%><html>
<head>
	<meta http-equiv="Content-Type" content="text/html; charset=utf-8">
	<title>设置伴生num</title>
</head>

<body>
	
	
	
	<%
	
		String name = request.getParameter("name");
		if(name != null && !name.equals("") ){
			int num = PickFlowerManager.getInstance().pickFlowerEntity.伴生个数;
			out.print("success" + num);
			List<Long> list = PickFlowerManager.getInstance().pickFlowerEntity.agreePickFlowerPlayer;
			for(long id:list){
				out.print(id+"<br/>");
			}
			
			return;
		}
		
	%>
	
	<form action="">

		name:<input type="text" name="name"/>
		<input type="submit" value="submit">
	
	</form>
</body>
</html>
