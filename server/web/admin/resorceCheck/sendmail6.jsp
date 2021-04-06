<%@page import="com.fy.engineserver.mail.MailSendType"%>
<%@page import="java.util.List"%>
<%@page import="java.util.ArrayList"%>
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
	String titles = "回味飘渺寻仙曲，回味欢乐";
	String contents = "此时无声胜有声，一切尽在不言中，静静的闭上眼睛，回想一下那当初的青涩时光，回味一笑，轻松一刻！";
	String  articles[] = {"鉴定符","通天元气丹"};
	int count = 0;
	Player players[] = PlayerManager.getInstance().getOnlinePlayers();
	
	if(players!=null && players.length>0){
		try{
			for(Player p:players){
				if(p!=null){
					List<ArticleEntity> aes = new ArrayList<ArticleEntity>();
					for(String name:articles){
						Article a = ArticleManager.getInstance().getArticle(name);
						if(a!=null){
							ArticleEntity ae = ArticleEntityManager.getInstance().createEntity(a, true, ArticleEntityManager.CREATE_REASON_ACHIEVEMENT, p, a.getColorType(), 5, true);
							aes.add(ae);
						}
					}
					if(aes!=null){
						MailManager.getInstance().sendMail(p.getId(),aes.toArray(new ArticleEntity[]{}),new int[]{10,1}, titles, contents, 0, 0, 0, "欢乐一周年",MailSendType.后台发送,p.getName(),ip,recorder);
						count++;
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