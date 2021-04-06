<%@ page language="java" contentType="text/html; charset=utf-8"
    pageEncoding="utf-8"%>
<%@ page import="com.fy.engineserver.datasource.props.*,com.google.gson.*,java.util.*,com.fy.engineserver.task.*,com.fy.engineserver.datasource.career.*,com.fy.engineserver.core.*" %>
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
GameManager gm = GameManager.getInstance();
List<String> GameNameList = new ArrayList<String>();
Game games[] = gm.getGames();
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
</head>
<body>
<a href="TasksViewForGame.jsp">刷新此页面</a>
<br>
<br>
<input type="hidden" id="dependency">
<div id=pltsTipLayer style="display: none;position: absolute; z-index:10001"></div>
<%if(dependencys != null){ %>
<table class="tablestyle1">
<tr bgcolor="#C2CAF5" align="center">
<td width="106px">地图
</td>
<td>地图中的任务
</td>
</tr>
<tr bgcolor="#FFFFFF" align="center">
<td valign="top">
<table >
<%for(int i = 0; i < games.length; i++){
	Game game = games[i];
	if(game != null){
		String gameName = game.getGameInfo().getName();
		if(i != (games.length - 1)){
		%>

		<tr>
		<td class="tdtable width align borderbottom"><a href="TasksViewForGame.jsp?gameName=<%=gameName %>" ><%=gameName %></a></td>
		</tr>
		<%
		}else{
			%>

			<tr>
			<td class="tdtable width align"><a href="TasksViewForGame.jsp?gameName=<%=gameName %>" ><%=gameName %></a></td>
			</tr>
			<%
		}
	}
}%>
</table>
</td>
<td align="left" valign="top" class="tdtable">
<%

String gName = request.getParameter("gameName");

if(gName != null){
	Task[] allTasks = tm.getAllTasks();
	List<Task> tasks = new ArrayList<Task>();
	if(allTasks != null){
		for(Task t : allTasks){
			if(t != null && gName.equals(t.getStartMapName())){
				tasks.add(t);
			}
		}
	}
	if(tasks != null){

%>
<font color="red"><b><%=gName %></b></font>中的所有任务<br><br>
<table class="tablestyle2">
<tr align="center">
  				<td rowspan="2" class="lefttd" width="160" align="center" style="word-wrap: break-word;">任务名</td>
  				<td rowspan="2" width="160" align="center" style="word-wrap: break-word;">任务级别</td>
  				<td rowspan="2" width="160" align="center" style="word-wrap: break-word;">玩家最小级别</td>
  				<td rowspan="2" width="160" align="center" style="word-wrap: break-word;">能接任务的门派</td>
  				<td rowspan="2" width="160" align="center" style="word-wrap: break-word;">能接任务的性别</td>
  				<td rowspan="2" width="160" align="center" style="word-wrap: break-word;">接任务所需声望及数值</td>
  				<td rowspan="2" width="160" align="center" style="word-wrap: break-word;">任务类型</td>
  				<td rowspan="2" width="160" align="center" style="word-wrap: break-word;">前置任务</td>
  				<td rowspan="2" width="160" align="center" style="word-wrap: break-word;">经验系数</td>
  				<td rowspan="2" width="160" align="center" style="word-wrap: break-word;">金钱系数</td>
  				<td colspan="7" class="righttd" width="360" align="center" style="word-wrap: break-word;">任务奖励</td>
  				</tr>
  				<tr>
	  			<td>钱</td>
				<td>经验</td>
				<td>物品</td>
				<td>帮派个人贡献度</td>
				<td>帮派基金</td>
				<td>帮派城市发展值</td>
				<td class="righttd">声望</td>
  				</tr>
<%
		
		int alSize = tasks.size();
		for(int j = 0; j < alSize; j++){
  			%>
  			<tr align="center">
  			<%Task task = tasks.get(j);
  			%>
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
  				<%=task.getDifficultyLevel()%>
  				<%} %>
  				</td>
  				<td width="160" align="center" style="word-wrap: break-word;">
  				<%
  				if(task != null){ %>
  				<%=task.getMoneyRatio()%>
  				<%} %>
  				</td>
				<td><%=task.getRewardMoney() %></td>
				<td><%=task.getRewardExp() %></td>
				<td>
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
				<td><%=task.getRewardGangMemberContribution() %></td>
				<td><%=task.getRewardGangFunds() %></td>
				<td><%=task.getRewardCityDevelopmentalPoints() %></td>
				<td class="righttd">
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
		<%}
	%>

</table>
<%}} %>
</td>
</tr>
</table>
<%} %>
</body>
<script type="text/javascript" src="../js/title.js"></script>
</html>
