<%@page
	import="com.fy.engineserver.activity.feedsilkworm.FeedSilkwormManager"%>
<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@ include file="./inc.jsp"%>

<%
	List<Task> list = TaskManager.getInstance().fristTasksOfAlLine;
	for (Task task : list) {
		out.print(task.getName() + ",等级" + task.getGrade() + ",类型:" + task.getShowType() + "<BR/>");
	}
%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title>Insert title here</title>
</head>
<body>

</body>
</html>