<%@ page contentType="text/html;charset=UTF-8"%>
<%@include file="../inc.jsp"%>
<%@ page
	import="com.xuanzhi.tools.cache.diskcache.concrete.DefaultDiskCache,org.slf4j.*,java.io.*,java.util.*,
	com.xuanzhi.tools.text.*,java.util.*,com.xuanzhi.tools.text.*,com.sqage.stat.commonstat.entity.ChongZhi,
	com.sqage.stat.commonstat.dao.UserDao,com.sqage.stat.commonstat.entity.User,com.sqage.stat.commonstat.manager.UserManager,
	com.sqage.stat.commonstat.manager.Impl.*"
	%>

  <%
	String startDay = request.getParameter("day");
	String endDay = request.getParameter("endDay");
	String level = request.getParameter("level");
	String fenqu=request.getParameter("fenqu");
	String money = request.getParameter("money");
	ArrayList<String []> fenQuList  =  (ArrayList<String []>) session.getAttribute("FENQU_LIST");
	
	
	if("0".equals(fenqu)){fenqu=null;}
	if(money == null) money = "全部";
	
	String today = DateUtil.formatDate(new Date(),"yyyy-MM-dd");
	if(startDay == null) startDay = today;
	if(endDay == null) endDay = today;
	if(level == null) level = "全部";
	
              UserManagerImpl userManager=UserManagerImpl.getInstance();
              ChongZhiManagerImpl chongZhiManager=ChongZhiManagerImpl.getInstance();
               if(fenQuList==null)
	           {
		         fenQuList= userManager.getFenQu(null);//获得现有的分区信息
		         session.removeAttribute("FENQU_LIST");
		         session.setAttribute("FENQU_LIST", fenQuList);
	           }
             
              String money_search=null;
              String level_search=null;
              if(!"全部".equals(money)){money_search=money;}
              if(!"全部".equals(level)){level_search=level;}
		     List chongZhiList=chongZhiManager.getChongZhifenbu_(startDay,endDay,level_search,fenqu,money_search,null);
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
        <script type="text/javascript" src="<%=request.getContextPath()%>/comfile/js/calendar.js"></script>
        
	</head>
	<body>
		<center>
		<h2 style="font-weight:bold;">
			充值等级分布
		</h2>
		<form  method="post">
		
		<!--  开始日期：<input type='text' name='day' value='<%=startDay %>'>
		-- 结束日期：<input type='text' name='endDay' value='<%=endDay %>'>(格式：2012-02-09)-->
		
		  开始日期： <input type="text" name="day" id="day" class="nqz_input" style="width:80px;" readonly="readonly" onfocus="new Calendar(false,'yyyy-MM-dd').showTimePicker(this);" value="<%=startDay %>"/>&nbsp;到
		  结束日期： <input type="text" name="endDay" id="endDay" class="nqz_input" style="width:80px;" readonly="readonly" onfocus="new Calendar(false,'yyyy-MM-dd').showTimePicker(this);" value="<%=endDay %>"/>
		 
		<br/><br/>
		     &nbsp;&nbsp;&nbsp;&nbsp;
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
                      
                </select>
               
                
                                              充值金额：<input type='text' name='money' value='<%=money %>'>
                                               统计级别：<input type='text' name='level' value='<%=level %>'>
		    		<input type='submit' value='提交'></form>
		    		<br/>
		    		    <a href="chongzhi_info_page.jsp">充值查询—分页</a>|
		    		     <a href="chongzhi_info.jsp">充值查询-不分页</a>|
		    		     <a href="chongzhi_fenbu.jsp">  充值等级分布</a>|
		    		     <a href="chongzhi_money_fenbu.jsp">充值金额区间分布</a>|
		    		     <a href="qianrensouyi_info.jsp">千人收益</a>|
		    		     <a href="Kri_qianrensouyi_info.jsp">K 日千人收益</a>|
		    		     <a href="Krisouyi_info.jsp">K 日收益</a>|
		    		     <a href="quDaoChongzhi_info.jsp">按渠道充值查询</a>|
		    		     <a href="fenQuChongzhi_info.jsp">按分区充值查询</a>|
		    		     <a href="chongzhi_jine_fenbu.jsp">充值金额分布</a>|
		    		      <a href="modeTypeChongzhi_info.jsp">安手机类型充值查询</a>
		    		<br/>
		    		<h3>充值等级分布</h3>
		    		<%
		    		out.println("<table id='test2' align='center' width='90%' cellpadding='1' bgcolor='#000000' cellspacing='1' border='0'>");
					out.print("<tr bgcolor='#EEEEBB'><td>等级</td><td>充值账号数</td><td>充值金额</td><td>充值占比</td><td>充值ARPU</td><td>新增充值账号数</td><td>新增充值金额</td><td>新增充值占比</td><td>新增充值ARPU</td></tr>");
					Long oldmoneysum=0L;
					Long newmoneysum=0L;
					
					for(int i = 0 ; i < chongZhiList.size() ; i++){
					String[] chognzhi=(String[])chongZhiList.get(i);
					if(chognzhi[2]!=null){oldmoneysum+=Long.parseLong(chognzhi[2]);}
					if(chognzhi[5]!=null){newmoneysum+=Long.parseLong(chognzhi[5]);}
					
					}
					for(int order=0;order<200;order++){
					for(int i = 0 ; i < chongZhiList.size() ; i++){
					String[] chognzhi=(String[])chongZhiList.get(i);
					Long oldchongzhibi=0L;
					Long newchongzhibi=0L;
					if(chognzhi[2]!=null&&oldmoneysum!=0){oldchongzhibi=Long.parseLong(chognzhi[2])*100/oldmoneysum;}
					if(chognzhi[5]!=null&&newmoneysum!=0){newchongzhibi=Long.parseLong(chognzhi[5])*100/newmoneysum;}
					Long chongZhiARPU=0L;
					Long xinzengARPU=0L;
					if(chognzhi[3]!=null){chongZhiARPU=(long)Float.parseFloat(chognzhi[3]);}
					if(chognzhi[6]!=null){xinzengARPU=(long)Float.parseFloat(chognzhi[6]);}
					if(Integer.parseInt(chognzhi[0])==order){
					String dataTabel="<tr bgcolor='#FFFFFF'><td>"+chognzhi[0]+"</td><td>"+chognzhi[1]+"</td><td>"+chognzhi[2]+"</td><td>"+oldchongzhibi+"%&nbsp;</td><td>"+chongZhiARPU+"</td>"+
						                                                        "<td>"+chognzhi[4]+"</td><td>"+chognzhi[5]+"</td><td>"+newchongzhibi+"%&nbsp;</td><td>"+xinzengARPU+"</td>";
						out.print(dataTabel);
						out.println("</tr>");
						}
					}
					}
					out.print("<tr bgcolor='#EEEEBB'><td>总计</td><td>&nbsp;</td><td>"+oldmoneysum+"</td><td>&nbsp;</td><td>&nbsp;</td><td>&nbsp;</td><td>"+newmoneysum+"</td><td>&nbsp;</td><td>&nbsp;</td></tr>");
					out.println("</table><br>");
		    		%>
		    		
		    		<div id="regUserContainer" style="width:100%;height:400px;display:block;"></div>
		 
		</center>
		<br>
		<i>这里的用户都是指进入游戏的用户</i>
		<br>
	</body>
</html>
