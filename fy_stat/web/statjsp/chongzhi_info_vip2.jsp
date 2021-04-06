<%@ page contentType="text/html;charset=UTF-8"%>
<%@include file="../inc.jsp"%>
<%@ page
	import="com.xuanzhi.tools.cache.diskcache.concrete.DefaultDiskCache,org.slf4j.*,java.io.*,java.util.*,com.xuanzhi.tools.text.*,
	com.xuanzhi.tools.text.*,com.sqage.stat.commonstat.entity.ChongZhi,
	com.sqage.stat.commonstat.dao.UserDao,com.sqage.stat.commonstat.entity.User,com.sqage.stat.commonstat.manager.UserManager,
	com.sqage.stat.commonstat.manager.Impl.*,com.sqage.stat.model.Channel"
	%>
  <%
  out.print(response.getContentType());
  String ls=request.getParameter("lr");
  out.print("test:"+ls);
  // PrintWriter wr= response.getWriter();
  //  wr.toString();
 
	String startDay = request.getParameter("day");
	String endDay = request.getParameter("endDay");
	String vip=request.getParameter("vip");
	String moneyStart = request.getParameter("moneyStart");
	String moneyEnd = request.getParameter("moneyEnd");
	String qudao=request.getParameter("qudao");
	//if("0".equals(qudao)){qudao=null;}
	if(moneyStart == null) moneyStart = "0";
	if(moneyEnd == null) moneyEnd = "0";
	if(vip == null){ vip = "15";}
	
	String today = DateUtil.formatDate(new Date(),"yyyy-MM-dd");
	if(startDay == null) startDay = today;
	if(endDay == null) endDay = today;
	 
	List<Channel> channelList = (ArrayList<Channel>)session.getAttribute("QUDAO_LIST");
	  if(channelList==null || channelList.size() == 0)
	           {
	             ChannelManager cmanager = ChannelManager.getInstance();
		         channelList = cmanager.getChannels();//渠道信息
		         session.removeAttribute("QUDAO_LIST");
		         session.setAttribute("QUDAO_LIST", channelList);
	           }
              ChongZhiManagerImpl chongZhiManager=ChongZhiManagerImpl.getInstance();
            
            String sql="select  c.username,c.fenqu,c.playname,c.qudao,sum(c.money)/100 money,max(c.vip) vip from stat_chongzhi c "
              +" where c.time between to_date('"+startDay+" 00:00:00','YYYY-MM-DD hh24:mi:ss') and to_date('"+endDay+" 23:59:59','YYYY-MM-DD hh24:mi:ss') ";
             
            if(!"0".equals(qudao)){sql += " and c.qudao = '"+qudao+"'"; }
             sql +=" group by c.username,c.fenqu,c.playname,c.qudao  having max(c.vip)="+vip
              +" and sum(c.money)/100 between "+moneyStart+" and "+moneyEnd
              +" order by sum(c.money) desc";

              System.out.println("sql:"+sql);
              String[] columns= new String[6];
                   columns[0]="username";
                   columns[1]="fenqu";
                   columns[2]="playname";
                   columns[3]="qudao";
                   columns[4]="money";
                   columns[5]="vip";
                   List<String []> chongZhiList=new ArrayList<String []>();
		      chongZhiList=chongZhiManager.getChongZhiInfo(sql,columns);
		    
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
        
         function isTwoMons(){
           var startdate = document.getElementById("day").value;
           var enddate = document.getElementById("today").value;
           
           var startD = new Date(Date.parse(startdate.replace(/-/g,"/")));
           var endD   = new Date(Date.parse(enddate.replace(/-/g,"/")));
           var days = parseInt((endD.getTime()-startD.getTime()) / (1000 * 60 * 60 * 24));
            if(startdate>enddate)
           {
            alert("亲，你输入的开始日期晚于终止日期。");
            return false;
           } else
           if(days > 61){
           alert("请确保日期在2个月之内");
           return false;
          } else
          
           {
           $('form1').submit();
           }
         }
