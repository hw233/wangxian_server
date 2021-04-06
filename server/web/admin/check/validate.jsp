<%@page import="com.fy.engineserver.validate.*"%>
<%@page import="java.lang.reflect.Array"%>
<%@page import="java.util.*"%>
<%@page import="java.util.Arrays"%>
<%@page import="com.xuanzhi.tools.text.*"%>
<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">

<%
ValidateAskManager vam = ValidateAskManager.getInstance();
List<ValidateAsk> asks = vam.askList;
String change = request.getParameter("change");
if(change != null && change.equals("true")) {
	List<ValidateAsk> alist = new ArrayList<ValidateAsk>();
	for(int i=0; i<100; i++) {
		String ss = StringUtil.randomString(2);
		ValidateAsk ask = new ValidateAsk();
		ask.setAsk(ss);
		ask.setAnswer(ss);
		alist.add(ask);
	}
	vam.askList = alist;
}
%>
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title>验证问题</title>
</head>
<body>
<br><br>
<center><h2>验证问题</h2></center>
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
<form action="validate.jsp" name=f1>
	<input type=submit value=" 更换题库 " name="sub1">
	<input type=hidden value="true" name="change">
</form>
</body>
</html>