<%@ page contentType="text/html;charset=utf-8" import="java.util.*,com.xuanzhi.tools.text.*,
com.fy.engineserver.sprite.*,com.fy.engineserver.sprite.concrete.*,com.fy.engineserver.core.*,java.lang.reflect.*,com.google.gson.*,com.fy.engineserver.sprite.monster.*,com.fy.engineserver.downcity.*"%><%! 
	
	String aaa(String s,int len){
		StringBuffer sb = new StringBuffer();
		char chars[] = s.toCharArray();
		int c = 0;
		for(int i = 0 ; i < chars.length ; i++){
			sb.append(chars[i]);
			c++;
			if( c >= len && (chars[i] == ',' || chars[i] == '{' || chars[i] == '}' || chars[i] == ':')){
				sb.append("<br/>");
				c = 0;
			}
		}
		return sb.toString();
	}
%>
<%@include file="IPManager.jsp" %><html><head>
<link rel="stylesheet" type="text/css" href="../css/common.css" />
<link rel="stylesheet" type="text/css" href="../css/table.css" />
<style type="text/css">
.titlecolor{
background-color:#C2CAF5;
}
</style>
</HEAD>
<BODY>
<h2></h2><br><br>
<%
DownCityManager dcm = DownCityManager.getInstance();
if(dcm != null){
	ArrayList<DownCityInfo> dciList = dcm.getDciList();
	if(dciList != null){
		%>
		<table>
		<tr class="titlecolor">
		<td nowrap="nowrap">副本的名字</td><td nowrap="nowrap">副本的编号</td><td nowrap="nowrap">人数限制</td><td nowrap="nowrap">地图名</td><td nowrap="nowrap">玩家级别限制</td><td nowrap="nowrap">重置类型</td><td nowrap="nowrap">副本类型</td><td>开启后，持续多少时间后，被系统重置</td>
		</tr>
		<%
		for(DownCityInfo dci : dciList){
			if(dci != null){
				%>
				<tr>
					<td><%=dci.getName() %></td><td><%=dci.getSeqNum() %></td><td><%=dci.getPlayerNumLimit() %></td><td><%=dci.getMapName() %></td><td><%=dci.getMinPlayerLevel() %></td><td><%=dci.getResetType() %></td><td><%=dci.getDownCityType() %></td><td><%=dci.getLastingTime()+"ms" %></td>
				</tr>
				<%
			}
		}
		%>
		</table>
		<%
	}
%>



<%
}else{
	out.println("instance error");
}
%>
</BODY></html>
