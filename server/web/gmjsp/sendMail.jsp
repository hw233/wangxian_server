<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@page import="java.util.Map"%>
<%@page import="java.util.HashMap"%>
<%@page import="org.apache.commons.lang.StringUtils"%>
<%@page import="java.net.URLDecoder"%>
<%@page import="com.fy.engineserver.mail.service.MailManager"%>
<%@page import="com.fy.engineserver.sprite.PlayerManager"%>
<%@page import="com.fy.engineserver.sprite.Player"%>
<%@page import="java.util.List"%>
<%@page import="java.util.ArrayList"%>
<%@page import="java.util.Iterator"%>
<%@page
	import="com.fy.engineserver.datasource.article.data.entity.ArticleEntity"%>
<%@page import="net.sf.json.JSONObject"%>
<%@page import="net.sf.json.JSONArray"%>
<%@page import="com.gm.servlet.GMServlet"%>
<%@page import="org.apache.commons.logging.Log"%>
<%@page import="com.fy.engineserver.activity.ActivitySubSystem"%>
<%
	Log log = GMServlet.log;
	String mailAddress = request.getParameter("mailAddress");
	String mailTitle = request.getParameter("mailTitle");
	String mailContent = request.getParameter("mailContent");
	Map<String, String> map = new HashMap<String, String>();
	try {
		if (StringUtils.isNotBlank(mailContent)) {
			mailContent = URLDecoder.decode(mailContent, "utf-8");
		}
		if (StringUtils.isNotBlank(mailTitle)) {
			mailTitle = URLDecoder.decode(mailTitle, "utf-8");
		}
		log.info("发送邮件接收到参数,mailAddress:" + mailAddress + ", mailTitle:" + mailTitle + ",mailContent:" + mailContent);
		MailManager mm = MailManager.getInstance();
		PlayerManager pm = PlayerManager.getInstance();
		if ((StringUtils.isNotBlank(mailAddress)) && (StringUtils.isNotBlank(mailTitle)) && (StringUtils.isNotBlank(mailContent))) {
			String[] names = mailAddress.split(",");
			Player[] ps = null;
			boolean issend = true;
			List<Long> pid = new ArrayList<Long>();
			if ((names != null) && (names.length <= 10)) {
				for (String name : names) {
					try {
						ps = pm.getPlayerByUser(name);
						if (ps.length > 0) {
							for (Player player : ps) {
								if (player != null) pid.add(Long.valueOf(player.getId()));
							}
						} else issend = false;
					} catch (Exception e) {
						log.error("", e);
					}
				}
				if ((issend) && (pid.size() > 0)) {
					for (Iterator localIterator = pid.iterator(); localIterator.hasNext();) {
						long id = ((Long) localIterator.next()).longValue();
						mm.sendMail(id, new ArticleEntity[0], mailTitle, mailContent, 0L, 0L, 0L, "GM后台发送");
					}
				}
				map.put("result", "success");
			} else {
				issend = false;
				map.put("result", "fail");
			}
		} else {
			map.put("result", "fail");
		}
	} catch (Exception e) {
		log.error("", e);
		map.put("result", "fail");
	}
	JSONArray json = JSONArray.fromObject(map);
	out.print(json.toString());
	out.flush();
	out.close();
%>
