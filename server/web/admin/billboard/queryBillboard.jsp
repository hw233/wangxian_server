<%@ page contentType="text/html;charset=utf-8"%><!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Frameset//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-frameset.dtd">

<%@page import="com.fy.engineserver.sprite.Player"%>
<%@page import="com.fy.engineserver.sprite.PlayerManager"%>
<%@page import="com.fy.engineserver.activity.fateActivity.*"%>
<%@page import="java.util.*"%>

<%@page import="com.fy.engineserver.sprite.ActivityRecordOnPlayer"%>
<%@page import="com.fy.engineserver.newBillboard.BillboardsManager"%>
<%@page import="com.fy.engineserver.newBillboard.*"%>
<%@page import="com.xuanzhi.tools.simplejpa.*"%>



<%@page import="com.fy.engineserver.util.StringTool"%>
<%@page import="com.xuanzhi.tools.text.StringUtil"%><html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
<title>test</title>
</head>
<body>

	<%
		
		BillboardsManager bbm = BillboardsManager.getInstance();
		List<BillboardMenu> list = bbm.getMenuList();
		
		%>
		
		<table cellspacing="1" cellpadding="2" border="1">
		
			<tr>
 				<th>一级菜单</th>
 				<th>二级菜单</th>
 			</tr>
		
		<%
		for(BillboardMenu bm : list){
			String menu = bm.getMenuName();
			
			String[] submenus = bm.getSubMenus();
			
			String temp = "";
			for(String sub : submenus){
				temp += "<a href=\"queryOneBillboard.jsp?menu="+menu+"&sub="+sub+"\">"+sub+"</a>  ";
			}
			%>
			<tr>
				<td><%= menu%></td>
				<td><%= temp %></td>
			
			</tr>
			<%
		}
		%>
		</table>
		<%
	%>


</body>

</html>
