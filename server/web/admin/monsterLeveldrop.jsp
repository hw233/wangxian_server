<%@ page contentType="text/html;charset=utf-8" import="java.util.*,com.xuanzhi.tools.text.*,
com.fy.engineserver.sprite.*,com.fy.engineserver.sprite.concrete.*,com.fy.engineserver.core.*,com.fy.engineserver.datasource.buff.*,
com.fy.engineserver.datasource.skill.*,com.google.gson.*,java.lang.reflect.*,com.fy.engineserver.sprite.monster.*,com.fy.engineserver.datasource.props.*"%><% 
	
MonsterManager sm = MemoryMonsterManager.getMonsterManager();
Map<int[],FlopSet[]> map = ((MemoryMonsterManager)sm).getLevel2FlopListMap();
Map<int[],DateTimePreciseMinuteSlice> mapTime = ((MemoryMonsterManager)sm).getTimeLimitlevel2FlopListMap();

	%>
<%@include file="IPManager.jsp" %><html><head>
<link rel="stylesheet" href="../css/common.css"/>
<style type="text/css">
body{
text-align: center;
}
table{
width:100%;
border: 1px solid #69c;
border-collapse: collapse;
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
<tr class="titlecolor"><td>等级区间</td><td>掉落物品名</td><td>掉率</td></tr>
<%
if(map.keySet() != null){
	for(int[] str : map.keySet()){
		FlopSet[] fs = map.get(str);
		DateTimePreciseMinuteSlice dts = mapTime.get(str);
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
					<td rowspan=<%=rowCount %>><%=str[0]+"-"+ str[1]%><%=(dts != null ?(dts.isValid(new Date())?"目前掉落":dts.getStartTime()+"到"+dts.getEndTime()):"") %></td>
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
