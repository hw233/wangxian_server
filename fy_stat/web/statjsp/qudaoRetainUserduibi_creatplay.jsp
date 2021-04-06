<%@ page contentType="text/html;charset=UTF-8"%>
<%@include file="../inc.jsp"%>
<%@ page
	import="com.xuanzhi.tools.cache.diskcache.concrete.DefaultDiskCache,org.slf4j.*,java.io.*,java.util.*,com.xuanzhi.tools.text.*,
	com.xuanzhi.tools.text.*,org.springframework.context.ApplicationContext,org.springframework.web.context.support.WebApplicationContextUtils,
	com.sqage.stat.commonstat.dao.UserDao,com.sqage.stat.commonstat.entity.User,com.sqage.stat.commonstat.manager.UserManager,
	com.sqage.stat.commonstat.manager.Impl.UserManagerImpl,com.sqage.stat.commonstat.manager.Impl.PlayGameManagerImpl,
	java.math.BigDecimal,java.math.MathContext"
	%>
	<%!Object lock = new Object() {
	};
	ApplicationContext ctx = null;%>
  <%
  	String daynum = request.getParameter("daynum");
  	if (daynum == null) {
  		daynum = "1";
  	}
  	String startDay = request.getParameter("day");
  	String endDay = request.getParameter("endDay");
  	String fenqu=request.getParameter("fenqu");
	if("0".equals(fenqu)){fenqu=null;}
	String jixing=request.getParameter("jixing");
	if("0".equals(jixing)){jixing=null;}

  	String today = DateUtil.formatDate(new Date(), "yyyy-MM-dd");
  	if (startDay == null) startDay = today;
  	if (endDay == null) endDay = today;
  	Date t = DateUtil.parseDate(endDay, "yyyy-MM-dd");
  	Date s = DateUtil.parseDate(startDay, "yyyy-MM-dd");
  	ArrayList<String> dayList = new ArrayList<String>();

  	/**
  	 *获得渠道信息
  	 **/
  	ArrayList<String> channelList = null;
  	if (ctx == null) {
  		ctx = WebApplicationContextUtils.getRequiredWebApplicationContext(this.getServletContext());
  	}
  	UserManagerImpl userManager = (UserManagerImpl) ctx.getBean("UserManager");
  	PlayGameManagerImpl playGameManager = (PlayGameManagerImpl) ctx.getBean("PlayGameManager");
  	channelList = userManager.getQudao(null);//获得现有的渠道信息
  	ArrayList<String []> fenQuList= userManager.getFenQu(null);//获得现有的分区信息
            
  	synchronized (lock) {
  		while (s.getTime() <= t.getTime() + 3600000) {
  			String day = DateUtil.formatDate(s, "yyyy-MM-dd");
  			dayList.add(day);
  			s.setTime(s.getTime() + 24 * 3600 * 1000);
  		}
  	}
  	//对比各个渠道数据
  	ArrayList<String> realChannelList = new ArrayList<String>();
  	//某个渠道，注册日期
  	if (request.getParameter("filterChannel") == null) {
  		//realChannelList=channelList;
  	} else {
  		String ss[] = request.getParameterValues("filterChannel");
  		for (int i = 0; i < ss.length; i++) {
  			if (ss[i].trim().length() > 0) {
  				realChannelList.add(ss[i].trim());
  			}
  		}
  	}
  	String dateStr[] = daynum.split(",");
  	String channel_regday_nums[][][] = new String[dateStr.length][realChannelList.size()][dayList.size()];
  	String channel_regday_nums_tu[][][] = new String[dateStr.length][realChannelList.size()][dayList.size()];
  	String[][] zonghe=new String[dateStr.length][dayList.size()];//存放统计日期内 每天总的留存率
  	
  	for (int tianshu = 0; tianshu < dateStr.length; tianshu++) {
  		for (int k = 0; k < dayList.size(); k++) {
  		
  		BigDecimal firstdayNum=new BigDecimal("0");//注册日期当日留存人数
  		BigDecimal dayNum=new BigDecimal("0");//指定日期留存人数
  		
  			String _day = dayList.get(k);

  			ArrayList<String> qudaoshuList = null;
  			ArrayList<String> qudaoshuList_tu = null;
  			ArrayList<String> qudaoshuList_1 = null;
  			ArrayList<String> qudaoshuList_2 = null;
  			qudaoshuList = new ArrayList<String>();
  			qudaoshuList_tu = new ArrayList<String>();

  			Long tt = (DateUtil.parseDate(_day, "yyyy-MM-dd").getTime()) + Long.parseLong(dateStr[tianshu]) * 24 * 3600 * 1000;
  			Date targetdate = new Date(tt);

  			String dateStr2 = DateUtil.formatDate(targetdate, "yyyy-MM-dd");//留存率统计日
  			int teger = 0;
  			if (dateStr2.compareTo(DateUtil.formatDate(new Date(), "yyyy-MM-dd")) < 0) {
  				qudaoshuList_1 = playGameManager.getQuDaoRetainUserCount_createplayer(_day, _day,fenqu,jixing);//注册当日留存
  				qudaoshuList_2 = playGameManager.getQuDaoRetainUserCount_createplayer(_day, dateStr2,fenqu,jixing);//指定日期留存
  				for (int j = 0; j < realChannelList.size(); j++) {
  					String _channel = realChannelList.get(j);
  					for (String qudaoshu_1 : qudaoshuList_1) {
  						String[] detail_1 = qudaoshu_1.split(" ");
  						for (String qudaoshu_2 : qudaoshuList_2) {
  							String[] detail_2 = qudaoshu_2.split(" ");
  							if (detail_1[0].equals(detail_2[0])) {
  								if (!"0".equals(detail_1[1])) {
  									BigDecimal b1 = new BigDecimal(detail_2[1]);
  									BigDecimal b2 = new BigDecimal(detail_1[1]);

                                
  									BigDecimal b100 = new BigDecimal("100");
  									BigDecimal b11 = b100.multiply(b1);
  									BigDecimal b = b11.divide(b2, new MathContext(3));
  									BigDecimal b_tu = b1.divide(b2, new MathContext(3));
  								
                               firstdayNum=firstdayNum.add(b11);
                               dayNum=dayNum.add(b2);
  									qudaoshuList.add(detail_1[0] + " " + b + "%");
  									qudaoshuList_tu.add(detail_1[0] + " " + b_tu);
  								} else {
  									qudaoshuList.add(detail_1[0] + " 0%");
  									qudaoshuList_tu.add(detail_1[0] + " 0");
  								}
  							}
  						}
  					}

  					for (int l = 0; l < qudaoshuList.size(); l++) {
  						String ss[] = qudaoshuList.get(l).split(" ");
  						if (ss[0].equals(_channel)) {
  							if (ss[1] != null) {
  								channel_regday_nums[tianshu][j][k] = ss[1];
  							}
  						}
  					}

  					for (int l = 0; l < qudaoshuList_tu.size(); l++) {
  						String ss_tu[] = qudaoshuList_tu.get(l).split(" ");
  						if (ss_tu[0].equals(_channel)) {
  							if (ss_tu[1] != null) {
  								channel_regday_nums_tu[tianshu][j][k] = ss_tu[1];
  							}
  						}
  					}
  				}
  			}
  			if(firstdayNum.compareTo(new BigDecimal("0"))!=0){
  			BigDecimal bDay = firstdayNum.divide(dayNum, new MathContext(3));
  			zonghe[tianshu][k]=bDay.toString()+"";
  			}else
  			{
  			zonghe[tianshu][k]="0";
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

   function quanxuanabd(){
       var m = document.getElementsByName('filterChannel');
        var l = m.length;
        for ( var i=0; i< l; i++)
        {
            m[i].checked=true;
         }
      }
      function buxuanabd(){
        var m = document.getElementsByName('filterChannel');
        var l = m.length;
        for ( var i=0; i< l; i++)
        {
            m[i].checked=false;
         }
         }



var mycars=[<%for (int i = 0; i < dayList.size(); i++) {
				out.print("\"" + dayList.get(i) + "\",");
			}%>"E"];
<%for (int tianshu_ = 0; tianshu_ < dateStr.length; tianshu_++) {%>
 function drawRegUserFlotr_<%=tianshu_%>(){
		    var f = Flotr.draw(
		$('regUserContainer_<%=tianshu_%>'), [
		<%for (int j = 0; j < realChannelList.size(); j++) {
					StringBuffer sb2 = new StringBuffer();
					sb2.append("[");
					for (int k = 0; k < channel_regday_nums_tu[tianshu_][j].length; k++) {
						sb2.append("[" + k + "," + channel_regday_nums_tu[tianshu_][j][k] + "]");
						if (k < channel_regday_nums_tu[tianshu_][j].length - 1) sb2.append(",");
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
	
	//zonghe[tianshu_][i]
	 function drawZongShuFlotr_<%=tianshu_%>(){
		    var f = Flotr.draw(
		$('regZongShuContainer_<%=tianshu_%>'), [
		<%//for (int j = 0; j < realChannelList.size(); j++) {
					StringBuffer sb2 = new StringBuffer();
					sb2.append("[");
					for (int k = 0; k <zonghe[tianshu_].length; k++) {
						sb2.append("[" + k + "," + Float.parseFloat(zonghe[tianshu_][k])/100 + "]");
						if (k < zonghe[tianshu_].length - 1) sb2.append(",");
					}
					sb2.append("]");
					out.println("{");
					out.println("data:" + sb2.toString() + ",");
					out.println("label:'" + "总留存率" + "'");
					out.println("}");
					//if (j < realChannelList.size() - 1) out.print(",");
				//}
				%>
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
			<%=startDay%>各渠道注册并进入玩家K日留存对比
		</h2>
		<form method="post">开始日期：<input type='text' name='day' value='<%=startDay%>'>
		-- 结束日期：<input type='text' name='endDay' value='<%=endDay%>'>(格式：2012-02-09) 
		
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
                  </select>&nbsp;&nbsp;
		    <br/>
		             过滤渠道：
		     <table width="500"><tr>
		    		<%
		    			for (int i = 0; i < channelList.size(); i++) {
		    				if (i > 0 && i % 9 == 0) {
		    					out.print("</tr><tr>");
		    				}
		    				String _channel = channelList.get(i);
		    				if (realChannelList.contains(_channel)) {
		    					out.print("<td align='left'><input type='checkbox' name='filterChannel' value='" + _channel + "' checked >" + _channel
		    							+ "&nbsp;</td>");
		    				} else {
		    					out.print("<td  align='left'><input type='checkbox' name='filterChannel' value='" + _channel + "'>" + _channel + "&nbsp;</td>");
		    				}
		    			}
		    		%>
		    		</tr>
		    		</table>
		    		 <input type="button" name="quanxuanab" onclick="quanxuanabd();" value="全选" >
		    		 <input type="button" name="buxuanab" onClick="buxuanabd();" value="全不选" >
		    		<br/>
		    		输入留存天数：<input type='text' name='daynum' value='<%=daynum%>'>
		    		
		    		<br/>
		    		<input type='submit' value='提交'></form>
		    		<i>如果查询所有分区，安装用户名统计；如果查询单个分区，按这个分区的角色名统计。</i>
		    		
		    		<%
		    			for (int tianshu_ = 0; tianshu_ < dateStr.length; tianshu_++) {
		    				    				    		%>
		    		<h3>各渠道注册并进入玩家<%=dateStr[tianshu_]%>日留存对比</h3>
		    		<%
		    			out.println("<table id='test2' align='center' width='90%' cellpadding='1' bgcolor='#000000' cellspacing='1' border='0'>");
		    				out.print("<tr bgcolor='#EEEEBB'><td>日期</td>");
		    				out.print("<td>总和</td>");
		    				for (int c = 0; c < realChannelList.size(); c++) {
		    					out.print("<td>" + realChannelList.get(c) + "</td>");
		    				}
		    				out.println("</tr>");
		    				for (int i = 0; i < dayList.size(); i++) {
		    					out.print("<tr bgcolor='#FFFFFF'><td>" + dayList.get(i) + "</td>");
		    					out.print("<td>" + zonghe[tianshu_][i] + "% </td>");
		    					for (int c = 0; c < realChannelList.size(); c++) {
		    						out.print("<td>" + channel_regday_nums[tianshu_][c][i] + "</td>");
		    					}
		    					out.println("</tr>");
		    				}
		    				out.println("</table><br>");
		    		%>
		    		
		    		<div id="regUserContainer_<%=tianshu_%>" style="width:100%;height:400px;display:block;"></div>
		             <script>
		              drawRegUserFlotr_<%=tianshu_%>();
		            </script>
		            <h3>注册并进入玩家<%=dateStr[tianshu_]%>日总留存率</h3><br>
		            <div id="regZongShuContainer_<%=tianshu_%>" style="width:100%;height:400px;display:block;"></div>
		             <script>
		              drawZongShuFlotr_<%=tianshu_%>();
		            </script>
		             <%
		             	}
		             %>
		</center>
		<br>
		<i>在指定日期注册的用户，第K日进入游戏的比例</i>
		<br>
	</body>
</html>
