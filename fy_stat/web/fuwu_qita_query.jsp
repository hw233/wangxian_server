<%@ page contentType="text/html;charset=UTF-8"%>
<%@include file="./inc.jsp"%>
<%@ page
	import="com.xuanzhi.tools.cache.diskcache.concrete.DefaultDiskCache,org.slf4j.*,java.io.*,java.util.*,com.xuanzhi.tools.text.*,
	com.xuanzhi.tools.text.*,org.springframework.context.ApplicationContext,org.springframework.web.context.support.WebApplicationContextUtils,
	com.sqage.stat.commonstat.dao.UserDao,com.sqage.stat.commonstat.entity.User,com.sqage.stat.commonstat.manager.UserManager,
	com.sqage.stat.commonstat.manager.Impl.*,com.sqage.stat.commonstat.manager.Impl.PlayGameManagerImpl,java.sql.*,
	com.xuanzhi.tools.dbpool.*,com.sqage.stat.service.ChannelManager,com.sqage.stat.model.*,com.sqage.stat.service.ChannelItemManager,java.io.BufferedWriter"
	%>
  <%
     String flag = request.getParameter("flag");
    if(flag!=null){
    String name="moneydetail";  //只用写要执行的文件的名称即可
    			String targetFilename="/data2/statserver/daily/daily/"+name+".txt";
             FileUtils.chkFolder(targetFilename);
             BufferedWriter wrout = new BufferedWriter(new FileWriter(targetFilename));
				
	      long now=System.currentTimeMillis();
    	  Connection con = null;
           con = DataSourceManager.getInstance().getConnection();
           
                  
            String sql=" select t.username,t.registdate from sj_stat_user_zc t where not exists ( select 1 from sj_stat_user z where t.username=z.name)" ;
          
           System.out.println(sql);
           PreparedStatement ps          = con.prepareStatement(sql);
		   try {
		   long count=0;
		  String instring=null;
			ps.executeQuery();
			wrout.write("用户名"+","+"注册时间\n");
			ResultSet rs =  ps.getResultSet();
			if(rs.next()){   //判断是否有记录
			do {   //有的话 先获取第一条记录
			String username =rs.getString("username");
			String registdate    =rs.getString("registdate");
		
			wrout.write(username+","+registdate+"\n");
			
			if(count%300000==0)
			{	
			wrout.flush();		
			System.out.println("[查询] [get:"+count+"条] [耗时：]  [cost:"+(System.currentTimeMillis()-now)+"ms] 继续！！！");      
			}
			count++;
			} while(rs.next());  
			
			}else{
			System.out.println("无记录\t"+"无记录"+"\n");
			wrout.write("无记录\t"+"无记录"+"\n");
			}
	 	wrout.flush();	   
        wrout.close();
			System.out.println("[查询结束] [get:"+count+"条] [耗时：]  [cost:"+(System.currentTimeMillis()-now)+"ms] 结束！！！");
		    } catch (SQLException e) {
		     e.printStackTrace(System.out);
		    } finally {
			try {
				wrout.flush();	   
                wrout.close();
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
