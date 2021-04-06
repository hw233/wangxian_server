<%@page import="com.fy.engineserver.newtask.service.DeliverTaskManager"%>
<%@page import="com.fy.engineserver.newtask.DeliverTask"%>
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
<title>将没有和的Player插入到新的</title>
</head>
<body>
<%
	//循环合服的所有数据库schema 然后将没有被合入的数据 插入到ddc中 然后将ddc上传到被合服的游戏服
	boolean isAdd = ParamUtils.getBooleanParameter(request, "isAdd");
	String dbuser = null;
	if(isAdd)
	{
		dbuser = ParamUtils.getParameter(request, "dbuser");
		String[] dbusers = dbuser.split("\r*\n");
		for(String info : dbusers)
		{
			String infos[] = info.trim().split("[\\s]+");
			String user = infos[0];
			String confPath = infos[1];
			DefaultSimpleEntityManagerFactory factory = new DefaultSimpleEntityManagerFactory(confPath);
			SimpleEntityManagerOracleImpl<DeliverTask> em = (SimpleEntityManagerOracleImpl)factory.getSimpleEntityManager(DeliverTask.class);
			
			Connection conn = null;
			Class.forName("oracle.jdbc.driver.OracleDriver");
			Properties conProps = new Properties();
	        conProps.put("user", "sys");
	        conProps.put("password", "asdfas#$#4ergfa");
	        conProps.put("defaultRowPrefetch", "15");
	        conProps.put("internal_logon", "sysdba");
			conn = DriverManager.getConnection("jdbc:oracle:thin:@116.213.205.26:1521:ora11g",conProps);
			
			String sql = "select p2.ID from wangxian"+user+".DELIVERTASK p2  where not exists  (select 'x' from  (select ID    from hefu.DELIVERTASK  where to_char(ID) like '1"+user+"%') p1  where p1.ID    = p2.ID  )";
			Statement stmt = conn.createStatement();
			ResultSet rs = stmt.executeQuery(sql);
			int totalBytes[] = new int[1];
			ArrayList<DeliverTask> lst  = new ArrayList<DeliverTask>();
			while(rs.next()){
				long id = rs.getLong("ID");
				try{
					DeliverTask t = em.find(id);
					
					if(t != null){

						Field versionField = UnitedServerManager.getFieldByAnnotation(DeliverTask.class, SimpleVersion.class);
						versionField.setAccessible(true);
						versionField.setInt(t, 0);
						DeliverTaskManager deliverTaskManager = (DeliverTaskManager)DeliverTaskManager.getInstance();
						deliverTaskManager.em.save(t);
						out.println("[添加DeliverTask成功] ["+t.getId()+"] ["+t.getPlayerId()+"]<br/>");
					}
					
				}catch(Exception e){
					rs.close();
					stmt.close();
					throw e;
				}
				
			}
		
			rs.close();
			stmt.close();
			conn.close();
			
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