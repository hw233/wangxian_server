<%@page import="java.net.URLEncoder"%>
<%@page import="org.apache.commons.lang.StringUtils"%>
<%@page import="org.apache.commons.lang.time.DateUtils"%>
<%@page import="java.text.DateFormat"%>
<%@ page contentType="text/html;charset=UTF-8"%>
<%@include file="../inc.jsp"%>
<%@ page
	import="com.xuanzhi.tools.cache.diskcache.concrete.DefaultDiskCache,org.slf4j.*,java.io.*,java.util.*,
	com.xuanzhi.tools.text.*,com.sqage.stat.commonstat.entity.ChongZhi,com.xuanzhi.tools.text.*,
	com.sqage.stat.commonstat.dao.UserDao,com.sqage.stat.commonstat.entity.User,com.sqage.stat.commonstat.manager.UserManager,
    com.sqage.stat.commonstat.manager.Impl.*,java.net.*,com.sqage.stat.model.Channel,com.sqage.stat.service.*"
	%>
  <%
  	long startTime = System.currentTimeMillis();
  	String commitStr = request.getParameter("isCommit");
  	Boolean isCommit = false;
  	if(!StringUtils.isEmpty(commitStr))
  	{
  		isCommit = Boolean.valueOf(commitStr); 
  	}
	String startDay = request.getParameter("day");
	String endDay = request.getParameter("endday");
	String afterDayStr = request.getParameter("afterday");
  	Integer afterDay = null;
  	
  	if(afterDayStr != null)
  	{
  		afterDay = Integer.parseInt(afterDayStr);	
  	}
  	else
  	{
  		afterDay = 0;
  	}

	String fenqu=request.getParameter("fenqu");
	String qudao=request.getParameter("qudao");
	
	List<Channel> channelList = (ArrayList<Channel>)session.getAttribute("QUDAO_LIST");
	ArrayList<String []> fenQuList  =  (ArrayList<String []>) session.getAttribute("FENQU_LIST");
	
	if(fenqu != null && fenqu.trim().equals("0"))
	{
		fenqu = null;
	}
	if(qudao != null && qudao.trim().equals("0"))
	{
		qudao = null;
	}
	if(startDay == null)
	{
		startDay = DateUtil.formatDate(new Date(), "yyyy-MM-dd");
	}
	if(endDay == null)
	{
		endDay = DateUtil.formatDate(new Date(), "yyyy-MM-dd");
	}
    UserManagerImpl userManager=UserManagerImpl.getInstance();
    ChannelManager cmanager = ChannelManager.getInstance();

	if(channelList==null || channelList.size() == 0)
	{
		  channelList = cmanager.getChannels();//渠道信息
	}
	if(fenQuList==null)
	{
		fenQuList= userManager.getFenQu(null);//获得现有的分区信息
	}
	session.setAttribute("QUDAO_LIST", channelList);
	session.setAttribute("FENQU_LIST", fenQuList);
	Object[] info = null;
	if(isCommit)
	{
		info = userManager.countLoginAndRunOff(startDay,endDay,afterDay,fenqu,qudao);
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
		<h2 style="font-weight:bold;">
			用户登录流失情况
		</h2>
		<div style="clear: both;">
		<form  method="post">开始日期：<input type='text' name='day' value='<%=startDay %>'/>(格式：2012-02-09)
		- 结束日期：<input type='text' name='endday' value='<%=endDay %>' />(格式：2012-02-09)
		<input type="hidden" name="isCommit" value="true" />
		<br/><br/>
			 需要查询<input type='text' name='afterday' value='<%if(afterDay != null){ %><%=afterDay %><%}%>' maxlength="10">(请填入阿拉伯数字)天后流失人数
			 <br/><br/>
                                             分区：<select name="fenqu">
                       <option  value="0">全部</option>
                       <% 
                       for(int i = 0 ; i < fenQuList.size() ; i++){
                       String[] _fenqu = fenQuList.get(i);
                       %>
                        <option value="<%=_fenqu[1] %>" 
                        <%
                        if(_fenqu[1].equals(fenqu)){
                        out.print(" selected=\"selected\"");
                        }
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
                       Channel _qudao = channelList.get(i);
                       %>
                        <option value="<%=_qudao.getKey() %>" 
                        <%
                        if(_qudao.getKey().equals(qudao)){
                        out.print(" selected=\"selected\"");
                        }
                         %>
                          ><%=_qudao.getKey() %></option>
                       <%
                       }
                       %>
                </select>&nbsp;&nbsp;
		    		<input type='submit' value='提交'></form>
		    		 <a href="chargestatinfo.jsp">充值流失</a><br/>
		    		
		    		<table id='onlineinfo' align='center' width='90%' cellpadding='1' bgcolor='#000000' cellspacing='1' border='0'>
		    		<tr bgcolor='#EEEEBB'><td>日期</td><td>登录用户数</td><td>
		    		<%if(isCommit &&  afterDay!=null) {%>
		    		<a href="notloginlevelspread.jsp?startday=<%=startDay%>&endday=<%=endDay%>&afterday=<%=afterDay%>&fenqu=
		    		<%if(fenqu != null ){%><%=URLEncoder.encode(fenqu, "utf-8")%><%}
		    		else{%>0<%} %>&qudao=<%if(qudao != null){%><%=qudao%><%}else{%>0<%} %>&isCommit=<%=isCommit%>">
		    		<%=afterDay %>天流失</a><%}else{%>天流失<%}%>
		    		</td></tr>
		    		<%
						if(info != null)
						{
					%>
							<tr bgcolor='#FFFFFF'><td><%=startDay%>至<%=endDay%></td><td><%if(info[0] != null) {%><%=info[0]%><%} %></td><td><%if(info[1] != null) {%><%=info[1]%><%} %></td></tr>
					<% 	
						}
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

