<%@ page contentType="text/html;charset=UTF-8" pageEncoding="UTF-8"%>
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
    String gamelevel = request.getParameter("gamelevel");
	String fenqu=request.getParameter("fenqu");
	String task=request.getParameter("task");
	        
	String taskdetail=request.getParameter("taskdetail");//查询活动收益的类型
		if(taskdetail!=null){
		 taskdetail = new String(taskdetail.getBytes("ISO-8859-1"), "UTF-8");//中文参数要转换
	}
	if(gamelevel == null) gamelevel = "全部";
	if("0".equals(fenqu)){fenqu=null;}
	if("0".equals(task)){task=null;}
	
	String today = DateUtil.formatDate(new Date(),"yyyy-MM-dd");
	if(startDay == null) startDay = today;
	if(endDay == null) endDay = today;
	/**
	*获得渠道信息
	**/
	ArrayList<String> taskList = null;
	ArrayList<String> taskTypeList = null;
	
              UserManagerImpl userManager=UserManagerImpl.getInstance();
              ChongZhiManagerImpl chongZhiManager=ChongZhiManagerImpl.getInstance();
              HuoDonginfoManagerImpl huoDonginfoManager=HuoDonginfoManagerImpl.getInstance();
              
              ArrayList<String []> fenQuList= userManager.getFenQu(null);//获得现有的分区信息
              taskList=huoDonginfoManager.gethuoDonginfos(null);//任务
              taskTypeList=huoDonginfoManager.gethuoDonginfoType(null);//任务类型
             
              String gamelevel_search=null;
              if(!"全部".equals(gamelevel)){gamelevel_search=gamelevel;}
             List<String[]>  taskstatList=huoDonginfoManager.gethuoDonginfoShouYi(startDay,endDay,fenqu,task,gamelevel_search);
%>
		
<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN">
<html>
	<head>
	 <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
	
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
	     function search(taskName) {
		     document.getElementById("task").value=taskName;
		      $('form1').action="huodongstatinfo.jsp";
		      $('form1').submit();
	        }
	        
	    function downloads(){
          $('form1').action="huodongstat_download.jsp";
	      $('form1').submit();
          }
         </script>

	</head>
	<body>
		<center>
	     <h2 style="font-weight: bold;">活动完成情况统计 </h2>
		<form name="form1" id="form1" method="post">开始日期：<input type='text' name='day' value='<%=startDay %>'>
		                                          -- 结束日期：<input type='text' name='endDay' value='<%=endDay %>'>(格式：2012-02-09)
		<br/><br/>
		            等级：<input type='text' name='gamelevel' value='<%=gamelevel %>'>&nbsp;&nbsp;
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
                          ><%=_fenqu[1]%></option>
                       <%
                       }
                       %>
                  </select>&nbsp;&nbsp;
                
                                          活动名称：<select name="task" id ="task">
                       <option  value="0">全部</option>
                       <% 
                       for(int i = 0 ; i < taskList.size() ; i++){
                       String _task = taskList.get(i);
                       %>
                        <option value="<%=_task %>" 
                        <%
                        if(_task.equals(task)){
                        out.print(" selected=\"selected\"");
                        }
                         %>
                          ><%=_task %></option>
                       <%
                       }
                       %>
                  </select>&nbsp;&nbsp;
		    		<input type='submit' value='提交'>
		    		<input type="button" name="download" onclick="downloads();" value="下&nbsp;载excel" >
		    		
		    		</form>
		    		<h3>活动完成情况统计</h3>
		    		<%
		    		out.println("<table id='test2' align='center' width='90%' cellpadding='1' bgcolor='#000000' cellspacing='1' border='0'>");
					out.print("<tr bgcolor='#EEEEBB'><td>活动名称</td><td>参与人数</td><td>活动接受</td><td>活动完成</td><td>活动完成率</td></tr>");
					
					for(int i = 0 ; i < taskstatList.size() ; i++){
					String[] taskstat=taskstatList.get(i);
					Long bi=0L;
				    if(taskstat[5]!=null&&!"0".equals(taskstat[5])&&taskstat[6]!=null){
				       bi=Long.parseLong(taskstat[6])*100/Long.parseLong(taskstat[5]);
				       }
				      String tsktail=taskstat[0];
				      tsktail = java.net.URLEncoder.encode(tsktail, "UTF-8");//中文参数要转换
				
				       // String gameleveltail="";
				      // if(!"全部".equals(gamelevel)){gameleveltail=gamelevel;}
				    out.print("<tr bgcolor='#FFFFFF'><td ><a href=javascript:search('"+taskstat[0]+"')>"+taskstat[0]+"</a></td>"+
				              "<td>"+taskstat[1]+"</td><td>"+taskstat[5]+"</td><td>"+taskstat[6]+"</td><td>"+bi+" %&nbsp;</td>");
				    out.println("</tr>");
					}
					Long jieshou=0L;
					Long wancheng=0L;
				for(int i = 0 ; i < taskstatList.size() ; i++){
					String[] taskstat=taskstatList.get(i);
				    if(taskstat[5]!=null&&!"".equals(taskstat[5].trim())){jieshou+=Long.parseLong(taskstat[5]);}
				    if(taskstat[6]!=null&&!"".equals(taskstat[6].trim())){wancheng+=Long.parseLong(taskstat[6]);}
				
					}
					out.print("<tr bgcolor='#EEEEBB'><td>总和</td><td>&nbsp;</td><td>"+jieshou+"</td><td>"+wancheng+"</td><td>&nbsp;</td></tr>");
					out.println("</table><br>");
		    		%>
		    		<div id="regUserContainer" style="width:100%;height:400px;display:block;"></div>
		</center>
		<br>
		<i>这里的用户都是指进入游戏的用户</i>
		<br>
		
	</body>
</html>
