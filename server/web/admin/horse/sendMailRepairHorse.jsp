<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Frameset//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-frameset.dtd">
<%@ page language="java" contentType="text/html; charset=utf-8" pageEncoding="utf-8"%>
<%@page import="java.util.*"%>
<%@page import="com.fy.engineserver.sprite.horse.HorseManager"%>


<%@page import="com.fy.engineserver.sprite.Player"%>
<%@page import="com.fy.engineserver.sprite.PlayerManager"%>
<%@page import="com.fy.engineserver.datasource.article.data.articles.Article"%>
<%@page import="com.fy.engineserver.datasource.article.manager.ArticleManager"%>
<%@page import="com.fy.engineserver.datasource.article.data.props.HorseProps"%>
<%@page import="com.fy.engineserver.datasource.article.data.entity.ArticleEntity"%>
<%@page import="com.fy.engineserver.datasource.article.manager.ArticleEntityManager"%>
<%@page import="com.fy.engineserver.mail.service.MailManager"%><html>
<head>
<title>发邮件补丢失坐骑</title>
</head>
<body>

<%
	String name = request.getParameter("name");
	String horseName = request.getParameter("horseName");
	
	if(name != null && !name.equals("") && horseName != null && !horseName.equals("")){
		
		try{
			Player player = PlayerManager.getInstance().getPlayer(name);
			Article a = ArticleManager.getInstance().getArticle(horseName);
			if(a != null && a instanceof HorseProps){
				
				ArticleEntity ae = ArticleEntityManager.getInstance().createEntity(a,true,ArticleEntityManager.CREATE_REASON_后台,null,0,1,true);
				if(ae != null){
					MailManager.getInstance().sendMail(player.getId(), new ArticleEntity[]{ae}, "丢失坐骑补偿", "您好，由于系统问题导致坐骑丢失，经过核实给您补上，造成的不便请您谅解。", 0, 0, 0);
					HorseManager.logger.warn("[发送补偿坐骑成功] ["+player.getLogString()+"]");
					out.print("发送完成");
				}
			}
		}catch(Exception e){
			out.print(e);
			HorseManager.logger.warn("[发送补偿坐骑异常] ["+name+"]",e);
		}
		
		return ;
	}else{
		%>
		
		<form action="">
			playerName:<input type="text" name="name"/></br>
			horseName:<input type="text" name="horseName"/></br>
			<input type="submit" value="submit"/>
		</form>
		
		<%
	}


%>




</body>

</html>
