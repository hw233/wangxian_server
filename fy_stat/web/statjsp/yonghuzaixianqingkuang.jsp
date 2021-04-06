<%@page import="org.apache.commons.lang.time.DateUtils"%>
<%@page import="java.text.DateFormat"%>
<%@ page contentType="text/html;charset=UTF-8"%>
<%@include file="../inc.jsp"%>
<%@ page
	import="com.xuanzhi.tools.cache.diskcache.concrete.DefaultDiskCache,org.slf4j.*,java.io.*,java.util.*,
	com.xuanzhi.tools.text.*,com.sqage.stat.commonstat.entity.ChongZhi,com.xuanzhi.tools.text.*,
	com.sqage.stat.commonstat.dao.UserDao,com.sqage.stat.commonstat.entity.User,com.sqage.stat.commonstat.manager.UserManager,
    com.sqage.stat.commonstat.manager.Impl.*,com.sqage.stat.model.Channel"
	%>
	<%!
	Object lock = new Object(){};
      %>
  <%
	String startDay = request.getParameter("day");
	String fenqu=request.getParameter("fenqu");
	String qudao=request.getParameter("qudao");
	
	if(fenqu != null && fenqu.trim().equals("0")){ fenqu = null; }
	if(qudao != null && qudao.trim().equals("0")){ qudao = null; }
	
	if(startDay == null) {
		startDay = DateUtil.formatDate(new Date(), "yyyy-MM-dd");	}
		
	ChannelManager cmanager = ChannelManager.getInstance();
    UserManagerImpl userManager=UserManagerImpl.getInstance();
    DaoJuManagerImpl daoJuManager=DaoJuManagerImpl.getInstance();
	//ArrayList<String> quDaoList=null;
	//quDaoList = userManager.getQudao(null);//获得现有的渠道信息
	List<Channel> channelList = (ArrayList<Channel>)session.getAttribute("QUDAO_LIST");
	ArrayList<String []> fenQuList  =  (ArrayList<String []>) session.getAttribute("FENQU_LIST");
	if(channelList==null || channelList.size() == 0)
	           {
		         channelList = cmanager.getChannels();//渠道信息
		         session.removeAttribute("QUDAO_LIST");
		         session.setAttribute("QUDAO_LIST", channelList);
	           }
	         if(fenQuList==null)
	           {
		         fenQuList= userManager.getFenQu(null);//获得现有的分区信息
		         session.removeAttribute("FENQU_LIST");
		         session.setAttribute("FENQU_LIST", fenQuList);
	           }
	//ArrayList<String []> fenQuList= userManager.getFenQu(null);//获得现有的分区信息
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

	</head>
	<body>
		<center>
		<h2 style="font-weight:bold;">
			用户在线情况
		</h2>
		<div style="clear: both;">
		<form  method="post">
		
		日期： <input type="text" name="day" id="day" class="nqz_input" style="width:100px;" readonly="readonly" onfocus="new Calendar(false,'yyyy-MM-dd').showTimePicker(this);" value="<%=startDay %>"/>
		
		<br/><br/>
                                             分区：<select name="fenqu">
                       <option  value="0">全部</option>
                       <% 
                       for(int i = 0 ; i < fenQuList.size() ; i++){
                       String[] _fenqu = fenQuList.get(i);
                       %>
                        <option value="<%=_fenqu[1] %>" 
                        <%
                        if(_fenqu[1].equals(fenqu)){ out.print(" selected=\"selected\"");    }
                         %>
                          ><%=_fenqu[1] %></option>
                       <%
                       }
                       %>
                </select>&nbsp;&nbsp;
                
               渠道：<select name="qudao">
                       <option  value="0">全部</option>
                                              <% 
                       for(int i = 0 ; i < channelList.size() ; i++){
                       String _qudao = channelList.get(i).getName();
                       %>
                        <option value="<%=_qudao %>" 
                        <%
                        if(_qudao.equals(qudao)){
                        out.print(" selected=\"selected\"");
                        }
                         %>
                          ><%=_qudao %></option>
                       <%
                       }
                       %>
                </select>&nbsp;&nbsp;
		    		<input type='submit' value='提交'></form>
		    		<%
			    		out.println("<table id='onlineinfo' align='center' width='90%' cellpadding='1' bgcolor='#000000' cellspacing='1' border='0'>");
						out.print("<tr bgcolor='#EEEEBB'><td></td><td>最高在线</td><td>平均在线</td></tr>");
						out.print("<tr bgcolor='#EEEEBB'><td>时间</td><td>每分钟记录一次在线人数,每小时60次，60次中最高1次为这一小时的最高在线</td><td>每分钟记录一次在线人数，每小时60次，60次在线人数总和除60次为这一小时平均在线</td></tr>");
						List<Object[]> onlineinfo = userManager.getMaxAndAvgUserNumByHoursWholeDay(DateUtil.parseDate(startDay, "yyyy-MM-dd"), fenqu, qudao);
						int maxNum = 0;
						int avgNum = 0;
						int sumMaxNum = 0;
						int sumAvgNum = 0;
						int maxMaxOnline = 0;
						int maxAvgOnline = 0;
						
						
						for(int i = 0 ; i < 24 ; i++)
						{
							if(i < onlineinfo.size())
							{
								maxNum = (Integer)onlineinfo.get(i)[1];
								avgNum = (Integer)onlineinfo.get(i)[2];
							}
							else
							{
								maxNum = 0;
								avgNum = 0;
							}
							
							sumMaxNum += maxNum;
							sumAvgNum += avgNum;
							
							if(maxMaxOnline < maxNum)maxMaxOnline = maxNum;
							if(maxAvgOnline < avgNum)maxAvgOnline = avgNum;
							
					%>
							<tr bgcolor=<%if((i%2) == 0){ %>'#FFFFFF'<%}else{%>'#EEEEBB'<% }%>><td><%=i%></td><td><%=maxNum%></td><td><%=avgNum%></td></tr>
					<% 	
						}
					%>
							<tr bgcolor='#EEEEBB'><td>总计</td><td><%=sumMaxNum%></td><td><%=sumAvgNum%></td></tr>
					<% 	
						out.println("</table><br>");
					%>	

		 
		</center>
		</div>
		<br>
		<br>
		<div style="clear:both;"></div>
		<div>
			<div id="userStatCanvas" style="width:100%;height:400px;display:block;"></div>
		</div>
	</body>
