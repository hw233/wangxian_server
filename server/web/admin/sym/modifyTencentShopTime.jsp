<%@page import="java.util.Calendar"%>
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
        String shopName = "限时抢购";
        Shop shop = ShopManager.getInstance().getShops().get(shopName);
        if (shop == null) {
             out.print("商店："+shopName + ",不存在");
             return;
        }
        
        List<Goods> gs =  shop.getGoods();
        for(Goods g:gs){
        	if (g.getArticleName().equals("汉钟离赐福锦囊")) {
        		String startStr = "2015-01-01-00-00";
				String[] times = startStr.split("-");
				Calendar cal = Calendar.getInstance();
				cal.set(Integer.parseInt(times[0]), Integer.parseInt(times[1])-1, Integer.parseInt(times[2]), Integer.parseInt(times[3]), Integer.parseInt(times[4]),0);
        		g.setFixTimeBeginLimit(cal.getTimeInMillis());
        		out.println("商品:" + g.getArticleName() + " 限时抢购开启时间修改为:" + startStr + "<br>");
        	} else if ("大鹏骨".equals(g.getArticleName())) {
        		String startStr = "2014-12-25-00-00";
				String[] times = startStr.split("-");
				Calendar cal = Calendar.getInstance();
				cal.set(Integer.parseInt(times[0]), Integer.parseInt(times[1])-1, Integer.parseInt(times[2]), Integer.parseInt(times[3]), Integer.parseInt(times[4]),0);
        		g.setFixTimeBeginLimit(cal.getTimeInMillis());
        		out.println("商品:" + g.getArticleName() + " 限时抢购开启时间修改为:" + startStr + "<br>");
        	} else if ("羽翼碎片".equals(g.getArticleName())) {
        		String startStr = "2014-12-25-00-00";
				String[] times = startStr.split("-");
				Calendar cal = Calendar.getInstance();
				cal.set(Integer.parseInt(times[0]), Integer.parseInt(times[1])-1, Integer.parseInt(times[2]), Integer.parseInt(times[3]), Integer.parseInt(times[4]),0);
        		g.setFixTimeBeginLimit(cal.getTimeInMillis());
        		out.println("商品:" + g.getArticleName() + " 限时抢购开启时间修改为:" + startStr + "<br>");
        	}
        }
        
        
%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title>修改限时抢购商店物品时间</title>
</head>
<body>

</body>
</html>