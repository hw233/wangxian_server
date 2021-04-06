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
		ArrayList<ChongZhiServerConfig> czs = ChongZhiActivity.getInstance().chongZhiServerConfigs;
		boolean isHave1120 = false;
		for (int i = 0; i < czs.size(); i++) {
			ChongZhiServerConfig c = czs.get(i);
			if (c.getActivityID() == 1235) {
				isHave1120 = true;
				c.setServerName(GameConstants.getInstance().getServerName());
				c.setStartTime("2013-05-01 00:00:00");
				c.setEndTime("2013-05-06 23:59:50");
				c.creatLongTime();
				c.setPropName("3000");
				c.setMailTitle("樂享充返,活動返利");
				c.setMailMsg("您參與“樂享充返，暢玩飘渺寻仙曲”活動，獲得30%充值返利已經在附件中，請盡快提取！感謝您的參與！祝您遊戲愉快！");
			}
		}
		if (!isHave1120) {
			ChongZhiServerConfig c = new ChongZhiServerConfig();
			c.setActivityID(1235);
			c.setServerName(GameConstants.getInstance().getServerName());
			c.setType(ChongZhiServerConfig.CHONGZHI_FANLI_TIMELY);
			c.setStartTime("2013-05-01 00:00:00");
			c.setEndTime("2013-05-06 23:59:50");
			c.setMoney(1L);
			c.setPropName("3000");
			c.setPropNum(1);
			c.setColorType(-1);
			c.setBind(true);
			c.setMailTitle("樂享充返,活動返利");
			c.setMailMsg("您參與“樂享充返，暢玩飘渺寻仙曲”活動，獲得30%充值返利已經在附件中，請盡快提取！感謝您的參與！祝您遊戲愉快！");
			c.creatLongTime();
			ChongZhiActivity.getInstance().chongZhiServerConfigs.add(c);
		}
		response.sendRedirect("activity/ChongZhiActivity.jsp");
	%>
</body>
</html>
