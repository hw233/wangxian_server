<%@page import="com.fy.engineserver.datasource.skill.master.SkBean"%>
<%@page import="com.xuanzhi.tools.simplejpa.annotation.SimpleVersion"%>
<%@page import="com.fy.engineserver.uniteserver.UnitedServerManager"%>
<%@page import="java.lang.reflect.Field"%>
<%@page import="com.fy.engineserver.sprite.concrete.GamePlayerManager"%>
<%@page import="java.util.Properties"%>
<%@page import="java.util.ArrayList"%>
<%@page import="java.util.List"%>
<%@page import="java.io.File"%>
<%@page import="com.xuanzhi.tools.cache.diskcache.concrete.DefaultDiskCache"%>
<%@page import="com.xuanzhi.tools.simplejpa.impl.SimpleEntityManagerOracleImpl"%>
<%@page import="com.fy.engineserver.sprite.Player"%>
<%@page import="com.xuanzhi.tools.simplejpa.impl.DefaultSimpleEntityManagerFactory"%>
<%@page import="com.xuanzhi.tools.simplejpa.SimpleEntityManager"%>
<%@page import="java.sql.ResultSet"%>
<%@page import="java.sql.Statement"%>
<%@page import="java.sql.DriverManager"%>
<%@page import="java.sql.Connection"%>
<%@page import="com.xuanzhi.tools.text.ParamUtils"%>
<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title>将角色恢复</title>
</head>
<body>
<%
	//循环合服的所有数据库schema 然后将没有被合入的数据 插入到ddc中 然后将ddc上传到被合服的游戏服
	boolean isAdd = ParamUtils.getBooleanParameter(request, "isAdd");
	String mainConf = ParamUtils.getParameter(request, "mainConf", "");
	String otherConfs = ParamUtils.getParameter(request, "otherConfs", "");

	if(isAdd)
	{
		Class destClass =  Player.class;
		//获取主数据库的simpleentity
		SimpleEntityManager<Player> mainSimpleManager = new  DefaultSimpleEntityManagerFactory(mainConf).getSimpleEntityManager(destClass);
		
		String[] others = otherConfs.split("\r*\n");
		for(String confPath : others)
		{
			DefaultSimpleEntityManagerFactory factory = new DefaultSimpleEntityManagerFactory(confPath);
			SimpleEntityManager<Player> em = factory.getSimpleEntityManager(Player.class);
			
			long ids[] = em.queryIds(destClass, "");
			
			for(long id :  ids)
			{
				Player mainPlayer = mainSimpleManager.find(id);
				if(mainPlayer == null)
				{
					Player player =	em.find(id);
					if(player == null)
					{
						continue;
					}
					
					if(player != null)
					{
						Field  versionField = UnitedServerManager.getFieldByAnnotation(destClass, SimpleVersion.class);
						versionField.setAccessible(true);
						versionField.set(player, 0);
						mainSimpleManager.save(player);
						out.println("[增加用户] ["+id+"] ["+player.getName()+"] ["+player.getUsername()+"]<br/>");
					}
					
				}
			}
			
					
		}
	}



%>


<form method="post">
<input type="hidden"  name="isAdd" value="true" /><br/>
主数据库配置文件:<input type="text" name="mainConf" value=<%=mainConf %> /><br/>
副数据库配置文件:<textarea cols="50" rows="50"  name="otherConfs"><%=otherConfs %></textarea>(格式:远程数据库用户名 本地配置的配置文件地址)<br/>
<input type="submit" value="提交" />
</form>
</body>
</html>