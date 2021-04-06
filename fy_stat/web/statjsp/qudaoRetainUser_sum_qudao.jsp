<%@ page contentType="text/html;charset=UTF-8"%>
<%@include file="../inc.jsp"%>
<%@ page
	import="com.xuanzhi.tools.cache.diskcache.concrete.DefaultDiskCache,org.slf4j.*,java.io.*,java.util.*,com.xuanzhi.tools.text.*,
	com.xuanzhi.tools.text.*,org.springframework.context.ApplicationContext,org.springframework.web.context.support.WebApplicationContextUtils,
	com.sqage.stat.commonstat.dao.UserDao,com.sqage.stat.commonstat.entity.User,com.sqage.stat.commonstat.manager.UserManager,
	com.sqage.stat.commonstat.manager.Impl.UserManagerImpl,com.sqage.stat.commonstat.manager.Impl.PlayGameManagerImpl,com.sqage.stat.model.Channel"
	%>
	<%!
	Object lock = new Object(){};
	ApplicationContext ctx=null;
      %>
  <%
	String startDay = request.getParameter("day");
	String beginingDay = request.getParameter("beginingDay");
	String endDay = request.getParameter("endDay");
	String fenqu=request.getParameter("fenqu");
	String qudao=request.getParameter("qudao");
	if("0".equals(fenqu)){fenqu=null;}
	if("0".equals(qudao)){qudao=null;}
	
	String today = DateUtil.formatDate(new Date(),"yyyy-MM-dd");
	if(startDay == null) startDay =today;
	 beginingDay =startDay;
	if(endDay == null) endDay = today;
	
	Date t = DateUtil.parseDate(endDay,"yyyy-MM-dd");
	Date s = DateUtil.parseDate(beginingDay,"yyyy-MM-dd");
	ArrayList<String> dayList = new ArrayList<String>();
	/**
	*获得渠道信息
	**/
	ArrayList<String> channelList = null;
		if(ctx==null){
		    ctx = WebApplicationContextUtils.getRequiredWebApplicationContext(this.getServletContext());
		    }
            UserManagerImpl userManager=(UserManagerImpl)ctx.getBean("UserManager");
            PlayGameManagerImpl playGameManager=(PlayGameManagerImpl)ctx.getBean("PlayGameManager");
            channelList= userManager.getQudao(null);//获得现有的渠道信息
            ArrayList<String []> fenQuList= userManager.getFenQu(null);//获得现有的分区信息
            
           
synchronized(lock){
	while(s.getTime() <= t.getTime() + 3600000){
		String day = DateUtil.formatDate(s,"yyyy-MM-dd");
		dayList.add(day);
		s.setTime(s.getTime() + 24*3600*1000);
	}
}	

	
	//对比各个渠道数据

	ArrayList<String> realChannelList = new ArrayList<String>();
	//某个渠道，注册日期
	      realChannelList=channelList;
		
		int channel_regday_nums[][] = new int[0][0];
		channel_regday_nums = new int[realChannelList.size()][dayList.size()];


        for(int k = 0 ; k < dayList.size() ; k++){
				String _day = dayList.get(k);
				ArrayList<String> qudaoshuList=null;
				qudaoshuList =new ArrayList<String>();
				qudaoshuList =playGameManager.getQuDaoRetainUserCount(startDay,_day,fenqu,null);

		for(int j = 0 ; j < realChannelList.size() ; j++){
			String _channel = realChannelList.get(j);
			
				 
				for(int l = 0 ; l < qudaoshuList.size() ; l++){
					String ss[] = qudaoshuList.get(l).split(" ");
					if(ss[0].equals(_channel)){
					channel_regday_nums[j][k] =Integer.parseInt(ss[1]);
					
					}
				}
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

	</head>
	<body>
		<center>
		<h2 style="font-weight:bold;">
			<%=startDay %>注册用户 到<%=endDay %>的留存累计留存率
		</h2>
		<form method="post">
		用户注册日期：<input type='text' name='day' value='<%=startDay %>'>(格式：2012-02-09) 
		统计日期：<input type='text' name='endDay' value='<%=endDay %>'>(格式：2012-02-09) &nbsp;&nbsp;&nbsp;&nbsp;
		
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
                       String _channel = channelList.get(i);
                       %>
                        <option value="<%=_channel %>" 
                        <%
                        if(_channel.equals(qudao)){
                        out.print(" selected=\"selected\"");
                        }
                         %>
                          ><%=_channel %></option>
                       <%
                       }
                       %>
                </select> &nbsp;&nbsp;
		                   <br/>
		    		<br/><input type='submit' value='提交'></form>
		    		<h3>各渠道某一天的留存</h3>
		    		
		    		<%
		    		out.println("<table id='test2' align='center' width='90%' cellpadding='1' bgcolor='#000000' cellspacing='1' border='0'>");
					out.print("<tr bgcolor='#EEEEBB'><td>日期</td><td>留存用户数</td></tr>");
		    		
					Long totalcount=0L;
					Long registcount=0L;
					for(int i = 0 ; i < dayList.size() ; i++){
						out.print("<tr bgcolor='#FFFFFF'><td>"+dayList.get(i)+"</td>");
						Integer count = 0;
						for(int c = 0 ; c < realChannelList.size() ; c++){
			    			if(qudao==null){
			    			 count+=channel_regday_nums[c][i];
			    			 totalcount+=channel_regday_nums[c][i]; 
			    			  if(i==0){ registcount+=channel_regday_nums[c][i];} 
			    			 }
			    			  else if(qudao.equals(realChannelList.get(c)))
			    			  { count+=channel_regday_nums[c][i];
			    			    totalcount+=channel_regday_nums[c][i]; 
			    			  if(i==0){	registcount+=channel_regday_nums[c][i];}
			    			  }
			    		}
			    		
						out.print("<td bgcolor='#EEEEBB'>"+count+"</td></tr>");
					}
					
					out.print("<tr bgcolor='#EEEEBB'><td>累计留存率</td><td>"+Float.parseFloat(totalcount.toString())/Float.parseFloat(registcount.toString())+"</td></tr>");
					out.println("</table><br>");
		    		%>
		    		
		    		<div id="regUserContainer" style="width:100%;height:400px;display:block;"></div>
		</center>
		<br>
		<i>指定日期注册的用户，第K日进入游戏的人数</i>
		<br>
	</body>
</html>
