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
<%@page import="com.xuanzhi.tools.text.StringUtil"%>
<%@page import="com.fy.engineserver.newBillboard.monitorLog.LogRecordManager"%>
<%@page import="com.fy.engineserver.newBillboard.monitorLog.LogRecord"%><html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
<title>打印指定的排行榜</title>
</head>
<body>

	<%
		
		BillboardsManager bbm = BillboardsManager.getInstance();
		
		String menu = request.getParameter("menu");	
		String sub = request.getParameter("sub");	
		String name = request.getParameter("name");	
		
		if(menu != null && sub != null && name != null){
			Billboard bb = bbm.getBillboard(menu,sub);
			if(bb != null){
				LogRecord lr = LogRecordManager.getInstance().map.get(name);
				if(lr != null){
					lr.打印(bb,"后台","后台");
					out.print("over");
				}else{
					out.print("指定监控不存在");
				}
			}
			return;
		}
		
	%>


	<form action="">
		一级:<input type="text" name="menu"/> <br/>
		二级:<input type="text" name="sub"/><br/>
		那个监控:<input type="text" name="name"/><br/>
		<input type="submit" value="submit"/><br/>
	</form>

</body>

</html>
