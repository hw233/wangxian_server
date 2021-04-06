<%@ page contentType="text/html;charset=utf-8"%>
<%@ page
	import="java.util.*,com.xuanzhi.tools.text.*,com.xuanzhi.tools.mem.*,com.fy.gamegateway.stat.*,
	com.fy.gamegateway.mieshi.resource.manager.*,
	com.fy.gamegateway.mieshi.server.*,java.sql.*,com.xuanzhi.tools.simplejpa.*,com.xuanzhi.tools.simplejpa.impl.* "%><%!
	public static ArrayList<String> ADD_CHANNELS = new ArrayList<String>();
	//%Y-%m-%d %H:%i:%S
	public String getSql(String startDay,String endDay,String channel,String platform,String SPV,String SRV){
		String sql = "select " + 
		"(select count(*) from client where "+
		"timeoffirstconnected >= to_date('"+startDay+"','yyyy-mm-dd') and timeoffirstconnected < to_date('"+endDay+"','yyyy-mm-dd') "
		+ (channel == null?"":(" and channel='"+channel+"'"))
		+ (platform == null?"":(" and platform='"+platform+"'"))
		+") as registerNUM, "+
		"(select count(*) from client where "+ 
		"timeoffirstconnected >= to_date('"+startDay+"','yyyy-mm-dd') and timeoffirstconnected < to_date('"+endDay+"','yyyy-mm-dd') "+
		"and CPVOLC='"+SPV+"' "
		+ (channel == null?"":(" and channel='"+channel+"'"))
		+ (platform == null?"":(" and platform='"+platform+"'"))
		+") as newlyPackageOfReg,"+
		"(select count(*) from client where "+
		"timeoffirstconnected >= to_date('"+startDay+"','yyyy-mm-dd') and timeoffirstconnected < to_date('"+endDay+"','yyyy-mm-dd') "+
		"and CRVOLC='"+SRV+"' "
		+ (channel == null?"":(" and channel='"+channel+"'"))
		+ (platform == null?"":(" and platform='"+platform+"'"))
		+") as newlyResourceOfReg, "+
		
		"(select count(*) from client where "+ 
		"timeoffirstconnected >= to_date('"+startDay+"','yyyy-mm-dd') and timeoffirstconnected < to_date('"+endDay+"','yyyy-mm-dd') "+
		"	and (hasLogin='T' or hasRegister='T') "
		+ (channel == null?"":(" and channel='"+channel+"'"))
		+ (platform == null?"":(" and platform='"+platform+"'"))
		+") as regOfReg, "+
				
		"(select count(*) from client where "+
		"timeoflastconnected >= to_date('"+startDay+"','yyyy-mm-dd') and timeoflastconnected < to_date('"+endDay+"','yyyy-mm-dd') "
		+ (channel == null?"":(" and channel='"+channel+"'"))
		+ (platform == null?"":(" and platform='"+platform+"'"))
		+") as loginNUM, "+
		"(select count(*) from client where "+
		"timeoflastconnected >= to_date('"+startDay+"','yyyy-mm-dd') and timeoflastconnected < to_date('"+endDay+"','yyyy-mm-dd') "+
		"and CPVOLC='"+SPV+"' "
		+ (channel == null?"":(" and channel='"+channel+"'"))
		+ (platform == null?"":(" and platform='"+platform+"'"))
		+") as newlyPackageOfLogin, "+
		"(select count(*) from client where "+
		"timeoflastconnected >= to_date('"+startDay+"','yyyy-mm-dd') and timeoflastconnected < to_date('"+endDay+"','yyyy-mm-dd') "+
		"and CRVOLC='"+SRV+"' "
		+ (channel == null?"":(" and channel='"+channel+"'"))
		+ (platform == null?"":(" and platform='"+platform+"'"))
		+") as newLyResourceOfLogin, "+
		"(select count(*) from client where "+
		"lastLoginTime >= to_date('"+startDay+"','yyyy-mm-dd') and lastLoginTime < to_date('"+endDay+"','yyyy-mm-dd') "+
		"and timeoflastconnected >= to_date('"+startDay+"','yyyy-mm-dd') and timeoflastconnected < to_date('"+endDay+"','yyyy-mm-dd') "
		
		+ (channel == null?"":(" and channel='"+channel+"'"))
		+ (platform == null?"":(" and platform='"+platform+"'"))
		+") as regOfLogin, "+
		"(select count(*) from client where "+
		"lastLoginTime >= to_date('"+startDay+"','yyyy-mm-dd') and lastLoginTime < to_date('"+endDay+"','yyyy-mm-dd') "
		+ (channel == null?"":(" and channel='"+channel+"'"))
		+ (platform == null?"":(" and platform='"+platform+"'"))
		+") as regOfLogin2 "+
		"from dual";
		return sql;
	}
	
	//%Y-%m-%d %H:%i:%S
