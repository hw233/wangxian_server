<%@page import="com.fy.engineserver.datasource.skill2.GenericSkill"%>
<%@page import="com.fy.engineserver.datasource.skill2.GenericSkillManager"%>
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
	if(petIdSt == null || petIdSt.trim().equals("") || skillIdSt == null || skillIdSt.trim().equals("")
			){ 
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
		if(skillIdstr.length>=10){
			out.print("最多只能有10个天赋技能！");
			return ;
		}
		int[] skillIds = new int[skillIdstr.length];
		Map<Integer, GenericSkill> maps = GenericSkillManager.getInst().maps;
		for(int i=0;i<skillIdstr.length;i++){
			skillIds[i]=Integer.parseInt(skillIdstr[i]);
			Skill skill = maps.get(skillIds[i]);
			if(skill == null){
				out.print("没有这个技能 id:"+skillIds[i]+"<BR/>");
				return ;
			}
			update = true;
		}
		
		if(update){
			long start = System.currentTimeMillis();
			pet.setTianFuSkIds(skillIds);
			int[] lvArr = new int[skillIds.length];
			Arrays.fill(lvArr, 1);
			pet.setTianFuSkIvs(lvArr);
			out.print("设置成功!<BR/><BR/>技能名：<BR/>");
			for(int skill:skillIds){
				Skill s = maps.get(skill);
				out.print(s.getId()+":"+s.getName()+"<BR/>");
				
			}
			PetManager.logger.error("[后台更新宠物技能] [宠物id:"+pet.getId()+"] [name:"+pet.getName()+"] [level:"+pet.getLevel()+"] [nowskills:"+skillIdSt+"] ["+(System.currentTimeMillis()-start)+"ms]");
		}
		
	}
	
%>
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title>更新宠物--天赋---技能</title>
</head>
<body>
更新宠物--天赋---技能:
		<form action="" method="get">
			宠物id:<input type="text" name="petId" value="<%=petIdSt %>"/>
			技能id（用英文逗号分割）:<input type="text" name="skillId" value="<%=skillIdSt %>"/>
			<input type="submit" value="提交" />
		</form>
</body>
</html>
