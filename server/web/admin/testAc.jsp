<%@page import="com.fy.engineserver.mail.MailSendType"%>
<%@page import="com.fy.engineserver.util.TimeTool"%>
<%@page import="com.fy.engineserver.activity.ActivitySubSystem"%>
<%@page import="com.fy.engineserver.datasource.article.manager.ArticleEntityManager"%>
<%@page import="com.fy.engineserver.activity.PlayerActivityRecord"%>
<%@page import="com.fy.engineserver.util.Utils"%>
<%@page import="com.fy.engineserver.datasource.article.manager.ArticleManager"%>
<%@page import="com.fy.engineserver.mail.service.MailManager"%>
<%@page import="com.fy.engineserver.datasource.article.data.entity.ArticleEntity"%>
<%@page import="com.fy.engineserver.datasource.article.data.articles.Article"%>
<%@page import="com.fy.engineserver.activity.ActivityManager"%>
<%@page import="com.fy.engineserver.sprite.concrete.GamePlayerManager"%>
<%@page import="com.fy.engineserver.sprite.Player"%>
<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">

<%
	Player p = GamePlayerManager.getInstance().getPlayer("feeling");
	onlineActivity(p,System.currentTimeMillis(),out);
	ip = request.getRemoteAddr();
	Object o = session.getAttribute("authorize.username");
	if(o!=null){
		recorder = o.toString();
	}
%>
<%!
String ip = "";
String recorder = "";
public static String 在线一重礼 = "元宵禮盒";//Translate.在线一重礼;
public static String 在线二重礼 = "精緻元宵禮盒";//Translate.在线二重礼;
public static String 在线三重礼 = "珍品元宵禮盒";//Translate.在线三重礼;
//public static String 在线四重礼 = Translate.在线四重礼;
public static String 在线活动标题 = "连续在线活动奖励";

