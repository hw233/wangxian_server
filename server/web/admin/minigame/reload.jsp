<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<%@ page language="java" contentType="text/html; charset=utf-8"
	pageEncoding="utf-8"%>
	
<%@page import="com.fy.engineserver.minigame.*"%>
<%@page import="com.xuanzhi.tools.simplejpa.SimpleEntityManagerFactory"%>
<%@page import="com.fy.engineserver.minigame.MiniGameManager"%>
<%@page import="com.fy.engineserver.minigame.instance.MemoryGame"%>
<%@page import="com.fy.engineserver.message.INITDATA_MINIGAME_ACTIVITY_REQ"%>
<%@page import="com.fy.engineserver.minigame.MiniGameSystem"%>
<%@page import="com.fy.engineserver.minigame.MiniGameEntity"%>
<%@page import="com.fy.engineserver.sprite.concrete.GamePlayerManager"%>
<%@page import="com.fy.engineserver.minigame.MiniGameEntityManager"%>
<%@page import="com.fy.engineserver.message.HANDLE_MINIGAME_ACTIVITY_REQ"%>
<%@page import="com.fy.engineserver.message.END_MINIGAME_ACTIVITY_REQ"%>
<%@page import="com.fy.engineserver.message.HANDLE_MINIGAME_ACTIVITY_REQ"%>
<%@page import="com.fy.engineserver.menu.marriage.Option_Temp2_Minigame"%>
<%@page import="java.util.List"%>
<%@page import="com.xuanzhi.tools.page.PageUtil"%><html>
<head>
</head>
<body>
<%
	String action = request.getParameter("action");
	String petId = request.getParameter("petId");
	String playerId = request.getParameter("playerId");
	//
	petId = petId == null ? "" : petId;
	playerId = playerId == null ? "" : playerId;
	
%>
<h2>重新加载小游戏静态文件</h2>
<form action="reload.jsp" method="get">
<input type="hidden" name="action" value="reload"/>
<input type="submit" value="重新加载静态文件"/>
</form>
<br/>
<h2>查看数据</h2>
<form action="reload.jsp" method="get">
<input type="hidden" name="action" value="serch"/>
<input type="submit" value="查看数据"/>
</form>
<br/>
<h2>发送初始化协议</h2>
<form action="reload.jsp" method="get">
<input type="hidden" name="action" value="sendinitMsg"/>
<input type="submit" value="发送初始化协议"/>
</form>
<br/>
<h2>发送操作协议</h2>
<form action="reload.jsp" method="get">
<input type="hidden" name="action" value="sendactMsg"/>
<input type="submit" value="发送操作协议"/>
</form>
<br/>
<h2>发送结束协议</h2>
<form action="reload.jsp" method="get">
<input type="hidden" name="action" value="sendendMsg"/>
<input type="submit" value="发送结束协议"/>
</form>
<br/>
<h2>领取对对碰游戏</h2>
<form action="reload.jsp" method="get">
<input type="hidden" name="action" value="sendduiduipMsg"/>
<input type="submit" value="领取对对碰游戏"/>
</form>
<br/>
<h2>领取管道游戏</h2>
<form action="reload.jsp" method="get">
<input type="hidden" name="action" value="sendguandaoMsg"/>
<input type="submit" value="领取管道游戏"/>
</form>
<br/>



<%
	MiniGameManager mgr2 = MiniGameManager.getInstance();
if("reload".equals(action)){
	MiniGameManager.getInstance().setMinigameinitdata("/home/game/resin_test/webapps/game_server/WEB-INF/game_init_config/minigame/minigameinitdata.xls");
	MiniGameManager.getInstance().setPipedatainit("/home/game/resin_test/webapps/game_server/WEB-INF/game_init_config/minigame/pipedatainit.xls");
	MiniGameManager.getInstance().init();
	out.println("初始化完成");
	out.println("数据：" + MiniGameManager.getInstance().getInitDataByNameAndDifficult("拼图",1));
}else if("serch".equals(action)){
	MemoryGame m = new MemoryGame(1);
	out.println("====" + m.getGameType());
} else if("sendinitMsg".equals(action)){
	INITDATA_MINIGAME_ACTIVITY_REQ req = new INITDATA_MINIGAME_ACTIVITY_REQ();
	MiniGameSystem sys = new MiniGameSystem();
	sys.handle_INITDATA_MINIGAME_ACTIVITY_REQ(null,req,GamePlayerManager.getInstance().getPlayer("a"));
	MiniGameEntity entity = MiniGameEntityManager.getInstance().getMiniGameEntity(GamePlayerManager.getInstance().getPlayer("a"));
	out.println("=========" + MiniGameEntityManager.getInstance().minigamePlayer.get(GamePlayerManager.getInstance().getPlayer("a").getId()));
} else if("sendendMsg".equals(action)){
	END_MINIGAME_ACTIVITY_REQ req = new END_MINIGAME_ACTIVITY_REQ();
	MiniGameSystem sys = new MiniGameSystem();
	sys.handle_END_MINIGAME_ACTIVITY_REQ(null,req,GamePlayerManager.getInstance().getPlayer("a"));
} else if("sendactMsg".equals(action)){
	HANDLE_MINIGAME_ACTIVITY_REQ req = new HANDLE_MINIGAME_ACTIVITY_REQ();
	MiniGameSystem sys = new MiniGameSystem();
	byte a = 0;
	String[] aa = new String[9];
	aa[0] = "3";
	req.setGameType(a);
	req.setInitDataArr(aa);
	sys.handle_HANDLE_MINIGAME_ACTIVITY_REQ(null,req,GamePlayerManager.getInstance().getPlayer("a"));
}else if("sendduiduipMsg".equals(action)){
	Option_Temp2_Minigame opt = new Option_Temp2_Minigame();
	opt.setTempType(2);
	opt.doSelect(null,GamePlayerManager.getInstance().getPlayer("a"));
}else if("sendguandaoMsg".equals(action)){
	Option_Temp2_Minigame opt = new Option_Temp2_Minigame();
	opt.setTempType(3);
	opt.doSelect(null,GamePlayerManager.getInstance().getPlayer("a"));
}

%>
</BODY></html>
