<%@ page language="java" contentType="text/html; charset=utf-8"
    pageEncoding="utf-8"%>
<%@ page import="com.fy.engineserver.datasource.props.*,com.google.gson.*,java.util.*,com.fy.engineserver.task.*" %>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<%@include file="IPManager.jsp" %><html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8">
<title>任务</title>
<link rel="stylesheet" type="text/css" href="../css/common.css" />
<style type="text/css">
.tablestyle{
width:100%;
border:0px solid #69c;
border-top:0px solid #69c;
border-right:0px solid #69c;
border-bottom:0px solid #69c;
border-left:0px solid #69c;
border-collapse: collapse;
}
td{
border:1px solid #69c;
}
.lefttd{
border-left:0px solid #69c;
}
.toptd{
border-top:0px solid #69c;
}
.righttd{
border-right:0px solid #69c;
}
.bottomtd{
border-bottom:0px solid #69c;
}
</style>
</head>
<body>
<%

String dependency = request.getParameter("dependency");

if(dependency != null){

	TaskManager tm = TaskManager.getInstance();
	Task[] tasks = null;
	if(tm != null){
		tasks = tm.getAllTasksOnDependency(dependency);
	}
	if(tasks != null){

%>
<font color="red"><b><%=dependency %></b></font>中的所有任务<br><br>
<table class="tablestyle">
<tr align="center">
  				<td width="160" align="center" style="word-wrap: break-word;">任务名</td>
  				<td width="160" align="center" style="word-wrap: break-word;">任务级别</td>
  				<td width="160" align="center" style="word-wrap: break-word;">玩家最小级别</td>
  				<td width="160" align="center" style="word-wrap: break-word;">任务类型</td>
  				<td width="160" align="center" style="word-wrap: break-word;">前置任务</td>
  				</tr>
<%
		
		int alSize = tasks.length;
		for(int j = 0; j < alSize; j++){
  			%>
  			<tr align="center">
  			<%Task task = tasks[j];
  			%>
  				<td width="160" align="center" style="word-wrap: break-word;">
  				
  				<%
  				if(task != null){ %>
  				<a href="TaskByTaskId.jsp?taskId=<%=task.getId() %>" ><%=task.getName() %></a>
  				<%} %>
  				</td>
  				<td width="160" align="center" style="word-wrap: break-word;">
  				
  				<%
  				if(task != null){ %>
  				<%=task.getTaskLevel() %>
  				<%} %>
  				</td>
  				<td width="160" align="center" style="word-wrap: break-word;">
  				
  				<%
  				if(task != null){ %>
  				<%=task.getMinPlayerLevel() %>
  				<%} %>
  				</td>
  				<td width="160" align="center" style="word-wrap: break-word;">
  				
  				<%
  				if(task != null){ 
  					String taskTypeStr = "";
  					if(task.getTaskType()==Task.TYPE_ONCE){ 
  						taskTypeStr = "一次性任务";
  					}else if(task.getTaskType()==Task.TYPE_DAILY){ 
  						taskTypeStr = "日常任务";
  					}else if(task.getTaskType()==Task.TYPE_LOOP){ 
  						taskTypeStr = "循环任务";
  					}else if(task.getTaskType()==Task.TYPE_RUNNING){
  						taskTypeStr = "跑环任务";
  					}
  				
  				%>
  				<%=taskTypeStr %>
  				<%} %>
  				</td>
  				<td width="160" align="center" style="word-wrap: break-word;">
  				
  				<%
  				if(task != null){ %>
  				<%String preStrs = "无前置任务"; 
			if(task.getPreviousTaskIds() != null){
				StringBuffer sbTemp = new StringBuffer();
				for(int id : task.getPreviousTaskIds()){
					if(tm.getTaskById(id) != null){
						sbTemp.append("<a href='TaskByTaskId.jsp?taskId="+id+"'>"+tm.getTaskById(id).getName()+"</a> ");
					}
				}
				preStrs = sbTemp.toString();
			}
			
			%>
  				<%=preStrs %>
  				<%} %>
  				</td>
			</tr>
		<%} 

	%>

</table>
<%}} %>
</body>
</html>
