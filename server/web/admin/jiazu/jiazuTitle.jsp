<%@page import="com.fy.engineserver.jiazu.JiazuFunction"%>
<%@page import="java.util.Iterator"%>
<%@page import="java.util.BitSet"%>
<%@page import="java.util.HashMap"%>
<%@page import="com.fy.engineserver.jiazu.JiazuTitle"%>
<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title>家族职位及其权利</title>
</head>
<body>
	<%
	
		HashMap<JiazuTitle, BitSet> set = JiazuTitle.functionMap;
		
		for (Iterator<JiazuTitle> itor = set.keySet().iterator();itor.hasNext();) {
			JiazuTitle title = itor.next();
			BitSet funSet = set.get(title);
			out.print("<HR/>");
			out.print("<font color=red>" + title.ordinal() + ":" + title.getName() + "</font><BR/>");
			out.print("<HR/>");
			int num = 1;
			for (JiazuFunction jf : JiazuFunction.values()) {
				if (funSet.get(jf.ordinal())) {
					out.print(jf.ordinal() + ":" + jf.getName() + ((num%5==0) ? "<BR/>" :"&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;"));
					num ++;
				}
			}
		}
	
	%>
</body>
</html>