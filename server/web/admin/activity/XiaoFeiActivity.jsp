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
	XiaoFeiServerConfig xiaofei = null;
	String action = request.getParameter("action");
	if (action != null) {
		if (action.equals("chooseActivity")) {
			long aid = Long.parseLong(request.getParameter("activityID"));
			for (int i = 0; i < ChongZhiActivity.getInstance().xiaoFeiServerConfigs.size(); i++) {
				XiaoFeiServerConfig xiaofeiConfig = ChongZhiActivity.getInstance().xiaoFeiServerConfigs.get(i);
				if (xiaofeiConfig.getId() == aid) {
					xiaofei = xiaofeiConfig;
					break;
				}
			}
		}else if (action.equals("xiugai")) {
			long aid = Long.parseLong(request.getParameter("activityID"));
			for (int i = 0; i < ChongZhiActivity.getInstance().xiaoFeiServerConfigs.size(); i++) {
				XiaoFeiServerConfig xiaofeiConfig = ChongZhiActivity.getInstance().xiaoFeiServerConfigs.get(i);
				if (xiaofeiConfig.getId() == aid) {
					xiaofei = xiaofeiConfig;
					break;
				}
			}
			xiaofei.setStartTime(request.getParameter("startTime"));
			xiaofei.setEndTime(request.getParameter("endTime"));
			xiaofei.creatLongTime();
		}
	}
%>
	参与首次消费的玩家:<br>
	<%
		for (int i = 0; i < ChongZhiActivity.getInstance().getFirstXiaoFeiPlayers().size(); i++) {
			long fid = ChongZhiActivity.getInstance().getFirstXiaoFeiPlayers().get(i);
			%>
			<%=fid %><br>
			<%
		}
	%>
	<br>
	<br>
	参与累计消费多次活动的玩家:<br>
	<%
		for (Long key : ChongZhiActivity.getInstance().getXiaoFeiMoneys().keySet()) {
			Long moneys = ChongZhiActivity.getInstance().getXiaoFeiMoneys().get(key);
			%>
			id=<%=key %><%="   money=" %><%=moneys %><br>
			<%
		}
	%>
	<br>
	<table border="1">
		<tr>
			<td>index </td>
			<td>服务器名 </td>
			<td>活动类型 </td>
			<td>消费通道 </td>
			<td>开启时间 </td>
			<td>结束时间 </td>
			<td>金额RMB </td>
			<td>物品名称 </td>
			<td>数量 </td>
			<td>颜色 </td>
			<td>是否绑定 </td>
			<td>邮件title </td>
			<td>是否开启 </td>
		</tr>
	<%
		for (int i = 0; i < ChongZhiActivity.getInstance().xiaoFeiServerConfigs.size(); i++) {
			XiaoFeiServerConfig xiaofeiConfig = ChongZhiActivity.getInstance().xiaoFeiServerConfigs.get(i);
			String xiaofeitongdao = "";
			for (int j = 0; j < xiaofeiConfig.getXiaoFeiTongDao().length; j++) {
				xiaofeitongdao += XiaoFeiServerConfig.XIAOFEI_TONGDAO_STR[xiaofeiConfig.getXiaoFeiTongDao()[j]]+",";
			}
			
			%>
			<tr>
				<td><%=i %> </td>
				<td><%=xiaofeiConfig.getServerName() %> </td>
				<td><%=XiaoFeiServerConfig.XIAOFEI_TYPE_STR[xiaofeiConfig.getType()] %> </td>
				<td><%=xiaofeitongdao %> </td>
				<td><%=xiaofeiConfig.getStartTime() %> </td>
				<td><%=xiaofeiConfig.getEndTime() %> </td>
				<td><%=xiaofeiConfig.getMoney() %> </td>
				<td><%=xiaofeiConfig.getPropName() %> </td>
				<td><%=xiaofeiConfig.getPropNum() %> </td>
				<td><%=xiaofeiConfig.getColorType() %> </td>
				<td><%=xiaofeiConfig.isBind() %> </td>
				<td><%=xiaofeiConfig.getMailTitle() %> </td>
				<td><%=xiaofeiConfig.isStart()&&(xiaofeiConfig.getServerName().equals(GameConstants.getInstance().getServerName()) || xiaofeiConfig.getServerName().equals(XiaoFeiServerConfig.ALL_SERVER_NAME)) %> </td>
			</tr>
			<%
		}
	%>
	</table>
	<br>
	<br>
	<form name="f3">
		修改某个活动
		<input type="hidden" name="action" value="chooseActivity">
		活动ID<input name="activityID">
		<input type="submit" value=<%="确定"%>>
	</form>
	<br>
	
	<%
		if (xiaofei != null) {
	%>
		<form name="f4">
		<hr>
		活动信息
		<input type="hidden" name="action" value="xiugai">
		活动ID<input name="activityID" value=<%=xiaofei.getId() %>>
		服务器名字<input name="serverNames" value="<%=xiaofei.getServerName() %>" size="100">
		<br>
		startTime<input name="startTime" value="<%=xiaofei.getStartTime() %>">
		endTime<input name="endTime" value="<%=xiaofei.getEndTime() %>">
		<br>
		邮件标题<input name="mailTile" value=<%=xiaofei.getMailTitle() %>>
		邮件内容<input name="mailMsg" value="<%=xiaofei.getMailMsg() %>" size="100">
		<input type="submit" value=<%="确定"%>>
		<hr>
		</form>
	<%
		}
	%>
	
</body>
</html>
