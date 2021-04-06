<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">

<%@page import="com.fy.engineserver.newtask.service.TaskManager"%>
<%@page import="java.util.HashMap"%>
<%@page import="java.util.List"%>
<%@page import="com.fy.engineserver.newtask.Task"%><html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title>Insert title here</title>
</head>
<body>
<%
int level=999;
TaskManager tm=TaskManager.getInstance();
HashMap<String, List<Task>> taskGrouopMap=tm.getTaskGrouopMap();
Task task = TaskManager.getInstance().getTask(100227);
task.setMinGradeLimit(level);
Task task2 = TaskManager.getInstance().getTask("内测财神金银袋");
task2.setMinGradeLimit(level);
out.print("后台修改任务["+task.getName()+"]的可接等级为"+level+"<br>");
%>

</body>
</html>