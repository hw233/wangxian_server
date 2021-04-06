<%@ page contentType="text/html;charset=utf-8" import="java.util.*,
											com.fy.engineserver.shop.*,
											com.fy.engineserver.sprite.*,
											
											com.xuanzhi.tools.text.*,
											com.xuanzhi.boss.game.*"%>
<%@include file="../IPManager.jsp" %>
<%@page import="com.fy.engineserver.datasource.article.manager.ArticleManager"%>
<%@page import="com.fy.engineserver.datasource.article.data.articles.Article"%>
<%@page import="com.fy.engineserver.economic.CurrencyType"%><html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8">
<title></title>
<link rel="stylesheet" href="../../css/common.css"/>
<link rel="stylesheet" href="../../css/table.css"/>
<style type="text/css">
.titlecolor{
background-color:#C2CAF5;
}

</style>
<script language="JavaScript">
<!--
function subcheck() {
	f1.submit();
}
-->
</script>
<script type="text/javascript">
function buyArticles(mapStr,index){
	var idStr = mapStr + index;
	document.getElementById("articleCount").value = document.getElementById(idStr).value;
	document.getElementById("mapStr").value = mapStr;
	document.getElementById("articleIndex").value = index;
	document.form1.submit();
}
</script>
</head>
<body>

<br>
<%
PlayerManager pm = PlayerManager.getInstance();
String action = request.getParameter("action");
ShopManager sm = ShopManager.getInstance();
Map<String,Shop> map = sm.getShops();
long playerId = -1;
Player player = null;
//服务器类型为0代表为可修改的开发服务器
if(GameConstants.getInstance().getServerType() == 0){

Object obj = session.getAttribute("playerid");
if(obj != null){
	playerId = Long.parseLong(obj.toString());
}
String errorMessage = null;

if (action == null && playerId != -1) {
	if (errorMessage == null) {
		player = pm.getPlayer(playerId);
		if (player == null) {
			errorMessage = "ID为" + playerId + "的玩家不存在！";
		}
	}
}else if (action != null && action.equals("login")) {
	try {
		playerId = Long.parseLong(request
				.getParameter("playerid"));
	} catch (Exception e) {
		errorMessage = "玩家ID必须是数字，不能含有非数字的字符";
	}
	if (errorMessage == null) {
		player = pm.getPlayer(playerId);
		if (player == null) {
			errorMessage = "ID为" + playerId + "的玩家不存在！";
		}else{
			session.setAttribute("playerid",request.getParameter("playerid"));
		}
	}
}else if (action != null && action.equals("buyArticle")) {
	try {
		playerId = Long.parseLong(request
				.getParameter("playerid"));
	} catch (Exception e) {
		errorMessage = "玩家ID必须是数字，不能含有非数字的字符";
	}
	if (errorMessage == null) {
		player = pm.getPlayer(playerId);
		if (player == null) {
			errorMessage = "ID为" + playerId + "的玩家不存在！";
		}else{
			session.setAttribute("playerid",request.getParameter("playerid"));
		}
		int articleCount = 0;
			try {
				articleCount = Integer.parseInt(request
					.getParameter("articleCount"));
			}catch(Exception e){
				errorMessage = "购买数量，不能含有非数字的字符";
			}
			if (errorMessage == null) {
				try {
					String mapStr = request.getParameter("mapStr");
					int articleIndex = Integer.parseInt(request.getParameter("articleIndex"));
					if(mapStr != null){
						Shop shop = sm.getShop(mapStr,null);
						if(shop != null){
							//shop.buyGoods(player,articleIndex,articleCount);
						}else{
							errorMessage = "没有名为"+mapStr+"的商店";
						}
					}else{
						errorMessage = "购买商品出问题，请再次购买，如果仍然有问题，请联系管理员";
					}
				}catch(Exception e){
					errorMessage = "购买数量，不能含有非数字的字符";
				}
			}
	}
} 
if (errorMessage != null) {
	out.println("<center><table border='0' cellpadding='0' cellspacing='2' width='100%' bgcolor='#FF0000' align='center'>");
	out.println("<tr bgcolor='#FFFFFF' align='center'><td>");
	out.println("<font color='red'><pre>" + errorMessage + "</pre></font>");
	out.println("</td></tr>");
	out.println("</table></center>");
}
%>
<form id='f1' name='f1' action="shops.jsp" method="post"><input type='hidden' name='action' value='login'>
请输入用户的ID：<input type='text' name='playerid' id='playerid' value='<%=playerId %>' size='20'><input type='submit' value='提  交'>
</form>
<form name="form1" action="shops.jsp" method="post">
<input type="hidden" name="action" id="action" value="buyArticle">
<input type='hidden' name='playerid' value='<%=playerId %>'>
<input type="hidden" name="mapStr" id="mapStr">
<input type="hidden" name="articleIndex" id="articleIndex">
<input type="hidden" name="articleCount" id="articleCount">
</form>
<%
}
if(player != null && map != null && map.keySet() != null){
	%>
	<table>
	<tr class="titlecolor">
	<td rowspan="2">商店标识</td><td colspan="16">商店中的物品</td><td colspan="7">购买限制</td><%if(playerId != -1){ %><td rowspan="2">购买商品</td><%} %>
	</tr>
	<tr class="titlecolor">
	<td>物品的名称</td><td>一捆的数量</td><td>颜色</td><td>支付方式</td><td>绑银的价格</td><td>银子的价格</td><td>物品交换</td><td>荣誉值</td><td>师徒积分</td><td>帮派资金</td><td>帮派积分</td><td>充值积分</td><td>是否有声望限制</td><td>限制声望的名字</td><td>声望的等级</td><td>需要物品的描述</td>
	<td>总量限制</td>
	<td>每个人购买数量限制</td>
	<td>每天定点限制</td>
	<td>每周定点限制</td>
	<td>每月定点限制</td>
	<td>固定时间限制</td>
	</tr>
	<%
	for(String mapStr : map.keySet()){
		if(mapStr != null){
			Shop shop = map.get(mapStr);
			if(shop != null){
				if(shop.getGoods(player) != null && shop.getGoods(player).length != 0){
					for(int m = 0; m < shop.getGoods(player).length; m++){
						Goods goods = shop.getGoods(player)[m];
				if(goods != null){
				%>
				<tr>
				<%
				if(m == 0){
					%><td rowspan="<%=shop.getGoods(player).length %>"><%=shop.getName() %></td><%
				}
				%>
				<td><a href="../ArticleByName.jsp?articleName=<%=goods.getArticleName() %>"><%=goods.getArticleName() %></a></td>
				<td><%=goods.getBundleNum() %></td>
				<td><%=goods.getColor() %></td>
				<td><%=goods.getPayType() %></td>

				<td><%=goods.getPrice() %></td>
				<td><%=goods.getPrice() %></td>
				<%
				StringBuffer sb = new StringBuffer();
				if(goods.getExchangeArticleNames() != null){
					for(int i = 0; i < goods.getExchangeArticleNames().length; i++){
						sb.append(goods.getExchangeArticleNames()[i]+goods.getExchangeArticleNums()[i]+"个  ");
					}
				}
				%>
				<td><%=sb.toString() %></td>
				<td><%= goods.getPrice() %></td>
				<td><%= goods.getPrice() %></td>
				<td><%= goods.getPrice() %></td>
				<td><%= goods.getPrice() %></td>
				<td><%= goods.getPrice() %></td>
				<td><%=goods.isReputeLimit() %></td>
				<td><%=goods.getLimitReputeName() %></td>
				<td><%=goods.getLimitReputeLevel() %></td>
				<td><%=goods.getExchangeArticleDescription() %></td>
				
				<%
					if( (goods.getLimitValue() & ShopManager.LIMIT_TOTAL_SELL_NUM) == 0){
						out.println("<td>无限制</td>");
					}else{
						out.println("<td>"+goods.getTotalSellNum()+"/"+goods.getTotalSellNumLimit()+"</td>");
					}
				
					if( (goods.getLimitValue() & ShopManager.LIMIT_SELL_NUM_BY_PLAYER) == 0){
						out.println("<td>无限制</td>");
					}else{
						out.println("<td>"+goods.getPersonSellNumLimit()+"</td>");
					}
				
					if( (goods.getLimitValue() & ShopManager.LIMIT_EVERYDAY_HOURS) == 0){
						out.println("<td>无限制</td>");
					}else{
						int k1 = goods.getEveryDayBeginLimit()/60;
						int r1 = goods.getEveryDayBeginLimit() - k1 * 60;
						
		
						int k2 = goods.getEveryDayEndLimit()/60;
						int r2 = goods.getEveryDayEndLimit() - k2 * 60;
						
						out.println("<td>"+k1+":"+r1+"~"+k2+":"+r2+"</td>");
					}
					
					if( (goods.getLimitValue() & ShopManager.LIMIT_EVERYWEEK_DAYS) == 0){
						out.println("<td>无限制</td>");
					}else{
						int k1 = goods.getEveryWeekBeginLimit()/24;
						int r1 = goods.getEveryWeekBeginLimit() - k1 * 24;
						if(k1 > 6) k1 = 0;
						int k2 = goods.getEveryWeekEndLimit()/24;
						int r2 = goods.getEveryWeekEndLimit() - k2 * 24;
						if(k2 > 6) k2 = 0;
						
						out.println("<td>"+k1+":"+r1+"~"+k2+":"+r2+"</td>");
					}
					
					if( (goods.getLimitValue() & ShopManager.LIMIT_EVERYMONTH_DAYS) == 0){
						out.println("<td>无限制</td>");
					}else{
						
						out.println("<td>"+goods.getEveryMonthBeginLimit()+"日~"+goods.getEveryMonthEndLimit()+"日</td>");
					}
					
					if( (goods.getLimitValue() & ShopManager.LIMIT_FIX_TIME) == 0){
						out.println("<td>无限制</td>");
					}else{
						
						out.println("<td>"+DateUtil.formatDate(new Date(goods.getFixTimeBeginLimit()),"yyyy-MM-dd HH:mm:ss")+"~"+DateUtil.formatDate(new Date(goods.getFixTimeEndLimit()),"yyyy-MM-dd HH:mm:ss")+"</td>");
					}
				
					
				%>
				<%if(playerId != -1){ %><td>数量<input id="<%=mapStr+m%>" style="width:20px"><input type="button" value="购买" onclick="javascript:buyArticles('<%=mapStr%>',<%=m%>);"></td><%} %>
				</tr>
				<%
					
				}
					
				}}
			}
		}
	}
	%>
	</table>
	<%
}
%>
</body>
</html>
