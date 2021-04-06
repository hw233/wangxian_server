<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<%@page import="com.fy.engineserver.newtask.service.DeliverTaskManager"%>
<%@page import="com.fy.engineserver.newtask.DeliverTask"%>
<%@page import="java.util.List"%>
<%@page import="com.fy.engineserver.newtask.service.TaskConfig"%>
<%@page import="com.fy.engineserver.newtask.NewDeliverTaskManager"%>
<%@page import="com.fy.engineserver.newtask.service.TaskManager"%>
<%@page import="com.fy.engineserver.newtask.Task"%>
<%@page import="com.fy.engineserver.newtask.NewDeliverTask"%>
<%@page import="com.fy.engineserver.sprite.concrete.GamePlayerManager"%>
<%@page import="com.fy.engineserver.sprite.Player"%>
<%@ page language="java" contentType="text/html; charset=utf-8"
	pageEncoding="utf-8"%>

<head>
</head>
<body>
	<%
		String action = request.getParameter("action");
		String playerId = request.getParameter("playerId");
		String taskId = request.getParameter("taskId");
		action = action == null ? "" : action;
		playerId = playerId == null ? "" :playerId;
		taskId = taskId == null ? "" :taskId;
	%>
	<form action="PlayerMainTask4mysql.jsp" method="post">
		<input type="hidden" name="action" value="finishMainTask" />
		角色id:<input type="text" name="playerId" value="<%=playerId%>" /> 
		结束任务名(完成):<input type="text" name="taskId" value="<%=taskId%>" /> 
		<input type="submit" value="完成主线任务" />
	</form>
	<br />
	
	<%
	if("finishMainTask".equals(action)) {
		Player p = GamePlayerManager.getInstance().getPlayer(Long.parseLong(playerId));
		if(p != null) {
			Task task = TaskManager.getInstance().getTask(taskId);
			if(task == null) {
				out.println("[任务 "+taskId+" 不存在!]<br>");
				return;
			}
			if(task.getMaxGradeLimit() > p.getSoulLevel()) {
				out.println("[任务 "+taskId+" 等级超过玩家等级!]<br>");
				return;
			}
			if(task.getType() != TaskConfig.TASK_TYPE_ONCE) {
				out.println("[任务 "+taskId+" 不是单次任务！]<br>");
				return;
			}
			DeliverTaskManager m = DeliverTaskManager.getInstance();
			DeliverTask dt22 = new DeliverTask();
			dt22.setId(TaskManager.deliverTaskEm.nextId());
			dt22.setPlayerId(p.getId());
			dt22.setTaskName(task.getName());
			dt22.setTaskId(task.getId());
			dt22.setDeliverTimes(1);
			m.notifyNewDeliverTask(p, dt22);
		//	gropName = task.getFrontGroupName();
			List<Task> list = TaskManager.getInstance().getTaskGroupByGroupName(task.getFrontGroupName());
			while (true) {
				String gropName = "";
				for (int i=0; i<list.size(); i++) {
					Task t = list.get(i);
					if (m.isDelived(p, t)) {
						continue;
					}
					DeliverTask dt = new DeliverTask();
					out.println(t.getId() + " = " + t.getName() + " = " + t.getFrontGroupName() + "<br>");
					dt.setId(TaskManager.deliverTaskEm.nextId());
					dt.setPlayerId(p.getId());
					dt.setTaskName(t.getName());
					dt.setTaskId(t.getId());
					dt.setDeliverTimes(1);
					m.notifyNewDeliverTask(p, dt);
					gropName = t.getFrontGroupName();
				}
				list = TaskManager.getInstance().getTaskGroupByGroupName(gropName);
				if (list == null || list.size() <= 0) {
					break;
				}
			}
			List<Task> list2 = TaskManager.getInstance().getnextTask(task.getGroupName());
			String doTaskName = "";
			out.println("[完成任务 "+taskId+" ]<br>");
			if (list2 != null && list2.size() > 0) {
				doTaskName = list2.get(0).getName();
				out.println("[下一个可接任务为: "+doTaskName+" ] [ 接取地点 :" + list2.get(0).getStartMap() + " && npc :" + list2.get(0).getStartNpc());
			}
		}
	}
	%>
</body>
</html>
