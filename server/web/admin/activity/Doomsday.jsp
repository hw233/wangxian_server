<%@page import="com.fy.engineserver.activity.doomsday.DoomsdayBossGame"%>
<%@page import="com.fy.engineserver.activity.doomsday.DoomsdayThread"%>
<%@page import="com.fy.engineserver.activity.doomsday.DoomsdayFuLiGame"%>
<%@page import="java.util.ArrayList"%>
<%@page import="com.fy.engineserver.activity.doomsday.DoomsdayManager.DoomsdayDCKey"%>
<%@page import="com.xuanzhi.tools.text.DateUtil"%>
<%@page import="com.fy.engineserver.newBillboard.BillboardsManager"%>
<%@page import="com.fy.engineserver.menu.Option"%>
<%@page import="com.fy.engineserver.menu.WindowManager"%>
<%@page import="com.fy.engineserver.message.GameMessageFactory"%>
<%@page import="com.fy.engineserver.message.CONFIRM_WINDOW_REQ"%>
<%@page import="com.fy.engineserver.menu.MenuWindow"%>
<%@page import="com.fy.engineserver.activity.doomsday.DoomsdayManager.ContributeData"%>
<%@page import="com.fy.engineserver.util.TimeTool"%>
<%@page import="com.fy.engineserver.country.manager.CountryManager"%>
<%@page import="com.fy.engineserver.activity.doomsday.DoomsdayManager"%>
<%@page import="java.util.Date"%>
<%@page import="java.text.SimpleDateFormat"%>
<%@page import="java.util.Arrays"%>
<%@page import="com.fy.engineserver.activity.chongzhiActivity.XiaoFeiServerConfig"%>
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
	SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
	String startTime = format.format(new Date(DoomsdayManager.doomsdayStarttime));
	String boatStartTime = format.format(new Date(DoomsdayManager.doomsdayBoatBornTime));
	String endTime = format.format(new Date(DoomsdayManager.doomsdayEndtime));
	String now_time = format.format(new Date());
	
	String action = request.getParameter("action");
	if (action != null) {
		if ("setTime".equals(action)) {
			String startTime1_input = request.getParameter("startTime1");
			String startTime2_input = request.getParameter("startTime2");
			String endTime_input = request.getParameter("endTime");
			if (startTime1_input != null) {
				DoomsdayManager.doomsdayStarttime = TimeTool.formatter.varChar19.parse(startTime1_input);
			}
			if (startTime2_input != null) {
				DoomsdayManager.doomsdayBoatBornTime = TimeTool.formatter.varChar19.parse(startTime2_input);
			}
			if (endTime_input != null) {
				DoomsdayManager.doomsdayEndtime = TimeTool.formatter.varChar19.parse(endTime_input);
			}
			response.sendRedirect("./Doomsday.jsp");
		}else if ("refNotice".equals(action)) {
			for (int i=0 ; i < DoomsdayManager.getInstance().isNotice.length; i++) {
				DoomsdayManager.getInstance().isNotice[i] = false;
			}
			response.sendRedirect("./Doomsday.jsp");
		}else if ("addDevote".equals(action)) {
			String playerName_input = request.getParameter("playerName");
			String devoteName_input = request.getParameter("devoteName");
			String devoteNum_input = request.getParameter("devoteNum");
			if (playerName_input != null && devoteName_input != null && devoteNum_input != null
				&& !"".equals(playerName_input) && !"".equals(devoteName_input) && !"".equals(devoteNum_input)) {
				Player player = null;
				try{
					player = PlayerManager.getInstance().getPlayer(playerName_input);
				}catch(Exception e) {
				}
				boolean isPropTrue = false;
				for (int i = 0; i < DoomsdayManager.req_propNames.length; i++) {
					if (devoteName_input.equals(DoomsdayManager.req_propNames[i])){
						isPropTrue = true;
						break;
					}
				}
				int devoteNum = Integer.parseInt(devoteNum_input);
				if (player != null && isPropTrue) {
					DoomsdayManager.getInstance().addExchargeCardDaily(player, devoteName_input, devoteNum);
				}
			}
			response.sendRedirect("./Doomsday.jsp");
		}else if ("qing_billboard".equals(action)) {
			BillboardsManager.getInstance().lastFlushBillboardTime = 0;
			response.sendRedirect("./Doomsday.jsp");
		}else if ("xiugai_bossTime".equals(action)) {
			String boss_1_1 = request.getParameter("time1_1");
			String boss_1_2 = request.getParameter("time1_2");
			String boss_2_1 = request.getParameter("time2_1");
			
			DoomsdayManager.boss1StartTimes[0] = boss_1_1;
			DoomsdayManager.boss1StartTimes[1] = boss_1_2;
			DoomsdayManager.boss2StartTime = boss_2_1;
			
			DoomsdayManager.boss1StartTimesLong = new long[2];
			String day = DateUtil.formatDate(new Date(), "yyyy-MM-dd");
			String day1_1 = day + " " + DoomsdayManager.boss1StartTimes[0];
			String day1_2 = day + " " + DoomsdayManager.boss1StartTimes[1];
			DoomsdayManager.boss1StartTimesLong[0] = DateUtil.parseDate(day1_1, "yyyy-MM-dd HH:mm:ss").getTime();
			DoomsdayManager.boss1StartTimesLong[1] = DateUtil.parseDate(day1_2, "yyyy-MM-dd HH:mm:ss").getTime();
			
			String day2_1 = day + " " + DoomsdayManager.boss2StartTime;
			DoomsdayManager.boss2StartTimeLong = DateUtil.parseDate(day2_1, "yyyy-MM-dd HH:mm:ss").getTime();
			
			response.sendRedirect("./Doomsday.jsp");
		}else if ("qing_fuli".equals(action)) {
			String dayKey = DoomsdayDCKey.DAILY_PLAYER_ENTER_GUAJI_RECORD.getDaykey(DateUtil.formatDate(new Date(), "yyyy-MM-dd"));
			ArrayList<Long> dayPlayerData = DoomsdayManager.getInstance().getDayPlayerFuLi(dayKey);
			dayPlayerData.clear();
			DoomsdayManager.getInstance().diskCache.put(dayKey, dayPlayerData);
			response.sendRedirect("./Doomsday.jsp");
		}else if ("qing_boss".equals(action)) {
			HashMap<Long, List<String>> playerEnterRecord = DoomsdayManager.getInstance().getPlayerEnterBoss();
			playerEnterRecord.clear();
			String key = DoomsdayDCKey.DAILY_PLAYER_ENTER_BOSS_RECORD.name();
			DoomsdayManager.getInstance().diskCache.put(key, playerEnterRecord);
			response.sendRedirect("./Doomsday.jsp");
		}else if ("qing_all".equals(action)) {
			DoomsdayManager.getInstance().diskCache.clear();
			response.sendRedirect("./Doomsday.jsp");
		}else if ("fa_bill".equals(action)) {
			DoomsdayManager.getInstance().everyDayPrize(System.currentTimeMillis());
			response.sendRedirect("./Doomsday.jsp");
		}else if ("fa_activityOver".equals(action)) {
			DoomsdayManager.getInstance().activityOver();
			response.sendRedirect("./Doomsday.jsp");
		}else if ("new_day".equals(action)) {
			DoomsdayManager.getInstance().lastHeatTime = System.currentTimeMillis() - 1000L * 60 * 60 * 24;
			response.sendRedirect("./Doomsday.jsp");
		}else if ("clear_BOSS".equals(action)) {
			DoomsdayManager.getInstance().boss1Games[0] = null;
			DoomsdayManager.getInstance().boss1Games[1] = null;
			DoomsdayManager.getInstance().boss1Games[2] = null;
			
			DoomsdayManager.getInstance().boss2Games[0] = null;
			DoomsdayManager.getInstance().boss2Games[1] = null;
			DoomsdayManager.getInstance().boss2Games[2] = null;
			out.print("清空成功~~~~~~~~~~~~~!!!!!<BR/>");
			//response.sendRedirect("./Doomsday.jsp");
		}
	}
