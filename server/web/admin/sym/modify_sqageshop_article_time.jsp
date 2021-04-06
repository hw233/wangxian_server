<%@page import="java.sql.Timestamp"%>
<%@page import="java.util.Calendar"%>
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
        String startStr = "2015-09-24-00-00";
		String[] times = startStr.split("-");
		Calendar cal = Calendar.getInstance();
		cal.set(Integer.parseInt(times[0]), Integer.parseInt(times[1])-1, Integer.parseInt(times[2]), Integer.parseInt(times[3]), Integer.parseInt(times[4]),0);
        if (shop == null) {
             out.print("商店："+shopName + ",不存在");
             return;
        }
        //11-31
        int baseID = 10000;
		for(int i=10;i<=31;i++){
			Goods good = shop.getGoodsById(i);
			good.setFixTimeBeginLimit(cal.getTimeInMillis());
			out.println("[商店名:" + shop.getName() + "] [商品名:" + good.getArticleName() + "] [物品id:" + good.getId() + "] [fixTimeBegin:"+(new Timestamp(good.getFixTimeBeginLimit()))+"]<br>");
		}
        
		
%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title>修改商店物品时间</title>
</head>
<body>

</body>
</html>