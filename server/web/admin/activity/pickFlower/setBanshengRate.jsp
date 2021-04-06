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
	<title>设置伴生的比例</title>
</head>

<body>
	
	
	<%
	
		String name = request.getParameter("name");
		if(name != null && !name.equals("")){
	
			float[] fs = {0.9f,1.0f};
			PickFlowerManager.伴生物品概率 = fs;
			out.print("success");
			return;
		}
		
	%>
	
	<form action="">

		设置:<input type="text" name="name"/>
		<input type="submit" value="submit">
	
	</form>
</body>
</html>
