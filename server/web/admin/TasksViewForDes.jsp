<%@ page language="java" contentType="text/html; charset=utf-8"
    pageEncoding="utf-8"%>
<%@ page import="com.fy.engineserver.datasource.props.*,com.google.gson.*,java.util.*,com.fy.engineserver.task.*,com.fy.engineserver.datasource.career.*,com.fy.engineserver.core.*,
com.fy.engineserver.sprite.monster.*,com.fy.engineserver.sprite.npc.*" %>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<%@include file="IPManager.jsp" %><html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8">
<title>任务</title>
<% TaskManager tm = TaskManager.getInstance();
String[] dependencys = null;
if(tm != null){
	dependencys = tm.getAllDependencys();
}
String dependency = request.getParameter("dependency");
if(dependency == null){
	dependency = "全部";
}
%>
<link rel="stylesheet" type="text/css" href="../css/common.css" />
<style type="text/css">
.tablestyle1{
width:100%;
border:0px solid #69c;
border-top:1px solid #69c;
border-right:1px solid #69c;
border-bottom:1px solid #69c;
border-left:1px solid #69c;
border-collapse: collapse;
}
.tablestyle2{
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
.tdtable{
 padding: 0px;
 border-top:0px solid #69c;
border-right:0px solid #69c;
border-bottom:0px solid #69c;
border-left:0px solid #69c;
}
.width{
width:100px;
}
.align{
text-align: left;
}
.borderbottom{
border-bottom:1px dotted #69c;
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
<script type="text/javascript">
function selectdependencys(){
	var selected = document.getElementById("selectdependedcy");
	window.location.href="TasksViewForDes.jsp?dependency="+selected.value;
}
</script>
</head>
<body>
<a href="TasksView.jsp">刷新此页面</a>
<br>
<br>
<input type="hidden" id="dependency">
<select id="selectdependedcy" onchange="javascript:selectdependencys();">
<option value="全部" <%=(dependency != null? (dependency.equals("全部") ? "selected":""):"") %>><%="全部" %>
<%if(dependencys != null){ 
	for(int i = 0; i < dependencys.length; i++){
		String depStr = dependencys[i];
		if(depStr != null){
			%>
			<option value="<%=depStr %>" <%=(dependency != null? (dependency.equals(depStr) ? "selected":""):"") %>><%=depStr %>
			<%
		}
	}
}
%>
</select>
<div id=pltsTipLayer style="display: none;position: absolute; z-index:10001"></div>
<%if(dependencys != null){

if(dependency != null){
	Task[] tasks = null;
	if(tm != null){
		if("全部".equals(dependency)){
			tasks = tm.getAllTasks();
		}else{
			tasks = tm.getAllTasksOnDependency(dependency);
		}
	}
	if(tasks != null){
			ArticleManager am = ArticleManager.getInstance();
			MonsterManager mmm = MemoryMonsterManager.getMonsterManager();
			GameManager gm = GameManager.getInstance();
%>
<font color="red"><b><%=dependency %></b></font>中的所有任务<br><br>
<table class="tablestyle1">
<tr align="center">
	<td class="lefttd" width="160" align="center" style="word-wrap: break-word;">ID</td>
	<td class="lefttd" width="160" align="center" style="word-wrap: break-word;">任务名</td>
	<td width="160" align="center" style="word-wrap: break-word;">简要描述</td>
	<td width="160" align="center" style="word-wrap: break-word;">任务详细描述</td>
	<td width="160" align="center" style="word-wrap: break-word;">任务未完成描述</td>
	<td width="160" align="center" style="word-wrap: break-word;">任务完成前描述</td>
	<td width="160" align="center" style="word-wrap: break-word;">完成任务描述</td>
</tr>
<%
		int alSize = tasks.length;
		for(int jj = 0; jj < alSize; jj++){
  			%>
  			<tr align="center">
  			<%Task task = tasks[jj];
  			%>
  			<td class="lefttd" width="160" align="center" style="word-wrap: break-word;">
  				<%
  				if(task != null){ %>
  				<a href="TaskByTaskId.jsp?taskId=<%=task.getId() %>" ><%=task.getId() %></a>
  				<%} %>
  				</td>
  				<td class="lefttd" width="160" align="center" style="word-wrap: break-word;">
  				<%
  				if(task != null){ %>
  				<a href="TaskByTaskId.jsp?taskId=<%=task.getId() %>" ><%=task.getName() %></a>
  				<%} %>
  				</td>
  				<td width="160" align="center" style="word-wrap: break-word;">
  				<%
  				if(task != null){ %>
  				<%=("".equals(task.getTaskSummary())? "<h1><font color='red'>描述为空</font></h1>":task.getTaskSummary()) %>
  				<%} %>
  				</td>
  				<td width="160" align="center" style="word-wrap: break-word;">
  				<%
  				if(task != null){ %>
  				<%=("".equals(task.getTaskDescription())?"<h1><font color='red'>描述为空</font></h1>":task.getTaskDescription()) %>
  				<%} %>
  				</td>
  				<td width="160" align="center" style="word-wrap: break-word;">
  				<%
  				if(task != null){ %>
  				<%=("".equals(task.getTaskNotFinishDescription())?"<h1><font color='red'>描述为空</font></h1>":task.getTaskNotFinishDescription()) %>
  				<%} %>
  				</td>
  				<td width="160" align="center" style="word-wrap: break-word;">
  				<%
  				if(task != null){ %>
  				<%=("".equals(task.getTaskBeforeFinishDescription())?"<h1><font color='red'>描述为空</font></h1>":task.getTaskBeforeFinishDescription()) %>
  				<%} %>
  				</td>
  				<td width="160" align="center" style="word-wrap: break-word;">
  				<%
  				if(task != null){ %>
  				<%=("".equals(task.getTaskFinishDescription())?"<h1><font color='red'>描述为空</font></h1>":task.getTaskFinishDescription()) %>
  				<%} %>
  				</td>
			</tr>
		<%}
	%>
</table>
<%}} %>
<%} %>
</body>
<script type="text/javascript" src="../js/title.js"></script>
</html>
