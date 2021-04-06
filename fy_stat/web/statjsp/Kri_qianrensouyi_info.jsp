<%@ page contentType="text/html;charset=UTF-8"%>
<%@include file="../inc.jsp"%>
<%@ page
	import="com.xuanzhi.tools.cache.diskcache.concrete.DefaultDiskCache,org.slf4j.*,java.io.*,java.util.*,
	com.xuanzhi.tools.text.*,com.xuanzhi.tools.text.*,com.sqage.stat.commonstat.entity.ChongZhi,
	com.sqage.stat.commonstat.dao.UserDao,com.sqage.stat.commonstat.entity.User,com.sqage.stat.commonstat.manager.UserManager,
	com.sqage.stat.commonstat.manager.Impl.UserManagerImpl,com.sqage.stat.commonstat.manager.Impl.*,com.sqage.stat.service.*,
	com.sqage.stat.model.Channel"
	%>
	<%!Object lock = new Object() {
	};%>
  <%
  	String startDay = request.getParameter("day");
  	String endDay = request.getParameter("endDay");
  	String qudao = request.getParameter("qudao");
  	String daynum = request.getParameter("daynum");
  	if (daynum == null) {
  		daynum = "0";
  	}
  	String dateStr[] = daynum.split(",");

  	if ("0".equals(qudao)) {
  		qudao = null;
  	}
  	String today = DateUtil.formatDate(new Date(), "yyyy-MM-dd");
  	if (startDay == null) startDay = today;
  	if (endDay == null) endDay = today;

  	Date t = DateUtil.parseDate(endDay, "yyyy-MM-dd");
  	Date s = DateUtil.parseDate(startDay, "yyyy-MM-dd");
  	ArrayList<String> dayList = new ArrayList<String>();
  //	int daycount = Integer.parseInt(daynum);

  	synchronized (lock) {
  		while (s.getTime() <= t.getTime() + 3600000) {
  			String day = DateUtil.formatDate(s, "yyyy-MM-dd");
  			dayList.add(day);
  			s.setTime(s.getTime() + 24 * 3600 * 1000);
  			//if (s.getTime() + (daycount + 1) * 24 * 3600 * 1000 > System.currentTimeMillis()) {
  			//	break;
  			//}
  		}
  	}

  	String[][] zonghe = new String[dateStr.length][dayList.size()];//存放统计日期内 每天总的留存率
  	/**
  	 *获得渠道信息
  	 **/
  	ChannelManager cmanager = ChannelManager.getInstance();
  	UserManagerImpl userManager = UserManagerImpl.getInstance();
  	ChongZhiManagerImpl chongZhiManager = ChongZhiManagerImpl.getInstance();

  	List<Channel> channelList = cmanager.getChannels();//渠道信息
  	List[] qianrensouyiListList=new List[dateStr.length];
  	
  	for (int tianshu_ = 0; tianshu_ < dateStr.length; tianshu_++) {
  	String d=dateStr[tianshu_];
  	List qianrensouyiList = chongZhiManager.getKRi_QianRenShouyi(startDay, endDay, d, qudao);
  	qianrensouyiListList[tianshu_]=qianrensouyiList;
  	for (int ii = 0; ii < dayList.size(); ii++) {
  		if (qianrensouyiList.size() > 0 && ii<qianrensouyiList.size()&& qianrensouyiList.get(ii) != null) {
  			String[] souyi = (String[]) qianrensouyiList.get(ii);
  			String moeny = souyi[3];
  			String registUsercount = souyi[1];
  			if (moeny != null && registUsercount != null && !"0".equals(registUsercount)) {
  				Long ss = (Long.parseLong(moeny) * 1000) / (Long.parseLong(registUsercount));
  				zonghe[tianshu_][ii] = ss.toString();
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

var mycars=[<%for (int i = 0; i < dayList.size(); i++) {
				out.print("\"" + dayList.get(i) + "\",");
			}%>"E"];
<%for (int tianshu_ = 0; tianshu_ < dateStr.length; tianshu_++) {%>
 
	 function drawZongShuFlotr_<%=tianshu_%>(){
		    var f = Flotr.draw(
		$('regZongShuContainer_<%=tianshu_%>'), [
		<%StringBuffer sb2 = new StringBuffer();
				sb2.append("[");
				for (int k = 0; k < zonghe[tianshu_].length; k++) {
					sb2.append("[" + k + "," + zonghe[tianshu_][k] + "]");
					if (k < zonghe[tianshu_].length - 1) sb2.append(",");
				}
				sb2.append("]");
				out.println("{");
				out.println("data:" + sb2.toString() + ",");
				out.println("label:'" +dateStr[tianshu_]+ "日千人收益" + "'");
				out.println("}");%>
		],{
			xaxis:{
				noTicks: <%=dayList.size()%>,	// Display 7 ticks.	
				tickFormatter: function(n){ return mycars[Math.floor(n)];}, // =>
				min: 0,
				max: <%=dayList.size()%>
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
	<%}%>
</script>
	</head>
	<body>
		<center>
		<h2 style="font-weight:bold;">
			K日千人收益
		</h2>
		<form  method="post">
		        注册起始日期：<input type='text' name='day' value='<%=startDay%>'>
		-- 注册结束日期：<input type='text' name='endDay' value='<%=endDay%>'>(格式：2012-01-01)
	
		<br/><br/>
		    		输入收益天数：<input type='text' name='daynum' value='<%=daynum%>'>(注册当天为第0天)
		    		
		    		<br/>
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
		    		<%
		    			for (int tianshu_ = 0; tianshu_ < dateStr.length; tianshu_++) {
		    			out.print(dateStr[tianshu_]+"<h3>日千人收益</h3>");
		    			
		    				out.println("<table id='test2' align='center' width='90%' cellpadding='1' bgcolor='#000000' cellspacing='1' border='0'>");
		    				out.print("<tr bgcolor='#EEEEBB'><td>注册日期</td><td>注册人数</td><td>付费情况</td><td>独立付费人数</td><td>千人收益</td><td>距离注册天数</td></tr>");
		    				for (int i = 0; i < qianrensouyiListList[tianshu_].size(); i++) {
		    					if (qianrensouyiListList[tianshu_].get(i) != null) {
		    						String[] qianrensy = (String[]) qianrensouyiListList[tianshu_].get(i);
		    						Long money = 0L;
		    						Long qianrenshouyi = 0L;
		    						if (qianrensy[3] != null && qianrensy[1] != null && !"".equals(qianrensy[1])) {
		    							money = Long.parseLong(qianrensy[3]);
		    							qianrenshouyi = money * 1000 / Long.parseLong(qianrensy[1]);
		    						}
                                    
                                    String date0="";
                                    String date4="";
                                    if(qianrensy[0]!=null){date0= qianrensy[0].substring(0, 10);}
                                    if(qianrensy[4]!=null){date4= qianrensy[4];}
                                    
		    						out.print("<tr bgcolor='#FFFFFF'><td>" + date0+ "</td><td>" + qianrensy[1] + "</td><td>"
		    								+ qianrensy[3] + "</td>" + "<td>" + qianrensy[2] + "</td><td>" + qianrenshouyi + "</td><td>"
		    								+ date4 + "</td></tr>");
		    					}
		    				}
		    				out.println("</table><br>");
		    		%>
		    		   <h3><%=dateStr[tianshu_]%>日千人收益</h3><br>
		            <div id="regZongShuContainer_<%=tianshu_%>" style="width:100%;height:400px;display:block;"></div>
		             <script>
		              drawZongShuFlotr_<%=tianshu_%>();
		            </script>
		            <%
		            	}
		            %>
		</center>
		<br>
		<i>这里的用户都是指进入游戏的用户</i>
		<br>
	</body>
</html>
