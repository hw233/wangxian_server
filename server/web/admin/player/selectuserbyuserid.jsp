<%@ page language="java" contentType="text/html; charset=utf-8" pageEncoding="utf-8"%>
<%@include file="../IPManager.jsp" %><html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8">
<title>selectbyuserid.jsp</title>
</head>
<body>
<form method="post" action="../resultbyid.jsp">  
	<table border="0" cellpadding="0" cellspacing="1" bgcolor="#000000">
		<tr bgcolor="#FFFFFF">
			<td>用户名:</td><td><input type="text" name="text"></td>
		</tr>
		<tr bgcolor="#FFFFFF" align="center">
			<td><input type="submit" value="提交"></td>
			<td><input type="reset" name="重置"></td>
		</tr>
	</table>
</form>
</body>
</html>
