<%@page import="com.fy.engineserver.sprite.NewUserEnterServerService"%>
<%@page import="com.fy.engineserver.trade.requestbuy.service.RequestBuyManager"%>
<%@page import="com.fy.engineserver.message.GET_RSA_DATA_RES"%>
<%@page import="com.fy.engineserver.message.GET_RSA_DATA_REQ"%>
<%@page import="com.fy.engineserver.message.GameMessageFactory"%>
<%@page import="com.fy.engineserver.sprite.PlayerManager"%>
<%@page import="com.fy.engineserver.sprite.Player"%>
<%@page import="com.fy.engineserver.enterlimit.EnterLimitManager"%>
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
			if ("closeCom".equals(action)) {
				EnterLimitManager.isCompareRSA = !EnterLimitManager.isCompareRSA;
				response.sendRedirect("./mishi.jsp");
			}else if ("closeConnet".equals(action)) {
				EnterLimitManager.instance.isCloseConnet = !EnterLimitManager.instance.isCloseConnet;
				response.sendRedirect("./mishi.jsp");
			}else if ("randomCom".equals(action)) {
				EnterLimitManager.instance.isRandomCompare = !EnterLimitManager.instance.isRandomCompare;
				response.sendRedirect("./mishi.jsp");
			}else if ("sendRes".equals(action)) {
				String pName = request.getParameter("pName");
				Player player = null;
				try{
					player = PlayerManager.getInstance().getPlayer(pName);
				}catch(Exception e) {
					
				}
				if (player != null) {
					player.addMessageToRightBag(new GET_RSA_DATA_RES(GameMessageFactory.nextSequnceNum()));
				}
				response.sendRedirect("./mishi.jsp");
			}else if ("getProcess".equals(action)) {
				EnterLimitManager.isGetPlayerProcess = !EnterLimitManager.isGetPlayerProcess;
				response.sendRedirect("./mishi.jsp");
			}else if ("waiguaProcessCloseConn".equals(action)) {
				EnterLimitManager.isWaiGuaProcessCloseConn = !EnterLimitManager.isWaiGuaProcessCloseConn;
			}else if ("isUnUserOldHand".equals(action)) {
				NewUserEnterServerService.isUnUserOldHand = !NewUserEnterServerService.isUnUserOldHand;
			}
		}
	%>

	<form name="f1">
		<%=EnterLimitManager.isCompareRSA ? "关闭检查" : "开始检查" %>
		<input type="hidden" name="action" value="closeCom">
		<input type="submit" value="确定">
	</form>
	
	<form name="f2">
		<%=EnterLimitManager.instance.isCloseConnet ? "直接关闭连接" : "不关闭连接" %>
		<input type="hidden" name="action" value="closeConnet">
		<input type="submit" value="确定">
	</form>
	
	<form name="f3">
		<%=EnterLimitManager.instance.isRandomCompare ? "随机比较" : "不随机比较" %>
		<input type="hidden" name="action" value="randomCom">
		<input type="submit" value="确定">
	</form>
	
	<form name="f4">
		<%="发送另外一条协议给客户端" %>
		<br>
		玩家名字
		<input type="text" name="pName">
		<input type="hidden" name="action" value="sendRes">
		<input type="submit" value="确定">
	</form>
	
	<form name="f5">
		<%=EnterLimitManager.isGetPlayerProcess ? "取进程" : "不取进程" %>
		<input type="hidden" name="action" value="getProcess">
		<input type="submit" value="确定">
	</form>
	<br>
	<br>
	是否打开模拟器进程检查断连接
	<br>
	目前是:<%=EnterLimitManager.isWaiGuaProcessCloseConn ? "打开" : "关闭" %>
	<form name="f7">
		<input type="hidden" name="action" value="waiguaProcessCloseConn">
		<input type="submit" value="<%=EnterLimitManager.isWaiGuaProcessCloseConn ? "关闭" : "打开" %>">
	</form>
	<br>
	<br>
	被关闭的模拟器进程次数:<%
		int closeNum = 0;
		for (Long key : EnterLimitManager.playerCloseNum.keySet()) {
			closeNum += EnterLimitManager.playerCloseNum.get(key).intValue();
		}
		out.println(closeNum + "");
	%>
	<br>
	<form name="f8">
		<%=NewUserEnterServerService.isUnUserOldHand ? "目前是发送旧握手" : "目前不发送旧握手的" %>
		<input type="hidden" name="action" value="isUnUserOldHand">
		<input type="submit" value="确定">
	</form>
</body>
</html>