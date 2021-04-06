<%@ page language="java" contentType="text/html; charset=utf-8"
	pageEncoding="utf-8"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<%@page import="com.fy.engineserver.datasource.article.data.articles.Article"%>
<%@page import="java.util.List"%>
<%@page import="com.fy.engineserver.sprite.pet.PetManager"%>
<%@page import="com.fy.engineserver.sprite.pet.PetEatPropRule"%>
<%@page import="com.fy.engineserver.sprite.pet.PetEatProp2Rule"%>
<%@page import="java.util.Map"%>
<%@page import="com.fy.engineserver.sprite.pet.PetFlySkillInfo"%>
<%@page import="java.util.Iterator"%>
<%@page import="java.util.Map.Entry"%>
<%@page import="com.fy.engineserver.sprite.pet2.GradePet"%>
<%@page import="com.fy.engineserver.sprite.pet2.PetGrade"%>
<%@page import="com.fy.engineserver.sprite.Player"%>
<%@page import="com.fy.engineserver.sprite.PlayerManager"%>
<%@page import="com.fy.engineserver.sprite.pet.Pet"%>
<%@page import="com.fy.engineserver.sprite.pet.PetFlyState"%><html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title>宠物飞升测试数据</title>
</head>
<body>
<%
	out.print("<h2>易筋丹信息</h2>");
	List<PetEatPropRule> eatRules = PetManager.eatRules;
	for(PetEatPropRule rule : eatRules){
		out.print(rule+"<br>");
	}
	out.print("<hr>");
	
	out.print("<h2>升华露信息</h2>");
	List<PetEatProp2Rule> eat2Rules = PetManager.eat2Rules;
	for(PetEatProp2Rule rule : eat2Rules){
		out.print(rule+"<br>");
	}
	out.print("<hr>");
	
	out.print("<h2>宠物飞升技能信息</h2>");
	Map<Integer,PetFlySkillInfo> petFlySkills = PetManager.petFlySkills;
	Iterator it = petFlySkills.entrySet().iterator();
	while(it.hasNext()){
		Map.Entry<Integer, PetFlySkillInfo> en = (Map.Entry<Integer, PetFlySkillInfo>)it.next();
		out.print(en.getKey()+"--"+en.getValue()+"<br>");
	}
	out.print("<hr>");
	
	out.print("<h2>进阶宠物信息</h2>");
	GradePet[] petList = PetGrade.petList;
	for(GradePet pet : petList){
		out.print("[宠物:"+pet.getName()+"] [avatatype:"+pet.getFlyType()+"] [avata:"+pet.getFlyAvata()+"]<br>");
	}
	out.print("<hr>");
	String pname = request.getParameter("pname");
	String lxz = request.getParameter("lxz");
	if(lxz != null && pname != null){
		Player player = PlayerManager.getInstance().getPlayer(pname);
		if(player != null){
			Pet pet = PetManager.getInstance().getPet(player.getActivePetId());
			if(pet == null){
				out.print("宠物不存在");
				return;
			}
			PetFlyState stat = PetManager.getInstance().getPetFlyState(pet.getId());
			if(stat != null){
				stat.setLingXingPoint(Integer.valueOf(lxz));
				PetManager.getInstance().savePetFlyState(stat,pet.getId(),player);
				pet.initTitle();
				out.print("设置成功,当前灵性:"+stat.getLingXingPoint());
				out.print("<br>当前宠物title:"+pet.getTitle());
			}
		}else{
			out.print("玩家不存在");
		}
	}
	
%>
<form>
	<table>
		<tr><th>玩家名：</th><td><input type='text' name='pname'></td></tr>
		<tr><th>修改玩家灵性值：</th><td><input type='text' name='lxz'></td></tr>
		<tr><td><input type="submit" name='确定'></td></tr>
	</table>
</form>

</body>
</html>
