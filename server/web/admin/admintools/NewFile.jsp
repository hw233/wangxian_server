<%@page import="com.fy.engineserver.economic.BillingCenter"%>
<%@page import="com.fy.engineserver.shop.Goods"%>
<%@page import="java.util.List"%>
<%@page import="java.util.LinkedHashMap"%>
<%@page import="com.fy.engineserver.shop.ShopManager"%>
<%@page import="com.fy.engineserver.shop.Shop"%>
<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">

<%
	LinkedHashMap<String, Shop> shops = ShopManager.getInstance().getShops();
	Shop shop = shops.get("全部道具");
	List<Goods> list = shop.getGoods();
	Goods g = null;
	for (Goods temp : list) {
		if (temp.getArticleName().equals("灵兽内丹")) {
			if(temp.getColor() == 4) {
				temp.setPrice(21000000);
				out.print("设置OK,价格:" + BillingCenter.getInstance().得到带单位的银两(temp.getPrice()));
			}
		}
	}
%>
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title>Insert title here</title>
</head>
<body>

</body>
</html>