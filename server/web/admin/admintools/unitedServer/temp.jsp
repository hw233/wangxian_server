<%@page import="com.fy.engineserver.newtask.TaskEntity"%>
<%@page import="java.lang.reflect.Method"%>
<%@page import="com.fy.engineserver.uniteserver.UnitedServer"%>
<%@page import="java.util.Map"%>
<%@page import="java.lang.reflect.Field"%>
<%@page import="com.fy.engineserver.uniteserver.UnitedServerManager"%>
<%@ page language="java" contentType="text/html; charset=utf-8"
    pageEncoding="utf-8"%>
<%
	 UnitedServerManager unitedServerManager = UnitedServerManager.getInstance();
	 Field f = UnitedServerManager.class.getDeclaredField("unitedServerMap");
	 f.setAccessible(true);
	 
	 Map<String, UnitedServer> unitedServerMap =  ( Map<String, UnitedServer>) f.get(unitedServerManager);
	 UnitedServer unitedServer  =  unitedServerMap.remove("雪域冰城");
	 
	 if(unitedServer != null)
	 {
		 Method method = UnitedServerManager.class.getDeclaredMethod("merge", new Class<?>[] { Class.class} ); 
		 method.setAccessible(true);
		 Object result = method.invoke(unitedServerManager, new Object[] { TaskEntity.class });
		 
		 out.println("调用方法成功！");
	 }
	 else
	 {
		 out.println("服务器不存在");
	 }
	 
	 
		



%>        
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8">
<title></title>
</head>
<body>
	
</body>
</html>