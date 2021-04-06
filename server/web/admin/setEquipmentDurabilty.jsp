<%@page import="com.fy.engineserver.datasource.article.data.entity.EquipmentEntity"%>
<%@page import="com.fy.engineserver.datasource.article.manager.ArticleEntityManager"%>
<%@page import="com.fy.engineserver.datasource.article.data.entity.ArticleEntity"%>
<%@page import="com.fy.engineserver.sprite.PlayerManager"%>
<%@page import="com.fy.engineserver.sprite.Player"%>
<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title>Insert title here</title>
</head>
<body>
<%
	String action = request.getParameter("action");
	if (action != null) {
		if (action.equals("setEqu")) {
			String pName = request.getParameter("pName");
			if (pName != null && !"".equals(pName)) {
				Player player = null;
				try{
					player = PlayerManager.getInstance().getPlayer(pName);
				}catch(Exception e) {
				}
				if (player != null) {
					long[] eids = player.getEquipmentColumns().getEquipmentIds();
					for (int i = 0; i < eids.length; i++) {
						long eid = eids[i];
						if (eid > 0) {
							ArticleEntity entity = ArticleEntityManager.getInstance().getEntity(eid);
							if (entity != null && entity instanceof EquipmentEntity) {
								EquipmentEntity equipment = (EquipmentEntity)entity;
								equipment.setDurability(1);
							}
						}
					}
				}
			}
		}
	}
%>
	把玩家的身上装备耐久设置成1<br>
	<form name="f1">
		<input type="hidden" name="action" value="setEqu">
		输入人物名字：<input type="text" name="pName">
		<input type="submit" value="设置">
	</form>
</body>
</html>