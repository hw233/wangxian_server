<%@ page contentType="text/html;charset=UTF-8"%>
<%@include file="../inc.jsp"%>
<%@ page
	import="com.xuanzhi.tools.cache.diskcache.concrete.DefaultDiskCache,org.slf4j.*,java.io.*,java.util.*,
	com.xuanzhi.tools.text.*,com.sqage.stat.commonstat.entity.ChongZhi,com.xuanzhi.tools.text.*,
	com.sqage.stat.commonstat.dao.UserDao,com.sqage.stat.commonstat.entity.User,com.sqage.stat.commonstat.manager.UserManager,
	com.sqage.stat.commonstat.manager.Impl.*,com.sqage.stat.service.*,com.sqage.stat.model.Channel"
	%>

  <%
	String startDay = request.getParameter("day");
	String endDay = request.getParameter("endDay");
	String statstartDay = request.getParameter("statday");
	String statendDay = request.getParameter("statendDay");
	String qudao=request.getParameter("qudao");
	List<Channel> channelList = (ArrayList<Channel>)session.getAttribute("QUDAO_LIST");

	if("0".equals(qudao)){qudao=null;}
	
	String today = DateUtil.formatDate(new Date(),"yyyy-MM-dd");
	if(startDay == null) startDay = today;
	if(endDay == null) endDay = today;
	if(statstartDay == null) statstartDay = today;
	if(statendDay == null) statendDay = today;
	/**
	*获得渠道信息
	**/
		      
              UserManagerImpl userManager=UserManagerImpl.getInstance();
              ChongZhiManagerImpl chongZhiManager=ChongZhiManagerImpl.getInstance();

               if(channelList==null || channelList.size() == 0)
	           { 
	             ChannelManager cmanager = ChannelManager.getInstance();
		         channelList = cmanager.getChannels();//渠道信息
		         session.removeAttribute("QUDAO_LIST");
		         session.setAttribute("QUDAO_LIST", channelList);
	           }
              
              String money_search=null;
              String username_search=null;
		     List qianrensouyiList=chongZhiManager.getQianRenShouyi(startDay,endDay,statstartDay,statendDay,qudao,null);
             Long registUserNum=   userManager.getRegistUerCount(startDay,endDay,qudao,null,null);
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
		<h2 style="font-weight:bold;">
			千人收益
		</h2>
		<form  method="post">
		注册起始日期：<input type='text' name='day' value='<%=startDay %>'>
		-- 注册结束日期：<input type='text' name='endDay' value='<%=endDay %>'>(格式：2012-02-09)
		<br>统计起始日期：<input type='text' name='statday' value='<%=statstartDay %>'>
		-- 统计结束日期：<input type='text' name='statendDay' value='<%=statendDay %>'>(格式：2012-02-09)
		  	
		
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
		    		<h3>千人收益</h3>
		    		<%
		    		out.println("<table id='test2' align='center' width='90%' cellpadding='1' bgcolor='#000000' cellspacing='1' border='0'>");
					out.print("<tr bgcolor='#EEEEBB'><td>日期</td><td>注册人数</td><td>付费情况</td><td>独立付费人数</td><td>千人收益</td><td>距离注册天数</td></tr>");
					Long summomey=0L;
					for(int i = 0 ; i < qianrensouyiList.size() ; i++){
					String[] qianrensy=(String[])qianrensouyiList.get(i);
					Long money=0L;
					Long qianrenshouyi=0L;
					if(qianrensy[3]!=null&&!"".equals(qianrensy[3]))
					     {   money=Long.parseLong(qianrensy[3]); 
					         summomey+=money;
					        if(registUserNum!=0){ qianrenshouyi= money*1000/registUserNum; }
					        }
					
						out.print("<tr bgcolor='#FFFFFF'><td>"+qianrensy[0]+"</td><td>"+registUserNum+"</td><td>"+qianrensy[3]+"</td>"+
						                                "<td>"+qianrensy[2]+"</td><td>"+qianrenshouyi+"</td><td>"+qianrensy[4]+"</td></tr>");
					}
					Long sumqianrenshouyi=0L;
					if(registUserNum!=0){ sumqianrenshouyi= summomey*1000/registUserNum; }
					
						out.print("<tr bgcolor='#FFFFFF'><td>总计</td><td>"+registUserNum+"</td><td>"+summomey+"</td><td>&nbsp;</td><td>"+sumqianrenshouyi+"</td><td>&nbsp;</td>");
						out.println("</tr>");
					out.println("</table><br>");
		    		%>
		    		<font color="brown"><i>注：千人收益是以用户的注册渠道为统计依托。而其它的充值统计在没有特殊说明的情况下，是以</i><br>
		    		<i>玩家的充值渠道为依托。因此千人收益查出的充值金额和其它查询方式的充值金额不能拿来比较。</i></font>
		    		<br><br><br>
		    		<font color="blue">
		    		<i>注册渠道：玩家注册账号时的渠道，无论玩家是否更换了渠道的游戏包，玩家的注册渠道不变!</i><br>
		    		<i>进入渠道：玩家进入游戏当时使用的渠道。如果玩家更换了渠道的游戏包，进入渠道是会变化的!</i><br>
		    		<i>充值渠道：玩家充值当时使用的渠道。如果玩家更换了渠道的游戏包，充值渠道随之变化的!</i><br></font>
		    		<div id="regUserContainer" style="width:100%;height:400px;display:block;"></div>
		 
		</center>
	</body>
</html>
