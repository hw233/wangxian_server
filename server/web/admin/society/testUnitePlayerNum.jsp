<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Frameset//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-frameset.dtd">
<%@ page language="java" contentType="text/html; charset=utf-8" pageEncoding="utf-8"%>
<%@page import="java.util.*"%>
<%@page import="com.fy.engineserver.sprite.Player"%>
<%@page import="com.fy.engineserver.sprite.PlayerManager"%>


<%@page import="com.fy.engineserver.society.Relation"%>
<%@page import="com.fy.engineserver.society.SocialManager"%>
<%@page import="com.fy.engineserver.unite.Unite"%>
<%@page import="com.fy.engineserver.unite.UniteManager"%><html>
<head>
<title>测试结义的人数成就</title>
</head>
<body>

<%

		String name = request.getParameter("name");
	
		if(name != null && !name.equals("")){
			Player player = PlayerManager.getInstance().getPlayer(name);
			
			Relation r = SocialManager.getInstance().getRelationById(player.getId());
			long uniteId = r.getUniteId();
			if(uniteId > 0){
				
				Unite u = UniteManager.getInstance().getUnite(uniteId);
				if(u != null){
					
					List<Long> list = u.getMemberIds();
					if(list.size() < 10){
						for(int i= 0;i< 10;i++){
							list.add(100l+i);
						}
					}
					out.print("设置 完成");
				}else{
					out.print(" unite null");
				}
			}else{
				out.print("uniteId <= 0");
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
