<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">

<%@page import="com.fy.engineserver.society.SocialManager"%>
<%@page import="com.fy.engineserver.sprite.Player"%>
<%@page import="com.fy.engineserver.sprite.PlayerManager"%><html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title>创建聊天组</title>
</head>
<body>

<%
	String playerName = request.getParameter("playerName");
	if(playerName == null ){
		out.print("参数错误");
	}else{
		SocialManager sm = SocialManager.getInstance();
		Player player ;
		try{
			player = PlayerManager.getInstance().getPlayer(playerName);
		}catch(Exception e){
			e.printStackTrace();
			out.print("不存在此玩家"+playerName);
			return;
		}
		String result = sm.createGroup(player);
		if(result.equals("")){
			out.print("创建成功");
			out.print("<a href=\"queryChatGroups.jsp\">返回</a>");
			
		}else{
			out.print(result);
		}
	}
%>

</body>
</html>