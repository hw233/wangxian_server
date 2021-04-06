<%@ page contentType="text/html;charset=utf-8" import="java.util.*,com.xuanzhi.tools.text.*,
com.fy.engineserver.sprite.*,com.fy.engineserver.sprite.concrete.*,com.fy.engineserver.core.*,com.fy.engineserver.datasource.buff.*,
com.fy.engineserver.datasource.skill.*,com.google.gson.*,java.lang.reflect.*,com.fy.engineserver.sprite.monster.*,com.fy.engineserver.datasource.props.*"%><% 
	
MonsterManager sm = MemoryMonsterManager.getMonsterManager();
Map<String,FlopSet[]> map = ((MemoryMonsterManager)sm).getMap2FlopListMap();

	%>
<%@include file="IPManager.jsp" %><html><head>
<link rel="stylesheet" href="../css/common.css"/>
<style type="text/css">
table{
border: 1px solid #69c;
border-collapse: collapse;
text-align: center;

}
td{
text-align: center;
border: 1px solid #69c;
}
.titlecolor{
background-color:#C2CAF5;
}
</style>
</HEAD>
<BODY>
<%if(map != null){
	%>
<table>
<tr class="titlecolor"><td>地图名</td><td>掉落物品名</td><td>掉率</td><td>掉落类型0共享1私有</td></tr>
<%
if(map.keySet() != null){
	for(String str : map.keySet()){
		FlopSet[] fs = map.get(str);
		int rowCount = 0;
		if(fs != null){
			for(FlopSet f : fs){
				if(f != null){
					if(f.articles != null){
						rowCount++;
					}
				}
			}
			for(int i = 0; i < fs.length; i++){
				FlopSet f = fs[i];
				if(i == 0){
					%>
					<tr>
					<td rowspan=<%=rowCount %>><%=str %></td>
					<%
					StringBuffer sb = new StringBuffer();
					if(f.articles != null){
						for(Article a : f.articles){
							if(a != null){
								sb.append("<a href='ArticleByName.jsp?articleName="+a.getName()+"'>"+a.getName()+"</a> ");
							}
						}
					}
					%>
					<td style="text-align: left;"><%=sb.toString() %></td>
					<td><%=f.floprate %>%</td>
					<td><%=f.getFlopType()%></td>
					</tr>
					<%
				}else{
			%>
			<tr>
					<%
					StringBuffer sb = new StringBuffer();
					if(f.articles != null){
						for(Article a : f.articles){
							if(a != null){
								sb.append("<a href='ArticleByName.jsp?articleName="+a.getName()+"'>"+a.getName()+"</a> ");
							}
						}
					}
					%>
					<td style="text-align: left;"><%=sb.toString() %></td>
			<td><%=f.floprate %>%</td>
			<td><%=f.getFlopType()%></td>
			</tr>
			<%
				}
			}
		}
	}
}
%>
</table>
<%} %>
</BODY></html>
