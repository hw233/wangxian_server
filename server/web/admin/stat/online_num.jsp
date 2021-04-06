<%@ page contentType="text/html;charset=utf-8"%>
<%@ page
	import="com.xuanzhi.tools.text.*,java.util.*,org.slf4j.*,com.fy.engineserver.sprite.*"%>
<%
String startday = request.getParameter("startday");
String endday = request.getParameter("endday");
String width = request.getParameter("width");
if(width == null) width= "1000";
if(startday == null) startday = DateUtil.formatDate(new Date(),"yyyy-MM-dd");
if(endday == null) endday = DateUtil.formatDate(new Date(),"yyyy-MM-dd");
%>
<%
		PlayerManager pm = PlayerManager.getInstance();
		Date s = DateUtil.parseDate(startday,"yyyy-MM-dd");
		Date e = DateUtil.parseDate(endday,"yyyy-MM-dd");
		
		StringBuffer sb = new StringBuffer();
		sb.append("[");
		int index = 0;
		while(s.getTime() < e.getTime() + 3600000){
			String day = DateUtil.formatDate(s,"yyyy-MM-dd");
			int nums[] = pm.getOnlineNumAsEveryMinute(day);
			
			for(int i = 0 ; i < nums.length ; i++){
				sb.append("["+(i + index*24*60)+","+nums[i]+"]");
				if(i < nums.length - 1 || !day.equals(endday)){
					sb.append(",");
				}
						
			}
			index++;
			s.setTime(s.getTime() + 24L * 3600 * 1000);
		}
		
		sb.append("]");
		
		int hours = 24;
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
		<script language="javascript" type="text/javascript" src="../js/flotr/lib/prototype-1.6.0.2.js"></script>
		<script language="javascript" type="text/javascript" src="../js/flotr/flotr-0.2.0-alpha.js"></script>

<script language="JavaScript">
<!--
function drawFlotr1() {
	var f = Flotr.draw(
		$('container1'), [
		{
		    data: <%=sb%>,
		    lines: {show: false, fill: true},
		}],{
			xaxis:{
				noTicks: <%=hours%>,	// Display 7 ticks.	
				tickFormatter: function(n){    var a=Math.floor(n/(60*24)); var b=n-a*60*24; var c=Math.floor(b/60); var d = b - c*60;     if(d<10){return c+':0'+d;}else{return c+':'+d;}}, // =>
				min: 0,
				max: <%= index*24*60 %>
			}
		}
	);
}
-->
</script>

	</head>

	<body>
		
		<h2 style="font-weight:bold;">
			<%=startday %> ~ <%= endday %> 在线人数图
		</h2>
		<form>输入开始日期：<input type='text' name='startday' value='<%=startday %>'>结束日期<input type='text' name='endday' value='<%=endday %>'>(格式：2011-01-01)  宽度：<input type='text' name='width' value='<%=width %>'><input type='submit' value='提交'> </form>
		<br/>

		
		<center>
			<div id="container1" style="width:<%=width %>px;height:400px;display:block;"></div>
			<script>
				drawFlotr1();
			</script>
			
		<center>
		<br>
		<br>
	</body>
</html>
