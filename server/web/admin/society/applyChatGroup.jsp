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
<title>申请聊天组</title>
</head>
<body>

<%

		//群主名：<input type="text" name="hostName"/>
		//申请者名：<input type="text" name="applyer"/>
		String hostName = request.getParameter("hostName");
		String applyer = request.getParameter("applyer");
		
		if(hostName != null && applyer != null){
			
			PlayerManager pm = PlayerManager.getInstance();
			Player host = pm.getPlayer(hostName);
			Player apply = pm.getPlayer(applyer);
			
			ChatGroupManager cgm = ChatGroupManager.getInstance();
			SocialManager sm = SocialManager.getInstance();
			
			ChatGroup cg = cgm.getChatGroup(host.getId());
			if(cg == null){
				out.print("没有这个聊天组");
				return;
			}
			
			if(cg.check(apply.getId())){
				out.print("已经在这个聊天组");
				return;
			}
				
			if(cg.getMembers().size() >= SocialManager.GROUP_MAX_NUM){
				out.print("人数已满");
				return;
			}
			
			if(host.isOnline()){
				boolean bln = sm.applyGroup(apply, host.getId());
				if(bln){
					out.print("加入聊天分组申请发送成功");
					out.print("<a href=\"queryChatGroups.jsp\">返回</a>");
				}
			}else{
				out.print("群主不在线");
			}
		}else{
			out.print("parameter err");
		}

%>

</body>
</html>