<%@page import="com.fy.gamegateway.mieshi.waigua.NewLoginHandler"%>
<%@page import="com.fy.gamegateway.mieshi.waigua.SessionManager"%>
<%@page import="java.util.Arrays"%>
<%@page import="com.fy.gamegateway.mieshi.waigua.ClientWaiGuaProcessManager"%>
<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title>Insert title here</title>
</head>
<body>
	
	<%
		String action = request.getParameter("action");
		if (action != null) {
			if (action.equals("setProcess")) {
				String v = request.getParameter("names");
				v = v.substring(1, v.length()-1);
				String[] names = v.split(", ");
				ClientWaiGuaProcessManager.waiGuaProcessNames = names;
			}else if (action.equals("openSession")) {
				SessionManager.isSessionServerWeiHu = !SessionManager.isSessionServerWeiHu;
			}else if (action.equals("closeHand1")) {
				String mima = request.getParameter("mima");
				if ("sqage".equals(mima)) {
					NewLoginHandler.isSendHand1 = !NewLoginHandler.isSendHand1;
				}
			}else if (action.equals("closeHand2")) {
				String mima = request.getParameter("mima");
				if ("sqage".equals(mima)) {
					NewLoginHandler.isSendHand2 = !NewLoginHandler.isSendHand2;
				}
			}
		}
	%>
	<br>
	打开或关闭在校验session的时候是否检查服务器是维护状态
	<form>
		<input type="hidden" name="action" value="openSession">
		<input type="submit" value="<%=SessionManager.isSessionServerWeiHu ? "关闭": "打开"%>">
	</form>
	
	<br>
	修改外挂进程名字
	<form>
		<input type="hidden" name="action" value="setProcess">
		<input type="text" name="names" size="150" value="<%=Arrays.toString(ClientWaiGuaProcessManager.waiGuaProcessNames) %>">
		<input type="submit" value="确定">
	</form>
	<br>
	<br>
	关闭某种握手协议:
	<br>
	(当前2013-09-17修改后 新的协议是hand_N)
	<br>
	当前是:<%=NewLoginHandler.isSendHand1 ? "(hand_N)协议发送" : "(hand_N)协议不发送" %>
	<form>
		<input type="hidden" name="action" value="closeHand1">
		<input type="text" name="mima">
		<input type="submit" value="确定">
	</form>
	当前是:<%=NewLoginHandler.isSendHand2 ? "(模拟正常)协议发送" : "(模拟正常)协议不发送" %>
	<form>
		<input type="hidden" name="action" value="closeHand2">
		<input type="text" name="mima">
		<input type="submit" value="确定">
	</form>
</body>
</html>
