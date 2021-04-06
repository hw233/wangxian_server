<%@ page contentType="text/html;charset=UTF-8"%>
<%@include file="../inc.jsp"%>
<%@ page
	import="com.xuanzhi.tools.cache.diskcache.concrete.DefaultDiskCache,org.slf4j.*,java.io.*,java.util.*,
	com.xuanzhi.tools.text.*,com.sqage.stat.commonstat.entity.ChongZhi,com.xuanzhi.tools.text.*,
	com.sqage.stat.commonstat.dao.UserDao,com.sqage.stat.commonstat.entity.User,com.sqage.stat.commonstat.manager.UserManager,
	com.sqage.stat.commonstat.manager.Impl.*,com.sqage.stat.service.*,com.sqage.stat.model.Channel"
	%>
	<%!
	Object lock = new Object(){};
      %>
  <%
	String startDay = request.getParameter("day");
	String endDay = request.getParameter("endDay");
	String qudao=request.getParameter("qudao");
	if(qudao==null){qudao="APPSTORE_MIESHI";}
	
	String today = DateUtil.formatDate(new Date(),"yyyy-MM-dd");
	if(startDay == null) startDay = today;
	if(endDay == null) endDay = today;
	
	Date t = DateUtil.parseDate(endDay,"yyyy-MM-dd");
	Date s = DateUtil.parseDate(startDay,"yyyy-MM-dd");
	ArrayList<String> dayList = new ArrayList<String>();
	
	 synchronized(lock){
	     while(s.getTime() <= t.getTime() + 3600000){
		 String day = DateUtil.formatDate(s,"yyyy-MM-dd");
		 dayList.add(day);
		 s.setTime(s.getTime() + 24*3600*1000);
	      }
      }
	
             ChongZhiManagerImpl chongZhiManager=ChongZhiManagerImpl.getInstance();
	        
		     List<String []> chongZhiList=chongZhiManager.getModeTypeDateChongZhi(startDay,endDay,qudao);
		     String[] modeTypeList={"iPad","iPhone","offline_UNKNOWN","iPod touch"};
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
			手机类型充值查询
		</h2>
		<form  method="post">
		   
		  开始日期： <input type="text" name="day" id="day" class="nqz_input" style="width:80px;" readonly="readonly" onfocus="new Calendar(false,'yyyy-MM-dd').showTimePicker(this);" value="<%=startDay %>"/>&nbsp;到
		  结束日期： <input type="text" name="endDay" id="endDay" class="nqz_input" style="width:80px;" readonly="readonly" onfocus="new Calendar(false,'yyyy-MM-dd').showTimePicker(this);" value="<%=endDay %>"/>
		  
		<br/><br/>
		       渠道：<select name="qudao">
                        <option value="APPSTORE_MIESHI" 
                          <% if("APPSTORE_MIESHI".equals(qudao)){ out.print(" selected=\"selected\"");  }  %>
                          >APPSTORE_MIESHI</option>
                           <option value="APP_TMALL" 
                          <% if("APP_TMALL".equals(qudao)){ out.print(" selected=\"selected\"");  }  %>
                          >APP_TMALL</option>
                       
                </select> &nbsp;&nbsp;
                                           
		    		<input type='submit' value='提交'></form><br/>
		    		
		    		     <a href="chongzhi_info_page.jsp">充值查询—分页</a>|
		    		     <a href="chongzhi_info.jsp">充值查询-不分页</a>|
		    		     <a href="chongzhi_fenbu.jsp">  充值等级分布</a>|
		    		     <a href="chongzhi_money_fenbu.jsp">充值金额区间分布</a>|
		    		     <a href="qianrensouyi_info.jsp">千人收益</a>|
		    		     <a href="Kri_qianrensouyi_info.jsp">K 日千人收益</a>|
		    		     <a href="Krisouyi_info.jsp">K 日收益</a>|
		    		     <a href="quDaoChongzhi_info.jsp">按渠道充值查询</a>|
		    		     <a href="fenQuChongzhi_info.jsp">按分区充值查询</a>|
		    		     <a href="chongzhi_jine_fenbu.jsp">充值金额分布</a>|
		    		<br/>
		    		<h3>手机类型充值查询(由于手机类型太多，这里只展示前100种手机)</h3>
		    		<%
		    		out.println("<table id='test2' align='center' width='90%' cellpadding='1' bgcolor='#000000' cellspacing='1' border='0'>");
					out.print("<tr bgcolor='#EEEEBB'><td>日期</td>");
					for(int j=0;j<modeTypeList.length;j++)
					{
					out.print("<td>"+modeTypeList[j]+"&nbsp;</td>");
					}
					out.print("</tr>");
					
					for(int i=0;i<dayList.size();i++){
					out.print("<tr bgcolor='#FFFFFF'><td>"+dayList.get(i)+"&nbsp;</td>");
					for(int j=0;j<modeTypeList.length;j++)
					{
					boolean flag=true;
					if(modeTypeList[j]!=null){
					for(int num=0;num<chongZhiList.size();num++){
					String[] ls=chongZhiList.get(num);
					if(ls[0].equals(dayList.get(i))&& modeTypeList[j].equals(ls[1]))
					    {  out.print("<td>"+ls[2]+"&nbsp;</td>");
					    flag=false;
					    }
					}
					}
					
					if(flag)
					{
					 out.print("<td>0&nbsp;</td>");
					}
					}
					out.print("</tr>");
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
