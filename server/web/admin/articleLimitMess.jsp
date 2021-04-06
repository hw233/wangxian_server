<%@ page language="java" contentType="text/html; charset=utf-8"
    pageEncoding="utf-8"%>

<%@page import="com.fy.engineserver.downcity.downcity2.DownCityManager2"%>
<%@page import="java.util.*"%>

<%@page import="com.fy.engineserver.downcity.downcity2.MapFlop"%><html>
<head>
<link rel="stylesheet" type="text/css" href="../css/common.css" />
<link rel="stylesheet" type="text/css" href="../css/table.css" />
<style type="text/css">
.titlecolor{
background-color:#C2CAF5;
}
</style>
</HEAD>
<BODY>
<h2>道具产出限制清单</h2>
<br>
<a href="./articleLimitMess.jsp">刷新此页面</a>
<br>
<br>
<table>
	<tr class="titlecolor">
		<td>物品</td>
		<td>今天产出</td>
		<td>限制个数</td>
		<td>产出地图</td>
		<td>产出者类型</td>
	</tr>
	<%
	Map<String,List<MapFlop>> mapFlops = DownCityManager2.instance.mapFlops;
	if(mapFlops == null || mapFlops.size() <= 0){
		out.print("没有产出限制配置<br>");
	}else{
		Iterator<List<MapFlop>> it = mapFlops.values().iterator();
		while(it.hasNext()){
			List<MapFlop> flops = (List<MapFlop>)it.next();
			if(flops != null && flops.size() > 0){
				for(MapFlop flop : flops){
					if(flop.getLimitFlopNum() > 0){
	%>			
						<tr>
							<td><%=flop.getArticleName() %></td>
							<%
								if(DownCityManager2.instance.getOutPutNum(flop.getArticleName()) > 0){
									out.print("<td color='yellor'>"+DownCityManager2.instance.getOutPutNum(flop.getArticleName())+"</td>");
								}else{
									out.print("<td>"+DownCityManager2.instance.getOutPutNum(flop.getArticleName())+"</td>");
								}
							%>
							<td><%=flop.getLimitFlopNum() %></td>
							<td><%=flop.getMapName() %></td>
							<td><%=flop.getMonsterType()==0?"小怪":"大BOSS"%></td>
						</tr>		
	<% 			
					}
				}
			}
		}
	}
	%>
</table>

</BODY>
</html>
