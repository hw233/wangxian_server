<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<%@page import="com.fy.engineserver.society.*"%>
<%@page import="com.fy.engineserver.sprite.PlayerManager"%>
<%@page import="com.fy.engineserver.sprite.Player"%>
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title>解散特定的聊天组</title>
</head>
<body>
<%

// exitSpecialChat.jsp?cgId="+id+"&oprator="+player.getId()+"

			ChatGroupManager cgm = ChatGroupManager.getInstance(); 
			String cgIdSt = request.getParameter("cgId");
			String opratorSt = request.getParameter("oprator");
			PlayerManager pm = PlayerManager.getInstance();
			SocialManager sm =SocialManager.getInstance();
			if(cgIdSt != null && opratorSt != null){
				
				long cgId = Long.parseLong(cgIdSt);
				long opratorId = Long.parseLong(opratorSt);
				
				ChatGroup cg = cgm.getChatGroup(cgId);
				if(cg != null){
					Player oprator = pm.getPlayer(opratorId);
					boolean bln = sm.dismissGroup(oprator, cgId);
					if(bln){
						out.print("解散成功");
						out.print("<a href=\"queryChatGroups.jsp\">返回</a>");
					}
				}else{
					out.print("参数错误，没有这个聊天组");
				}
			}else{
				out.print("参数错误");
			}
		
		
%>
</body>
</html>