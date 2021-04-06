<%@page import="com.xuanzhi.tools.text.ParamUtils"%>
<%@page import="java.util.Iterator"%>
<%@page import="java.sql.ResultSet"%>
<%@page import="java.sql.PreparedStatement"%>
<%@page import="java.sql.DriverManager"%>
<%@page import="java.sql.Connection"%>
<%@ page language="java" contentType="text/html; charset=utf-8"
    pageEncoding="utf-8"%>
    
<%

Class.forName("com.mysql.jdbc.Driver").newInstance();
String dburl = "10.147.21.115:3301/qq_game_server_101"; //太虚

String url="jdbc:mysql://"+dburl+"?characterEncoding=utf-8&useUnicode=true";
String user="wangxian";
String password="wang123xian"; 
Connection conn  = DriverManager.getConnection(url,user,password); 
/*		String sql = "select * from PASSPORT_S1 p where p.USERNAME = ? ";
String updateSql = "update PASSPORT_S1 set USERNAME = ?,NICKNAME=? where ID = ?";*/
long id = ParamUtils.getLongParameter(request, "id", 0);

String sql = "select T0.*,T1.*,T2.* from ARTICLEENTITY_S64 T0 left join ARTICLEENTITY_T1_S64 T1 on T0.ID=T1.ID_SEC_1 left join ARTICLEENTITY_T2_S64 T2 on T0.ID=T2.ID_SEC_2  where T0.ID=? ";

PreparedStatement pstmt = conn.prepareStatement(sql);
pstmt.setLong(1, id);


ResultSet rs = null;
rs = pstmt.executeQuery();
if(rs.next())
{
	String values2 = rs.getString("values2");
	out.println("[values2:"+values2+"] [id:"+id+"] [dburl:"+url+"] [sql:"+sql+"]" );
}
else
{
	out.println("[没有匹配的值] [id:"+id+"] [dburl:"+url+"] [sql:"+sql+"]" );
}


rs.close();
pstmt.close();
conn.close();



%>    
    
    
