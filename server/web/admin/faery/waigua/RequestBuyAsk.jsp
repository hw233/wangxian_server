
<%@page import="com.fy.engineserver.validate.ValidateAskManager"%>
<%@page import="com.fy.engineserver.validate.DefaultValidateManager"%>
<%@page import="com.fy.engineserver.trade.requestbuy.service.RequestBuyManager"%>
<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title>Insert title here</title>
</head>
<body>
	<%
		String action = request.getParameter("action");
		if (action != null) {
			if ("openAsk".equals(action)) {
				RequestBuyManager.getInstance().isAsk = !RequestBuyManager.getInstance().isAsk;
			}else if ("changeSaleNum".equals(action)) {
				String saleNumString = request.getParameter("saleNum");
				int saleNum = Integer.parseInt(saleNumString);
				DefaultValidateManager.maxRequestBuySaleNumForOneIp = saleNum;
			}else if ("changeVip".equals(action)) {
				String viplevelString = request.getParameter("viplevel");
				int viplevel = Integer.parseInt(viplevelString);
				DefaultValidateManager.chargeMoneyLess = viplevel;
			}else if ("changePlayerLevel".equals(action)) {
				String playerLevelString = request.getParameter("playerLevel");
				int playerLevel = Integer.parseInt(playerLevelString);
				RequestBuyManager.getInstance().askPlayerLevel = playerLevel;
			}else if ("changeAskType".equals(action)) {
				int askType = Integer.parseInt(request.getParameter("askType"));
				ValidateAskManager.getInstance().askType = askType;
			}
			response.sendRedirect("./RequestBuyAsk.jsp");
		}
	%>

	<form name="f1">
		<input type="hidden" name="action" value="openAsk">
		<input type="submit" value=<%=RequestBuyManager.getInstance().isAsk ? "关闭答题" : "打开答题" %>>
	</form>
	<br>
	<br>
	<form name="f2">
		多少次快速出售才答题:
		<input type="hidden" name="action" value="changeSaleNum">
		<input type="text" name="saleNum" value=<%=DefaultValidateManager.maxRequestBuySaleNumForOneIp %>>
		<input type="submit" value="确定">
	</form>
	<br>
	<br>
	<form name="f3">
		rmb多少就不答题了:
		<input type="hidden" name="action" value="changeVip">
		<input type="text" name="viplevel" value=<%=DefaultValidateManager.chargeMoneyLess %>>
		<input type="submit" value="确定">
	</form>
	<br>
	<br>
	<form name="f4">
		多少等级不答题:
		<input type="hidden" name="action" value="changePlayerLevel">
		<input type="text" name="playerLevel" value=<%=RequestBuyManager.getInstance().askPlayerLevel %>>
		<input type="submit" value="确定">
	</form>
	<br>
	<br>
	<form name="f5">
		题目类型:
		<input type="hidden" name="action" value="changeAskType">
		<input type="text" name="askType" value=<%=ValidateAskManager.getInstance().askType %>>
		0是1+1=？   1是填入图片内容
		<input type="submit" value="确定">
	</form>
</body>
</html>