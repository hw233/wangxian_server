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
	<meta http-equiv="Content-Type" content="text/html; charset=utf-8">
	<title>查看某人的活动记录</title>
	<link rel="stylesheet" type="text/css" href="../css/common.css" />
</head>

<body>
	<%
	
		String name = request.getParameter("name");
		String types = request.getParameter("types");
	
		if(name == null || name.equals("")){
			
			%>
			
			<form action="">
				name:<input type="text" name="name"/>
				types:<input type="text" name="types"/>
				<input type="submit" value="submit"/>
			</form>
			
			<%
		}else{
			
			Player player = PlayerManager.getInstance().getPlayer(name.trim());
			byte type = Byte.parseByte(types);
			ActivityRecordOnPlayer arp = player.getActivityRecord(type);
			
			List<FinishRecord> list = arp.getFinishRecordList();
			if(list == null || list.size() == 0){
				out.print("没有完成记录");
			}else{
				
				Player[] ps = PlayerManager.getInstance().getOnlinePlayers();
				for(FinishRecord f: list){
					out.print(f.getPlayerId());
					for(Player p: ps){
						if(p.getId() == f.getPlayerId()){
							
							ActivityRecordOnPlayer arp2 = p.getActivityRecord(type);
							boolean bln = true;
							if(arp2 != null){
								FateTemplate ft = FateManager.getInstance().getFateActivity(player,type).getTemplate();
								bln = arp2.isCanInvited(ft);
							}
							out.print(" "+bln);
							break;
						}
					}
					
					out.print("<br/>");
				}
			}
		}
	
	 %>
</body>
</html>
