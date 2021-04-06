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
<%@page import="com.fy.engineserver.sprite.PlayerManager"%>
<%@page import="com.fy.engineserver.jiazu.Jiazu"%>
<%@page import="com.fy.engineserver.jiazu.service.JiazuManager"%>
<%@page import="com.fy.engineserver.septstation.service.SeptStationManager"%>
<%@page import="com.fy.engineserver.septstation.SeptStation"%><html>
	<head>
		<title>清楚家族篝火的次数 </title>
	</head>
	
	<body>
	
	
		<%
			String st = request.getParameter("id");
		
			if(st != null && !st.equals("")){
				Player player = PlayerManager.getInstance().getPlayer(st);
				long jiazuId = player.getJiazuId();
				
				Jiazu jz = JiazuManager.getInstance().getJiazu(jiazuId);
				
				long septId = jz.getBaseID();
				SeptStation ss = SeptStationManager.getInstance().getSeptStationById(septId);
				ss.getFireActivityNpcEntity().setLastFireTime(SystemTime.currentTimeMillis() - 1l*24*60*60*1000);
				out.print("设置成功");
			}
		%>
		
		<form action="">
			玩家name:<input type="text" name="id" />
			<input type="submit" value="submit" />
		
		</form>
		
	</body>
</html>