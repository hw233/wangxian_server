<%@ page contentType="text/html;charset=utf-8" import="java.util.*,
													com.fy.engineserver.sprite.*,
													com.xuanzhi.tools.cache.*"%>
<%
LruMapCache cache = CachedPlayerManager.getInstance().getIdPlayerMap();
int objNum = cache.getNumElements();
int size = cache.getSize()/1024;
int maxSize = cache.getMaxSize()/1024;
int perc = objNum*100/maxSize;
%>
<%@include file="../IPManager.jsp" %><html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8">
<meta http-equiv="Refresh" content="5"/>
<title>缓存信息</title>
<script language="JavaScript">
<!--
-->
</script>
</head>
<body>
缓存对象数量:<%=objNum %><br>
缓存大小/最大:<%=size %>/<%=maxSize %>K: <%=perc %>%<br>
</body>
</html>
