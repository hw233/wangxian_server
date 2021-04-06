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
<%@page import="com.fy.engineserver.datasource.article.data.articles.Article"%>
<%@page import="com.fy.engineserver.jsp.propertyvalue.SkillManager"%>
<%@page import="com.fy.engineserver.datasource.skill.Skill"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">

<%
	String petIdSt = request.getParameter("petId");
	String skillIdSt = request.getParameter("skillId");
	String skillIdTF = request.getParameter("skillIdtf");
	if(petIdSt == null || petIdSt.trim().equals("") ||skillIdTF == null || skillIdTF.trim().equals("")){
		
	}else{
		long petId = Long.parseLong(petIdSt.trim());
		PetManager pm = PetManager.getInstance();
		Pet pet = pm.getPet(petId);
		boolean update = false;
		if(pet == null){
			out.print("没有这个宠物 id:"+petId+"<BR/>");
			return;
		}
		
		String[] skillIdstr = skillIdTF.trim().split(",");
		int[] skillIds = new int[skillIdstr.length];
		CareerManager cm = CareerManager.getInstance();
		for(int i=0;i<skillIdstr.length;i++){
			skillIds[i]=Integer.parseInt(skillIdstr[i]);
		}
		
			long start = System.currentTimeMillis();
			pet.setTianFuSkIds(skillIds);
			out.print("设置天赋技能成功!<BR/><BR/>技能名："+Arrays.toString(skillIds)+"<BR/>");
	}
	
	if(petIdSt == null || petIdSt.trim().equals("") || skillIdSt == null || skillIdSt.trim().equals("")){ 
		out.print("<h2>宠物id和技能id都不能为空</h2><BR/>"); 
		petIdSt = "";
		skillIdSt = "";
	}else{
		long petId = Long.parseLong(petIdSt.trim());
		PetManager pm = PetManager.getInstance();
		Pet pet = pm.getPet(petId);
		boolean update = false;
		if(pet == null){
			out.print("没有这个宠物 id:"+petId+"<BR/>");
			return;
		}
		
		String[] skillIdstr = skillIdSt.trim().split(",");
		int[] skillIds = new int[skillIdstr.length];
		CareerManager cm = CareerManager.getInstance();
		for(int i=0;i<skillIdstr.length;i++){
			skillIds[i]=Integer.parseInt(skillIdstr[i]);
			Skill skill = cm.getSkillById(skillIds[i]);
			if(skill == null){
				out.print("没有这个技能 id:"+skillIds[i]+"<BR/>");
				return ;
			}
			update = true;
		}
		
		if(update){
			long start = System.currentTimeMillis();
			pet.setSkillIds(skillIds);
			out.print("设置成功!<BR/><BR/>技能名：<BR/>");
			for(int skill:skillIds){
				Skill s = cm.getSkillById(skill);
				out.print(s.getId()+":"+s.getName()+"<BR/>");
				
			}
			PetManager.logger.error("[后台更新宠物技能] [宠物id:"+pet.getId()+"] [name:"+pet.getName()+"] [level:"+pet.getLevel()+"] [nowskills:"+skillIdSt+"] ["+(System.currentTimeMillis()-start)+"ms]");
		}
		
	}
	
%>
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title>更新宠物技能</title>
</head>
<body>
		<form action="" method="get">
			宠物id:<input type="text" name="petId" value="<%=petIdSt %>"/>
			技能id:<input type="text" name="skillId" value="<%=skillIdSt %>"/>
			天赋技能id:<input type="text" name="skillIdtf" value="<%=skillIdTF %>"/>
			<input type="submit" value="提交" />
		</form>
</body>
</html>
