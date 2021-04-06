<%@ page contentType="text/html;charset=UTF-8"%>
<%@include file="../inc.jsp"%>
<%@ page
	import="com.xuanzhi.tools.cache.diskcache.concrete.DefaultDiskCache,org.slf4j.*,java.io.*,java.util.*,com.xuanzhi.tools.text.*,
	com.xuanzhi.tools.text.*,org.springframework.context.ApplicationContext,org.springframework.web.context.support.WebApplicationContextUtils,
	com.sqage.stat.commonstat.dao.UserDao,com.sqage.stat.commonstat.entity.User,com.sqage.stat.commonstat.manager.UserManager,
	com.sqage.stat.commonstat.manager.Impl.*,com.sqage.stat.commonstat.manager.Impl.PlayGameManagerImpl,java.sql.*,
	com.xuanzhi.tools.dbpool.*,com.sqage.stat.service.ChannelManager,com.sqage.stat.model.*,com.sqage.stat.service.ChannelItemManager"
	%>
  <%
     String flag = request.getParameter("flag");
    if(flag!=null){
    BufferedReader bufferedreader=new BufferedReader(new InputStreamReader(new  FileInputStream("D:\\ip1.txt"),"UTF-8")); 
	      long now=System.currentTimeMillis();
    	  Connection con = null;
           con = DataSourceManager.getInstance().getConnection();
           con.setAutoCommit(false);
           PreparedStatement ps          = con.prepareStatement("insert into linshi_ip v (ip,SHENGFEN,shi) values(?,?,?)");
		   try {
		    int i =-1;
		    long num=1L;
		 String instring=null;
         while((instring=bufferedreader.readLine())!=null){
         System.out.println(instring);
         String[] strs= instring.replaceAll("\t","").replaceAll("\r","").trim().split(",");
         System.out.println(strs[0]+strs[1]+strs[2]);
			ps.setString(1, strs[0]);
			ps.setString(2, strs[1]);
			ps.setString(3, strs[2]);
			
			i =ps.executeUpdate();
			num++;
			if(num!=0&&num%500==0){  
				con.commit(); 
				System.out.println("[更新充值渠道信息] [OK] [更新"+num+"条] [cost:"+(System.currentTimeMillis()-now)+"ms]");
			 }
          }
			con.commit();
			System.out.println("[更新充值渠道信息] [OK] [更新"+num+"条] [cost:"+(System.currentTimeMillis()-now)+"ms] 结束！！！");
			con.setAutoCommit(true);
		    } catch (SQLException e) {
		    e.printStackTrace(System.out);
		    } finally {
			try {
				if(ps != null) ps.close();
				if(con != null) con.close();
			} catch (SQLException e) {
				e.printStackTrace();
			}
		}
		}
   %>
		
<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN">
<html>
	<head>
		<title></title>
		<meta http-equiv="pragma" content="no-cache">
		<meta http-equiv="cache-control" content="no-cache">
		<meta http-equiv="expires" content="0">
		<meta http-equiv="keywords" content="keyword1,keyword2,keyword3">
		<meta http-equiv="description" content="This is my page">
		<link rel="stylesheet" href="../css/style.css" />
		<link rel="stylesheet" href="../css/atalk.css" />
		<script language="javascript" type="text/javascript" src="../js/flotr/lib/prototype-1.6.0.2.js"></script>
		<script language="javascript" type="text/javascript" src="../js/flotr/flotr-0.2.0-alpha.js"></script>

<script language="JavaScript">
</script>
	</head>
	<body>
		<center>
		<h2 style="font-weight: bold;"><br> 
			</h2><h2 style="font-weight: bold;">更改用户渠道信息 
		</h2>
		<form method=post>
		
		<input type="hidden" name="flag" value="true">
		<input type='submit' value='提交'> </form>
		<br/>
		</center>
		<br/>
		<center>
		    		<h3>使用指南：1、从文件读取数据插入数据库中</h3>
		    		<div id="regUserContainer" style="width:100%;height:400px;display:block;"></div>
		</center>
		
	</body>
</html>
