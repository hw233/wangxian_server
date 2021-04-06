<%@page import="com.fy.engineserver.homestead.cave.Cave"%>
<%@page import="java.util.Iterator"%>
<%@page
	import="com.fy.engineserver.homestead.faery.service.FaeryManager"%>
<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">

<%
	FaeryManager faeryManager = FaeryManager.getInstance();
	for (Iterator<Long> itor = faeryManager.getPlayerCave().keySet().iterator(); itor.hasNext();) {
		Long pId = itor.next();
		Cave cave = faeryManager.getPlayerCave().get(pId);
		out.print("playerId:" + pId);
		out.print("Cave:status:" + cave.getStatus() + ",ownerId:" + cave.getOwnerId() + ",ownerName" + cave.getOwnerName() + ",index:" + cave.getIndex());
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