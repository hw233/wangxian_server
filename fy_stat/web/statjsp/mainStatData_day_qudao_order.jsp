<%@ page contentType="text/html;charset=UTF-8"%>
<%@include file="../inc.jsp"%>
<%@ page
	import="com.xuanzhi.tools.cache.diskcache.concrete.DefaultDiskCache,org.slf4j.*,java.io.*,java.util.*,com.xuanzhi.tools.text.*,
	org.springframework.context.ApplicationContext,org.springframework.web.context.support.WebApplicationContextUtils,
	com.sqage.stat.commonstat.dao.UserDao,com.sqage.stat.commonstat.entity.User,com.sqage.stat.commonstat.manager.UserManager,
	com.sqage.stat.commonstat.manager.Impl.*,com.sqage.stat.service.*,com.sqage.stat.model.Channel,com.sqage.stat.tools.ListStrSort"
	%>
  <%
    String flag = null;
    flag = request.getParameter("flag");
	String startDay = request.getParameter("day");
	String fenqu=request.getParameter("fenqu");
	String jixing = request.getParameter("jixing");
	
	long channel_regday_nums[][] = (long [][])request.getAttribute("channel_regday_nums");
	
	
	List<Channel> channelList = (ArrayList<Channel>)session.getAttribute("QUDAO_LIST");
	ArrayList<String []> fenQuList  =  (ArrayList<String []>) session.getAttribute("FENQU_LIST");
	
	if("0".equals(jixing)){jixing=null;}
	if("0".equals(fenqu)){fenqu=null;}
	
	String today = DateUtil.formatDate(new Date(),"yyyy-MM-dd");
	if(startDay == null){    // startDay = today;
	         startDay = DateUtil.formatDate(new Date(new Date().getTime()-24*60*60*1000),"yyyy-MM-dd");}
	         
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
	
	realChannelList.add("当天进入AEPU(不分区)");
	realChannelList.add("付费ARPU");
	realChannelList.add("付费渗透率 (千分之)");
	realChannelList.add("独立进入（分区排重）");

	long channel_regday_nums[][] = new long[realChannelList.size()][channelList.size()];
     if ("true".equals(flag)) {//如果是刚打开页面，不查询
       for(int k = 0 ; k < channelList.size() ; k++){
				Channel dqudao=channelList.get(k);
				String qudao=dqudao.getKey();
				String _day =startDay;
				List<String[]> stat_commonList=null;
				String key="";
				if("null".equals(qudao)){key=_day+"内侧用户"+fenqu+jixing;}
				else{
				key=_day+qudao+fenqu+jixing;
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
				
				  Long reg_LoginUserCount=playGameManager.getReg_LoginUserCount(_day,_day,qudao,fenqu,jixing);//当天注册，在分区道登陆的用户
                  if(reg_LoginUserCount!=null){
                  stat_common[5]=reg_LoginUserCount.toString();
				  channel_regday_nums[4][k] =reg_LoginUserCount;//独立登陆(当天)
				   }else{ stat_common[5]="0";}
				
				  Long youXiaoUserCount=userManager.getYouXiaoUerCount(_day,_day,qudao,fenqu,jixing);
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
				
				  Long enterGameUserCount=playGameManager.getEnterGameUserCount(_day,_day,qudao,fenqu,jixing);
				  if(enterGameUserCount!=null){
				  stat_common[4]=enterGameUserCount.toString();
				  channel_regday_nums[3][k] =enterGameUserCount;//独立登陆
				}else{ stat_common[4]="0";}
  
                
				  Long avgOnlineUserCount=onLineUsersCountManager.getAvgOnlineUserCount(_day,_day,qudao,fenqu,jixing);
				  if(avgOnlineUserCount!=null){
				  stat_common[6]=avgOnlineUserCount.toString();
				  channel_regday_nums[5][k] =avgOnlineUserCount;//平均在线
				  }else{stat_common[6]="0";}
				
				  Long maxOnlineUserCount=onLineUsersCountManager.getMaxOnlineUserCount(_day,_day,qudao,fenqu,jixing);
				  if(maxOnlineUserCount!=null){
				  stat_common[7]=maxOnlineUserCount.toString();
				  channel_regday_nums[6][k] =maxOnlineUserCount;//最高在线
				  }else{ stat_common[7]="0";}
				  
				 Long avgOnLineTime=playGameManager.getAvgOnLineTime(_day,_day,qudao,fenqu,jixing);
				 if(avgOnLineTime!=null){
				  Long fenzhong=avgOnLineTime/60000;
				  stat_common[8]=fenzhong.toString();
				  channel_regday_nums[7][k] =fenzhong;//平均在线
				}else{stat_common[8]="0";}
				
				 Long chongZhiUserCount=chongZhiManager.getChongZhiUserCount(_day,_day,qudao,fenqu,jixing);
				 if(chongZhiUserCount!=null){
				   stat_common[9]=chongZhiUserCount.toString();
				   channel_regday_nums[8][k] =chongZhiUserCount;//付费人数
				 }else{ stat_common[9]="0";}
				 Long unAheadChongZhiUserCount=chongZhiManager.getUnAheadChongZhiUserCount(_day,_day,qudao,fenqu,jixing);
				 if(unAheadChongZhiUserCount!=null){
				   stat_common[10]=unAheadChongZhiUserCount.toString();
				   channel_regday_nums[9][k] =unAheadChongZhiUserCount;//新付费人数
				  }else{stat_common[10]="0";}
				
				 Long youXiaoUerAVGOnLineTime=userManager.getYouXiaoUerAVGOnLineTime(_day,_day,qudao,fenqu,jixing);
				 //Long chongzhi_LoginUserCount=chongZhiManager.getChongZhi_LoginUserCount(_day,_day,qudao,fenqu,null);
				 if(youXiaoUerAVGOnLineTime!=null){
				 Long fenzheng= youXiaoUerAVGOnLineTime/60000;
				 stat_common[11]=fenzheng.toString();
				 channel_regday_nums[10][k] =fenzheng;//有效用户平均在线时长
				   }else{stat_common[11]="0";}
				   
				 Long chongZhiCount=chongZhiManager.getChongZhiCount(_day,_day,qudao,fenqu,jixing);//分区充值金额
				 if(chongZhiCount!=null){
				 stat_common[12]=chongZhiCount.toString();
				 channel_regday_nums[11][k] =chongZhiCount;//充值金额
				   }else{ stat_common[12]="0";}
				
				 Long chongZhiCount_all=chongZhiManager.getChongZhiCount(_day,_day,qudao,null,jixing);//全部充值金额，不分区
				if(reg_LoginUserCount!=null&&chongZhiCount_all!=null&&reg_LoginUserCount!=0){
				Long zhuceAEPU=chongZhiCount_all/reg_LoginUserCount;
				stat_common[13]=zhuceAEPU.toString();
				channel_regday_nums[12][k] =zhuceAEPU;//当天进入AEPU
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
				  
				   Long enterGameUserCount_PAICHONG=playGameManager.getEnterGameUserCount_paichong(_day,_day,qudao,fenqu,jixing);
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
	</head>
	<body>
		<center>
		<h2 style="font-weight:bold;">
			统计总表数据（渠道日报）
		</h2>
		<form   name="form1" id="form1" method="post">
		  <input type='hidden' name='flag' value='true'/>
		
		 开始日期： <input type="text" name="day" id="day" class="nqz_input" style="width:100px;" readonly="readonly" onfocus="new Calendar(false,'yyyy-MM-dd').showTimePicker(this);" value="<%=startDay %>"/>
		  &nbsp;&nbsp;&nbsp;&nbsp;
                
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
                </select>&nbsp;&nbsp;
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
                  </select> &nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;
		        
                
		    	<input type='submit' value='提交'></form>
		    		
		    		<h3>统计总表数据（渠道日报）</h3>
		    		<%
		    		out.println("<table id='test2' align='center' width='90%' cellpadding='1' bgcolor='#000000' cellspacing='1' border='0'>");
					out.print("<tr bgcolor='#EEEEBB'><td>日期</td>");
					
				
					long[] huizong=new long[realChannelList.size()];
		    		for(int c = 0 ; c < realChannelList.size() ; c++){
		    			out.print("<td>"+realChannelList.get(c)+"</td>");
		    		}
					out.println("</tr>");
					
					if ("true".equals(flag)) {  //如果是刚打开页面，不查询){
					
					for(int i = 0 ; i < channelList.size() ; i++){
						out.print("<tr bgcolor='#FFFFFF'><td>"+channelList.get(i).getKey()+"</td>");
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
					
					huizong[5]=huizong[5]/channelList.size();
					huizong[7]=huizong[7]/channelList.size();
					huizong[10]=huizong[10]/channelList.size();
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
		    		
		    
		</center>
		<br>
		<i>这里的用户都是指进入游戏的用户</i>
		<br>
	</body>
</html>
