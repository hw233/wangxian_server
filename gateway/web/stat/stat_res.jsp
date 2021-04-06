<%@ page contentType="text/html;charset=utf-8"%>
<%@ page
	import="java.util.*,com.xuanzhi.tools.text.*,com.xuanzhi.tools.mem.*,com.fy.gamegateway.stat.*,
	com.fy.gamegateway.mieshi.resource.manager.*,
	com.fy.gamegateway.mieshi.server.*,java.sql.*,com.xuanzhi.tools.simplejpa.*,com.xuanzhi.tools.simplejpa.impl.* "%><%!
	
	public String getSql(String startDay,String endDay,String channel,String platform,String column){
		String sql = "select "+column+",count(1) from client where " + 
		"timeoflastconnected >= to_date('"+startDay+"','yyyy-mm-dd') and timeoflastconnected < to_date('"+endDay+"','yyyy-mm-dd') "
		+ (channel == null?"":(" and channel='"+channel+"'"))
		+ (platform == null?"":(" and platform='"+platform+"'"))
		+" group by "+column+"";
		return sql;
	}
	
	public String getSql2(String startDay,String endDay,String channel,String platform,String column){
		String sql = "select "+column+",count(1) from client where " + 
		"timeoffirstconnected >= to_date('"+startDay+"','yyyy-mm-dd') and timeoffirstconnected < to_date('"+endDay+"','yyyy-mm-dd') "
		+ (channel == null?"":(" and channel='"+channel+"'"))
		+ (platform == null?"":(" and platform='"+platform+"'"))
		+" group by "+column+"";
		return sql;
	}
	
	public String getSqlForMysql(String startDay,String endDay,String channel,String platform,String column){
		String sql = "select "+column+",count(1) from CLIENT_S1 where " + 
		"timeoflastconnected >= str_to_date('"+startDay+"','%Y-%m-%d') and timeoflastconnected < str_to_date('"+endDay+"','%Y-%m-%d') "
		+ (channel == null?"":(" and channel='"+channel+"'"))
		+ (platform == null?"":(" and platform='"+platform+"'"))
		+" group by "+column+"";
		return sql;
	}
	
	public String getSql2ForMysql(String startDay,String endDay,String channel,String platform,String column){
		String sql = "select "+column+",count(1) from CLIENT_S1 where " + 
		"timeoffirstconnected >= str_to_date('"+startDay+"','%Y-%m-%d') and timeoffirstconnected < str_to_date('"+endDay+"','%Y-%m-%d') "
		+ (channel == null?"":(" and channel='"+channel+"'"))
		+ (platform == null?"":(" and platform='"+platform+"'"))
		+" group by "+column+"";
		return sql;
	}
	
	%>
