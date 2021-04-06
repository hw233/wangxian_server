<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">

<%@page import="com.fy.engineserver.sprite.PlayerManager"%>
<%@page import="com.fy.engineserver.sprite.Player"%>
<%@page import="com.fy.engineserver.society.SocialManager"%><html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title>添加社会关系</title>
</head>
<body>

<%

	String addNames = request.getParameter("addName");
	String addedIds= request.getParameter("addedId");
	String types = request.getParameter("ptype");
	if(addNames == null || addedIds == null || types == null){
		out.print("参数错误<br/>");
	}else{
		String playerName = addNames.trim();
		long pid = Long.parseLong(addedIds);
		byte type = Byte.parseByte(types);
		
		SocialManager sm = SocialManager.getInstance();
		PlayerManager pm = PlayerManager.getInstance();
		Player p1 ;
		Player p2 ;
		try{
			p1 = pm.getPlayer(playerName);
			p2 = pm.getPlayer(pid);
		}catch(Exception e){
			out.print("error");
			return;
		}
		if(type == 0){

			String bln1 = sm.addFriend(p1, p2);
			String bln2 = sm.addFriend(p2, p1);
			if(!bln1.equals("")){
				out.print("邀请者"+bln1);
			}
			if(!bln2.equals("")){
				out.print("被邀请者"+bln2);
			}
			out.print("<a href=\"queryRelation.jsp\">返回</a>");
			
		}else if(type == 2){
			
			 boolean bln = sm.addBlackuser(p1, pid);
			 if(bln){
				 out.print("success");
					out.print("<a href=\"queryRelation.jsp\">返回</a>");
			 }
		}else{
			out.print("参数错误");
		}
		
		
	}

%>


</body>
</html>