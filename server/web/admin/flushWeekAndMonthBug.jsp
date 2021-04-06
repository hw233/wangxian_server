<%@page import="java.util.Arrays"%>
<%@page import="com.fy.engineserver.activity.newChongZhiActivity.WeekAndMonthChongZhiActivity"%>
<%@page import="java.util.ArrayList"%>
<%@page import="com.fy.engineserver.activity.newChongZhiActivity.NewChongZhiActivityManager"%>
<%@page import="com.fy.engineserver.smith.ProcessCat"%>
<%@page import="com.fy.engineserver.qiancengta.QianCengTa_Dao"%>
<%@page import="com.fy.engineserver.qiancengta.QianCengTa_Ceng"%>
<%@page import="com.fy.engineserver.authority.AuthorityAgent"%>
<%@page import="com.fy.engineserver.qiancengta.QianCengTa_Ta"%>
<%@page import="com.fy.engineserver.qiancengta.QianCengTaManager"%>
<%@page import="com.fy.engineserver.sprite.PlayerManager"%>
<%@page import="com.fy.engineserver.sprite.Player"%>
<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title>查看周月活动</title>
</head>
<body>
<%

	out.println("<table border='1'>");
	for (WeekAndMonthChongZhiActivity ac : NewChongZhiActivityManager.instance.weekMonthDatas) {
	out.println("<tr>");
		out.println("<td>活动ID:"+ac.getDataID()+"</td><td>开始时间:"+ac.getStartTime()+"</td><td>结束时间"+ac.getEndTime());
		out.println("</td><td>活动类型:"+(ac.getType()==0?"周礼包":"月礼包")+"</td><td>奖励物品:"+Arrays.toString(ac.getRewardPropNames()));
	out.println("<tr>");
	}
	out.println("</table>");
%>

</body>
</html>