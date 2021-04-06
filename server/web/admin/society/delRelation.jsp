<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">

<%@page import="com.fy.engineserver.sprite.PlayerManager"%>
<%@page import="com.fy.engineserver.sprite.Player"%>
<%@page import="com.fy.engineserver.society.SocialManager"%><html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title>删除社会关系</title>
</head>
<body>

<%
	String ids = request.getParameter("id");
	String pids= request.getParameter("pid");
	String types = request.getParameter("type");
	if(ids == null || pids == null || types == null){
		out.print("参数错误<br/>");
	}else{
		long id = Long.parseLong(ids);
		long pid = Long.parseLong(pids);
		byte type = Byte.parseByte(types);
		
		PlayerManager pm = PlayerManager.getInstance();
		Player p1;
		try{
			p1 = pm.getPlayer(id);
			Player p2 = pm.getPlayer(pid);
		}catch(Exception e){
			e.printStackTrace();
			out.print("error");
			return;
		}
		SocialManager sm = SocialManager.getInstance();

		// 删除  好友  黑名单  仇人
		if(type == 0){
			int relationShip = sm.getRelationA2B(p1.getId(), pid);
			if(relationShip >0){
				out.print("不能直接删除好友，有其他关系");
				return ;
			}else{
				if(sm.removeFriend(p1,pid)){
					out.print("<a href=\"queryRelation.jsp\">返回</a>");
				}else{
					out.print("出错");
				}
			}
		}else if(type == 2){
			if(sm.removeBlackuser(p1, pid)){
				out.print("<a href=\"queryRelation.jsp\">返回</a>");
			}else{
				out.print("出错");
			}
		} else if(type == 3){
			if(sm.removeChouren(p1, pid)){
				out.print("<a href=\"queryRelation.jsp\">返回</a>");
			}else{
				out.print("出错");
			}
		}else {
			out.print("参数出错"+type);
		}
	}


%>


</body>
</html>