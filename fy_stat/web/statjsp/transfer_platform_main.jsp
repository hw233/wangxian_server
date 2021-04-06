<%@ page contentType="text/html;charset=UTF-8"%>
<%@include file="../inc.jsp"%>
<%@ page
	import="com.xuanzhi.tools.cache.diskcache.concrete.DefaultDiskCache,org.slf4j.*,java.io.*,java.util.*,com.xuanzhi.tools.text.*,
	com.xuanzhi.tools.text.*,com.sqage.stat.commonstat.entity.ChongZhi,
	com.sqage.stat.commonstat.dao.UserDao,com.sqage.stat.commonstat.entity.User,com.sqage.stat.commonstat.manager.UserManager,
	com.sqage.stat.commonstat.manager.Impl.*,com.sqage.stat.model.Channel"
	%>
  <%
	String startDay = request.getParameter("day");
	String endDay = request.getParameter("endDay");
	String tradetype=request.getParameter("tradetype");
	String qudao=request.getParameter("qudao");
	String fenqu=request.getParameter("fenqu");
	if(tradetype == null){ tradetype = "0";}
	
	String today = DateUtil.formatDate(new Date(),"yyyy-MM-dd");
	if(startDay == null) startDay = today;
	if(endDay == null) endDay = today;
	
	ArrayList<String []> fenQuList  =  (ArrayList<String []>) session.getAttribute("FENQU_LIST");
	List<Channel> channelList = (ArrayList<Channel>)session.getAttribute("QUDAO_LIST");
	  
	  if(channelList==null || channelList.size() == 0)
	           {
	             ChannelManager cmanager = ChannelManager.getInstance();
		         channelList = cmanager.getChannels();//渠道信息
		         session.removeAttribute("QUDAO_LIST");
		         session.setAttribute("QUDAO_LIST", channelList);
	           }
	  if(fenQuList==null)
	           {
	            UserManagerImpl userManager=UserManagerImpl.getInstance();
		         fenQuList= userManager.getFenQu(null);//获得现有的分区信息
		         session.removeAttribute("FENQU_LIST");
		         session.setAttribute("FENQU_LIST", fenQuList);
	           }
             Transaction_FaceManagerImpl transaction_FaceManager=Transaction_FaceManagerImpl.getInstance();
             
             String newsql="select   to_char(t.createtime,'YYYY-MM-DD') day,count(case  when t.responseresult = 1 then 1 end) dealcount, "
             +" sum( case  when t.responseresult = 1 then t.paymoney end) dealmoney,count(*) newcount,sum(t.paymoney) newmoney, "
             +" sum(t.trademoney) tax,sum(t.column1) fee from stat_transfer_platform t "
             +" where t.createtime between to_date('"+startDay+" 00:00:00','YYYY-MM-DD hh24:mi:ss') and to_date('"+endDay+" 23:59:59','YYYY-MM-DD hh24:mi:ss') ";
            
             if(!"0".equals(qudao)){newsql += " and t.sellpassportchannel = '"+qudao+"'"; }
             if(!"0".equals(fenqu)){newsql += " and t.servername = '"+fenqu+"'"; }
             if(!"0".equals(tradetype)){newsql += " and t.tradetype = '"+tradetype+"'"; }
            
            
             newsql +=" group by to_char(t.createtime,'YYYY-MM-DD') order by to_char(t.createtime,'YYYY-MM-DD') desc";
   
            
             
           String[] columns= new String[7];
                   columns[0]="day";
                   columns[1]="dealcount";
                   columns[2]="dealmoney";
                   columns[3]="newcount";
                   columns[4]="newmoney";
                   columns[5]="tax";
                   columns[6]="fee";
             
		     List<String []> chongZhiList=transaction_FaceManager.getTransaction_Face(newsql,columns);
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
           }
          
          else
          
           {
           $('form1').submit();
           }
         }
