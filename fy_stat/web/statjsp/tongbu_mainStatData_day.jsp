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
	String qudao=request.getParameter("qudao");
	String jixing = request.getParameter("jixing");
	if("0".equals(jixing)){jixing=null;}
	String tqudao =qudao;
	
	if(qudao==null||"-1".equals(qudao)){ qudao="tongbu_MIESHI','TBTNEWSDK_MIESHI','TBTSDK_MIESHI"; tqudao="-1";   }
	

	String today = DateUtil.formatDate(new Date(),"yyyy-MM-dd");
	if(startDay == null) startDay = today;
	if(endDay == null) endDay = today;
	
	Date t = DateUtil.parseDate(endDay,"yyyy-MM-dd");
	Date s = DateUtil.parseDate(startDay,"yyyy-MM-dd");
	ArrayList<String> dayList = new ArrayList<String>();
	
	          //ChannelManager cmanager = ChannelManager.getInstance();
              UserManagerImpl userManager=UserManagerImpl.getInstance();
              PlayGameManagerImpl playGameManager=PlayGameManagerImpl.getInstance();
              OnLineUsersCountManagerImpl onLineUsersCountManager=OnLineUsersCountManagerImpl.getInstance();
              ChongZhiManagerImpl chongZhiManager=ChongZhiManagerImpl.getInstance();
              //List<Channel> channelList = cmanager.getChannels();//渠道信息
              //ArrayList<String []> fenQuList= userManager.getFenQu(null);//获得现有的渠道信息
           
 
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
	realChannelList.add("充值金额(分)");
	
	realChannelList.add("注册ARPU(不分区)");
	realChannelList.add("付费ARPU(分)");
	realChannelList.add("元宝库存");
	realChannelList.add("游戏币库存");

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
				String key="";
				if("null".equals(qudao)||"-1".equals(tqudao)){key=_day+"tongbu"+jixing;}
				else{
				key=_day+qudao+jixing;
				}
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
				Long registUserCount=userManager.getRegistUerCount(_day,_day,qudao,null,jixing);
				if(registUserCount!=null){
				  stat_common[1]=registUserCount.toString();
				  channel_regday_nums[0][k] =registUserCount;//注册人数
				}else{stat_common[1]="0";}
				
				  Long reg_LoginUserCount=playGameManager.getReg_LoginUserCount(_day,_day,qudao,null,jixing);//当天注册，在分区道登陆的用户
                  if(reg_LoginUserCount!=null){
                  stat_common[5]=reg_LoginUserCount.toString();
				  channel_regday_nums[4][k] =reg_LoginUserCount;//独立登陆(当天)
				   }else{ stat_common[5]="0";}
				
				  Long youXiaoUserCount=userManager.getYouXiaoUerCount(_day,_day,qudao,null,jixing);
				if(youXiaoUserCount!=null){
				  stat_common[2]=youXiaoUserCount.toString();
				  channel_regday_nums[1][k] =youXiaoUserCount;//有效注册人数
				    }else{stat_common[2]="0";}

				 
				 Long zhuCeNOCreatPlayer=0L;
				  Long reg_LoginUserCount_all=playGameManager.getReg_LoginUserCount(_day,_day,qudao,null,jixing);//注册并登陆的用户，不区分区
				if(registUserCount!=null&&reg_LoginUserCount_all!=null){
			     zhuCeNOCreatPlayer= registUserCount -reg_LoginUserCount_all;
				   stat_common[3]=zhuCeNOCreatPlayer.toString();
				   channel_regday_nums[2][k] =zhuCeNOCreatPlayer;//注册但未激活账号
				}else{stat_common[3]="0";}
				
				  Long enterGameUserCount=playGameManager.getEnterGameUserCount(_day,_day,qudao,null,jixing);
				  if(enterGameUserCount!=null){
				  stat_common[4]=enterGameUserCount.toString();
				  channel_regday_nums[3][k] =enterGameUserCount;//独立登陆
				}else{ stat_common[4]="0";}
  
                
				  Long avgOnlineUserCount=onLineUsersCountManager.getAvgOnlineUserCount(_day,_day,qudao,null,jixing);
				  if(avgOnlineUserCount!=null){
				  stat_common[6]=avgOnlineUserCount.toString();
				  channel_regday_nums[5][k] =avgOnlineUserCount;//每分钟平均在线
				  }else{stat_common[6]="0";}
				
				  Long maxOnlineUserCount=onLineUsersCountManager.getMaxOnlineUserCount(_day,_day,qudao,null,jixing);
				  if(maxOnlineUserCount!=null){
				  stat_common[7]=maxOnlineUserCount.toString();
				  channel_regday_nums[6][k] =maxOnlineUserCount;//每分钟最高在线
				  }else{ stat_common[7]="0";}
				  
				 Long avgOnLineTime=playGameManager.getAvgOnLineTime(_day,_day,qudao,null,jixing);
				 if(avgOnLineTime!=null){
				  Long fenzhong=avgOnLineTime/60000;
				  stat_common[8]=fenzhong.toString();
				  channel_regday_nums[7][k] =fenzhong;//平均在线
				}else{stat_common[8]="0";}
				
				 Long chongZhiUserCount=chongZhiManager.getChongZhiUserCount(_day,_day,qudao,null,jixing);
				 if(chongZhiUserCount!=null){
				   stat_common[9]=chongZhiUserCount.toString();
				   channel_regday_nums[8][k] =chongZhiUserCount;//付费人数
				 }else{ stat_common[9]="0";}
				 Long unAheadChongZhiUserCount=chongZhiManager.getUnAheadChongZhiUserCount(_day,_day,qudao,null,jixing);
				 if(unAheadChongZhiUserCount!=null){
				   stat_common[10]=unAheadChongZhiUserCount.toString();
				   channel_regday_nums[9][k] =unAheadChongZhiUserCount;//新付费人数
				  }else{stat_common[10]="0";}
				
				 Long youXiaoUerAVGOnLineTime=userManager.getYouXiaoUerAVGOnLineTime(_day,_day,qudao,null,jixing);
				 //Long chongzhi_LoginUserCount=chongZhiManager.getChongZhi_LoginUserCount(_day,_day,qudao,fenqu,null);
				 if(youXiaoUerAVGOnLineTime!=null){
				 Long fenzheng= youXiaoUerAVGOnLineTime/60000;
				 stat_common[11]=fenzheng.toString();
				 channel_regday_nums[10][k] =fenzheng;//有效用户平均在线时长
				   }else{stat_common[11]="0";}
				   
				 Long chongZhiCount=chongZhiManager.getChongZhiCount(_day,_day,qudao,null,jixing);//分区充值金额
				 if(chongZhiCount!=null){
				 stat_common[12]=chongZhiCount.toString();
				 channel_regday_nums[11][k] =chongZhiCount;//充值金额
				   }else{ stat_common[12]="0";}
				
				 Long chongZhiCount_all=chongZhiManager.getChongZhiCount(_day,_day,qudao,null,jixing);//全部充值金额，不分区
				if(registUserCount!=null&&chongZhiCount_all!=null&&registUserCount!=0){
				Long zhuceAEPU=chongZhiCount_all/registUserCount;
				stat_common[13]=zhuceAEPU.toString();
				channel_regday_nums[12][k] =zhuceAEPU;//注册AEPU
				   }else{stat_common[13]="0";}
				   
				if(chongZhiCount!=null&&chongZhiUserCount!=null&&chongZhiUserCount!=0){
				Long fufeiAEPU= chongZhiCount/chongZhiUserCount;
				  stat_common[14]=fufeiAEPU.toString();
				  channel_regday_nums[13][k] =fufeiAEPU;//付费AEPU
				  }else{ stat_common[14]="0";}
				  /**
				Long leftYouXiBiCount=playGameManager.getLeftYuanbaoCount(_day,qudao,fenqu,null);
			    if(leftYouXiBiCount!=null){
				  stat_common[15]=leftYouXiBiCount.toString();
				  channel_regday_nums[14][k] =leftYouXiBiCount;//元宝库存
				  }else{stat_common[15]="0";}
				  Long leftYuanBaoCount=playGameManager.getLeftYouXIBiCount(_day,qudao,fenqu,null);
				if(leftYuanBaoCount!=null){
				stat_common[16]=leftYuanBaoCount.toString();
				channel_regday_nums[15][k] =leftYuanBaoCount;//游戏币库存
				  }else{stat_common[16]="0";}
				  */
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
			统计总表数据（日报）
		</h2>
		<form  method="post">
		 <input type='hidden' name='flag' value='true'/>
		  
		 开始日期： <input type="text" name="day" id="day" class="nqz_input" style="width:100px;" readonly="readonly" onfocus="new Calendar(false,'yyyy-MM-dd').showTimePicker(this);" value="<%=startDay %>"/>&nbsp;到
		  结束日期： <input type="text" name="endDay" id="endDay" class="nqz_input" style="width:100px;" readonly="readonly" onfocus="new Calendar(false,'yyyy-MM-dd').showTimePicker(this);" value="<%=endDay %>"/>
		
		<br/><br/>
		       渠道：<select name="qudao" style="width:150px" >
                       <option  value="-1">全部</option>
                       
                        <option value="tongbu_MIESHI" 
                        <%  if("tongbu_MIESHI".equals(qudao)){ out.print(" selected=\"selected\"");  } %>
                        >tongbu_MIESHI</option>
                        
                        <option value="TBTNEWSDK_MIESHI" 
                        <%  if("TBTNEWSDK_MIESHI".equals(qudao)){ out.print(" selected=\"selected\"");  } %>
                        >TBTNEWSDK_MIESHI</option>
                        
                        <option value="TBTSDK_MIESHI" 
                        <%  if("TBTSDK_MIESHI".equals(qudao)){ out.print(" selected=\"selected\"");  } %>
                        >TBTSDK_MIESHI</option>
                        
                 </select> &nbsp;&nbsp;
                                           
                                           平台：<select name="jixing">
                       <option  value="0">全部</option>
                        <option value="Android" 
                        <%
                        if("Android".equals(jixing)){ out.print(" selected=\"selected\"");  }
                         %>>Android</option>
                          
                       <option value="IOS" 
                        <%
                        if("IOS".equals(jixing)){ out.print(" selected=\"selected\"");    }
                         %>>IOS</option>
                  </select>
		          <br/>
		    		<input type='submit' value='提交'></form>
		    		
		    		<h3>统计总表数据（日报）</h3>
		    		<%
		    		out.println("<table id='test2' align='center' width='90%' cellpadding='1' bgcolor='#000000' cellspacing='1' border='0'>");
					out.print("<tr bgcolor='#EEEEBB'><td>日期</td>");
					
					long[] huizong=new long[realChannelList.size()];
		    		for(int c = 0 ; c < realChannelList.size() ; c++){
		    			out.print("<td>"+realChannelList.get(c)+"</td>");
		    		}
					out.println("</tr>");
					if ("true".equals(flag)) {//如果是刚打开页面，不查询
					for(int i = 0 ; i < dayList.size() ; i++){
						out.print("<tr bgcolor='#FFFFFF'><td>"+dayList.get(i)+"</td>");
						for(int c = 0 ; c < realChannelList.size() ; c++){
			    			out.print("<td>"+channel_regday_nums[c][i]+"</td>");
			    			
			    			if(c==6){//汇总最高在线
			    			if(huizong[c]<channel_regday_nums[c][i]){
			    			huizong[c]=channel_regday_nums[c][i];
			    			}
			    			}else
			    			{
			    			huizong[c]+=channel_regday_nums[c][i];
			    			}
			    		}
						out.println("</tr>");
					}
					
					huizong[5]=huizong[5]/dayList.size();
					huizong[7]=huizong[7]/dayList.size();
					huizong[10]=huizong[10]/dayList.size();
					if(huizong[0]!=0){	huizong[12]=huizong[11]/huizong[0];}
					if(huizong[8]!=0){huizong[13]=huizong[11]/huizong[8];}
					
					out.print("<tr bgcolor='#EEEEBB'><td>汇总</td>");
						for(int c = 0 ; c < realChannelList.size() ; c++){
			    			out.print("<td>"+huizong[c]+"</td>");
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
