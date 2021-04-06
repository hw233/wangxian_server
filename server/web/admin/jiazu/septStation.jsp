<%@page import="com.fy.engineserver.sprite.npc.TaskCollectionNPC"%>
<%@page import="com.fy.engineserver.sprite.npc.SeptStationNPC"%>
<%@page
	import="com.fy.engineserver.septstation.JiazuBossDamageRecord"%>
<%@page import="com.fy.engineserver.jiazu.service.JiazuManager"%>
<%@page import="com.fy.engineserver.jiazu.JiazuMember"%>
<%@page import="com.fy.engineserver.sprite.monster.Monster"%>
<%@page import="com.fy.engineserver.core.LivingObject"%>
<%@page import="com.fy.engineserver.sprite.npc.NPC"%>
<%@page import="com.fy.engineserver.sprite.Player"%>
<%@page
	import="com.fy.engineserver.septbuilding.entity.SeptBuildingEntity"%>
<%@page
	import="com.fy.engineserver.septstation.service.SeptStationManager"%>
<%@page import="com.fy.engineserver.septstation.SeptStation"%>
<%@page import="java.util.*"%>
<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">

<%
	String jiazuIds = request.getParameter("jiazuId");
	if (jiazuIds == null || "".equals(jiazuIds)) {
		out.print("id异常");
		return;
	}
	SeptStation septStation = SeptStationManager.getInstance().getSeptStationBySeptId(Long.valueOf(jiazuIds));
	if (septStation == null) {
		out.print("家族还没有驻地");
		return;
	}
