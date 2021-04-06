<%@ page contentType="text/html;charset=UTF-8"%>
<%@ taglib uri="http://displaytag.sf.net/el" prefix="display"%>
<%@ taglib uri="/WEB-INF/c.tld" prefix="c"%>
<%@include file="../inc.jsp"%>
<%@ page
	import="com.xuanzhi.tools.cache.diskcache.concrete.DefaultDiskCache,org.slf4j.*,java.io.*,java.util.*,com.xuanzhi.tools.text.*,
	com.xuanzhi.tools.text.*,com.sqage.stat.commonstat.entity.ChongZhi,com.sqage.stat.commonstat.dao.UserDao,
	com.sqage.stat.commonstat.entity.User,com.sqage.stat.commonstat.manager.UserManager,
	com.sqage.stat.commonstat.manager.Impl.*,com.sqage.stat.service.*,com.sqage.stat.model.Channel"
	%>
  <%
	String startDay = request.getParameter("day");
	String endDay = request.getParameter("endDay");
	String cardtype=request.getParameter("cardtype");
	ArrayList<String> cardtypeList =  (ArrayList<String>) session.getAttribute("CARDTYPE_LIST");
	
	String qudao="kuaiyong_MIESHI";
	if("0".equals(cardtype)){cardtype=null;}
	
	String today = DateUtil.formatDate(new Date(),"yyyy-MM-dd");
	if(startDay == null) startDay = today;
	if(endDay == null) endDay = today;
              UserManagerImpl userManager=UserManagerImpl.getInstance();
              ChongZhiManagerImpl chongZhiManager=ChongZhiManagerImpl.getInstance();
               
              if(cardtypeList==null){
	              cardtypeList=chongZhiManager.getCardTypes();//获得现有的充值卡类型
	              session.removeAttribute("CARDTYPE_LIST");
	              session.setAttribute("CARDTYPE_LIST", cardtypeList);
	           }
		     List<ChongZhi> chongZhiList=chongZhiManager.getChongZhi_cost(startDay,endDay,qudao,null,null,cardtype,null,null,null);
		     long totalmoney=chongZhiManager.getChongZhi_totalMoney_cost(startDay,endDay,qudao,null,null,cardtype,null,null,null);
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
		          
		  开始日期： <input type="text" name="day" id="day" class="nqz_input" style="width:80px;" readonly="readonly" onfocus="new Calendar(false,'yyyy-MM-dd').showTimePicker(this);" value="<%=startDay %>"/>&nbsp;到
		  结束日期： <input type="text" name="endDay" id="endDay" class="nqz_input" style="width:80px;" readonly="readonly" onfocus="new Calendar(false,'yyyy-MM-dd').showTimePicker(this);" value="<%=endDay %>"/>
                    &nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp;
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
              
		    		<input type='submit' value='提交查询'>
		    		
		    		  </form><br/>
		    		<br/><br/> 
		    		<h3>充值总额：<%= totalmoney %> &nbsp;&nbsp;&nbsp;&nbsp;(单位：分)</h3>
		    		
		    		<display:table name="chongZhiList" pagesize="20" id="chognzhi" cellspacing="0" cellpadding="0"  size="100" 
		    		                       style="word-break:break-all; word-wrap: break-word; margin-left: 5px;*margin-left:-1px;">
                     
                      <display:column property="userName" title="账号" style="width: 12%"/>
                      <display:column property="money" title="金额" style="width: 15%"/>
                      <display:column property="gameLevel" title="级别" style="width: 15%"/>
                      <display:column property="fenQu" title="分区" style="width: 10%"/>
                      <display:column property="cardType" title="充值卡类型" style="width: 15%"/>
                      <display:column property="time" title="充值时间" style="width: 20%"/>
                      <display:column property="cost" title="手续费" style="width: 15%"/>
                    
                    </display:table> 
		        </center>
		        <br>
		<i>这里的用户都是指进入游戏的用户</i>
		<br>
	</body>
</html>
