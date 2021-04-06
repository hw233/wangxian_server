<%@page import="com.fy.engineserver.mail.MailSendType"%>
<%@page import="com.xuanzhi.boss.game.GameConstants"%>
<%@page import="com.fy.engineserver.constants.GameConstant"%>
<%@page import="com.xuanzhi.tools.authorize.User"%>
<%@page import="com.xuanzhi.tools.authorize.AuthorizeManager"%>
<%@page import="com.fy.engineserver.mail.service.MailManager"%>
<%@page
	import="com.fy.engineserver.datasource.article.manager.ArticleEntityManager"%>
<%@page
	import="com.fy.engineserver.datasource.article.data.entity.ArticleEntity"%>
<%@page
	import="com.fy.engineserver.datasource.article.manager.ArticleManager"%>
<%@page
	import="com.fy.engineserver.datasource.article.data.articles.Article"%>
<%@page
	import="com.fy.engineserver.sprite.concrete.GamePlayerManager"%>
<%@page import="com.fy.engineserver.sprite.Player"%>
<%@page
	import="com.fy.engineserver.platform.PlatformManager.Platform"%>
<%@page import="com.fy.engineserver.platform.PlatformManager"%>
<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<%
String ip = request.getRemoteAddr();
String recorder = "";
Object o = session.getAttribute("authorize.username");
if(o!=null){
	recorder = o.toString();
}
	AuthorizeManager am = AuthorizeManager.getInstance();
	User user = am.getUserManager().getUser(String.valueOf(request.getSession().getAttribute("authorize.username")));

	String[] articles = new String[] {};
	if (PlatformManager.getInstance().isPlatformOf(Platform.台湾)) {
		articles = new String[] {"寶石福袋",
				"酒票",
				"帖券",
				"如意錦囊",
				"煉星符",
				"初級煉妖石"};
	} else if (PlatformManager.getInstance().isPlatformOf(Platform.韩国)) {
		articles = new String[] {"연성부",//炼星符
				"술표",//酒票
				"초급 연요석",// 初級煉妖石
				"쿠폰",// 初級煉妖石
				"초급 연요석 ",// 初級煉妖石
				"향화",// 香火
				"감정부",// 鉴定符
				"노반령",// 鲁班令
			};
	}
	if ("true".equals(request.getParameter("send"))) {
		String articleName = request.getParameter("articleName");
		String mailContent = request.getParameter("mailContent");
		String mailTitle = request.getParameter("mailTitle");
		int num = Integer.valueOf(request.getParameter("num"));
		int playerLevel = Integer.valueOf(request.getParameter("playerLevel"));
		boolean bind = "true".equals(request.getParameter("bind"));

		boolean found = false;
		for (String s : articles) {
			if (s.equals(articleName)) {
				found = true;
				break;
			}
		}
		if (!found) {
			out.print("<H1>你输入的物品[" + articleName + "],不是合法的,请看下方列表</H1>");
		} else {
			Article article = ArticleManager.getInstance().getArticle(articleName);
			if (article == null) {
				out.print("<h2>物品不存在:" + articleName + "</h2>");
			} else {
				Player[] onlinePlayer = GamePlayerManager.getInstance().getOnlinePlayers();
				int sendNum = 0;
				int totalNum = onlinePlayer.length;
				for (Player p : onlinePlayer) {
					if (p != null) {
						if (p.getSoulLevel() < playerLevel) {
							continue;
						}
						ArticleEntity ae = ArticleEntityManager.getInstance().createEntity(article, bind, 1, p, article.getColorType(), num, true);
						long mailId = MailManager.getInstance().sendMail(p.getId(), new ArticleEntity[] { ae }, new int[] { num }, mailTitle, mailContent, 0L, 0L, 0L, "",MailSendType.后台发送,p.getName(),ip,recorder);
						MailManager.logger.warn("[后台发送物品] [发送者:" + user.getRealName() + "] [发送物品:" + articleName + "] [数量:" + num + "] [接受人:" + p.getLogString() + "] [邮件ID:" + mailId + "]");
						p.sendError(mailContent);
						sendNum++;
					}
				}
				out.print("<H1>发送完毕,成功发送:" + sendNum + "/" + totalNum + "个</H1>");
			}
		}
	}
%>
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title>给所有在线玩家发邮件</title>
</head>
<body style="font-size: 12px;">
	<H2><%=GameConstants.getInstance().getServerName()%></H2>
	<H2>
		你好,<%=user.getRealName()%></H2>
	<form action="./sendMailToOnlinePlayer.jsp" method="post">
		要发的物品:<input type="text" name="articleName"><br /> 
		要发的数量:<input type="text" name="num" value="1"><br /> 
		玩家等级:<input type="text" 	name="playerLevel" value="20"><br /> 
		邮件标题:<input type="text" 	name="mailTitle" value="系统邮件"><br /> 
		邮件内容(会弹框提示玩家):<input type="text" name="mailContent"><br /> 
		是否&nbsp;&nbsp;绑定:<input 	type="text" name="bind" value="true"><br /> 
		<input type="hidden" name="send" value="true">
		<input type="submit" value="发送">
	</form>
	<hr>
	<hr>
	<h1>当前可发放道具列表</h1>
	<%
		for (String s : articles) {
			out.print(s + "<BR/>");
		}
	%>
	<hr>
	<hr>
</body>
</html>