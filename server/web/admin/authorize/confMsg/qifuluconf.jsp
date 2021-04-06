<%@ page language="java" contentType="text/html; charset=utf-8"
    pageEncoding="utf-8"%>
<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@page import="com.fy.engineserver.activity.clifford.manager.CliffordManager"%>
<%@page import="com.fy.engineserver.datasource.article.data.props.ArticleProperty"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8">
<title>祈福录配置信息</title>
<link rel="stylesheet" type="text/css" href="../css/conf.css" />
</head>
<body>
<table border=1 cellspacing=0  width=100% bordercolorlight=#333333 bordercolordark=#efefef>

		<thead>
		    <th width="10%">祈福录中物品的名字</th>
		    <th width="30%">开出几率值</th>
		    <th width="20%">查看几率值</th>
		    <th width="30%">包裹中的物品颜色</th>
		    <th width="10%">物品的个数</th>
  		</thead>
		<%
		
		CliffordManager cfmanager = CliffordManager.getInstance();
		ArticleProperty[][] openApss = cfmanager.开启祈福获得物品概率配置;
		ArticleProperty[][] seeApss = cfmanager.查看祈福物品概率配置;
		for(int i=0;i<openApss.length;i++){
			for(int j=0;j<openApss[i].length;j++){
				ArticleProperty ap = openApss[i][j];
		%><tr>
			<td><%=ap.getArticleName()%></td>
			<td><%=ap.getProb() %></td>
			<td><%=seeApss[i][j].getProb() %></td>
			<td><%=ap.getColor() %></td>
			<td><%=ap.getCount() %></td>
		</tr><%
			}
		}
		%>
		
	</table>
</body>
</html>