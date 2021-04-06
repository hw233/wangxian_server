<%@page import="java.util.Arrays"%>
<%@page import="java.util.Date"%>
<%@page import="java.text.SimpleDateFormat"%>
<%@page import="com.fy.engineserver.sprite.PlayerManager"%>
<%@page import="java.util.Hashtable"%>
<%@page import="com.fy.engineserver.marriage.MarriageInfo"%>
<%@page import="com.fy.engineserver.marriage.manager.MarriageManager"%>
<%@page import="java.util.Iterator"%>
<%@page import="java.util.List"%>
<%@page import="java.util.HashMap"%>
<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%
	Hashtable<Long, MarriageInfo> underRuleMap = MarriageManager.getInstance().getInfoMap();
%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title>结婚送礼包活动</title>
</head>
<body>
	<%
		String action = request.getParameter("action");
		if (action != null) {
			if (action.equals("Mactivity")) {
				String marriageActivityStartTime = request.getParameter("startTime");
				String marriageActivityEndTime = request.getParameter("endTime");
				SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
				MarriageManager.getInstance().marriageActivityStartTimeL = format.parse(marriageActivityStartTime).getTime();
				MarriageManager.getInstance().marriageActivityEndTimeL = format.parse(marriageActivityEndTime).getTime();
				MarriageManager.getInstance().marriageActivityStartTime = marriageActivityStartTime;
				MarriageManager.getInstance().marriageActivityEndTime = marriageActivityEndTime;
			}else if (action.equals("MTimes")) {
				String oneH = request.getParameter("oneHour");
				String guangbo = request.getParameter("guangbo");
				guangbo = guangbo.substring(1, guangbo.length()-1);
				MarriageManager.ONE_HOUR = Long.parseLong(oneH);
				String[] gbs = guangbo.split(", ");
				long[] gg = new long[gbs.length];
				for (int i = 0; i < gbs.length; i++) {
					gg[i] = Long.parseLong(gbs[i]);
				}
				MarriageManager.hunli_time = gg;
			}else if (action.equals("xiufuHanfu")) {
				MarriageManager.getInstance().marriageActivityNames = new String[]{"면죄금패","면죄금패","면죄금패","면죄금패","면죄금패"};
				MarriageManager.getInstance().marriageActivityColor = new int[]{3,3,3,3,3};
				MarriageManager.getInstance().marriageActivityNum = new int[]{1,2,4,7,11};
				MarriageManager.getInstance().isOpenMarriageActivity = true;
				SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
				MarriageManager.getInstance().marriageActivityStartTime = "2013-08-06 00:00:00";
				MarriageManager.getInstance().marriageActivityEndTime = "2013-09-13 23:59:59";
				MarriageManager.getInstance().marriageActivityStartTimeL = format.parse(MarriageManager.getInstance().marriageActivityStartTime).getTime();
				MarriageManager.getInstance().marriageActivityEndTimeL = format.parse(MarriageManager.getInstance().marriageActivityEndTime).getTime();
			}
		}
	%>

<table border="1">

		<tr>
		<td>rb.getId() </td>
		<td>A玩家ID </td>
		<td>在线 </td>
		<td>邀请人数 </td>
		<td>B玩家ID </td>
		<td>在线 </td>
		<td>邀请人数 </td>
		<td>规模 </td>
		<td>状态 </td>
		<td>婚礼开始时间 </td>
		<td>婚礼完成时间 </td>
		<td>掉落 </td>
		<td>cityID</td>
	</tr>

	<%
		SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		for (Iterator<MarriageInfo> iterator = underRuleMap.values().iterator(); iterator.hasNext();) {

			MarriageInfo rb = iterator.next();
			boolean isOnlineA = PlayerManager.getInstance().isOnline(rb.getHoldA());
			boolean isOnlineB = PlayerManager.getInstance().isOnline(rb.getHoldB());
			String startTime = format.format(new Date(rb.getStartTime()));
			String successTime = format.format(new Date(rb.getSuccessTime()));
	%>
	<tr>
		<td><%=rb.getId() %></td>
		<td><%=rb.getHoldA() %></td>
		<td><%=isOnlineA %></td>
		<td><%=rb.getGuestA().size() %></td>
		<td><%=rb.getHoldB() %></td>
		<td><%=isOnlineB %></td>
		<td><%=rb.getGuestB().size() %></td>
		<td><%=rb.getLevel() %></td>
		<td><%=rb.getState() %></td>
		<td><%=startTime %></td>
		<td><%=successTime %></td>
		<td><%=rb.getDropNum() %></td>
		<td><%=rb.getCityId() %></td>
	</tr>
	<%
		}
	%>
	</table>
	
	<br>
	<form>
		<input type="hidden" name="action" value="Mactivity">
		<input type="text" name="startTime" value="<%=MarriageManager.getInstance().marriageActivityStartTime %>">
		<input type="text" name="endTime" value="<%=MarriageManager.getInstance().marriageActivityEndTime %>">
		<input type="submit" value="确定">
	</form>
	<br>
	<form>
		<input type="hidden" name="action" value="MTimes">
		<input type="text" name="oneHour" value="<%=MarriageManager.ONE_HOUR %>">
		<input type="text" name="guangbo" value="<%=Arrays.toString(MarriageManager.hunli_time) %>">
		<input type="submit" value="确定">
	</form>
	<br>
	<form>修复韩服
		<input type="hidden" name="action" value="xiufuHanfu">
		<input type="submit" value="确定">
	</form>
</body>
</html>