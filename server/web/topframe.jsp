<%@page import="com.fy.engineserver.util.server.TestServerConfigManager"%>
<%@ page contentType="text/html;charset=utf-8"%><!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<%@page import="com.fy.engineserver.core.*,com.xuanzhi.boss.game.*"%>

<%@page import="com.fy.engineserver.sprite.PlayerManager"%>
<%@page import="com.fy.engineserver.seal.SealManager"%><html xmlns="http://www.w3.org/1999/xhtml">
<head>
<META http-equiv=Content-Type content="text/html; charset=utf-8">
<link href="css/dtree.css" type="text/css" rel="stylesheet" />
<SCRIPT src="js/dtree.js" type=text/javascript></SCRIPT>

<META content="MSHTML 6.00.2900.3157" name=GENERATOR><style type="text/css"><style type="text/css">
<!--
body {
	margin-left: 0px;
	margin-top: 16px;
	margin-right: 0px;
	margin-bottom: 0px;
	background-color: #F0E68C;
}
-->
</style>
</HEAD>
<%GameConstants gc = GameConstants.getInstance();
int online = PlayerManager.getInstance().getOnlinePlayers() != null ? PlayerManager.getInstance().getOnlinePlayers().length : 0;
%>
<body bgcolor="#F0E68C">
<center><h3><%="【服务器："+gc.getServerName()+"】 【在线："+online+"】 【封印："+SealManager.getInstance().getSealLevel()+"】 "%></h3></center>
</body>
</html>
