<%@ page language="java" contentType="text/html; charset=utf-8"
    pageEncoding="utf-8"%>
<%@ page import="com.google.gson.*,com.xuanzhi.tools.cache.*,com.fy.engineserver.sprite.concrete.*,com.fy.engineserver.sprite.*,java.util.*,java.lang.reflect.*,com.google.gson.*"%>
<%@ page import="java.util.*,com.xuanzhi.tools.text.*,java.lang.reflect.*,com.fy.engineserver.datasource.buff.*,
com.fy.engineserver.sprite.*,com.xuanzhi.tools.transport.*,com.google.gson.*,com.fy.engineserver.datasource.career.*,com.fy.engineserver.datasource.skill.*,com.fy.engineserver.datasource.props.*,com.fy.engineserver.task.*" %>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<%@include file="IPManager.jsp" %><html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8">
<title>一级用户缓存</title>
<script type="text/javascript">
function subcheck(sm) {
	document.getElementById("sm").value=sm;
	document.form1.submit();
}
</script>
</head>
<body>
<%
try {
GamePlayerManager pmanager = (GamePlayerManager)PlayerManager.getInstance();
Hashtable<Integer,Player> idPlayerMap = pmanager.getIdPlayerMap();
Hashtable<String,Player> namePlayerMap = pmanager.getNamePlayerMap();
int idCount = idPlayerMap.size();
int nameCount = namePlayerMap.size();
String idStr = request.getParameter("id");
String name = request.getParameter("name");
Player player = null;
String smStr = request.getParameter("sm");
if(smStr != null && smStr.equals("0") && idStr != null) {
	int id = Integer.parseInt(idStr);
	player = idPlayerMap.get(id);
}
if(smStr != null && smStr.equals("1") && name != null) {
	player = namePlayerMap.get(name);
}
%>
<h2>一级用户缓存</h2>
<div style="padding:6px;">
	ID缓存中共有玩家数据:<%=idCount %>个<br>
	Name缓存中共有玩家数据:<%=nameCount %>个
</div>
<div style="padding:6px;">
<form name=form1 action="" method=get>
	从ID缓存中获取玩家数据:
	<input type=text size=18 name="id" id="id">
	<input type=button name=bt1 value=提交 onclick="subcheck(0)"><br>
	从Name缓存中获取玩家数据:
	<input type=text size=18 name="name" id="name">
	<input type=button name=bt2 value=提交 onclick="subcheck(1)"><br>
	<input type=hidden name=sm value="" id="sm">
</form>
</div>
<div style="padding:6px">
<%if(player != null) {%>

<!-- 状态 -->

<table border="0" cellpadding="0" cellspacing="1" width="100%" bgcolor="#000000" align="center">
<%StringBuffer stateStr = new StringBuffer();
if(player != null){
	if(player.isHold()){
		stateStr.append(" 定身");
	}
	if(player.isStun()){
		stateStr.append(" 眩晕");
	}
	if(player.isImmunity()){
		stateStr.append(" 免疫");
	}
	if(player.isPoison()){
		stateStr.append(" 中毒");
	}
	if(player.isWeak()){
		stateStr.append(" 虚弱");
	}
	if(player.isInvulnerable()){
		stateStr.append(" 无敌");
	}
	if(player.isFighting()){
		stateStr.append(" 战斗");
	}
%>
<tr bgcolor="#C2CAF5" align="center">
<td colspan="13">玩家<font color="red"><%=player.getId()%>(<%=player.getName() %>)</font>的状态</td>
</tr>
<tr bgcolor="#FFFFFF" align="center"><td colspan="13"><%=stateStr.length()==0?"无状态":stateStr.toString() %></td></tr>
<%
	ArrayList<Buff> buffs = player.getBuffs();
	if(buffs != null){
		%>
<tr bgcolor="#C2CAF5" align="center">
<td>Buff</td>
<td>BuffTemplate</td>
<td>图标</td>
<td>发起者</td>
<td>Buff级别</td>
<td>开始时间</td>
<td>失效时间</td>
<td>心跳开始时间</td>
<td>描述</td>
<td>bufferType</td>
<td>是否要与客户端同步</td>
<td>Buff是否有利</td>
<td>是否永久</td>
</tr>
		<%
		for(Buff buff : buffs){
			if(buff != null){
%>
<tr bgcolor="#FFFFFF" align="center">
<td><%=buff.getTemplateName() %></td>
<td><a href="../buffbyname.jsp?buffName=<%=buff.getTemplate()!=null?buff.getTemplate().getName():"" %>"><%=buff.getTemplate()!=null?buff.getTemplate().getName():"" %></a></td>
<td><img src="/game_server/imageServlet?id=<%=buff.getIconId() %>"></td>
<td><%=buff.getCauser()!=null?buff.getCauser().getName():""%></td>
<td><%=buff.getLevel()%></td>
<td><%=buff.getStartTime()%></td>
<td><%=buff.getInvalidTime()%></td>
<td><%=buff.getHeartBeatStartTime()%></td>
<td><%=buff.getDescription()%></td>
<td><%=buff.getBufferType()%></td>
<td><%=buff.isSyncWithClient()?"同步":"不同步"%></td>
<td><%=buff.isAdvantageous()?"有利":"有害"%></td>
<td><%=buff.isForover()?"不依赖时间":"依赖时间"%></td>
</tr>
<%}}}}%>
</table>
<%
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
	map.put("scheme","");
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
	map.put("","");
%>
<br>
用户属性列表：<br/>
<table border="0" cellpadding="0" cellspacing="1" width="100%" bgcolor="#000000" align="center" style="table-layout: fixed">
<tr bgcolor="#C2CAF5" align="center">
<td>属性</td><td>属性描述</td><td>值的类型</td><td>原来的值</td></tr>	
<%
Class cl = Player.class;
Method ms[] = cl.getMethods();
ArrayList<Method> al = new ArrayList<Method>();
for(int i = 0 ; i < ms.length ; i++){
	if(ms[i].getName().length() > 3 && (ms[i].getName().startsWith("get") || ms[i].getName().startsWith("is")) 
			&& (ms[i].getModifiers() & Modifier.PUBLIC) != 0
			&& ms[i].getParameterTypes().length == 0){
		String mname = ms[i].getName();
		mname = "s" + mname.substring(1);
		Class c = ms[i].getReturnType();
		if(c.isPrimitive() || c == String.class || c.toString().indexOf("class [") >= 0){
			try{
				Method m = cl.getMethod(mname,new Class[]{c});
				if(m != null){
					al.add(ms[i]);
				}
			}catch(Exception e){
				
			}
		}
	}
}
Gson gson = new Gson();
for(int i = 0 ; i < al.size() ; i++){
	Method m = al.get(i);
	String o = "";
	if(player != null){
		if(m.getReturnType().getName().equals("java.lang.String")){
			o = "" +m.invoke(player,new Object[]{});
		}else{
			o = "" +gson.toJson(m.invoke(player,new Object[]{}));
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
	String mname = m.getName().substring(3);
	mname = Character.toLowerCase(mname.charAt(0)) + mname.substring(1);
	StringBuffer sbm = new StringBuffer();


	try{
		Field f = cl.getField(mname);

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
	<td width="160" align="center" nowrap="nowrap" style="word-wrap: break-word;"><b><%= mname %></b></td>
	<td width="160" align="center" nowrap="nowrap" style="word-wrap: break-word;"><b><%= map.get(mname) %></b></td>
	<td width="160" align="center" nowrap="nowrap" style="word-wrap: break-word;"><i><%= typeStr %></i>	</td>
	<td width="160" align="center" nowrap="nowrap" style="word-wrap: break-word;"><%= o %></td><%
}
%>
</table>	
	<%} else {
		if(idStr != null || name != null) {
			out.println("<font color=red>用户数据不在缓存中</font>");
		}
	}
} catch(Exception e) {
	e.printStackTrace();
}
%>
</div>
</body>
</html>
