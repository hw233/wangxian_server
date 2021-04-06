<%@ page contentType="text/html;charset=UTF-8"%>
<%@include file="../inc.jsp"%>
<%@ page
	import="com.xuanzhi.tools.cache.diskcache.concrete.DefaultDiskCache,org.slf4j.*,java.io.*,java.util.*,com.xuanzhi.tools.text.*,
	org.springframework.context.ApplicationContext,org.springframework.web.context.support.WebApplicationContextUtils,
	com.sqage.stat.commonstat.dao.UserDao,com.sqage.stat.commonstat.entity.*,com.sqage.stat.commonstat.manager.*,
	com.sqage.stat.commonstat.manager.Impl.*,com.sqage.stat.service.*,com.sqage.stat.model.Channel,java.sql.*"
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
	
	          String today = DateUtil.formatDate(new java.util.Date(new java.util.Date().getTime()-24*60*60*1000),"yyyy-MM-dd");
	          if(startDay == null) startDay = today;
	          if(endDay == null) endDay = today;
	
	         java.util.Date tt = DateUtil.parseDate(endDay,"yyyy-MM-dd");
	         String endDay_kucun = DateUtil.formatDate(new java.util.Date(tt.getTime()+24*60*60*1000),"yyyy-MM-dd");
	         java.util.Date t= DateUtil.parseDate(endDay_kucun,"yyyy-MM-dd");
	         java.util.Date s = DateUtil.parseDate(startDay,"yyyy-MM-dd");
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
        
        
        
        
         String fenqu=null;
         String pingBi_fenqu="春暖花开','";
	       
		Connection conn = DriverManager.getConnection("jdbc:oracle:thin:@116.213.142.188:1521:ora11g","stat_mieshi","stat_mieshi");
		try{
			PreparedStatement ps = null;
			try {
				ps = conn.prepareStatement("select distinct(t.fenqu) name from stat_yinzikucun t "
				+" where to_Char(t.createdate, 'YYYY-MM-DD') between '"+startDay+"' and '"+endDay_kucun+"' and t.column1 = 1 and t.column2 = 2 and t.column4 = '-1' "
				+" group by t.fenqu,to_char(t.createdate,'YYYY-MM-DD') having sum(t.count)<1000 ");
				
			ps.execute();
			ResultSet rs = ps.getResultSet();
			out.println("存在积分库存不足1000的服：");
			int num=0;
			while (rs.next()) {
			num++;
			String nn=rs.getString("name");
			pingBi_fenqu+=nn+"','";
			if(num%10!=0){
			out.print(nn+"||");
			}else
			{
			out.print(nn+"||<br>");
			}
		     } 
		      ps.close();
		     
		     String s2="select fenqu name,count(fenqu) count from "
		     +"( select t.fenqu,to_char(t.createdate,'YYYY-MM-DD'),sum(t.count)  "
		     +" from stat_yinzikucun t where to_Char(t.createdate, 'YYYY-MM-DD') between '"+startDay+"' and '"+endDay_kucun+"' "
		     +" and t.column1 = 1 and t.column2 = 2  and t.column4 = '-1'  group by t.fenqu,to_char(t.createdate,'YYYY-MM-DD')  ) group  by fenqu  ";
				
				
				num=0;
		     ps = conn.prepareStatement(s2) ;
				
				System.out.println("s2:"+s2);
			ps.execute();
			out.println("<br>存在积分库存断档的服：");
			rs = ps.getResultSet();
			while (rs.next()) {
			num++;
			String nn=rs.getString("name");
			int count=rs.getInt("count");
			System.out.println("count "+count+"dayList.size() "+dayList.size());
			if(count!=dayList.size()){
			pingBi_fenqu+=nn+"','";
			
			if(num%10!=0){
			out.print(nn+"||");
			}else
			{
			out.print(nn+"||<br>");
			}
			}
		     } 
		     }catch (SQLException e) {
			e.printStackTrace();
		} finally{
			ps.close();
			conn.close();
		}
		}catch(Exception e){
			throw e;
		}finally{
			conn.close();
		}
	   
	       pingBi_fenqu+="桃花仙岛','天下无双','海纳百川','琼楼金阙','飘渺仙道','万里苍穹','盛世欢歌','修罗转生";
	           String subSql="";
	           String ysubSql="";
	           if(fenqu!=null){
	           subSql+= "  and  g.fenqu ='"+fenqu+"'";
	           ysubSql+=" and  y.fenqu ='"+fenqu+"'";
	           }else
	           {
	            subSql +=" and  g.fenqu not in ('"+pingBi_fenqu+"')";
	            ysubSql+=" and  y.fenqu not in ('"+pingBi_fenqu+"')";
	           }
        
        
        
        
        
        if ("true".equals(flag)) {//如果是刚打开页面，不查询
        
        String sql=" select nn.d1 fengyin,nn.huoqu count,nn.huikui money,nn.xiaohao avg,mm.money kucun from ( "
        +" select to_char(g.time,'YYYY-MM-DD') d1, sum(case g.action when 0 then g.money end) huoqu, sum(case g.action when 2 then g.money end) huikui,"
        +" sum(case g.action when 1 then g.money end)/2 xiaohao from stat_gamechongzhi_jifen g where g.currencytype=31 and "
        +" g.time between to_date('"+startDay+" 00:00:00','YYYY-MM-DD hh24:mi:ss') and to_date('"+endDay_kucun+" 23:59:59','YYYY-MM-DD hh24:mi:ss')  "+subSql
        +" group by to_char(g.time,'YYYY-MM-DD')"
        +" ) nn "
        +" left join ( "
        +" select to_char(y.createdate,'YYYY-MM-DD') d2,sum(y.count) money  from stat_yinzikucun y where y.column1=1 and y.column2=2 and y.column4='-1' "
        +" and y.createdate  between to_date('"+startDay+" 00:00:00','YYYY-MM-DD hh24:mi:ss') and to_date('"+endDay_kucun+" 23:59:59','YYYY-MM-DD hh24:mi:ss') "+ysubSql
        +" group by to_char(y.createdate,'YYYY-MM-DD')) mm on nn.d1=mm.d2 order by nn.d1 ";
         
           ls= daoJuManager.getJiFeninfo(sql);
		 
		 for(int k=0;k<ls.size()-1;k++)
					{
					String[] strS=ls.get(k);
					String[] strS2=ls.get(k+1);
			          
			             Long gg=Long.parseLong(strS2[4]) - Long.parseLong(strS[4]);
			             
			            if("0".equals(strS2[4])||"0".equals(strS[4])){
			            tuxing[0][k]=0;
			            }else{
			            tuxing[0][k]=((gg>Long.parseLong(money)||gg< -Long.parseLong(money))&&k>0)   ? tuxing[0][k-1] : gg;
			            }
			           
			            //真实库存
			            
			            if(mintarget>tuxing[0][k]){mintarget=tuxing[0][k];}
			          
			            Long temp=Long.parseLong(strS[1])+Long.parseLong(strS[2]) - Long.parseLong(strS[3]);
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
		<h2 style="font-weight:bold;">剔除库存断档的服</h2>
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
