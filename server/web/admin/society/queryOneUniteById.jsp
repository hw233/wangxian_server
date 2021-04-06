<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Frameset//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-frameset.dtd">
<%@ page language="java" contentType="text/html; charset=utf-8" pageEncoding="utf-8"%>
<%@page import="java.util.*"%>
<%@page import="com.fy.engineserver.sprite.Player"%>
<%@page import="com.fy.engineserver.sprite.PlayerManager"%>


<%@page import="com.fy.engineserver.unite.Unite"%>
<%@page import="com.fy.engineserver.unite.UniteManager"%><html>
<head>
<title>根据name查询结义</title>
</head>
<body>

<%
	String uIds = request.getParameter("uids");
	String set = request.getParameter("set");
	
	if(uIds == null){
		out.print("输入结义id");
	}else{
		
		if(set == null || set.equals("")){
			//设置
			String pids = request.getParameter("pids");
			if(pids == null){
				out.print("输入玩家Id");
			}else{
				
				Unite u = UniteManager.getInstance().getUnite(Long.parseLong(uIds));
				if(u != null){
					u.getMemberIds().remove(Long.parseLong(pids));
					u.setMemberIds(u.getMemberIds());
					UniteManager.logger.error("[后台删除结义玩家] [unite:"+u.getLogString()+"] ["+Long.parseLong(pids)+"]");
				}else{
					out.print("u null");
				}
			}
			
		}else{
			
			Unite u = UniteManager.getInstance().getUnite(Long.parseLong(uIds));
			if(u != null){
				List<Long> list =  u.getMemberIds();
				for(long id:list){
					out.print(id+"<br/>");
				}
			}else{
				out.print("unite null");
			}
			
		}
		
	}

%>


<form action="">
	
	不设置(有值只是查询):<input type="text" name="set" value="b"/>
	uniteId:<input type="text" name="uids"/>
	playerId:<input type="text" name="pids"/>
	<input type="submit" value="submit"/>
</form>



</body>

</html>
