<%@ page contentType="text/html;charset=UTF-8"%>
<%@include file="../inc.jsp"%>
<%@ page
	import="com.xuanzhi.tools.cache.diskcache.concrete.DefaultDiskCache,org.slf4j.*,java.io.*,java.util.*,
	com.xuanzhi.tools.text.*,com.sqage.stat.commonstat.entity.ChongZhi,com.xuanzhi.tools.text.*,
	com.sqage.stat.commonstat.dao.UserDao,com.sqage.stat.commonstat.entity.User,com.sqage.stat.commonstat.manager.UserManager,
	com.sqage.stat.commonstat.manager.Impl.UserManagerImpl,com.sqage.stat.commonstat.manager.Impl.*,
	com.sqage.stat.service.*,com.sqage.stat.model.Channel"
	%>

  <%
	String startDay = request.getParameter("day");
	String endDay = request.getParameter("endDay");
	String statstartDay = request.getParameter("statday");
	String statendDay = request.getParameter("statendDay");
	String qudao=request.getParameter("qudao");
	String fenqu=request.getParameter("fenqu");
	if("0".equals(qudao)){qudao=null;}
	if("0".equals(fenqu)){fenqu=null;}
	String today = DateUtil.formatDate(new Date(),"yyyy-MM-dd");
	if(startDay == null) startDay = today;
	if(endDay == null) endDay = today;
	if(statstartDay == null) statstartDay = today;
	if(statendDay == null) statendDay = today;
	
	/**
	*获得渠道信息
	**/
		      ChannelManager cmanager = ChannelManager.getInstance();
              UserManagerImpl userManager=UserManagerImpl.getInstance();
              ChongZhiManagerImpl chongZhiManager=ChongZhiManagerImpl.getInstance();
              
              StatUserGuideManagerImpl statUserGuideManager=StatUserGuideManagerImpl.getInstance();
              ArrayList<String []> fenQuList= userManager.getFenQu(null);//获得现有的分区信息
              List<Channel> channelList = cmanager.getChannels();//渠道信息
              Map<String,String> map= statUserGuideManager.search(startDay,endDay,statstartDay,statendDay,qudao,fenqu);
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
			新手引导监控
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
                           分区：<select name="fenqu">
                       <option  value="0">全部</option>
                       <% 
                       for(int i = 0 ; i < fenQuList.size() ; i++){
                       String[] _fenqu = fenQuList.get(i);
                       %>
                        <option value="<%=_fenqu[1] %>" 
                        <%
                        if(_fenqu[1].equals(fenqu)) {   out.print(" selected=\"selected\"");  }
                         %>
                          ><%=_fenqu[1] %></option>
                       <%
                       }
                       %>
                </select>&nbsp;&nbsp;
                
            	<input type='submit' value='提交'></form>
		    		<br/><br/>
		    		<h3>新手引导监控</h3>
		    		<%
		    		out.println("<table id='test2' align='center' width='90%' cellpadding='1' bgcolor='#000000' cellspacing='1' border='0'>");
					out.print("<tr bgcolor='#EEEEBB'><td>引导步骤</td><td>到达界面的人数</td><td>继续下级引导人数</td><td>选择跳过人数</td><td>流失人数</td></tr>");

                      long tots1= Long.parseLong(map.get("TS1"))+Long.parseLong(map.get("LS1"));
                      long tots2= Long.parseLong(map.get("TS2"))+Long.parseLong(map.get("LS2"));
                      long tots3= Long.parseLong(map.get("TS3"))+Long.parseLong(map.get("LS3"));
                      long tots4= Long.parseLong(map.get("TS4"))+Long.parseLong(map.get("LS4"));
                      long tots5= Long.parseLong(map.get("TS5"))+Long.parseLong(map.get("LS5"));
                      long tots6= Long.parseLong(map.get("TS6"))+Long.parseLong(map.get("LS6"));
                      long tots7= Long.parseLong(map.get("TS7"))+Long.parseLong(map.get("LS7"));
                      long tots8= Long.parseLong(map.get("TS8"))+Long.parseLong(map.get("LS8"));
                      long tots9= Long.parseLong(map.get("TS9"))+Long.parseLong(map.get("LS9"));
                      long tots10=Long.parseLong(map.get("TS10"))+Long.parseLong(map.get("LS10"));
                      long tots11=Long.parseLong(map.get("TS11"))+Long.parseLong(map.get("LS11"));
                      long tots12=Long.parseLong(map.get("TS12"))+Long.parseLong(map.get("LS12"));
                      long tots13=Long.parseLong(map.get("TS13"))+Long.parseLong(map.get("LS13"));
                      long tots14=Long.parseLong(map.get("TS14"))+Long.parseLong(map.get("LS14"));
                      long tots15=Long.parseLong(map.get("TS15"))+Long.parseLong(map.get("LS15"));
                      long tots16=Long.parseLong(map.get("TS16"))+Long.parseLong(map.get("LS16"));
                      long tots17=Long.parseLong(map.get("TS17"))+Long.parseLong(map.get("LS17"));
                      long tots18=Long.parseLong(map.get("TS18"))+Long.parseLong(map.get("LS18"));
                      long tots19=Long.parseLong(map.get("TS19"))+Long.parseLong(map.get("LS19"));     
                      long tots20=Long.parseLong(map.get("TS20"))+Long.parseLong(map.get("LS20"));   
                      long tots21=Long.parseLong(map.get("TS21"))+Long.parseLong(map.get("LS21"));
                      long tots22=Long.parseLong(map.get("TS22"))+Long.parseLong(map.get("LS22"));  
                      long tots23=Long.parseLong(map.get("TS23"))+Long.parseLong(map.get("LS23"));  
                      long tots24=Long.parseLong(map.get("TS24"))+Long.parseLong(map.get("LS24")); 
                      
                      long losts1= tots1-tots2;
                      long losts2=Long.parseLong(map.get("TS2"))-tots3; 
                      long losts3=Long.parseLong(map.get("TS3"))-tots4; 
                      long losts4=Long.parseLong(map.get("TS4"))-tots5; 
                      long losts5=Long.parseLong(map.get("TS5"))-tots6; 
                      long losts6=Long.parseLong(map.get("TS6"))-tots7; 
                      long losts7=Long.parseLong(map.get("TS7"))-tots8; 
                      long losts8=Long.parseLong(map.get("TS8"))-tots9; 
                      long losts9=Long.parseLong(map.get("TS9"))-tots10; 
                      long losts10=Long.parseLong(map.get("TS10"))-tots11; 
                      long losts11=Long.parseLong(map.get("TS11"))-tots12; 
                      long losts12=Long.parseLong(map.get("TS12"))-tots13; 
                      long losts13=Long.parseLong(map.get("TS13"))-tots14; 
                      long losts14=Long.parseLong(map.get("TS14"))-tots15; 
                      long losts15=Long.parseLong(map.get("TS15"))-tots16; 
                      long losts16=Long.parseLong(map.get("TS16"))-tots17; 
                      long losts17=Long.parseLong(map.get("TS17"))-tots18; 
                      long losts18=Long.parseLong(map.get("TS18"))-tots19;
                      long losts19=Long.parseLong(map.get("TS19"))-tots20;       
                      long losts20=Long.parseLong(map.get("TS20"))-tots21;       
                      long losts21=Long.parseLong(map.get("TS21"))-tots22;       
                      long losts22=Long.parseLong(map.get("TS22"))-tots23;       
                      long losts23=Long.parseLong(map.get("TS23"))-tots24;       
                      long losts24=Long.parseLong(map.get("TS24"))-Long.parseLong(map.get("TS25"));             

					out.print("<tr bgcolor='#FFFFFF'><td>S1 </td><td>"+tots1 +"</td><td>&nbsp;</td><td>"+map.get("LS1")+"</td><td>"+losts1+"</td></tr>");
					out.print("<tr bgcolor='#FFFFFF'><td>S2 </td><td>"+tots2 +"</td><td>"+map.get("TS2") +"</td><td>"+map.get("LS2") +"</td><td>"+losts2+"</td></tr>");
					out.print("<tr bgcolor='#FFFFFF'><td>S3 </td><td>"+tots3 +"</td><td>"+map.get("TS3") +"</td><td>"+map.get("LS3") +"</td><td>"+losts3+"</td></tr>");
					out.print("<tr bgcolor='#FFFFFF'><td>S4 </td><td>"+tots4 +"</td><td>"+map.get("TS4") +"</td><td>"+map.get("LS4") +"</td><td>"+losts4+"</td></tr>");
					out.print("<tr bgcolor='#FFFFFF'><td>S5 </td><td>"+tots5 +"</td><td>"+map.get("TS5") +"</td><td>"+map.get("LS5") +"</td><td>"+losts5+"</td></tr>");
					out.print("<tr bgcolor='#FFFFFF'><td>S6 </td><td>"+tots6 +"</td><td>"+map.get("TS6") +"</td><td>"+map.get("LS6") +"</td><td>"+losts6+"</td></tr>");
					out.print("<tr bgcolor='#FFFFFF'><td>S7 </td><td>"+tots7 +"</td><td>"+map.get("TS7") +"</td><td>"+map.get("LS7") +"</td><td>"+losts7+"</td></tr>");
					out.print("<tr bgcolor='#FFFFFF'><td>S8 </td><td>"+tots8 +"</td><td>"+map.get("TS8") +"</td><td>"+map.get("LS8") +"</td><td>"+losts8+"</td></tr>");
					out.print("<tr bgcolor='#FFFFFF'><td>S9 </td><td>"+tots9 +"</td><td>"+map.get("TS9") +"</td><td>"+map.get("LS9") +"</td><td>"+losts9+"</td></tr>");
					out.print("<tr bgcolor='#FFFFFF'><td>S10</td><td>"+tots10+"</td><td>"+map.get("TS10")+"</td><td>"+map.get("LS10")+"</td><td>"+losts10+"</td></tr>");
					out.print("<tr bgcolor='#FFFFFF'><td>S11</td><td>"+tots11+"</td><td>"+map.get("TS11")+"</td><td>"+map.get("LS11")+"</td><td>"+losts11+"</td></tr>");
					out.print("<tr bgcolor='#FFFFFF'><td>S12</td><td>"+tots12+"</td><td>"+map.get("TS12")+"</td><td>"+map.get("LS12")+"</td><td>"+losts12+"</td></tr>");
					out.print("<tr bgcolor='#FFFFFF'><td>S13</td><td>"+tots13+"</td><td>"+map.get("TS13")+"</td><td>"+map.get("LS13")+"</td><td>"+losts13+"</td></tr>");
					out.print("<tr bgcolor='#FFFFFF'><td>S14</td><td>"+tots14+"</td><td>"+map.get("TS14")+"</td><td>"+map.get("LS14")+"</td><td>"+losts14+"</td></tr>");
					out.print("<tr bgcolor='#FFFFFF'><td>S15</td><td>"+tots15+"</td><td>"+map.get("TS15")+"</td><td>"+map.get("LS15")+"</td><td>"+losts15+"</td></tr>");
					out.print("<tr bgcolor='#FFFFFF'><td>S16</td><td>"+tots16+"</td><td>"+map.get("TS16")+"</td><td>"+map.get("LS16")+"</td><td>"+losts16+"</td></tr>");
					out.print("<tr bgcolor='#FFFFFF'><td>S17</td><td>"+tots17+"</td><td>"+map.get("TS17")+"</td><td>"+map.get("LS17")+"</td><td>"+losts17+"</td></tr>");
					out.print("<tr bgcolor='#FFFFFF'><td>S18</td><td>"+tots18+"</td><td>"+map.get("TS18")+"</td><td>"+map.get("LS18")+"</td><td>"+losts18+"</td></tr>");
					out.print("<tr bgcolor='#FFFFFF'><td>S19</td><td>"+tots19+"</td><td>"+map.get("TS19")+"</td><td>"+map.get("LS19")+"</td><td>"+losts19+"</td></tr>");
					out.print("<tr bgcolor='#FFFFFF'><td>S20</td><td>"+tots20+"</td><td>"+map.get("TS20")+"</td><td>"+map.get("LS20")+"</td><td>"+losts20+"</td></tr>");
					out.print("<tr bgcolor='#FFFFFF'><td>S21</td><td>"+tots21+"</td><td>"+map.get("TS21")+"</td><td>"+map.get("LS21")+"</td><td>"+losts21+"</td></tr>");
					out.print("<tr bgcolor='#FFFFFF'><td>S22</td><td>"+tots22+"</td><td>"+map.get("TS22")+"</td><td>"+map.get("LS22")+"</td><td>"+losts22+"</td></tr>");
					out.print("<tr bgcolor='#FFFFFF'><td>S23</td><td>"+tots23+"</td><td>"+map.get("TS23")+"</td><td>"+map.get("LS23")+"</td><td>"+losts23+"</td></tr>");
					out.print("<tr bgcolor='#FFFFFF'><td>S24</td><td>"+tots24+"</td><td>"+map.get("TS24")+"</td><td>"+map.get("LS24")+"</td><td>"+losts24+"</td></tr>");
					out.print("<tr bgcolor='#FFFFFF'><td>S25</td><td>"+map.get("TS25")+"</td><td>&nbsp;    </td><td>"+map.get("LS25")+"</td><td>0</td></tr>");
					
					out.println("</table><br>");
		    		%>
		    		<div id="regUserContainer" style="width:100%;height:400px;display:block;"></div>
		</center>
		<br>
		<i>这里的用户都是指完成注册的用户</i>
		<br>
	</body>
</html>