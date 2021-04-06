<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">

<%@page import="com.fy.engineserver.gm.feedback.service.FeedbackManager"%><html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title>修改自动关闭反馈时间</title>
</head>
<body>

	<%
		String ss = request.getParameter("time");
		if(ss != null && !ss.equals("")){
			FeedbackManager.关闭间隔 = 5*60*1000;
			out.print("修改成功");
			return;
		}
	
	%>


	<form action="">
		修改自动关闭为5分钟:<input type="text" name="time"><br/>
		<input type="submit" value="close">
	</form>


</body>
</html>