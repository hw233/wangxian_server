<%@page import="com.fy.engineserver.mail.MailSendType"%>
<%@page import="com.fy.engineserver.datasource.article.data.articles.Article"%>
<%@page import="com.fy.engineserver.datasource.article.manager.ArticleEntityManager"%>
<%@page import="com.fy.engineserver.datasource.article.manager.ArticleManager"%>
<%@page import="com.fy.engineserver.datasource.article.data.entity.ArticleEntity"%>
<%@page import="com.fy.engineserver.mail.service.MailManager"%>
<%@page import="com.fy.engineserver.sprite.Player"%>
<%@page import="com.fy.engineserver.sprite.PlayerManager"%>
<%@ page language="java" contentType="text/html; charset=utf-8" pageEncoding="utf-8"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<head>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8">
<title>发邮件</title>
</head>
<%
String ip = request.getRemoteAddr();
String recorder = "";
Object o = session.getAttribute("authorize.username");
if(o!=null){
	recorder = o.toString();
}
	String titles = "欢乐一周年";
	String contents = "飘渺寻仙曲一周年，欢乐共开怀，举起您的酒杯让我们共同庆祝这特殊的一天！";
	String  articles = "酒票";
	int count = 0;
	Player players[] = PlayerManager.getInstance().getOnlinePlayers();
	if(players!=null && players.length>0){
		try{
			for(Player p:players){
				if(p!=null){
					Article a = ArticleManager.getInstance().getArticle(articles);
					if(a!=null){
						ArticleEntity ae = ArticleEntityManager.getInstance().createEntity(a, true, ArticleEntityManager.CREATE_REASON_ACHIEVEMENT, p, a.getColorType(), 5, true);
						if(ae!=null){
							MailManager.getInstance().sendMail(p.getId(), new ArticleEntity[]{ae},new int[]{5}, titles, contents, 0, 0, 0, "欢乐一周年",MailSendType.后台发送,p.getName(),ip,recorder);
							count++;
						}
					}
				}
			}
		}catch(Exception e){
			e.printStackTrace();
			out.print(e);
		}
		out.print("发送完毕，数量："+count);
	}
	
%>