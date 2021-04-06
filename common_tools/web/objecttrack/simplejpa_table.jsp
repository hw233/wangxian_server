<%@ page contentType="text/html;charset=utf-8"%>
<%@ page
	import="java.util.*,com.xuanzhi.tools.text.*,com.xuanzhi.tools.simplejpa.impl.*,com.xuanzhi.tools.simplejpa.*,com.xuanzhi.tools.dbpool.ConnectionPool "%>
<%

String clazz = request.getParameter("cl");

String dbType = SimpleEntityManagerFactoryImpl.getDbType();


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
<% 
	if(dbType.equalsIgnoreCase("oracle")){
		SimpleEntityManagerOracleImpl<?> o = (SimpleEntityManagerOracleImpl<?>)SimpleEntityManagerFactoryHelper.getSimpleEntityManagerImpl(clazz);
	%>		

		<h2>主表数据字段</h2>
		<table id='test2' align='center' width='90%' cellpadding='1' bgcolor='#000000' cellspacing='1' border='0'>
		<tr bgcolor='#EEEEBB'><td>类名</td><td>属性名</td><td>属性类型</td><td>属性长度</td><td>字段名</td></tr>
		<%
		MetaDataField[] fs = SimpleEntityManagerFactoryHelper.getAllMetaDataField(o);
			for(int i = 0 ; i < fs.length ; i++){
				if(fs[i].inPrimaryTable){
					out.println("<tr bgcolor='#FFFFFF'><td>"+fs[i].field.getDeclaringClass().getName()+"</td><td>"
						+ fs[i].field.getName()
						+"</td><td>"+fs[i].field.getGenericType().toString().replaceAll("<","&lt;").replaceAll(">","&gt;")+"</td>"
						+"</td><td>"+fs[i].simpleColumn.length() +"</td><td>"
						+fs[i].columnNames[0].toUpperCase()+"</td></tr>");
				}
			}
		%>
		</table>
		
		<h2>副表数据字段</h2>
		<table id='test2' align='center' width='90%' cellpadding='1' bgcolor='#000000' cellspacing='1' border='0'>
		<tr bgcolor='#EEEEBB'><td>类名</td><td>属性名</td><td>属性类型</td><td>属性长度</td><td>字段名</td></tr>
		<%
			for(int i = 0 ; i < fs.length ; i++){
				if(fs[i].inPrimaryTable == false){
					for(int j = 0; j < fs[i].columnNames.length ; j++){
						out.println("<tr bgcolor='#FFFFFF'><td>"+fs[i].field.getDeclaringClass().getName()+"</td><td>"
						+ fs[i].field.getName()
						+"</td><td>"+fs[i].field.getGenericType().toString().replaceAll("<","&lt;").replaceAll(">","&gt;")+"</td>"
						+"</td><td>4000</td><td>"
						+fs[i].columnNames[j].toUpperCase()+"</td></tr>");
					}
				}
			}
		%>
		</table>
		
		<br/>
<%
 }else{
	 SimpleEntityManagerMysqlImpl<?> o = (SimpleEntityManagerMysqlImpl<?>)SimpleEntityManagerFactoryHelper.getSimpleEntityManagerImpl(clazz);
	 MysqlSectionManager<?> sectionManager = SimpleEntityManagerFactoryHelper.getMysqlSectionManager(o);
 %>		
 <h2>分区信息，单表最大行数: <%= o.maxRowNum %></h2>
 <table id='test2' align='center' width='90%' cellpadding='1' bgcolor='#000000' cellspacing='1' border='0'>
<tr bgcolor='#EEEEBB'><td>分区类型</td><td>分区名</td><td>分区索引</td><td>分区内行数</td><td>最小ID</td><td>最大ID</td></tr>
<%
	for(int i = 0 ; i < sectionManager.getSections().length ; i++){
		MysqlSection section = sectionManager.getSections()[i];
		out.println("<tr bgcolor='#FFFFFF'><td>数据分区</td><td>"+section.getName()+"</td><td>"+section.sectionIndex+"</td><td>"
				+section.currentRowNum+"</td><td>"+section.minId+"</td><td>"+section.maxId+"</td></tr>");
	}
	if(sectionManager.getNextIdleSection() != null){
		MysqlSection section = sectionManager.getNextIdleSection();
		out.println("<tr bgcolor='#FFFFFF'><td>空闲分区</td><td>"+section.getName()+"</td><td>"+section.sectionIndex+"</td><td>"
				+section.currentRowNum+"</td><td>"+section.minId+"</td><td>"+section.maxId+"</td></tr>");
	}else{
		out.println("<tr bgcolor='#FFFFFF'><td>空闲分区</td><td>--</td><td>--</td><td>--</td><td>--</td><td>--</td></tr>");
	}
%>
</table>

 <h2>主表数据字段</h2>
		<table id='test2' align='center' width='90%' cellpadding='1' bgcolor='#000000' cellspacing='1' border='0'>
		<tr bgcolor='#EEEEBB'><td>类名</td><td>属性名</td><td>属性类型</td><td>属性长度</td><td>表名</td><td>字段名</td></tr>
		<%
		MetaDataField[] fs = SimpleEntityManagerFactoryHelper.getAllMetaDataField(o);
			for(int i = 0 ; i < fs.length ; i++){
				if(fs[i].inPrimaryTable){
					out.println("<tr bgcolor='#FFFFFF'><td>"+fs[i].field.getDeclaringClass().getName()+"</td><td>"
						+ fs[i].field.getName()
						+"</td><td>"+fs[i].field.getGenericType().toString().replaceAll("<","&lt;").replaceAll(">","&gt;")+"</td>"
						+"</td><td>"+fs[i].simpleColumn.length() +"</td><td>"+fs[i].tableNameForMysql+"</td><td>"
						+fs[i].columnNameForMysql+"</td></tr>");
				}
			}
		%>
		</table>
		
		<h2>副表数据字段</h2>
		<table id='test2' align='center' width='90%' cellpadding='1' bgcolor='#000000' cellspacing='1' border='0'>
		<tr bgcolor='#EEEEBB'><td>类名</td><td>属性名</td><td>属性类型</td><td>属性长度</td><td>表名</td><td>字段名</td></tr>
		<%
			for(int i = 0 ; i < fs.length ; i++){
				if(fs[i].inPrimaryTable == false){
						out.println("<tr bgcolor='#FFFFFF'><td>"+fs[i].field.getDeclaringClass().getName()+"</td><td>"
						+ fs[i].field.getName()
						+"</td><td>"+fs[i].field.getGenericType().toString().replaceAll("<","&lt;").replaceAll(">","&gt;")+"</td>"
						+"</td><td>"+fs[i].simpleColumn.length()+"</td><td>"+fs[i].tableNameForMysql+"</td><td>"
						+fs[i].columnNameForMysql+"</td></tr>");
					
				}
			}
		%>
		</table>
 <%
 }
 %>
		
		
	</body>
</html>
