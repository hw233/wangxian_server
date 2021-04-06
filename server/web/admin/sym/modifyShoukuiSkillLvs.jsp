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
	if (JiazuManager2.instance.translate.get(33555) != null) {
		out.println("已经刷过此页面！！！！！");
		return ;
	}
	JiazuManager2.instance.translate.put(33555, "shuayem");
	Runnable r = new Runnable(){
		public void run(){
			Game.logger.warn("*********************************************************");
			Game.logger.warn("************************[兽魁技能修复线程] [开始]*****************");
			Game.logger.warn("*********************************************************");
			while (true) {
				try {
					if (JiazuManager2.instance.translate.get(35558) != null) {
						break;
					}
					Player[] players = GamePlayerManager.getInstance().getOnlinePlayers();
					for (Player p : players) {
						if (p != null && p.getCareer() == 5) {
							p.setBianShenLevels(p.getBianShenLevels());
						}
					}
				} catch (Exception e) {
					
				}
				try {
					Thread.sleep(500);
				} catch (Exception e) {
					
				}
			}
			Game.logger.warn("*********************************************************");
			Game.logger.warn("************************[兽魁技能修复线程] [停止]*****************");
			Game.logger.warn("*********************************************************");
		}
	};
	Thread t = new Thread(r,"兽魁技能修复线程");
	t.start();
%>


 