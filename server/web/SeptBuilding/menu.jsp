<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@page import="com.fy.engineserver.septbuilding.templet.SeptBuildingTemplet.BuildingType"%>
<%@page import="com.fy.engineserver.septbuilding.templet.BiaoZhiXiangQL"%>
<%@page import="com.fy.engineserver.septbuilding.templet.JiaZuQi"%>
<%@page import="com.fy.engineserver.septbuilding.templet.FangJuFang"%>
<%@page import="com.fy.engineserver.septbuilding.templet.WuQiFang"%>
<%@page import="com.fy.engineserver.septbuilding.templet.HuanYuZhen"%>
<%@page import="com.fy.engineserver.septbuilding.templet.LongTuGe"%>
<%@page import="com.fy.engineserver.septbuilding.templet.XianLingDong"%>
<%@page import="com.fy.engineserver.septbuilding.templet.JuBaoZhuang"%>
<%@page import="com.fy.engineserver.septbuilding.templet.JuXianDian"%>
 <%@page import="com.fy.engineserver.septbuilding.templet.SeptBuildingTemplet"%>
<%@page import="com.fy.engineserver.septbuilding.service.SeptBuildingManager"%>
<%@ page import="com.fy.engineserver.septbuilding.entity.*"%>
<%@ page import="com.fy.engineserver.septstation.*"%>
<%@ page import="com.fy.engineserver.septstation.service.*"%>
<%@ page import="com.xuanzhi.tools.cache.CacheObject" %>
<%@ page import="java.util.*"%>
<%
String path = request.getContextPath();
String basePath = request.getScheme()+"://"+request.getServerName()+":"+request.getServerPort()+path+"/";
%>

<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN">
<html>
  <head>
    <base href="<%=basePath%>">
    
    <title>My JSP 'nemu.jsp' starting page</title>
    
	<meta http-equiv="pragma" content="no-cache">
	<meta http-equiv="cache-control" content="no-cache">
	<meta http-equiv="expires" content="0">    
	<meta http-equiv="keywords" content="keyword1,keyword2,keyword3">
	<meta http-equiv="description" content="This is my page">
	<link rel="stylesheet" type="text/css" href="../SeptBuilding/css/septStation.css">

  </head>
	<a href="../SeptBuilding/SeptStationList.jsp" target="list">查看家族列表</a><br/><br/>
	<a href="../SeptBuilding/septStationTemplet.jsp" target="value">查看地图位置模板</a><br/><br/>
	<a href="../SeptBuilding/buildingTempletInfo.jsp" target="value">查看建筑模板</a><br/><br/>
  <body>
  </body>
</html>
