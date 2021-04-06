<%@page import="com.xuanzhi.tools.cache.diskcache.DiskCache"%>
<%@page import="java.sql.Connection"%>
<%@page import="java.sql.ResultSet"%>
<%@page import="java.sql.PreparedStatement"%>
<%@page import="java.sql.DriverManager"%>
<%@ page contentType="text/html;charset=utf-8" import="java.util.*,
com.xuanzhi.tools.text.*,
java.nio.channels.*,com.fy.gamegateway.mieshi.server.*"%>

<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8">
<title>获取身份证</title>
</head>
<body>
<%
	//List<PassportExtend> passportExtends = new ArrayList<PassportExtend>();
	Class.forName("oracle.jdbc.driver.OracleDriver");
	Connection conn = DriverManager.getConnection(
		"jdbc:oracle:thin:@116.213.192.216:1521:ora183", "mieshiboss", "4Fxtkq5J9ydy3sK1");
	PreparedStatement pstmt = null;
	ResultSet rs = null;

	String sql  = "select p.username from passport p where p.registerdate >= to_date(?,'yyyy-mm-dd hh24:mi:ss') and rownum <= 100";
	
	pstmt = conn.prepareStatement(sql);
	pstmt.setString(1, "2013-07-01 00:00:00");
	rs = pstmt.executeQuery();
	DiskCache  shenfenzhengCache =  MieshiGatewaySubSystem.getInstance().shenfenzhengCache;
%>
	<table border="1px">
	<tr>
		<td>账号</td>
		<td>真实姓名</td>
		<td>身份证</td>
	</tr>
<% 	
	while(rs.next())
	{
		PassportExtend pe = (PassportExtend) shenfenzhengCache.get(rs.getString(1));
		
		if(pe != null && pe.shenfenzheng != null && pe.shenfenzheng.length() > 0)
		{
			
%>
		<tr>
			<td><%=rs.getString(1) %></td>
			<td><%=pe.realname %></td>
			<td><%=pe.shenfenzheng %></td>
		</tr>
<% 			
		}
	}

	rs.close();
	pstmt.close();
	conn.close();

%>
</table>


</body>
</html>
