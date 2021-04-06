<%@page import="com.fy.engineserver.mail.MailSendType"%>
<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<%!int enterTimes = 0;%>
<%
String ip = request.getRemoteAddr();
String recorder = "";
Object o = session.getAttribute("authorize.username");
if(o!=null){
	recorder = o.toString();
}
	if (enterTimes != 0) {
		out.print("你访问次数太多了:" + enterTimes);
		return;
	}
	String mailTitle = "补发2月17日武圣奖励";
	String mailContent = "亲爱的用户您好，由于系统延迟问题，以下是补发您2月17日的武圣奖励，给您带来不便敬请谅解！";

	enterTimes++;
	out.print("<H2>这是你第" + enterTimes + "次访问</H2>");
	//TODO
	//File file = new File("/home/game/resin/webapps/game_server/WEB-INF/game_init_config/sendSilver.txt");
	String path = request.getRealPath("sendTournamentPrize.txt");
	out.print(path + " 是否存在:" + new File(path).exists() + "<HR/>");
	FileInputStream fis = new FileInputStream(path);
	InputStreamReader isr = new InputStreamReader(fis, "GBK");
	BufferedReader bufferedReader = new BufferedReader(isr);
	String line = bufferedReader.readLine();
	List<String[]> list = new ArrayList<String[]>();
	String thisServerName = GameConstants.getInstance().getServerName();
	try {
		while (line != null && !"".equals(line.trim())) {
			String[] values = line.split("\t");
			String serverName = values[0];
			if (thisServerName.equals(serverName)) {
				list.add(values);
			}
			line = bufferedReader.readLine();
		}
	} catch (Exception e) {
		out.print("解析出异常了,去stderr看看!<BR/>");
		e.printStackTrace();
	} finally {
		if (bufferedReader != null) {
			try {
				bufferedReader.close();
			} catch (Exception ee) {
				out.print("关闭出异常了,去stderr看看!<BR/>");
				ee.printStackTrace();
			}
		}
	}
	//TODO 发送
	int hassend = 0;
	int notsend = 0;
	for (String[] arr : list) {
		long playerId = Long.valueOf(arr[1]);
		String articleName = arr[2];
		long silver = Long.valueOf(arr[3]);
		Player player = null;
		try {
			player = PlayerManager.getInstance().getPlayer(playerId);
		} catch (Exception e) {
		}
		if (player != null) {
			Article a = ArticleManager.getInstance().getArticle(articleName);
			if (a != null) {
				try {
					ArticleEntity ae = ArticleEntityManager.getInstance().createEntity(a, true, ArticleEntityManager.CREATE_REASON_TOURNAMENT_REWARD, player, a.getColorType(), 1, true);
					MailManager.getInstance().sendMail(playerId, new ArticleEntity[] { ae }, new int[] { 1 }, mailTitle, mailContent, silver, 0, 0, "后台补发武圣奖励",MailSendType.后台发送,player.getName(),ip,recorder);
					hassend++;
					out.print(arr[0] + "--" + playerId + "--"+player.getName()+"--" + ae.getArticleName() + "--" + BillingCenter.得到带单位的银两(silver) + "<br/>");
				} catch (Exception ex) {
					out.print("[补发竞技奖励] [异常] [" + player.getLogString() + "] [ex:" + ex + "]<br>");
				}
			}
		} else {
			notsend++;
			out.print("<hr>id为" + playerId + "的玩家不存在<br>");
		}
	}
	out.print("已发送:" + hassend + "<BR/>未发送:" + notsend);
%>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title>补发武圣奖励页面</title>
</head>
<body>

</body>

<%@page import="com.xuanzhi.boss.game.GameConstants"%>
<%@page import="java.util.ArrayList"%>
<%@page import="java.util.List"%>
<%@page import="java.io.*"%>
<%@page import="com.fy.engineserver.mail.service.MailManager"%>
<%@page
	import="com.fy.engineserver.activity.activeness.ActivenessManager"%>
<%@page
	import="com.fy.engineserver.datasource.article.data.articles.Article"%>
<%@page
	import="com.fy.engineserver.datasource.article.manager.ArticleManager"%>
<%@page
	import="com.fy.engineserver.datasource.article.data.entity.ArticleEntity"%>
<%@page
	import="com.fy.engineserver.datasource.article.manager.ArticleEntityManager"%>
<%@page import="com.fy.engineserver.sprite.PlayerManager"%>
<%@page import="com.fy.engineserver.sprite.Player"%>
<%@page import="com.fy.engineserver.economic.BillingCenter"%></html>