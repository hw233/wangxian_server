<%@ page contentType="text/html;charset=utf-8" import="java.util.*,com.xuanzhi.tools.text.*,
com.fy.engineserver.sprite.*,com.fy.engineserver.sprite.concrete.*,com.fy.engineserver.core.*,
com.fy.engineserver.datasource.skill.*"%><% 
	
	ExperienceManager em = ExperienceManager.getInstance();
	%>
<%@include file="IPManager.jsp" %><html><head>
</HEAD>
<%
String level = request.getParameter("level");
boolean can = false;
if(can && level != null) {
	int nlevel = Integer.parseInt(level);
	int needexp = Integer.parseInt(request.getParameter("needexp"));
	em.maxExpOfLevel[nlevel] = needexp;
}
 %>

<BODY>
<h2>各级别的经验值</h2><br><a href="./exp.jsp">刷新此页面</a><br><br>
<table border="0" cellpadding="0" cellspacing="1" width="100%" bgcolor="#000000" align="center">
<tr bgcolor="#FFFFFF" align="center"><td>最大级别</td><td><%=em.maxLevel %></td></tr>
<%
	for(int i = 1 ; i < em.maxExpOfLevel.length ; i++){
		%><tr bgcolor="#FFFFFF" align="center"><td>级别[<%=i%>]所需经验值</td><td><%=em.maxExpOfLevel[i] %></td></tr><%
	}
%>
</table>	

<form action="" method="post">
	<select name="level">
		<%for(int i=45; i<50; i++) {%>
		<option value="<%=i %>"><%=i %>级</option>
		<%} %>
	</select>
	需要经验值:<input type=text name="needexp" size=10>
	<input type=submit value="修改" name=sub1>
</form>
</BODY></html>
