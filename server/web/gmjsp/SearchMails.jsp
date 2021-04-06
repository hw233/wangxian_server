<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>

<%@page
	import="com.fy.engineserver.datasource.article.data.props.Cell"%>
<%@page import="com.fy.engineserver.mail.Mail"%>
<%@page import="java.text.SimpleDateFormat"%>
<%@page import="org.apache.commons.lang.StringUtils"%>
<%@page import="java.util.ArrayList"%>
<%@page import="java.util.List"%>
<%@page import="java.util.Map"%>
<%@page import="java.util.HashMap"%>
<%@page import="java.net.URLDecoder"%>
<%@page import="com.gm.servlet.GMServlet"%>
<%@page import="com.fy.engineserver.sprite.PlayerManager"%>
<%@page import="com.fy.engineserver.sprite.Player"%>
<%@page
	import="com.fy.engineserver.datasource.article.data.entity.ArticleEntity"%>
<%@page
	import="com.fy.engineserver.datasource.article.manager.ArticleEntityManager"%>
<%@page
	import="com.fy.engineserver.datasource.article.data.articles.Article"%>
<%@page
	import="com.fy.engineserver.datasource.article.manager.ArticleManager"%>
<%@page import="com.fy.engineserver.mail.service.MailManager"%>
<%@page import="com.gm.service.GMActionCommon"%>
<%@page import="net.sf.json.JSONObject"%>
<%@page import="net.sf.json.JSONArray"%>
<%
	List<Mail> mails = null;
	List<Map<String, String>> list = new ArrayList<Map<String, String>>();
	try {
		PlayerManager pmanager = PlayerManager.getInstance();
		MailManager mmanager = MailManager.getInstance();
		String playerName = URLDecoder.decode(request.getParameter("playerName"), "utf-8");
		String playerId = request.getParameter("playerId");
		GMServlet.log.info("邮件查询，playerName:" + playerName + ",playerId:" + playerId);

		Player p = null;
		if (StringUtils.isNotBlank(playerId)) p = pmanager.getPlayer(Long.valueOf(playerId).longValue());
		else {
			p = pmanager.getPlayer(playerName);
		}
		if (p != null) {
			mails = mmanager.getAllMails(p);
		}
		if ((mails != null) && (mails.size() > 0)) {
			ArticleEntityManager aemanager = ArticleEntityManager.getInstance();
			ArticleManager amanager = ArticleManager.getInstance();
			SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
			for (Mail mail : mails) {
				Map map = new HashMap();
				long senderId = mail.getPoster();
				String sender = "系统";
				if (senderId > 0L) {
					try {
						sender = pmanager.getPlayer(senderId).getName();
					} catch (Exception e) {
						e.printStackTrace();
						sender = "未知(角色不存在)";
					}
				}
				map.put("sender", sender);
				map.put("title", mail.getTitle());
				map.put("id", Long.valueOf(mail.getId()));
				map.put("content", mail.getContent());
				map.put("coins", Long.valueOf(mail.getCoins()));
				map.put("price", Long.valueOf(mail.getPrice()));
				map.put("sendTime", format.format(mail.getCreateDate()));
				map.put("status", Integer.valueOf(mail.getStatus()));
				Cell[] cells = mail.getCells();
				map.put("status_desp", Mail.STATUS_DESP[mail.getStatus()]);
				StringBuilder fujian = new StringBuilder();
				for (Cell cell : cells) {
					long aid = cell.getEntityId();
					if (aid > 0L) {
						int count = cell.getCount();
						ArticleEntity ae = aemanager.getEntity(aid);
						if (ae != null) {
							Article a = amanager.getArticle(ae.getArticleName());
							fujian.append(a.getName() + "(" + count + ")<br>");
						} else {
							fujian.append("无实体" + aid + "(" + count + ")<br>");
						}
					}
				}

				map.put("fujian", fujian.toString());
				list.add(map);
			}
		}
	} catch (Exception e) {
		GMServlet.log.error("", e);
		ArticleManager.logger.warn("查询邮件异常", e);
	}
	ArticleManager.logger.warn("邮件数量：" + list.size());
	JSONArray json = JSONArray.fromObject(list);
	out.print(json.toString());
	out.flush();
	out.close();
%>