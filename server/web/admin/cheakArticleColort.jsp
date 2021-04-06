<%@page import="com.fy.engineserver.util.CompoundReturn"%>
<%@page import="com.fy.engineserver.util.datacheck.DataCheckManager"%>
<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<%
	
	String articlename = request.getParameter("articlename");
	String articlecolor = request.getParameter("articlecolor");
	if(articlename!=null && articlecolor!=null){
		DataCheckManager dcm = DataCheckManager.getInstance();
		CompoundReturn cr = dcm.isRightColorOfArticle(articlename, Integer.parseInt(articlecolor));
		out.print(cr.getBooleanValue()+"--"+cr.getStringValue());
	}
%>
<html>
<head>
<link rel="stylesheet" type="text/css" href="../css/style.css" />
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title>哦哦</title>
</head>
<body>
<table><form>
	<tr><th>物品：</th><td><input type='text' value ='<%=articlename==null?"":articlename %>' name='articlename'></td></tr>
	<tr><th>颜色：</th><td><input type='text' value ='<%=articlecolor==null?"":articlecolor %>' name='articlecolor'></td></tr>
	<tr><td><input type='submit'></td></tr>
</form></table>
</body>
</html>