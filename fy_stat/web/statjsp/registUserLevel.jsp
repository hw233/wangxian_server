<%@ page contentType="text/html;charset=UTF-8"%>
<%@include file="../inc.jsp"%>
<%@ page
	import="com.xuanzhi.tools.cache.diskcache.concrete.DefaultDiskCache,org.slf4j.*,java.io.*,java.util.*,
	com.xuanzhi.tools.text.*,com.sqage.stat.commonstat.entity.ChongZhi,com.xuanzhi.tools.text.*,
	com.sqage.stat.commonstat.dao.UserDao,com.sqage.stat.commonstat.entity.User,com.sqage.stat.commonstat.manager.UserManager,
	com.sqage.stat.commonstat.manager.Impl.UserManagerImpl,com.sqage.stat.commonstat.manager.Impl.*,com.sqage.stat.service.*,
	com.sqage.stat.model.Channel,java.math.*"
	%>

  <%
	String startDay = request.getParameter("day");
	String endDay = request.getParameter("endDay");
	String statstartDay = request.getParameter("statday");
	String qudao=request.getParameter("qudao");
	String fenqu=request.getParameter("fenqu");
	
	if("0".equals(fenqu)){fenqu=null;}
	if("0".equals(qudao)){qudao=null;}
	
	String today = DateUtil.formatDate(new Date(),"yyyy-MM-dd");
	if(startDay == null) startDay = today;
	if(endDay == null) endDay = today;
	if(statstartDay == null) statstartDay = today;
	
	/**
	*获得渠道信息
	**/
		      ChannelManager cmanager = ChannelManager.getInstance();
              UserManagerImpl userManager=UserManagerImpl.getInstance();
              ChongZhiManagerImpl chongZhiManager=ChongZhiManagerImpl.getInstance();
              TaskAnalysisManagerImpl taskAnalysisManager=new TaskAnalysisManagerImpl();
              List<Channel> channelList = cmanager.getChannels();//渠道信息
              ArrayList<String []> fenQuList= userManager.getFenQu(null);//获得现有的渠道信息
             
            List<String[]> lsStr= userManager.getregestUserLevel(startDay,endDay,statstartDay,qudao,fenqu);
               	long total=0L;
				for(int i = 0 ; i < lsStr.size() ; i++){
					String[] ls=(String[])lsStr.get(i);
					if(ls[1]!=null){total+=Long.parseLong(ls[1]);}
					}
					
              int maxLevel=70;
              ArrayList dayList=new ArrayList();
              for(int i=0;i<maxLevel;i++){ dayList.add(i);}
              
              ArrayList realChannelList=new ArrayList();
			  realChannelList.add("用户级别");
				
             
              String channel_regday_nums[][] = new String[realChannelList.size()][dayList.size()];
              for(int j = 0 ; j < lsStr.size() ; j++){
					String[] ls=(String[])lsStr.get(j);
				   if(Integer.parseInt(ls[0])<maxLevel){
				    float zhanbi=0f;
					if(ls[1]!=null&&total!=0){zhanbi= Float.parseFloat(ls[1])/total;}
				   channel_regday_nums[0][Integer.parseInt(ls[0])]=Float.toString(zhanbi);
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
		账号等级分布
		</h2>
		<form  method="post">
		<!-- 注册起始日期：<input type='text' name='day' value='<%=startDay %>'>
		-- 注册结束日期：<input type='text' name='endDay' value='<%=endDay %>'>(格式：2012-02-09) -->
		
		 注册 开始日期： <input type="text" name="day" id="day" class="nqz_input" style="width:100px;" readonly="readonly" onfocus="new Calendar(false,'yyyy-MM-dd').showTimePicker(this);" value="<%=startDay %>"/>&nbsp;到
		  注册 结束日期： <input type="text" name="endDay" id="endDay" class="nqz_input" style="width:100px;" readonly="readonly" onfocus="new Calendar(false,'yyyy-MM-dd').showTimePicker(this);" value="<%=endDay %>"/>
		           
		
		
		<br>统计日期：<input type='text' name='statday' value='<%=statstartDay %>'>
		  	
		
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
                </select>
                
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
		    		<br/>
		    		<h3>账号等级分布</h3>
		    		<%
		    		out.println("<table id='test2' align='center' width='90%' cellpadding='1' bgcolor='#000000' cellspacing='1' border='0'>");
					out.print("<tr bgcolor='#EEEEBB'><td>等级</td><td>人数</td><td>所占比例</td></tr>");
				
					for(int i = 0 ; i < lsStr.size() ; i++){
					String[] ls=(String[])lsStr.get(i);
					
					long zhanbi=0L;
					BigDecimal b=new BigDecimal("0");
					if(ls[1]!=null&&total!=0){
					
					                BigDecimal b1 = new BigDecimal(ls[1]);
  									BigDecimal b2 = new BigDecimal(total);

                                
  									BigDecimal b100 = new BigDecimal("100");
  									BigDecimal b11 = b100.multiply(b1);
  									       b = b11.divide(b2, new MathContext(4));
					
					}
					
					
				    out.print("<tr bgcolor='#FFFFFF'><td>"+ls[0]+"</td><td>"+ls[1]+"</td><td>"+b.toString()+" %</td></tr>");
					}
					 out.print("<tr bgcolor='#EEEEBB'><td>汇总</td><td>"+total+"</td><td>&nbsp;</td></tr>");
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
