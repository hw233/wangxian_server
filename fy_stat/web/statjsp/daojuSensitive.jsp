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
	String daoJuType=request.getParameter("daoJuType");
	String fenqu=request.getParameter("fenqu");
    String jixing = request.getParameter("jixing");
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
              ArrayList<String []> fenQuList= userManager.getFenQu(null);//获得现有的分区信息
             
             List<String []> ls= daoJuManager.getSensitiveDaoJu(startDay,endDay,fenqu,jixing,daoJuType);
              
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

         function search(daojuname) {
		      document.getElementById("daojuname").value=daojuname;
		      $('form1').action="daojuSensitiveReasonType.jsp";
		      $('form1').submit();
	        }
	        
	       function searchkp(getType) {
		      document.getElementById("daojuname").value=daojuname;
		      $('form1').action="daojuSensitiveReasonTypekp.jsp?getType="+getType;
		      $('form1').submit();
	        }  
	        
	        
	        
         function downloads(){
              $('form1').action="daojuxiaohao_download.jsp";
	         $('form1').submit();
           }
</script>
	</head>
	<body>
		<center>
		<h2 style="font-weight:bold;">
			敏感道具
		</h2>
		<form name="form1" id="form1" method="post" >开始日期：<input type='text' name='day' value='<%=startDay %>'>
		<input type="hidden" id="daojuname" name="daojuname" value=""/>
		-- 结束日期：<input type='text' name='endDay' value='<%=endDay %>'>(格式：2012-02-09)
		<br/><br/>
		       道具分类：<select name="daoJuType">
                       <option value="古董" 
                        <% if("古董".equals(daoJuType)){ out.print(" selected=\"selected\"");  } %>
                        >古董</option>
                         <option value="商城道具" 
                        <% if("商城道具".equals(daoJuType)){ out.print(" selected=\"selected\"");  } %>
                        >商城道具</option>
                       <option value="银子类道具" 
                        <% if("银子类道具".equals(daoJuType)){ out.print(" selected=\"selected\"");  } %>
                        >银子类道具</option>
                       <option value="宠物蛋" 
                        <% if("宠物蛋".equals(daoJuType)){ out.print(" selected=\"selected\"");  } %>
                        >宠物蛋</option>
                        <option value="飞行坐骑" 
                        <% if("飞行坐骑".equals(daoJuType)){ out.print(" selected=\"selected\"");  } %>
                        >飞行坐骑</option>
                        <option value="重要碎片" 
                        <% if("重要碎片".equals(daoJuType)){ out.print(" selected=\"selected\"");  } %>
                        >重要碎片</option>
                         <option value="万灵榜装备" 
                        <% if("万灵榜装备".equals(daoJuType)){ out.print(" selected=\"selected\"");  } %>
                        >万灵榜装备</option>
                        <option value="酒" 
                        <% if("酒".equals(daoJuType)){ out.print(" selected=\"selected\"");  } %>
                        >酒</option>
                        <option value="屠魔帖" 
                        <% if("屠魔帖".equals(daoJuType)){ out.print(" selected=\"selected\"");  } %>
                        >屠魔帖</option>
                        <option value="古董" 
                        <% if("古董".equals(daoJuType)){ out.print(" selected=\"selected\"");  } %>
                        >古董</option>

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
		    		<h3>敏感道具</h3>
		    		<%
		    		out.println("<table id='test2' align='center' width='90%' cellpadding='1' bgcolor='#000000' cellspacing='1' border='0'>");
					out.print("<tr bgcolor='#EEEEBB'><td>道具名称</td><td>别名</td><td>产出数量</td><td>消耗数量</td></tr>");
					Long chansheng=0L;
					Long xiaohao=0L;
					for(int i = 0 ; i < ls.size() ; i++){
					
					String [] goumai=(String[])ls.get(i);
					chansheng+=Long.parseLong(goumai[1]);
					xiaohao+=Long.parseLong(goumai[2]);
					}
					for(int i = 0 ; i < ls.size() ; i++){
					String [] goumai=(String[])ls.get(i);
						
		          out.print("<tr bgcolor='#FFFFFF'><td><a href=javascript:search('"+goumai[0]+"')>"+goumai[0]+"</a>");
		           
		        
		          
		          if("古董".equals(goumai[0])){
		              out.print("&nbsp;|&nbsp;<a href=javascript:searchkp('开瓶子获得')>开瓶子获得</a>");
		          }
		          
		          out.print("<td>"+MultiLanguageManager.translateMap.get(goumai[0])+"</td>");
		          
		          out.print("</td><td>"+goumai[1]+"</td><td>"+goumai[2]+"</td></tr>");
					}
					out.print("<tr bgcolor='#EEEEBB'><td>总计</td><td>&nbsp;</td><td>"+chansheng+"</td><td>"+xiaohao+"</td></tr>");
					out.println("</table><br>");
		    		%>
		    		<div id="regUserContainer" style="width:100%;height:400px;display:block;"></div>
		</center>
		<br>
		  <i>这里的用户都是指进入游戏的用户</i>
		<br>
	</body>
</html>
