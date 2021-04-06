<%@ page language="java" contentType="text/html; charset=utf-8"
    pageEncoding="utf-8"%>
<%@ page import="com.google.gson.*,java.util.*,com.fy.engineserver.sprite.*,
com.fy.engineserver.sprite.concrete.*,java.io.*,java.lang.reflect.*" %>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<%@include file="IPManager.jsp" %>
<%@page import="com.fy.engineserver.datasource.article.manager.ArticleManager"%>
<%@page import="com.fy.engineserver.datasource.article.data.props.PropsCategory"%><html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8">
<link rel="stylesheet" type="text/css" href="../css/common.css" />
<style type="text/css">
.tablestyle1{
width:96%;
border:0px solid #69c;
border-top:1px solid #69c;
border-right:1px solid #69c;
border-bottom:1px solid #69c;
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
border-right:0px solid #69c;
border-bottom:0px solid #69c;
border-left:0px solid #69c;
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
<title>物品</title>
<%
String  categoryName = request.getParameter("categoryName");
ArticleManager am = ArticleManager.getInstance();
PropsCategory pcs[] = am.getAllPropsCategory();
%>
</head>
<body>
<input width="68" type="button" value="返回" onclick="javascript:history.back()">
<br>
<br>
	<%
	if(categoryName != null && pcs != null){
		for(PropsCategory pc : pcs){
			if(pc != null && categoryName.equals(pc.getCategoryName())){
				%>
				<table class="tablestyle1">
				<tr>
				<td>道具类型名</td><td>使用时间间隔(cooldown)</td><td>引导类型</td><td>引导时间</td>
				</tr>
				<tr>
				<%
				String stalemateName = "";
				if(pc.getStalemateType() == 0){
					stalemateName = "无需引导";
				}
				if(pc.getStalemateType() == 1){
					stalemateName = "可以打断的引导";
				}
				if(pc.getStalemateType() == 2){
					stalemateName = "不可以打断的引导";
				}
				%>
				<td><%=categoryName %></td><td><%=pc.getCooldownLimit() %>毫秒</td><td><%=stalemateName %></td><td><%=pc.getStalemateTime() %>毫秒</td>
				</tr>
				</table>
				<%
				break;
			}
		}
	}else{
		out.println("没有该道具类型");
	}
	%>
<br/>
<input width="68" type="button" value="返回" onclick="javascript:history.back()">


</body>
</html>
