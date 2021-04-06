<%@page import="java.sql.ResultSet"%>
<%@page import="java.sql.Statement"%>
<%@page import="java.sql.DriverManager"%>
<%@page import="java.sql.Connection"%>
<%@page import="com.xuanzhi.tools.simplejpa.SimpleEntityManagerFactory"%>
<%@page import="com.xuanzhi.tools.simplejpa.SimpleEntityManager"%>
<%@page import="com.xuanzhi.tools.text.ParamUtils"%>
<%@page import="com.xuanzhi.tools.text.StringUtil"%>
<%@page import="com.fy.boss.authorize.model.Passport"%>
<%@page import="com.fy.boss.authorize.service.PassportManager"%>
<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>

<% 


	Class.forName("oracle.jdbc.driver.OracleDriver");
	Connection conn = DriverManager.getConnection(
		"jdbc:oracle:thin:@116.213.192.216:1521:ora183", "mieshiboss", "sqagepwboss");
	
	String sql = 
		"select passwd,username from passport p where length(p.passwd) != 32";
	
	Statement pstmt = conn.createStatement();
	

	ResultSet rs = pstmt.executeQuery(sql);
	while(rs.next())
	{
		String upsql = "update  passport p set p.passwd='"+StringUtil.hash(rs.getString("passwd")).toLowerCase()+"' where p.username='"+rs.getString("username")+"' and  length(p.passwd) != 32";
		out.println("修改["+rs.getString("username")+"]的密码["+rs.getString("passwd")+"]为["+StringUtil.hash(rs.getString("passwd")).toLowerCase()+"]<br>");
		Statement ss = conn.createStatement();
		int count = ss.executeUpdate(upsql);
		if(count > 0)
		{
			out.println("修改成功<br>");
		}
		conn.commit();
		ss.close();
	}
	
	
	rs.close();
	pstmt.close();
	conn.close();
	
	
	


%>