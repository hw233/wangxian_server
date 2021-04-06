<%@page import="com.fy.engineserver.newtask.NewDeliverTask"%>
<%@page import="com.fy.engineserver.newtask.NewDeliverTaskManager"%>
<%@page import="java.util.List"%>
<%@page
	import="com.fy.engineserver.newtask.service.DeliverTaskManager"%>
<%@page import="com.fy.engineserver.newtask.TaskEntity"%>
<%@page import="com.fy.engineserver.newtask.DeliverTask"%>
<%@page import="com.fy.engineserver.newtask.service.TaskConfig"%>
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
	String startTaskName = request.getParameter("startTaskName");
	String endTaskName = request.getParameter("endTaskName");
	String pwd = request.getParameter("pwd");

	if (playerName != null && !"".equals(playerName)) {
		if ("dodotask".equals(pwd)) {
			Player player = GamePlayerManager.getInstance().getPlayer(playerName);
			if (player != null) {
				Task startTask = TaskManager.getInstance().getTask(startTaskName);
				Task endTask = TaskManager.getInstance().getTask(endTaskName);
				if (startTask == null) {
					out.print("<H1>任务不存在:" + startTaskName + "</H1>");
					return;
				}
				if (endTask == null) {
					out.print("<H1>任务不存在:" + endTaskName + "</H1>");
					return;
				}
				if (endTask.getShowType() != TaskConfig.TASK_SHOW_TYPE_MAIN) {
					out.print("<H1>任务不是主线:" + endTaskName + "</H1>");
					return;
				}
				if (startTask.getShowType() != TaskConfig.TASK_SHOW_TYPE_MAIN) {
					out.print("<H1>任务不是主线:" + startTaskName + "</H1>");
					return;
				}
				String doTaskName = startTaskName;
				boolean hasNext = true;
				int count = 0;
				TaskManager.logger.error("开始执行自动完成任务");
				NewDeliverTaskManager ndtm = NewDeliverTaskManager.getInstance();
				while (hasNext && !doTaskName.equals(endTaskName)) {
					TaskManager.logger.error("当前任务:" + doTaskName);
					Task task = TaskManager.getInstance().getTask(doTaskName);
					if (task == null) {
						hasNext = false;
						TaskManager.logger.warn("任务不存在:" + doTaskName);
						continue;
					}
					TaskEntity entity = new TaskEntity(task, player);
					DeliverTask ddd = new DeliverTask(entity);
					ddd.setDeliverTimes(DeliverTaskManager.deleteFlag);
					DeliverTaskManager.getInstance().notifyNewDeliverTask(player, ddd);
					
					NewDeliverTask newDeliverT = new NewDeliverTask(entity.getPlayerId(), entity.getTaskId());
					ndtm.notifyNewDeliverTask(player, newDeliverT);
					TaskManager.logger.warn("自动完成任务:" + doTaskName);
					count++;
					List<Task> list = TaskManager.getInstance().getnextTask(task.getGroupName());
					if (list != null && list.size() > 0) {
						doTaskName = list.get(0).getName();
						TaskManager.logger.warn("下一个任务:" + doTaskName);
					} else {
						TaskManager.logger.warn("后续任务不存在:" + doTaskName);
						hasNext = false;
					}
				}
				out.print("自动完成了任务" + count + "个<BR/>");

			} else {
				out.print("<H2>角色不存在:" + playerName + "<H2>");
			}
		} else {
			out.print("<H2>密码错误<H2>");
		}
	}
%>
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title>快速做任务</title>
</head>
<body>

	<form action="./fastTask.jsp">
		角色名:<input type="text" name="playerName">
		<HR>
		开始任务:<input type="text" name="startTaskName"><br>
		结束任务(不完成):<input type="text" name="endTaskName"><br>
		提交密码:<input type="password" name="pwd"><br> <input
			type="submit" value="提交">
	</form>

</body>
</html>