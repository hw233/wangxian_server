<%@page import="java.math.BigDecimal"%>
<%@page import="java.text.DecimalFormat"%>
<%@page import="org.apache.commons.lang.StringUtils"%>
<%@page import="org.apache.commons.lang.time.DateUtils"%>
<%@page import="java.text.DateFormat"%>
<%@ page contentType="text/html;charset=UTF-8"%>
<%@page pageEncoding="utf-8" %>
<%@include file="../inc.jsp"%>
<%@ page
	import="com.xuanzhi.tools.cache.diskcache.concrete.DefaultDiskCache,org.slf4j.*,java.io.*,java.util.*,
	com.xuanzhi.tools.text.*,com.sqage.stat.commonstat.entity.ChongZhi,com.xuanzhi.tools.text.*,
	com.sqage.stat.commonstat.dao.UserDao,com.sqage.stat.commonstat.entity.User,com.sqage.stat.commonstat.manager.UserManager,
    com.sqage.stat.commonstat.manager.Impl.*,
    java.net.*
    "
	%>
<%!
	//设置充值区间数组
	BigDecimal[] moneySections = {new BigDecimal(50),new BigDecimal(100),new BigDecimal(150),new BigDecimal(200),new BigDecimal(300),new BigDecimal(500),new BigDecimal(1000),new BigDecimal(2000),new BigDecimal(5000),new BigDecimal(10000),new BigDecimal(Integer.MAX_VALUE)}; 
