<%@page import="com.fy.engineserver.tournament.data.OneTournamentData"%>
<%@page import="java.util.Calendar"%>
<%@page import="com.fy.engineserver.battlefield.BattleField"%>
<%@page import="com.fy.engineserver.uniteserver.UnitServerFunctionManager.Function"%>
<%@page import="com.fy.engineserver.uniteserver.UnitServerFunctionManager"%>
<%@page import="java.util.Arrays"%>
<%@page import="com.fy.engineserver.tournament.manager.TournamentManager"%>
<%@page import="com.fy.engineserver.core.LivingObject"%>
<%@page import="com.fy.engineserver.core.Game"%>
<%@page import="com.fy.engineserver.sprite.npc.MemoryNPCManager"%>
<%@page import="com.fy.engineserver.core.GameManager"%>
<%@page import="com.fy.engineserver.sprite.npc.NPC"%>
<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<%
	//15*60+30
	Calendar calendar = Calendar.getInstance();
	long now = com.fy.engineserver.gametime.SystemTime.currentTimeMillis();
	calendar.setTimeInMillis(now);
	int hour = calendar.get(Calendar.HOUR_OF_DAY);
	int minute = calendar.get(Calendar.MINUTE);
	minute = 60 * hour + minute;
				out.print(minute+"----<br>");
	TournamentManager.每天比武开启时间 = new int[]{816,831}; 
	TournamentManager.每天比武提示时间 = new int[]{815,830}; 
	TournamentManager tm = TournamentManager.getInstance();
	tm.notify1 = false;
	tm.notify2 = false;
	TournamentManager.getInstance().td.currentSignUpList.clear();
	
	out.print("每天比武开启时间"+Arrays.toString(TournamentManager.每天比武开启时间)+"<br>");
	out.print("每天比武提示时间"+Arrays.toString(TournamentManager.每天比武提示时间)+"<br>");
	out.print("条件一："+com.fy.engineserver.util.ContextLoadFinishedListener.isLoadFinished()+"<br>");
	out.print("条件二："+UnitServerFunctionManager.needCloseFunctuin(Function.武圣)+"<br>");
	out.print("报名条件一："+(minute == TournamentManager.每天比武提示时间[0])+"<br>");
	out.print("报名条件二："+TournamentManager.getInstance().notify1+"<br>");
	out.print("报名条件一一："+(minute == TournamentManager.每天比武提示时间[1])+"<br>");
	out.print("报名条件二二："+TournamentManager.getInstance().notify2+"<br>");
	out.print("弹窗条件一："+TournamentManager.getInstance().td+"<br>");
	out.print("弹窗条件二："+TournamentManager.getInstance().td.currentSignUpList+"<br>");
	out.print("当前分钟:"+minute+"<br>");
// BattleField battleTemp = tm.比武场分配Map.get(1100000000007683075L);
// BattleField battleTemp = tm.比武场分配Map.get(1100000000007683075L);
// BattleField battleTemp2 = tm.比武场分配Map.get(1100000000007660545L);
// out.print("battleTemp:"+battleTemp+"<br>");
// out.print("battleTemp2:"+battleTemp2+"<br>");
// try{
// 	TournamentManager.getInstance().给在线玩家发送报名窗口();
// 	out.print("弹窗ok1"+TournamentManager.getInstance().td.currentSignUpList.size());
// 	TournamentManager.getInstance().td.currentSignUpList.add(1100000000007660545L);
// 	out.print("弹窗ok2"+TournamentManager.getInstance().td.currentSignUpList.size());
// }catch(Exception e){
// 	out.print("异常");
// }
	//结算
	/**
	//long[] 上周比武按名次排序后的id = {1107000000000057346L,1107000000000063489L};
	tm.上周比武按名次排序后的id = 上周比武按名次排序后的id;
	int currentWeek = TournamentManager.得到一年中的第几个星期_周日并到上星期(System.currentTimeMillis());
	OneTournamentData data = new OneTournamentData(1107000000000057346L,13);
	data.setPickReward(false);
	tm.onePlayerTournamentDataMap.put(1107000000000057346L, data);
	out.print("比武id："+Arrays.toString(tm.上周比武按名次排序后的id)+"-currentWeek:"+currentWeek);
	**/
%>
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title>修改武圣时间</title>
</head>
<body>

</body>
</html>