/**
	select count(*) from
*/
	public String getSqlForMysql(String startDay,String endDay,String channel,String platform,String SPV,String SRV){
		
		int tableNum = 2;
		
		String subSql4RegisterNum = "select sum(c) from ( ";
				
		for(int i=0; i<tableNum; i++)
		{
			String templateSql =  " select count(*) c from CLIENT_S"+(i+1)+" where "+
					"timeoffirstconnected >= str_to_date('"+startDay+"','%Y-%m-%d') and timeoffirstconnected < str_to_date('"+endDay+"','%Y-%m-%d') "
					+ (channel == null?"":(" and channel='"+channel+"'"))
					+ (platform == null?"":(" and platform='"+platform+"'"))
					+" ";
					
			if(i == 0)
			{
				subSql4RegisterNum += templateSql;
			}
			else
			{
				subSql4RegisterNum += " union  " + templateSql;
			}
		}
		
		subSql4RegisterNum += " ) t";
		
		
		
		String subSql4RegOfReg = "select sum(c) from ( ";
		for(int i=0; i<tableNum; i++)
		{
			String templateSql =  "select count(*) c from CLIENT_S"+(i+1)+" where "+ 
					"timeoffirstconnected >= str_to_date('"+startDay+"','%Y-%m-%d') and timeoffirstconnected < str_to_date('"+endDay+"','%Y-%m-%d') "+
					"	and (hasLogin='T' or hasRegister='T') "
					+ (channel == null?"":(" and channel='"+channel+"'"))
					+ (platform == null?"":(" and platform='"+platform+"'"))
					+" ";
					
			if(i == 0)
			{
				subSql4RegOfReg += templateSql;
			}
			else
			{
				subSql4RegOfReg += " union  " + templateSql;
			}
		}
		
		subSql4RegOfReg += " ) t";
		
		
		
		String subSql4LoginNUM = "select sum(c) from ( ";
		for(int i=0; i<tableNum; i++)
		{
			String templateSql =  " select count(*) c from CLIENT_S"+(i+1)+" where "+
					"timeoflastconnected >= str_to_date('"+startDay+"','%Y-%m-%d') and timeoflastconnected < str_to_date('"+endDay+"','%Y-%m-%d') "
					+ (channel == null?"":(" and channel='"+channel+"'"))
					+ (platform == null?"":(" and platform='"+platform+"'"))
					+" ";
					
			if(i == 0)
			{
				subSql4LoginNUM += templateSql;
			}
			else
			{
				subSql4LoginNUM += " union  " + templateSql;
			}
		}
		
		subSql4LoginNUM += " ) t";

		 
		String sql = "select " + 
		"("+subSql4RegisterNum+") as registerNUM, "+//新增客户端数
		"(select count(*) from CLIENT_S1 where "+ 
		"timeoffirstconnected >= str_to_date('"+startDay+"','%Y-%m-%d') and timeoffirstconnected < str_to_date('"+endDay+"','%Y-%m-%d') "+
		"and CPVOLC='"+SPV+"' "
		+ (channel == null?"":(" and channel='"+channel+"'"))
		+ (platform == null?"":(" and platform='"+platform+"'"))
		+") as newlyPackageOfReg,"+
		"(select count(*) from CLIENT_S1 where "+
		"timeoffirstconnected >= str_to_date('"+startDay+"','%Y-%m-%d') and timeoffirstconnected < str_to_date('"+endDay+"','%Y-%m-%d') "+
		"and CRVOLC='"+SRV+"' "
		+ (channel == null?"":(" and channel='"+channel+"'"))
		+ (platform == null?"":(" and platform='"+platform+"'"))
		+") as newlyResourceOfReg, "+
		
		"("+subSql4RegOfReg+") as regOfReg, "+  //新增客户端的注册或者登录数
				
		"(" +subSql4LoginNUM +" ) as loginNUM, "+  //当时 全部客户端数
		"(select count(*) from CLIENT_S1 where "+
		"timeoflastconnected >= str_to_date('"+startDay+"','%Y-%m-%d') and timeoflastconnected < str_to_date('"+endDay+"','%Y-%m-%d') "+
		"and CPVOLC='"+SPV+"' "
		+ (channel == null?"":(" and channel='"+channel+"'"))
		+ (platform == null?"":(" and platform='"+platform+"'"))
		+") as newlyPackageOfLogin, "+
		"(select count(*) from CLIENT_S1 where "+
		"timeoflastconnected >= str_to_date('"+startDay+"','%Y-%m-%d') and timeoflastconnected < str_to_date('"+endDay+"','%Y-%m-%d') "+
		"and CRVOLC='"+SRV+"' "
		+ (channel == null?"":(" and channel='"+channel+"'"))
		+ (platform == null?"":(" and platform='"+platform+"'"))
		+") as newLyResourceOfLogin, "+
		"(select count(*) from CLIENT_S1 where "+
		"lastLoginTime >= str_to_date('"+startDay+"','%Y-%m-%d') and lastLoginTime < str_to_date('"+endDay+"','%Y-%m-%d') "+
		"and timeoflastconnected >= str_to_date('"+startDay+"','%Y-%m-%d') and timeoflastconnected < str_to_date('"+endDay+"','%Y-%m-%d') "
		
		+ (channel == null?"":(" and channel='"+channel+"'"))
		+ (platform == null?"":(" and platform='"+platform+"'"))
		+") as regOfLogin, "+
		"(select count(*) from CLIENT_S1 where "+
		"lastLoginTime >= str_to_date('"+startDay+"','%Y-%m-%d') and lastLoginTime < str_to_date('"+endDay+"','%Y-%m-%d') "
		+ (channel == null?"":(" and channel='"+channel+"'"))
		+ (platform == null?"":(" and platform='"+platform+"'"))
		+") as regOfLogin2 "+
		"from dual";
		return sql;
	}
	
	public String getChannelSql(String startDay,String endDay,String platform,String SPV,String SRV,String type){
		if(type.equals("registerNUM")){
			return "select channel,count(*) as count from client where "+
			"timeoffirstconnected >= to_date('"+startDay+"','yyyy-mm-dd') and timeoffirstconnected < to_date('"+endDay+"','yyyy-mm-dd') "
			+ (platform == null?"":(" and platform='"+platform+"'"))
			+ " group by channel order by count desc";
		}
		if(type.equals("newlyPackageOfReg")){
			return "select channel,count(*) from client where "+ 
			"timeoffirstconnected >= to_date('"+startDay+"','yyyy-mm-dd') and timeoffirstconnected < to_date('"+endDay+"','yyyy-mm-dd') "+
			"and CPVOLC='"+SPV+"' "
			+ (platform == null?"":(" and platform='"+platform+"'"))
			+" group by channel ";
		}
		if(type.equals("newlyResourceOfReg")){
			return "select channel,count(*) from client where "+ 
			"timeoffirstconnected >= to_date('"+startDay+"','yyyy-mm-dd') and timeoffirstconnected < to_date('"+endDay+"','yyyy-mm-dd') "+
			"and CRVOLC='"+SRV+"' "
			+ (platform == null?"":(" and platform='"+platform+"'"))
			+" group by channel ";

		}

		if(type.equals("regOfReg")){
			return "select channel,count(*) from client where "+ 
			"timeoffirstconnected >= to_date('"+startDay+"','yyyy-mm-dd') and timeoffirstconnected < to_date('"+endDay+"','yyyy-mm-dd') "+
			"	and (hasLogin='T' or hasRegister='T') "
			+ (platform == null?"":(" and platform='"+platform+"'"))
			+" group by channel ";

		}
		
		if(type.equals("loginNUM")){
			return "select channel,count(*) from client where "+ 
			"timeoflastconnected >= to_date('"+startDay+"','yyyy-mm-dd') and timeoflastconnected < to_date('"+endDay+"','yyyy-mm-dd') "
			+ (platform == null?"":(" and platform='"+platform+"'"))
			+" group by channel ";

		}
		
		if(type.equals("newlyPackageOfLogin")){
			return "select channel,count(*) from client where "+ 
			"timeoflastconnected >= to_date('"+startDay+"','yyyy-mm-dd') and timeoflastconnected < to_date('"+endDay+"','yyyy-mm-dd') "
			+"and CPVOLC='"+SPV+"' "
			+ (platform == null?"":(" and platform='"+platform+"'"))
			+" group by channel ";

		}
		
		if(type.equals("newLyResourceOfLogin")){
			return "select channel,count(*) from client where "+ 
			"timeoflastconnected >= to_date('"+startDay+"','yyyy-mm-dd') and timeoflastconnected < to_date('"+endDay+"','yyyy-mm-dd') "
			+"and CRVOLC='"+SRV+"' "
			+ (platform == null?"":(" and platform='"+platform+"'"))
			+" group by channel ";

		}

		if(type.equals("regOfLogin")){
			return "select channel,count(*) from client where "+ 
			"lastLoginTime >= to_date('"+startDay+"','yyyy-mm-dd') and lastLoginTime < to_date('"+endDay+"','yyyy-mm-dd') and "+
			"timeoflastconnected >= to_date('"+startDay+"','yyyy-mm-dd') and timeoflastconnected < to_date('"+endDay+"','yyyy-mm-dd') "
			+ (platform == null?"":(" and platform='"+platform+"'"))
			+" group by channel ";

		}


		if(type.equals("regOfLogin2")){
			return "select channel,count(*) from client where "+ 
			"lastLoginTime >= to_date('"+startDay+"','yyyy-mm-dd') and lastLoginTime < to_date('"+endDay+"','yyyy-mm-dd') "
			+ (platform == null?"":(" and platform='"+platform+"'"))
			+" group by channel ";

		}
		return null;
	}
	
	
	public String getChannelSqlForMysql(String startDay,String endDay,String platform,String SPV,String SRV,String type){
		if(type.equals("registerNUM")){
			return "select channel,count(*) as count from CLIENT_S1 where "+
			"timeoffirstconnected >= str_to_date('"+startDay+"','%Y-%m-%d') and timeoffirstconnected < str_to_date('"+endDay+"','%Y-%m-%d') "
			+ (platform == null?"":(" and platform='"+platform+"'"))
			+ " group by channel order by count desc";
		}
		if(type.equals("newlyPackageOfReg")){
			return "select channel,count(*) from CLIENT_S1 where "+ 
			"timeoffirstconnected >= str_to_date('"+startDay+"','%Y-%m-%d') and timeoffirstconnected < str_to_date('"+endDay+"','%Y-%m-%d') "+
			"and CPVOLC='"+SPV+"' "
			+ (platform == null?"":(" and platform='"+platform+"'"))
			+" group by channel ";
		}
		if(type.equals("newlyResourceOfReg")){
			return "select channel,count(*) from CLIENT_S1 where "+ 
			"timeoffirstconnected >= str_to_date('"+startDay+"','%Y-%m-%d') and timeoffirstconnected < str_to_date('"+endDay+"','%Y-%m-%d') "+
			"and CRVOLC='"+SRV+"' "
			+ (platform == null?"":(" and platform='"+platform+"'"))
			+" group by channel ";

		}

		if(type.equals("regOfReg")){
			return "select channel,count(*) from CLIENT_S1 where "+ 
			"timeoffirstconnected >= str_to_date('"+startDay+"','%Y-%m-%d') and timeoffirstconnected < str_to_date('"+endDay+"','%Y-%m-%d') "+
			"	and (hasLogin='T' or hasRegister='T') "
			+ (platform == null?"":(" and platform='"+platform+"'"))
			+" group by channel ";

		}
		
		if(type.equals("loginNUM")){
			return "select channel,count(*) from CLIENT_S1 where "+ 
			"timeoflastconnected >= str_to_date('"+startDay+"','%Y-%m-%d') and timeoflastconnected < str_to_date('"+endDay+"','%Y-%m-%d') "
			+ (platform == null?"":(" and platform='"+platform+"'"))
			+" group by channel ";

		}
		
		if(type.equals("newlyPackageOfLogin")){
			return "select channel,count(*) from CLIENT_S1 where "+ 
			"timeoflastconnected >= str_to_date('"+startDay+"','%Y-%m-%d') and timeoflastconnected < str_to_date('"+endDay+"','%Y-%m-%d') "
			+"and CPVOLC='"+SPV+"' "
			+ (platform == null?"":(" and platform='"+platform+"'"))
			+" group by channel ";

		}
		
		if(type.equals("newLyResourceOfLogin")){
			return "select channel,count(*) from CLIENT_S1 where "+ 
			"timeoflastconnected >= str_to_date('"+startDay+"','%Y-%m-%d') and timeoflastconnected < str_to_date('"+endDay+"','%Y-%m-%d') "
			+"and CRVOLC='"+SRV+"' "
			+ (platform == null?"":(" and platform='"+platform+"'"))
			+" group by channel ";

		}

		if(type.equals("regOfLogin")){
			return "select channel,count(*) from CLIENT_S1 where "+ 
			"lastLoginTime >= str_to_date('"+startDay+"','%Y-%m-%d') and lastLoginTime < str_to_date('"+endDay+"','%Y-%m-%d') and "+
			"timeoflastconnected >= str_to_date('"+startDay+"','%Y-%m-%d') and timeoflastconnected < str_to_date('"+endDay+"','%Y-%m-%d') "
			+ (platform == null?"":(" and platform='"+platform+"'"))
			+" group by channel ";

		}


		if(type.equals("regOfLogin2")){
			return "select channel,count(*) from CLIENT_S1 where "+ 
			"lastLoginTime >= str_to_date('"+startDay+"','%Y-%m-%d') and lastLoginTime < str_to_date('"+endDay+"','%Y-%m-%d') "
			+ (platform == null?"":(" and platform='"+platform+"'"))
			+" group by channel ";

		}
		return null;
	}
	%>
