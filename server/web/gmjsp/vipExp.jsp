<%@page import="java.io.IOException"%>
<%@page
	import="com.fy.engineserver.datasource.article.manager.ArticleManager"%>
<%@page import="net.sf.json.JSONArray"%>
<%@page import="java.util.HashMap"%>
<%@page import="java.util.Map"%>
<%@page import="com.gm.service.GMActionCommon"%>
<%@page
	import="com.fy.engineserver.sprite.concrete.GamePlayerManager"%>
<%@page import="org.apache.commons.lang.StringUtils"%>
<%@page import="java.net.URLDecoder"%>
<%@page import="com.fy.engineserver.sprite.Player"%>
<%@page import="com.gm.servlet.GMServlet"%>
<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%
	String playerID = request.getParameter("playerID");
	String vipExp = request.getParameter("vipExp");
	String jifen = request.getParameter("jifen");

	GMServlet.log.info("调整VIP等级：playerID=" + playerID + ";vipExp=" + vipExp + ";jifen=" + jifen);
	Map<String, String> map = new HashMap<String, String>();
	Player player = null;
	map.put("result", "fail");
	try {
		String gmUserName = URLDecoder.decode(request.getParameter("gmUserName"), "UTF-8");
		String reason = URLDecoder.decode(request.getParameter("reason"), "UTF-8");
		if ((StringUtils.isNotBlank(playerID)) && (StringUtils.isNotBlank(vipExp))) {
			long id = Long.valueOf(playerID).longValue();
			player = GamePlayerManager.getInstance().getPlayer(id);
			long rmb = Long.valueOf(vipExp).longValue();
			if (rmb > 0L) {
				player.setRMB(player.getRMB() + rmb);
			} else if (rmb < 0L) {
				if (player.getRMB() < rmb) {
					map.put("result", "fail");
					return;
				} else {
					player.setRMB(player.getRMB() + rmb);
				}
			}
			long chargePoints = Long.valueOf(jifen).longValue();
			if (chargePoints > 0L) {
				player.setChargePoints(chargePoints + player.getChargePoints());
			} else if (chargePoints < 0L) {
				if (player.getChargePoints() < chargePoints) {
					map.put("result", "fail");
					return;
				} else {
					player.setChargePoints(chargePoints + player.getChargePoints());
				}
			}
			GMActionCommon.sendActionLogToGateway(gmUserName, player, 2, 1L, "增加VIP经验:" + vipExp + ";增加积分:" + jifen, "", new String[0], reason);
			ArticleManager.logger.warn("[后台修改vip经验和积分]" + player.getLogString() + "[VIP经验变化:" + vipExp + "] [变化后vip经验:"+player.getRMB()+"] [积分变化:" + jifen + "] [变化后积分:"+player.getChargePoints()+"]");
			map.put("result", "success");
		}
	} catch (Exception e) {
		GMServlet.log.error("", e);
		map.put("result", "fail");
	}
	JSONArray json = JSONArray.fromObject(map);
	//out.print(json.toString());
	//out.flush();
	//out.close();
	ServletOutputStream out1 = null;
	try {
		response.setCharacterEncoding("utf-8");
		out1 = response.getOutputStream();
		if (out != null) {
			out1.write(json.toString().getBytes("utf-8"));
			out1.flush();
		}
	} catch (Exception e) {
		ArticleManager.logger.error(e.getMessage(), e);
	} finally {
		try {
			if (out != null) {
				out1.close();
			}
		} catch (IOException e) {
			ArticleManager.logger.error(e.getMessage(), e);
		}
	}
%>