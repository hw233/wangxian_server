<%@page import="java.util.List"%>
<%@page import="com.fy.engineserver.newtask.service.TaskConfig"%>
<%@page import="com.fy.engineserver.newtask.Task"%>
<%@page import="java.util.Arrays"%>
<%@page import="com.fy.engineserver.newtask.service.TaskManager"%>
<%@page import="java.util.Iterator"%>
<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title>所有地图上的任务</title>
</head>
<body>
	<%
		TaskManager taskManager = TaskManager.getInstance();
		for (Iterator<String> itor = taskManager.gameTasks.keySet().iterator(); itor.hasNext();) {
			String gameName = itor.next();
			out.print("<hr/>");
			out.print("<font color = red>" + gameName + "</font><BR/>");
			out.print("<hr/>");
			for (Iterator<String> inner = taskManager.gameTasks.get(gameName).keySet().iterator(); inner.hasNext();) {
				String npcName = inner.next();
				List<Task> tasks = taskManager.gameTasks.get(gameName).get(npcName);
				out.print("&nbsp;&nbsp;&nbsp;&nbsp;<font color = green>" + npcName + "[" + tasks.size() + "]" + "</font>:");
				for (Task task : tasks) {
					String grade = "等级[低限:" + task.getMinGradeLimit() + ",高限:" + task.getMaxGradeLimit() + ",任务等级:" + task.getGrade() + ",交付地图:" + task.getEndMap() + "," + task.getEndNpc() + "]";
					out.print("<a href='./Task.jsp?id=" + task.getId() + "' title=" + grade + ">" + task.getName() + "</a>[" + TaskConfig.SHOW_NAMES[task.getShowType()] + "]");
					out.print("&nbsp;&nbsp;,");
				}
				out.print("<BR/>");
			}
		}
	%>
</body>
</html>