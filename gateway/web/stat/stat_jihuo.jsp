<%@ page contentType="text/html;charset=utf-8"%>
<%@ page
	import="java.util.*,com.xuanzhi.tools.text.*,com.xuanzhi.tools.mem.*,com.fy.gamegateway.stat.*,
	com.fy.gamegateway.mieshi.resource.manager.*,
	com.fy.gamegateway.mieshi.server.*,java.sql.*,com.xuanzhi.tools.simplejpa.*,com.xuanzhi.tools.simplejpa.impl.* "%><%!
	//%Y-%m-%d %H:%i:%S
	public String getSql(String startDay,String endDay,String channel,String platform,boolean regFlag){
		String sql = "select aaaaa,count(*) from " + 
		"(select to_char(timeoffirstconnected,'yyyy-mm-dd') as aaaaa from client where "+
		"timeoffirstconnected >= to_date('"+startDay+"','yyyy-mm-dd') and timeoffirstconnected < to_date('"+endDay+"','yyyy-mm-dd') "
		+ (channel == null?"":(" and channel='"+channel+"'"))
		+ (platform == null?"":(" and platform='"+platform+"'"))
		+ (regFlag?" and (hasLogin='T' or hasRegister='T')":"")
		+") group by aaaaa ";
		return sql;
	}
	
	public String getChannelsSql(String startDay,String endDay){
		String sql = "" + 
		"select channel,count(*) as aaaaaa from client where "+
		"timeoffirstconnected >= to_date('"+startDay+"','yyyy-mm-dd') and timeoffirstconnected < to_date('"+endDay+"','yyyy-mm-dd') "
		+" group by channel order by aaaaaa desc";
		return sql;
	}
	
	public String getChannelsSqlForMysql(String startDay,String endDay){
		String sql = "" + 
		"select channel,count(*) as aaaaaa from CLIENT_S1 where "+
		"timeoffirstconnected >= str_to_date('"+startDay+"','%Y-%m-%d') and timeoffirstconnected < str_to_date('"+endDay+"','%Y-%m-%d') "
		+" group by channel order by aaaaaa desc";
		return sql;
	}
	//%Y-%m-%d %H:%i:%S
	public String getSqlForMysql(String startDay,String endDay,String channel,String platform,boolean regFlag){
		String sql = "select aaaaa,count(*) from " + 
		"(select date_to_str(timeoffirstconnected,'%Y-%m-%d') from CLIENT_S1 where "+
		"timeoffirstconnected >= str_to_date('"+startDay+"','%Y-%m-%d') and timeoffirstconnected < str_to_date('"+endDay+"','%Y-%m-%d') "
		+ (channel == null?"":(" and channel='"+channel+"'"))
		+ (platform == null?"":(" and platform='"+platform+"'"))
		+ (regFlag?" and (hasLogin='T' or hasRegister='T')":"")
		+") group by aaaaa ";
		return sql;
	}
	%>
<%
boolean regFlag = false;

