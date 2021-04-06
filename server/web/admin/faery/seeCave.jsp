<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<%
	
	String caveIdstr = request.getParameter("caveId");

	if (caveIdstr != null && !"".equals(caveIdstr.trim())) {
		Long id = Long.valueOf(caveIdstr.trim());
		Cave cave  = FaeryManager.getInstance().getCaveIdmap().get(id);
		if (cave == null) {
			out.print("<H1>仙府不存在</H1>");
		} else {
			Hashtable<Long, Faery>  map = FaeryManager.getInstance().getFaeryMap();
			out:for (Iterator<Long> itor = map.keySet().iterator();itor.hasNext();) {
				Long faeryId  = itor.next();
				Faery f = map.get(faeryId);
				for (Long cId : f.getCaveIds()) {
					if (cId != null && cId.longValue() == id) {
						out.print("找到对应的仙府:" + f.getId());
						break out;
					}
					
				}
			}
			
			out.print("<H1>仙府存在，所在仙境:"+ cave.getBuildings() + "</H1>");
			for (Iterator<Long> itor = cave.getBuildings().keySet().iterator();itor.hasNext();) {
				CaveBuilding cb = cave.getBuildings().get(itor);
				out.print(cb.getNpc().getName() + ":" + cb.getGrade() + "<BR/>");
			}
		}
	} else {
		out.print("无输入");
		caveIdstr  = "";
	}

%>

<%@page import="com.fy.engineserver.homestead.faery.service.FaeryManager"%>
<%@page import="com.fy.engineserver.homestead.cave.Cave"%>
<%@page import="java.util.Hashtable"%>
<%@page import="com.fy.engineserver.homestead.faery.Faery"%>
<%@page import="java.util.Iterator"%>
<%@page import="com.fy.engineserver.homestead.cave.CaveBuilding"%><html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title>Insert title here</title>
</head>
<body>
	<form action="./seeCave.jsp" method="post">
		caveId<input type="text" value="<%=caveIdstr  %>" name="caveId">
		<input type="submit" value="查询">
	</form>
</body>
</html>