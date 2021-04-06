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
	String today = DateUtil.formatDate(new Date(),"yyyy-MM-dd");
	if(startDay == null) startDay = today;
	if(endDay == null) endDay = today;
	
              NpcinfoManagerImpl npcinfoManager=NpcinfoManagerImpl.getInstance();
            
            String sql="select pdd, subday,npccount,pcount,round(npccount*100/pcount,3) ratio from "
            +" (select to_char(p.enterdate,'YYYY-MM-DD') pdd,count(distinct(p.username||p.fenqu)) pcount "
            +" from stat_playgame p where  p.enterdate between to_date('"+startDay+" 00:00:00','YYYY-MM-DD hh24:mi:ss') "
            +" and to_date('"+endDay+" 23:59:59','YYYY-MM-DD hh24:mi:ss') and p.maxlevel>149  group by to_char(p.enterdate,'YYYY-MM-DD') "
            +" ) nn   left join ( select to_char(t.createdate,'YYYY-MM-DD') npcdd,count(distinct(t.username||t.fenqu)) npccount,"
            +" case when substr(to_char(t.createdate,'YYYY-MM-DD hh24:mi:ss'),12,2)<15 then '上午' else '下午' end as subday "
            +" from stat_npcinfo t where t.npcname='武神之巅' and t.tasktype='进入' "
            +" and t.createdate between to_date('"+startDay+" 00:00:00','YYYY-MM-DD hh24:mi:ss') and to_date('"+endDay+" 23:59:59','YYYY-MM-DD hh24:mi:ss') "
            +" group by to_char(t.createdate,'YYYY-MM-DD'),case when substr(to_char(t.createdate,'YYYY-MM-DD hh24:mi:ss'),12,2)<15 then '上午' else '下午' end ) mm "
            +" on nn.pdd=mm.npcdd order by nn.pdd desc";
            
            
System.out.println("///sql:"+sql);

              String[] columns= new String[5];
                   columns[0]="pdd";
                   columns[1]="subday";
                   columns[2]="npccount";
                   columns[3]="pcount";
                   columns[4]="ratio";
             
		     List<String []> chongZhiList=npcinfoManager.getNpcinfo(sql,columns);
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
           if(days > 31){
           alert("请确保日期在1个月之内");
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
			武神之巅-参与度统计
		</h2>
		<form  name="form1" id="form1" method="post">
		  开始日期： <input type="text" name="day" id="day" class="nqz_input" style="width:80px;" readonly="readonly" onfocus="new Calendar(false,'yyyy-MM-dd').showTimePicker(this);" value="<%=startDay %>"/>&nbsp;到
		  结束日期： <input type="text" name="endDay" id="endDay" class="nqz_input" style="width:80px;" readonly="readonly" onfocus="new Calendar(false,'yyyy-MM-dd').showTimePicker(this);" value="<%=endDay %>"/>
		 
		   <input type="hidden" name="today" id="today"  value="<%=today %>"/>
		    		<input name="" type="button" onclick="isTwoMons()" value="提交" />
		    		</form><br/><br/>
		    		<h3>武神之巅-参与度统计</h3>
		    		<%
		    		out.println("<table id='test2' align='center' width='90%' cellpadding='1' bgcolor='#000000' cellspacing='1' border='0'>");
					out.print("<tr bgcolor='#EEEEBB'><td>日期</td><td>上/下午</td><td>进入玩家数</td><td>DAU</td><td>进入玩家占比</td></tr>");
					
				for(int i = 0 ; i < chongZhiList.size() ; i++){
					String[] chognzhi=(String[])chongZhiList.get(i);
						out.print("<tr bgcolor='#FFFFFF'><td>"+chognzhi[0]+"</td><td>"+chognzhi[1]+"</td><td>"+chognzhi[2]+"</td><td>"+chognzhi[3]+"</td><td>"+chognzhi[4]+"%</td>");
						out.println("</tr>");
					}
					out.println("</table><br>");
		    		%>
		    		
		        </center>
		        <br>
		<i>这里的用户都是指进入游戏的用户</i>
		<br>
	</body>
</html>