<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<%@page import="java.lang.reflect.Field"%>
<%@page import="com.fy.engineserver.sprite.horse.HorseOtherData"%>
<%@page import="com.fy.engineserver.sprite.horse2.model.HorseRankModel"%>
<%@page import="com.fy.engineserver.util.server.TestServerConfigManager"%>
<%@page import="com.fy.engineserver.sprite.pet2.Pet2Manager"%>
<%@page import="com.fy.engineserver.sprite.pet2.PetsOfPlayer"%>
<%@page import="com.fy.engineserver.sprite.horse2.manager.Horse2Manager"%>
<%@page import="com.fy.engineserver.sprite.horse2.model.HorseAttrModel"%>
<%@page import="java.util.Arrays"%>
<%@page import="java.util.List"%>
<%@page import="com.fy.engineserver.sprite.horse2.instance.Horse2RelevantEntity"%>
<%@page import="com.fy.engineserver.sprite.horse2.manager.Horse2EntityManager"%>
<%@page import="com.fy.engineserver.sprite.horse.HorseManager"%>
<%@page import="com.fy.engineserver.sprite.horse.Horse"%>
<%@page import="java.util.ArrayList"%>
<%@page import="com.fy.engineserver.sprite.concrete.GamePlayerManager"%>
<%@page import="com.fy.engineserver.sprite.Player"%>
<%@ page language="java" contentType="text/html; charset=utf-8"
	pageEncoding="utf-8"%>
