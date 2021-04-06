<%@ page contentType="text/html;charset=utf-8"
	import="java.util.*,com.xuanzhi.tools.text.*,com.fy.engineserver.datasource.skill.*,com.xuanzhi.tools.transport.*,com.google.gson.Gson,com.fy.engineserver.core.*,java.io.*,java.lang.reflect.*,com.fy.engineserver.datasource.career.*,com.fy.engineserver.jsp.propertyvalue.*,com.fy.engineserver.menu.question.*"%>
<%@include file="IPManager.jsp" %><html>
	<head>
	<link rel="stylesheet" type="text/css" href="../css/common.css" />
	<link rel="stylesheet" type="text/css" href="../css/table.css" />
	<% QuestionManager qm = QuestionManager.getInstance();
	String titleName = request.getParameter("titleName");
	Question q = null;
	if(qm != null && titleName != null){
		q = qm.getQuestionByName(titleName);
	}
	
	
	%>
	<style type="text/css">
	<!--
		.titlecolor{
		background-color:#C2CAF5;
		}
	-->
	</style>
	</head>
	<body>
	<br>
	<input type="button" value="返回" onclick="javascript:history.back()"><br>
	<br>
	<table>
	<tr class="titlecolor">
	<td>题目</td><td>问题描述</td><td>问题选项</td><td>正确选项</td>
	</tr>
	<%
			if(q != null){
				%>
				<tr>
				<td><%=q.getName() %></td>
				<td><%=q.getDescription() %></td>
				<%StringBuffer sb = new StringBuffer();
				String[] options = q.getOptions();
				if(options != null){
					for(int i = 0; i < options.length; i++){
						String o = options[i];
						if(sb.length() != 0){
							sb.append("\n");
						}
						sb.append("选项"+i+":"+o);
					}
				}
				
				%>
				<td><%=sb.toString() %></td>
				<td><%=q.getRightOption() %></td>
				</tr>
				<%
			}
	%>
	</table>
</BODY>
</html>
