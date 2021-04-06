<%@ page language="java" contentType="text/html; charset=utf-8" pageEncoding="utf-8"%>
<%@page import="com.fy.engineserver.country.manager.CountryManager"%>
<%@page import="com.fy.engineserver.country.data.Country"%>
<%@page import="com.fy.engineserver.zongzu.manager.ZongPaiManager"%>

<%@page import="com.fy.engineserver.zongzu.data.ZongPai"%>
<%@page import="java.util.List"%><html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8">
<title>修改占领城市</title>

</head>
<body>

	<%
		List<ZongPai> list = ZongPaiManager.getInstance().em.query(ZongPai.class,"","",1,5000);
		if(list != null){
			out.print("宗派个数"+list.size()+"<br/>");
			for(ZongPai zp : list){
				if(zp.getSeizeCity() != null && !zp.getSeizeCity().equals("")){
					out.print(zp.getSeizeCity()+zp.getZpname()+"<br/>");
					zp.setSeizeCity("");
				}
			}
		}
		
		out.print("over<br/>");
	
	%>


</body>
</html>
