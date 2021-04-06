<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Frameset//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-frameset.dtd">
<%@ page language="java" contentType="text/html; charset=utf-8" pageEncoding="utf-8"%>
<%@page import="java.util.*"%>
<%@page import="com.fy.engineserver.sprite.Player"%>
<%@page import="com.fy.engineserver.sprite.PlayerManager"%>


<%@page import="com.fy.engineserver.society.SocialManager"%>
<%@page import="com.fy.engineserver.society.Relation"%>
<%@page import="com.fy.engineserver.unite.Unite"%>
<%@page import="com.fy.engineserver.unite.UniteManager"%><html>
<head>
<title>查询一个人的结义</title>
</head>
<body>

<%
	String name = request.getParameter("name");

	if(name != null && !name.equals("")){
		Player player = PlayerManager.getInstance().getPlayer(name);
		
		Relation r =  SocialManager.getInstance().getRelationById(player.getId());
		long uniteId = r.getUniteId();
		if(uniteId > 0){
			out.print("玩家的结义id:"+uniteId+"<br/>");
			Unite u = UniteManager.getInstance().getUnite(uniteId);
			if(u != null){
				
				List<Long> members = u.getMemberIds();
				for(long id:members){
					Player temp  = PlayerManager.getInstance().getPlayer(id);
					out.print("结义玩家:"+temp.getName()+"<br/>");
				}
			}else{
				out.print("unite null");
			}
			
		}else{
			out.print("没有结义"+uniteId);
		}
		return;
	}
%>


<form action="">
	
	name:<input type="text" name="name"/>
	<input type="submit" value="submit"/>
</form>



</body>

</html>
