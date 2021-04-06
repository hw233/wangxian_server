<%@ page contentType="text/html;charset=utf-8" import="java.util.*,com.xuanzhi.tools.text.*,java.lang.reflect.*,
com.fy.engineserver.sprite.*,com.xuanzhi.tools.transport.*,com.google.gson.*,com.fy.engineserver.datasource.career.*,com.fy.engineserver.datasource.skill.*,com.xuanzhi.tools.text.*,com.xuanzhi.boss.game.*"%>
<%
try{
	
	//服务器类型为0代表为可修改的开发服务器
	if(GameConstants.getInstance().getServerType() == 0){
	}else{
		out.println("此服务器不能修改属性");
		return;
	}
	Gson gson = new Gson();
	PlayerManager sm = PlayerManager.getInstance();
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
			errorMessage = "玩家ID必须是数字，不能含有非数字的字符。";
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
	} else if ((action != null && action.equals("modify_player"))
			|| (action != null && action.equals("playingMode"))
			|| (action != null && action.equals("modifyScheme"))) {
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
			}
		}
		if (errorMessage == null) {
			session.setAttribute("playerid",request.getParameter("playerid"));
			Enumeration em = request.getParameterNames();
			try {
				while (em.hasMoreElements()) {
					String name = (String) em.nextElement();
					String value = request.getParameter(name);
					if (!name.equals("action")
							&& !name.equals("playerid")
							&& value != null
							&& value.trim().length() > 0
							&& !value.equals("null")) {
						//name = name.substring(5);
						Class cl = Player.class;
						Method m = cl.getMethod("get"
								+ Character.toUpperCase(name.charAt(0))
								+ name.substring(1), new Class[] {});
						Class c = m.getReturnType();
						Object o = null;
						if (c == String.class) {
							o = value;
						} else if (c == Byte.TYPE) {
							o = Byte.parseByte(value);
						} else if (c == Short.TYPE) {
							o = Short.parseShort(value);
						} else if (c == Integer.TYPE) {
							o = Integer.parseInt(value);
						} else if (c == Long.TYPE) {
							o = Long.parseLong(value);
						} else if (c == Integer.TYPE) {
							o = Integer.parseInt(value);
						} else if (c == Double.TYPE) {
							o = Double.parseDouble(value);
						} else if (c == Character.TYPE) {
							o = value.charAt(0);
						} else if (c == Boolean.TYPE) {
							o = value.equalsIgnoreCase("true");
						} else if (c.getName().indexOf("[") >= 0) {
							if (name.equals("skillLevels")) {
								//String[] str = request
								//		.getParameterValues("skillLevels");
								//byte[] sls = player.getSkillLevels();
								//if (str != null) {
								//	for (int i = 0; i < str.length; i++) {
								//		sls[i] = new Byte(str[i])
								//				.byteValue();
								//	}
								//}
								//o = sls;

							} else {
								try{
								o = gson.fromJson(value, c);
								}catch(Exception e){
									throw new Exception("JSON解析出错：类型为"+c+",值为" + value);
								}
							}

						} else {
							Constructor construct = c
									.getConstructor(new Class[] { String.class });
							o = construct
									.newInstance(new Object[] { value });
						}
						Object oldO = m.invoke(player, new Object[] {});
						m = cl.getMethod("set"
								+ Character.toUpperCase(name.charAt(0))
								+ name.substring(1), new Class[] { c });
						if (name.equals("exp")) {
							value = request.getParameter("exp");
							if (value != null
									&& value.trim().length() > 0) {
								//2表示其他途径获得经验
								int addValue = new Integer(value)
										.intValue();
								player.addExp(addValue, 2);
							}

						} else {
							m.invoke(player, new Object[] { o });
						}
						if (errorMessage == null)
							errorMessage = "";
						errorMessage += "玩家[" + player.getName()
								+ "]的属性[" + name + "]值从{" + oldO
								+ "}被设置为{" + o + "}\n";
					}
				}
				sm.updatePlayer(player);
			} catch (Exception e) {
				ByteArrayOutputStream bout = new ByteArrayOutputStream();
				e.printStackTrace(new PrintStream(bout));
				errorMessage = new String(bout.toByteArray());
			}
		}
	} else if (action != null && action.equals("openAuro")) {
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
			}
		}
		if (errorMessage == null) {
			session.setAttribute("playerid",request.getParameter("playerid"));
			byte auroIndex = Byte.parseByte(request
					.getParameter("openAuro"));
			String str = request.getParameter("flag");
			if ("open".equals(str)) {
				player.openAura(auroIndex);
			}
			if ("close".equals(str)) {
				player.closeAura();
			}
		}

	}else if(action != null && action.equalsIgnoreCase("skilllearn")){
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
			}
		}
		int index = Integer.parseInt(request.getParameter("index"));
		int careerThread = Integer.parseInt(request.getParameter("careerThread"));
		if (errorMessage == null) {
			session.setAttribute("playerid",request.getParameter("playerid"));
			//player.tryToLearnSkill(index,false,false);
		}
	}else if(action != null && action.equalsIgnoreCase("addTaskByTaskName")){
		try {
			playerId = Long.parseLong(request.getParameter("playerid")); 
		} catch (Exception e) {
			errorMessage = "玩家ID必须是数字，不能含有非数字的字符";
		}
		if (errorMessage == null) {
			player = sm.getPlayer(playerId);
			if (player == null) {
				errorMessage = "ID为" + playerId + "的玩家不存在！";
			}
		}
		int taskId = Integer.parseInt(request.getParameter("taskId"));
		if (errorMessage == null) {

		}
	}
