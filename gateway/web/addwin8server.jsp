<%@page import="java.lang.reflect.Method"%>
<%@page import="com.fy.gamegateway.mieshi.server.MieshiGatewaySubSystem"%>
<%@page import="java.util.ArrayList"%>
<%@page import="com.fy.gamegateway.mieshi.server.MieshiServerInfoManager.DenyUser"%>
<%@page import="com.xuanzhi.tools.cache.diskcache.concrete.DiskCacheHelper"%>
<%@page import="com.xuanzhi.tools.cache.diskcache.DiskCache"%>
<%@page import="com.xuanzhi.tools.cache.diskcache.concrete.DataBlock"%>
<%@page import="java.io.File"%>
<%@page import="com.xuanzhi.tools.cache.diskcache.concrete.DefaultDiskCache"%>
<%@page import="com.fy.gamegateway.mieshi.server.MieshiServerInfoManager"%>
<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>

<%
	MieshiGatewaySubSystem.ServersShowForWin8 = new String[]{"潘多拉星","潘多拉星333"};


	for(String str : MieshiGatewaySubSystem.ServersShowForWin8 )
	{
		out.println(str+"<br/>");
	}
	
%>