String startDay = request.getParameter("startday");
String endDay = request.getParameter("endday");
if(startDay == null) startDay = DateUtil.formatDate(new java.util.Date(System.currentTimeMillis()-30*24*3600000L),"yyyy-MM-dd");
if(endDay == null) endDay = DateUtil.formatDate(new java.util.Date(System.currentTimeMillis()),"yyyy-MM-dd");

	ArrayList<String> dayList = new ArrayList<String>();
	
	String dd = startDay;
	while(dayList.size() < 100){
		if(dd.equals(endDay)) break;;
		dayList.add(dd);
		long ll = DateUtil.parseDate(dd,"yyyy-MM-dd").getTime() + 25*3600000;
		dd = DateUtil.formatDate(new java.util.Date(ll),"yyyy-MM-dd");
	}
	
	ArrayList<String> channels = new ArrayList<String> ();


	StatManager stat = StatManager.getInstance();

	Connection conn = null;
	String sql = null;
	String dbType = SimpleEntityManagerFactory.getDbType();

	if(dbType.equals("oracle")){
		SimpleEntityManagerOracleImpl<Client> em =  (SimpleEntityManagerOracleImpl<Client>)stat.em4Client;
		conn = em.getConnection();
		sql = getChannelsSql(startDay,endDay);
	}else{
		SimpleEntityManagerMysqlImpl<Client> em =  (SimpleEntityManagerMysqlImpl<Client>)stat.em4Client;
		conn = em.getConnection();
		sql = getChannelsSqlForMysql(startDay,endDay);
	}
	Statement stmt = conn.createStatement();
	ResultSet rs = stmt.executeQuery(sql);
	while(rs.next()){
		String c = rs.getString(1);
		channels.add(c);
	}
	rs.close();

	
	String platforms[] = new String[]{"全部","IOS","Android"};
	String platform = null;

	String ss = request.getParameter("platform");
	if(ss != null){
		int k = Integer.parseInt(ss);
		if(k > 0 && k < platforms.length) {
			platform = platforms[k];
		}
	}


	String regFlags[] = new String[]{"激活","注册"};

	ss = request.getParameter("regFlag");
	if(ss != null){
		int k = Integer.parseInt(ss);
		if(k == 1) regFlag = true;
		else regFlag = false;
	}
	
	String selectedChannels[] = request.getParameterValues("channel");
	if(selectedChannels == null){
		if(channels.size() > 5){
			selectedChannels = new String[5];
		}else{
			selectedChannels = new String[channels.size()];
		}
		for(int i = 0 ; i < selectedChannels.length ; i++){
			selectedChannels[i] = channels.get(i);
		}
	}
	
	HashMap<String,HashMap<String,Integer>> map = new HashMap<String,HashMap<String,Integer>>();
	for(int i = 0 ; i < selectedChannels.length ; i++){
		sql = null;
		if(dbType.equals("oracle")){
			sql = getSql(startDay,endDay,selectedChannels[i],platform,regFlag);
		}else{
			sql = getSqlForMysql(startDay,endDay,selectedChannels[i],platform,regFlag);
		}
		HashMap<String,Integer> m = new HashMap<String,Integer>();
		rs = stmt.executeQuery(sql);
		while(rs.next()){
			String day = rs.getString(1);
			int num = rs.getInt(2);
			m.put(day,num);
		}
		rs.close();
		
		map.put(selectedChannels[i],m);
	}
	conn.close();
	
	
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
<script language="JavaScript">
<!--
function drawFlotr1() {
	var f = Flotr.draw(
		$('container1'), [
		<%
		for(int i = 0 ; i < selectedChannels.length ; i++){
			HashMap<String,Integer> m = map.get(selectedChannels[i]);
			if(m == null) m = new HashMap<String,Integer>();
			StringBuffer sb = new StringBuffer();
			sb.append("[");
			for(int k = 0 ; k < dayList.size() ; k++){
				String day = dayList.get(k);
				Integer num = m.get(day);
				if(num == null) num = 0;
				sb.append("["+(k)+","+num+"]");
				if(k < dayList.size() - 1 ){
					sb.append(",");
				}
			}
			sb.append("]");
			%>{
		    data: <%=sb%>,
		    lines: {show: false, fill: false},
		    label: '<%=selectedChannels[i]%>',
			},<%
		}
		%>
		],{
			xaxis:{
				noTicks: <%=dayList.size() %>,	// Display 7 ticks.	
				tickFormatter: function(n){ <%
					for(int i = 0 ; i < dayList.size() ;i++){
						out.println(" if(n=="+i+") return '"+dayList.get(i)+"';");
					}
					out.println("return n;");
				%>}, // =>
				min: 0,
				max: <%= dayList.size()  %>
			},
			yaxis:{
				min:0
			}
			,legend:{
				position: 'nw', // => position the legend 'south-east'.
				backgroundColor: '#D2E8FF' // => a light blue background color.
			},
			mouse:{
				track: true,
				color: 'purple',
				sensibility: 1, // => distance to show point get's smaller
				trackDecimals: 2,
				trackFormatter: function(obj){ return 'y = ' + obj.y; }
			}
		}
	);
}
-->
</script>
</head>

	<body>
		
		<h2 style="font-weight:bold;">
			各个渠道激活量的历史曲线图
		</h2>
		<form>
		输入开始日期：<input type='text' name='startday' value='<%=startDay %>'>
		结束日期：<input type='text' name='endday' value='<%=endDay %>'>(格式：2011-01-01)<br/>
		请选择渠道：
		<table border="0" cellpadding="0" cellspacing="1" width="100%" bgcolor="#000000" align="center">
			<%
				out.println("<tr bgcolor='#FFFFFF' align='center'>");
				for(int i = 0 ; i < channels.size() ; i++){
					if(i>0 && i % 8 == 0){
						out.println("</tr>");
						out.println("<tr bgcolor='#FFFFFF' align='center'>");
					}
					boolean selected =false;
					for(int j = 0 ; j < selectedChannels.length ; j++){
						if(selectedChannels[j].equals(channels.get(i))){
							selected = true;
						}
					}
					out.println("<td><input type='checkbox' name='channel' value='"+channels.get(i)+"' "+(selected?"checked":"")+" >"+channels.get(i)+"</td>");
				}
				out.println("</tr>");
			%>
		</table>
		<br/>&nbsp;&nbsp;请选择平台：<select name='platform'>
		<%
			for(int i = 0 ; i < platforms.length ; i++){
				if(platforms[i].equals(platform)){
					out.println("<option value='"+i+"' selected >"+platforms[i]+"</option>");
				}else{
					out.println("<option value='"+i+"' >"+platforms[i]+"</option>");
				}
			}
		%>
		</select> 请查询项：<select name='regFlag'>
		<%
			for(int i = 0 ; i < regFlags.length ; i++){
				if((regFlag && i == 1) || (regFlag == false && i==0)){
					out.println("<option value='"+i+"' selected >"+regFlags[i]+"</option>");
				}else{
					out.println("<option value='"+i+"' >"+regFlags[i]+"</option>");
				}
			}
		%>
		</select> 
		<input type='submit' value='提交'> </form>
		<br/>
		<center>
			<div id="container1" style="width:100%;height:400px;display:block;"></div>
			<script>
				drawFlotr1();
			</script>
			<br><br>
		</center>

	
	</body>
</html>
