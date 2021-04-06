<%@page import="java.util.List"%>
<%@page
	import="com.fy.engineserver.homestead.faery.service.QuerySelect"%>
<%@page import="java.util.Iterator"%>
<%@page import="com.fy.engineserver.homestead.cave.Cave"%>
<%@page import="java.util.Hashtable"%>
<%@page
	import="com.fy.engineserver.homestead.faery.service.FaeryManager"%>
<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<%
	Hashtable<Long, Cave> ad = FaeryManager.getInstance().getCaveIdmap();
	int index = 0;
	for (Long id : ad.keySet()) {
		Cave cave = ad.get(id);
		out.print("[" + (index++) + "]ID=" + id + ",caveOName:" + cave.getOwnerName() + "<BR/>");
	}
	out.print("<HR>");
	out.print("----");
	out.print("<HR>");
	out.print("<HR>");
	index = 0;
	QuerySelect<Cave> queryCave = new QuerySelect<Cave>();
	List<Cave> allCaves = queryCave.selectAll(Cave.class, " status = ?", new Object[] { 0 }, null, FaeryManager.caveEm);
	Hashtable<Long, Cave> caveIdmap = new Hashtable<Long, Cave>();
	for (Cave cave : allCaves) {
		caveIdmap.put(cave.getId(), cave);
		out.print("[" + (index++) + "]ID=" + cave.getId() + ",caveOName:" + cave.getOwnerName() + "<BR/>");
	}
	out.print("size:" + caveIdmap.size());
	index = 0;
	out.print("<HR>");
	out.print("----");
	out.print("<HR>");
	out.print("<HR>");
	for (Long id : ad.keySet()) {
		if (!caveIdmap.containsKey(id)) {
			out.print("[" + (++index) + "]不存在的ID:" + id + "<BR/>");
		}
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