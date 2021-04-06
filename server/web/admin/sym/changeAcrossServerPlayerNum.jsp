<%@page import="java.util.Arrays"%>
<%@page import="com.fy.engineserver.activity.across.AcrossServerManager"%>
<%@page import="com.fy.engineserver.activity.across.module.ServerAddressModule"%>
<%@page import="com.fy.engineserver.seal.SealManager"%>
<%@ page language="java" contentType="text/html; charset=UTF-8"
        pageEncoding="UTF-8"%>
<%
String aa = request.getParameter("playernum");
int temp = -1;
if (aa != null) {
	temp = Integer.parseInt(aa);
}
int sealLevel = SealManager.getInstance().getSeal().getSealLevel();
ServerAddressModule module = AcrossServerManager.acrossServers.get(sealLevel);
if (temp <= 0) {
	module.getServers()[0].setPlayerNums(2000);
} else {
	module.getServers()[0].setPlayerNums(temp);
}
out.println("[封印等级:" + sealLevel + "] [下次跨服进入服务器:" + Arrays.toString(AcrossServerManager.getInstance().getServerInfo()) + "]<br>");

%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title>修正白格</title>
</head>
<body>

</body>
</html>