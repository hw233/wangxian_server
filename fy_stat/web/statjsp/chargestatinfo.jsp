<%@page import="org.apache.commons.lang.StringUtils"%>
<%@page import="org.apache.commons.lang.time.DateUtils"%>
<%@page import="java.text.DateFormat"%>
<%@ page contentType="text/html;charset=UTF-8"%>
<%@include file="../inc.jsp"%>
<%@ page
	import="com.xuanzhi.tools.cache.diskcache.concrete.DefaultDiskCache,org.slf4j.*,java.io.*,java.util.*,
	com.xuanzhi.tools.text.*,com.sqage.stat.commonstat.entity.ChongZhi,com.xuanzhi.tools.text.*,
	com.sqage.stat.commonstat.dao.UserDao,com.sqage.stat.commonstat.entity.User,com.sqage.stat.commonstat.manager.UserManager,
    com.sqage.stat.commonstat.manager.Impl.*,java.net.*"
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
	
	if(fenQuList==null )
	{
	    fenQuList= new ArrayList<String>();
		List ls =userManager.getFenQu(null);//获得现有的分区信息
		for(int num=0;num<ls.size();num++)
		{
		String [] fq = (String [])ls.get(num);
		fenQuList.add(fq[1]);
		}
	}
	
	request.setAttribute("QUDAO_LIST", quDaoList);
	request.setAttribute("FENQU_LIST", fenQuList);
	Object[] info = null;
	if(isCommit)
	{
		info = userManager.countLoginAndCharge(startDay,endDay,afterDay,fenqu,qudao);
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
			用户充值信息统计
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
                       String _fenqu = fenQuList.get(i);
                       %>
                        <option value="<%=_fenqu %>" 
                        <%
                        if(_fenqu.equals(fenqu)){
                        out.print(" selected=\"selected\"");
                        }
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
                        if(_qudao.equals(qudao)){
                        out.print(" selected=\"selected\"");
                        }
                         %>
                          ><%=_qudao %></option>
                       <%
                       }
                       %>
                </select>&nbsp;&nbsp;
                <% 
               // out.println("加载渠道和分区的执行的时间为：["+(System.currentTimeMillis() - startTime)+"]ms");
                %>
		    		<input type='submit' value='提交'></form>
		    		     <a href="loginstatinfo.jsp">登陆流失</a>|
		    		<table id='onlineinfo' align='center' width='90%' cellpadding='1' bgcolor='#000000' cellspacing='1' border='0'>
		    		<tr bgcolor='#EEEEBB'><td>流失原因</td><td>日期</td><td>付费用户数</td><td><%if(isCommit &&  afterDay!=null) {%><a href="notloginandchargespread.jsp?startday=<%=startDay%>&endday=<%=endDay%>&afterday=<%=afterDay%>&fenqu=<%if(fenqu != null ){%><%=URLEncoder.encode(fenqu, "utf-8")%><%}else{%>0<%} %>&qudao=<%if(qudao != null){%><%=qudao%><%}else{%>0<%} %>&isCommit=<%=isCommit%>"><%=afterDay %>天流失</a><%}else{%>天流失<%}%></td></tr>
		    		<%
						if(info != null)
						{
					%>
							<tr bgcolor='#FFFFFF'><td>未登录</td><td><%=startDay%>至<%=endDay%></td><td><%if(info[0] != null) {%><%=info[0]%><%} %></td><td><%if(info[1] != null) {%><%=info[1]%><%} %></td></tr>
							<tr bgcolor='#FFFFFF'><td>未付费</td><td><%=startDay%>至<%=endDay%></td><td><%if(info[0] != null) {%><%=info[0]%><%} %></td><td><%if(info[2] != null) {%><%=info[2]%><%} %></td></tr>
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