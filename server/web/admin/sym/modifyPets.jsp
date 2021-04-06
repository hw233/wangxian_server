<%@page import="com.fy.engineserver.datasource.skill2.GenericSkillManager"%>
<%@page import="com.fy.engineserver.datasource.article.data.props.PetEggProps"%>
<%@page import="com.fy.engineserver.sprite.pet.Pet"%>
<%@page import="com.fy.engineserver.datasource.article.data.props.PetProps"%>
<%@page import="com.fy.engineserver.sprite.pet2.PetGrade"%>
<%@page import="com.fy.engineserver.sprite.pet2.GradePet"%>
<%@page import="com.fy.engineserver.datasource.article.data.props.ArticleProperty"%>
<%@page import="com.fy.engineserver.datasource.article.data.props.PackageProps"%>
<%@page import="com.fy.engineserver.activity.loginActivity.ActivityManagers"%>
<%@page import="com.fy.engineserver.activity.ActivityShowInfo"%>
<%@page import="java.util.Hashtable"%>
<%@page import="java.util.Map"%>
<%@page import="java.lang.reflect.Field"%>
<%@page import="com.fy.engineserver.datasource.article.manager.ArticleManager"%>
<%@page import="com.fy.engineserver.datasource.article.data.articles.Article"%>
<%@page import="com.fy.engineserver.activity.shop.ActivityProp"%>
<%@page import="com.fy.engineserver.activity.taskDeliver.TaskDeliverAct"%>
<%@page import="com.fy.engineserver.activity.BaseActivityInstance"%>
<%@page import="com.fy.engineserver.activity.AllActivityManager"%>
<%@page import="com.fy.engineserver.activity.activeness.ActivenessManager"%>
<%@page import="java.util.Arrays"%>
<%@page import="com.fy.engineserver.menu.Option_ExchangeSliver_Salary"%>
<%@page import="com.fy.engineserver.menu.Option"%>
<%@page import="com.fy.engineserver.menu.MenuWindow"%>
<%@page import="com.fy.engineserver.menu.WindowManager"%>
<%@page import="java.util.ArrayList"%>
<%@page import="com.fy.engineserver.util.TimeTool"%>
<%@page import="java.util.List"%>
<%@page import="com.fy.engineserver.shop.Goods"%>
<%@page import="com.fy.engineserver.shop.Shop"%>
<%@page import="com.fy.engineserver.shop.ShopManager"%>
<%@ page language="java" contentType="text/html; charset=UTF-8"
        pageEncoding="UTF-8"%>
<%
PetProps pp = (PetProps)ArticleManager.getInstance().getArticle("幽冥天帝");
PetEggProps egg = (PetEggProps)ArticleManager.getInstance().getArticle("幽冥天帝蛋");
GenericSkillManager skInst = GenericSkillManager.getInst();
for (GradePet gp : PetGrade.petList) {
	if ("幽冥天帝".equals(gp.progName)) {
		gp.setName(pp.getName());
		gp.bornSkill[0] = pp.talent1skill;
		gp.bornSkill[1] = pp.talent2skill;
		gp.icon = pp.getIconId();
		gp.icons[0] = skInst.getSkillIcon(pp.talent1skill);
		gp.icons[1] = skInst.getSkillIcon(pp.talent2skill);
		gp.skDesc[0] = skInst.getSkillDesc(pp.talent1skill);
		gp.skDesc[1] = skInst.getSkillDesc(pp.talent2skill);
		gp.baseAvatar = pp.getAvataRace().concat("_").concat(pp.getAvataSex());
		//
		byte career = 0;
		//for (PetEggProps egg : ArticleManager.getInstance().allPetEggProps) {
			//if (egg.getName().equals(pp.getEggAticleName())) {
				gp.setCharacter(egg.getCharacter());
				gp.setTakeLevel(egg.getArticleLevel());
				career = (byte) (egg.getCareer() - 1);
				gp.setRarity(egg.getRarity());
				//break;
			//}
		//}
		//
		Pet fakeOne = new Pet();
		fakeOne.setCareer(career);
		fakeOne.setCharacter((byte) gp.getCharacter());
		fakeOne.setRealTrainLevel(gp.getTakeLevel() == 220 ? 225 : gp.getTakeLevel());
		fakeOne.setRarity((byte) gp.getRarity());
		fakeOne.calcShowAttMinMax();
		//
		gp.maxValues = new int[] { fakeOne.getShowMaxStrengthQuality(), fakeOne.getShowMaxDexterityQuality(), fakeOne.getShowMaxSpellpowerQuality(), fakeOne.getShowMaxConstitutionQuality(), fakeOne.getShowMaxDingliQuality() };
		gp.minValues = new int[] { fakeOne.getShowMinStrengthQuality(), fakeOne.getShowMinDexterityQuality(), fakeOne.getShowMinSpellpowerQuality(), fakeOne.getShowMinConstitutionQuality(), fakeOne.getShowMinDingliQuality() };
		gp.scaleArr[0] = pp.getObjectScale();
		out.println("ok!");
		break;
	}
}

%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title>修正白格</title>
</head>
<body>

</body>
</html>