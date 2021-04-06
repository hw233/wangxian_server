<%@page import="java.util.Date"%>
<%@page import="java.util.Arrays"%>
<%@page import="com.fy.engineserver.activity.chongzhiActivity.ChongZhiActivityFanLiToDay"%>
<%@page import="com.fy.engineserver.activity.chongzhiActivity.ChongZhiSpecialActivity"%>
<%@page import="java.text.SimpleDateFormat"%>
<%@page import="com.sqage.stat.commonstat.entity.ChongZhi"%>
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
		ChongZhiActivityFanLiToDay chooseOne = null;
		int chooseindex = -1;
		String action = request.getParameter("action");
		if (action != null) {
			if (action.equals("xiugai")) {
				String acIndex_str = request.getParameter("activityIndex");
				String acID_str = request.getParameter("activityID");
				String serverName_str = request.getParameter("serverName");
				String startTime_str = request.getParameter("startTime");
				String endTime_str = request.getParameter("endTime");
				String mailTitle_str = request.getParameter("mailTitle");
				String mailMsg_str = request.getParameter("mailMsg");
				//返利比例
				String fanlibili_str = request.getParameter("fanlibili");
				fanlibili_str = fanlibili_str.substring(1, fanlibili_str.length()-1);
				String[] fanlibili_str_sp = fanlibili_str.split(", ");
				//返利时间
				String fanliTimes_str = request.getParameter("fanliTimes");
				fanliTimes_str = fanliTimes_str.substring(1, fanliTimes_str.length()-1);
				String[] fanliTimes_str_sp = fanliTimes_str.split(", ");
				//返利间隔
				String fanliSpaces_str = request.getParameter("fanliSpaces");
				fanliSpaces_str = fanliSpaces_str.substring(1, fanliSpaces_str.length()-1);
				String[] fanliSpaces_str_sp = fanliSpaces_str.split(", ");
				
				int acIndex = Integer.parseInt(acIndex_str);
				int acID = Integer.parseInt(acID_str);
				int[] fanlibili = new int[fanlibili_str_sp.length];
				for (int i = 0 ; i < fanlibili.length; i++) {
					fanlibili[i] = Integer.parseInt(fanlibili_str_sp[i]);
				}
				
				long[] fanliSpaces = new long[fanliSpaces_str_sp.length];
				for (int i = 0; i < fanliSpaces.length ; i++) {
					fanliSpaces[i] = Long.parseLong(fanliSpaces_str_sp[i]);
				}
				
				ChongZhiActivityFanLiToDay ac = (ChongZhiActivityFanLiToDay)ChongZhiActivity.getInstance().chongZhiServerConfigs.get(acIndex);
				if (ac != null) {
					ac.setActivityID(acID);
					ac.setServerName(serverName_str);
					ac.setStartTime(startTime_str);
					ac.setEndTime(endTime_str);
					ac.setMailTitle(mailTitle_str);
					ac.setMailMsg(mailMsg_str);
					ac.setFanliBiLi(fanlibili);
					ac.setFanliTimes(fanliTimes_str_sp);
					ac.setFanliSpaces(fanliSpaces);
					ac.creatLongTime();
				}
				
				response.sendRedirect("./ChongZhiActivityFanLi.jsp");
			}else if (action.equals("fuzhi")) {
				chooseindex = Integer.parseInt(request.getParameter("activityIndex"));
				chooseOne = (ChongZhiActivityFanLiToDay)ChongZhiActivity.getInstance().chongZhiServerConfigs.get(chooseindex);
			}else if (action.equals("chongzhi")) {
				String pName = request.getParameter("pName");
				String pMoney_str = request.getParameter("cMoney");
				long cMoney = Long.parseLong(pMoney_str);
				Player player = PlayerManager.getInstance().getPlayer(pName);
				ChongZhiActivity.getInstance().doChongZhiActivity(player, cMoney);
				response.sendRedirect("./ChongZhiActivityFanLi.jsp");
			}else if (action.equals("chongzhi_1")) {
				String pId_str = request.getParameter("pId");
				long pid = Long.parseLong(pId_str);
				String pMoney_str = request.getParameter("cMoney");
				long cMoney = Long.parseLong(pMoney_str);
				Player player = PlayerManager.getInstance().getPlayer(pid);
				ChongZhiActivity.getInstance().doChongZhiActivity(player, cMoney);
				response.sendRedirect("./ChongZhiActivityFanLi.jsp");
			}
		}
	
	%>
	<table border="2">
		<tr>
			<td>index</td>
			<td>活动id</td>
			<td>服务器名字</td>
			<td>startTime</td>
			<td>endTime</td>
			<td>邮件标题</td>
			<td>邮件内容</td>
			<td>比例</td>
			<td>返时间</td>
			<td>返间隔</td>
			<td>开关</td>
		</tr>
		<%
			for (int i = 0 ; i < ChongZhiActivity.getInstance().chongZhiServerConfigs.size(); i++) {
				ChongZhiServerConfig chongzhi = ChongZhiActivity.getInstance().chongZhiServerConfigs.get(i);
				if (chongzhi.getType() == ChongZhiServerConfig.CHONGZHI_FANLI_TODAY_TYPE) {
					ChongZhiActivityFanLiToDay ac = (ChongZhiActivityFanLiToDay)chongzhi;
					
					
		%>
					<tr>
						<td><%=i %></td>
						<td><%=ac.getActivityID() %></td>
						<td><%=ac.getServerName() %></td>
						<td><%=ac.getStartTime() %></td>
						<td><%=ac.getEndTime() %></td>
						<td><%=ac.getMailTitle() %></td>
						<td><%=ac.getMailMsg() %></td>
						<td><%=Arrays.toString(ac.getFanliBiLi()) %></td>
						<td><%=Arrays.toString(ac.getFanliTimes()) %></td>
						<td><%=Arrays.toString(ac.getFanliSpaces()) %></td>
						<td><%=ac.isStart()&&(ac.getServerName().equals(GameConstants.getInstance().getServerName()) || ac.getServerName().equals(ChongZhiServerConfig.ALL_SERVER_NAME))  %></td>
					</tr>
		<%
				}
			}
		%>
	</table>
	
	<form name="f1">
		<input type="hidden" name="action" value="xiugai">
		<%
			String index = "";
			String id = "";
			String serverName = "";
			String startTime = "";
			String endTime = "";
			String mailTile = "";
			String mailMsg = "";
			String fanhuibili = "";
			String fanhuitimes = "";
			String fanhuispaces = "";
			if (chooseOne != null) {
				index = chooseindex + "";
				id = "" + chooseOne.getActivityID();
				serverName = chooseOne.getServerName();
				startTime = chooseOne.getStartTime();
				endTime = chooseOne.getEndTime();
				mailTile = chooseOne.getMailTitle();
				mailMsg = chooseOne.getMailMsg();
				fanhuibili = Arrays.toString(chooseOne.getFanliBiLi());
				fanhuitimes = Arrays.toString(chooseOne.getFanliTimes());
				fanhuispaces = Arrays.toString(chooseOne.getFanliSpaces());
			}
		%>
		活动index<input name="activityIndex" value="<%=index %>">
		ID<input name="activityID" value="<%=id %>">
		服务器<input name="serverName" value="<%=serverName %>">
		开始<input name="startTime" value="<%=startTime %>">
		结束<input name="endTime" value="<%=endTime %>">
		邮件标题<input name="mailTitle"  value="<%=mailTile %>">
		邮件内容<input name="mailMsg" value="<%=mailMsg %>">
		<br>
		返回比例<input name="fanlibili" value="<%=fanhuibili %>" size="100">
		<br>
		返回时间<input name="fanliTimes" value="<%=fanhuitimes %>" size="150">
		<br>
		返回间隔<input name="fanliSpaces" value="<%=fanhuispaces %>" size="100">
		<input type="submit" value="修改">
	</form>
	<br>
	<br>
	<form name="f2">
		<input type="hidden" name="action" value="fuzhi">
		活动index(这个会把上面活动index的值都放到上面的修改框中)<input name="activityIndex">
		<input type="submit" value="准备修改">
	</form>
	<br>
	<form name="f3">
		<input type="hidden" name="action" value="chongzhi">
		测试充值   玩家名字<input name="pName">
		金钱<input name="cMoney">
		<input type="submit" value="确定">
	</form>
	<br>
	<form name="f55">
		<input type="hidden" name="action" value="chongzhi_1">
		测试充值   玩家ID<input name="pId">
		金钱<input name="cMoney">
		<input type="submit" value="确定">
	</form>
	<br>
	累计充值数目:
	<br>
	<table border="1">
		<tr>
			<td>活动id</td>
			<td>pID</td>
			<td>金额</td>
		</tr>
	<%
		for (Integer activityID : ChongZhiActivity.getInstance().chongzhiFanLiMoneys.keySet()) {
			HashMap<Long, Long> playerMoneys = ChongZhiActivity.getInstance().chongzhiFanLiMoneys.get(activityID);
			for (Long pID : playerMoneys.keySet()) {
				Long money = playerMoneys.get(pID);
	%>
				<tr>
					<td><%=activityID %></td>
					<td><%=pID %></td>
					<td><%=money %></td>
				</tr>
	<%
			}
		}
	%>
	</table>
	<br>
	发放情况:
	<br>
	<table border="1">
		<tr>
			<td>活动id</td>
			<td>时间</td>
		</tr>
		<%
			for (Integer activityID : ChongZhiActivity.getInstance().chongzhiFanLiTimes.keySet()) {
				Long sendTime = ChongZhiActivity.getInstance().chongzhiFanLiTimes.get(activityID);
				SimpleDateFormat fo = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
				String t = fo.format(new Date(sendTime));
		%>
				<tr>
					<td><%=activityID %></td>
					<td><%=t %></td>
				</tr>
		<%
			}
		%>
	</table>
</body>
</html>
