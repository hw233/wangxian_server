<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<%@page import="java.util.Date"%>
<%@page import="java.text.SimpleDateFormat"%>
<%@page import="java.util.List"%>
<%@page import="com.fy.boss.gm.gmuser.GameChargeRecord"%>
<%@page import="com.fy.boss.gm.gmuser.ChargeStatRecord"%>
<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<html xmlns="http://www.w3.org/1999/xhtml">
<head>    
<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
<title>玩家充值流水记录</title>
<link rel="stylesheet" href="style.css"/>
<script type="text/javascript"> 
	function query(){
		
		var username = document.getElementById('userName').value;
		var queryTime = document.getElementById('queryTime').value;
		var moneyType = document.getElementById('moneyType').value;
		var queryType = document.getElementById('querytype').options;
		var qutype;
		for(var i=0;i<queryType.length;i++){
			if(queryType[i].selected){
				qutype = queryType[i].value;
			}
		}
		if(queryTime&&username){
			window.location.replace('playerChargeRecords.jsp?queryType='+qutype+"&moneyType="+moneyType+"&username="+username+"&queryTime="+queryTime);	
		}else{
			alert("请输入您要查记录的时间或者账号！！");
		}
		
		
	}
				
</script>
</head>
<body bgcolor="#c8edc1">

<table>
	<tr><th>查询类型</th><td><select id = 'querytype'><option>--</option><option>腾讯</option></select></td></tr>
	<% 
		SimpleDateFormat ssdf = new	SimpleDateFormat("yyyy-MM-dd");	
		String currentDate = ssdf.format(new Date()); 
		String queryDate = request.getParameter("queryTime");
	%>
	<tr><th>时间</th><td><input type='text' id='queryTime' value="<%=(queryDate!=null?queryDate:currentDate) %>"></input></td></tr>
	<tr><th>账号</th><td><input type='text' id='userName'/></td></tr>
	<tr><th>货币类型</th><td><input type='text' id='moneyType'/></td></tr>
	<tr><input type='button' value='查询' onclick='query()'/></tr>
</table>

<table>
  <tr><th>账号</th><th>服务器</th><th>国家</th><th>等级</th><th>钱数</th><th>货币类型</th><th>消费/充值</th><th>原因</th><th>时间</th><th>渠道</th><th>机型</th></tr>


<% 
	ChargeStatRecord csr = ChargeStatRecord.getInstance();
	String queryType = request.getParameter("queryType");
	String userName =  request.getParameter("username");
	String moneyType = request.getParameter("moneyType");
	List<GameChargeRecord> records = null;
	records = csr.getRecordsByPage("11");
	 
	if(queryDate!=null){
		if(queryType!=null&&("腾讯").equals(queryType)){
			if(userName!=null&&userName.trim().length()>0&&"".equals(moneyType)){
				records = csr.getRecordsByUsername(userName,"QQ",queryDate);
			}else if(userName!=null&&userName.trim().length()>0&&moneyType!=null&&moneyType.trim().length()>0){
				records = csr.getRecordsByMoneyTypeAndUser(moneyType, userName,"QQ",queryDate);
			}
		}else{
			if(userName!=null&&userName.trim().length()>0&&"".equals(moneyType)){
				records = csr.getRecordsByUsername(userName,"--",queryDate);
			}else if(userName!=null&&userName.trim().length()>0&&moneyType!=null&&moneyType.trim().length()>0){
				records = csr.getRecordsByMoneyTypeAndUser(moneyType, userName,"--",queryDate);
			}
		}
	}
	
	if(records!=null){
		out.println("数量:<font color='red'>"+records.size()+"</font>");
		for(GameChargeRecord gg:records){
			String type = "";
			if(gg.getAction()==0){
				type = "充值";
			}else{
				type ="消耗";
			}
			
			SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss:SS");
			String time = sdf.format(gg.getTime());
			
%>
		<tr><td><%=gg.getUserName() %></td><td><%=gg.getFenQu() %></td><td><%=gg.getGame() %></td><td><%=gg.getGameLevel() %></td><td><%=gg.getMoney() %></td><td><%=gg.getCurrencyType() %></td><td><%=type %></td><td><%=gg.getReasonType() %></td><td><%=time %></td><td><%=gg.getQuDao() %></td><td><%=gg.getJiXing() %></td></tr>
<%		
		}
	}else{
		out.println("<font color='red'>没有符合条件的记录！</font>");
	}
%>
</table>
</body>
</html>
