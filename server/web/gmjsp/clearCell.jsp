
<%@page import="net.sf.json.JSONArray"%>
<%@page import="com.fy.engineserver.core.Game"%>
<%@page import="com.gm.servlet.GMServlet"%>
<%@page import="com.gm.service.GMActionCommon"%>
<%@page
	import="com.fy.engineserver.datasource.article.data.entity.ArticleEntity"%>
<%@page import="com.fy.engineserver.sprite.PlayerManager"%>
<%@page import="com.fy.engineserver.sprite.Player"%>
<%@page import="java.net.URLDecoder"%>
<%@page import="java.util.HashMap"%>
<%@page import="java.util.Map"%><%@ page language="java"
	contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%
	String playerId = request.getParameter("playerId");
	Map<String, String> map = new HashMap<String, String>();
	try {
		String gmUserName = URLDecoder.decode(request.getParameter("gmUserName"), "utf-8");
		String type = request.getParameter("type");
		int index = Integer.valueOf(request.getParameter("index")).intValue();
		GMServlet.log.info("清空表格内物品参数,playerId:" + playerId + ", type:" + type + ",index:" + index);
		long id = Long.valueOf(playerId).longValue();
		Player p = PlayerManager.getInstance().getPlayer(id);
		if (type.equals("common")) {
			p.getKnapsack_common().clearCell(index, "GM后台操作", false);
			ArticleEntity ar = p.getKnapsack_common().getArticleEntityByCell(index);
			int num = p.getKnapsack_common().getCell(index).getCount();
			if (ar != null) GMActionCommon.sendActionLogToGateway(gmUserName, p, 12, num, ar.getArticleName(), "", new String[0], "清空背包格子" + index);
		} else if (type.equals("fangbao")) {
			p.getKnapsack_fangbao().clearCell(index, "GM后台操作", false);
			ArticleEntity ar = p.getKnapsack_fangbao().getArticleEntityByCell(index);
			int num = p.getKnapsack_fangbao().getCell(index).getCount();
			if (ar != null) GMActionCommon.sendActionLogToGateway(gmUserName, p, 12, num, ar.getArticleName(), "", new String[0], "清空防爆包格子" + index);
		} else if (type.equals("cangku")) {
			p.getKnapsacks_cangku().clearCell(index, "GM后台操作", false);
			ArticleEntity ar = p.getKnapsacks_cangku().getArticleEntityByCell(index);
			int num = p.getKnapsacks_cangku().getCell(index).getCount();
			if (ar != null) {
				GMActionCommon.sendActionLogToGateway(gmUserName, p, 12, num, ar.getArticleName(), "", new String[0], "清空仓库格子" + index);
			}
		}else if (type.equals("twocangku")) {
			p.getKnapsacks_warehouse().clearCell(index, "GM后台操作", false);
			ArticleEntity ar = p.getKnapsacks_warehouse().getArticleEntityByCell(index);
			int num = p.getKnapsacks_warehouse().getCell(index).getCount();
			if (ar != null) {
				GMActionCommon.sendActionLogToGateway(gmUserName, p, 12, num, ar.getArticleName(), "", new String[0], "清空仓库格子" + index);
			}
		}
		map.put("result", "success");
	} catch (Exception e) {
		GMServlet.log.error("", e);
		e.printStackTrace();
		Game.logger.warn("[GM平台清空格子] [异常]", e);
		map.put("result", "fail");
	}
	JSONArray json = JSONArray.fromObject(map);
	out.print(json.toString());
	out.flush();
	out.close();
%>