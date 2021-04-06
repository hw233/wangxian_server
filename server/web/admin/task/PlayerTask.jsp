<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@ include file="./inc.jsp"%>
<%
	String playerIdStr = request.getParameter("playerId");
	String taskIdStr = request.getParameter("taskId");
	long playerId = -1;
	long taskId = -1;
	System.out.print("playerIdStr=" + playerIdStr + ",taskIdStr="+taskIdStr);
	if (taskIdStr == null || taskIdStr.isEmpty() || taskIdStr == null || taskIdStr.isEmpty()) {
		out.print("输入错误 TASKID=[" + taskIdStr + "]playerID=[" + playerIdStr + "]");
		return;
	}
	playerId = Long.valueOf(playerIdStr);
	taskId = Long.valueOf(taskIdStr);
	if (playerId == -1 || taskId == -1) {
		out.print("输入错误 TASKID=[" + taskIdStr + "]playerID=[" + playerIdStr + "]");
		return;
	}

	Player player = PlayerManager.getInstance().getPlayer(playerId);
	Task task = TaskManager.getInstance().getTask(taskId);

	if (player == null || task == null) {
		out.print("所选的记录不存在");
		return;
	}
	TaskEntity entity = player.getTaskEntity(task.getId());
	if (entity == null) {
		out.print("角色没这个任务");
		return;
	}
%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<link rel="stylesheet" type="text/css" href="../task/css/main.css">
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title>角色[<%=player.getName()%>]的任务[<%=task.getName()%>]</title>
</head>
<body>
	<table border="1">
		<tr class="title">
			<td>任务ID</td>
			<td>任务名</td>
			<td>任务类型</td>
			<td>任务等级</td>
		</tr>
		<tr>
			<td><%=task.getId()%></td>
			<td><%=task.getName() + "/" + entity.getTaskName()%></td>
			<td><%=task.getShowType()%></td>
			<td><%=task.getGrade()%></td>
		</tr>
		<tr class="title">
			<td>第一次接取时间</td>
			<td>第一次完成时间</td>
			<td>体力限制(接取)</td>
			<td>体力消耗</td>
		</tr>
		<tr>
			<td><%=timeToString(entity.getFirstGetTime())%></td>
			<td><%=timeToString(entity.getFirstDeliverTime())%></td>
			<td><%=task.getThewLimit()%></td>
			<td><%=task.getThewCost()%></td>
		</tr>
		<tr class="title">
			<td>任务状态</td>
			<td>完成次数</td>
			<td>任务得分</td>
			<td>最后一次完成时间</td>
		</tr>
		<tr>
			<td><%=entity.getStatus()%></td>
			<td><%=entity.getTotalDeliverTimes()%></td>
			<td><%=entity.getScore()%></td>
			<td><%=timeToString(entity.getLastDeliverTime())%></td>
		</tr>
		<tr class="head">
			<td colspan="4">目标完成情况</td>
		</tr>
			<tr>
				<td>类型</td>
				<td>目标(个)</td>
				<td>完成度</td>
				<td>是否完成</td>
			</tr>
		<%
			for (int i = 0; i < task.getTargets().length; i++) {
				TaskTarget tt = task.getTargets()[i];
				int completedNum = entity.getCompleted()[i];
		%>
			<tr>
				<td><%=tt.getTargetType().getName() %></td>
				<td><%=tt.getTargetName().length %></td>
				<td><%=entity.getCompleted()[i] + "/" + entity.getTaskDemand()[i] %></td>
				<td><%=entity.isComplete(i) %></td>
			</tr>
		<%
			}
		%>
	</table>
</body>
</html>