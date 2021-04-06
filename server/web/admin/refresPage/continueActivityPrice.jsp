<%@page import="com.fy.engineserver.mail.MailSendType"%>
<%@page import="com.fy.engineserver.datasource.article.manager.ArticleEntityManager"%>
<%@page import="com.fy.engineserver.datasource.article.data.articles.Article"%>
<%@page import="com.fy.engineserver.datasource.article.manager.ArticleManager"%>
<%@page import="java.util.*"%>
<%@page import="com.xuanzhi.boss.game.GameConstants"%>
<%@page import="com.fy.engineserver.datasource.article.data.entity.ArticleEntity"%>
<%@page import="com.fy.engineserver.mail.service.MailManager"%>
<%@page import="com.fy.engineserver.sprite.PlayerManager"%>
<%@page import="com.fy.engineserver.sprite.Player"%>
<%@ page language="java" contentType="text/html; charset=UTF-8"pageEncoding="UTF-8"%>
<%
String ip = request.getRemoteAddr();
String recorder = "";
Object o = session.getAttribute("authorize.username");
if(o!=null){
	recorder = o.toString();
}
	String mailtitile = "连登奖励领取补偿";
	String mailcontent = "亲爱的玩家，为补偿连登领取奖励的问题，现赠予10个连登令旗作为补偿，注意查收";
	String mailcontent_green = "亲爱的玩家，为补偿连登领取奖励的问题，现赠予30个银块作为补偿，注意查收。";
	Set<String> set4 = new HashSet<String>();
	set4.add("天下无双");
	set4.add("海纳百川");
	set4.add("琼楼金阙");
	set4.add("飘渺仙道");
	set4.add("万里苍穹");
	set4.add("盛世欢歌");
	set4.add("修罗转生");
	Player ps [] = PlayerManager.getInstance().getOnlinePlayers();
	MailManager mm = MailManager.getInstance();
	String servername = GameConstants.getInstance().getServerName();
	Article a = ArticleManager.getInstance().getArticle("银块");
	Article a2 = ArticleManager.getInstance().getArticle("连登令旗");
	ArticleEntity ae = null;
	int 成功数 = 0;
	int 异常数 = 0;
	
	for(Player p : ps){
		try{
			if(set4.contains(servername)){
				ae = ArticleEntityManager.getInstance().createEntity(a, true, ArticleEntityManager.活动, p, a.getColorType(), 30, true);
				mm.sendMail(p.getId(), new ArticleEntity[]{ae}, new int[]{30}, mailtitile, mailcontent_green, 0, 0, 0, "补偿",MailSendType.后台发送,p.getName(),ip,recorder);
				成功数++;
			}else{
				ae = ArticleEntityManager.getInstance().createEntity(a2, true, ArticleEntityManager.活动, p, a.getColorType(), 10, true);
				mm.sendMail(p.getId(), new ArticleEntity[]{ae}, new int[]{10}, mailtitile, mailcontent, 0, 0, 0, "补偿",MailSendType.后台发送,p.getName(),ip,recorder);
				成功数++;
			}
		}catch(Exception e){
			异常数++;
			out.print(e+"<br>");
		}
	}
	out.print("<"+servername+">发送补偿成功玩家数量："+成功数+"--失败数量："+异常数);

%>
