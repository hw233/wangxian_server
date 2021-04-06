<%@page import="com.fy.engineserver.activity.newChongZhiActivity.WeekChongZhiActivity.TodayActivity"%>
<%@page import="java.lang.reflect.Array"%>
<%@page import="com.fy.engineserver.activity.newChongZhiActivity.WeekChongZhiActivity"%>
<%@page import="java.util.ArrayList"%>
<%@page import="com.xuanzhi.boss.game.GameConstants"%>
<%@page import="com.fy.engineserver.activity.newChongZhiActivity.WeekAndMonthChongZhiActivity"%>
<%@page import="com.fy.engineserver.activity.newChongZhiActivity.NewMoneyActivity"%>
<%@page import="com.fy.engineserver.activity.newChongZhiActivity.SingleBalanceChongZhiActivity"%>
<%@page import="com.fy.engineserver.activity.newChongZhiActivity.TotalTimesChongZhiActivity"%>
<%@page import="com.fy.engineserver.activity.newChongZhiActivity.FirstChongZhiFanLiActivity"%>
<%@page import="com.fy.engineserver.newBillboard.BillboardsManager"%>
<%@page import="com.fy.engineserver.activity.newChongZhiActivity.FanLiTimelyActivity"%>
<%@page import="com.fy.engineserver.activity.newChongZhiActivity.BillBoardActivity"%>
<%@page import="com.fy.engineserver.activity.newChongZhiActivity.TotalChongZhiActivity"%>
<%@page import="com.fy.engineserver.activity.newChongZhiActivity.SingleChongZhiActivity"%>
<%@page import="com.fy.engineserver.activity.newChongZhiActivity.FanLi4LongTimeChongZhiActivity"%>
<%@page import="com.fy.engineserver.activity.newChongZhiActivity.FirstChongZhiActivity"%>
<%@page import="java.util.Arrays"%>
<%@page import="com.fy.engineserver.sprite.PlayerManager"%>
<%@page import="com.fy.engineserver.activity.newChongZhiActivity.NewChongZhiActivityManager"%>
<%@page import="com.fy.engineserver.sprite.Player"%>
<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<%@page import="com.fy.engineserver.newtask.service.TaskManager"%>
<%@page import="java.util.List"%>
<%@page import="com.fy.engineserver.newtask.Task"%>
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title>Insert title here</title>
</head>
<body>

	<%
	String serverName = GameConstants.getInstance().getServerName();
	ArrayList<String> unServerName = new ArrayList<String>();
	unServerName.add("国内本地测试");
	unServerName.add("pan2");
	unServerName.add("ST");
	unServerName.add("化外之境");
	unServerName.add("仙尊降世");
	unServerName.add("更端测试A");
	unServerName.add("更端测试B");
	unServerName.add("更端测试C");
	if (!unServerName.contains(serverName)) {

	String userName = (String)session.getAttribute("username");
	if(userName == null){

		String action = request.getParameter("action");
		if(action != null && action.equals("login")){
			String u = request.getParameter("username");
			String p = request.getParameter("password");

			if(u != null && p != null && u.equals("wtx") && p.equals("wtx111") ){
				userName = u;
				session.setAttribute("username",userName);
			}else{
				out.println("<h3>用户名密码不对</h3>");
			}
		}

		if(userName == null){
%>
	<center>
		<h1>请先登录</h1>
		<form>
		<input type="hidden" name="action" value="login">
		请输入用户名：<input type="text" name="username" value="" size=30><br/>
		请输入密码：<input type="text" name="password" value="" size=30><br/>
		<input type="submit" value="提  交">
		</form>
	</center>
<%
		return;
		}
	}
	}
