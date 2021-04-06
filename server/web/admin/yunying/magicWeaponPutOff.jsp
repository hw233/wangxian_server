<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">

<%@page import="com.fy.engineserver.sprite.PlayerManager"%>
<%@page import="com.fy.engineserver.sprite.Player"%>
<%@page import="com.fy.engineserver.datasource.article.data.equipments.EquipmentColumn"%>
<%@page import="com.fy.engineserver.datasource.article.data.entity.EquipmentEntity"%>
<%@page import="com.fy.engineserver.datasource.article.data.entity.ArticleEntity"%>
<%@page import="com.fy.engineserver.datasource.article.data.entity.MagicWeaponEntity"%>
<%@page import="com.fy.engineserver.datasource.article.data.entity.NewMagicWeaponEntity"%>
<%@page import="com.fy.engineserver.datasource.article.data.magicweapon.MagicWeaponManager"%>
<%@page import="com.fy.engineserver.sprite.Soul"%>
<%@page import="com.fy.engineserver.sprite.concrete.GamePlayerManager"%><html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title>移除玩家法宝</title>
</head>
<body>
<%
	String name=request.getParameter("name");
	String type=request.getParameter("soulType");
	if(name!=null&&!"".equals(name)&&type!=null&&!"".equals(type)){
		int soulType=Integer.valueOf(type);
		Player p=PlayerManager.getInstance().getPlayer(name);
		if(p!=null){
			Soul soul = p.getSoul(soulType);
			if (soul == null) {
				if (GamePlayerManager.logger.isWarnEnabled()) {
					GamePlayerManager.logger.warn(p.getLogString() + "[后台要脱装备] [元神类型不存在] [soulType:" + soulType + "] [equipmentType:" + 11 + "]");
				}
				return;
			}
			EquipmentColumn ec=p.getEquipmentColumns();
			if(ec!=null){
				ArticleEntity ees[]=ec.getEquipmentArrayByCopy();
				if(ees[11]!=null && ees[11] instanceof NewMagicWeaponEntity){
					NewMagicWeaponEntity ee=(NewMagicWeaponEntity)ees[11];
					try {
						ee = soul.getEc().takeOffMw(11, soulType);
						out.print("[后台脱法宝][" + p.getLogString() + "][法宝id:["+ee.getId()+"]");
						MagicWeaponManager.logger.error("[后台脱法宝][" + p.getLogString() + "][法宝id:["+ee.getId()+"]");
					} catch (Exception e) {
						MagicWeaponManager.logger.error("[后台脱法宝错误][" + p.getLogString() + "]");
					}
				}
			}else{
				out.print("没有获取到玩家装备栏");
			}
		}else{
			out.print("玩家不存在");
		}
		
	}else{
		out.print("请输入角色名和元神类型");
	}
%>
<form action="">
请输入玩家角色名:<input name="name" type="text" />
移除法宝的元神类型:0-本尊,1-元神<input name="soulType" type="text" />
<input type="submit" value="提交">
</form>
</body>
</html>