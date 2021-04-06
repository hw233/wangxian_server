<%@ page language="java" contentType="text/html; charset=utf-8"
    pageEncoding="utf-8"%>
<%@include file="../IPManager.jsp" %>

<%@page import="com.fy.engineserver.cityfight.CityFightManager"%>
<%@page import="com.fy.engineserver.country.manager.CountryManager"%><html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8">
<title></title>
<script language="JavaScript">
<!--
-->
</script>
<link rel="stylesheet" type="text/css" href="../../css/common.css">
<link rel="stylesheet" type="text/css" href="../../css/table.css">
<script type="text/javascript">
function qiehuan(){
	document.f2.submit();
}
</script>
</head>
<body>
<%
CityFightManager cfm = CityFightManager.getInstance();
String 申请时间 = request.getParameter("shenqing");
String 领取奖励时间 = request.getParameter("lingqu");
String 开战时 = request.getParameter("kaizhanshi");
String 开战分 = request.getParameter("kaizhanfen");
String 关战时 = request.getParameter("guanzhanshi");
String 关战分 = request.getParameter("guanzhanfen");
String 繁荣度 = request.getParameter("fanrongdu");
String 消耗 = request.getParameter("yinzi");
String npc存活时间 = request.getParameter("npcshuaxin");
if(申请时间  != null){
	CityFightManager.申请时间 = Integer.parseInt(申请时间);
}
if(领取奖励时间 != null){
	CityFightManager.领取奖励时间 = Integer.parseInt(领取奖励时间);
}
if(开战时  != null){
	CityFightManager.开战时 = Integer.parseInt(开战时);
}
if(开战分  != null){
	CityFightManager.开战分= Integer.parseInt(开战分);
}
if(关战时  != null){
	CityFightManager.关战时 = Integer.parseInt(关战时);
}
if(关战分  != null){
	CityFightManager.关战分 = Integer.parseInt(关战分);
}
if(繁荣度   != null){
	CityFightManager.繁荣度  = Integer.parseInt(繁荣度 );
}
if(消耗   != null){
	CityFightManager.消耗  = Integer.parseInt(消耗 );
}
if(npc存活时间   != null){
	CityFightManager.npc存活时间  = Integer.parseInt(npc存活时间 );
}
String action = request.getParameter("action");
if(action != null && action.equals("qiehuan")){
	cfm.切换天的操作();
}
%>
<table>
<tr><td>国家</td><td>城市信息一览</td></tr>
<%
for(int i = 1; i <= 3; i++){
	out.println("<tr><td>"+CountryManager.得到国家名(i)+"</td>");
	String cityFightInfo = cfm.得到占领城市及帮派信息(i);
	out.println("<td>"+cityFightInfo+"</td></tr>");
}
%>
</table>
<form name="f1">
<table>
<tr><td>城战数据</td></tr>
<tr><td>申请时间(小时) : <input type="text" name="shenqing" id="shenqing" value="<%=cfm.申请时间 %>">时</td></tr>
<tr><td>领取奖励时间(小时) : <input type="text" name="lingqu" id="lingqu" value="<%=cfm.领取奖励时间 %>"></input>时</td></tr>
<tr><td>开战时(小时) : <input type="text" name="kaizhanshi" id="kaizhanshi" value="<%=cfm.开战时 %>"></input>时</td></tr>
<tr><td>开战分(分钟) : <input type="text" name="kaizhanfen" id="kaizhanfen" value="<%=cfm.开战分 %>"></input>分</td></tr>
<tr><td>关战时(小时)  : <input type="text" name="guanzhanshi" id="guanzhanshi" value="<%=cfm.关战时 %>"></input>时</td></tr>
<tr><td>关战分(分钟) : <input type="text" name="guanzhanfen" id="guanzhanfen" value="<%=cfm.关战分 %>"></input>分</td></tr>
<tr><td>繁荣度: <input type="text" name="fanrongdu" id="fanrongdu" value="<%=cfm.繁荣度 %>"></input></td></tr>
<tr><td>消耗: <input type="text" name="yinzi" id="yinzi" value="<%=cfm.消耗 %>"></input></td></tr>
<tr><td>npc存活时间(毫秒，可以把npc刷新没，结束战斗): <input type="text" name="npcshuaxin" id="npcshuaxin" value="<%=cfm.npc存活时间 %>"></input></td></tr>
<tr><td><input type="submit" value="修改"></td></tr>
</table>
</form>
<form name="f2">
<input type="hidden" name="action" value="qiehuan">
<input type="submit"  value="切换天的操作">
</form>
</body>
</html>
