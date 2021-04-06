<%@ page language="java" contentType="text/html; charset=utf-8"%>
<%@page import="com.fy.engineserver.activity.fateActivity.FateManager"%>
<%@page import="com.fy.engineserver.sprite.PlayerManager"%>
<%@page import="com.fy.engineserver.sprite.Player"%>
<%@page import="com.fy.engineserver.sprite.ActivityRecordOnPlayer"%>
<%@page import="com.fy.engineserver.activity.fateActivity.base.FateActivity"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">


<%@page import="javax.servlet.jsp.tagext.TryCatchFinally"%><html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title>删除情缘煮酒活动id</title>
</head>

<body>
	<%
	
		String name = request.getParameter("name");
		String types = request.getParameter("types");
		String vorved = request.getParameter("vorved");
		boolean changed = false;
		
		if(name == null || name.equals("")|| null == vorved || "".equals(vorved)){
			out.print("请输入角色名、选择活动类型和是否邀请方<br><br>");
		}else{
			try{
				Player player = PlayerManager.getInstance().getPlayer(name.trim());
				byte type = Byte.parseByte(types);
				ActivityRecordOnPlayer arp = player.getActivityRecord(type);
				
				long[] ids = arp.getActivityId();
				out.print("主动活动:"+ids[0]+"<br/>");
				out.print("被动活动:"+ids[1]+"<br/>");
				if(vorved.equals("主动")){
					if(ids[0]>0){
						arp.setInitiativeActivityId(-1,player,type);
						FateManager.logger.error("[后台删除邀请方活动id][{角色id:"+player.getId()+"}{账号:"+player.getUsername()+"}{角色名:"+player.getName()+"}] [删除的活动id:"+ids[0]+"]");
						out.print("删除成功！");
					}else{
						out.print("无可删除的活动id<br><br>");
					}
				}
				if(vorved.equals("被动")){
					if(ids[1]>0){
						arp.setPassivityActivityId(-1,player,type);
						FateManager.logger.error("[后台删除被邀请方活动id][{角色id:"+player.getId()+"}{账号:"+player.getUsername()+"}{角色名:"+player.getName()+"}] [删除的活动id:"+ids[1]+"]");
						out.print("删除成功！");
					}else{
						out.print("无可删除的活动id<br><br>");
					}
				}
			}catch(Exception e){
				out.print("无此玩家,请检查角色名！<br><br>");
			}
			
		}
	 %>
	 <form action="">
		角色名:<input type="text" name="name"/>
		<select name="types">
			<option value="0">国内情缘</option>
			<option value="1">国外情缘</option>
			<option value="2">国内煮酒</option>
			<option value="3">国外煮酒</option>
		</select>
		<input type="radio" name="vorved" value="主动"/>发出邀请方<input type="radio" name="vorved" value="被动"/>被邀请方
		<input type="submit" value="submit"/>
	</form>
</body>
</html>
