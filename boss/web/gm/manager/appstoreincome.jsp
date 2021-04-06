<%@page import="java.sql.*,java.util.*,com.xuanzhi.tools.text.*"%>
<%@ page language="java" contentType="text/html; charset=utf-8"
	pageEncoding="utf-8"%><%!
public class DataPair {
	public String mt;
	public long pay;
	
	public DataPair(String mt, long pay) {
		this.mt = mt;
		this.pay = pay;
	}
}
%>
<html xmlns="http://www.w3.org/1999/xhtml" lang="zh-cn">
	<head>
		<meta http-equiv="Content-Type" content="text/html; charset=utf-8">
		<title>查询</title>
		<link rel="stylesheet" href="../css/style.css" />
		<link rel="stylesheet" href="../css/atalk.css" />
	</head>
	<body>
		<br>
		<br>
		<h1>
			APPSTORE渠道收入统计
		</h1>
<%
String sdate = request.getParameter("sdate");
String edate = request.getParameter("edate");
%>
<form action="appstoreincome.jsp" method=post name=f1>
	开始日期:
	<input type=text name="sdate" size="25" value="<%=(sdate!=null?sdate:DateUtil.formatDate(new java.util.Date(),"yyyy-MM-dd")) %>"/>(yyyy-MM-dd)
	结束日期:
	<input type=text name="edate" size="25" value="<%=(edate!=null?edate:DateUtil.formatDate(new java.util.Date(),"yyyy-MM-dd")) %>"/>(yyyy-MM-dd)
	<input type=submit name=sub1 value="查询">
</form>
		
		<% 
if(sdate != null) {
	LinkedHashMap<String,List<DataPair>> dpmap = new LinkedHashMap<String,List<DataPair>>();
	HashMap<String,String> headMap = new HashMap<String,String>();
	Connection conn = null;
	try
	{
		Class.forName("oracle.jdbc.driver.OracleDriver");
	}catch(ClassNotFoundException e) {
		out.println("装载驱动包出现异常!请查正！");
		e.printStackTrace();
	}
	java.util.Date cdate = DateUtil.parseDate(sdate, "yyyy-MM-dd");
	Calendar cal = Calendar.getInstance();
	cal.setTime(cdate);
	cal.set(Calendar.HOUR_OF_DAY, 0);
	cal.set(Calendar.MINUTE, 0);
	cal.set(Calendar.SECOND, 0);
	for(int i=0; i<365; i++) {
		try
		{
			long start = cal.getTimeInMillis();
			cal.add(Calendar.DAY_OF_YEAR, 1);
			long end = cal.getTimeInMillis();
			conn = DriverManager.getConnection("jdbc:oracle:thin:@116.213.192.216:1521:ora183","mieshiboss","4Fxtkq5J9ydy3sK1");
			String sql = "select sum(paymoney) as money,pp.registermobiletype from " + 
			"passport pp,orderform o where pp.id=o.passportid and " + 
			"pp.REGISTERCHANNEL='APPSTORE_MIESHI' and o.notifySucc='T' " + 
			"and o.createtime between ? and ? group by pp.registermobiletype";
			PreparedStatement pstmt = conn.prepareStatement(sql);
			pstmt.setLong(1, start);
			pstmt.setLong(2, end);
			ResultSet rs = pstmt.executeQuery();
			List<DataPair> dplist = new ArrayList<DataPair>();
			while(rs.next())
			{
				String mt = rs.getString(2);
				long pay = rs.getLong(1);
				DataPair dp = new DataPair(mt, pay);
				dplist.add(dp);
				headMap.put(dp.mt, "");
			}
			String ndate = DateUtil.formatDate(new java.util.Date(start), "yyyy-MM-dd");
			dpmap.put(ndate, dplist);
			rs.close();
			pstmt.close();
			conn.close();
			if(ndate.equals(edate)) {
				break;
			}
		}catch(Exception e) {
			System.out.println("链接数据库发生异常!");
			e.printStackTrace();
		}
	}


%>
		<table align="center" width="60%" cellpadding="0" cellspacing="0"
			border="0"
			class="sortable-onload-5-6r rowstyle-alt colstyle-alt no-arrow">
			<tr>
				<th align=center width=20%>
					<b>日期</b>
				</th>
				<%
				String mts[] = headMap.keySet().toArray(new String[0]);
				for(String mt : mts) {
				%>
				<th align=center width=20%>
					<b><%=mt %></b>
				</th>
				<%} %>
			</tr>
			<%
			Set<String> dates = dpmap.keySet();
			for(String date : dates) {
			
		 %>
			<tr onmouseover="overTag(this);" onmouseout="outTag(this);">
				<td align="center">
					<%=date %>
				</td>
				<%
				for(String mt : mts) {
					long pay = 0;
					List<DataPair> dplist = dpmap.get(date);
					if(dplist == null) {
						continue;
					}
					for(DataPair dp : dplist) {
						if(dp.mt != null && dp.mt.equals(mt)) {
							pay = dp.pay;
							break;
						} else if(dp.mt == null && mt == null) {
							pay = dp.pay;
							break;
						}
					}
				%>
				<td align="center">
					<%=pay %>
				</td>
				<%} %>
			</tr>
			<%}%>
		</table>
		<br>
		<br>
		<Br>
<%} %>
	</body>
</html>