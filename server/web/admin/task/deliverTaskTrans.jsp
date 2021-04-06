<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<%@page import="java.util.HashSet"%>
<%@page import="java.util.ArrayList"%>
<%@page import="com.fy.engineserver.newtask.service.DeliverTaskManager"%>
<%@page import="com.fy.engineserver.newtask.DeliverTask"%>
<%@page import="com.fy.engineserver.newtask.NewDeliverTask"%>
<%@page import="java.util.Set"%>
<%@page import="com.fy.engineserver.newtask.Task"%>
<%@page import="com.fy.engineserver.newtask.service.TaskManager"%>
<%@page import="com.fy.engineserver.newtask.NewDeliverTaskManager"%>
<%@page import="com.fy.engineserver.sprite.Player"%>
<%@page import="com.fy.engineserver.society.SocialManager"%>
<%@page import="com.fy.engineserver.society.Relation"%>
<%@page import="com.fy.engineserver.sprite.concrete.GamePlayerManager"%>
<%@page import="java.util.List"%>
<%@ page language="java" contentType="text/html; charset=utf-8"
	pageEncoding="utf-8"%>
<html>
<head>
</head>
<body>
	<%
		String action = request.getParameter("action");
		String playerId = request.getParameter("playerId");
		action = action == null ? "" : action;
		playerId = playerId == null ? "" :playerId;
	%>
	<form action="deliverTaskTrans.jsp" method="get">
		<input type="hidden" name="action" value="transData" /> 玩家ID<input
			type="text" name="playerId" value="<%=playerId%>" /> <input
			type="submit" value="任务数据导入(好友)" />
	</form>
	<br />
	<form action="deliverTaskTrans.jsp" method="get">
		<input type="hidden" name="action" value="transData2" /> 玩家ID<input
			type="text" name="playerId" value="<%=playerId%>" /> <input
			type="submit" value="任务数据导入(自己)" />
	</form>
	<br />
	
	<%
	if("transData".equals(action)) {
		SocialManager socialManager = SocialManager.getInstance();
		//Relation relation = socialManager.getRelationById(Long.parseLong(playerId));
		//List<Long> friendList =relation.getFriendlist();
		NewDeliverTaskManager ndt = NewDeliverTaskManager.getInstance();
		Player player = null;
		player = GamePlayerManager.getInstance().getPlayer(Long.parseLong(playerId));
		ndt.transforOldDeliverData(player);
		out.println("执行代码，");
		//if(friendList != null && friendList.size() > 0){
			//for(Long fId : friendList) {
				//player = GamePlayerManager.getInstance().getPlayer(fId);
				//if(!player.isOnline()) {
					//ndt.transforOldDeliverData(player);
					//out.println("[导入玩家任务数据][" + player.getLogString() + "]<br/>");
				//}
			//}
		//}
	} else if("transData2".equals(action)) {
		Player player = null;
		player = GamePlayerManager.getInstance().getPlayer(Long.parseLong(playerId));
		DeliverTaskManager dtm = DeliverTaskManager.getInstance();
		NewDeliverTaskManager ndtm = NewDeliverTaskManager.getInstance();
		//List<DeliverTask> oldList = dtm.loadAllDeliverTask(player.getId());
		List<DeliverTask> oldList = dtm.em.query(DeliverTask.class, "playerId=?", new Object[]{Long.parseLong(playerId)},"", 1, 2000);
		
		if(oldList == null || oldList.size() <= 0) {
			out.println("数据库查出来数据为空");
			return ;
		}
		TaskManager tm = TaskManager.getInstance();
		List<DeliverTask> nuFrontG = new ArrayList<DeliverTask>();			//没有前置任务的任务组
		Set<Long> deliveredList = new HashSet<Long>();						//已经完成的任务id
		for(DeliverTask dd : oldList) {
			Task task = tm.getTask(dd.getTaskId());
			deliveredList.add(dd.getTaskId());
			if(task != null) {
				if(task.getFrontGroupName() == null || task.getFrontGroupName().isEmpty()) {		//挑出已完成任务重没有前置任务的任务列表
					nuFrontG.add(dd);
				}
			}
		}
		for(DeliverTask dd : oldList) {
			Task task = tm.getTask(dd.getTaskId());
			deliveredList.add(dd.getTaskId());
			if(task != null) {
				if(task.getFrontGroupName() == null || task.getFrontGroupName().isEmpty()) {		//挑出已完成任务重没有前置任务的任务列表
					nuFrontG.add(dd);
				}
			}
		}
		
		for(DeliverTask dt : nuFrontG) {									
			try {
				ndtm.notifyNewDeliverTask(player, new NewDeliverTask(player.getId(), dt.getTaskId()));	//首先将没有前置任务的任务存入新库
				this.transAllDeliverdTask(player, dt.getTaskName(), deliveredList);
			} catch (Exception e) {
				// TODO Auto-generated catch block
				out.println("跑出异常," + e);
			}
		}
		out.println("执行完毕");
	}
	%>
	
	<%!
	private int transAllDeliverdTask(Player p ,String taskName, Set<Long> deliveredList) {
		int num = 0;
		TaskManager tm = TaskManager.getInstance();
		NewDeliverTaskManager ndtm = NewDeliverTaskManager.getInstance();
		Task t = TaskManager.getInstance().getTask(taskName);
		if(t == null) {
			return 0;
		}
		List<Task> nextList = tm.getnextTask(t.getGroupName());
		if(nextList != null && nextList.size() > 0) {
			for(Task nt : nextList) {
				if(deliveredList.contains(nt.getId())) {
					try {
						NewDeliverTask temp = new NewDeliverTask(p.getId(), nt.getId());
						ndtm.notifyNewDeliverTask(p, temp);
						num++;
						this.transAllDeliverdTask(p, nt.getName(), deliveredList);
					} catch (Exception e) {
						// TODO Auto-generated catch block
					}
				}
			}
		}
		return num;
	}
	%>
</body>
</html>
