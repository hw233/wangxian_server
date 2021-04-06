<%@ page contentType="text/html;charset=utf-8"%>
<%@ page import="java.util.*,com.fy.engineserver.guozhan.*,com.xuanzhi.tools.text.*"%>
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
		GuozhanOrganizer org = GuozhanOrganizer.getInstance();
		Constants cons = org.getConstants();
		String reset = request.getParameter("reset");
		if(reset != null && reset.equals("true")) {
			cons = new Constants();
			org.setConstants(cons);
			org.updateConstants();
		}
		String subed = request.getParameter("subed");
		if(subed!=null && subed.equals("true")) {
			String declareStart = request.getParameter("declareStart");
			cons.开始宣战时间 = declareStart;
			String declareEnd = request.getParameter("declareEnd");
			cons.结束宣战时间 = declareEnd;
			String openTime = request.getParameter("openTime");
			cons.国战开启时间 = openTime;
			String guozhanTime = request.getParameter("guozhanTime");
			cons.国战初始时长 = Integer.valueOf(guozhanTime);
			String levelLimit = request.getParameter("levelLimit");
			cons.国战等级限制 = Integer.valueOf(levelLimit);
			String delayCount = request.getParameter("delayCount");
			cons.延长国战时间次数 = Integer.valueOf(delayCount);
			String delayTime = request.getParameter("delayTime");
			cons.延长国战时长 = Integer.valueOf(delayTime);
			String delayResource = request.getParameter("delayResource");
			cons.延长国战时长消耗资源 = Integer.valueOf(delayResource);
			String pullCount = request.getParameter("pullCount");
			cons.国战拉人次数 = Integer.valueOf(pullCount);
			String pullResource = request.getParameter("pullResource");
			cons.国战拉人消耗资源 = Integer.valueOf(pullResource);
			String cureCount = request.getParameter("cureCount");
			cons.国战加血次数 = Integer.valueOf(cureCount);
			String cureHP = request.getParameter("cureHP");
			cons.国战每次加血量 = Double.valueOf(cureHP);
			String cureResource = request.getParameter("cureResource");
			cons.国战加血消耗资源 = Integer.valueOf(cureResource);
			String description = request.getParameter("description");
			cons.控制面板说明 = description;
			String longyaguanNPCPoint = request.getParameter("longyaguanNPCPoint");
			cons.NPC_边城守将A_data[2] = Integer.valueOf(longyaguanNPCPoint.split(",")[0]);
			cons.NPC_边城守将A_data[3] = Integer.valueOf(longyaguanNPCPoint.split(",")[1]);
			String yaosaiNPCPoint = request.getParameter("yaosaiNPCPoint");
			cons.NPC_边城守将B_data[2] = Integer.valueOf(yaosaiNPCPoint.split(",")[0]);
			cons.NPC_边城守将B_data[3] = Integer.valueOf(yaosaiNPCPoint.split(",")[1]);
			String wangchengNPCPoint = request.getParameter("wangchengNPCPoint");
			cons.NPC_王城戌卫官_data[2] = Integer.valueOf(wangchengNPCPoint.split(",")[0]);
			cons.NPC_王城戌卫官_data[3] = Integer.valueOf(wangchengNPCPoint.split(",")[1]);
			String boss1Point = request.getParameter("boss1Point");
			cons.NPC_龙象卫长_data[2] = Integer.valueOf(boss1Point.split(",")[0]);
			cons.NPC_龙象卫长_data[3] = Integer.valueOf(boss1Point.split(",")[1]);
			String boss2Point = request.getParameter("boss2Point");
			cons.NPC_龙象释帝_data[2] = Integer.valueOf(boss2Point.split(",")[0]);
			cons.NPC_龙象释帝_data[3] = Integer.valueOf(boss2Point.split(",")[1]);
			String gongxunzhi = request.getParameter("gongxunzhi");
			String str[] = gongxunzhi.split(",");
			if(str.length == cons.击杀功勋值表.length) {
				for(int i=0; i<str.length; i++) {
					cons.击杀功勋值表[i] = Integer.valueOf(str[i]);
				}
			}
			String order1 = request.getParameter("order1");
			str = order1.split("/");
			cons.publicMessages.get(0).setIcon(str[0]);
			cons.publicMessages.get(0).setName(str[1]);
			cons.publicMessages.get(0).setMessage(str[2]);
			String order2 = request.getParameter("order2");
			str = order2.split("/");
			cons.publicMessages.get(1).setIcon(str[0]);
			cons.publicMessages.get(1).setName(str[1]);
			cons.publicMessages.get(1).setMessage(str[2]);
			String order3 = request.getParameter("order3");
			str = order3.split("/");
			cons.publicMessages.get(2).setIcon(str[0]);
			cons.publicMessages.get(2).setName(str[1]);
			cons.publicMessages.get(2).setMessage(str[2]);
			org.updateConstants();
		}
	    %>                                              
	    <h2 style="font-weight:bold;">          
	    	国战常量设置
	    </h2> 
		<center>
			<form action="constants.jsp" name=f1 method="post">
			<table id="test1" align="center" width="100%" cellpadding="0"
				cellspacing="0" border="0"
				class="sortable-onload-5-6r rowstyle-alt colstyle-alt no-arrow">
				<tr>
					<th width=11%>
						<b>开始宣战时间</b>
					</th>
					<th width=11%>
						<b>结束宣战时间</b>
					</th>
					<th width=11%>
						<b>开启时间</b>
					</th>
					<th width=11%>
						<b>默认时长(分)</b>
					</th>
					<th width=11%>
						<b>等级限制</b>
					</th>
					<th width=11%>
						<b>延长时间次数</b>
					</th>
					<th width=11%>
						<b>延长国战时长(分)</b>
					</th>
					<th width=11%>
						<b>延长消耗资源</b>
					</th>
				</tr>
				<tr>
					<td><input type="text" size=5 name="declareStart" value="<%=cons.开始宣战时间 %>"></td>
					<td><input type="text" size=5 name="declareEnd" value="<%=cons.结束宣战时间 %>"></td>
					<td><input type="text" size=5 name="openTime" value="<%=cons.国战开启时间 %>"></td>
					<td><input type="text" size=5 name="guozhanTime" value="<%=cons.国战初始时长 %>"></td>
					<td><input type="text" size=5 name="levelLimit" value="<%=cons.国战等级限制 %>"></td>
					<td><input type="text" size=5 name="delayCount" value="<%=cons.延长国战时间次数 %>"></td>
					<td><input type="text" size=5 name="delayTime" value="<%=cons.延长国战时长 %>"></td>
					<td><input type="text" size=5 name="delayResource" value="<%=cons.延长国战时长消耗资源 %>"></td>
				</tr>
				<tr>
					<th width=11%>
						<b>拉人次数</b>
					</th>
					<th width=11%>
						<b>拉人消耗资源</b>
					</th>
					<th width=11%>
						<b>加血次数</b>
					</th>
					<th width=11%>
						<b>每次加血量</b>
					</th>
					<th width=11%>
						<b>加血消耗资源</b>
					</th>
					<th width=11% colspan="3">
						<b>国战面板顶部说明</b>
					</th>
				</tr>
				<tr>
					<td><input type="text" size=5 name="pullCount" value="<%=cons.国战拉人次数 %>"></td>
					<td><input type="text" size=5 name="pullResource" value="<%=cons.国战拉人消耗资源 %>"></td>
					<td><input type="text" size=5 name="cureCount" value="<%=cons.国战加血次数 %>"></td>
					<td><input type="text" size=5 name="cureHP" value="<%=cons.国战每次加血量 %>"></td>
					<td><input type="text" size=5 name="cureResource" value="<%=cons.国战加血消耗资源 %>"></td>
					<td colspan="3"><input type="text" size=50 name="description" value="<%=cons.控制面板说明 %>"></td>
				</tr>
				<tr>
					<th width=11%>
						<b>边城守将A坐标</b>
					</th>
					<th width=11%>
						<b>边城守将B坐标</b>
					</th>
					<th width=11%>
						<b>王城戌卫官坐标</b>
					</th>
					<th width=11%>
						<b>龙象卫长坐标</b>
					</th>
					<th width=11%>
						<b>龙象释帝坐标</b>
					</th>
					<th width=11% colspan="3">
						<b>功勋值表(0阶,1阶...)</b>
					</th>
				</tr>
				<tr>
					<td><input type="text" size=8 name="longyaguanNPCPoint" value="<%=cons.NPC_边城守将A_data[2] %>,<%=cons.NPC_边城守将A_data[3] %>"></td>
					<td><input type="text" size=8 name="yaosaiNPCPoint" value="<%=cons.NPC_边城守将B_data[2] %>,<%=cons.NPC_边城守将B_data[3] %>"></td>
					<td><input type="text" size=8 name="wangchengNPCPoint" value="<%=cons.NPC_王城戌卫官_data[2] %>,<%=cons.NPC_王城戌卫官_data[3] %>"></td>
					<td><input type="text" size=8 name="boss1Point" value="<%=cons.NPC_龙象卫长_data[2] %>,<%=cons.NPC_龙象卫长_data[3] %>"></td>
					<td><input type="text" size=8 name="boss2Point" value="<%=cons.NPC_龙象释帝_data[2] %>,<%=cons.NPC_龙象释帝_data[3] %>"></td>
					<td colspan="3"><input type="text" size=50 name="gongxunzhi" value="<%=StringUtil.arrayToString(cons.击杀功勋值表,",") %>"></td>
				</tr>
				<tr>
					<th width=11% colspan="3">
						<b>预设命令1</b>
					</th>
					<th width=11% colspan="3">
						<b>预设命令2</b>
					</th>
					<th width=11% colspan="2">
						<b>预设命令3</b>
					</th>
				</tr>
				<tr>
					<td colspan="3"><input type="text" size=40 name="order1" value="<%=cons.publicMessages.get(0).getIcon()+"/"+cons.publicMessages.get(0).getName()+"/"+cons.publicMessages.get(0).getMessage() %>"></td>
					<td colspan="3"><input type="text" size=40 name="order2" value="<%=cons.publicMessages.get(1).getIcon()+"/"+cons.publicMessages.get(1).getName()+"/"+cons.publicMessages.get(1).getMessage() %>"></td>
					<td colspan="2"><input type="text" size=40 name="order3" value="<%=cons.publicMessages.get(2).getIcon()+"/"+cons.publicMessages.get(2).getName()+"/"+cons.publicMessages.get(2).getMessage() %>"></td>
				</tr>
			</table><br><br>
			<input type=hidden name=subed value="true">
			<input type=submit name=sub1 value="确定">
			<input type=button name=bt1 value="重设所有常量" onclick="location.replace('constants.jsp?reset=true')">
			</form>
			<center>
		<br>
		<br>
	</body>
</html>