public static long 十分钟 = 1l * 10 * 60 * 1000;
public static long 五十分钟 = 1l * 50 * 60 * 1000;
public static long 一二零分钟 = 1l * 120 * 60 * 1000;
public int[] 在线特殊日期 = new int[] { 2013, 2, 18 };
//public int[][] 指定日期 = { 在线特殊日期, { 2013, 2, 18 }, { 2013, 2, 19 }, { 2013, 2, 20 } };
public long start = TimeTool.formatter.varChar19.parse("2013-02-19 00:00:00");
public long end = TimeTool.formatter.varChar19.parse("2013-03-19 00:00:00");
public synchronized void onlineActivity(Player player, long time,JspWriter out) {
	try {
		out.print("open:" + ActivityManager.getInstance().分服活动open + "<br/>");
		if (!ActivityManager.getInstance().分服活动open) {
			return;
		}

			// 所有10级以上（含10级）的玩家只要连续在线达到一定时间，就可以获得飘渺寻仙曲送出的在线好礼。连续在线10分钟可获“在线一重礼”，
			// 连续在线30分钟可获“在线二重礼”，连续在线50分钟可获“在线三重礼”，连续在线70分钟可获“在线四重礼”

			if (player.getLevel() < 10) {
				return;
			}
			boolean bln = start <= time && time <= end;
			out.print("bln:"+bln + "</BR>");
			if (bln) {
				PlayerActivityRecord pr = null;
				Object o = null;//disk.get(player.getId());
				if (o == null) {
					pr = new PlayerActivityRecord();
				} else {
					pr = (PlayerActivityRecord) o;
				}

				long lastFlushOnlineTime = pr.getLastOnlineReceiveTime();

				boolean isSameDay = Utils.isSameDay(time, lastFlushOnlineTime);
				if (!isSameDay) {
					pr.setLastOnlineReceiveTime(time);
					pr.setFirstReceive(false);
					pr.setSecondReceive(false);
					pr.setThirdReceive(false);
					pr.setFourthReceive(false);
				}

				long duartionTime = time - player.getEnterServerTime();

//				if (duartionTime >= 七十分钟) {
//					if (!pr.isFourthReceive()) {
//						pr.setFourthReceive(true);
//						// 发邮件;
//
//						Article a = ArticleManager.getInstance().getArticle(在线四重礼);
//						ArticleEntity ae = ArticleEntityManager.getInstance().createEntity(a, true, ArticleEntityManager.运营发放活动奖励, player, a.getColorType(), 1, true);
//						MailManager.getInstance().sendMail(player.getId(), new ArticleEntity[] { ae }, new int[] { 1 }, 在线活动标题, "你已连续在线70分钟，获得" + 在线四重礼, 0, 0, 0, "qq在线活动");
//						player.sendError("你已连续在线70分钟，获得" + 在线四重礼);
//						ActivitySubSystem.logger.error("[连续在线活动完成] [70分钟] [" + player.getLogString() + "]");
//					}
//				}

				if (duartionTime >= 一二零分钟) {
					if (!pr.isThirdReceive()) {
						pr.setThirdReceive(true);
						// 发邮件;
						Article a = ArticleManager.getInstance().getArticle(在线三重礼);
						ArticleEntity ae = ArticleEntityManager.getInstance().createEntity(a, true, ArticleEntityManager.运营发放活动奖励, player, a.getColorType(), 1, true);
						MailManager.getInstance().sendMail(player.getId(), new ArticleEntity[] { ae }, new int[] { 1 }, 在线活动标题, "你已連續線上120分鐘，獲得:" + 在线三重礼, 0, 0, 0, "在线活动",MailSendType.后台发送,player.getName(),ip,recorder);
						player.sendError("你已連續線上120分鐘，獲得:" + 在线三重礼);
						ActivitySubSystem.logger.error("[连续在线活动完成] [120分钟] [" + player.getLogString() + "]");
					}

				}

				if (duartionTime >= 五十分钟) {
					if (!pr.isSecondReceive()) {
						pr.setSecondReceive(true);
						// 发邮件;
						Article a = ArticleManager.getInstance().getArticle(在线二重礼);
						ArticleEntity ae = ArticleEntityManager.getInstance().createEntity(a, true, ArticleEntityManager.运营发放活动奖励, player, a.getColorType(), 1, true);
						MailManager.getInstance().sendMail(player.getId(), new ArticleEntity[] { ae }, new int[] { 1 }, 在线活动标题, "你已连续在线50分钟，获得" + 在线二重礼, 0, 0, 0, "在线活动",MailSendType.后台发送,player.getName(),ip,recorder);
						player.sendError("你已連續線上50分鐘，獲得:" + 在线二重礼);
						ActivitySubSystem.logger.error("[连续在线活动完成] [50分钟] [" + player.getLogString() + "]");
					}
				}

				if (duartionTime >= 十分钟) {
					if (!pr.isFirstReceive()) {
						pr.setFirstReceive(true);
						// 发邮件;
						Article a = ArticleManager.getInstance().getArticle(在线一重礼);
						ArticleEntity ae = ArticleEntityManager.getInstance().createEntity(a, true, ArticleEntityManager.运营发放活动奖励, player, a.getColorType(), 1, true);
						MailManager.getInstance().sendMail(player.getId(), new ArticleEntity[] { ae }, new int[] { 1 }, 在线活动标题, "你已連續線上10分鐘，獲得:" + 在线一重礼, 0, 0, 0, "在线活动",MailSendType.后台发送,player.getName(),ip,recorder);
						player.sendError("你已連續線上10分鐘，獲得:" + 在线一重礼);
						ActivitySubSystem.logger.error("[连续在线活动完成] [10分钟] [" + player.getLogString() + "]");
					}
				}

				if (pr.isDirty()) {
					ActivitySubSystem.logger.warn("[保存活动对象] [" + player.getLogString() + "] [" + pr.isDirty() + "]");
					pr.setDirty(false);
					//disk.put(player.getId(), pr);
				}
			} else {
				if (ActivitySubSystem.logger.isDebugEnabled()) {
					ActivitySubSystem.logger.debug("[在线活动不在指定时间] [" + player.getLogString() + "]");
				}

		}
	} catch (Exception e) {
		ActivitySubSystem.logger.error("[连续在线活动异常] [" + player.getLogString() + "]", e);
	}
}
%>
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title>Insert title here</title>
</head>
<body>

</body>
</html>