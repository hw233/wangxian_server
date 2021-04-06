<%@page import="java.text.DecimalFormat"%>
<%@ page contentType="text/html;charset=utf-8"%>
<%@ page
	import="java.util.*,com.xuanzhi.tools.text.*,com.xuanzhi.tools.mem.*,com.fy.gamegateway.stat.*,
	com.fy.gamegateway.mieshi.resource.manager.*,
	com.fy.gamegateway.mieshi.server.*,java.sql.*,com.xuanzhi.tools.simplejpa.*,com.xuanzhi.tools.simplejpa.impl.* "%><%!
	public static ArrayList<String> ADD_CHANNELS = new ArrayList<String>();
	//%Y-%m-%d %H:%i:%S
	
	/*
	</td><td>当日全部客户端数</td><td>当日客户端首次运行数</td><td>当日客户端开始下载数</td><td>当日客户端下载完成数</td><td>当日客户端中注册数</td>
			<td>当日客户端进入游戏服数</td><td>当日客户端创建角色数</td><</tr>
	*/
	
	public String getSql(String startDay,String endDay,String channel,String platform){
		String sql = "select " + 
		"(select count(*) from client2 where "+
		"timeoffirstconnected >= to_date('"+startDay+"','yyyy-mm-dd') and timeoffirstconnected < to_date('"+endDay+"','yyyy-mm-dd') "
		+""
		+ (channel == null?"":(" and channel='"+channel+"'"))
		+ (platform == null?"":(" and platform='"+platform+"'"))
		+") as sumNum, "+
		"(select count(*) from client2 where "+ 
		"timeoffirstconnected >= to_date('"+startDay+"','yyyy-mm-dd') and timeoffirstconnected < to_date('"+endDay+"','yyyy-mm-dd') "+
		"and clientstate >= 0 "
		+ (channel == null?"":(" and channel='"+channel+"'"))
		+ (platform == null?"":(" and platform='"+platform+"'"))
		+") as firstRunNum," +
		"(select count(*) from client2 where "+
		"timeoffirstconnected >= to_date('"+startDay+"','yyyy-mm-dd') and timeoffirstconnected < to_date('"+endDay+"','yyyy-mm-dd') "+
		" and  clientstate >= 2 "
		+ (channel == null?"":(" and channel='"+channel+"'"))
		+ (platform == null?"":(" and platform='"+platform+"'"))
		+") as startDownloadNum,  "+
		
		"(select count(*) from client2 where "+ 
		"timeoffirstconnected >= to_date('"+startDay+"','yyyy-mm-dd') and timeoffirstconnected < to_date('"+endDay+"','yyyy-mm-dd') "+
		"	and  clientstate >= 3  "
		+ (channel == null?"":(" and channel='"+channel+"'"))
		+ (platform == null?"":(" and platform='"+platform+"'"))
		+") as finishDownloadNum, "+
				
		"(select count(*) from client2 where "+
		"timeoffirstconnected >= to_date('"+startDay+"','yyyy-mm-dd') and timeoffirstconnected < to_date('"+endDay+"','yyyy-mm-dd') "+
		" and   clientstate >= 4 "
		+ (channel == null?"":(" and channel='"+channel+"'"))
		+ (platform == null?"":(" and platform='"+platform+"'"))
		+") as regNum, "+
		"(select count(*) from client2 where "+
		"timeoffirstconnected >= to_date('"+startDay+"','yyyy-mm-dd') and timeoffirstconnected < to_date('"+endDay+"','yyyy-mm-dd') "+
		" and clientstate >= 5 "
		+ (channel == null?"":(" and channel='"+channel+"'"))
		+ (platform == null?"":(" and platform='"+platform+"'"))
		+") as loginNum , "+
		"(select count(*) from client2 where "+
		"timeoffirstconnected >= to_date('"+startDay+"','yyyy-mm-dd') and timeoffirstconnected < to_date('"+endDay+"','yyyy-mm-dd') "+
		"  and clientstate >= 6 "
		+ (channel == null?"":(" and channel='"+channel+"'"))
		+ (platform == null?"":(" and platform='"+platform+"'"))
		+") as enterServerNum, "+
		"(select count(*) from client2 where "+
		"timeoffirstconnected >= to_date('"+startDay+"','yyyy-mm-dd') and timeoffirstconnected < to_date('"+endDay+"','yyyy-mm-dd') "+
		"  and clientstate >= 7 "
		+ (channel == null?"":(" and channel='"+channel+"'"))
		+ (platform == null?"":(" and platform='"+platform+"'"))
		+") as createPlayerNum  "+
				
		"from dual";
		return sql;
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
				
				String sql = getSql(day,nextDay,channel,platform);
				
				
				Connection conn = null;
				try{
					String dbType = SimpleEntityManagerFactory.getDbType();

					if(dbType.equals("oracle")){
						SimpleEntityManagerOracleImpl<Client2> em =  (SimpleEntityManagerOracleImpl<Client2>)stat.em4Client;
						conn = em.getConnection();
					}else{
					/* 	SimpleEntityManagerMysqlImpl<Client> em =  (SimpleEntityManagerMysqlImpl<Client>)stat.em4Client;
						conn = em.getConnection();
						sql = getSqlForMysql(day,nextDay,channel,platform,SPV,SRV); */
					}
					Statement stmt = conn.createStatement();
					ResultSet rs = stmt.executeQuery(sql);
					int r[] = new int[8];
					if(rs.next()){
						r[0] = rs.getInt(1);
						r[1] = rs.getInt(2);
						r[2] = rs.getInt(3);
						r[3] = rs.getInt(4);
						r[4] = rs.getInt(5);
						r[5] = rs.getInt(6);
						r[6] = rs.getInt(7);
						r[7] = rs.getInt(8);
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
	}else{}
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
	<!-- 	<input type='checkbox' name='channel_expand' value='true' <%= channel_expand?"checked":""%> >展开所有渠道 -->
		<input type='submit' value='提交'> </form>
		<br/>

		<table id='test2' align='center' width='100%' cellpadding='1' bgcolor='#000000' cellspacing='1' border='0'>
		<tr bgcolor='#EEEEBB'>
			<td><%= channel_expand?"渠道":"日期" %></td><td>当日全部客户端数</td><td>当日客户端首次运行数</td><td>当日客户端开始下载数</td><td>当日客户端下载完成数</td><td>安装完成转化率</td><td>当日客户端中注册数</td>
			<td>安装注册转化率</td><td>当日客户端登陆数</td>
			<td>当日客户端进入游戏服数</td><td>安装进入转化率</td><td>当日客户端创建角色数</td><</tr>
		<%
			String keys[] = result.keySet().toArray(new String[0]);	
			for(int i = 0 ; i < keys.length ; i++){
				int[] r = result.get(keys[i]);
				out.println("<tr bgcolor='#FFFFFF'>");
				out.println("<td>"+keys[i]+"</td>");
				Map needCalColumnMap = new HashMap();
				needCalColumnMap.put(4, "install");
				needCalColumnMap.put(6, "reg");
				needCalColumnMap.put(9, "enter");
				
				for(int j = 0,rj=0 ; j < r.length+3 ; j++,rj++){
					if(needCalColumnMap.containsKey(j))
					{
						rj--;
						DecimalFormat df = new DecimalFormat("#.00");
						String v = (String)needCalColumnMap.get(j);
						if(v.contains("install"))
						{
							out.println("<td><font color=\"red\">" +( df.format((r[rj]/(1.0d*r[0]))*100))+"%</font></td>");
						}
						
						if(v.contains("reg"))
						{
							out.println("<td><font color=\"red\">" +( df.format((r[rj]/(1.0d*r[0]))*100))+"%</font></td>");
						}
						
						if(v.contains("enter"))
						{
							out.println("<td><font color=\"red\">" +( df.format((r[rj]/(1.0d*r[0]))*100))+"%</font></td>");
						}
						
						
					}
					else
					{
						out.println("<td>"+r[rj]+"</td>");
					}
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
