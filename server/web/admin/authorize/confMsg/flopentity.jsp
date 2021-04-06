<%@ page language="java" contentType="text/html; charset=utf-8"
    pageEncoding="utf-8"%>
<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@page import="com.fy.engineserver.flop.FlopManager"%>
<%@page import="java.lang.Class"%>
<%@page import="java.lang.reflect.*"%>
<%@page import="java.util.*"%>
<%@page import="java.util.Map.Entry"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8">
<title>掉落物品配置</title>
<link rel="stylesheet" type="text/css" href="../css/conf.css" />
</head>
<body>
<table border=1 cellspacing=0  width=100% bordercolorlight=#333333 bordercolordark=#efefef>

		<thead>
		    <th width="40%">道具名称</th>
		    <th width="30%">颜色</th>
		    <th width="30%">数量</th>
  		</thead>
		<%
		FlopManager fm = FlopManager.getInstance();
		Class clazz = fm.getClass();
		Field field = clazz.getDeclaredField("articleFlopMaxValues");
		field.setAccessible(true);
		HashMap<String,Integer> hm = (HashMap<String,Integer>)field.get(fm);
		
		hm.put("粽子_0",3);
		hm.put("粽子_1",3);
		hm.put("粽子_2",3);
		hm.put("粽子_3",3);
		
		hm.put("礼包_0",3);
		hm.put("礼包_1",3);
		hm.put("礼包_2",3);
		hm.put("礼包_3",3);
		
		Iterator iter = hm.entrySet().iterator();
		while (iter.hasNext()) {
			Map.Entry<String,Integer> entry = (Map.Entry<String,Integer>) iter.next();
			String key = entry.getKey();
			Integer val = entry.getValue();
			String[] nc = key.split("_");
			%><tr>
			<td><%=nc[0] %></td>
			<td><%=nc[1]%></td>
			<td><%=val %></td>
		</tr><%
		}
		

		%>
		
	</table>
</body>
</html>