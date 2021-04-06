<%@ page language="java" contentType="text/html; charset=utf-8"
    pageEncoding="utf-8"%>
<%@ page import="com.fy.engineserver.datasource.props.*,com.google.gson.*,java.util.*,com.fy.engineserver.task.*,com.fy.engineserver.datasource.career.*" %>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<%@include file="IPManager.jsp" %><html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8">
<title>任务</title>
<% TaskManager tm = TaskManager.getInstance();
RuningLoopTask[] runingLoopTasks = null;
if(tm != null){
	runingLoopTasks = tm.getAllRuningLoopTasks();
}

%>
<link rel="stylesheet" type="text/css" href="../css/common.css" />
<link rel="stylesheet" type="text/css" href="../css/table.css" />
<style type="text/css">
.titlecolor{
background-color: #C2CAF5;
}
</style>
</head>
<body>
<a href="TasksView.jsp">刷新此页面</a>
<br>
<br>
<input type="hidden" id="dependency">
<div id=pltsTipLayer style="display: none;position: absolute; z-index:10001"></div>

<table >
<tr class="titlecolor"><td colspan="9">所有跑环任务</td></tr>
<tr class="titlecolor"><td>跑环任务的唯一标识</td><td>任务的归属地</td><td>类型</td><td>类型data</td><td>玩家级别限制</td><td>描述</td><td nowrap="nowrap">跑环任务中的环</td></tr>
<%
if(runingLoopTasks != null){
for(int i = 0; i < runingLoopTasks.length; i++){ 
	RuningLoopTask runingLoopTask = runingLoopTasks[i];
	if(runingLoopTask != null){
		boolean exist = false;
		String dep = runingLoopTask.getDependency();
		if(dep != null){
			String[] deps = tm.getAllDependencys();
			for(String d : deps){
				if(d.equals(dep)){
					exist = true;
					break;
				}
			}
			
		}
		%>
		<tr>
		<td <%=runingLoopTask.getItems() != null ? "rowspan='"+runingLoopTask.getItems().length+"'":"" %><%=exist ? "" : "bgcolor=#ff0000" %>>><%=runingLoopTask.getName() %></td>
		<td <%=runingLoopTask.getItems() != null ? "rowspan='"+runingLoopTask.getItems().length+"'":"" %>><%=runingLoopTask.getDependency() %></td>
		<td <%=runingLoopTask.getItems() != null ? "rowspan='"+runingLoopTask.getItems().length+"'":"" %>><%=runingLoopTask.getLoopType() %></td>
		<td <%=runingLoopTask.getItems() != null ? "rowspan='"+runingLoopTask.getItems().length+"'":"" %>><%=runingLoopTask.getLoopTypeData() %></td>
		<td <%=runingLoopTask.getItems() != null ? "rowspan='"+runingLoopTask.getItems().length+"'":"" %>><%=runingLoopTask.getMinPlayerLevelLimit()+"/"+runingLoopTask.getMaxPlayerLevelLimit() %></td>
		<td <%=runingLoopTask.getItems() != null ? "rowspan='"+runingLoopTask.getItems().length+"'":"" %>><%=runingLoopTask.getDescription() %></td>
		<%
		RuningLoopItem[] rlis = runingLoopTask.getItems();
		%>
		<%if(rlis != null && rlis.length != 0){
			RuningLoopItem rli = rlis[0];
				if(rli != null){
					StringBuffer sb = new StringBuffer();
					Task[] tasks = rli.getTasks();
					if(tasks != null){
						for(Task task : tasks){
							if(task != null){
								sb.append("<a href='TaskByTaskId.jsp?taskId="+task.getId()+"'>"+task.getName()+"</a><br>");
							}
						}
					}
					%>
					<td><%=sb.toString() %></td>
					<%
				}
			}else{
				out.println("<td></td>");
			} %>
		</tr>
		<%
		if(rlis != null){
			for(int j = 1; j < rlis.length; j++){
				RuningLoopItem rli = rlis[j];
				if(rli != null){
					%>
					<tr>
					<%
					StringBuffer sb = new StringBuffer();
					Task[] tasks = rli.getTasks();
					if(tasks != null){
						for(Task task : tasks){
							if(task != null){
								sb.append("<a href='TaskByTaskId.jsp?taskId="+task.getId()+"'>"+task.getName()+"</a><br>");
							}
						}
					}
					%>
					<td><%=sb.toString() %></td>
					</tr>
					<%
				}
			}
			
		}

	}
}
}%>
</table>
</body>
<script type="text/javascript" src="../js/title.js"></script>
</html>
