<%@ page language="java" contentType="text/html; charset=utf-8"
    pageEncoding="utf-8"%>
<%@ page import="com.google.gson.*,java.util.*" %>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">

<%@page import="com.fy.engineserver.economic.charge.ChargeManager"%><head>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8">
<title>close </title>
<%
ChargeManager chargeManager = ChargeManager.getInstance();
chargeManager.addCloseChannelList("YOUXIQUNSDK_MIESHI");
out.print("ok");
%>
