<%@ page contentType="text/html;charset=utf-8" import="java.util.*,com.xuanzhi.tools.text.*,java.lang.reflect.*,com.fy.engineserver.datasource.buff.*,
com.fy.engineserver.sprite.*,com.xuanzhi.tools.transport.*,com.google.gson.*,com.fy.engineserver.datasource.career.*,com.fy.engineserver.datasource.skill.*"%>
<%
if(true){
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

	}else if(action != null && action.equals("skilllearn")){ 
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
		if (errorMessage == null) {
			session.setAttribute("playerid",request.getParameter("playerid"));
			//player.tryToLearnSkill((byte)index);
		}
	}else if(action != null && action.equals("levelUp")){
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
			player.playerLevelUpByClick();
		}
	}else if(action != null && action.equals("openSeal")){
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
			SealManager sealManager = SealManager.getInstance();
			sealManager.openSeal(player);
		}
	}
%>
<%@page import="java.io.ByteArrayOutputStream"%>
<%@page import="java.io.PrintStream"%>
<%@include file="../IPManager.jsp" %>
<%@page import="com.fy.engineserver.seal.SealManager"%>
<%@page import="com.fy.engineserver.seal.data.Seal"%>
<%@page import="com.fy.engineserver.country.manager.CountryManager"%>
<%@page import="com.fy.engineserver.tournament.manager.TournamentManager"%>
<%@page import="com.fy.engineserver.tournament.data.OneTournamentData"%><html><head>
<link rel="stylesheet" type="text/css" href="../../css/common.css" />
<link rel="stylesheet" type="text/css" href="../../css/table.css" />
</HEAD>
<BODY>
<h2>查询用户状态</h2><br><br>
<%
	if (errorMessage != null) {
		out.println("<center><table border='0' cellpadding='0' cellspacing='2' width='100%' bgcolor='#FF0000' align='center'>");
		out.println("<tr bgcolor='#FFFFFF' align='center'><td>");
		out.println("<font color='red'><pre>" + errorMessage + "</pre></font>");
		out.println("</td></tr>");
		out.println("</table></center>");
	}
%>
<table>
<%Calendar calendar = Calendar.getInstance(); %>
<tr><td>当前积分currentTournamentPoint</td><td>上周积分lastWeekTournamentPoint</td><td>本周currentWeek本周是第<%=calendar.get(Calendar.WEEK_OF_YEAR) %>周</td><td>lastWeek</td><td>奖励是否领取</td></tr>
<%
if(player != null){
	TournamentManager tm = TournamentManager.getInstance();
	OneTournamentData otd = tm.emPlayer.find(player.getId());
	
	if(otd != null){
		%>
		<tr><td><%=otd.currentTournamentPoint %></td><td><%=otd.lastWeekTournamentPoint %></td><td><%=otd.currentWeek %></td><td><%=otd.lastWeek %></td><td><%=otd.pickReward %></td></tr>
		<%
	}
	
	
}

%>
</table>
<%
String actions = request.getParameter("actions");
String rewardPlayerId = request.getParameter("rewardPlayerId");
if(actions != null && actions.equals("modify") && rewardPlayerId != null){
	long id = Long.parseLong(rewardPlayerId);
	TournamentManager tm = TournamentManager.getInstance();
	OneTournamentData otd = tm.emPlayer.find(player.getId());
	if(otd != null){
		otd.setLastWeek(calendar.get(Calendar.WEEK_OF_YEAR) - 1);
		otd.setPickReward(false);
		out.println("成功将id为"+rewardPlayerId+"的玩家的比武领奖记录清空");
	}
}


%>
<form id='f1' name='f1' action=""><input type='hidden' name='action' value='select_playerId'>
请输入要用户的ID：<input type='text' name='playerid' id='playerid' value='<%=playerId %>' size='20'><input type='submit' value='提  交'>
</form>
<form id='f01' name='f01' action=""><input type='hidden' name='action' value='select_playerName'>
请输入要角色名：<input type='text' name='playerName' id='playerName' value='<%=playerName %>' size='20'><input type='submit' value='提  交'>
</form>
<form name="f3">
<input type="hidden" name="actions" value="modify">
玩家id:<input name="rewardPlayerId">
<input type="submit" value="清除这个玩家的领奖记录">
</form>
</BODY></html>
