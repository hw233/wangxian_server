<%@ page contentType="text/html;charset=UTF-8"%>
<%@include file="../inc.jsp"%>
<%@ page
	import="com.xuanzhi.tools.cache.diskcache.concrete.DefaultDiskCache,org.slf4j.*,java.io.*,java.util.*,
	com.xuanzhi.tools.text.*,com.sqage.stat.commonstat.entity.ChongZhi,com.xuanzhi.tools.text.*,
	com.sqage.stat.commonstat.dao.UserDao,com.sqage.stat.commonstat.entity.User,com.sqage.stat.commonstat.manager.UserManager,
    com.sqage.stat.commonstat.manager.Impl.*,com.sqage.stat.language.*"
	%>
	<%!
	Object lock = new Object(){};
      %>
  <%
	String startDay = request.getParameter("day");
	String endDay = request.getParameter("endDay");
	String fenqu=request.getParameter("fenqu");
	if("0".equals(fenqu)){fenqu=null;}

	
	String today = DateUtil.formatDate(new Date(),"yyyy-MM-dd");
	if(startDay == null) startDay = today;
	if(endDay == null) endDay = today;
	
	ArrayList<String> daojuList=null;
	
              UserManagerImpl userManager=UserManagerImpl.getInstance();
              TransactionManagerImpl transactionManager=TransactionManagerImpl.getInstance();
              ArrayList<String []> fenQuList= userManager.getFenQu(null);//获得现有的分区信息
              List<String []> ls=transactionManager.getDaoJuName(startDay,endDay,fenqu);
              
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
			道具交易统计--获取方式
		</h2>
		<form name="form1" id="form1" method="post" >开始日期：<input type='text' name='day' value='<%=startDay %>'>
		<input type="hidden" id="daojuname" name="daojuname" value=""/>
		-- 结束日期：<input type='text' name='endDay' value='<%=endDay %>'>(格式：2012-02-09)
		<br/><br/>
		    
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
		    		<h3>道具交易统计--获取方式</h3>
		    		<%
		    		out.println("<table id='test2' align='center' width='90%' cellpadding='1' bgcolor='#000000' cellspacing='1' border='0'>");
					out.print("<tr bgcolor='#EEEEBB'><td>名称</td><td>别名</td><td>发布数量</td><td>交易数量</td><td>交易金额(文)</td><td>交易成功率</td><td>交易量占比</td><td>交易额占比</td></tr>");
					Long zfabu=0L;
					Long zjiaoyi=0L;
					Long zongjine=0L;
					for(int i = 0 ; i < ls.size() ; i++){
					
					String [] goumai=(String[])ls.get(i);
					zfabu   +=goumai[1]!=null ? Long.parseLong(goumai[1]) : 0L;
					zjiaoyi +=goumai[2]!=null ? Long.parseLong(goumai[2]) : 0L;
					zongjine+=goumai[3]!=null ? Long.parseLong(goumai[3]) : 0L;
					}
					for(int i = 0 ; i < ls.size() ; i++){
					String [] goumai=(String[])ls.get(i);
					long wanchenglv=0;
					long jiaoyiLiangZhanB=0;
					long jiaoyieZhanB=0;
					
					if(goumai[1]!=null&&!"0".equals(goumai[1])&&goumai[2]!=null){ wanchenglv= (Long.parseLong(goumai[2])*100) / Long.parseLong(goumai[1]); }
					if(zjiaoyi!=0&&goumai[2]!=null){jiaoyiLiangZhanB=(Long.parseLong(goumai[2])*100)/zjiaoyi;}
					if(zongjine!=0&&goumai[3]!=null){jiaoyieZhanB=(Long.parseLong(goumai[3])*100)/zongjine;}
					
					 out.print("<tr bgcolor='#FFFFFF'><td>"+goumai[0]+"</td><td>"+MultiLanguageManager.translateMap.get(goumai[0])+"</td><td>"+(goumai[1]==null? 0 : StringUtil.addcommas(Long.parseLong(goumai[1])))+"</td><td>"+ (goumai[2]==null ? 0 : StringUtil.addcommas(Long.parseLong(goumai[2]))) +"</td><td>"+(goumai[3]==null ? 0 : StringUtil.addcommas(Long.parseLong(goumai[3]))) +"</td><td>"+wanchenglv+"%</td><td>"+jiaoyiLiangZhanB+"%</td><td>"+jiaoyieZhanB+"%</td></tr>");
					}
					long zwanchenglv=0;
					if(zfabu!=0){zwanchenglv=(zjiaoyi*100)/zfabu;}
					out.print("<tr bgcolor='#EEEEBB'><td>总计</td><td>&nbsp;</td><td>"+StringUtil.addcommas(zfabu)+"</td><td>"+StringUtil.addcommas(zjiaoyi)+"</td><td>"+StringUtil.addcommas(zongjine)+"</td><td>&nbsp;"+zwanchenglv+"%</td><td>&nbsp;</td><td>&nbsp;</td></tr>");
					out.println("</table><br>");
		    		%>
		    		<div id="regUserContainer" style="width:100%;height:400px;display:block;"></div>
		</center>
		<br>
		  <i>这里的用户都是指进入游戏的用户</i>
		<br>
	</body>
</html>