%>	
  <%
  	String commitStr = request.getParameter("isCommit");
	Boolean isCommit = false;
	if(!StringUtils.isEmpty(commitStr))
	{
		isCommit = Boolean.valueOf(commitStr); 
	}
	
	if(isCommit)
	{
	  	long startTime = System.currentTimeMillis();
		
	  	//初始化区间计数数组
	  	Integer[] countArray = {0,0,0,0,0,0,0,0,0,0,0};
	  	BigDecimal[] sumChargeArray = {new BigDecimal(0),new BigDecimal(0),new BigDecimal(0),new BigDecimal(0),new BigDecimal(0),new BigDecimal(0),new BigDecimal(0),new BigDecimal(0),new BigDecimal(0),new BigDecimal(0),new BigDecimal(0)};
		
		String startDay = request.getParameter("startday");
		String endDay = request.getParameter("endday");
		String afterDayStr = request.getParameter("afterday");
		Integer afterDay = null;
		
		if(afterDayStr != null)
		{
			afterDay = Integer.parseInt(afterDayStr);	
		}
		else
		{
			afterDay = 0;
		}
		
	
		String fenqu=URLDecoder.decode(request.getParameter("fenqu"),"utf-8");
		String qudao=URLDecoder.decode(request.getParameter("qudao"),"utf-8");
		
		ArrayList<String> quDaoList = (ArrayList<String>)request.getAttribute("QUDAO_LIST");
		ArrayList<String> fenQuList  =  (ArrayList<String>) request.getAttribute("FENQU_LIST");
		
		
		if(fenqu != null && fenqu.trim().equals("0"))
		{
			fenqu = null;
		}
		
		if(qudao != null && qudao.trim().equals("0"))
		{
			qudao = null;
		}
		
		
		if(startDay == null)
		{
			startDay = DateUtil.formatDate(new Date(), "yyyy-MM-dd");
		}
		
		if(endDay == null)
		{
			endDay = DateUtil.formatDate(new Date(), "yyyy-MM-dd");
		}
		
		
	 	 UserManagerImpl userManager=UserManagerImpl.getInstance();
	  //DaoJuManagerImpl daoJuManager=DaoJuManagerImpl.getInstance();
	  
	
		if(quDaoList==null || quDaoList.size() == 0)
		{
			quDaoList = userManager.getQudao(null);//获得现有的渠道信息
		}
		
		if(fenQuList==null )
		{
		    fenQuList=new ArrayList<String>();
			ArrayList lsfenqu= userManager.getFenQu(null);//获得现有的分区信息
			for(int num=0;num<lsfenqu.size();num++){
			String[] dfenqu=(String[])lsfenqu.get(num);
			fenQuList.add(dfenqu[1]);
			}
		}
		
		request.setAttribute("QUDAO_LIST", quDaoList);
		request.setAttribute("FENQU_LIST", fenQuList);
		
		Object[] info = null;
		
	
		
/* 	out.println("开始日期："+startDay);
			out.println("结束日期："+endDay);
			out.println("拖后日期："+afterDay);
			out.println("分区："+fenqu);
			out.println("渠道："+qudao); */ 
			
		info = userManager.countNotLoginAndChargeSpread(startDay,endDay,afterDay,fenqu,qudao);
		int sumNotLoginNum = 0;
		
		if(info != null)
		{
			//计算未登陆总人数
				Iterator<Integer> it = ((Map<Integer,Integer>)info[0]).keySet().iterator();
				
				while(it.hasNext())
				{
					int key = it.next();
					int value = ((Map<Integer,Integer>)info[0]).get(key);
					sumNotLoginNum += value; 
				}
			
			//计算金额区间分布人数和总额
			Iterator<BigDecimal> it1 = ((Map<BigDecimal,Integer>)info[1]).keySet().iterator();
			while(it1.hasNext())
			{
				BigDecimal key = it1.next();
				int value = ((Map<BigDecimal,Integer>)info[1]).get(key);
				
				if(key.divide(new BigDecimal(100)).compareTo(moneySections[0]) < 0)
				{
					countArray[0]+=value;
					sumChargeArray[0] = sumChargeArray[0].add(key);
				}
				else if(( key.divide(new BigDecimal(100)).compareTo(moneySections[0]) == 0 || key.divide(new BigDecimal(100)).compareTo(moneySections[0]) > 0 ) && key.divide(new BigDecimal(100)).compareTo(moneySections[1]) < 0)
				{
					countArray[1]+=value;
					sumChargeArray[1] = sumChargeArray[1].add(key);
				}
				else if(( key.divide(new BigDecimal(100)).compareTo(moneySections[1]) == 0 || key.divide(new BigDecimal(100)).compareTo(moneySections[1]) > 0 ) && key.divide(new BigDecimal(100)).compareTo(moneySections[2]) < 0)
				{
					countArray[2]+=value;
					sumChargeArray[2] = sumChargeArray[2].add(key);
				}
				else if(( key.divide(new BigDecimal(100)).compareTo(moneySections[2]) == 0 || key.divide(new BigDecimal(100)).compareTo(moneySections[2]) > 0 ) && key.divide(new BigDecimal(100)).compareTo(moneySections[3]) < 0)
				{
					countArray[3]+=value;
					sumChargeArray[3] = sumChargeArray[3].add(key);
				}
				else if(( key.divide(new BigDecimal(100)).compareTo(moneySections[3]) == 0 || key.divide(new BigDecimal(100)).compareTo(moneySections[3]) > 0 ) && key.divide(new BigDecimal(100)).compareTo(moneySections[4]) < 0)
				{
					countArray[4]+=value;
					sumChargeArray[4] = sumChargeArray[4].add(key);
				}
				else if(( key.divide(new BigDecimal(100)).compareTo(moneySections[4]) == 0 || key.divide(new BigDecimal(100)).compareTo(moneySections[4]) > 0 ) && key.divide(new BigDecimal(100)).compareTo(moneySections[5]) < 0)
				{
					countArray[5]+=value;
					sumChargeArray[5] = sumChargeArray[5].add(key);
				}
				else if(( key.divide(new BigDecimal(100)).compareTo(moneySections[5]) == 0 || key.divide(new BigDecimal(100)).compareTo(moneySections[5]) > 0 ) && key.divide(new BigDecimal(100)).compareTo(moneySections[6]) < 0)
				{
					countArray[6]+=value;
					sumChargeArray[6] = sumChargeArray[6].add(key);
				}
				else if(( key.divide(new BigDecimal(100)).compareTo(moneySections[6]) == 0 || key.divide(new BigDecimal(100)).compareTo(moneySections[6]) > 0 ) && key.divide(new BigDecimal(100)).compareTo(moneySections[7]) < 0)
				{
					countArray[7]+=value;
					sumChargeArray[7] = sumChargeArray[7].add(key);
				}
				else if(( key.divide(new BigDecimal(100)).compareTo(moneySections[7]) == 0 || key.divide(new BigDecimal(100)).compareTo(moneySections[7]) > 0 ) && key.divide(new BigDecimal(100)).compareTo(moneySections[8]) < 0)
				{
					countArray[8]+=value;
					sumChargeArray[8] = sumChargeArray[8].add(key);
				}
				else if(( key.divide(new BigDecimal(100)).compareTo(moneySections[8]) == 0 || key.divide(new BigDecimal(100)).compareTo(moneySections[8]) > 0 ) && key.divide(new BigDecimal(100)).compareTo(moneySections[9]) < 0)
				{
					countArray[9]+=value;
					sumChargeArray[9] = sumChargeArray[9].add(key);
				}
				else if(( key.divide(new BigDecimal(100)).compareTo(moneySections[9]) == 0 || key.divide(new BigDecimal(100)).compareTo(moneySections[9]) > 0 ) && key.divide(new BigDecimal(100)).compareTo(moneySections[10]) < 0)
				{
					countArray[10]+=value;
					sumChargeArray[10] = sumChargeArray[10].add(key);
				}
			}
		}

			//out.println("userManager.countLoginAndRunOff 执行的时间为：["+(System.currentTimeMillis() - startTime)+"]ms");

	
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
			未登录用户等级分布信息统计
		</h2>
		<div style="clear: both;">
		开始日期：<%=startDay %>-结束日期：<%=endDay %>
		<br/><br/>
			 <%if(isCommit &&  afterDay!=null){ %><%=afterDay %><%}%>天后流失人数
			 <br/><br/>
                                             分区：<select name="fenqu">
                       <option  value="0">全部</option>
                       <% 
                       for(int i = 0 ; i < fenQuList.size() ; i++){
                       String _fenqu = fenQuList.get(i);
                       %>
                        <option value="<%=_fenqu %>" 
                        <%
                        if(_fenqu.equals(fenqu)){
                        out.print(" selected=\"selected\"");
                        }
                         %>
                          ><%=_fenqu %></option>
                       <%
                       }
                       %>
                </select>&nbsp;&nbsp;
                
               渠道：<select name="qudao">
                       <option  value="0">全部</option>
                                              <% 
                       for(int i = 0 ; i < quDaoList.size() ; i++){
                       String _qudao = quDaoList.get(i);
                       if(_qudao == null)out.println("第"+i+"个元素为空！");
                       %>
                        <option value="<%=_qudao %>" 
                        <%
                        if(_qudao.equals(qudao)){
                        out.print(" selected=\"selected\"");
                        }
                         %>
                          ><%=_qudao %></option>
                       <%
                       }
                       %>
                </select>&nbsp;&nbsp;
                <% 
               // out.println("加载渠道和分区的执行的时间为：["+(System.currentTimeMillis() - startTime)+"]ms");
                %>
		    		<table id='onlineinfo' align='center' width='90%' cellpadding='1' bgcolor='#000000' cellspacing='1' border='0'>
		    		<tr bgcolor='#EEEEBB'><td>级别</td><td>人数</td><td>占比</td></tr>
		    		<%
						if(info != null)
						{
							//未登录人数
							Iterator<Integer> it = ((Map<Integer,Integer>)info[0]).keySet().iterator();
							while(it.hasNext())
							{
								int key = it.next();
								int value = ((Map<Integer,Integer>)info[0]).get(key);
								DecimalFormat decimalFormat = new DecimalFormat("0.00");
					%>
							<tr bgcolor='#FFFFFF'><td><%=key%></td><td><%=value%></td><td><%=decimalFormat.format((((value*1.0)/sumNotLoginNum) * 100 ))%>%</td></tr>
						
							
					<% 	
							}
					%>
					<tr bgcolor='#EEEEBB'><td>总计:<%=((Map<Integer,Integer>)info[0]).size()%>行</td><td><%=sumNotLoginNum %></td><td>--</td></tr>	
					<%		
						}
						out.println("</table><br>");
						
					%>	
		<h2 style="font-weight:bold;">
			未充值用户曾充值金额区间信息统计
		</h2>

                <% 
               // out.println("加载渠道和分区的执行的时间为：["+(System.currentTimeMillis() - startTime)+"]ms");
                %>
		    		<table id='chargeinfo' align='center' width='90%' cellpadding='1' bgcolor='#000000' cellspacing='1' border='0'>
		    		<tr bgcolor="#EEEEBB">
						<td>充值区间(元)</td>
						<td>0-50</td>
						<td>50-100</td>
						<td>100-150</td>
						<td>150-200</td>
						<td>200-300</td>
						<td>300-500</td>
						<td>500-1000</td>
						<td>1000-2000</td>
						<td>2000-5000</td>
						<td>5000-10000</td>
						<td>10000以上</td>
					</tr>
		    		<%
						if(info != null)
						{
								
					%>
							<tr bgcolor='#FFFFFF'>
								<td>充值用户数</td>
								<td><%=countArray[0] %></td>
								<td><%=countArray[1] %></td>
								<td><%=countArray[2] %></td>
								<td><%=countArray[3] %></td>
								<td><%=countArray[4] %></td>
								<td><%=countArray[5] %></td>
								<td><%=countArray[6] %></td>
								<td><%=countArray[7] %></td>
								<td><%=countArray[8] %></td>
								<td><%=countArray[9] %></td>
								<td><%=countArray[10] %></td>
							</tr>
							<tr bgcolor='#EEEEBB'>
								<td>充值总额</td>
								<td><%=sumChargeArray[0].divide(new BigDecimal(100)).doubleValue()%></td>
								<td><%=sumChargeArray[1].divide(new BigDecimal(100)).doubleValue()%></td>
								<td><%=sumChargeArray[2].divide(new BigDecimal(100)).doubleValue()%></td>
								<td><%=sumChargeArray[3].divide(new BigDecimal(100)).doubleValue()%></td>
								<td><%=sumChargeArray[4].divide(new BigDecimal(100)).doubleValue()%></td>
								<td><%=sumChargeArray[5].divide(new BigDecimal(100)).doubleValue()%></td>
								<td><%=sumChargeArray[6].divide(new BigDecimal(100)).doubleValue()%></td>
								<td><%=sumChargeArray[7].divide(new BigDecimal(100)).doubleValue()%></td>
								<td><%=sumChargeArray[8].divide(new BigDecimal(100)).doubleValue()%></td>
								<td><%=sumChargeArray[9].divide(new BigDecimal(100)).doubleValue()%></td>
								<td><%=sumChargeArray[10].divide(new BigDecimal(100)).doubleValue()%></td>
							</tr>
					<%		
						}
						out.println("</table><br>");
						
					%>	
		 
		</center>
		</div>
		<br>
		<br>
		<div style="clear:both;"></div>
		<div>
			<div id="userStatCanvas" style="width:100%;height:400px;display:block;"></div>
		</div>
	</body>
	            <% 
               // out.println("结束的时间为：["+(System.currentTimeMillis() - startTime)+"]ms");
                %>
</html>
<% }%>

