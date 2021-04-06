<%@ page language="java" contentType="text/html; charset=utf-8"
    pageEncoding="utf-8"%>
<%@ page import="com.fy.engineserver.datasource.props.*,com.google.gson.*,java.util.*,com.fy.engineserver.task.*,com.fy.engineserver.datasource.career.*,com.fy.engineserver.menu.question.*" %>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<%@include file="IPManager.jsp" %><html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8">
<title>任务</title>
<style type="text/css">
<!--
.tablestyle{
width:96%;
border:0px solid #69c;
border-top:1px solid #69c;
border-right:1px solid #69c;
border-bottom:1px solid #69c;
border-left:1px solid #69c;
border-collapse: collapse;
}
.tablestyle1{
 margin:0px;
 padding: 0px;
width:100%;
height:100%;
border-top:0px solid #69c;
border-right:0px solid #69c;
border-bottom:0px solid #69c;
border-left:0px solid #69c;
border-collapse: collapse;
}
.trcolor1{
background-color: #C2CAF5;
}
.tdcolor1{
border:1px solid #69c;
background-color: #C2CAF5;
}
.trcolor2{
}
.tdcolor2{
border:1px solid #69c;
}
.tdtable{
 padding: 0px;
}
.td{
border-top:0px solid #69c;
border-left:0px solid #69c;
}
.tdwidth1{
width:25%
}
.tdwidth2{
width:33%
}
.bottomtd{border-bottom:0px solid #69c;}
.righttd{border-right:0px solid #69c;}

-->


</style>
</head>
<body>

<input width="68" type="button" value="返回" onclick="javascript:history.back()">
<br>
<br>
<%

String taskId = request.getParameter("taskId");

if(taskId != null){

	TaskManager tm = TaskManager.getInstance();
	Task task = null;
	if(tm != null){
		task = tm.getTaskById(Integer.parseInt(taskId));
		if(task != null){
			if(task.getName().equals("神魂天降（副本）")){
				if(task.getGoals() != null && task.getGoals().length > 0){
					for(TaskGoal goal : task.getGoals()){
						if(goal != null && goal.getMonsterNames() != null && goal.getMonsterNames().length > 0 && "三清道君神魂".equals(goal.getMonsterNames()[0])){
							goal.getMonsterNames()[0] = "三清道尊";
						}
					}
				}
			}
			TaskSeries taskSeries = tm.getTaskSeries(task.getTaskSeriesName());
			if(taskSeries != null){
				int taskIndex = taskSeries.indexOf(task);
				Task[] tasks = taskSeries.getTasks();
				if(tasks != null){
					%>
					此任务为系列任务，任务系列名为:<font color="blue"><%=task.getTaskSeriesName() %></font>，其中红色代表本任务，绿色代表前一个任务，橙色代表后一个任务。<br>
					<%
					for(int i = 0; i < tasks.length; i++){
						Task t = tasks[i];
						if(i == (taskIndex-1)){
							out.println("<a href='TaskByTaskId.jsp?taskId="+t.getId()+"' style='text-decoration:none;color:green'>"+t.getName()+"</a>&nbsp;");
						}else if(i == taskIndex){
							out.println("<a href='TaskByTaskId.jsp?taskId="+t.getId()+"' style='text-decoration:none;color:red'>"+t.getName()+"</a>&nbsp;");
						}else if(i == (taskIndex+1)){
							out.println("<a href='TaskByTaskId.jsp?taskId="+t.getId()+"' style='text-decoration:none;color:orange'>"+t.getName()+"</a>&nbsp;");
						}else{
							out.println("<a href='TaskByTaskId.jsp?taskId="+t.getId()+"' style='text-decoration:none;color:#2D20CA'>"+t.getName()+"</a>&nbsp;");
						}
					}
					%>
					
					<%
				}else{
					%>
					<b>此任务不是系列任务</b><br>
					<%
				}
			}else{
				%>
				<b>此任务不是系列任务</b><br>
				<%
			}
			%>
			<br><b>提示：如果下面的信息与上面的不一致，那么说明数据错误</b><br>
			<%
			TaskSeries[] taskSeriesArrays = tm.getTaskSeries();
			StringBuffer sbs = new StringBuffer();
			if(taskSeriesArrays != null){
				for(TaskSeries ts : taskSeriesArrays){
					if(ts != null && ts.contains(task)){
						sbs.append(ts.getName()+" ");
					}
				}
			}
			if(sbs.length() != 0){
				%>
				任务系列名为:<%=sbs.toString() %><br>
				<%
			}else{
				%>
				<b>此任务不是系列任务</b><br>
				<%
			}
			%>
			<br>
			<br>
			<%
			String taskTypeStr = "";
			if(task.getTaskType()==Task.TYPE_ONCE){ 
				taskTypeStr = "一次性任务";
			}else if(task.getTaskType()==Task.TYPE_DAILY){ 
				taskTypeStr = "日常任务";
			}else if(task.getTaskType()==Task.TYPE_LOOP){ 
				taskTypeStr = "循环任务";
			}else if(task.getTaskType()==Task.TYPE_RUNNING){
				taskTypeStr = "跑环任务";
			}
			%>
			<table class="tablestyle" >
			<tr align="center">
			<td class="tdcolor1 tdwidth1">任务名</td><td class="tdcolor2"><%=task.getName() %></td>
			</tr><tr align="center">
			<td class="tdcolor1 tdwidth1">任务id</td><td class="tdcolor2"><%=task.getId() %></td>
			</tr><tr align="center">
			<td class="tdcolor1 tdwidth1">任务级别</td><td class="tdcolor2"><%=task.getTaskLevel() %></td>
			</tr><tr align="center">
			<td class="tdcolor1 tdwidth1">玩家最小级别</td><td class="tdcolor2"><%=task.getMinPlayerLevel() %></td>
			</tr><tr align="center">
			<td class="tdcolor1 tdwidth1">玩家最大级别</td><td class="tdcolor2"><%=task.getMaxPlayerLevel() %></td>
			</tr><tr align="center">
			<td class="tdcolor1 tdwidth1">是否是帮派任务</td><td class="tdcolor2"><%=task.isIsGangTask() ? "是":"否" %></td>
			</tr><tr align="center">
			<td class="tdcolor1 tdwidth1">任务归属地</td><td class="tdcolor2"><%=task.getDependency() %></td>
			</tr><tr align="center">
			<td class="tdcolor1 tdwidth1">任务系列</td><td class="tdcolor2"><%=task.getTaskSeriesName() %></td>
			</tr><tr align="center">
			<td class="tdcolor1 tdwidth1">任务类型</td><td class="tdcolor2"><%=taskTypeStr %></td>
			</tr><tr align="center">
			<td class="tdcolor1 tdwidth1">奖励经验系数</td><td class="tdcolor2"><%=task.getDifficultyLevel() %></td>
			</tr><tr align="center">
			<td class="tdcolor1 tdwidth1">奖励金钱系数</td><td class="tdcolor2"><%=task.getMoneyRatio() %></td>
			</tr><tr align="center">
			<td class="tdcolor1 tdwidth1">任务开始点</td>
			<td class="tdcolor2 tdtable">
			<table class="tablestyle1">
			<tr>
			<td class="tdcolor1 td tdwidth2">开始地图</td>
			<td class="tdcolor1 td tdwidth2">开始NPC</td>
			<td class="tdcolor1 td righttd">开始坐标</td>
			</tr>
			<tr>
			<td class="tdcolor2 td bottomtd"><%=task.getStartMapName() %></td>
			<td class="tdcolor2 td bottomtd"><%=task.getStartNPC() %></td>
			<td class="tdcolor2 td bottomtd righttd"><%="("+task.getStartX()+","+task.getStartY()+")" %></td>
			</tr>
			</table></td>
			</tr><tr align="center">
			<td class="tdcolor1 tdwidth1">任务结束点</td>
			<td class="tdcolor2 tdtable">
			<table class="tablestyle1">
			<tr>
			<td class="tdcolor1 td tdwidth2">结束地图</td>
			<td class="tdcolor1 td tdwidth2">结束NPC</td>
			<td class="tdcolor1 td righttd">结束坐标</td>
			</tr>
			<tr>
			<td class="tdcolor2 td bottomtd"><%=task.getEndMapName() %></td>
			<td class="tdcolor2 td bottomtd"><%=task.getEndNPC() %></td>
			<td class="tdcolor2 td bottomtd righttd"><%="("+task.getEndX()+","+task.getEndY()+")" %></td>
			</tr>
			</table>
			</td>
			</tr><tr align="center">
			<td class="tdcolor1 tdwidth1">任务奖励</td>
			<td class="tdcolor2 tdtable">
			<table class="tablestyle1">
			<tr>
			<td class="tdcolor1 td">钱</td>
			<td class="tdcolor1 td">经验</td>
			<td class="tdcolor1 td">物品</td>
			<td class="tdcolor1 td">帮派个人贡献度</td>
			<td class="tdcolor1 td">帮派基金</td>
			<td class="tdcolor1 td">帮派城市发展值</td>
			<td class="tdcolor1 td">帮派经验</td>
			<td class="tdcolor1 td righttd">声望</td>
			</tr>
			<tr>
			<td class="tdcolor2 td bottomtd"><%=task.getRewardMoney() %></td>
			<td class="tdcolor2 td bottomtd"><%=task.getRewardExp() %></td>
			<td class="tdcolor2 td bottomtd">
			<%
			StringBuffer sb = new StringBuffer();
			if(task.getRewardArticleNames() != null){ 
			for(int i = 0; i < task.getRewardArticleNames().length; i++){
				String str = task.getRewardArticleNames()[i];
				if(str != null){
					sb.append(str+"("+task.getRewardArticleCounts()[i]+")");
					sb.append(" ");
				}
			}
			}
			%>
			
			<%=sb.toString() %></td>
			<td class="tdcolor2 td bottomtd"><%=task.getRewardGangMemberContribution() %></td>
			<td class="tdcolor2 td bottomtd"><%=task.getRewardGangFunds() %></td>
			<td class="tdcolor2 td bottomtd"><%=task.getRewardCityDevelopmentalPoints() %></td>
			<td class="tdcolor2 td bottomtd"><%=task.getRewardGangExp() %></td>
			<td class="tdcolor2 td bottomtd righttd">
			<%
			StringBuffer sbb = new StringBuffer();
			if(task.getRewardPrestigeNames() != null){ 
			for(int i = 0; i < task.getRewardPrestigeNames().length; i++){
				String str = task.getRewardPrestigeNames()[i];
				if(str != null){
					sbb.append(str+"("+task.getRewardPrestigeValues()[i]+")");
					sbb.append(" ");
				}
			}
			}
			%>
			<%=sbb.toString() %></td>
			</tr>
			</table>
			</td>
			</tr><tr align="center">
			<td class="tdcolor1 tdwidth1">前置任务</td><td class="tdcolor2">
			<%String preStrs = "无前置任务"; 
			if(task.getPreviousTaskIds() != null){
				StringBuffer sbTemp = new StringBuffer();
				for(int id : task.getPreviousTaskIds()){
					if(tm.getTaskById(id) != null){
						sbTemp.append("<a href='TaskByTaskId.jsp?taskId="+id+"'>"+tm.getTaskById(id).getName()+"</a> ");
					}
				}
				preStrs = sbTemp.toString();
			}
			
			%>
  				<%=preStrs %></td>
			</tr><tr align="center">
			<td class="tdcolor1 tdwidth1">接任务的门派</td><td class="tdcolor2">
			<%
			StringBuffer careerSb = new StringBuffer();
			if(task.getCareerLimits() != null){
				CareerManager cm = CareerManager.getInstance();
				if(task.getCareerLimits().length == 0){
					careerSb.append("无限制");
				}
				for(byte b : task.getCareerLimits()){
					Career c = cm.getCareer(b);
					if(c != null){
						careerSb.append(c.getName());
						careerSb.append(" ");
					}
					
				}
			}
			
			%>
			
			<%=careerSb.toString() %></td>
			</tr><tr align="center">
			<td class="tdcolor1 tdwidth1">接任务所需声望及数值</td><td class="tdcolor2"><%=task.getPrestigeNameLimit() +"("+task.getPrestigeValueLimit()+")"%></td>
			</tr><tr align="center">
			<td class="tdcolor1 tdwidth1">接任务的性别</td><td class="tdcolor2">
			<%
			String sexStr = "";
			if(task.getSexLimit() == 0){
				sexStr = "无限制";
			}else if(task.getSexLimit() == 1){
				sexStr = "男";
			}else if(task.getSexLimit() == 2){
				sexStr = "女";
			}
			%>
			<%=sexStr %></td>
			</tr><tr align="center">
			<td class="tdcolor1 tdwidth1">领取时间</td>
			<td class="tdcolor2">
			<% 
			TimeSlice ts = task.getValidTimeSlice();
			String validStr = "";
			if(ts == null){
				out.println("无限制");
			}else{
				if(ts.isValid(new Date())){
					validStr = "(现在可以接)";
				}else{
					validStr = "(现在不可接)";
				}
				if(ts instanceof DailyTimeSlice){
					out.println("每天的"+((DailyTimeSlice)ts).getStartHour()+"点到"+((DailyTimeSlice)ts).getEndHour()+"点"+validStr);
				}else if(ts instanceof WeeklyTimeSlice){
					boolean[] weekBoolean = ((WeeklyTimeSlice)ts).getWeeklyValid();
					StringBuffer daysb = new StringBuffer();
					for(int i = 0; i < weekBoolean.length; i++){
						switch(i){
						case 0:
							if(weekBoolean[i]){
								daysb.append("星期日 ");
							}
							break;
						case 1:
							if(weekBoolean[i]){
								daysb.append("星期一 ");
							}
							break;
						case 2:
							if(weekBoolean[i]){
								daysb.append("星期二 ");
							}
							break;
						case 3:
							if(weekBoolean[i]){
								daysb.append("星期三 ");
							}
							break;
						case 4:
							if(weekBoolean[i]){
								daysb.append("星期四 ");
							}
							break;
						case 5:
							if(weekBoolean[i]){
								daysb.append("星期五 ");
							}
							break;
						case 6:
							if(weekBoolean[i]){
								daysb.append("星期六 ");
							}
							break;
						}
						
					}
					if(daysb.length() == 0){
						out.println("<h1><font color='red'>没有可接日期</font></h1>");
					}else{
						out.println(daysb.toString()+validStr);
					}
				}else if(ts instanceof DateTimeSlice){
					out.println(ts.toString()+validStr);
				}
			}
			%>
			</td>
			</tr><tr align="center">
			<td class="tdcolor1 tdwidth1">接到任务到完成任务的时间</td><td class="tdcolor2">
			<%=task.getLimitTime() == 0?"无时间限制":task.getLimitTime()+"秒" %></td>
			</tr><tr align="center">
			<td class="tdcolor1 tdwidth1">用于传递的物品</td><td class="tdcolor2"><%=task.getTransitiveArticleName() %></td>
			</tr><tr align="center">
			<td class="tdcolor1 tdwidth1">任务目标</td><td class="tdcolor2 tdtable">
			<table class="tablestyle1">
			<%
			
			if(task.getGoals() != null){
				for(int i = 0; i < task.getGoals().length; i++){
					TaskGoal tg = task.getGoals()[i];
					if(tg != null){
			if(i != task.getGoals().length - 1){
				%>
							<tr>
			<td class="tdcolor1 td">任务目标<%=(i+1) %></td>
			<td class="tdcolor2 td righttd tdtable">
			<table class="tablestyle1">
			<tr>
			<td class="tdcolor1 td">目标类型</td>
			<td class="tdcolor1 td">目标的名字</td>
			<td class="tdcolor1 td">数量</td>
			<td class="tdcolor1 td">地理位置</td>
			<td class="tdcolor1 td">坐标</td>
			<td class="tdcolor1 td">任务目标的描述</td>
			<td class="tdcolor1 td righttd">和NPC对话的内容</td>
			</tr>
			<%
			if(tg.getGoalType() != tg.GOALTYPE_QUESTION){
			%>
			<tr>
			<td class="tdcolor2 td bottomtd"><%=tg.goalTypeToString(tg.getGoalType()) %></td>
			<td class="tdcolor2 td bottomtd"><%=tg.getGoalName() %></td>
			<td class="tdcolor2 td bottomtd"><%=tg.getGoalAmount() %></td>
			<td class="tdcolor2 td bottomtd"><%=tg.getMapName() %></td>
			<td class="tdcolor2 td bottomtd"><%="("+tg.getX()+","+tg.getY()+")" %></td>
			<td class="tdcolor2 td bottomtd tdtable">
			<table class="tablestyle1">
			<tr>
			<td class="tdcolor2 td bottomtd">描述:<%=tg.getDescription() %></td>
			<td class="tdcolor2 td bottomtd righttd tdtable">
			<table class="tablestyle1">
			<tr>
			<td class="tdcolor1 td">怪名</td>
			<td class="tdcolor1 td righttd">掉落几率</td>
			</tr>
			<%
			String monsters[] = tg.getMonsterNames();
			int[] flopRates = tg.getFlopRate();
			if(monsters != null && monsters.length != 0){
				%>
				
				<%
			}
			%>
			
			<%
			
			if(monsters != null && flopRates != null){
				for(int j = 0; j < monsters.length && j < flopRates.length; j++){
					if(j == (monsters.length -1)){
						%>
						<tr>
			<td class="tdcolor2 td bottomtd"><%=monsters[j] %></td>
			<td class="tdcolor2 td bottomtd righttd"><%=flopRates[j] %>%</td>
			</tr>
						<%
					}
			%>
			
			
			<%} }%>
			
			</table>
			</td>
			</tr>
			</table>
			
			
			</td>
			<td class="tdcolor2 bottomtd righttd"><%=tg.getTalkLines() %></td>
			</tr>
			<%}else{
				%>
				<tr>
				<td class="tdcolor2 td bottomtd"><%=tg.goalTypeToString(tg.getGoalType()) %></td>
				<td class="tdcolor2 td bottomtd"><%=tg.getGoalName() %></td>
				<td class="tdcolor2 td bottomtd"><%=tg.getGoalAmount() %></td>
				<td class="tdcolor2 td bottomtd"><%=tg.getMapName() %></td>
				<td class="tdcolor2 td bottomtd"><%="("+tg.getX()+","+tg.getY()+")" %></td>
				<td class="tdcolor2 td bottomtd tdtable">
				<table class="tablestyle1">
				<tr>
				<td class="tdcolor2 td bottomtd">描述:<%=tg.getDescription() %></td>
				<td class="tdcolor2 td bottomtd righttd tdtable">
				<%String questionTitles[] = tg.getMonsterNames();
				StringBuffer qsb = new StringBuffer();
				if(questionTitles != null){
					for(int m = 0; m < questionTitles.length; m++){
						String questionTitle = questionTitles[m];
						if(questionTitle != null){
							if(qsb.length() != 0){
								qsb.append("\n");
							}
							qsb.append("题目"+(m+1)+":<a href='question.jsp?titleName="+java.net.URLEncoder.encode(questionTitle)+"'>"+questionTitle+"</a>");
						}
					}
				}
				%>
				</td>
				</tr>
				</table>
				
				
				</td>
				<td class="tdcolor2 bottomtd righttd"><%=tg.getTalkLines() %></td>
				</tr>
				<%
				
			} %>
			
			</table>
			</td>
			</tr>
				<%
			}else{
						%>
						
						<tr>
			<td class="tdcolor1 td bottomtd">任务目标<%=(i+1) %></td>
			<td class="tdcolor2 td bottomtd righttd tdtable">
			<table class="tablestyle1">
			<tr>
			<td class="tdcolor1 td">目标类型</td>
			<td class="tdcolor1 td">目标的名字</td>
			<td class="tdcolor1 td">数量</td>
			<td class="tdcolor1 td">地理位置</td>
			<td class="tdcolor1 td">坐标</td>
			<td class="tdcolor1 td">任务目标的描述</td>
			<td class="tdcolor1 td righttd">和NPC对话的内容</td>
			</tr>
			<%
			if(tg.getGoalType() != tg.GOALTYPE_QUESTION){
			%>
			<tr>
			<td class="tdcolor2 td bottomtd"><%=tg.goalTypeToString(tg.getGoalType()) %></td>
			<td class="tdcolor2 td bottomtd"><%=tg.getGoalName() %></td>
			<td class="tdcolor2 td bottomtd"><%=tg.getGoalAmount() %></td>
			<td class="tdcolor2 td bottomtd"><%=tg.getMapName() %></td>
			<td class="tdcolor2 td bottomtd"><%="("+tg.getX()+","+tg.getY()+")" %></td>
			<td class="tdcolor2 td bottomtd tdtable">
			<table class="tablestyle1">
			<tr>
			<td class="tdcolor2 td bottomtd">描述:<%=tg.getDescription() %></td>
			<td class="tdcolor2 td bottomtd righttd tdtable">
			<table class="tablestyle1">
			<tr>
			<td class="tdcolor1 td">怪名</td>
			<td class="tdcolor1 td righttd">掉落几率</td>
			</tr>
			<%
			String monsters[] = tg.getMonsterNames();
			int[] flopRates = tg.getFlopRate();
			if(monsters != null && monsters.length != 0){
				%>
				
				<%
			}
			%>
			
			<%
			
			if(monsters != null && flopRates != null){
				for(int j = 0; j < monsters.length && j < flopRates.length; j++){
					if(j == (monsters.length -1)){
						%>
						<tr>
			<td class="tdcolor2 td bottomtd"><%=monsters[j] %></td>
			<td class="tdcolor2 td bottomtd righttd"><%=flopRates[j] %>%</td>
			</tr>
						<%
					}
			%>
			
			
			<%} }%>
			
			</table>
			</td>
			</tr>
			</table>
			
			
			</td>
			<td class="tdcolor2 td bottomtd righttd"><%=tg.getTalkLines() %></td>
			</tr>
			<%}else{
				%>
				<tr>
				<td class="tdcolor2 td bottomtd"><%=tg.goalTypeToString(tg.getGoalType()) %></td>
				<td class="tdcolor2 td bottomtd"><%=tg.getGoalName() %></td>
				<td class="tdcolor2 td bottomtd"><%=tg.getGoalAmount() %></td>
				<td class="tdcolor2 td bottomtd"><%=tg.getMapName() %></td>
				<td class="tdcolor2 td bottomtd"><%="("+tg.getX()+","+tg.getY()+")" %></td>
				<td class="tdcolor2 td bottomtd tdtable">
				<table class="tablestyle1">
				<tr>
				<td class="tdcolor2 td bottomtd">描述:<%=tg.getDescription() %></td>
				<td class="tdcolor2 td bottomtd righttd tdtable">
				<%String questionTitles[] = tg.getMonsterNames();
				StringBuffer qsb = new StringBuffer();
				if(questionTitles != null){
					for(int m = 0; m < questionTitles.length; m++){
						String questionTitle = questionTitles[m];
						if(questionTitle != null){
							if(qsb.length() != 0){
								qsb.append("\n");
							}
							qsb.append("题目"+(m+1)+":<a href='question.jsp?titleName="+java.net.URLEncoder.encode(questionTitle)+"'>"+questionTitle+"</a>");
						}
					}
				}
				%>
				</td>
				</tr>
				</table>
				
				
				</td>
				<td class="tdcolor2 bottomtd righttd"><%=tg.getTalkLines() %></td>
				</tr>
				<%
			} %>
			
			</table>
			</td>
			</tr>
						
						<%
					}
					}
				}
			}
			
			%>
			
			
			</table>
			
			</td>
			</tr>
			</table>
			<pre><font color="#8CA5BD">简要描述:</font><br><Label><%=task.getTaskSummary() %></Label></pre><br><br>
			<pre><font color="#A7A770">任务详细描述:</font><br><Label><%=task.getTaskDescription() %></Label></pre><br><br>
			<pre><font color="#C6998C">任务未完成描述:</font><br><Label><%=task.getTaskNotFinishDescription() %></Label></pre><br><br>
			<pre><font color="#D791CE">任务完成前描述:</font><br><Label><%=task.getTaskBeforeFinishDescription() %></Label></pre><br><br>
			<pre><font color="#D78A9D">完成任务描述:</font><br><Label><%=task.getTaskFinishDescription() %></Label></pre><br>
		<%
		}else{
			out.println("没有ID为"+taskId+"的任务");
		}

	}
} %>
</body>
</html>
