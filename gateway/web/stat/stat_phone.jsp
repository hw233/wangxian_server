<%@ page contentType="text/html;charset=utf-8"%>
<%@ page
	import="java.util.*,com.xuanzhi.tools.text.*,com.xuanzhi.tools.mem.*,com.fy.gamegateway.stat.*,
	com.fy.gamegateway.mieshi.resource.manager.*,
	com.fy.gamegateway.mieshi.server.*,java.sql.*,com.xuanzhi.tools.simplejpa.*,com.xuanzhi.tools.simplejpa.impl.* "%><%!
	
	public String getSql(String startDay,String endDay,String SPV,String channel){
		String sql = "select R.aaa,R.ccc,T.ccc from " +  
		"( select aaa,sum(cc) ccc from ( " +
		"select aa aaa,cc from( " +

		"select aa,count(1) cc from ( " +
		"select phonetype aa from client c where C.TIMEOFFIRSTCONNECTED >= to_date('"+startDay+"','yyyy-mm-dd') " + 
		"and C.TIMEOFFIRSTCONNECTED < to_date('"+endDay+"','yyyy-mm-dd')  " +
		"and (C.HASLOGIN='T' or C.HASREGISTER='T')   " +
		(channel==null?"":(" and channel='"+channel+"'")) +
		") group by aa order by cc desc " +
		") " +
		") group by aaa order by ccc desc " +
		") R left join ( " +


		"select aaa,sum(cc) ccc from ( " +
		"select aa aaa,cc from( " +

		"select aa,count(1) cc from ( " +
		"select phonetype aa from client c where C.TIMEOFFIRSTCONNECTED >= to_date('"+startDay+"','yyyy-mm-dd') " + 
		"and C.TIMEOFFIRSTCONNECTED < to_date('"+endDay+"','yyyy-mm-dd') " +
		"and (C.HASLOGIN='F' and C.HASREGISTER='F')  " +
		(channel==null?"":(" and channel='"+channel+"'")) +
		") group by aa order by cc desc " +
		") " +
		") group by aaa order by ccc desc " +
		") T on R.aaa= T.aaa order by R.ccc desc ";
		
		return sql;
	}
	
	
	public String getSqlForMysql(String startDay,String endDay,String SPV,String channel){
		String sql = "select R.aaa,R.ccc,T.ccc from " +  
		"( select aaa,sum(cc) ccc from ( " +
		"select aa aaa,cc from( " +

		"select aa,count(1) cc from ( " +
		"select phonetype aa from CLIENT_S1 C where C.TIMEOFFIRSTCONNECTED >= str_to_date('"+startDay+"','%Y-%m-%d') " + 
		"and C.TIMEOFFIRSTCONNECTED < str_to_date('"+endDay+"','%Y-%m-%d') " +
		"and (C.HASLOGIN='T' or C.HASREGISTER='T')   " +
		(channel==null?"":(" and channel='"+channel+"'")) +
		") DD group by aa order by cc desc " +
		") FF " +
		") EE group by aaa order by ccc desc " +
		") R left join ( " +


		"select aaa,sum(cc) ccc from ( " +
		"select aa aaa,cc from( " +

		"select aa,count(1) cc from ( " +
		"select phonetype aa from CLIENT_S1 C where C.TIMEOFFIRSTCONNECTED >= str_to_date('"+startDay+"','%Y-%m-%d') " + 
		"and C.TIMEOFFIRSTCONNECTED < str_to_date('"+endDay+"','%Y-%m-%d')   " +
		"and (C.HASLOGIN='F' and C.HASREGISTER='F')  " +
		(channel==null?"":(" and channel='"+channel+"'")) +
		") DD1 group by aa order by cc desc " +
		") FF1 " +
		") EE1 group by aaa order by ccc desc " +
		") T on R.aaa= T.aaa order by R.ccc desc ";
		
		return sql;
	}
	%>
