<%@page import="com.fy.engineserver.sprite.horse2.manager.Horse2Manager"%>
<%@page import="com.fy.engineserver.carbon.devilSquare.DevilSquareManager"%>
<%@page import="com.fy.engineserver.activity.TransitRobbery.TransitRobberyManager"%>
<%@page import="com.fy.engineserver.activity.dailyTurnActivity.DailyTurnManager"%>
<%@page import="com.fy.engineserver.sprite.Player"%>
<%@page import="com.fy.engineserver.sprite.concrete.GamePlayerManager"%>
<%@page import="com.fy.boss.client.BossClientService"%>
<%@page import="java.util.Calendar"%>
<%@page import="com.fy.engineserver.event.cate.EventPlayerLogin"%>
<%@page import="com.fy.engineserver.event.EventRouter"%>
<%@page import="com.fy.engineserver.event.Event"%>
<%@page import="com.fy.engineserver.event.EventProc"%>
<%@ page language="java" contentType="text/html; charset=UTF-8"
        pageEncoding="UTF-8"%>
<%
if (Horse2Manager.instance.translate.get(53249) != null) {
	out.println("已刷过此页面！！");
	return ;
}
Horse2Manager.instance.translate.put(53249, "不用再刷了");

ModifyActivityNew a = new ModifyActivityNew();
a.doReg();
try {
Player[] players = GamePlayerManager.getInstance().getOnlinePlayers();
Calendar cd = Calendar.getInstance();
cd.set(Calendar.HOUR_OF_DAY, 0);
cd.set(Calendar.MINUTE, 0);
cd.set(Calendar.SECOND, 0);
cd.set(Calendar.MILLISECOND, 0);
long startTime = cd.getTimeInMillis();
long endTime = System.currentTimeMillis();
for (int i=0; i<players.length; i++) {
	if (players[i].getOne_day_rmb() != 0) {
		long rmbs = BossClientService.getInstance().getSavingAmount4Player(players[i].getId(), startTime, endTime);
		players[i].setOne_day_rmb(rmbs);
	}
}
}catch(Exception e){}
out.println("ok!");
%>
<%!
class ModifyActivityNew implements EventProc{
	public void proc(Event evt) {
		try {
			switch (evt.id) {
			case Event.PLAYER_LOGIN:
				EventPlayerLogin epe = (EventPlayerLogin) evt;
				if (epe.player.getVipLevel() > 0) {
					Calendar cd = Calendar.getInstance();
					cd.set(Calendar.HOUR_OF_DAY, 0);
					cd.set(Calendar.MINUTE, 0);
					cd.set(Calendar.SECOND, 0);
					cd.set(Calendar.MILLISECOND, 0);
					long startTime = cd.getTimeInMillis();
					long endTime = System.currentTimeMillis();
					long rmbs = BossClientService.getInstance().getSavingAmount4Player(epe.player.getId(), startTime, endTime);
					epe.player.setOne_day_rmb(rmbs);
					if (TransitRobberyManager.logger.isInfoEnabled()) {
						TransitRobberyManager.logger.info("[重新获取玩家当天充值数] [成功] [" + epe.player.getLogString() + "] [数:" + epe.player.getOne_day_rmb() + "]");
					}
				}
				break;
			case Event.SERVER_DAY_CHANGE:
				Player[] players = GamePlayerManager.getInstance().getOnlinePlayers();
				for (int i=0; i<players.length; i++) {
					players[i].setOne_day_rmb(1);
				}
				break;
			}
		} catch (Exception e) {
			DailyTurnManager.logger.warn("[修正每日充值数] [异常]", e);
		}
	}
	
	public void doReg() {
		EventRouter.register(Event.PLAYER_LOGIN, this);
		EventRouter.register(Event.SERVER_DAY_CHANGE, this);
	}
	
}
%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title>修正每日转盘活动</title>
</head>
<body>

</body>
</html>