</script>
	</head>
	<body>
		<center>
		<h2 style="font-weight:bold;">
			平台交易总览
		</h2>
		<form  name="form1" id="form1" method="post">
		 
		  开始日期： <input type="text" name="day" id="day" class="nqz_input" style="width:80px;" readonly="readonly" onfocus="new Calendar(false,'yyyy-MM-dd').showTimePicker(this);" value="<%=startDay %>"/>&nbsp;到
		  结束日期： <input type="text" name="endDay" id="endDay" class="nqz_input" style="width:80px;" readonly="readonly" onfocus="new Calendar(false,'yyyy-MM-dd').showTimePicker(this);" value="<%=endDay %>"/>
		 
		   <input type="hidden" name="today" id="today"  value="<%=today %>"/>
		 
		<br/><br/>
		      
                    交易类型：<select name="tradetype">
                       <option  value="0"  <% if("0".equals(tradetype)){  out.print(" selected=\"selected\""); } %> >全部</option>
                       <option  value="1"  <% if("1".equals(tradetype)){  out.print(" selected=\"selected\""); } %> >道具</option>
                       <option  value="2"  <% if("2".equals(tradetype)){  out.print(" selected=\"selected\""); } %> >银锭</option>
                      
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
                </select> 
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
		    		&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;
		    		<input name="" type="button" onclick="isTwoMons()" value="提交" />
		    		</form><br/>
		    		     
		    		<br/>
		    		<h3>平台交易总览</h3>
		    		<%
		    		out.println("<table id='test2' align='center' width='90%' cellpadding='1' bgcolor='#000000' cellspacing='1' border='0'>");
					out.print("<tr bgcolor='#EEEEBB'><td>日期</td><td>成交笔数</td><td>成交金额</td><td>新挂单笔数</td><td>新挂单金额</td><td>交易税</td><td>手续费</td></tr>");
					
					Long count=0L;
					Long moneysum=0L;
					Long newSubmitCount=0L;
					Long newmoneysum=0L;
					Long tax=0L;
					Long cost=0L;
					
					for(int j = 0 ; j < chongZhiList.size() ; j++){
					String[] chognzhi=(String[])chongZhiList.get(j);
					
					count+=Long.parseLong(chognzhi[1]==null?"0":chognzhi[1]);
					moneysum+=Long.parseLong(chognzhi[2]==null?"0":chognzhi[2]);
					newSubmitCount+=Long.parseLong(chognzhi[3]==null?"0":chognzhi[3]);
					newmoneysum+=Long.parseLong(chognzhi[4]==null?"0":chognzhi[4]);
					
					tax+=Long.parseLong(chognzhi[5]==null?"0":chognzhi[5]);
					cost+=Long.parseLong(chognzhi[6]==null?"0":chognzhi[6]);
					
					
					}
					
					out.print("<tr bgcolor='#EEEEBB'><td>总计</td><td>&nbsp;"+count+"</td><td>&nbsp;"+moneysum+"</td><td>&nbsp;"+newSubmitCount+"</td><td>"+moneysum+"</td><td>&nbsp;"+tax+"</td><td>&nbsp;"+cost+"</td></tr>");
					for(int i = 0 ; i < chongZhiList.size() ; i++){
					String[] chognzhi=(String[])chongZhiList.get(i);
						out.print("<tr bgcolor='#FFFFFF'><td>"+chognzhi[0]+"</td><td>"+chognzhi[1]+"</td><td>"+chognzhi[2]+"</td><td>"+chognzhi[3]+"</td><td>"+chognzhi[4]+"</td><td>"+chognzhi[5]+"</td><td>"+chognzhi[6]+"</td>");
						out.println("</tr>");
					}
					out.print("<tr bgcolor='#EEEEBB'><td>总计</td><td>&nbsp;"+count+"</td><td>&nbsp;"+moneysum+"</td><td>&nbsp;"+newSubmitCount+"</td><td>"+moneysum+"</td><td>&nbsp;"+tax+"</td><td>&nbsp;"+cost+"</td></tr>");
					out.println("</table><br>");
		    		%>
		    		
		        </center>
		        <br>
		<i>这里的用户都是指进入游戏的用户</i>
		<br>
	</body>
</html>