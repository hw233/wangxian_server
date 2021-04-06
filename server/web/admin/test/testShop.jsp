<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">


<%@page import="java.util.Map"%>
<%@page import="com.fy.engineserver.shop.Shop"%>
<%@page import="com.fy.engineserver.shop.ShopManager"%>
<%@page import="java.util.ArrayList"%>
<%@page import="java.util.List"%>
<%@page import="java.util.Iterator"%>
<%@page import="com.fy.engineserver.shop.Goods"%>
<%@page import="com.fy.engineserver.util.datacheck.handler.GoodsPriceLimit"%>
<%@page import="com.fy.engineserver.util.datacheck.handler.GoodsPriceLimitManager"%>
<%@page import="com.fy.engineserver.util.datacheck.MailEventManger"%>
<%@page import="com.fy.engineserver.util.datacheck.event.MailEvent"%><html>
<%
	Map<String, Shop> shops = ShopManager.getInstance().getShops();
	List<String[]> sendMailList = new ArrayList<String[]>();
	for (Iterator<String> itor = shops.keySet().iterator(); itor.hasNext();) {
		String name = itor.next();
		Shop shop = shops.get(name);
		if (shop == null) {
			continue;
		}
		List<Goods> list = shop.getGoods();
		for (Goods g : list) {
			if (g == null) {
				out.print("商品为空");
				continue;
			}
			// 判断数量不超过上限
			if ((Math.ceil(Integer.MAX_VALUE / g.getPrice())) < g.getGoodMaxNumLimit()) {
				sendMailList.add(new String[] { shop.getName_stat(), g.getArticleName_stat(), g.getColor() + "", "单次可购买数量超过上限", "上限(" + Math.ceil(Integer.MAX_VALUE / g.getPrice()) + ")可购(" + g.getGoodMaxNumLimit() + ")" });
			}
			// 判断价格是否高于最低折扣价
			GoodsPriceLimit gpl = GoodsPriceLimitManager.getInstance().getGoodPriceLimit(g.getArticleName_stat(), g.getColor());
			if (gpl != null) {
				if ((g.getPrice() / g.getBundleNum()) < gpl.getLimitPrice() * 1000) {
					sendMailList.add(new String[] { shop.getName_stat(), g.getArticleName_stat(), g.getColor() + "", "价格低于最低折扣价", "最低价(" + gpl.getLimitPrice() + ")现价(" + g.getPrice() / g.getBundleNum() + ")" });
					out.print("[商店检查] [" + g.getArticleName_stat() + "单价:" + g.getPrice() / g.getBundleNum() + "最低折扣价:" + gpl.getLimitPrice() * 1000 + "]");
					out.print("[商店检查] [价格低于最低折扣价]<br>");
				}
			}
		}
	}
	if (sendMailList.size() > 0) {
		String[][] sendMailArr = new String[sendMailList.size()][5];
		for (int i = 0; i < sendMailList.size(); i++) {
			sendMailArr[i] = sendMailList.get(i);
		}
		MailEventManger.getInstance().addTask(MailEvent.商店检查.立即发送(sendMailArr));
		out.print("发送邮件");
	}
%>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title>商店检查</title>
</head>
<body>

</body>
</html>