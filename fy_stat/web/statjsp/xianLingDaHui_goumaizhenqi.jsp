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
             
             String sql="select npcdd,npccount,jfcount,round(jfcount*100/npccount,3) ratio from "+
             "( select to_char(t.createdate,'YYYY-MM-DD') npcdd,count(distinct(t.username||t.fenqu)) npccount "
             +"  from stat_npcinfo t where t.npcname='仙灵大会'  and t.tasktype='消耗真气' "+
             " and t.createdate between to_date('"+startDay+" 00:00:00','YYYY-MM-DD hh24:mi:ss') and to_date('"+endDay+" 23:59:59','YYYY-MM-DD hh24:mi:ss') "
             +" group by to_char(t.createdate,'YYYY-MM-DD') ) nn "
             +" left join "
             +" (select to_char(t.createdate,'YYYY-MM-DD') jfdd,count(distinct(t.username||t.fenqu)) jfcount "
             +"  from stat_npcinfo t where t.npcname='仙灵大会'  and t.tasktype='购买真气' "+
             " and t.createdate between to_date('"+startDay+" 00:00:00','YYYY-MM-DD hh24:mi:ss') and to_date('"+endDay+" 23:59:59','YYYY-MM-DD hh24:mi:ss') "
             +" group by to_char(t.createdate,'YYYY-MM-DD')) mm  on nn.npcdd=mm.jfdd ";
            
              String[] columns= new String[4];
                   columns[0]="npcdd";
                   columns[1]="npccount";
                   columns[2]="jfcount";
                   columns[3]="ratio";
             
		     List<String []> chongZhiList=npcinfoManager.getNpcinfo(sql,columns);
		     
		     
		     
		      String sql_zong="select npccount,jfcount,round(jfcount*100/npccount,3) ratio from "+
             "( select count(distinct(t.username||t.fenqu)) npccount "
             +"  from stat_npcinfo t where t.npcname='仙灵大会'  and t.tasktype='消耗真气' "
             +" and t.createdate between to_date('"+startDay+" 00:00:00','YYYY-MM-DD hh24:mi:ss') and to_date('"+endDay+" 23:59:59','YYYY-MM-DD hh24:mi:ss') "
             +"  ) nn "
             +" left join "
             +" (select count(distinct(t.username||t.fenqu)) jfcount "
             +"  from stat_npcinfo t where t.npcname='仙灵大会'  and t.tasktype='购买真气' "
             +" and t.createdate between to_date('"+startDay+" 00:00:00','YYYY-MM-DD hh24:mi:ss') and to_date('"+endDay+" 23:59:59','YYYY-MM-DD hh24:mi:ss') "
             +"  ) mm  on 1=1 ";
            
              String[] columns_zong= new String[3];
                   columns_zong[0]="npccount";
                   columns_zong[1]="jfcount";
                   columns_zong[2]="ratio";
             
		     List<String []> chongZhi_zong=npcinfoManager.getNpcinfo(sql_zong,columns_zong);
		     
		     
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
			仙灵大会-购买真气
		</h2>
		<form  name="form1" id="form1" method="post">
		  开始日期： <input type="text" name="day" id="day" class="nqz_input" style="width:80px;" readonly="readonly" onfocus="new Calendar(false,'yyyy-MM-dd').showTimePicker(this);" value="<%=startDay %>"/>&nbsp;到
		  结束日期： <input type="text" name="endDay" id="endDay" class="nqz_input" style="width:80px;" readonly="readonly" onfocus="new Calendar(false,'yyyy-MM-dd').showTimePicker(this);" value="<%=endDay %>"/>
		 
		   <input type="hidden" name="today" id="today"  value="<%=today %>"/>
		    		<input name="" type="button" onclick="isTwoMons()" value="提交" />
		    		</form><br/><br/>
		    		<h3>仙灵大会-购买真气</h3>
		    		<%
		    		out.println("<table id='test2' align='center' width='90%' cellpadding='1' bgcolor='#000000' cellspacing='1' border='0'>");
					out.print("<tr bgcolor='#EEEEBB'><td>日期</td><td>进入玩家数</td><td>购买真气玩家数</td><td>购买真气玩家占比</td></tr>");
					
				for(int i = 0 ; i < chongZhiList.size() ; i++){
					String[] chognzhi=(String[])chongZhiList.get(i);
						out.print("<tr bgcolor='#FFFFFF'><td>"+chognzhi[0]+"</td><td>"+chognzhi[1]+"</td><td>"+chognzhi[2]+"</td><td>"+chognzhi[3]+"%</td>");
						out.println("</tr>");
					}
					if(chongZhi_zong.size()>0){
					String[] chongZhiList_zong=chongZhi_zong.get(0);
					out.print("<tr bgcolor='#FFFFFF'><td>&nbsp;排重汇总</td><td>"+chongZhiList_zong[0]+"</td><td>"+chongZhiList_zong[1]+"</td><td>"+chongZhiList_zong[2]+"%</td></tr>");
					}
					out.println("</table><br>");
		    		%>
		    		
		        </center>
		        <br>
		<i>规定：输入时间跨度为一个活动周期的时间</i>
		<br>
	</body>
</html>