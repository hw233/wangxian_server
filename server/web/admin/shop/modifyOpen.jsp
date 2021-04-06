<%@page import="com.fy.engineserver.util.TimeTool"%>
<%@page import="com.fy.engineserver.shop.Goods"%>
<%@page import="com.fy.engineserver.shop.ShopManager"%>
<%@page import="com.fy.engineserver.shop.Shop"%>
<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<%
	String shopName = "活跃商城";
	String timeLimits = "2013-09-30-00-00#2513-11-31-23-59";
	Shop shop = ShopManager.getInstance().getShops().get(shopName);
	if (shop != null) {
		shop.setTimelimits(timeLimits);
		for (Goods g : shop.getGoods()) {
			g.setFixTimeEndLimit(TimeTool.formatter.varChar19.parse("2513-11-30 23:59:59"));
		}
		out.print("<H2>商店[" + shopName + "]时间限制设置完毕:[" + shop.getTimelimits()  + "]是否有效:[" + shop.isOpen().toString() + "]</H2>");
	} else {
		out.print("<H1>商店不存在:" + shopName + "</H1>");
	}
%>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title>开启活跃度商城</title>
</head>
<body>

</body>
</html>