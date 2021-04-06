<%@ page contentType="text/html;charset=UTF-8"%>
<%@include file="../inc.jsp"%>
<%@ page
	import="com.xuanzhi.tools.cache.diskcache.concrete.DefaultDiskCache,org.slf4j.*,java.io.*,java.util.*,
	com.xuanzhi.tools.text.*,org.springframework.context.ApplicationContext,
	org.springframework.web.context.support.WebApplicationContextUtils,
	com.sqage.stat.commonstat.dao.UserDao,com.sqage.stat.commonstat.entity.*,
	com.sqage.stat.commonstat.manager.*,com.sqage.stat.commonstat.manager.Impl.UserManagerImpl,
	com.sqage.stat.commonstat.manager.Impl.*,com.sqage.stat.service.*,com.sqage.stat.model.Channel"
	%>
     <%
     	String flag = request.getParameter("flag");
     	String startDay = request.getParameter("day");
     	String endDay = request.getParameter("endDay");
     	String statstartDay = request.getParameter("statday");
     	String statendDay = request.getParameter("statendDay");
     	String qudao = request.getParameter("qudao");
     	String displayType = request.getParameter("displaytype");
     	String jixing = request.getParameter("jixing");

     	if ("0".equals(jixing)) {
     		jixing = null;
     	}
     	if ("0".equals(qudao)) {
     		qudao = null;
     	}
     	if (displayType == null) {
     		displayType = "2";
     	}

     	String today = DateUtil.formatDate(new Date(new Date().getTime() - 24 * 60 * 60 * 1000), "yyyy-MM-dd");
     	if (startDay == null) startDay = today;
     	if (endDay == null) endDay = today;
     	if (statstartDay == null) statstartDay = today;
     	if (statendDay == null) statendDay = today;

     	ArrayList<String> dayList = new ArrayList<String>();

     	java.util.Date regstartdate = DateUtil.parseDate(startDay + " 00:00:01", "yyyy-MM-dd HH:mm:ss");
     	java.util.Date regenddate = DateUtil.parseDate(endDay + " 00:00:01", "yyyy-MM-dd HH:mm:ss");
     	java.util.Date statstartdate = DateUtil.parseDate(statstartDay + " 00:00:01", "yyyy-MM-dd HH:mm:ss");
     	java.util.Date statenddate = DateUtil.parseDate(statendDay + " 00:00:01", "yyyy-MM-dd HH:mm:ss");

     	List<Date[]> regweeks = DateUtil.splitTime(regstartdate, regenddate, Integer.parseInt(displayType));
     	List<Date[]> statweeks = DateUtil.splitTime(statstartdate, statenddate, Integer.parseInt(displayType));

     	ChannelManager cmanager = ChannelManager.getInstance();
     	UserManagerImpl userManager = UserManagerImpl.getInstance();
     	List<Channel> channelList = cmanager.getChannels();//渠道信息
     	List<String[]> ls = new ArrayList<String[]>();
     	String data[][] = new String[regweeks.size() + 1][statweeks.size() + 2];
     	
     	if ("true".equals(flag)) {
     		for (int i = 0; i < regweeks.size(); i++) {
     			data[i+1][0] = DateUtil.formatDate(regweeks.get(i)[0], "yyyy-MM-dd") +"到"+ DateUtil.formatDate(regweeks.get(i)[1], "yyyy-MM-dd"); //注册日期
     		    data[i+1][1]=userManager.getRegistUerCount(DateUtil.formatDate(regweeks.get(i)[0], "yyyy-MM-dd"),DateUtil.formatDate(regweeks.get(i)[1], "yyyy-MM-dd"),qudao,null,jixing).toString();
     		}
     		data[0][0] = "注册日期/充值日期";
     		data[0][1] = "注册人数";
     		for (int j = 0; j < statweeks.size(); j++) {
     			data[0][j+2] = DateUtil.formatDate(statweeks.get(j)[0], "yyyy-MM-dd") + "到"+DateUtil.formatDate(statweeks.get(j)[1], "yyyy-MM-dd"); //注册日期
     		   	}

     		for (int i1 = 0; i1 < regweeks.size(); i1++) {
     		   Date[] dreg=regweeks.get(i1);
     		    ls = userManager.getZhuCeUserChongzhi_Mon(DateUtil.formatDate(dreg[0], "yyyy-MM-dd"), DateUtil.formatDate(dreg[1],"yyyy-MM-dd"),
                      statstartDay, statendDay, qudao, displayType, jixing);
     			for (int j2 = 0; j2 < statweeks.size(); j2++) {
     			
     			   for(int count=0;count<ls.size();count++){
     			     if(DateUtil.formatDate(statweeks.get(j2)[0],"yyyy-MM-dd").indexOf(ls.get(count)[1])!=-1){
     			        data[i1+1][j2+2]=ls.get(count)[0];
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

	</head>
	 <body>
		<center>
		<h2 style="font-weight:bold;">注册用户充值统计</h2>
		<form  method="post">
		  <input type='hidden' name='flag' value='true'/>
		              注册开始日期：<input type='text' name='day' value='<%=startDay%>'>
		    -- 注册结束日期：<input type='text' name='endDay' value='<%=endDay%>'>(格式：2012-02-09)
		    
		   <br>统计起始日期：<input type='text' name='statday' value='<%=statstartDay%>'>
		   -- 统计结束日期：<input type='text' name='statendDay' value='<%=statendDay%>'>(格式：2012-02-09)
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
                                            展示方式：
                   <input type="radio" <%if (displayType == null || "0".equals(displayType)) {
				out.print("checked");
			}%>  name="displaytype" value="0" />按天
                   <!--  <input type="radio" <%if ("1".equals(displayType)) {
				out.print("checked");
			}%>  name="displaytype" value="1" />按周-->
                   <input type="radio" <%if ("2".equals(displayType)) {
				out.print("checked");
			}%>  name="displaytype" value="2" />按月
		    	<input type='submit' value='提交'></form>
		    		
		    		<h3>注册用户充值</h3>
		    		<%
		    			out.println("<table id='test2' align='center' width='90%' cellpadding='1' bgcolor='#000000' cellspacing='1' border='0'>");
		    			if ("true".equals(flag)) {      //如果是刚打开页面，不查询){

                         long[] sumreguser  = new long [statweeks.size() + 2];
                         long[] sumstatmoney= new long [regweeks.size()+1];
                         for (int ii1 = 1; ii1 < regweeks.size() + 1; ii1++) {
     			         for (int jj2 =1; jj2 < statweeks.size() + 2; jj2++) {
     			         if(data[ii1][jj2]!=null){ 
     			                    sumreguser[jj2]  +=Long.parseLong(data[ii1][jj2]);
     			                    sumstatmoney[ii1]+=Long.parseLong(data[ii1][jj2]);
     			                   }
     			         }
     			         }





		    		   for (int ii1 = 0; ii1 < regweeks.size() + 1; ii1++) {
		    		   if(ii1==0) {out.print("<tr bgcolor='#EEEEBB'>");} else{out.print("<tr bgcolor='#FFFFFF'>");}
     			        
     			         for (int jj2 = 0; jj2 < statweeks.size() + 2; jj2++) {
     			         if(data[ii1][jj2]==null){ 
     			                    out.print("<td>&nbsp;</td>");
     			                     }else { out.print("<td>"+data[ii1][jj2]+"</td>"); }
     			         }
     			          out.print("</tr>");
     			         }
		    			//////////汇总start//////////
     			          out.print("<tr bgcolor='#EEEEBB'>");
     			          for (int jj3 = 0; jj3 < statweeks.size() + 2; jj3++) {
     			          if(jj3 == 0){out.print("<td>汇总</td>"); }
     			               else{ out.print("<td>"+sumreguser[jj3]+"</td>"); }
     			           }
     			            out.print("</tr>");
     			            //////////汇总end//////////
		    			}
		    			out.println("</table><br>");
		    		%>
		    		<font color="brown"><i>注：注册用户充值统计是以用户的注册渠道为统计依托。而其它的充值统计在没有特殊说明的情况下，是以</i><br>
		    		<i>玩家的充值渠道为依托。因此注册用户充值统计查出的充值金额和其它查询方式的充值金额不能拿来比对。</i></font>
		    		<br><br><br>
		    		<font color="blue">
		    		<i>注册渠道：玩家注册账号时的渠道，无论玩家是否更换不同渠道的游戏包，玩家的注册渠道不变!</i><br>
		    		<i>进入渠道：玩家进入游戏当时使用的渠道。如果玩家更换不渠道的游戏包，进入渠道时会变化的!</i><br>
		    		<i>充值渠道：玩家进入游戏当时使用的渠道。如果玩家更换不渠道的游戏包，进入渠道时会变化的!</i><br></font>
		    		<div id="regUserContainer" style="width:100%;height:400px;display:block;"></div>
		    </center>
		<br>
		<i>注册用户，在注册后的某个周期内的充值情况。</i>
		<br>
	</body>
</html>
