<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">


<%@page import="com.fy.engineserver.billboard.special.SpecialEquipmentManager"%>
<%@page import="com.fy.engineserver.datasource.article.data.equipments.Equipment"%>
<%@page import="com.fy.engineserver.datasource.article.manager.ArticleEntityManager"%>
<%@page import="com.fy.engineserver.datasource.article.manager.ArticleManager"%>
<%@page import="com.fy.engineserver.datasource.article.data.entity.*"%>



<%@page import="com.fy.engineserver.datasource.article.data.articles.Article"%>
<%@page import="com.fy.engineserver.sprite.PlayerManager"%>
<%@page import="com.fy.engineserver.sprite.Player"%><html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title>创建</title>
</head>
<body>
	
	<%
	
		String name = request.getParameter("articleName").trim();
		String playerName = request.getParameter("playerName").trim();
	
		Equipment[] s1 = ArticleManager.getInstance().allSpecialEquipments1;
		Equipment[] s2 = ArticleManager.getInstance().allSpecialEquipments2;
		boolean have = false;
		Equipment ee = null;
		for(Equipment e : s1){
			if(e.getName().equals(name)){
				have = true;
				ee = e;
				break;
			}
		}
		
		for(Equipment e :s2){
			if(e.getName().equals(name)){
				have = true;
				ee = e;
				break;
			}
		}
		
		if(have){
			SpecialEquipmentManager sem = SpecialEquipmentManager.getInstance();
			ArticleEntity ae = sem.createEntity(ee,35);
			if(ae == null){
				out.print("已经有了");
			}else{
				PlayerManager pm = PlayerManager.getInstance();
				Player p =  pm.getPlayer(playerName);
				boolean bln = p.putToKnapsacks(ae,"后台测试");
				if(bln){
					out.print("放入背包");
				}
			}
		}else{
			out.print("没有这种类型");
		}
		
	
	%>
	
	
</body>
</html>