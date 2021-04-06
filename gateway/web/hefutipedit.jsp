<%@page import="com.fy.gamegateway.mieshi.server.MieshiGatewaySubSystem"%>
<%@page import="com.xuanzhi.tools.cache.diskcache.concrete.DefaultDiskCache"%>
<%@page import="com.xuanzhi.tools.text.DateUtil"%>
<%@page import="com.fy.gamegateway.mieshi.server.HeFuTipConfigInfo"%>
<%@page import="org.apache.commons.lang.StringUtils"%>
<%@page import="com.xuanzhi.tools.text.ParamUtils"%>
<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%
	boolean isDo = ParamUtils.getBooleanParameter(request, "isDo");
	String paraBeginTime = null;
	String paraEndTime = null;
	int tipnum = 100;
	String servers = null;
	String content = null;
	
	if(isDo)
	{
		DefaultDiskCache defaultDiskCache = MieshiGatewaySubSystem.getInstance().hefuTipInfoCache;
		
		paraBeginTime = "";
		paraEndTime = "";
		
		
		servers = ParamUtils.getParameter(request, "servers");
		content = "";
		
		tipnum = 0;
		
		String[] serverss = servers.split("\r*\n");
		
		for(String str : serverss)
		{
			HeFuTipConfigInfo heFuTipConfigInfo = new HeFuTipConfigInfo();
			long begintime = DateUtil.parseDate(paraBeginTime, "yyyy-MM-dd HH:mm:ss").getTime();
			long endtime = DateUtil.parseDate(paraEndTime, "yyyy-MM-dd HH:mm:ss").getTime();
			
			String oldServer = str.split("[\\s]+")[0].trim();
			
			HeFuTipConfigInfo hc = (HeFuTipConfigInfo)defaultDiskCache.get(oldServer);
			content += hc.oldServerRealName+":"+"\r\n"+hc.tipContent+"\r\n"+"=======================================>"+"\r\n";
		}
		
		
	}


%>

<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Frameset//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-frameset.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">
<head>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
<title>合服提示</title>
</head>
<body>
<form method="post">
	<input type="hidden" name="isDo" value="true" /><br/>
	提示开始时间段:<input type="text" name="begintime" value=<%=paraBeginTime %> /> -结束时间段:<input type="text" name="endtime" value=<%=paraEndTime %> />[时间格式:2013-xx-xx 00:00:00]<br/>
	指定合服的服务器以及对应的新服务器(格式为:老服 新服):<textarea rows="20" cols="30" name="servers" value=""><%=servers%></textarea><br/>
	指定内容:<textarea rows="20" cols="30" name="content" ><%=content%></textarea><br/>
	提示次数:<input type="text" name="tipnum" value="<%=tipnum %>"/><br/>
	<input type="submit" value="提交">
</form>
</body>
</html>