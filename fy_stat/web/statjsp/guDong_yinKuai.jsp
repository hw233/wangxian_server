<%@ page contentType="text/html;charset=UTF-8"%>
<%@include file="../inc.jsp"%>
<%@ page
	import="com.xuanzhi.tools.cache.diskcache.concrete.DefaultDiskCache,org.slf4j.*,java.io.*,java.util.*,com.xuanzhi.tools.text.*,
	com.xuanzhi.tools.text.*,org.springframework.context.ApplicationContext,org.springframework.web.context.support.WebApplicationContextUtils,
	com.sqage.stat.commonstat.dao.UserDao,com.sqage.stat.commonstat.entity.User,com.sqage.stat.commonstat.manager.UserManager,
	com.sqage.stat.commonstat.manager.Impl.*,com.sqage.stat.commonstat.entity.*"
	%>
	<%!
	Object lock = new Object(){};
      %>
  <%
	String startDay = request.getParameter("day");
	String endDay = request.getParameter("endDay");
	String displayType=request.getParameter("displaytype");
	if(displayType==null){ displayType="1"; }
	
	ArrayList<String []> fenQuList  =  (ArrayList<String []>) session.getAttribute("FENQU_LIST");
	
	String today = DateUtil.formatDate(new Date(),"yyyy-MM-dd");
	if(startDay == null) startDay =today;
	if(endDay == null) endDay =today;
	Date t = DateUtil.parseDate(endDay,"yyyy-MM-dd");
	Date s = DateUtil.parseDate(startDay,"yyyy-MM-dd");
	ArrayList<String> dayList = new ArrayList<String>();
	
            UserManagerImpl userManager=UserManagerImpl.getInstance();
            PlayGameManagerImpl playGameManager=PlayGameManagerImpl.getInstance();
            DaoJuManagerImpl daoJuManager =DaoJuManagerImpl.getInstance();
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
	//List<String[]> ls=transaction_SpecialManager.getTransactionSpec_Total_fenQu2(startDay,endDay,fenqu);
      List<String[]> ls=daoJuManager.getGoDong_YinKuai(startDay,endDay,fenqu);
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
			古董银块汇总
		</h2>
		<form  method="post">
		  
		 开始日期： <input type="text" name="day" id="day" class="nqz_input" style="width:100px;" readonly="readonly" onfocus="new Calendar(false,'yyyy-MM-dd').showTimePicker(this);" value="<%=startDay %>"/>&nbsp;到
		  结束日期： <input type="text" name="endDay" id="endDay" class="nqz_input" style="width:100px;" readonly="readonly" onfocus="new Calendar(false,'yyyy-MM-dd').showTimePicker(this);" value="<%=endDay %>"/>
	
	   展示方式：
                   <input type="radio" <% if(displayType==null||"1".equals(displayType)){out.print("checked");} %>  name="displaytype" value="1" />AppStor服务器
                   <input type="radio" <% if("2".equals(displayType)){out.print("checked");} %>  name="displaytype" value="2" />非AppStor服务器
                   <input type="radio" <% if("0".equals(displayType)){out.print("checked");} %>  name="displaytype" value="0" />全部服务器
                   
          
		<input type='submit' value='提交'> </form>
		
		  <a href="guDong_yinKuai_fenqu.jsp">古董——银块分区汇总</a>| <a href="guDong_yinKuai_day_fenqu.jsp">古董—银块分天分区汇总</a>
		 
		
		<br/>
		</center>
		<br/>
		<center>
		    		
		    		<%
		    		out.println("<table id='test2' align='center' width='90%' cellpadding='1' bgcolor='#000000' cellspacing='1' border='0'>");
					out.print("<tr bgcolor='#EEEEBB'><td>日期</td><td>古董产出</td><td>古董消耗</td><td>银块产出</td><td>银块消耗</td></tr>");
						
						for(int ds=0;ds<ls.size();ds++){
						out.print("<tr bgcolor='#FFFFFF'> ");
						String []  tranS=ls.get(ds);
						for( int strs=0;strs<tranS.length;strs++){
						out.print("<td>"+tranS[strs]+"</td>");
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
