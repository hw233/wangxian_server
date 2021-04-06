<%@ page contentType="text/html;charset=utf-8" %>
<%@page import="java.util.*,com.xuanzhi.tools.text.*,java.lang.reflect.*,
com.fy.engineserver.sprite.*,com.xuanzhi.tools.transport.*,com.google.gson.*,com.xuanzhi.boss.game.*"%><% 

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
	}else if(action != null && action.equals("modify_player")){
		
	}
	Map<String,String> map = new HashMap<String,String>();
	map.put("exp", "当前经验值");
	map.put("hp", "角色当前的生命值");
	map.put("mp", "角色当前的魔法值");
	map.put("totalHpB", "角色的最大生命值");
	map.put("totalHpC", "角色的最大生命值(百分比)");
	map.put("totalMpB", "角色的最大魔法值");
	map.put("totalMpC", "角色的最大魔法值(百分比)");
	map.put("hpRecoverBaseB", "hp恢复速度，每五秒恢复一次");
	map.put("mpRecoverBaseB", "mp恢复速度，每五秒恢复一次");
	map.put("hpRecoverExtend", "hp恢复速度，每半秒恢复一次");
	map.put("mpRecoverExtend", "mp恢复速度，每半秒恢复一次");
	map.put("speedC", "角色的移动速度(百分比)");
	map.put("strengthB", "力量");
	map.put("strengthC", "力量(百分比)");
	map.put("spellpowerB", "内力");
	map.put("spellpowerC", "内力(百分比)");
	map.put("dexterityB", "灵巧");
	map.put("dexterityC", "灵巧(百分比)");
	map.put("constitutionB", "体质");
	map.put("constitutionC", "体质(百分比)");
	map.put("physicalDamageUpperLimitB", "物理攻击伤害力的上限");
	map.put("physicalDamageUpperLimitC", "物理攻击伤害力的上限(百分比)");
	map.put("physicalDamageLowerLimitB", "物理攻击伤害力的下限");
	map.put("physicalDamageLowerLimitC", "物理攻击伤害力的下限(百分比)");
	map.put("defenceB", "物理防御");
	map.put("defenceC", "物理防御(百分比)");
	map.put("spellDamageUpperLimitB", "道术攻击伤害力的上限");
	map.put("spellDamageUpperLimitC", "道术攻击伤害力的上限(百分比)");
	map.put("spellDamageLowerLimitB", "道术攻击伤害力的下限");
	map.put("spellDamageLowerLimitC", "道术攻击伤害力的下限(百分比)");
	map.put("resistanceB", "道术防御");
	map.put("resistanceC", "道术防御(百分比)");
	map.put("attackRatingB", "命中");
	map.put("attackRatingC", "命中(百分比)");
	map.put("dodgeB", "回避");
	map.put("dodgeC", "回避(百分比)");
	map.put("coolDownTimePercentB", "技能冷却时间的百分比");
	map.put("setupTimePercentB", "技能僵直时间的百分比");
	map.put("criticalHitB", "会心一击");
	map.put("criticalHitC", "会心一击(百分比)");
	map.put("ironMaidenPercent", "反噬伤害的百分比，缺省为零");
	map.put("hpStealPercent", "攻击时吸取生命值的百分比，吸取的生命值加到攻击者身上");
	map.put("mpStealPercent", "攻击时吸取魔法值的百分比，吸取的魔法值加到攻击者身上");
	map.put("luckB", "幸运值");
	map.put("expPercent", "经验值获得的百分比，缺省为100");
	map.put("username","用户名");
	map.put("world","所在分区");
	map.put("game","游戏");
	map.put("lastGame","最后登陆游戏");
	map.put("hatridRate","增加仇恨的比例1表示百分之一个点");
	map.put("lastRequestTime","最后一次请求时间");
	map.put("name","名字");
	map.put("state","角色当前的状态，如站立、行走等");
	map.put("scheme","人物avata");
	map.put("level","等级");
	map.put("strength","力量");
	map.put("dexterity","灵巧");
	map.put("constitution","体制");
	map.put("spellpower","内力");
	map.put("career","职业");
	map.put("nextLevelExp","下一级别经验");
	map.put("unallocatedPropertyPoint","未分配属性点");
	map.put("totalHP","最大血量");
	map.put("totalMP","最大魔法值");
	map.put("playingMode","角色模式");
	map.put("hpRecoverBase","hp恢复速度，每五秒恢复一次");
	map.put("mpRecoverBase","mp恢复速度，每五秒恢复一次");
	map.put("physicalDamageUpperLimit","物理攻击伤害力的上限");
	map.put("physicalDamageLowerLimit","物理攻击伤害力的下限");
	map.put("spellDamageUpperLimit","道术攻击伤害力的上限");
	map.put("spellDamageLowerLimit","道术攻击伤害力的下限");
	map.put("weaponType","武器类型");
	map.put("sex","性别");
	map.put("direction","角色面朝的方向");
	map.put("style","动画风格，主要受到武器类型和技能的影响");
	map.put("aura","阴影层的光环，受光环辅助技能影响，默认值-1");
	map.put("encloser","角色周身的外罩，一种透明的光影效果");
	map.put("horse","标记有马无马，0-无马，1-有马");
	map.put("totalHpA","角色的最大生命值(基本)");
	map.put("totalMpA","角色的最大魔法值(基本)");
	map.put("hpRecoverBaseA","hp恢复速度，每五秒恢复一次(基本)");
	map.put("mpRecoverBaseA","mp恢复速度，每五秒恢复一次(基本)");
	map.put("speed","角色的移动速度，单位：像素/秒");
	map.put("speedA","角色的移动速度，单位：像素/秒(基本)");
	map.put("strengthA","力量(基本)");
	map.put("spellpowerA","内力(基本)");
	map.put("dexterityA","灵巧(基本)");
	map.put("constitutionA","体质(基本)");
	map.put("physicalDamageUpperLimitA","物理攻击伤害力的上限(基本)");
	map.put("physicalDamageLowerLimitA","物理攻击伤害力的下限(基本)");
	map.put("defence","物理防御(总)");
	map.put("defenceA","物理防御(基本)");
	map.put("spellDamageUpperLimitA","道术攻击伤害力的上限(基本)");
	map.put("spellDamageLowerLimitA","道术攻击伤害力的下限(基本)");
	map.put("resistance","道术防御(总)");
	map.put("resistanceA","道术防御(基本)");
	map.put("attackRating","命中");
	map.put("dodge","回避");
	map.put("coolDownTimePercentA","技能冷却时间的百分比(基本)");
	map.put("setupTimePercent","技能僵直时间的百分比");
	map.put("criticalHit","会心一击(总)");
	map.put("criticalHitA","会心一击(基本)");
	map.put("setupTimePercentA","技能僵直时间的百分比(基本)");
	map.put("luck","幸运值(总)");
	map.put("luckA","幸运值(基本)");
	map.put("unallocatedSkillPoint","未分配的技能点");
	map.put("skillLevels","玩家掌握的各种技能等级，0表示未掌握，下标表示职业中的第几个技能");
	map.put("openedAuraSkillIndex","当前启用的光环技能索引");
	map.put("id","用户ID");
	map.put("x","地图横坐标");
	map.put("y","地图纵坐标");
	map.put("viewWidth","可视区域宽");
	map.put("viewHeight","可视区域高");
	map.put("equipmentColumns","玩家身上的装备栏");
	map.put("faShuHuDunDamage","法术护盾");
	map.put("wuLiHuDunDamage","物理护盾");
	map.put("currentSuit","表明当前启用的是哪一套装备");
	map.put("teamMark","标注玩家的组队状态：2队长、1队员、0未组队");
	map.put("shield","护盾类型，-1代表没有护盾，玩家下线此变量需保存");
	map.put("attackRatingA","命中(基本)");
	map.put("dodgeA","回避(基本)");
	map.put("coolDownTimePercent","技能冷却时间的百分比");
	map.put("hold","定身");
    map.put("stun","眩晕");
    map.put("immunity","免疫");
    map.put("poison","中毒");
    map.put("weak","虚弱");
    map.put("invulnerable","无敌");
    map.put("dirty","数据是否有改动");
    map.put("lastUpdateTime","上次更新时间");
    map.put("horseName","坐骑名称");
    map.put("dailyTaskDeliverCount","当天日常任务交付的次数");
    map.put("lastDailyTaskDeliverTime","日常任务最后一次交付的时间");
    map.put("chatChannelStatus","玩家身上的聊天频道状态 0关闭 1开启， 下标为频道的类别");
    map.put("propertyPointAllocatePlan","属性点分配方案");
    map.put("mapName","地图名");
    map.put("fighting","战斗");
    map.put("commonAttackSpeed","普通攻击的攻击速度（毫秒为单位）");
    map.put("commonAttackRange","普通攻击的攻击距离（像素为单位）");
    map.put("cure","缓疗状态");
    map.put("toughness","韧性，克制对方暴击的能力");
    map.put("skillOneLevels","玩家掌握的第一系技能等级，0表示未掌握，下标表示职业中的第几个技能");
    map.put("skillTwoLevels","玩家掌握的第二系技能等级，0表示未掌握，下标表示职业中的第几个技能");
    map.put("openedAuraSkillID","当前启用的光环技能编号");
    map.put("alive","是否在游戏中");
    map.put("currentMapAreaName","当前地图区域名");
    map.put("shortcut","快捷键");
    map.put("xp","怒气值");
    map.put("totalXp","怒气值的满值");
    map.put("weaponDamageUpperLimit","武器伤害力的上限");
    map.put("weaponDamageLowerLimit","武器伤害力的下限");
    map.put("meleeAttackIntensity","近战攻击强度(总)");
    map.put("meleeAttackIntensityA","近战攻击强度(基本)");
    map.put("meleeAttackIntensityB","近战攻击强度");
    map.put("meleeAttackIntensityC","近战攻击强度(百分比)");
    map.put("spellAttackIntensity","法术攻击强度(总)");
    map.put("spellAttackIntensityA","法术攻击强度(基本)");
    map.put("spellAttackIntensityB","法术攻击强度");
    map.put("spellAttackIntensityC","法术攻击强度(百分比)");
    map.put("newlyEnterGameFlag","标记用户是否刚刚进入地图");
    map.put("loginServerTime","登陆服务器时间");
    map.put("politicalCamp","阵营");
    map.put("rmbyuanbao","角色自身的元宝，不绑定，不在不同角色间共享");
    map.put("totalRmbyuanbao","角色的全部元宝，包括账号充值得到的元宝");
    map.put("bindyuanbao","绑定元宝");
    map.put("money","游戏币");
    map.put("homeMapName","回城点所在的地图");
    map.put("homeX","回城点横坐标");
    map.put("homeY","回城点纵坐标");
    map.put("resurrectionMapName","复活点所在的地图");
    map.put("resurrectionX","复活点横坐标");
    map.put("resurrectionY","复活点纵坐标");
    map.put("quitGameTime","离线时间点");
    map.put("totalOfflineTime","总离线时间");
    map.put("exchangeableOfflineTime","可转化为经验的离线时间");
    map.put("gangName","公会");
    map.put("gangTitle","公会中的职位");
    map.put("","");
