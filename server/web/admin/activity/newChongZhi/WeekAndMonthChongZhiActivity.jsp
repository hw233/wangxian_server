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

	<%
		WeekAndMonthChongZhiActivity activity = null;
		WeekAndMonthChongZhiActivity seeActivity = null;
		int isCreatType = -1;
		String action = request.getParameter("action");
		if (action != null) {
			if ("backExcel".equals(action)) {
				NewChongZhiActivityManager.instance.loadWeekMonthFile();
			}else if ("chooseActivity".equals(action)) {
				String idS = request.getParameter("activityID");
				int id = Integer.parseInt(idS);
				for (WeekAndMonthChongZhiActivity ac : NewChongZhiActivityManager.weekMonthDatas) {
					if (ac.getDataID() == id) {
						activity = ac;
						break;
					}
				}
			}else if ("seeActivity".equals(action)) {
				String idS = request.getParameter("activityID");
				int id = Integer.parseInt(idS);
				for (WeekAndMonthChongZhiActivity ac : NewChongZhiActivityManager.weekMonthDatas) {
					if (ac.getDataID() == id) {
						seeActivity = ac;
						break;
					}
				}
			}else if ("testChongZhi".equals(action)) {
				String pName = request.getParameter("pName");
				String pMoney_str = request.getParameter("money");
				String[] pNames = pName.split(",");
				String[] pMoneys = pMoney_str.split(",");
				if (pNames.length == pMoneys.length) {
					for (int i = 0; i < pNames.length; i++) {
						try {
							long cMoney = Long.parseLong(pMoneys[i]);
							Player player = PlayerManager.getInstance().getPlayer(Long.parseLong(pNames[i]));
							NewChongZhiActivityManager.instance.doChongZhiActivity(player, cMoney);
						} catch(Exception e) {
							out.println("失败--"+pNames[i]+"~~~"+pMoneys[i]);
							out.println(e);
							out.println("<br>");
						}
						out.println("成功--"+pNames[i]+"~~~"+pMoneys[i]);
						out.println("<br>");
					}
				}else {
					out.println("失败，输入的ID数目和金额数目不一致");
					out.println("<br>");
				}
			}else if ("cleanActivity".equals(action)) {
				String idS = request.getParameter("activityID");
				int id = Integer.parseInt(idS);
				for (WeekAndMonthChongZhiActivity ac : NewChongZhiActivityManager.weekMonthDatas) {
					if (ac.getDataID() == id) {
						ac.player_moneys.clear();
						ac.player_buys.clear();
						ac.player_gets.clear();
						WeekAndMonthChongZhiActivity.diskcache.put(WeekAndMonthChongZhiActivity.DISK_KEY_MONEY + ac.getDataID(), ac.player_moneys);
						WeekAndMonthChongZhiActivity.diskcache.put(WeekAndMonthChongZhiActivity.DISK_KEY_BUY + ac.getDataID(), ac.player_buys);
						WeekAndMonthChongZhiActivity.diskcache.put(WeekAndMonthChongZhiActivity.DISK_KEY_GET + ac.getDataID(), ac.player_gets);
					}
				}
			}
		}
	
	%>
	
	<form name="f8">
		重新载入
		<input type="hidden" name="action" value="backExcel">
		<input type="submit" value=<%="确定"%>>
	</form>
	<br>
	
	<table border="2">
		<tr>
			<td>活动ID</td>
			<td>活动类型</td>
			<td>开始时间</td>
			<td>结束时间</td>
			<td>平台</td>
			<td>服务器名字</td>
			<td>不参与服务器名字</td>
			<td>领取奖励</td>
			<td>购买奖励</td>
			<td>购买金额</td>
			<td>是否server</td>
			<td>是否start</td>
		</tr>
		<%
			for (int i = 0; i < NewChongZhiActivityManager.weekMonthDatas.size(); i++) {
				WeekAndMonthChongZhiActivity ac = NewChongZhiActivityManager.weekMonthDatas.get(i);
				long now = System.currentTimeMillis();
				boolean isStart = ac.isInTime();
				String rewardString = "";
				for (int kk = 0; kk < ac.getRewardPropNames().length; kk++) {
					rewardString += "["+ac.getRewardPropNames()[kk]+"-"+ac.getRewardPropNums()[kk]+"个"+"]";
				}
				String buyString = "";
				for (int kk = 0; kk < ac.getBuyPropNames().length; kk++) {
					buyString += "["+ac.getBuyPropNames()[kk]+"-"+ac.getBuyPropNums()[kk]+"个"+"]";
				}
				
		%>
			<tr>
			<td><%=ac.getDataID() %></td>
			<td><%=ac.getType()==0?"周累计":"月累计" %></td>
			<td><%=ac.getStartTime() %></td>
			<td><%=ac.getEndTime() %></td>
			<td><%=ac.getPlatform() %></td>
			<td><%=Arrays.toString(ac.getServerNames()) %></td>
			<td><%=Arrays.toString(ac.getUnServerNames()) %></td>
			<td><%=rewardString %></td>
			<td><%=buyString %></td>
			<td><%=ac.getBuyPrice() %></td>
			<td><%=ac.isCanServer() %></td>
			<td><%=isStart %></td>
		</tr>
		<%
			}
		%>
	</table>
	<br>
	<form name="f3">
		修改某个活动
		<input type="hidden" name="action" value="chooseActivity">
		活动ID<input name="activityID">
		<input type="submit" value=<%="确定"%>>
	</form>
	<br>
	<form name="f6">
		创建活动
		<input type="hidden" name="action" value="createTO">
		活动Type(0周累计，1月累计)<input name="createType">
		<input type="submit" value=<%="确定"%>>
	</form>
	<br>
	<form name="f7">
		查看活动参与情况
		<input type="hidden" name="action" value="seeActivity">
		活动ID<input name="activityID">
		<input type="submit" value=<%="确定"%>>
	</form>
	<br>
	<%
		if (activity != null) {
	%>
		<form name="f4">
		<hr>
		活动信息
		<input type="hidden" name="action" value="xiugai">
		活动ID<input name="activityID" value=<%=activity.getDataID() %>>
		显示RMB<input name="showRMBMoney" value=<%=activity.getShowRMBMoney() %>>
		需要金额<input name="needMoney" value=<%=activity.getNeedMoney() %>>
		type<input name="type" value=<%=activity.getType() %>>
		平台<input name="platform" value=<%=activity.getPlatform() %>>
		<br>
		服务器名字<input name="serverNames" value="<%=Arrays.toString(activity.getServerNames()) %>" size="100">
		<br>
		un服务器名字<input name="unserverNames" value="<%=Arrays.toString(activity.getUnServerNames()) %>" size="100">
		<br>
		startTime<input name="startTime" value="<%=activity.getStartTime() %>">
		endTime<input name="endTime" value="<%=activity.getEndTime() %>">
		<br>
		邮件标题<input name="mailTile" value="<%=activity.getMailTitle() %>">
		邮件内容<input name="mailMsg" value="<%=activity.getMailMsg() %>" size="100">
		<br>
		奖励物品名字<input name="rewardPropNames" value="<%=Arrays.toString(activity.getRewardPropNames()) %>" size="100">
		奖励物品数量<input name="rewardPropNums" value="<%=Arrays.toString(activity.getRewardPropNums()) %>" size="100">
		奖励物品颜色<input name="rewardPropColors" value="<%=Arrays.toString(activity.getRewardColors()) %>" size="100">
		奖励珍贵<input name="rewardPropRares" value="<%=Arrays.toString(activity.getRewardRare()) %>" size="100">
		奖励绑定<input name="rewardPropBinds" value="<%=Arrays.toString(activity.getRewardBinds()) %>" size="100">
		<br>
		购买物品名字<input name="buyPropNames" value="<%=Arrays.toString(activity.getBuyPropNames()) %>" size="100">
		购买物品数量<input name="buyPropNums" value="<%=Arrays.toString(activity.getBuyPropNums()) %>" size="100">
		购买物品颜色<input name="buyPropColors" value="<%=Arrays.toString(activity.getBuyColors()) %>" size="100">
		购买珍贵<input name="buyPropRares" value="<%=Arrays.toString(activity.getBuyRare()) %>" size="100">
		购买绑定<input name="buyPropBinds" value="<%=Arrays.toString(activity.getBuyBinds()) %>" size="100">
		购买价钱<input name="buyPrice" value="<%=activity.getBuyPrice() %>" size="100">
		<input type="submit" value=<%="确定"%>>
		<hr>
		</form>
	<%
		}
	%>
	<%
		if (isCreatType >= 0) {
	%>
		<form name="f5">
		<hr>
		活动信息
		<input type="hidden" name="action" value="createOne">
		<input type="hidden" name="creteType" value="<%=isCreatType %>">
		活动ID<input name="activityID">
		显示RMB<input name="showRMBMoney">
		需要金额<input name="needMoney">
		平台<input name="platform">
		<br>
		服务器名字<input name="serverNames" size="100">
		<br>
		un服务器名字<input name="unserverNames" size="100">
		<br>
		startTime<input name="startTime">
		endTime<input name="endTime">
		<br>
		邮件标题<input name="mailTile">
		邮件内容<input name="mailMsg">
		<br>
		奖励物品名字<input name="rewardPropNames" size="100">
		奖励物品数量<input name="rewardPropNums" size="100">
		奖励物品颜色<input name="rewardPropColors" size="100">
		奖励珍贵<input name="rewardPropRares" size="100">
		奖励绑定<input name="rewardPropBinds" size="100">
		<br>
		购买物品名字<input name="buyPropNames" size="100">
		购买物品数量<input name="buyPropNums" size="100">
		购买物品颜色<input name="buyPropColors" size="100">
		购买珍贵<input name="buyPropRares" size="100">
		购买绑定<input name="buyPropBinds" size="100">
		购买价钱<input name="buyPrice" size="100">
		<input type="submit" value=<%="确定"%>>
		<hr>
		</form>
	<%
		}
	%>
	<br>
	<form name="f1">
		测试充值活动
		<input type="hidden" name="action" value="testChongZhi">
		玩家ID<input name="pName">
		金额<input name="money">
		<input type="submit" value=<%="确定"%>>
	</form>
	<br>
	<form name="f2">
		清除活动数据
		<input type="hidden" name="action" value="cleanActivity">
		活动ID<input name="activityID">
		<input type="submit" value=<%="确定"%>>
	</form>
	<br>
	<%
		if (seeActivity != null) {
	%>
		活动详细信息
		<br>
		<%
			for (Long key : seeActivity.player_moneys.keySet()) {
				long money = seeActivity.player_moneys.get(key);
				boolean isGET = seeActivity.player_gets.contains(key);
				
				boolean isBuy = seeActivity.player_buys.contains(key);
				String aa = key + "~" + money+"--领取:" + isGET + "  购买:" + isBuy;
		%>
			<%=aa %>
			<br>
		<%
			}
		%>
	<%
		}
	%>
</body>
</html>
