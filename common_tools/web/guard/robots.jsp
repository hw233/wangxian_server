<%@ page contentType="text/html;charset=utf-8"
	import="java.util.*,
com.xuanzhi.tools.guard.*,com.xuanzhi.tools.text.*"%>
<%
HashMap<String, RobotGroup> groupMap = RobotStater.groupMap;
String act = request.getParameter("act");
if(act != null && act.equals("clear")) {
	groupMap.clear();
}
String keys[] = groupMap.keySet().toArray(new String[0]);
String nowKey = request.getParameter("g");
RobotGroup group = null;
if(nowKey != null && nowKey.length() > 0) {
	group  = groupMap.get(nowKey);
} else {
	if(keys.length > 0) {
		group = groupMap.get(keys[0]);
	}
}
%><html>
<head>
<style>
body {font-size:12px;}
</style>
<SCRIPT type="text/javascript">
<!-- 
function clearAll() {
	document.getElementById("act").value = "clear";
	document.f1.submit();
}
-->
</script>
</head>
	<body>
		<center>
			<h2>
				机器人运行状态
			</h2>
			<br>
			<form action="robots.jsp" name=f1>
				选择机器人所属的组别：
				<select name="g">
					<%
						for (String key : keys) {
					%>
					<option value="<%=key%>"
						<%if(nowKey != null && nowKey.equals(key)) out.print("selected");%>><%=key%></option>
					<%
						}
					%>
				</select>
				<input type=submit name=sub1 value="提交">
				<input type=button name=button1 onclick="clearAll()" value="全部清除">
				<input type=hidden name=act id="act" value="submit">
			</form>
			<br>
			<table border="0" cellpadding="0" cellspacing="1" width="100%"
				bgcolor="#000000" align="center">
				<tr bgcolor="#00FFFF" align="center">
					<td>
						No.
					</td>
					<td>
						名称
					</td>
					<td>
						状态
					</td>
					<td>
						动作
					</td>
					<td>
						动作序列
					</td>
					<td>
						上次动作时间
					</td>
					<td>
						创建时间
					</td>
				</tr>
				<%
					if (group != null) {
						List<Robot> robots = group.getRobots();
						for (int i=0; i<robots.size(); i++) {
							Robot robot = robots.get(i);
				%>
				<tr bgcolor="#FFFFFF" align="center">
					<td><%=(i+1)%></td>
					<td><%=robot.getName()%></td>
					<td><%=Robot.STATE_DESP[robot.getState()]%></td>
					<td><%=robot.getAction()%></td>
					<td><%=robot.getSequence()%></td>
					<td><%=DateUtil.formatDate(new Date(robot.getLastActionTime()), "yyyy-MM-dd HH:mm:ss")%></td>
					<td><%=DateUtil.formatDate(new Date(robot.getCreateTime()), "yyyy-MM-dd HH:mm:ss")%></td>
				</tr>
				<%
					}
					}
				%>
			</table>
			<br>

		</center>
	</body>
</html>
