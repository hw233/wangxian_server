<%@page import="com.fy.engineserver.newtask.Task"%>
<%@page import="com.fy.engineserver.newtask.dao.DeliverTaskDao"%>
<%@page import="com.fy.engineserver.newtask.service.TaskManager"%>
<%@page import="java.util.*"%>
<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<%
	TaskManager manager = TaskManager.getInstance();

	DeliverTaskDao dao = manager.getDeliverDao();
	
	long playerId = -1;
	
	String id = request.getParameter("id");
	
	if (id != null && !id.isEmpty()) {
		playerId = Long.valueOf(id);
	}
	
%>
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title>Insert title here</title>
</head>
<body>
<form action="./checkPlayerTask.jsp" method="post">
<input type="text" name="id" value="<%=id%>">
<input type="submit" value="OK">
</form>
<HR/>
<BR/>
<% if(playerId != -1){ 
	List<Long> taskIds = dao.getPlayerDeilverTask(playerId);
	for (int i = 0; i < taskIds.size(); i++) {
		Task task = manager.getTask(taskIds.get(i));
		out.print("[" + task.getId());
		out.print(",");
		out.print(task.getName() + "]");
		if (i % 10 == 9) {
			out.print("<BR/>");
		} else {
			out.print("&nbsp;&nbsp;&nbsp;&nbsp;");
		}
	}	
}%>
</body>
</html>