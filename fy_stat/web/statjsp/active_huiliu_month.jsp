<%@ page contentType="text/html;charset=UTF-8"%>
<%@include file="../inc.jsp"%>
<%@ page
	import="com.xuanzhi.tools.cache.diskcache.concrete.DefaultDiskCache,org.slf4j.*,java.io.*,java.util.*,com.xuanzhi.tools.text.*,
	com.xuanzhi.tools.text.*,org.springframework.context.ApplicationContext,org.springframework.web.context.support.WebApplicationContextUtils,
	com.sqage.stat.commonstat.dao.UserDao,com.sqage.stat.commonstat.entity.User,com.sqage.stat.commonstat.manager.UserManager,
	com.sqage.stat.commonstat.manager.Impl.*,com.sqage.stat.service.*,com.sqage.stat.model.Channel"
	%>
  <%
	String startDay = request.getParameter("day");
	String endDay = request.getParameter("endDay");
	String qudao=request.getParameter("qudao");
	String fenqu=request.getParameter("fenqu");
	List<Channel> channelList = (ArrayList<Channel>)session.getAttribute("QUDAO_LIST");
	ArrayList<String []> fenQuList  =  (ArrayList<String []>) session.getAttribute("FENQU_LIST");
	
	
	if("0".equals(fenqu)){fenqu=null;}
	if("0".equals(qudao)){qudao=null;}
	
	String today = DateUtil.formatDate(new Date(),"yyyy-MM-dd");
	if(startDay == null) startDay = today;
	if(endDay == null) endDay = today;
	
              
              UserManagerImpl userManager=UserManagerImpl.getInstance();
              PlayGameManagerImpl playGameManager=PlayGameManagerImpl.getInstance();
              OnLineUsersCountManagerImpl onLineUsersCountManager=OnLineUsersCountManagerImpl.getInstance();
              ChongZhiManagerImpl chongZhiManager=ChongZhiManagerImpl.getInstance();
              
              if(channelList==null || channelList.size() == 0)
	           {
	             ChannelManager cmanager = ChannelManager.getInstance();
		         channelList = cmanager.getChannels();//渠道信息
		         session.removeAttribute("QUDAO_LIST");
		         session.setAttribute("QUDAO_LIST", channelList);
	           }
	         if(fenQuList==null)
	           {
		         fenQuList= userManager.getFenQu(null);//获得现有的分区信息
		         session.removeAttribute("FENQU_LIST");
		         session.setAttribute("FENQU_LIST", fenQuList);
	           }
              
              //ArrayList<String []> fenQuList= userManager.getFenQu(null);//获得现有的渠道信息
              //List<Channel> channelList = cmanager.getChannels();//渠道信息

	//对比各个渠道数据

	ArrayList<String> realChannelList = new ArrayList<String>();
	realChannelList.add("人数");
	realChannelList.add("平均独立登录次数");
	realChannelList.add("平均在线时长(小时)");
	realChannelList.add("独立付费人数");
	
	realChannelList.add("付费总金额(元)");
	realChannelList.add("平均付费次数");
	realChannelList.add("独立消费人数");
	realChannelList.add("消费金额");
	
	realChannelList.add("元宝库存");
	realChannelList.add("游戏币库存");
	
		String channel_regday_nums[] = new String[realChannelList.size()];
				
				String userCount=playGameManager.active_userCount_Mhuiliu(startDay,endDay,fenqu,qudao);
				if(userCount!=null){
				channel_regday_nums[0] =userCount;//人数
				}
				
				  String loginUserCount=playGameManager.active_userLoginTimes_Mhuiliu(startDay,endDay,fenqu,qudao);
                  if(loginUserCount!=null){
				   channel_regday_nums[1] =loginUserCount;//平均独立登录次数
				   }
				   
				   String avgOnLineTime=playGameManager.active_userAVGOnLineTime_Mhuiliu(startDay,endDay,fenqu,qudao);
				  if(avgOnLineTime!=null){
				   channel_regday_nums[2] =avgOnLineTime;//平均在线时长
				    }
				    
				   String pay_userCount=playGameManager.active_pay_userCount_Mhuiliu(startDay,endDay,fenqu,qudao);//独立付费人数
				  if(pay_userCount!=null){
				   channel_regday_nums[3] =pay_userCount;//独立付费人数
				    }
				    
				  String pay_Money=playGameManager.active_pay_Money_Mhuiliu(startDay,endDay,fenqu,qudao);
				  if(pay_Money!=null){
				   channel_regday_nums[4] =pay_Money;//付费总金额(元)
				}
  
                
				  String pay_AVGTimes=playGameManager.active_pay_AVGTimes_Mhuiliu(startDay,endDay,fenqu,qudao);
				  if(pay_AVGTimes!=null){
				   channel_regday_nums[5] =pay_AVGTimes;//平均付费次数
				  }
				
				  //String spend_userCount=playGameManager.active_spend_userCount_Mhuiliu(startDay,endDay,fenqu,qudao);
				  String spend_userCount=null;
				  if(spend_userCount!=null){
				   channel_regday_nums[6] =spend_userCount;//独立消费人数
				  }
				 //String spend_money=playGameManager.active_spend_money_Mhuiliu(startDay,endDay,fenqu,qudao);
				 String spend_money=null;
				 if(spend_money!=null){
				   channel_regday_nums[7] =spend_money;//消费金额
				}
				
				 //String left_yuanbao=playGameManager.active_left_yuanbao_Mhuiliu(startDay,endDay,fenqu,qudao);
				 String left_yuanbao=null;
				 if(left_yuanbao!=null){
				channel_regday_nums[8] =left_yuanbao;//元宝库存
				}
				
				 //String left_youxibi=playGameManager.active_left_youxibi_Mhuiliu(startDay,endDay,fenqu,qudao);
				 String left_youxibi=null;
				  if(left_youxibi!=null){
				channel_regday_nums[9] =left_youxibi;//游戏币
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
			月回流用户信息统计
		</h2>
		<form  method="post">
		<!--  开始日期：<input type='text' name='day' value='<%=startDay %>'>
		-- 结束日期：<input type='text' name='endDay' value='<%=endDay %>'>(格式：2012-02-09)--> 
		
		  
		 开始日期： <input type="text" name="day" id="day" class="nqz_input" style="width:100px;" readonly="readonly" onfocus="new Calendar(false,'yyyy-MM-dd').showTimePicker(this);" value="<%=startDay %>"/>&nbsp;到
		  结束日期： <input type="text" name="endDay" id="endDay" class="nqz_input" style="width:100px;" readonly="readonly" onfocus="new Calendar(false,'yyyy-MM-dd').showTimePicker(this);" value="<%=endDay %>"/>
		
		<br/><br/>
	
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
		    		<input type='submit' value='提交'></form><br>
		    		
		    		<a href="activeUserStat_month.jsp">月活跃用户</a>|
		    		<a href="active_liuCun_month.jsp">月留存活跃用户</a>|
		    		<a href="active_huiliu_month.jsp">月回流活跃用户</a>|
		    		<a href="active_jihuo_month.jsp">再次激活用户</a>
		    		
		    		<h3>月回流用户信息统计</h3>
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
		<i>月活跃用户定义：1月内独立登录12次或以上，或累计在线时长达到24小时或以上</i><br/><br/>
		<i>月留存定义：M0活跃，M1不活跃，M2活跃，则视为W2月的回流活跃用户</i>
		
		
		<br>
	</body>
</html>
