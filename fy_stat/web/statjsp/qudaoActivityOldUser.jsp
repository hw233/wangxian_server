<%@ page contentType="text/html;charset=UTF-8"%>
<%@include file="../inc.jsp"%>
<%@ page
	import="com.xuanzhi.tools.cache.diskcache.concrete.DefaultDiskCache,org.slf4j.*,java.io.*,java.util.*,com.xuanzhi.tools.text.*,
	com.xuanzhi.tools.text.*,org.springframework.context.ApplicationContext,org.springframework.web.context.support.WebApplicationContextUtils,
	com.sqage.stat.commonstat.dao.UserDao,com.sqage.stat.commonstat.entity.User,com.sqage.stat.commonstat.manager.UserManager,
    com.sqage.stat.commonstat.manager.Impl.*,com.sqage.stat.service.*,com.sqage.stat.model.Channel"
	%>
	<%!
	Object lock = new Object(){};
	ApplicationContext ctx=null;
      %>
  <%
	String startDay = request.getParameter("day");
	String endDay = request.getParameter("endDay");
	String fenqu=request.getParameter("fenqu");
	
	if("0".equals(fenqu)){fenqu=null;}
	
	String today = DateUtil.formatDate(new Date(),"yyyy-MM-dd");
	if(startDay == null) startDay = today;
	if(endDay == null) endDay = today;
	Date t = DateUtil.parseDate(endDay,"yyyy-MM-dd");
	Date s = DateUtil.parseDate(startDay,"yyyy-MM-dd");
	ArrayList<String> dayList = new ArrayList<String>();
	
	/**
	*获得渠道信息
	**/
		if(ctx==null){
		    ctx = WebApplicationContextUtils.getRequiredWebApplicationContext(this.getServletContext());
		    }
		     ChannelManager cmanager = ChannelManager.getInstance();
            UserManagerImpl userManager=(UserManagerImpl)ctx.getBean("UserManager");
            PlayGameManagerImpl playGameManager=(PlayGameManagerImpl)ctx.getBean("PlayGameManager");
            
            List<Channel> channelList = cmanager.getChannels();//渠道信息
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
		if(request.getParameter("filterChannel") == null){
		//for(Channel channel:channelList)
		//{
		//   realChannelList.add(channel.getKey());
		//}
			//realChannelList=channelList;
		}else{
			String ss[] = request.getParameterValues("filterChannel");
			for(int i = 0 ; i < ss.length  ;i++){
				if(ss[i].trim().length() > 0){
					realChannelList.add(ss[i].trim());
				}
			}
		}
		int channel_regday_nums[][] = new int[0][0];
		channel_regday_nums = new int[realChannelList.size()][dayList.size()];
       for(int k = 0 ; k < dayList.size() ; k++){
				String _day = dayList.get(k);
				
				ArrayList<String> qudaoshuList =null;
				if(qudaoshuList == null){ 
				qudaoshuList =new ArrayList<String>();
				qudaoshuList =playGameManager.getQuDaoActivityOldUserCount(_day,fenqu);
				}
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

<script language="JavaScript">

   function quanxuanabd(){
       var m = document.getElementsByName('filterChannel');
        var l = m.length;
        for ( var i=0; i< l; i++)
        {
            m[i].checked=true;
         }
      }
      function buxuanabd(){
        var m = document.getElementsByName('filterChannel');
        var l = m.length;
        for ( var i=0; i< l; i++)
        {
            m[i].checked=false;
         }
         }


var mycars=[<% for(int i = 0 ; i < dayList.size() ; i++){ out.print("\""+dayList.get(i)+"\",");} %>"E"];
 function drawRegUserFlotr(){
		    var f = Flotr.draw(
		$('regUserContainer'), [
		<%for(int j = 0 ; j < realChannelList.size() ; j++){
			StringBuffer sb2 = new StringBuffer();
			sb2.append("[");
			for(int k = 0 ; k < channel_regday_nums[j].length ; k++){
				sb2.append("["+k+","+channel_regday_nums[j][k]+"]");
				if(k < channel_regday_nums[j].length -1) sb2.append(",");
			}
			sb2.append("]");
			out.println("{");
			out.println("data:"+sb2.toString()+",");
			out.println("label:'"+realChannelList.get(j)+"'");
			out.println("}");
			if(j < realChannelList.size()-1) out.print(",");
		}
		%>
		],{
			xaxis:{
				noTicks: <%=dayList.size()%>,	// Display 7 ticks.	
				tickFormatter: function(n){ return mycars[Math.floor(n)];}, // =>
				min: 0,
				max: <%= dayList.size()%>
			},
			yaxis:{
				tickFormatter: function(n){ return (n);}, // =>
				min: 0,
			},
			legend:{
				position: 'ne', // => position the legend 'south-east'.
				backgroundColor: '#D2E8FF' // => a light blue background color.
			},
			mouse:{
				track: true,
				color: 'purple',
				sensibility: 1, // => distance to show point get's smaller
				trackDecimals: 2,
				trackFormatter: function(obj){ return 'y = ' + obj.y; }
			}
		}
	);
	}

</script>
	</head>
	<body>
		<center>
		<h2 style="font-weight:bold;">
			<%=startDay %>各渠道每天进入的老户数
		</h2>
		<form method="post">开始日期：<input type='text' name='day' value='<%=startDay %>'>
		结束日期：<input type='text' name='endDay' value='<%=endDay %>'>(格式：2012-02-09) 
		
		
		    分区：<select name="fenqu">
                       <option  value="0">全部</option>
                       <% 
                       for(int i = 0 ; i < fenQuList.size() ; i++){
                       String[] _fenqu = fenQuList.get(i);
                       %>
                        <option value="<%=_fenqu[1] %>" 
                        <%
                        if(_fenqu[1].equals(fenqu)){  out.print(" selected=\"selected\"");   }
                         %>
                          ><%=_fenqu[1] %></option>
                       <%
                       }
                       %>
                </select>&nbsp;&nbsp;
		
		 <br/>
		   过滤渠道：
		    <table width="500" cellpadding="0">
		    <tr>
		    		<%
		    		for(int i = 0 ; i < channelList.size() ; i++){
		    		if(i>0&&i%9==0){out.print("</tr><tr>");}
		    			Channel _channel = channelList.get(i);
		    			if(realChannelList.contains(_channel.getKey())){
		    				out.print("<td align='left'><input type='checkbox' name='filterChannel' value='"+_channel.getKey()+"' checked >"+_channel.getKey()+"&nbsp;</td>");
		    			}else{
		    				out.print("<td align='left'><input type='checkbox' name='filterChannel' value='"+_channel.getKey()+"'>"+_channel.getKey()+"&nbsp;</td>");
		    			}
		    		}
		    		%>
		    		</tr>
		    		</table>
		    		 <input type="button" name="quanxuanab" onclick="quanxuanabd();" value="全选" >
		    		 <input type="button" name="buxuanab" onClick="buxuanabd();" value="全不选" >
		    		<br/><input type='submit' value='提交'></form><br>
		    		     <a href="qudaoregistuser.jsp">每天的注册用户数</a>|
		    		     <a href="reg_loginUser.jsp">注册并进入用户数</a>|
		    		     <a href="qudaoActivityUser.jsp">每天进入用户数</a>|
		    		     <a href="qudaoActivityOldUser.jsp">每天进入的老户数</a>|
		    		
		    		<br>
		    		<i>如果查询所有分区，安装用户名统计；如果查询单个分区，按这个分区的角色名统计。</i>
		    		<h3>各渠道每天进入的老户数</h3>
		    		
		    		<%
		    		out.println("<table id='test2' align='center' width='90%' cellpadding='1' bgcolor='#000000' cellspacing='1' border='0'>");
					out.print("<tr bgcolor='#EEEEBB'><td>日期</td><td>活动老用户数</td>");
		    		for(int c = 0 ; c < realChannelList.size() ; c++){
		    			out.print("<td>"+realChannelList.get(c)+"</td>");
		    		}
					out.println("</tr>");
					for(int i = 0 ; i < dayList.size() ; i++){
						out.print("<tr bgcolor='#FFFFFF'><td>"+dayList.get(i)+"</td>");
						int count = 0;
						for(int c = 0 ; c < realChannelList.size() ; c++){
			    			count+=channel_regday_nums[c][i];
			    		}
						out.print("<td bgcolor='#EEEEBB'>"+count+"</td>");
						
						for(int c = 0 ; c < realChannelList.size() ; c++){
			    			out.print("<td>"+channel_regday_nums[c][i]+"</td>");
			    		}
						out.println("</tr>");
					}
					out.println("</table><br>");
		    		%>
		    		
		    		<div id="regUserContainer" style="width:100%;height:400px;display:block;"></div>
		   <script>
		  drawRegUserFlotr();
		   </script>
		</center>
		<br>
		<i>这里的用户,分区排重(即：选择全部分区查询，如果一个用户名进入过多个分区，只记一次)</i>
		<br>
	</body>
</html>
