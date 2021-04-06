<%@page import="com.fy.engineserver.message.GameMessageFactory"%>
<%@page import="com.fy.engineserver.message.NEXT_TASK_OPEN"%>
<%@page import="com.fy.engineserver.newtask.service.TaskConfig"%>
<%@page import="com.fy.engineserver.newtask.Task"%>
<%@page import="com.fy.engineserver.newtask.service.TaskManager"%>
<%@page import="java.util.List"%>
<%@page
	import="com.fy.engineserver.sprite.concrete.GamePlayerManager"%>
<%@page import="com.fy.engineserver.sprite.Player"%>
<%@page import="com.xuanzhi.tools.simplejpa.SimpleEntityManagerFactory"%>
<%@page import="com.fy.engineserver.newtask.DeliverTask"%>
<%@page import="com.xuanzhi.tools.simplejpa.SimpleEntityManager"%>
<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<%
	String playerName = request.getParameter("playerName");
	if (playerName != null) {
		Player player = GamePlayerManager.getInstance().getPlayer(playerName);
		if (player == null) {
			out.print("角色不存在:" + playerName);
			return;
		} else {
			//SimpleEntityManager<DeliverTask> em = DeliverTaskManager.getInstance().em; //SimpleEntityManagerFactory.getSimpleEntityManager(DeliverTask.class);
			//List<DeliverTask> tasks = DeliverTaskManager.getInstance().loadAllDeliverTask(player.getId());
			List<NewDeliverTask> tasks = NewDeliverTaskManager.getInstance().em.query(NewDeliverTask.class, "playerId=?", new Object[]{player.getId()},"", 1, 9999);
			//out.print(NewDeliverTaskManager.getInstance().em.count()+"---"+tasks.size()+"<br>");
			//List<DeliverTask> tasks = DeliverTaskManager.getInstance().em.query(DeliverTask.class, "playerId = ?", new Object[] { player.getId() }, "id desc", 1, 1000);
			if (tasks != null) {
				TaskManager tm = TaskManager.getInstance();
				boolean found = false;
				Task task = null;
				for (NewDeliverTask dt : tasks) {
					Task t = TaskManager.getInstance().getTask(dt.getTaskId());
					if (t != null && t.getShowType() == TaskConfig.TASK_SHOW_TYPE_MAIN) {//是主线,找后面的
						task = t;
						break;
					}
				}
				if(task != null){
					List<Task> nextList = tm.getnextTask(task.getGroupName());
					if (nextList != null && nextList.size() > 0) {
						for (Task tt : nextList) {
							if (TaskManager.workFit(player, tt)) {
								if (!player.getNextCanAcceptTasks().contains(tt.getId()) && player.getTaskEntity(tt.getId()) == null) {
									player.getNextCanAcceptTasks().add(tt.getId());
									player.setDirty(true, "nextCanAcceptTasks");
									found = true;
									out.print("玩家[" + player.getName() + "]完成的最后一个主线是["+task.getName()+"],给他增加可接任务[" + tt.getName() + "]成功");
									if (player.isOnline()) {
										NEXT_TASK_OPEN res = new NEXT_TASK_OPEN(GameMessageFactory.nextSequnceNum(), tt, tt.getTargets(), tt.getPrizes());
										player.addMessageToRightBag(res);
									}
									break;
								}

							}
						}
					}
				}
				if (!found) {
					out.print("未找到可用任务.[" + ((player.getNextCanAcceptTasks().size() > 0) ? "原来就有可接列表" : "无可接列表") + "] [任务数:"+(tasks!=null?tasks.size():"0")+"] [最后完成的主线任务："+(task != null ?task.getId()+"/" +task.getName():"不存在")+"]");
				}
			} else {
				out.print("异常");
			}
		}
	}
%>

<%@page import="com.fy.engineserver.newtask.service.DeliverTaskManager"%>
<%@page import="com.fy.engineserver.newtask.NewDeliverTask"%>
<%@page import="com.fy.engineserver.newtask.NewDeliverTaskManager"%><html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title>Insert title here</title>
</head>
<body>
	<h2>专门修复无主线可接列表</h2>
	<form action="./repairPlayerTask.jsp">
		要修复的玩家名:<input name="playerName" type="text" value="<%=playerName%>">
		<input type="submit" value="提交">
	</form>
</body>
</html>