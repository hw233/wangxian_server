<%@page import="com.fy.engineserver.util.server.TestServerConfigManager"%>
<%@ page contentType="text/html;charset=utf-8"%><!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Frameset//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-frameset.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">
<head>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
<title><%=com.xuanzhi.boss.game.GameConstants.getInstance().getServerName() %>[<%=TestServerConfigManager.isTestServer() ? "测试服":"正式服" %>]</title>
</head>


<frameset rows="70,*" cols="*" framespacing="8" frameborder="yes" border="8" bordercolor="#C7EDCC">
  <frame src="topframe.jsp" name="topFrame" scrolling="auto" noresize="noresize" id="topFrame" title="topFrame" />

  <frameset cols="220,*" frameborder="no">
    <frame src="leftframe.jsp" name="leftFrame" scrolling="auto" frameborder="no" noresize="noresize" id="leftFrame" title="leftFrame" />
    <frame src="mainframe.html" name="mainFrame" id="mainFrame" title="mainFrame" />
  </frameset>
</frameset>
<noframes><body>
</body>
</noframes></html>
