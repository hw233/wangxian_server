<%@page import="com.fy.engineserver.sprite.Player"%>
<%@page
	import="com.fy.engineserver.enterlimit.EnterLimitManager.PlayerRecordData"%>
<%@page
	import="com.fy.engineserver.enterlimit.EnterLimitManager.EnterLimitData"%>
<%@page import="java.util.*"%>
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
			if (action.equals("playerOnLinePiLao")) {
				Player.isUseOnLineTimePiLao  = !Player.isUseOnLineTimePiLao;
			}else if (action.equals("isCanChongZhi")) {
				EnterLimitManager.isCanChongZhi = !EnterLimitManager.isCanChongZhi;
			}else if (action.equals("isTest18")) {
				Player.isTest18 = !Player.isTest18;
			}else if (action.equals("setNoChongZhiMac")) {
				String macs = request.getParameter("macs");
				macs = macs.substring(1, macs.length() - 1);
				String[] ms = macs.split(", ");
				EnterLimitManager.noChongZhiMacs = ms;
			}else if (action.equals("setNoChongZhiUsername")) {
				String usernames = request.getParameter("usernames");
				usernames = usernames.substring(1, usernames.length() - 1);
				String[] us = usernames.split(", ");
				EnterLimitManager.noChongZhiUserNames = us;
			}else if (action.equals("setNoChongZhiClientID")) {
				String clientIDs = request.getParameter("clientIDs");
				clientIDs = clientIDs.substring(1, clientIDs.length() - 1);
				String[] cs = clientIDs.split(", ");
				EnterLimitManager.noChongZhiClientIDs = cs;
			}else if (action.equals("setNoChongZhiIP")) {
				String ips = request.getParameter("ips");
				ips = ips.substring(1, ips.length() - 1);
				String[] is = ips.split(", ");
				EnterLimitManager.noChongZhiIPs = is;
			}
		}
	%>
	
	<form>
		开启或关闭18岁以下在线检查-----当前(<%=Player.isUseOnLineTimePiLao %>)
		<input type="hidden" value="playerOnLinePiLao" name="action">
		<input type="submit" value="<%=Player.isUseOnLineTimePiLao ? "关闭" : "开启"%>">
	</form>
	<br>
	<br>
	<form>
		开启或关闭检查Mac地址不显示充值-----当前(<%=EnterLimitManager.isCanChongZhi %>)
		<input type="hidden" value="isCanChongZhi" name="action">
		<input type="submit" value="<%=EnterLimitManager.isCanChongZhi ? "关闭" : "开启"%>">
	</form>
	<br>
	<br>
	<form>
		开启或关闭18岁防沉迷测试-----当前(<%=Player.isTest18 %>)
		<input type="hidden" value="isTest18" name="action">
		<input type="submit" value="<%=Player.isTest18 ? "关闭" : "开启"%>">
	</form>
	<br>
	<br>
	<form>
		设置Mac地址-----当前(<%=Arrays.toString(EnterLimitManager.noChongZhiMacs) %>)
		<br>
		<input type="hidden" value="setNoChongZhiMac" name="action">
		<input type="text" name="macs" value = "<%=Arrays.toString(EnterLimitManager.noChongZhiMacs) %>">
		<input type="submit" value="确定">
	</form>
	<br>
	<form>
		设置用户-----当前(<%=Arrays.toString(EnterLimitManager.noChongZhiUserNames) %>)
		<br>
		<input type="hidden" value="setNoChongZhiUsername" name="action">
		<input type="text" name="usernames" value = "<%=Arrays.toString(EnterLimitManager.noChongZhiUserNames) %>">
		<input type="submit" value="确定">
	</form>
	<br>
	<form>
		设置ClientId-----当前(<%=Arrays.toString(EnterLimitManager.noChongZhiClientIDs) %>)
		<br>
		<input type="hidden" value="setNoChongZhiClientID" name="action">
		<input type="text" name="clientIDs" value = "<%=Arrays.toString(EnterLimitManager.noChongZhiClientIDs) %>">
		<input type="submit" value="确定">
	</form>
	<br>
	<form>
		设置Ip地址-----当前(<%=Arrays.toString(EnterLimitManager.noChongZhiIPs) %>)
		<br>
		<input type="hidden" value="setNoChongZhiIP" name="action">
		<input type="text" name="ips" value = "<%=Arrays.toString(EnterLimitManager.noChongZhiIPs) %>">
		<input type="submit" value="确定">
	</form>
</body>
</html>