<%@page import="com.fy.engineserver.mail.MailSendType"%>
<%@ page language="java" contentType="text/html; charset=utf-8" pageEncoding="utf-8"%>
<%@page import="java.util.*"%>

<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<%@page import="com.fy.engineserver.datasource.article.data.articles.Article"%>
<%@page import="com.fy.engineserver.sprite.pet.PetManager"%>
<%@page import="com.fy.engineserver.sprite.pet.Pet"%>
<%@page import="com.fy.engineserver.sprite.Player"%>
<%@page import="com.fy.engineserver.sprite.PlayerManager"%>
<%@page import="com.fy.engineserver.datasource.article.data.entity.PetPropsEntity"%>
<%@page import="com.fy.engineserver.datasource.article.manager.ArticleEntityManager"%>
<%@page import="com.fy.engineserver.datasource.article.data.entity.PetEggPropsEntity"%>
<%@page import="com.fy.engineserver.datasource.article.data.entity.ArticleEntity"%>
<%@page import="com.fy.engineserver.mail.service.MailManager"%>
<%@page import="com.fy.engineserver.datasource.article.manager.ArticleManager"%>
<%@page import="com.fy.engineserver.datasource.article.data.props.Knapsack"%>
<%@page import="com.fy.engineserver.datasource.article.data.props.Cell"%>
<html>
<head>
<title>更新指定玩家的宠物背包</title>
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
		String entityIds = request.getParameter("entityId");
		if(name == null || name.equals("") || entityIds == null || entityIds.equals("")){
			
		}else{
			
			
			long entityId = Long.parseLong(entityIds);
			Player player = PlayerManager.getInstance().getPlayer(name);
			Knapsack k = player.getPetKnapsack();
			
			Cell[] cells = k.getCells();
			for(Cell c:cells){
				long id = c.getEntityId();
				ArticleEntity ae = ArticleEntityManager.getInstance().getEntity(id);
				
				if(ae != null){
					if(ae instanceof PetPropsEntity && id == entityId){
						
						PetPropsEntity ppe = (PetPropsEntity)ae;
						
						String eggArticleName = ppe.getEggArticle();
						Article eggArticle = ArticleManager.getInstance().getArticle(eggArticleName);
						if(eggArticle != null){
							
							ArticleEntity eggAE = ArticleEntityManager.getInstance().createEntity(eggArticle,false,ArticleEntityManager.CREATE_REASON_后台,null,0,1,true);
							
							if(eggAE != null){
								MailManager.getInstance().sendMail(-1,player.getId(), new ArticleEntity[]{eggAE}, "寻回丢失宠物", "寻回丢失宠物", 0, 0, 0,"后台补丢失宠物",MailSendType.后台发送,player.getName(),ip,recorder);
								PetManager.logger.warn("[寻回丢失宠物] ["+player.getLogString()+"] [宠物道具id:"+entityId+"]");
							}
							c.entityId = -1;
							k.setModified(true);
							out.print("发送完成<br/>");
						}else{
							out.print("没有找到对应的蛋的article"+eggArticleName);
						}
					}
				}
			}
			out.print("success");
			return;
		}
	%>
	
	<form action="">
		人物名:<input type="text" name="name"/><br/>
		指定id:<input type="text" name="entityId"/><br/>
		<input type="submit" value="submit"/>
	</form>
	
	

</body>
</html>