<%
	PackageManager pm = PackageManager.getInstance();
	ResourceManager rm = ResourceManager.getInstance();
	PackageManager.Version vs[] = pm.getRealVersions();
	String addNewChannel = request.getParameter("newChannel");
	if(addNewChannel != null && !addNewChannel.trim().equals("")){
		if(!ADD_CHANNELS.contains(addNewChannel)){
			ADD_CHANNELS.add(addNewChannel);
		}
	}
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
	for(int i = 0; i < ADD_CHANNELS.size(); i++){
		String newChannel = ADD_CHANNELS.get(i);
		channels.add(newChannel);
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
	
	String platforms[] = new String[]{"全部","IOS","Android"};

	String channel = null;
	String platform = null;

	String ss = request.getParameter("channel");
	if(ss != null){
		int k = Integer.parseInt(ss);
		if(k > 0 && k < channels.size()) {
			channel = channels.get(k);
		}
	}
	ss = request.getParameter("platform");
	if(ss != null){
		int k = Integer.parseInt(ss);
		if(k > 0 && k < platforms.length) {
			platform = platforms[k];
		}
	}
	
	String startDay = request.getParameter("startday");
	String endDay = request.getParameter("endday");
	if(startDay == null) startDay = DateUtil.formatDate(new java.util.Date(),"yyyy-MM-dd");
	if(endDay == null) endDay = DateUtil.formatDate(new java.util.Date(),"yyyy-MM-dd");
	
	

	boolean channel_expand = "true".equalsIgnoreCase(request.getParameter("channel_expand"));
	
	java.util.Date s = DateUtil.parseDate(startDay,"yyyy-MM-dd");
	java.util.Date e = DateUtil.parseDate(endDay,"yyyy-MM-dd");
	
	LinkedHashMap<String,int[]> result = new LinkedHashMap<String,int[]>();	
	
	if(channel_expand == false){
	
		int index = 0;
			while(s.getTime() < e.getTime() + 3600000){
				String day = DateUtil.formatDate(s,"yyyy-MM-dd");
				String nextDay = DateUtil.formatDate(new java.util.Date(s.getTime()+25*3600*1000L),"yyyy-MM-dd");
				
				String sql = getSql(day,nextDay,channel,platform,SPV,SRV);
				
				
				Connection conn = null;
				try{
					String dbType = SimpleEntityManagerFactory.getDbType();

					if(dbType.equals("oracle")){
						SimpleEntityManagerOracleImpl<Client> em =  (SimpleEntityManagerOracleImpl<Client>)stat.em4Client;
						conn = em.getConnection();
					}else{
						SimpleEntityManagerMysqlImpl<Client> em =  (SimpleEntityManagerMysqlImpl<Client>)stat.em4Client;
						conn = em.getConnection();
						sql = getSqlForMysql(day,nextDay,channel,platform,SPV,SRV);
					}
					Statement stmt = conn.createStatement();
					ResultSet rs = stmt.executeQuery(sql);
					int r[] = new int[9];
					if(rs.next()){
						r[0] = rs.getInt(1);
						r[1] = rs.getInt(2);
						r[2] = rs.getInt(3);
						r[3] = rs.getInt(4);
						r[4] = rs.getInt(5);
						r[5] = rs.getInt(6);
						r[6] = rs.getInt(7);
						r[7] = rs.getInt(8);
						r[8] = rs.getInt(9);
					}
					result.put(day,r);
					rs.close();
					stmt.close();
				}finally{
					conn.close();
				}
	
				index++;
				s.setTime(s.getTime() + 24L * 3600 * 1000);
			}
	}else{
		String sss[] = new String[]{"registerNUM","newlyPackageOfReg","newlyResourceOfReg","regOfReg","loginNUM","newlyPackageOfLogin","newlyPackageOfLogin","regOfLogin","regOfLogin2"};
		
		String day = DateUtil.formatDate(s,"yyyy-MM-dd");
		String nextDay = DateUtil.formatDate(new java.util.Date(e.getTime()+25*3600*1000L),"yyyy-MM-dd");
		ArrayList<LinkedHashMap<String,Integer>> al = new ArrayList<LinkedHashMap<String,Integer>>();
		for(int i = 0 ; i < sss.length ; i++){
			String sql = getChannelSql(day,nextDay,platform,SPV,SRV,sss[i]);
			
			//System.out.println("sql = " + sql);
			
			Connection conn = null;
			try{
				String dbType = SimpleEntityManagerFactory.getDbType();

				if(dbType.equals("oracle")){
					SimpleEntityManagerOracleImpl<Client> em =  (SimpleEntityManagerOracleImpl<Client>)stat.em4Client;
					conn = em.getConnection();
				}else{
					SimpleEntityManagerMysqlImpl<Client> em =  (SimpleEntityManagerMysqlImpl<Client>)stat.em4Client;
					conn = em.getConnection();
					sql = getChannelSqlForMysql(day,nextDay,platform,SPV,SRV,sss[i]);
				}
				Statement stmt = conn.createStatement();
				ResultSet rs = stmt.executeQuery(sql);
	
				LinkedHashMap<String,Integer> m = new LinkedHashMap<String,Integer>();
				while(rs.next()){
					String cc = rs.getString(1);
					int count = rs.getInt(2);
					m.put(cc,count);
				}
				al.add(m);
				rs.close();
				stmt.close();
			}finally{
				conn.close();
			}
		}
		LinkedHashMap<String,Integer> m = al.get(0);
		String cs[] = m.keySet().toArray(new String[0]);
		for(int i = 0 ; i < cs.length ; i++){
			String ch = cs[i];
			int r[] = new int[sss.length];
			for(int j = 0 ; j < sss.length ; j++){
				m = al.get(j);
				Integer v = m.get(ch);
				if(v == null) r[j] = 0;
				else r[j] = v.intValue();
			}
			result.put(ch,r);
		}
		
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
		%>
		</select> &nbsp;&nbsp;请选择平台：<select name='platform'>
		<%
			for(int i = 0 ; i < platforms.length ; i++){
				if(platforms[i].equals(platform)){
					out.println("<option value='"+i+"' selected >"+platforms[i]+"</option>");
				}else{
					out.println("<option value='"+i+"' >"+platforms[i]+"</option>");
				}
			}
		%>
		</select> 
		<input type='checkbox' name='channel_expand' value='true' <%= channel_expand?"checked":""%> >展开所有渠道
		<input type='submit' value='提交'> </form>
		<br/>

		<table id='test2' align='center' width='100%' cellpadding='1' bgcolor='#000000' cellspacing='1' border='0'>
		<tr bgcolor='#EEEEBB'>
			<td><%= channel_expand?"渠道":"日期" %></td><td>当日新增客户端数</td><td>新增客户端中最新程序包</td><td>新增客户端中最新资源包</td><td>新增客户端中有注册或登录</td>
			<td>当日全部客户端数</td><td>全部客户端中最新程序包</td><td>全部客户端中最新资源包</td><td>全部客户端中今日登陆数</td>
		<td>今日登陆客户端数</td></tr>
		<%
			String keys[] = result.keySet().toArray(new String[0]);	
			for(int i = 0 ; i < keys.length ; i++){
				int[] r = result.get(keys[i]);
				out.println("<tr bgcolor='#FFFFFF'>");
				out.println("<td>"+keys[i]+"</td>");
				for(int j = 0 ; j < r.length ; j++){
					out.println("<td>"+r[j]+"</td>");
				}
				out.println("</tr>");
			}
		%>
		</table>
		<form name="f2">
		<input name="newChannel">
		<input type="submit" value="添加新渠道">
		</form>
	</body>
</html>
