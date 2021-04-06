<form method="post">
<input type="hidden" name="isdone" value="true" />
<input type="text" name="begindate"  /> (yyyy-MM-dd)-<input type="text" name="enddate"  />  (yyyy-MM-dd)
<input type="submit">
</form>


<%@page import="java.sql.ResultSet"%>
<%@page import="com.xuanzhi.tools.text.DateUtil"%>
<%@page import="java.sql.PreparedStatement"%>
<%@page import="com.fy.gamegateway.mieshi.waigua.AuthorizeManager"%>
<%@page import="com.fy.gamegateway.mieshi.waigua.ClientAuthorization"%>
<%@page import="com.xuanzhi.tools.simplejpa.impl.SimpleEntityManagerOracleImpl"%>
<%@page import="java.sql.Connection"%>
<%@page import="com.xuanzhi.tools.text.ParamUtils"%>
<%@ page contentType="text/html; charset=utf-8"%>
<%
	boolean isdone = ParamUtils.getBooleanParameter(request,"isdone");
	if(isdone)
	{
		String beginDate = ParamUtils.getParameter(request, "begindate", "");
		String endDate = ParamUtils.getParameter(request, "enddate", "");
		
		long begintime = DateUtil.parseDate(beginDate, "yyyy-MM-dd").getTime();
		long endtime = DateUtil.parseDate(endDate, "yyyy-MM-dd").getTime();
		
		String sql = "select t.phonetype,count(t.phonetype) " +
						"from clientauthorization c, " + 
						"     cliententity t " + 
						"where c.lastlogintime >= ? and c.lastlogintime <= ?" + 
						"and c.clientid = t.clientid " + 
						"group by t.phonetype order by count(t.phonetype) desc";
		
		Connection conn = null;	
		PreparedStatement pstmt = null;
		ResultSet rs = null;
		SimpleEntityManagerOracleImpl<ClientAuthorization> em = (SimpleEntityManagerOracleImpl)AuthorizeManager.getInstance().em_ca;
		try{
			conn = em.getConnection();
			pstmt = conn.prepareStatement(sql);
			pstmt.setLong(1, begintime);
			pstmt.setLong(2, endtime);
			rs = pstmt.executeQuery();
%>
	<table border="1px;">
	<tr>
		<td>机型</td>
		<td>人数</td>
	</tr>
<% 
			int sumPhoneType = 0;
			int sum = 0;
			while(rs.next())
			{
				String phoneType = rs.getString(1);
				int personNum = rs.getInt(2);
				sumPhoneType+=1;
				sum += personNum;
%>
	<tr>
		<td><%=phoneType %></td>
		<td><%=personNum %></td>
	</tr>	
<%				
			}
%>
	<tr>
		<td>共<%=sumPhoneType %>种机型</td>
		<td>共<%=sum%>人</td>
	</tr>
	</table>
<%			
			rs.close();
			pstmt.close();
			conn.close();
		}
		finally
		{
			rs = null;
			pstmt = null;
			conn = null;
		}
		
		
		
	}
	
%>


