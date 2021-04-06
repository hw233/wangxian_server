<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@ include file="./inc.jsp"%>
<%
	HashMap<String, List<Task>> taskMap = TaskManager.getInstance().getTaskGrouopMap();


%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<link rel="stylesheet" type="text/css" href="../task/css/main.css">
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title>任务列表</title>
</head>
<body>
	<table>
		<tr>
			<td>ID</td>
			<td>任务名</td>
			<td>组名</td>
			<td>任务等级</td>
			<td>任务类型</td>
		</tr>

		<%
			int groupCount = 0;
			for (Iterator<String> itor = taskMap.keySet().iterator(); itor.hasNext();) {
				String groupName = itor.next();
				List<Task> list = taskMap.get(groupName);
				boolean onlyOne = list.size() == 1;
				groupCount++;
				for (int i = 0; i < list.size(); i++) {
					Task task = list.get(i);
			%>
			<tr class=<%=groupCount%2==1 ? "head" : ""%>>
				<td><%=task.getId()%></td>
				<td><a href="./Task.jsp?id=<%=task.getId()%>"><%=task.getName()%></a>
				</td>
				<td><%=task.getGroupName()%></td>
				<td><%=task.getGrade()%></td>
				<td><%=TaskConfig.SHOW_NAMES[task.getShowType()]%></td>
			</tr>
			<%
				}
			}
		%>
	</table>
	<HR/>
	所有"线"的第一个任务
	<HR/>
		<%
			for (int i = 0; i < TaskManager.getInstance().fristTasksOfAlLine.size(); i ++) {
				Task task = TaskManager.getInstance().fristTasksOfAlLine.get(i);
				out.print(task.getName());
				out.print("[" + task.getId() + "]");
				if (i % 5 == 4) {
					out.print("<BR/>");
				} else {
					out.print(",");
				}
		%>
	<% }%>
</body>
</html>