<%@page import="com.fy.engineserver.activity.newChongZhiActivity.WeekAndMonthChongZhiActivity"%>
<%@page import="com.fy.engineserver.activity.newChongZhiActivity.ShowShopChongZhiActivity"%>
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
<table border="2">
	<tr>
		<td>活动ID</td>
		<td>活动需要金额</td>
		<td>活动参与玩家总数</td>
		<td>活动领取玩家数</td>
		<td>活动购买玩家数</td>
	</tr>
	<%
	for (int i = 0; i < NewChongZhiActivityManager.instance.weekMonthDatas.size(); i++) {
		WeekAndMonthChongZhiActivity ac = NewChongZhiActivityManager.instance.weekMonthDatas.get(i);
		out.print("<tr>");
		int canGetNum = 0;
		for (Long key : ac.player_moneys.keySet()) {
			long money = ac.player_moneys.get(key);
			if (money >= ac.getNeedMoney()) {
				canGetNum++;
			}
		}
		out.print("<td>"+ac.getDataID()+"</td>");
		out.print("<td>"+ac.getNeedMoney()+"</td>");
		out.print("<td>"+canGetNum+"</td>");
		int getNum = ac.player_gets.size();
		out.print("<td>"+getNum+"</td>");
		int buyNum = ac.player_buys.size();
		out.print("<td>"+buyNum+"</td>");
		out.print("</tr>");
	}
	%>
</table>
</body>
</html>