%>
	
	活动开启时间:<%=startTime %>活动是否开启:<%=DoomsdayManager.getInstance().isDoomsdatBoatStart() %>
	<br>
	船开启时间:<%=boatStartTime %>船是不是开了:<%=DoomsdayManager.getInstance().isDoomsdayBoatBornStart() %>
	<br>
	活动结束:<%=endTime %>
	<br>
	活动数据
	<br>
	广播情况<%=Arrays.toString(DoomsdayManager.getInstance().isNotice) %>
	<br>
	<table border="1">
		<tr>
			<td>国家</td>
			<td>楠木</td>
			<td>铸铁</td>
			<td>胶黏剂</td>
		</tr>
		<%
			HashMap<Integer, Long[]> coutrys = DoomsdayManager.getInstance().getCountryProgress();
			for (int i = 0; i < coutrys.size(); i++) {
				Long[] value = coutrys.get(i + 1);
		%>
		<tr>
			<td><%=CountryManager.得到国家名(i+1) %></td>
			<td><%=value[0] %></td>
			<td><%=value[1] %></td>
			<td><%=value[2] %></td>
		</tr>
		<%
			}
		%>
	</table>
	<br><br>
	<form name="f13">
		<input type="hidden" name="action" value="clear_BOSS">
		<input type="submit" value="清除BOSSGame">
	</form>
	<br><br>
	<form name="f1">
		<input type="hidden" name="action" value="setTime">
		活动开始时间<input name="startTime1" value="2012-12-09 11:00:00">
		船开始时间<input name="startTime2" value="2012-12-20 11:00:00">
		活动结束时间<input name="endTime" value="2012-12-20 11:00:00">
		<input type="submit" value="修改活动时间">
	</form>
	<br><br>
	<form name="f2">
		<input type="hidden" name="action" value="refNotice">
		<input type="submit" value="刷新公告">
	</form>
	<br><br>
	<form name="f3">
		<input type="hidden" name="action" value="addDevote">
		玩家名字<input name="playerName">
		贡献物品名字<input name="devoteName">
		数量<input name="devoteNum">
		<input type="submit" value="玩家交贡献">
	</form>
	<br>
	<form name="f5">
		<input type="hidden" name="action" value="qing_billboard">
		<input type="submit" value="清排行">
	</form>
	<br>
	现在服务器时间<%=now_time %>
	BOSS活动开启:<%=(DoomsdayManager.getInstance().boss1Games[0]!=null) %>:<%=(DoomsdayManager.getInstance().boss1Games[1]!=null) %>:<%=(DoomsdayManager.getInstance().boss1Games[2]!=null) %>
	<br>
	<%
		for (int i = 0; i < DoomsdayManager.getInstance().boss1Games.length; i++) {
			if (DoomsdayManager.getInstance().boss1Games[i] != null) {
				DoomsdayBossGame game = DoomsdayManager.getInstance().boss1Games[i];
				int playerNum = -1;
				if (game.getGame() != null) {
					playerNum = game.getGame().getNumOfPlayer();
				}
				out.print(i+"~r=" + game.isRellOver() + " s=" + game.getState() + " o=" + game.isOver() + "/" +game.isTimeOver() + " P=" + playerNum +  "<BR/>");
			}
		}
	%>
	BOSS2活动开启:<%=(DoomsdayManager.getInstance().boss2Games[0]!=null) %>:<%=(DoomsdayManager.getInstance().boss2Games[1]!=null) %>:<%=(DoomsdayManager.getInstance().boss2Games[2]!=null) %>
	<%
		for (int i = 0; i < DoomsdayManager.getInstance().boss2Games.length; i++) {
			if (DoomsdayManager.getInstance().boss2Games[i] != null) {
				DoomsdayBossGame game = DoomsdayManager.getInstance().boss2Games[i];
				int playerNum = -1;
				if (game.getGame() != null) {
					playerNum = game.getGame().getNumOfPlayer();
				}
				out.print(i+"~r=" + game.isRellOver() + "  s=" + game.getState() + "  o=" + game.isOver() + "/" +game.isTimeOver() + " P=" + playerNum +  "<BR/>");
			}
		}
	%>
	<form name="f6">
		<input type="hidden" name="action" value="xiugai_bossTime">
		boss1_1时间<input name="time1_1" value=<%=DoomsdayManager.boss1StartTimes[0] %>>
		boss1_2时间<input name="time1_2" value=<%=DoomsdayManager.boss1StartTimes[1] %>>
		boss2_1时间<input name="time2_1" value=<%=DoomsdayManager.boss2StartTime %>>
		<input type="submit" value="修改BOSS时间">
	</form>
	<br>
	<form name="f7">
		<input type="hidden" name="action" value="qing_fuli">
		<input type="submit" value="清福利房数据">
	</form>
	<br>
	<form name="f8">
		<input type="hidden" name="action" value="qing_boss">
		<input type="submit" value="清BOSS参加数据">
	</form>
	<br>
	<form name="f9">
		<input type="hidden" name="action" value="qing_all">
		<input type="submit" value="清所有数据">
	</form>
	<br>
	<form name="f10">
		<input type="hidden" name="action" value="fa_bill">
		<input type="submit" value="给今天的玩家发排行奖励">
	</form>
	<br>
	<form name="f11">
		<input type="hidden" name="action" value="fa_activityOver">
		<input type="submit" value="活动结束发奖励">
	</form>
	<br>
	<form name="f12">
		<input type="hidden" name="action" value="new_day">
		<input type="submit" value="新的一天">
	</form>
	
	
	当天个人捐献
	<table border="1">
		<tr>
			<td>玩家ID</td>
			<td>国家</td>
			<td>捐献</td>
		</tr>
		<%
			String todayKey = DoomsdayDCKey.DAILY_CONTRIBUTE.getDaykey(DateUtil.formatDate(new Date(), "yyyy-MM-dd"));
			HashMap<Long, ContributeData> datas = DoomsdayManager.getInstance().getDayPlayerData(todayKey);
			for (Long id : datas.keySet()) {
				ContributeData data = datas.get(id);
		%>
		<tr>
			<td><%=data.getPlayerId() %></td>
			<td><%=data.getCountry() %></td>
			<td><%=data.getContributeCardNum() %></td>
		</tr>
		<%
			}
		%>
	</table>
	<br>
	<table border="1">
		<tr>
			<td>玩家ID</td>
			<td>game</td>
			<td>monsterID</td>
			<td>startTime</td>
			<td>threadIndex</td>
			<td>state</td>
			<td>monSize</td>
		</tr>
		<%
			for (Long key : DoomsdayManager.getInstance().fuliGames.keySet()) {
				DoomsdayFuLiGame fuli = DoomsdayManager.getInstance().fuliGames.get(key);
		%>
		<tr>
			<td><%=fuli.getPlayerID() %></td>
			<td><%=fuli.getGame() %></td>
			<td><%=fuli.getMonsterID() %></td>
			<td><%=DateUtil.formatDate(new Date(fuli.getStartTime()), "yyyy-MM-dd HH:mm:ss") %></td>
			<td><%=fuli.getThreadIndex() %></td>
			<td><%=fuli.getState() %></td>
			<td><%=fuli.getMonsters().size() %></td>
		</tr>
		<%
			}
		%>
	</table>
	
	
	
	<table border="1">
		<tr>
			<%
				for (int i = 0; i < DoomsdayManager.getInstance().doomsdayThreads.length; i++) {
			%>
					<td>线程<%=i %></td>
			<%
				}
			%>
		</tr>
		<tr>
			<%
				for (int i = 0; i < DoomsdayManager.getInstance().doomsdayThreads.length; i++) {
					DoomsdayThread thread = DoomsdayManager.getInstance().doomsdayThreads[i];
			%>
					<td><%=thread.getFuliGames().size() %></td>
			<%
				}
			%>
		</tr>
	</table>
	
</body>
</html>
