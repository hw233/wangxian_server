<%@ page contentType="text/html;charset=UTF-8"%>
<%@include file="../inc.jsp"%>
<%@ page
	import="com.xuanzhi.tools.cache.diskcache.concrete.DefaultDiskCache,org.slf4j.*,java.io.*,java.util.*,
	com.xuanzhi.tools.text.*,org.springframework.context.ApplicationContext,
	org.springframework.web.context.support.WebApplicationContextUtils,com.sqage.stat.commonstat.dao.UserDao,
	com.sqage.stat.commonstat.entity.User,com.sqage.stat.commonstat.manager.UserManager,com.sqage.stat.commonstat.manager.Impl.*,
	com.sqage.stat.service.*,com.sqage.stat.model.Channel,java.math.BigDecimal,java.math.MathContext"
	%>
  <%
  	String flag = null;
  	flag = request.getParameter("flag");
  	String startDay = request.getParameter("day");
  	String endday = request.getParameter("endday");

  	String qudao = request.getParameter("qudao");
  	
  	String fenqugroup = request.getParameter("fenqugroup");
  	String jixing = request.getParameter("jixing");
  	List<Channel> channelList = (ArrayList<Channel>) session.getAttribute("QUDAO_LIST");
  	ArrayList<String[]> fenQuList = (ArrayList<String[]>) session.getAttribute("FENQU_LIST");

  	if ("0".equals(jixing)) {	jixing = null; 	}
  	if ("0".equals(qudao))  {    qudao = null;	}

  	String today = DateUtil.formatDate(new Date(), "yyyy-MM-dd");
  	if (startDay == null) { 	startDay = "0"; }
  	if (endday == null)   { 	endday = "0";  	}

  	UserManagerImpl userManager = UserManagerImpl.getInstance();
  	PlayGameManagerImpl playGameManager = PlayGameManagerImpl.getInstance();
  	OnLineUsersCountManagerImpl onLineUsersCountManager = OnLineUsersCountManagerImpl.getInstance();
  	ChongZhiManagerImpl chongZhiManager = ChongZhiManagerImpl.getInstance();

  	if (channelList == null || channelList.size() == 0) {
  		ChannelManager cmanager = ChannelManager.getInstance();
  		channelList = cmanager.getChannels();//渠道信息
  		session.removeAttribute("QUDAO_LIST");
  		session.setAttribute("QUDAO_LIST", channelList);
  	}
  	if (fenQuList == null) {
  		fenQuList = userManager.getFenQuByStatus("0");;//获得现有的分区信息
  		session.removeAttribute("FENQU_LIST");
  		session.setAttribute("FENQU_LIST", fenQuList);
  	}
  	
  	ArrayList<String[]> fenQuGroupList = userManager.getFenQu_Group(null);//带分组的分区信息
  	ArrayList<String[]> fenQuGroupls = userManager.getFenQuGroup(null);   ///分区的分组信息

  	//对比各个渠道数据

  	ArrayList<String> realChannelList = new ArrayList<String>();
  	realChannelList.add("注册人数(不分区)");
  	realChannelList.add("有效注册人数");
  	realChannelList.add("未激活账号(不分区)");
  	realChannelList.add("独立进入(分区不排重)");

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
  	realChannelList.add("付费渗透率");
  	realChannelList.add("独立进入(分区排重)");
  	
  	
  	//过滤对比各个分区数据

	ArrayList<String[]> realFenQuList = new ArrayList<String []>();
		if(request.getParameter("filterFenQu") != null)
          {
			String ss[] = request.getParameterValues("filterFenQu");
			for(int i = 0 ; i < ss.length  ;i++){
			
			for(int fenQuNum=0;fenQuNum<fenQuList.size();fenQuNum++){
			      String[] fenq= fenQuList.get(fenQuNum);
			      if(ss[i].trim().equals(fenq[1])){
					realFenQuList.add(fenq);
				}
			}
			}
		}
  	
     int daynum = Integer.parseInt(endday) - Integer.parseInt(startDay);
  	 long channel_regday_nums[][][] = new long[realChannelList.size()][realFenQuList.size()][daynum];
  	 
  	 String dauMaU_nums[][][] = new String[1][realFenQuList.size()][daynum];
  	 String fenQu_regday_nums[][][] = new String[3][realFenQuList.size()][daynum]; //各个分区开服后N天的1 3,7日留存
  	  
  	    int weekNum=1;
  	    if(daynum>6){ weekNum=(daynum+1)/7;}
  		String fenQu_weekLiuCun_nums[][][] = new String[1][realFenQuList.size()][weekNum]; //各个分区开服后N天周留存
  		
  		int monthNum=1;
  		if(daynum>29){monthNum=(daynum+1)/30;}
  		String fenQu_monthLiuCun_nums[][][] = new String[2][realFenQuList.size()][monthNum]; //各个分区开服后N天月留存
  				 
  	if ("true".equals(flag)) {//如果是刚打开页面，不查询
  	
  		for (int k = 0; k < realFenQuList.size(); k++) {
  			String[] dfenqu = realFenQuList.get(k);
  			String fenqu = dfenqu[1];
  			Date fuQustartDay = DateUtil.parseDate(dfenqu[2], "yyyy-MM-dd");
  			
  			for (int daycount =0; daycount < daynum; daycount++) {
  				String day = DateUtil.formatDate(new Date(fuQustartDay.getTime() + daycount*24*3600000+24*3600000L), "yyyy-MM-dd");
  				if(day.compareTo(today)>=0){ break;}
  				//FenQuday[k][daycount] = day;
  				
  				
  			String _day = day;
  			List<String[]> stat_commonList = null;
  			String key = "";
  			if ("null".equals(qudao)) {
  				key = _day + "内侧用户" + fenqu + jixing;
  			} else {
  				key = _day + qudao + fenqu + jixing;
  			}
  			if (today.compareTo(_day) > 0) {
  				stat_commonList = userManager.getstat_common(key);
  			}
  			String[] stat_common = new String[17];

  			if (stat_commonList != null && stat_commonList.size() > 0) {
  				stat_common = stat_commonList.get(0);
  				channel_regday_nums[0][k][daycount] = Long.parseLong(stat_common[1]);
  				channel_regday_nums[1][k][daycount] = Long.parseLong(stat_common[2]);
  				channel_regday_nums[2][k][daycount] = Long.parseLong(stat_common[3]);
  				channel_regday_nums[3][k][daycount] = Long.parseLong(stat_common[4]);

  				channel_regday_nums[4][k][daycount] = Long.parseLong(stat_common[5]);
  				channel_regday_nums[5][k][daycount] = Long.parseLong(stat_common[6]);
  				channel_regday_nums[6][k][daycount] = Long.parseLong(stat_common[7]);
  				channel_regday_nums[7][k][daycount] = Long.parseLong(stat_common[8]);

  				channel_regday_nums[8][k][daycount] = Long.parseLong(stat_common[9]);
  				channel_regday_nums[9][k][daycount] = Long.parseLong(stat_common[10]);
  				channel_regday_nums[10][k][daycount] = Long.parseLong(stat_common[11]);
  				channel_regday_nums[11][k][daycount] = Long.parseLong(stat_common[12]);

  				channel_regday_nums[12][k][daycount] = Long.parseLong(stat_common[13]);
  				channel_regday_nums[13][k][daycount] = Long.parseLong(stat_common[14]);
  				channel_regday_nums[14][k][daycount] = stat_common[15] == null ? 0L : Long.parseLong(stat_common[15]);
  				channel_regday_nums[15][k][daycount] = stat_common[16] == null ? 0L : Long.parseLong(stat_common[16]);

  			} else {
  				stat_common[0] = key;
  				Long registUserCount = userManager.getRegistUerCount(_day, _day, qudao, null, jixing);
  				if (registUserCount != null) {
  					stat_common[1] = registUserCount.toString();
  					channel_regday_nums[0][k][daycount] = registUserCount;//注册人数
  				} else {
  					stat_common[1] = "0";
  				}

  				Long reg_LoginUserCount = playGameManager.getReg_LoginUserCount(_day, _day, qudao, fenqu, jixing);//当天注册，在分区道登陆的用户
  				if (reg_LoginUserCount != null) {
  					stat_common[5] = reg_LoginUserCount.toString();
  					channel_regday_nums[4][k][daycount] = reg_LoginUserCount;//独立登陆(当天)
  				} else {
  					stat_common[5] = "0";
  				}

  				Long youXiaoUserCount = userManager.getYouXiaoUerCount(_day, _day, qudao, fenqu, jixing);
  				if (youXiaoUserCount != null) {
  					stat_common[2] = youXiaoUserCount.toString();
  					channel_regday_nums[1][k][daycount] = youXiaoUserCount;//有效注册人数
  				} else {
  					stat_common[2] = "0";
  				}

  				Long zhuCeNOCreatPlayer = 0L;
  				Long reg_LoginUserCount_all = playGameManager.getReg_LoginUserCount(_day, _day, qudao, null, jixing);//注册并登陆的用户，不区分区
  				if (registUserCount != null && reg_LoginUserCount_all != null) {
  					zhuCeNOCreatPlayer = registUserCount - reg_LoginUserCount_all;
  					stat_common[3] = zhuCeNOCreatPlayer.toString();
  					channel_regday_nums[2][k][daycount] = zhuCeNOCreatPlayer;//注册但未激活账号
  				} else {
  					stat_common[3] = "0";
  				}

  				Long enterGameUserCount = playGameManager.getEnterGameUserCount(_day, _day, qudao, fenqu, jixing);
  				if (enterGameUserCount != null) {
  					stat_common[4] = enterGameUserCount.toString();
  					channel_regday_nums[3][k][daycount] = enterGameUserCount;//独立登陆
  				} else {
  					stat_common[4] = "0";
  				}

  				Long avgOnlineUserCount = onLineUsersCountManager.getAvgOnlineUserCount(_day, _day, qudao, fenqu, jixing);
  				if (avgOnlineUserCount != null) {
  					stat_common[6] = avgOnlineUserCount.toString();
  					channel_regday_nums[5][k][daycount] = avgOnlineUserCount;//平均在线
  				} else {
  					stat_common[6] = "0";
  				}

  				Long maxOnlineUserCount = onLineUsersCountManager.getMaxOnlineUserCount(_day, _day, qudao, fenqu, jixing);
  				if (maxOnlineUserCount != null) {
  					stat_common[7] = maxOnlineUserCount.toString();
  					channel_regday_nums[6][k][daycount] = maxOnlineUserCount;//最高在线
  				} else {
  					stat_common[7] = "0";
  				}

  				Long avgOnLineTime = playGameManager.getAvgOnLineTime(_day, _day, qudao, fenqu, jixing);
  				if (avgOnLineTime != null) {
  					Long fenzhong = avgOnLineTime / 60000;
  					stat_common[8] = fenzhong.toString();
  					channel_regday_nums[7][k][daycount] = fenzhong;//平均在线
  				} else {
  					stat_common[8] = "0";
  				}

  				Long chongZhiUserCount = chongZhiManager.getChongZhiUserCount(_day, _day, qudao, fenqu, jixing);
  				if (chongZhiUserCount != null) {
  					stat_common[9] = chongZhiUserCount.toString();
  					channel_regday_nums[8][k][daycount] = chongZhiUserCount;//付费人数
  				} else {
  					stat_common[9] = "0";
  				}
  				Long unAheadChongZhiUserCount = chongZhiManager.getUnAheadChongZhiUserCount(_day, _day, qudao, fenqu, jixing);
  				if (unAheadChongZhiUserCount != null) {
  					stat_common[10] = unAheadChongZhiUserCount.toString();
  					channel_regday_nums[9][k][daycount] = unAheadChongZhiUserCount;//新付费人数
  				} else {
  					stat_common[10] = "0";
  				}

  				Long youXiaoUerAVGOnLineTime = userManager.getYouXiaoUerAVGOnLineTime(_day, _day, qudao, fenqu, jixing);
  				//Long chongzhi_LoginUserCount=chongZhiManager.getChongZhi_LoginUserCount(_day,_day,qudao,fenqu,null);
  				if (youXiaoUerAVGOnLineTime != null) {
  					Long fenzheng = youXiaoUerAVGOnLineTime / 60000;
  					stat_common[11] = fenzheng.toString();
  					channel_regday_nums[10][k][daycount] = fenzheng;//有效用户平均在线时长
  				} else {
  					stat_common[11] = "0";
  				}

  				Long chongZhiCount = chongZhiManager.getChongZhiCount(_day, _day, qudao, fenqu, jixing);//分区充值金额
  				if (chongZhiCount != null) {
  					stat_common[12] = chongZhiCount.toString();
  					channel_regday_nums[11][k][daycount] = chongZhiCount;//充值金额
  				} else {
  					stat_common[12] = "0";
  				}

  				Long chongZhiCount_all = chongZhiManager.getChongZhiCount(_day, _day, qudao, null, jixing);//全部充值金额，不分区
  				if (reg_LoginUserCount != null && chongZhiCount_all != null && reg_LoginUserCount != 0) {
  					Long zhuceAEPU = chongZhiCount_all / reg_LoginUserCount;
  					stat_common[13] = zhuceAEPU.toString();
  					channel_regday_nums[12][k][daycount] = zhuceAEPU;//当天进入AEPU
  				} else {
  					stat_common[13] = "0";
  				}

  				if (chongZhiCount != null && chongZhiUserCount != null && chongZhiUserCount != 0) {
  					Long fufeiAEPU = chongZhiCount / chongZhiUserCount;
  					stat_common[14] = fufeiAEPU.toString();
  					channel_regday_nums[13][k][daycount] = fufeiAEPU;//付费AEPU
  				} else {
  					stat_common[14] = "0";
  				}
  				
  					if(enterGameUserCount!=null&&enterGameUserCount!=0){//   付费渗透率=  付费人数/独立进入
			    Long shenTouLv=(chongZhiUserCount*1000)/enterGameUserCount;
				  stat_common[15]=shenTouLv.toString();
				  channel_regday_nums[14][k][daycount] =shenTouLv;//     付费渗透率=付费人数/独立进入人数
				  }else{stat_common[15]="0";}
				  
				  
				 Long enterGameUserCount_PAICHONG=playGameManager.getEnterGameUserCount_paichong(_day,_day,qudao,fenqu,jixing);
				  if(enterGameUserCount_PAICHONG!=null){
				  stat_common[16]=enterGameUserCount_PAICHONG.toString();
				  channel_regday_nums[15][k][daycount] =enterGameUserCount_PAICHONG;//独立登陆（分区排重）
				}else{ stat_common[16]="0";}
  				 
  				if (today.compareTo(_day) > 0) {
  					userManager.addstat_common(_day, stat_common);
  				}
  			}
  		}
  	}
  	 	
  
  	
  	/////////////////////////////1 3 7 留存 start /////////////////////////
  	
  
     for (int k = 0; k < realFenQuList.size(); k++) {
  			String[] dfenqu = realFenQuList.get(k);
  			String fenqu = dfenqu[1];
  			Date fuQustartDay = DateUtil.parseDate(dfenqu[2], "yyyy-MM-dd");
  			
  			for (int daycount =0; daycount < daynum; daycount++) {
  			    Date day_datenew=new Date(fuQustartDay.getTime() + daycount*24*3600000+24*3600000L);//开服第daycount天的日期.
  				String day = DateUtil.formatDate(day_datenew, "yyyy-MM-dd");
  				ArrayList<String> dayUserCounts = playGameManager.getSepFenQuRetainUserCount(day,day,fenqu,qudao,jixing);//开服第daycount天当天的留存数
  				if(day.compareTo(today)>=0){ break;}//如果超过了当天,则退出
  				
  				
  				
  				//一日留存计算 start
  				Date oneDay_date= new Date(day_datenew.getTime()+24*3600000L);  //开服第daycount天的1日留存的日期.
  				String  oneDay = DateUtil.formatDate(oneDay_date, "yyyy-MM-dd");
  				if(oneDay.compareTo(today)>=0){ break;}//如果超过了当天,则退出
  				ArrayList<String> oneDayUserCounts = playGameManager.getSepFenQuRetainUserCount(day,oneDay,fenqu,qudao,jixing);//开服第daycount天的1日的留存数
  				float s1=0f;
  				if(dayUserCounts!=null&&!"0".equals(dayUserCounts.get(0))){
  				 s1=Float.parseFloat(oneDayUserCounts.get(0)) / Float.parseFloat(dayUserCounts.get(0));
  				   }
  				fenQu_regday_nums[0][k][daycount]= String.valueOf(s1);
  				//一日留存计算 end
  				//三日留存计算 start
  				Date sanDay_date= new Date(day_datenew.getTime()+3*24*3600000);  //开服第daycount天的3日留存的日期.
  				String  sanDay = DateUtil.formatDate(sanDay_date, "yyyy-MM-dd");
  				if(sanDay.compareTo(today)>=0){ break;}//如果超过了当天,则退出
  				ArrayList<String> sanDayUserCounts = playGameManager.getSepFenQuRetainUserCount(day,sanDay,fenqu,qudao,jixing);//开服第daycount天的1日的留存数
  				float s3=0f;
  				if(dayUserCounts!=null&&!"0".equals(dayUserCounts.get(0))){
  				 s3=Float.parseFloat(oneDayUserCounts.get(0)) / Float.parseFloat(dayUserCounts.get(0));
  				   }
  				fenQu_regday_nums[1][k][daycount]= String.valueOf(s3);
  				//三日留存计算end
  				
  				//七日留存计算 start
  				Date qiDay_date= new Date(day_datenew.getTime()+7*24*3600000);  //开服第daycount天的1日留存的日期.
  				String  qiDay = DateUtil.formatDate(qiDay_date, "yyyy-MM-dd");
  				if(qiDay.compareTo(today)>=0){ break;}//如果超过了当天,则退出
  				ArrayList<String> qiDayUserCounts = playGameManager.getSepFenQuRetainUserCount(day,qiDay,fenqu,qudao,jixing);//开服第daycount天的1日的留存数
  				float s7=0f;
  				if(dayUserCounts!=null&&!"0".equals(dayUserCounts.get(0))){
  				 s7=Float.parseFloat(oneDayUserCounts.get(0)) / Float.parseFloat(dayUserCounts.get(0));
  				   }
  				fenQu_regday_nums[2][k][daycount]= String.valueOf(s7);
  				
  			//七日留存计算 end
  		}
  	}

  	
  	/////////////////////////////1 3 7 留存 end /////////////////////////
  	

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
         function changeDisplay(fenqugroup)
          {
            var zhuan91=document.getElementById("91专服");
            var uc=document.getElementById("UC专服");
            var appstor=document.getElementById("APPSTORE");
            var hunfu=document.getElementById("混服");
            var disp91=document.getElementById("91不可见");
            var disp91uc=document.getElementById("91UC不可见");
            var disDangleUc=document.getElementById("当乐UC不可见");
                 
              if(fenqugroup==1){ zhuan91.style.display='block'; }else{zhuan91.style.display='none'; }
              if(fenqugroup==2){ uc.style.display='block';      }else{uc.style.display='none'; }
              if(fenqugroup==3){ appstor.style.display='block';  }else{appstor.style.display='none'; }
              if(fenqugroup==4){ hunfu.style.display='block';    }else{hunfu.style.display='none'; }
              if(fenqugroup==5){ disp91.style.display='block';   }else{disp91.style.display='none'; }
              if(fenqugroup==6){ disp91uc.style.display='block';   }else{disp91uc.style.display='none'; }
              if(fenqugroup==7){ disDangleUc.style.display='block';}else{disDangleUc.style.display='none'; }
                   }
           </script>
       
       
<script language="JavaScript">
var mycars=[<%for (int i = 0; i < daynum; i++) {
				out.print("\"" + i+"天" + "\",");
			}%>"E"];
       function drawRegUserFlotr(){
		    var f = Flotr.draw(
		$('regUserContainer'), [
		<%for (int j = 0; j <  realFenQuList.size(); j++) {
				StringBuffer sb2 = new StringBuffer();
				sb2.append("[");
				for (int kk = 0; kk < channel_regday_nums[3][j].length; kk++) {
					sb2.append("[" + kk + "," + channel_regday_nums[3][j][kk] + "]");
					if (kk < channel_regday_nums[3][j].length - 1) sb2.append(",");
				}
				sb2.append("]");
				out.println("{");
				out.println("data:" + sb2.toString() + ",");
				out.println("label:'" + realFenQuList.get(j)[1] + "'");
				out.println("}");
				if (j < realFenQuList.size() - 1) out.print(",");
			}%>
		],{
			xaxis:{
				noTicks: <%=daynum%>,	// Display 7 ticks.	
				tickFormatter: function(n){ return mycars[Math.floor(n)];}, // =>
				min: 0,
				max: <%=daynum%>
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


<script language="JavaScript">
var mycars=[<%for (int i = 0; i < daynum; i++) {
				out.print("\"" + i+"天" + "\",");
			}%>"E"];
       function drawpayRateFlotr(){
		    var f = Flotr.draw(
		$('payRateContainer'), [
		<%for (int j = 0; j <  realFenQuList.size(); j++) {
				StringBuffer sb2 = new StringBuffer();
				sb2.append("[");
				for (int kk = 0; kk < channel_regday_nums[8][j].length; kk++) {
					sb2.append("[" + kk + "," + 
					
					(channel_regday_nums[8][j][kk]==0 ? 0:(float)channel_regday_nums[8][j][kk]/(float)channel_regday_nums[3][j][kk] )
					
					+ "]");
					if (kk < channel_regday_nums[8][j].length - 1) sb2.append(",");
				}
				sb2.append("]");
				out.println("{");
				out.println("data:" + sb2.toString() + ",");
				out.println("label:'" + realFenQuList.get(j)[1] + "'");
				out.println("}");
				if (j < realFenQuList.size() - 1) out.print(",");
			}%>
		],{
			xaxis:{
				noTicks: <%=daynum%>,	// Display 7 ticks.	
				tickFormatter: function(n){ return mycars[Math.floor(n)];}, // =>
				min: 0,
				max: <%=daynum%>
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

<script language="JavaScript">
var mycars=[<%for (int i = 0; i < daynum; i++) {
				out.print("\"" + i+"天" + "\",");
			}%>"E"];
       function drawpayACUFlotr(){
		    var f = Flotr.draw(
		$('payACUContainer'), [
		<%for (int j = 0; j <  realFenQuList.size(); j++) {
				StringBuffer sb2 = new StringBuffer();
				sb2.append("[");
				for (int kk = 0; kk < channel_regday_nums[8][j].length; kk++) {
					sb2.append("[" + kk + "," + 
					
					(channel_regday_nums[11][j][kk]==0 ? 0:(float)channel_regday_nums[11][j][kk]/(float)channel_regday_nums[3][j][kk] )
					
					+ "]");
					if (kk < channel_regday_nums[11][j].length - 1) sb2.append(",");
				}
				sb2.append("]");
				out.println("{");
				out.println("data:" + sb2.toString() + ",");
				out.println("label:'" + realFenQuList.get(j)[1] + "'");
				out.println("}");
				if (j < realFenQuList.size() - 1) out.print(",");
			}%>
		],{
			xaxis:{
				noTicks: <%=daynum%>,	// Display 7 ticks.	
				tickFormatter: function(n){ return mycars[Math.floor(n)];}, // =>
				min: 0,
				max: <%=daynum%>
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

<script language="JavaScript">
var mycars=[<%for (int i = 0; i < daynum; i++) {
				out.print("\"" + i+"天" + "\",");
			}%>"E"];
       function drawpayARPUFlotr(){
		    var f = Flotr.draw(
		$('payARPUContainer'), [
		<%for (int j = 0; j <  realFenQuList.size(); j++) {
				StringBuffer sb2 = new StringBuffer();
				sb2.append("[");
				for (int kk = 0; kk < channel_regday_nums[13][j].length; kk++) {
					sb2.append("[" + kk + "," + channel_regday_nums[13][j][kk] + "]");
					if (kk < channel_regday_nums[13][j].length - 1) sb2.append(",");
				}
				sb2.append("]");
				out.println("{");
				out.println("data:" + sb2.toString() + ",");
				out.println("label:'" + realFenQuList.get(j)[1] + "'");
				out.println("}");
				if (j < realFenQuList.size() - 1) out.print(",");
			}%>
		],{
			xaxis:{
				noTicks: <%=daynum%>,	// Display 7 ticks.	
				tickFormatter: function(n){ return mycars[Math.floor(n)];}, // =>
				min: 0,
				max: <%=daynum%>
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


<script language="JavaScript">
var mycars=[<%for (int i = 0; i < daynum; i++) {
				out.print("\"" + i+"天" + "\",");
			}%>"E"];
       function drawAVGOnLineTimeFlotr(){
		    var f = Flotr.draw(
		$('AVGOnLineTimeContainer'), [
		<%for (int j = 0; j <  realFenQuList.size(); j++) {
				StringBuffer sb2 = new StringBuffer();
				sb2.append("[");
				for (int kk = 0; kk < channel_regday_nums[7][j].length; kk++) {
					sb2.append("[" + kk + "," + channel_regday_nums[7][j][kk] + "]");
					if (kk < channel_regday_nums[7][j].length - 1) sb2.append(",");
				}
				sb2.append("]");
				out.println("{");
				out.println("data:" + sb2.toString() + ",");
				out.println("label:'" + realFenQuList.get(j)[1] + "'");
				out.println("}");
				if (j < realFenQuList.size() - 1) out.print(",");
			}%>
		],{
			xaxis:{
				noTicks: <%=daynum%>,	// Display 7 ticks.	
				tickFormatter: function(n){ return mycars[Math.floor(n)];}, // =>
				min: 0,
				max: <%=daynum%>
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


<script language="JavaScript">
var mycars=[<%for (int i = 0; i < daynum; i++) {
				out.print("\"" + i+"天" + "\",");
			}%>"E"];
       function drawAVGOnLineUserFlotr(){
		    var f = Flotr.draw(
		$('AVGOnLineUserContainer'), [
		<%for (int j = 0; j <  realFenQuList.size(); j++) {
				StringBuffer sb2 = new StringBuffer();
				sb2.append("[");
				for (int kk = 0; kk < channel_regday_nums[5][j].length; kk++) {
					sb2.append("[" + kk + "," + channel_regday_nums[5][j][kk] + "]");
					if (kk < channel_regday_nums[5][j].length - 1) sb2.append(",");
				}
				sb2.append("]");
				out.println("{");
				out.println("data:" + sb2.toString() + ",");
				out.println("label:'" + realFenQuList.get(j)[1] + "'");
				out.println("}");
				if (j < realFenQuList.size() - 1) out.print(",");
			}%>
		],{
			xaxis:{
				noTicks: <%=daynum%>,	// Display 7 ticks.	
				tickFormatter: function(n){ return mycars[Math.floor(n)];}, // =>
				min: 0,
				max: <%=daynum%>
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

<script language="JavaScript">
var mycars=[<%for (int i = 0; i < daynum; i++) {
				out.print("\"" + i+"天" + "\",");
			}%>"E"];
       function drawMAXOnLineUserFlotr(){
		    var f = Flotr.draw(
		$('MAXOnLineUserContainer'), [
		<%for (int j = 0; j <  realFenQuList.size(); j++) {
				StringBuffer sb2 = new StringBuffer();
				sb2.append("[");
				for (int kk = 0; kk < channel_regday_nums[6][j].length; kk++) {
					sb2.append("[" + kk + "," + channel_regday_nums[6][j][kk] + "]");
					if (kk < channel_regday_nums[6][j].length - 1) sb2.append(",");
				}
				sb2.append("]");
				out.println("{");
				out.println("data:" + sb2.toString() + ",");
				out.println("label:'" + realFenQuList.get(j)[1] + "'");
				out.println("}");
				if (j < realFenQuList.size() - 1) out.print(",");
			}%>
		],{
			xaxis:{
				noTicks: <%=daynum%>,	// Display 7 ticks.	
				tickFormatter: function(n){ return mycars[Math.floor(n)];}, // =>
				min: 0,
				max: <%=daynum%>
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


<script language="JavaScript">
var mycars=[<%for (int i = 0; i < daynum; i++) {
				out.print("\"" + i+"天" + "\",");
			}%>"E"];
       function drawpayMoneyFlotr(){
		    var f = Flotr.draw(
		$('payMoneyContainer'), [
		<%for (int j = 0; j <  realFenQuList.size(); j++) {
				StringBuffer sb2 = new StringBuffer();
				sb2.append("[");
				for (int kk = 0; kk < channel_regday_nums[11][j].length; kk++) {
					sb2.append("[" + kk + "," + channel_regday_nums[11][j][kk] + "]");
					if (kk < channel_regday_nums[11][j].length - 1) sb2.append(",");
				}
				sb2.append("]");
				out.println("{");
				out.println("data:" + sb2.toString() + ",");
				out.println("label:'" + realFenQuList.get(j)[1] + "'");
				out.println("}");
				if (j < realFenQuList.size() - 1) out.print(",");
			}%>
		],{
			xaxis:{
				noTicks: <%=daynum%>,	// Display 7 ticks.	
				tickFormatter: function(n){ return mycars[Math.floor(n)];}, // =>
				min: 0,
				max: <%=daynum%>
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

      <!-- 1 3 7 留存 start、 -->
      
      <script language="JavaScript">
         var mycars=[<%for (int i = 0; i < daynum; i++) {
				out.print("\"" + i+"天" + "\",");
			}%>"E"];
       function drawoneDayRetainUserFlotr(){
		    var f = Flotr.draw(
		$('oneDayRetainUserContainer'), [
		<%for (int j = 0; j <  realFenQuList.size(); j++) {
				StringBuffer sb2 = new StringBuffer();
				sb2.append("[");
				for (int kk = 0; kk < fenQu_regday_nums[0][j].length; kk++) {
					sb2.append("[" + kk + "," + fenQu_regday_nums[0][j][kk] + "]");
					if (kk < fenQu_regday_nums[0][j].length - 1) sb2.append(",");
				}
				sb2.append("]");
				out.println("{");
				out.println("data:" + sb2.toString() + ",");
				out.println("label:'" + realFenQuList.get(j)[1] + "'");
				out.println("}");
				if (j < realFenQuList.size() - 1) out.print(",");
			}%>
		],{
			xaxis:{
				noTicks: <%=daynum%>,	// Display 7 ticks.	
				tickFormatter: function(n){ return mycars[Math.floor(n)];}, // =>
				min: 0,
				max: <%=daynum%>
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
 <script language="JavaScript">
         var mycars=[<%for (int i = 0; i < daynum; i++) {
				out.print("\"" + i+"天" + "\",");
			}%>"E"];
       function drawthreeDayRetainUserFlotr(){
		    var f = Flotr.draw(
		$('threeDayRetainUserContainer'), [
		<%for (int j = 0; j <  realFenQuList.size(); j++) {
				StringBuffer sb2 = new StringBuffer();
				sb2.append("[");
				for (int kk = 0; kk < fenQu_regday_nums[1][j].length; kk++) {
					sb2.append("[" + kk + "," + fenQu_regday_nums[1][j][kk] + "]");
					if (kk < fenQu_regday_nums[1][j].length - 1) sb2.append(",");
				}
				sb2.append("]");
				out.println("{");
				out.println("data:" + sb2.toString() + ",");
				out.println("label:'" + realFenQuList.get(j)[1] + "'");
				out.println("}");
				if (j < realFenQuList.size() - 1) out.print(",");
			}%>
		],{
			xaxis:{
				noTicks: <%=daynum%>,	// Display 7 ticks.	
				tickFormatter: function(n){ return mycars[Math.floor(n)];}, // =>
				min: 0,
				max: <%=daynum%>
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
      
      
  <script language="JavaScript">
         var mycars=[<%for (int i = 0; i < daynum; i++) {
				out.print("\"" + i+"天" + "\",");
			}%>"E"];
       function drawsevenDayRetainUserFlotr(){
		    var f = Flotr.draw(
		$('sevenDayRetainUserContainer'), [
		<%for (int j = 0; j <  realFenQuList.size(); j++) {
				StringBuffer sb2 = new StringBuffer();
				sb2.append("[");
				for (int kk = 0; kk < fenQu_regday_nums[2][j].length; kk++) {
					sb2.append("[" + kk + "," + fenQu_regday_nums[2][j][kk] + "]");
					if (kk < fenQu_regday_nums[2][j].length - 1) sb2.append(",");
				}
				sb2.append("]");
				out.println("{");
				out.println("data:" + sb2.toString() + ",");
				out.println("label:'" + realFenQuList.get(j)[1] + "'");
				out.println("}");
				if (j < realFenQuList.size() - 1) out.print(",");
			}%>
		],{
			xaxis:{
				noTicks: <%=daynum%>,	// Display 7 ticks.	
				tickFormatter: function(n){ return mycars[Math.floor(n)];}, // =>
				min: 0,
				max: <%=daynum%>
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

      <!-- 1 3 7 留存end -->


<script language="JavaScript">
var mycarsdauMau=[<%for (int i = 0; i < daynum; i++) {
				out.print("\"" + i+"天" + "\",");
			}%>"E"];
       function drawdauMauFlotr(){
		    var f = Flotr.draw(
		$('dauMauContainer'), [
		<%for (int j = 0; j <  realFenQuList.size(); j++) {
				StringBuffer sb2 = new StringBuffer();
				sb2.append("[");
				for (int kk = 0; kk < dauMaU_nums[0][j].length; kk++) {
					sb2.append("[" + kk + "," + dauMaU_nums[0][j][kk] + "]");
					if (kk < dauMaU_nums[0][j].length - 1) sb2.append(",");
				}
				sb2.append("]");
				out.println("{");
				out.println("data:" + sb2.toString() + ",");
				out.println("label:'" + realFenQuList.get(j)[1] + "'");
				out.println("}");
				if (j < realFenQuList.size() - 1) out.print(",");
			}%>
		],{
			xaxis:{
				noTicks: <%=daynum%>,	// Display 7 ticks.	
				tickFormatter: function(n){ return mycars[Math.floor(n)];}, // =>
				min: 0,
				max: <%=daynum%>
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
		
		  离开服起始天数：<select name="day">
		  <%
		           for(int days=1;days<33;days++){
		           String subhtm="";
		            if (new Integer(days).toString().equals(startDay)) { subhtm= " selected=\"selected\"";} 
		           String htm="<option value="+days +subhtm+" >"+days+"</option>";
		           out.println(htm);
		           }
                  %>      
                
                </select> &nbsp;&nbsp;
                
                       离开服结束天数：<select name="endday">
                  <%
		           for(int days=1;days<63;days++){
		           String subhtm="";
		            if (new Integer(days).toString().equals(endday)) { subhtm= " selected=\"selected\"";} 
		           String htm="<option value="+days +subhtm+" >"+days+"</option>";
		           out.println(htm);
		           }
                  %>   
       
                </select> &nbsp;&nbsp;
		       渠道：<select name="qudao">
                       <option  value="0">全部</option>
                       <%
                       	for (int i = 0; i < channelList.size(); i++) {
                       		Channel _channel = channelList.get(i);
                       %>
                        <option value="<%=_channel.getKey()%>" 
                        <%if (_channel.getKey().equals(qudao)) {
					out.print(" selected=\"selected\"");
				}%>
                          ><%=_channel.getName()%></option>
                       <%
                       	}
                       %>
                </select> &nbsp;&nbsp;
                
                
                 分区类型<select name="fenqugroup" onchange="javascript:changeDisplay(this.options[this.options.selectedIndex].value);">
                       <option  value="4">混服</option>
                       <%
                       	for (int i = 0; i < fenQuGroupls.size(); i++) {
                       		String[]  fenQuG = fenQuGroupls.get(i);
                       %>
                        <option value="<%=fenQuG[0]%>" 
                        <%if (fenQuG[0].equals(fenqugroup)) {
					  out.print(" selected=\"selected\"");
				           }%>
                          ><%=fenQuG[1]%></option>
                       <%
                       	}
                       %>
                </select> &nbsp;&nbsp;
                
                                   平台：<select name="jixing">
                       <option  value="0">全部</option>
                        <option value="Android" 
                        <%if ("Android".equals(jixing)) {
				out.print(" selected=\"selected\"");
			}%>>Android</option>
                          
                       <option value="IOS" 
                        <%if ("IOS".equals(jixing)) {
				out.print(" selected=\"selected\"");
			}%>>IOS</option>
                  </select>
		        <br/>
		        
		          过滤分区：
		          <% for(int fgroupNUm=0;fgroupNUm<fenQuGroupls.size();fgroupNUm++){ 
		         String [] fgroups= fenQuGroupls.get(fgroupNUm);
		         int count=0;
		          %>
		          
		    <table width="1000" id="<%=fgroups[1] %>" 
		    <% 
		    if(fenqugroup==null){fenqugroup="4";}
		    if(fenqugroup.equals(fgroups[0])){
		             out.print("style=\"display:block;\"");
		     }else{  out.print("style=\"display:none;\"");}
		    
		    %>
		        
		        >
		        <tr>
		    		<%
		    		for(int i = 0 ; i < fenQuGroupList.size() ; i++){
		    		 String[] fenqus = fenQuGroupList.get(i);
		    		
		    		  String channel= fenqus[3];
		    		  if(fenqus[0].equals(fgroups[0])){
		    		  
		    		  if(count>0&&count%9==0){out.print("</tr><tr>");}
		    		  count++;
		    		  boolean contains=false;
		    		  for(int fenQujj=0;fenQujj<realFenQuList.size();fenQujj++){
		    		  String[] realchanel=realFenQuList.get(fenQujj);
		    			if(realchanel[1].contains(channel)){
		    				contains=true;
		    			}
		    			}
		    			if(contains){
		    				out.print("<td align='left'><input type='checkbox' name='filterFenQu' value='"+channel+"' checked/>"+channel+"&nbsp;</td>");
		    			}else{
		    				out.print("<td align='left'><input type='checkbox' name='filterFenQu' value='"+channel+"'/>"+channel+"&nbsp;</td>");
		    			}
		    			
		    			}
		    		}
		    		%>
		    		</tr>
		    		</table><br><br>
                <% }%>
		    	<input type='submit' value='提交'></form>
		    		<%
		    		out.println("<table id='test2' align='center' width='90%' cellpadding='1' bgcolor='#000000' cellspacing='1' border='0'>");
					out.print("<tr bgcolor='#EEEEBB'><td>日期</td>");
		    		
		    		
		    		
		    		
		    		
		    		
		    		
		    		
		    		
		    		out.println("</table><br>");
		    		 %>
		    		 
		    	  <h3>DAU(日活跃用户数)</h3>
		    		<div id="regUserContainer" style="width:100%;height:400px;display:block;"></div>
		            <script>
		            drawRegUserFlotr();
		            </script>
		   
		          <h3>日付费率  ：付费人数/DAU</h3>
		    		<div id="payRateContainer" style="width:100%;height:400px;display:block;"></div>
		            <script>
		            drawpayRateFlotr();
		            </script>
		            
		          <h3>ACU（活跃用户的ARPU） ：付费总额/DAU</h3>
		    		<div id="payACUContainer" style="width:100%;height:400px;display:block;"></div>
		            <script>
		            drawpayACUFlotr();
		            </script>
		            
		           <h3>ARPU（付费用户的ARPU） ：付费总额/付费用户数</h3>
		    		<div id="payARPUContainer" style="width:100%;height:400px;display:block;"></div>
		            <script>
		            drawpayARPUFlotr();
		            </script>
		            
		           <h3>平均在线时长（单位：分钟）</h3>
		    		<div id="AVGOnLineTimeContainer" style="width:100%;height:400px;display:block;"></div>
		            <script>
		            drawAVGOnLineTimeFlotr();
		            </script> 
		            
		           <h3>ACU：平均在线人数</h3>
		    		<div id="AVGOnLineUserContainer" style="width:100%;height:400px;display:block;"></div>
		            <script>
		            drawAVGOnLineUserFlotr();
		            </script> 
		            
		           <h3>PCU：最高在线人数</h3>
		    		<div id="MAXOnLineUserContainer" style="width:100%;height:400px;display:block;"></div>
		            <script>
		            drawMAXOnLineUserFlotr();
		            </script> 
		            
		             <h3>收入 (单位：分)</h3>
		    		<div id="payMoneyContainer" style="width:100%;height:400px;display:block;"></div>
		            <script>
		            drawpayMoneyFlotr();
		            </script> 
		            
		           <h3>一日留存</h3>
		    		<div id="oneDayRetainUserContainer" style="width:100%;height:400px;display:block;"></div>
		            <script>
		            drawoneDayRetainUserFlotr();
		            </script> 
		            
		           <h3>三日留存</h3>
		    		<div id="threeDayRetainUserContainer" style="width:100%;height:400px;display:block;"></div>
		            <script>
		            drawthreeDayRetainUserFlotr();
		            </script> 
		            
		           <h3>七日留存</h3>
		    		<div id="sevenDayRetainUserContainer" style="width:100%;height:400px;display:block;"></div>
		            <script>
		            drawsevenDayRetainUserFlotr();
		            </script> 
		            
		           <h3>DAU/MAU</h3>
		    		<div id="dauMauContainer" style="width:100%;height:400px;display:block;"></div>
		            <script>
		            drawdauMauFlotr();
		            </script> 
		    		
		</center>
		<br>
		<i>这里的用户都是指进入游戏的用户</i>
		<br>
	</body>
</html>
