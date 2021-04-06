<%@page import="com.fy.engineserver.activity.activeness.ActivenessManager"%>
<%@page import="java.util.Arrays"%>
<%@page import="com.fy.engineserver.menu.Option_ExchangeSliver_Salary"%>
<%@page import="com.fy.engineserver.menu.Option"%>
<%@page import="com.fy.engineserver.menu.MenuWindow"%>
<%@page import="com.fy.engineserver.menu.WindowManager"%>
<%@page import="java.util.ArrayList"%>
<%@page import="com.fy.engineserver.util.TimeTool"%>
<%@page import="java.util.List"%>
<%@page import="com.fy.engineserver.shop.Goods"%>
<%@page import="com.fy.engineserver.shop.Shop"%>
<%@page import="com.fy.engineserver.shop.ShopManager"%>
<%@ page language="java" contentType="text/html; charset=UTF-8"
        pageEncoding="UTF-8"%>
<%
        String shopName = "活跃商城";
        Shop shop = ShopManager.getInstance().getShops().get(shopName);
        if (shop == null) {
             out.print("商店："+shopName + ",不存在");
             return;
        }
        
        shop.setTimelimits("2014-01-06-00-00#2114-01-25-23-59");
        
        List<Goods> gs =  shop.getGoods();
        for(Goods g:gs){
        	g.setTimeLimitType(0);
        }
        
		out.print("修改成功"+gs.size());
        
%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title>修改商店物品时间和转化卡</title>
</head>
<body>

</body>
</html>