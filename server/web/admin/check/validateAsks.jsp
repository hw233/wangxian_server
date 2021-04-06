<%@page import="com.fy.engineserver.validate.*"%>
<%@page import="java.lang.reflect.Array"%>
<%@page import="java.util.*"%>
<%@page import="java.util.Arrays"%>
<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">

<%
ValidateAskManager vam = ValidateAskManager.getInstance();
String ask = request.getParameter("ask");
if(ask != null) {
	String answer = request.getParameter("answer");
	ValidateAsk a = new ValidateAsk();
	a.setAsk(ask);
	a.setAnswer(answer);
	vam.addAsk(a);
}
List<ValidateAsk> asks = vam.getAsks();
%>
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title>查看当前验证问题</title>
</head>
<body>
<br><br>
<center><h2>当前验证问题</h2></center>
<table width="60%" border=1>
	<tr>
		<td width="70%" align="center">问题</td>
		<td width="30%" align="center">答案</td>
	</tr>
	<%for(int i=0; i<asks.size(); i++) {%>
	<tr>
		<td align="left"><%=asks.get(i).getAsk() %></td>
		<td align="center"><%=asks.get(i).getAnswer() %></td>
	</tr>
	<%} %>
</table>
<br><br>
<form action="validateAsks.jsp" name=f1>
	增加新问题:<input type="text" size=30 name="ask"/> 答案:<input type="text" size=10 name="answer"/>
	<input type=submit value=" 提 交 " name="sub1">
</form>
</body>
</html>