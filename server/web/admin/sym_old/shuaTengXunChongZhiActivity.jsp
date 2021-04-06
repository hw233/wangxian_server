<%@page import="com.fy.engineserver.activity.newChongZhiActivity.WeekAndMonthChongZhiActivity"%>
<%@page import="com.fy.engineserver.activity.newChongZhiActivity.FirstFanArticle4LongTimeChongZhiActivity"%>
<%@page import="com.fy.engineserver.activity.newChongZhiActivity.FanArticle4LongTimeChongZhiActivity"%>
<%@page import="com.fy.engineserver.activity.newChongZhiActivity.SingleChongZhiSingleActivity"%>
<%@page import="java.util.ArrayList"%>
<%@page import="com.xuanzhi.boss.game.GameConstants"%>
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

	<%
		NewChongZhiActivityManager.instance.chongZhiActivitys.clear();
		NewChongZhiActivityManager.instance.loadFile();
		NewChongZhiActivityManager.instance.allRmbDatas.clear();
		NewChongZhiActivityManager.instance.loadChangQiFile();
		NewChongZhiActivityManager.instance.xiaoFeiAcitivitys.clear();
		NewChongZhiActivityManager.instance.loadXiaoFeiFile();
		NewChongZhiActivityManager.instance.weekMonthDatas.clear();
		NewChongZhiActivityManager.instance.loadWeekMonthFile();
		
		WeekAndMonthChongZhiActivity ac9000001 = null;
		WeekAndMonthChongZhiActivity ac9000002 = null;
		WeekAndMonthChongZhiActivity ac154 = null;
		for (WeekAndMonthChongZhiActivity ac : NewChongZhiActivityManager.weekMonthDatas) {
			if (ac.getDataID() == 154) {
				ac154 = ac;
			}else if (ac.getDataID() == 9000001) {
				ac9000001 = ac;
			}else if (ac.getDataID() == 9000002) {
				ac9000002 = ac;
			}
		}
		if (ac9000001 != null && ac9000002 != null && ac154 != null) {
			String msg = "";
			for (Long key : ac154.player_moneys.keySet()) {
				Long money = ac154.player_moneys.get(key);
				ac9000001.player_moneys.put(key, money);
				ac9000002.player_moneys.put(key, money);
				msg = msg + "玩家ID["+key+"]" + "--["+money+"]" + "<br>";
			}
			out.println(msg);
			WeekAndMonthChongZhiActivity.diskcache.put(WeekAndMonthChongZhiActivity.DISK_KEY_MONEY
					+ ac9000001.getDataID(), ac9000001.player_moneys);
			WeekAndMonthChongZhiActivity.diskcache.put(WeekAndMonthChongZhiActivity.DISK_KEY_MONEY
					+ ac9000002.getDataID(), ac9000002.player_moneys);
		}
		
	%>
</body>
</html>
