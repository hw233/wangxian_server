<%@ page contentType="text/html;charset=UTF-8"%>
<%@include file="../inc.jsp"%>
<%@ page
	import="com.xuanzhi.tools.cache.diskcache.concrete.DefaultDiskCache,org.slf4j.*,java.io.*,java.util.*,
	com.xuanzhi.tools.text.*,com.sqage.stat.commonstat.entity.ChongZhi,com.xuanzhi.tools.text.*,
	com.sqage.stat.commonstat.dao.UserDao,com.sqage.stat.commonstat.entity.User,com.sqage.stat.commonstat.manager.UserManager,
	com.sqage.stat.commonstat.manager.Impl.UserManagerImpl,com.sqage.stat.commonstat.manager.Impl.*,com.sqage.stat.service.*,com.sqage.stat.model.Channel"
	%>

  <%
	String startDay = request.getParameter("day");
	String fenqu=request.getParameter("fenqu");
	
	if(fenqu==null||"-1".equals(fenqu)){fenqu="龙吟虎啸','南蛮入侵','万箭齐发";}
	
	String today = DateUtil.formatDate(new Date(),"yyyy-MM-dd");
	if(startDay == null) startDay = today;
	
	/**
	*获得渠道信息
	**/
              //UserManagerImpl userManager=UserManagerImpl.getInstance();
              PlayGameManagerImpl playGameManager=PlayGameManagerImpl.getInstance();
              //ArrayList<String []> fenQuList= userManager.getFenQu(null);//获得现有的分区信息
              List<String[]> zaixiaList=playGameManager.getZaiXianShiChangFenBu(startDay,null,fenqu,"30");
              List<String[]> zaixiaList_new=playGameManager.getZaiXianShiChangFenBu_new(startDay,null,fenqu,"30");
		   // Long registUserCount=userManager.getRegistUerCount(startDay,endDay,qudao,null,null);//注册用户数
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

	</head>
	<body>
		<center>
		<h2 style="font-weight:bold;">
			活跃用户在线时长分布
		</h2>
		<form  method="post">开始日期：<input type='text' name='day' value='<%=startDay %>'>(格式：2012-02-09)
		<br/> &nbsp;&nbsp;
                                             分区：<select name="fenqu">
                       <option  value="-1">全部</option>
                    
                       <option value="龙吟虎啸" 
                        <%
                        if("龙吟虎啸".equals(fenqu)){ out.print(" selected=\"selected\""); }
                         %>
                          >龙吟虎啸</option>
                          
                       <option value="南蛮入侵" 
                        <%
                        if("南蛮入侵".equals(fenqu)){ out.print(" selected=\"selected\""); }
                         %>
                          >南蛮入侵</option>
                          
                       <option value="万箭齐发" 
                        <%
                        if("万箭齐发".equals(fenqu)){ out.print(" selected=\"selected\""); }
                         %>
                          >万箭齐发</option>
                   </select>
    
		    		<input type='submit' value='提交'></form><br/>
		    		     
		    		<br/>
		    		<h3>活跃用户在线时长分布</h3>
		    		<%
		    		out.println("<table id='test2' align='center' width='90%' cellpadding='1' bgcolor='#000000' cellspacing='1' border='0'>");
					out.print("<tr bgcolor='#EEEEBB'><td>在线时长(小时)</td><td>[0,0.5)</td><td>[0.5,1)</td><td>[1,1.5)</td>"+
					"<td>[1.5,2)</td><td>[2,3)</td><td>[3,4)</td><td>4小时以上</td></tr>");
					
					Long unum05=0L;//0到半小时内人数
					Long unum10=0L;//0.5-1个小时内人数
					Long unum15=0L;//1-1.5小时内人数
					Long unum20=0L;
					Long unum30=0L;
					Long unum40=0L;
					Long unum100=0L;
					////////////////新用户在线时长分布
					Long unum05_new=0L;//0到半小时内人数
					Long unum10_new=0L;//0.5-1个小时内人数
					Long unum15_new=0L;//1-1.5小时内人数
					Long unum20_new=0L;
					Long unum30_new=0L;
					Long unum40_new=0L;
					Long unum100_new=0L;
					
					Long unum05_old=0L;//0到半小时内人数
					Long unum10_old=0L;//0.5-1个小时内人数
					Long unum15_old=0L;//1-1.5小时内人数
					Long unum20_old=0L;
					Long unum30_old=0L;
					Long unum40_old=0L;
					Long unum100_old=0L;
					
					
					for(int j=0;j<zaixiaList.size();j++){
					String [] zaixian=zaixiaList.get(j);
					if("0".equals(zaixian[0])){if(zaixian[1]==null){zaixian[1]="0"; } unum05+=Long.parseLong(zaixian[1]);}else
					if("1".equals(zaixian[0])){if(zaixian[1]==null){zaixian[1]="0"; } unum10+=Long.parseLong(zaixian[1]);}else
					if("2".equals(zaixian[0])){if(zaixian[1]==null){zaixian[1]="0"; } unum15+=Long.parseLong(zaixian[1]);}else
					if("3".equals(zaixian[0])){if(zaixian[1]==null){zaixian[1]="0"; } unum20+=Long.parseLong(zaixian[1]);}else
					if("4".equals(zaixian[0])||"5".equals(zaixian[0])){if(zaixian[1]==null){zaixian[1]="0"; } unum30+=Long.parseLong(zaixian[1]);}else
					if("6".equals(zaixian[0])||"7".equals(zaixian[0])){if(zaixian[1]==null){zaixian[1]="0"; } unum40+=Long.parseLong(zaixian[1]);}else
					{
					if(zaixian[1]==null){zaixian[1]="0"; } unum100+=Long.parseLong(zaixian[1]);
					}
					}
					
					for(int j=0;j<zaixiaList_new.size();j++){
					String [] zaixian_new=zaixiaList_new.get(j);
					if("0".equals(zaixian_new[0])){if(zaixian_new[1]==null){zaixian_new[1]="0"; } unum05_new+=Long.parseLong(zaixian_new[1]);}else
					if("1".equals(zaixian_new[0])){if(zaixian_new[1]==null){zaixian_new[1]="0"; } unum10_new+=Long.parseLong(zaixian_new[1]);}else
					if("2".equals(zaixian_new[0])){if(zaixian_new[1]==null){zaixian_new[1]="0"; } unum15_new+=Long.parseLong(zaixian_new[1]);}else
					if("3".equals(zaixian_new[0])){if(zaixian_new[1]==null){zaixian_new[1]="0"; } unum20_new+=Long.parseLong(zaixian_new[1]);}else
					if("4".equals(zaixian_new[0])||"5".equals(zaixian_new[0])){if(zaixian_new[1]==null){zaixian_new[1]="0"; } unum30_new+=Long.parseLong(zaixian_new[1]);}else
					if("6".equals(zaixian_new[0])||"7".equals(zaixian_new[0])){if(zaixian_new[1]==null){zaixian_new[1]="0"; } unum40_new+=Long.parseLong(zaixian_new[1]);}else
					{
					if(zaixian_new[1]==null){zaixian_new[1]="0"; } unum100_new+=Long.parseLong(zaixian_new[1]);
					}
					}
					
					unum05_old=unum05-unum05_new;//0到半小时内人数
					unum10_old=unum10-unum10_new;//0.5-1个小时内人数
					unum15_old=unum15-unum15_new;//1-1.5小时内人数
					unum20_old=unum20-unum20_new;
					unum30_old=unum30-unum30_new;
					unum40_old=unum40-unum40_new;
					unum100_old=unum100-unum100_new;
					
					
					out.print("<tr bgcolor='#FFFFFF'>"+
					                             "<td>用户数</td>"+
					                             "<td>"+unum05+"</td><td>"+unum10+"</td>"+
					                             "<td>"+unum15+"</td><td>"+unum20+"</td>"+
					                             "<td>"+unum30+"</td><td>"+unum40+"</td>"+
					                             "<td>"+unum100+"</td>");
					                             out.print("<tr bgcolor='#FFFFFF'>"+
					                             "<td>新用户数</td>"+
					                             "<td>"+unum05_new+"</td><td>"+unum10_new+"</td>"+
					                             "<td>"+unum15_new+"</td><td>"+unum20_new+"</td>"+
					                             "<td>"+unum30_new+"</td><td>"+unum40_new+"</td>"+
					                             "<td>"+unum100_new+"</td>");
					                              out.print("<tr bgcolor='#FFFFFF'>"+
					                             "<td>老用户数</td>"+
					                             "<td>"+unum05_old+"</td><td>"+unum10_old+"</td>"+
					                             "<td>"+unum15_old+"</td><td>"+unum20_old+"</td>"+
					                             "<td>"+unum30_old+"</td><td>"+unum40_old+"</td>"+
					                             "<td>"+unum100_old+"</td>");
					out.println("</tr>");
					out.println("</table><br>");
		    		%>
		</center>
		<br>
		<i>这里的用户都是指进入游戏的用户</i>
		<br>
	</body>
</html>