%>
<%@page import="java.io.ByteArrayOutputStream"%>
<%@page import="java.io.PrintStream"%>
<%@include file="../IPManager.jsp"%>
<html><head>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8">
</HEAD>
<BODY>

<h2>修改用户的属性</h2><br><br>
<%
	if(errorMessage != null){
		out.println("<center><table border='0' cellpadding='0' cellspacing='2' width='100%' bgcolor='#FF0000' align='center'>");
		out.println("<tr bgcolor='#FFFFFF' align='center'><td>");
		out.println("<font color='red'><pre>"+errorMessage+"</pre></font>");
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
用户属性列表：<br/>
<form id='f2' name='f2' method="post" action="mod_playerLookOnly.jsp"><input type='hidden' name='action' value='modify_player'>
<input type='hidden' name='playerid' value='<%=playerId %>'>
<table border="0" cellpadding="0" cellspacing="1" width="100%" bgcolor="#000000" align="center" style="table-layout: fixed">
<tr bgcolor="#C2CAF5" align="center">
<td>属性</td><td>属性描述</td><td>值的类型</td><td>原来的值</td><td>新设定的值</td></tr>	
<%
Class cl = Player.class;
Method ms[] = cl.getMethods();
ArrayList<Method> al = new ArrayList<Method>();
for(int i = 0 ; i < ms.length ; i++){
	if(ms[i].getName().length() > 3 && (ms[i].getName().startsWith("get") || ms[i].getName().startsWith("is")) 
			&& (ms[i].getModifiers() & Modifier.PUBLIC) != 0
			&& ms[i].getParameterTypes().length == 0){
		String name = ms[i].getName();
		if(name.startsWith("get"))
            name = "s" + name.substring(1);
        else
            name = "set" + name.substring(2);
		Class c = ms[i].getReturnType();
		if(c.isPrimitive() || c == String.class || c.toString().indexOf("class [") >= 0){
			try{
				Method m = cl.getMethod(name,new Class[]{c});
				if(m != null){
					al.add(ms[i]);
				}
			}catch(Exception e){
				
			}
		}
	}
}


for(int i = 0 ; i < al.size() ; i++){
	Method m = al.get(i);
	String o = "";
	if(player != null){
		if(m.getReturnType().getName().equals("java.lang.String")){
			
			try{
				o = "" +m.invoke(player,new Object[]{});
			}catch(Exception e){
				o ="出错了";
				e.printStackTrace();
			}
		}else{
			try{
				o = "" +gson.toJson(m.invoke(player,new Object[]{}));
			}catch(Exception ex){
				
			}
		}
	}
	String typeStr = "";
	if(m.getReturnType().getName().equals("[I")){
		typeStr = "int[]";
	}else if(m.getReturnType().getName().equals("[B")){
		typeStr = "byte[]";
	}else{
		typeStr = m.getReturnType().getName();
	}
	String name = "";
	if(m.getName().startsWith("is")){
		name = m.getName().substring(2);
	}else{
		name = m.getName().substring(3);
	}
	name = Character.toLowerCase(name.charAt(0)) + name.substring(1);
	StringBuffer sbm = new StringBuffer();


	try{
		Field f = cl.getField(name);

		int modifier = f.getModifiers();
		sbm.append(Modifier.toString(modifier));
		if( (modifier & Modifier.PRIVATE) != 0){
			sbm.append("private,");
		}
		if( (modifier & Modifier.PROTECTED) != 0){
			sbm.append("protected,");
		}
		if( (modifier & Modifier.PUBLIC) != 0){
			sbm.append("public,");
		}
		if( (modifier & Modifier.TRANSIENT) != 0){
			sbm.append("transient,");
		}
	}catch(Exception e){}
	%>
	<tr bgcolor="#FFFFFF">
	<td width="160" align="center" nowrap="nowrap" style="word-wrap: break-word;"><b><%= name %></b></td>
	<td width="160" align="center" nowrap="nowrap" style="word-wrap: break-word;"><b><%= map.get(name) %></b></td>
	<td width="160" align="center" nowrap="nowrap" style="word-wrap: break-word;"><i><%= typeStr %></i>	</td>
	<td width="160" align="center" nowrap="nowrap" style="word-wrap: break-word;"><%= o %></td>
	<td width="160" align="center" nowrap="nowrap" style="word-wrap: break-word;"><input type='text' name='<%=name %>' value ='' size='20'></td></tr><%
}


%>
<tr bgcolor="#FFFFFF"><td width="160" align="center" nowrap="nowrap" style="word-wrap: break-word;"><b>currentGame</b></td>
	<td width="160" align="center" nowrap="nowrap" style="word-wrap: break-word;"><b><%= "玩家当前所在地图" %></b></td>
	<td width="160" align="center" nowrap="nowrap" style="word-wrap: break-word;"><i><%= "Game" %></i>	</td>
	<td width="160" align="center" nowrap="nowrap" style="word-wrap: break-word;"><%= (player != null ?(player.getCurrentGame()!= null? player.getCurrentGame().gi.getName() : "-"):"") %></td>
	<td width="160" align="center" nowrap="nowrap" style="word-wrap: break-word;"></td></tr>
<%
//服务器类型为0代表为可修改的开发服务器
if(GameConstants.getInstance().getServerType() == 0){ %>
<tr bgcolor="#C2CAF5" align="center"><td><input type='submit' value='修  改'></td><td></td><td></td><td></td><td></td></tr>
<%}else{
	out.println("此服务器不能修改数据");
}

%>
</table>	
</form>

</BODY></html>
