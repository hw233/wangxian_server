<%@ page contentType="text/html;charset=UTF-8"%>
<%@include file="../inc.jsp"%>
<%@ page
	import="com.xuanzhi.tools.cache.diskcache.concrete.DefaultDiskCache,org.slf4j.*,java.io.*,java.util.*,com.xuanzhi.tools.text.*,
	org.springframework.context.ApplicationContext,org.springframework.web.context.support.WebApplicationContextUtils,
	com.sqage.stat.commonstat.dao.UserDao,com.sqage.stat.commonstat.entity.*,com.sqage.stat.commonstat.manager.*,
	com.sqage.stat.commonstat.manager.Impl.*,com.sqage.stat.service.*,com.sqage.stat.model.Channel"
	%>

     <%
              String flag = null;
              flag = request.getParameter("flag");
	          String startDay = request.getParameter("day");
	          String endDay = request.getParameter("endDay");
	
	          String today = DateUtil.formatDate(new Date(new Date().getTime()-24*60*60*1000),"yyyy-MM-dd");
	          if(startDay == null) startDay = today;
	          if(endDay == null) endDay = today;
	
	         Date tt = DateUtil.parseDate(endDay,"yyyy-MM-dd");
	         ArrayList<String> dayList = new ArrayList<String>();
             LibaoManagerImpl libaoManager = LibaoManagerImpl.getInstance();
             ArrayList<String []> ls=new ArrayList<String []>();
		 
	       int lenth=(15>dayList.size())? dayList.size():15;
        if ("true".equals(flag)) {//如果是刚打开页面，不查询
        
      		String sql=" select t.daojuname,t.danjia,sum(case t.type when 1 then 1 end ) dadao," +
				"sum(case t.type when 2 then 1 end ) lingqu from stat_libao t " +
				"where t.createdate between to_date('"+startDay+" 00:00:00','YYYY-MM-DD hh24:mi:ss') " +
				"and to_date('"+endDay+" 23:59:59','YYYY-MM-DD hh24:mi:ss') group by t.daojuname,t.danjia order by t.daojuname,t.danjia";
		
		String [] strs={"daojuname","danjia","dadao","lingqu"};
         
           ls= libaoManager.getliBaoData(sql,strs);
		 
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
		<h2 style="font-weight:bold;">周月礼包统计</h2>
		<form   method="post" id="form1">
		  <input type='hidden' name='flag' value='true'/>
		

                        开始日期： <input type="text" name="day" id="day" class="nqz_input" style="width:100px;" readonly="readonly" onfocus="new Calendar(false,'yyyy-MM-dd').showTimePicker(this);" value="<%=startDay %>"/>&nbsp;到
		  结束日期： <input type="text" name="endDay" id="endDay" class="nqz_input" style="width:100px;" readonly="readonly" onfocus="new Calendar(false,'yyyy-MM-dd').showTimePicker(this);" value="<%=endDay %>"/>
		 &nbsp;&nbsp;&nbsp;&nbsp;
		<br/>
                                            
		    	<input type='submit' value='提交'></form>
		    	
		    	<br/>
		    	
		    		<%
		    		out.println("<table id='test2' align='center' width='90%' cellpadding='1' bgcolor='#000000' cellspacing='1' border='0'>");
					out.print("<tr bgcolor='#EEEEBB'><td>礼包</td><td>单价</td><td>达到领取资格数量</td><td>领取数量</td></tr>");
					for(int i=0;i<ls.size();i++)
					{
					String[] strS=ls.get(i);
					
					out.print("<tr bgcolor='#FFFFFF'><td>"+strS[0]+"</td><td>"+strS[1]+"</td><td>"+strS[2]+"</td><td>"+strS[3]+"</td></tr>");
					}
					out.println("</table><br>");
		    		%>
		    		
		    		<div id="regUserContainer" style="width:100%;height:400px;display:block;"></div>
	

		</center>
		<br>
		<i>这里的用户都是指进入游戏的用户</i>
		<br>
	</body>
</html>
