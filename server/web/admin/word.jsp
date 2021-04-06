<%@page import="java.util.Iterator"%>
<%@page import="java.util.List"%>
<%@page import="java.util.HashMap"%>
<%@page import="java.lang.reflect.Field"%>
<%@page import="java.lang.reflect.Method"%>
<%@page import="java.io.File"%>
<%@page import="com.xuanzhi.tools.text.WordFilter"%>
<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<%
	//String messageContent = WordFilter.getInstance().nvalidAndReplace("ri", 1, "@#\\$%^\\&\\*");
	Class clazz = WordFilter.class;
	Field wordMapF = clazz.getDeclaredField("wordMap");
	wordMapF.setAccessible(true);
	HashMap<String, List<String>> wordMap = (HashMap<String, List<String>>) wordMapF.get(WordFilter.getInstance());
	out.print("<HR>");
	out.print("wordMap:" + wordMap.size());
	out.print("<HR>");
	wordMap.clear();
	
	Field filterFileF = clazz.getDeclaredField("filterFile");
	
	filterFileF.setAccessible(true);
	
	String filterFile = (String)filterFileF.get(WordFilter.getInstance());
	
	Method m = clazz.getDeclaredMethod("loadFile", File.class);
	out.print("m:" + m + "<BR/>");
	File f = new File(filterFile);
	m.setAccessible(true);
	m.invoke(WordFilter.getInstance(), f);
	out.print(filterFile + "=======================================" + WordFilter.getInstance().getWords());
%>
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title>Insert title here</title>
</head>
<body>
</body>
</html>