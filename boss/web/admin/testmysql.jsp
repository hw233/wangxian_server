<%@page import="java.sql.DriverManager"%>
<%@page import="java.sql.ResultSet"%>
<%@page import="com.mysql.jdbc.Statement"%>
<%@page import="com.mysql.jdbc.Connection"%>
<%@ page language="java" contentType="text/html; charset=utf-8"
    pageEncoding="utf-8"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=ISO-8859-1">
<title>Insert title here</title>
</head>
<body>
<% 
Connection conn = null;
try
{
	Class.forName("com.mysql.jdbc.Driver");
}catch(ClassNotFoundException e) {
	out.println("装载驱动包出现异常!请查正！");
	e.printStackTrace();
}
// try
// {
// 	conn = DriverManager.getConnection("jdbc:mysql://10.147.21.120:3311/qq_game_boss?characterEncoding=utf-8&useUnicode=true","wangxian","wang123xian");
// 	String sql = "select * from PASSPORT";
// 	Statement s = conn.createStatement();
// 	ResultSet rs = s.executeQuery(sql);
// 	while(rs.next())
// 	{
// 		out.println("玩家的姓名是:"+rs.getString("userName"));
// 	}
// 	rs.close();
// 	s.close();
// 	conn.close();
// }catch(SQLException e) {
// 	System.out.println("链接数据库发生异常!");
// 	e.printStackTrace();
// }



%>

</body>
</html>