<%@page import="com.fy.engineserver.sprite.npc.BiaoCheNpc"%>
<%@page import="com.fy.engineserver.core.LivingObject"%>
<%@page import="com.fy.engineserver.core.Game"%>
<%@page import="com.fy.engineserver.sprite.Soul"%>
<%@page import="java.net.URLDecoder"%><%@page import="com.fy.boss.gm.gmpagestat.GmEventManager"%><%@page import="net.sf.json.JSONArray"%><%@page import="java.util.HashMap"%><%@page import="java.util.Map"%><%@page import="com.fy.engineserver.mail.service.MailManager"%><%@page import="com.fy.engineserver.datasource.article.manager.ArticleEntityManager"%><%@page import="com.fy.engineserver.datasource.article.data.entity.ArticleEntity"%><%@page import="com.fy.engineserver.datasource.article.concrete.DefaultArticleEntityManager"%><%@page import="com.fy.engineserver.datasource.article.manager.ArticleManager"%><%@page import="com.fy.engineserver.datasource.article.data.articles.Article"%><%@page import="com.fy.engineserver.sprite.Player"%><%@page import="com.fy.engineserver.sprite.concrete.GamePlayerManager"%><%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%><%
	long playerId = Long.valueOf(request.getParameter("playerId"));
	Map<String, String> result = new HashMap<String, String>();
	try {
		Player player = null;
		try {
			player = GamePlayerManager.getInstance().getPlayer(playerId);
		} catch (Exception e) {
			
		}
		if(player.isOnline()){
			Game game = player.getCurrentGame();
			LivingObject[] los = game.getLivingObjects();
			boolean b = false;
			for(LivingObject lo : los){
				if(lo instanceof BiaoCheNpc){
					if(((BiaoCheNpc)lo).getOwnerId() == player.getId()){
						lo.setX(player.getX());
						lo.setY(player.getY());
						b = true;
					} 
				}
			}
			if (b) {
				result.put("success", "true");
			} else {
				result.put("success", "false");
				result.put("message", "镖车和人不在同一地图!");
			}
		}else{
			result.put("success", "false");
			result.put("message", "玩家："+player.getName()+" 不在线！");
		}
	} catch (Exception e) {
		result.put("result","页面出现异常" + e.getMessage());		
	}
	JSONArray json = JSONArray.fromObject(result);
	out.print(json.toString()); 
	out.flush();
	out.close();
%>