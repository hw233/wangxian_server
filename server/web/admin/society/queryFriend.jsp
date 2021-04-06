<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">

<%@page import="com.fy.engineserver.sprite.PlayerManager"%>
<%@page import="com.fy.engineserver.sprite.Player"%>
<%@page import="com.fy.engineserver.society.*"%>
<%@page import="com.fy.engineserver.society.SocialManager"%>
<%@page import="java.util.List"%><html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title>查找好友</title>
</head>
<body>

<%
	//国家，省份，城市，星座 ，职业  ，最小级别  ，最大级别
	String[] st = {"-1","-1","-1","-1","3","-1","-1"};
	
	SocialManager sm = SocialManager.getInstance();
	Player player = PlayerManager.getInstance().getPlayer("FF");
	List<Player_RelatinInfo> list = sm.queryFriend(player,st);
	
	for(Player_RelatinInfo pr : list){
		out.print(pr.getName()+"||"+pr.getCareer());
		out.print("<br/>");
	}

%>


</body>
</html>