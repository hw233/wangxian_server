<%@page import="java.util.Iterator"%>
<%@page import="com.fy.engineserver.datasource.buff.BuffTemplate"%>
<%@page import="java.util.HashMap"%>
<%@page import="com.fy.engineserver.datasource.buff.BuffTemplateManager"%>
<%@page import="com.fy.engineserver.activity.activeness.ActivenessManager"%>
<%@page import="java.util.Arrays"%>
<%@page import="com.fy.engineserver.menu.Option_ExchangeSliver_Salary"%>
<%@page import="com.fy.engineserver.menu.Option"%>
<%@page import="com.fy.engineserver.menu.MenuWindow"%>
<%@page import="com.fy.engineserver.menu.WindowManager"%>
<%@page import="java.util.ArrayList"%>
<%@page import="com.fy.engineserver.util.TimeTool"%>
<%@page import="java.util.List"%>
<%@page import="com.fy.engineserver.shop.Goods"%>
<%@page import="com.fy.engineserver.shop.Shop"%>
<%@page import="com.fy.engineserver.shop.ShopManager"%>
<%@ page language="java" contentType="text/html; charset=UTF-8"
        pageEncoding="UTF-8"%>
<%
	HashMap<Integer, BuffTemplate> buffTemplates = BuffTemplateManager.getInstance().getIntKeyTemplates();		
	Iterator<BuffTemplate> it = buffTemplates.values().iterator();
	while(it.hasNext()){
		BuffTemplate bt = it.next();
		if(bt!=null){
			if(bt.getIconId()==null){
				out.print("<font color='red'>name:"+bt.getName()+"--IconId:"+bt.getIconId()+"--id:"+bt.getId()+"</font><br>");
				
			}
			out.print("<font color='green'>name:"+bt.getName()+"--IconId:"+bt.getIconId()+"--id:"+bt.getId()+"</font><br>");
		}
	}
	
	HashMap<String, BuffTemplate> buffTemplateNamesMap = BuffTemplateManager.getInstance().getTemplates();
	Iterator<BuffTemplate> itt = buffTemplateNamesMap.values().iterator();
	out.print("<hr>");
	while(itt.hasNext()){
		BuffTemplate bt = itt.next();
		if(bt!=null){
			if(bt.getIconId()==null){
				out.print("<font color='red'>name:"+bt.getName()+"--IconId:"+bt.getIconId()+"--id:"+bt.getId()+"</font><br>");
				
			}
			out.print("<font color='green'>name:"+bt.getName()+"--IconId:"+bt.getIconId()+"--id:"+bt.getId()+"</font><br>");
		}
	}
%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title>修改buffIconId</title>
</head>
<body>

</body>
</html>