<%@ page language="java" contentType="text/html; charset=utf-8"
    pageEncoding="utf-8"%>
<%@ page import="com.fy.engineserver.datasource.props.*,com.google.gson.*,java.util.*,com.fy.engineserver.task.*,com.fy.engineserver.datasource.career.*" %>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<%@include file="IPManager.jsp" %><html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8">
<title>任务</title>
<% TaskManager tm = TaskManager.getInstance();
String[] dependencys = null;
if(tm != null){
	dependencys = tm.getAllDependencys();
}
String dependency = request.getParameter("dependency");
if(dependency == null){
	dependency = "全部";
}
%>
<link rel="stylesheet" type="text/css" href="../css/common.css" />
<style type="text/css">
.tablestyle1{
width:100%;
border:0px solid #69c;
border-top:1px solid #69c;
border-right:1px solid #69c;
border-bottom:1px solid #69c;
border-left:1px solid #69c;
border-collapse: collapse;
}
.tablestyle2{
width:100%;
border:0px solid #69c;
border-top:0px solid #69c;
border-right:0px solid #69c;
border-bottom:0px solid #69c;
border-left:0px solid #69c;
border-collapse: collapse;
}
td{
border:1px solid #69c;
}
.tdtable{
 padding: 0px;
 border-top:0px solid #69c;
border-right:0px solid #69c;
border-bottom:0px solid #69c;
border-left:0px solid #69c;
}
.width{
width:100px;
}
.align{
text-align: left;
}
.borderbottom{
border-bottom:1px dotted #69c;
}
.lefttd{
border-left:0px solid #69c;
}
.toptd{
border-top:0px solid #69c;
}
.righttd{
border-right:0px solid #69c;
}
.bottomtd{
border-bottom:0px solid #69c;
}
</style>
<script type="text/javascript">
function selectdependencys(){
	var selected = document.getElementById("selectdependedcy");
	window.location.href="TasksViewForDependency.jsp?dependency="+selected.value;
}
</script>
</head>
<body>
<a href="TasksView.jsp">刷新此页面</a>
<br>
<br>
<input type="hidden" id="dependency">
<select id="selectdependedcy" onchange="javascript:selectdependencys();">
<option value="全部" <%=(dependency != null? (dependency.equals("全部") ? "selected":""):"") %>><%="全部" %>
<%if(dependencys != null){ 
	for(int i = 0; i < dependencys.length; i++){
		String depStr = dependencys[i];
		if(depStr != null){
			%>
			<option value="<%=depStr %>" <%=(dependency != null? (dependency.equals(depStr) ? "selected":""):"") %>><%=depStr %>
			<%
		}
	}
}
%>
</select>
<div id=pltsTipLayer style="display: none;position: absolute; z-index:10001"></div>
<%if(dependencys != null){

if(dependency != null){
	Task[] tasks = null;
	if(tm != null){
		if("全部".equals(dependency)){
			tasks = tm.getAllTasks();
		}else{
			tasks = tm.getAllTasksOnDependency(dependency);
		}
	}
	if(tasks != null){
%>
<font color="red"><b><%=dependency %></b></font>中的所有任务<br><br>
<table class="tablestyle1">
<tr align="center">
	<td class="lefttd" width="160" align="center" style="word-wrap: break-word;">ID</td>
	<td class="lefttd" width="160" align="center" style="word-wrap: break-word;">任务名</td>
	<td width="160" align="center" style="word-wrap: break-word;">任务等级</td>
	<td width="160" align="center" style="word-wrap: break-word;">可接等级</td>
	<td width="160" align="center" style="word-wrap: break-word;">能接任务的门派</td>
	<td width="160" align="center" style="word-wrap: break-word;">能接任务的性别</td>
	<td width="160" align="center" style="word-wrap: break-word;">接任务所需声望及数值</td>
	<td width="160" align="center" style="word-wrap: break-word;">任务类型</td>
	<td width="160" align="center" style="word-wrap: break-word;">前置任务</td>
	<td width="160" align="center" style="word-wrap: break-word;">开始NPC</td>
	<td width="160" align="center" style="word-wrap: break-word;">结束NPC</td>
	<td width="160" align="center" style="word-wrap: break-word;">归属地</td>
	<td width="160" align="center" style="word-wrap: break-word;">开始地图</td>
	<td width="160" align="center" style="word-wrap: break-word;">结束地图</td>
	<td width="160" align="center" style="word-wrap: break-word;">目标</td>
</tr>
<%
		int alSize = tasks.length;
		for(int jj = 0; jj < alSize; jj++){
  			%>
  			<tr align="center">
  			<%Task task = tasks[jj];
  			%>
  			<td class="lefttd" width="160" align="center" style="word-wrap: break-word;">
  				<%
  				if(task != null){ %>
  				<a href="TaskByTaskId.jsp?taskId=<%=task.getId() %>" ><%=task.getId() %></a>
  				<%} %>
  				</td>
  				<td class="lefttd" width="160" align="center" style="word-wrap: break-word;">
  				<%
  				if(task != null){ %>
  				<a href="TaskByTaskId.jsp?taskId=<%=task.getId() %>" ><%=task.getName() %></a>
  				<%} %>
  				</td>
  				<td width="160" align="center" style="word-wrap: break-word;">
  				<%
  				if(task != null){ %>
  				<%=task.getTaskLevel() %>
  				<%} %>
  				</td>
  				<td width="160" align="center" style="word-wrap: break-word;">
  				<%
  				if(task != null){ %>
  				<%=task.getMinPlayerLevel() %>
  				<%} %>
  				</td>
  				<td width="160" align="center" style="word-wrap: break-word;">
  				<%
  				if(task != null){ 
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
  				<%=careerSb.toString() %>
  				<%} %>
  				</td>
  				<td width="160" align="center" style="word-wrap: break-word;">
  				<%
  				if(task != null){ 
  					String sexStr = "";
  					if(task.getSexLimit() == 0){
  						sexStr = "无限制";
  					}else if(task.getSexLimit() == 1){
  						sexStr = "男";
  					}else if(task.getSexLimit() == 2){
  						sexStr = "女";
  					}
  				%>
  				<%=sexStr%>
  				<%} %>
  				</td>
  				<td width="160" align="center" style="word-wrap: break-word;">
  				<%
  				if(task != null){ %>
  				<%=task.getPrestigeNameLimit() +"("+task.getPrestigeValueLimit()+")" %>
  				<%} %>
  				</td>
  				<td width="160" align="center" style="word-wrap: break-word;">
  				<%
  				if(task != null){ 
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
  				<%=taskTypeStr %>
  				<%} %>
  				</td>
  				<td width="160" align="center" style="word-wrap: break-word;">
  				<%
  				if(task != null){ %>
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
  				<%=preStrs %>
  				<%} %>
  				</td>
  				<td width="160" align="center" style="word-wrap: break-word;">
  				<%
  				if(task != null){ %>
  				<%=task.getStartNPC()%>
  				<%} %>
  				</td>
  				<td width="160" align="center" style="word-wrap: break-word;">
  				<%
  				if(task != null){ %>
  				<%=task.getEndNPC()%>
  				<%} %>
  				</td>
  				<td><%
  				if(task != null){ %>
  				<%=task.getDependency()%>
  				<%} %></td>
				<td><%
  				if(task != null){ %>
  				<%=task.getStartMapName()%>
  				<%} %></td>
				<td>
				<%
  				if(task != null){ %>
  				<%=task.getEndMapName()%>
  				<%} %></td>
				<td>
				<%
				if(task != null){
					if(task.getGoals() != null){
						%><table><%
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
					%></table><%
					}
				}
				%>
				</td>
				
			</tr>
		<%}
	%>
</table>
<%}} %>
<%} %>
</body>
<script type="text/javascript" src="../js/title.js"></script>
</html>
