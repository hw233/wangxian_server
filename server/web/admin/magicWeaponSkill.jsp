<%@ page language="java" contentType="text/html; charset=utf-8"
    pageEncoding="utf-8"%>
<%@ page import="com.fy.engineserver.datasource.props.*,com.google.gson.*,java.util.*,com.fy.engineserver.sprite.*,
com.fy.engineserver.sprite.concrete.*" %>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">

<%@page import="com.xuanzhi.boss.game.GameConstants"%><%@include file="IPManager.jsp" %><html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8">
<title>物品</title>
<%
	//服务器类型为0代表为可修改的开发服务器
if(GameConstants.getInstance().getServerType() == 0){
	String articleIdStr = request.getParameter("articleId");
	String skillIdStr = request.getParameter("skillId");
	if(articleIdStr != null && skillIdStr != null){
		ArticleEntityManager aem = ArticleEntityManager.getInstance();
		ArticleEntity ae = aem.getEntity(Long.parseLong(articleIdStr));
		if(ae != null && ae instanceof NewMagicWeaponEntity){
	MagicWeaponManager mwm = MagicWeaponManager.getInstance();
	MagicWeapon mw = mwm.getMagicWeapon(((NewMagicWeaponEntity)ae).getMagicWeaponId());
	if(mw != null){
		String[] skillIds = skillIdStr.split(",");
		int[] skills = new int[skillIds.length];
		int[] skillLevels = new int[skillIds.length];
		for(int i = 0; i < skillIds.length; i++){
			skills[i] = Integer.parseInt(skillIds[i]);
			skillLevels[i] = 1;
		}
		mw.setSkillIds(skills);
		mw.setSkillLevels(skillLevels);
		out.println("法宝设置成功");
	}
		}
	}
}else{
	out.println("服务器不能改数据");
}
%>
</head>
<body>
<form name="f1">
物品id:<input name="articleId" id="articleId"><br>
技能id(多个id之间用,分隔):<input name="skillId" id="skillId"><br>
<input type="submit" value="提交">
</form>
</body>
</html>
