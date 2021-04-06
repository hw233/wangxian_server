<%@ page contentType="text/html;charset=utf-8"%>
<%@ page
	import="java.util.*,com.xuanzhi.tools.text.*,com.xuanzhi.tools.mem.*,com.fy.gamegateway.mieshi.server.* "%>
<%

	MieshiServerInfoManager mm = MieshiServerInfoManager.getInstance();
	

	MieshiServerInfo sis[] = mm.getServerInfoSortedList();
	int totalOnlineNums[] = new int[24*60];
	for(int i = 0 ; i < sis.length ; i++){
		int aa[] = sis[i].getOnlineNumInEveryMinute();
		for(int j = 0 ; j < aa.length ; j++){
			totalOnlineNums[j]+=aa[j];
		}
	}
	StringBuffer sb0 = new StringBuffer();
	sb0.append("[");
	long now = System.currentTimeMillis();
	Calendar cal = Calendar.getInstance();
	cal.setTimeInMillis(now);
	int nowIndex = cal.get(Calendar.HOUR_OF_DAY)*60+cal.get(Calendar.MINUTE)-1;
	if(nowIndex<0)nowIndex=0;

	for(int i = 0 ; i < totalOnlineNums.length ; i++){
			
		sb0.append("["+(i)+","+totalOnlineNums[i]+"]");
		if(i < totalOnlineNums.length - 1 ){
			sb0.append(",");
		}
	}
	sb0.append("]");
	
	
		
	String serverNames[] = request.getParameterValues("realName");
	if(serverNames == null || serverNames.length == 0){
		serverNames = new String[sis.length];
		for(int i = 0 ; i < sis.length ; i++){
			serverNames[i] = sis[i].getRealname();
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

<script language="JavaScript">
<!--
function drawFlotr1() {
	var f = Flotr.draw(
		$('container1'), [
		{
		    data: <%=sb0%>,
		    lines: {show: false, fill: false},
		    label: '所有服总在线',
		},
		],{
			xaxis:{
				noTicks: <%=24%>,	// Display 7 ticks.	
				tickFormatter: function(n){ var c=Math.floor(n/60); var d = n - c*60;     if(d<10){return c+':0'+d;}else{return c+':'+d;}}, // =>
				min: 0,
				max: <%= 24*60  %>
			},legend:{
				position: 'ne', // => position the legend 'south-east'.
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


function drawFlotr2() {
	var f = Flotr.draw(
		$('container2'), [
<%
	for(int i = 0 ; i < serverNames.length ; i++){
		MieshiServerInfo si = mm.getServerInfoByName(serverNames[i]);
		if(si == null) continue;
		int onlineNums[] = si.getOnlineNumInEveryMinute();
		StringBuffer sb = new StringBuffer();
		sb.append("[");
		for(int k = 0 ; k < onlineNums.length ; k++){
				
			sb.append("["+(k)+","+onlineNums[k]+"]");
			if(k < onlineNums.length - 1 ){
				sb.append(",");
			}
		}
		sb.append("]");
		
		out.println("{");
		out.println("data: "+sb+",");
		out.println("lines: {show: false, fill: false},");
		out.println("label: '"+si.getName()+"',");
		out.println("},");
	}
%>
		],{
			xaxis:{
				noTicks: <%=24%>,	// Display 7 ticks.	
				tickFormatter: function(n){ var c=Math.floor(n/60); var d = n - c*60;     if(d<10){return c+':0'+d;}else{return c+':'+d;}}, // =>
				min: 0,
				max: <%= 24*60  %>
			},legend:{
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
			服务器在线情况
		</h2>
		
		<center>
			所有服务器在线(<%= totalOnlineNums[nowIndex] %>)
			<div id="container1" style="width:100%;height:400px;display:block;"></div>
			<script>
				drawFlotr1();
			</script>
			<br><br>
			分服务器在线情况
			<form><input type='hidden' name='action'>
			<table border="0" cellpadding="0" cellspacing="1" width="100%" bgcolor="#000000" align="center">
			<%
				out.println("<tr bgcolor='#FFFFFF' align='center'>");
				for(int i = 0 ; i < sis.length ; i++){
					if(i>0 && i % 4 == 0){
						out.println("</tr>");
						out.println("<tr bgcolor='#FFFFFF' align='center'>");
					}
					boolean selected = false;
					for(int j = 0 ; j < serverNames.length ; j++){
						if(sis[i].getRealname().equals(serverNames[j])){
							selected = true;
						}
					}
					out.println("<td><input type='checkbox' name='realName' value='"+sis[i].getRealname()+"' "+(selected?"checked":"")+" >"+sis[i].getName()+"("+sis[i].getOnlinePlayerNum()+")</td>");
				}
				out.println("</tr>");
			%>
			<tr bgcolor='#FFFFFF' align='center'><td  colspan=4><input type='submit' value='提 交'></td></tr>
			</table>
			<div id="container2" style="width:100%;height:800px;display:block;"></div>
			<script>
				drawFlotr2();
			</script>
		<center>
		
	</body>
</html>
