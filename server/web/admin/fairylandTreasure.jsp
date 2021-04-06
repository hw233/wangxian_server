<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">

<%@page
	import="com.fy.engineserver.activity.fairylandTreasure.FairylandTreasureManager"%>

<%@page import="java.util.List"%>
<%@page
	import="com.fy.engineserver.activity.fairylandTreasure.FairylandTreasureActivity"%>
<%@page
	import="com.fy.engineserver.activity.expactivity.DayilyTimeDistance"%>
<%@page import="java.util.ArrayList"%>
<%@page import="com.xuanzhi.tools.cache.diskcache.DiskCache"%>
<%@page import="com.fy.engineserver.sprite.PlayerManager"%>
<%@page import="com.fy.engineserver.sprite.Player"%>
<%@page
	import="com.fy.engineserver.activity.fairylandTreasure.FairylandBox"%><html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title>仙界宝箱</title>
</head>
<body>
<%
	FairylandTreasureManager ftm = FairylandTreasureManager.getInstance();
	/*List<FairylandBox> fairylandBoxList =ftm.getFairylandBoxList();
	 out.print("宝箱信息:<br>");
	 for(FairylandBox fb:fairylandBoxList){
	 out.print(fb.getBoxNameStat()+"刷新个数"+fb.getNum()+"<br>");
	 }*/
	List<FairylandTreasureActivity> activityList = ftm.getActivityList();
	FairylandTreasureActivity fta = activityList.get(0);
	List<DayilyTimeDistance> times = new ArrayList<DayilyTimeDistance>();
	String startHourStr = request.getParameter("startHour");
	String startMinuteStr = request.getParameter("startMinute");
	String endHourStr = request.getParameter("endHour");
	String endMinuteStr = request.getParameter("endMinute");
	if (startHourStr != null && !"".equals(startHourStr) && startMinuteStr != null && !"".equals(startMinuteStr) && endHourStr != null && !"".equals(endHourStr) && endMinuteStr != null && !"".equals(endMinuteStr)) {
		int startHour = Integer.parseInt(startHourStr);
		int startMinute = Integer.parseInt(startMinuteStr);
		int endHour = Integer.parseInt(endHourStr);
		int endMinute = Integer.parseInt(endMinuteStr);
		DayilyTimeDistance dtd = new DayilyTimeDistance(startHour, startMinute, endHour, endMinute);
		times.add(dtd);
		fta.setTimes(times);
		out.print("时间段设置成功<br>");
	}

	String lastingStr = request.getParameter("lasting");
	if (lastingStr != null && !"".equals(lastingStr)) {
		int lasting = Integer.parseInt(lastingStr);
		fta.setLastingTime(lasting);
		out.print("npc持续时间设置成功<br>");
	}

	String spaceStr = request.getParameter("space");
	if (spaceStr != null && !"".equals(spaceStr)) {
		int space = Integer.parseInt(spaceStr);
		fta.setRefreshSpace(space);
		out.print("刷新间隔设置成功<br>");
	}

	String maxNumStr = request.getParameter("maxNum");
	if (maxNumStr != null && !"".equals(maxNumStr)) {
		int maxNum = Integer.parseInt(maxNumStr);
		ftm.setMaxOpenNum(maxNum);
		out.print("每人每天开启最大数量设置成功<br>");
	}

	String nameStr = request.getParameter("playerName");
	if (nameStr != null && !"".equals(nameStr)) {
		Player p = PlayerManager.getInstance().getPlayer(nameStr);
		if (p != null) {
			String key = ftm.getCurrentDateStr() + p.getId();
			if (ftm.disk.get(key) != null) {
				ftm.disk.put(key, 0);
				out.print(nameStr + " 的开宝箱数已经清零<br>");
			}
		}
	}

	DiskCache disk = ftm.disk;
	if (disk != null) {
	}
%>

<form action="">开始小时<input type="text" name="startHour" value="" /><br>
开始分钟<input type="text" name="startMinute" value="" /><br>
结束小时<input type="text" name="endHour" value="" /><br>
结束分钟<input type="text" name="endMinute" value="" /><br>
<br>
npc持续分钟<input type="text" name="lasting" value="" /><br><br>
<!-- 间隔分钟<input type="text" name="space" value="" /><br><br>  -->
每人每天开箱子上限<input type="text" name="maxNum" value="" /><br><br>
开宝箱数清零,角色名:<input type="text" name="playerName" value="" /><br><br>
<input type="submit" value="提交"></form>
</body>
</html>