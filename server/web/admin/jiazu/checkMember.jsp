<%@page import="com.fy.engineserver.jiazu.JiazuMember"%>
<%@page import="java.util.Set"%>
<%@page import="com.fy.engineserver.jiazu.JiazuMember4Client"%>
<%@page import="java.util.ArrayList"%>
<%@page import="com.fy.engineserver.jiazu.service.JiazuManager"%>
<%@page import="com.fy.engineserver.jiazu.Jiazu"%>
<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<%
	Jiazu jiazu = JiazuManager.getInstance().getJiazu(1177000000000000006L);
	ArrayList<JiazuMember4Client>  members = jiazu.getMember4Clients();
	ArrayList<JiazuMember4Client> remove = new ArrayList<JiazuMember4Client>();
	for (JiazuMember4Client jc : members) {
		try {
			long offlineTime = jc.getJiazuMember().getLastOffLineTime();
			out.print(jc.getPlayerName() + "	OK<BR/>" );
		} catch (Exception e) {
			out.print("<HR/>");
			out.print("异常" + jc.toString());
			out.print("<HR/>");
			remove.add(jc);
		}
	}
	for (JiazuMember4Client jc : remove) {
		jiazu.getMember4Clients().remove(jc);
			out.print("<HR/>");
		out.print("移除" + jc.toString());
			out.print("<HR/>");
	}
%>
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title>Insert title here</title>
</head>
<body>

</body>
</html>