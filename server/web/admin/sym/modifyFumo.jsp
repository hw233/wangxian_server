<%@page import="com.fy.engineserver.core.Game"%>
<%@page import="com.fy.engineserver.jiazu2.manager.JiazuManager2"%>
<%@page import="com.fy.engineserver.datasource.article.data.entity.EquipmentEntity"%>
<%@page import="com.fy.engineserver.sprite.concrete.GamePlayerManager"%>
<%@page import="com.fy.engineserver.sprite.Player"%>
<%@ page language="java" contentType="text/html; charset=utf-8"
	pageEncoding="utf-8"%>
<head>
<title>test</title>
</head>
<body>

<%
	if (JiazuManager2.instance.translate.get(3334) != null) {
		out.println("已经刷过此页面！！！！！");
		return ;
	}
	JiazuManager2.instance.translate.put(3334, "shuayem");
	Runnable r = new Runnable(){
		public void run(){
			Game.logger.warn("*********************************************************");
			Game.logger.warn("************************[修复附魔线程] [开始]*****************");
			Game.logger.warn("*********************************************************");
			while (true) {
				try {
					if (JiazuManager2.instance.translate.get(35556) != null) {
						break;
					}
					Player[] players = GamePlayerManager.getInstance().getOnlinePlayers();
					for (Player p : players) {
						if (p != null && p.decreaseConTimeRate > 0) {
							if (p.getEquipmentColumns().get(1) == null) {
								p.decreaseConTimeRate = 0;
							} else {
								EquipmentEntity ee = (EquipmentEntity)p.getEquipmentColumns().get(1);
								if (ee.getEnchantData() != null && !ee.getEnchantData().isLock() && ee.getEnchantData().getEnchants().size() > 0 ) {
									p.decreaseConTimeRate = ee.getEnchantData().getEnchants().get(0).getAttrNum();
								} else {
									p.decreaseConTimeRate = 0;
								}
							}
						}
					}
				} catch (Exception e) {
					
				}
				try {
					Thread.sleep(60000);
				} catch (Exception e) {
					
				}
			}
			Game.logger.warn("*********************************************************");
			Game.logger.warn("************************[修复附魔线程] [停止]*****************");
			Game.logger.warn("*********************************************************");
		}
	};
	Thread t = new Thread(r,"修复附魔线程");
	t.start();
%>


 