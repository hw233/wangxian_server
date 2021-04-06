<%@page import="com.fy.engineserver.newtask.targets.TaskTarget"%>
<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@ include file="./inc.jsp"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<%
	String palyerName = request.getParameter("palyerName");
	Player player = null;
	if (palyerName != null && !palyerName.isEmpty()) {
		player = PlayerManager.getInstance().getPlayer(palyerName);
		if (player == null) {
			out.print("角色不存在");
			return;
		}
	} else {
		palyerName = "";
	}
%>
<html>
<head>
<link rel="stylesheet" type="text/css" href="../task/css/main.css">
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title>角色【<%=palyerName%>】的任务</title>
</head>
<body style="font-size: 12px">
	<form action="./PlayerTaskList.jsp" method="post">
		角色名: <input name="palyerName" value="<%=palyerName%>" type="text">
		<input name="OK" value="提交" type="submit">
	</form>
	<hr>
	已接任务:
	<BR />
	<table bgcolor="#88F491" border="1">
		<tr style="font-family: fantasy;">
			<td>ID</td>
			<td>名字</td>
			<td>类型</td>
			<td>等级</td>
			<td>所属任务组</td>
			<td>任务状态</td>
			<td>总次数</td>
		</tr>
		<%
			if (player != null) {
				long[] ids = TaskEntityManager.em.queryIds(TaskEntity.class," playerId = "+player.getId());
				for (int i = 0; i < ids.length; i++) {
					TaskEntity taskEntity = TaskEntityManager.em.find(ids[i]);
					if(taskEntity == null){
						out.print("<tr>数据库中没有发现任务:+"+ids[i]+"</tr>");
						continue;
					}
					//类型: 主线0支线1境界2日常3其它4
					if(taskEntity.getTask() == null){
						out.print("<tr>没有发现任务:+"+ids[i]+"---task:"+taskEntity.getTaskId()+"/"+taskEntity.getTaskName()
								+"--类型:"+taskEntity.getShowType()+"--状态:"+taskEntity.getStatus()+""+"<br></tr>");
						continue;
					}
						%>
						<tr>
							<td><%=taskEntity.getTask().getId()%></td>
							<td><a
								href="./PlayerTask.jsp?playerId=<%=player.getId()%>&taskId=<%=taskEntity.getTaskId()%>"><%=taskEntity.getTask().getName()%></a>
							</td>
							<td><%=taskEntity.getTask().getShowType()%></td>
							<td><%=taskEntity.getTask().getGrade()%></td>
							<td><%=taskEntity.getTask().getGroupName()%></td>
							<td><%=TaskConfig.taskStatus[taskEntity.getStatus()]%></td>
							<td><%=taskEntity.getTotalDeliverTimes()%></td>
						</tr>
						<%
				}
		%>
		可接任务:
			<BR/>
		<%
		for (int i = 0; i < player.getNextCanAcceptTasks().size();i++){
			long id = player.getNextCanAcceptTasks().get(i);
			Task task = TaskManager.getInstance().getTask(id);
			if (task != null) {
				out.print("<tr>"+task.getName() + ",类型:" + task.getShowType() + ",等级:" + task.getGrade() + ",</tr><BR/>") ;
			} else {
				out.print("任务不存在:" + id + "<BR/>");
			}
		}
			}
		%>
	</table>
	<hr>
</body>
</html>