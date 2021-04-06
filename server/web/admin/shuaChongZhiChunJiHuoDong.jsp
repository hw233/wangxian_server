<%@page import="com.fy.engineserver.platform.PlatformManager.Platform"%>
<%@page import="com.fy.engineserver.platform.PlatformManager"%>
<%@page import="com.fy.engineserver.activity.chongzhiActivity.ChongZhiSpecialActivity"%>
<%@page import="com.xuanzhi.boss.game.GameConstants"%>
<%@page import="com.fy.engineserver.activity.chongzhiActivity.ChongZhiServerConfig"%>
<%@page import="java.util.ArrayList"%>
<%@page import="com.fy.engineserver.activity.chongzhiActivity.ChongZhiActivity"%>
<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title>Insert title here</title>
</head>
<body>
	<%
		if (PlatformManager.getInstance().platformOf(Platform.台湾)) {
			ArrayList<ChongZhiServerConfig> czs = ChongZhiActivity.getInstance().chongZhiServerConfigs;
			boolean isHave300010 = false;
			for (int i = 0; i < czs.size(); i++) {
				ChongZhiServerConfig c = czs.get(i);
				if (c.getActivityID() == 300010) {
					isHave300010 = true;
				}
			}
			if (!isHave300010) {
				ChongZhiSpecialActivity activity = new ChongZhiSpecialActivity(GameConstants.getInstance().getServerName(), 
						"2013-05-07 00:00:00", "2013-05-10 23:59:59",
						"春季盛宴之神寵現世", "恭喜您在神寵現世活動中獲得獎勵，請查收附件。感謝您對飘渺寻仙曲的支持！", 
						4900000, "四君子特惠錦囊", 2500000, "梅蘭竹菊錦囊");
				activity.setActivityID(300010);
				ChongZhiActivity.getInstance().chongZhiServerConfigs.add(activity);
				ChongZhiActivity.getInstance().specialActivity = activity;
			}
			response.sendRedirect("activity/ChongZhiActivity.jsp");
		}
	%>
</body>
</html>
