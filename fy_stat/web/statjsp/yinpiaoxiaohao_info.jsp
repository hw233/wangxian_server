<%@ page contentType="text/html;charset=UTF-8"%>
<%@include file="../inc.jsp"%>
<%@ page
	import="com.xuanzhi.tools.cache.diskcache.concrete.DefaultDiskCache,org.slf4j.*,java.io.*,java.util.*,com.xuanzhi.tools.text.*,
	org.springframework.context.ApplicationContext,org.springframework.web.context.support.WebApplicationContextUtils,
	com.sqage.stat.commonstat.dao.UserDao,com.sqage.stat.commonstat.entity.*,com.sqage.stat.commonstat.manager.*,
	com.sqage.stat.commonstat.manager.Impl.UserManagerImpl,com.sqage.stat.commonstat.manager.Impl.*,com.sqage.stat.service.*,com.sqage.stat.model.Channel"
	%>
	<%!
	Object lock = new Object(){};
      %>
     <%
              String flag = null;
              flag = request.getParameter("flag");
    
	          String startDay = request.getParameter("day");
	          String endDay = request.getParameter("endDay");
	          String type=request.getParameter("type");
	          String fenqu=request.getParameter("fenqu");
	          String displayType=request.getParameter("displaytype");
	
	          if("0".equals(fenqu)){fenqu=null;}
	
	          String today = DateUtil.formatDate(new Date(new Date().getTime()-24*60*60*1000),"yyyy-MM-dd");
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
              
              ArrayList<String []> fenQuList= userManager.getFenQuByStatus("0");//获得现有的分区信息
	          Transaction_SpecialManagerImpl transaction_SpecialManager = Transaction_SpecialManagerImpl.getInstance();
	       
	           String subSql="";
	           if(fenqu!=null){
	           subSql+=" and  t.fenqu ='"+fenqu+"'";
	           }
	            List<Transaction_yinzi> ls_SuiHouLiuTong=null;
	            List<Transaction_yinzi> ls_YinDingChanChu=null;
	            List<Transaction_yinzi> ls_YinDingChongZhi=null;
	            List<Transaction_yinzi> ls_YinDingXiaoHao=null;
	            List<Transaction_yinzi> ls_Sui1=null;
	            List<Transaction_yinzi> ls_Sui2=null;
	            
	            
	           if("1".equals(displayType)){
	           String sql_SuiHouLiuTong="select t.createdate,'dd' fenqu,'税后流通' REASONTYPE,'0' ACTION,sum(t.money) money from stat_gamechongzhi_yinpiao t "
	                            +" where  t.reasontype in ('摆摊在摆摊出售了东西','交易获取从他人那交易来','求购求购出售物品','邮件获取给卖家发钱') "
	                            +" and  t.createdate between '"+startDay+"' and '"+endDay+"' "+subSql+" group by t.createdate order by t.createdate";
	                         ls_SuiHouLiuTong=transaction_SpecialManager.getTransaction_yinziBySql(sql_SuiHouLiuTong);
	       
	       
	       
	                       String sql_Sui1="select t.createdate,'dd' fenqu,'税后流通' REASONTYPE,'0' ACTION,sum(t.money) money from stat_gamechongzhi_yinpiao t "
	                            +" where  t.reasontype in ('摆摊在摆摊出售了东西','交易获取从他人那交易来') "
	                            +" and  t.createdate between '"+startDay+"' and '"+endDay+"' "+subSql+" group by t.createdate order by t.createdate";
	                        // ls_SuiHouLiuTong=transaction_SpecialManager.getTransaction_yinziBySql(sql_SuiHouLiuTong);
	                         
	                       String sql_Sui2="select t.createdate,'dd' fenqu,'税后流通' REASONTYPE,'0' ACTION,sum(t.money) money from stat_gamechongzhi_yinpiao t "
	                            +" where  t.reasontype in ('求购求购出售物品') "
	                            +" and  t.createdate between '"+startDay+"' and '"+endDay+"' "+subSql+" group by t.createdate order by t.createdate";
	                        // ls_SuiHouLiuTong=transaction_SpecialManager.getTransaction_yinziBySql(sql_SuiHouLiuTong);
	                       ls_Sui1=transaction_SpecialManager.getTransaction_yinziBySql(sql_Sui1);
	                       ls_Sui2=transaction_SpecialManager.getTransaction_yinziBySql(sql_Sui2);
	       
	       
	       
               String sql_YinDingChanChu="select t.createdate,'dd' fenqu,'银锭产出' REASONTYPE,'0' ACTION,sum(t.money) money from stat_gamechongzhi_yinpiao t "
                                +" where t.reasontype in ('金钱道具使用银块','10级奖励银子10级奖励银子','占领城市给的钱占领城市给的钱','占领矿给的钱占领矿给的钱'"
                                +",'竞技奖励银子竞技奖励银子')  and  t.createdate between '"+startDay+"' and '"+endDay+"' "+subSql+" group by t.createdate order by t.createdate";
	                         ls_YinDingChanChu=transaction_SpecialManager.getTransaction_yinziBySql(sql_YinDingChanChu);
	    
	           //String sql_YinDingChongZhi="select t.createdate,'dd' fenqu,'银锭充值' REASONTYPE,'0' ACTION,sum(t.money) money "
	             //               +" from stat_gamechongzhi_yinpiao t where t.reasontype like '充值%' and  "
	              //              +" t.createdate between '"+startDay+"' and '"+endDay+"'"+subSql+" group by t.createdate order by t.createdate";
	                            
	                            
	                            String sql_YinDingChongZhi="select to_char(t.time,'YYYY-MM-DD') createdate ,'dd' fenqu,'银锭充值' REASONTYPE,'0' ACTION,round(sum(t.money)-sum(t.cost)) money from stat_chongzhi t "+
	                              " where to_char(t.time,'YYYY-MM-DD') between '"+startDay+"' and  '"+endDay+"' " +subSql
	                              +" group by to_char(t.time,'YYYY-MM-DD')";
	                            
	                            
	                            
	                            
	                          ls_YinDingChongZhi=transaction_SpecialManager.getTransaction_yinziBySql(sql_YinDingChongZhi);
	                 
	           String sql_YinDingXiaoHao="select t.createdate,'dd' fenqu,'银锭消耗' REASONTYPE,'0' ACTION,sum(t.money) money from stat_gamechongzhi_yinpiao t "
	                            +" where t.reasontype in ('祈福祈福','商店购买','宠物炼化宠物炼化','进入付费地图进入付费地图','宠物合体宠物合体','扩展背包扩展背包'"
	                            +",'配方合成用银子合成装备','结婚婚礼规模龙凤呈祥-100-山盟海誓对戒(男)~山盟海誓对戒(女)~','家族捐献家族捐献','原地复活原地复活'"
	                            +",'千层塔刷新千层塔刷新','装备升级用银子强化装备','结婚婚礼规模天造地设-50-三生相守对戒(男)~三生相守对戒(女)~','庄园开地','洗练洗练'"
	                            +",'修理修理','刷新任务','炼器炼器','拍卖拍卖手续费','创建宗派创建宗派','JIEYI_COST结义消耗','天尊令使用天尊令','宠物炼化宠物强化'"
	                            +",'结婚求婚送花或糖2','结婚婚礼规模白首同心-5-一心一意对戒(男)~一心一意对戒(女)~','结婚强制离婚','结婚求婚送花或糖0','矿战矿战',"
	                            +"'祈福家族开炉炼丹','拍卖违约金取消拍卖违约金','结婚婚礼规模福禄鸳鸯-20-比翼双飞对戒(男)~比翼双飞对戒(女)~','结婚送花或糖1',"
	                            +"'结婚婚礼规模琴瑟和弦-10-三生缱绻对戒(男)~三生缱绻对戒(女)~','结婚送花或糖2','结婚请求结婚','王者之印使用王者之印','进入庄园仙府开门'"
	                            +",'结婚送花或糖0','结婚求婚送花或糖1','家族宣战家族宣战') and  t.createdate between '"+startDay+"' and '"+endDay+"'"+subSql
	                            +" group by t.createdate order by t.createdate";
	                           ls_YinDingXiaoHao = transaction_SpecialManager.getTransaction_yinziBySql(sql_YinDingXiaoHao);
	                 }
	                 else if("0".equals(displayType))
	                 {
	                 
	           String sql_SuiHouLiuTong="select 'dd' createdate,t.fenqu,'税后流通' REASONTYPE,'0' ACTION,sum(t.money) money from stat_gamechongzhi_yinpiao t "
	                            +" where  t.reasontype in ('摆摊在摆摊出售了东西','交易获取从他人那交易来','求购求购出售物品','邮件获取给卖家发钱') "
	                            +" and  t.createdate between '"+startDay+"' and '"+endDay+"' "+subSql+" group by t.fenqu order by t.fenqu";
	                           ls_SuiHouLiuTong=transaction_SpecialManager.getTransaction_yinziBySql(sql_SuiHouLiuTong);
	       
	                      String sql_Sui1="select 'dd' createdate,t.fenqu,'税后流通' REASONTYPE,'0' ACTION,sum(t.money) money from stat_gamechongzhi_yinpiao t "
	                            +" where  t.reasontype in ('摆摊在摆摊出售了东西','交易获取从他人那交易来') "
	                            +" and  t.createdate between '"+startDay+"' and '"+endDay+"' "+subSql+" group by t.fenqu";
	                         //ls_SuiHouLiuTong=transaction_SpecialManager.getTransaction_yinziBySql(sql_SuiHouLiuTong);
	                         
	                      String sql_Sui2="select 'dd' createdate,t.fenqu,'税后流通' REASONTYPE,'0' ACTION,sum(t.money) money from stat_gamechongzhi_yinpiao t "
	                            +" where  t.reasontype in ('求购求购出售物品') "
	                            +" and  t.createdate between '"+startDay+"' and '"+endDay+"' "+subSql+" group by t.fenqu ";
	                         //ls_SuiHouLiuTong=transaction_SpecialManager.getTransaction_yinziBySql(sql_SuiHouLiuTong);
	       
	                       ls_Sui1=transaction_SpecialManager.getTransaction_yinziBySql(sql_Sui1);
	                       ls_Sui2=transaction_SpecialManager.getTransaction_yinziBySql(sql_Sui2);
	       
	                     System.out.println("sql_Sui1按区:"+sql_Sui1);
	                     System.out.println("sql_Sui2按区:"+sql_Sui2);
	       
	       
	       
	       
               String sql_YinDingChanChu="select 'dd' createdate,t.fenqu,'银锭产出' REASONTYPE,'0' ACTION,sum(t.money) money from stat_gamechongzhi_yinpiao t "
                                +" where t.reasontype in ('金钱道具使用银块','10级奖励银子10级奖励银子','占领城市给的钱占领城市给的钱','占领矿给的钱占领矿给的钱'"
                                +",'竞技奖励银子竞技奖励银子')  and  t.createdate between '"+startDay+"' and '"+endDay+"' "+subSql+" group by t.fenqu order by t.fenqu";
	                           ls_YinDingChanChu=transaction_SpecialManager.getTransaction_yinziBySql(sql_YinDingChanChu);
	    
	           //String sql_YinDingChongZhi="select 'dd' createdate,t.fenqu,'银锭充值' REASONTYPE,'0' ACTION,sum(t.money) money "
	            //                +" from stat_gamechongzhi_yinpiao t where t.reasontype like '充值%' and  "
	            //                +" t.createdate between '"+startDay+"' and '"+endDay+"'"+subSql+" group by t.fenqu order by t.fenqu";
	                            
	                     String sql_YinDingChongZhi="select 'dd' createdate ,t.fenqu fenqu,'银锭充值' REASONTYPE,'0' ACTION,round(sum(t.money)-sum(t.cost)) money from stat_chongzhi t "+
	                              " where to_char(t.time,'YYYY-MM-DD') between '"+startDay+"' and  '"+endDay+"' " +subSql
	                              +" group by  t.fenqu";
	                            
	                           ls_YinDingChongZhi=transaction_SpecialManager.getTransaction_yinziBySql(sql_YinDingChongZhi);
	                 
	           String sql_YinDingXiaoHao="select 'dd' createdate,t.fenqu,'银锭消耗' REASONTYPE,'0' ACTION,sum(t.money) money from stat_gamechongzhi_yinpiao t "
	                            +" where t.reasontype in ('祈福祈福','商店购买','宠物炼化宠物炼化','进入付费地图进入付费地图','宠物合体宠物合体','扩展背包扩展背包'"
	                            +",'配方合成用银子合成装备','结婚婚礼规模龙凤呈祥-100-山盟海誓对戒(男)~山盟海誓对戒(女)~','家族捐献家族捐献','原地复活原地复活'"
	                            +",'千层塔刷新千层塔刷新','装备升级用银子强化装备','结婚婚礼规模天造地设-50-三生相守对戒(男)~三生相守对戒(女)~','庄园开地','洗练洗练'"
	                            +",'修理修理','刷新任务','炼器炼器','拍卖拍卖手续费','创建宗派创建宗派','JIEYI_COST结义消耗','天尊令使用天尊令','宠物炼化宠物强化'"
	                            +",'结婚求婚送花或糖2','结婚婚礼规模白首同心-5-一心一意对戒(男)~一心一意对戒(女)~','结婚强制离婚','结婚求婚送花或糖0','矿战矿战',"
	                            +"'祈福家族开炉炼丹','拍卖违约金取消拍卖违约金','结婚婚礼规模福禄鸳鸯-20-比翼双飞对戒(男)~比翼双飞对戒(女)~','结婚送花或糖1',"
	                            +"'结婚婚礼规模琴瑟和弦-10-三生缱绻对戒(男)~三生缱绻对戒(女)~','结婚送花或糖2','结婚请求结婚','王者之印使用王者之印','进入庄园仙府开门'"
	                            +",'结婚送花或糖0','结婚求婚送花或糖1','家族宣战家族宣战') and  t.createdate between '"+startDay+"' and '"+endDay+"'"+subSql
	                            +" group by t.fenqu order by t.fenqu";
	                           ls_YinDingXiaoHao=transaction_SpecialManager.getTransaction_yinziBySql(sql_YinDingXiaoHao);
	                 }

	    
	     //对比各个渠道数据
	       ArrayList<String> realChannelList = new ArrayList<String>();
	          realChannelList.add("税后流通(锭)");
	          realChannelList.add("流通税收(锭)");
	          realChannelList.add("银票产出(锭)");
	          realChannelList.add("银锭充值(锭)");
	          realChannelList.add("银票消耗(锭)");
	
	     synchronized(lock){
	         while(s.getTime() <= t.getTime() + 3600000){
		       String day = DateUtil.formatDate(s,"yyyy-MM-dd");
		       dayList.add(day);
		       s.setTime(s.getTime() + 24*3600*1000);
	           }
             }
             if("0".equals(displayType)){
              if(fenqu!=null){
               dayList.clear(); dayList.add(fenqu); 
               }else{
                 dayList.clear();
                  for(int num=0;num<fenQuList.size();num++){
                  String [] ls=fenQuList.get(num);
                  dayList.add(ls[1]);}
                }}
	     long channel_regday_nums[][] = new long[realChannelList.size()][dayList.size()];
        if ("true".equals(flag)) {//如果是刚打开页面，不查询
         if("1".equals(displayType)){
      
			for( int g=0;g< ls_SuiHouLiuTong.size();g++){
			       Transaction_yinzi transaction_yinzi=(Transaction_yinzi)ls_SuiHouLiuTong.get(g);
			         for(int k = 0 ; k < dayList.size() ; k++){
		            	String _day = dayList.get(k);
		            	if(_day.equals(transaction_yinzi.getCreateDate())){
			              channel_regday_nums[0][k]=transaction_yinzi.getMoney()/1000000;
			           }
			           }
			       }
			       
			    for( int g=0;g< ls_Sui1.size();g++){
			       Transaction_yinzi transaction_yinzi1=(Transaction_yinzi)ls_Sui1.get(g);
			       Transaction_yinzi transaction_yinzi2=(Transaction_yinzi)ls_Sui2.get(g);
			         for(int k = 0 ; k < dayList.size() ; k++){
		            	String _day = dayList.get(k);
		            	if(_day.equals(transaction_yinzi1.getCreateDate())){
			              channel_regday_nums[1][k]+=(long)(transaction_yinzi1.getMoney()/20)/1000000;
			               }
			           
			            if(_day.equals(transaction_yinzi2.getCreateDate())){
			              channel_regday_nums[1][k]+=(long)(transaction_yinzi2.getMoney()/10)/1000000;
			               }
			           }
			       }
			       
			for( int g=0;g< ls_YinDingChanChu.size();g++){
			       Transaction_yinzi transaction_yinzi=(Transaction_yinzi)ls_YinDingChanChu.get(g);
			        for(int k = 0 ; k < dayList.size() ; k++){
		            	String _day = dayList.get(k);
		            if(_day.equals(transaction_yinzi.getCreateDate())){
			       channel_regday_nums[2][k]=transaction_yinzi.getMoney()/1000000;
			       }
			       }
			       }
			for( int g=0;g< ls_YinDingChongZhi.size();g++){
			       Transaction_yinzi transaction_yinzi=(Transaction_yinzi)ls_YinDingChongZhi.get(g);
			          for(int k = 0 ; k < dayList.size() ; k++){
		            	String _day = dayList.get(k);
		            if(_day.equals(transaction_yinzi.getCreateDate())){
			       channel_regday_nums[3][k]=transaction_yinzi.getMoney()/2000;
			       }
			       }
			       }
			for( int g=0;g< ls_YinDingXiaoHao.size();g++){
			       Transaction_yinzi transaction_yinzi=(Transaction_yinzi)ls_YinDingXiaoHao.get(g);
			         for(int k = 0 ; k < dayList.size() ; k++){
		            	String _day = dayList.get(k);
		            if(_day.equals(transaction_yinzi.getCreateDate())){
			       channel_regday_nums[4][k]=transaction_yinzi.getMoney()/1000000;
			       }
			       }
			       }
			 } else if ("0".equals(displayType)){
          
			     for( int g=0;g< ls_SuiHouLiuTong.size();g++){
			       Transaction_yinzi transaction_yinzi=(Transaction_yinzi)ls_SuiHouLiuTong.get(g);
			       
			         for(int k = 0 ; k < dayList.size() ; k++){
		            	String _day = dayList.get(k);
		              if(_day.equals(transaction_yinzi.getFenQu())){
			           channel_regday_nums[0][k]=transaction_yinzi.getMoney()/1000000;
			         }
			         }
			       }
			       
			     for( int g1=0;g1< ls_Sui1.size();g1++){
			       Transaction_yinzi transaction_yinzi1=(Transaction_yinzi)ls_Sui1.get(g1);
			       
			         for(int k = 0 ; k < dayList.size() ; k++){
		            	String _day = dayList.get(k);
		              if(_day.equals(transaction_yinzi1.getFenQu())){
			           channel_regday_nums[1][k]+=(long)(transaction_yinzi1.getMoney()/20)/1000000;
			         }
			         }
			       }
			       for( int g2=0;g2< ls_Sui2.size();g2++){
			       Transaction_yinzi transaction_yinzi2=(Transaction_yinzi)ls_Sui2.get(g2);
			         for(int k = 0 ; k < dayList.size() ; k++){
		            	String _day = dayList.get(k);
			         if(_day.equals(transaction_yinzi2.getFenQu())){
			           channel_regday_nums[1][k]+=(long)(transaction_yinzi2.getMoney()/10)/1000000;
			         }
			         }
			       }
			       
			       
			       
			     for( int g=0;g< ls_YinDingChanChu.size();g++){
			       Transaction_yinzi transaction_yinzi=(Transaction_yinzi)ls_YinDingChanChu.get(g);
			        for(int k = 0 ; k < dayList.size() ; k++){
		            	String _day = dayList.get(k);
		              if(_day.equals(transaction_yinzi.getFenQu())){
			          channel_regday_nums[2][k]=transaction_yinzi.getMoney()/1000000;
			         }
			         }
			       }
			     for( int g=0;g< ls_YinDingChongZhi.size();g++){
			       Transaction_yinzi transaction_yinzi=(Transaction_yinzi)ls_YinDingChongZhi.get(g);
			        for(int k = 0 ; k < dayList.size() ; k++){
		            	String _day = dayList.get(k);
		              if(_day.equals(transaction_yinzi.getFenQu())){
			       channel_regday_nums[3][k]=transaction_yinzi.getMoney()/2000;
			        }
			        }
			       }
			     for( int g=0;g< ls_YinDingXiaoHao.size();g++){
			       Transaction_yinzi transaction_yinzi=(Transaction_yinzi)ls_YinDingXiaoHao.get(g);
			        for(int k = 0 ; k < dayList.size() ; k++){
		            	String _day = dayList.get(k);
		              if(_day.equals(transaction_yinzi.getFenQu())){
			       channel_regday_nums[4][k]=transaction_yinzi.getMoney()/1000000;
			       }
			       }
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
		<h2 style="font-weight:bold;">银子流通统计</h2>
		<form  method="post">
		  <input type='hidden' name='flag' value='true'/>
		    
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
                        if(_fenqu[1].equals(fenqu)){  out.print(" selected=\"selected\"");  }
                         %>
                          ><%=_fenqu[1] %></option>
                       <%
                       }
                       %>
                </select>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;
                
                                                     展示方式：
                   <input type="radio" <% if(displayType==null||"1".equals(displayType)){out.print("checked");} %>  name="displaytype" value="1" />时间
                   <input type="radio" <% if("0".equals(displayType)){out.print("checked");} %>  name="displaytype" value="0" />分区
               <!-- 
                                       流通类型：<select name="type">
                       <option  value="税后流通"
                           <% if("税后流通".equals(type)){  out.print(" selected=\"selected\""); } %>
                           >税后流通</option>
                       <option  value="银锭产出"
                           <% if("银锭产出".equals(type)){  out.print(" selected=\"selected\""); } %>
                           >银锭产出</option>
                       <option  value="银锭消耗"
                           <% if("银锭消耗".equals(type)){  out.print(" selected=\"selected\""); } %>
                           >银锭消耗</option>
                       <option  value="银锭产出"
                           <% if("银锭产出".equals(type)){  out.print(" selected=\"selected\""); } %>
                           >银锭产出</option>
                       <option  value="银锭充值"
                           <% if("银锭充值".equals(type)){  out.print(" selected=\"selected\""); } %>
                           >银锭充值</option>
                </select>&nbsp;&nbsp;
                 --> 
		           <br/>
		    	<input type='submit' value='提交'></form>
		    		
		    		<h3>银子流通统计</h3>
		    		<%
		    		out.println("<table id='test2' align='center' width='90%' cellpadding='1' bgcolor='#000000' cellspacing='1' border='0'>");
					out.print("<tr bgcolor='#EEEEBB'><td>日期</td>");
					
					long[] huizong=new long[realChannelList.size()];
		    		for(int c = 0 ; c < realChannelList.size() ; c++){
		    			out.print("<td>"+realChannelList.get(c)+"</td>");
		    		}
					out.println("</tr>");
					
					if ("true".equals(flag)) {  //如果是刚打开页面，不查询){
					
					for(int i = 0 ; i < dayList.size() ; i++){
						out.print("<tr bgcolor='#FFFFFF'><td>"+dayList.get(i)+"</td>");
						for(int c = 0 ; c < realChannelList.size() ; c++){
			    			out.print("<td>"+StringUtil.addcommas(channel_regday_nums[c][i])+"</td>");
			    			
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
					
					out.print("<tr bgcolor='#EEEEBB'><td>汇总</td>");
						for(int c = 0 ; c < realChannelList.size() ; c++){
			    			out.print("<td>"+StringUtil.addcommas(huizong[c])+"</td>");
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
