<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">

<%@page import="com.fy.engineserver.activity.godDown.GodDwonManager"%><html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title>设置时间间隔</title>
</head>
<body>

		<%
		String set = request.getParameter("set");
		if(set != null && !set.equals("")){
			GodDwonManager.十分钟 = (10*60*1000)/5;
			GodDwonManager.半小时 = (30*60*1000)/5;
			GodDwonManager.五分钟 = (5*60*1000)/5;
			out.print("设置为原来的1/5时间");
			return;
		}else{
			GodDwonManager.十分钟 = 10*60*1000;
			GodDwonManager.半小时 = 30*60*1000;
			GodDwonManager.五分钟 = 5*60*1000;
			out.print("设置为正常时间<br/>");
		}
	
	%>

	<form action="">
		设置天神下凡时间间隔(没有值为正常，有值为缩小5倍时间):<input type="text" name="set"/ >
		<input type="submit" value="submit"> 
	</form>


</body>
</html>