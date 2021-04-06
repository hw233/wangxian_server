<%@page import="java.sql.Connection"%>
<%@page import="java.sql.PreparedStatement"%>
<%@page import="java.sql.ResultSet"%>
<%@page import="com.xuanzhi.tools.simplejpa.impl.SimpleEntityManagerMysqlImpl"%>
<%@page import="com.fy.engineserver.sprite.concrete.GamePlayerManager"%>
<%@page import="com.fy.engineserver.sprite.PlayerManager"%>
<%@page import="com.xuanzhi.tools.simplejpa.impl.SimpleEntityManagerOracleImpl"%>
<%@page import="com.fy.engineserver.platform.PlatformManager"%>
<%@page import="com.fy.engineserver.platform.PlatformManager.Platform"%>
<%@ page language="java" contentType="text/html; charset=utf-8"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">

<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8">

<link rel="stylesheet" type="text/css" href="../css/common.css" />
</head>
<table>
<tr>
	<td>
		PLAYERID
	</td>
	<td>
		银两
	</td>
	<td>
		高水位银两
	</td>
	<td>
		账号
	</td>
	<td>
		角色名
	</td>
<%
	PlatformManager platformManager = PlatformManager.getInstance();
	ResultSet rs = null;
	PreparedStatement pstmt = null;
	Connection conn = null;
	if(platformManager.isPlatformOf(Platform.官方))
	{
		SimpleEntityManagerOracleImpl simpleEntityManagerOracleImpl = 
				(SimpleEntityManagerOracleImpl) ((GamePlayerManager)GamePlayerManager.getInstance()).em;
		
		String sql = "select ID,SILVER,HIGHWATEROFSILVER,USERNAME,NAME from PLAYER where SILVER  >  HIGHWATEROFSILVER";
		conn = simpleEntityManagerOracleImpl.getConnection();
		pstmt = conn.prepareStatement(sql);
		rs = pstmt.executeQuery();
		
	}
	else
	{
		SimpleEntityManagerMysqlImpl simpleEntityManagerMysqlImpl = 
				(SimpleEntityManagerMysqlImpl) ((GamePlayerManager)GamePlayerManager.getInstance()).em;
		String sql = "select ID,SILVER,HIGHWATEROFSILVER,USERNAME,NAME from PLAYER_S1 where SILVER  >  HIGHWATEROFSILVER";
		conn = simpleEntityManagerMysqlImpl.getConnection();
		pstmt = conn.prepareStatement(sql);
		rs = pstmt.executeQuery();
	}
	
	while(rs.next())
	{
		long id = rs.getLong("ID");
		long silver = rs.getLong("SILVER");
		long hignsilver = rs.getLong("HIGHWATEROFSILVER");
		String username = rs.getString("USERNAME");
		String name = rs.getString("NAME");
%>
	<tr>
		<td>
			<%=id %>
		</td>
		<td>
			<%=silver %>
		</td>
		<td>
			<%=hignsilver %>
		</td>
		<td>
			<%=username %>
		</td>
		<td>
			<%=name %>
		</td>
	</tr>			
<% 		
	}

	if(rs != null)
	{
		rs.close();
	}
	
	if(pstmt != null)
	{
		pstmt.close();
	}
	
	if(conn != null)
	{
		conn.close();
	}

%>
</table>
</html>