<head>
</head>
<body>
	<%
		String action = request.getParameter("action");
		String playerId = request.getParameter("playerId");
		String horseId = request.getParameter("horseId");
		String refreshType = request.getParameter("refreshType");
		String skillIds = request.getParameter("skillIds");
		String skillLevels = request.getParameter("skillLevels");
		String colorType1 = request.getParameter("colorType1");
		action = action == null ? "" : action;
		colorType1 = colorType1 == null ? "" : colorType1;
		skillLevels = skillLevels == null ? "" : skillLevels;
		skillIds = skillIds == null ? "" : skillIds;
		horseId = horseId == null ? "" : horseId;
		playerId = playerId == null ? "" :playerId;
		refreshType = refreshType == null ? "" :refreshType;
		
		if (!playerId.isEmpty()) {
			try {
				Player tempP = GamePlayerManager.getInstance().getPlayer(playerId);
				ArrayList<Long> hIds  = tempP.getCurrSoul().getHorseArr();
				int index = 0;
				for (int i=0; i<hIds.size(); i++) {
					Horse h = HorseManager.getInstance().getHorseById(hIds.get(i), tempP);
					if (!h.isFly()) {
						out.println("["+(index++)+"] [坐骑名:" + h.getHorseName() + "] [坐骑显示名:"+h.getHorseShowName()+"] [坐骑id:" + h.getHorseId() + "][技能:"+Arrays.toString(h.getOtherData().getSkillList())+"][技能等级:" + Arrays.toString(h.getOtherData().getSkillLevel()) +"]<br>");
						out.println("[color:" + h.getColorType() + "] [rank" + h.getRank() + "] [bloodStar:" + h.getBloodStar() + "][icon:"+h.getIcon()+"] [career:"+h.getCareer()+"]<br>");
						out.println("[avatarKey:" + h.getAvataKey() + "] [avatar:" + h.getAvata() + "]<br>");
						out.println("[体力值:" + h.getEnergy() + "][是否战斗坐骑 :" + h.isFight() + "]" + "<br>");
					}
				}
				out.println("<br><br>");
			}catch(Exception e){}
		} 
		
		if ("receiveReward".equals(action)) {
			if(!TestServerConfigManager.isTestServer()) {
				out.println("不是测试服，不允许修改");
				return;
			}
			Player tempP = GamePlayerManager.getInstance().getPlayer(playerId);
			int[] result = Horse2EntityManager.instance.refreshSkill(tempP, 0, Long.parseLong(horseId));
			out.println("刷新得到的技能:" + result[0] + "&&免费剩余次数:" + result[1]);
		} else if ("checkAttrData".equals(action)) {
			byte natural = Byte.parseByte(playerId);
			int colorType = Integer.parseInt(colorType1);
			int rankStar = Integer.parseInt(horseId);
			int bloodStar = Integer.parseInt(refreshType);
			int[] skills1 = null;
			int[] skillLevels1 = null; 
			if(!skillIds.isEmpty()){
				String[] str = skillIds.split(",");
				String[] str2 = skillLevels.split(",");
				skills1 = new int[str.length];
				skillLevels1 = new int[str.length];
				for (int i=0; i<str.length; i++) {
					skills1[i] = Integer.parseInt(str[i]);
					skillLevels1[i] = Integer.parseInt(str2[i]);
				}
			} else {
				skills1 = new int[0];
				skillLevels1 = new int[0]; 
			}
			HorseAttrModel ham = Horse2Manager.instance.getNewHorseAttrModel(111111L, natural, colorType, rankStar,
					bloodStar, skills1, skillLevels1);
			out.println(ham + "<br>");
		} else if ("setXueMai".equals(action)) {
			if(!TestServerConfigManager.isTestServer()) {
				out.println("不是测试服，不允许修改");
				return;
			}
			Player tempP = GamePlayerManager.getInstance().getPlayer(playerId);
			PetsOfPlayer bean0 = Pet2Manager.getInst().findPetsOfPlayer(tempP);
			bean0.setXueMai(Long.parseLong(horseId));
			out.println("设置血脉值成功!!");
			PetsOfPlayer bean1 = Pet2Manager.getInst().findPetsOfPlayer(tempP);
			out.println("当前血脉值：" + bean1.getXueMai() + "<br>");
		} else if ("setEnergy".equals(action)) {
			if(!TestServerConfigManager.isTestServer()) {
				out.println("不是测试服，不允许修改");
				return;
			}
			Player tempP = GamePlayerManager.getInstance().getPlayer(playerId);
			Horse horse = HorseManager.getInstance().getHorseById(Long.parseLong(horseId), tempP);
			horse.setEnergy(Integer.parseInt(refreshType));
			out.println("设置成功");
		} else if ("recorverAttr".equals(action)) {
			Player tempP = GamePlayerManager.getInstance().getPlayer(playerId);
			if (tempP.isOnline()) {
				out.println("需要玩家下线才可操作！");
				return ;
			}
			Horse hors1 = HorseManager.getInstance().getHorseById(Long.parseLong(horseId), tempP);
			if (hors1 == null) {
				out.println("当前玩家元神没有找到相应坐骑！");
				return;
			}
			boolean add = hors1.isInited();
			Field ff = Horse.class.getDeclaredField("inited");
			ff.setAccessible(true);
			ff.set(hors1, false);
			hors1.setOldBasicAttr(null);
			hors1.init();
			if (!add && hors1.getOtherData().getRankStar() > 1 && tempP.isIsUpOrDown() && tempP.getMaxHPB() < hors1.getMaxHPB()) {
				tempP.addHorseProperty(hors1);
			}
			if (!add && hors1.getOtherData().getRankStar() > 1 && !tempP.isIsUpOrDown() && tempP.getMaxHPB() <= 1000) {
				tempP.addHorseProperty(hors1);
			}
			out.println("坐骑属性修正完毕!");
		} else if ("setBloodStar".equals(action)) {
			if(!TestServerConfigManager.isTestServer()) {
				out.println("不是测试服，不允许修改");
				return;
			}
			if (Integer.parseInt(refreshType) <0 || Integer.parseInt(refreshType)>20) {
				out.println("值不对!!!");
				return ;
			}
			Player plo = GamePlayerManager.getInstance().getPlayer(playerId);
			Horse h1 = HorseManager.getInstance().getHorseById(Long.parseLong(horseId), plo);
			HorseOtherData hod = h1.getOtherData();
			out.println("当前血脉星级:" + hod.getBloodStar() + "<br>");
			h1.setBloodStar(Integer.parseInt(refreshType));
			hod.setBloodStar(Integer.parseInt(refreshType));
			h1.setOtherData(hod);
			h1.initBasicAttr();
			out.println("设置后血脉星级:" + h1.getBloodStar() + "<br>");
		}
	%>
	<form action="horse2Opt.jsp" method="post">
		<input type="hidden" name="action" value="checkHorseId" />角色名   :
		<input type="text" name="playerId" value="<%=playerId%>" />
		<input type="submit" value="查询坐骑id" />
	</form>
	<br />
	<form action="horse2Opt.jsp" method="post">
		<input type="hidden" name="action" value="receiveReward" />角色名  :
		<input type="text" name="playerId" value="<%=playerId%>" />坐骑id:
		<input type="text" name="horseId" value="<%=horseId%>" />刷新类型(0免费,1普通道具,2高级道具):
		<input type="text" name="refreshType" value="<%=refreshType%>" />
		<input type="submit" value="刷新坐骑技能" />
	</form>
	<br />
	<form action="horse2Opt.jsp" method="post">
		<input type="hidden" name="action" value="setXueMai" />角色名  :
		<input type="text" name="playerId" value="<%=playerId%>" />血脉值:
		<input type="text" name="horseId" value="<%=horseId%>" />
		<input type="submit" value="设置血脉值" />
	</form>
	<br />
	<form action="horse2Opt.jsp" method="post">
		<input type="hidden" name="action" value="setEnergy" />角色名  :
		<input type="text" name="playerId" value="<%=playerId%>" />坐骑id:
		<input type="text" name="horseId" value="<%=horseId%>" />体力:
		<input type="text" name="refreshType" value="<%=refreshType%>" />
		<input type="submit" value="设置坐骑体力值" />
	</form>
	<br />
	<form action="horse2Opt.jsp" method="post">
		<input type="hidden" name="action" value="setBloodStar" />角色名  :
		<input type="text" name="playerId" value="<%=playerId%>" />坐骑id:
		<input type="text" name="horseId" value="<%=horseId%>" />血脉星级:
		<input type="text" name="refreshType" value="<%=refreshType%>" />
		<input type="submit" value="设置坐骑血脉星级" />
	</form>
	<br />
	<form action="horse2Opt.jsp" method="post">
		<input type="hidden" name="action" value="recorverAttr" />角色名  :
		<input type="text" name="playerId" value="<%=playerId%>" />坐骑id:
		<input type="text" name="horseId" value="<%=horseId%>" />
		<input type="submit" value="玩家坐骑属性负值修正（需要玩家下线）" />
	</form>
	<br />
	<form action="horse2Opt.jsp" method="post">
		<input type="hidden" name="action" value="checkAttrData" />职业  :
		<input type="text" name="playerId" value="<%=playerId%>" />颜色:
		<input type="text" name="colorType1" value="<%=colorType1%>" />阶星级:
		<input type="text" name="horseId" value="<%=horseId%>" />血脉星级:
		<input type="text" name="refreshType" value="<%=refreshType%>" />技能(多个用,分隔):
		<input type="text" name="skillIds" value="<%=skillIds%>" />技能等级(多个用,分隔):
		<input type="text" name="skillLevels" value="<%=skillLevels%>" />
		<input type="submit" value="测试坐骑基础数值计算" />
	</form>
	<br />  
	
	<%-- <form action="horse2Opt.jsp" method="post">
		<input type="hidden" name="action" value="strongerRank" />角色名  :
		<input type="text" name="playerId" value="<%=playerId%>" />坐骑id:
		<input type="text" name="horseId" value="<%=horseId%>" />刷新类型(0免费,1普通道具,2高级道具):
		<input type="text" name="refreshType" value="<%=refreshType%>" />
		<input type="submit" value="坐骑阶培养" />
	</form>
	<br />
	<form action="horse2Opt.jsp" method="post">
		<input type="hidden" name="action" value="StrongBlood" />角色名  :
		<input type="text" name="playerId" value="<%=playerId%>" />坐骑id:
		<input type="text" name="horseId" value="<%=horseId%>" />刷新类型(0免费,1普通道具,2高级道具):
		<input type="text" name="refreshType" value="<%=refreshType%>" />
		<input type="submit" value="坐骑血脉升星" />
	</form>
	<br />
	<form action="horse2Opt.jsp" method="post">
		<input type="hidden" name="action" value="StongerColor" />角色名  :
		<input type="text" name="playerId" value="<%=playerId%>" />坐骑id:
		<input type="text" name="horseId" value="<%=horseId%>" />刷新类型(0免费,1普通道具,2高级道具):
		<input type="text" name="refreshType" value="<%=refreshType%>" />
		<input type="submit" value="坐骑升颜色" />
	</form>
	<br /> --%>
</body>
</html>
