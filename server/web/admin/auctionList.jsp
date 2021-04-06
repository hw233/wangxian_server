<%@page import="com.fy.engineserver.datasource.article.manager.ArticleManager"%>
<%@page import="com.fy.engineserver.datasource.article.data.articles.Article"%>
<%@page import="java.util.ArrayList"%>
<%@page import="com.fy.engineserver.datasource.article.manager.ArticleEntityManager"%>
<%@page import="com.fy.engineserver.datasource.article.data.entity.ArticleEntity"%>
<%@page import="com.fy.engineserver.economic.BillingCenter"%>
<%@page import="java.util.Hashtable"%>
<%@page import="com.fy.engineserver.sprite.PlayerManager"%>
<%@page import="java.util.Iterator"%>
<%@page import="java.util.List"%>
<%@page import="java.util.HashMap"%>
<%@page import="com.fy.engineserver.auction.Auction"%>
<%@page import="com.fy.engineserver.auction.service.AuctionManager"%>
<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%
	Hashtable<Long, Auction> underRuleMap = AuctionManager.getInstance().getAuctionMap();
%>
<%
	String action = request.getParameter("action");
	if (action != null) {
		if ("xiuzheng".equals(action)) {
			ArrayList<Auction> de = new ArrayList<Auction>();
			for (Long key : underRuleMap.keySet()) {
				Auction a = underRuleMap.get(key);
				ArticleEntity entity = ArticleEntityManager.getInstance().getEntity(a.getArticleEntityId());
				if (entity == null) {
					de.add(a);
				}
			}
			for (Auction a : de) {
				AuctionManager.getInstance().em.remove(a);
				underRuleMap.remove(a.getId());
				Article article = ArticleManager.getInstance().getArticle(a.getName());
				AuctionManager.getInstance().getAuctionMap4Article().get(article).remove(a);
				out.println(a.getId() + "  " + a.getName() + "  " + a.getArticleEntityId());
				out.println("<br>");
			}
		}
	}
%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title>删除拍卖纪录中不存在的物品</title>
</head>
<body>

删除拍卖纪录中不存在的物品
<form name='f1'>
	<input type="hidden" name="action" value="xiuzheng">
	<input type="submit" value="确定">
</form>
<br>

<table border="1">
	<tr>
		<td><%="手续费                    " %></td>
		<td><%="单子上的钱          " %></td>
		<td><%="拍卖者得到的钱" %></td>
		<td><%="退竞拍者的钱     " %></td>
		<td><%="竞拍花的钱          " %></td>
		<td><%="税                              " %></td>
		<td><%="取消拍卖违约金" %></td>
	</tr>
	<tr>
		<td><%=BillingCenter.得到带单位的银两(AuctionManager.getInstance().auctionMoney) %></td>
		<td><%=BillingCenter.得到带单位的银两(AuctionManager.getInstance().haveMoney) %></td>
		<td><%=BillingCenter.得到带单位的银两(AuctionManager.getInstance().saleMoney) %></td>
		<td><%=BillingCenter.得到带单位的银两(AuctionManager.getInstance().backJingpaiMoney) %></td>
		<td><%=BillingCenter.得到带单位的银两(AuctionManager.getInstance().jingpaiMoney) %></td>
		<td><%=BillingCenter.得到带单位的银两(AuctionManager.getInstance().taxMoney) %></td>
		<td><%=BillingCenter.得到带单位的银两(AuctionManager.getInstance().cancleMoney) %></td>
	</tr>
	
</table>

<table border="1">
	<tr>
		<td><%="启动时库里单数" %></td>
		<td><%="总共单数" %></td>
		<td><%="新加单数" %></td>
		<td><%="一口价单数" %></td>
		<td><%="过期单数" %></td>
	</tr>
	<tr>
		<td><%=AuctionManager.getInstance().oldSize %></td>
		<td><%=AuctionManager.getInstance().haveSize %></td>
		<td><%=AuctionManager.getInstance().addSize %></td>
		<td><%=AuctionManager.getInstance().yikoujiaSize %></td>
		<td><%=AuctionManager.getInstance().timeOverSize %></td>
	</tr>
</table>

<table border="1">

		<tr>
		<td>rb.getId() </td>
		<td>rb.getName() </td>
		<td>rb.getSellName() </td>
		<td>rb.getCount()</td>
		<td>rb.getStartPrice()</td>
		<td>rb.getNowPrice()</td>
		<td>rb.getBuyPrice()</td>
		<td>rb.getAtype()</td>
		<td>rb.getAsubtype()</td>
		<td>rb.getEndDate()</td>
		<td>最高竞拍者</td>
	</tr>

	<%
		for (Iterator<Auction> iterator = underRuleMap.values().iterator(); iterator.hasNext();) {

			Auction rb = iterator.next();
			String name = "";
			if (rb.getMaxPricePlayer() > 0) {
				name = PlayerManager.getInstance().getPlayer(rb.getMaxPricePlayer()).getName();
			}
	%>
	<tr>
		<td><%=rb.getId() %></td>
		<td><%=rb.getName() %></td>
		<td><%=rb.getSellName() %></td>
		<td><%=rb.getCount() %></td>
		<td><%=rb.getStartPrice() %></td>
		<td><%=rb.getNowPrice() %></td>
		<td><%=rb.getBuyPrice() %></td>
		<td><%=rb.getAtype() %></td>
		<td><%=rb.getAsubtype() %></td>
		<td><%=rb.getEndDate() %></td>
		<td><%=name %></td>
	</tr>
	<%
		}
	%>
	</table>
</body>
</html>