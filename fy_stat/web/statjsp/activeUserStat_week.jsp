<%@ page contentType="text/html;charset=UTF-8"%>
<%@include file="../inc.jsp"%>
<%@ page
	import="com.xuanzhi.tools.cache.diskcache.concrete.DefaultDiskCache,org.slf4j.*,java.io.*,java.util.*,com.xuanzhi.tools.text.*,
	com.xuanzhi.tools.text.*,org.springframework.context.ApplicationContext,org.springframework.web.context.support.WebApplicationContextUtils,
	com.sqage.stat.commonstat.dao.UserDao,com.sqage.stat.commonstat.entity.User,com.sqage.stat.commonstat.manager.UserManager,
	com.sqage.stat.commonstat.manager.Impl.UserManagerImpl,com.sqage.stat.commonstat.manager.Impl.*,com.sqage.stat.service.*,com.sqage.stat.model.Channel"
	%>
  <%
	String startDay = request.getParameter("day");
	String endDay = request.getParameter("endDay");
	String qudao=request.getParameter("qudao");
	String fenqu=request.getParameter("fenqu");
	
	if("0".equals(fenqu)){fenqu=null;}
	if("0".equals(qudao)){qudao=null;}
	
	String yesterday = DateUtil.formatDate(new Date(new Date().getTime()-24*60*60*1000),"yyyy-MM-dd");
	if(startDay == null) startDay = yesterday;
	if(endDay == null) endDay = yesterday;
	
	/**
	*获得渠道信息
	**/
		      ChannelManager cmanager = ChannelManager.getInstance();
              UserManagerImpl userManager=UserManagerImpl.getInstance();
              PlayGameManagerImpl playGameManager=PlayGameManagerImpl.getInstance();
              Stat_base_onlineManagerImpl stat_base_onlineManager=Stat_base_onlineManagerImpl.getInstance();
              
              ArrayList<String []> fenQuList= userManager.getFenQu(null);//获得现有的渠道信息
              List<Channel> channelList = cmanager.getChannels();//渠道信息

	//对比各个渠道数据

	ArrayList<String> realChannelList = new ArrayList<String>();
	realChannelList.add("人数");
	realChannelList.add("平均独立登录次数");
	realChannelList.add("平均在线时长(小时)");
	realChannelList.add("独立付费人数");
	
	realChannelList.add("付费总金额(元)");
	realChannelList.add("平均付费次数");
	
		String channel_regday_nums[] = new String[realChannelList.size()];
		java.text.SimpleDateFormat format = new java.text.SimpleDateFormat("yyyy-MM-dd");    
		java.util.Date beginDate;
		java.util.Date endDate;
		try {
			beginDate = format.parse(startDay);
			endDate= format.parse(endDay);    
			long day=(endDate.getTime()-beginDate.getTime())/(24*60*60*1000);    //两个日期之间相隔的天数
			
			if(day==6){
				String userCount=playGameManager.active_userCount(startDay,endDay,fenqu,qudao);
				if(userCount!=null){
				   channel_regday_nums[0] =userCount;//人数
				}
				
				String loginUserCount=playGameManager.active_userLoginTimes(startDay,endDay,fenqu,qudao);
                  if(loginUserCount!=null){
				     channel_regday_nums[1] =loginUserCount;//平均独立登录次数
				   }
				   
				String avgOnLineTime=playGameManager.active_userAVGOnLineTime(startDay,endDay,fenqu,qudao);
				  if(avgOnLineTime!=null){
				     channel_regday_nums[2] =avgOnLineTime;//平均在线时长
				    }
				    
				String pay_userCount=playGameManager.active_pay_userCount(startDay,endDay,fenqu,qudao);//独立付费人数
				  if(pay_userCount!=null){
				     channel_regday_nums[3] =pay_userCount;//独立付费人数
				    }
				    
			    String pay_Money=playGameManager.active_pay_Money(startDay,endDay,fenqu,qudao);
				  if(pay_Money!=null){
				     channel_regday_nums[4] =pay_Money;//付费总金额(元)
				    }
                
				String pay_AVGTimes=playGameManager.active_pay_AVGTimes(startDay,endDay,fenqu,qudao);
				  if(pay_AVGTimes!=null){
				     channel_regday_nums[5] =pay_AVGTimes;//平均付费次数
				  }
				  }
		} catch (Exception e) {
			e.printStackTrace();
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
    

<script type="text/javascript">


   //计算天数差的函数，通用  
   function  DateDiff(sDate1,  sDate2){    //sDate1和sDate2是2006-12-18格式  
       var  aDate,  oDate1,  oDate2,  iDays  
       aDate  =  sDate1.split("-")  
       oDate1  =  new  Date(aDate[0]  +  '-'  +  aDate[1]  +  '-'  +  aDate[2])    //转换为12-18-2006格式  
       aDate  =  sDate2.split("-")  
       oDate2  =  new  Date(aDate[0]  +  '-'  +  aDate[1]  +  '-'  +  aDate[2])  
       iDays  =  parseInt(Math.abs(oDate1  -  oDate2)  /  1000  /  60  /  60  /24)    //把相差的毫秒数转换为天数  
       return  iDays  
   }    
  function  btnCount_Click(){  
      s1 = document.getElementById("day");
      s2 =  document.getElementById("endDay");
      if(s1!=null&&s2!=null){
       s11=s1.value;
       s22=s2.value;
       if(DateDiff(s11,s22)==6){
		 $('form1').submit();
       }else{
        alert(s11+"和"+s22+",相差"+DateDiff(s11,s22)+"天,不是一周,请确认日期");
        return;
       }
      }
   }  

</script>
	</head>
	<body>
		<center>
		<h2 style="font-weight:bold;">
			周活跃用户信息统计
		</h2>
		<form  name="form1" id="form1" method="post">
		<!--
		开始日期：<input type='text' name='day' id="day" value='<%=startDay %>'>
		-- 结束日期：<input type='text' name='endDay' id="endDay" value='<%=endDay %>'>(格式：2012-02-09)
		  -->
			  
		 开始日期： <input type="text" name="day" id="day" class="nqz_input" style="width:100px;" readonly="readonly" onfocus="new Calendar(false,'yyyy-MM-dd').showTimePicker(this);" value="<%=startDay %>"/>&nbsp;到
		 结束日期： <input type="text" name="endDay" id="endDay" class="nqz_input" style="width:100px;" readonly="readonly" onfocus="new Calendar(false,'yyyy-MM-dd').showTimePicker(this);" value="<%=endDay %>"/>
		
		
		
		 
                                             分区：<select name="fenqu">
                       <option  value="0">全部</option>
                       <% 
                       for(int i = 0 ; i < fenQuList.size() ; i++){
                       String[] _fenqu = fenQuList.get(i);
                       %>
                        <option value="<%=_fenqu[1] %>" 
                        <%
                        if(_fenqu[1].equals(fenqu)){ out.print(" selected=\"selected\""); }
                         %>
                          ><%=_fenqu[1] %></option>
                       <%
                       }
                       %>
                      
                </select>
                  <br/><br/>
		    		<input name="" type="button" onclick="btnCount_Click()" value="提交" />
		    		
		    		
		    		</form><br>
		    		
		    		<a href="activeUserStat_week.jsp">周活跃用户</a>|
		    		<a href="active_liuCun_week.jsp">周留存活跃用户</a>|
		    		<a href="active_huiliu_week.jsp">周回流活跃用户</a>|
		    		<a href="active_jihuo_week.jsp">再次激活用户</a>
		    		
		    		<h3>周活跃用户信息统计</h3>
		    		<%
		    		out.println("<table id='test2' align='center' width='90%' cellpadding='1' bgcolor='#000000' cellspacing='1' border='0'>");
					out.print("<tr bgcolor='#EEEEBB'>");
		    		for(int c = 0 ; c < realChannelList.size() ; c++){
		    			out.print("<td>"+realChannelList.get(c)+"</td>");
		    		}
					out.println("</tr>");
						out.print("<tr bgcolor='#FFFFFF'>");
						for(int c = 0 ; c < realChannelList.size() ; c++){
			    			out.print("<td>"+channel_regday_nums[c]+"</td>");
			    		}
						out.println("</tr>");
					out.println("</table><br>");
		    		%>
		 
		</center>
		<br>
		<i>周活跃用户定义：等级大于40级，1周内独立登录3次或以上，并累计在线时长达到6小时或以上</i>
		<br>
	</body>
</html>
