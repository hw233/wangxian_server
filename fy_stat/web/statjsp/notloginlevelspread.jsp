<%@page import="java.text.DecimalFormat"%>
<%@page import="org.apache.commons.lang.StringUtils"%>
<%@page import="org.apache.commons.lang.time.DateUtils"%>
<%@page import="java.text.DateFormat"%>
<%@ page contentType="text/html;charset=UTF-8"%>
<%@page pageEncoding="utf-8" %>
<%@include file="../inc.jsp"%>
<%@ page
	import="com.xuanzhi.tools.cache.diskcache.concrete.DefaultDiskCache,org.slf4j.*,java.io.*,java.util.*,
	com.xuanzhi.tools.text.*,com.sqage.stat.commonstat.entity.ChongZhi,com.xuanzhi.tools.text.*,
	com.sqage.stat.commonstat.dao.UserDao,com.sqage.stat.commonstat.entity.User,com.sqage.stat.commonstat.manager.UserManager,
    com.sqage.stat.commonstat.manager.Impl.*,java.net.*"
	%>

  <%
  	String commitStr = request.getParameter("isCommit");
	Boolean isCommit = false;
	if(!StringUtils.isEmpty(commitStr))
	{
		isCommit = Boolean.valueOf(commitStr); 
	}
	
	if(isCommit)
	{
	  	long startTime = System.currentTimeMillis();
		
		String startDay = request.getParameter("startday");
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
	
		String fenqu=URLDecoder.decode(request.getParameter("fenqu"),"utf-8");
		String qudao=URLDecoder.decode(request.getParameter("qudao"),"utf-8");
		ArrayList<String> quDaoList = (ArrayList<String>)request.getAttribute("QUDAO_LIST");
		ArrayList<String> fenQuList  =  (ArrayList<String>) request.getAttribute("FENQU_LIST");
		
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
		if(quDaoList==null || quDaoList.size() == 0)
		{
			quDaoList = userManager.getQudao(null);//获得现有的渠道信息
		}
		if(fenQuList==null)
		{
		    fenQuList= new ArrayList<String>();
			ArrayList lsfenqu= userManager.getFenQu(null);//获得现有的分区信息
			for(int num=0;num<lsfenqu.size();num++){
			String[] dfenqu=(String[])lsfenqu.get(num);
			fenQuList.add(dfenqu[1]);
			}
		}
		request.setAttribute("QUDAO_LIST", quDaoList);
		request.setAttribute("FENQU_LIST", fenQuList);
		Map<Integer,Integer>info = null;
		info = userManager.countRunOffLevelSpread(startDay,endDay,afterDay,fenqu,qudao);
		Integer sumNotLoginNum = 0;
		Iterator<Integer> it = info.keySet().iterator();
		while(it.hasNext())
		{
			sumNotLoginNum += info.get(it.next());
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
			未登录用户等级分布信息统计
		</h2>
		<div style="clear: both;">
		开始日期：<%=startDay %>-结束日期：<%=endDay %>
		<br/><br/>
			 <%if(isCommit &&  afterDay!=null){ %><%=afterDay %><%}%>天后流失人数
			 <br/><br/>
                                             分区：<select name="fenqu">
                       <option  value="0">全部</option>
                       <% 
                       for(int i = 0 ; i < fenQuList.size() ; i++){
                       String _fenqu = fenQuList.get(i);
                       %>
                        <option value="<%=_fenqu %>" 
                        <%
                        if(_fenqu.equals(fenqu)){  out.print(" selected=\"selected\""); }
                         %>
                          ><%=_fenqu %></option>
                       <%
                       }
                       %>
                </select>&nbsp;&nbsp;
                
               渠道：<select name="qudao">
                       <option  value="0">全部</option>
                                              <% 
                       for(int i = 0 ; i < quDaoList.size() ; i++){
                       String _qudao = quDaoList.get(i);
                       if(_qudao == null)out.println("第"+i+"个元素为空！");
                       %>
                        <option value="<%=_qudao %>" 
                        <%
                        if(_qudao.equals(qudao)){ out.print(" selected=\"selected\"");  }
                         %>
                          ><%=_qudao %></option>
                       <%
                       }
                       %>
                </select>&nbsp;&nbsp;
		    	<table id='onlineinfo' align='center' width='90%' cellpadding='1' bgcolor='#000000' cellspacing='1' border='0'>
		    		<tr bgcolor='#EEEEBB'><td>级别</td><td>人数</td><td>占比</td></tr>
		    		<%
						if(info != null)
						{
							it = info.keySet().iterator();
							while(it.hasNext())
							{
								int key = it.next();
								int value = info.get(key);
								DecimalFormat decimalFormat = new DecimalFormat("0.00");
					%>
							<tr bgcolor='#FFFFFF'><td><%=key%></td><td><%=value%></td><td><%=decimalFormat.format((((value*1.0)/sumNotLoginNum) * 100 ))%>%</td></tr>
					<% 	
							}
					%>
					<tr bgcolor='#EEEEBB'><td>总计:<%=info.size()%>行</td><td><%=sumNotLoginNum %></td><td>--</td></tr>	
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
<% }%>