<%
	PackageManager pm = PackageManager.getInstance();
	ResourceManager rm = ResourceManager.getInstance();
	PackageManager.Version vs[] = pm.getRealVersions();
	ArrayList<String> channels = new ArrayList<String> ();
	channels.add("全部");
	for(int i = 0 ; i < vs.length ; i++){
		for(PackageManager.Package p : vs[i].packageList){
			if(channels.contains(p.channel) == false){
				channels.add(p.channel);
			}
		}
	}
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
	if(endDay == null) endDay = DateUtil.formatDate(new java.util.Date(System.currentTimeMillis()+24*3600000),"yyyy-MM-dd");

		
	String columns[] = new String[]{"khdgxzt","zykbzt"
			,"zybxzzt","zybjyzt","ZGXZZYZT"};
	
	String queryNames[] = new String[]{"客户端更新状态","资源拷贝状态"
			,"资源zip包下载状态","资源zip包解压状态","逐个资源下载状态"};
	
	LinkedHashMap<String,LinkedHashMap<String,Integer>> mm = new LinkedHashMap<String,LinkedHashMap<String,Integer>>();
	
	for(int i = 0 ; i < columns.length ; i++){
		String sql = getSql(startDay,endDay,channel,platform,columns[i]);
		
		
		LinkedHashMap<String,Integer> map = new LinkedHashMap<String,Integer>();
		Connection conn = null;
		try{
			String dbType = SimpleEntityManagerFactory.getDbType();

			if(dbType.equals("oracle")){
				SimpleEntityManagerOracleImpl<Client> em =  (SimpleEntityManagerOracleImpl<Client>)stat.em4Client;
				conn = em.getConnection();
			}else{
				SimpleEntityManagerMysqlImpl<Client> em =  (SimpleEntityManagerMysqlImpl<Client>)stat.em4Client;
				conn = em.getConnection();
				sql = getSqlForMysql(startDay,endDay,channel,platform,columns[i]);
			}
			Statement stmt = conn.createStatement();
			ResultSet rs = stmt.executeQuery(sql);
			while(rs.next()){
				String key = rs.getString(1);
				if(key == null) key = "null";
				int count = rs.getInt(2);
				map.put(key,count);
			}
			
			mm.put(columns[i],map);
			
		}finally{
			conn.close();
		}
	}
	
	
	LinkedHashMap<String,LinkedHashMap<String,Integer>> mm2 = new LinkedHashMap<String,LinkedHashMap<String,Integer>>();
	
	for(int i = 0 ; i < columns.length ; i++){
		String sql = getSql2(startDay,endDay,channel,platform,columns[i]);
		
		LinkedHashMap<String,Integer> map = new LinkedHashMap<String,Integer>();
		Connection conn = null;
		try{
			String dbType = SimpleEntityManagerFactory.getDbType();

			if(dbType.equals("oracle")){
				SimpleEntityManagerOracleImpl<Client> em =  (SimpleEntityManagerOracleImpl<Client>)stat.em4Client;
				conn = em.getConnection();
			}else{
				SimpleEntityManagerMysqlImpl<Client> em =  (SimpleEntityManagerMysqlImpl<Client>)stat.em4Client;
				conn = em.getConnection();
				sql = getSql2ForMysql(startDay,endDay,channel,platform,columns[i]);
			}
			Statement stmt = conn.createStatement();
			ResultSet rs = stmt.executeQuery(sql);
			while(rs.next()){
				String key = rs.getString(1);
				if(key == null) key = "null";
				int count = rs.getInt(2);
				map.put(key,count);
			}
			
			mm2.put(columns[i],map);
			
		}finally{
			conn.close();
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
		<input type='submit' value='提交'> </form>
		<br/>

<h2 style="font-weight:bold;">新增客户端查询结果：</h2>
<%
	for(int i = 0 ; i < columns.length ; i++){
		LinkedHashMap<String,Integer> map = mm2.get(columns[i]);
		out.println("<table id='test2' align='center' width='30%' cellpadding='1' bgcolor='#000000' cellspacing='1' border='0'>");
		out.println("<tr bgcolor='#EEEEEE'><td width='50%'>"+queryNames[i]+"</td><td>客户端数量</td></tr>");
		String keys[] = map.keySet().toArray(new String[0]);
		for(int j = 0 ; j < keys.length ; j++){
			Integer value = map.get(keys[j]);
			out.println("<tr bgcolor='#FFFFFF'><td>"+keys[j]+"</td><td>"+value+"</td></tr>");
		}
		out.println("</table><br/>");
	}
%>

<h2 style="font-weight:bold;">连接客户端查询结果：</h2>
<%
	for(int i = 0 ; i < columns.length ; i++){
		LinkedHashMap<String,Integer> map = mm.get(columns[i]);
		out.println("<table id='test2' align='center' width='30%' cellpadding='1' bgcolor='#000000' cellspacing='1' border='0'>");
		out.println("<tr bgcolor='#EEEEEE'><td width='50%'>"+queryNames[i]+"</td><td>客户端数量</td></tr>");
		String keys[] = map.keySet().toArray(new String[0]);
		for(int j = 0 ; j < keys.length ; j++){
			Integer value = map.get(keys[j]);
			out.println("<tr bgcolor='#FFFFFF'><td>"+keys[j]+"</td><td>"+value+"</td></tr>");
		}
		out.println("</table><br/>");
	}
%>
	</body>
</html>
