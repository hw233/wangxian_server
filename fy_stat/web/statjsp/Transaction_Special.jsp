<%@ page contentType="text/html;charset=UTF-8"%>
<%@include file="../inc.jsp"%>
<%@ page
	import="com.xuanzhi.tools.cache.diskcache.concrete.DefaultDiskCache,org.slf4j.*,java.io.*,java.util.*,com.xuanzhi.tools.text.*,
	com.xuanzhi.tools.text.*,com.sqage.stat.commonstat.entity.*,com.sqage.stat.model.Channel,com.sqage.stat.service.*,
	com.sqage.stat.commonstat.dao.UserDao,com.sqage.stat.commonstat.entity.User,com.sqage.stat.commonstat.manager.UserManager,
	com.sqage.stat.commonstat.manager.Impl.*"
	%>
  <%
	String startDay = request.getParameter("day");
	String endDay = request.getParameter("endDay");
	String qudao=request.getParameter("qudao");
	String fenqu=request.getParameter("fenqu");
	String jiaoyitype=request.getParameter("jiaoyitype");
	
	
	if("0".equals(qudao)){qudao=null;}
	if("0".equals(fenqu)){fenqu=null;}
	
	String today = DateUtil.formatDate(new Date(),"yyyy-MM-dd");
	if(startDay == null) startDay = today;
	if(endDay == null) endDay = today;
	
	/**
	*获得渠道信息
	**/
	ArrayList<String> typeList = null;
	ArrayList<String> cardtypeList = null;
	          ChannelManager cmanager = ChannelManager.getInstance();
              UserManagerImpl userManager=UserManagerImpl.getInstance();
              Transaction_SpecialManagerImpl transaction_SpecialManager=Transaction_SpecialManagerImpl.getInstance();
              ArrayList<String []> fenQuList= userManager.getFenQu(null);//获得现有的分区信息
              List<Channel> channelList = cmanager.getChannels();//渠道信息
             
             String sql="select t.*  from stat_transaction_special "
             +" t where  to_char(t.createdate,'YYYY-MM-DD')  between '"+startDay+"' and '"+endDay+"'";
             if(!"".equals(fenqu)&&fenqu!=null){ sql +=" and t.fenqu='"+fenqu+"'";}
             if(!"0".equals(jiaoyitype)){ sql+=" and t.transactiontype='"+jiaoyitype+"'";}
		     List chongZhiList=transaction_SpecialManager.getBySql(sql);
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
         $('form1').action="chongzhi_info_download.jsp";
	     $('form1').submit();
         }
</script>
	</head>
	<body>
		<center>
		<h2 style="font-weight:bold;">
			充值查询
		</h2>
		<form  name="form1" id="form1" method="post">开始日期：<input type='text' name='day' value='<%=startDay %>'>
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
                
                                      异常交易类型：<select name="jiaoyitype">
                                <option  value="0">全部</option>
                     
                                <option  value="不正常面对面交易"  
                                 <% if("不正常面对面交易".equals(jiaoyitype)){ out.print(" selected=\"selected\""); } %>
                                  >不正常面对面交易</option>
                                 <% if("不正常摆摊交易".equals(jiaoyitype)){ out.print(" selected=\"selected\""); } %>
                                <option  value="不正常摆摊交易"
                                
                                >不正常摆摊交易</option> 
                           </select>&nbsp;&nbsp;
                                   
		        <br/>
		    		<input type='submit' value='提交查询'>
		    		</form><br/>
		    		<br/>
		    		<h3>充值查询</h3>
		    		<%
		    		out.println("<table id='test2' align='center' width='90%' cellpadding='1' bgcolor='#000000' cellspacing='1' border='0'>");
					out.print("<tr bgcolor='#EEEEBB'>");
					out.print("<td>时间</td>        <td>分区</td>       <td>交易类型</td> ");
					out.print("<td>发出者用户名</td><td>发出者角色名</td><td>发出者级别</td><td>发出者vip</td><td>发出者渠道</td>");
					out.print("<td>发出者UUID</td>  <td>发出道具名称</td><td>发出者银子</td>");
					out.print("<td>发出者价值</td>  <td>接收者用户名</td><td>接收者角色名</td><td>接收者级别</td><td>接收者vip</td>");
					out.print("<td>接收者渠道</td>  <td>接收者UUID</td>  <td>接收道具名称</td>");
					out.print("<td>接收者银子</td>  <td>接收者价值</td> </tr>");
					
					
					long times=0L;
					long fjiazhi=0L;
					long fyinzi=0L;
					long toJiaZhi=0L;
					long toYinZhi=0L;
					
					
					for(int i = 0 ; i < chongZhiList.size() ; i++){
					Transaction_Special chognzhi=(Transaction_Special)chongZhiList.get(i);
						times++;
						fjiazhi+=chognzhi.getFjiazhi();
						fyinzi+=chognzhi.getFyinzi();
						toJiaZhi+=chognzhi.getTojiazhi();
						toYinZhi+=chognzhi.getToyinzi();
					}
					
					out.print("<tr bgcolor='#EEEEBB'>");
					out.print("<td>汇总 共"+times+"条</td>        <td>&nbsp;</td>       <td>&nbsp;</td> ");
					out.print("<td>&nbsp;</td>  <td>&nbsp;</td><td>&nbsp;</td><td>&nbsp;</td><td>&nbsp;</td>");
					out.print("<td>&nbsp;</td>  <td>&nbsp;</td><td>"+fyinzi+"</td>");
					out.print("<td>"+fjiazhi+"</td> <td>&nbsp;</td><td>&nbsp;</td>");
					out.print("<td>&nbsp;</td> <td>&nbsp;</td><td>&nbsp;</td> <td>&nbsp;</td>  <td>&nbsp;</td>");
					out.print("<td>"+toYinZhi+"</td>  <td>"+toJiaZhi+"</td> </tr>");
					
					for(int i = 0 ; i < chongZhiList.size() ; i++){
					Transaction_Special chognzhi=(Transaction_Special)chongZhiList.get(i);
						out.print("<tr bgcolor='#FFFFFF'>");
						out.print("<td>"+chognzhi.getCreateDate()+"</td><td>"+chognzhi.getFenQu()+"</td>      <td>"+chognzhi.getTransactionType()+"</td>");
						out.print("<td>"+chognzhi.getFuserName()+"</td> <td>"+chognzhi.getFplayerName()+"</td><td>"+chognzhi.getfLevel()+"</td><td>"+chognzhi.getfVip()+"</td><td>"+chognzhi.getFquDao()+"</td>");
						out.print("<td>"+chognzhi.getFuuId()+"</td>     <td>"+chognzhi.getFdaoJuName()+"</td> <td>"+chognzhi.getFyinzi()+"</td>");
						out.print("<td>"+chognzhi.getFjiazhi()+"</td>   <td>"+chognzhi.getToUserName()+"</td> <td>"+chognzhi.getToPlayername()+"</td><td>"+chognzhi.getToLevel()+"</td><td>"+chognzhi.getToVip()+"</td>");
						out.print("<td>"+chognzhi.getToquDao()+"</td>   <td>"+chognzhi.getTouuId()+"</td>     <td>"+chognzhi.getTodaoJuName()+"</td>");
						out.print("<td>"+chognzhi.getToyinzi()+"</td>   <td>"+chognzhi.getTojiazhi()+"</td>");
						
						out.println("</tr>");
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
