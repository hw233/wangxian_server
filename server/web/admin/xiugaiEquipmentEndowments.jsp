<%@page import="com.fy.engineserver.datasource.article.data.entity.EquipmentEntity"%>
<%@page import="com.fy.engineserver.datasource.article.manager.ArticleEntityManager"%>
<%@page import="com.fy.engineserver.datasource.article.data.entity.ArticleEntity"%>
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
			if ("xiugai".equals(action)) {
				String equipID_str = request.getParameter("equipID");
				String value_str = request.getParameter("value");
				long equipID = Long.parseLong(equipID_str);
				int value = Integer.parseInt(value_str);
				ArticleEntity entity = ArticleEntityManager.getInstance().getEntity(equipID);
				if (entity instanceof EquipmentEntity) {
					((EquipmentEntity)entity).setEndowments(value);
				}
			}else if ("chakan".equals(action)) {
				String equipID_str = request.getParameter("equipID");
				long equipID = Long.parseLong(equipID_str);
				ArticleEntity entity = ArticleEntityManager.getInstance().getEntity(equipID);
				if (entity instanceof EquipmentEntity) {
					out.print(equipID + "~~~~~~~~~~" + ((EquipmentEntity)entity).getEndowments());
				}
			}
		}
	%>
	
	<form name='f1'>
		<input type="hidden" name="action" value="xiugai">
		<input name="equipID">
		<input name="value">
		<input type="submit" name="确定">
	</form>
	
	<form name='f2'>
		<input type="hidden" name="action" value="chakan">
		<input name="equipID">
		<input type="submit" name="确定">
	</form>
</body>
</html>
