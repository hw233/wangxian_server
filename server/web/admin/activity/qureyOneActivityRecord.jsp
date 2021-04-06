<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<
<%@page import="java.util.Calendar"%>
<%@page import="com.fy.engineserver.activity.ActivityManager"%>
<%@page import="com.fy.engineserver.gametime.SystemTime"%>
<%@page import="com.fy.engineserver.activity.fireActivity.FireManager"%>
<%@page import="com.fy.engineserver.activity.ActivityThread"%>
<%@page import="com.fy.engineserver.newBillboard.monitorLog.LogRecordManager"%>
<%@page import="java.util.Date"%>
<%@page import="com.fy.engineserver.sprite.Player"%>
<%@page import="com.fy.engineserver.mail.service.concrete.DefaultMailManager.PlayerMailRequest"%>
<%@page import="com.fy.engineserver.sprite.PlayerManager"%>
<%@page import="com.fy.engineserver.activity.PlayerActivityRecord"%><html>
	<head>
		<title>查询指定的活动记录 </title>
	</head>
	
	<body>
	
		<%
			String st = request.getParameter("id");
		
			if(st != null && !st.equals("")){
				
				Player player = PlayerManager.getInstance().getPlayer(st);
				Object o =  ActivityManager.getInstance().disk.get(player.getId());
				if(o != null){
					PlayerActivityRecord pr = (PlayerActivityRecord)o;
					out.print(pr+"<br/>");
					out.print(pr.getDafengsongTime()+"<br/>");
					out.print(pr.getLastOnlineReceiveTime()+"<br/>");
					out.print(pr.isFirstReceive()+"<br/>");
					out.print(pr.isSecondReceive()+"<br/>");
					out.print(pr.isThirdReceive()+"<br/>");
					out.print(pr.isFourthReceive()+"<br/>");
					out.print(pr.isDirty()+"<br/>");
				}
				
				return;
			}
		%>
		
		<form action="">
			查看(某人的活动记录):<input type="text" name="id" />
			<input type="submit" value="submit" />
		
		</form>
		
	</body>
</html>