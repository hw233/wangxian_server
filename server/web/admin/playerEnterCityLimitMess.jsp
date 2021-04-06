<%@ page language="java" contentType="text/html; charset=utf-8"
    pageEncoding="utf-8"%>
<%@page import="com.fy.engineserver.downcity.downcity2.MapFlop"%>
<%@page import="com.fy.engineserver.sprite.Player"%>
<%@page import="com.fy.engineserver.sprite.PlayerManager"%><html>
<head>
<link rel="stylesheet" type="text/css" href="../css/common.css" />
<link rel="stylesheet" type="text/css" href="../css/table.css" />
<style type="text/css">
.titlecolor{
background-color:#C2CAF5;
}
</style>
</HEAD>
<BODY>
<h2>查询玩家进入副本次数限制信息</h2>
<br>
<form>
<table>
<%
String pname= request.getParameter("pname");
if(pname != null){
	try{
		Player p = PlayerManager.getInstance().getPlayer(pname);
		out.print("玩家:"+pname+"<br>");	
		out.print("今天进入次数:"+p.getCityEnterLimitNum()[0]+"<br>");	
		out.print("本周进入次数:"+p.getCityEnterLimitNum()[1]+"<br>");	
		out.print("今天第一次进入时间:"+p.getCityEnterLimitDate()[0]+"<br>");	
		out.print("本周第一次进入时间:"+p.getCityEnterLimitDate()[1]+"<br>");	
	}catch(Exception e){
		out.print("玩家不存在:"+pname+"<br>");
	}
	
}
%>
<input type='text' name="pname"></input>
<input type='submit' name='查询'></input>
</table>
</form>
</BODY>

</html>
