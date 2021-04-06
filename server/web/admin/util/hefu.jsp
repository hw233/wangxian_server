<%@ page language="java" contentType="text/html; charset=utf-8"
    pageEncoding="utf-8"%>
<%@ page import="com.fy.engineserver.util.*,com.xuanzhi.tools.dbpool.*" %>
<%
HefuManager hm = HefuManager.getInstance();
StringBuffer message = new StringBuffer();
String operation = request.getParameter("operation");
if(operation != null && operation.equals("start")) {
	if(!hm.isRunning()) {
		PooledDataSource ds = new PooledDataSource();
		ds.setAlias("hefu");
		ds.setCheckoutTimeout(120);
		ds.setDriverClassName("oracle.jdbc.driver.OracleDriver");
		ds.setUrl("jdbc:oracle:thin:@221.179.174.52:1521:orcl11");
		ds.setUsername("qianlong47");
		ds.setPassword("1qazxsw2");
		ds.setMaxActive(100);
		ds.setIdleTimeout(120);
		ds.initialize();
		
		String p1 = request.getParameter("p1");
		String p2 = request.getParameter("p2");
		String p3 = request.getParameter("p3");
		String p4 = request.getParameter("p4");
		int intertestFlag = 0;
		if(p1 != null && p1.trim().length() > 0){
			intertestFlag |= HefuManager.IMPORT_PLAYER;
			message.append("导入玩家，");
		}
		if(p2 != null && p2.trim().length() > 0){
			intertestFlag |= HefuManager.IMPORT_PLAYER_RELEATED;
			message.append("导入二元关系，");
		}
		if(p3 != null && p3.trim().length() > 0){
			intertestFlag |= HefuManager.IMPORT_MAIL;
			message.append("导入邮件，");
		}
		if(p4 != null && p4.trim().length() > 0){
			intertestFlag |= HefuManager.IMPORT_GANG;
			message.append("导入帮派，");
		}
		hm.startImport("潜龙发布", ds, "/home/game/resin/import/jiayuan", false,intertestFlag);
	}
} else if(operation != null && operation.equals("stop")) {
	hm.stop();
}
%>
<%@include file="IPManager.jsp" %><html>
<script>
function start() {
	document.getElementById("operation").value="start";
	document.f1.submit();
}
function stop() {
	document.getElementById("operation").value="stop";
	document.f1.submit();
}
</script>
<body>
<%
	if(message.length() > 0){
		out.println("<h1>"+message.toString()+"</h1>");
	}
 %>
<form name=f1 action="hefu.jsp">
	<%if(!hm.isRunning()) {%>
	<input type='checkbox' name="p1" value="1" checked>导入玩家
	<input type='checkbox' name="p2" value="2" checked>导入二元关系
	<input type='checkbox' name="p3" value="4" checked>导入邮件
	<input type='checkbox' name="p4" value="8" checked>导入帮派
	
	<input type=button name="bt1" value="开始合服" onclick="start()">
	<%} else { %>
	<input type=button name="bt2" value="停止合服" onclick="stop()">
	<%} %>
	<input type=hidden name=operation id=operation value="">
</form>
</body>
</html>
