<%@ page contentType="text/html;charset=UTF-8"%>
<%@include file="../inc.jsp"%>
<%@ page
	import="com.xuanzhi.tools.cache.diskcache.concrete.DefaultDiskCache,org.slf4j.*,java.io.*,java.util.*,com.xuanzhi.tools.text.*,
	org.springframework.context.ApplicationContext,org.springframework.web.context.support.WebApplicationContextUtils,
	com.sqage.stat.commonstat.dao.UserDao,com.sqage.stat.commonstat.entity.User,com.sqage.stat.commonstat.manager.UserManager,
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
	
	String today = DateUtil.formatDate(new Date(),"yyyy-MM-dd");
	if(startDay == null) startDay = today;
	if(endDay == null) endDay = today;
	
	Date t = DateUtil.parseDate(endDay,"yyyy-MM-dd");
	Date s = DateUtil.parseDate(startDay,"yyyy-MM-dd");
	ArrayList<String> dayList = new ArrayList<String>();
	          
              UserManagerImpl userManager=UserManagerImpl.getInstance();
              PlayGameManagerImpl playGameManager=PlayGameManagerImpl.getInstance();
              OnLineUsersCountManagerImpl onLineUsersCountManager=OnLineUsersCountManagerImpl.getInstance();
              ChongZhiManagerImpl chongZhiManager=ChongZhiManagerImpl.getInstance();
              
	//对比各个渠道数据

	ArrayList<String> realChannelList = new ArrayList<String>();
	realChannelList.add("注册人数(不分区)");
	realChannelList.add("有效注册人数");
	realChannelList.add("未激活账号(不分区)");
	realChannelList.add("独立进入（分区不排重）");
	
	realChannelList.add("独立进入(当天)");
	realChannelList.add("平均在线");
	realChannelList.add("最高在线");
	realChannelList.add("平均在线时长(分钟)");
	
	realChannelList.add("付费人数");
	realChannelList.add("新付费人数");
	realChannelList.add("有效用户平均在线时长(分钟)");
	realChannelList.add("充值金额");
	
	realChannelList.add("当天进入AEPU(不分区)");
	realChannelList.add("付费ARPU");
	realChannelList.add("付费渗透率 (千分之)");
	realChannelList.add("独立进入（分区排重）");

	  synchronized(lock){
	     while(s.getTime() <= t.getTime() + 3600000){
		 String day = DateUtil.formatDate(s,"yyyy-MM-dd");
		 dayList.add(day);
		 s.setTime(s.getTime() + 24*3600*1000);
	      }
      }
	long channel_regday_nums[][] = new long[realChannelList.size()][dayList.size()];
     if ("true".equals(flag)) {//如果是刚打开页面，不查询
 
       for(int k = 0 ; k < dayList.size() ; k++){
				String _day = dayList.get(k);
				List<String[]> stat_commonList=null;
				String key=_day;
				if(today.compareTo(_day)>0){
				stat_commonList=userManager.getstat_common(key);
				}
				String[] stat_common=new String[17];
				
				if(stat_commonList!=null&&stat_commonList.size()>0){
				stat_common=stat_commonList.get(0);
				channel_regday_nums[0][k]=Long.parseLong(stat_common[1]);
				channel_regday_nums[1][k]=Long.parseLong(stat_common[2]);
				channel_regday_nums[2][k]=Long.parseLong(stat_common[3]);
				channel_regday_nums[3][k]=Long.parseLong(stat_common[4]);
				
				channel_regday_nums[4][k]=Long.parseLong(stat_common[5]);
				channel_regday_nums[5][k]=Long.parseLong(stat_common[6]);
				channel_regday_nums[6][k]=Long.parseLong(stat_common[7]);
				channel_regday_nums[7][k]=Long.parseLong(stat_common[8]);
				
				channel_regday_nums[8][k]=Long.parseLong(stat_common[9]);
				channel_regday_nums[9][k]=Long.parseLong(stat_common[10]);
				channel_regday_nums[10][k]=Long.parseLong(stat_common[11]);
				channel_regday_nums[11][k]=Long.parseLong(stat_common[12]);
				
				channel_regday_nums[12][k]=Long.parseLong(stat_common[13]);
				channel_regday_nums[13][k]=Long.parseLong(stat_common[14]);
				channel_regday_nums[14][k]=stat_common[15]==null?0L:Long.parseLong(stat_common[15]);
				channel_regday_nums[15][k]=stat_common[16]==null?0L:Long.parseLong(stat_common[16]);
				
				}else{
				stat_common[0]=key;
				
				 
				   
				 Long chongZhiCount=chongZhiManager.getChongZhiCount(_day,_day,null,null,null);//分区充值金额
				 if(chongZhiCount!=null){
				 stat_common[12]=chongZhiCount.toString();
				 channel_regday_nums[11][k] =chongZhiCount;//充值金额
				   }else{ stat_common[12]="0";}
				
				  
	
  
				  if(today.compareTo(_day)>0){ userManager.addstat_common(_day,stat_common);}
				  }
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
			玩家充值消耗
		</h2>
		<form  method="post">
		  <input type='hidden' name='flag' value='true'/>
		   开始日期： <input type="text" name="day" id="day" class="nqz_input" style="width:100px;" readonly="readonly" onfocus="new Calendar(false,'yyyy-MM-dd').showTimePicker(this);" value="<%=startDay %>"/>&nbsp;到
		  结束日期： <input type="text" name="endDay" id="endDay" class="nqz_input" style="width:100px;" readonly="readonly" onfocus="new Calendar(false,'yyyy-MM-dd').showTimePicker(this);" value="<%=endDay %>"/>
		<br/><br/>
		    	<input type='submit' value='提交'></form>
		    		
		    		<%
		    		out.println("<table id='test2' align='center' width='90%' cellpadding='1' bgcolor='#000000' cellspacing='1' border='0'>");
					out.print("<tr bgcolor='#EEEEBB'><td>日期</td>");
					
		    		for(int c = 0 ; c < realChannelList.size() ; c++){
		    			out.print("<td>"+realChannelList.get(c)+"</td>");
		    		}
					out.println("</tr>");
					
					if ("true".equals(flag)) {  //如果是刚打开页面，不查询){
					
					for(int i = 0 ; i < dayList.size() ; i++){
						out.print("<tr bgcolor='#FFFFFF'><td>"+dayList.get(i)+"</td>");
						for(int c = 0 ; c < realChannelList.size() ; c++){
			    			out.print("<td>"+channel_regday_nums[c][i]+"</td>");
			    		}
						out.println("</tr>");
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
