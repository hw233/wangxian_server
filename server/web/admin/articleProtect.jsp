<%@page import="com.fy.engineserver.articleProtect.ArticleProtectData"%>
<%@page import="com.fy.engineserver.articleProtect.PlayerArticleProtectData"%>
<%@page import="com.fy.engineserver.sprite.PlayerManager"%>
<%@page import="com.fy.engineserver.sprite.Player"%>
<%@page import="com.fy.engineserver.articleProtect.ArticleProtectManager"%>
<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title>解除锁魂</title>
</head>
<%
	String action = request.getParameter("action");
	if (action != null) {
		if (action.equals("cleanProtect")) {
			String pName = request.getParameter("pName");
			if (pName != null) {
				Player player = null;
				try{
					player = PlayerManager.getInstance().getPlayer(pName);
				}catch(Exception e) {
					
				}
				if (player != null) {
					PlayerArticleProtectData data = ArticleProtectManager.instance.getPlayerData(player);
					for (int i = 0; i <data.getDatas().size(); i++) {
						ArticleProtectData info = data.getDatas().get(i);
						ArticleProtectManager.instance.em.remove(info);
					}
					data.getDatas().clear();
				}
			}
		}
	}
%>
<body>
	<form name="f1">
		<input name="action" type="hidden" value="cleanProtect"/>
		输入清除玩家名字<input name="pName" type="text" />
		<input type="submit" value="确定"/>
	</form>
</body>
</html>