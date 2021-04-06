<%@ page language="java" contentType="text/html; charset=utf-8"
    pageEncoding="utf-8"%>
<%@ page import="com.google.gson.*,com.xuanzhi.tools.cache.*,com.fy.engineserver.sprite.concrete.*,com.fy.engineserver.sprite.*,java.util.*,java.lang.reflect.*,com.google.gson.*"%>
<%@ page import="java.util.*,com.xuanzhi.tools.text.*,java.lang.reflect.*,com.fy.engineserver.datasource.buff.*,
com.fy.engineserver.sprite.*,com.xuanzhi.tools.transport.*,com.google.gson.*,com.fy.engineserver.datasource.career.*,com.fy.engineserver.datasource.skill.*,com.fy.engineserver.datasource.props.*,com.fy.engineserver.task.*" %>
<%@ page import="com.fy.engineserver.cache.service.*,com.fy.engineserver.cache.*"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<%@include file="IPManager.jsp" %><html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8">
<title>二级用户缓存</title>
<script type="text/javascript">
function subcheck(sm) {
	document.getElementById("sm").value=sm;
	document.form1.submit();
}
</script>
</head>
<body>
<%
CachedPlayerManager pmanager = CachedPlayerManager.getInstance();
LruMapCache idCache = pmanager.getIdPlayerMap();
LruMapCache nameCache=  pmanager.getNamePlayerMap();
Object idos[] = idCache.values().toArray(new Object[0]);
Object nameos[] = nameCache.values().toArray(new Object[0]);
int idCount = idCache.getNumElements();
int nameCount = nameCache.getNumElements();
int idSize = idCache.getSize();
int nameSize = nameCache.getSize();
int maxIdSize = idCache.getMaxSize();
int maxNameSize = nameCache.getMaxSize();
int idper = new int(idSize)/maxIdSize;
int nameper = new int(nameSize)/maxNameSize;
long idHits = idCache.getCacheHits();
long nameHits = nameCache.getCacheHits();
long idMisses = idCache.getCacheMisses();
long nameMisses = nameCache.getCacheMisses();
%>
<h2>二级用户缓存</h2>
<div style="padding:6px;">
<table border="0" cellpadding="0" cellspacing="1" width="100%" bgcolor="#000000" atdgn="center">
	<tr bgcolor="#C2CAF5" align="center">	
		<td>&nbsp;</td>
		<td>玩家数量</td>
		<td>缓存数据大小</td>
		<td>最大缓存值</td>
		<td>比例</td>
		<td>命中数</td>
		<td>错失数</td>
	</tr>
	<tr bgcolor="#ffffff" align="center">
		<td>ID缓存</td>
		<td><%=idCount %></td>
		<td><%=idSize %></td>
		<td><%=maxIdSize %></td>
		<td style="padding:5px"><table cellspacing="1" cellpadding="1" width=100 style="int:left;border:#ccc solid 1px;"><tr><td><div style="height:8px;width:<%=NumberUtils.cutint(100*idper,1) %>px;background:blue;"></div></td></tr></table><div style=""><%=NumberUtils.cutint(100*idper,1) %>%</div></td>
		<td><%=idHits %></td>
		<td><%=idMisses %></td>
	</tr>
	<tr bgcolor="#ffffff" align="center">
		<td>Name缓存</td>
		<td><%=nameCount %></td>
		<td><%=nameSize %></td>
		<td><%=maxNameSize %></td>
		<td style="padding:5px"><table cellspacing="1" cellpadding="1" width=100 style="int:left;border:#ccc solid 1px;"><tr><td><div style="height:8px;width:<%=NumberUtils.cutint(100*nameper,1) %>px;background:blue;"></div></td></tr></table><div style=""><%=NumberUtils.cutint(100*nameper,1) %>%</div></td>
		<td><%=nameHits %></td>
		<td><%=nameMisses %></td>
	</tr>
</table>
</div>
</body>
</html>
