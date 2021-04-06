<%@page import="com.fy.engineserver.authority.AuthorityAgent"%>
<%@page import="com.fy.engineserver.qiancengta.QianCengTaManager"%>
<%@page import="com.fy.engineserver.qiancengta.QianCengTa_Ta"%>
<%@page import="com.fy.engineserver.sprite.Soul"%>
<%@page import="java.net.URLDecoder"%><%@page import="com.fy.boss.gm.gmpagestat.GmEventManager"%><%@page import="net.sf.json.JSONArray"%><%@page import="java.util.HashMap"%><%@page import="java.util.Map"%><%@page import="com.fy.engineserver.mail.service.MailManager"%><%@page import="com.fy.engineserver.datasource.article.manager.ArticleEntityManager"%><%@page import="com.fy.engineserver.datasource.article.data.entity.ArticleEntity"%><%@page import="com.fy.engineserver.datasource.article.concrete.DefaultArticleEntityManager"%><%@page import="com.fy.engineserver.datasource.article.manager.ArticleManager"%><%@page import="com.fy.engineserver.datasource.article.data.articles.Article"%><%@page import="com.fy.engineserver.sprite.Player"%><%@page import="com.fy.engineserver.sprite.concrete.GamePlayerManager"%><%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%><%
	long playerId = Long.valueOf(request.getParameter("playerId"));
	String gmUser = URLDecoder.decode(request.getParameter("gmUserName"),"utf-8");
	Map<String, String> result = new HashMap<String, String>();
	try {
		Player player = null;
		try {
			player = GamePlayerManager.getInstance().getPlayer(playerId);
		} catch (Exception e) {
			
		}
		if (player != null) {
			QianCengTa_Ta ta = QianCengTaManager.getInstance().getTa(player.getId());
			ta.setTotalCostDaoSilver2Index(0, 0);
			ta.setTotalFlushDaoTimes2Index(0, 0, 0);
			for (int i = 0; i < 3; i++) {
				for (int j = 0; j < 6; j++) {
					ta.getAuthority()[i][j].lastRefreshDay = -1;
					ta.getAuthority()[i][j].lastRefreshItem = -1;
					AuthorityAgent.getInstance().refreshPlayerAuthority(ta.getAuthority()[i][j], player);
				}
			}
			result.put("success", "true");
			ArticleManager.logger.warn("[gm平台操作] [刷新千层塔] [修改者:"+gmUser+"] [" + player.getLogString() + "] ");
		}
	} catch (Exception e) {
		result.put("success","false");		
		result.put("message", e.getMessage());		
	}
	JSONArray json = JSONArray.fromObject(result);
	out.print(json.toString()); 
	out.flush();
	out.close();
%>