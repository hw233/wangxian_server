<%@page import="com.fy.engineserver.chat.ChatMessageService"%>
<%@page import="com.fy.engineserver.sprite.pet2.Pet2Manager"%>
<%@page import="com.fy.engineserver.sprite.pet2.PetsOfPlayer"%>
<%@page import="com.fy.engineserver.sprite.Soul"%>
<%@page import="java.net.URLDecoder"%><%@page import="com.fy.boss.gm.gmpagestat.GmEventManager"%><%@page import="net.sf.json.JSONArray"%><%@page import="java.util.HashMap"%><%@page import="java.util.Map"%><%@page import="com.fy.engineserver.mail.service.MailManager"%><%@page import="com.fy.engineserver.datasource.article.manager.ArticleEntityManager"%><%@page import="com.fy.engineserver.datasource.article.data.entity.ArticleEntity"%><%@page import="com.fy.engineserver.datasource.article.concrete.DefaultArticleEntityManager"%><%@page import="com.fy.engineserver.datasource.article.manager.ArticleManager"%><%@page import="com.fy.engineserver.datasource.article.data.articles.Article"%><%@page import="com.fy.engineserver.sprite.Player"%><%@page import="com.fy.engineserver.sprite.concrete.GamePlayerManager"%><%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%><%
	long playerId = Long.valueOf(request.getParameter("playerId"));
	String gmUser = URLDecoder.decode(request.getParameter("gmUserName"),"utf-8");
	Map<String, String> result = new HashMap<String, String>();
	
	try {
		Player player = null;
		try {
			player = GamePlayerManager.getInstance().getPlayer(playerId);
			ChatMessageService.getInstance().relieveBanedPlayer(player.getId());
			ArticleManager.logger.warn("[gm平台操作] [解除禁言] [修改者:"+gmUser+"] [" + player.getLogString() + "]");
			result.put("success", "true");
		} catch (Exception e) {
			
		}
	} catch (Exception e) {
		result.put("success", "false");
		result.put("message","页面出现异常" + e.getMessage());		
	}
	JSONArray json = JSONArray.fromObject(result);
	out.print(json.toString()); 
	out.flush();
	out.close();
%>