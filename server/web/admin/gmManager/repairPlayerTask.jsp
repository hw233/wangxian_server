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
	<%@ include file="./inc.jsp" %>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<%
	String playerName = request.getParameter("playerName");
	String pwd = request.getParameter("pwd");
	if (playerName != null) {
		Player player = GamePlayerManager.getInstance().getPlayer(playerName);
		if (player == null) {
			out.print("角色不存在:" + playerName);
			return;
		} else {
			if (!"repaIr_task".equals(pwd)) {
				out.print("密码错误");
				return;
			}
			SimpleEntityManager<DeliverTask> em = SimpleEntityManagerFactory.getSimpleEntityManager(DeliverTask.class);
			List<DeliverTask> tasks = em.query(DeliverTask.class, "playerId = ?", new Object[] { player.getId() }, "id desc", 1, 100);
			if (tasks != null) {
				TaskManager tm = TaskManager.getInstance();
				boolean found = false;
				Task task = null;
				for (DeliverTask dt : tasks) {
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
					out.print("未找到可用任务.[" + ((player.getNextCanAcceptTasks().size() > 0) ? "原来就有可接列表" : "无可接列表") + "]");
				}
			} else {
				out.print("异常");
			}
		}
	}
%>
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title>Insert title here</title>
</head>
<body>
	<h2>专门修复无主线可接列表</h2>
	<form action="./repairPlayerTask.jsp" method="post" style="font-size: 12px;">
		要修复的玩家名:<input name="playerName" type="text" value="<%=playerName%>"><br>
		输入密码:<input name="pwd" type="password"><br>
		<input type="submit" value="提交">
	</form>
</body>
</html>