%>
<%@page import="java.io.ByteArrayOutputStream"%>
<%@page import="java.io.PrintStream"%>
<%@include file="../IPManager.jsp" %>
<%@page import="com.fy.engineserver.datasource.article.data.equipments.Weapon"%><html><head>
<link rel="stylesheet" type="text/css" href="../../css/common.css" />
<style type="text/css">
<!--
.tdwidth{width:100px;word-wrap: break-word;}
-->

</style>
</HEAD>
<BODY>
<%
//服务器类型为0代表为可修改的开发服务器
if(GameConstants.getInstance().getServerType() == 0){ %>
<h2>修改用户的属性</h2><br><br>
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
		

		//weaponStr = Weapon.getWeaponTypeName(player.getWeaponType());

	}
%>
<br>
角色<font color="red"><%=nameStr%></font>的技能:
<br>
<form id='f51' name='f51' action="mod_player_skill.jsp">
<input type='hidden' name='action' value='skilllearn'>
<input type='hidden' name='playerid' value='<%=playerId %>'>
<table border="0" cellpadding="0" cellspacing="1" width="100%" bgcolor="#000000" align="center">
<tr bgcolor="#C2CAF5" align="center">
<td class="tdwidth">技能名称</td>
<td class="tdwidth">技能类型</td>
<td>技能描述</td>
<td>技能等级</td>
<td>是否可学习</td>
<td>技能升级</td>
</tr>
<%
	if (player != null) {
		Career career = cm.getCareer(player.getCareer());
		//short skillIds[] = career.getSkillIds();
		CareerThread careerThreads[] = null;
		if(career != null){
			careerThreads = career.getCareerThreads();
			careerStr = career.getName();
		}
		if(careerThreads != null){
			for(int i = 0; i < careerThreads.length; i++){
				CareerThread careerThread = careerThreads[i];
				if(careerThread != null){
					Skill skills[] = careerThread.getSkills();
					if(skills != null){
						for(int j = 0; j < skills.length; j++){
							Skill skill = skills[j];
							if(skill != null){
								String skillType = "---";
								if(skill instanceof ActiveSkill){
									skillType = "主动技能";
								}else if(skill instanceof PassiveSkill){
									skillType = "被动技能";
								}else if(skill instanceof AuraSkill){
									skillType = "辅助光环技能";
								}
								//boolean canLearn = career.isUpgradable(player,i,j);
					%>
						<tr bgcolor="#FFFFFF" align="center">
							<td><b><a href="../skillbyid.jsp?id=<%=skill != null ?skill.getId():"" %>&className=<%=skill != null ?skill.getClass().getName():"" %>"><%=skill != null ?skill.getName():"错误:技能为空"%></a></b></td>
							<td><b><%= skillType %></b></td>
							<td align="left"><%
								if(skill != null){
								String s = SkillInfoHelper.generate(player,skill);
								if(s != null){
									s = s.replaceAll("\\[/color\\]","</font>");
									s = s.replaceAll("\\[color=(.*)\\]","<font color='$1'>");
									s = s.replaceAll("\\[icon\\](.*)\\[/icon\\]","<img src='/icons/$1'>");
								}else{
									s = "无描述";
								}
								out.print("<pre>"+s+"</pre>"); }%></td>
							<td><b><%= skill != null ? player.getSkillLevel(skill.getId()):"技能为空" %></b></td>
							<td><b></b></td>
							<%

							%>
							
						</tr>
					<%
							
							}
						}
					}
				}
			}
		}
	%>
		<tr bgcolor="#C2CAF5" align="center">
			<td><b>剩余技能点：</b></td>
			<td colspan="5" align="left">&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;<b><%= player.getUnallocatedSkillPoint() %></b></td>
		</tr>
	<%
	}
%>
</table>
</form>
<%}
		}else{
	out.println("此服务器不能修改数据");
}
}catch(Exception e){
	 out.print(StringUtil.getStackTrace(e)); 
	}%>
</BODY></html>
