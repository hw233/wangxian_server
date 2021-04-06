<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@page import="com.fy.engineserver.jiazu.service.JiazuManager"%>
<%@page import="com.fy.engineserver.jiazu.Jiazu"%>
<%@page import="java.util.Set"%>
<%@page import="java.util.Iterator"%>
<%@page import="com.fy.engineserver.jiazu.JiazuMember"%>
<%@page import="com.fy.engineserver.sprite.PlayerManager"%>
<%@page import="com.fy.engineserver.sprite.Player"%>
<%@page import="java.util.List"%>
<%@page import="java.util.ArrayList"%>
<%@page import="com.fy.engineserver.core.JiazuSubSystem"%>
<%@page import="com.fy.engineserver.economic.BillingCenter"%>
    
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<%
	String jiazuIdStr = request.getParameter("jiazuId");
	String jiazuMoney = request.getParameter("jiazuMoney");
	if(jiazuIdStr == null || "".equals(jiazuIdStr.trim()) || jiazuMoney == null || "".equals(jiazuMoney.trim())){
		out.print("<h2>每一项都必填</h2>");
		jiazuIdStr = "";
		jiazuMoney = "";
	}else{
		Jiazu jiazu = JiazuManager.getInstance().getJiazu(Long.valueOf(jiazuIdStr));
		if (jiazu == null) {
			out.print("没有该家族，请确认家族ID是否正确:" + jiazuIdStr + "<BR/>");
			return;
		}else{
			int money = Integer.valueOf(jiazuMoney);
			jiazu.setJiazuMoney(money);
			JiazuManager.logger.error("[后台重置家族资金] [家族id:" + jiazuIdStr + "] [家族名:" + jiazu.getName() + "] [家族资金重置为:" + money + "]" );
			out.print("已成功将家族" + jiazu.getName() + "的资金重置为:" +BillingCenter.得到带单位的银两(jiazu.getJiazuMoney())+"<BR/>");
		}
	}
%>

<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title>重置家族资金</title>
</head>
<body>
	<form action="">
		请输入以下信息：
		家族id:<input type="text" name="jiazuId" value="<%=jiazuIdStr %>"/>
		家族资金重置为(单位:文)：<input type="text" name="jiazuMoney" value="<%=jiazuMoney %>" />
		<input type="submit" value="submit" />
	</form>

</body>
</html>