<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<%@page import="com.fy.engineserver.menu.activity.exchange.DuJieActivity"%>
<%@page import="com.fy.engineserver.menu.activity.exchange.ExchangeActivityManager"%>
<%@page import="com.xuanzhi.boss.game.GameConstants"%>
<%@page import="com.fy.engineserver.activity.clifford.manager.CliffordManager"%>
<%@page import="com.fy.engineserver.activity.TransitRobbery.model.CleConditionModel"%>
<%@page import="com.fy.engineserver.activity.TransitRobbery.TransitRobberyEntity"%>
<%@page import="com.fy.engineserver.core.LivingObject"%>
<%@page import="com.fy.engineserver.sprite.npc.MemoryNPCManager"%>
<%@page import="com.fy.engineserver.sprite.npc.NPC"%>
<%@page import="com.fy.engineserver.datasource.article.data.entity.EquipmentEntity"%>
<%@page import="com.fy.engineserver.datasource.article.data.equipments.EquipmentColumn"%>
<%@page import="java.lang.reflect.Field"%>
<%@page import="java.util.*"%>
<%@page import="com.fy.engineserver.datasource.article.data.props.LevelRandomPackage"%>
<%@page import="com.fy.engineserver.datasource.article.data.articles.Article"%>
<%@page import="com.fy.engineserver.datasource.article.manager.ArticleManager"%>
<%@page import="com.fy.engineserver.datasource.article.data.magicweapon.MagicWeaponManager"%>
<%@page import="com.fy.engineserver.sprite.petdao.PetDaoManager"%>
<%@page import="com.fy.engineserver.core.TransportData"%>
<%@page import="com.fy.engineserver.sprite.Sprite"%>
<%@page import="com.fy.engineserver.sprite.Player"%>
<%@page import="com.fy.engineserver.sprite.PlayerManager"%>
<%@page import="com.fy.engineserver.activity.TransitRobbery.TransitRobberySystem"%>
<%@page import="com.fy.engineserver.activity.TransitRobbery.TransitRobberyManager"%>
<%@page import="com.fy.engineserver.activity.TransitRobbery.TransitRobberyEntityManager"%>
<%@page import="com.fy.engineserver.sprite.Player"%>
<%@page import="com.fy.engineserver.sprite.monster.MemoryMonsterManager"%>
<%@page import="com.fy.engineserver.core.Game"%>
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
	ExchangeActivityManager em = ExchangeActivityManager.getInstance();
	List<DuJieActivity> dujieActivity = em.dujieActivitys;
	for(DuJieActivity d:dujieActivity){
		out.print(d.toString()+"<br>");
	}
	dujieActivity.clear();
	DuJieActivity activity = new DuJieActivity("天劫奖励20140304", new int[]{1, 2, 3, 4, 5, 6, 7, 8, 9}, new String[]{"一劫散仙锦囊", "二劫散仙锦囊", "三劫散仙锦囊", "四劫散仙锦囊", "五劫散仙锦囊", "六劫散仙锦囊", "七劫散仙锦囊", "八劫散仙锦囊", "九劫散仙锦囊"}, new int[]{3, 3, 3, 3, 3, 3, 3, 3, 3}, new int[]{1, 1, 1, 1, 1, 1, 1, 1, 1}, "sqage", 1394121600000l,1394726399000l,new ArrayList<String>() , new ArrayList<String>(), "恭喜您获得了渡劫回馈奖励", "请查收附件，收取奖励！",1);
	dujieActivity.add(activity);
	out.print(dujieActivity.size()+"添加成功："+activity.toString()+"<br>");

%>