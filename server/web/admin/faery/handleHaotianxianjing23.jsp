<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">

<%@page
	import="com.fy.engineserver.homestead.faery.service.FaeryManager"%>
<%@page import="com.fy.engineserver.homestead.faery.Faery"%>
<%@page import="com.fy.engineserver.country.manager.CountryManager"%>
<%@page import="com.fy.engineserver.homestead.cave.Cave"%>
<%@page import="java.util.Hashtable"%>
<%@page import="java.util.List"%><html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title>Insert title here</title>
</head>
<body>
<%
	FaeryManager fm = FaeryManager.getInstance();
	Faery faery = fm.getFaery(1117000000000001063l);
	if (faery != null) {
		return;
		int country=faery.getCountry();
%>

<%
	} else {
		out.print("通过仙境id获得仙境faery=null<br>");
	}
	
	Cave cave=fm.getPlayerCave().get(1117000000000009381l);
	//Cave cave=FaeryManager.caveEm.query(Cave.class,"id=?",new Object[]{1117000000000002262l},"",1,2).get(0);
	if(cave!=null){
		//out.print(cave.getId()+"<br>");
		//out.print("cave.getPethouse()"+cave.getPethouse()+"<br>");
		//out.print("cave.getPethouse().getNpc()"+cave.getPethouse().getNpc());
		//out.print("cave.getPethouse().getNpc().getName()"+cave.getPethouse().getNpc().getName());
		//out.print("cave.getPethouse().getNpc().getId()"+cave.getPethouse().getNpc().getId());
		Faery faery1=cave.getFaery();
		if(faery1!=null){
			out.print("通过仙府获得仙境faery1!=null<br>");
			int country1=faery1.getCountry();
			out.print("country1="+country1+"<br>");
			%>
			<table border="1">
				<tr>
					<td>ID</td>
					<td>名字</td>
					<td>国家</td>
					<td>当前仙府数</td>
					<td>地图</td>
				</tr>
				<tr class="<%=country1 % 2 == 1 ? "prize" : "target"%>">
					<td><%=faery1.getId()%></td>
					<td><%=CountryManager.国家名称[faery1.getCountry() - 1]%></td>
					<td><a title="<%="faery1.getCountry()=" + faery1.getCountry()%>"
						href="./faery.jsp?country=<%=country1%>&id=<%=faery1.getId()%>"><%=faery1.getName()%></a>
					</td>
					<td><%=faery1.getCaveIds().length%></td>
					<td><%=faery1.getGameName() + "<>" + faery1.getMemoryMapName()%></td>
				</tr>
			</table>
			
			<%
			Hashtable<Integer, List<Faery>> countyMap = FaeryManager.getInstance().getCountryMap();
			List<Faery> faeryList=countyMap.get(1);
			faeryList.add(faery1);
			countyMap.put(1,faeryList);
			fm.setCountryMap(countyMap);
			out.print("添加结束1<br>");
			
			Hashtable<Long, Faery> faeryMap=fm.getFaeryMap();
			faeryMap.put(1117000000000001063l,faery1);
			fm.setFaeryMap(faeryMap);
			out.print("添加结束2<br>");
		}else {
			out.print("通过仙府获得仙境faery1=null<br>");
		}
	}else{
		out.print("cave=null<br>");
	}
%>

</body>
</html>