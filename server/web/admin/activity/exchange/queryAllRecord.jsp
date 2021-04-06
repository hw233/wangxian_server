<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">

<%@page import="com.fy.engineserver.sprite.Player"%>
<%@page import="com.fy.engineserver.sprite.PlayerManager"%>
<%@page import="com.fy.engineserver.activity.ActivityManager"%>
<%@page import="com.fy.engineserver.activity.ExchangeActivityRecord"%>
<%@page import="com.fy.engineserver.newBillboard.monitorLog.LogRecordManager"%>
<%@page import="java.util.Date"%>
<%@page import="java.util.Hashtable"%>
<%@page import="java.util.Map"%><html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title>查看所有的兑换记录</title>
</head>
<body>

	<%
		String name = request.getParameter("name");
		if(name != null && !name.equals("")){
			
			Hashtable<Long, ExchangeActivityRecord> hb = ActivityManager.getInstance().exchangeRecordMap;
			out.print("总数"+hb.size()+"<br/>");
			for(Map.Entry<Long,ExchangeActivityRecord> en :hb.entrySet()){
				ExchangeActivityRecord er = en.getValue();
				if(er != null){
					out.print("酒次数:"+er.addBeerNum+"<br/>");
					out.print("酒时间:"+LogRecordManager.sdf.format(new Date(er.exchangeBeerTime)) +"<br/>");
					out.print("帖次数:"+er.addTieNum+"<br/>");
					out.print("帖时间:"+LogRecordManager.sdf.format(new Date(er.exchangeTieTime)) +"<br/>");
					out.print(en.getKey()+"xxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxx<br/>");
				}else{
					out.print("er null");
				}
			}
			return ;
		}
	%>

	<form action="">
		玩家name:<input type="text" name="name"/ >
		<input type="submit" value="submit"> 
	</form>
</body>
</html>