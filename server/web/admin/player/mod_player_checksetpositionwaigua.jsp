<%@ page contentType="text/html;charset=utf-8" import="java.util.*,com.xuanzhi.tools.text.*,java.lang.reflect.*,
com.fy.engineserver.sprite.*,com.xuanzhi.tools.transport.*,com.google.gson.*,com.fy.engineserver.datasource.career.*,com.fy.engineserver.datasource.skill.*,com.xuanzhi.tools.text.*,com.fy.engineserver.downcity.*"%>
<%
try{
	Gson gson = new Gson();
	PlayerManager sm = PlayerManager.getInstance();
	TaskManager tm = TaskManager.getInstance();
	String action = request.getParameter("action");
	long playerId = -1;
	String playerName = null;
	Object obj = session.getAttribute("playerid");
	Object obj2 = session.getAttribute("playerName");
	if(obj != null){
		playerId = Long.parseLong(obj.toString());
	}
	if(obj2 != null){
		playerName = obj2.toString();
	}
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
			playerId = Long.parseLong(request
					.getParameter("playerid"));
		} catch (Exception e) {
			errorMessage = "玩家ID必须是数字，不能含有非数字的字符";
		}
		if (errorMessage == null) {
			player = sm.getPlayer(playerId);
			if (player == null) {
				errorMessage = "ID为" + playerId + "的玩家不存在！";
			}else{
				session.setAttribute("playerid",request.getParameter("playerid"));
				playerName = player.getName();
				session.setAttribute("playerName",playerName);
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
				session.setAttribute("playerName",request.getParameter("playerName"));
				playerId = player.getId();
				session.setAttribute("playerid",playerId);
			}
		}
	}else if (action != null && action.equals("xuanyun")) {
		try {
			playerId = Long.parseLong(request
					.getParameter("playerid"));
		} catch (Exception e) {
			errorMessage = "玩家ID必须是数字，不能含有非数字的字符"; 
		}
		if (errorMessage == null) {
			player = sm.getPlayer(playerId);
			if (player == null) {
				errorMessage = "角色名为" + playerName + "的玩家不存在！";
			}else{
				playerName = player.getName();
				session.setAttribute("playerName",playerName);
				playerId = player.getId();
				session.setAttribute("playerid",playerId);
				String time = request.getParameter("time");
				if(time != null){
					long invalidTime = Long.parseLong(time)*1000;
					BuffTemplateManager btm = BuffTemplateManager.getInstance();
					BuffTemplate bt = btm.getBuffTemplateByName("眩晕");
					Buff buff = bt.createBuff(1);
					buff.setInvalidTime(System.currentTimeMillis() + invalidTime);
					player.placeBuff(buff);
					out.println("已经让该玩家眩晕"+time+"秒。");
				}
				
			}
		}
	}else if (action != null && action.equals("jiechu")) {
		try {
			playerId = Long.parseLong(request
					.getParameter("playerid"));
		} catch (Exception e) {
			errorMessage = "玩家ID必须是数字，不能含有非数字的字符";
		}
		if (errorMessage == null) {
			player = sm.getPlayer(playerId);
			if (player == null) {
				errorMessage = "角色名为" + playerName + "的玩家不存在！";
			}else{
				playerName = player.getName();
				session.setAttribute("playerName",playerName);
				playerId = player.getId();
				session.setAttribute("playerid",playerId);
				BuffTemplateManager btm = BuffTemplateManager.getInstance();
				BuffTemplate bt = btm.getBuffTemplateByName("圣者散");
				Buff buff = bt.createBuff(1);
				buff.setInvalidTime(System.currentTimeMillis() + 1000);
				player.placeBuff(buff);
				out.println("已经解除玩家眩晕");
			}
		}
	}
%>
<%@page import="java.io.ByteArrayOutputStream"%>
<%@page import="java.io.PrintStream"%>

<%@page import="com.fy.engineserver.billboard.concrete.EquipmentBillboards"%>
<%@page import="com.fy.engineserver.datasource.buff.BuffTemplateManager"%>
<%@page import="com.fy.engineserver.datasource.buff.BuffTemplate"%>
<%@page import="com.fy.engineserver.datasource.buff.Buff"%><%@include file="../IPManager.jsp" %>
<%@page import="com.fy.engineserver.newtask.service.TaskManager"%><html><head>
<link rel="stylesheet" type="text/css" href="../../css/common.css" />
<style type="text/css">
<!--
*{margin: 0px;padding: 0px;list-style: none;}
body{
 margin:0px;
 padding: 0px;
 text-align: center;
 table-layout: fixed;
 word-wrap: break-word;
 list-style: none;
}
#main{
width:1004px;
margin:10px auto;
border:0px solid #69c;
background-color:#DCE0EB;
}
.titlecolor{
background-color:#C2CAF5;
text-align: center;
}
.tableclass1{
width:100%;
border:0px solid #69c;
border-top:1px solid #69c;
border-right:1px solid #69c;
border-bottom:1px solid #69c;
border-left:1px solid #69c;
border-collapse: collapse;
}
td{
border:1px solid #69c;
word-wrap: break-word;
}
.tdwidth{width:100px;word-wrap: break-word;}
-->

</style>
</HEAD>
<BODY>
<h2>角色基本信息</h2><br><br>
<%
	if (errorMessage != null) {
		out.println("<center><table border='0' cellpadding='0' cellspacing='2' width='100%' bgcolor='#FF0000' align='center'>");
		out.println("<tr bgcolor='#FFFFFF' align='center'><td>");
		out.println("<font color='red'><pre>" + errorMessage + "</pre></font>");
		out.println("</td></tr>");
		out.println("</table></center>");
	}
%>
<form id='f1' name='f1' action="mod_player_checksetpositionwaigua.jsp"><input type='hidden' name='action' value='select_playerId'>
请输入要用户的ID：<input type='text' name='playerid' id='playerid' value='<%=playerId %>' size='20'><input type='submit' value='提  交'>
</form>
<form id='f01' name='f01' action="mod_player_checksetpositionwaigua.jsp"><input type='hidden' name='action' value='select_playerName'>
请输入要角色名：<input type='text' name='playerName' id='playerName' value='<%=playerName %>' size='20'><input type='submit' value='提  交'>
</form>
<br>
<%  if(player != null){ 
		CareerManager cm = CareerManager.getInstance();	
		Player p = player;
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
	}
%>
<br>
角色<font color="red"><%=nameStr%></font><%="("+sexStr+")" %>的基本信息:
<br>

<form name="form1">
<input type="hidden" name="action" value="xuanyun">
<input type='hidden' name='playerid' value='<%=playerId %>'>
让此人眩晕<input type="text" name="time" value="8">秒<br>
<input type="submit" value="让此人眩晕">
</form>
<form name="form2">
<input type="hidden" name="action" value="jiechu">
<input type='hidden' name='playerid' value='<%=playerId %>'>
<input type="submit" value="解除此人眩晕">
</form>
<%}}catch(Exception e){
	 out.print(StringUtil.getStackTrace(e)); 
	}%>
</BODY></html>
