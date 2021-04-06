<%@ page language="java" contentType="text/html; charset=utf-8"
    pageEncoding="utf-8"%>
<%@ page import="com.google.gson.*,java.util.*,com.fy.engineserver.sprite.*,
com.fy.engineserver.sprite.concrete.*" %>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<%@include file="IPManager.jsp" %>
<%@page import="com.fy.engineserver.datasource.article.manager.ArticleManager"%>
<%@page import="com.fy.engineserver.datasource.article.data.equipments.SuitEquipment"%><html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8">
<link rel="stylesheet" type="text/css" href="../css/common.css" />
<style type="text/css">
.tablestyle1{
width:96%;
border:0px solid #69c;
border-top:1px solid #69c;
border-right:1px solid #69c;
border-bottom:0px solid #69c;
border-left:1px solid #69c;
border-collapse: collapse;
}
.tablestyle2{
width:100%;
border:0px solid #69c;
border-top:0px solid #69c;
border-right:0px solid #69c;
border-bottom:0px solid #69c;
border-left:0px solid #69c;
border-collapse: collapse;
}
td{
border:1px solid #69c;
}
.tdtable{
padding: 0px;
margin:0px;
border-top:0px solid #69c;
border-bottom:0px solid #69c;
}
.lefttd{
border-left:0px solid #69c;
}
.toptd{
border-top:0px solid #69c;
}
.righttd{
border-right:0px solid #69c;
}
.bottomtd{
border-bottom:0px solid #69c;
}
</style>
<title>套装</title>
<%
Gson gson = new Gson();
ArticleManager am = ArticleManager.getInstance();
HashMap<String,SuitEquipment> hm = am.getSuitEquipmentMap();
%>
</head>
<body>
<table class="tablestyle1">
<tr bgcolor="#C2CAF5" align="center">
<td colspan="8">服务器套装资源</td>
</tr>
<%
Set<String> set = hm.keySet();
if(set != null){
%>
<tr bgcolor="#C2CAF5" align="center">
<td>套装名</td><td>套装级别</td><td colspan="4">装备套装后的效果（增加属性值）</td><td>属性开启顺序</td>
</tr>
<%
for(String str : set){ 
	SuitEquipment se = hm.get(str);
	if(se != null){
%>
<tr align="center">
<td><%=se.getSuitEquipmentName() %></td><td><%=se.getSuitLevel() %></td><td>火:<%=se.getPropertyValue()[0]%></td><td>冰:<%=se.getPropertyValue()[1]%></td><td>风:<%=se.getPropertyValue()[2]%></td><td>雷:<%=se.getPropertyValue()[3]%></td><td><%=se.getOpenPropertyIndexs()[0]+","+se.getOpenPropertyIndexs()[1]+","+se.getOpenPropertyIndexs()[2]+","+se.getOpenPropertyIndexs()[3] %></td>
</tr>
<%
	}
} }%>
</table>
</body>
</html>
