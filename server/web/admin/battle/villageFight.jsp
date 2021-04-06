<%@ page language="java" contentType="text/html; charset=utf-8" pageEncoding="utf-8"%>
<%@ page import="
java.util.*,
com.fy.engineserver.battlefield.*,
com.fy.engineserver.battlefield.concrete.* " %>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<%@page import="com.fy.engineserver.battlefield.concrete.BattleFieldLineupService.WaittingItem"%>
<%@page import="com.fy.engineserver.sprite.Player"%>
<%@page import="com.xuanzhi.tools.text.DateUtil"%>
<%@include file="../IPManager.jsp" %>
<%@page import="com.fy.engineserver.tournament.manager.TournamentManager"%>
<%@page import="com.fy.engineserver.sprite.PlayerManager"%>
<%@page import="com.fy.engineserver.activity.village.manager.VillageFightManager"%>
<%@page import="com.fy.engineserver.country.manager.CountryManager"%>
<%@page import="com.fy.engineserver.activity.village.data.OreNPCData"%>
<%@page import="com.fy.engineserver.sprite.npc.OreNPC"%><html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8">
<link rel="stylesheet" type="text/css" href="../../css/common.css" />
<link rel="stylesheet" type="text/css" href="../../css/table.css" />
<title>战场</title>
<% 
	VillageFightManager vfm = VillageFightManager.getInstance();
	PlayerManager pm = PlayerManager.getInstance();

	String selectName = request.getParameter("selectName");
	String selectValue = request.getParameter("selectValue");
	String action = request.getParameter("action");
	if(action != null && "qiehuantian".equals(action)){
		vfm.每天凌晨更新数据(false);
	}
	if(action != null && "qiehuanzhou".equals(action)){
		vfm.每天凌晨更新数据(true);
	}
	if(selectName != null && selectValue != null && !selectValue.trim().equals("")){
		int value = Integer.parseInt(selectValue.trim());
		if(selectName.equals("0")){
			VillageFightManager.每天最早申请时间 = value;
		}else if(selectName.equals("1")){
			VillageFightManager.每天最晚申请时间 = value;
		}else if(selectName.equals("2")){
			VillageFightManager.每天战斗开始时间 = value;
		}else if(selectName.equals("3")){
			VillageFightManager.战斗持续时间 = value;
		}else if(selectName.equals("4")){
			VillageFightManager.申请矿战花费 = value;
		}else if(selectName.equals("5")){
			VillageFightManager.大圈半径 = value;
		}else if(selectName.equals("6")){
			VillageFightManager.cooldown = value;
		}else if(selectName.equals("7")){
			VillageFightManager.占领所需时间 = value;
		}else if(selectName.equals("8")){
			vfm.hour = value;
		}
		out.println("修改成功");
	}
%>
</head>
<body>
<h2>村庄战情况</h2>
<a href="./villageFight.jsp">刷新此页面</a>
<br>
<br>
<table>
<tr bgcolor="#C2CAF5" align="center">
<td>描述</td>
<td>值</td>
</tr>
<tr>
<td>每天最早申请时间</td>
<td><%=VillageFightManager.每天最早申请时间 %></td>
</tr>
<tr>
<td>每天最晚申请时间</td>
<td><%=VillageFightManager.每天最晚申请时间 %></td>
</tr>
<tr>
<td>每天战斗开始时间</td>
<td><%=VillageFightManager.每天战斗开始时间 %></td>
</tr>
<tr>
<td>战斗持续时间</td>
<td><%=VillageFightManager.战斗持续时间 %></td>
</tr>
<tr>
<td>申请矿战花费</td>
<td><%=VillageFightManager.申请矿战花费 %></td>
</tr>
<tr>
<td>大圈半径</td>
<td><%=VillageFightManager.大圈半径 %></td>
</tr>
<tr>
<td>cooldown</td>
<td><%=VillageFightManager.cooldown %></td>
</tr>
<tr>
<td>占领所需时间</td>
<td><%=VillageFightManager.占领所需时间 %></td>
</tr>
<tr>
<td>系统记录时间</td>
<td><%=vfm.hour %></td>
</tr>
</table>
<table>
<tr><td>国家</td><td>占有灵矿的行会<td>申请攻打灵矿的行会</td></tr>
<%
byte[] types = vfm.vd.getOreType();
for(int i = 1; i <= CountryManager.国家C; i++){
	%>
	<tr><td><%=CountryManager.得到国家名(i) %></td>
	
	<td><%
	StringBuffer sb = new StringBuffer();
	Hashtable<Byte,OreNPCData[]> ondMap = vfm.vd.countryOres;
	OreNPCData[] onds = ondMap.get((byte)i);
	for(int j = 0; j < onds.length; j++){
		sb.append(onds[j].getArrayIndex());
		sb.append("-");
		sb.append(onds[j].getJiazuId());
		sb.append("-");
		sb.append(vfm.得到矿的名字(types[j]));
		sb.append("<br/>");
	}
	out.print(sb.toString());
	%></td>
		
	<td><%
	Hashtable<Byte,long[]> prepareFightJiazuIdMap = vfm.vd.countryPrepareToFightOres;
	sb = new StringBuffer();
	for(int j = 0; j < onds.length; j++){
		sb.append(prepareFightJiazuIdMap.get((byte)i)[j]);
		sb.append("<br/>");
	}
	out.print(sb.toString());
	%></td>
	</tr>
	<%
}
%>
<tr><td>国家</td><td colspan="2">灵矿信息</td></tr>
<%

ArrayList<OreNPC> oreNPCList = vfm.oreNPCList;
for(int i = 0; i < oreNPCList.size(); i++){
	OreNPC on = oreNPCList.get(i);
	%>
	<tr><td><%=CountryManager.得到国家名(on.getCountry()) %></td>
	
	<td colspan="2"><%
	StringBuffer sb = new StringBuffer();

	sb.append(on.arrayIndex);
	sb.append(" ");
	sb.append("占领家族:"+on.jiazuId);
	sb.append(" ");
	sb.append("title:"+on.getTitle());
	sb.append("<br/>");
	out.print(sb.toString());
	%></td>
	</tr>
	<%
}
%>
</table>
<form name="f1">
<table>
<tr>
<td>
要修改的内容
</td>
</tr>
<tr>
<td>
<select id="selectName" name="selectName">
<option value="0">每天最早申请时间
<option value="1">每天最晚申请时间
<option value="2">每天战斗开始时间
<option value="3">战斗持续时间
<option value="4">申请矿战花费
<option value="5">大圈半径
<option value="6">cooldown
<option value="7">占领所需时间
<option value="8">系统记录时间

</select>
<input type="text" id="selectValue" name="selectValue">
<input type="submit" value="修改">
</td>
</tr>
</table>
</form>
<form name="f2">
<input type="hidden" name="action" value="qiehuantian">
<input type="submit" value="切换天">
</form>
<form name="f3">
<input type="hidden" name="action" value="qiehuanzhou">
<input type="submit" value="切换周">
</form>
</body>
</html>
