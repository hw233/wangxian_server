<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<%@ page contentType="text/html;charset=utf-8" import="java.util.*"%>

<%@page import="com.fy.boss.tools.JacksonManager"%>
<%@page import="com.google.gson.JsonParseException"%>
<%@page import="java.io.IOException"%>
<%@page import="com.fy.boss.platform.appchina.AppChinaSavingNewCallBackServlet"%><html xmlns="http://www.w3.org/1999/xhtml">
<head>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
<title>北京神奇时代客服后台系统</title>
<style type="text/css">
</style>
  
</head>

<%
AppChinaSavingNewCallBackServlet.miyao = AppChinaSavingNewCallBackServlet.privateKey;
out.print("<br>"+AppChinaSavingNewCallBackServlet.miyao);
/**
String transdata = "trdata:{appid:5000781547,appuserid:861022005736337,cporderid:appchina-20160606-1100000000000025603,currency:RMB,feetype:0,money:1.00,paytype:401,result:0,transid:32011606061127202335,transtime:2016-06-06 11:30:10,transtype:0,waresid:1}";
JacksonManager jm = JacksonManager.getInstance();	
try {
	Map dataMap = (Map)jm.jsonDecodeObject(transdata);
	String transtype = dataMap.get("transtype")+"";
	String cporderid = (String)dataMap.get("cporderid");
	String transid = (String)dataMap.get("transid");
	String appuserid = (String)dataMap.get("appuserid");
	String appid = (String)dataMap.get("appid");
	String waresid = ((Integer)dataMap.get("waresid")).intValue() + "";
	String feetype = ((Integer)dataMap.get("feetype")).intValue()+"";
	String money = ((Float)dataMap.get("money")).floatValue()+"";
	String currency = ((String)dataMap.get("currency"));
	String result = dataMap.get("result")+"";
	String transtime = dataMap.get("transtime")+"";
	String count = dataMap.get("count")+"";
	out.println("transtype:"+transtype);
	out.println("cporderid:"+cporderid);
	out.println("transid:"+transid);
	out.println("waresid:"+waresid);
	out.println("result:"+result);
} catch (JsonParseException e) {
	e.printStackTrace();
} catch (IOException e) {
	e.printStackTrace();
}
*/
%>
</html>
