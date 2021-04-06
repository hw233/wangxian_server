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
    //String flag = null;
    //OnLineUsersCountManagerImpl onLineUsersCountManager=OnLineUsersCountManagerImpl.getInstance();  
    //flag = request.getParameter("flag");
    String content = request.getParameter("content");
    if(content==null) content="";
    ChannelManager channelManager =ChannelManager.getInstance();
    ChannelItemManager channelItemManager=ChannelItemManager.getInstance();
    
     String[] ss=content.split("\r*\n");
    	  Connection con = null;
           con = DataSourceManager.getInstance().getConnection();
           con.setAutoCommit(false);
           PreparedStatement ps          = con.prepareStatement("update stat_chongzhi set qudao=?  where username=? and qudao=?");
           PreparedStatement user_ps     = con.prepareStatement("update stat_user set qudao=?  where name=? and qudao=?");
           PreparedStatement playGame_ps = con.prepareStatement("update stat_playgame set qudao=?  where username=? and qudao=?");
		   try {
		    int i =-1;
		    long now =System.currentTimeMillis();
		   for(int j1=0;j1<ss.length;j1++){//更新充渠道
		   if(ss[j1].indexOf("\r")!=-1){ss[j1] = ss[j1].replaceAll("\r","");}
	         String[] str = ss[j1].split(",");
	         if(str.length==3){
	         
	         //////////////////////渠道处理 start //////////////////////////
		   Channel channel= channelManager.getChannel(str[2].trim());
		   Long quDaoId=null; 
				if(channel==null){
					Channel channel_temp=new Channel();
					channel_temp.setName(str[2].trim());
					channel_temp.setKey(str[2].trim());
					channelManager.createChannel(channel_temp);
					channel=channelManager.getChannel(str[2].trim());
					quDaoId=channel.getId();
				}else{ quDaoId=channel.getId();}
				//添加子渠道
				if(channelItemManager.getChannelItem(str[2].trim())==null)
				{
					ChannelItem channelItem=new ChannelItem();
					channelItem.setChannelid(quDaoId);
					channelItem.setKey(str[2].trim());
					channelItem.setName(str[2].trim());
					channelItem.setCmode(0L);
					channelItem.setPrate(1F);
					channelItemManager.createChannelItem(channelItem);
				}
		    //////////////////////渠道处理  end //////////////////////////
		   
			ps.setString(1, str[2].trim());
			ps.setString(2, str[1].trim());
			ps.setString(3, str[0].trim());
			i =ps.executeUpdate();
			System.out.println("[修改充值渠道] ["+j1+"] ["+str[1].trim()+"] ["+str[2].trim()+"] [totalcost:"+(System.currentTimeMillis() - now)+"ms]");
			if(j1!=0&&j1%100==0){  
				con.commit(); 
			 }
			 }
			}
			con.commit();
			System.out.println("[更新充值渠道信息] [OK] [更新"+ss.length+"条] [cost:"+(System.currentTimeMillis()-now)+"ms]");
			now =System.currentTimeMillis();
		  for(int j2=0;j2<ss.length;j2++){//更新用户注册渠道
		  if(ss[j2].indexOf("\r")!=-1){ss[j2] = ss[j2].replaceAll("\r","");}
	         String[] str = ss[j2].split(",");
	         if(str.length==3){
			user_ps.setString(1, str[2].trim());
			user_ps.setString(2, str[1].trim());
			user_ps.setString(3, str[0].trim());
			i =user_ps.executeUpdate();
			System.out.println("[修改注册渠道] ["+j2+"] ["+str[1].trim()+"] ["+str[2].trim()+"] [totalcost:"+(System.currentTimeMillis() - now)+"ms]");
			if(j2!=0&&j2%100==0){  con.commit();  }
			}
			}
			con.commit();
		    System.out.println("[更新用户注册渠道信息] [OK] [更新"+ss.length+"条] [cost:"+(System.currentTimeMillis()-now)+"ms]");
			now =System.currentTimeMillis();
		   for(int j3=0;j3<ss.length;j3++){// 更新用户进入渠道
		   if(ss[j3].indexOf("\r")!=-1){ss[j3] = ss[j3].replaceAll("\r","");}
	        String[] str = ss[j3].split(",");
	         if(str.length==3){
			playGame_ps.setString(1, str[2].trim());
			playGame_ps.setString(2, str[1].trim());
			playGame_ps.setString(3, str[0].trim());
			i =playGame_ps.executeUpdate();
			System.out.println("[修改进入渠道] ["+j3+"] ["+str[1].trim()+"] ["+str[2].trim()+"] [totalcost:"+(System.currentTimeMillis() - now)+"ms]");
			if(j3!=0&&j3%100==0){  con.commit();  }
			}
			}
			con.commit();
			System.out.println("[更新用户进入渠道信息] [OK] [更新"+ss.length+"条] [cost:"+(System.currentTimeMillis()-now)+"ms]");
			
			con.setAutoCommit(true);
			if(i>=0){
			%>
		    <script language=javascript> 
              alert("处理成功！"); 
             </script>
		    <% }
		    } catch (SQLException e) {
		    %>
		    <script language=javascript> 
              alert("出现异常，处理失败！"); 
             </script>
		    <% 
			  //logger.error("[更新在线用户信息] [FAIL] [oldQuDao] ["+oldQuDao+"] [newQuDao] ["+newQuDao+"] [userName] ["+userName+"] [cost:"+(System.currentTimeMillis()-now)+"ms]",e);
		    } finally {
			try {
				if(ps != null) ps.close();
				if(user_ps != null) user_ps.close();
				if(playGame_ps != null) playGame_ps.close();
				
				if(con != null) con.close();
			} catch (SQLException e) {
				e.printStackTrace();
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
		<form method=post> 输入信息：<textarea name='content' id='content' value='' style='width:60%;height:250px;'></textarea>
		<br>(格式：老渠道,用户名,新渠道   例如：qq,Luckystar,APPSTORE_MIESHI)
		<input type="hidden" name="flag" value="true">
		<input type='submit' value='提交'> </form>
		<br/>
		</center>
		<br/>
		<center>
		    		<h3>使用指南：1、本功能为特殊需求开发，非指定人员，不准使用本功能。</h3>
		    		<h3>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;2、输入信息格式  ：老渠道,用户名,新渠道  用英文","分隔</h3>
		    		<h3>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;3、执行解析：例如输入"字符串1,字符串2,字符串3" ,执行的结果是：把渠道是"字符串1",并且用户名是"字符串2"的渠道修改该为"字符串3"  </h3>
		    		<h3>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;4、如果要执行多条记录，每条另起一行 </h3>
		    		
		    		<div id="regUserContainer" style="width:100%;height:400px;display:block;"></div>
		 
		</center>
		
	</body>
</html>
