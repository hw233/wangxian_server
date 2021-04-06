<%@page import="java.text.SimpleDateFormat"%>
<%@page
	import="com.fy.engineserver.datasource.article.manager.ArticleManager"%>
<%@page
	import="com.fy.engineserver.datasource.article.data.articles.Article"%>
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
<%@page
	import="com.fy.engineserver.datasource.article.data.equipments.Equipment"%>
<%@page
	import="com.fy.engineserver.datasource.article.data.equipments.Weapon"%>
<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<%
	ShopManager sm = ShopManager.getInstance();
	Map<String, Shop> map = sm.getShops();
	//Set<String> error = new HashSet<String>();
	Map<String, String> errorGoods = new HashMap<String, String>();
	List<Goods> allGoods = new ArrayList<Goods>();
%>
<%!String[] timeType = { "无", "每天", "每周", "每月", "固定时间" };%>
<%!//String[] shopType = {"绑银商店","银子商店","工资商店","资源商店","挂机商店","历练商店","功勋商店","文采商店","兑换"};%>
<%!String[] shopType = { "绑银商店", "银子商店", "工资商店", "资源商店", "挂机商店", "历练商店", "功勋商店", "文采商店", "兑换商店", "商店银子", "活跃度积分", "积分商店" };%>


<%@page import="com.fy.engineserver.shop.Shop"%>
<%@page import="org.easymock.internal.matchers.InstanceOf"%><html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title>商店列表</title>
<link rel="stylesheet" type="text/css" href="../../css/table.css" />
<style type="text/css">
.color0 {
	background-color: #ffffff;
}

.color1 {
	background-color: #35ca41;
}

.color2 {
	background-color: #35b2fe;
}

.color3 {
	background-color: #8b50ed;
}

.color4 {
	background-color: orange;
}
</style>
</head>
<body>
<div>
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
<div style="float: left">
<table style="font-size: 12px;" border="1">
	<tr style="background-color: #83AAEB; font-weight: bold;">
		<td>商店名称</td>
	</tr>
	<%
		out.print("<tr><td><a href='?shopName=all'>显示全部</a></td><tr>");
		Set<String> shopNameSet = map.keySet();
		Iterator<String> shopNameIter = shopNameSet.iterator();
		while (shopNameIter.hasNext()) {
			String name = shopNameIter.next();
			out.print("<tr><td><a href='?shopName=" + name + "'>" + name + "</a></td><tr>");
		}
		out.print("</table>");
	%>
