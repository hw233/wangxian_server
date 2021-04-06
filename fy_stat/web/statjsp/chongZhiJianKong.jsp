<%@ page contentType="text/html;charset=UTF-8"%>
<%@include file="../inc.jsp"%>
<%@ page
	import="com.xuanzhi.tools.cache.diskcache.concrete.DefaultDiskCache,org.slf4j.*,java.io.*,java.util.*,com.xuanzhi.tools.text.*,
	org.springframework.context.ApplicationContext,org.springframework.web.context.support.WebApplicationContextUtils,
	com.sqage.stat.commonstat.dao.UserDao,com.sqage.stat.commonstat.entity.User,com.sqage.stat.commonstat.manager.UserManager,
	com.sqage.stat.commonstat.manager.Impl.*,com.sqage.stat.service.*,com.sqage.stat.model.Channel"
	%>
  <%
    String flag = null;
    flag = request.getParameter("flag");
    
	String startDay = request.getParameter("day");
	String fenqu=request.getParameter("fenqu");
	ArrayList<String []> fenQuList  =  (ArrayList<String []>) session.getAttribute("FENQU_LIST");
	
	if("0".equals(fenqu)){fenqu=null;}
	String today = DateUtil.formatDate(new Date(),"yyyy-MM-dd");
	if(startDay == null) startDay = today;
	
	          Date s = DateUtil.parseDate(startDay,"yyyy-MM-dd");
	          Date ss= new Date(s.getTime()-24*60*60*1000);
	          String dayss = DateUtil.formatDate(ss,"yyyy-MM-dd");
	          ArrayList<String> dayList = new ArrayList<String>();
              UserManagerImpl userManager=UserManagerImpl.getInstance();
              ChongZhiManagerImpl chongZhiManager=ChongZhiManagerImpl.getInstance();
              
	         if(fenQuList==null)
	           {
		         fenQuList= userManager.getFenQu(null);//获得现有的分区信息
		         session.removeAttribute("FENQU_LIST");
		         session.setAttribute("FENQU_LIST", fenQuList);
	           }
	         
	//对比各个渠道数据

	ArrayList<String> realChannelList = new ArrayList<String>();
	realChannelList.add("00");
	realChannelList.add("01");
	realChannelList.add("02");
	realChannelList.add("03");
	
	realChannelList.add("04");
	realChannelList.add("05");
	realChannelList.add("06");
	realChannelList.add("07");
	
	realChannelList.add("08");
	realChannelList.add("09");
	realChannelList.add("10");
	realChannelList.add("11");
	
	realChannelList.add("12");
	realChannelList.add("13");
	realChannelList.add("14");
	realChannelList.add("15");
	
	realChannelList.add("16");
	realChannelList.add("17");
	realChannelList.add("18");
	realChannelList.add("19");
	realChannelList.add("20");
	realChannelList.add("21");
	realChannelList.add("22");
	realChannelList.add("23");


     ArrayList<String>  channelList = new ArrayList<String>();
	   channelList.add("APPSTORE_MIESHI");
	    channelList.add("91ZHUSHOU_MIESHI");
	     channelList.add("APP_TMALL");
	      channelList.add("BAIDUYY_MIESHI");
	       channelList.add("360SDK_MIESHI");
	        channelList.add("PPZHUSHOU_MIESHI");
	         channelList.add("youxiqun_MIESHI");
	          channelList.add("BAKA001_MIESHI");
	           channelList.add("APPSTOREGUOJI_MIESHI");
	            channelList.add("DCN_MIESHI");
	             channelList.add("piba_MIESHI");
	               channelList.add("ANZHISDK_MIESHI");
	                channelList.add("kuaiyong_MIESHI");
	                channelList.add("SQAGE_MIESHI");
	                channelList.add("139SDK_MIESHI");
	                channelList.add("wandoujia_MIESHI");
	                channelList.add("UC_MIESHI");
	   
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
			充值分时监控
		</h2>
		<form  method="post">
		  <input type='hidden' name='flag' value='true'/>
		  
		  
		 开始日期： <input type="text" name="day" id="day" class="nqz_input" style="width:100px;" readonly="readonly" onfocus="new Calendar(false,'yyyy-MM-dd').showTimePicker(this);" value="<%=startDay %>"/>&nbsp;
                                             分区：<select name="fenqu">
                       <option  value="0">全部</option>
                       <% 
                       for(int i = 0 ; i < fenQuList.size() ; i++){
                       String[] _fenqu = fenQuList.get(i);
                       %>
                        <option value="<%=_fenqu[1] %>" 
                        <%
                        if(_fenqu[1].equals(fenqu)){ out.print(" selected=\"selected\""); }
                         %>
                          ><%=_fenqu[1] %></option>
                       <%
                       }
                       %>
                </select>&nbsp;&nbsp;
		    	<input type='submit' value='提交'></form>
		    		
		    		<h3>充值分时监控</h3>
		    		<%
		    		out.println("<table id='test2' align='center' width='90%' cellpadding='1' bgcolor='#000000' cellspacing='1' border='0'>");
					out.print("<tr bgcolor='#EEEEBB'><td>钟点</td>");
					
		    		for(int c = 0 ; c < realChannelList.size() ; c++){
		    			out.print("<td>"+realChannelList.get(c)+"点</td>");
		    		}
					out.println("</tr>");
					
					if ("true".equals(flag)) {  //如果是刚打开页面，不查询){
					
					for(int channelnum=0;channelnum<channelList.size();channelnum++){
					List <String []>lss = chongZhiManager.chongZhiJianKong(dayss,channelList.get(channelnum),fenqu);
					List <String []>ls =chongZhiManager.chongZhiJianKong(startDay,channelList.get(channelnum),fenqu);
					
					  
			    		    out.print("<tr  bgcolor='#EEEEBB'>");
			    		    out.print("<td  >"+dayss+channelList.get(channelnum)+"</td>");
			    			for(int c = 0 ; c < realChannelList.size() ; c++){
			    			boolean ishaveValue=false;
			    			for(int j=0;j<lss.size();j++){
			    			if(realChannelList.get(c).equals(lss.get(j)[0].substring(11,13))){
			    			out.print("<td>"+lss.get(j)[1]+"</td>");
			    			ishaveValue=true;
			    			break;
			    			}
			    			}
			    			if(!ishaveValue){out.print("<td >0</td>");}
			    			}
			    			 out.print("</tr>");
			    			 
			    			out.print("<tr  bgcolor='#FFFFFF'>");
			    			out.print("<td  bgcolor='#EEEEBB'>"+startDay+channelList.get(channelnum)+"</td>");
			    			for(int c = 0 ; c < realChannelList.size() ; c++){
			    			boolean ishaveValue=false;
			    			for(int j=0;j<ls.size();j++){
			    			if(realChannelList.get(c).equals(ls.get(j)[0].substring(11,13))){
			    			out.print("<td>"+ls.get(j)[1]+"</td>");
			    			ishaveValue=true;
			    			break;
			    			}
			    			}
			    			if(!ishaveValue){out.print("<td bgcolor='blue'>0</td>");}
			    			}
			    			
						out.println("</tr>");
						}
					
						}
					out.println("</table><br>");
		    		%>
		</center>
		<br>
		<i>这里的用户都是指进入游戏的用户</i>
		<br>
	</body>
</html>