</html>
<script>
//Execute this when the page's finished loading
var f = Flotr.draw(
	$('userStatCanvas'), [
	{ // => first series
	    data: [ 
	           
	           <%
	           		for(int i=0; i<24;i++)
	           		{
	           			
	           			if(onlineinfo.size() != 0)
	           			{
		           			if(i == 0)
		           			{
	           	%>
	           					[<%=i%>,<%=(Integer)onlineinfo.get(i)[1]%>]
	           	<%
		           			}
		           			else if( i < onlineinfo.size())
		           			{
	           	%>
	           					,[<%=i%>,<%=(Integer)onlineinfo.get(i)[1]%>]
	           	<%
		           			}
		           			else
		           			{
	           	%>
	           					,[<%=i%>,0]
	           	<%
	           			
	           				}
	           			}
	           			else
	           			{
	           				if(i == 0)
	           				{
	           	%>
	           					[0,0]
	           			
	           	<%
	           				}
	           				else
	           				{
	           	%>
	           					,[0,0]
	           	<%
	           				}
	           			}
	           			
	           		}
	           %>
	           
	           ],
	    label: "最高在线",
	    lines: {show: true, fill: true},
	    points: {show: true}
	},
	{
    data: [ 
           
           <%
      		for(int i=0; i<24;i++)
      		{
      			
      			if(onlineinfo.size() != 0)
      			{
          			if(i == 0)
          			{
      	%>
      					[<%=i%>,<%=(Integer)onlineinfo.get(i)[2]%>]
      	<%
          			}
          			else if( i < onlineinfo.size())
          			{
      	%>
      					,[<%=i%>,<%=(Integer)onlineinfo.get(i)[2]%>]
      	<%
          			}
          			else
          			{
      	%>
      					,[<%=i%>,0]
      	<%
      			
      				}
      			}
      			else
      			{
      				if(i == 0)
      				{
      	%>
      					[0,0]
      			
      	<%
      				}
      				else
      				{
      	%>
      					,[0,0]
      	<%
      				}
      			}
      			
      		}
      %>
           
           ],
    label: "平均在线",
    lines: {show: true, fill: true},
    points: {show: true}
}
	
	]
	,
	{
	
		
		xaxis:{
			ticks:[
			       
			       <%
			      		 for(int i = 0 ; i < 24 ; i++)
			      		 {
			      			 if(i == 0)
			      			 {
			      	%>
			      			<%=i%>,
			      	<%		 
			      			 }
			      			 else
			      			 {
			      	%>
			      			,<%=i%>
			      	<%
			      			 }
			      		 }
			       
			     	 %>
			       
			       ],    
			noTicks: 24,
			min: 0,
			max:23
		},
		
		yaxis:{    
			tickFormatter: function(n){ return n; },
			tickDecimals:0,
			min:0,
			max:<%if(maxMaxOnline!=0)%><%=maxMaxOnline%><%else{%>1<%}%>
		},
		mouse:{track:true},
		legend:{position:'ne',backgroundColor: '#D2E8FF'}
	}
	
);
</script>

