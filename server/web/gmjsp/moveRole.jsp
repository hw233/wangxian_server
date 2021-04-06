<%@page import="com.fy.engineserver.core.TransportData"%>
<%@page import="com.fy.engineserver.core.Game"%>
<%@page import="com.fy.engineserver.sprite.Soul"%>
<%@page import="java.net.URLDecoder"%><%@page import="com.fy.boss.gm.gmpagestat.GmEventManager"%><%@page import="net.sf.json.JSONArray"%><%@page import="java.util.HashMap"%><%@page import="java.util.Map"%><%@page import="com.fy.engineserver.mail.service.MailManager"%><%@page import="com.fy.engineserver.datasource.article.manager.ArticleEntityManager"%><%@page import="com.fy.engineserver.datasource.article.data.entity.ArticleEntity"%><%@page import="com.fy.engineserver.datasource.article.concrete.DefaultArticleEntityManager"%><%@page import="com.fy.engineserver.datasource.article.manager.ArticleManager"%><%@page import="com.fy.engineserver.datasource.article.data.articles.Article"%><%@page import="com.fy.engineserver.sprite.Player"%><%@page import="com.fy.engineserver.sprite.concrete.GamePlayerManager"%><%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%><%
	long playerId = Long.valueOf(request.getParameter("playerId"));
	String type = request.getParameter("type");
	String mapName = request.getParameter("mapName");
	String xsit = request.getParameter("xsit");
	String ysit = request.getParameter("ysit");
	String gmUser = URLDecoder.decode(request.getParameter("gmUserName"),"utf-8");
	Map<String, String> result = new HashMap<String, String>();
	try {
		Player player = null;
		try {
			player = GamePlayerManager.getInstance().getPlayer(playerId);
		} catch (Exception e) {
			
		}
		boolean b = false;
		Game game = player.getCurrentGame();
		if (game == null) {
			b = true;
			result.put("success", "false");
			result.put("message", "currentgame=null");
		}
		String mm = mapName;
		int x = Integer.parseInt(xsit);
		int y = Integer.parseInt(ysit);
		if (!b) {
			
			if (type.equals("2")) {			//移动到另一玩家身边
				long targetId = Long.valueOf(request.getParameter("targetId"));
				Player target = null;
				try {
					target = GamePlayerManager.getInstance().getPlayer(targetId);
				} catch (Exception e) {
					
				}
				Game game2 = target.getCurrentGame();
				if (game2 == null) {
					b = true;
					result.put("success", "false");
					result.put("message", "currentgame=null");
				}
				mm = game2.gi.name;
			}
		}
		if (!b) {
			game.transferGame(player, new TransportData(0, 0, 0, 0, mm, x, y));
			result.put("success", "true");
			ArticleManager.logger.warn("[gm平台操作] [传送玩家到指定位置] [修改者:"+gmUser+"] [" + player.getLogString() + "] [type:" +  type + "] ");
		}
	} catch (Exception e) {
		result.put("result","页面出现异常" + e.getMessage());		
	}
	JSONArray json = JSONArray.fromObject(result);
	out.print(json.toString()); 
	out.flush();
	out.close();
%>