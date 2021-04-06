<%@ page contentType="text/html;charset=UTF-8"%>
<%@include file="../inc.jsp"%>
<%@ page
	import="com.xuanzhi.tools.cache.diskcache.concrete.DefaultDiskCache,org.slf4j.*,java.io.*,java.util.*,
	com.xuanzhi.tools.text.*,com.sqage.stat.commonstat.entity.ChongZhi,com.xuanzhi.tools.text.*,
	com.sqage.stat.commonstat.dao.UserDao,com.sqage.stat.commonstat.entity.User,com.sqage.stat.commonstat.manager.UserManager,
    com.sqage.stat.commonstat.manager.Impl.*"
	%>
	
  <%
	String startDay = request.getParameter("day");
	String endDay = request.getParameter("endDay");
	String huoBiType=request.getParameter("huoBiType");
	String fenqu=request.getParameter("fenqu");
	String reasontype=request.getParameter("reasontype");
	String gamelevel = request.getParameter("gamelevel");
	
	String startNum= request.getParameter("startNum");
	String endNum=request.getParameter("endNum");
	String quDao="";
	String action=request.getParameter("action");
	
	
	if(huoBiType==null||"0".equals(huoBiType)){huoBiType=null;}
	if("0".equals(fenqu)){fenqu=null;}
	if("0".equals(reasontype)){reasontype=null;}
	if(gamelevel == null) gamelevel = "全部";
	if(startNum == null) startNum = "0";
	if(endNum == null) endNum = "0";
	if(action == null) action = "-1";
	
	String today = DateUtil.formatDate(new Date(),"yyyy-MM-dd");
	if(startDay == null) startDay = today;
	if(endDay == null) endDay = today;
	
	/**
	*获得渠道信息
	**/
	ArrayList<String []> huoBiTypeList=null;
	ArrayList<String []> reasontypeist=null;
	
              UserManagerImpl userManager=UserManagerImpl.getInstance();
              GameChongZhiManagerImpl gameChongZhiManager=GameChongZhiManagerImpl.getInstance();
              huoBiTypeList=gameChongZhiManager.getCurrencyType();
              reasontypeist=gameChongZhiManager.getReasontypeType();
              
              ArrayList<String []> fenQuList= userManager.getFenQu(null);//获得现有的分区信息
              String gamelevel_search=null;
              if(!"全部".equals(gamelevel)){gamelevel_search=gamelevel;}
              String actionStr=null;
              if(!"-1".equals(action)){actionStr=action;}
		       List goumaiList=gameChongZhiManager.getPlayerActionWatch(startDay,endDay,startNum,endNum,fenqu,quDao,gamelevel_search,huoBiType,reasontype,actionStr);
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
        <script type="text/javascript" language="javascript">
	     function search(username) {
		      //document.getElementById("huoBiType").value=huoBiType;
		      $('form1').action="playerAciotnWatch_download.jsp?username="+username;
		      $('form1').submit();
	        }
	        
	     function downloads(){
          $('form1').action="gameChongZhiStat_download.jsp";
	      $('form1').submit();
          }
         </script>
	</head>
	<body>
		<center>
		<h2 style="font-weight:bold;">
			特别用户监控
		</h2>
		<form name="form1" id="form1" method="post">开始日期：<input type='text' name='day' value='<%=startDay %>'>
		                                         -- 结束日期：<input type='text' name='endDay' value='<%=endDay %>'>(格式：2012-02-09)
		<br/><br/>
		       货币类型：<select id="huoBiType" name="huoBiType">
                       <option  value="0">全部</option>
                       <% 
                       for(int i = 0 ; i < huoBiTypeList.size() ; i++){
                       String[] _huoBiType = huoBiTypeList.get(i);
                       %>
                        <option value="<%=_huoBiType[0] %>" 
                        <%
                        if(_huoBiType[0].equals(huoBiType)){
                        out.print(" selected=\"selected\"");
                        }
                         %>
                          ><%=_huoBiType[1] %></option>
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
                
                                           货币产生原因：<select name="reasontype">
                       <option  value="0">全部</option>
                       <% 
                       for(int i = 0 ; i < reasontypeist.size() ; i++){
                       String[] _reasontype = reasontypeist.get(i);
                       %>
                        <option value="<%=_reasontype[0] %>" 
                        <%
                        if(_reasontype[0].equals(reasontype)){
                        out.print(" selected=\"selected\"");
                        }
                         %>
                          ><%=_reasontype[1] %></option>
                       <%
                       }
                       %>
                </select>&nbsp;&nbsp;
                
                                 得失类型：<select name="action">
                       <option  value="-1">全部</option>          
                       <option  value="0">充值</option>
                       <option value="1">消耗</option>
                </select>&nbsp;&nbsp;
                <!--
                                            等级：<input type='text' name='gamelevel' size="5" value='<%=gamelevel %>'>&nbsp;&nbsp;
                                              -->
                                            
                                           从第<input type='text' name='startNum' size="5" value='<%=startNum %>'>&nbsp;名&nbsp;
                                               到<input type='text' name='endNum' size="5" value='<%=endNum %>'>&nbsp;&nbsp;名 玩家
                                               
		    		<input type='submit' value='提交'>
		    		<!-- 
		    		<input type="button" name="download" onclick="downloads();" value="下&nbsp;载excel" >
		    		 -->
		    		</form>
		    		
		    		<h3>特别用户监控</h3>
		    		<%
		    		out.println("<table id='test2' align='center' width='90%' cellpadding='1' cellspacing='1' border='0'>");
					out.print("<tr bgcolor='#EEEEBB'><td>排名</td><td>用户名</td><td>数量</td></tr>");
					
					for(int t=0;t<goumaiList.size();t++){
                       String[] goumai=(String[])goumaiList.get(t);
					//out.println("<tr><td><a href=javascript:search('"+goumai[2]+"')>"+goumai[2]+"</a><td>"+goumai[0]+"</td><td>"+goumai[1]+"</td></tr>");
					
					
					out.println("<tr><td>"+goumai[2]+"<td><a href=javascript:search('"+goumai[0]+"')>"+goumai[0]+"</a></td>   <td>"+goumai[1]+"</td></tr>");
					
					
					}
				  
					out.println("</table><br>");
		    		%>
		    		
		    		<div id="regUserContainer" style="width:100%;height:400px;display:block;"></div>
		 
		</center>
		<br>
		<i>这里的特殊用户是指涉及金额巨大的用户，这类用户最具风险特征，本功能就是把这类玩家筛选出来。为运营人员定位异常玩家缩小范围。</i>
		<br>
	</body>
</html>
