<%@page import="com.fy.engineserver.sprite.concrete.GamePlayerManager"%>
<%@page import="com.fy.engineserver.sprite.PlayerManager"%>
<%@page import="com.xuanzhi.tools.simplejpa.annotation.SimpleVersion"%>
<%@page import="java.lang.reflect.Field"%>
<%@page import="com.fy.engineserver.uniteserver.UnitedServerManager"%>
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
<title>将没有和的Player插入到新的</title>
</head>
<body>
<%!
	DefaultDiskCache  needAddPlayerCache =  new DefaultDiskCache(new File(("/home/game/resin_server/webapps/game_server/WEB-INF/game_runtime_data/addplayer.ddc")), "需要添加没合入的Player", 1000L*60*60*24*10000l);
%>
<%
	//循环合服的所有数据库schema 然后将没有被合入的数据 插入到ddc中 然后将ddc上传到被合服的游戏服
	boolean isAdd = ParamUtils.getBooleanParameter(request, "isAdd");
	String dbuser = null;
	if(isAdd)
	{
		dbuser = ParamUtils.getParameter(request, "dbuser");
		String[] dbusers = dbuser.split("\r*\n");
		for(String user : dbusers)
		{
			Player p = (Player)needAddPlayerCache.get(user);
			if(p != null)
			{
				
				Field versionField = UnitedServerManager.getFieldByAnnotation(Player.class, SimpleVersion.class);
				versionField.setAccessible(true);
				versionField.setInt(p, 0);
				
				GamePlayerManager gamePlayerManager = (GamePlayerManager)GamePlayerManager.getInstance();
				gamePlayerManager.em.save(p);
				out.println("[插入Player成功] ["+p.getId()+"] ["+p.getUsername()+"] ["+p.getName()+"] ["+p.getRMB()+"] ["+p.getLevel()+"]<br/>");
			}
		}
	}



%>


<form method="post">
<input type="hidden"  name="isAdd" value="true" /><br/>
<textarea cols="50" rows="50"  name="dbuser"><%=dbuser %></textarea><br/>
<input type="submit" value="提交" />
</form>
</body>
</html>