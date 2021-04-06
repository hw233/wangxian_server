<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Frameset//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-frameset.dtd">
<%@ page language="java" contentType="text/html; charset=utf-8" pageEncoding="utf-8"%>
<%@page import="java.util.*"%>
<%@page import="com.fy.engineserver.sprite.Player"%>
<%@page import="com.fy.engineserver.sprite.PlayerManager"%>


<%@page import="com.fy.engineserver.unite.UniteManager"%>
<%@page import="com.fy.engineserver.unite.Unite"%><html>
<head>
<title>更新结义成员</title>
</head>
<body>

<%


		String name = request.getParameter("name");
	
		if(name != null && !name.equals("")){
			
			UniteManager um = UniteManager.getInstance();
			Set set =(um.mCache.keySet());
			Iterator it = set.iterator();
			Unite u = null;
			if(it.hasNext()){
				long id = (Long)it.next();
				u = (Unite)um.mCache.get(id);
				u.setMemberIds(u.getMemberIds());
				UniteManager.logger.error("[后台更新结义成员] ["+u.getLogString()+"]");
			}
			out.print("更新完成");
		}
%>


<form action="">
	
	更新:<input type="text" name="name"/>
	<input type="submit" value="submit"/>
</form>



</body>

</html>
