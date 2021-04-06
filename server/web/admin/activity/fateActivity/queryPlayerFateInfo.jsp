<%@ page language="java" contentType="text/html; charset=utf-8"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<%
String path = request.getContextPath();
String basePath = request.getScheme()+"://"+request.getServerName()+":"+request.getServerPort()+path+"/";
%>

<%@page import="com.fy.engineserver.sprite.Player"%>
<%@page import="com.fy.engineserver.sprite.PlayerManager"%>
<%@page import="com.fy.engineserver.activity.fateActivity.FateManager"%>
<%@page import="com.fy.engineserver.activity.fateActivity.base.FateActivity"%>
<%@page import="java.util.List"%>

<%@page import="com.fy.engineserver.activity.fateActivity.FateActivityType"%>
<%@page import="com.fy.engineserver.sprite.ActivityRecordOnPlayer"%>
<%@page import="com.fy.engineserver.activity.fateActivity.FinishRecord"%>
<%@page import="com.fy.engineserver.activity.fateActivity.FateTemplate"%><html>
<head>
	<title>查看指定玩家的情缘信息</title>
</head>

<body>
	<%
	
		String name = request.getParameter("name");
		String types = request.getParameter("types");
	
		if(name == null || name.equals("")){
			
			%>
			
			<form action="">
				playerName:<input type="text" name="name"/>
				情缘(0,1,2,3):<input type="text" name="types"/>
				<input type="submit" value="submit"/>
			</form>
			
			<%
		}else{
			
			Player player = PlayerManager.getInstance().getPlayer(name.trim());
			byte type = Byte.parseByte(types);
			ActivityRecordOnPlayer arp = player.getActivityRecord(type);
			
			long[] ids = arp.getActivityId();
			out.print("主动活动:"+ids[0]+"<br/>");
			if(ids[0] > 0){
				FateActivity fa = FateManager.getInstance().getFateActivity(ids[0]);
				if(fa != null){
					out.print("状态:"+fa.getState()+"<br/>");
					long invitedId = fa.getInvitedId();
					if(invitedId > 0){
						try{
							Player invited = PlayerManager.getInstance().getPlayer(invitedId);
							out.print("主动活动邀请人"+player.getName()+"<br/>");
						}catch(Exception e){
							out.print("主动邀请人 null");
						}
					}else{
						out.print("主动活动还没邀请人<br/>");
					}
				}else{
					out.print("主动活动实体 null<br/>");
				}
			}
			
			out.print("被动活动:"+ids[1]+"<br/>");
			
			if(ids[1] > 0){
				FateActivity fa = FateManager.getInstance().getFateActivity(ids[1]);
				if(fa != null){
					out.print("状态:"+fa.getState()+"<br/>");
					long inviteId = fa.getInviteId();
					if(inviteId > 0){
						try{
							Player invite = PlayerManager.getInstance().getPlayer(inviteId);
							out.print("被动活动邀请人"+invite.getName()+"<br/>");
						}catch(Exception e){
							out.print("被动邀请人 null");
						}
					}else{
						out.print("被动活动还没邀请人<br/>");
					}
				}else{
					out.print("被动活动实体 null<br/>");
				}
			}
		}
	
	 %>
</body>
</html>
