<%@ page contentType="text/html;charset=UTF-8"%>
<%@include file="../inc.jsp"%>
<%@ page
	import="com.xuanzhi.tools.cache.diskcache.concrete.DefaultDiskCache,org.slf4j.*,java.io.*,java.util.*,
	com.xuanzhi.tools.text.*,com.sqage.stat.commonstat.entity.ChongZhi,com.xuanzhi.tools.text.*,
	com.sqage.stat.commonstat.dao.UserDao,com.sqage.stat.commonstat.entity.User,com.sqage.stat.commonstat.manager.UserManager,
	com.sqage.stat.commonstat.manager.Impl.UserManagerImpl,com.sqage.stat.commonstat.manager.Impl.*,com.sqage.stat.service.*,
	com.sqage.stat.model.Channel"
	%>
  <%
	String startDay = request.getParameter("day");
	//String endDay = request.getParameter("endDay");
	String qudao=request.getParameter("qudao");
	String fenqu=request.getParameter("fenqu");
	List<Channel> channelList = (ArrayList<Channel>)session.getAttribute("QUDAO_LIST");
	ArrayList<String []> fenQuList  =  (ArrayList<String []>) session.getAttribute("FENQU_LIST");
	
	if("0".equals(qudao)){qudao=null;}
	if("0".equals(fenqu)){fenqu=null;}
	
	String today = DateUtil.formatDate(new Date(),"yyyy-MM-dd");
	if(startDay == null) startDay = today;
	
	Date s = DateUtil.parseDate(startDay,"yyyy-MM-dd");
	s.setTime(s.getTime() - 24*3600*1000);
	String lastday = DateUtil.formatDate(s,"yyyy-MM-dd");
	
	/**
	*获得渠道信息
	**/
	          ChannelManager cmanager = ChannelManager.getInstance();
              UserManagerImpl userManager=UserManagerImpl.getInstance();
              ChongZhiManagerImpl chongZhiManager=ChongZhiManagerImpl.getInstance();
             if(channelList==null || channelList.size() == 0)
	           {
		         channelList = cmanager.getChannels();//渠道信息
		         session.removeAttribute("QUDAO_LIST");
		          session.setAttribute("QUDAO_LIST", channelList);
	           }
	         if(fenQuList==null)
	           {  fenQuList= userManager.getFenQu(null);//获得现有的分区信息
	              session.removeAttribute("FENQU_LIST");
	              session.setAttribute("FENQU_LIST", fenQuList);
	                     }
              
              String money_search=null;
              String username_search=null;
		     //List chongZhiList=chongZhiManager.getChongZhiUser(lastday,lastday,qudao,fenqu,null,null);
		      List<String[]> chongZhiList=chongZhiManager.getChongZhiUser(lastday,lastday,qudao,fenqu,null,null);
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
       <script type="text/javascript" language="javascript">
	      function search(username) {
		      //document.getElementById("huoBiType").value=huoBiType;
		      $('form1').action="100Chongzhi_info_xiaofei.jsp?username="+username;
		      $('form1').submit();
	        }
	        
	     function downloads(){
          $('form1').action="100Chongzhi_info_xiaofei_download.jsp";
	      $('form1').submit();
          }
         </script>
	</head>
	<body>
		<center>
		<h2 style="font-weight:bold;">
			百元充用户消费查询
		</h2>
		<form  method="post" id="form1">
		
		<!-- 查询日期：<input type='text' name='day' value='<%=startDay %>'> -->
		查询日期： <input type="text" name="day" id="day" class="nqz_input" style="width:80px;" readonly="readonly" onfocus="new Calendar(false,'yyyy-MM-dd').showTimePicker(this);" value="<%=startDay %>"/>
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
		    		<h3>百元充用户消费查询</h3>
		    		<%
		    		out.println("<table id='test2' align='center' width='90%' cellpadding='1' bgcolor='#000000' cellspacing='1' border='0'>");
					out.print("<tr bgcolor='#EEEEBB'><td>用户名</td><td>前一天付费次数</td><td>前一天金额(分)</td><td>消费情况</td></tr>");
					
					Long moneysum=0L;
					Long usercount=0L;
					
					for(int j = 0 ; j < chongZhiList.size() ; j++){
					String[] chognzhi=(String[])chongZhiList.get(j);
					moneysum+=Long.parseLong(chognzhi[2]);
					usercount+=Long.parseLong(chognzhi[1]);
					}
					
					for(int i = 0 ; i < chongZhiList.size() ; i++){
					String[] chognzhi=(String[])chongZhiList.get(i);
						out.print("<tr bgcolor='#FFFFFF'><td>"+chognzhi[0]+"</td><td>"+chognzhi[1]+"</td><td>"+chognzhi[2]+"</td><td><a href=javascript:search('"+chognzhi[0]+"')>消费情况</a></td>");
						out.println("</tr>");
					}
					out.print("<tr bgcolor='#EEEEBB'><td>总计</td><td>"+usercount+"&nbsp;</td><td>"+moneysum+"</td><td>&nbsp;</td></tr>");
					out.println("</table><br>");
		    		%>
		    		
		    		<div id="regUserContainer" style="width:100%;height:400px;display:block;"></div>
		 
		</center>
		<br>
		<i>这里的用户指输入日期前一天充值达到100元的用户,在前一天和当天的消费情况</i>
		<br>
	</body>
</html>
