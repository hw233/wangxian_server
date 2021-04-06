<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<%@page import="com.fy.engineserver.sprite.horse2.manager.Horse2Manager"%>
<%@page import="com.fy.engineserver.playerAims.manager.PlayerAimManager"%>
<%@page import="com.fy.engineserver.event.cate.EventOfPlayer"%>
<%@page import="com.fy.engineserver.event.EventRouter"%>
<%@page import="com.fy.engineserver.event.Event"%>
<%@page import="com.fy.engineserver.event.EventProc"%>
<%@ page language="java" contentType="text/html; charset=utf-8"
	pageEncoding="utf-8"%>

<head>
</head>
<body>
	<%
	String action = request.getParameter("action");
	if (!"houtaishuapinbi".equals(action)) {
		out.println("找技术要密码去！");
		return;	
	}
	String str = Horse2Manager.instance.translate.get(999);
	if (str == null) {
		Horse2Manager.instance.translate.put(999, "houtaishuapinbi");
		PlayerPrize pp = new PlayerPrize();
		pp.doReg();
		out.println("刷页面成功!!!!!!!");
	} else {
		out.println("这个页面已经刷过了！！");
	}
	%>

</body>
</html>
<%!
String recorder = "";
String ip = "";
class PlayerPrize implements EventProc{
	@Override
	public void proc(Event evt) {
		try {
			switch (evt.id) {
			case Event.PLAYER_LOGIN:
				EventOfPlayer ep = (EventOfPlayer) evt;
				ep.player.hiddenAllPlayer = false;
				ep.player.hiddenChatMessage = false;
				ep.player.hiddenSameCountryPlayer = false;
				if (PlayerAimManager.logger.isDebugEnabled()) {
					PlayerAimManager.logger.debug("[登陆设置玩家屏蔽信息] [" + ep.player.getLogString() + "]");
				}
				break;
			}
		} catch(Exception e){}
	}
	
	@Override
	public void doReg() {
		// TODO Auto-generated method stub
		EventRouter.register(Event.PLAYER_LOGIN, this);
	}
}
%>
