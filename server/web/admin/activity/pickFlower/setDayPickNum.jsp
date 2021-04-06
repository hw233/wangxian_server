<%@ page language="java" contentType="text/html; charset=utf-8"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<%
String path = request.getContextPath();
String basePath = request.getScheme()+"://"+request.getServerName()+":"+request.getServerPort()+path+"/";
%>


<%@page import="com.fy.engineserver.activity.pickFlower.PickFlowerManager"%>
<%@page import="com.fy.engineserver.activity.pickFlower.PickFlowerEntity"%>
<%@page import="com.fy.engineserver.sprite.Player"%>
<%@page import="com.fy.engineserver.sprite.PlayerManager"%><html>
<head>
	<meta http-equiv="Content-Type" content="text/html; charset=utf-8">
	<title>设置每天玩家可以的采集数</title>
</head>

<body>
	
	
	<%
	
		String nums = request.getParameter("nums");
		if(nums != null && !nums.equals("")){
			int num = Integer.parseInt(nums.trim());
			PickFlowerManager.一天允许采几个 = num;
			out.print("success");
			return;
		}
		
	%>
	
	<form action="">

		num:<input type="text" name="nums"/>
		<input type="submit" value="submit">
	
	</form>
</body>
</html>
