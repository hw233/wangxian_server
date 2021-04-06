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
	String fenqu=request.getParameter("fenqu");
	if(tradetype == null){ tradetype = "0";}
	
	String today = DateUtil.formatDate(new Date(),"yyyy-MM-dd");
	if(startDay == null) startDay = today;
	if(endDay == null) endDay = today;
	
	ArrayList<String []> fenQuList  =  (ArrayList<String []>) session.getAttribute("FENQU_LIST");

	  if(fenQuList==null)
	           {
	            UserManagerImpl userManager=UserManagerImpl.getInstance();
		         fenQuList= userManager.getFenQu(null);//获得现有的分区信息
		         session.removeAttribute("FENQU_LIST");
		         session.setAttribute("FENQU_LIST", fenQuList);
	           }
             Transaction_FaceManagerImpl transaction_FaceManager=Transaction_FaceManagerImpl.getInstance();
             
             String newsql=" select to_char(t.createdate,'YYYY-MM-DD') datestr,t.fenqu,t.type,t.gongxun,count(*) count from stat_battle_teamstat t "
             +" where t.createdate between to_date('"+startDay+" 00:00:00','YYYY-MM-DD hh24:mi:ss') and to_date('"+endDay+" 23:59:59','YYYY-MM-DD hh24:mi:ss') ";
            
             if(!"0".equals(fenqu)){newsql += " and t.fenqu = '"+fenqu+"'"; }
             if(!"0".equals(tradetype)){newsql += " and t.type = '"+tradetype+"'"; }
            
             newsql +=" group by to_char(t.createdate,'YYYY-MM-DD'),t.fenqu,t.type,t.gongxun";
   
             
           String[] columns= new String[5];
                   columns[0]="datestr";
                   columns[1]="fenqu";
                   columns[2]="type";
                   columns[3]="gongxun";
                   columns[4]="count";
             
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
			对战团队功勋信息汇总
		</h2>
		<form  name="form1" id="form1" method="post">
		 
		  开始日期： <input type="text" name="day" id="day" class="nqz_input" style="width:80px;" readonly="readonly" onfocus="new Calendar(false,'yyyy-MM-dd').showTimePicker(this);" value="<%=startDay %>"/>&nbsp;到
		  结束日期： <input type="text" name="endDay" id="endDay" class="nqz_input" style="width:80px;" readonly="readonly" onfocus="new Calendar(false,'yyyy-MM-dd').showTimePicker(this);" value="<%=endDay %>"/>
		 
		   <input type="hidden" name="today" id="today"  value="<%=today %>"/>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;
		 
                    对战模式：<select name="tradetype">
                       <option  value="0"  <% if("0".equals(tradetype)){  out.print(" selected=\"selected\""); } %> >全部</option>
                       <option  value="1"  <% if("1".equals(tradetype)){  out.print(" selected=\"selected\""); } %> >1v1</option>
                       <option  value="2"  <% if("2".equals(tradetype)){  out.print(" selected=\"selected\""); } %> >2v2</option>
                       <option  value="3"  <% if("3".equals(tradetype)){  out.print(" selected=\"selected\""); } %> >3v3</option>
                      
                </select>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;
                
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
		    		     
		    		<%
		    		out.println("<table id='test2' align='center' width='90%' cellpadding='1' bgcolor='#000000' cellspacing='1' border='0'>");
					out.print("<tr bgcolor='#EEEEBB'><td>日期</td><td>分区</td><td>类型</td><td>功勋</td><td>数量</td></tr>");
					
					Long moneysum=0L;
					for(int j = 0 ; j < chongZhiList.size() ; j++){
					String[] chognzhi=(String[])chongZhiList.get(j);
					moneysum+=Long.parseLong(chognzhi[4]==null?"0":chognzhi[4]);
					}
					
					for(int i = 0 ; i < chongZhiList.size() ; i++){
					String[] chognzhi=(String[])chongZhiList.get(i);
					out.print("<tr bgcolor='#FFFFFF'><td>"+chognzhi[0]+"</td><td>"+chognzhi[1]+"</td><td>"+chognzhi[2]+"</td><td>"+chognzhi[3]+"</td><td>"+chognzhi[4]+"</td>");
				
						out.println("</tr>");
					}
					out.print("<tr bgcolor='#EEEEBB'><td>总计</td><td>&nbsp;</td><td>&nbsp;</td><td>&nbsp;</td><td>"+moneysum+"</td></tr>");
					out.println("</table><br>");
		    		%>
		    		
		        </center>
		        <br>
		<i>这里的用户都是指进入游戏的用户</i>
		<br>
	</body>
</html>