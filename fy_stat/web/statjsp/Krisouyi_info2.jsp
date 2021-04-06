<%@ page contentType="text/html;charset=UTF-8"%>
<%@include file="../inc.jsp"%>
<%@ page
	import="com.xuanzhi.tools.cache.diskcache.concrete.DefaultDiskCache,org.slf4j.*,java.io.*,
	java.util.*,com.xuanzhi.tools.text.*,com.xuanzhi.tools.text.*,
	com.sqage.stat.commonstat.entity.ChongZhi,com.sqage.stat.commonstat.dao.UserDao,
	com.sqage.stat.commonstat.entity.User,com.sqage.stat.commonstat.manager.UserManager,
	com.sqage.stat.commonstat.manager.Impl.UserManagerImpl,com.sqage.stat.commonstat.manager.Impl.*,
	com.sqage.stat.service.*,com.sqage.stat.model.Channel,java.text.SimpleDateFormat"
	%>
	<%!Object lock = new Object() {	};%>
  <%
  	String startDay = request.getParameter("day");
  	String endDay = request.getParameter("endDay");
  	String qudao = request.getParameter("qudao");

  	if ("0".equals(qudao)) {
  		qudao = null;
  	}

  	String today = DateUtil.formatDate(new Date(), "yyyy-MM-dd");
  	if (startDay == null) startDay = today;
  	if (endDay == null) endDay = today;


  	/**
  	 *获得渠道信息
  	 **/
  	ArrayList<String> typeList = null;
  	ChannelManager cmanager = ChannelManager.getInstance();
  	//UserManagerImpl userManager = UserManagerImpl.getInstance();
  	ChongZhiManagerImpl chongZhiManager = ChongZhiManagerImpl.getInstance();
  	PlayGameManagerImpl playGameManager=PlayGameManagerImpl.getInstance();
  	//ArrayList<String []> fenQuList = userManager.getFenQu(null);//获得现有的分区信息
  	typeList = chongZhiManager.getTypes(null);//获得现有的充值类型
  	List<Channel> channelList = cmanager.getChannels();//渠道信息

  	String money_search = null;
  	String username_search = null;

  	ArrayList<String> realChannelList = new ArrayList<String>();
  	Date t = DateUtil.parseDate(endDay, "yyyy-MM-dd");
  	Date s = DateUtil.parseDate(startDay, "yyyy-MM-dd");
  	ArrayList<String> regdayList = new ArrayList<String>();
  	synchronized (lock) {
  		while (s.getTime() <= t.getTime() + 3600000) {
  			String day = DateUtil.formatDate(s, "yyyy-MM-dd");
  			regdayList.add(day);
  			s.setTime(s.getTime() + 24 * 3600 * 1000);
  		}
  	}
  	for (String regdayStr : regdayList) {//注册日期列表
  		realChannelList.add(regdayStr);
  	}
  	ArrayList<String> daysList = new ArrayList<String>();
  	for (int days = 0; days < 50; days++)///展示日期数
  	{
  		daysList.add(new Integer(days).toString());
  	}
  	   long channel_regday_nums[][] = new long[realChannelList.size()][daysList.size()];
  	   List qianrensouyiList =new ArrayList();
  		   
       for (int regday=0;regday<regdayList.size();regday++) {//注册日期列表
            String regdayStr=regdayList.get(regday);
  		    qianrensouyiList = chongZhiManager.getQianRenShouyi(regdayStr, regdayStr, null, null, qudao, null);
  		    Long reg_LoginUserCount=playGameManager.getReg_LoginUserCount(regdayStr,regdayStr,qudao,null,null);//当天注册，在分区道登陆的用户
                 
  		
  		        long daymoney=0L;
					for(int i = 0 ; i < qianrensouyiList.size() ; i++){
					String[] qianrensy=(String[])qianrensouyiList.get(i);
					 String date=  qianrensy[0];
					 String money=  qianrensy[3];
					
					 
					 long daynum=0L;
					  if(regdayStr!=null&&date!=null){
				       Date date1 = new SimpleDateFormat("yyyy-MM-dd").parse(regdayStr); 
	                   Date date2 = new SimpleDateFormat("yyyy-MM-dd").parse(date); 
	                //日期相减得到相差的日期 
	                   daynum = (date1.getTime()-date2.getTime())/(24*60*60*1000)>0 ? (date1.getTime()-date2.getTime())/(24*60*60*1000):
	                            (date2.getTime()-date1.getTime())/(24*60*60*1000); 
	                            }
					if(money!=null){daymoney+=Long.parseLong(money);  }
				    
				    if(daynum<50){
				    if(reg_LoginUserCount!=0){
				   long avg= daymoney/reg_LoginUserCount;
				    
				         channel_regday_nums[regday][(int)daynum]=avg;
				         
				     }
				      } 
					}
  	               }
  	
  	
  	
  		                 for (int regdaynum = 0; regdaynum < realChannelList.size(); regdaynum++) {
		    				for (int daynum = 0; daynum < daysList.size(); daynum++) {
		    				if(daynum!=0&&channel_regday_nums[regdaynum][daynum]==0){
		    					channel_regday_nums[regdaynum][daynum]=channel_regday_nums[regdaynum][daynum-1];
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
var mycars=[<%for (int i = 0; i < daysList.size(); i++) {
				out.print("\"" + daysList.get(i) + "\",");
			}%>"E"];
       function drawRegUserFlotr(){
		    var f = Flotr.draw(
		$('regUserContainer'), [
		<%for (int j = 0; j < realChannelList.size(); j++) {
				StringBuffer sb2 = new StringBuffer();
				sb2.append("[");
				for (int kk = 0; kk < channel_regday_nums[j].length; kk++) {
					sb2.append("[" + kk + "," + channel_regday_nums[j][kk] + "]");
					if (kk < channel_regday_nums[j].length - 1) sb2.append(",");
				}
				sb2.append("]");
				out.println("{");
				out.println("data:" + sb2.toString() + ",");
				out.println("label:'" + realChannelList.get(j) + "'");
				out.println("}");
				if (j < realChannelList.size() - 1) out.print(",");
			}%>
		],{
			xaxis:{
				noTicks: <%=daysList.size()%>,	// Display 7 ticks.	
				tickFormatter: function(n){ return mycars[Math.floor(n)];}, // =>
				min: 0,
				max: <%=daysList.size()%>
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
			K日收益
		</h2>
		<form  method="post">
		注册起始日期：<input type='text' name='day' value='<%=startDay%>'>
		-- 注册结束日期：<input type='text' name='endDay' value='<%=endDay%>'>(格式：2012-02-09)
		<br>
		<br/><br/>
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
                </select>
            	<input type='submit' value='提交'></form>
		    		<br/>
		    		     
		    		    <a href="chongzhi_info_page.jsp">充值查询—分页</a>|
		    		     <a href="chongzhi_info.jsp">充值查询-不分页</a>|
		    		     <a href="chongzhi_fenbu.jsp">  充值等级分布</a>|
		    		     <a href="chongzhi_money_fenbu.jsp">充值金额区间分布</a>|
		    		     <a href="qianrensouyi_info.jsp">千人收益</a>|
		    		     <a href="Kri_qianrensouyi_info.jsp">K 日千人收益</a>|
		    		     <a href="Krisouyi_info.jsp">K 日收益</a>|
		    		     <a href="quDaoChongzhi_info.jsp">按渠道充值查询</a>|
		    		     <a href="fenQuChongzhi_info.jsp">按分区充值查询</a>|
		    		     <a href="chongzhi_jine_fenbu.jsp">充值金额分布</a>
		    		<br/>
		    		<h3>K日收益</h3>
		    		<%
		    			out.println("<table id='test2' align='center' width='90%' cellpadding='1' bgcolor='#000000' cellspacing='1' border='0'>");
		    			out.print("<tr bgcolor='#EEEEBB'><td>日期</td>");
		    			for (int daynum = 0; daynum < daysList.size(); daynum++) {
		    				out.print("<td>" + daynum + "</td>");
		    			}
		    			out.print("</tr>");

		    			for (int regdaynum = 0; regdaynum < realChannelList.size(); regdaynum++) {
		    				out.print("<tr  bgcolor='#FFFFFF'><td>" + realChannelList.get(regdaynum) + "</td>");
		    				for (int daynum = 0; daynum < daysList.size(); daynum++) {
		    					out.print("<td>" + channel_regday_nums[regdaynum][daynum] + "</td>");
		    				}
		    				out.print("</tr>");
		    			}

		    			out.println("</table><br>");
		    		%>
		 	
		      <div id="regUserContainer" style="width:100%;height:400px;display:block;"></div>
		          <script>
		          drawRegUserFlotr();
		          </script>
		</center>
		<br>
		<i>某日期内的注册用户，在注册以后的K日内收益的和除以注册日期当天的注册并进入用户数。</i>
		<br>
	</body>
</html>
