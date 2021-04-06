<%@ page contentType="text/html;charset=UTF-8"%>
<%@include file="../inc.jsp"%>
<%@ page
	import="com.xuanzhi.tools.cache.diskcache.concrete.DefaultDiskCache,org.slf4j.*,java.io.*,java.util.*,
	com.xuanzhi.tools.text.*,com.sqage.stat.commonstat.entity.ChongZhi,com.xuanzhi.tools.text.*,
	com.sqage.stat.commonstat.dao.UserDao,com.sqage.stat.commonstat.entity.User,com.sqage.stat.commonstat.manager.UserManager,
    com.sqage.stat.commonstat.manager.Impl.*"
	%>
	<%!
	Object lock = new Object(){};
      %>
  <%
	String startDay = request.getParameter("day");
	String endDay = request.getParameter("endDay");
	String daoJuType=request.getParameter("daoJuType");
	String fenqu=request.getParameter("fenqu");
    String jixing = request.getParameter("jixing");
    String getType=request.getParameter("getType");
	if("0".equals(jixing)){jixing=null;}
	if("0".equals(daoJuType)){daoJuType=null;}
	if("0".equals(fenqu)){fenqu=null;}

	
	String today = DateUtil.formatDate(new Date(),"yyyy-MM-dd");
	if(startDay == null) startDay = today;
	if(endDay == null) endDay = today;
	/**
	*获得渠道信息
	**/
	ArrayList<String> daojuList=null;
	
              UserManagerImpl userManager=UserManagerImpl.getInstance();
              DaoJuManagerImpl daoJuManager=DaoJuManagerImpl.getInstance();
              ArrayList<String []>  fenQuList= userManager.getFenQu(null);//获得现有的分区信息
             
             List<String []> ls= daoJuManager.getSensitiveKaiPingHuoDe(startDay,endDay,fenqu,jixing,getType);
              
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
			开瓶子获得 道具
		</h2>
		<form name="form1" id="form1" method="post" >
		<input type="hidden" id="getType" name="getType" value="<%=getType %>"/>
		
		开始日期：<input type='text' name='day' value='<%=startDay %>'>
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
                        if(_fenqu[1].equals(fenqu)){  out.print(" selected=\"selected\"");  }
                         %>
                          ><%=_fenqu[1] %></option>
                       <%
                       }
                       %>
                </select>&nbsp;&nbsp;
                
                                           平台：<select name="jixing">
                       <option  value="0">全部</option>
                        <option value="Android" 
                        <%
                        if("Android".equals(jixing)){ out.print(" selected=\"selected\"");  }
                         %>>Android</option>
                       <option value="IOS" 
                        <%
                        if("IOS".equals(jixing)){ out.print(" selected=\"selected\"");    }
                         %>>IOS</option>
                  </select>
                                                    
		    		<input type='submit' value='提交'>
		    		</form>
		    		<h3>开瓶子获得 道具</h3>
		    		<%
		    		out.println("<table id='test2' align='center' width='90%' cellpadding='1' bgcolor='#000000' cellspacing='1' border='0'>");
					out.print("<tr bgcolor='#EEEEBB'><td>获取方式</td><td>产出数量</td></tr>");
					Long chansheng=0L;
					for(int i = 0 ; i < ls.size() ; i++){
					String [] goumai=(String[])ls.get(i);
					chansheng+=Long.parseLong(goumai[1]);
					}
					for(int i = 0 ; i < ls.size() ; i++){
					String [] goumai=(String[])ls.get(i);
						out.print("<tr bgcolor='#FFFFFF'><td>"+goumai[0]+"</td><td>"+goumai[1]+"</td></tr>");
					}
					out.print("<tr bgcolor='#EEEEBB'><td>总计</td><td>"+chansheng+"</td></tr>");
					out.println("</table><br>");
		    		%>
		    		<div id="regUserContainer" style="width:100%;height:400px;display:block;"></div>
		</center>
		<br>
		  <i>这里的用户都是指进入游戏的用户</i>
		<br>
	</body>
</html>
