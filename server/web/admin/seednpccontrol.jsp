<%@ page contentType="text/html;charset=utf-8" import="java.util.*,com.xuanzhi.tools.text.*,
com.fy.engineserver.sprite.*,com.fy.engineserver.sprite.concrete.*,com.fy.engineserver.core.*,
com.fy.engineserver.datasource.skill.*,com.fy.engineserver.sprite.npc.*"%>
<% 
String req = request.getParameter("action");
if(req != null && req.equals("control")){
	if(SeedNPCProps.canPlant){
		SeedNPCProps.canPlant = false;
	}else{
		SeedNPCProps.canPlant = true;
	}
}
%>

<%@page import="com.fy.engineserver.datasource.props.SeedNPCProps"%><%@include file="IPManager.jsp" %><html>
<head>
<link rel="stylesheet" type="text/css" href="../css/common.css" />
<link rel="stylesheet" type="text/css" href="../css/table.css" />
<style type="text/css">
.titlecolor{
background-color:#C2CAF5;
}
td {
white-space:nowrap;
}
</style>
<script type="text/javascript">
function selectionNameForMap(obj){
	if(obj.value != ""){
		window.location.href="npcs.jsp?gameName="+obj.value;
	}
}

</script>
</HEAD>
<BODY>
<%
if(SeedNPCProps.canPlant){
	out.println("现在服务器可以种植");
}else{
	out.println("现在服务器不能种植");
}
	%>
	<form >
	<input type="hidden" name="action" value="control">
	<input type="submit" value = "<%=(SeedNPCProps.canPlant ? "关闭种植功能":"开启种植功能") %>">
	</form>
	<%

%>
	
</BODY>
</html>