</script>
	</head>
	<body>
		<center>
		<h2 style="font-weight:bold;">
			高级VIP充值查询
		</h2>
		<form  name="form1" id="form1" method="post" action="http://127.0.0.1:8080/stat_common_mieshi/VipTransferServlet">
		 
		  开始日期： <input type="text" name="day" id="day" class="nqz_input" style="width:80px;" readonly="readonly" onfocus="new Calendar(false,'yyyy-MM-dd').showTimePicker(this);" value="<%=startDay %>"/>&nbsp;到
		  结束日期： <input type="text" name="endDay" id="endDay" class="nqz_input" style="width:80px;" readonly="readonly" onfocus="new Calendar(false,'yyyy-MM-dd').showTimePicker(this);" value="<%=endDay %>"/>
		 
		   <input type="hidden" name="today" id="today"  value="<%=today %>"/>
		 
		<br/><br/>
		      
                VIP级别：<select name="vip">
                       <option  value="6"  <% if("6".equals(vip)){  out.print(" selected=\"selected\""); } %> >vip6</option>
                       <option  value="7"  <% if("7".equals(vip)){  out.print(" selected=\"selected\""); } %> >vip7</option>
                       <option  value="8"  <% if("8".equals(vip)){  out.print(" selected=\"selected\""); } %>>vip8</option>
                       <option  value="9"  <% if("9".equals(vip)){  out.print(" selected=\"selected\""); } %>>vip9</option>
                       <option  value="10"  <% if("10".equals(vip)){  out.print(" selected=\"selected\""); } %>>vip10</option>
                       <option  value="11"  <% if("11".equals(vip)){  out.print(" selected=\"selected\""); } %>>vip11</option>
                       <option  value="12"  <% if("12".equals(vip)){  out.print(" selected=\"selected\""); } %>>vip12</option>
                       <option  value="13"  <% if("13".equals(vip)){  out.print(" selected=\"selected\""); } %>>vip13</option>
                       <option  value="14"  <% if("14".equals(vip)){  out.print(" selected=\"selected\""); } %>>vip14</option>
                       <option  value="15"  <% if("15".equals(vip)){  out.print(" selected=\"selected\""); } %>>vip15</option>
                    
                </select>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;
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
                
                             充值金额：<input type='text' name='moneyStart' value='<%=moneyStart%>' style="width:80px;"> &nbsp;元 &nbsp;TO &nbsp; &nbsp;
                           <input type='text' name='moneyEnd' value='<%=moneyEnd%>' style="width:80px;">元
                             
		    		&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;
		    		 <input name="" type="button" onclick="isTwoMons()" value="提交" />
		    		<!-- <button type="button">Click Me!</button> -->
		    		
		    		</form><br/>
		    		     
		    		<br/>
		    		<h3>高级VIP充值查询</h3>
		    		<%
		    		out.println("<table id='test2' align='center' width='90%' cellpadding='1' bgcolor='#000000' cellspacing='1' border='0'>");
					out.print("<tr bgcolor='#EEEEBB'><td>账号</td><td>分区</td><td>角色名</td><td>渠道</td><td>金额</td><td>vip等级</td></tr>");
					
					Long moneysum=0L;
					for(int j = 0 ; j < chongZhiList.size() ; j++){
					String[] chognzhi=(String[])chongZhiList.get(j);
					moneysum+=Long.parseLong(chognzhi[4]==null?"0":chognzhi[4]);
					}
					
					out.print("<tr bgcolor='#EEEEBB'><td>总计</td><td>&nbsp;</td><td>&nbsp;</td><td>&nbsp;</td><td>"+moneysum+"</td><td>&nbsp;</td></tr>");
					for(int i = 0 ; i < chongZhiList.size() ; i++){
					String[] chognzhi=(String[])chongZhiList.get(i);
						out.print("<tr bgcolor='#FFFFFF'><td>"+chognzhi[0]+"</td><td>"+chognzhi[1]+"</td><td>"+chognzhi[2]+"</td><td>"+chognzhi[3]+"</td><td>"+chognzhi[4]+"</td><td>"+chognzhi[5]+"</td>");
						out.println("</tr>");
					}
					out.print("<tr bgcolor='#EEEEBB'><td>总计</td><td>&nbsp;</td><td>&nbsp;</td><td>&nbsp;</td><td>"+moneysum+"</td><td>&nbsp;</td></tr>");
					out.println("</table><br>");
		    		%>
		    		
		        </center>
		        <br>
		<i>这里的用户都是指进入游戏的用户</i>
		<br>
	</body>
</html>