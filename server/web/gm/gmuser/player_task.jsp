<%@ page contentType="text/html;charset=utf-8" import="java.util.*,com.xuanzhi.tools.text.*,java.lang.reflect.*,
com.fy.engineserver.sprite.*,com.xuanzhi.tools.transport.*,com.google.gson.*,com.fy.engineserver.datasource.career.*,com.fy.engineserver.datasource.skill.*,com.fy.engineserver.datasource.props.*,com.fy.engineserver.task.*,com.xuanzhi.boss.game.*"%>
<%@include file="../authority.jsp" %>
<%
try{
	Gson gson = new Gson();
	PlayerManager sm = PlayerManager.getInstance();
	TaskManager tm = TaskManager.getInstance();
	String gnmane = session.getAttribute("username").toString();
	String action = request.getParameter("action");
	long playerId = -1;
	String playerName = null;
	if(request.getParameter("playerid")!=null&&request.getParameter("playerid").trim().length()>0)
	  playerId = Integer.parseInt(request.getParameter("playerid").trim());
	 playerName = request.getParameter("playername");
	Player player = null;
	String errorMessage = null;

	if (action == null) {
		if (errorMessage == null) { 
			if(playerId != -1){
				player = sm.getPlayer(playerId);
				if (player == null) {
					errorMessage = "ID为" + playerId + "的玩家不存在！";
				}
			}else if(playerName != null){
				player = sm.getPlayer(playerName);
				if (player == null) {
					errorMessage = "ID为" + playerId + "的玩家不存在！";
				}
			}
		}
	}else if (action != null && action.equals("select_playerId")) {
		try {
			playerId = Integer.parseInt(request
					.getParameter("playerid"));
		} catch (Exception e) {
			errorMessage = "玩家ID必须是数字，不能含有非数字的字符";
		}
		if (errorMessage == null) {
			player = sm.getPlayer(playerId);
			if (player == null) {
				errorMessage = "ID为" + playerId + "的玩家不存在！";
			}else{
				playerName = player.getName();
			}
		}
	}else if (action != null && action.equals("select_playerName")) {
		try {
			playerName = request
					.getParameter("playerName");
		} catch (Exception e) {
			errorMessage = "玩家ID必须是数字，不能含有非数字的字符";
		}
		if (errorMessage == null) {
			player = sm.getPlayer(playerName);
			if (player == null) {
				errorMessage = "角色名为" + playerName + "的玩家不存在！";
			}else{
				playerId = player.getId();
			}
		}
	}
%>
<%@page import="java.io.ByteArrayOutputStream"%>
<%@page import="java.io.PrintStream"%>
<html><head>
<title>玩家任务管理 </title>
<style type="text/css">
.tablestyle1{
width:96%;
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
height:20px;
}
.tdtable{
padding: 0px;
margin:0px;
border-top:0px solid #69c;
border-right:0px solid #69c;
border-bottom:0px solid #69c;
border-left:0px solid #69c;
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
.tdlittlewidth{
width:20%
}
.tdbigwidth{
width:60%
}
</style>
</HEAD>
<BODY>
<h2>查询角色的任务记录</h2><br><br>
<%
	if (errorMessage != null) {
		out.println("<center><table border='0' cellpadding='0' cellspacing='2' width='100%' bgcolor='#FF0000' align='center'>");
		out.println("<tr bgcolor='#FFFFFF' align='center'><td>");
		out.println("<font color='red'><pre>" + errorMessage + "</pre></font>");
		out.println("</td></tr>");
		out.println("</table></center>");
	}
%>
<form id='f1' name='f1' action=""><input type='hidden' name='action' value='select_playerId'>
请输入要用户的ID：<input type='text' name='playerid' id='playerid' value='<%=playerId %>' size='20'><input type='submit' value='提  交'>
</form>
<form id='f01' name='f01' action=""><input type='hidden' name='action' value='select_playerName'>
请输入要角色名：<input type='text' name='playerName' id='playerName' value='<%=playerName %>' size='20'><input type='submit' value='提  交'>
</form>
<br>
<%  

     

     if(player != null){ 
        out.print("<input type='button' value='玩家管理' onclick='window.location.replace(\"user_action.jsp?playerid="+player.getId()+"\")' />");
		CareerManager cm = CareerManager.getInstance();	
		Player p = player;
		out.print("<font color='red'><h3>玩家当天的日常任务数是："+p.getDailyTaskDeliverCount()+" </h3></font>");
		byte teamMark = p.getTeamMark();
		String teamMarkStr = "无组队";
		if(teamMark == 1){
			teamMarkStr = "团队成员";
		}else if(teamMark == 2){
			teamMarkStr = "团队队长";
		}
		Team team = p.getTeam();
		Player captain = null;
		if(team != null){
			captain = team.getCaptain();
		}
		String captainName = "";
		if(captain != null){
			captainName = "'"+captain.getName()+"'";
		}
		Player ps[] = p.getTeamMembers();
		StringBuffer sb = new StringBuffer();
		if(ps != null){
			for(Player pl : ps){
				//if(pl != null && !pl.equals(captain)){
				if(pl != null){
					sb.append("'"+pl.getName()+"'");
				}
			}
		}

	String careerStr = "";
	String weaponStr = "";
	String nameStr = "";
	String sexStr = "";

	int skillCount = 0;
	String nextLevelExpStr = "";
	String currentLevel = "";
	//光环技能名称
	String[] auroStr = null;
	//光环技能下标，开启光环技能需要知道下标
	List auraIndexList = new ArrayList();
	int aura = -1;
	int[] auraIndex = null;
	List<AuraSkill> auraSkillList = new ArrayList<AuraSkill>();

	String playModeStr = "";

	if (player != null) {
		nameStr = player.getName();
		nextLevelExpStr = ""
				+ (player.getNextLevelExp() - player.getExp());
		currentLevel = "" + player.getLevel();
		int careerId = player.getCareer();
		Career career = cm.getCareer(careerId);

		if (player.getSex() == 0) {
			sexStr = "男";
		} else {
			sexStr = "女";
		}
		

		weaponStr = Weapon.getWeaponTypeName(player.getWeaponType());
			
		if (player.getPlayingMode() == 0) {
			playModeStr = "练功模式";
		} else if (player.getPlayingMode() == 1) {
			playModeStr = "屠杀模式";
		}
	}
%>
<br>角色<font color="red"><%=nameStr%></font>的任务数据:
<br>
<form id="f221" name="f221" action="mod_player_task.jsp">
<input type='hidden' name='action' value='addTaskByTaskName'>
<input type='hidden' name='playerid' value='<%=playerId %>'>
<%
Task[] tasks = tm.getAllTasks();

%>
<table class="tablestyle1">
	<tr bgcolor="#C2CAF5" align="center">
	<td colspan="2">玩家未完成的任务</td>
	</tr>
	<tr bgcolor="#C2CAF5" align="center">
	<td>任务名称</td><td>任务进度(目标类型|目标名|进度)</td>
	</tr>
	<%if(p.getInProcessTasks() != null){
		HashMap<Integer, TaskEntity> hm = p.getInProcessTasks();
		Set<Integer> set = hm.keySet();
		if(set != null){
		for(Integer integer : set){
			if(integer != null){
				TaskEntity te = hm.get(integer);
				if(te != null){
					
		%>

	<tr bgcolor="#FFFFFF" align="center">
		<td><%=te.getTask() != null? te.getTask().getName():"" %></td>
		<td class="tdtable">
		<%if(te.getTask()!=null && te.getTask().getGoals()!= null) {
			TaskGoal[] tgs = te.getTask().getGoals();
		%>
		<table class="tablestyle2">
		<%
		boolean existFlag = false;
		for(int i = 0; i < tgs.length; i++){ 
			TaskGoal tg = tgs[i];
			if(tg != null){
				existFlag = true;
			%>
			<tr>
			<td class="lefttd toptd tdlittlewidth"><%=tg.goalTypeToString(tg.getGoalType()) %></td>
			<td class="toptd tdbigwidth"><%=tg.getGoalName() %></td>
			<td class="righttd toptd"><%
			if(te.getGoalProcessAmount() != null && i < te.getGoalProcessAmount().length){
				%>
				<%=te.getGoalProcessAmount(i)+"/"+tg.getGoalAmount() %>
				<%
			}else{
				%>
				任务进度错误
				<%
			}
			%></td>
			</tr>
			<%}
		
		} 
		if(!existFlag){
			%>
			<tr>
		<td class="lefttd toptd tdlittlewidth">&nbsp;</td>
		<td class="toptd tdbigwidth">&nbsp;</td>
		<td class="righttd toptd">&nbsp;</td>
		</tr>
			<%
		}
		%>
		</table>
		<%} %>
		</td>
	</tr>
	<%}}}}} %>
	<tr bgcolor="#C2CAF5" align="center">
	<td colspan="2">玩家已完成的任务</td>
	</tr>
	<tr bgcolor="#C2CAF5" align="center">
	<td>任务名称</td><td>已交付次数</td>
	</tr>
	<%if(p.getDeliverTasks() != null){
		HashMap<Integer,TaskDeliverEntity> hm = p.getDeliverTasks();
		Set<Integer> set = hm.keySet();
		if(set != null){
		for(Integer integer : set){
			if(integer != null){
				TaskDeliverEntity te = hm.get(integer);
				if(te != null){
					String taskNameStr = "";
					if(tm.getTaskById(te.getTaskId()) != null){
						taskNameStr = tm.getTaskById(te.getTaskId()).getName()+"("+te.getTaskId()+")";
					}else{
						taskNameStr = "("+te.getTaskId()+")";
					}
		%>

	<tr bgcolor="#FFFFFF" align="center">
		<td><%=taskNameStr%></td>
		<td>
		<%=te.getDeliverCount() %>
		</td>
	</tr>
	<%}}}}} %>
</table>
</form>
<%}
		}catch(Exception e){
		  out.print(StringUtil.getStackTrace(e));
	 //out.print(e.getLocalizedMessage()); 
	}%>
</BODY></html>
