<%@page import="com.fy.engineserver.sprite.PlayerManager"%>
<%@page import="com.fy.engineserver.sprite.Player"%>
<%@page import="com.fy.engineserver.tengxun.TengXunDataManager"%>
<%@page import="java.util.Calendar"%>
<%@page import="java.util.Date"%>
<%@page import="java.text.SimpleDateFormat"%>
<%@page import="com.fy.engineserver.activity.tengXun.TengXunActivityManager"%>
<%@page import="com.fy.engineserver.activity.tengXun.TengXunActivity"%>
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
			if ("nextActivity".equals(action)) {
				TengXunActivityManager.instance.activitys.get(TengXunActivityManager.instance.activityIndex).onActivityEnd();
				TengXunActivityManager.instance.nextActivity();
			}else if ("setWeekDay".equals(action)) {
				Calendar calendar = Calendar.getInstance();
				int dayWeek = calendar.get(Calendar.DAY_OF_WEEK);
				TengXunActivityManager.instance.ACTIVITY_WEEKDAY = dayWeek;
			}else if ("clearWeekLiBao".equals(action)) {
				TengXunDataManager.instance.week_libao_sets.clear();
			}else if ("clearPlayerProp".equals(action)) {
				String pName = request.getParameter("pName");
				Player player = null;
				try{
					player = PlayerManager.getInstance().getPlayer(pName);
				}catch(Exception e) {
					
				}
				if (player != null) {
					player.getPropsUseRecordMap().clear();
				}
			}
			response.sendRedirect("./TengXunActivity.jsp");
		}
	%>
	活动开始星期 = <%
		String[] week = new String[]{"星期天","星期1","星期2","星期3","星期4","星期5","星期6"};
	%><%=week[TengXunActivityManager.instance.ACTIVITY_WEEKDAY - 1] %>
	<br>
	<table border="1">
		<tr>
			<td>活动名字</td>
			<td>活动开始时间</td>
			<td>活动结束时间</td>
			<td>isStart</td>
			<td>当前</td>
		<tr>
		<%
		SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH-mm-ss");
			for (int i = 0; i < TengXunActivityManager.instance.activitys.size(); i++) {
				TengXunActivity ac = TengXunActivityManager.instance.activitys.get(i);
				String startT = format.format(new Date(ac.getStartTime_Long()));
				String endT = format.format(new Date(ac.getEndTime_Long()));
				boolean isStart = false;
				if (i == TengXunActivityManager.instance.activityIndex) {
					isStart = true;
				}
				%>
				<tr>
					<td><%=ac.getName() %></td>
					<td><%=startT %></td>
					<td><%=endT %></td>
					<td><%=ac.isStart %></td>
					<td><%=isStart %></td>
				<tr>
				<%
			}
		%>
	</table>
	
	<form name="f1">
		设置活动开启为今天
		<input type="hidden" name="action" value="setWeekDay">
		<input type="submit" value="设置">
	</form>
	<br>
	
	<form name="f2">
		进行到下个活动
		<input type="hidden" name="action" value="nextActivity">
		<input type="submit" value="下个活动">
	</form>
	
	<form name="f3">
		清空魔钻每周礼包领取
		<input type="hidden" name="action" value="clearWeekLiBao">
		<input type="submit" value="清空">
	</form>
	
	清空玩家道具使用次数
	<br>
	<form name="f4">
		<input type="hidden" name="action" value="clearPlayerProp">
		玩家名字<input type="text" name="pName">
		<input type="submit" value="清空">
	</form>
	
</body>
</html>
