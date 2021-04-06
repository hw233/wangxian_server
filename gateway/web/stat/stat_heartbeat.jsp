<%@ page contentType="text/html;charset=utf-8"%>
<%@ page
	import="java.util.*,com.xuanzhi.tools.text.*,com.xuanzhi.tools.mem.*,com.fy.gamegateway.stat.*,
	com.fy.gamegateway.mieshi.resource.manager.*,
	com.fy.gamegateway.mieshi.server.*,java.sql.*,com.xuanzhi.tools.simplejpa.*,com.xuanzhi.tools.simplejpa.impl.* "%>

<%

MieshiServerInfoManager mm = MieshiServerInfoManager.getInstance();
MieshiServerInfo sis[] = mm.getServerInfoSortedList();
String serverNames[] = request.getParameterValues("realName");
if(serverNames == null || serverNames.length == 0){
	serverNames = new String[sis.length];
	for(int i = 0 ; i < sis.length ; i++){
		serverNames[i] = sis[i].getRealname();
	}
}

String platforms[] = new String[]{"1秒以上","5秒以上","10秒以上","30秒以上"};
String platform = null;
int delayType = 0;
String ss = request.getParameter("platform");
if(ss != null){
	int k = Integer.parseInt(ss);
	if(k > 0 && k < platforms.length) {
		platform = platforms[k];
	}
	delayType = k;
}

String startDay = request.getParameter("startday");
String endDay = request.getParameter("endday");
if(startDay == null) startDay = DateUtil.formatDate(new java.util.Date(System.currentTimeMillis()-30*24*3600000L),"yyyy-MM-dd");
if(endDay == null) endDay = DateUtil.formatDate(new java.util.Date(System.currentTimeMillis()+24*3600000L),"yyyy-MM-dd");

	ArrayList<String> dayList = new ArrayList<String>();
	
	String dd = startDay;
	while(dayList.size() < 100){
		if(dd.equals(endDay)) break;;
		dayList.add(dd);
		long ll = DateUtil.parseDate(dd,"yyyy-MM-dd").getTime() + 25*3600000;
		dd = DateUtil.formatDate(new java.util.Date(ll),"yyyy-MM-dd");
	}
	
	MieshiServerHeartBeatInfoManager hbim = MieshiServerHeartBeatInfoManager.getInstance();

	final HashMap<String,int[]> map = new HashMap<String,int[]>();
	for(int i = 0 ; i < serverNames.length ; i++){
		int data[] = new int[dayList.size()];
		for(int j = delayType ; j < 4 ;j++){
			int d[] = hbim.getDelayTimesByDayAndServer(serverNames[i],j,dayList);
			for(int k = 0 ; k < d.length ; k++){
				data[k]+=d[k];
			}
		} 
		map.put(serverNames[i],data);
	}
	
	Arrays.sort(serverNames,new Comparator<String>(){
		public int compare(String s1,String s2){
			int d1[] = map.get(s1);
			int d2[] = map.get(s2);
			if(d1[d1.length-1] < d2[d2.length-1]){
				return 1;
			}
			if(d1[d1.length-1] > d2[d2.length-1]){
				return -1;
			}
			return 0;
		}
	} );
	
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
		for(int i = 0 ; i < serverNames.length ; i++){
			int data[] = map.get(serverNames[i]);
			if(data == null) data = new int[dayList.size()];
			StringBuffer sb = new StringBuffer();
			sb.append("[");
			for(int k = 0 ; k < dayList.size() ; k++){
				String day = dayList.get(k);
				Integer num = data[k];
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
		    label: '<%= serverNames[i]+"("+data[data.length-1]+")" %>',
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
			各个服务器心跳超时的历史曲线图
		</h2>
		<form>
		输入开始日期：<input type='text' name='startday' value='<%=startDay %>'>
		结束日期：<input type='text' name='endday' value='<%=endDay %>'>(格式：2011-01-01)<br/>
		请选择渠道：
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
					out.println("<td><input type='checkbox' name='realName' value='"+sis[i].getRealname()+"' "+(selected?"checked":"")+" >"+sis[i].getName()+"</td>");
				}
				out.println("</tr>");
			%>
			<tr bgcolor='#FFFFFF' align='center'><td  colspan=4><br/>&nbsp;&nbsp;请选择类型：<select name='platform'>
		<%
			for(int i = 0 ; i < platforms.length ; i++){
				if(platforms[i].equals(platform)){
					out.println("<option value='"+i+"' selected >"+platforms[i]+"</option>");
				}else{
					out.println("<option value='"+i+"' >"+platforms[i]+"</option>");
				}
			}
		%>
		</select> <input type='submit' value='提 交'></td></tr>
			</table>
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
