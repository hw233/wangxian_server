<%@ page contentType="text/html;charset=UTF-8"%>
<%@include file="../inc.jsp"%>
<%@ page
	import="com.xuanzhi.tools.cache.diskcache.concrete.DefaultDiskCache,org.slf4j.*,java.io.*,java.util.*,com.xuanzhi.tools.text.*,
	com.xuanzhi.tools.text.*,org.springframework.context.ApplicationContext,org.springframework.web.context.support.WebApplicationContextUtils,
	com.sqage.stat.commonstat.dao.UserDao,com.sqage.stat.commonstat.entity.User,com.sqage.stat.commonstat.manager.UserManager,
	com.sqage.stat.commonstat.manager.Impl.UserManagerImpl,com.sqage.stat.commonstat.manager.Impl.PlayGameManagerImpl"
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
	if("0".equals(fenqu)){fenqu=null;}
	String jixing=request.getParameter("jixing");
	if("0".equals(jixing)){jixing=null;}
	
	
	String today = DateUtil.formatDate(new Date(),"yyyy-MM-dd");
	if(startDay == null) startDay =today;
	if(beginingDay == null) beginingDay =today;
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
	
		if(request.getParameter("filterChannel") == null){
			//realChannelList=channelList;
		}else{
			String ss[] = request.getParameterValues("filterChannel");
			for(int i = 0 ; i < ss.length  ;i++){
				if(ss[i].trim().length() > 0){
				//TODO
					realChannelList.add(ss[i].trim());
				}
			}
		}
		int channel_regday_nums[][] = new int[0][0];
		channel_regday_nums = new int[realChannelList.size()][dayList.size()];

      //各个渠道注册用户
         HashMap<String,Integer> map =playGameManager.getQuDaoRetainUserCount_reg(startDay,null,fenqu,jixing);

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
					Integer regisCount=map.get(_channel);
					if(regisCount!=null&&regisCount!=0){
					channel_regday_nums[j][k] = (Integer.parseInt(ss[1])*100)/regisCount;
					}else{
						channel_regday_nums[j][k]=0;
					}
					
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
			<%=startDay %>各渠道某一天的留存
		</h2>
		<form method="post">用户注册日期：<input type='text' name='day' value='<%=startDay %>'>(格式：2012-02-09) <br/><br/>
		统计开始日期：<input type='text' name=beginingDay value='<%=beginingDay %>'>
		-- 统计结束日期：<input type='text' name='endDay' value='<%=endDay %>'>(格式：2012-02-09) &nbsp;&nbsp;&nbsp;&nbsp;
		
		    分区：<select name="fenqu">
                       <option  value="0">全部</option>
                       <% 
                       for(int i = 0 ; i < fenQuList.size() ; i++){
                       String[] _fenqu = fenQuList.get(i);
                       %>
                        <option value="<%=_fenqu[1] %>" 
                        <%
                        if(_fenqu[1].equals(fenqu)){ out.print(" selected=\"selected\"");   }
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
                  </select>&nbsp;&nbsp;
		<br/>
		
		<input type='hidden' name='day' value='<%=startDay %>'>过滤渠道：
		<table width="500"><tr>
		    		<%
		    		for(int i = 0 ; i < channelList.size() ; i++){
		    		if(i>0&&i%9==0){out.print("</tr><tr>");}
		    			String _channel = channelList.get(i);
		    			if(realChannelList.contains(_channel)){
		    				out.print("<td align='left'><input type='checkbox' id='filterChannel' name='filterChannel' value='"+_channel+"' checked />"+_channel+"&nbsp;</td>");
		    			}else{
		    				out.print("<td align='left'><input type='checkbox' id='filterChannel' name='filterChannel' value='"+_channel+"'/>"+_channel+"&nbsp;</td>");
		    			}
		    		}
		    		%>
		    		</tr>
		    		</table>
		    		 <input type="button" name="quanxuanab" onclick="quanxuanabd();" value="全选" >
		    		  <input type="button" name="buxuanab" onClick="buxuanabd();" value="全不选" >
		    		<br/><input type='submit' value='提交'></form>
		    		
		    		<a href="qudaoRetainUser_sum.jsp">累计充值率</a>|
		    		<a href="qudaoRetainUser_sum_qudao.jsp">渠道累计充值率</a>
		    		<i>如果查询所有分区，安装用户名统计；如果查询单个分区，按这个分区的角色名统计。</i>
		    		<h3>各渠道某一天的留存</h3>
		    		
		    		<%
		    		out.println("<table id='test2' align='center' width='90%' cellpadding='1' bgcolor='#000000' cellspacing='1' border='0'>");
					out.print("<tr bgcolor='#EEEEBB'><td>日期</td>");
		    		for(int c = 0 ; c < realChannelList.size() ; c++){
		    			out.print("<td>"+realChannelList.get(c)+"</td>");
		    		}
					out.println("</tr>");
					for(int i = 0 ; i < dayList.size() ; i++){
						out.print("<tr bgcolor='#FFFFFF'><td>"+dayList.get(i)+"</td>");
						//int count = 0;
						//for(int c = 0 ; c < realChannelList.size() ; c++){
			    		//	count+=channel_regday_nums[c][i];
			    		//}
						//out.print("<td bgcolor='#EEEEBB'>"+count+"</td>");
						
						for(int c = 0 ; c < realChannelList.size() ; c++){
			    			out.print("<td>"+channel_regday_nums[c][i]+" %</td>");
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
		<i>指定日期注册的用户，第K日进入游戏的人数</i>
		<br>
	</body>
</html>
