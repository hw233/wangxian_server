<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">

<%@page import="com.fy.engineserver.sprite.PlayerManager"%>
<%@page import="com.fy.engineserver.sprite.Player"%>
<%@page import="com.fy.engineserver.society.SocialManager"%>
<%@page import="com.fy.engineserver.society.ChatGroupManager"%>
<%@page import="com.fy.engineserver.society.Relation"%>
<%@page import="java.util.List"%>
<%@page import="com.fy.engineserver.society.ChatGroup"%>
<%@page import="java.lang.reflect.Array"%>
<%@page import="java.util.ArrayList"%><html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title>查看个人聊天组</title>
</head>
<body>

	<form action="">
	
		名字：<input type="text" name="playerName"/>
		<input type="submit" value="提交"/>
	
	</form>
	<%
		String playerName = request.getParameter("playerName");
		if(playerName == null || playerName.equals("")){
			out.print("参数错误");
		}else{
			Player player;
			PlayerManager pm = PlayerManager.getInstance();
			try{
				player = pm.getPlayer(playerName);
			}catch(Exception e){
				e.printStackTrace();
				out.print("没有此玩家");
				return;
			}
			SocialManager sm = SocialManager.getInstance();
			Relation relation =  sm.getRelationById(player.getId());
			List<Long> cgIds =  relation.getChatGrouplist();
			ChatGroupManager cgm = ChatGroupManager.getInstance();
			List<ChatGroup> cgs = new ArrayList<ChatGroup>();
			if(cgIds != null && cgIds.size() > 0){
				for(long id : cgIds){
					ChatGroup cg = cgm.getChatGroup(id);
					if(cg != null){
						cgs.add(cg);
					}else{
						out.print("聊天组为null"+id);
						return;
					}
				}
			}else{
				out.print("还没有聊天组");
				return;
			}
			
			%>
			<table>
			<tr><td>聊天组id</td><td>聊天组名</td><td>查看</td><td>退出</td><td>解散</td></tr>
			<%
				for(ChatGroup cg : cgs){
					long id = cg.getChatGroupId();
					String name = cg.getName();
					out.print("<tr><td>"+id+"</td><td>"+name+"</td><td><a href=\"querySpecialChat.jsp?cgId="+id+"\">查看</a></td><td><a href=\"exitSpecialChat.jsp?cgId="+id+"&oprator="+player.getId()+"\">退出</a></td><td><a href=\"dismissSpecialChat.jsp?cgId="+id+"&oprator="+player.getId()+"\">解散</a></td></tr>");
				}
			%>
			</table>
			<%
		}
	%>

	<h2>创建聊天组</h2>
	<form action="createChatGroup.jsp">
		玩家名：<input type="text" name="playerName"/>
		<input type="submit" value="创建聊天组"/>
	</form>

	<h2>申请聊天组</h2>
	<form action="applyChatGroup.jsp">
		群主名：<input type="text" name="hostName"/>
		申请者名：<input type="text" name="applyer"/>
		<input type="submit" value="申请"/>
	</form>
	
	<h2>邀请某人加入聊天组</h2>
	<form action="inviteChatGroup.jsp">
		群主名：<input type="text" name="hostName"/>
		被邀请者：<input type="text" name="invitedName"/>
		<input type="submit" value="邀请"/>
	</form>
	
	<h2>踢出某人从聊天组</h2>
		<form action="kickChatGroup.jsp">
		群主名：<input type="text" name="hostName"/>
		被踢出者：<input type="text" name="kickedName"/>
		<input type="submit" value="踢出"/>
	</form>

</body>
</html>