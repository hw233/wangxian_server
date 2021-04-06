<%@ page contentType="text/html;charset=UTF-8"%><!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Frameset//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-frameset.dtd">
<%@include file="inc.jsp"%>
<html xmlns="http://www.w3.org/1999/xhtml">
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8" />
<title>忘仙游戏统计平台</title>
</head>

<frameset rows="42,*" cols="*" framespacing="0" frameborder="no" border="0" bordercolor="#BADAFE">
  <frame src="topframe.html" name="topFrame" scrolling="no" noresize="noresize" id="topFrame" title="topFrame" />
  <frameset cols="180,10,*" id="sonframeset" frameborder="no">
    <frame src="leftframe.jsp" name="leftFrame" scrolling="auto" frameborder="no" noresize="noresize" id="leftFrame" title="leftFrame" />
    <frame src="jt.html" name="jtFrame" scrolling="no" noresize="noresize" id="jtFrame" title="jtFrame" />
    <frame src="mainframe.html" name="mainFrame" id="mainFrame" title="mainFrame" />
  </frameset>
</frameset>

<noframes><body>
</body>
</noframes></html>
