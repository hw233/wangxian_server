<%@ page contentType="text/html;charset=UTF-8"%>
<%@include file="../inc.jsp"%>
<%@ page
	import="com.xuanzhi.tools.cache.diskcache.concrete.DefaultDiskCache,org.slf4j.*,java.io.*,java.util.*,com.xuanzhi.tools.text.*,
	com.xuanzhi.tools.text.*,org.springframework.context.ApplicationContext,org.springframework.web.context.support.WebApplicationContextUtils,
	com.sqage.stat.commonstat.dao.UserDao,com.sqage.stat.commonstat.entity.User,com.sqage.stat.commonstat.manager.UserManager,
	com.sqage.stat.commonstat.manager.Impl.UserManagerImpl,com.sqage.stat.commonstat.manager.Impl.*,com.sqage.stat.service.*,com.sqage.stat.model.Channel"
	%>
	<%!
	Object lock = new Object(){};
      %>
  <%
	String startDay = request.getParameter("day");
	String endDay = request.getParameter("endDay");
	String qudao=request.getParameter("qudao");
	String fenqu=request.getParameter("fenqu");
	
	if("0".equals(qudao)){qudao=null;}
	if("0".equals(fenqu)){fenqu=null;}
	
	String today = DateUtil.formatDate(new Date(),"yyyy-MM-dd");
	if(startDay == null) startDay = today;
	if(endDay == null) endDay = today;
	
	Date t = DateUtil.parseDate(endDay,"yyyy-MM-dd");
	Date s = DateUtil.parseDate(startDay,"yyyy-MM-dd");
	ArrayList<String> dayList = new ArrayList<String>();
	
	/**
	*获得渠道信息
	**/
		      ChannelManager cmanager = ChannelManager.getInstance();
              UserManagerImpl userManager=UserManagerImpl.getInstance();
              PlayGameManagerImpl playGameManager=PlayGameManagerImpl.getInstance();
              OnLineUsersCountManagerImpl onLineUsersCountManager=OnLineUsersCountManagerImpl.getInstance();
              ChongZhiManagerImpl chongZhiManager=ChongZhiManagerImpl.getInstance();
              
              List<Channel> channelList = cmanager.getChannels();//渠道信息
              ArrayList<String []> fenQuList= userManager.getFenQu(null);//获得现有的渠道信息
           