%>
	
	<br><a href="./WeekChongZhiActivity.jsp">刷新此页面</a><br>

	<%
		String action = request.getParameter("action");
		if (action != null) {
			if (action.equals("backExcel")) {
				NewChongZhiActivityManager.instance.LoadWeekChongZhiActivity();
			}else if (action.equals("changeSpace")) {
				long t = Long.parseLong(request.getParameter("time"));
				WeekChongZhiActivity.ACTIVITY_SPACE = t;
			}else if (action.equals("MoNiChongZhi")) {
				long cMoney = Long.parseLong(request.getParameter("money"));
				if (cMoney > 2000000) {
					return;
				}
				long pid = Long.parseLong(request.getParameter("pId"));
				Player player = PlayerManager.getInstance().getPlayer(pid);
				NewChongZhiActivityManager.instance.doChongZhiActivity(player, cMoney);
			}else if (action.equals("changeIndex")) {
				int i = Integer.parseInt(request.getParameter("AIndex"));
				WeekChongZhiActivity.setAddIndex(i);
			}else if (action.equals("clearMsg")) {
				long pId = Long.parseLong(request.getParameter("pId"));
				ArrayList<WeekChongZhiActivity> weekCZs = NewChongZhiActivityManager.instance.weekCZs;
				WeekChongZhiActivity ac = null;
				for (int i = 0; i < weekCZs.size(); i++) {
					if (weekCZs.get(i).isStart()) {
						ac = weekCZs.get(i);
						break;
					}
				}
				ac.playerTotal4Week.remove(pId);
				ac.playerGetReward.remove(pId);
			}else if (action.equals("GetReward")) {
				String acID_str = request.getParameter("acID");
				String index_str = request.getParameter("index");
				String playerId_str = request.getParameter("pID");
				int acId = Integer.parseInt(acID_str);
				int index = Integer.parseInt(index_str);
				ArrayList<WeekChongZhiActivity> weekCZs = NewChongZhiActivityManager.instance.weekCZs;
				Player p = PlayerManager.getInstance().getPlayer(Long.parseLong(playerId_str));
				WeekChongZhiActivity ac = null;
				for (int i = 0; i < weekCZs.size(); i++) {
					if (weekCZs.get(i).getId() == acId) {
						ac = weekCZs.get(i);
						break;
					}
				}
				if (index < ac.getWeekActivitys().size()) {
					NewChongZhiActivityManager.instance.WeekCZ_GetReward(p, WeekChongZhiActivity.WEEK_CZ_TYPE_0);
				}else if (index == ac.getWeekActivitys().size()) {
					NewChongZhiActivityManager.instance.WeekCZ_GetReward(p, WeekChongZhiActivity.WEEK_CZ_TYPE_1);
				}else {
					NewChongZhiActivityManager.instance.WeekCZ_GetReward(p, WeekChongZhiActivity.WEEK_CZ_TYPE_2);
				}
			}else if (action.equals("seePlayer")) {
				String pId = request.getParameter("pId");
				Player p = PlayerManager.getInstance().getPlayer(Long.parseLong(pId));
				if (p != null) {
					ArrayList<WeekChongZhiActivity> weekCZs = NewChongZhiActivityManager.instance.weekCZs;
					WeekChongZhiActivity ac = null;
					for (int i = 0; i < weekCZs.size(); i++) {
						if (weekCZs.get(i).isStart()) {
							ac = weekCZs.get(i);
							break;
						}
					}
					if (ac != null) {
						%>
						<br>
						参与活动ID:<%=ac.getId() %>
						<br>
						<table border="2">
							<tr>
								<td><%="说明" %></td>
								<%
								for (int i =0; i < ac.getWeekActivitys().size(); i++) {
									%>
									<td><%="第"+i+"天" %></td>
									<%
								}
								%>
								<td><%="累计"+ac.getSmall().getTotalNum()+"天" %></td>
								<td><%="累计"+ac.getBig().getTotalNum()+"天" %></td>
							</tr>
							<tr>
								<td>充值金额</td>
								 <%
								 	long[] totals = ac.playerTotal4Week.get(p.getId());
								 	if (totals == null) {
								 		totals = new long[ac.getWeekActivitys().size()];
								 	}
								 	for (int i = 0; i < totals.length; i++) {
								 		%>
								 		<td><%=totals[i] %></td>
								 		<%
								 	}
								 %>
							</tr>
							<tr>
								<td>可领取</td>
								<%
									int totalNum = 0;
								 	for (int i = 0; i < totals.length; i++) {
								 		if (totals[i]>=ac.getNeedMonty()) {
								 			totalNum++;
								 		}
								 		%>
								 		<td><%=totals[i]>=ac.getNeedMonty() %></td>
								 		<%
								 	}
								 %>
								 	<td><%=totalNum>=ac.getSmall().getTotalNum() %></td>
								 	<td><%=totalNum>=ac.getBig().getTotalNum() %></td>
							</tr>
							<tr>
								<td>已领取</td>
								<%
								 	boolean[] gets = ac.playerGetReward.get(p.getId());
								 	if (gets == null) {
								 		gets = new boolean[ac.getWeekActivitys().size()+2];
								 	}
								 	for (int i = 0; i < gets.length; i++) {
								 		%>
								 		<td><%=gets[i] %></td>
								 		<%
								 	}
								 %>
							</tr>
							<tr>
								<td>操作</td>
								<%
								 	for (int i = 0; i < gets.length; i++) {
								 		String href = "./WeekChongZhiActivity.jsp?action=GetReward&pID="+p.getId()+"&acID="+ac.getId()+"&index="+i+"";
								 		%>
								 		<td><a href=<%=href %>>修改</a></td>
								 		<%
								 	}
								 %>
							</tr>
						</table>
						<br>
						<form name="f2">
							清除玩家参与情况
							<input type="hidden" name="action" value="clearMsg">
							<input type="hidden" name="pId" value=<%=p.getId() %>>
							<input type="submit" value=<%="确定"%>>
						</form>
						<br>
						<br>
						<%
					}
				}
			}
		}
	
	%>
	
	<form name="f2">
		重新载入
		<input type="hidden" name="action" value="backExcel">
		<input type="submit" value=<%="确定"%>>
	</form>
	<br>
	<form name="f3">
		查询某个玩家活动情况
		<input type="hidden" name="action" value="seePlayer">
		<input type="text" name="pId">
		<input type="submit" value=<%="确定"%>>
	</form>
	<br>
	<form name="f3">
		修改活动时间
		<input type="hidden" name="action" value="changeIndex">
		<input type="text" name="AIndex" value=<%=WeekChongZhiActivity.getAddIndex() %>>
		<input type="submit" value=<%="确定"%>>
	</form>
	<br>
	<form name="f4">
		给玩家模拟充值
		<input type="hidden" name="action" value="MoNiChongZhi">
		<input type="text" name="pId">
		<input type="text" name="money">
		<input type="submit" value=<%="确定"%>>
	</form>
	<br>
	<br>
	<br>
	<table border="2">
		<tr>
			<td>活动ID</td>
			<td>开始时间</td>
			<td>结束时间</td>
			<td>平台</td>
			<td>服务器名字</td>
			<td>不参与服务器名字</td>
			<td>购买金额</td>
			<td>是否server</td>
			<td>是否start</td>
			<td>领取奖励</td>
		</tr>
		<%
			for (int i = 0; i < NewChongZhiActivityManager.instance.weekCZs.size(); i++) {
				WeekChongZhiActivity ac = NewChongZhiActivityManager.instance.weekCZs.get(i);
				String s = Arrays.toString(ac.getServerNames());
				
		%>
			<tr>
			<td><%=ac.getId() %></td>
			<td><%=ac.getStr_startTime() %></td>
			<td><%=ac.getStr_endTime() %></td>
			<td><%=ac.getPlatform() %></td>
			<td><%=s %></td>
			<td><%=Arrays.toString(ac.getUnServerNames()) %></td>
			<td><%=ac.getNeedMonty() %></td>
			<td><%=ac.isServer() %></td>
			<td><%=ac.isStart() %></td>
			</tr>
		<%

				for (int j = 0; j < ac.getWeekActivitys().size(); j++) {
					TodayActivity tc = ac.getWeekActivitys().get(j);
					String nn = "第"+j+"天";
					if (ac.isStart() && j == ac.getActivityIndex()) {
						nn = "【今天】";
					}
					%>
					<tr>
					<td></td>
					<td><%=nn %></td>
					<td></td>
					<td></td>
					<td></td>
					<td></td>
					<td></td>
					<td></td>
					<td></td>
					<td><%=Arrays.toString(tc.getRewardPropNames()) %></td>
					</tr>
					
					<%
				}
				%>
					<tr>
					<td></td>
					<td><%="累计"+ac.getSmall().getTotalNum()+"天" %></td>
					<td></td>
					<td></td>
					<td></td>
					<td></td>
					<td></td>
					<td></td>
					<td></td>
					<td><%=Arrays.toString(ac.getSmall().getRewardPropNames()) %></td>
					</tr>
					<tr>
					<td></td>
					<td><%="累计"+ac.getBig().getTotalNum()+"天" %></td>
					<td></td>
					<td></td>
					<td></td>
					<td></td>
					<td></td>
					<td></td>
					<td></td>
					<td><%=Arrays.toString(ac.getBig().getRewardPropNames()) %></td>
					</tr>
				<%
			}
		%>
	</table>
	<br>
</body>
</html>
