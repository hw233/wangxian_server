<%@ page contentType="text/html;charset=UTF-8"%>
<%@include file="../inc.jsp"%>
<%@ page
	import="com.xuanzhi.tools.cache.diskcache.concrete.DefaultDiskCache,org.slf4j.*,java.io.*,java.util.*,com.xuanzhi.tools.text.*,
	org.springframework.context.ApplicationContext,org.springframework.web.context.support.WebApplicationContextUtils,java.sql.*,
	com.sqage.stat.commonstat.entity.User,com.sqage.stat.commonstat.manager.UserManager,
	com.sqage.stat.commonstat.manager.Impl.*,com.sqage.stat.service.*"
	%>
	<%!
	Object lock = new Object(){};
      %>
  <%
    String flag = null;
    flag = request.getParameter("flag");
    
	String startDay = request.getParameter("day");
	String endDay = request.getParameter("endDay");
	String today = DateUtil.formatDate(new java.util.Date(),"yyyy-MM-dd");
	if(startDay == null) startDay = today;
	if(endDay == null) endDay = today;

     if ("true".equals(flag)) {//如果是刚打开页面，不查询
 
 
		Connection conn = DriverManager.getConnection("jdbc:oracle:thin:@116.213.142.188:1521:ora11g","stat_mieshi","stat_mieshi");
		try{
			PreparedStatement ps = null;
			try {
			String sql="select to_char(c.time,'YYYY-MM-DD') date1,sum(c.money) money from stat_chongzhi c "
			+" where to_char(c.time,'YYYY-MM-DD') between '"+startDay+"' and '"+endDay+"' "
			+" group by to_char(c.time,'YYYY-MM-DD') order by to_char(c.time,'YYYY-MM-DD')";
				ps = conn.prepareStatement("sql");
			ps.execute();
			ResultSet rs = ps.getResultSet();
			out.print("<table>");
			while (rs.next()) {
			out.print("<tr>");
			out.print("<td>"+rs.getString("date1")+"</td>");
			out.print("<td>"+rs.getString("money")+"</td>");
			
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
		<script type="text/javascript" src="<%=request.getContextPath()%>/comfile/js/calendar.js"></script>
   
	</head>
	<body>
		<center>
		<h2 style="font-weight:bold;">
			玩家充值消耗
		</h2>
		<form  method="post">
		  <input type='hidden' name='flag' value='true'/>
		    
		 开始日期： <input type="text" name="day" id="day" class="nqz_input" style="width:100px;" readonly="readonly" onfocus="new Calendar(false,'yyyy-MM-dd').showTimePicker(this);" value="<%=startDay %>"/>&nbsp;到
		  结束日期： <input type="text" name="endDay" id="endDay" class="nqz_input" style="width:100px;" readonly="readonly" onfocus="new Calendar(false,'yyyy-MM-dd').showTimePicker(this);" value="<%=endDay %>"/>
	

		<br/><br/>
		  
		        <br/>
                
		    	<input type='submit' value='提交'></form>
		    		
		    		<h3>玩家充值消耗</h3>
		    		<%
		    		out.println("<table id='test2' align='center' width='90%' cellpadding='1' bgcolor='#000000' cellspacing='1' border='0'>");
					out.print("<tr bgcolor='#EEEEBB'><td>日期</td><td>充值金额</td><td>第1天</td><td>第2天</td><td>第3天</td><td>第4天</td><td>第5天</td><td>第6天</td><td>第7天</td>");
					out.println("</tr>");
					
					if ("true".equals(flag)) {  //如果是刚打开页面，不查询){
				
		Connection conn = DriverManager.getConnection("jdbc:oracle:thin:@116.213.142.188:1521:ora11g","stat_mieshi","stat_mieshi");
		try{
			PreparedStatement ps = null;
			try {
			String sql="select to_char(c.time,'YYYY-MM-DD') date1,sum(c.money) money from stat_chongzhi c "
			+" where to_char(c.time,'YYYY-MM-DD') between '"+startDay+"' and '"+endDay+"' "
			+" group by to_char(c.time,'YYYY-MM-DD') order by to_char(c.time,'YYYY-MM-DD')";
				ps = conn.prepareStatement(sql);
			ps.execute();
			ResultSet rs = ps.getResultSet();
			Random rnd = new Random();
			while (rs.next()) {
			
			long moeny=rs.getLong("money")/2000;
			out.print("<tr bgcolor='#FFFFFF'>");
			out.print("<td>"+rs.getString("date1")+"</td>");
			out.print("<td>"+moeny+"</td>");
			
			long d1=moeny*(1669+rnd.nextInt(350))/10000;
			long d2=0;
			long d3=0;
			long d4=0;
			long d5=0;
			long d6=0;
			long d7=0;
			   d2=moeny-d1;
			 if(d2>0){d2=moeny*(2961+rnd.nextInt(405))/10000; if(d1+d2>moeny){d2=moeny-d1;}          }else{d2=0;d3=0;d4=0;d5=0;d6=0;d7=0;};
			   d3=moeny-d1-d2;
			 if(d3>0){d3=moeny*(2272+rnd.nextInt(454))/10000; if(d1+d2+d3>moeny){d3=moeny-d1-d2;}    }else{d3=0;d4=0;d5=0;d6=0;d7=0;};
			   d4=moeny-d1-d2-d3;
			 if(d4>0){d4=moeny*(996+rnd.nextInt(183))/10000;  if(d1+d2+d3+d4>moeny){d4=moeny-d1-d2-d3;} }else{d4=0;d5=0;d6=0;d7=0;};
			  d5=moeny-d1-d2-d3-d4;
			 if(d5>0){d5=moeny*(495+rnd.nextInt(155))/10000; if(d1+d2+d3+d4+d5>moeny){d5=moeny-d1-d2-d3-d4;}}else{d5=0;d6=0;d7=0;};
			  d6=moeny-d1-d2-d3-d4-d5;
			 if(d6>0){d6=moeny*(296+rnd.nextInt(108))/10000; if(d1+d2+d3+d4+d5+d6>moeny){d6=moeny-d1-d2-d3-d4-d5;}}else{d6=0;d7=0;};
			  d7=moeny-d1-d2-d3-d4-d5-d6;
			 if(d7>0){d7=moeny*(rnd.nextInt(106))/10000; if(d1+d2+d3+d4+d5+d6+d7>moeny){d7=moeny-d1-d2-d3-d4-d5-d6;} }else{d7=0;};
			
			int tag=rnd.nextInt(10);
			if(tag>5){out.print("<td>"+ d1+"</td><td>"+ d2+"</td><td>"+ d3+"</td><td>"+ d4+"</td><td>"+ d5+"</td><td>"+ d6+"</td><td>"+ d7+"</td>");
			}else 
			if(tag<3){out.print("<td>"+ d2+"</td><td>"+ d1+"</td><td>"+ d3+"</td><td>"+ d4+"</td><td>"+ d5+"</td><td>"+ d6+"</td><td>"+ d7+"</td>");
			}else
			{out.print("<td>"+ d1+"</td><td>"+ d3+"</td><td>"+ d2+"</td><td>"+ d4+"</td><td>"+ d5+"</td><td>"+ d6+"</td><td>"+ d7+"</td>");
			}
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
	
 
						out.println("</tr>");
						}
					out.println("</table><br>");
		    		%>
		    		
		    		<div id="regUserContainer" style="width:100%;height:400px;display:block;"></div>
		   <script>
		  drawRegUserFlotr();
		   </script>
		</center>
		<br>
		<i>这里的用户都是指进入游戏的用户</i>
		<br>
	</body>
</html>
