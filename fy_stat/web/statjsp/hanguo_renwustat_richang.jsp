<%@ page contentType="text/html;charset=UTF-8"%>
<%@include file="../inc.jsp"%>
<%@ page
	import="com.xuanzhi.tools.cache.diskcache.concrete.DefaultDiskCache,org.slf4j.*,java.io.*,java.util.*,
	com.xuanzhi.tools.text.*,com.sqage.stat.commonstat.entity.ChongZhi,com.xuanzhi.tools.text.*,
	com.sqage.stat.commonstat.dao.UserDao,com.sqage.stat.commonstat.entity.User,com.sqage.stat.commonstat.manager.UserManager,
	com.sqage.stat.commonstat.manager.Impl.UserManagerImpl,com.sqage.stat.commonstat.manager.Impl.*"
	%>

  <%
	String startDay = request.getParameter("day");
	String endDay = request.getParameter("endDay");
	String taskType=request.getParameter("taskType");
	String fenqu=request.getParameter("fenqu");
	
	if("0".equals(taskType)){taskType=null;}
	if("0".equals(fenqu)){fenqu=null;}
	
	String today = DateUtil.formatDate(new Date(),"yyyy-MM-dd");
	if(startDay == null) startDay = today;
	if(endDay == null) endDay = today;
	/**
	*获得渠道信息
	**/
	ArrayList<String> taskTypeList = null;
	
              UserManagerImpl userManager=UserManagerImpl.getInstance();
              TaskinfoManagerImpl taskinfoManager=TaskinfoManagerImpl.getInstance();
              TaskAnalysisManagerImpl taskAnalysisManager=TaskAnalysisManagerImpl.getInstance();
              ArrayList<String []> fenQuList= userManager.getFenQu(null);//获得现有的分区信息
              taskTypeList=taskinfoManager.gettaskType(null);//任务类型
             List<String[]>  taskstatList= taskAnalysisManager.getTaskStat(startDay,endDay,fenqu,null);
             
             String renwuArray[][]={
				
 {"方舟建造(楠木)","1"},      
 {"方舟建造(铸铁)","2"},   
 {"方舟建造(胶黏剂)","3"},   
 {"鉴定符兑换","3"},   
 {"2级宝石无相兑换","4"},   
 {"蓝色妖姬兑换","4"}, 
  {"软糖兑换","4"}, 
 {"3级宝石无相兑换","5"},   
 {"3级宝石混沌兑换","5"},   
 {"2级宝石魔渊兑换","6"},   
 {"2级宝石炎焚兑换","6"},   
  {"白玫瑰兑换","6"}, 
  {"棒棒糖兑换","6"}, 
  {"铭刻符兑换","6"}, 
  {"2级宝石混沌兑换","6"}, 
  {"红玫瑰兑换","6"}, 
  {"巧克力兑换","6"}, 
  {"3级宝石魔渊兑换","6"}, 
  {"3级宝石炎焚兑换","6"}};             
             
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
		<script language="JavaScript">
           function downloads(){
               $('form1').action="renwustat_download.jsp";
	           $('form1').submit();
            }
        </script>
	</head>
	<body>
		<center>
		<h2 style="font-weight:bold;">
			日常任务热度统计
		</h2>
		<form   name="form1" id="form1" method="post">开始日期：
		            <input type='text' name='day' value='<%=startDay %>'>
		-- 结束日期：<input type='text' name='endDay' value='<%=endDay %>'>(格式：2012-02-09)
		<br/><br/>
		       任务类型：<select name="taskType">
                       <option  value="0">全部</option>
                       <% 
                       for(int i = 0 ; i < taskTypeList.size() ; i++){
                       String _taskType = taskTypeList.get(i);
                       %>
                        <option value="<%=_taskType %>" 
                        <%
                        if(_taskType.equals(taskType)){
                        out.print(" selected=\"selected\"");
                        }
                         %>
                          ><%=_taskType %></option>
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
                        if(_fenqu[1].equals(fenqu)){  out.print(" selected=\"selected\"");  }
                         %>
                          ><%=_fenqu[1] %></option>
                       <%
                       }
                       %>
                 </select>&nbsp;&nbsp;
                
		    		<input type='submit' value='提交'>
		    		</form>
		    		
		    		       <a href="renwustat_fenxi.jsp">主线任务热度统计</a>|
		    		     <a href="renwustat_fenxi_zhixian.jsp">支线任务热度统计</a>|
		    		     <a href="renwustat_fenxi_jingjie.jsp">境界任务热度统计</a>|
		    		     <a href="renwustat_richang.jsp">日常任务热度统计</a>
		    		
		    		<h3>日常任务热度统计</h3>
		    		<%
		    		out.println("<table id='test2' align='center' width='90%' cellpadding='1' bgcolor='#000000' cellspacing='1' border='0'>");
					out.print("<tr bgcolor='#EEEEBB'><td>任务组名称</td><td>任务组级别</td><td>接受数</td><td>完成数</td><td>完成比</td></tr>");
					Long jieshousum=0L;
					Long wanchengsum=0L;
					
					for(int i = 0 ; i < taskstatList.size() ; i++){
					String[] taskstat=taskstatList.get(i);
					if(taskstat[1]!=null){jieshousum+=Long.parseLong(taskstat[1]);}
					if(taskstat[2]!=null){wanchengsum+=Long.parseLong(taskstat[2]);}
					}
					
					for(int j=0;j<renwuArray.length;j++){
					
					for(int i = 0 ; i < taskstatList.size() ; i++){
					String[] taskstat=taskstatList.get(i);
					Long xiaJiJieshouLV=0L;
					Long bi=0L;
					if(taskstat[0]!=null&&taskstat[0].equals(renwuArray[j][0])){
				    if(taskstat[1]!=null&&!"0".equals(taskstat[1])&&taskstat[2]!=null){
				     bi=Long.parseLong(taskstat[2])*100/Long.parseLong(taskstat[1]);
				     }
				   if(taskstat[2]!=null&&!"0".equals(taskstat[2])&&taskstat[3]!=null){
				   xiaJiJieshouLV=Long.parseLong(taskstat[3])*100/Long.parseLong(taskstat[2]);
				   }
				//out.print("<tr bgcolor='#FFFFFF'><td>"+taskstat[0]+"</td><td>"+renwuArray[j][1]+"</td><td>"+taskstat[1]+"</td><td>"+taskstat[2]+"</td><td>"+bi+"% &nbsp;</td><td>"+taskstat[3]+"</td><td>"+xiaJiJieshouLV+"%</td>");
				
				out.print("<tr bgcolor='#FFFFFF'><td>"+taskstat[0]+"</td><td>"+renwuArray[j][1]+"</td><td>"+taskstat[1]+"</td><td>"+taskstat[2]+"</td><td>"+bi+"% &nbsp;</td>");
				out.println("</tr>");
					}
					}
					}
					
					out.print("<tr bgcolor='#EEEEBB'><td>总计</td><td>&nbsp</td><td>"+jieshousum+"</td><td>"+wanchengsum+"</td><td>&nbsp</td></tr>");
					out.println("</table><br>");
		    		%>
		    		<div id="regUserContainer" style="width:100%;height:400px;display:block;"></div>
		</center>
		<br>
		<i>这里的用户都是指进入游戏的用户</i>
		<br>
	</body>
</html>
