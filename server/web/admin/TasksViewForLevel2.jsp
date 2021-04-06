<%@ page language="java" contentType="text/html; charset=utf-8" pageEncoding="utf-8"%>
<%@ page import="com.fy.engineserver.datasource.props.*,java.util.*,com.fy.engineserver.task.*,com.fy.engineserver.datasource.career.*,com.fy.engineserver.core.*" %>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<%@include file="IPManager.jsp" %><html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8">
<title>任务</title>
<% 
	ExperienceManager em = ExperienceManager.getInstance();
	 
	TaskManager tm = TaskManager.getInstance();
	
	//各个级别的任务，分主线，支线，日常，循环，跑环
	Task tasks[][][] = new Task[em.maxLevel][][];
	for(int i = 0 ; i < tasks.length ; i++){
		tasks[i] = new Task[5][];
		for(int j = 0 ; j < tasks[i].length ; j++){
			//tasks[i][j] = new Task[];
			ArrayList<Task> al = new ArrayList<Task>();
			Task tt[] = tm.getAllTasks();
			for(int x = 0 ; x < tt.length ; x++){
				if(tt[x].getMinPlayerLevel() == i+1){
					String dd = tt[x].getDependency();
					if(j == 0 && tt[x].getTaskType() == Task.TYPE_ONCE && dd.indexOf("[支]") == -1){
						al.add(tt[x]);
					}else if(j == 1 && tt[x].getTaskType() == Task.TYPE_ONCE && dd.indexOf("[支]")>=0){
						al.add(tt[x]);
					}else if(j == 2 && tt[x].getTaskType() == Task.TYPE_DAILY){
						al.add(tt[x]);
					}else if(j == 3 && tt[x].getTaskType() == Task.TYPE_LOOP){
						al.add(tt[x]);
					}else if(j == 4 && tt[x].getTaskType() == Task.TYPE_RUNNING){
						al.add(tt[x]);
					}
				}
			}
			tasks[i][j] = al.toArray(new Task[0]);
		}
	}
	
	
	
	int[] 紫微任务奖励 = new int[em.maxLevel];
	int[] 紫微任务数量 = new int[em.maxLevel];
	int[] 日月任务奖励 = new int[em.maxLevel];
	int[] 日月任务数量 = new int[em.maxLevel];
	int[] 中立任务奖励 = new int[em.maxLevel];
	int[] 中立任务数量 = new int[em.maxLevel];
	
	int[] 紫微日常跑环奖励 = new int[em.maxLevel];
	int[] 紫微日常跑环数量 = new int[em.maxLevel];
	int[] 日月日常跑环奖励 = new int[em.maxLevel];
	int[] 日月日常跑环数量 = new int[em.maxLevel];
	int[] 中立日常跑环奖励 = new int[em.maxLevel];
	int[] 中立日常跑环数量 = new int[em.maxLevel];
	
	for(int i = 0 ;i < tasks.length ; i++){
		for(int j = 0 ; j < tasks[i].length ; j++){
				for(int k = 0 ; k < tasks[i][j].length ; k++){
					Task t = tasks[i][j][k];
					Task tt = null;
					if(t.getCareerLimits() != null && t.getCareerLimits().length > 0){
						byte cc[] = t.getCareerLimits();
						boolean b = false;
						for(int ii = 0 ; ii < cc.length ; ii++){
							if(cc[ii] == 0){
								b = true;
							}
						}
						if(b == true){
							tt = t;
						}
					}else{
						tt = t;
					}
					
					if(tt  != null && tt.isGangTask() == false){
						if(j == 0 || j == 1){
							if(tt.getCampLimit() == 1){
								紫微任务奖励[i] += tt.getRewardExp();
								紫微任务数量[i] ++;
							}else if(tt.getCampLimit() == 2){
								日月任务奖励[i] += tt.getRewardExp();
								日月任务数量[i] ++;
							}else{
								中立任务奖励[i] += tt.getRewardExp();
								中立任务数量[i] ++;
							}
						}else{
							if(tt.getCampLimit() == 1){
								紫微日常跑环奖励[i] += tt.getRewardExp();
								紫微日常跑环数量[i] ++;
							}else if(tt.getCampLimit() == 2){
								日月日常跑环奖励[i] += tt.getRewardExp();
								日月日常跑环数量[i] ++;
							}else{
								中立日常跑环奖励[i] += tt.getRewardExp();
								中立日常跑环数量[i] ++;
							}
						}
					}
				}
			
		}
	}
	
%>
</head>
<body>
<h2>任务等级分布情况，任务数量：<%=tm.getAllTasks().length %></h2>
<a href="TasksViewForLevel2.jsp">刷新此页面</a>
<br>
<br>
<table border="0" cellpadding="0" cellspacing="1" width="100%" bgcolor="#000000" align="center">
<tr bgcolor="#C2CAF5" align="center">
<td rowspan=2>等级</td>
<td rowspan=2>升级经验</td>
<td colspan=4>紫微</td>
<td colspan=4>日月</td>
<td colspan=4>中立</td>
</tr>
<tr bgcolor="#C2CAF5" align="center">
<td>任务经验</td>
<td>任务数量</td>
<td>日跑经验</td>
<td>日跑数量</td>
<td>任务经验</td>
<td>任务数量</td>
<td>日跑经验</td>
<td>日跑数量</td>
<td>任务经验</td>
<td>任务数量</td>
<td>日跑经验</td>
<td>日跑数量</td>

</tr>

<%
	for(int i = 0 ; i < em.maxLevel ; i++){
		out.println("<tr bgcolor='#FFFFFF' align='center'>");
		out.println("<td>"+(i+1)+"</td>");
		out.println("<td>"+em.maxExpOfLevel[i+1]+"</td>");
		
		out.println("<td>"+紫微任务奖励[i]+"</td>");
		out.println("<td>"+紫微任务数量[i]+"</td>");
		out.println("<td>"+紫微日常跑环奖励[i]+"</td>");
		out.println("<td>"+紫微日常跑环数量[i]+"</td>");
		
		
		out.println("<td>"+日月任务奖励[i]+"</td>");
		out.println("<td>"+日月任务数量[i]+"</td>");
		out.println("<td>"+日月日常跑环奖励[i]+"</td>");
		out.println("<td>"+日月日常跑环数量[i]+"</td>");
		
		
		out.println("<td>"+中立任务奖励[i]+"</td>");
		out.println("<td>"+中立任务数量[i]+"</td>");
		out.println("<td>"+中立日常跑环奖励[i]+"</td>");
		out.println("<td>"+中立日常跑环数量[i]+"</td>");
		
		
		out.println("</tr>");
	}
%>
</table>

</body>
</html>
