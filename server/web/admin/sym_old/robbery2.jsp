<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
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
		String action = request.getParameter("action");
		String playerId = request.getParameter("playerId");
		action = action == null ? "" : action;
		playerId = playerId == null ? "" :playerId;
	%>
	<form action="robbery2.jsp" method="get">
		<input type="hidden" name="action" value="enterRobbery" />密码：<input
			type="text" name="playerId" value="<%=playerId%>" /> <input
			type="submit" value="启动修复线程" />
	</form>
	<br />
	
	<%
		if("enterRobbery".equals(action) && "dujiexitongxiufu123".equals(playerId)) {
			if(SkEnhanceManager.translation.containsKey("dujiexitongxiufu123")) {
				out.println("渡劫修复线程已经启动！");
				return;
			}
			SkEnhanceManager.translation.put("dujiexitongxiufu123", "dujiexitongxiufu123");
			Runnable r = new Runnable(){
				public void run(){
					while(true) {
						try{
							Field ff = TransitRobberyManager.class.getDeclaredField("robberyThreads");
							ff.setAccessible(true);
							RobberyThread[] arr = (RobberyThread[])ff.get(TransitRobberyManager.getInstance());
							Object[] allPlayer = GamePlayerManager.getInstance().getAllPlayerInCache();
							for(Object o : allPlayer) {
								Player p = (Player)o;
								boolean exist = false;
								if(p.getLevel() < 110 || !TransitRobberyEntityManager.getInstance().isPlayerInRobbery(p.getId())) {
									continue;
								}
								for(int i=0; i<arr.length; i++) {
									for(BaseRobbery b : arr[i].allRobbery) {
										if(b.playerId == p.getId()) {
											exist = true;
											break;
										}
									}
									if(exist) {
										break;
									}
								}
								if(!exist) {
									TransitRobberyEntityManager.getInstance().removeFromRobbering(p.getId());
									try{
										p.getCurrentGame().transferGame(p, new TransportData(0, 0, 0, 0, p.getResurrectionMapName(), p.getResurrectionX(), p.getResurrectionY()), true);
									}catch(Exception e){
										TransitRobberyManager.logger.error("[后台线程出错]{}",e);
									}
									TransitRobberyManager.logger.warn("已经修正完毕，player{}", p.getLogString());
								}
							}
							
						}catch(Exception e) {
							TransitRobberyManager.logger.error("[后台线程出错2]{}",e);
						}
						try{
							Thread.sleep(60000);
						}catch(Exception e) {
							TransitRobberyManager.logger.error("[后台线程出错3]{}",e);
						}
					}
				}
			};
			new Thread(r,"后台渡劫修复线程").start();
			out.println("修复线程启动成功");
		}
	%>
</body>
</html>
