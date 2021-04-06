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
	String tqudao=request.getParameter("tqudao");
	String jixing ="Android";
	
	String qudao="";
	if("anzhi".equals(tqudao)){   qudao="anzhi_MIESHI','ANZHISDK_MIESHI"; tqudao="-1";   }
	if("91用户".equals(tqudao)){  qudao="91ZHUSHOU_MIESHI";; tqudao="-1";   }
	if("360".equals(tqudao)){     qudao="360SDK_MIESHI','360JIEKOU_MIESHI','360JIEKOU01_MIESHI"; tqudao="-1";   }
	
	if("UC用户".equals(tqudao)){  qudao="UC_MIESHI','UC01_MIESHI','UC02_MIESHI','UC03_MIESHI','UC04_MIESHI','UC05_MIESHI','UC06_MIESHI','UC07_MIESHI','UC08_MIESHI','UC09_MIESHI',"
	                  +"'UC10_MIESHI','UC11_MIESHI','UC12_MIESHI','UC13_MIESHI','UC14_MIESHI','UC15_MIESHI','UC16_MIESHI','UC17_MIESHI','UC18_MIESHI','UCNEWSDK_MIESHI','UCSDK_MIESHI"; tqudao="-1";   }
	
	 if("当乐".equals(tqudao)){    qudao="当乐"; tqudao="-1";   }
	
         

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
	realChannelList.add("注册");
	//realChannelList.add("注册人数(不分区)");
	realChannelList.add("有效注册人数");
	realChannelList.add("未激活账号(不分区)");
	realChannelList.add("日均登录用户");
	
	//realChannelList.add("独立进入(当天)");
	realChannelList.add("新增角色");
	realChannelList.add("平均在线");
	realChannelList.add("最高在线");
	realChannelList.add("平均在线时长(分钟)");
	
	realChannelList.add("付费账户");
	realChannelList.add("新付费人数");
	realChannelList.add("有效用户平均在线时长(分钟)");
	realChannelList.add("信息费(分)");
	
	realChannelList.add("注册ARPU(不分区)");
	realChannelList.add("ARPU值(分)");
	realChannelList.add("付费率 (千分之)");
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
				String key="";
				
				if("anzhi".equals(tqudao)){key=_day+tqudao+jixing;}
				if("91用户".equals(tqudao)){key=_day+tqudao+jixing;}
				if("360".equals(tqudao)){key=_day+tqudao+jixing;}
				if("UC用户".equals(tqudao)){key=_day+tqudao+jixing;}
				if("当乐".equals(tqudao)){key=_day+tqudao+jixing;}
				
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
				 	
				 	if(enterGameUserCount!=null&&enterGameUserCount!=0){//   付费渗透率=  付费人数/独立进入
			    Long shenTouLv=(chongZhiUserCount*1000)/enterGameUserCount;
				  stat_common[15]=shenTouLv.toString();
				  channel_regday_nums[14][k] =shenTouLv;//     付费渗透率=付费人数/独立进入人数
				  }else{stat_common[15]="0";}
				  
				  
				 Long enterGameUserCount_PAICHONG=playGameManager.getEnterGameUserCount_paichong(_day,_day,qudao,null,jixing);
				  if(enterGameUserCount_PAICHONG!=null){
				  stat_common[16]=enterGameUserCount_PAICHONG.toString();
				  channel_regday_nums[15][k] =enterGameUserCount_PAICHONG;//独立登陆（分区排重）
				}else{ stat_common[16]="0";}
				
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
       if(DateDiff(s11,s22)==6||DateDiff(s11,s22)>27){
		 $('form1').submit();
       }else{
        alert(s11+"和"+s22+",相差"+DateDiff(s11,s22)+"天,不是一周或者一个月,请确认日期");
        return;
       }
      }
   }  

</script>
	</head>
	<body>
		<center>
		<h2 style="font-weight:bold;">
			统计总表数据
		</h2>
		<form name="form1" id="form1" method="post">
		 <input type='hidden' name='flag' value='true'/>
		
		 开始日期： <input type="text" name="day" id="day" class="nqz_input" style="width:100px;" readonly="readonly" onfocus="new Calendar(false,'yyyy-MM-dd').showTimePicker(this);" value="<%=startDay %>"/>&nbsp;到
		  结束日期： <input type="text" name="endDay" id="endDay" class="nqz_input" style="width:100px;" readonly="readonly" onfocus="new Calendar(false,'yyyy-MM-dd').showTimePicker(this);" value="<%=endDay %>"/>
		
		       渠道：<select name="tqudao" style="width:150px" >
                     <option value="anzhi" 
                        <%  if("anzhi".equals(tqudao)){ out.print(" selected=\"selected\"");  } %>
                        >anzhi用户</option>
                     <option value="91用户" 
                        <%  if("91用户".equals(tqudao)){ out.print(" selected=\"selected\"");  } %>
                        >91用户</option>
                      <option value="360" 
                        <%  if("360".equals(tqudao)){ out.print(" selected=\"selected\"");     } %>
                        >360用户</option>
                      <option value="UC用户" 
                        <%  if("UC用户".equals(tqudao)){ out.print(" selected=\"selected\"");  } %>
                        >UC用户</option>
                      <option value="当乐" 
                        <%  if("当乐".equals(tqudao)){ out.print(" selected=\"selected\"");     } %>
                        >当乐用户</option>
                        
                        
                 </select> &nbsp;&nbsp;
		          
		    		<input type="button"  onclick="btnCount_Click()" value='提交'></form>
		    		
		    		<h3>统计总表数据</h3>
		    		<%
		    		out.println("<table id='test2' align='center' width='90%' cellpadding='1' bgcolor='#000000' cellspacing='1' border='0'>");
					out.print("<tr bgcolor='#EEEEBB'><td>日期</td>");
					long[] huizong=new long[realChannelList.size()];
		    		for(int c = 0 ; c < realChannelList.size() ; c++){
		    		 if(c==0||c==3||c==4||c==8||c==11||c==13||c==14){
		    			out.print("<td>"+realChannelList.get(c)+"</td>");
		    			}
		    		}
					out.println("</tr>");
					if ("true".equals(flag)) {//如果是刚打开页面，不查询
					for(int i = 0 ; i < dayList.size() ; i++){
						//out.print("<tr bgcolor='#FFFFFF'><td>"+dayList.get(i)+"</td>");
						for(int c = 0 ; c < realChannelList.size() ; c++){
						    //if(c==0||c==3||c==4||c==8||c==11||c==13||c==14){
			    			//out.print("<td>"+channel_regday_nums[c][i]+"</td>");
			    			//}
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
					
					out.print("<tr bgcolor='#EEEEBB'><td>结果</td>");
						for(int c = 0 ; c < realChannelList.size() ; c++){
						 if(c==0||c==3||c==4||c==8||c==11||c==13||c==14){
						 if(c!=3&&c!=14){
			    			out.print("<td>"+huizong[c]+"</td>");
			    			}else
			    			{
			    			out.print("<td>"+huizong[c]/dayList.size()+"</td>");
			    			}
			    			}
			    			
			    			
			    		}
						out.println("</tr>");
						}
					out.println("</table><br>");
		    		%>
		</center>
	</body>
</html>
