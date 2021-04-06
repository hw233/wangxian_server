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
              ChongZhiManagerImpl chongZhiManager=ChongZhiManagerImpl.getInstance();
              TaskinfoManagerImpl taskinfoManager=TaskinfoManagerImpl.getInstance();
              TaskAnalysisManagerImpl taskAnalysisManager=TaskAnalysisManagerImpl.getInstance();
              ArrayList<String []> fenQuList= userManager.getFenQu(null);//获得现有的分区信息
              taskTypeList=taskinfoManager.gettaskType(null);//任务类型
             
             List<String[]>  taskstatList= taskAnalysisManager.getTaskStat(startDay,endDay,fenqu,null);
             
             String renwuArray[][]={
             {"부족함을 채우다","15","补缺堵漏颠倒巅"}, 
{"퇴부진화입용천","15","退符进火入涌泉"}, 
{"영대로 오르다","15","凝神静气上灵台"}, 
{"삼화로 단약을 만들다","15","三花聚顶汇泥丸"}, 
{"신선으로 되는 길","15","脱胎换骨似神仙"}, 
{"마음의 악마를 베다","21","仗剑而歌斩心魔"}, 
{"업정륙근성혜안","21","业净六根成慧眼"}, 
{"쇄심마를 처치하다","35","楚天极目畅辽阔"}, 
{"천지의 정의","39","天地浩然有正气"}, 
{"진군의 큰 지혜","40","访得真君开大智"}, 
{"금단을 기다리다","41","阿难入定待金丹"}, 
{"백병부침도유년","46","百病不侵度流年"}, 
{"태화신산을 처치하다","51","普度众生出圣贤"}, 
{"보라색 빛을 보다","55","虚室生光化紫霜"}, 
{"잡념을 털다","60","真空一境去凡尘"}, 
{"혼원장두공","80","混元桩头铁脚仙"}, 
{"조창기혈간목면","80","调畅气血干沐面"}, 
{"음양교차","80","阴阳交济成大统"}, 
{"금단 비법 획득","80","潜移默化赤子心"}, 
{"건곤마동 격파","80","霞光满室起莲台"}, 
{"원신 출규를 기대한다","80","乘物游心竞自由"}, 
{"종횡환우소요유","80","纵横寰宇逍遥游"}, 
{"바람타고 천리길","80","御风而行至千里"}, 
{"위험을 감수하다","80","冰原寻仙不辞险"}, 
{"원신 출규 획득","80","道德经里悟非凡"}, 
{"원신 방황","80","水火既济小周天"}, 
{"륙신합일입호전","80","六神合一入毫巅"}, 
{"오기조원 본존으로 합체","80","五气朝元归本尊"}, 
{"건곤역전","80","逆转乾坤御强敌"}, 
{"구천십지로 무극을 증명","80","九天十地证无极"}, 
{"금단 임무 1별","80","金丹任务1星"}, 
{"금단 임무 2별","80","金丹任务2星"}, 
{"금단 임무 3별","80","金丹任务3星"}, 
{"금단 임무 4별","80","金丹任务4星"}, 
{"금단 임무 5별","80","金丹任务5星"}, 
{"금단 임무 6별","80","金丹任务6星"}, 
{"금단 임무 7별","80","金丹任务7星"}, 
{"금단 임무 8별","80","金丹任务8星"}, 
{"금단 임무 9별","80","金丹任务9星"}, 
{"금단 임무 10별","80","金丹任务10星"}
};             
             
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
			境界任务完成统计
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
                
		    		<input type='submit' value='提交'>
		    		</form>
		    		
		    		     <a href="renwustat_fenxi.jsp">主线任务热度统计</a>|
		    		     <a href="renwustat_fenxi_zhixian.jsp">支线任务热度统计</a>|
		    		     <a href="renwustat_fenxi_jingjie.jsp">境界任务热度统计</a>|
		    		
		    		<h3>任务完成统计</h3>
		    		<%
		    		out.println("<table id='test2' align='center' width='90%' cellpadding='1' bgcolor='#000000' cellspacing='1' border='0'>");
					out.print("<tr bgcolor='#EEEEBB'><td>任务组名称</td><td>任务组别名</td><td>接受数</td><td>完成数</td></tr>");
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
					if(taskstat[0]!=null&&taskstat[0].equals(renwuArray[j][0])){
				    if(taskstat[1]!=null&&!"0".equals(taskstat[1])&&taskstat[2]!=null){
				     bi=Long.parseLong(taskstat[2])*100/Long.parseLong(taskstat[1]);
				}
				
				out.print("<tr bgcolor='#FFFFFF'><td>"+taskstat[0]+"</td><td>"+renwuArray[j][2]+"</td><td>"+taskstat[1]+"</td><td>"+taskstat[2]+"</td><td>"+bi+"% &nbsp;</td>");
				out.println("</tr>");
					}
					}
					}
					
					out.print("<tr bgcolor='#EEEEBB'><td>总计</td><td>&nbsp;</td><td>"+jieshousum+"</td><td>"+wanchengsum+"</td><td>&nbsp</td><td>&nbsp</td></tr>");
					out.println("</table><br>");
		    		%>
		    		<div id="regUserContainer" style="width:100%;height:400px;display:block;"></div>
		</center>
		<br>
		<i>这里的用户都是指进入游戏的用户</i>
		<br>
	</body>
</html>
