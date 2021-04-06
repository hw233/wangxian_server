<%@ page language="java" import="java.util.*,java.sql.*,com.sqage.stat.commonstat.entity.*,com.xuanzhi.tools.text.*,java.io.*"  pageEncoding="UTF-8"%>
<%!
	public void insertUser(Connection con,String USERNAME,String PHONE,String other) throws Exception{
			PreparedStatement ps = null;
			int i = -1;
			try {
				ps = con.prepareStatement("insert into temp(" +
					"USERNAME," +
					"PHONE," +
					"CREATETIME," +
					"OTHER" +
					") values (?,?,?,?)");
					
			
			ps.setString(1, USERNAME);
			ps.setString(2, PHONE);
			
			ps.setDate(3,new java.sql.Date(new java.util.Date().getTime()));
			ps.setString(4, other);
			
			i = ps.executeUpdate();
		} catch (SQLException e) {
			e.printStackTrace();
		} finally{
			ps.close();
		}
	}
public void readfile(Connection conn){

try{
   BufferedReader bufferedreader=new BufferedReader(new InputStreamReader(new  FileInputStream("/home/sanstat/statserver/stat_common_resin/log/result.txt"),"UTF-8")); 
	 String instring;
	 conn.setAutoCommit(false);
	 int num=0;
	 while((instring=bufferedreader.readLine())!=null){
	 
	 long ll=System.currentTimeMillis();
	 System.out.print(instring);
	  String[] str=instring.split("jj");
	  System.out.println("用户["+str.length+"] ["+str[0]+"] ["+str[1]+"] 擦入成功,耗时"+(System.currentTimeMillis() - ll)+"ms！<br>");
			PreparedStatement ps = null;
			int i = -1;
			try {
				ps = conn.prepareStatement("insert into temp(" +
					"USERNAME," +
					"PHONE," +
					"CREATETIME," +
					"OTHER" +
					") values (?,?,?,?)");
			ps.setString(1, "sina_weibo-"+str[0]);
			ps.setString(2, str[1]);
			ps.setDate(3,new java.sql.Date(new java.util.Date().getTime()));
			ps.setString(4, "");
			
			i = ps.executeUpdate();
		} catch (SQLException e) {
			e.printStackTrace();
		} finally{
			ps.close();
		}
	
	  num++;
	  if(num>200)
	  {
	  num=0;
	  System.out.println("数据库提交记录"+DateUtil.formatDate(new java.util.Date(),"yyyy-MM-dd HH:mm:ss"));
	  conn.commit();
	  }
	 //insertUser(conn,str[0],str[1],null);
	 }
	}catch(Exception e){
	e.printStackTrace();
	}finally{
	
	try{conn.setAutoCommit(true);
	conn.close();}
	catch(Exception e){}
	}
}
%><%
	String regDay = request.getParameter("regday");	
	String endDay = request.getParameter("endday");	
	
	if(regDay == null) regDay = DateUtil.formatDate(new java.util.Date(),"yyyy-MM-dd");
	if(endDay == null) endDay = DateUtil.formatDate(new java.util.Date(),"yyyy-MM-dd");
	int num = 100;
	try{
		num = Integer.parseInt(request.getParameter("num"));
	}catch(Exception e){}
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
		Connection conn = DriverManager.getConnection("jdbc:oracle:thin:@221.179.174.52:1521:ora10g","commonstat","commonstat");
		try{
				readfile(conn);
		
		}catch(Exception e){
			throw e;
		}finally{
			conn.close();
		}
	}
   %>
  <br></body>
</html>
