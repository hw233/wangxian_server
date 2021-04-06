<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<%@page import="java.sql.Timestamp"%>
<%@page import="java.util.List"%>
<%@page import="com.fy.engineserver.sprite.concrete.GamePlayerManager"%>
<%@page import="com.fy.engineserver.sprite.Player"%>
<%@page
	import="com.fy.engineserver.economic.thirdpart.migu.MiGuTradeServiceWorker"%>
<%@page
	import="com.fy.engineserver.economic.thirdpart.migu.entity.SaleRecord"%>
<%@page
	import="com.fy.engineserver.economic.thirdpart.migu.entity.SaleRecordManager"%>
<%@page import="com.xuanzhi.tools.text.ParamUtils"%>
<%@ page language="java" contentType="text/html; charset=utf-8"
	pageEncoding="utf-8"%>
<head>
</head>
<body>
	<%
		String ssId = request.getParameter("ssId");
		String username = request.getParameter("username");
		String playerId = request.getParameter("playerId");
		String action = request.getParameter("action");
		action = action == null ? "" : action;
	%>

	<form action="querysalerecord.jsp" method="post">
		<input type="hidden" name="action" value="querySalerecord4player" />角色名: <input
			type="text" name="ssId" value="<%=ssId%>" /> <input
			type="submit" value="查询玩家订单" />
	</form>
	<br />
	<form action="querysalerecord.jsp" method="post">
		<input type="hidden" name="action" value="querySalerecord" />订单id: <input
			type="text" name="ssId" value="<%=ssId%>" /> <input
			type="submit" value="查询订单" />
	</form>
	<br />
	<form action="querysalerecord.jsp" method="post">
		<input type="hidden" name="action" value="cancleSalerecord" />订单id: <input
			type="text" name="ssId" value="<%=ssId%>" /> 账号: <input
			type="text" name="username" value="<%=username%>" />角色id: <input
			type="text" name="playerId" value="<%=playerId%>" /> <input
			type="submit" value="下架" />
	</form>
	<br />
	<%
		if ("querySalerecord".equals(action)) {
			SaleRecord saleRecord = SaleRecordManager.getInstance().getSaleRecord(Long.parseLong(ssId));
			if (saleRecord == null) {
				out.println("不存在订单  :" + ssId);
				return ;
			}
			if (saleRecord.getResponseResult() == SaleRecord.BACK_MONEY_SUCC) {
				out.print("下架成功");
			} else if (saleRecord.getResponseResult() == SaleRecord.RESPONSE_FAILED) {
				out.print("失败");
			} else if (saleRecord.getResponseResult() == SaleRecord.RESPONSE_NOBACK) {
				out.print("尚未确认");
			} else if (saleRecord.getResponseResult() == SaleRecord.RESPONSE_SHIFT) {
				out.print("已上架");
			} else if (saleRecord.getResponseResult() == SaleRecord.RESPONSE_SUCC) {
				out.print("成功");
			} else {
				out.print("未知状态");
			}
		} else if ("querySalerecord4player".equals(action)) {
			Player player = GamePlayerManager.getInstance().getPlayer(ssId);
			String whereSql = "sellPassportId=" + player.getId() + " and responseResult=" + SaleRecord.RESPONSE_NOBACK;
			List<SaleRecord> list = SaleRecordManager.getInstance().getSaleRecordsByCondition(whereSql);
			for (SaleRecord sr : list) {
				out.println("[saleId:" + sr.getId() + "] [时间:"+new Timestamp(sr.getCreateTime())+"] [articleId:" + sr.getArticleId() + "] [amount:" + sr.getGoodsCount() + "] <br>");
			}
		} else if ("cancleSalerecord".equals(action)) {
			SaleRecord saleRecord = SaleRecordManager.getInstance().getSaleRecord(Long.parseLong(ssId));
			if (saleRecord == null) {
				out.println("不存在订单 :" + ssId);
				return ;
			}
			String returnContent = MiGuTradeServiceWorker.cancelSell(Long.parseLong(ssId), Long.parseLong(playerId), "下架", username);
			out.println("下架结果:" + returnContent + "<br>");
			if (saleRecord.getResponseResult() == SaleRecord.BACK_MONEY_SUCC) {
				out.print("下架成功");
			} else if (saleRecord.getResponseResult() == SaleRecord.RESPONSE_FAILED) {
				out.print("失败");
			} else if (saleRecord.getResponseResult() == SaleRecord.RESPONSE_NOBACK) {
				out.print("尚未确认");
			} else if (saleRecord.getResponseResult() == SaleRecord.RESPONSE_SHIFT) {
				out.print("已上架");
			} else if (saleRecord.getResponseResult() == SaleRecord.RESPONSE_SUCC) {
				out.print("成功");
			} else {
				out.print("未知状态");
			}
		}
	
		/* long saleid = ParamUtils.getLongParameter(request, "saleid", -1);
		String optiontype = request.getParameter("optiontype");
		if (optiontype != null && optiontype.equals("cancelSale")) {
			long roleId = ParamUtils.getLongParameter(request, "roleId", -1);
			String username = request.getParameter("username");
			String returnContent = MiGuTradeServiceWorker.cancelSell(saleid, roleId, "下架", username);
			out.println("[saleId:" + saleid + "] [下架结果:" + returnContent + "]<br>");
		}
		SaleRecord saleRecord = SaleRecordManager.getInstance().getSaleRecord(saleid);
		if (saleRecord != null) {
			if (saleRecord.getResponseResult() == SaleRecord.BACK_MONEY_SUCC) {
				out.print("下架成功");
			} else if (saleRecord.getResponseResult() == SaleRecord.RESPONSE_FAILED) {
				out.print("失败");
			} else if (saleRecord.getResponseResult() == SaleRecord.RESPONSE_NOBACK) {
				out.print("尚未确认");
			} else if (saleRecord.getResponseResult() == SaleRecord.RESPONSE_SHIFT) {
				out.print("已上架");
			} else if (saleRecord.getResponseResult() == SaleRecord.RESPONSE_SUCC) {
				out.print("成功");
			} else {
				out.print("未知状态");
			}
		} else {
			out.print("没有对应记录");
		} */
	%>

</body>
</html>
