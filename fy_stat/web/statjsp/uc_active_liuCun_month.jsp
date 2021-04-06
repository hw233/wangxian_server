<%@ page contentType="text/html;charset=UTF-8"%>
<%@include file="../inc.jsp"%>
<%@ page
	import="com.xuanzhi.tools.cache.diskcache.concrete.DefaultDiskCache,org.slf4j.*,java.io.*,java.util.*,com.xuanzhi.tools.text.*,java.util.*,
	com.xuanzhi.tools.text.*,org.springframework.context.ApplicationContext,org.springframework.web.context.support.WebApplicationContextUtils,
	com.sqage.stat.commonstat.dao.UserDao,com.sqage.stat.commonstat.entity.User,com.sqage.stat.commonstat.manager.UserManager,
	com.sqage.stat.commonstat.manager.Impl.UserManagerImpl,com.sqage.stat.commonstat.manager.Impl.*,com.sqage.stat.service.*,com.sqage.stat.model.Channel"
	%>
  <%
	String startDay = request.getParameter("day");
	String endDay = request.getParameter("endDay");
	String qudao=request.getParameter("qudao");
	String fenqu=request.getParameter("fenqu");
	
	//if("0".equals(fenqu)){fenqu=null;}
	if(fenqu==null||"-1".equals(fenqu)){fenqu="龙吟虎啸','南蛮入侵','万箭齐发";}
	if("0".equals(qudao)){qudao=null;}
	
	String today = DateUtil.formatDate(new Date(),"yyyy-MM-dd");
	if(startDay == null) startDay = today;
	if(endDay == null) endDay = today;
	
	/**
	*获得渠道信息
	**/
		      ChannelManager cmanager = ChannelManager.getInstance();
             // UserManagerImpl userManager=UserManagerImpl.getInstance();
              PlayGameManagerImpl playGameManager=PlayGameManagerImpl.getInstance();
              OnLineUsersCountManagerImpl onLineUsersCountManager=OnLineUsersCountManagerImpl.getInstance();
              ChongZhiManagerImpl chongZhiManager=ChongZhiManagerImpl.getInstance();
              
              //ArrayList<String []> fenQuList= userManager.getFenQu(null);//获得现有的渠道信息
              List<Channel> channelList = cmanager.getChannels();//渠道信息

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
				
				String userCount=playGameManager.active_userCount_Mliucun(startDay,endDay,fenqu,qudao);
				if(userCount!=null){
				channel_regday_nums[0] =userCount;//人数
				}
				
				  String loginUserCount=playGameManager.active_userLoginTimes_Mliucun(startDay,endDay,fenqu,qudao);
                  if(loginUserCount!=null){
				   channel_regday_nums[1] =loginUserCount;//平均独立登录次数
				   }
				   
				   String avgOnLineTime=playGameManager.active_userAVGOnLineTime_Mliucun(startDay,endDay,fenqu,qudao);
				  if(avgOnLineTime!=null){
				   channel_regday_nums[2] =avgOnLineTime;//平均在线时长
				    }
				    
				   String pay_userCount=playGameManager.active_pay_userCount_Mliucun(startDay,endDay,fenqu,qudao);//独立付费人数
				  if(pay_userCount!=null){
				   channel_regday_nums[3] =pay_userCount;//独立付费人数
				    }
				    
				  String pay_Money=playGameManager.active_pay_Money_Mliucun(startDay,endDay,fenqu,qudao);
				  if(pay_Money!=null){
				   channel_regday_nums[4] =pay_Money;//付费总金额(元)
				}
  
                
				  String pay_AVGTimes=playGameManager.active_pay_AVGTimes_Mliucun(startDay,endDay,fenqu,qudao);
				  if(pay_AVGTimes!=null){
				   channel_regday_nums[5] =pay_AVGTimes;//平均付费次数
				  }
				
				  //String spend_userCount=playGameManager.active_spend_userCount_Mliucun(startDay,endDay,fenqu,qudao);
				   String spend_userCount=null;
				  if(spend_userCount!=null){
				   channel_regday_nums[6] =spend_userCount;//独立消费人数
				  }
				 //String spend_money=playGameManager.active_spend_money_Mliucun(startDay,endDay,fenqu,qudao);
				 String spend_money=null;
				 if(spend_money!=null){
				   channel_regday_nums[7] =spend_money;//消费金额
				}
				
				 //String left_yuanbao=playGameManager.active_left_yuanbao_Mliucun(startDay,endDay,fenqu,qudao);
				 String left_yuanbao=null;
				 if(left_yuanbao!=null){
				channel_regday_nums[8] =left_yuanbao;//元宝库存
				}
				
				// String left_youxibi=playGameManager.active_left_youxibi_Mliucun(startDay,endDay,fenqu,qudao);
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

	</head>
	<body>
		<center>
		<h2 style="font-weight:bold;">
			UC月留存用户信息统计
		</h2>
		<form  method="post">开始日期：<input type='text' name='day' value='<%=startDay %>'>
		-- 结束日期：<input type='text' name='endDay' value='<%=endDay %>'>(格式：2012-02-09)
		<br/><br/>
		<!--
                                       渠道：<select name="qudao">
                       <option  value="0">全部</option>
                       <% 
                       for(int i = 0 ; i < channelList.size() ; i++){
                       Channel _channel = channelList.get(i);
                       %>
                        <option value="<%=_channel.getKey() %>" 
                        <%
                        if(_channel.getKey().equals(qudao)){
                        out.print(" selected=\"selected\"");
                        }
                         %>
                          ><%=_channel.getName() %></option>
                       <%
                       }
                       %>
                      
                     </select> &nbsp;&nbsp;&nbsp;&nbsp;   
                       --> 
                                             
                                  分区：<select name="fenqu">
                       <option  value="-1">全部</option>
                    
                       <option value="龙吟虎啸" 
                        <%
                        if("龙吟虎啸".equals(fenqu)){ out.print(" selected=\"selected\""); }
                         %>
                          >龙吟虎啸</option>
                          
                       <option value="南蛮入侵" 
                        <%
                        if("南蛮入侵".equals(fenqu)){ out.print(" selected=\"selected\""); }
                         %>
                          >南蛮入侵</option>
                          
                       <option value="万箭齐发" 
                        <%
                        if("万箭齐发".equals(fenqu)){ out.print(" selected=\"selected\""); }
                         %>
                          >万箭齐发</option>
                   </select>
                
                
		    		<input type='submit' value='提交'></form><br>
		    		
		    		<a href="uc_activeUserStat_month.jsp">月活跃用户</a>|
		    		<a href="uc_active_liuCun_month.jsp">月留存活跃用户</a>|
		    		<!-- 
		    		<a href="active_huiliu_month.jsp">月回流活跃用户</a>|
		    		<a href="active_jihuo_month.jsp">再次激活用户</a>
		    		 -->
		    		
		    		<h3>UC月留存用户信息统计</h3>
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
		<i>月留存定义：上月达到活跃标准，本月依然达到活跃标准的用户</i>
		
		
		<br>
	</body>
</html>
