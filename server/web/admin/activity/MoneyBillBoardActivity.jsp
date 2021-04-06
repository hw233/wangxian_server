<%@page import="com.fy.engineserver.newBillboard.BillboardsManager"%>
<%@page import="java.text.SimpleDateFormat"%>
<%@page import="com.fy.engineserver.activity.chongzhiActivity.MoneyBillBoardActivity"%>
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
		String action = request.getParameter("action");
		if (action != null) {
			if ("ceshi_chongzhi".equals(action)) {
				String playerName = request.getParameter("playerName");
				String moneys = request.getParameter("money");
				Player player = null;
				try{
					player = PlayerManager.getInstance().getPlayer(playerName);
				}catch(Exception e) {
					System.out.println(e);
				}
				ChongZhiActivity.getInstance().doChongZhiMoneyActivity(player, Long.parseLong(moneys));
				response.sendRedirect("./MoneyBillBoardActivity.jsp");
			}else if ("ceshi_xiaofei".equals(action)) {
				String playerName = request.getParameter("playerName");
				String moneys = request.getParameter("money");
				String xiaofeiType = request.getParameter("xiaofei_type");
				Player player = null;
				try{
					player = PlayerManager.getInstance().getPlayer(playerName);
				}catch(Exception e) {
					System.out.println(e);
				}
				ChongZhiActivity.getInstance().doXiaoFeiMoneyActivity(player, Long.parseLong(moneys), Integer.parseInt(xiaofeiType));
				response.sendRedirect("./MoneyBillBoardActivity.jsp");
			}else if ("xiugai_chongzhi".equals(action)) {
				if (ChongZhiActivity.getInstance().chongZhiBillBoard != null) {
					SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
					String startTime = request.getParameter("startTime");
					String endTime = request.getParameter("endTime");
					long start_time = format.parse(startTime).getTime();
					long end_time = format.parse(endTime).getTime();
					ChongZhiActivity.getInstance().chongZhiBillBoard.setStartTime(startTime);
					ChongZhiActivity.getInstance().chongZhiBillBoard.setStart_time(start_time);
					ChongZhiActivity.getInstance().chongZhiBillBoard.setEndTime(endTime);
					ChongZhiActivity.getInstance().chongZhiBillBoard.setEnd_time(end_time);
				}
				response.sendRedirect("./MoneyBillBoardActivity.jsp");
			}else if ("xiugai_xiaofei".equals(action)) {
				if (ChongZhiActivity.getInstance().xiaoFeiBillBoard != null) {
					SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
					String startTime = request.getParameter("startTime");
					String endTime = request.getParameter("endTime");
					long start_time = format.parse(startTime).getTime();
					long end_time = format.parse(endTime).getTime();
					ChongZhiActivity.getInstance().xiaoFeiBillBoard.setStartTime(startTime);
					ChongZhiActivity.getInstance().xiaoFeiBillBoard.setStart_time(start_time);
					ChongZhiActivity.getInstance().xiaoFeiBillBoard.setEndTime(endTime);
					ChongZhiActivity.getInstance().xiaoFeiBillBoard.setEnd_time(end_time);
				}
				response.sendRedirect("./MoneyBillBoardActivity.jsp");
			}else if ("qing_billboard".equals(action)) {
				BillboardsManager.getInstance().lastFlushBillboardTime = 0;
				response.sendRedirect("./MoneyBillBoardActivity.jsp");
			}else if ("qing_faguo".equals(action)) {
				ChongZhiActivity.getInstance().isChongZhiEndOver = false;
				ChongZhiActivity.getInstance().isXiaoFeiEndOver = false;
				ChongZhiActivity.getInstance().diskCache.put(ChongZhiActivity.CHONGZHI_ENDOVER, ChongZhiActivity.getInstance().isChongZhiEndOver);
				ChongZhiActivity.getInstance().diskCache.put(ChongZhiActivity.XIAOFEI_ENDOVER, ChongZhiActivity.getInstance().isXiaoFeiEndOver);
				response.sendRedirect("./MoneyBillBoardActivity.jsp");
			}
		}
	%>


	参与充值排行的玩家:<br>
	<%
		for (Long key : ChongZhiActivity.getInstance().getChongZhiBillBoardMsg().keySet()) {
			long money = ChongZhiActivity.getInstance().getChongZhiBillBoardMsg().get(key);
			%>
			<%=key %>====<%=money %><br>
			<%
		}
	%>
	<br>
	参与消费排行的玩家:<br>
	<%
		for (Long key : ChongZhiActivity.getInstance().getXiaoFeiBillBoardMsg().keySet()) {
			long money = ChongZhiActivity.getInstance().getXiaoFeiBillBoardMsg().get(key);
			%>
			<%=key %>====<%=money %><br>
			<%
		}
	%>
	<br>
	是否已经发过充值<%=ChongZhiActivity.getInstance().isChongZhiEndOver %>
	<br>
	是否已经发过消费<%=ChongZhiActivity.getInstance().isXiaoFeiEndOver %>
	<br>
	<br>
	
	<table border="1">
		<tr>
			<td>index </td>
			<td>服务器名 </td>
			<td>活动类型 </td>
			<td>参数 </td>
			<td>开启时间 </td>
			<td>结束时间 </td>
			<td>物品名称 </td>
			<td>邮件title </td>
			<td>是否开启 </td>
			<td>是否关闭 </td>
		</tr>
	<%
		for (int i = 0; i < ChongZhiActivity.getInstance().moneyBillboardConfigs.size(); i++) {
			MoneyBillBoardActivity moneyConfig = ChongZhiActivity.getInstance().moneyBillboardConfigs.get(i);
			%>
			<tr>
				<td><%=i %> </td>
				<td><%=moneyConfig.getServerName() %> </td>
				<td><%=moneyConfig.getType() %> </td>
				<td><%=Arrays.toString(moneyConfig.getParameters()) %> </td>
				<td><%=moneyConfig.getStartTime() %> </td>
				<td><%=moneyConfig.getEndTime() %> </td>
				<td><%=Arrays.toString(moneyConfig.getPropNames()) %> </td>
				<td><%=moneyConfig.getMailTitle() %> </td>
				<td><%=moneyConfig.isStart() %> </td>
				<td><%=moneyConfig.isEnd() %> </td>
			</tr>
			<%
		}
	%>
	</table>
	
	<br>
	<br>
	<form name="f1">
		<input type="hidden" name="action" value="ceshi_chongzhi">
		玩家名字<input name="playerName">
		充值金额<input name="money">
		<input type="submit" value="测试充值 ">
	</form>
	<br>
	<br>
	<form name="f2">
		<input type="hidden" name="action" value="ceshi_xiaofei">
		玩家名字<input name="playerName">
		消费金额<input name="money">
		消费类型<input name="xiaofei_type">
		<input type="submit" value="测试消费">
	</form>
	<br>
	<br>
	<form name="f3">
		<input type="hidden" name="action" value="xiugai_chongzhi">
		开始时间<input name="startTime"  value="2012-01-01 00:00:00">
		结束时间<input name="endTime" value="2012-01-01 00:00:00">
		<input type="submit" value="修改充值时间">
	</form>
	<br>
	<br>
	<form name="f4">
		<input type="hidden" name="action" value="xiugai_xiaofei">
		开始时间<input name="startTime" value="2012-01-01 00:00:00">
		结束时间<input name="endTime" value="2012-01-01 00:00:00">
		<input type="submit" value="修改消费时间">
	</form>
	<br>
	<br>
	<form name="f5">
		<input type="hidden" name="action" value="qing_billboard">
		<input type="submit" value="请排行">
	</form>
	<br>
	<br>
	<form name="f6">
		<input type="hidden" name="action" value="qing_faguo">
		<input type="submit" value="修改是否发过">
	</form>
</body>
</html>
