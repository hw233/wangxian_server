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
	String type=request.getParameter("type");
	
	ArrayList<String> typeList=  (ArrayList<String>) session.getAttribute("TYPE_LIST");
	
	if("0".equals(type)){type=null;}
	
	String today = DateUtil.formatDate(new Date(),"yyyy-MM-dd");
	if(startDay == null) startDay = today;
	if(endDay == null) endDay = today;
	
	          ChannelManager cmanager = ChannelManager.getInstance();
              UserManagerImpl userManager=UserManagerImpl.getInstance();
              ChongZhiManagerImpl chongZhiManager=ChongZhiManagerImpl.getInstance();
             
	         
	         if(typeList==null){
	              typeList=chongZhiManager.getTypes(null);//获得现有的充值类型
	              session.removeAttribute("TYPE_LIST");
	              session.setAttribute("TYPE_LIST", typeList);
	           }
             
              String money_search=null;
              String username_search=null;
              String sql="select t.qudao,to_char(t.time,'YYYY-MM'),sum(t.money) money,count(*) usercount,count(distinct(t.username)) chongzhiusercount,"
              +"sum(t.cost) rmoney from stat_chongzhi t where t.type='网站充值'  and to_char(t.time,'YYYY-MM-DD') between '"+startDay+"' and '"+endDay+"' "
              +" group by t.qudao,to_char(t.time,'YYYY-MM')";
              
		     List chongZhiList=chongZhiManager.queryQuDaoChongZhi(sql);
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
			网站充值分渠道查询（财务专用）
		</h2>
		<form  method="post">
		
		   开始日期： <input type="text" name="day" id="day" class="nqz_input" style="width:80px;" readonly="readonly" onfocus="new Calendar(false,'yyyy-MM-dd').showTimePicker(this);" value="<%=startDay %>"/>&nbsp;到
		  结束日期： <input type="text" name="endDay" id="endDay" class="nqz_input" style="width:80px;" readonly="readonly" onfocus="new Calendar(false,'yyyy-MM-dd').showTimePicker(this);" value="<%=endDay %>"/>
		  
		  
		<br/><br/>
                                            
                                            充值类型：<select name="type">
                       <option  value="0">全部</option>
                       <% 
                       for(int i = 0 ; i < typeList.size() ; i++){
                       String _type = typeList.get(i);
                       %>
                        <option value="<%=_type %>" 
                        <%
                        if(_type.equals(type)){
                        out.print(" selected=\"selected\"");
                        }
                         %>
                          ><%=_type %></option>
                       <%
                       }
                       %>
                </select>&nbsp;&nbsp;
                
           
		    		<input type='submit' value='提交'></form><br/>
		    		  
		    		<br/>
		    		<h3>网站充值分渠道查询（财务专用）</h3>
		    		<%
		    		out.println("<table id='test2' align='center' width='90%' cellpadding='1' bgcolor='#000000' cellspacing='1' border='0'>");
					out.print("<tr bgcolor='#EEEEBB'><td>渠道</td><td>金额(分)</td><td>充值次数</td><td> 充值人数</td><td>渠道手续费(分)</td></tr>");
					
					Long moneysum=0L;
					Long usercount=0L;
					Long rusercount=0L;
					Float rmoneysum=0f;
					
					for(int j = 0 ; j < chongZhiList.size() ; j++){
					String[] chognzhi=(String[])chongZhiList.get(j);
					moneysum+=Long.parseLong(chognzhi[1]);
					usercount+=Long.parseLong(chognzhi[2]);
					rusercount+=Long.parseLong(chognzhi[3]);
					rmoneysum+=Float.parseFloat(chognzhi[4]);
					}
					
					for(int i = 0 ; i < chongZhiList.size() ; i++){
					String[] chognzhi=(String[])chongZhiList.get(i);
						out.print("<tr bgcolor='#FFFFFF'><td>"+chognzhi[0]+"</td><td>"+chognzhi[1]+"</td><td>"+chognzhi[2]+"</td><td>"+chognzhi[3]+"</td><td>"+chognzhi[4]+"</td>");
						out.println("</tr>");
					}
					out.print("<tr bgcolor='#EEEEBB'><td>总计</td><td>"+moneysum +"&nbsp;</td><td>"+usercount+"</td><td>"+rusercount+"&nbsp;</td><td>"+rmoneysum+"&nbsp;</td></tr>");
					out.println("</table><br>");
		    		%>
		    		
		    		<div id="regUserContainer" style="width:100%;height:400px;display:block;"></div>
		 
		</center>
		<br>
		<i>这里的用户都是指进入游戏的用户</i>
		<br>
	</body>
</html>
