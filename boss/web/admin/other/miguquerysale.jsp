<%@page import="java.util.Iterator"%>
<%@page import="com.xuanzhi.tools.servlet.HttpUtils"%>
<%@page import="java.net.URL"%>
<%@page import="com.fy.boss.platform.migu.MiguWorker"%>
<%@page import="com.fy.boss.platform.migu.MiguWorker.UrlInfo"%>
<%@page import="java.util.HashMap"%>
<%@page import="java.util.Map"%>
<%@page import="com.fy.boss.game.service.ServerManager"%>
<%@page import="com.fy.boss.game.model.Server"%>
<%@page import="com.fy.boss.platform.anzhi.AnZhiSavingManager"%>
<%@page import="java.util.ArrayList"%>
<%@page import="java.util.List"%>
<%@page import="com.fy.boss.finance.service.PlatformSavingCenter"%>
<%@page import="java.util.Date"%>
<%@page import="com.xuanzhi.tools.text.DateUtil"%>
<%@page import="org.apache.commons.lang.StringUtils"%>
<%@page import="com.fy.boss.platform.uc.UCSDKSavingManager"%>
<%@page import="com.fy.boss.finance.dao.impl.ExceptionOrderFormDAOImpl"%>
<%@page import="com.fy.boss.finance.model.ExceptionOrderForm"%>
<%@page import="com.fy.boss.authorize.service.PassportManager"%>
<%@page import="com.fy.boss.authorize.model.Passport"%>
<%@page import="com.fy.boss.finance.service.OrderFormManager"%>
<%@page import="com.fy.boss.finance.model.OrderForm"%>
<%@page import="com.xuanzhi.tools.text.ParamUtils"%>

<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%
	//
	String infos = ParamUtils.getParameter(request, "infos", "");
	Map<Long,String> salesMap = new HashMap<Long,String>();
	
	boolean isDo = ParamUtils.getBooleanParameter(request, "isDo");
	if(isDo)
	{
		
		String[] saleinfos = infos.split("\r*\n");
		for(String saleinfo : saleinfos)
		{
			String[] splitedSaleinfo = saleinfo.split("[\\s]+"); 
			long saleId = Long.decode(splitedSaleinfo[0]);
			String servername = splitedSaleinfo[1];
			Server server = ServerManager.getInstance().getServer(servername);
			
			UrlInfo urlInfo = MiguWorker.getUrlInfo(server.getSavingNotifyUrl());
			
			String params = "saleid="+saleId;
			
			params+="&authorize.username=migu_trade";
			params+="&authorize.password=akiwWE123)sq";
			
			
			Map headers = new HashMap();
			URL url = new URL(urlInfo.url+":"+urlInfo.port+"/migu/querysalerecord.jsp");
			try {
				byte bytes[] = HttpUtils.webPost(url, params.getBytes("utf-8"), headers, 60000, 60000);
				String encoding = (String)headers.get(HttpUtils.ENCODING);
				Integer code = (Integer)headers.get(HttpUtils.Response_Code);
				String message = (String)headers.get(HttpUtils.Response_Message);
				String result = new String(bytes,"utf-8");
				salesMap.put(saleId, result);
			} catch (Exception e) {
				throw e;
			}
			
		}
		
	}

%>


<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html>
<head>

</head>
<body>
	<h2>米谷订单状态查询</h2>
	<table>
		<tr>
			<td>记录id</td>
			<td>状态描述</td>
		</tr>
		<%
			Iterator<Long> it = salesMap.keySet().iterator();
			while(it.hasNext())
			{
				long key = it.next();
		%>
				<tr><td><%=key %></td><td><%=salesMap.get(key) %></td></tr>	
		<% 	
			}
		
		%>
		
	</table>
	
	<form method="post">
	<input type="hidden" name="isDo" value="true" />
		订单信息(saleid  server):<textarea rows="100" cols="100" name="infos" value="<%=infos%>"></textarea>(saleId 服务器)<br>
		<input type="submit" value="提交">
	</form>
</body>
</html>