<%
	PackageManager pm = PackageManager.getInstance();
	ResourceManager rm = ResourceManager.getInstance();
	PackageManager.Version vs[] = pm.getRealVersions();
	
	
	ArrayList<String> channels = new ArrayList<String> ();

	for(int i = 0 ; i < vs.length ; i++){
		for(PackageManager.Package p : vs[i].packageList){
			if(channels.contains(p.channel) == false){
				channels.add(p.channel);
			}
		}
	}
	if(channels.contains("APPSTORE_MIESHI") == false){
		channels.add("APPSTORE_MIESHI");
	}
	java.util.Collections.sort(channels);
	channels.add(0,"全部");
	
	StatManager stat = StatManager.getInstance();
	
	
	String SPV = "";
	String SRV = "";
	
	SPV = vs[vs.length-1].versionString;
	if(SPV.indexOf("_") >= 0){
		SPV = SPV.substring(0,SPV.indexOf("_"));
	}
	SRV = ""+rm.getRealResourceVersion();
	
		
	String startDay = request.getParameter("startday");
	String endDay = request.getParameter("endday");
	if(startDay == null) startDay = DateUtil.formatDate(new java.util.Date(),"yyyy-MM-dd");
	if(endDay == null) endDay = DateUtil.formatDate(new java.util.Date(System.currentTimeMillis() + 24*3600000),"yyyy-MM-dd");
	
	String channel = null;

	String ss = request.getParameter("channel");
	if(ss != null){
		int k = Integer.parseInt(ss);
		if(k > 0 && k < channels.size()) {
			channel = channels.get(k);
		}
	}

		
	java.util.Date s = DateUtil.parseDate(startDay,"yyyy-MM-dd");
	java.util.Date e = DateUtil.parseDate(endDay,"yyyy-MM-dd");
		
	int index = 0;
		
	LinkedHashMap<String,int[]> result = new LinkedHashMap<String,int[]>();	
	
			String sql = getSql(startDay,endDay,SPV,channel);
			
			Connection conn = null;
			try{
				String dbType = SimpleEntityManagerFactory.getDbType();

				if(dbType.equals("oracle")){
					SimpleEntityManagerOracleImpl<Client> em =  (SimpleEntityManagerOracleImpl<Client>)stat.em4Client;
					conn = em.getConnection();
				}else{
					SimpleEntityManagerMysqlImpl<Client> em =  (SimpleEntityManagerMysqlImpl<Client>)stat.em4Client;
					conn = em.getConnection();
					 sql = getSqlForMysql(startDay,endDay,SPV,channel);
				}
				Statement stmt = conn.createStatement();
				ResultSet rs = stmt.executeQuery(sql);
				
				while(rs.next()){
					int r[] = new int[2];
					String phone = rs.getString(1);
					r[0] = rs.getInt(2);
					r[1] = rs.getInt(3);
					result.put(phone,r);
				}
				rs.close();
				stmt.close();
			}finally{
				conn.close();
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
		<script language="javascript" type="text/javascript" src="./flotr/lib/prototype-1.6.0.2.js"></script>
		<script language="javascript" type="text/javascript" src="./flotr/flotr-0.2.0-alpha.js"></script>
</head>

	<body>
		
		<h2 style="font-weight:bold;">
			客户端下载统计，服务器程序版本：<%= SPV %>，服务器资源版本：<%= SRV %>
		</h2>
		<form>
		输入开始日期：<input type='text' name='startday' value='<%=startDay %>'>
		结束日期：<input type='text' name='endday' value='<%=endDay %>'>(格式：2011-01-01)<br/>
		请选择渠道：<select name='channel'>
		<%
			int selected = 0;
			if(channel != null) selected = channels.indexOf(channel);
			for(int i = 0 ; i < channels.size() ; i++){
				if(i == selected){
					out.println("<option value='"+i+"' selected >"+channels.get(i)+"</option>");
				}else{
					out.println("<option value='"+i+"' >"+channels.get(i)+"</option>");
				}
			}
		%></select>
		<input type='submit' value='提交'> </form>
		<br/>

		<table id='test2' align='center' width='100%' cellpadding='1' bgcolor='#000000' cellspacing='1' border='0'>
		<tr bgcolor='#EEEEBB'>
			<td>机型</td><td>客户端总数</td><td>成功注册数量</td><td>没有注册数量</td><td>成功注册率</td>
		</tr>
		<%
			String keys[] = result.keySet().toArray(new String[0]);	
			for(int i = 0 ; i < keys.length ; i++){
				int[] r = result.get(keys[i]);
				if(r[0]*100/(r[0]+r[1]) > 60){
					out.println("<tr bgcolor='#FFFFFF'>");
					out.println("<td>"+keys[i]+"</td>");
					out.println("<td>"+(r[0]+r[1])+"</td>");
					out.println("<td>"+r[0]+"</td>");
					out.println("<td>"+r[1]+"</td>");
					out.println("<td>"+(r[0]*100/(r[0]+r[1]))+"%</td>");
					out.println("</tr>");
				}else{
					out.println("<tr bgcolor='#EEEEEE'>");
					out.println("<td>"+keys[i]+"</td>");
					out.println("<td>"+(r[0]+r[1])+"</td>");
					out.println("<td>"+r[0]+"</td>");
					out.println("<td>"+r[1]+"</td>");
					out.println("<td>"+(r[0]*100/(r[0]+r[1]))+"%</td>");
					out.println("</tr>");
				}
			}
		%>
		</table>
	</body>
</html>