%>
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title>家族驻地</title>
</head>
<body>
	
	<!-- form action="./septCallBoss.jsp?jiazuId=<%=jiazuIds%>" name="f1"
		method="post">
		<table>
			<tr>
				<td>开启boss<input type="submit"></td>
			</tr>
		</table>
	</form>
	 -->
	<hr />
	<BR />
	<table
		style="text-align: right; font-size: 12px; float: left; border-width: 10px;"
		border="1">
		<tr bgcolor="#FFFF33">
			<td colspan="5">家族建筑</td>
		</tr>
		<tr bgcolor="#FFFF33">
			<td width="60px">建造状态</td>
			<td width="60px">ID</td>
			<td width="60px">名字</td>
			<td width="60px">等级</td>
			<td width="60px">位置</td>
		</tr>
		<tr bgcolor="#FFFF33">
			<td bgcolor="#FFFFFF">关联地表</td>
			<td width="60px">ID</td>
			<td width="60px">名字</td>
			<td width="60px">等级</td>
			<td width="60px">位置</td>
		</tr>
		<%
			for (SeptBuildingEntity sbe : septStation.getBuildingEntities()) {
				NPC npc = sbe.getNpc();
		%>
		<tr bgcolor="#8CACE8">
			<td width="60px"><%=sbe.isInBuild() ? "建造中(" + sbe.getCurrTaskNum() + "/" + sbe.getLvUpTaskNum() + ")" : "空闲"%></td>
			<td width="60px"><%=npc.getId()%></td>
			<td width="70px"><%=npc.getName()%></td>
			<td width="60px"><%=sbe.getBuildingState().getLevel()%></td>
			<td width="60px"><%="(" + npc.getX() + "," + npc.getY() + ")"%></td>
		</tr>
		<%
			}
		%>
	</table>
	<%
		List<Player> playerList = new ArrayList<Player>();
		List<Monster> monsterList = new ArrayList<Monster>();
		List<NPC> npcList = new ArrayList<NPC>();
		for (LivingObject lo : septStation.getGame().getLivingObjects()) {
			if (lo instanceof Player) {
				playerList.add((Player) lo);
			} else if (lo instanceof Monster) {
				monsterList.add((Monster) lo);
			} 
		}
	%>
	<table
		style="text-align: right; font-size: 12px; float: left; border-width: 10px;"
		border="1">
		<tr bgcolor="#FFFF33">
			<td colspan="6">当前怪物</td>
		</tr>
		<tr bgcolor="#FFFF33">
			<td>怪物ID</td>
			<td>怪物名字</td>
			<td>怪物等级</td>
			<td>怪物模板ID</td>
			<td>怪物位置</td>
			<td>怪物HP</td>
		</tr>
		<%
			int index = 0;
			for (Monster monster : monsterList) {
		%>
		<tr bgcolor="<%=(index++ % 2) == 0 ? "#FFFFFF" : "#8CACE8"%>">
			<td><%=monster.getId()%></td>
			<td><%=monster.getName()%></td>
			<td><%=monster.getLevel()%></td>
			<td title="<%=monster.getClass()%>"><%=monster.getSpriteCategoryId()%></td>
			<td><%="(" + monster.getX() + "," + monster.getY() + ")"%></td>
			<td
				style="background-color: <%=(monster.getMaxHP() == monster.getHp()) ? "" : "red"%>"><%=(monster.getHp() == monster.getMaxHP()) ? ("[满]" + monster.getMaxHP()) : (monster.getHp() + "/" + monster.getMaxHP())%></td>
		</tr>
		<%
			}
			index = 0;
			for (NPC npc : npcList) {
		%>
		<tr bgcolor="<%=(index++ % 2) == 0 ? "#FFFFFF" : "#8CACE8"%>" style="border-color: orange;">
			<td><%=npc.getId()%></td>
			<td><%=npc.getName()%></td>
			<td><%=npc.getLevel()%></td>
			<td title="<%=npc.getClass()%>"><%=npc.getnPCCategoryId()%></td>
			<td><%="(" + npc.getX() + "," + npc.getY() + ")"%></td>
			<td
				style="background-color: <%=(npc.getMaxHP() == npc.getHp()) ? "" : "red"%>"><%=(npc.getHp() == npc.getMaxHP()) ? ("[满]" + npc.getMaxHP()) : (npc.getHp() + "/" + npc.getMaxHP())%></td>
		</tr>
		<%
			}
		%>
	</table>
	<table
		style="text-align: right; font-size: 12px; float: left; border-width: 10px;"
		border="1">
		<tr bgcolor="#FFFF33">
			<td colspan="7">当前角色</td>
		</tr>
		<tr bgcolor="#FFFF33">
			<td>角色Id</td>
			<td>角色名字</td>
			<td>角色等级</td>
			<td>角色性别</td>
			<td>角色职位</td>
			<td>角色位置</td>
			<td>角色HP</td>
		</tr>
		<%
			index = 0;
			for (Player player : playerList) {
				JiazuMember member = JiazuManager.getInstance().getJiazuMember(player.getId(), septStation.getJiazu().getJiazuID());
		%>
		<tr bgcolor="<%=(index++ % 2) == 0 ? "#FFFFFF" : "#8CACE8"%>">
			<td><%=player.getId()%></td>
			<td><%=player.getName()%></td>
			<td><%=player.getLevel()%></td>
			<td><%=player.getSex()%></td>
			<td><%=member == null ? "---" : septStation.getJiazu().getTitleName(member.getTitle())%></td>
			<td><%="(" + player.getX() + "," + player.getY() + ")"%></td>
			<td
				style="background-color: <%=(player.getMaxHP() == player.getHp()) ? "" : "red"%>"><%=(player.getMaxHP() == player.getHp()) ? ("[满]" + player.getMaxHP()) : (player.getHp() + "/" + player.getMaxHP())%></td>
		</tr>
		<%
			}
		%>
	</table>
	<table
		style="text-align: right; font-size: 12px; float: none; border-width: 10px;border-color: red;"
		border="1">
		<tr bgcolor="#FFFF33">
			<td colspan="4">BOSS伤害量</td>
		</tr>
		<tr bgcolor="#FFFF33">
			<td>角色ID</td>
			<td>角色名字</td>
			<td>伤害量</td>
			<td>百分比</td>
		</tr>
		<%
			long maxHp = septStation.getThisBoss() == null ? -1 : septStation.getThisBoss().getMaxHP();
			List<JiazuBossDamageRecord> list = septStation.getBossDamageRecords();
			for (JiazuBossDamageRecord dr : list) {
		%>
		<tr>
			<td><%=dr.getPlayerId()%></td>
			<td><%=dr.getPlayerName()%></td>
			<td><%=dr.getDamage()%></td>
			<td><%=SeptStation.df.format(((double) dr.getDamage()) / maxHp)%></td>
		</tr>
		<%
			}
		%>
	</table>
</body>
</html>