synchronized(lock){
     List<Date[]> zhouList=DateUtil.splitTime(s,t,1);//把时间段时间分周

     for(Date[] zhou: zhouList)
		{
				String zhou_startDay=DateUtil.formatDate(zhou[0], "yyyy-MM-dd");
				String zhou_endDay=DateUtil.formatDate(zhou[1], "yyyy-MM-dd");
				String zhou_fanwei=zhou_startDay+"to"+zhou_endDay;
			dayList.add(zhou_fanwei);
		}

	//while(s.getTime() <= t.getTime() + 3600000){
	//	String day = DateUtil.formatDate(s,"yyyy-MM-dd");
	//	dayList.add(day);
	//	s.setTime(s.getTime() + 24*3600*1000);
	//}
}	
	//对比各个渠道数据

	ArrayList<String> realChannelList = new ArrayList<String>();
	realChannelList.add("注册人数(不分区)");
	realChannelList.add("有效注册人数");
	realChannelList.add("未激活账号(不分区)");
	realChannelList.add("独立进入");
	
	realChannelList.add("独立进入(当天)");
	realChannelList.add("平均在线");
	realChannelList.add("最高在线");
	realChannelList.add("平均在线时长(分钟)");
	
	realChannelList.add("付费人数");
	realChannelList.add("新付费人数");
	realChannelList.add("有效用户平均在线时长(分钟)");
	realChannelList.add("充值金额");
	
	realChannelList.add("注册ARPU(不分区)");
	realChannelList.add("付费ARPU");
	realChannelList.add("元宝库存");
	realChannelList.add("游戏币库存");

		//int channel_regday_nums[][] = new int[0][0];
		long channel_regday_nums[][] = new long[realChannelList.size()][dayList.size()];
		
       for(int k = 0 ; k < dayList.size() ; k++){
				String _day = dayList.get(k);
				String[] day= _day.split("to");
				
				Long registUserCount=userManager.getRegistUerCount(day[0],day[1],qudao,null,null);
				if(registUserCount!=null){
				channel_regday_nums[0][k] =registUserCount;//注册人数
				}
				
				  Long reg_LoginUserCount=playGameManager.getReg_LoginUserCount(day[0],day[1],qudao,fenqu,null);
                  if(reg_LoginUserCount!=null){
				   channel_regday_nums[4][k] =reg_LoginUserCount;//独立登陆(当天)
				   }
				   
				   Long youXiaoUserCount=userManager.getYouXiaoUerCount(day[0],day[1],qudao,fenqu,null);
				  if(youXiaoUserCount!=null){
				   channel_regday_nums[1][k] =youXiaoUserCount;//有效注册人数
				    }
				    
				   Long zhuCeNOCreatPlayer=0L;
				   Long reg_LoginUserCount_all=playGameManager.getReg_LoginUserCount(day[0],day[1],qudao,null,null);//注册并登陆的用户数，不分区
				  if(zhuCeNOCreatPlayer!=null){
				   zhuCeNOCreatPlayer= registUserCount-reg_LoginUserCount_all;
				   channel_regday_nums[2][k] =zhuCeNOCreatPlayer;//注册但未激活账号
				    }
				    
				  Long enterGameUserCount=playGameManager.getEnterGameUserCount(day[0],day[1],qudao,fenqu,null);
				  if(enterGameUserCount!=null){
				   channel_regday_nums[3][k] =enterGameUserCount;//独立登陆
				}
  
                
				  Long avgOnlineUserCount=onLineUsersCountManager.getAvgOnlineUserCount(day[0],day[1],qudao,fenqu,null);
				  if(avgOnlineUserCount!=null){
				channel_regday_nums[5][k] =avgOnlineUserCount;//平均用户数
				  }
				
				  Long maxOnlineUserCount=onLineUsersCountManager.getMaxOnlineUserCount(day[0],day[1],qudao,fenqu,null);
				  if(maxOnlineUserCount!=null){
				channel_regday_nums[6][k] =maxOnlineUserCount;//每分钟最高在线
				  }
				 Long avgOnLineTime=playGameManager.getAvgOnLineTime(day[0],day[1],qudao,fenqu,null);//在线时长
				 if(avgOnLineTime!=null){
				 Long avgOnLine=avgOnLineTime/60000;
				channel_regday_nums[7][k] =avgOnLine;//每人平均在线
				}
				
				 Long chongZhiUserCount=chongZhiManager.getChongZhiUserCount(day[0],day[1],qudao,fenqu,null);
				 if(chongZhiUserCount!=null){
				channel_regday_nums[8][k] =chongZhiUserCount;//付费人数
				}
				
				 Long unAheadChongZhiUserCount=chongZhiManager.getUnAheadChongZhiUserCount(day[0],day[1],qudao,fenqu,null);
				  if(unAheadChongZhiUserCount!=null){
				channel_regday_nums[9][k] =unAheadChongZhiUserCount;//新付费人数
				  }
				
				 Long youXiaoUerAVGOnLineTime=userManager.getYouXiaoUerAVGOnLineTime(day[0],day[1],qudao,fenqu,null);
				 if(youXiaoUerAVGOnLineTime!=null){
				 Long fenzheng= youXiaoUerAVGOnLineTime/60000;
				 channel_regday_nums[10][k] =fenzheng;//有效用户平均在线时长
				   }
				
				 Long chongZhiCount=chongZhiManager.getChongZhiCount(day[0],day[1],qudao,fenqu,null);
				 if(chongZhiCount!=null){
				channel_regday_nums[11][k] =chongZhiCount;//充值金额
				}
				
				Long chongZhiCount_all=chongZhiManager.getChongZhiCount(day[0],day[1],qudao,null,null);//所有充值金额，不分区
				if(registUserCount!=null&&chongZhiCount_all!=null&&registUserCount!=0){
				   Long zhuceAEPU=chongZhiCount_all/registUserCount;
				   channel_regday_nums[12][k] =zhuceAEPU;//注册AEPU
				}
				
				if(chongZhiCount!=null&&chongZhiUserCount!=null&&chongZhiUserCount!=0){
				   Long fufeiAEPU= chongZhiCount/chongZhiUserCount;
				  channel_regday_nums[13][k] =fufeiAEPU;//付费AEPU
				}
				/**
				  Long leftYouXiBiCount=playGameManager.getLeftYuanbaoCount(day[1],qudao,fenqu,null);
				if(leftYouXiBiCount!=null){
				channel_regday_nums[14][k] =leftYouXiBiCount;//元宝库存
				 }
				 
				  Long leftYuanBaoCount=playGameManager.getLeftYouXIBiCount(day[1],qudao,fenqu,null);
				if(leftYuanBaoCount!=null){
				channel_regday_nums[15][k] =leftYuanBaoCount;//游戏币库存
				  }
				  */
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
var mycars=[<% for(int i = 0 ; i < dayList.size() ; i++){ out.print("\""+dayList.get(i)+"\",");} %>"E"];
       function drawRegUserFlotr(){
		    var f = Flotr.draw(
		$('regUserContainer'), [
		<%for(int j = 0 ; j < realChannelList.size() ; j++){
			StringBuffer sb2 = new StringBuffer();
			sb2.append("[");
			for(int kk = 0 ; kk < channel_regday_nums[j].length ; kk++){
				sb2.append("["+kk+","+channel_regday_nums[j][kk]+"]");
				if(kk < channel_regday_nums[j].length -1) sb2.append(",");
			}
			sb2.append("]");
			out.println("{");
			out.println("data:"+sb2.toString()+",");
			out.println("label:'"+realChannelList.get(j)+"'");
			out.println("}");
			if(j < realChannelList.size()-1) out.print(",");
		}
		%>
		],{
			xaxis:{
				noTicks: <%=dayList.size()%>,	// Display 7 ticks.	
				tickFormatter: function(n){ return mycars[Math.floor(n)];}, // =>
				min: 0,
				max: <%= dayList.size()%>
			},
			yaxis:{
				tickFormatter: function(n){ return (n);}, // =>
				min: 0,
			},
			legend:{
				position: 'ne', // => position the legend 'south-east'.
				backgroundColor: '#D2E8FF' // => a light blue background color.
			},
			mouse:{
				track: true,
				color: 'purple',
				sensibility: 1, // => distance to show point get's smaller
				trackDecimals: 2,
				trackFormatter: function(obj){ return 'y = ' + obj.y; }
			}
		}
	);
	}

</script>
	</head>
	<body>
		<center>
		<h2 style="font-weight:bold;">
			统计总表数据（周报）
		</h2>
		<form  method="post">开始日期：<input type='text' name='day' value='<%=startDay %>'>
		-- 结束日期：<input type='text' name='endDay' value='<%=endDay %>'>(格式：2012-02-09)
		<br/><br/>
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
                </select> &nbsp;&nbsp;
                                             分区：<select name="fenqu">
                       <option  value="0">全部</option>
                       <% 
                       for(int i = 0 ; i < fenQuList.size() ; i++){
                       String[] _fenqu = fenQuList.get(i);
                       %>
                        <option value="<%=_fenqu[1] %>" 
                        <%
                        if(_fenqu[1].equals(fenqu)){ out.print(" selected=\"selected\"");  }
                         %>
                          ><%=_fenqu[1] %></option>
                       <%
                       }
                       %>
                      
                </select>&nbsp;&nbsp;
		    		<input type='submit' value='提交'></form>
		    		<h3>统计总表数据（周报）</h3>
		    		<%
		    		out.println("<table id='test2' align='center' width='90%' cellpadding='1' bgcolor='#000000' cellspacing='1' border='0'>");
					out.print("<tr bgcolor='#EEEEBB'><td>日期</td>");
		    		for(int c = 0 ; c < realChannelList.size() ; c++){
		    			out.print("<td>"+realChannelList.get(c)+"</td>");
		    		}
					out.println("</tr>");
					for(int i = 0 ; i < dayList.size() ; i++){
						out.print("<tr bgcolor='#FFFFFF'><td>"+dayList.get(i)+"</td>");
						for(int c = 0 ; c < realChannelList.size() ; c++){
			    			out.print("<td>"+channel_regday_nums[c][i]+"</td>");
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
