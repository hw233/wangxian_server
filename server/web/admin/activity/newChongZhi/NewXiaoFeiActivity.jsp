<%@page import="java.util.ArrayList"%>
<%@page import="com.xuanzhi.boss.game.GameConstants"%>
<%@page
	import="com.fy.engineserver.activity.newChongZhiActivity.BillXiaoFeiBoardActivity"%>
<%@page
	import="com.fy.engineserver.activity.newChongZhiActivity.TotalTimesXiaoFeiActivity"%>
<%@page
	import="com.fy.engineserver.activity.newChongZhiActivity.TotalXiaoFeiActivity"%>
<%@page
	import="com.fy.engineserver.activity.newChongZhiActivity.SingleXiaoFeiActivity"%>
<%@page
	import="com.fy.engineserver.activity.newChongZhiActivity.FirstXiaoFeiActivity"%>
<%@page
	import="com.fy.engineserver.activity.newChongZhiActivity.NewXiaoFeiActivity"%>
<%@page
	import="com.fy.engineserver.activity.newChongZhiActivity.SingleBalanceChongZhiActivity"%>
<%@page
	import="com.fy.engineserver.activity.newChongZhiActivity.TotalTimesChongZhiActivity"%>
<%@page
	import="com.fy.engineserver.activity.newChongZhiActivity.FirstChongZhiFanLiActivity"%>
<%@page import="com.fy.engineserver.newBillboard.BillboardsManager"%>
<%@page
	import="com.fy.engineserver.activity.newChongZhiActivity.FanLiTimelyActivity"%>
<%@page
	import="com.fy.engineserver.activity.newChongZhiActivity.BillBoardActivity"%>
<%@page
	import="com.fy.engineserver.activity.newChongZhiActivity.TotalChongZhiActivity"%>
<%@page
	import="com.fy.engineserver.activity.newChongZhiActivity.SingleChongZhiActivity"%>
<%@page
	import="com.fy.engineserver.activity.newChongZhiActivity.FanLi4LongTimeChongZhiActivity"%>
<%@page
	import="com.fy.engineserver.activity.newChongZhiActivity.FirstChongZhiActivity"%>
<%@page import="java.util.Arrays"%>
<%@page import="com.fy.engineserver.sprite.PlayerManager"%>
<%@page
	import="com.fy.engineserver.activity.newChongZhiActivity.NewChongZhiActivityManager"%>
