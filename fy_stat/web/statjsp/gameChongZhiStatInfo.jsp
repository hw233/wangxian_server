<%@ page contentType="text/html;charset=UTF-8"%>
<%@include file="../inc.jsp"%>
<%@ page
	import="com.xuanzhi.tools.cache.diskcache.concrete.DefaultDiskCache,org.slf4j.*,java.io.*,
	com.xuanzhi.tools.text.*,com.sqage.stat.commonstat.entity.ChongZhi,java.util.*,com.xuanzhi.tools.text.*,
	com.sqage.stat.commonstat.dao.UserDao,com.sqage.stat.commonstat.entity.User,com.sqage.stat.commonstat.manager.UserManager,
    com.sqage.stat.commonstat.manager.Impl.*"
	%>
	
  <%
	String startDay = request.getParameter("day");
	String endDay = request.getParameter("endDay");
	String huoBiType=request.getParameter("huoBiType");
	String fenqu=request.getParameter("fenqu");
	String wupintype=request.getParameter("wupintype");
	String gamelevel = request.getParameter("gamelevel");
	
	if("0".equals(huoBiType)){huoBiType=null;}
	if("0".equals(fenqu)){fenqu=null;}
	if("0".equals(wupintype)){wupintype=null;}
	if(gamelevel == null) gamelevel = "全部";
	
	String today = DateUtil.formatDate(new Date(),"yyyy-MM-dd");
	if(startDay == null) startDay = today;
	if(endDay == null) endDay = today;
	/**
	*获得渠道信息
	**/
	ArrayList<String []> huoBiTypeList=null;
	ArrayList<String []> daojuList=null;
	
              UserManagerImpl userManager=UserManagerImpl.getInstance();
              GameChongZhiManagerImpl gameChongZhiManager=GameChongZhiManagerImpl.getInstance();
              huoBiTypeList=gameChongZhiManager.getCurrencyType();
              daojuList=gameChongZhiManager.getReasontypeType();
              ArrayList<String []> fenQuList= userManager.getFenQu(null);//获得现有的分区信息
             
              String gamelevel_search=null;
              if(!"全部".equals(gamelevel)){gamelevel_search=gamelevel;}
              
		      List goumaiList=gameChongZhiManager.getChongZhiStat_reasontype(startDay,endDay,fenqu,gamelevel_search,huoBiType,wupintype,"0",null);
		      List goumaiList_=gameChongZhiManager.getChongZhiStat_reasontype(startDay,endDay,fenqu,gamelevel_search,huoBiType,wupintype,"1",null);
		      
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
	    function downloads(){
          $('form1').action="gameChongZhiStatInfo_download.jsp";
	      $('form1').submit();
          }
         </script>
	</head>
	<body>
		<center>
		<h2 style="font-weight:bold;">
			货币明细统计
		</h2>
		<form name="form1" id="form1" method="post">开始日期：<input type='text' name='day' value='<%=startDay %>'>
		                   -- 结束日期：<input type='text' name='endDay' value='<%=endDay %>'>(格式：2012-02-09)
		<br/><br/>
		       货币类型：<select name="huoBiType">
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
                        if(_fenqu[1].equals(fenqu)){
                        out.print(" selected=\"selected\"");
                        }
                         %>
                          ><%=_fenqu[1] %></option>
                       <%
                       }
                       %>
                </select>&nbsp;&nbsp;
                
                                           货币产生原因：<select name="wupintype">
                       <option  value="0">全部</option>
                       <% 
                       for(int i = 0 ; i < daojuList.size() ; i++){
                       String[] _wupintype = daojuList.get(i);
                       %>
                        <option value="<%=_wupintype[0] %>" 
                        <%
                        if(_wupintype[0].equals(wupintype)){
                        out.print(" selected=\"selected\"");
                        }
                         %>
                          ><%=_wupintype[1] %></option>
                       <%
                       }
                       %>
                </select>&nbsp;&nbsp;
                
                                            等级：<input type='text' name='gamelevel' value='<%=gamelevel %>'>&nbsp;&nbsp;
		    		<input type='submit' value='提交'>
		    		<input type="button" name="download" onclick="downloads();" value="下&nbsp;载excel" >
		    		
		    		</form>
		    		<h3><a href="gameChongZhiStat.jsp">货币统计</a></h3>
		    		<%
		    		out.println("<table id='test2' align='center' width='90%' cellpadding='1' cellspacing='1' border='0'>");
					out.print("<tr bgcolor='#EEEEBB'><td>货币产生原因</td><td>获得数量</td><td>消耗数量</td></tr>");
					 String table="";
					  for(int i = 0 ; i < daojuList.size() ; i++){
                       String _huoBiType[] = daojuList.get(i);
                       table="<tr><td>"+_huoBiType[1]+"</td><td>";
                       for(int t=0;t<goumaiList.size();t++){
                       String[] goumai=(String[])goumaiList.get(t);
                      
                       if(goumai[0].equals(_huoBiType)){ table+=(goumai[1]==null ? 0 : StringUtil.addcommas(Long.parseLong(goumai[1])));}
                       }
                       
                       table+="</td><td>";
                       
                       for(int t=0;t<goumaiList_.size();t++){
                       String[] goumai_=(String[])goumaiList_.get(t);
                       if(goumai_[0].equals(_huoBiType)){ table+=(goumai_[1] ==null ? 0 : StringUtil.addcommas(Long.parseLong(goumai_[1])));}
                       }
                       table+="</td></tr>";
                         out.println(table);
                       }
				  
					out.println("</table><br>");
		    		%>
		    		
		    		<div id="regUserContainer" style="width:100%;height:400px;display:block;"></div>
		</center>
		<br>
		<i>这里的用户都是指进入游戏的用户</i>
		<br>
	</body>
</html>
