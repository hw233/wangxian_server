<%@page import="java.util.List"%>
<%@page import="com.fy.engineserver.achievement.GameDataRecord"%>
<%@page import="com.fy.engineserver.achievement.AchievementManager"%>
<%@page import="com.fy.engineserver.newBillboard.BillboardStatDate"%>
<%@page import="com.fy.engineserver.sprite.concrete.GamePlayerManager"%>
<%@page import="com.fy.engineserver.sprite.Player"%>
<%@page import="com.fy.engineserver.newBillboard.BillboardStatDateManager"%>
<%@page import="com.fy.engineserver.newBillboard.date.brick.BrickALLBillboard"%>
<%@ page language="java" contentType="text/html; charset=utf-8"
	pageEncoding="utf-8"%>

<%@page import="com.fy.engineserver.sprite.pet.Pet"%>
<%@page import="com.fy.engineserver.sprite.pet.PetManager"%><head>
<title>test</title>
</head>
<body>

<%
	String checkId = request.getParameter("checkId");
	if(checkId != null && !checkId.isEmpty()){
		Pet pet = PetManager.getInstance().getPet(Long.parseLong(checkId));
		if(pet == null){
			out.print("【检查玩家属性】id为"+checkId+"宠物不存在");
			return;
		}
		out.print("[总法防"+pet.getMagicDefence()+"] [A:"+pet.getMagicDefenceA()+"] [B:"+pet.getMagicDefenceB()+"] [C:"+pet.getMagicDefenceC()+"] [D:"+pet.getMagicDefenceD()+"]<br>");
		out.print("[总物防"+pet.getPhyDefence()+"] [A:"+pet.getPhyDefenceA()+"] [B:"+pet.getPhyDefenceB()+"] [C:"+pet.getPhyDefenceC()+"] [D:"+pet.getPhyDefenceD()+"]");
	}
	String repairId = request.getParameter("repairId");
	if(repairId != null && !repairId.isEmpty()){
		Pet pet = PetManager.getInstance().getPet(Long.parseLong(repairId));
		if(pet == null){
			out.print("【修复玩家属性】id为"+checkId+"宠物不存在");
			return;
		}
		if(pet.getMagicDefenceC() < 0){
			pet.setMagicDefenceC(0);
		}
		if(pet.getPhyDefenceC() < 0){
			pet.setPhyDefenceC(0);
		}
		out.print("[总法防"+pet.getMagicDefence()+"] [A:"+pet.getMagicDefenceA()+"] [B:"+pet.getMagicDefenceB()+"] [C:"+pet.getMagicDefenceC()+"] [D:"+pet.getMagicDefenceD()+"]<br>");
		out.print("[总物防"+pet.getPhyDefence()+"] [A:"+pet.getPhyDefenceA()+"] [B:"+pet.getPhyDefenceB()+"] [C:"+pet.getPhyDefenceC()+"] [D:"+pet.getPhyDefenceD()+"]");
	}
%>

<form>
	宠物id：<input type="text" name="checkId" value="" />
	<input type="submit" value="检查玩家属性" />
</form>
<form>
	宠物id：<input type="text" name="repairId" value="" />
	<input type="submit" value="修复玩家属性" />
</form>
<br />
 