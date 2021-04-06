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
	
	String fenqu=request.getParameter("fenqu");
	String daynum=request.getParameter("daynum");
	String nation=request.getParameter("nation");
	
		String qudao=request.getParameter("qudao");
	
	if("0".equals(fenqu)){fenqu=null;}
	if("0".equals(daynum)){daynum="7";}
	if("0".equals(nation)){nation=null;}
	if("0".equals(qudao)){qudao=null;}
	
	String today = DateUtil.formatDate(new Date(),"yyyy-MM-dd");
	if(startDay == null) startDay = today;
	if(endDay == null) endDay = today;
	
	List<Channel> channelList = (ArrayList<Channel>)session.getAttribute("QUDAO_LIST");
	ArrayList<String []> fenQuList  =  (ArrayList<String []>) session.getAttribute("FENQU_LIST");
	/**
	*获得渠道信息
	**/
              
              PlayGameManagerImpl playGameManager=PlayGameManagerImpl.getInstance();
              
               if(channelList==null || channelList.size() == 0)
	           {
	             ChannelManager cmanager = ChannelManager.getInstance();
		         channelList = cmanager.getChannels();//渠道信息
		         session.removeAttribute("QUDAO_LIST");
		         session.setAttribute("QUDAO_LIST", channelList);
	           }
	         if(fenQuList==null)
	           {
	             UserManagerImpl userManager=UserManagerImpl.getInstance();
		         fenQuList= userManager.getFenQu(null);//获得现有的分区信息
		         session.removeAttribute("FENQU_LIST");
		         session.setAttribute("FENQU_LIST", fenQuList);
	           }
		     List<String[]>  playerLevelFenBuList=playGameManager.getLostPlayerLevelFenBu(startDay,endDay,fenqu,qudao,nation,daynum);
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
			连续<%=daynum %>天未登录角色等级分布
		</h2>
		<form  method="post">
		
		                  <!-- 注册起始日期：<input type='text' name='day' value='<%=startDay %>'>
		                  -- 注册终止日期：<input type='text' name='endDay' value='<%=endDay %>'>(格式：2012-01-01)
		                   -->
		                  注册 开始日期： <input type="text" name="day" id="day" class="nqz_input" style="width:100px;" readonly="readonly" onfocus="new Calendar(false,'yyyy-MM-dd').showTimePicker(this);" value="<%=startDay %>"/>&nbsp;到
		                  注册 结束日期： <input type="text" name="endDay" id="endDay" class="nqz_input" style="width:100px;" readonly="readonly" onfocus="new Calendar(false,'yyyy-MM-dd').showTimePicker(this);" value="<%=endDay %>"/>
		                  
		                  <br/>
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
                        if(_fenqu[1].equals(fenqu)){ out.print(" selected=\"selected\"");   } %>
                          ><%=_fenqu[1] %></option>
                       <% } %>
                   </select>&nbsp;&nbsp;
                                      
                    
                                            国家：<select name="nation">
                      <option  value="0">全部</option>
                     
                      <option value="沧月" 
                        <%
                        if("沧月".equals(nation)){
                        out.print(" selected=\"selected\"");
                        } %>
                          >沧月</option>
                          
                      <option value="无尘" 
                        <%
                        if("无尘".equals(nation)){
                        out.print(" selected=\"selected\"");
                        } %>
                          >无尘</option>
                          
                      <option value="昊天" 
                        <%
                        if("昊天".equals(nation)){
                        out.print(" selected=\"selected\"");
                        } %>
                          >昊天</option>
                </select>&nbsp;&nbsp;
                
                   连续未登录天数：<select name="daynum">
                   
                     
                      <option value="5" 
                        <% if("5".equals(daynum)){ out.print(" selected=\"selected\""); } %>
                          >5</option>
                       <option value="7" 
                        <% if("7".equals(daynum)){ out.print(" selected=\"selected\""); } %>
                          >7</option>
                        <option value="8" 
                        <% if("8".equals(daynum)){ out.print(" selected=\"selected\""); } %>
                          >8</option>
                          
                      <option value="9" 
                        <% if("9".equals(daynum)){ out.print(" selected=\"selected\""); } %>
                          >9</option>
                          
                       <option value="10" 
                        <% if("10".equals(daynum)){ out.print(" selected=\"selected\""); } %>
                          >10</option>
                    </select>&nbsp;&nbsp;

		    	<input type='submit' value='提交'></form><br/>
		    	<i>选全部分区时：是用户等级；如果选择某个分区，是这个分区的角色等级。</i><br>
		    	<i> 注：因为有老用户进入新区，所以查询新区所有用户的等级时，注意注册时间的限制；又因用户量比较多，大注册时间区间的查询慎用！</i>
		    	
		    		<%
		    		out.println("<table id='test2' align='center' width='90%' cellpadding='1' bgcolor='#000000' cellspacing='1' border='0'>");
					out.print("<tr bgcolor='#EEEEBB'><td>级别</td><td>角色数</td><td>占比</td></tr>");
					Long sum=0L;
					
					for(int i = 0 ; i < playerLevelFenBuList.size() ; i++){
					    String[] playerLevelFenBu=(String[])playerLevelFenBuList.get(i);
					    if(playerLevelFenBu[1]!=null){sum+=Long.parseLong(playerLevelFenBu[1]);}
					}
					
					for(int i = 0 ; i < playerLevelFenBuList.size() ; i++){
					String[] playerLevelFenBu=(String[])playerLevelFenBuList.get(i);
					BigDecimal b=new BigDecimal("0");
					if(playerLevelFenBu[1]==null){playerLevelFenBu[1]="0";}
					
					                BigDecimal b1 = new BigDecimal(playerLevelFenBu[1]);
  									BigDecimal b2 = new BigDecimal(sum);
  									BigDecimal b100 = new BigDecimal("100");
  									BigDecimal b11 = b100.multiply(b1);
  									       b = b11.divide(b2, new MathContext(4));
					
					
						out.print("<tr bgcolor='#FFFFFF'><td>"+playerLevelFenBu[0]+"</td>"+
						                                "<td>"+playerLevelFenBu[1]+"</td>"+
						                                "<td>"+b.toString()+"% </td></tr>"
						                                 );
					}
					
					out.print("<tr bgcolor='#EEEEBB'><td>总计</td><td>"+sum+"</td><td>&nbsp;</td></tr>");
					out.println("</table><br>");
		    		%>
		    		
		    		<div id="regUserContainer" style="width:100%;height:400px;display:block;"></div>
		 
		</center>
		<br>
		<i>这里的用户都是指进入游戏的用户</i>
		<br>
	</body>
</html>
