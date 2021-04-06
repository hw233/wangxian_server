<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">

<%
	ArticleEntity ae = ArticleEntityManager.getInstance().getEntity(1103000000066193878L);
	out.print(ae.getClass());
	out.print(getCommonInfoShow((EquipmentEntity)ae));
%>
<%!
public String getCommonInfoShow(ArticleEntity ae) {
	ArticleManager am = ArticleManager.getInstance();
	String articleName  = ae.getArticleName();
	System.out.println("enter------------------000000");
	if(am == null){
		return Translate.装备+articleName;
	}
	Equipment e = (Equipment)am.getArticle(articleName);
	if(e == null){
		return Translate.物品类型错误;
	}
	StringBuffer sb = new StringBuffer();
	
	System.out.println("enter------------------1");
	
	EquipmentEntity en = (EquipmentEntity)ae;
	sb.append("<f color='0xFFFFFF'>").append(Translate.装备等级).append(":").append(e.getPlayerLevelLimit()).append("</f>\n");
	for(int i = en.getStar()/2; --i>=0;){
		if (en.getStar() <= 10) {
			//星星
			sb.append("<i imagePath='/ui/texture_map1n2.png' rectX='643' rectY='1' rectW='36' rectH='36' width='20' height='20'>").append("</i>");
		}else if (en.getStar() <= 16) {
			//月亮
			sb.append("<i imagePath='/ui/texture_map1n2.png' rectX='715' rectY='1' rectW='36' rectH='36' width='20' height='20'>").append("</i>");
		}else {
			//太阳
			sb.append("<i imagePath='/ui/texture_map1n2.png' rectX='787' rectY='1' rectW='36' rectH='36' width='20' height='20'>").append("</i>");
		}
//		sb.append("★");
	}
	System.out.println("enter------------------2");
	if(en.getStar()%2==1){
		if (en.getStar() <= 10) {
			//星星
			sb.append("<i imagePath='/ui/texture_map1n2.png' rectX='607' rectY='1' rectW='36' rectH='36' width='20' height='20'>").append("</i>\n");
		}else if (en.getStar() <= 16) {
			//月亮
			sb.append("<i imagePath='/ui/texture_map1n2.png' rectX='679' rectY='1' rectW='36' rectH='36' width='20' height='20'>").append("</i>\n");
		}else {
			//太阳
			sb.append("<i imagePath='/ui/texture_map1n2.png' rectX='751' rectY='1' rectW='36' rectH='36' width='20' height='20'>").append("</i>\n");
		}
//		sb.append("☆");
	}else if(en.getStar() >0){
		sb.append(" \n \n");
	}
	if(en.getCreaterName() != null && !en.getCreaterName().equals("")){
		sb.append("<f color='0xFFFF00'>").append(en.getCreaterName()).append(Translate.的专属).append("</f>");
	}else{
	}
	if(ae.isBinded()){
		sb.append("\n<f color='0xFFFF00'>").append(Translate.已绑定).append("</f>");
	}else{
		sb.append("\n<f color='0xFFFF00'>").append(Translate.未绑定).append("</f>");
	}

	System.out.println("enter------------------3");
	if(e.getEquipmentType() >= 10){
		
		if(e.getArticleType()==Article.ARTICLE_TYPE_WEAPON){
			Weapon weapon = (Weapon)e;
			sb.append("\n<f color='0x00FF00'>").append(Weapon.WEAPONTYPE_NAME[weapon.getWeaponType()]).append("(").append(CareerManager.careerNameByType(0)).append(")").append("</f>");
		}else{
			sb.append("\n<f color='0x00FF00'>").append(EquipmentColumn.ALL_EQUIPMENT_TYPE_NAMES[e.getEquipmentType()]).append("(").append(CareerManager.careerNameByType(0)).append(")").append("</f>");
		}
		//坐骑装备
		int rank = e.getClassLimit();
		//sb.append("\n<f color='0x00FF00'>").append(Translate.等阶限定).append(":").append(坐骑装备阶[rank]).append("</f>");
	}else{
		
		if(e.getArticleType()==Article.ARTICLE_TYPE_WEAPON){
			Weapon weapon = (Weapon)e;
			sb.append("\n<f color='0x00FF00'>").append(Weapon.WEAPONTYPE_NAME[weapon.getWeaponType()]).append("(").append(CareerManager.careerNameByType(e.getCareerLimit())).append(")").append("</f>");
		}else{
			sb.append("\n<f color='0x00FF00'>").append(EquipmentColumn.ALL_EQUIPMENT_TYPE_NAMES[e.getEquipmentType()]).append("(").append(CareerManager.careerNameByType(e.getCareerLimit())).append(")").append("</f>");
		}
		
		sb.append("\n<f color='0x00FF00'>").append(Translate.境界限定).append(":").append(PlayerManager.classlevel[e.getClassLimit()]).append("</f>");
	}
	sb.append("\n<f color='0x00FF00'>").append(Translate.耐久度).append(":").append("").append("/").append(e.getDurability()).append("</f>");
	sb.append("\n");
	System.out.println("enter------------------4");
	for(int i = 0; i<en.getBasicPropertyValue().length; i++){
		if(en.getBasicPropertyValue()[i]==0){
			continue;
		}
		int value = en.getBasicPropertyValue()[i];
		switch(i){
		case 0:
			sb.append("\n<f color='0xFFFFFF'>").append(Translate.气血值).append(":").append(value).append("</f>");
			break;
		case 1:
			sb.append("\n<f color='0xFFFFFF'>").append(Translate.外功修为).append(":").append(value).append("</f>");
			break;
		case 2:
			sb.append("\n<f color='0xFFFFFF'>").append(Translate.内法修为).append(":").append(value).append("</f>");
			break;
		case 3:
			sb.append("\n<f color='0xFFFFFF'>").append(Translate.外功防御).append(":").append(value).append("</f>");
			break;
		case 4:
			sb.append("\n<f color='0xFFFFFF'>").append(Translate.内法防御).append(":").append(value).append("</f>");
			break;
		}
	}
	System.out.println("enter------------------5");
	//附加属性
	if(en.isTempEntityFlag()){
		sb.append("\n").append(Translate.附加属性随机);
		sb.append("\n").append(Translate.宝石孔随机);
	}else{

		//宝石
	}

	System.out.println("enter------------------6");
	if (en.getInlayQiLingArticleTypes().length > 0 && !en.isTempEntityFlag()){
		int idNums = 0;
		for (int i = 0; i < en.getInlayQiLingArticleIds().length; i++) {
			if (en.getInlayQiLingArticleIds()[i] > 0) {
				idNums++;
			}
		}
		sb.append("\n<f color='0xFFFF00'>").append(Translate.器灵槽).append(":").append(idNums).append("/").append(en.getInlayQiLingArticleTypes().length).append("</f>\n");
		for (int i = 0; i < en.getInlayQiLingArticleTypes().length; i++) {
			String icon = "";
			if (en.getInlayQiLingArticleIds()[i] > 0) {
				icon = "/ui/qiling/add"+(en.getInlayQiLingArticleTypes()[i])+".png";
			}else {
				icon = "/ui/qiling/aslot"+(en.getInlayQiLingArticleTypes()[i])+".png";
			}
			sb.append("<i imagePath='").append(icon).append("'").append("></i>");
		}
		System.out.println("enter------------------7---------" + en.getInlayQiLingArticleTypes());
		System.out.println("enter------------------7" + Arrays.toString(en.getInlayQiLingArticleTypes()));
		sb.append(" \n \n");
		for (int i = 0; i < en.getInlayQiLingArticleTypes().length; i++) {
			sb.append("\n<f color='").append(en.qilingInfoColor[en.getInlayQiLingArticleTypes()[i]]).append("'>").append(en.qilingInfoNames[en.getInlayQiLingArticleTypes()[i]]).append(":");

			System.out.println("enter------------------7ddd-" + en.qilingInfoColor[en.getInlayQiLingArticleTypes()[i]] + "******" + en.qilingInfoNames[en.getInlayQiLingArticleTypes()[i]]);
			if (en.getInlayQiLingArticleIds()[i] > 0) {
				QiLingArticleEntity entity = (QiLingArticleEntity)ArticleEntityManager.getInstance().getEntity(en.getInlayQiLingArticleIds()[i]);

				System.out.println("enter------------------=============" + entity + "---" + en.getInlayQiLingArticleIds()[i]);
				sb.append("+").append(entity.getPropertyValue());
			}else {
				sb.append(Translate.空);
			}
			sb.append("</f>");
		}
	}

	System.out.println("enter------------------8");
	return sb.toString();
}
%>
<%@page import="com.fy.engineserver.datasource.article.data.entity.ArticleEntity"%>
<%@page import="com.fy.engineserver.datasource.article.manager.ArticleEntityManager"%>
<%@page import="com.fy.engineserver.sprite.Player"%>
<%@page import="com.fy.engineserver.datasource.article.manager.ArticleManager"%>
<%@page import="com.fy.engineserver.datasource.article.data.articles.Article"%>
<%@page import="com.fy.engineserver.datasource.language.Translate"%>
<%@page import="com.fy.engineserver.sprite.PlayerManager"%>
<%@page import="com.fy.engineserver.datasource.article.data.articles.InlayArticle"%>
<%@page import="com.fy.engineserver.datasource.article.manager.AritcleInfoHelper"%>
<%@page import="com.fy.engineserver.datasource.article.data.entity.EquipmentEntity"%>
<%@page import="com.fy.engineserver.datasource.article.data.equipments.Equipment"%>
<%@page import="com.fy.engineserver.datasource.article.data.equipments.Weapon"%>
<%@page import="com.fy.engineserver.datasource.article.data.equipments.EquipmentColumn"%>
<%@page import="com.fy.engineserver.datasource.career.CareerManager"%>
<%@page import="antlr.Utils"%>
<%@page import="com.fy.engineserver.datasource.article.data.entity.QiLingArticleEntity"%>
<%@page import="java.util.Arrays"%><html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title>Insert title here</title>
</head>
<body>

</body>
</html>