<%@page import="com.fy.engineserver.sprite.Player"%>
<%@page import="com.fy.engineserver.sprite.PlayerManager"%>
<%@page import="java.util.Arrays"%>
<%@page import="com.fy.engineserver.sprite.PlayerSimpleInfoManager"%>
<%@page import="com.fy.engineserver.sprite.PlayerSimpleInfo"%>
<%@page import="java.util.Date"%>
<%@page import="com.xuanzhi.tools.text.DateUtil"%>
<%@page import="com.fy.engineserver.activity.doomsday.DoomsdayManager.ContributeData"%>
<%@page import="com.fy.engineserver.activity.doomsday.DoomsdayManager.DoomsdayDCKey"%>
<%@page import="com.fy.engineserver.activity.doomsday.DoomsdayManager"%>
<%@page import="java.util.ArrayList"%>
<%@page import="java.util.HashMap"%>
<%@page import="java.io.InputStreamReader"%>
<%@page import="java.io.BufferedReader"%>
<%@page import="java.io.FileInputStream"%>
<%@page import="java.io.File"%>
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
			if ("buDangTian".equals(action)) {
				int num = Integer.parseInt(request.getParameter("num"));
				String playerName = request.getParameter("playerName");
				Player player = PlayerManager.getInstance().getPlayer(playerName);
				if (player != null) {
					DoomsdayManager.getInstance().addExchargeCardDaily(player, "楠木", num);
				}
				response.sendRedirect("./DoomsdayBu.jsp");
			}else if ("buZong".equals(action)) {
				int num = Integer.parseInt(request.getParameter("num"));
				String playerName = request.getParameter("playerName");
				Player player = PlayerManager.getInstance().getPlayer(playerName);
				if (player != null) {
					HashMap<Long, ContributeData> map = DoomsdayManager.getInstance().getAllPlayerData();
					if (!map.containsKey(player.getId())) {
						map.put(player.getId(), new ContributeData(player.getId(), player.getCountry(), 0));
					}
					ContributeData contributeData = map.get(player.getId());
					contributeData.setContributeCardNum(contributeData.getContributeCardNum() + num);
					DoomsdayManager.getInstance().diskCache.put(DoomsdayDCKey.TOTAL_CONTRIBUTE.name(), map);
					DoomsdayManager.logger.warn(player.getLogString() + DoomsdayManager.getInstance().doomsdayLoggerHead + "[总贡献] [增加贡献值:" + num + "] [增加后:" + contributeData.getContributeCardNum() + "]");
				}
				response.sendRedirect("./DoomsdayBu.jsp");
			}else if ("buguojia".equals(action)) {
				int num = Integer.parseInt(request.getParameter("num"));
				HashMap<Integer, Long[]> countrys = DoomsdayManager.getInstance().getCountryProgress();
				for (Integer key : countrys.keySet()) {
					Long[] values = countrys.get(key);
					values[0] += num;
					values[1] += num;
					values[2] += num;
					countrys.put(key, values);
					DoomsdayManager.logger.warn("[后台补国家]" + DoomsdayManager.getInstance().doomsdayLoggerHead + "[国家贡献:" + num + "] [增加后:" + Arrays.toString(values) + "]");
				}
				//保存国家兑换数据
				DoomsdayManager.getInstance().diskCache.put(DoomsdayDCKey.COUNTRY_BOAT_PROGRESS.name(), countrys);
			}
		}
	%>
	
	<form name="f3">
		<input type="hidden" name="action" value="buDangTian">
		玩家名字<input name="playerName">
		当天<input name="num">
		<input type="submit" value="补当天">
	</form>
	<form name="f1">
		<input type="hidden" name="action" value="buZong">
		玩家名字<input name="playerName">
		总<input name="num">
		<input type="submit" value="补总量">
	</form>
	<form name="f2">
		<input type="hidden" name="action" value="buguojia">
		总<input name="num">
		<input type="submit" value="补国家">
	</form>
</body>
</html>
