<%@ page contentType="text/html;charset=utf-8" import="java.util.*,
													com.fy.engineserver.sprite.*,
													com.xuanzhi.tools.cache.*,
													com.xuanzhi.tools.text.*"%>
<%
int playerNum = ParamUtils.getIntParameter(request,"playerNum",0);
int perNum = ParamUtils.getIntParameter(request,"perNum",0);
String action = request.getParameter("act");
System.out.println(playerNum + "=" + perNum + "=" + action);
if(action != null && action.equals("start")) {
	TestCacheSystem sys = TestCacheSystem.getInstance();
	sys.setPerNum(perNum);
	sys.setPlayerNum(playerNum);
	sys.start();
} else if(action != null){
	TestCacheSystem sys = TestCacheSystem.getInstance();
	sys.stop();
}
%>
<%@include file="../IPManager.jsp" %><html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8">
<title>压力测试</title>
<script language="JavaScript">
<!--
function checksub(act) {
	if(act == 0) {
		document.getElementById("act").value = "start";
	} else {
		document.getElementById("act").value = "stop";
	}
	document.f1.submit();
}
-->
</script>
</head>
<body>
<form action="" name=f1>
	Load玩家数量：<input type=text size=10 name=playerNum value="<%=playerNum %>">
	每100ms更新量：<input type=text size=10 name=perNum value="<%=perNum %>">
	<input type=hidden name=act id=act>
	<input type=button name=sub1 value="开始" onclick="checksub(0)">
	<input type=button name=sub2 value="结束" onclick="checksub(1)">
</form>
</body>
</html>
