<%@ page language="java" contentType="text/html; charset=utf-8"
    pageEncoding="utf-8"%>
<%@page import="java.util.*,
	com.xuanzhi.tools.text.*,
	com.fy.engineserver.datasource.article.manager.*,
	com.fy.engineserver.datasource.article.data.props.*,
	com.fy.engineserver.datasource.article.data.entity.*,
	com.fy.engineserver.util.*,
	com.fy.engineserver.sprite.*,
	com.fy.engineserver.sprite.pet.*,
	com.fy.engineserver.datasource.skill.*,
	com.fy.engineserver.datasource.career.*"

%>

<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<%@page import="com.fy.engineserver.datasource.article.data.articles.Article"%>
<%@page import="com.fy.engineserver.jsp.propertyvalue.SkillManager"%>
<%@page import="com.fy.engineserver.datasource.skill.Skill"%><html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title></title>
<style type="text/css">

</style>
<script type="text/javascript">

</script>
</head>
<body>
<br><br>

	<%
		String petIdSt = request.getParameter("petId");
		String skillIdSt = request.getParameter("skillId");
		if(petIdSt == null || petIdSt.trim().equals("") || skillIdSt == null || skillIdSt.trim().equals("")){ 
			%>
				<form action="" method="get">
					宠物id:<input type="text" name="petId" />
					技能id:<input type="text" name="skillId" />
					<input type="submit" value="提交" />
				</form>
			
			
			<% 
		}else{
			long petId = Long.parseLong(petIdSt.trim());
			int skillId = Integer.parseInt(skillIdSt.trim());
			
			PetManager pm = PetManager.getInstance();
			CareerManager cm = CareerManager.getInstance();
			
			Pet pet = pm.getPet(petId);
			if(pet == null){
				out.print("没有这个宠物");
				return;
			}
			Skill skill = cm.getSkillById(skillId);
			if(skill == null){
				out.print("没有这个技能");
				return ;
			}
			int[] newSkill = {skillId};
			int[] newSkillLevel = {1};
			pet.setSkillIds(newSkill);
			pet.setActiveSkillLevels(newSkillLevel);
			out.print("设置成功,技能名："+skill.getName());
			
		}
		
	%>

</body>
</html>
