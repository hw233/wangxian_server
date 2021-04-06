<%@page import="com.fy.engineserver.sprite.pet2.PetGrade"%>
<%@page import="com.fy.engineserver.sprite.pet2.GradePet"%>
<%@page import="com.fy.engineserver.sprite.pet.PetManager"%>
<%@page import="com.fy.engineserver.sprite.pet.Pet"%>
<%@page import="com.fy.engineserver.datasource.article.data.entity.PetPropsEntity"%>
<%@page import="com.fy.engineserver.datasource.article.concrete.DefaultArticleEntityManager"%>
<%@page import="com.fy.engineserver.datasource.article.data.entity.ArticleEntity"%>
<%@page import="com.fy.engineserver.datasource.article.data.props.Cell"%>
<%@page import="com.fy.engineserver.datasource.article.data.props.Knapsack"%>
<%@page import="com.fy.engineserver.sprite.Player"%>
<%@page import="com.fy.engineserver.sprite.concrete.GamePlayerManager"%>
<%@page import="com.fy.engineserver.datasource.skill2.GenericSkill"%>
<%@page import="com.fy.engineserver.datasource.skill2.GenericSkillManager"%>
<%@page import="java.util.*"%>
<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<%

	GradePet[] aa = PetGrade.petList;
	for (int i=0; i<aa.length; i++) {
		if (aa[i].name.contains("五行剑奴")) {
			aa[i].skDesc[1] = "增加800%血量，并附带100%反伤效果";
			//out.println(aa[i].name + " == " + aa[i].skDesc[1] + "<br>");
		}
	}

	GenericSkill gs =  GenericSkillManager.getInst().maps.get(110125);
	if (gs.buff.paramInt != 100) {
		gs.buff.paramInt = 100;
		gs.setDescription("增加800%血量，并附带100%反伤效果");
	}  
	out.println(gs.getName() + " = " + gs.buff.paramInt +  "  = " + gs.getDescription() + "<br>");

	Player[] players = GamePlayerManager.getInstance().getOnlinePlayers();
	for (Player p : players) {
		if (p != null) {
			Knapsack bag = p.getPetKnapsack();
			for (Cell c : bag.getCells()) {
				long aeId = c.getEntityId();
				if (aeId > 0) {
					ArticleEntity ae = DefaultArticleEntityManager.getInstance().getEntity(aeId);
					if (ae instanceof PetPropsEntity) {
						Pet pet = PetManager.getInstance().getPet(((PetPropsEntity)ae).getPetId());
						if (pet != null) {
							if (pet.talent1Skill == 110125 || pet.talent2Skill == 110125) {
								pet.init();
								out.println("[重新初始化无形剑奴] [" + p.getLogString() + "] [petId:" + pet.getId() + "] [" + pet.antiInjuryRate + "]<br>");
							}
						}
					}
				}
			}
		}
	} 
	
%>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title>Insert title here</title>
</head>
<body>

</body>
</html>