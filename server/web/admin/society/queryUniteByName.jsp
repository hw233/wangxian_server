<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Frameset//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-frameset.dtd">
<%@ page language="java" contentType="text/html; charset=utf-8" pageEncoding="utf-8"%>
<%@page import="java.util.*"%>
<%@page import="com.fy.engineserver.sprite.Player"%>
<%@page import="com.fy.engineserver.sprite.PlayerManager"%>


<%@page import="com.fy.engineserver.unite.UniteManager"%>
<%@page import="com.fy.engineserver.unite.Unite"%><html>
<head>
<title>根据name查询结义</title>
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
				if(u.getUniteName().equals(name)){
					out.print("cache: "+u.getUniteName() +" "+u.getUniteId()+" "+u.getMemberName());
					return;
				}
			}
			u = null;
			if(u == null){
				try {
					List<Unite> temp = UniteManager.em.query(Unite.class, "uniteName = ?",new Object[]{name.trim()}, null, 1, 10);
					if(temp != null && temp.size() > 0){
						u = temp.get(0);
						for(Unite u1: temp){
							out.print("for: "+u1.getUniteName() +" "+u1.getUniteId()+" "+u1.getMemberName()+"<br/>");
						}
					}
				} catch (Exception e) {
					UniteManager.logger.error("[结义称号验证异常]",e);
				}
			}
			
			if(u != null){
				out.print("db: "+u.getUniteName() +" "+u.getUniteId()+" "+u.getMemberName());
			}else{
				out.print("null");
			}
		}
	
%>


<form action="">
	
	name:<input type="text" name="name"/>
	<input type="submit" value="submit"/>
</form>



</body>

</html>
