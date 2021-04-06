<%@ page contentType="text/html;charset=UTF-8"%>
<%@include file="../inc.jsp"%>
<%@ page
	import="com.xuanzhi.tools.cache.diskcache.concrete.DefaultDiskCache,org.slf4j.*,java.io.*,java.util.*,com.xuanzhi.tools.text.*,
	org.springframework.context.ApplicationContext,org.springframework.web.context.support.WebApplicationContextUtils,
	com.sqage.stat.commonstat.dao.UserDao,com.sqage.stat.commonstat.entity.*,com.sqage.stat.commonstat.manager.*,
	com.sqage.stat.commonstat.manager.Impl.UserManagerImpl,com.sqage.stat.commonstat.manager.Impl.*,com.sqage.stat.service.*,com.sqage.stat.model.Channel"
	%>
	<%!
	Object lock = new Object(){};
      %>
     <%
              String flag = null;
              flag = request.getParameter("flag");
	          String startDay = request.getParameter("day");
	          String endDay = request.getParameter("endDay");
	          String type=request.getParameter("type");
	          String fenqu=request.getParameter("fenqu");
	
	          if("0".equals(fenqu)){fenqu=null;}
	
	          String today = DateUtil.formatDate(new Date(new Date().getTime()-24*60*60*1000),"yyyy-MM-dd");
	          if(startDay == null) startDay = today;
	          if(endDay == null) endDay = today;
	
	         Date t = DateUtil.parseDate(endDay,"yyyy-MM-dd");
	         Date s = DateUtil.parseDate(startDay,"yyyy-MM-dd");
	         ArrayList<String> dayList = new ArrayList<String>();
	/**
	*获得渠道信息
	**/
	          ChannelManager cmanager = ChannelManager.getInstance();
              UserManagerImpl userManager=UserManagerImpl.getInstance();
              PlayGameManagerImpl playGameManager=PlayGameManagerImpl.getInstance();
              OnLineUsersCountManagerImpl onLineUsersCountManager=OnLineUsersCountManagerImpl.getInstance();
              ChongZhiManagerImpl chongZhiManager=ChongZhiManagerImpl.getInstance();
              
              ArrayList<String []> fenQuList= userManager.getFenQu(null);//获得现有的渠道信息
	          Transaction_SpecialManagerImpl transaction_SpecialManager = Transaction_SpecialManagerImpl.getInstance();
	       
	           String subSql="";
	           if(fenqu!=null){
	           subSql+=" and  t.fenqu ='"+fenqu+"'";
	           }
	            List<Transaction_yinzi> ls_qita=null;
	       
	                String sql_qita="select t.createdate createdate,t.fenqu,r.name REASONTYPE,'0' ACTION,sum(t.money) money from stat_gamechongzhi_yinzi t,dt_reasontype r "
	                            +" where t.action='0' and  r.name not in ('金钱道具使用银块','10级奖励银子10级奖励银子','占领城市给的钱占领城市给的钱','占领矿给的钱占领矿给的钱'"
                                +",'竞技奖励银子竞技奖励银子') and r.name not like '充值%' and  t.createdate between '"+startDay+"' and '"+endDay+"'"+subSql
	                            +"  and t.reasontype=r.id group by t.createdate,t.fenqu,r.name order by createdate";
	                           
	                           ls_qita=transaction_SpecialManager.getTransaction_yinziBySql(sql_qita); 
	    
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
		<h2 style="font-weight:bold;">银子流通详情</h2>
		<form  method="post">
		  <input type='hidden' name='flag' value='true'/>
		              开始日期：<input type='text' name='day' value='<%=startDay %>'>
		    -- 结束日期：<input type='text' name='endDay' value='<%=endDay %>'>(格式：2012-02-09)
		    <br/><br/>
                                             分区：<select name="fenqu">
                       <option  value="0">全部</option>
                       <% 
                       for(int i = 0 ; i < fenQuList.size() ; i++){
                       String[] _fenqu = fenQuList.get(i);
                       %>
                        <option value="<%=_fenqu[1] %>" 
                        <%
                        if(_fenqu[1].equals(fenqu)){  out.print(" selected=\"selected\"");  }
                         %>
                          ><%=_fenqu[1] %></option>
                       <%
                       }
                       %>
                </select>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;
                
              
		           <br/>
		    	<input type='submit' value='提交'></form>
		    	
		    	<br/>
		    		   <a href="yinzixiaohao_info.jsp">银子消耗</a> 
		    		     |<a href="yinzixiaohao_info_old.jsp">银子消耗(旧版)</a>
		    		     |<a href="yinpiaoxiaohao_info.jsp">银票消耗</a>
		    		     |<a href="yinpiaoxiaohao_info_old.jsp">银票消耗(老版)</a>|
		    		
		    		<h3>统计总表数据（日报）</h3>
		    		<%
		    		out.println("<table id='test2' align='center' width='90%' cellpadding='1' bgcolor='#000000' cellspacing='1' border='0'>");
					out.print("<tr bgcolor='#EEEEBB'><td>日期</td><td>分区</td><td>银子产生方式</td><td>金额(单位：文)</td></tr>");
					
					if ("true".equals(flag)) {     //如果是刚打开页面，不查询){
				    long totalMoney=0L;
				    for(Transaction_yinzi transaction_yinzi:ls_qita){
				    out.print("<tr bgcolor='#FFFFFF'><td>"+transaction_yinzi.getCreateDate()+"</td><td>"+transaction_yinzi.getFenQu()+
				    "</td><td>"+transaction_yinzi.getReasonType()+"</td><td>"+transaction_yinzi.getMoney()+"</td></tr>");
				    totalMoney+=transaction_yinzi.getMoney();
				    }
				    
				    out.print("<tr bgcolor='#EEEEBB'><td>&nbsp;</td><td>&nbsp;</td><td>&nbsp;</td><td>"+totalMoney+"</td></tr>");
				    
						}
					out.println("</table><br>");
		    		%>
		    		
		    		<div id="regUserContainer" style="width:100%;height:400px;display:block;"></div>
		   <script>
		         drawRegUserFlotr();
		   </script>
		</center>
		<br>
		<i>这里的用户都是指进入游戏的用户</i>
		<br>
	</body>
</html>
