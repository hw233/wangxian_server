<%@ page contentType="text/html;charset=utf-8"%><!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Frameset//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-frameset.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">
<head>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
<title>faq</title>
</head>

<frameset rows="42,*" cols="*" framespacing="0" frameborder="no" border="0" bordercolor="#BADAFE">
  <frame src="../../faq_module/topframe.jsp" name="topFrame" scrolling="no" noresize="noresize" id="topFrame" title="topFrame" />
  <frameset cols="180,10,*" id="sonframeset" frameborder="no">
	  <frameset rows="*,1%">
	    <frame src="../../faq_module/faq_list.jsp" name="leftFrame" scrolling="auto" frameborder="no" noresize="noresize" id="leftFrame" title="leftFrame" />
	    <frame src="../../faq_module/session.jsp"  name="sesFrame" scrolling="auto" frameborder="no" noresize="noresize" id="sesFrame" title="sesFrame" /> 
	  </frameset>
      <frame src="../../faq_module/jt.html" name="jtFrame" scrolling="no" noresize="noresize" id="jtFrame" title="jtFrame" />
      <frame src="../../faq_module/mainframe.html" name="mainFrame" id="mainFrame" title="mainFrame" />
  </frameset>
</frameset>

<noframes><body>
</body>
</noframes></html>

