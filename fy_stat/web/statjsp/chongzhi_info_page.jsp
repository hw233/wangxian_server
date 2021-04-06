<%@ page contentType="text/html;charset=UTF-8"%>
<%@ taglib uri="http://displaytag.sf.net/el" prefix="display"%>
<%@ taglib uri="/WEB-INF/c.tld" prefix="c"%>
<%@include file="../inc.jsp"%>
<%@ page
	import="com.xuanzhi.tools.cache.diskcache.concrete.DefaultDiskCache,org.slf4j.*,java.io.*,java.util.*,com.xuanzhi.tools.text.*,
	com.xuanzhi.tools.text.*,com.sqage.stat.commonstat.entity.ChongZhi,
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
	String username = request.getParameter("username");
	String money = request.getParameter("money");
	String jixing = request.getParameter("jixing");
	String modeType = request.getParameter("modeType");
	    
	List<Channel> channelList = (ArrayList<Channel>)session.getAttribute("QUDAO_LIST");
	ArrayList<String []> fenQuList  =  (ArrayList<String []>) session.getAttribute("FENQU_LIST");
	ArrayList<String> typeList=  (ArrayList<String>) session.getAttribute("TYPE_LIST");
	ArrayList<String> cardtypeList =  (ArrayList<String>) session.getAttribute("CARDTYPE_LIST");
	
	
	if("0".equals(jixing)){jixing=null;}
	if("0".equals(qudao)){qudao=null;}
	if("0".equals(fenqu)){fenqu=null;}
	if("0".equals(type)){type=null;}
	if("0".equals(cardtype)){cardtype=null;}
	if("0".equals(modeType)){modeType=null;}
	if(username == null) username = "全部";
	if(money == null) money = "全部";
	
	
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
		         fenQuList= userManager.getFenQuByStatus("0");//获得现有的分区信息
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
              if(!"全部".equals(username)){username_search=username;}
              if(!"全部".equals(money)){money_search=money;}
              
              
		     List<ChongZhi> chongZhiList=chongZhiManager.getChongZhi(startDay,endDay,qudao,fenqu,type,cardtype,username_search,money_search,jixing,modeType);
		     long totalmoney=chongZhiManager.getChongZhi_totalMoney(startDay,endDay,qudao,fenqu,type,cardtype,username_search,money_search,jixing,modeType);
		     
             request.setAttribute("chongZhiList",chongZhiList); 
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
		<script type="text/javascript" src="<%=request.getContextPath()%>/comfile/js/common_displaytagURL.js"></script>


     <script language="JavaScript">
        function downloads(){
         $('form1').action="chongzhi_info_download.jsp";
	     $('form1').submit();
         }
</script>
	</head>
	<body>
		<center>
		<h2 style="font-weight:bold;">
			充值查询
		</h2>
		<form  name="form1" id="form1" method="post">
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
                        if(_fenqu[1].equals(fenqu)){ out.print(" selected=\"selected\"");  }
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
                  </select>
                  <br>
                  
		        
                
                             玩家账号：<input type='text' name='username' value='<%=username %>'>&nbsp;&nbsp;
                             充值金额：<input type='text' name='money' value='<%=money %>'>
                             
		    		<input type='submit' value='提交查询'>
		    		<input type="button" name="download" onclick="downloads();" value="下&nbsp;载excel" >
		    		
		    		  </form><br/>
		    		  <!-- 
		    		     <a href="chongzhi_info_page.jsp">充值查询—分页</a>|
		    		     <a href="chongzhi_info.jsp">充值查询-不分页</a>|
		    		     <a href="chongzhi_fenbu.jsp">  充值等级分布</a>|
		    		      -->
		    		     <a href="chongzhi_money_fenbu.jsp">充值金额区间分布</a>|
		    		     <a href="qianrensouyi_info.jsp">千人收益</a>|
		    		     <a href="Kri_qianrensouyi_info.jsp">K 日千人收益</a>|
		    		     <a href="Krisouyi_info.jsp">K 日收益</a>|
		    		     <a href="quDaoChongzhi_info.jsp">按渠道充值查询</a>|
		    		     <a href="fenQuChongzhi_info.jsp">按分区充值查询</a>|
		    		     <a href="chongzhi_jine_fenbu.jsp">充值金额分布</a>|
		    		      <a href="modeTypeChongzhi_info.jsp">按手机类型充值查询</a>
		    		      
		    		     <a href="modeTypeDateChongzhi_info.jsp">apple按手机类型充值查询</a>
		    		      
		    		     <br>
		    		    
		    		     
		    		     
		    		     
		    		     
		    		<br/><br/> 
		    		<h3>充值总额：<%= totalmoney %> &nbsp;&nbsp;&nbsp;&nbsp;(单位：分)</h3>
		    		
		    		<display:table name="chongZhiList" pagesize="20" id="chognzhi" cellspacing="0" cellpadding="0"  size="100" 
		    		                       style="word-break:break-all; word-wrap: break-word; margin-left: 5px;*margin-left:-1px;">
                     
                      <display:column property="userName" title="账号" style="width: 14%"/>
                      <display:column property="money" title="金额" style="width: 8%"/>
                      <display:column property="gameLevel" title="级别"/>
                      <display:column property="fenQu" title="分区"/>
                      <display:column property="quDao" title="渠道" style="width: 11%"/>
                      <display:column property="type" title="充值类型"/>
                      <display:column property="cardType" title="充值卡类型"/>
                      <display:column property="time" title="充值时间" style="width: 14%"/>
                      <display:column property="jixing" title="平台"/>
                      <display:column property="cost" title="手续费"/>
                      
                    
                    </display:table> 
		        </center>
		        <br>
		<i>这里的用户都是指进入游戏的用户</i>
		<br>
	</body>
</html>
