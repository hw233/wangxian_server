<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Frameset//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-frameset.dtd">
<%@ page language="java" contentType="text/html; charset=utf-8" pageEncoding="utf-8"%>
<%@page import="java.util.*"%>
<%@page import="com.fy.engineserver.sprite.Player"%>
<%@page import="com.fy.engineserver.sprite.PlayerManager"%>


<%@page import="com.fy.engineserver.society.Relation"%>
<%@page import="com.fy.engineserver.society.SocialManager"%>
<%@page import="com.fy.engineserver.unite.Unite"%>
<%@page import="com.fy.engineserver.unite.UniteManager"%>
<%@page import="com.fy.engineserver.seal.SealManager"%><html>
<head>
<title>修改测试时间</title>
</head>
<body>

<%

		String name = request.getParameter("name");
	
		if(name != null && !name.equals("")){
			
			SocialManager.测试日期 = 3;
			out.print("修改测试日期为3");
		}
	
%>


<form action="">
	随便:<input type="text" name="name"/><br/>
	<input type="submit" value="submit"/>
</form>



</body>

</html>
