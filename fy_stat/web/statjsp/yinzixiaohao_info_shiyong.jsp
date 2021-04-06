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
	public long[][] sub(long [] x,long [] y,int n){
		long[][] z=new long[n][n];
		for(int i=0;i<n;i++)
		{
			for(int j=i;j<n;j++){
				
			long a=0;
			long b=0;
			for(int k=i;k<=j-1;k++){a+=z[i][k];}
			for(int k=0;k<=i-1;k++){b+=z[k][j];}
			z[i][j]=Math.max(0,Math.min(x[i]-a, y[j]-b));
			
			}
		}
		return z;
	
	}
      %>
     <%
              String flag = null;
              flag = request.getParameter("flag");
    
	          String startDay = request.getParameter("day");
	          String endDay = request.getParameter("endDay");
	          String fenqu=request.getParameter("fenqu");
	          String displayType="1";
	
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
              
              ArrayList<String []> fenQuList= userManager.getFenQu(null);//获得现有的渠道信息
	          Transaction_SpecialManagerImpl transaction_SpecialManager = Transaction_SpecialManagerImpl.getInstance();
	       
	           String subSql="";
	           if(fenqu!=null){
	           subSql+=" and  t.fenqu ='"+fenqu+"'";
	           }
	            List<Transaction_yinzi> ls_YinDingChanChu=null;
	            List<Transaction_yinzi> ls_YinDingChongZhi=null;
	            List<Transaction_yinzi> ls_YinDingXiaoHao=null;
	            
	            int displaynum=(15 > dayList.size()) ? dayList.size() : 15;
	           if("1".equals(displayType)){
	   
               String sql_YinDingChanChu="select t.createdate,'dd' fenqu,'银锭产出' REASONTYPE,'0' ACTION,sum(t.money) money from stat_gamechongzhi_yinzi t,dt_reasontype r "
                                +" where  r.name in ('邮件获取系统补单','邮件获取充值返利','邮件获取充值活动','充值活动奖励累计充值奖励返利','邮件获取GM工具补发',"
                            +"'邮件获取后台发送-系统奖励！','邮件获取后台发送-系统邮件','金钱道具使用碎银子','金钱道具使用银两','金钱道具使用银块',"
                            +"'金钱道具使用银条','金钱道具使用银锭','金钱道具使用银砖','竞技奖励银子竞技奖励银子','占领城市给的钱占领城市给的钱',"
                            +"'占领矿给的钱占领矿给的钱','10级奖励银子10级奖励银子','10级奖励银子20级奖励银子','10级奖励银子30级奖励银子',"
                            +"'10级奖励银子40级奖励银子','10级奖励银子50级奖励银子','10级奖励银子60级奖励银子','10级奖励银子70级奖励银子',"
                            +"'10级奖励银子80级奖励银子','10级奖励银子90级奖励银子')  and  t.createdate between '"+startDay+"' and '"+endDay+"' "+subSql+"  and to_char(t.reasontype)=to_char(r.id) group by t.createdate order by t.createdate";
	                         ls_YinDingChanChu=transaction_SpecialManager.getTransaction_yinziBySql(sql_YinDingChanChu);
	                            
	                            String sql_YinDingChongZhi="select to_char(t.time,'YYYY-MM-DD') createdate ,'dd' fenqu,'银锭充值' REASONTYPE,'0' ACTION,round(sum(t.money)) money from stat_chongzhi t "+
	                              " where to_char(t.time,'YYYY-MM-DD') between '"+startDay+"' and  '"+endDay+"' " +subSql
	                              +" group by to_char(t.time,'YYYY-MM-DD')";
	                            
	                          ls_YinDingChongZhi=transaction_SpecialManager.getTransaction_yinziBySql(sql_YinDingChongZhi);
	                 
	           String sql_YinDingXiaoHao="select t.createdate,'dd' fenqu,'银锭消耗' REASONTYPE,'0' ACTION,"
	           
	           +"sum(case "
	           +"when r.name in ('商店购买','银票不足商店购买','配方合成鉴定符合成','装备升级用银子强化装备','宠物合体宠物合体','发送邮件扣费发邮件收费'"
	           +",'祈福家族开炉炼丹','押镖','庄园开地','家族篝火家族篝火','家族捐献家族捐献','进入付费地图进入付费地图','扩展背包扩展背包'"
	           +",'JIEYI_COST加入结义消耗','创建宗派创建宗派','购买家族徽章购买家族徽章','封印任务封印任务','结婚婚礼规模白首同心-5-一心一意对戒(男)~一心一意对戒(女)~'"
	           +",'结婚婚礼规模琴瑟和弦-10-三生缱绻对戒(男)~三生缱绻对戒(女)~','结婚婚礼规模福禄鸳鸯-20-比翼双飞对戒(男)~比翼双飞对戒(女)~'"
	           +",'结婚婚礼规模天造地设-50-三生相守对戒(男)~三生相守对戒(女)~','结婚婚礼规模龙凤呈祥-100-山盟海誓对戒(男)~山盟海誓对戒(女)~'"
	           +",'结婚婚礼规模500000','结婚婚礼规模5000000','结婚婚礼规模1000000','结婚婚礼规模2000000','结婚婚礼规模10000000'"
	           +",'结婚请求结婚','千层塔刷新千层塔刷新','结婚送花或糖0','结婚送花或糖1','结婚送花或糖2','结婚结婚补领戒指','结婚求婚送花或糖0'"
	           +",'结婚求婚送花或糖1','结婚求婚送花或糖2','结婚强制离婚','刷新任务','宠物炼化炼化','JIEYI_COST结义消耗','天尊令使用天尊令'"
	           +",'家族宣战家族宣战','配方合成用银子合成装备','炼器炼器','洗练洗练','城市争夺战城市争夺战','矿战矿战','绑银不足配方合成鉴定符合成'"
	           +",'绑银不足使用绑银装备绑定','绑银不足商店购买','绑银不足使用绑银装备摘星','绑银不足使用绑银强化装备','绑银不足宠物强化强化'"
	           +",'绑银不足活动-祭祖','绑银不足申请家族驻地','绑银不足申请家族','绑银不足修理修理','绑银不足进入庄园仙府开门','绑银不足宠物炼化宠物强化'"
	           +",'绑银不足技能升级技能升级','绑银不足原地复活原地复活','绑银不足配方合成用绑银合成装备','绑银不足取喝酒buff取酒buff'"
	           +",'绑银不足王者之印使用王者之印','祈福祈福','银票不足祈福祈福','拍卖拍卖手续费','拍卖违约金取消拍卖违约金','周月周月反馈购买'"
	           +",'活动金剪刀活动','活动银剪刀活动','兑换活动兑换活动扣除银子','进入宠物迷城进入宠物迷城','接取任务挖宝','大师技能加点skMasterAddPoint',"
	           +"'大师技能加点skMasterExPoint') then t.money "
	           
	           +"when r.name in ('交付镖车交付镖车','金钱道具使用镖玉(白)','金钱道具使用镖玉(绿)','金钱道具使用镖玉(蓝)','金钱道具使用镖玉(紫)','金钱道具使用镖玉(橙)'"
	           +",'金钱道具使用镖银(白)','金钱道具使用镖银(绿)','金钱道具使用镖银(蓝)','金钱道具使用镖银(紫)','金钱道具使用镖银(橙)'"
	           +",'金钱道具使用镖金(白)','金钱道具使用镖金(绿)','金钱道具使用镖金(蓝)','金钱道具使用镖金(紫)','金钱道具使用镖金(橙)','击杀奸细击杀奸细') then -t.money "
	           +"when r.name in ('竞标竞标','追加资金追加资金') then t.money*0.03 "
	           +"when r.name in ('交易交易给他人','摆摊在摆摊上购买物品') then round(t.money/21) "
	           +"when r.name in ('拍卖竞拍费拍卖竞拍费') then round(t.money*0.2) "
	           +"when r.name in ('求购求购预存费用和手续费') then round(t.money/11)  end) money "
	           
	           +"from stat_gamechongzhi_yinzi t,dt_reasontype r "
	                            +" where  t.createdate between '"+startDay+"' and '"+endDay+"'"+subSql
	                            +"  and to_char(t.reasontype)=to_char(r.id) group by t.createdate order by t.createdate";
	                           ls_YinDingXiaoHao = transaction_SpecialManager.getTransaction_yinziBySql(sql_YinDingXiaoHao);
	                 }

	    
	     //对比各个渠道数据
	       ArrayList<String> realChannelList = new ArrayList<String>();
	          realChannelList.add("银锭产出(锭)");
	          realChannelList.add("银锭充值(锭)");
	          realChannelList.add("银锭消耗(锭)");
	
	     synchronized(lock){
	         while(s.getTime() <= t.getTime() + 3600000){
		       String day = DateUtil.formatDate(s,"yyyy-MM-dd");
		       dayList.add(day);
		       s.setTime(s.getTime() + 24*3600*1000);
	           }
             }
	       long channel_regday_nums[][] = new long[realChannelList.size()][dayList.size()];
	       long xiaohaoDays[][] = new long[1][dayList.size()];
	       long x[]=new long[dayList.size()];
		   long y[]=new long[dayList.size()];
		   long[][] a=new long[ dayList.size()][dayList.size()];
        if ("true".equals(flag)) {//如果是刚打开页面，不查询
         if("1".equals(displayType)){
      
			for( int g=0;g< ls_YinDingChanChu.size();g++){
			       Transaction_yinzi transaction_yinzi=(Transaction_yinzi)ls_YinDingChanChu.get(g);
			        for(int k = 0 ; k < dayList.size() ; k++){
		            	String _day = dayList.get(k);
		            if(_day.equals(transaction_yinzi.getCreateDate())){
			       channel_regday_nums[0][k]=transaction_yinzi.getMoney()/1000000;
			       }
			       }
			       }
			for( int g=0;g< ls_YinDingChongZhi.size();g++){
			       Transaction_yinzi transaction_yinzi=(Transaction_yinzi)ls_YinDingChongZhi.get(g);
			          for(int k = 0 ; k < dayList.size() ; k++){
		            	String _day = dayList.get(k);
		            if(_day.equals(transaction_yinzi.getCreateDate())){
			       channel_regday_nums[1][k]=transaction_yinzi.getMoney()/2000;
			       }
			       }
			       }
			for( int g=0;g< ls_YinDingXiaoHao.size();g++){
			       Transaction_yinzi transaction_yinzi=(Transaction_yinzi)ls_YinDingXiaoHao.get(g);
			       if("卧虎藏龙".equals(fenqu)||"伏虎降龙".equals(fenqu)){
			         for(int k = 0 ; k < dayList.size() ; k++){
		            	String _day = dayList.get(k);
		            if(_day.equals(transaction_yinzi.getCreateDate())){
			      // channel_regday_nums[2][k]=transaction_yinzi.getMoney()/1000000;
			        channel_regday_nums[2][k]=transaction_yinzi.getMoney()/10000000*11;
			       }
			       }
			       }else
			       {
			         for(int k = 0 ; k < dayList.size() ; k++){
		            	String _day = dayList.get(k);
		            if(_day.equals(transaction_yinzi.getCreateDate())){
			         channel_regday_nums[2][k]=transaction_yinzi.getMoney()/1000000;
			       }
			       }
			       
			       }
			       }
			      
			 
					for(int i = 0 ; i < dayList.size() ; i++){
						for(int c = 0 ; c < realChannelList.size() ; c++){
			    			x[i]=channel_regday_nums[1][i];
			    			y[i]=channel_regday_nums[2][i];
			    		}
					}
				
					
		             a= sub(x, y,dayList.size());
			 
			 
			 
			 for(int i=0;i<dayList.size();i++)
		      {
		      int daysNum=0;
		      
			 for(int j=i;j<dayList.size();j++){
			 
			   long aa = 0;
				 for(int k = 0 ; k <= j ; k++)
				   {
				      aa+=a[i][k];
				      }
				   if(i<=j&&(x[i]- aa)!=0)
					{ daysNum++; }
			     }
			      xiaohaoDays[0][i]=daysNum;
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
		<%for(int j = 0 ; j < 1 ; j++){
			StringBuffer sb2 = new StringBuffer();
			sb2.append("[");
			for(int kk = 0 ; kk < xiaohaoDays[j].length ; kk++){
				sb2.append("["+kk+","+xiaohaoDays[j][kk]+"]");
				if(kk < xiaohaoDays[j].length -1) sb2.append(",");
			}
			sb2.append("]");
			out.println("{");
			out.println("data:"+sb2.toString()+",");
			out.println("label:'人民币消耗天数 '");
			out.println("}");
			if(j < 1-1) out.print(",");
		}
		%>
		],{
			xaxis:{
				noTicks: <%= displaynum %>,	// Display 7 ticks.	
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
		<h2 style="font-weight: bold;"><br></h2><h2 style="font-weight: bold;">人民币消耗统计</h2>
		<form   method="post" id="form1">
		  <input type='hidden' name='flag' value='true'/>
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
                        if(_fenqu[1].equals(fenqu)){  out.print(" selected=\"selected\"");  }
                         %>
                          ><%=_fenqu[1] %></option>
                       <%
                       }
                       %>
                </select>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;
                
		    	<input type='submit' value='提交'></form>
		    	<br/>
		    		<%
		    		out.println("<table id='test2' align='center' width='90%' cellpadding='1' bgcolor='#000000' cellspacing='1' border='0'>");
					out.print("<tr bgcolor='#EEEEBB'><td>日期</td>");
					
		    		for(int c = 0 ; c < dayList.size() ; c++){
		    			out.print("<td>"+dayList.get(c)+"</td>");
		    		}
					out.println("</tr>");
					
				if ("true".equals(flag)) {  //如果是刚打开页面，不查询){
		              for(int i=0;i<dayList.size();i++)
		              {
		                out.print("<tr bgcolor='#FFFFFF'><td>"+dayList.get(i)+"</td>");
			              for(int j=0;j<dayList.size();j++){
				              long aa = 0;
				            for(int k = 0 ; k <= j ; k++)
				            {
				              aa+=a[i][k];
				              }
				   if(i<=j)
					out.print("<td>"+(x[i]- aa)+"</td>");
				  else
					out.print("<td>&nbsp;</td>");
			   }
			     out.print("</tr>");
		       }
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
