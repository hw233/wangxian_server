<%@page import="java.text.SimpleDateFormat"%>
<%@page import="com.fy.engineserver.datasource.article.manager.ArticleManager"%>
<%@page import="com.fy.engineserver.datasource.article.data.articles.Article"%>
<%@page import="java.util.HashSet"%>
<%@page import="com.fy.engineserver.util.TimeTool"%>
<%@page import="com.fy.engineserver.datasource.language.Translate"%>
<%@page import="com.fy.engineserver.economic.BillingCenter"%>
<%@page import="com.fy.engineserver.shop.Goods"%>
<%@page import="java.util.List"%>
<%@page import="java.util.Iterator"%>
<%@page import="com.fy.engineserver.shop.Shop"%>
<%@page import="java.util.*"%>
<%@page import="com.fy.engineserver.shop.ShopManager"%>
<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<%
	ShopManager sm = ShopManager.getInstance();
	Map<String, Shop> map = sm.getShops();
	Set<String> error = new HashSet<String>();
%>
<%!String[] timeType = { "无", "每天", "每周", "每月", "固定时间" };%>
<%!//String[] shopType = {"绑银商店","银子商店","工资商店","资源商店","挂机商店","历练商店","功勋商店","文采商店","兑换"};%>
<%!String[] shopType = {"绑银商店","银子商店","工资商店","资源商店","挂机商店","历练商店","功勋商店","文采商店","兑换商店","商店银子","活跃度积分","积分商店","","战勋商店","充值商店","","跨服商店"};%>
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title>Insert title here</title>
</head>
<body>
	<%
		String action = request.getParameter("action");
		if (action != null) {
			if ("chanagerShopTime".equals(action)) {
				String shopName = request.getParameter("shopName");
				String propName = request.getParameter("propName");
				String startTime = request.getParameter("startTime");
				String endTime = request.getParameter("endTime");
				SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
				long sTime = format.parse(startTime).getTime();
				long eTime = format.parse(endTime).getTime();
				Shop shop = map.get(shopName);
				if (shop != null) {
					List<Goods> list = shop.getGoods();
					for (Goods g : list) {
						String articleName = g.getArticleName();
						if (articleName.equals(propName)) {
							g.setFixTimeBeginLimit(sTime);
							g.setFixTimeEndLimit(eTime);
						}
					}
				}
			}
		}
	%>

	<table style="font-size: 12px;" border="1">
		<tr style="background-color: #83AAEB;font-weight: bold;">
			<td>道具</td>
			<td>道具统计名</td>
			<td>颜色</td>
			<td>原价</td>
			<td>卖价</td>
			<td>总限制</td>
			<td>个人限制</td>
			<td>限时类型</td>
			<td>限时开始</td>
			<td>限时结束</td>
		</tr>
		<%
			for (Iterator<String> itor = map.keySet().iterator(); itor.hasNext();) {
				String name = itor.next();
				Shop shop = map.get(name);
				if (shop == null) {
					continue;
				}
				try {
					shop.isOpen();
				} catch (Exception e) {
					out.print(shop.getName() + "-----------------------<BR>");
				}
		%>
		<tr>
			<td colspan="9" style="color: blue; font-weight: bold;font-size: 16px;text-align: center;"><%=shop.getName()%> [<%=shopType[shop.shopType] %>] (<%=shop.getGoods().size() %>)</td>
		</tr>
		<%
			List<Goods> list = shop.getGoods();
				for (Goods g : list) {
					String articleName = g.getArticleName();
					Article article = ArticleManager.getInstance().getArticle(articleName);
					if (article == null) {
						error.add(g.getArticleName());
					}
					boolean changePrice = g.getOldPrice() != g.getPrice();
					String start = "";
					String end = "";
					switch (g.getTimeLimitType()) {
					case 0:
						break;
					case 1:
						start = "" + g.getEveryDayBeginLimit();//Translate.translateString(Translate.购买时间每天, new String[][] { { Translate.STRING_1, g.getEveryDayBeginLimit() + "" }, { Translate.STRING_2, g.getEveryDayEndLimit() + "" } });
						end = "" + g.getEveryDayEndLimit();
						break;
					case 3:
						start = "" + g.getEveryMonthBeginLimit();
						end = "" + g.getEveryMonthEndLimit();
						break;
					case 2:
						start = "" + g.getEveryWeekBeginLimit();
						end = "" + g.getEveryWeekEndLimit();
						break;
					case 4:
						start = TimeTool.formatter.varChar19.format(g.getFixTimeBeginLimit());
						end = TimeTool.formatter.varChar19.format(g.getFixTimeEndLimit());
						break;
					}
		%>
		<tr style="color: <%=changePrice ? "red" : ""%>">
			<td><%=g.getArticleName()%></td>
			<td><%=g.getArticleName_stat()%></td>
			<td><%=g.getColor()%></td>
			<td><%=g.getOldPrice() + "/" + BillingCenter.得到带单位的银两(g.getOldPrice())%></td>
			<td><%=g.getPrice() + "/" + BillingCenter.得到带单位的银两(g.getPrice())%></td>
			<td><%=g.getTotalSellNumLimit() == 0 ? "--" : g.getTotalSellNumLimit()%></td>
			<td><%=g.getPersonSellNumLimit() == 0 ? "--" : g.getPersonSellNumLimit()%></td>
			<td><%=timeType[g.getTimeLimitType()]%></td>
			<td><%=start%></td>
			<td><%=end%></td>
		</tr>
		<%
			}
			}
		%>
	</table>
<div style="background-color: red;">	
	<HR>
	 不存在的物品:
	<HR>
	
		<% for (String er: error) {
			out.print(er + "<br>");
			} %>
	<HR>
	</div>
	
	<form name="f1">
		<input type="hidden" name="action" value="chanagerShopTime">
		<input type="text" name="shopName">
		<input type="text" name="propName">
		<input type="text" name="startTime" value="2013-02-01 10:00:00">
		<input type="text" name="endTime" value="2013-02-02 10:00:00">
		<input type="submit" name="确定">
	</form>
</body>
</html>