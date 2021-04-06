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
	String task=request.getParameter("task");
	
	if("0".equals(taskType)){taskType=null;}
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
              TaskinfoManagerImpl taskinfoManager=TaskinfoManagerImpl.getInstance();
              
              ArrayList<String []> fenQuList= userManager.getFenQu(null);//获得现有的分区信息
              taskList=taskinfoManager.gettasks(null);//任务
              taskTypeList=taskinfoManager.gettaskType(null);//任务类型
             
             List<String[]>  taskstatList=taskinfoManager.getTaskStat(startDay,endDay,fenqu,task,taskType,"完成");//完成
             
             String renwuArray[][]={
				 {"补缺堵漏颠倒巅","15"},
				 {"退符进火入涌泉","15"},
				 {"凝神静气上灵台","15"},
				 {"三花聚顶汇泥丸","15"},
				 {"脱胎换骨似神仙","15"},
				 {"仗剑而歌斩心魔","21"},
				 {"业净六根成慧眼","21"},
				 {"楚天极目畅辽阔","35"},
				 {"天地浩然有正气","39"},
				 {"访得真君开大智","40"},
				 {"阿难入定待金丹","41"},
				 {"百病不侵度流年","46"},
				 {"普度众生出圣贤","51"},
				 {"虚室生光化紫霜","55"},
				 {"真空一境去凡尘","60"},
				 {"混元桩头铁脚仙","80"},
				 
				 {"调畅气血干沐面","80"},
				 {"阴阳交济成大统","80"},
				 {"潜移默化赤子心","80"},
				 {"霞光满室起莲台","80"},
				 {"乘物游心竞自由","80"},
				 {"纵横寰宇逍遥游","80"},
				 {"御风而行至千里","80"},
				 {"冰原寻仙不辞险","80"},
				 {"道德经里悟非凡","80"},
				 {"水火既济小周天","80"},
				 {"六神合一入毫巅","80"},
				 {"五气朝元归本尊","80"},
				 {"逆转乾坤御强敌","80"},
				 {"九天十地证无极","80"}};     
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
               $('form1').action="renwustat_jingjie_download.jsp";
	           $('form1').submit();
            }
        </script>
	</head>
	<body>
		<center>
		<h2 style="font-weight:bold;">
			任务完成统计
		</h2>
		<form   name="form1" id="form1" method="post">开始日期：<input type='text' name='day' value='<%=startDay %>'>
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
                        if(_fenqu[1].equals(fenqu)){ out.print(" selected=\"selected\"");  }
                         %>
                          ><%=_fenqu[1] %></option>
                       <%
                       }
                       %>
                </select>&nbsp;&nbsp;
                
                                           任务：<select name="task">
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
		    		
		    		  <a href="renwustat.jsp">主线任务完成统计</a>
		    		
		    		
		    		<h3>境界任务完成统计</h3>
		    		<%
		    		out.println("<table id='test2' align='center' width='90%' cellpadding='1' bgcolor='#000000' cellspacing='1' border='0'>");
					out.print("<tr bgcolor='#EEEEBB'><td>任务名称</td><td>接受数</td><td>完成数</td><td>完成比</td></tr>");
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
					Long bi=0L;
					if(taskstat[0].equals(renwuArray[j][0])){
				    if(taskstat[1]!=null&&!"0".equals(taskstat[1])&&taskstat[2]!=null){
				     bi=Long.parseLong(taskstat[2])*100/Long.parseLong(taskstat[1]);
				}
				
				out.print("<tr bgcolor='#FFFFFF'><td>"+taskstat[0]+"</td><td>"+taskstat[1]+"</td><td>"+taskstat[2]+"</td><td>"+bi+"% &nbsp;</td>");
				out.println("</tr>");
					}
					}
					}
					
					out.print("<tr bgcolor='#EEEEBB'><td>总计</td><td>"+jieshousum+"</td><td>"+wanchengsum+"</td><td>&nbsp</td></tr>");
					out.println("</table><br>");
		    		%>
		    		<div id="regUserContainer" style="width:100%;height:400px;display:block;"></div>
		</center>
		<br>
		<i>这里的用户都是指进入游戏的用户</i>
		<br>
	</body>
</html>
