<%@ page language="java" contentType="text/html; charset=utf-8"
    pageEncoding="utf-8"%>
<%@ page import="com.fy.engineserver.datasource.props.*,com.google.gson.*,java.util.*,com.fy.engineserver.task.*,com.fy.engineserver.datasource.career.*,com.fy.engineserver.menu.question.*" %>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<%@include file="IPManager.jsp" %><html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8">
<title>任务</title>
<style type="text/css">
<!--
.tablestyle{
width:96%;
border:0px solid #69c;
border-top:1px solid #69c;
border-right:1px solid #69c;
border-bottom:1px solid #69c;
border-left:1px solid #69c;
border-collapse: collapse;
}
.tablestyle1{
 margin:0px;
 padding: 0px;
width:100%;
height:100%;
border-top:0px solid #69c;
border-right:0px solid #69c;
border-bottom:0px solid #69c;
border-left:0px solid #69c;
border-collapse: collapse;
}
.trcolor1{
background-color: #C2CAF5;
}
.tdcolor1{
border:1px solid #69c;
background-color: #C2CAF5;
}
.trcolor2{
}
.tdcolor2{
border:1px solid #69c;
}
.tdtable{
 padding: 0px;
}
.td{
border-top:0px solid #69c;
border-left:0px solid #69c;
}
.tdwidth1{
width:25%
}
.tdwidth2{
width:33%
}
.bottomtd{border-bottom:0px solid #69c;}
.righttd{border-right:0px solid #69c;}

-->


</style>
</head>
<body>

<br>
<br>
<%

String taskId = "1954920532";

TaskManager tm = TaskManager.getInstance();
Task task = null;
if(tm != null){
	task = tm.getTaskById(Integer.parseInt(taskId));
	if(task != null){
		TaskGoal goals[] = task.getGoals();
		goals[0].setMonsterNames(new String[]{"苗疆傀儡人"});
		goals[0].setFlopRate(new int[]{70});
		out.println("已设置完成，人物名称:" + task.getName());
	}
}
%>
</body>
</html>
