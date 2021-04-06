<%@ page contentType="text/html;charset=utf-8"%><!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<%@page import="com.xuanzhi.boss.game.GameConstants"%>
<%@page import="java.util.Iterator"%>
<%@page import="com.fy.engineserver.economic.charge.ChargeManager"%>
<%@page import="java.util.*"%>
<%@page import="com.fy.engineserver.util.TimeTool"%>
<%@page import="com.fy.engineserver.gametime.SystemTime"%>
<%@page import="com.fy.engineserver.activity.newChongZhiActivity.NewMoneyActivity"%>
<%@page import="com.fy.engineserver.activity.newChongZhiActivity.NewChongZhiActivityManager"%>
<%@page import="com.fy.engineserver.activity.newChongZhiActivity.FirstChongZhiActivity"%><html xmlns="http://www.w3.org/1999/xhtml">
<head>
<META http-equiv=Content-Type content="text/html; charset=utf-8">
</style>
</HEAD>
<%
long yinzi = 50000L;
ArrayList<NewMoneyActivity> chongZhiActivitys  = NewChongZhiActivityManager.instance.chongZhiActivitys;
for(NewMoneyActivity a : chongZhiActivitys){
	if(a.isCanServer() && a instanceof FirstChongZhiActivity){
		FirstChongZhiActivity f = (FirstChongZhiActivity)a;
		long now = System.currentTimeMillis();
		if (now < f.getStartTimeLong() || now > f.getEndTimeLong()) {
			out.print("活动还未开始");
			return;
		}
		if (f.activityPlayers.contains(1209000000000001025L)) {
			out.print("参见过");
			return;
		}
		if (yinzi < f.getNeedMoney()) {
			out.print("钱不够");
			return;
		}
	}
}

%>