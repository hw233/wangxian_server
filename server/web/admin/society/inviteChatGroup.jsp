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
<title>邀请某人加入聊天组</title>
</head>
<body>

<%


	//群主名：<input type="text" name="hostName"/>
	//被邀请者：<input type="text" name="invitedName"/>

		String hostName = request.getParameter("hostName");
		String invitedName = request.getParameter("invitedName");
		
		if(hostName != null && invitedName != null){
			
			PlayerManager pm = PlayerManager.getInstance();
			Player host = pm.getPlayer(hostName);
			Player invited= pm.getPlayer(invitedName);
			
			ChatGroupManager cgm = ChatGroupManager.getInstance();
			SocialManager sm = SocialManager.getInstance();
			ChatGroup cg = cgm.getChatGroup(host.getId());
		
			
			ChatGroup group = cgm.getChatGroup(host.getId());
			if(group == null){
				out.print("没有这个聊天组");
				return;
			}
			
			if(group.check(invited.getId())){
				out.print("已经在这个聊天组");
				return;
			}
			
			if(group.getMembers().size() >= SocialManager.GROUP_MAX_NUM){
				out.print("人数已满");
				return;
			}
			boolean bln = sm.groupMasterApplyToOne(host,invited,host.getId());
			
			if(bln){
				out.print("添加聊天群组成员请求发送成功");
				out.print("<a href=\"queryChatGroups.jsp\">返回</a>");
			}
		
		}else{
			out.print("parameter err");
		}


%>

</body>
</html>