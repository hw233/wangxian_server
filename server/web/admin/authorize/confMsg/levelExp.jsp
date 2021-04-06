<%@ page language="java" contentType="text/html; charset=utf-8"
    pageEncoding="utf-8"%>
<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@page import="com.fy.engineserver.levelExpTag.LevelExpTagManager"%>
<%@page import="java.util.ArrayList"%>
<%@page import="java.lang.Class"%>
<%@page import="java.lang.reflect.*"%>

<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8">
<title>技能等级</title>
<link rel="stylesheet" type="text/css" href="../css/conf.css" />
</head>
<body>
<table border=1 cellspacing=0  width=100% bordercolorlight=#333333 bordercolordark=#efefef>

		<thead>
		    <th width="4%">等级</th>
		    <th width="7%">家族建设任务</th>
		    <th width="4%">神农</th>
		    <th width="5%">平定四方</th>
		    <th width="5%">国内寻宝</th>
		    <th width="5%">国内情缘</th>
		    <th width="5%">国内煮酒</th>
		    <th width="5%">国外寻宝</th>
		    <th width="5%">国外情缘</th>
		    <th width="5%">国外煮酒</th>
		    <th width="5%">国外刺探</th>
		    <th width="5%">国外宝藏</th>
		    <th width="7%">经验挂机副本</th>
		    <th width="7%">家族经验副本</th>
		    <th width="7%">装备挂机副本</th>
		    <th width="7%">宠物捕捉副本</th>
  		</thead>
		<%
		LevelExpTagManager letm =  LevelExpTagManager.getInstance();
		Class clazz = letm.getClass();
		Field field = clazz.getDeclaredField("mapping");
		field.setAccessible(true);
		long[][] mapping = (long[][])field.get(letm);
		for(int i=0;i<LevelExpTagManager.level;i++){
			%>
			<tr>
			<td><%=i %></td>
			<%
			for(int j=0;j<LevelExpTagManager.activityNum;j++){
				long value = mapping[i][j];

				%><td><%=value %></td><% 
			}
			%>
			</tr>
			<%
		}
		
		

		%>
		
	</table>
</body>
</html>