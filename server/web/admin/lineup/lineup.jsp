<%@ page contentType="text/html;charset=utf-8"%>
<%@ page import="java.util.*,com.fy.engineserver.lineup.*"%>
<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN">
<html>
	<head>
	<title></title>
	<meta http-equiv="pragma" content="no-cache">
	<meta http-equiv="cache-control" content="no-cache">
	<meta http-equiv="expires" content="0">
	<link rel="stylesheet" href="css/style.css" />
	<link rel="stylesheet" href="css/atalk.css" />
	<script type="text/javascript">
	</script>
	</head>

	<body>
		<%     
		EnterServerAgent agent = EnterServerAgent.getInstance();       
		String clear = request.getParameter("clear");  
		if(clear != null && clear.equals("true")) {
			agent.clearLineup();
		}
		String maxOnlineStr = request.getParameter("maxOnline");        
		String xinshoucunOnlineStr = request.getParameter("xinshoucunOnline");
		if(maxOnlineStr != null) {
			agent.setMaxOnline(Integer.valueOf(maxOnlineStr));
			agent.setXinShouCunOnline(Integer.valueOf(xinshoucunOnlineStr));
		}
		String enterPeriodStr = request.getParameter("enterPeriod");
		String newNumCutPeriodStr = request.getParameter("newNumCutPeriod");
		String totalCutPeriodStr = request.getParameter("totalCutPeriod");
		if(enterPeriodStr != null) {
			if(EnterServerTester.tester == null || !EnterServerTester.tester.isRunning()) {
				EnterServerTester.tester = new EnterServerTester(Integer.valueOf(enterPeriodStr), Integer.valueOf(newNumCutPeriodStr), Integer.valueOf(totalCutPeriodStr));
				EnterServerTester.tester.setAgent(agent);
				//agent.setTester(EnterServerTester.tester);
				EnterServerTester.tester.start();
			} else {
				EnterServerTester.tester.stop();
			}
		}
		String reset = request.getParameter("reset");
		if(reset != null && reset.equals("reset")) {
			if(EnterServerTester.tester != null) {
				EnterServerTester.tester.stop();
			}
			agent.reInit();
		}
	    %>                                              
	    <h2 style="font-weight:bold;">          
	    	排队进入服务器
	    </h2> 
		<center>
			<table id="test1" align="center" width="100%" cellpadding="0"
				cellspacing="0" border="0"
				class="sortable-onload-5-6r rowstyle-alt colstyle-alt no-arrow">
				<tr>
					<th width=9%>
						<b>总数</b>
					</th>
					<th width=9%>
						<b>新手村</b>
					</th>
					<th width=9%>
						<b>准备进入</b>
					</th>
					<th width=9%>
						<b>队列(在线/离线/总)</b>
					</th>
					<th width=9%>
						<b>平均进入间隔</b>
					</th>
					<th width=9%>
						<b>排队进入</b>
					</th>
					<th width=9%>
						<b>超时(通知/准备进入)</b>
					</th>
					<th width=9%>
						<b>最大允许</b>
					</th>
					<th width=9%>
						<b>新手村允许</b>
					</th>
				</tr>
				<%
				if(agent != null) {
					int stat[] = agent.getLineupStatus();
				%>
				<tr>
					<td><%=agent.得到所有在线玩家数量() %></td>
					<td><%=agent.得到新手村在线玩家数量() %></td>
					<td><%=agent.得到准备进入游戏的玩家数量() %></td>
					<td><%=stat[0]+"/"+stat[1]+"/"+(stat[0]+stat[1]) %></td>
					<td><%=agent.getAvgElapedPerNum() %>ms</td>
					<td><%=agent.getLineupIn() %></td>
					<td><%=agent.getLineup().getNotifyTimeoutNum()+"/"+agent.getLineup().getEnterTimeoutNum() %></td>
					<td><%=agent.getMaxOnline() %></td>
					<td><%=agent.getXinShouCunOnline() %></td>
				</tr>
				<%
					}
				%>
			</table><br><br>
			<input type="button" name=bt5 value="  刷 新  " onclick="location.replace('lineup.jsp');"><br><br>
			<form action=lineup.jsp name=f1>
			最大允许数量:<input type=text size=5 name=maxOnline value="<%=(agent!=null?agent.getMaxOnline():0) %>">
			新手村允许数量:<input type=text size=5 name=xinshoucunOnline value="<%=(agent!=null?agent.getXinShouCunOnline():0) %>">
			<input type=submit name=sub1 value="确定">
			<input type=button name=sub2 value=" 清空排队 " onclick="location.replace('lineup.jsp?clear=true')">
			</form>
			
			
			<!-- br><br><br><br><br><br><br><br><br>
			<%
			EnterServerTester tester = EnterServerTester.tester;
			%>
			<form action=lineup.jsp name=f2>
			<b>测试排队:</b>1.进入间隔<input type=text size=5 name=enterPeriod value="<%=tester!=null?tester.getEnterPeriod():"" %>">毫秒  
			2.离开新手村间隔:<input type=text size=5 name=newNumCutPeriod value="<%=tester!=null?tester.getNewNumCutPeriod():"" %>">毫秒  
			3.离开游戏间隔:<input type=text size=5 name=totalCutPeriod value="<%=tester!=null?tester.getTotalCutPeriod():"" %>">毫秒  
			 <input type=submit name=sub2 value="<%=(tester!=null&&tester.isRunning())?"结束":"开始" %>">
			</form>
			<br><br>
			<form action=lineup.jsp name=f3>
				<input type=submit name=sub3 value="重置EnterServerAgent">
				<input type=hidden name=reset value="true">
			</form-->
			<center>
		<br>
		<br>
	</body>
</html>