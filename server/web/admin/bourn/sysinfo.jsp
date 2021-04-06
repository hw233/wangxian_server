<%@page import="com.fy.engineserver.core.LivingObject"%>
<%@page import="com.fy.engineserver.core.GameManager"%>
<%@page import="com.fy.engineserver.core.Game"%>
<%@page import="com.fy.engineserver.sprite.npc.NPC"%>
<%@page import="com.fy.engineserver.sprite.npc.MemoryNPCManager"%>
<%@page import="com.fy.engineserver.newtask.service.TaskManager"%>
<%@page
	import="com.fy.engineserver.sprite.concrete.GamePlayerManager"%>
<%@page import="com.fy.engineserver.sprite.Player"%>
<%@page import="com.fy.engineserver.newtask.Task"%>
<%@page import="com.fy.engineserver.bourn.BournManager"%>
<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<%
	BournManager bournManager = BournManager.getInstance();
	for (int i = 0; i < BournManager.starTasks.length; i++) {
		Task[] tasks = BournManager.starTasks[i];
		out.print("--------------------------[颜色" + i + "]-----------------------<BR/>");
		if (tasks == null) {
			out.print("任务列表NULL");
			continue;
		}
		for (Task task : tasks) {
			if (task == null) {
				out.print("任务NULL");
				continue;
			}
			out.print(task.getId() + "," + task.getName() + "\t\t");
		}
	}
	out.print("@@@@@@@@@@@@@@@@@@@@@@@@@@<BR/>");

%>

<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title>Insert title here</title>
</head>
<body>

</body>
</html>