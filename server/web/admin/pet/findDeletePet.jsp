<%@page import="com.fy.engineserver.mail.MailSendType"%>
<%@ page language="java" contentType="text/html; charset=utf-8"
	pageEncoding="utf-8"%>
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
<%@page import="com.fy.engineserver.util.gmstat.RecordEvent"%>
<%@page import="com.fy.engineserver.util.gmstat.EventManager"%>
<%@page import="com.fy.engineserver.util.gmstat.event.MailEvent"%><html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title>找回删除的宠物</title>
</head>
<body>
	
	<%
	String ip = request.getRemoteAddr();
	String recorder = "";
	Object o = session.getAttribute("authorize.username");
	if(o!=null){
		recorder = o.toString();
	}
		String petIds = request.getParameter("petIds");
		String name = request.getParameter("name");
		String mailTitle = request.getParameter("mailTitle");
		String mailContent = request.getParameter("mailContent");
		if(petIds == null || petIds.equals("") ||name == null || name.equals("")){
			
		}else{
			
			if(mailTitle == null || mailTitle.equals("") || mailContent == null || mailContent.equals("")){
				out.print("正确输入邮件标题，内容");
			}else{
				long petId = Long.parseLong(petIds);
				Pet pet = PetManager.getInstance().getPet(petId);
				if(pet != null){
						
						Player player = PlayerManager.getInstance().getPlayer(name);
						pet.setDelete(false);
						
						long petEntityId = pet.getPetPropsId();
						
						ArticleEntity ae =  ArticleEntityManager.getInstance().getEntity(petEntityId);
						if(ae != null){
							
							if(ae instanceof PetPropsEntity){
							PetPropsEntity ppe = (PetPropsEntity)ae;
							String eggName =  ppe.getEggArticle();
							out.print(eggName+"<br/>");
							
							Article a = ArticleManager.getInstance().getArticle(eggName);
							PetEggPropsEntity pep = (PetEggPropsEntity) ArticleEntityManager.getInstance().createEntity(a, false, ArticleEntityManager.CREATE_REASON_PET_SEAL, null, 0, 1, true);
							pep.setPetId(pet.getId());
							if (pet.isIdentity()) {
								switch (pet.getQuality()) {
								case 0:
									pep.setColorType(0);
									break;
								case 1:
									pep.setColorType(1);
									break;
								case 2:
									pep.setColorType(2);
									break;
								case 3:
									pep.setColorType(3);
									break;
								case 4:
									pep.setColorType(4);
									break;
								default:
									break;
	
								}
							} else {
								pep.setColorType(0);
							}
							
							if(pep != null){
								//MailManager.getInstance().sendMail(player.getId(), new ArticleEntity[]{pep}, "丢失物品找回", "您好，由于误操作放生，导致宠物删除，经过核实给您补上。", 0, 0, 0,"后台补删除宠物");
								MailManager.getInstance().sendMail(-1,player.getId(), new ArticleEntity[]{pep}, mailTitle, mailContent, 0, 0, 0,"后台补删除宠物",MailSendType.后台发送,player.getName(),ip,recorder);
								//MailManager.getInstance().sendMail(player.getId(), new ArticleEntity[]{pep}, "成长活动三：崭露头角", "恭喜您获得当乐成长活动幸运奖，请您在附件中查收奖励，感谢您的参与和支持！", 0, 0, 0,"后台发活动奖励宠物");
								PetManager.logger.warn("[找回删除宠物成功] ["+player.getLogString()+"] [petId:"+pet.getId()+"]");
								out.print("发送完成");
								return;
							}
							}else{
								out.print(ae.getClass());
							}
						}else{
							out.print("没有找到宠物实体"+petEntityId);
						}
				
				}else{
					out.print("没有这个宠物"+petId);
				}
			}
		}
	%>
	
	<form action="">
		
		人物名:<input type="text" name="name"/><br/>
		宠物id:<input type="text" name="petIds"/><br/>
		邮件标题:<input type="text" name="mailTitle" value="丢失宠物找回"/><br/>
		邮件内容:<input type="text" name="mailContent" value="您好，由于误操作放生，导致宠物删除，经过核实给您补上。"/><br/>
		<input type="submit" value="submit"/>
	</form>
	
	

</body>
</html>
