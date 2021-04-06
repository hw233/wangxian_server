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
      %>
  <%
	String startDay = request.getParameter("day");
	String endDay = request.getParameter("endDay");
	//String fenqu=request.getParameter("fenqu");
	
	String displayType=request.getParameter("displaytype");
	if(displayType==null){ displayType="1"; }
	
	ArrayList<String []> fenQuList  =  (ArrayList<String []>) session.getAttribute("FENQU_LIST");
	//if("0".equals(fenqu)){fenqu=null;}
	
	String today = DateUtil.formatDate(new Date(),"yyyy-MM-dd");
	if(startDay == null) startDay =today;
	if(endDay == null) endDay =today;
	Date t = DateUtil.parseDate(endDay,"yyyy-MM-dd");
	Date s = DateUtil.parseDate(startDay,"yyyy-MM-dd");
	ArrayList<String> dayList = new ArrayList<String>();
	
            UserManagerImpl userManager=UserManagerImpl.getInstance();
            PlayGameManagerImpl playGameManager=PlayGameManagerImpl.getInstance();
            Transaction_SpecialManagerImpl transaction_SpecialManager=Transaction_SpecialManagerImpl.getInstance();
           
           if(fenQuList==null)
	           {
		         fenQuList= userManager.getFenQu(null);//获得现有的分区信息
		         session.removeAttribute("FENQU_LIST");
		         session.setAttribute("FENQU_LIST", fenQuList);
	           }
	           
	       ArrayList<String []> fenQuListT=new ArrayList();
           String fenqu="";
           if("1".equals(displayType)){
           
           fenqu=" and t.fenqu in ('问天灵台','玉幡宝刹','飞瀑龙池','西方灵山','雪域冰城','白露横江','左岸花海','裂风峡谷','右道长亭','永安仙城','霹雳霞光','对酒当歌','独霸一方','独步天下','飞龙在天','九霄龙吟','万象更新','春风得意','天下无双','幻灵仙境','仙子乱尘','梦倾天下','再续前缘','兰若古刹','权倾皇朝','诸神梦境','倾世情缘','傲啸封仙')	";
           for(int c = 0 ; c < fenQuList.size() ; c++){
		    			if(fenqu.indexOf(fenQuList.get(c)[1])!=-1)
		    			{fenQuListT.add(fenQuList.get(c));}
		    		}
           } else if("2".equals(displayType)){
           fenqu=" and t.fenqu not in ('问天灵台','玉幡宝刹','飞瀑龙池','西方灵山','雪域冰城','白露横江','左岸花海','裂风峡谷','右道长亭','永安仙城','霹雳霞光','对酒当歌','独霸一方','独步天下','飞龙在天','九霄龙吟','万象更新','春风得意','天下无双','幻灵仙境','仙子乱尘','梦倾天下','再续前缘','兰若古刹','权倾皇朝','诸神梦境','倾世情缘','傲啸封仙')	";
             for(int c = 0 ; c < fenQuList.size() ; c++){
		    			if(fenqu.indexOf(fenQuList.get(c)[1])==-1)
		    			{fenQuListT.add(fenQuList.get(c));}
		    		}

                } else{
                fenQuListT=fenQuList;
                }
	           
	           
	           
           
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
			realChannelList.add("交易银子");
			realChannelList.add("发出者银子");
			realChannelList.add("接收者银子");
		List<String []> ls=transaction_SpecialManager.getTransactionSpec_Total(startDay,endDay,fenqu);
		String channel_regday_nums[][] = new String[realChannelList.size()+1][ls.size()];
		
      for(int k = 0 ; k < ls.size() ; k++){
				String[] trans = ls.get(k);
		for(int j = 0 ; j < realChannelList.size()+1 ; j++){
		
				channel_regday_nums[j][k] =trans[j];
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
        <script type="text/javascript" src="<%=request.getContextPath()%>/comfile/js/calendar.js"></script>
       
<script language="JavaScript">
var mycars=[<% for(int i = 0 ; i < ls.size() ; i++){ out.print("\""+ls.get(i)[0]+"\",");} %>"E"];
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
			特殊交易汇总
		</h2>
		<form  method="post">
		  
		 开始日期： <input type="text" name="day" id="day" class="nqz_input" style="width:100px;" readonly="readonly" onfocus="new Calendar(false,'yyyy-MM-dd').showTimePicker(this);" value="<%=startDay %>"/>&nbsp;到
		  结束日期： <input type="text" name="endDay" id="endDay" class="nqz_input" style="width:100px;" readonly="readonly" onfocus="new Calendar(false,'yyyy-MM-dd').showTimePicker(this);" value="<%=endDay %>"/>
	
	
	   展示方式：
                   <input type="radio" <% if(displayType==null||"1".equals(displayType)){out.print("checked");} %>  name="displaytype" value="1" />AppStor服务器
                   <input type="radio" <% if("2".equals(displayType)){out.print("checked");} %>  name="displaytype" value="2" />非AppStor服务器
                   <input type="radio" <% if("0".equals(displayType)){out.print("checked");} %>  name="displaytype" value="0" />全部服务器
	
		<input type='submit' value='提交'> </form>
		
		   <a href="Transaction_Special_Tatal_fenqu.jsp">异常交易按分区汇总</a>|
		   <a href="Transaction_Special_Tatal_fenqu2.jsp">异常交易按日期和分区汇总</a>|
		  
		  
		
		<br/>
		</center>
		<br/>
		<center>
		    		<h3>特殊交易汇总（定义：单笔交易取双方银子的一个最大值为交易银子）</h3>
		    		<%
		    		out.println("<table id='test2' align='center' width='90%' cellpadding='1' bgcolor='#000000' cellspacing='1' border='0'>");
					out.print("<tr bgcolor='#EEEEBB'><td>日期</td>");
		    		for(int c = 0 ; c < realChannelList.size() ; c++){
		    			out.print("<td>"+realChannelList.get(c)+"</td>");
		    		}
					out.println("</tr>");
					for(int i = 0 ; i < ls.size() ; i++){
						//out.print("<tr bgcolor='#FFFFFF'><td>"+dayList.get(i)+"</td>");
						out.print("<tr bgcolor='#FFFFFF'> ");
						
						for(int c = 0 ; c < realChannelList.size()+1 ; c++){
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
		<i>这里的用户都是指进入游戏的用户</i>
		<br>
	</body>
</html>
