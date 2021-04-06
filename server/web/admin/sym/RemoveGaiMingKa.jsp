<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">

<%@page import="com.fy.engineserver.shop.ShopManager"%>
<%@page import="java.util.LinkedHashMap"%>
<%@page import="com.fy.engineserver.shop.Shop"%>
<%@page import="java.util.List"%>
<%@page import="com.fy.engineserver.shop.Goods"%><html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title>Insert title here</title>
</head>
<body>

<%
LinkedHashMap<String,Shop> shops=ShopManager.getInstance().getShops();
Shop shop=shops.get("积分商城");
List<Goods> goods=shop.getGoods();
Goods removeGoods=null;
for(Goods g:goods){
	if(g.getArticleName().equals("改名卡")){
		removeGoods=g;
	}
}
goods.remove(removeGoods);
out.print("后台移除积分商城的改名卡");
ShopManager.logger.warn("后台移除积分商城的改名卡");

%>

</body>
</html>