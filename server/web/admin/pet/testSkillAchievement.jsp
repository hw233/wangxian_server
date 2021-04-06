<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<%@ page language="java" contentType="text/html; charset=utf-8" pageEncoding="utf-8"%>
<%@page import="java.util.*,
	com.xuanzhi.tools.text.*,
	com.fy.engineserver.datasource.article.manager.*,
	com.fy.engineserver.datasource.article.data.props.*,
	com.fy.engineserver.datasource.article.data.entity.*,
	com.fy.engineserver.util.*,
	com.fy.engineserver.sprite.*,
	com.fy.engineserver.sprite.pet.*"
%>
<%@page import="com.fy.engineserver.datasource.article.data.articles.Article"%>

<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8">
<title>测试宠物技能成就</title>
</head>
<body>

	<%
	
	String petEntityIds = request.getParameter("petEntityId");
	
	if(petEntityIds != null && !petEntityIds.equals("")){
		long entityId = Long.parseLong(petEntityIds);
		ArticleEntity ae =  ArticleEntityManager.getInstance().getEntity(entityId);
		if(ae != null ){
			
			PetPropsEntity ppe = (PetPropsEntity)ae;
			
			long petId = ppe.getPetId();
			Pet pet = PetManager.getInstance().getPet(petId);
			
			if(pet != null){
				int matchNum = 0;
				for(int skillId : pet.getSkillIds()){
					Map<Integer, PetSkillReleaseProbability> map = PetManager.getInstance().getMap();
					if(map != null){
						PetSkillReleaseProbability pb = map.get(skillId);
						out.print("技能职业:"+pb.getCharacter()+"宠物职业："+pet.getCharacter()+"<br/>");
						if(pb != null){
							if(pb.getCharacter() == pet.getCharacter()){
								++matchNum;
							}
						}
					}
				}
				out.print("合适技能:"+matchNum);
			}else{
				out.print("pet null");
			}
			
		}else{
			out.print("ae null");
		}
		
		
		return ;
	}
	%>



	<form action="">
		宠物道具id:<input type="text" name="petEntityId"/><br/>
		<input type="submit" value="submit"/><br/>
	</form>

</body>
</html>
