<%@page import="java.util.Iterator"%>
<%@page import="java.util.HashMap"%>
<%@page import="com.fy.engineserver.datasource.language.Translate"%>
<%@page
	import="com.fy.engineserver.datasource.language.MultiLanguageTranslateManager"%>
<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<%
	String baseStr = request.getParameter("baseStr");
	String translatedStr = null;
	if (baseStr != null && !"".equals(baseStr)) {
		translatedStr = MultiLanguageTranslateManager.doNothing(baseStr);
	} else {
		baseStr = "";
		translatedStr = "";
	}
%>
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title>Insert title here</title>
</head>
<body>
	<form action="./translate.jsp" method="post">
		<table style="font-size: 12px;">
			<tr>
				<td>输入要翻译的文字:<BR /> <textarea name="baseStr" rows="5"
						cols="40"><%=baseStr%></textarea></td>
				<td>翻译后的文字:<BR /> <textarea name="translatedStr" rows="5"
						cols="40"><%=translatedStr%></textarea></td>
			</tr>
			<tr>
				<td colspan="2"><input type="submit" value="翻译" align="middle">
				</td>
			</tr>
			<tr></tr>
		</table>
	</form>
</body>
</html>