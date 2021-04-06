<%@ page contentType="text/html;charset=UTF-8"%>
<%@include file="../inc.jsp"%>
<%@ page
	import="com.xuanzhi.tools.cache.diskcache.concrete.DefaultDiskCache,org.slf4j.*,java.io.*,java.util.*,com.xuanzhi.tools.text.*,
	org.springframework.context.ApplicationContext,org.springframework.web.context.support.WebApplicationContextUtils,
	com.sqage.stat.commonstat.dao.UserDao,com.sqage.stat.commonstat.entity.*,com.sqage.stat.commonstat.manager.*,
    com.sqage.stat.commonstat.manager.Impl.*,com.sqage.stat.service.*,com.sqage.stat.model.Channel"
	%>
     <%
              String flag = null;
              flag = request.getParameter("flag");
	          String startDay = request.getParameter("day");
	          String endDay = request.getParameter("endDay");
	          String daoJuName=request.getParameter("daoJuName");
	          String fenqu=request.getParameter("fenqu");
	
	          if("0".equals(fenqu)){fenqu=null;}
	
	          String today = DateUtil.formatDate(new Date(new Date().getTime()-24*60*60*1000),"yyyy-MM-dd");
	          if(startDay == null) startDay = today;
	          if(endDay == null) endDay = today;
	
	         Date t = DateUtil.parseDate(endDay,"yyyy-MM-dd");
	         Date s = DateUtil.parseDate(startDay,"yyyy-MM-dd");
	         ArrayList<String> dayList = new ArrayList<String>();

	          ChannelManager cmanager = ChannelManager.getInstance();
              UserManagerImpl userManager=UserManagerImpl.getInstance();
              OnLineUsersCountManagerImpl onLineUsersCountManager=OnLineUsersCountManagerImpl.getInstance();
              DaoJuManagerImpl daoJuManager = DaoJuManagerImpl.getInstance();
              ArrayList<String []> fenQuList= userManager.getFenQu(null);//获得现有的渠道信息
	    
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
		<h2 style="font-weight:bold;">酒类道具跟踪</h2>
		<form  method="post">
		  <input type='hidden' name='flag' value='true'/>
		      
		 开始日期： <input type="text" name="day" id="day" class="nqz_input" style="width:100px;" readonly="readonly" onfocus="new Calendar(false,'yyyy-MM-dd').showTimePicker(this);" value="<%=startDay %>"/>&nbsp;到
		  结束日期： <input type="text" name="endDay" id="endDay" class="nqz_input" style="width:100px;" readonly="readonly" onfocus="new Calendar(false,'yyyy-MM-dd').showTimePicker(this);" value="<%=endDay %>"/>
	
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
                
              
                                       道具名称：<select name="daoJuName">
                       <option  value="杏花村"
                           <% if("杏花村".equals(daoJuName)){  out.print(" selected=\"selected\""); } %>
                           >杏花村</option>
                       <option  value="屠苏酒"
                           <% if("屠苏酒".equals(daoJuName)){  out.print(" selected=\"selected\""); } %>
                           >屠苏酒</option>
                       <option  value="妙沁药酒"
                           <% if("妙沁药酒".equals(daoJuName)){  out.print(" selected=\"selected\""); } %>
                           >妙沁药酒</option>
                </select>&nbsp;&nbsp;
                
		    	<input type='submit' value='提交'></form>
		    	
		    	 <a href="daoJuJingXiFenXi_vip.jsp">vip用户喝酒信息</a>|
		    		
		    		<h3>酒类道具跟踪</h3>
		    		<%
		    		
					if ("true".equals(flag)) {  //如果是刚打开页面，不查询
					 List<String []> lbai = daoJuManager.getDaoJuImportant(startDay,endDay,fenqu,daoJuName, "白色");  
                     List<String []> llv  = daoJuManager.getDaoJuImportant(startDay,endDay,fenqu,daoJuName, "绿色");   
                     List<String []> llan = daoJuManager.getDaoJuImportant(startDay,endDay,fenqu,daoJuName, "蓝色");   
                     List<String []> lzi  = daoJuManager.getDaoJuImportant(startDay,endDay,fenqu,daoJuName, "紫色"); 
                     List<String []> lceng= daoJuManager.getDaoJuImportant(startDay,endDay,fenqu,daoJuName, "橙色");     
					
					////白色酒
					out.println("<br/>白色酒<br/>");
					out.println("<table id='test2' align='center' width='90%' cellpadding='1' bgcolor='#000000' cellspacing='1' border='0'>");
					out.print("<tr bgcolor='#EEEEBB'><td>日期</td><td>获取</td><td>使用</td><td>系统回收</td><td>死亡掉落</td></tr>");
					
					long[] bai=new long[5];
					for(int i = 0 ; i < lbai.size() ; i++){
					String [] barStr = lbai.get(i);
						out.print("<tr bgcolor='#FFFFFF'><td>"+barStr[0]+"</td><td>"+barStr[1]+"</td><td>"+barStr[2]+"</td><td>"+barStr[3]+"</td><td>"+barStr[4]+"</td></tr>");
					       bai[1]+=barStr[1]==null? 0: Long.parseLong(barStr[1]);
					       bai[2]+=barStr[2]==null? 0: Long.parseLong(barStr[2]);
					       bai[3]+=barStr[3]==null? 0: Long.parseLong(barStr[3]);
					       bai[4]+=barStr[4]==null? 0: Long.parseLong(barStr[4]);
					}
					out.print("<tr bgcolor='#EEEEBB'><td>总计</td><td>"+bai[1]+"</td><td>"+bai[2]+"</td><td>"+bai[3]+"</td><td>"+bai[4]+"</td></tr>");
					out.println("</table><br><br/>绿色酒<br/>");
					
					
					out.println("<table id='test2' align='center' width='90%' cellpadding='1' bgcolor='#000000' cellspacing='1' border='0'>");
					out.print("<tr bgcolor='#EEEEBB'><td>日期</td><td>获取</td><td>使用</td><td>系统回收</td><td>死亡掉落</td></tr>");
					
					long[] lv=new long[5];
					for(int i = 0 ; i < llv.size() ; i++){
					String [] lvStr = llv.get(i);
						out.print("<tr bgcolor='#FFFFFF'><td>"+lvStr[0]+"</td><td>"+lvStr[1]+"</td><td>"+lvStr[2]+"</td><td>"+lvStr[3]+"</td><td>"+lvStr[4]+"</td></tr>");
					       lv[1]+=lvStr[1]==null? 0: Long.parseLong(lvStr[1]);
					       lv[2]+=lvStr[2]==null? 0: Long.parseLong(lvStr[2]);
					       lv[3]+=lvStr[3]==null? 0: Long.parseLong(lvStr[3]);
					       lv[4]+=lvStr[4]==null? 0: Long.parseLong(lvStr[4]);
					   }
					out.print("<tr bgcolor='#EEEEBB'><td>总计</td><td>"+lv[1]+"</td><td>"+lv[2]+"</td><td>"+lv[3]+"</td><td>"+lv[4]+"</td></tr>");
					out.println("</table><br><br/>蓝色酒<br/>");
					
					
					
				    out.println("<table id='test2' align='center' width='90%' cellpadding='1' bgcolor='#000000' cellspacing='1' border='0'>");
					out.print("<tr bgcolor='#EEEEBB'><td>日期</td><td>获取</td><td>使用</td><td>系统回收</td><td>死亡掉落</td></tr>");
					long[] lan = new long[5];
					for(int i = 0 ; i < llan.size() ; i++){
					String [] lanStr = llan.get(i);
					out.print("<tr bgcolor='#FFFFFF'><td>"+lanStr[0]+"</td><td>"+lanStr[1]+"</td><td>"+lanStr[2]+"</td><td>"+lanStr[3]+"</td><td>"+lanStr[4]+"</td></tr>");
					       lan[1]+=lanStr[1]==null? 0: Long.parseLong(lanStr[1]);
					       lan[2]+=lanStr[2]==null? 0: Long.parseLong(lanStr[2]);
					       lan[3]+=lanStr[3]==null? 0: Long.parseLong(lanStr[3]);
					       lan[4]+=lanStr[4]==null? 0: Long.parseLong(lanStr[4]);
					}
					out.print("<tr bgcolor='#EEEEBB'><td>总计</td><td>"+lan[1]+"</td><td>"+lan[2]+"</td><td>"+lan[3]+"</td><td>"+lan[4]+"</td></tr>");
					out.println("</table><br><br/>紫色酒<br/>");
					
					
					out.println("<table id='test2' align='center' width='90%' cellpadding='1' bgcolor='#000000' cellspacing='1' border='0'>");
					out.print("<tr bgcolor='#EEEEBB'><td>日期</td><td>获取</td><td>使用</td><td>系统回收</td><td>死亡掉落</td></tr>");
					
					long[] zi = new long[5];
					for(int i = 0 ; i < lzi.size() ; i++){
					String [] lziStr = lzi.get(i);
						out.print("<tr bgcolor='#FFFFFF'><td>"+lziStr[0]+"</td><td>"+lziStr[1]+"</td><td>"+lziStr[2]+"</td><td>"+lziStr[3]+"</td><td>"+lziStr[4]+"</td></tr>");
					       zi[1]+=lziStr[1]==null? 0: Long.parseLong(lziStr[1]);
					       zi[2]+=lziStr[2]==null? 0: Long.parseLong(lziStr[2]);
					       zi[3]+=lziStr[3]==null? 0: Long.parseLong(lziStr[3]);
					       zi[4]+=lziStr[4]==null? 0: Long.parseLong(lziStr[4]);
					}
					out.print("<tr bgcolor='#EEEEBB'><td>总计</td><td>"+zi[1]+"</td><td>"+zi[2]+"</td><td>"+zi[3]+"</td><td>"+zi[4]+"</td></tr>");
					out.println("</table><br><br/>橙色酒<br/>");
						
					out.println("<table id='test2' align='center' width='90%' cellpadding='1' bgcolor='#000000' cellspacing='1' border='0'>");
					out.print("<tr bgcolor='#EEEEBB'><td>日期</td><td>获取</td><td>使用</td><td>系统回收</td><td>死亡掉落</td></tr>");
					long[] ceng = new long[5];
					for(int i = 0 ; i < lceng.size() ; i++){
					String [] lcengStr = lceng.get(i);
						out.print("<tr bgcolor='#FFFFFF'><td>"+lcengStr[0]+"</td><td>"+lcengStr[1]+"</td><td>"+lcengStr[2]+"</td><td>"+lcengStr[3]+"</td><td>"+lcengStr[4]+"</td></tr>");
					       ceng[1]+=lcengStr[1]==null? 0: Long.parseLong(lcengStr[1]);
					       ceng[2]+=lcengStr[2]==null? 0: Long.parseLong(lcengStr[2]);
					       ceng[3]+=lcengStr[3]==null? 0: Long.parseLong(lcengStr[3]);
					       ceng[4]+=lcengStr[4]==null? 0: Long.parseLong(lcengStr[4]);
					}
					out.print("<tr bgcolor='#EEEEBB'><td>总计</td><td>"+ceng[1]+"</td><td>"+ceng[2]+"</td><td>"+ceng[3]+"</td><td>"+ceng[4]+"</td></tr>");
					out.println("</table><br><br/><br/>");
					}
		    		%>
		    		<div id="regUserContainer" style="width:100%;height:400px;display:block;"></div>
		</center>
		<br>
		<i>这里的用户都是指进入游戏的用户</i>
		<br>
	</body>
</html>
