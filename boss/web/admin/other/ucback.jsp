<%@ page contentType="text/html;charset=UTF-8"%><%@ page import="com.fy.boss.finance.model.*,com.fy.boss.finance.service.*,java.util.*"%><?xml version="1.0" encoding="UTF-8"?>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Strict//EN" "DTD/xhtml1-strict.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">
<head>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
<meta http-equiv="Cache-Control" content="no-cache"/>
<meta name="viewport" content="width=device-width, minimum-scale=1.0, maximum-scale=2.0"/>
<style>
*{ margin:0; padding:0;}
body{ font-size:14px;color:#C3E9FF; font-family:"宋体";background:#339BC1;}
.foot{ height:163px; background:url(images/f_bg.jpg) no-repeat center 0; text-align:center; line-height:20px; padding-top:80px; color:#000;}
</style>
</head>
<body>
<br>
<p>
<center>
<img src="images/logo.png" width="120"><br>
<%
String ua = request.getHeader("User-Agent").toLowerCase();
String order_id = request.getParameter("order_id");
OrderForm order = OrderFormManager.getInstance().getOrderForm(order_id);
if(order == null) {
	out.println("支付失败：未找到订单！");
} else {
	if(order.getResponseResult() == OrderForm.HANDLE_RESULT_SUCC) {
		out.println("支付成功：请检查你的游戏账户。");
	} else if(order.getResponseResult() == OrderForm.HANDLE_RESULT_FAILED) {
		out.println("支付失败，请重新下单！");
	} else {
		out.println("订单已提交，请稍候查询结果。");
	}
}
String uri = "wangxian://ucwappay";
%><br><br>
<%
if(ua.indexOf("ios") != -1 || ua.indexOf("iphone") != -1) {
%>
<a href="<%=uri %>">
        <img src="images/back.png" style="border:0px;"/>
</a>
<br><br><br><br>
*如果不能返回，请手动关闭浏览器返回游戏。
<%} else {%>
请关闭浏览器返回游戏，方法：点【菜单】键选择【退出】
<br><br><br><br>
<%}%>
</center>
</p>
<br><br>
<div class="foot"></div>
</body>
</html>