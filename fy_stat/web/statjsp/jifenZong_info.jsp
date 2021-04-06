<%@ page contentType="text/html;charset=UTF-8"%>
<%@include file="../inc.jsp"%>
<%@ page
	import="com.xuanzhi.tools.cache.diskcache.concrete.DefaultDiskCache,org.slf4j.*,java.io.*,java.util.*,com.xuanzhi.tools.text.*,
	org.springframework.context.ApplicationContext,org.springframework.web.context.support.WebApplicationContextUtils,
	com.sqage.stat.commonstat.dao.UserDao,com.sqage.stat.commonstat.entity.*,com.sqage.stat.commonstat.manager.*,
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
	           String money=request.getParameter("money");
	          if("0".equals(money)||money==null){money="10000000";}
	
	          String today = DateUtil.formatDate(new Date(new Date().getTime()-24*60*60*1000),"yyyy-MM-dd");
	          if(startDay == null) startDay = today;
	          if(endDay == null) endDay = today;
	
	         Date tt = DateUtil.parseDate(endDay,"yyyy-MM-dd");
	         String endDay_kucun = DateUtil.formatDate(new Date(tt.getTime()+24*60*60*1000),"yyyy-MM-dd");
	         Date t= DateUtil.parseDate(endDay_kucun,"yyyy-MM-dd");
	         Date s = DateUtil.parseDate(startDay,"yyyy-MM-dd");
	         ArrayList<String> dayList = new ArrayList<String>();
	         
	          synchronized(lock){
	         while(s.getTime() <= t.getTime() + 3600000){
		       String day = DateUtil.formatDate(s,"yyyy-MM-dd");
		       dayList.add(day);
		       s.setTime(s.getTime() + 24*3600*1000);
	           }
	           
             }
             
             
              ChannelManager cmanager = ChannelManager.getInstance();
              DaoJuManagerImpl daoJuManager=DaoJuManagerImpl.getInstance();
              
             ArrayList<String []> ls=new ArrayList<String []>();
             ArrayList<String> xiang = new ArrayList<String>();
	          xiang.add("实际库存变化");
	          xiang.add("预计库存变化");
		 
		   long tuxing[][] = new long[xiang.size()][dayList.size()];
	       long mintarget=0;
	       int lenth=(15>dayList.size())? dayList.size():15;
        if ("true".equals(flag)) {//如果是刚打开页面，不查询
        
        String sql=" select nn.d1 fengyin,nn.huoqu count,nn.huikui money,round(nn.xiaohao) avg,mm.money kucun from ( "
        +" select to_char(g.time,'YYYY-MM-DD') d1, sum(case g.action when 0 then g.money end) huoqu, sum(case g.action when 2 then g.money end) huikui,"
        +" sum(case g.action when 1 then g.money end)/2 xiaohao from stat_gamechongzhi_jifen g where "
        +" g.time between to_date('"+startDay+" 00:00:00','YYYY-MM-DD hh24:mi:ss') and to_date('"+endDay_kucun+" 23:59:59','YYYY-MM-DD hh24:mi:ss') "
        +" group by to_char(g.time,'YYYY-MM-DD')"
        +" ) nn "
        +" left join ( "
        +" select to_char(y.createdate,'YYYY-MM-DD') d2,sum(y.count) money  from stat_yinzikucun y where y.column1=1 and y.column2=2 and y.column4='-1'  "
        +" and y.createdate  between to_date('"+startDay+" 00:00:00','YYYY-MM-DD hh24:mi:ss') and to_date('"+endDay_kucun+" 23:59:59','YYYY-MM-DD hh24:mi:ss') "
        +" group by to_char(y.createdate,'YYYY-MM-DD')) mm on nn.d1=mm.d2 order by nn.d1 ";
         
           ls= daoJuManager.getJiFeninfo(sql);
		 
		 for(int k=0;k<ls.size()-1;k++)
					{
					String[] strS=ls.get(k);
					String[] strS2=ls.get(k+1);
			          
			             Long gg=Long.parseLong(strS2[4]==null?"0":strS2[4]) - Long.parseLong(strS[4]==null?"0":strS[4]);
			             
			            if("0".equals(strS2[4])||"0".equals(strS[4])){
			            tuxing[0][k]=0;
			            }else{
			            tuxing[0][k]=((gg>Long.parseLong(money)||gg< -Long.parseLong(money))&&k>0)   ? tuxing[0][k-1] : gg;
			            }
			           
			            //真实库存
			            
			            if(mintarget>tuxing[0][k]){mintarget=tuxing[0][k];}
			          
			            Long temp=Long.parseLong(strS[1]!=null?strS[1]:"0")+Long.parseLong(strS[2]!=null?strS[2]:"0") - Long.parseLong(strS[3]!=null?strS[3]:"0");
			            tuxing[1][k]= ((temp > Long.parseLong(money)||temp< -Long.parseLong(money))&&k>0) ? tuxing[1][k-1] : temp;
			           
			            if(mintarget>tuxing[1][k]){mintarget=tuxing[1][k];}
			            
					     //系统变化
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
    <script type="text/javascript" language="javascript">
	      function search() {
		      $('form1').action="yinzixiaohao_info_detail.jsp";
		      $('form1').submit();
	        }
	        
	        
	      function search2() {
		      $('form1').action="yinzichanchu_info_detail.jsp";
		      $('form1').submit();
	        }
	        	
	        
	   
         </script>

     <script language="JavaScript">
   var mycars=[<% for(int i = 0 ; i < dayList.size() ; i++){ out.print("\""+dayList.get(i)+"\",");} %>"E"];
       function drawRegUserFlotr(){
		    var f = Flotr.draw(
		$('regUserContainer'), [
		<%for(int j = 0 ; j < xiang.size() ; j++){
			StringBuffer sb2 = new StringBuffer();
			sb2.append("[");
			for(int kk = 0 ; kk < tuxing[j].length ; kk++){
				sb2.append("["+kk+","+tuxing[j][kk]+"]");
				if(kk < tuxing[j].length -1) sb2.append(",");
			}
			sb2.append("]");
			out.println("{");
			out.println("data:"+sb2.toString()+",");
			out.println("label:'"+xiang.get(j)+"'");
			out.println("}");
			if(j < xiang.size()-1) out.print(",");
		}
		%>
		],{
			xaxis:{
				noTicks: <%= lenth%>,	// Display 7 ticks.	
				tickFormatter: function(n){ return mycars[Math.floor(n)];}, // =>
				min: 0,
				max: <%= dayList.size()%>
			},
			yaxis:{
				tickFormatter: function(n){ return (n);}, // =>
				min: <%=mintarget%>,
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
		<h2 style="font-weight:bold;">积分统计</h2>
		<form   method="post" id="form1">
		  <input type='hidden' name='flag' value='true'/>
		

                        开始日期： <input type="text" name="day" id="day" class="nqz_input" style="width:100px;" readonly="readonly" onfocus="new Calendar(false,'yyyy-MM-dd').showTimePicker(this);" value="<%=startDay %>"/>&nbsp;到
		  结束日期： <input type="text" name="endDay" id="endDay" class="nqz_input" style="width:100px;" readonly="readonly" onfocus="new Calendar(false,'yyyy-MM-dd').showTimePicker(this);" value="<%=endDay %>"/>
		 &nbsp;&nbsp;&nbsp;&nbsp;
		  抹平幅度<input type='text' name='money' value='<%=money %>'>（如果振幅超过这个值，就用前一天的值替带）
		    <br/><br/>
                                            
		    	<input type='submit' value='提交'></form>
		    	
		    	<br/>
		    	
		    	      <a href="jifenZong_info_qufenqu.jsp">剔除库存断档的服</a>|
		    	      <a href="jifenZong_info_shaixuan_fenqu.jsp">各个服务器数据</a>|
		    	      <a href="jifenZong_info_shaixuan.jsp">筛选超标服务器</a>|
		    		  <a href="jifen_chanchu_info.jsp">积分产出</a>|
		    		  <a href="GameChongZhiJiFen_huizong1.jsp">积分明细下载</a>|
		    		  <a href="jifen_info.jsp">积分消耗</a>
		    	
		    		<%
		    		out.println("<table id='test2' align='center' width='90%' cellpadding='1' bgcolor='#000000' cellspacing='1' border='0'>");
					out.print("<tr bgcolor='#EEEEBB'><td>日期</td><td>充值积分</td><td>老用户回馈</td><td>积分消耗</td><td>积分库存剩余</td><td>预计库存变化</td><td>实际库存变化</td></tr>");
					for(int i=0;i<ls.size();i++)
					{
					String[] strS=ls.get(i);
					
					out.print("<tr bgcolor='#FFFFFF'><td>"+strS[0]+"</td><td>"+strS[1]+"</td><td>"+strS[2]+"</td><td>"+strS[3]+"</td><td>"+strS[4]+"</td><td bgcolor='#EEEEBB'>"+tuxing[1][i]+"</td><td bgcolor='#EEEEBB'>"+tuxing[0][i]+"</td></tr>");
					
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
