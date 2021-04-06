<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Frameset//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-frameset.dtd">
<%@ page contentType="text/html;charset=utf-8" pageEncoding="utf-8"%>

<%@page import="com.fy.engineserver.sprite.Player"%>
<%@page import="com.fy.engineserver.sprite.PlayerManager"%>
<%@page import="com.fy.engineserver.newBillboard.BillboardsManager"%>
<%@page import="com.fy.engineserver.newBillboard.*"%>
<%@page import="java.lang.reflect.Field"%>
<%@page import="java.lang.reflect.Method"%>
<%@page import="com.fy.engineserver.util.StringTool"%>
<%@page import="com.xuanzhi.tools.text.StringUtil"%><html>
<head>
<title>刷新指定排行榜</title>
</head>
<body>

	<%
	
	
		String menu = request.getParameter("menu");
		String subMenu = request.getParameter("subMenu");
		if(menu != null && !menu.equals("") && subMenu != null && !subMenu.equals("")){
			
			Billboard bb = BillboardsManager.getInstance().getBillboard(menu, subMenu);
			if(bb != null){
				bb.setDates(null);
				out.print("刷新成功");
			}else{
				out.print("billboard null");
			}
			               
		}
	%>
	
	<form action="">
		一级:<input type="text" name="menu"/><br/>
		二级:<input type="text" name="subMenu"/><br/>
		<input type="submit" value="submit"/><br/>
	
	
	</form>
	
</body>

</html>
