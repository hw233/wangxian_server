<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<%@page import="com.xuanzhi.tools.simplejpa.impl.SimpleEntityManagerOracleImpl"%>
<%@page import="com.fy.boss.finance.service.OrderFormManager"%>
<%@page import="com.xuanzhi.tools.text.DateUtil"%>
<%@page import="java.sql.Statement"%>
<%@page import="java.text.DecimalFormat"%>
<%@page import="org.apache.commons.lang.StringUtils"%>
<%@page import="com.xuanzhi.tools.text.ParamUtils"%>
<%@page import="java.util.Calendar"%>
<%@page import="java.util.HashMap"%>
<%@page import="java.util.Map"%>
<%@page import="java.sql.ResultSet"%>
<%@page import="java.sql.PreparedStatement"%>
<%@page import="java.sql.Connection"%>
<%@page import="java.sql.DriverManager"%>
<%@page import="java.util.Date"%>
<%@page import="java.text.SimpleDateFormat"%>
<%@page import="java.util.List"%>
<%@page import="com.fy.boss.gm.gmuser.GameChargeRecord"%>
<%@page import="com.fy.boss.gm.gmuser.ChargeStatRecord"%>
<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<% 
	String username = ParamUtils.getParameter(request, "username");
	String passwd =  ParamUtils.getParameter(request, "passwd");
	if(!StringUtils.isEmpty(username) && !(StringUtils.isEmpty(passwd)))
	{
		session.setAttribute("ausername", username);
		session.setAttribute("apassword", passwd);
	}

	if(session.getAttribute("ausername") != null && session.getAttribute("apassword") != null)
	{
		if(!session.getAttribute("ausername").equals("finance"))
		{
			return;
		}
		if(!session.getAttribute("apassword").equals("financewerw@#$@"))
		{
			return;
		}
		
		String dateStr = ParamUtils.getParameter(request, "queryTime","");
		Date date = DateUtil.parseDate(dateStr, "yyyyMM");	
		String dateStr1 = DateUtil.formatDate(date, "yyyy-MM");
		String nextDateStr = dateStr1;
		if(date.getMonth() == 11)
		{
			nextDateStr = date.getYear()+1+1900+"-01";
		}
		else
		{
			String style="##00";
			DecimalFormat df = new DecimalFormat();
			df.applyPattern(style);
			nextDateStr = date.getYear()+1900+"-"+df.format(date.getMonth()+2);
		}
		
		
		/**
		to_date(TO_CHAR(o.createTime / (1000 * 60 * 60 * 24) +
												 TO_DATE('1970-01-01 08:00:00', 'YYYY-MM-DD HH24:MI:SS'),
												 'YYYY-MM-DD HH24:MI:SS'),
								 'YYYY-MM-DD HH24:MI:SS') >= to_date('2012-10-16 18:00:00', 'YYYY-MM-DD HH24:MI:SS') and
			to_date(TO_CHAR(o.createTime / (1000 * 60 * 60 * 24) +
												 TO_DATE('1970-01-01 08:00:00', 'YYYY-MM-DD HH24:MI:SS'),
												 'YYYY-MM-DD HH24:MI:SS'),
								 'YYYY-MM-DD HH24:MI:SS')	<= to_date('2012-10-17 16:00:00', 'YYYY-MM-DD HH24:MI:SS')
		*/
		
		String monthCondition = " and to_date(TO_CHAR(o.createTime / (1000 * 60 * 60 * 24) + " +
				 "TO_DATE('1970-01-01 08:00:00', 'YYYY-MM-DD HH24:MI:SS'), "+
				 "'YYYY-MM-DD HH24:MI:SS'), "+
				"'YYYY-MM-DD HH24:MI:SS') >= to_date('"+dateStr1+"-01 "+"00:00:00"+"', 'YYYY-MM-DD HH24:MI:SS') and "+
				"to_date(TO_CHAR(o.createTime / (1000 * 60 * 60 * 24) +"+
						 "TO_DATE('1970-01-01 08:00:00', 'YYYY-MM-DD HH24:MI:SS'),"+
						 "'YYYY-MM-DD HH24:MI:SS'),"+
		 "'YYYY-MM-DD HH24:MI:SS')	< to_date('"+nextDateStr+"-01 "+"00:00:00"+"', 'YYYY-MM-DD HH24:MI:SS')";
		
		
	/* 	Class.forName("oracle.jdbc.driver.OracleDriver");
		Connection conn = DriverManager.getConnection(
			"jdbc:oracle:thin:@116.213.142.183:1521:ora183", "mieshiboss", "4Fxtkq5J9ydy3sK1");
		
		out.println(conn.getMetaData().get); */
		
		SimpleEntityManagerOracleImpl em = (SimpleEntityManagerOracleImpl)OrderFormManager.getInstance().getEm();
		Connection conn = em.getConnection();
		
		String channels = request.getParameter("channels");
		String channelCondition = "";
		if(!StringUtils.isEmpty(channels))
		{
			String items[] = channels.split("\r*\n");
			if(items.length >= 1)
			{
				for(int i=0;i<items.length;i++)
				{
					String s = items[i];
					if(!StringUtils.isEmpty(s))
					{
						if(i == 0)
						{
							channelCondition = " and (o.userchannel = '" +s+"'";
						}
						else
						{
							channelCondition += " or o.userchannel = '" +s+ "'";
						}
						
						if(i == items.length - 1)
						{
							channelCondition += ")";
						}
					}
				}
				
			}
		}
		
		
		String sql = 
				"select o.userchannel as channel, sum(o.payMoney) as sumMoney " +
						"  from ORDERFORM o " + 
						" where o.responseresult = 1 " +  channelCondition + monthCondition +
						" group by o.userchannel order by o.userchannel";
	
		
		Statement pstmt = conn.createStatement();
		
	
		ResultSet rs = pstmt.executeQuery(sql);
		
		String channel = "";
		long sumMoney = -10000l;
		int sumrow = 0;
		long moneys = 0l;
		
	%>
	
	    
	    
	    
	<html xmlns="http://www.w3.org/1999/xhtml">
	<head>    
	<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
	<title>渠道充值总和</title>
	<link rel="stylesheet" href="style.css"/>
			
	</script>
	</head>
	<body bgcolor="#c8edc1">
	<input type="hidden" value="<%=sql%>" />
	<form method=post>
	日期:<input type="text" name="queryTime"  value="<%=dateStr%>"> 格式:201212 必须输入<br/>
	
	渠道串<textarea rows="20" cols="30" name='channels'  value="<%=channels%>"></textarea>格式：一个渠道串一行 不输入代表查询所有渠道
	<input type='submit' value='提交'>
	</form>
	
	<table>
	  <tr><th>渠道串</th><th>充值总金额</th></tr>
	<%	
		while(rs.next())
		{
			channel = rs.getString("channel");
			sumMoney = rs.getLong("sumMoney");
			sumrow+=1;
			moneys+=sumMoney;
		
				
	%>
			<tr><td><%=channel %></td><td><%=sumMoney %>分</td></tr>
	<%	
		}
		if(rs != null)
		{
			rs.close();
		}
	
		if(pstmt != null)
		{
			pstmt.close();
		}
		
		if(conn != null)
		{
			conn.close();
		}
	%>    
	
		<tr><td>共<%=sumrow%>行</td><td>总计：<%=moneys%>分</td></tr>
	</table>
	<%}
	else
	{
	%>
		<form method="post">
			用户名:<input type="text" name="username"/><br/>
			密码:<input type="password" name="passwd"/><br/>
			<input type="submit" value="提交" /> 
		</form>
	<%	
	}
	%>
</body>
</html>
