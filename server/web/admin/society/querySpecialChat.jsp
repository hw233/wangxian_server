<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">

<%@page import="com.fy.engineserver.society.ChatGroupManager"%>
<%@page import="com.fy.engineserver.society.ChatGroup"%>
<%@page import="java.util.List"%>
<%@page import="com.fy.engineserver.sprite.PlayerManager"%>
<%@page import="com.fy.engineserver.sprite.Player"%><html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title>查看特定的聊天组</title>
</head>
<body>

<%
	String cgIdSt = request.getParameter("cgId");
	if(cgIdSt != null){
		long cgId = Long.parseLong(cgIdSt);
		
		ChatGroupManager cgm = ChatGroupManager.getInstance();
		PlayerManager pm = PlayerManager.getInstance();
		ChatGroup cg = cgm.getChatGroup(cgId);
		List<Long> list =  cg.getMembers();
		for(long pid : list){
			
			try{
				Player player =  pm.getPlayer(pid);
				if(pid == cgId){
					out.print("id:"+player.getId()+"name:"+player.getName()+"群主");
				}else{
					out.print("id:"+player.getId()+"name:"+player.getName());
				}
			}catch(Exception e){
				e.printStackTrace();
			}
		}
		out.print("<a href=\"queryChatGroups.jsp\">返回</a>");
	}else{
		out.print("参数错误");
	}

%>
</body>
</html>