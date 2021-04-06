<%@page import="com.fy.engineserver.sprite.Soul"%><%@page import="com.fy.engineserver.datasource.article.data.entity.ArticleEntity"%><%@page import="com.fy.engineserver.sprite.PlayerManager"%><%@page import="com.fy.engineserver.sprite.Player"%><%@page import="com.fy.engineserver.datasource.article.data.magicweapon.model.MagicWeaponBaseModel"%><%@page import="com.fy.engineserver.datasource.article.data.magicweapon.MagicWeaponManager"%>
<%@ page language="java" contentType="text/html; charset=utf-8" pageEncoding="utf-8"%>
<%
	String playername = request.getParameter("playername");
	if(playername == null || playername.isEmpty()){
		out.print("请输入玩家name");
		return;
	}
	
	Player p = PlayerManager.getInstance().getPlayer(playername);
	if(p == null){
		out.print("角色名为："+playername+"的玩家不存在:");
		return;
	}		
	try{
		ArticleEntity ae = p.getSoul(Soul.SOUL_TYPE_BASE).getEc().get(11);
		if(ae != null){
			out.print(ae.getArticleName()+"@@@@"+ae.getId()+"@@##");
		}else{
// 			out.print("本尊没装载法宝<br>");
		}
	}catch(Exception e){
// 		out.print("查看本尊法宝异常<br>");
	}
	
	try{
	 	ArticleEntity aesoul = p.getSoul(Soul.SOUL_TYPE_SOUL).getEc().get(11);
	 	if(aesoul != null){
	 		out.print(aesoul.getArticleName()+"####"+aesoul.getId());
	 	}else{
// 	 		out.print("元神没装载法宝<br>");
	 	}
	}catch(Exception e){
// 		out.print("该角色没有元神<br>");
	}

%>
