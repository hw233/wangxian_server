<%@page import="java.sql.ResultSet"%>
<%@page import="java.sql.PreparedStatement"%>
<%@page import="java.sql.DriverManager"%>
<%@page import="java.sql.Connection"%>
<% 	Class.forName("oracle.jdbc.driver.OracleDriver");
	Connection conn = DriverManager.getConnection(
		"jdbc:oracle:thin:@117.121.22.9:1521:ora11g", "wangxian216", "wangxian216oracle");
	
	PreparedStatement pstmt = null;
	
	
	ResultSet rs = null;
	try
	{
		pstmt = conn.prepareStatement("select count(*) from Player p where p.rownum <=2");
		rs = pstmt.executeQuery();
		
		if(rs.next())
		{
			out.println("连接成功，执行结果为:"+""+rs.getInt(0));
		}
	}
	finally
	{
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
	}
		%>