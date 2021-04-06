<%@ page contentType="text/html;charset=UTF-8"%>
<%@include file="../inc.jsp"%>
<%@ page
	import="com.xuanzhi.tools.cache.diskcache.concrete.DefaultDiskCache,org.slf4j.*,java.io.*,java.util.*,com.xuanzhi.tools.text.*,
	com.xuanzhi.tools.text.*,org.springframework.context.ApplicationContext,org.springframework.web.context.support.WebApplicationContextUtils,
	com.sqage.stat.commonstat.dao.UserDao,com.sqage.stat.commonstat.entity.User,com.sqage.stat.commonstat.manager.UserManager,
	com.sqage.stat.commonstat.manager.Impl.*"
	%>
	<%!
	Object lock = new Object(){};
	ApplicationContext ctx=null;
      %>
  <%
	String startDay = request.getParameter("day");
	String endDay = request.getParameter("endDay");
	String today = DateUtil.formatDate(new Date(),"yyyy-MM-dd");
	if(startDay == null) startDay =today;
	if(endDay == null) endDay =today;
	Date t = DateUtil.parseDate(endDay,"yyyy-MM-dd");
	Date s = DateUtil.parseDate(startDay,"yyyy-MM-dd");
	ArrayList<String> dayList = new ArrayList<String>();
	
	/**
	*获得渠道信息
	**/
		if(ctx==null){
		    ctx = WebApplicationContextUtils.getRequiredWebApplicationContext(this.getServletContext());
		    }
		   ChongZhiManagerImpl chongZhiManager= ChongZhiManagerImpl.getInstance();
            //UserManagerImpl userManager=(UserManagerImpl)ctx.getBean("UserManager");
           // PlayGameManagerImpl playGameManager=(PlayGameManagerImpl)ctx.getBean("PlayGameManager");
           
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
	
			realChannelList.add("神州付");
			realChannelList.add("支付宝");
			realChannelList.add("易宝");
			realChannelList.add("天猫商城");
			realChannelList.add("网站充值");
			realChannelList.add("Wap支付宝");
			
			
			
			
			
		float channel_regday_nums[][] = new float[0][0];
		channel_regday_nums = new float[realChannelList.size()][dayList.size()];
      for(int k = 0 ; k < dayList.size() ; k++){
				String _day = dayList.get(k);
				ArrayList<String> qudaoshuList = null;
				if(qudaoshuList == null){ 
				qudaoshuList =new ArrayList<String>();
				qudaoshuList=qudaoshuList=chongZhiManager.getSqageChongZhi_nocost(_day);
				}
		for(int j = 0 ; j < realChannelList.size() ; j++){
			String _channel = realChannelList.get(j);
				for(int l = 0 ; l < qudaoshuList.size() ; l++){
					String ss[] = qudaoshuList.get(l).split(" ");
					if(ss[0].equals(_channel)){
					if(ss[1]!=null){
					channel_regday_nums[j][k] =Float.parseFloat(ss[1])/100;
					}else{channel_regday_nums[j][k] =0;
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
			自有支付充值汇总(扣除手续费)
		</h2>
		<form>开始日期：<input type='text' name='day' value='<%=startDay %>'>
		-- 结束日期：<input type='text' name='endDay' value='<%=endDay %>'>(格式：2012-02-09)
		<input type='submit' value='提交'> </form>
		<br/>
		</center>
		<br/>
		<center>
		    		<h3>自有支付充值汇总</h3>
		    		<%
		    		out.println("<table id='test2' align='center' width='90%' cellpadding='1' bgcolor='#000000' cellspacing='1' border='0'>");
					out.print("<tr bgcolor='#EEEEBB'><td>日期</td>");
					
					long[] zonshu=new long[realChannelList.size()];
		    		for(int c = 0 ; c < realChannelList.size() ; c++){
		    			out.print("<td>"+realChannelList.get(c)+"</td>");
		    		}
					    out.println("<td>汇总(元)</td></tr>");
					for(int i = 0 ; i < dayList.size() ; i++){
						out.print("<tr bgcolor='#FFFFFF'><td>"+dayList.get(i)+"</td>");
						long count = 0;
						for(int c = 0 ; c < realChannelList.size() ; c++){
			    			count+=channel_regday_nums[c][i];
			    			zonshu[c]+=channel_regday_nums[c][i];
			    		}
						
						for(int c = 0 ; c < realChannelList.size() ; c++){
			    			out.print("<td>"+channel_regday_nums[c][i]+"</td>");
			    		}
			    		out.print("<td bgcolor='#EEEEBB'>"+count+"</td></tr>");
					}
					
					
					out.print("<tr bgcolor='#FFFFFF'><td bgcolor='#EEEEBB'>总和</td>");
					long sum=0;
					for(int h=0;h<zonshu.length;h++)
					{
					out.print("<td bgcolor='#EEEEBB'>"+zonshu[h]+"</td>");
					sum+=(long)zonshu[h];
					}
					out.print("<td bgcolor='#EEEEBB'>"+sum+"</td></tr>");
					out.println("</table><br>");
		    		%>
		    		
		    		<div id="regUserContainer" style="width:100%;height:400px;display:block;"></div>
		   <script>
		  drawRegUserFlotr();
		   </script>
		</center>
		<br>
		<i>渠道商自己没有付费接口，当用户付费时，渠道商调用我公司的付费接口完成付费。采用这种付费方式的费用，在本页面的统计结果中是扣除了支付公司的手续费用的。</i>
		<br>
	</body>
</html>
