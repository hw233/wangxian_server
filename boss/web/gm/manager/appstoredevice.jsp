<%@page import="java.sql.*,java.util.*,com.xuanzhi.tools.text.*,com.fy.boss.finance.service.*,
				com.fy.boss.finance.model.*,
				com.fy.boss.authorize.service.*,
				com.fy.boss.authorize.model.*"%>
<%@ page language="java" contentType="text/html; charset=utf-8"
	pageEncoding="utf-8"%><%!
public class DataPair {
	public List<String> ulist;
	public String dcode;
	
	public DataPair(List<String> ulist, String dcode) {
		this.ulist = ulist;
		this.dcode = dcode;
	}
}
public class SavePair {
	public String username;
	public long amount;
	
	public SavePair(String username, long amount) {
		this.username = username;
		this.amount = amount;
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
			APPSTORE充值设备统计
		</h1>
		<form action="appstoredevice.jsp" method=post name=f1>
			<input type=submit name=sub1 value="查询">
			<input type=hidden name=subed value="true">
		</form>
		
		<%
String subed = request.getParameter("subed");
if(subed != null) {
	Connection conn = null;
	try
	{
		Class.forName("oracle.jdbc.driver.OracleDriver");
	}catch(ClassNotFoundException e) {
		out.println("装载驱动包出现异常!请查正！");
		e.printStackTrace();
	}
	HashMap<String,DataPair> smap = new HashMap<String,DataPair>();
	try
	{
		conn = DriverManager.getConnection("jdbc:oracle:thin:@116.213.192.216:1521:ora183","mieshiboss","sqagepwboss");
		String sql = "select distinct pp.username,o.deviceCode from passport pp, orderform o where o.passportid=pp.id " 
		+ "and registerchannel='APPSTORE_MIESHI' and o.deviceCode is not null";
		PreparedStatement pstmt = conn.prepareStatement(sql);
		ResultSet rs = pstmt.executeQuery();
		while(rs.next())
		{
			try {
				String uname = rs.getString(1);
				String dcode = rs.getString(2);
				dcode = dcode.split("MACADDRESS")[0];
				DataPair dp = smap.get(dcode);
				if(dp == null) {
					dp = new DataPair(new ArrayList<String>(), dcode);
					smap.put(dcode, dp);
				}
				if(!dp.ulist.contains(uname)) {
					dp.ulist.add(uname);
				}
			} catch(Exception e) {
				e.printStackTrace();
			}
		}
		rs.close();
		pstmt.close();
		conn.close();
	}catch(Exception e) {
		System.out.println("链接数据库发生异常!");
		e.printStackTrace();
	}
	DataPair dps[] = smap.values().toArray(new DataPair[0]);
	Arrays.sort(dps, new Comparator<DataPair>(){
		public int compare(DataPair d1, DataPair d2) {
			if(d1.ulist.size() > d2.ulist.size()) {
				return -1;
			} else if(d1.ulist.size() < d2.ulist.size()) {
				return 1;
			}
			return 0;
		}
	});
%>
		<table align="center" width="80%" cellpadding="0" cellspacing="0"
			border="0"
			class="sortable-onload-5-6r rowstyle-alt colstyle-alt no-arrow">
			<tr>
				<th align=center width=30% rowspan="2">
					<b>设备UUID</b>
				</th>
				<th align=center width=70% colspan="4">
					<b>充值账户详情</b>
				</th>
			</tr>
			<tr>
				<th align=center width=15%>
					<b>数量</b>
				</th>
				<th align=center width=20%>
					<b>总额</b>
				</th>
				<th align=center width=20%>
					<b>帐号</b>
				</th>
				<th align=center width=20%>
					<b>充值额度</b>
				</th>
			</tr>
			<%
			for(DataPair dp : dps) {
				String dcode = dp.dcode;
				List<String> ulist = dp.ulist;
				if(ulist.size() < 3) {
					continue;
				}
				OrderFormManager om = OrderFormManager.getInstance();
				List<SavePair> slist = new ArrayList<SavePair>();
				int total = 0;
				for(String uname : ulist) {
					int amount = 0;
					Passport passport = PassportManager.getInstance().getPassport(uname);
					List<OrderForm> olist = om.getUserSuccessOrderForms(passport.getId(), 0, 1000);
					for(OrderForm order : olist) {
						if(order.isNotifySucc()) {
							String dd = order.getDeviceCode();
							if(dd != null) {
								try {
									dd = dd.split("MACADDRESS")[0];
									if(dd.equals(dcode)) {
										amount += order.getPayMoney();
									}
								} catch(Exception e) {
									e.printStackTrace();
								}
							}
						}
					}
					if(amount > 0) {
						SavePair sp = new SavePair(uname, amount);
						slist.add(sp);
					}
					total += amount;
				}
				if(slist.size() < 3) {
					continue;
				}
		 %>
			<tr onmouseover="overTag(this);" onmouseout="outTag(this);">
				<td align="center" rowspan="<%=ulist.size() %>">
					<%=dcode %>
				</td>
				<td align="center" rowspan="<%=ulist.size() %>">
					<%=ulist.size() %>
				</td>
				<td align="center" rowspan="<%=ulist.size() %>">
					<%=total %>
				</td>
				<td align="center">
					<%=slist.get(0).username %>
				</td>
				<td align="center">
					<%=slist.get(0).amount %>
				</td>
			</tr>
		  <%
		  for(int i=1; i<slist.size(); i++) {%>
		  <tr>
		 	 <td align="center">
				<%=slist.get(i).username %>
			</td>
			<td align="center">
				<%=slist.get(i).amount %>
			</td>
		  </tr>
		  <%}%>
		  <%}%>
		</table>
<%} %>
		<br>
		<br>
		<Br>
	</body>
</html>