<%@ page contentType="text/html;charset=UTF-8"%>
<%@include file="../inc.jsp"%>
<%@ page
	import="com.xuanzhi.tools.cache.diskcache.concrete.DefaultDiskCache,org.slf4j.*,java.io.*,java.util.*,com.xuanzhi.tools.text.*,
	org.springframework.context.ApplicationContext,org.springframework.web.context.support.WebApplicationContextUtils,
	com.sqage.stat.commonstat.dao.UserDao,com.sqage.stat.commonstat.entity.*,com.sqage.stat.commonstat.manager.*,
	com.sqage.stat.commonstat.manager.Impl.*,com.sqage.stat.service.*,com.sqage.stat.model.Channel"
	%>
	<%!
	Object lock = new Object(){};
      %>
     <%
              String flag = null;
              flag = request.getParameter("flag");
	          String startDay = request.getParameter("day");
	          String endDay = request.getParameter("endDay");
	          String displayType=request.getParameter("displaytype");
	          if(displayType==null){ displayType="1"; }
	
	          String today = DateUtil.formatDate(new Date(new Date().getTime()-24*60*60*1000),"yyyy-MM-dd");
	          if(startDay == null) startDay = today;
	          if(endDay == null) endDay = today;
	
	         Date t = DateUtil.parseDate(endDay,"yyyy-MM-dd");
	         Date s = DateUtil.parseDate(startDay,"yyyy-MM-dd");
	         ArrayList<String> dayList = new ArrayList<String>();

	          ChannelManager cmanager = ChannelManager.getInstance();
              UserManagerImpl userManager=UserManagerImpl.getInstance();
              DaoJuManagerImpl daoJuManager=DaoJuManagerImpl.getInstance();
              
	     synchronized(lock){
	         while(s.getTime() <= t.getTime() + 3600000){
		       String day = DateUtil.formatDate(s,"yyyy-MM-dd");
		       dayList.add(day);
		       s.setTime(s.getTime() + 24*3600*1000);
	           }
             }
              ArrayList<String []> ls=new ArrayList<String []>();
        if ("true".equals(flag)) {//如果是刚打开页面，不查询
        
        String sql="";
      
        if("1".equals(displayType)){
         
         sql="select t.column3 fengyin,count(distinct(t.fenqu)) count,sum(d.daojudanjia) money ,round(sum(d.daojudanjia)/count(distinct(t.fenqu))) avg "
        +"from stat_yinzikucun t ,stat_daoju_mohu d where t.column2 is not null and  t.column2=2  and t.column1=1 "
        +"and  t.createdate between to_date('"+endDay+" 00:00:00','YYYY-MM-DD hh24:mi:ss')  and to_date('"+endDay+" 23:59:59','YYYY-MM-DD hh24:mi:ss')  "
        +"and d.createdate  between to_date('"+startDay+" 00:00:00','YYYY-MM-DD hh24:mi:ss') and to_date('"+endDay+" 23:59:59','YYYY-MM-DD hh24:mi:ss') "
        +"and t.fenqu=d.fenqu group by t.column3 order by t.column3";
         
             }
          else if ("2".equals(displayType))
         {
         sql="select d.fenqu fengyin,sum(d.daojunum) count,sum(d.daojudanjia) money,round(sum(d.daojudanjia)/sum(d.daojunum)) avg "
         +" from  stat_daoju_mohu d  where d.createdate  between to_date('"+startDay+" 00:00:00','YYYY-MM-DD hh24:mi:ss') and to_date('"+endDay+" 23:59:59','YYYY-MM-DD hh24:mi:ss') "
         +" group by d.fenqu";
         
          }
          else if ("3".equals(displayType))
          {
          sql="select n.name fengyin,sum(d.daojunum) count,sum(d.daojudanjia) money,round(sum(d.daojudanjia)/sum(d.daojunum)) avg "
          +" from  stat_daoju_mohu d,dt_daojuname n where to_char(d.daojuname)=to_char(n.id) and "
          +" d.createdate  between to_date('"+startDay+" 00:00:00','YYYY-MM-DD hh24:mi:ss') and to_date('"+endDay+" 23:59:59','YYYY-MM-DD hh24:mi:ss') "
          +" group by n.name";
          }
          else if("4".equals(displayType))
          {
          sql="select d.vip fengyin,sum(d.daojunum) count,sum(d.daojudanjia) money,round(sum(d.daojudanjia)/sum(d.daojunum)) avg "
          +" from  stat_daoju_mohu d where d.createdate  between to_date('"+startDay+" 00:00:00','YYYY-MM-DD hh24:mi:ss') and to_date('"+endDay+" 23:59:59','YYYY-MM-DD hh24:mi:ss')  "
          +" group by d.vip order by d.vip";
          }
           ls= daoJuManager.getDaoJu_MoHu(sql);
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
    <script type="text/javascript" language="javascript">
	      function search() {
		      $('form1').action="yinzixiaohao_info_detail.jsp";
		      $('form1').submit();
	        }
	        
	        
	      function search2() {
		      $('form1').action="yinzichanchu_info_detail.jsp";
		      $('form1').submit();
	        }
	        	
	        
	   
         </script>

	</head>
	 <body>
		<center>
		<h2 style="font-weight:bold;">积分统计</h2>
		<form   method="post" id="form1">
		  <input type='hidden' name='flag' value='true'/>
		

                        开始日期： <input type="text" name="day" id="day" class="nqz_input" style="width:100px;" readonly="readonly" onfocus="new Calendar(false,'yyyy-MM-dd').showTimePicker(this);" value="<%=startDay %>"/>&nbsp;到
		  结束日期： <input type="text" name="endDay" id="endDay" class="nqz_input" style="width:100px;" readonly="readonly" onfocus="new Calendar(false,'yyyy-MM-dd').showTimePicker(this);" value="<%=endDay %>"/>
		


		    <br/><br/>
                                            
                
                                                     展示方式：
                   <input type="radio" <% if(displayType==null||"1".equals(displayType)){out.print("checked");} %>  name="displaytype" value="1" />封印等级
                   <input type="radio" <% if("2".equals(displayType)){out.print("checked");} %>  name="displaytype" value="2" />分区
                   <input type="radio" <% if("3".equals(displayType)){out.print("checked");} %>  name="displaytype" value="3" />道具
                    <input type="radio" <% if("4".equals(displayType)){out.print("checked");} %>  name="displaytype" value="4" />vip
          
		           <br/>
		    	<input type='submit' value='提交'></form>
		    	
		    	<br/>
		    		  <a href="jifen_chanchu_info.jsp">积分产出</a>|
		    	
		    		<%
		    		out.println("<table id='test2' align='center' width='90%' cellpadding='1' bgcolor='#000000' cellspacing='1' border='0'>");
					out.print("<tr bgcolor='#EEEEBB'><td>统计项</td><td>数量</td><td>总消耗积分数</td><td>平均消耗积分</td></tr>");
					for(int i=0;i<ls.size();i++)
					{
					String[] strS=ls.get(i);
					
					out.print("<tr bgcolor='#FFFFFF'><td>"+strS[0]+"</td><td>"+strS[1]+"</td><td>"+strS[2]+"</td><td>"+strS[3]+"</td></tr>");
					
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
