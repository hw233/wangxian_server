<%@page import="com.fy.engineserver.platform.PlatformManager.Platform"%>
<%@page import="com.fy.engineserver.platform.PlatformManager"%>
<%@page import="com.fy.engineserver.newtask.targets.TaskTargetOfTalkToNPC"%>
<%@page import="com.fy.engineserver.newtask.targets.TaskTarget"%>
<%@page import="com.fy.engineserver.newtask.Task"%>
<%@page import="com.fy.engineserver.newtask.service.TaskManager"%>
<%@ page language="java" contentType="text/html; charset=utf-8"
	pageEncoding="utf-8"%>
<%@page import="java.util.*"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
	<head>
		<meta http-equiv="Content-Type"
			content="text/html; charset=utf-8">
		<title>修改任务目标</title>
		<link rel="stylesheet" href="gm/style.css" />
		<script type="text/javascript">
	
</script>
	</head>
	<body>
		<br>
		<%
		long taskId = 100561;
		String newTaskTargetNpc [] = {"帝释天"};
		String newTaskTargetMap [] = {"沧浪水"};
		int targetNum = 1;
		if(!PlatformManager.getInstance().isPlatformOf(Platform.官方,Platform.腾讯)){
			out.print("平台不符");
			return;
		}
		Task task = TaskManager.getInstance().getTask(taskId);
		boolean found = false;
		if (task != null) {
			TaskTarget[] targets = task.getTargets();
			if (targets == null || targets.length == 0) {
				out.print("[任务目标不存在:" + taskId + "]");
			} else {
				for (TaskTarget tt : targets) { 
					if (tt != null && tt instanceof TaskTargetOfTalkToNPC) {
						out.print("修改前："+task.getName()+"--"+task.getId()+"--交任务NPC："+Arrays.toString(tt.getTargetName())+"--交任务地图："+Arrays.toString(tt.getMapName())+"--<br>");
						tt.setTargetName(newTaskTargetNpc);
						tt.setMapName(newTaskTargetMap);
						out.print("修改后："+task.getName()+"--"+task.getId()+"--交任务NPC："+Arrays.toString(tt.getTargetName())+"--交任务地图："+Arrays.toString(tt.getMapName())+"--<br>");
					}
				}
			}
		} else {
			out.print("[任务不存在:" + taskId + "]");
		}
		
		
		%>

	</body>
</html>
