<%@ page contentType="text/html;charset=UTF-8"%>
<%@ page
	import="com.xuanzhi.tools.cache.diskcache.concrete.DefaultDiskCache,org.slf4j.*,java.io.*,java.util.*,com.xuanzhi.tools.text.*,java.util.*,
	com.xuanzhi.tools.text.*,org.springframework.context.ApplicationContext,org.springframework.web.context.support.WebApplicationContextUtils,
	com.sqage.stat.commonstat.dao.UserDao,com.sqage.stat.commonstat.entity.User,com.sqage.stat.commonstat.manager.UserManager,
	com.sqage.stat.commonstat.manager.Impl.UserManagerImpl,com.sqage.stat.commonstat.manager.Impl.PlayGameManagerImpl"
	%>
	<%!
	Object lock = new Object(){};
	ApplicationContext ctx=null;
      %>
  <%
  	//response.setCharacterEncoding("UTF-8");
	//request.setCharacterEncoding("UTF-8");
	String resinHome = System.getProperty("resin.home");
	if(resinHome==null){resinHome="D:/";}


	String startDay = request.getParameter("day");
	if(startDay == null) startDay = "2012-01-01";
	String beginingDay = "2012-01-01";
	
	String today = DateUtil.formatDate(new Date(),"yyyy-MM-dd");
	Date t = DateUtil.parseDate(today,"yyyy-MM-dd");
	Date s = DateUtil.parseDate(startDay,"yyyy-MM-dd");
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
			realChannelList=channelList;
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

		for(int j = 0 ; j < realChannelList.size() ; j++){
			String _channel = realChannelList.get(j);
			
			for(int k = 0 ; k < dayList.size() ; k++){
				String _day = dayList.get(k);
				
				ArrayList<String> qudaoshuList = null;
				if(qudaoshuList == null){ 
				qudaoshuList =new ArrayList<String>();
				qudaoshuList =playGameManager.getQuDaoActivityUserCount(_day);
				 
		
				}
				
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
		<link rel="stylesheet" href="./css/style.css" />
		<link rel="stylesheet" href="./css/atalk.css" />
		<script language="javascript" type="text/javascript" src="./js/flotr/lib/prototype-1.6.0.2.js"></script>
		<script language="javascript" type="text/javascript" src="./js/flotr/flotr-0.2.0-alpha.js"></script>

<script language="JavaScript">
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
			<%=startDay %>各渠道每天进入用户数
		</h2>
		<form>输入日期：<input type='text' name='day' value='<%=startDay %>'>(格式：2012-01-01) 
		<input type='submit' value='提交'> </form>
		<br/>
		</center>
		<br/>
		<center>
		   <form method="post"><input type='hidden' name='action' value='seereguser'><input type='hidden' name='day' value='<%=startDay %>'>过滤渠道：
		    		<%
		    		for(int i = 0 ; i < channelList.size() ; i++){
		    			String _channel = channelList.get(i);
		    			if(realChannelList.contains(_channel)){
		    				out.print("<input type='checkbox' name='filterChannel' value='"+_channel+"' checked >"+_channel+"&nbsp;");
		    			}else{
		    				out.print("<input type='checkbox' name='filterChannel' value='"+_channel+"'>"+_channel+"&nbsp;");
		    			}
		    		}
		    		%>
		    		<br/><input type='submit' value='提交'></form>
		    		<h3>各渠道每天进入用户数</h3>
		    		
		    		<%
		    		out.println("<table id='test2' align='center' width='90%' cellpadding='1' bgcolor='#000000' cellspacing='1' border='0'>");
					out.print("<tr bgcolor='#EEEEBB'><td>日期</td><td>登陆用户数</td>");
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
		<center>
		<br>
		<i>这里的用户都是指进入游戏的用户</i>
		<br>
	</body>
</html>
