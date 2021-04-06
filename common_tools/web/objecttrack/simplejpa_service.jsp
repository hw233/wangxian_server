<%@ page contentType="text/html;charset=utf-8"%>
<%@ page
	import="java.util.*,com.xuanzhi.tools.text.*,com.xuanzhi.tools.simplejpa.impl.*,com.xuanzhi.tools.simplejpa.*,com.xuanzhi.tools.dbpool.ConnectionPool "%>
<%

%>
		
<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN">
<html>
	<head>
		<title></title>
		<meta http-equiv="pragma" content="no-cache">
		<meta http-equiv="cache-control" content="no-cache">
		<meta http-equiv="expires" content="0">
		<meta http-equiv="keywords" content="keyword1,keyword2,keyword3">
		<meta http-equiv="description" content="This is my page">
</head>

	<body>
		
		<h2 style="font-weight:bold;">
			【<%= SimpleEntityManagerFactoryHelper.getServerName() %>】数据存储服务，serverId=<%= SimpleEntityManagerFactoryHelper.getServerId() %>，上一次启动时间：<%= DateUtil.formatDate(new Date(ObjectTrackerService.serviceStartTime),"yyyy-MM-dd HH:mm:ss") %>
		</h2>
		
		<table id='test2' align='center' width='90%' cellpadding='1' bgcolor='#000000' cellspacing='1' border='0'>
		<tr bgcolor='#EEEEBB'><td>类名</td><td>内存中的数量</td><td>简单对象的数量</td><td>插入锁</td><td>查询锁</td><td>详情</td></tr>
		<%
		String classNames[] = SimpleEntityManagerFactoryHelper.getAllEntityClassName();
			for(int i = 0 ; i < classNames.length ; i++){
				
				int entityNum =0;
				long proxyNum =0;
				int insertLockNum = 0;
				int SelectLockNum = 0;
				
				if(SimpleEntityManagerFactory.getDbType().equalsIgnoreCase("oracle")){
					SimpleEntityManagerOracleImpl<?> o = (SimpleEntityManagerOracleImpl<?>)SimpleEntityManagerFactoryHelper.getSimpleEntityManagerImpl(classNames[i]);
					 entityNum = SimpleEntityManagerFactoryHelper.getEntityNum(o);
					 proxyNum = SimpleEntityManagerFactoryHelper.getEntityProxyNum(o);
					 insertLockNum = SimpleEntityManagerFactoryHelper.getInsertLockNum(o);
					 SelectLockNum = SimpleEntityManagerFactoryHelper.getSelectLockNum(o);
				}else{
					SimpleEntityManagerMysqlImpl<?> o = (SimpleEntityManagerMysqlImpl<?>)SimpleEntityManagerFactoryHelper.getSimpleEntityManagerImpl(classNames[i]);
					 entityNum = SimpleEntityManagerFactoryHelper.getEntityNum(o);
					 proxyNum = SimpleEntityManagerFactoryHelper.getEntityProxyNum(o);
					 insertLockNum = SimpleEntityManagerFactoryHelper.getInsertLockNum(o);
					 SelectLockNum = SimpleEntityManagerFactoryHelper.getSelectLockNum(o);
				}
				out.println("<tr bgcolor='#FFFFFF'><td><a href='./simplejpa_table.jsp?cl="+classNames[i]+"'>"+classNames[i]+"</a></td><td>"
						+StringUtil.addcommas(entityNum)
						+"</td><td>"+StringUtil.addcommas(proxyNum)+"</td><td>"+StringUtil.addcommas(insertLockNum)+"</td>"
						+"</td><td>"+StringUtil.addcommas(SelectLockNum)+"</td><td><a href='./simplejpa_modify.jsp?cl="+classNames[i]+"'>比对修改记录</a></td></tr>");
			}
		%>
		</table>
		
		<br/>
		
		<h2>数据库连接池</h2>
		
<table border='0' width='100%' cellpadding='0' cellspacing='1' bgcolor='#000000' align='center'>
<tr align='center' bgcolor='#00FFFF'>
<td>名称</td>
<td>创建时间</td>
<td>空闲数</td>
<td>链接数</td>
<td>峰值</td>
<td>最大值</td>
<td>语句缓存数量</td>
<td>请求连接次数</td>
<td>等待连接次数</td>
<td>使用超时次数</td>
</tr>

<%
ConnectionPool pool = SimpleEntityManagerFactoryHelper.getPool();
int c = pool.getNumOfCachedStatement();
String color = "#FFFFFF";
if(c > 1000) color = "#FF0000";
else if(c > 500) color = "#888888";
else if(c > 400) color = "#999999";
else if(c > 300) color = "#aaaaaa";
else if(c > 200) color = "#bbbbbb";
else if(c > 100) color = "#cccccc";
else if(c > 50) color = "#dddddd";
if(c > 25) color = "#eeeeee";

out.println("<tr align='center' bgcolor='"+color+"'>");
out.println("<td>"+pool.getAlias()+"</td>");
out.println("<td>"+DateUtil.formatDate(pool.getCreateTime(),"yyyy-MM-dd HH:mm:ss")+"</td>");
out.println("<td>"+pool.getIdleSize()+"</td>");
out.println("<td>"+pool.size()+"</td>");
out.println("<td>"+pool.getPeakSize()+"</td>");		
out.println("<td>"+pool.getMaxConn()+"</td>");
out.println("<td>"+pool.getNumOfCachedStatement()+"</td>");
out.println("<td>"+pool.getNumRequests()+"</td>");
out.println("<td>"+pool.getNumWaits()+"</td>");
out.println("<td>"+pool.getNumCheckoutTimeouts()+"</td>");

out.println("</tr>");
%></table>

		<h2>已经跟踪的类型</h2>
		
		<table id='test2' align='center' width='90%' cellpadding='1' bgcolor='#000000' cellspacing='1' border='0'>
		<tr bgcolor='#EEEEBB'><td>名称</td><td>内存中的数量</td><td>数量峰值</td><td>已创建数量</td></tr>
		<%
			Class clazz[] = ObjectTrackerService.getTrackedClass();
			for(int i = 0 ; i < clazz.length ; i++){
				ObjectCreationTracker oct = ObjectTrackerService.getObjectCreationTracker(clazz[i]);
				out.println("<tr bgcolor='#FFFFFF'><td><a href='./simplejpa_object_tracker.jsp?cl="+clazz[i].getName()+"'>"+clazz[i].getName()+"</a></td><td>"+StringUtil.addcommas(oct.currentNumInMem)
						+"</td><td>"+StringUtil.addcommas(oct.peakNumInMem)+"</td><td>"+StringUtil.addcommas(oct.totalCreatedNum)
						+"</td></tr>");
			}
		%>
		</table>
		
	</body>
</html>

