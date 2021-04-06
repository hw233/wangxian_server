<%@ page language="java" contentType="text/html; charset=utf-8"%>
<%@ page
	import="com.xuanzhi.tools.transaction.*,com.google.gson.*,java.util.*,com.fy.engineserver.sprite.*,com.xuanzhi.gameresource.*,
com.fy.engineserver.sprite.concrete.*,com.fy.engineserver.datasource.career.*,com.fy.engineserver.mail.*,com.fy.engineserver.mail.service.*,java.sql.Connection,java.sql.*,java.io.*,com.xuanzhi.boss.game.*"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">

<%@page import="com.fy.engineserver.datasource.article.data.props.*"%>
<%@page import="com.fy.engineserver.datasource.article.manager.*"%>
<%@page
	import="com.fy.engineserver.datasource.article.data.articles.*"%>
<%@page
	import="com.fy.engineserver.datasource.article.data.entity.*"%>
<%@page
	import="com.fy.engineserver.datasource.article.data.equipments.*"%>
<%@include file="../IPManager.jsp"%>
<%@page import="com.fy.engineserver.country.manager.CountryManager"%>
<%@page import="com.fy.engineserver.country.data.Country"%>
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8">
<title>国家管理器信息</title>
<link rel="stylesheet" type="text/css" href="../../css/common.css" />
<link rel="stylesheet" type="text/css" href="../../css/table.css" />
</head>
<body>
	<form action="">
		天尊id:<input type="text" name="guowangId" /> 要罢免的id:<input type="text"
			name="guanyuanId" /> 要罢免的职位:<select name="guanzhi">
			<option value="0">--请选择职位--</option>
			<option value="2">大司马</option>
			<option value="3">大将军</option>
			<option value="4">元帅</option>
			<option value="5">宰相</option>
			<option value="6">巡捕_国王</option>
			<option value="7">巡捕_元帅</option>
			<option value="8">御史大夫_国王</option>
			<option value="9">御史大夫_宰相</option>
			<option value="10">护国公</option>
			<option value="11">辅国公</option>
			<option value="12">御林卫队</option>
		</select>
		<input type="submit" value="提交"/>
	</form>
	<%
		String guowangId = request.getParameter("guowangId");
		String guanyuanId = request.getParameter("guanyuanId");
		String guanzhi = request.getParameter("guanzhi");
		Player p1 = null;
		Player p2 = null;
		if (guowangId != null && !"".equals(guowangId)) {
			p1 = PlayerManager.getInstance().getPlayer(Long.valueOf(guowangId));
		}
		if (guanyuanId != null && !"".equals(guanyuanId)) {
			p2 = PlayerManager.getInstance().getPlayer(Long.valueOf(guanyuanId));
		}
		if(p1!=null && p2!=null && !guanzhi.equals(0)){
			CountryManager.getInstance().罢免确认(p1, p2, Integer.valueOf(guanzhi));
			out.print("罢免："+ CountryManager.getInstance().得到官职名(Integer.valueOf(guanzhi)) +"<br>");
		}else{
			out.print("请填写完整<br>");
		}
		/*Player p1 = PlayerManager.getInstance().getPlayer(1143000000010303810l);
		long ids[] = new long[] { 1143000000011184216l, 1143000000011184217l, 1143000000011184219l, 1143000000011184218l, 1143000000011184220l };
		int guanzhi[] = new int[] { 5, 6, 8, 7, 9 };
		for (int i = 0; i < ids.length; i++) {
			Player p2 = PlayerManager.getInstance().getPlayer(ids[i]);
			CountryManager.getInstance().罢免确认(p1, p2, guanzhi[i]);
			out.print("罢免："+ CountryManager.getInstance().得到官职名(guanzhi[i]) +"<br>");
		}*/
	%>
	<hr>
	国王 = 天尊
	<br>大司马 = 玄阴真圣
	<br>大将军 = 纯阳真圣
	<br>元帅 = 九天司命
	<br>宰相 = 九天司空
	<br>巡捕_国王 = 真武神君
	<br>巡捕_元帅 = 翔武神君
	<br>御史大夫_国王 = 道陵星君
	<br>御史大夫_宰相 = 葛玄星君
	<br>御林卫队 = 诸天太岁
	<br>护国公 = 武曲
	<br>辅国公 = 文曲
	<br>
</body>
</html>
