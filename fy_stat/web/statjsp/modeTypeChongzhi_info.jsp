<%@ page contentType="text/html;charset=UTF-8"%>
<%@include file="../inc.jsp"%>
<%@ page
	import="com.xuanzhi.tools.cache.diskcache.concrete.DefaultDiskCache,org.slf4j.*,java.io.*,java.util.*,
	com.xuanzhi.tools.text.*,com.sqage.stat.commonstat.entity.ChongZhi,com.xuanzhi.tools.text.*,
	com.sqage.stat.commonstat.dao.UserDao,com.sqage.stat.commonstat.entity.User,com.sqage.stat.commonstat.manager.UserManager,
	com.sqage.stat.commonstat.manager.Impl.*,com.sqage.stat.service.*,com.sqage.stat.model.Channel"
	%>
  <%
	String startDay = request.getParameter("day");
	String endDay = request.getParameter("endDay");
	String qudao=request.getParameter("qudao");
	String fenqu=request.getParameter("fenqu");
	String type=request.getParameter("type");
	String cardtype=request.getParameter("cardtype");
	
	List<Channel> channelList = (ArrayList<Channel>)session.getAttribute("QUDAO_LIST");
	ArrayList<String []> fenQuList  =  (ArrayList<String []>) session.getAttribute("FENQU_LIST");
	ArrayList<String> typeList=  (ArrayList<String>) session.getAttribute("TYPE_LIST");
	ArrayList<String> cardtypeList =  (ArrayList<String>) session.getAttribute("CARDTYPE_LIST");
	
	if("0".equals(qudao)){qudao=null;}
	if("0".equals(fenqu)){fenqu=null;}
	if("0".equals(type)){type=null;}
	if("0".equals(cardtype)){cardtype=null;}
	
	String today = DateUtil.formatDate(new Date(),"yyyy-MM-dd");
	if(startDay == null) startDay = today;
	if(endDay == null) endDay = today;
	
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
	           {
		         fenQuList= userManager.getFenQu(null);//获得现有的分区信息
		         session.removeAttribute("FENQU_LIST");
		         session.setAttribute("FENQU_LIST", fenQuList);
	           }
	         
	         if(typeList==null){
	              typeList=chongZhiManager.getTypes(null);//获得现有的充值类型
	              session.removeAttribute("TYPE_LIST");
	              session.setAttribute("TYPE_LIST", typeList);
	           }
	         if(cardtypeList==null){
	              cardtypeList=chongZhiManager.getCardTypes();//获得现有的充值卡类型
	              session.removeAttribute("CARDTYPE_LIST");
	              session.setAttribute("CARDTYPE_LIST", cardtypeList);
	           }
              
              String money_search=null;
              String username_search=null;
		     List chongZhiList=chongZhiManager.getModeTypeChongZhi(startDay,endDay,qudao,type,cardtype,fenqu);

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
			手机类型充值查询
		</h2>
		<form  method="post">
		
		<!-- 开始日期：<input type='text' name='day' value='<%=startDay %>'>
		  -- 结束日期：<input type='text' name='endDay' value='<%=endDay %>'>(格式：2012-02-09) -->
		   
		  开始日期： <input type="text" name="day" id="day" class="nqz_input" style="width:80px;" readonly="readonly" onfocus="new Calendar(false,'yyyy-MM-dd').showTimePicker(this);" value="<%=startDay %>"/>&nbsp;到
		  结束日期： <input type="text" name="endDay" id="endDay" class="nqz_input" style="width:80px;" readonly="readonly" onfocus="new Calendar(false,'yyyy-MM-dd').showTimePicker(this);" value="<%=endDay %>"/>
		  
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
                        if(_fenqu[1].equals(fenqu)){
                        out.print(" selected=\"selected\"");
                        }
                         %>
                          ><%=_fenqu[1] %></option>
                       <%
                       }
                       %>
                </select>&nbsp;&nbsp;
                                            充值类型：<select name="type">
                       <option  value="0">全部</option>
                       <% 
                       for(int i = 0 ; i < typeList.size() ; i++){
                       String _type = typeList.get(i);
                       %>
                        <option value="<%=_type %>" 
                        <%
                        if(_type.equals(type)){
                        out.print(" selected=\"selected\"");
                        }
                         %>
                          ><%=_type %></option>
                       <%
                       }
                       %>
                </select>&nbsp;&nbsp;
                
                                            充值卡类型：<select name="cardtype">
                       <option  value="0">全部</option>
                       <% 
                 for(int i = 0 ; i < cardtypeList.size() ; i++){
                       String _cardtype = cardtypeList.get(i);
                       %>
                        <option value="<%=_cardtype %>" 
                        <%
                        if(_cardtype.equals(cardtype)){
                        out.print(" selected=\"selected\"");
                        }
                         %>
                          ><%=_cardtype %></option>
                       <%
                       }
                       %>
                </select>&nbsp;&nbsp;
           
		    		<input type='submit' value='提交'></form><br/>
		    		     <a href="chongzhi_info_page.jsp">充值查询—分页</a>|
		    		     <a href="chongzhi_info.jsp">充值查询-不分页</a>|
		    		     <a href="chongzhi_fenbu.jsp">  充值等级分布</a>|
		    		     <a href="chongzhi_money_fenbu.jsp">充值金额区间分布</a>|
		    		     <a href="qianrensouyi_info.jsp">千人收益</a>|
		    		     <a href="Kri_qianrensouyi_info.jsp">K 日千人收益</a>|
		    		     <a href="Krisouyi_info.jsp">K 日收益</a>|
		    		     <a href="quDaoChongzhi_info.jsp">按渠道充值查询</a>|
		    		     <a href="fenQuChongzhi_info.jsp">按分区充值查询</a>|
		    		     <a href="chongzhi_jine_fenbu.jsp">充值金额分布</a>|
		    		<br/>
		    		<h3>手机类型充值查询(由于手机类型太多，这里只展示前100种手机)</h3>
		    		<%
		    		out.println("<table id='test2' align='center' width='90%' cellpadding='1' bgcolor='#000000' cellspacing='1' border='0'>");
					out.print("<tr bgcolor='#EEEEBB'><td>手机类型</td><td>独立付费次数</td><td>独立付费人数</td><td>金额(分)</td></tr>");
					
					Long times=0L;
					Long usercount=0L;
					Long moneysum=0L;
					
					
					for(int j = 0 ; j < chongZhiList.size() ; j++){
					String[] chognzhi=(String[])chongZhiList.get(j);
					times+=Long.parseLong(chognzhi[1]);
					usercount+=Long.parseLong(chognzhi[2]);
					 moneysum+=Long.parseLong(chognzhi[3]);
					}
					out.print("<tr bgcolor='#EEEEBB'><td>总计</td><td>"+times+"&nbsp;</td><td>"+usercount+"</td><td>"+moneysum+"&nbsp;</td></tr>");
					
					for(int i = 0 ; i < chongZhiList.size() ; i++){
					String[] chognzhi=(String[])chongZhiList.get(i);
						out.print("<tr bgcolor='#FFFFFF'><td>"+chognzhi[0]+"</td><td>"+chognzhi[1]+"</td><td>"+chognzhi[2]+"</td><td>"+chognzhi[3]+"</td>");
						out.println("</tr>");
						if(i==100)break;
					}
					out.print("<tr bgcolor='#EEEEBB'><td>总计</td><td>"+times+"&nbsp;</td><td>"+usercount+"</td><td>"+moneysum+"&nbsp;</td></tr>");
					out.println("</table><br>");
		    		%>
		    		
		    		<div id="regUserContainer" style="width:100%;height:400px;display:block;"></div>
		 
		</center>
		<br>
		<i>这里的用户都是指进入游戏的用户</i>
		<br>
	</body>
</html>
