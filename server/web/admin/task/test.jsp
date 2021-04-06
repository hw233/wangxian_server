<%@page import="java.util.HashMap"%>
<%@page import="com.fy.engineserver.newtask.service.TaskSubSystem"%>
<%@page import="com.fy.engineserver.util.CompoundReturn"%>
<%@page import="com.fy.engineserver.newtask.service.TaskManager"%>
<%@page import="com.fy.engineserver.newtask.Task"%>
<%@page
	import="com.fy.engineserver.sprite.concrete.GamePlayerManager"%>
<%@page import="com.fy.engineserver.sprite.Player"%>
<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<%
	String playerName = request.getParameter("playerName");
	String taskName = request.getParameter("taskName");
	String op = request.getParameter("op");
	if (playerName != null) {
		Player player = GamePlayerManager.getInstance().getPlayer(playerName);
		if(taskName != null){
			Task task = TaskManager.getInstance().getTask(taskName);
	
			if (player != null && task != null) {
				CompoundReturn cr = player.takeOneTask(task, false, null);
				out.print("能否接取:" + (cr.getBooleanValue() ? "<font color='green'>可接取</font>" : "<font color='red'>不能接取</font>") + "<BR/>");
				out.print("返回int:" + cr.getIntValue() + "<BR/>");
				out.print("返回原因:<font color='red'>" + TaskSubSystem.getInstance().getInfo(cr.getIntValue()) + "</font><BR/>");
			} else {
				if ("1".equals(op)) {
					out.print("角色:" + player);
					out.print("任务:" + task);
				}
			}
		}
		if("2".equals(op)){
			boolean find = false;
			HashMap<Long, Task> taskIdMap = TaskManager.getInstance().getTaskIdMap();
			for(Task task:taskIdMap.values()){
				if (player != null && task.getShowType()==(byte)0) {
					CompoundReturn cr = player.takeOneTask(task, false, null);
					if(cr.getBooleanValue()){
						find = true;
						out.print("可接取任务名:"+task.getName()+", 任务等级:"+task.getGrade()+", 任务接取npc:"+task.getStartNpc()+", 接取地图:"+task.getStartMap()+"<br>");
						return;
					}else{
						continue;
					}
				} 
			}
			if(!find){
				out.print("未找到可接取任务，清查看玩家是否等级不足，不能接取下一级任务");
			}
		}
	} else {
		playerName = "";
		taskName = "";
	}
%>
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title>Insert title here</title>
</head>
<body>
	<form action="./test.jsp" method="post">
		<span>角色名:<input name="playerName" type="text"
			value="<%=playerName%>"> </span> <span>任务名:<input
			name="taskName" type="text" value="<%=taskName%>"> </span> 
			<span><input type="hidden" name="op" value="1"> <input type="submit" value="测试"> </span>
	</form>
	
	<form action="./test.jsp" method="post">
		<span>角色名:<input name="playerName" type="text"
			value="<%=playerName%>"> </span>  
			<span><input type="hidden" name="op" value="2"> <input type="submit" value="查询可接主线任务"> </span>
	</form>
</body>
</html>