<%@page import="com.fy.engineserver.sprite.Player"%>
<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<%@page import="com.fy.engineserver.newtask.service.TaskManager"%>
<%@page import="java.util.List"%>
<%@page import="com.fy.engineserver.newtask.Task"%>
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title>Insert title here</title>
</head>
<body>

	<%
		String serverName = GameConstants.getInstance().getServerName();
		ArrayList<String> unServerName = new ArrayList<String>();
		unServerName.add("国内本地测试");
		unServerName.add("pan2");
		unServerName.add("ST");
		unServerName.add("化外之境");
		unServerName.add("仙尊降世");
		if (!unServerName.contains(serverName)) {

			String userName = (String) session.getAttribute("username");
			if (userName == null) {

				String action = request.getParameter("action");
				if (action != null && action.equals("login")) {
					String u = request.getParameter("username");
					String p = request.getParameter("password");

					if (u != null && p != null && u.equals("wtx") && p.equals("wtx111")) {
						userName = u;
						session.setAttribute("username", userName);
					} else {
						out.println("<h3>用户名密码不对</h3>");
					}
				}

				if (userName == null) {
	%>
	<center>
		<h1>请先登录</h1>
		<form>
			<input type="hidden" name="action" value="login"> 请输入用户名：<input
				type="text" name="username" value="" size=30><br /> 请输入密码：<input
				type="text" name="password" value="" size=30><br /> <input
				type="submit" value="提  交">
		</form>
	</center>
	<%
		return;
				}
			}
		}
	%>

	<%
		NewXiaoFeiActivity chooseActivity = null;
		NewXiaoFeiActivity seeActivity = null;
		int isCreatType = -1;
		String action = request.getParameter("action");
		if (action != null) {
			if (action.equals("testChongZhi")) {
				String pName = request.getParameter("pName");
				String pMoney_str = request.getParameter("money");
				int xType = Integer.parseInt(request.getParameter("xType"));
				long cMoney = Long.parseLong(pMoney_str);
				if (cMoney <= 1000000) {
					Player player = PlayerManager.getInstance().getPlayer(pName);
					NewChongZhiActivityManager.instance.doXiaoFeiActivity(player, cMoney, xType);
				}
			} else if (action.equals("cleanActivity")) {
				String acID_str = request.getParameter("activityID");
				int acID = Integer.parseInt(acID_str);
				for (int i = 0; i < NewChongZhiActivityManager.instance.xiaoFeiAcitivitys.size(); i++) {
					NewXiaoFeiActivity ac = NewChongZhiActivityManager.instance.xiaoFeiAcitivitys.get(i);
					if (acID == ac.getConfigID()) {
						ac.cleanActivityData();
					}
				}
			} else if (action.equals("chooseActivity")) {
				String acID_str = request.getParameter("activityID");
				int acID = Integer.parseInt(acID_str);
				for (int i = 0; i < NewChongZhiActivityManager.instance.xiaoFeiAcitivitys.size(); i++) {
					NewXiaoFeiActivity ac = NewChongZhiActivityManager.instance.xiaoFeiAcitivitys.get(i);
					if (acID == ac.getConfigID()) {
						chooseActivity = ac;
						break;
					}
				}
			} else if (action.equals("seeActivity")) {
				String acID_str = request.getParameter("activityID");
				int acID = Integer.parseInt(acID_str);
				for (int i = 0; i < NewChongZhiActivityManager.instance.xiaoFeiAcitivitys.size(); i++) {
					NewXiaoFeiActivity ac = NewChongZhiActivityManager.instance.xiaoFeiAcitivitys.get(i);
					if (acID == ac.getConfigID()) {
						seeActivity = ac;
						break;
					}
				}
			} else if (action.equals("createTO")) {
				String type_str = request.getParameter("createType");
				isCreatType = Integer.parseInt(type_str);
			} else if (action.equals("xiugai")) {
				String acID_str = request.getParameter("activityID");
				int acID = Integer.parseInt(acID_str);
				String xfType_str = request.getParameter("xiaoFeiType");
				xfType_str = xfType_str.substring(1, xfType_str.length() - 1);
				String[] xfTypes = xfType_str.split(", ");
				int[] xfT = new int[xfTypes.length];
				for (int kk = 0; kk < xfT.length; kk++) {
					xfT[kk] = Integer.parseInt(xfTypes[kk]);
				}
				String name_str = request.getParameter("name");
				String platform_str = request.getParameter("platform");
				int platform = Integer.parseInt(platform_str);
				String serverNames_str = request.getParameter("serverNames");
				serverNames_str = serverNames_str.substring(1, serverNames_str.length() - 1);
				String[] serverNames = serverNames_str.split(", ");
				String unserverNames_str = request.getParameter("unserverNames");
				unserverNames_str = unserverNames_str.substring(1, unserverNames_str.length() - 1);
				String[] unserverNames = unserverNames_str.split(", ");
				String startTime_str = request.getParameter("startTime");
				String endTime_str = request.getParameter("endTime");
				String mailTile_str = request.getParameter("mailTile");
				String mailMsg_str = request.getParameter("mailMsg");
				String[] parameters = new String[] { "", "", "", "", "", "" };
				String parameter1_str = request.getParameter("parameter1");
				String parameter2_str = request.getParameter("parameter2");
				String parameter3_str = request.getParameter("parameter3");
				String parameter4_str = request.getParameter("parameter4");
				String parameter5_str = request.getParameter("parameter5");
				String parameter6_str = request.getParameter("parameter6");
				if (parameter1_str != null) {
					parameters[0] = parameter1_str;
				}
				if (parameter2_str != null) {
					parameters[1] = parameter2_str;
				}
				if (parameter3_str != null) {
					parameters[2] = parameter3_str;
				}
				if (parameter4_str != null) {
					parameters[3] = parameter4_str;
				}
				if (parameter5_str != null) {
					parameters[4] = parameter5_str;
				}
				if (parameter6_str != null) {
					parameters[5] = parameter6_str;
				}
				NewXiaoFeiActivity ac = null;
				int index = -1;
				for (int i = 0; i < NewChongZhiActivityManager.instance.xiaoFeiAcitivitys.size(); i++) {
					NewXiaoFeiActivity ac1 = NewChongZhiActivityManager.instance.xiaoFeiAcitivitys.get(i);
					if (acID == ac1.getConfigID()) {
						ac = ac1;
						index = i;
					}
				}
				if (ac != null && index >= 0) {
					NewXiaoFeiActivity temp = null;
					if (ac instanceof FirstXiaoFeiActivity) {
						temp = new FirstXiaoFeiActivity(acID, xfT, name_str, platform, startTime_str, endTime_str, serverNames, unserverNames, mailTile_str, mailMsg_str, parameters);
					} else if (ac instanceof SingleXiaoFeiActivity) {
						//单笔消费
						temp = new SingleXiaoFeiActivity(acID, xfT, name_str, platform, startTime_str, endTime_str, serverNames, unserverNames, mailTile_str, mailMsg_str, parameters);
					} else if (ac instanceof TotalXiaoFeiActivity) {
						//累计一次
						temp = new TotalXiaoFeiActivity(acID, xfT, name_str, platform, startTime_str, endTime_str, serverNames, unserverNames, mailTile_str, mailMsg_str, parameters);
					} else if (ac instanceof TotalTimesXiaoFeiActivity) {
						temp = new TotalTimesXiaoFeiActivity(acID, xfT, name_str, platform, startTime_str, endTime_str, serverNames, unserverNames, mailTile_str, mailMsg_str, parameters);
					} else if (ac instanceof BillXiaoFeiBoardActivity) {
						temp = new BillXiaoFeiBoardActivity(acID, xfT, name_str, platform, startTime_str, endTime_str, serverNames, unserverNames, mailTile_str, mailMsg_str, parameters);
					}
					temp.creatParameter(parameters);
					temp.loadDiskCache();
					NewChongZhiActivityManager.instance.xiaoFeiAcitivitys.set(index, temp);
				}
			} else if (action.equals("createOne")) {
				String createType_str = request.getParameter("creteType");
				int createType = Integer.parseInt(createType_str);
				String acID_str = request.getParameter("activityID");
				int acID = Integer.parseInt(acID_str);
				String xfType_str = request.getParameter("xiaoFeiType");
				xfType_str = xfType_str.substring(1, xfType_str.length() - 1);
				String[] xfTypes = xfType_str.split(", ");
				int[] xfT = new int[xfTypes.length];
				for (int kk = 0; kk < xfT.length; kk++) {
					xfT[kk] = Integer.parseInt(xfTypes[kk]);
				}
				String name_str = request.getParameter("name");
				String platform_str = request.getParameter("platform");
				int platform = Integer.parseInt(platform_str);
				String serverNames_str = request.getParameter("serverNames");
				serverNames_str = serverNames_str.substring(1, serverNames_str.length() - 1);
				String[] serverNames = serverNames_str.split(", ");
				String unserverNames_str = request.getParameter("unserverNames");
				unserverNames_str = unserverNames_str.substring(1, unserverNames_str.length() - 1);
				String[] unserverNames = unserverNames_str.split(", ");
				String startTime_str = request.getParameter("startTime");
				String endTime_str = request.getParameter("endTime");
				String mailTile_str = request.getParameter("mailTile");
				String mailMsg_str = request.getParameter("mailMsg");
				String[] parameters = new String[] { "", "", "", "", "", "" };
				String parameter1_str = request.getParameter("parameter1");
				String parameter2_str = request.getParameter("parameter2");
				String parameter3_str = request.getParameter("parameter3");
				String parameter4_str = request.getParameter("parameter4");
				String parameter5_str = request.getParameter("parameter5");
				String parameter6_str = request.getParameter("parameter6");
				if (parameter1_str != null) {
					parameters[0] = parameter1_str;
				}
				if (parameter2_str != null) {
					parameters[1] = parameter2_str;
				}
				if (parameter3_str != null) {
					parameters[2] = parameter3_str;
				}
				if (parameter4_str != null) {
					parameters[3] = parameter4_str;
				}
				if (parameter5_str != null) {
					parameters[4] = parameter5_str;
				}
				if (parameter6_str != null) {
					parameters[5] = parameter6_str;
				}
				NewXiaoFeiActivity ac = null;
				int index = -1;
				for (int i = 0; i < NewChongZhiActivityManager.instance.xiaoFeiAcitivitys.size(); i++) {
					NewXiaoFeiActivity ac1 = NewChongZhiActivityManager.instance.xiaoFeiAcitivitys.get(i);
					if (acID == ac1.getConfigID()) {
						ac = ac1;
						index = i;
						break;
					}
				}
				if (ac == null && index < 0) {
					NewXiaoFeiActivity temp = null;
					if (createType == 0) {
						//首次消费
						temp = new FirstXiaoFeiActivity(acID, xfT, name_str, platform, startTime_str, endTime_str, serverNames, unserverNames, mailTile_str, mailMsg_str, parameters);
					} else if (createType == 1) {
						//单笔消费
						temp = new SingleXiaoFeiActivity(acID, xfT, name_str, platform, startTime_str, endTime_str, serverNames, unserverNames, mailTile_str, mailMsg_str, parameters);
					} else if (createType == 2) {
						temp = new TotalXiaoFeiActivity(acID, xfT, name_str, platform, startTime_str, endTime_str, serverNames, unserverNames, mailTile_str, mailMsg_str, parameters);
					} else if (createType == 3) {
						temp = new TotalTimesXiaoFeiActivity(acID, xfT, name_str, platform, startTime_str, endTime_str, serverNames, unserverNames, mailTile_str, mailMsg_str, parameters);
					} else if (createType == 4) {
						temp = new BillXiaoFeiBoardActivity(acID, xfT, name_str, platform, startTime_str, endTime_str, serverNames, unserverNames, mailTile_str, mailMsg_str, parameters);
					}
					temp.creatParameter(parameters);
					temp.loadDiskCache();
					NewChongZhiActivityManager.instance.xiaoFeiAcitivitys.add(temp);
				}
			} else if (action.equals("backExcel")) {
				NewChongZhiActivityManager.instance.xiaoFeiAcitivitys.clear();
				try {
					NewChongZhiActivityManager.instance.loadXiaoFeiFile();
				} catch (Throwable e) {e.printStackTrace();}
			} else if (action.equals("qing_billboard")) {
				BillboardsManager.getInstance().lastFlushBillboardTime = 0;
			}
		}
	%>

	<form name="f8">
		重新载入 <input type="hidden" name="action" value="backExcel"> <input
			type="submit" value=<%="确定"%>>
	</form>
	<br>

	<table border="2">
		<tr>
			<td>活动ID</td>
			<td>活动名字</td>
			<td>消费Type</td>
			<td>开始时间</td>
			<td>结束时间</td>
			<td>平台</td>
			<td>服务器名字</td>
			<td>不参与服务器名字</td>
			<td>参数</td>
			<td>是否server</td>
			<td>是否start</td>
		</tr>
		<%
			for (int i = 0; i < NewChongZhiActivityManager.instance.xiaoFeiAcitivitys.size(); i++) {
				NewXiaoFeiActivity ac = NewChongZhiActivityManager.instance.xiaoFeiAcitivitys.get(i);
				long now = System.currentTimeMillis();
				boolean isStart = now > ac.getStartTimeLong() && now < ac.getEndTimeLong();
		%>
		<tr>
			<td><%=ac.getConfigID()%></td>
			<td><%=ac.getName()%></td>
			<td>"<%=Arrays.toString(ac.getXiaofeiType())%>"
			</td>
			<td><%=ac.getStartTime()%></td>
			<td><%=ac.getEndTime()%></td>
			<td><%=ac.getPlatform()%></td>
			<td><%=Arrays.toString(ac.getServerNames())%></td>
			<td><%=Arrays.toString(ac.getUnServerNames())%></td>
			<td><%=ac.getParameter()%></td>
			<td><%=ac.isCanServer()%></td>
			<td><%=isStart%></td>
		</tr>
		<%
			}
		%>
	</table>
	<br>
	<form name="f3">
		修改某个活动 <input type="hidden" name="action" value="chooseActivity">
		活动ID<input name="activityID"> <input type="submit"
			value=<%="确定"%>>
	</form>
	<br>
	<form name="f6">
		创建活动 <input type="hidden" name="action" value="createTO">
		活动Type(0首充,1单次,2累计一次,3累计多次,4消费排行)<input name="createType"> <input
			type="submit" value=<%="确定"%>>
	</form>
	<br>
	<form name="f7">
		查看活动参与情况 <input type="hidden" name="action" value="seeActivity">
		活动ID<input name="activityID"> <input type="submit"
			value=<%="确定"%>>
	</form>
	<br>
	<%
		if (chooseActivity != null) {
	%>
	<form name="f4">
		<hr>
		活动信息 <input type="hidden" name="action" value="xiugai"> 活动ID<input
			name="activityID" value=<%=chooseActivity.getConfigID()%>>
		消费Type<input name="xiaoFeiType"
			value="<%=Arrays.toString(chooseActivity.getXiaofeiType())%>">
		名字<input name="name" value=<%=chooseActivity.getName()%>> 平台<input
			name="platform" value=<%=chooseActivity.getPlatform()%>> <br>
		服务器名字<input name="serverNames"
			value="<%=Arrays.toString(chooseActivity.getServerNames())%>"
			size="100"> <br> un服务器名字<input name="unserverNames"
			value="<%=Arrays.toString(chooseActivity.getUnServerNames())%>"
			size="100"> <br> startTime<input name="startTime"
			value="<%=chooseActivity.getStartTime()%>"> endTime<input
			name="endTime" value="<%=chooseActivity.getEndTime()%>"> <br>
		邮件标题<input name="mailTile" value="<%=chooseActivity.getMailTitle()%>">
		邮件内容<input name="mailMsg" value="<%=chooseActivity.getMailMsg()%>"
			size="100"> <br> 参数1<input name="parameter1"
			value="<%=chooseActivity.getParameters()[0]%>" size="100"> <br>
		参数2<input name="parameter2"
			value="<%=chooseActivity.getParameters()[1]%>" size="100"> <br>
		参数3<input name="parameter3"
			value="<%=chooseActivity.getParameters()[2]%>" size="100"> <br>
		参数4<input name="parameter4"
			value="<%=chooseActivity.getParameters()[3]%>" size="100"> <br>
		参数5<input name="parameter5"
			value="<%=chooseActivity.getParameters()[4]%>" size="100"> <br>
		参数6<input name="parameter6"
			value="<%=chooseActivity.getParameters()[5]%>" size="100"> <input
			type="submit" value=<%="确定"%>>
		<hr>
	</form>
	<%
		}
	%>
	<%
		if (isCreatType >= 0) {
	%>
	<form name="f5">
		<hr>
		活动信息 <input type="hidden" name="action" value="createOne"> <input
			type="hidden" name="creteType" value="<%=isCreatType%>">
		活动ID<input name="activityID"> 消费Type<input name="xiaoFeiType"
			value=""> 名字<input name="name"> 平台<input
			name="platform"> <br> 服务器名字<input name="serverNames"
			size="100"> <br> un服务器名字<input name="unserverNames"
			size="100"> <br> startTime<input name="startTime"
			value="2013-03-18 00:00:00"> endTime<input name="endTime"
			value="2013-03-18 23:59:59"> <br> 邮件标题<input
			name="mailTile"> 邮件内容<input name="mailMsg" size="100">
		<br> 参数1<input name="parameter1" size="100"> <br>
		参数2<input name="parameter2" size="100"> <br> 参数3<input
			name="parameter3" size="100"> <br> 参数4<input
			name="parameter4" size="100"> <br> 参数5<input
			name="parameter5" size="100"> <br> 参数6<input
			name="parameter6" size="100"> <input type="submit"
			value=<%="确定"%>>
		<hr>
	</form>
	<%
		}
	%>
	<br>
	<form name="f1">
		测试消费活动 <input type="hidden" name="action" value="testChongZhi">
		玩家名字<input name="pName"> 金额<input name="money">
		消费Type(0商城,1税)<input name="xType"> <input type="submit"
			value=<%="确定"%>>
	</form>
	<br>
	<form name="f2">
		清除活动数据 <input type="hidden" name="action" value="cleanActivity">
		活动ID<input name="activityID"> <input type="submit"
			value=<%="确定"%>>
	</form>
	<br>
	<br>
	<form name="f5">
		<input type="hidden" name="action" value="qing_billboard"> <input
			type="submit" value="请排行">
	</form>
	<br>
	<%
		if (seeActivity != null) {
	%>
	活动详细信息
	<br>
	<%
		for (int i = 0; i < seeActivity.getActivityPartake().length; i++) {
				String aa = seeActivity.getActivityPartake()[i];
	%>
	<%=aa%>
	<br>
	<%
		}
	%>
	<%
		}
	%>
</body>
</html>
