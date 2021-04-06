<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">

<%@page import="java.util.Calendar"%>
<%@page import="com.fy.engineserver.activity.ActivityManager"%><html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title>设置增加酒帖的特殊日期</title>
</head>
<body>

	<%
		String set = request.getParameter("set");
		if(set != null && !set.equals("")){
			
			Calendar cal = Calendar.getInstance();
			int day = cal.get(Calendar.DAY_OF_MONTH);
			int mon = cal.get(Calendar.MONTH);
			int year = cal.get(Calendar.YEAR);
			
			out.print("已经设置的年:"+ActivityManager.getInstance().增加酒帖特殊[0]+"   月:"+ActivityManager.getInstance().增加酒帖特殊[1]+"  日:"+ActivityManager.getInstance().增加酒帖特殊[2]+"<br/>");
			out.print("月会比现在小1，设置为当日时间"+"<br/>");
			ActivityManager.getInstance().增加酒帖特殊[0] = year;
			ActivityManager.getInstance().增加酒帖特殊[1] = mon;
			ActivityManager.getInstance().增加酒帖特殊[2] = day;
			out.print("设置成功");
			return;
		}
	
	%>

	<form action="">
		设置为当天时间:<input type="text" name="set"/ >
		<input type="submit" value="submit"> 
	</form>

</body>
</html>