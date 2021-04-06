<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<!DOCTYPE html PUBLIC "-//W3C//Dth HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dth">

<%@page import="com.fy.engineserver.notice.NoticeManager"%>
<%@page import="java.util.List"%>
<%@page import="com.fy.engineserver.notice.Notice"%>
<%@page import="java.text.SimpleDateFormat"%>
<%@page import="java.util.Date"%>

<%@page import="com.fy.engineserver.sprite.Player"%>
<%@page import="com.fy.engineserver.sprite.PlayerManager"%><html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title>设置玩家的活动公告时间</title>
</head>
<body>

	<%
	
	String name = request.getParameter("name");
	if(name == null || name.equals("")){
		%>
		<form action="" method="post">
			<input type="text" name="name" />
			<input type="submit" value="提交" />
 		</form>
		<%
	}else{
		
		Player player = PlayerManager.getInstance().getPlayer(name);
		if(player.getPopActivityNoticeTime() > 0){
			player.setPopActivityNoticeTime(player.getPopActivityNoticeTime()-1l*24*60*60*1000);
			out.print("设置成功");
		}else{
			out.print("玩家"+player.getName()+"还没有弹出活动公告");
		}
		
	}
	%>

</body>
</html>