<%@ page contentType="text/html;charset=UTF-8"%>
<%@include file="../inc.jsp"%>
<%@ page
	import="com.xuanzhi.tools.cache.diskcache.concrete.DefaultDiskCache,org.slf4j.*,java.io.*,java.util.*,com.xuanzhi.tools.text.*,
	com.xuanzhi.tools.text.*,org.springframework.context.ApplicationContext,org.springframework.web.context.support.WebApplicationContextUtils,
	com.sqage.stat.commonstat.dao.UserDao,com.sqage.stat.commonstat.entity.User,com.sqage.stat.commonstat.manager.UserManager,
	com.sqage.stat.commonstat.manager.Impl.*,com.sqage.stat.commonstat.entity.*"
	%>
	<%!
	Object lock = new Object(){};
      %>
  <%
	String startDay = request.getParameter("day");
	String endDay = request.getParameter("endDay");
	String displayType=request.getParameter("displaytype");
	 
	 if(displayType==null){ displayType="1"; }
	String today = DateUtil.formatDate(new Date(),"yyyy-MM-dd");
	if(startDay == null) startDay =today;
	if(endDay == null) endDay =today;
	Date t = DateUtil.parseDate(endDay,"yyyy-MM-dd");
	Date s = DateUtil.parseDate(startDay,"yyyy-MM-dd");
	ArrayList<String> dayList = new ArrayList<String>();
	
    PlayGameManagerImpl playGameManager=PlayGameManagerImpl.getInstance();
    
    String sql="";
    if("1".equals(displayType)){
    sql=" select  to_char(p.enterdate,'YYYY-MM-DD') head, sum(case when p.maxlevel between '0' and '40' then 1  end)  l1, "
     +" sum(case when p.maxlevel between '41' and '110' then 1  end)  l2, sum(case when p.maxlevel> '111'  then 1  end) l3 "
     +" from stat_playgame p "
     +" where p.enterdate  between to_date('"+startDay+" 00:00:00','YYYY-MM-DD hh24:mi:ss') and  to_date('"+endDay+" 23:59:59','YYYY-MM-DD hh24:mi:ss') "
     +" group by to_char(p.enterdate,'YYYY-MM-DD')";
    }else{
    
    sql=" select   f.name head, sum(case when p.maxlevel between '0' and '40' then 1  end)  l1, "
     +" sum(case when p.maxlevel between '41' and '110' then 1  end)  l2, sum(case when p.maxlevel> '111'  then 1  end) l3 "
     +" from stat_playgame p,stat_fenqu f "
     +" where p.enterdate  between to_date('"+startDay+" 00:00:00','YYYY-MM-DD hh24:mi:ss') and  to_date('"+endDay+" 23:59:59','YYYY-MM-DD hh24:mi:ss') "
     +" and p.fenqu=f.name group by f.name,f.seq order by f.seq";
    
    }
    
   
   
    List<String []> ls=playGameManager.getPlayerLevelFenBu_sum(sql);
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
			进入用户等级段分布
		</h2>
		<form  method="post">
		  
		 开始日期： <input type="text" name="day" id="day" class="nqz_input" style="width:100px;" readonly="readonly" onfocus="new Calendar(false,'yyyy-MM-dd').showTimePicker(this);" value="<%=startDay %>"/>&nbsp;到
		  结束日期： <input type="text" name="endDay" id="endDay" class="nqz_input" style="width:100px;" readonly="readonly" onfocus="new Calendar(false,'yyyy-MM-dd').showTimePicker(this);" value="<%=endDay %>"/>
		
                                                     展示方式：
                   <input type="radio" <% if(displayType==null||"1".equals(displayType)){out.print("checked");} %>  name="displaytype" value="1" />时间
                   <input type="radio" <% if("0".equals(displayType)){out.print("checked");} %>  name="displaytype" value="0" />分区
          
		
		<input type='submit' value='提交'> </form>
		<br/>
		</center>
		<br/>
		<center>
		    		<%
		    		out.println("<table id='test2' align='center' width='90%' cellpadding='1' bgcolor='#000000' cellspacing='1' border='0'>");
					out.print("<tr bgcolor='#EEEEBB'><td>项目</td><td>0-40级</td><td>41-110级</td><td>111级和以上</td></tr>");
		    		
		    		long l1=0L;
		    		long l2=0L;
		    		long l3=0L;
					for(int i = 0 ; i < ls.size() ; i++){
					String [] data=ls.get(i);
						out.print("<tr bgcolor='#FFFFFF'> ");
						out.print("<td>"+ (data[0]==null ? "0" : data[0])+"</td>");
						out.print("<td>"+(data[1]==null ? "0" : data[1])+"</td>"); l1+=Long.parseLong((data[1]==null ? "0" : data[1]));
						out.print("<td>"+(data[2]==null ? "0" : data[2])+"</td>"); l2+=Long.parseLong((data[2]==null ? "0" : data[2]));
						out.print("<td>"+(data[3]==null ? "0" : data[3])+"</td>"); l3+=Long.parseLong((data[3]==null ? "0" : data[3]));
						
						out.println("</tr>");
					}
					out.print("<tr bgcolor='#EEEEBB'><td>总计</td><td>"+l1+"</td><td>"+l2+"</td><td>"+l3+"</td></tr>");
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
