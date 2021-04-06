<%@page import="com.fy.engineserver.newtask.targets.TaskTarget"%>
<%@page import="java.util.List"%>
<%@page import="com.fy.engineserver.newtask.service.TaskConfig"%>
<%@page
	import="com.fy.engineserver.newtask.targets.TaskTargetOfMonster"%>
<%@page import="com.fy.engineserver.newtask.service.TaskManager"%>
<%@page import="com.fy.engineserver.newtask.Task"%>
<%@page
	import="com.fy.engineserver.newtask.service.TaskConfig.TargetType"%>
<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<%
	//任务ID 1006
	//桃源仙境
	//凋零鬼
	long taskId = 2337;
	String oldTargetName = "沧浪魅惑妖";
	String newTargetName = "极恶万鬼王";
	int targetNum = 1;
	Task task = TaskManager.getInstance().getTask(taskId);
	boolean found = false;
	if (task != null) {
		List<TaskTarget> monsters = task.getTargetByType(TargetType.MONSTER);
		if (monsters == null || monsters.size() == 0) {
			out.print("[任务目标不存在:" + taskId + "]");
		} else {
			int index = -1;
			for (TaskTarget tt : monsters) {
				if (found) {
					break;
				}
				if (tt != null && tt instanceof TaskTargetOfMonster) {
					String[] names = tt.getTargetName();
					for (int i = 0; i < names.length; i++) {
						String monsterName = names[i];
						if (monsterName.equals(oldTargetName)) {
							found = true;
						}
						break;

					}
				}
			}
			if (found) {
				task.getTargets()[0] = new TaskTargetOfMonster(new String[] { newTargetName }, -1, targetNum);
				out.print("OK" + task.getTargets()[0].toString());
			} else {
				out.print("没找到具体目标");
			}
		}
	} else {
		out.print("[任务不存在:" + taskId + "]");
	}
%>
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title>Insert title here</title>
</head>
<body>
</body>
</html>