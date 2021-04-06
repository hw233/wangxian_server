<%@ page contentType="text/html;charset=utf-8" import="java.util.*,com.xuanzhi.tools.text.*,java.lang.reflect.*,com.fy.engineserver.datasource.buff.*,
com.fy.engineserver.sprite.*,com.xuanzhi.tools.transport.*,com.google.gson.*,com.fy.engineserver.datasource.career.*,com.fy.engineserver.datasource.skill.*"%>
<%
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
<%@page import="com.fy.engineserver.country.manager.CountryManager"%><html><head>
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
<tr><td>当前封印等级</td><td>解封时间</td></tr>
<%
SealManager sealManager = SealManager.getInstance();
if(sealManager != null){
	

%>
<tr><td><%=sealManager.seal.sealLevel %></td><td>
<%Calendar calendar = Calendar.getInstance();
calendar.setTimeInMillis(sealManager.seal.sealCanOpenTime);
StringBuffer sb = new StringBuffer();
sb.append(calendar.get(Calendar.YEAR)+"年");
sb.append((calendar.get(Calendar.MONTH)+1)+"月");
sb.append(calendar.get(Calendar.DAY_OF_MONTH)+"日");
sb.append(calendar.get(Calendar.HOUR_OF_DAY)+"时");
sb.append(calendar.get(Calendar.MINUTE)+"分");
out.println(sb);
%>
</td></tr>
<%
}
%>
</table>
<table>
<tr><td>所有封印等级</td><td>解封时间</td><td><%= CountryManager.得到国家名(1) %></td><td><%= CountryManager.得到国家名(2) %></td><td><%= CountryManager.得到国家名(3) %></td></tr>
<%
if(sealManager != null){
Hashtable<Integer,Seal> sealMap = sealManager.sealMap;
if(sealMap != null){
	
for(Integer i : sealMap.keySet()){
	Seal seal = sealMap.get(i);

%>
<tr><td><%=seal.sealLevel %></td><td>
<%Calendar calendar = Calendar.getInstance();
calendar.setTimeInMillis(seal.sealCanOpenTime);
StringBuffer sb = new StringBuffer();
sb.append(calendar.get(Calendar.YEAR)+"年");
sb.append((calendar.get(Calendar.MONTH)+1)+"月");
sb.append(calendar.get(Calendar.DAY_OF_MONTH)+"日");
sb.append(calendar.get(Calendar.HOUR_OF_DAY)+"时");
sb.append(calendar.get(Calendar.MINUTE)+"分");
out.println(sb);
%>
<%
if(i == 150){
	out.println("减去910天后为");
	calendar.setTimeInMillis(seal.sealCanOpenTime - 910*1l*24*3600*1000);
	StringBuffer sbb = new StringBuffer();
	sbb.append(calendar.get(Calendar.YEAR)+"年");
	sbb.append((calendar.get(Calendar.MONTH)+1)+"月");
	sbb.append(calendar.get(Calendar.DAY_OF_MONTH)+"日");
	sbb.append(calendar.get(Calendar.HOUR_OF_DAY)+"时");
	sbb.append(calendar.get(Calendar.MINUTE)+"分");
	out.println(sbb);
	if("modifySeal".equals(action)){
		seal.setSealCanOpenTime(seal.sealCanOpenTime - 910*1l*24*3600*1000, "后台");
		out.println("修改了封印时间<br/>");
	}
}
%>
</td>
<td>
<%
if(seal.firstPlayerIdInCountry[0] > 0){
	out.println(seal.firstPlayerIdInCountry[0]+" ");
	try{
		out.println(PlayerManager.getInstance().getPlayer(seal.firstPlayerIdInCountry[0]).getName());
	}catch(Exception ex){
		
	}
} %>
</td>
<td>
<%
if(seal.firstPlayerIdInCountry[1] > 0){
	out.println(seal.firstPlayerIdInCountry[1]+" ");
	try{
		out.println(PlayerManager.getInstance().getPlayer(seal.firstPlayerIdInCountry[1]).getName());
	}catch(Exception ex){
		
	}
} %>
</td>
<td>
<%
if(seal.firstPlayerIdInCountry[2] > 0){
	out.println(seal.firstPlayerIdInCountry[2]+" ");
	try{
		out.println(PlayerManager.getInstance().getPlayer(seal.firstPlayerIdInCountry[2]).getName());
	}catch(Exception ex){
		
	}
} %>
</td>
</tr>
<%
}
if("modifySeal".equals(action)){
	sealManager.saveSeal();
	out.println("保存了封印时间");
}
}
}
%>
</table>
<form id='f1' name='f1' action=""><input type='hidden' name='action' value='select_playerId'>
请输入要用户的ID：<input type='text' name='playerid' id='playerid' value='<%=playerId %>' size='20'><input type='submit' value='提  交'>
</form>
<form id='f01' name='f01' action=""><input type='hidden' name='action' value='select_playerName'>
请输入要角色名：<input type='text' name='playerName' id='playerName' value='<%=playerName %>' size='20'><input type='submit' value='提  交'>
</form>
<br>
<%if(player != null){ %>
<form id='f51' name='f51' action="query_player.jsp">
<input type='hidden' name='action' value='skilllearn'>
<input type='hidden' name='playerid' value='<%=playerId %>'>
<table border="0" cellpadding="0" cellspacing="1" width="100%" bgcolor="#000000" align="center">

<tr bgcolor="#C2CAF5" align="center">
<td colspan="13">玩家<font color="red"><%=player.getName() %></font>的经验</td>
</tr>
<tr bgcolor="#FFFFFF" align="center"><td><td>当前经验:<%=player.getExp() %></td><td>当前升级经验:<%=player.getNextLevelExp() %></td><td>当前能积累的最大经验:<%=player.getNextLevelExpPool() %></td></tr>
</table>
</form>
<%} %>
<!-- 	<form name="f2">
	<input type="hidden" name="action" value="levelUp">
	<input type='hidden' name='playerid' value='<%=playerId %>'>
	<input type="submit" value="升级">
	</form>
	<form name="f3">
	<input type="hidden" name="action" value="openSeal">
	<input type='hidden' name='playerid' value='<%=playerId %>'>
	<input type="submit" value="国王解封">
	</form>
		<form name="f4">
	<input type="hidden" name="action" value="modifySeal">
	<input type="submit" value="修改150封印时间">
	</form>
	 -->
</BODY></html>
