<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">

<%@page import="com.fy.engineserver.activity.godDown.GodDwonManager"%>
<%@page import="java.util.Calendar"%><html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title>设置活动开始</title>
</head>
<body>

	<%
	String set = request.getParameter("set");
	if(set != null && !set.equals("")){
		
		GodDwonManager.getInstance().beginTime = System.currentTimeMillis();
		out.print("设置活动开始<br/>");
		
		GodDwonManager.logger.warn("[后台开启活动] [设置时间]");
		return;
	}
	%>


	<form action="">
		设置天神下凡的开始时间:<input type="text" name="set"/ >
		<input type="submit" value="submit"> 
	</form>

</body>
</html>