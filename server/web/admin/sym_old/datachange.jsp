<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<%@page import="com.fy.engineserver.sprite.Soul"%>
<%@page import="com.fy.engineserver.newtask.NewDeliverTask"%>
<%@page import="com.fy.engineserver.newtask.NewDeliverTaskManager"%>
<%@page import="org.apache.commons.lang.ArrayUtils"%>
<%@page import="java.util.Iterator"%>
<%@page import="com.fy.engineserver.activity.RefreshSpriteManager"%>
<%@page import="com.fy.engineserver.activity.RefreshSpriteManager.RefreshSpriteData"%>
<%@page import="java.util.Map"%>
<%@page import="com.fy.engineserver.sprite.pet.PetManager"%>
<%@page import="com.xuanzhi.tools.cache.LruMapCache"%>
<%@page import="com.fy.engineserver.newtask.DeliverTask"%>
<%@page import="com.fy.engineserver.newtask.TaskEntityManager"%>
<%@page import="com.fy.engineserver.sprite.CountdownAgent"%>
<%@page import="com.fy.engineserver.message.NOTICE_CLIENT_COUNTDOWN_REQ"%>
<%@page import="com.fy.engineserver.core.g2d.Point"%>
<%@page import="com.fy.engineserver.core.Game"%>
<%@page import="com.fy.engineserver.newBillboard.BillboardDate"%>
<%@page import="com.fy.engineserver.newBillboard.Billboard"%>
<%@page import="com.fy.engineserver.newBillboard.BillboardsManager"%>
<%@page import="com.fy.engineserver.billboard.concrete.ExpBillboards"%>
<%@page import="com.fy.engineserver.masterAndPrentice.MasterPrentice"%>
<%@page import="com.fy.engineserver.society.Relation"%>
<%@page import="com.fy.engineserver.society.SocialManager"%>
<%@page import="com.fy.engineserver.jiazu.service.JiazuManager"%>
<%@page import="com.fy.engineserver.jiazu.Jiazu"%>
<%@page import="com.fy.engineserver.unite.UniteManager"%>
<%@page import="com.fy.engineserver.unite.Unite"%>
<%@page import="com.fy.engineserver.country.data.Country"%>
<%@page import="com.fy.engineserver.country.manager.CountryManager"%>
<%@page import="com.fy.engineserver.sprite.concrete.GamePlayerManager"%>
<%@page import="com.fy.engineserver.datasource.buff.BuffTemplate"%>
<%@page import="com.fy.engineserver.datasource.buff.BuffTemplateManager"%>
<%@page import="com.fy.engineserver.datasource.buff.Buff"%>
<%@page import="java.util.ArrayList"%>
<%@page import="com.fy.engineserver.message.GameMessageFactory"%>
<%@page import="com.fy.engineserver.message.NOTICE_CLIENT_PLAY_PARTICLE_RES"%>
<%@page import="com.fy.engineserver.core.ParticleData"%>
<%@page import="com.fy.engineserver.core.TransportData"%>
<%@page import="com.fy.engineserver.sprite.Sprite"%>
<%@page import="com.fy.engineserver.sprite.Player"%>
<%@page import="com.fy.engineserver.sprite.PlayerManager"%>
<%@page import="com.fy.engineserver.sprite.Player"%>
<%@page
	import="com.fy.engineserver.sprite.monster.MemoryMonsterManager"%>
<%@ page language="java" contentType="text/html; charset=utf-8"
	pageEncoding="utf-8"%>

<%@page import="com.fy.engineserver.sprite.pet2.*"%>
<%@page import="com.xuanzhi.tools.simplejpa.SimpleEntityManagerFactory"%>
<%@page import="com.fy.engineserver.sprite.pet.Pet"%>
<%@page import="java.util.List"%>
<%@page import="com.xuanzhi.tools.page.PageUtil"%><html>
<%

String action = request.getParameter("action");
action = action == null ? "" : action;
if(action.equals("xiufushuju")) {
	Player popo = GamePlayerManager.getInstance().getPlayer(1353000000000004113L);
	List<Soul> list12 = popo.getUnusedSoul();
	out.println("[未启用元神个数:" + list12.size() + "]<br>");
	if(list12.size() == 1 && popo.getSoul(0) == null) {
		Soul ss = list12.get(0);
		Soul ss2 = popo.getCurrSoul();
		if(ss.getSoulType() == 1 && ss2.getSoulType() == 1 && ss.getGrade() == 40) {
			ss.setSoulType(0);
			ss.setGrade(110);
			list12.set(0, ss);
			popo.setUnusedSoul(list12);
			popo.setDirty(true, "unusedSoul");
			out.println("[设置成功] [本尊等级设置为:"+ ss.getGrade() +"]<br>");
		} else {
			out.println("[设置失败] [玩家存在本尊] [一个元神类型:" + ss.getSoulType() + "][另一个元神类型:" + ss2.getSoulType() +"]");
		}
	} else {
		out.println("[设置失败] [玩家未启用的元神个数:" + list12.size() + "][玩家是否存在本尊:" + (popo.getSoul(0) != null) +"]");
	}
}
	
%>
<head>

</head>
<body>
</body>
</html>
