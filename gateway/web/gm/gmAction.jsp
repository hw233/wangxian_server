<%@page import="java.text.SimpleDateFormat"%>
<%@page import="java.util.*"%>
<%@page import="com.xuanzhi.tools.text.DateUtil"%>
<%@page import="java.util.Arrays"%>
<%@page import="java.util.List"%>
<%@page import="com.fy.gamegateway.gmaction.GmAction"%>
<%@page import="com.fy.gamegateway.gmaction.GmActionManager"%>
<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title>Insert title here</title>
<style type="text/css">
body {
	background-image: url(images/beijing.gif);
}
</style>
<script src="SpryAssets/SpryTabbedPanels.js" type="text/javascript"></script>
<link href="SpryAssets/SpryTabbedPanels.css" rel="stylesheet" type="text/css" />
<style type="text/css">
#apDiv1 {
	position:absolute;
	width:545px;
	height:307px;
	z-index:1;
	left: 443px;
	top: 126px;
}
</style>
</head>
<body>
	<%
		String typeStr = request.getParameter("vtype");
		if(typeStr == null) {
			typeStr = "0";
		}
		int vtype = Integer.valueOf(typeStr);
		List<GmAction> gas = null;
		String action = request.getParameter("action");
		if (action != null) {
			if ("chaxun4Day".equals(action)) {
				String fromD = request.getParameter("fromDay");
				String toD = request.getParameter("toDay");
				SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
				long from = format.parse(fromD + " 00:00:00").getTime();
				long to = format.parse(toD + " 23:59:59").getTime();
				gas = GmActionManager.getInstance().em.query(GmAction.class, "actionTime>? and actionTime<?", new Object[]{from, to}, "", 1, 5000);
			}else if ("testAddGMACTION".equals(action)) {
				String serverName = request.getParameter("serverName");
				String userName = request.getParameter("userName");
				String pID = request.getParameter("pID");
				String pName = request.getParameter("pName");
				String operator = request.getParameter("operator");
				String actionType = request.getParameter("actionType");
				String reason = request.getParameter("reason");
				String amount = request.getParameter("amount");
				GmAction ac = new GmAction();
				ac.setServerName(serverName);
				ac.setUserName(userName);
				ac.setPlayerId(Long.parseLong(pID));
				ac.setPlayerName(pName);
				ac.setOperator(operator);
				ac.setActionTime(System.currentTimeMillis());
				ac.setActionType(Integer.parseInt(actionType));
				ac.setReason(reason);
				ac.setAmount(Long.parseLong(amount));
				ac.setArticleInfo("1111232132111111111");
				ac.setPetInfo("~~~~~~~~~~~~~~~~~~~~~");
				ac.setOtherInfos(new String[] {"123123", "qweqwe"});
				GmActionManager.getInstance().handle_GM_ACTION_REQ(ac);
			}
		}
	Date date = new Date();
	Calendar cal = Calendar.getInstance();
	cal.add(Calendar.DAY_OF_YEAR, -1) ;
	Date yesterday = cal.getTime();
	%>
	<br>
	<form>
		<input type="hidden" name="action" value="chaxun4Day">
		开始<input type="text" name="fromDay" value="<%=DateUtil.formatDate(yesterday,"yyyy-MM-dd") %>">
		--<input type="text" name="toDay" value="<%=DateUtil.formatDate(date,"yyyy-MM-dd") %>">
		<input type="submit" value="确定">
		<input type="radio" name=vtype value=0 checked>所有 <input type="radio" name=vtype value=1>仅列出高级操作 
	</form>
	<br>
	<table border="1" width="98%" style="text-align:center;">
		<tr>
			<td>操作人</td>
			<td>serverName</td>
			<td>目标用户名</td>
			<td>playerId</td>
			<td>玩家名字</td>
			<td>时间</td>
			<td>操作类型</td>
			<td>原因</td>
			<td>数目</td>
			<td>物品Info</td>
			<td>宠物Info</td>
			<td>otherInfos</td>
		</tr>
		<%
			if (gas != null) {
				for (GmAction ac : gas) {
					if(vtype == 1 && (ac.getActionType() != GmAction.ACTION_ADD_NEW_ARTICLE 
							&& ac.getActionType() != GmAction.ACTION_ADD_OLD_ARTICLE 
							&& ac.getActionType() != GmAction.ACTION_ADD_PET
							&& ac.getActionType() != GmAction.ACTION_ADD_SILVER)) {
						continue;
					}
					out.println("<tr>");
					out.println("<td>"+ac.getOperator()+"</td>");
					out.println("<td>"+ac.getServerName()+"</td>");
					out.println("<td>"+ac.getUserName()+"</td>");
					out.println("<td>"+ac.getPlayerId()+"</td>");
					out.println("<td>"+ac.getPlayerName()+"</td>");
					out.println("<td>"+DateUtil.formatDate(new Date(ac.getActionTime()), "yyyy-MM-dd HH:mm:ss")+"</td>");
					out.println("<td>"+getActionDesp(ac.getActionType())+"</td>");
					out.println("<td>"+ac.getReason()+"</td>");
					out.println("<td>"+ac.getAmount()+"</td>");
					out.println("<td>"+ac.getArticleInfo()+"</td>");
					out.println("<td>"+ac.getPetInfo()+"</td>");
					out.println("<td>"+Arrays.toString(ac.getOtherInfos())+"</td>");
					out.println("</tr>");
				}
			}
		%>
	</table>
	<br>
</body>
</html>
<%!
public String getActionDesp(int actionType) {
	switch(actionType) {
	case GmAction.ACTION_FORBID_CHAT:
		return "禁言";
	case GmAction.ACTION_ADD_NEW_ARTICLE: 
		return "发放新物品";
	case GmAction.ACTION_ADD_OLD_ARTICLE:
		return "找回删除物品";
	case GmAction.ACTION_ADD_PET:
		return "找回宠物";
	case GmAction.ACTION_ADD_SILVER:
		return "发放银锭";
	case GmAction.ACTION_CLEAR_SILVER:
		return "清空银锭";
	case GmAction.ACTION_REMOVE_ARTICLE:
		return "删除物品";
	case GmAction.ACTION_CLEAR_AUTHORIZATION:
		return "清空授权";
	case GmAction.ACTION_CLEAR_KNAPSACKCELL:
		return "清除背包格子";
	case GmAction.ACTION_CLEAR_MIBAO:
		return "清空密保";
	case GmAction.ACTION_EDIT_PASSWD:
		return "修改密码";
	case GmAction.ACTION_FORBID_ACCOUNT:
		return "封禁账号";
	case GmAction.ACTION_RELEASE_ACCOUNT:
		return "解封账号";
	default:
		//
	}
	return "";
}
%>
