<%@ page contentType="text/html;charset=UTF-8"%>
<%@include file="../inc.jsp"%>
<%@ page
	import="com.xuanzhi.tools.cache.diskcache.concrete.DefaultDiskCache,org.slf4j.*,java.io.*,java.util.*,com.xuanzhi.tools.text.*,
	org.springframework.context.ApplicationContext,org.springframework.web.context.support.WebApplicationContextUtils,
	com.sqage.stat.commonstat.dao.UserDao,com.sqage.stat.commonstat.entity.User,com.sqage.stat.commonstat.manager.UserManager,
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
	String qudao=request.getParameter("qudao");
	String fenqu=request.getParameter("fenqu");
	String jixing = request.getParameter("jixing");
	
	List<Channel> channelList = (ArrayList<Channel>)session.getAttribute("QUDAO_LIST");
	ArrayList<String []> fenQuList  =  (ArrayList<String []>) session.getAttribute("FENQU_LIST");
	
	if("0".equals(jixing)){jixing=null;}
	if("0".equals(qudao)){qudao=null;}
	if("0".equals(fenqu)){fenqu=null;}
	
	String today = DateUtil.formatDate(new Date(),"yyyy-MM-dd");
	if(startDay == null) startDay = today;
	if(endDay == null) endDay = today;
	
	Date t = DateUtil.parseDate(endDay,"yyyy-MM-dd");
	Date s = DateUtil.parseDate(startDay,"yyyy-MM-dd");
	ArrayList<String> dayList = new ArrayList<String>();
	          
              UserManagerImpl userManager=UserManagerImpl.getInstance();
              ChongZhiManagerImpl chongZhiManager=ChongZhiManagerImpl.getInstance();
              
             if(channelList==null || channelList.size() == 0)
	           {
	             ChannelManager cmanager = ChannelManager.getInstance();
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

	  synchronized(lock){
	     while(s.getTime() <= t.getTime() + 3600000){
		 String day = DateUtil.formatDate(s,"yyyy-MM-dd");
		 dayList.add(day);
		 s.setTime(s.getTime() + 24*3600*1000);
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
       
	</head>
	<body>
		<center>
		<h2 style="font-weight:bold;">
			各级VIP充值情况
		</h2>
		<form  method="post">
		  <input type='hidden' name='flag' value='true'/>
		  
		  
		 开始日期： <input type="text" name="day" id="day" class="nqz_input" style="width:100px;" readonly="readonly" onfocus="new Calendar(false,'yyyy-MM-dd').showTimePicker(this);" value="<%=startDay %>"/>&nbsp;到
		  结束日期： <input type="text" name="endDay" id="endDay" class="nqz_input" style="width:100px;" readonly="readonly" onfocus="new Calendar(false,'yyyy-MM-dd').showTimePicker(this);" value="<%=endDay %>"/>
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
                </select> &nbsp;&nbsp;
                                             分区：<select name="fenqu">
                       <option  value="0">全部</option>
                       <% 
                       for(int i = 0 ; i < fenQuList.size() ; i++){
                       String[] _fenqu = fenQuList.get(i);
                       %>
                        <option value="<%=_fenqu[1] %>" 
                        <%
                        if(_fenqu[1].equals(fenqu)){ out.print(" selected=\"selected\""); }
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
                  </select>
		        <br/>
                
		    	<input type='submit' value='提交'></form>
		    		
		    		<h3>各级VIP充值情况</h3>
		    		<%
		    		
					
					if ("true".equals(flag)) {  //如果是刚打开页面，不查询){
					
					for(int i = 0 ; i < dayList.size() ; i++){
					out.println("<h3>"+dayList.get(i)+"各级VIP充值情况</h3>");
					out.println("<table id='test2' align='center' width='90%' cellpadding='1' bgcolor='#000000' cellspacing='1' border='0'>");
					out.print("<tr bgcolor='#EEEEBB'><td>vip</td><td>充值人数</td><td>充值金额</td></tr>");
					
					  List <String []> vip0 = chongZhiManager.getVipChongzhi(dayList.get(i), "0", "1000", qudao, fenqu, jixing);// vip0
					  List <String []> vip1 =chongZhiManager.getVipChongzhi(dayList.get(i), "10", "7000", qudao, fenqu, jixing);// vip1 
					  List <String []> vip2 =chongZhiManager.getVipChongzhi(dayList.get(i), "7000", "16000", qudao, fenqu, jixing);// vip2 
					  List <String []> vip3 =chongZhiManager.getVipChongzhi(dayList.get(i), "16000", "30000", qudao, fenqu, jixing);// vip3 
					  List <String []> vip4 =chongZhiManager.getVipChongzhi(dayList.get(i), "30000", "60000", qudao, fenqu, jixing);// vip4 
					  List <String []> vip5 =chongZhiManager.getVipChongzhi(dayList.get(i), "60000", "120000", qudao, fenqu, jixing);// vip5 
					  List <String []> vip6 =chongZhiManager.getVipChongzhi(dayList.get(i), "120000", "250000", qudao, fenqu, jixing);// vip6 
					  List <String []> vip7 =chongZhiManager.getVipChongzhi(dayList.get(i), "250000", "700000", qudao, fenqu, jixing);// vip7 
					  List <String []> vip8 =chongZhiManager.getVipChongzhi(dayList.get(i), "700000", "1400000", qudao, fenqu, jixing);// vip8 
					  List <String []> vip9 =chongZhiManager.getVipChongzhi(dayList.get(i), "1400000", "3000000", qudao, fenqu, jixing);// vip9 
					  List <String []> vip10 =chongZhiManager.getVipChongzhi(dayList.get(i), "3000000", null, qudao, fenqu, jixing);// vip10 
					long usercont = Long.parseLong(vip0.get(0)[0])
					              + Long.parseLong(vip1.get(0)[0])
					              + Long.parseLong(vip2.get(0)[0])
					              + Long.parseLong(vip3.get(0)[0])
					              + Long.parseLong(vip4.get(0)[0])
					              + Long.parseLong(vip5.get(0)[0])
					              + Long.parseLong(vip6.get(0)[0])
					              + Long.parseLong(vip7.get(0)[0])
					              + Long.parseLong(vip8.get(0)[0])
					              + Long.parseLong(vip9.get(0)[0])
					              + Long.parseLong(vip10.get(0)[0]);
					              
				   long totalMoney = Long.parseLong(vip0.get(0)[1])
					              + Long.parseLong(vip1.get(0)[1])
					              + Long.parseLong(vip2.get(0)[1])
					              + Long.parseLong(vip3.get(0)[1])
					              + Long.parseLong(vip4.get(0)[1])
					              + Long.parseLong(vip5.get(0)[1])
					              + Long.parseLong(vip6.get(0)[1])
					              + Long.parseLong(vip7.get(0)[1])
					              + Long.parseLong(vip8.get(0)[1])
					              + Long.parseLong(vip9.get(0)[1])
					              + Long.parseLong(vip10.get(0)[1]);
					
						out.print("<tr bgcolor='#FFFFFF'><td>vip0</td><td>"+vip0.get(0)[0]+"</td><td>"+vip0.get(0)[1]+"</td>");
						out.print("<tr bgcolor='#FFFFFF'><td>vip1</td><td>"+vip1.get(0)[0]+"</td><td>"+vip1.get(0)[1]+"</td>");
						out.print("<tr bgcolor='#FFFFFF'><td>vip2</td><td>"+vip2.get(0)[0]+"</td><td>"+vip2.get(0)[1]+"</td>");
						out.print("<tr bgcolor='#FFFFFF'><td>vip3</td><td>"+vip3.get(0)[0]+"</td><td>"+vip3.get(0)[1]+"</td>");
						out.print("<tr bgcolor='#FFFFFF'><td>vip4</td><td>"+vip4.get(0)[0]+"</td><td>"+vip4.get(0)[1]+"</td>");
						out.print("<tr bgcolor='#FFFFFF'><td>vip5</td><td>"+vip5.get(0)[0]+"</td><td>"+vip5.get(0)[1]+"</td>");
						out.print("<tr bgcolor='#FFFFFF'><td>vip6</td><td>"+vip6.get(0)[0]+"</td><td>"+vip6.get(0)[1]+"</td>");
						out.print("<tr bgcolor='#FFFFFF'><td>vip7</td><td>"+vip7.get(0)[0]+"</td><td>"+vip7.get(0)[1]+"</td>");
						out.print("<tr bgcolor='#FFFFFF'><td>vip8</td><td>"+vip8.get(0)[0]+"</td><td>"+vip8.get(0)[1]+"</td>");
						out.print("<tr bgcolor='#FFFFFF'><td>vip9</td><td>"+vip9.get(0)[0]+"</td><td>"+vip9.get(0)[1]+"</td>");
						out.print("<tr bgcolor='#FFFFFF'><td>vip10</td><td>"+vip10.get(0)[0]+"</td><td>"+vip10.get(0)[1]+"</td>");
						out.print("<tr bgcolor='#EEEEBB'><td>总计</td><td>"+usercont+"</td><td>"+totalMoney+"</td>");
						out.println("</table><br>");
						}
					
					
					
					//startDateStr, beginmoney, endmoney, qudao, fenQu, jixing
					
					
					
						}
					
		    		%>
		    	<div id="regUserContainer" style="width:100%;height:400px;display:block;"></div>
		    </center>
		<br>
		<i>这里的用户都是指进入游戏的用户</i>
		<br>
	</body>
</html>
