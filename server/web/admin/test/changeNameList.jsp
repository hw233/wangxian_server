<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<%
	DiskCache sameNamePlayerCache = UnitedServerManager.getInstance().getSameNamePlayerCache();
	String changeNameListKey = UnitedServerManager.changeNameListKey;
	List<Long> changeList = new ArrayList<Long>();
	if (sameNamePlayerCache != null) {
		if (sameNamePlayerCache.get(changeNameListKey) != null) {
			changeList = (List<Long>) sameNamePlayerCache.get(changeNameListKey);
			for (Long id : changeList) {
				out.print(id + "<br>");
			}
			out.print("总数:" + changeList.size() + "<br>");
		} else {
			out.print("本服不存在需改名列表<br>");
		}

		String idStr = request.getParameter("id");
		if (idStr != null && !"".equals(idStr)) {
			long id = Long.valueOf(idStr);
			Player p = PlayerManager.getInstance().getPlayer(id);
			if (p != null) {
				changeList.add(id);
				sameNamePlayerCache.put(changeNameListKey, (Serializable) changeList);
				sameNamePlayerCache.put(id, p.getName());
				out.print("已添加");
			} else {
				out.print("角色不存在");
			}
			out.print("<hr>添加后:");
			changeList = (List<Long>) sameNamePlayerCache.get(changeNameListKey);
			for (Long ids : changeList) {
				out.print(ids + "<br>");
			}
			out.print("总数:" + changeList.size() + "<br>");
		}

	} else {
		out.print("本服不存在改名列表<br>");
	}
%>

<%@page import="com.fy.engineserver.uniteserver.UnitedServerManager"%>
<%@page import="com.xuanzhi.tools.cache.diskcache.DiskCache"%>
<%@page import="java.util.List"%>
<%@page import="java.io.Serializable"%>
<%@page import="com.fy.engineserver.sprite.PlayerManager"%>
<%@page import="com.fy.engineserver.sprite.Player"%>
<%@page import="java.util.ArrayList"%><html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title>合服改名列表</title>
</head>
<body>
<form><input type=text name="id" value=""> <input
	type=submit name="提交"></form>
</body>
</html>