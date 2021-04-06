<%@page import="com.fy.boss.gm.gmpagestat.handler.ServerEventHandle"%>
<%@page import="com.xuanzhi.tools.cache.diskcache.concrete.DefaultDiskCache"%>
<%@page import="com.fy.boss.gm.gmpagestat.event.ArticleRecordEvent"%>
<%@page import="com.fy.boss.gm.gmpagestat.EventData"%>
<%@page import="com.fy.boss.gm.gmpagestat.GmEventManager"%>
<%@page import="java.util.*"%>
<%@page import="java.util.Date"%>
<%@page import="java.text.SimpleDateFormat"%>
<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<link type="text/css" rel="stylesheet" href="style.css">
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title>详细信息</title>
</head>
<body bgcolor="#c8edcc">
<%
	try{
		Object obj = session.getAttribute("authorize.username");
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
		String key = sdf.format(new Date());
		GmEventManager gm = GmEventManager.getInstance();	
		DefaultDiskCache ddc = gm.getDdc();
		EventData data = (EventData)gm.getDdc().get(key);
		List<ArticleRecordEvent> alist = data.getAlist();
		Map<String,ServerEventHandle> servereventmap = gm.getServerEventMess(key);
		out.print(servereventmap.size());
	}catch(Exception e){
		out.print(e);		
	}
%>

<table id="msg">
	<caption id='title1' align="top"><font color='red' size="4">物品相关操作《单服统计》</font></caption>
		<tr>
			<th>服务器</th>
			<th>装备(完美紫)</th>
			<th>装备(橙色)</th>
			<th>装备(完美橙)</th>
			<th>道具(紫色)</th>
			<th>道具(橙色)</th>
			<th>是否报警</th>
			<th>操作</th>
 		</tr>
 		
 		<tr>
			<td>巍巍昆仑</td>
			<td>300---300</td>
			<td>100---300</td>
			<td>50---300</td>
			<td>200---300</td>
			<td>200---300</td>
			<td>是</td>
			<td>查看详情</td>
 		</tr>
</table>



<table id="msg">
	<caption id='title1'><font color='red' size="4">物品相关操作《个人统计》</font></caption>
		<tr>
			<th>服务器</th>
			<th>角色名</th>
			<th>物品名</th>
			<th>物品数量</th>
			<th>物品颜色</th>
			<th>物品类型</th>
			<th>操作人</th>
			<th>操作人IP</th>
			<th>操作时间</th>
 		</tr>
</table>

<table id="msg">
	<caption id='title1'><font color='red' size="4">物品相关操作《详细信息》</font></caption>
		<tr>
			<th>服务器</th>
			<th>角色名</th>
			<th>物品名</th>
			<th>物品数量</th>
			<th>物品颜色</th>
			<th>物品类型</th>
			<th>操作人</th>
			<th>操作人IP</th>
			<th>操作时间</th>
 		</tr>
</table>
<hr>


</body>
</html>