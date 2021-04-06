<%@ page language="java" import="java.util.*,java.sql.*,com.sqage.stat.commonstat.entity.*,com.xuanzhi.tools.text.*,java.io.*" 
 pageEncoding="UTF-8"%>
<%
	boolean submit = "true".equals( request.getParameter("submitted")	);
%>	

<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN">
<html>
  <head>
    <title>插入测试数据</title>
	<meta http-equiv="pragma" content="no-cache">
	<meta http-equiv="cache-control" content="no-cache">
	<meta http-equiv="expires" content="0">    
	<meta http-equiv="keywords" content="keyword1,keyword2,keyword3">
	<meta http-equiv="description" content="This is my page">
  </head>
  
  <body><br>
  <h1>插入测试数据</h1>
  <form>
  		<input type='hidden' name='submitted' value='true'/>
  	
  		<input type='submit' value='提交'/>
  </form>
  <%
  if(submit){
		Connection conn = DriverManager.getConnection("jdbc:oracle:thin:@116.213.142.188:1521:ora11g","stat_hanguo","stat_hanguo");
		try{
			PreparedStatement ps = null;
			try {
				ps = conn.prepareStatement("select * from dt_currencytype t");
			ps.execute();
			ResultSet rs = ps.getResultSet();
			out.print("<table>");
			while (rs.next()) {
			out.print("<tr>");
			out.print("<td>"+rs.getString("id")+"</td>");
			out.print("<td>"+rs.getString("name")+"</td>");
			
			out.print("</tr>");
			}
			out.print("</table>");
		} catch (SQLException e) {
			e.printStackTrace();
		} finally{
			ps.close();
			conn.close();
		}
		}catch(Exception e){
			throw e;
		}finally{
			conn.close();
		}
	}
   %>
  <br></body>
</html>
