<%@page import="com.xuanzhi.tools.simplejpa.SimpleEntityManager"%>
<%@page import="com.xuanzhi.tools.simplejpa.impl.DefaultSimpleEntityManagerFactory"%>
<%@page import="com.xuanzhi.tools.text.ParamUtils"%>
<%@page import="com.xuanzhi.boss.game.GameConstants"%>
<%@page import="java.util.List"%>
<%@page import="java.util.ArrayList"%>
<%@page import="com.fy.engineserver.uniteserver.UnitedServer"%>
<%@page import="com.fy.engineserver.uniteserver.UnitedServerManager"%>
<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<%
	List<UnitedServer> serverList = new ArrayList<UnitedServer>();
	
	String mainServerName = request.getParameter("mainServerName");
	String mainServerConf = request.getParameter("mainServerConf");
	String countInfos = "";
	boolean isDo = ParamUtils.getBooleanParameter(request, "isDo");
	if(isDo)
	{
		mainServerName= GameConstants.getInstance().getServerName();
		
		
		
		DefaultSimpleEntityManagerFactory factory = new DefaultSimpleEntityManagerFactory(mainServerConf);
		
		
		for(Class<?> c :UnitedServerManager.uniteClasses){
			SimpleEntityManager sem = factory.getSimpleEntityManager(c);
			
			if(sem != null)
			{
				//类名,数量
				long ct = sem.count();
				String className = c.getName();
				
				String line = className + "," + ct + "\n";
				countInfos += line;
				out.println(line+"<br/>");
			}
			else
			{
				out.println("获取"+c.getName()+"simplemanager出错!");
			}
		} 		
	}
	

%>
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title>Insert title here</title>
</head>
<body style="font-size: 12px;">
		<form action="./queryCount4Class.jsp" method="post">
			<input type="hidden" name="isDo" value="true" />
			<table border="1">
				<tr style="font-weight: bold;font-size: 14px; color: white;background-color: black;">
					<td>服务器类型</td>
					<td>服务器名字</td>
					<td>服务器配置</td>
				</tr>
				<tr>
					<td>主服务器/当前</td>
					<td><input type="text" name="mainServerName" value="<%=mainServerName%>"></td>
					<td><input type="text" name="mainServerConf" value="<%=mainServerConf%>" size="100"></td>
				</tr>
			</table>
			<input type="submit" value="提交">
		</form>
		
		<textarea rows="100" cols="100" name="countInfos" value="<%=countInfos%>"></textarea>
</body>
</html>