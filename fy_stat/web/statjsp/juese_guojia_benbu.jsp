<%@ page contentType="text/html;charset=UTF-8"%>
<%@include file="../inc.jsp"%>
<%@ page
	import="com.xuanzhi.tools.cache.diskcache.concrete.DefaultDiskCache,org.slf4j.*,java.io.*,java.util.*,
	com.xuanzhi.tools.text.*,com.sqage.stat.commonstat.entity.ChongZhi,com.xuanzhi.tools.text.*,
	com.sqage.stat.commonstat.dao.UserDao,com.sqage.stat.commonstat.entity.User,com.sqage.stat.commonstat.manager.UserManager,
	com.sqage.stat.commonstat.manager.Impl.UserManagerImpl,com.sqage.stat.commonstat.manager.Impl.*,com.sqage.stat.service.*,com.sqage.stat.model.Channel"
	%>

  <%
	String startDay = request.getParameter("day");
	String endDay = request.getParameter("endDay");
	String qudao=request.getParameter("qudao");
	String fenqu=request.getParameter("fenqu");
	
	if("0".equals(qudao)){qudao=null;}
	if("0".equals(fenqu)){fenqu=null;}
	
	
	List<Channel> channelList = (ArrayList<Channel>)session.getAttribute("QUDAO_LIST");
	ArrayList<String []> fenQuList  =  (ArrayList<String []>) session.getAttribute("FENQU_LIST");
	
	String today = DateUtil.formatDate(new Date(),"yyyy-MM-dd");
	if(startDay == null) startDay = today;
	if(endDay == null) endDay = today;
	
	/**
	*获得渠道信息
	**/
	         // ChannelManager cmanager = ChannelManager.getInstance();
              UserManagerImpl userManager=UserManagerImpl.getInstance();
              ChongZhiManagerImpl chongZhiManager=ChongZhiManagerImpl.getInstance();
              
             // ArrayList<String []> fenQuList= userManager.getFenQuByStatus("0");//获得现有的分区信息
             // List<Channel> channelList = cmanager.getChannels();//渠道信息
              
              
              
               if(channelList==null || channelList.size() == 0)
	           {
	             ChannelManager cmanager = ChannelManager.getInstance();
		         channelList = cmanager.getChannels();//渠道信息
		         session.removeAttribute("QUDAO_LIST");
		         session.setAttribute("QUDAO_LIST", channelList);
	           }
	         if(fenQuList==null)
	           {
		         fenQuList= userManager.getFenQuByStatus("0");;//获得现有的分区信息
		         session.removeAttribute("FENQU_LIST");
		         session.setAttribute("FENQU_LIST", fenQuList);
	           }
             
		    List<String[]>  jsfbList=userManager.getJueSeGuoJiaFenBu(startDay,endDay,null,qudao,fenqu);
		    List<String[]>  jsfbList_new=userManager.getJueSeGuoJiaFenBu(startDay,endDay,"0",qudao,fenqu);
		    Long registUserCount=userManager.getRegistUerCount(startDay,endDay,qudao,null,null);//注册用户数
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
			角色国家分布
		</h2>
		<form  method="post">
		     开始日期： <input type="text" name="day" id="day" class="nqz_input" style="width:100px;" readonly="readonly" onfocus="new Calendar(false,'yyyy-MM-dd').showTimePicker(this);" value="<%=startDay %>"/>&nbsp;到
		         结束日期： <input type="text" name="endDay" id="endDay" class="nqz_input" style="width:100px;" readonly="readonly" onfocus="new Calendar(false,'yyyy-MM-dd').showTimePicker(this);" value="<%=endDay %>"/>
		           
		
		<br/><br/>
		       渠道：<select name="qudao">
                       <option  value="0">全部</option>
                       <% 
                       for(int i = 0 ; i < channelList.size() ; i++){
                       Channel _channel = channelList.get(i);
                       %>
                        <option value="<%=_channel.getKey() %>" 
                        <%
                        if(_channel.getKey().equals(qudao)){
                        out.print(" selected=\"selected\"");
                        }
                         %>
                          ><%=_channel.getName() %></option>
                       <%
                       }
                       %>
                </select> &nbsp;&nbsp;
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
    
		    		<input type='submit' value='提交'></form><br/>
		    		     
		    		<br/>
		    		<h3>角色国家分布</h3>
		    		<%
		    		out.println("<table id='test2' align='center' width='90%' cellpadding='1' bgcolor='#000000' cellspacing='1' border='0'>");
					out.print("<tr bgcolor='#EEEEBB'><td>注册数据</td><td>登陆角色数量</td><td>登陆角色国家分布(无尘/昊天/沧月)</td><td>新增角色数量</td><td>新增角色分布(无尘/昊天/沧月)</td></tr>");
					
					Long sumCount=0L;
					String wu="";
					String shu="";
					String wei="";
					
					Long sumCount_new=0L;
					String wu_new="";
					String shu_new="";
					String wei_new="";
					
					for(int j=0;j<jsfbList.size();j++){
					String [] jsfb=jsfbList.get(j);
					if(jsfb[1]==null){jsfb[1]="0";}
					if("无尘".equals(jsfb[0])){wu=jsfb[1];	    }
					if("昊天".equals(jsfb[0])){wei=jsfb[1];	}
					if("沧月".equals(jsfb[0])){shu=jsfb[1];	}
					sumCount+=Long.parseLong(jsfb[1]);
					}
					
					for(int j=0;j<jsfbList_new.size();j++){
					String [] jsfb_new=jsfbList_new.get(j);
					if(jsfb_new[1]==null){jsfb_new[1]="0";}
					if("无尘".equals(jsfb_new[0])){wu_new=jsfb_new[1];	    }
					if("昊天".equals(jsfb_new[0])){wei_new=jsfb_new[1];	}
					if("沧月".equals(jsfb_new[0])){shu_new=jsfb_new[1];	}
					sumCount_new+=Long.parseLong(jsfb_new[1]);
					}
					
					
					out.print("<tr bgcolor='#FFFFFF'>"+
					                             "<td>"+registUserCount+"</td>"+
					                             "<td>"+sumCount+"</td><td>"+wu+"/"+wei+"/"+shu+"</td>"+
					                             "<td>"+sumCount_new+"</td><td>"+wu_new+"/"+wei_new+"/"+shu_new+"</td>");
					out.println("</tr>");
					out.println("</table><br>");
		    		%>
		</center>
		<br>
		<i>这里的用户都是指进入游戏的用户</i>
		<br>
	</body>
</html>