</table>
</div>
<div style="float: left; margin-left: 10px;">
<table style="font-size: 12px;" border="1">
	<tr style="background-color: #83AAEB; font-weight: bold;">
		<td>道具</td>
		<td>道具统计名</td>
		<td>颜色</td>
		<td>店内id</td>
		<td>原价</td>
		<td>卖价</td>
		<td>兑换所需物品/个数</td>
		<td>总限制</td>
		<td>个人限制</td>
		<td>限时类型</td>
		<td>限时开始</td>
		<td>限时结束</td>
	</tr>
	<%
		if ("all".equals(request.getParameter("shopName"))) {

			for (Iterator<String> itor = map.keySet().iterator(); itor.hasNext();) {
				String name = itor.next();
				Shop shop = map.get(name);
				if (shop == null) {
					continue;
				}
	%>
	<tr>
		<td colspan="10"
			style="color: blue; font-weight: bold; font-size: 16px; text-align: center;"><%=shop.getName()%>
		[<%=shopType[shop.shopType]%>] (<%=shop.getGoods().size()%>)</td>
	</tr>
	<%
		int color = 9;//设置一个大于所有颜色值的数，理论上这个数字在颜色里是不应该出现的
				List<Goods> list = shop.getGoods();
				List<Integer> gids = new ArrayList<Integer>();
				for (Goods g : list) {
					if (g.getColor() == 0) {
						allGoods.add(g);
					}
				}
				for (Goods g : list) {
					gids.add(g.getId());
					String articleName = g.getArticleName();
					Article article = ArticleManager.getInstance().getArticle(articleName);
					if (article == null) {
						//error.add(g.getArticleName());
						errorGoods.put(g.getArticleName(), "物品不存在");
					}
					boolean changePrice = g.getOldPrice() != g.getPrice();

					String ename = Arrays.toString(g.getExchangeArticleNames());
					String enumStr = Arrays.toString(g.getExchangeArticleNums());

					//根据商品颜色设置商品名称单元格的背景色
					int gColor = g.getColor();
					if (article instanceof Equipment || article instanceof Weapon) {
						if (gColor == 4) {
							color = 3;
						} else if (gColor == 5 || gColor == 6) {
							color = 4;
						} else {
							color = gColor;
						}
					} else {
						color = gColor;
					}

					//判断一个商品店内id出现的次数，超过两次的话就表示有重复
					int countId = 0;
					for (Integer id : gids) {
						if (id.intValue() == g.getId()) {
							countId++;
							if (countId > 1) errorGoods.put(articleName, "店内id重复");
						}
					}

					//比较高品质颜色的价格和用白色合成同等颜色的物品花费是否一致，不考虑武器和装备
					boolean dvalue = false;
					int prePrice = 0;
					if (gColor > 0 && !(article instanceof Equipment || article instanceof Weapon)) {
						for (Goods gg : allGoods) {
							if (g.getArticleName().equals(gg.getArticleName())) {
								prePrice = gg.getPrice() * (int) Math.pow(5, g.getColor());
								//dvalue = g.getPrice() < prePrice;
								if (g.getPrice() < prePrice) {
									dvalue = true;
									errorGoods.put(g.getArticleName(), "商品价格偏低");
								}
							}
						}
					}

					String start = "";
					String end = "";
					boolean saling = true;//当前是否可购买
					SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
					SimpleDateFormat hh = new SimpleDateFormat("hh");
					Calendar cal = Calendar.getInstance();
					Date date = new Date();
					switch (g.getTimeLimitType()) {
					case 0:
						break;
					case 1:
						start = "" + g.getEveryDayBeginLimit();//Translate.translateString(Translate.购买时间每天, new String[][] { { Translate.STRING_1, g.getEveryDayBeginLimit() + "" }, { Translate.STRING_2, g.getEveryDayEndLimit() + "" } });
						end = "" + g.getEveryDayEndLimit();
						if (hh.format(date).compareTo(end) > 0 || start.compareTo(hh.format(date)) > 0) saling = false;
						break;
					case 3:
						start = "" + g.getEveryMonthBeginLimit();
						end = "" + g.getEveryMonthEndLimit();
						if (cal.get(Calendar.DATE) < g.getEveryMonthBeginLimit() || cal.get(Calendar.DATE) > g.getEveryMonthEndLimit()) saling = false;
						break;
					case 2:
						start = "" + g.getEveryWeekBeginLimit();
						end = "" + g.getEveryWeekEndLimit();
						if (cal.get(Calendar.DAY_OF_WEEK) < g.getEveryWeekBeginLimit() || cal.get(Calendar.DAY_OF_WEEK) > g.getEveryWeekEndLimit()) saling = false;
						break;
					case 4:
						start = TimeTool.formatter.varChar19.format(g.getFixTimeBeginLimit());
						end = TimeTool.formatter.varChar19.format(g.getFixTimeEndLimit());
						if ((sdf.format(new Date()).compareTo(end)) > 0 || (start.compareTo(sdf.format(new Date()))) > 0) saling = false;
						break;
					}
	%>
	<tr
		style="color: <%=changePrice ? "red" : ""%>;background-color:<%=saling ? "" : "#e0e0e0"%>">
		<td class="color<%=color%>"><a
			href='ArticleByName.jsp?articleName=<%=g.getArticleName()%>'><%=g.getArticleName()%></a><%=shop.isBuyBinded() ? "(绑)" : ""%></td>
		<td class="color<%=color%>"><a
			href='ArticleByName.jsp?articleCNName=<%=g.getArticleName_stat()%>'><%=g.getArticleName_stat()%></a><%=shop.isBuyBinded() ? "(绑)" : ""%></td>
		<td><%=g.getColor()%></td>
		<td style="background-color:<%=countId < 2 ? "" : "red"%>"><%=g.getId()%></td>
		<%
			if (g.getPayType() == 3 || g.getPayType() == 5 || g.getPayType() == 6 || g.getPayType() == 7 || g.getPayType() == 8) {
		%>
		<td><%=g.getOldPrice()%></td>
		<td style="background-color:<%=dvalue ? "pink" : ""%>"><%=g.getPrice()%><%=dvalue ? "/" + prePrice : ""%></td>
		<%
			} else {
		%>
		<td><%=g.getOldPrice() + "/" + BillingCenter.得到带单位的银两(g.getOldPrice())%></td>
		<td style="background-color:<%=dvalue ? "pink" : ""%>"><%=g.getPrice() + "/" + BillingCenter.得到带单位的银两(g.getPrice())%><%=dvalue ? "(" + BillingCenter.得到带单位的银两(prePrice) + ")" : ""%></td>
		<%
			}
		%>
		<%
			if (g.getPayType() == 3 || g.getPayType() == 8) {
		%>
		<td><%=ename%>/<%=enumStr%></td>
		<%
			} else {
		%>
		<td>--/--</td>
		<%
			}
		%>
		<td><%=g.getTotalSellNumLimit() == 0 ? "--" : g.getTotalSellNumLimit()%></td>
		<td><%=g.getPersonSellNumLimit() == 0 ? "--" : g.getPersonSellNumLimit()%></td>
		<td><%=timeType[g.getTimeLimitType()]%></td>
		<td><%=start%></td>
		<td><%=end%></td>
	</tr>
	<%}}}else{
		Shop shop=map.get(request.getParameter("shopName"));
		if(shop!=null){
			%>
			<tr>
		<td colspan="10"
			style="color: blue; font-weight: bold; font-size: 16px; text-align: center;"><%=shop.getName()%>
		[<%=shopType[shop.shopType]%>] (<%=shop.getGoods().size()%>)</td>
	</tr>
	<%
		int color = 9;//设置一个大于所有颜色值的数，理论上这个数字在颜色里是不应该出现的
				List<Goods> list = shop.getGoods();
				List<Integer> gids = new ArrayList<Integer>();
				for (Goods g : list) {
					if (g.getColor() == 0) {
						allGoods.add(g);
					}
				}
				for (Goods g : list) {
					gids.add(g.getId());
					String articleName = g.getArticleName();
					Article article = ArticleManager.getInstance().getArticle(articleName);
					if (article == null) {
						//error.add(g.getArticleName());
						errorGoods.put(g.getArticleName(), "物品不存在");
					}
					boolean changePrice = g.getOldPrice() != g.getPrice();

					String ename = Arrays.toString(g.getExchangeArticleNames());
					String enumStr = Arrays.toString(g.getExchangeArticleNums());

					//根据商品颜色设置商品名称单元格的背景色
					int gColor = g.getColor();
					if (article instanceof Equipment || article instanceof Weapon) {
						if (gColor == 4) {
							color = 3;
						} else if (gColor == 5 || gColor == 6) {
							color = 4;
						} else {
							color = gColor;
						}
					} else {
						color = gColor;
					}

					//判断一个商品店内id出现的次数，超过两次的话就表示有重复
					int countId = 0;
					for (Integer id : gids) {
						if (id.intValue() == g.getId()) {
							countId++;
							if (countId > 1) errorGoods.put(articleName, "店内id重复");
						}
					}

					//比较高品质颜色的价格和用白色合成同等颜色的物品花费是否一致，不考虑武器和装备
					boolean dvalue = false;
					int prePrice = 0;
					if (gColor > 0 && !(article instanceof Equipment || article instanceof Weapon)) {
						for (Goods gg : allGoods) {
							if (g.getArticleName().equals(gg.getArticleName())) {
								prePrice = gg.getPrice() * (int) Math.pow(5, g.getColor());
								//dvalue = g.getPrice() < prePrice;
								if (g.getPrice() < prePrice) {
									dvalue = true;
									errorGoods.put(g.getArticleName(), "商品价格偏低");
								}
							}
						}
					}

					String start = "";
					String end = "";
					boolean saling = true;//当前是否可购买
					SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
					SimpleDateFormat hh = new SimpleDateFormat("hh");
					Calendar cal = Calendar.getInstance();
					Date date = new Date();
					switch (g.getTimeLimitType()) {
					case 0:
						break;
					case 1:
						start = "" + g.getEveryDayBeginLimit();//Translate.translateString(Translate.购买时间每天, new String[][] { { Translate.STRING_1, g.getEveryDayBeginLimit() + "" }, { Translate.STRING_2, g.getEveryDayEndLimit() + "" } });
						end = "" + g.getEveryDayEndLimit();
						if (hh.format(date).compareTo(end) > 0 || start.compareTo(hh.format(date)) > 0) saling = false;
						break;
					case 3:
						start = "" + g.getEveryMonthBeginLimit();
						end = "" + g.getEveryMonthEndLimit();
						if (cal.get(Calendar.DATE) < g.getEveryMonthBeginLimit() || cal.get(Calendar.DATE) > g.getEveryMonthEndLimit()) saling = false;
						break;
					case 2:
						start = "" + g.getEveryWeekBeginLimit();
						end = "" + g.getEveryWeekEndLimit();
						if (cal.get(Calendar.DAY_OF_WEEK) < g.getEveryWeekBeginLimit() || cal.get(Calendar.DAY_OF_WEEK) > g.getEveryWeekEndLimit()) saling = false;
						break;
					case 4:
						start = TimeTool.formatter.varChar19.format(g.getFixTimeBeginLimit());
						end = TimeTool.formatter.varChar19.format(g.getFixTimeEndLimit());
						if ((sdf.format(new Date()).compareTo(end)) > 0 || (start.compareTo(sdf.format(new Date()))) > 0) saling = false;
						break;
					}
	%>
	<tr
		style="color: <%=changePrice ? "red" : ""%>;background-color:<%=saling ? "" : "#e0e0e0"%>">
		<td class="color<%=color%>"><a
			href='ArticleByName.jsp?articleName=<%=g.getArticleName()%>'><%=g.getArticleName()%></a><%=shop.isBuyBinded() ? "(绑)" : ""%></td>
		<td class="color<%=color%>"><a
			href='ArticleByName.jsp?articleCNName=<%=g.getArticleName_stat()%>'><%=g.getArticleName_stat()%></a><%=shop.isBuyBinded() ? "(绑)" : ""%></td>
		<td><%=g.getColor()%></td>
		<td style="background-color:<%=countId < 2 ? "" : "red"%>"><%=g.getId()%></td>
		<%
			if (g.getPayType() == 3 || g.getPayType() == 5 || g.getPayType() == 6 || g.getPayType() == 7 || g.getPayType() == 8) {
		%>
		<td><%=g.getOldPrice()%></td>
		<td style="background-color:<%=dvalue ? "pink" : ""%>"><%=g.getPrice()%><%=dvalue ? "/" + prePrice : ""%></td>
		<%
			} else {
		%>
		<td><%=g.getOldPrice() + "/" + BillingCenter.得到带单位的银两(g.getOldPrice())%></td>
		<td style="background-color:<%=dvalue ? "pink" : ""%>"><%=g.getPrice() + "/" + BillingCenter.得到带单位的银两(g.getPrice())%><%=dvalue ? "(" + BillingCenter.得到带单位的银两(prePrice) + ")" : ""%></td>
		<%
			}
		%>
		<%
			if (g.getPayType() == 3 || g.getPayType() == 8) {
		%>
		<td><%=ename%>/<%=enumStr%></td>
		<%
			} else {
		%>
		<td>--/--</td>
		<%
			}
		%>
		<td><%=g.getTotalSellNumLimit() == 0 ? "--" : g.getTotalSellNumLimit()%></td>
		<td><%=g.getPersonSellNumLimit() == 0 ? "--" : g.getPersonSellNumLimit()%></td>
		<td><%=timeType[g.getTimeLimitType()]%></td>
		<td><%=start%></td>
		<td><%=end%></td>
	</tr>
	<%}}} %>
</table>
</div>
<div style="float: left; margin-left: 20px;">
<table style="font-size: 12px;" border="1">
	<tr style="background-color: #83AAEB; font-weight: bold;">
		<td>商品名</td>
		<td>问题</td>
	</tr>
	<%
		Set<String> ss = errorGoods.keySet();
		Iterator<String> it = ss.iterator();
		while (it.hasNext()) {
			String name = it.next();
			out.print("<tr><td>" + name + "</td><td>" + errorGoods.get(name) + "</td></tr>");
		}
	%>
</table>
<div>
</div>
</div>
<!-- <div style="background-color: red;">	
	<HR>
	 不存在的物品:
	<HR>
	
		
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
	 --></div>
</body>
</html>