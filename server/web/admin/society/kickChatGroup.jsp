<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<%@page import="com.fy.engineserver.sprite.Player"%>
<%@page import="com.fy.engineserver.sprite.PlayerManager"%>
<%@page import="com.fy.engineserver.society.*"%>
<%@page import="com.fy.engineserver.society.ChatGroupManager"%>

<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title>踢出某人从聊天组</title>
</head>
<body>

<%

//群主名：<input type="text" name="hostName"/>
//被踢出者：<input type="text" name="kickedName"/>

		String hostName = request.getParameter("hostName");
		String kickedName = request.getParameter("kickedName");
		
		if(hostName != null && kickedName != null){
			
			PlayerManager pm = PlayerManager.getInstance();
			Player host = pm.getPlayer(hostName);
			Player kicked= pm.getPlayer(kickedName);
			
			ChatGroupManager cgm = ChatGroupManager.getInstance();
			SocialManager sm = SocialManager.getInstance();
			ChatGroup cg = cgm.getChatGroup(host.getId());
		
			boolean bln = sm.delGroupMember(host, kicked, host.getId());
			if(bln){
				out.print("踢出某成员成功");
				out.print("<a href=\"queryChatGroups.jsp\">返回</a>");
			}
			
		}else{
			out.print("parameter err");
		}

%>

</body>
</html>