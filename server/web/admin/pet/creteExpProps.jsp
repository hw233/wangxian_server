<%@page import="com.fy.engineserver.mail.MailSendType"%>
<%@ page language="java" contentType="text/html; charset=utf-8"
	pageEncoding="utf-8"%>
<%@page import="java.util.*"%>

<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<%@page import="com.fy.engineserver.datasource.article.data.articles.Article"%>
<%@page import="com.fy.engineserver.datasource.article.manager.ArticleManager"%>
<%@page import="com.fy.engineserver.sprite.Player"%>
<%@page import="com.fy.engineserver.sprite.PlayerManager"%>
<%@page import="com.fy.engineserver.datasource.article.data.entity.ArticleEntity"%>
<%@page import="com.fy.engineserver.datasource.article.manager.ArticleEntityManager"%>
<%@page import="com.fy.engineserver.sprite.pet.SingleForPetPropsEntity"%>
<%@page import="com.fy.engineserver.mail.service.MailManager"%>
<%@page import="com.fy.engineserver.sprite.pet.PetManager"%><html>
<head>
<title>生成宠物经验丹</title>
</head>
<body>
	
	<%
	String ip = request.getRemoteAddr();
	String recorder = "";
	Object o = session.getAttribute("authorize.username");
	if(o!=null){
		recorder = o.toString();
	}
		String name = request.getParameter("name");
		String values = request.getParameter("values");
		String mailContent = request.getParameter("mailContent");
		if(name == null || name.equals("") ||values == null || values.equals("")){
			
		}else{
			Player player = PlayerManager.getInstance().getPlayer(name);
			long value = Long.parseLong(values);
			Article a = ArticleManager.getInstance().getArticle("玄兽丹");
			
			ArticleEntity ae = ArticleEntityManager.getInstance().createEntity(a,true,35,null,0,1,true);
			if(ae instanceof SingleForPetPropsEntity){
				SingleForPetPropsEntity  se = (SingleForPetPropsEntity)ae;
				se.setExp(value);
			}else{
				out.print("不是宠物经验丹"+ae.getClass());
			}
		  //MailManager.getInstance().sendMail(player.getId(), new ArticleEntity[]{pep}, mailTitle, mailContent, 0, 0, 0,"后台补删除宠物");
			MailManager.getInstance().sendMail(-1,player.getId(), new ArticleEntity[]{ae}, "补偿玄兽丹","经过确认，您使用玄兽丹喂养宠物没加经验，现补偿玄兽丹。",0, 0, 0,"后台补删除玄兽丹",MailSendType.后台发送,player.getName(),ip,recorder);
			PetManager.logger.warn("[补偿玄兽丹成功] ["+player.getLogString()+"] [物品:"+ae.getId()+"]");
			out.print("发送完成");
			
		}
	%>
	
	<form action="">
		
		人物名:<input type="text" name="name"/><br/>
		value:<input type="text" name="values"/><br/>
		<input type="submit" value="submit"/>
	</form>
	
	

</body>
</html>
