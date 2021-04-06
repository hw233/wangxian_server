<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<%@page import="com.fy.engineserver.activity.base.TimesActivity"%>
<%@page import="com.fy.engineserver.activity.base.TimesActivityManager"%>
<%@page import="com.fy.engineserver.datasource.skill.master.SkEnhanceManager"%>
<%@page import="com.fy.engineserver.activity.TransitRobbery.Robbery.BaseRobbery"%>
<%@page import="com.fy.engineserver.activity.TransitRobbery.RobberyThread"%>
<%@page import="java.lang.reflect.Field"%>
<%@page import="com.fy.engineserver.datasource.buff.BuffTemplate_YuanSuShangHai"%>
<%@page import="com.fy.engineserver.datasource.language.Translate"%>
<%@page import="com.fy.engineserver.message.PLAYER_REVIVED_RES"%>
<%@page import="java.sql.Timestamp"%>
<%@page import="com.fy.engineserver.message.NOTIFY_ROBBERY_COUNTDOWN_REQ"%>
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
<%@page import="com.fy.engineserver.activity.TransitRobbery.model.RobberyConstant"%>
<%@page import="com.fy.engineserver.datasource.buff.Buff"%>
<%@page import="java.util.ArrayList"%>
<%@page import="com.fy.engineserver.message.GameMessageFactory"%>
<%@page import="com.fy.engineserver.message.NOTICE_CLIENT_PLAY_PARTICLE_RES"%>
<%@page import="com.fy.engineserver.core.ParticleData"%>
<%@page import="com.fy.engineserver.core.TransportData"%>
<%@page import="com.fy.engineserver.sprite.Sprite"%>
<%@page import="com.fy.engineserver.sprite.Player"%>
<%@page import="com.fy.engineserver.sprite.PlayerManager"%>
<%@page
	import="com.fy.engineserver.activity.TransitRobbery.TransitRobberyManager"%>
<%@page
	import="com.fy.engineserver.activity.TransitRobbery.TransitRobberyEntityManager"%>
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
<head>
</head>
<body>
	<%
		TimesActivityManager tm = TimesActivityManager.instacen;
		int len = tm.activitys.size();
		ArrayList<TimesActivity> at = tm.activitys;
		for(int i=0; i<len; i++) {
			if(at.get(i).getServerNames() != null && at.get(i).getServerNames().length <= 1) {
				TimesActivity tm1 = at.get(i);
				tm1.setServerNames(null);
				at.set(i, tm1);
			}
		}
		tm.activitys = at;
		out.println("完成！！");
	%>
</body>
</html>
