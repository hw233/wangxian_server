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
<title>更新玩家的上次经验时间</title>
</head>
<body>

	<%
		
		String name= request.getParameter("name");
		String time= request.getParameter("time");
		if(name != null && !name.equals("")&&time != null && !time.equals("")){
			Player p = PlayerManager.getInstance().getPlayer(name);
			
			p.setLastUpdateExpTime(Long.parseLong(time));
			out.print("修改成功");
			
			return;
		}
	
	
	
	%>


	<form action="">
		playerName:<input type="text" name="name"/>
		time:<input type="text" name="time"/>
		<input type="submit" value="submit"/>
	
	</form>

</body>

</html>
