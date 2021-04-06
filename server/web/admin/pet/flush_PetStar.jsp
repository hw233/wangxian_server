<%@page import="com.fy.engineserver.sprite.pet.Pet"%>
<%@page import="com.fy.engineserver.sprite.pet.PetManager"%>
<%@page import="com.fy.engineserver.datasource.article.manager.ArticleEntityManager"%>
<%@page import="com.fy.engineserver.datasource.language.Translate"%>
<%@page import="com.fy.engineserver.datasource.article.manager.ArticleManager"%>
<%@page import="com.fy.engineserver.mail.service.MailManager"%>
<%@page import="com.fy.engineserver.datasource.article.data.entity.ArticleEntity"%>
<%@page import="com.fy.engineserver.datasource.article.data.articles.Article"%>
<%@page import="com.fy.engineserver.activity.chongzhiActivity.ChongZhiSpecialActivity"%>
<%@page import="java.text.SimpleDateFormat"%>
<%@page import="com.sqage.stat.commonstat.entity.ChongZhi"%>
<%@page import="com.fy.engineserver.sprite.PlayerManager"%>
<%@page import="com.fy.engineserver.sprite.Player"%>
<%@page import="com.xuanzhi.boss.game.GameConstants"%>
<%@page import="com.fy.engineserver.activity.chongzhiActivity.ChongZhiServerConfig"%>
<%@page import="java.util.HashMap"%>
<%@page import="com.fy.engineserver.activity.chongzhiActivity.ChongZhiActivity"%>
<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<%@page import="com.fy.engineserver.newtask.service.TaskManager"%>
<%@page import="java.util.List"%>
<%@page import="com.fy.engineserver.newtask.Task"%><html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title>Insert title here</title>
</head>
<body>
	<%
		String action = request.getParameter("action");
		if (action != null) {
			if ("flushPet".equals(action)) {
				String value = request.getParameter("petIDs");
				String[] vv = value.split(",");
				long[] petIDs = new long[vv.length/2];
				int[] nums = new int[vv.length/2];
				for (int i = 0; i < petIDs.length; i++) {
					petIDs[i] = Long.parseLong(vv[i*2]);
					nums[i] = Integer.parseInt(vv[i*2+1]);
				}
				for (int i = 0; i < petIDs.length; i++) {
					Pet p = PetManager.getInstance().getPet(petIDs[i]);
					if (p != null) {
						byte sub = (byte)(p.getStarClass() - nums[i]);
						if (sub > 0) {
							p.setStarClass((byte)sub);
						}else {
							p.setStarClass((byte)0);
						}
						out.println("pet--"+p.getName()+" ~~ ["+p.getCategory()+"] star["+p.getStarClass()+"]--  <br>");
					}
				}
			}
		}
	%>
	
	<form name="f1">
		<input type="hidden" name="action" value="flushPet">
		宠物ID<input name="petIDs">
		<input type="submit" value="确定">
	</form>
</body>
</html>
