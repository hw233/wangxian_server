<%@ page contentType="text/html;charset=utf-8" import="java.util.*,com.xuanzhi.tools.text.*,
com.fy.engineserver.sprite.*,com.fy.engineserver.sprite.concrete.*,com.fy.engineserver.core.*,
com.fy.engineserver.datasource.skill.*,com.fy.engineserver.datasource.repute.*,com.google.gson.Gson"%><% 
	
ReputeManager pm = ReputeManager.getInstance();
Gson gson = new Gson();
	%>
<%@include file="IPManager.jsp" %><html>
<head>
<link rel="stylesheet" type="text/css" href="../css/common.css" />
<style type="text/css">
<!--
*{margin: 0px;padding: 0px;list-style: none;}
body{
 margin:0px;
 padding: 0px;
 text-align: center;
 table-layout: fixed;
 word-wrap: break-word;
 list-style: none;
}
.titlecolor{
background-color:#C2CAF5;
}
.tableclass{
width:150%;
border:1px solid #69c;
color:blue;
border-collapse: collapse;
}
.tableclass1td{
border:1px solid blue;
overflow:auto;
margin-left: 0px;
margin-right: 0px;
padding-left: 0px;
padding-right: 0px;
}
.tableclass2{
width:100%;
border:0px solid #69c;
border-collapse: collapse;
}
td{
border:1px solid #69c;
word-wrap: break-word;
}
.td{
border-top:0px solid #69c;
border-right:0px solid #69c;
border-bottom:0px solid #69c;
}
.toptd{border-top:0px solid #69c;}
.lefttd{border-left:0px solid #69c;}
.tdlittlewidth{
width:50px;
}
.tdwidth{
width:25%;
}
.tdForBlank{
height:20px;
border-top:1px dotted #69c;
border-right:1px dotted #69c;
border-bottom:1px dotted #69c;
border-left:1px dotted #69c;
}
-->
</style>
</HEAD>
<BODY>
<h2>声望</h2><br><a href="npcs.jsp">刷新此页面</a>
<%if(pm != null){
	Map<String, ReputeDefine> mp = pm.getReputeDefineMap();
	%>
	<table class="tableclass">
	<tr class="titlecolor">
<td nowrap="nowrap">声望类名</td><td>最大级别</td><td>紫薇宫初始等级</td><td>日月盟初始等级</td><td>客户端显示声望的顺序</td><td>类型</td><td nowrap="nowrap">升级所需声望值对照表</td><td nowrap="nowrap">等级-称谓对照表</td><td>描述</td>
</tr>
<%if(mp != null && mp.keySet() != null){
	for(String str :mp.keySet()){
		ReputeDefine rd = mp.get(str);
		if(rd != null){
			%>
			<tr>
		<td><%=rd.getName() %></td>
		<td><%=rd.getMaxLevel() %></td>
		<td></td>
		<td></td>
		<td><%=rd.getShowOrder() %></td>
		<td><%=rd.getType() %></td>
		<td><%
		int[] intValues = rd.getLevelUpTable();
		if(intValues != null){
			%>
			<table class="tableclass2">
			<tr>
			
			
			<%
			for(int i = 0; i < intValues.length; i++){
				if(i == 0){
					%>
					<td class="td lefttd" nowrap><%=(i+1)+"级:"+intValues[i] %></td>
					<%
				}else{
					%>
					<td class="td" nowrap><%=(i+1)+"级:"+intValues[i] %></td>
					<%
				}
			}
			%>
			</tr>
			</table>
			<%
		}
		
		%></td>
		<td><%
		String[] titles = rd.getTitleTable();
		if(titles != null){
			%>
			<table class="tableclass2">
			<tr>
			
			
			<%
			for(int i = 0; i < titles.length; i++){
				if(i == 0){
					%>
					<td class="td lefttd" nowrap><%=(i+1)+"级:"+titles[i] %></td>
					<%
				}else{
					%>
					<td class="td" nowrap><%=(i+1)+"级:"+titles[i] %></td>
					<%
				}
			}
			%>
			</tr>
			</table>
			<%
		}
		
		
		
		%></td>
		<td><%=rd.getDescription() %></td>
		</tr>
		<%}
	}
} %>

	
	</table>
	
	<%		

} %>
</BODY></html>
