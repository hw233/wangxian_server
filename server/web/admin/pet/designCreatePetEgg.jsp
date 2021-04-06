<%@ page contentType="text/html;charset=utf-8"%><!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Frameset//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-frameset.dtd">
<%@page import="com.fy.engineserver.sprite.pet.*"%>
<%@page import="com.fy.engineserver.sprite.pet.Pet"%>
<%@page import="com.fy.engineserver.sprite.pet.PetManager"%>

<%@page import="com.fy.engineserver.sprite.Player"%>
<%@page import="com.fy.engineserver.sprite.PlayerManager"%>
<%@page import="com.fy.engineserver.datasource.article.data.articles.Article"%>
<%@page import="com.fy.engineserver.datasource.article.manager.ArticleManager"%>
<%@page import="com.fy.engineserver.datasource.article.data.props.PetEggProps"%>
<%@page import="com.fy.engineserver.datasource.article.data.entity.PetEggPropsEntity"%>
<%@page import="com.fy.engineserver.datasource.article.manager.ArticleEntityManager"%>
<%@page import="com.fy.engineserver.datasource.article.data.entity.PetPropsEntity"%>
<%@page import="com.fy.engineserver.datasource.article.data.props.PetProps"%>
<%@page import="com.xuanzhi.tools.text.StringUtil"%>
<%@page import="com.fy.engineserver.mail.service.MailManager"%>
<%@page import="com.fy.engineserver.datasource.article.data.entity.ArticleEntity"%><html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
<title>补偿宠物</title>
</head>
<body>

	<%
	
		
		String name = request.getParameter("name");
		String petEgg = request.getParameter("petEgg");
		String qualityst = request.getParameter("quality");
		String growthClassst = request.getParameter("growthClass");
		if(name == null || petEgg == null || qualityst == null || growthClassst == null){
			
		}else{
			
			Player player = PlayerManager.getInstance().getPlayer(name.trim());
			Article a = ArticleManager.getInstance().getArticle(petEgg);
			
			if(a != null){
				
				if(a instanceof PetEggProps){
					try{
						PetEggProps eggProps = (PetEggProps)a;
						String petArticleName = eggProps.getPetArticleName();
						Article petArticle = ArticleManager.getInstance().getArticle(petArticleName);
						
						PetPropsEntity ppe = (PetPropsEntity) ArticleEntityManager.getInstance().createEntity(petArticle, false, ArticleEntityManager.CREATE_REASON_PET_BREED, player, 0, 1, true);
						ppe.setEggArticle(eggProps.getName());
						
						Pet pet =  PetManager.getInstance().createPetByProps(eggProps,(PetProps)petArticle);
						pet.setQuality(Byte.parseByte(qualityst));
						pet.setGrowthClass(Byte.parseByte(growthClassst));
						pet.setIdentity(true);
						PetManager.logger.warn("[生成补偿宠物] [petId:"+pet.getId()+"]");
						
						PetEggPropsEntity pep = (PetEggPropsEntity) ArticleEntityManager.getInstance().createEntity(eggProps, false, ArticleEntityManager.CREATE_REASON_PET_SEAL, null, 0, 1, true);
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
							MailManager.getInstance().sendMail(player.getId(), new ArticleEntity[]{pep}, "宠物补偿", "您好，由于系统问题导致宠物丢失，经过核实给您补上，造成的不便请您谅解。", 0, 0, 0);
							PetManager.logger.warn("[发送补偿成功] ["+player.getLogString()+"]");
							out.print("发送完成");
						}
					}catch(Exception e){
						String st = StringUtil.getStackTrace(e);
						out.print(st);
					}
				}else{
					out.print("不是宠物蛋道具"+a.getClass());
				}
			}else{
				out.print("没有这个article"+petEgg);
			}
			
			
			return;
		}
	%>


	<form action="">
		
		人物名:<input type="text" name="name"/><br/>
		宠物蛋:<input type="text" name="petEgg"/><br/>
		
		品质(0~4:普,灵,仙,神,圣):<input type="text" name="quality"/><br/>
		成长率(0~4:普通,一般,优秀,卓越,完美):<input type="text" name="growthClass"/><br/>
		
		<input type="submit" value="submit"/>
	</form>

</body>

</html>
