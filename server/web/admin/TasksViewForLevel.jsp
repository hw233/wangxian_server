<%@ page language="java" contentType="text/html; charset=utf-8" pageEncoding="utf-8"%>
<%@ page import="com.fy.engineserver.datasource.props.*,java.util.*,com.fy.engineserver.task.*,com.fy.engineserver.datasource.career.*,com.fy.engineserver.core.*" %>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<%@include file="IPManager.jsp" %><html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8">
<title>任务</title>
<% 
	TaskManager tm = TaskManager.getInstance();
	Task tasks[][][] = new Task[50][][];
	for(int i = 0 ; i < tasks.length ; i++){
		tasks[i] = new Task[5][];
		for(int j = 0 ; j < tasks[i].length ; j++){
			//tasks[i][j] = new Task[];
			ArrayList<Task> al = new ArrayList<Task>();
			Task tt[] = tm.getAllTasks();
			for(int x = 0 ; x < tt.length ; x++){
				if(tt[x].getTaskLevel() == i+1){
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
%>
</head>
<body>
<h2>任务等级分布情况，任务数量：<%=tm.getAllTasks().length %></h2>
<a href="TasksViewForLevel.jsp">刷新此页面</a>
<br>
<br>
<table border="0" cellpadding="0" cellspacing="1" width="100%" bgcolor="#000000" align="center">
<tr bgcolor="#C2CAF5" align="center">
<td>等级</td>
<td>紫微数量经验</td>
<td>日月数量经验</td>
<td>中立数量经验</td>
<td width='60'>类型</td>
<td>紫微任务数量</td>
<td>紫微任务经验</td>
<td>紫微任务金钱</td>
<td>日月任务数量</td>
<td>日月任务经验</td>
<td>日月任务金钱</td>
<td>中立任务数量</td>
<td>中立任务经验</td>
<td>中立任务金钱</td>
</tr>
<% 
	String NAMES[] = new String[]{"主线","支线","日常","循环","跑环"};
	for(int i = 0 ;i < tasks.length ; i++){
		for(int j = 0 ; j < tasks[i].length ; j++){
			int count = 0;
			int money = 0;
			int exp = 0;
			int count1 = 0;
			int money1 = 0;
			int exp1 = 0;
			int count2 = 0;
			int money2 = 0;
			int exp2 = 0;
			for(int k = 0 ; k < tasks[i][j].length ; k++){
				Task t = tasks[i][j][k];
				if(t.getCampLimit() == 0){
					count++;
					money += t.getRewardMoney();
					exp += t.getRewardExp();
				}
				if(t.getCampLimit() == 1){
					count1++;
					money1 += t.getRewardMoney();
					exp1 += t.getRewardExp();
				}
				if(t.getCampLimit() == 2){
					count2++;
					money2 += t.getRewardMoney();
					exp2 += t.getRewardExp();
				}
			}
			
			out.println("<tr bgcolor='#FFFFFF' align='center'>");
			if(j == 0){
				out.println("<td rowspan='"+tasks[i].length+"'>"+(i+1)+"级</td>");
				int _exp = 0;
				int _exp1 = 0;
				int _exp2 = 0;
				int _count = 0;
				int _count1 = 0;
				int _count2 = 0;
				for(int jj = 0 ; jj < tasks[i].length ; jj++){
					for(int kk = 0 ; kk < tasks[i][jj].length ; kk++){
						Task t = tasks[i][jj][kk];
						if(t.getCampLimit() == 0){
							_exp += t.getRewardExp();
							_count++;
						}
						if(t.getCampLimit() == 1){
							_exp1 += t.getRewardExp();
							_count1++;
						}
						if(t.getCampLimit() == 2){
							_exp2 += t.getRewardExp();
							_count2++;
						}
					}
				}
				
				out.println("<td rowspan='"+tasks[i].length+"'>"+_count1+"/"+_exp1+"</td>");
				out.println("<td rowspan='"+tasks[i].length+"'>"+_count2+"/"+_exp2+"</td>");
				out.println("<td rowspan='"+tasks[i].length+"'>"+_count+"/"+_exp+"</td>");
			}
			out.println("<td>"+NAMES[j]+"</td>");
			
			out.println("<td>"+count1+"</td>");
			out.println("<td>"+exp1+"</td>");
			out.println("<td>"+money1+"</td>");
			
			out.println("<td>"+count2+"</td>");
			out.println("<td>"+exp2+"</td>");
			out.println("<td>"+money2+"</td>");
			
			out.println("<td>"+count+"</td>");
			out.println("<td>"+exp+"</td>");
			out.println("<td>"+money+"</td>");
			out.println("</tr>");
			
		}
		 
	}
%>
</table>

</body>
</html>
