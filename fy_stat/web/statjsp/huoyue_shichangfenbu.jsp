<%@ page contentType="text/html;charset=UTF-8"%>
<%@include file="../inc.jsp"%>
<%@ page
	import="com.xuanzhi.tools.cache.diskcache.concrete.DefaultDiskCache,org.slf4j.*,java.io.*,java.util.*,
	com.xuanzhi.tools.text.*,com.sqage.stat.commonstat.entity.ChongZhi,com.xuanzhi.tools.text.*,
	com.sqage.stat.commonstat.dao.UserDao,com.sqage.stat.commonstat.entity.User,com.sqage.stat.commonstat.manager.UserManager,
	com.sqage.stat.commonstat.manager.Impl.UserManagerImpl,com.sqage.stat.commonstat.manager.Impl.*,com.sqage.stat.service.*,
	com.sqage.stat.model.Channel,java.math.*"
	%>

  <%
     String flag = null;
    flag = request.getParameter("flag");
	String startDay = request.getParameter("day");
	String fenqu=request.getParameter("fenqu");
	
	if("0".equals(fenqu)){fenqu=null;}
	String today = DateUtil.formatDate(new Date(),"yyyy-MM-dd");
	if(startDay == null) startDay = today;
	/**
	*获得渠道信息
	**/
              UserManagerImpl userManager=UserManagerImpl.getInstance();
              PlayGameManagerImpl playGameManager=PlayGameManagerImpl.getInstance();
              ArrayList<String []> fenQuList= userManager.getFenQu(null);//获得现有的分区信息
          List<String[]> zaixiaList=new ArrayList();
          List<String[]> zaixiaList_new=new ArrayList();
          
          if("true".equals(flag)){
              zaixiaList=playGameManager.getZaiXianShiChangFenBu(startDay,null,fenqu,"30");
              zaixiaList_new=playGameManager.getZaiXianShiChangFenBu_new(startDay,null,fenqu,"30");
              }
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
			活跃用户在线时长分布
		</h2>
		<form  method="post">
		 <input type='hidden' name='flag' value='true'/>
		<!-- 开始日期：<input type='text' name='day' value='<%=startDay %>'>(格式：2012-02-09)
		 -->
		 统计日期： <input type="text" name="day" id="day" class="nqz_input" style="width:100px;" readonly="readonly" onfocus="new Calendar(false,'yyyy-MM-dd').showTimePicker(this);" value="<%=startDay %>"/>&nbsp;
		     
		
		 &nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;
                                             分区：<select name="fenqu">
                       <option  value="0">全部</option>
                       <% 
                       for(int i = 0 ; i < fenQuList.size() ; i++){
                       String[] _fenqu = fenQuList.get(i);
                       %>
                        <option value="<%=_fenqu[1] %>" 
                        <%
                        if(_fenqu[1].equals(fenqu)){ out.print(" selected=\"selected\"");   }
                         %>
                          ><%=_fenqu[1] %></option>
                       <%
                       }
                       %>
                   </select>
                       <br/><br/>
		    		<input type='submit' value='提交'></form><br/>
		    		     
		    		<br/>
		    		<h3>活跃用户在线时长分布</h3>
		    		<%
		    		out.println("<table id='test2' align='center' width='90%' cellpadding='1' bgcolor='#000000' cellspacing='1' border='0'>");
					out.print("<tr bgcolor='#EEEEBB'><td>在线时长(小时)</td><td>[0,0.5)</td><td>[0.5,1)</td><td>[1,1.5)</td>"+
					"<td>[1.5,2)</td><td>[2,3)</td><td>[3,4)</td><td>4小时以上</td><td bgcolor='#EEEEBB'>总计</td></tr>");
					if("true".equals(flag)){
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
					
					
					 long unum_zong=unum05+unum10+unum15+unum20+unum30+unum40+unum100;
					 long unum_new_zong=unum05_new+unum10_new+unum15_new+unum20_new+unum30_new+unum40_new+unum100_new;
					 long unum_old_zong=unum05_old+unum10_old+unum15_old+unum20_old+unum30_old+unum40_old+unum100_old;
					 
					                
  									BigDecimal b_zong = new BigDecimal(unum_zong);
  									
  									BigDecimal b05 = new BigDecimal(unum05*100);
  									BigDecimal b05_ = b05.divide(b_zong, new MathContext(4));
  									BigDecimal b10 = new BigDecimal(unum10*100);
  									BigDecimal b10_ = b10.divide(b_zong, new MathContext(4));
  									BigDecimal b15 = new BigDecimal(unum15*100);
  									BigDecimal b15_ = b15.divide(b_zong, new MathContext(4));
  									BigDecimal b20 = new BigDecimal(unum20*100);
  									BigDecimal b20_ = b20.divide(b_zong, new MathContext(4));
  									BigDecimal b30 = new BigDecimal(unum30*100);
  									BigDecimal b30_ = b30.divide(b_zong, new MathContext(4));
  									BigDecimal b40 = new BigDecimal(unum40*100);
  									BigDecimal b40_ = b40.divide(b_zong, new MathContext(4));
  									BigDecimal b100 = new BigDecimal(unum100*100);
  									BigDecimal b100_ = b100.divide(b_zong, new MathContext(4));
  									
  									BigDecimal b_n05 = new BigDecimal(unum05_new*100);
  									BigDecimal b_n05_ = b_n05.divide(b_zong, new MathContext(4));
  									BigDecimal b_n10 = new BigDecimal(unum10_new*100);
  									BigDecimal b_n10_ = b_n10.divide(b_zong, new MathContext(4));
  									BigDecimal b_n15 = new BigDecimal(unum15_new*100);
  									BigDecimal b_n15_ = b_n15.divide(b_zong, new MathContext(4));
  									BigDecimal b_n20 = new BigDecimal(unum20_new*100);
  									BigDecimal b_n20_ = b_n20.divide(b_zong, new MathContext(4));
  									BigDecimal b_n30 = new BigDecimal(unum30_new*100);
  									BigDecimal b_n30_ = b_n30.divide(b_zong, new MathContext(4));
  									BigDecimal b_n40 = new BigDecimal(unum40_new*100);
  									BigDecimal b_n40_ = b_n40.divide(b_zong, new MathContext(4));
  									BigDecimal b_n100 = new BigDecimal(unum100_new*100);
  									BigDecimal b_n100_ = b_n100.divide(b_zong, new MathContext(4));
  									
  									BigDecimal b_o05 = new BigDecimal(unum05_old*100);
  									BigDecimal b_o05_ = b_o05.divide(b_zong, new MathContext(4));
  									BigDecimal b_o10 = new BigDecimal(unum10_old*100);
  									BigDecimal b_o10_ = b_o10.divide(b_zong, new MathContext(4));
  									BigDecimal b_o15 = new BigDecimal(unum15_old*100);
  									BigDecimal b_o15_ = b_o15.divide(b_zong, new MathContext(4));
  									BigDecimal b_o20 = new BigDecimal(unum20_old*100);
  									BigDecimal b_o20_ = b_o20.divide(b_zong, new MathContext(4));
  									BigDecimal b_o30 = new BigDecimal(unum30_old*100);
  									BigDecimal b_o30_ = b_o30.divide(b_zong, new MathContext(4));
  									BigDecimal b_o40 = new BigDecimal(unum40_old*100);
  									BigDecimal b_o40_ = b_o40.divide(b_zong, new MathContext(4));
  									BigDecimal b_o100 = new BigDecimal(unum100_old*100);
  									BigDecimal b_o100_ = b_o100.divide(b_zong, new MathContext(4));
					 
					 
					 
					 
					 
					 
					out.print("<tr bgcolor='#FFFFFF'>"+
					                  
					                             "<td>用户数</td>"+
					                             "<td>"+unum05+"&nbsp;("+b05_.toString()+"%)</td><td>"+unum10+"&nbsp;("+b10_.toString()+"%)</td>"+
					                             "<td>"+unum15+"&nbsp;("+b15_.toString()+"%)</td><td>"+unum20+"&nbsp;("+b20_.toString()+"%)</td>"+
					                             "<td>"+unum30+"&nbsp;("+b30_.toString()+"%)</td><td>"+unum40+"&nbsp;("+b40_.toString()+"%)</td>"+
					                             "<td>"+unum100+"&nbsp;("+b100_.toString()+"%)</td><td  bgcolor='#EEEEBB'>"+unum_zong+"</td>");
					                             out.print("<tr bgcolor='#FFFFFF'>"+
					                             "<td>新用户数</td>"+
					                             "<td>"+unum05_new+"&nbsp;("+b_n05_.toString()+"%)</td><td>"+unum10_new+"&nbsp;("+b_n10_.toString()+"%)</td>"+
					                             "<td>"+unum15_new+"&nbsp;("+b_n15_.toString()+"%)</td><td>"+unum20_new+"&nbsp;("+b_n20_.toString()+"%)</td>"+
					                             "<td>"+unum30_new+"&nbsp;("+b_n30_.toString()+"%)</td><td>"+unum40_new+"&nbsp;("+b_n40_.toString()+"%)</td>"+
					                             "<td>"+unum100_new+"&nbsp;("+b_n100_.toString()+"%)</td><td  bgcolor='#EEEEBB'>"+unum_new_zong+"</td>");
					                              out.print("<tr bgcolor='#FFFFFF'>"+
					                             "<td>老用户数</td>"+
					                             "<td>"+unum05_old+"&nbsp;("+b_o05_.toString()+"%)</td><td>"+unum10_old+"&nbsp;("+b_o10_.toString()+"%)</td>"+
					                             "<td>"+unum15_old+"&nbsp;("+b_o15_.toString()+"%)</td><td>"+unum20_old+"&nbsp;("+b_o20_.toString()+"%)</td>"+
					                             "<td>"+unum30_old+"&nbsp;("+b_o30_.toString()+"%)</td><td>"+unum40_old+"&nbsp;("+b_o40_.toString()+"%)</td>"+
					                             "<td>"+unum100_old+"&nbsp;("+b_o100_.toString()+"%)</td><td  bgcolor='#EEEEBB'>"+unum_old_zong+"</td>");
					out.println("</tr>");
					}
					out.println("</table><br>");
		    		%>
		</center>
		<br>
		<i>这里的用户都是指进入游戏的用户</i>
		<br>
	</body>
</html>
