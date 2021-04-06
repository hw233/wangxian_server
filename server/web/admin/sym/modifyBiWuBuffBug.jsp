<%@page import="com.fy.engineserver.datasource.buff.BuffTemplateManager"%>
<%@page import="com.fy.engineserver.datasource.buff.BuffTemplate"%>
<%@page import="com.fy.engineserver.datasource.language.Translate"%>
<%@ page language="java" contentType="text/html; charset=UTF-8"
        pageEncoding="UTF-8"%>
<%
	BuffTemplate bt = BuffTemplateManager.getInstance().getBuffTemplateByName(Translate.沉默说话);
	if(bt!=null){
		boolean old_isAdvantageous = bt.isAdvantageous();
		bt.setAdvantageous(true);
		out.print("干死卖锭的old:"+old_isAdvantageous+"--new:"+bt.isAdvantageous());
	}
%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title>干死卖锭的</title>
</head>
<body>

</body>
</html>