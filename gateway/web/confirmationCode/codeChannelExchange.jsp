<%@page import="java.util.Iterator"%>
<%@page import="java.util.HashMap"%>
<%@page import="com.xuanzhi.confirmation.bean.ConfirmationCode"%>
<%@page import="com.xuanzhi.confirmation.bean.GameActivity"%>
<%@page import="java.util.List"%>
<%@page import="com.xuanzhi.confirmation.codestore.CodeStorer"%>
<%@page import="com.xuanzhi.confirmation.service.server.DataManager"%>
<%@page import="java.text.SimpleDateFormat"%>
<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<%!SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");%>
<%
	DataManager dataManager = DataManager.getInstance();
	CodeStorer codeStorer = dataManager.getCodeStorer();
	List<GameActivity> activities = codeStorer.getAllActivity();
	out.print("已有活动数量" + activities.size() + "<BR/>");
%>

<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title>查看渠道兑换激活码情况</title>
</head>
<body>
	<table style="font-size: 14px;">
		<tr style="background-color:#FFFF00">
			<td>活动ID</td>
			<td>活动名</td>
			<td>激活码总数</td>
			<td>已兑换数量</td>
			<td>渠道</td>
			<td>兑换数量</td>
		</tr>
		<%
			int index = 0;
			for (GameActivity activity : activities) {
				if (activity != null) {
					index++;
					int exchangeNum = 0;
					HashMap<String, Integer> channelMap = new HashMap<String, Integer>();
					for (String codeStr : activity.getAllCodes()) {
						ConfirmationCode code = dataManager.getCodeStorer().getConfirmationCode(codeStr);
						if (code != null && code.getExchangeTime() > 0) {//已经兑换了
							exchangeNum++;
							String channel = code.getChannel();
							if (channel != null) {
								if (!channelMap.containsKey(channel)) {
									channelMap.put(channel, 0);
								}
								channelMap.put(channel, channelMap.get(channel) + 1);
							}
						}
					}
		%>
		<tr style="background-color: <%=index%2==0?"#C872FC":"#7CF1D7"%>">
			<td><%=activity.getId()%></td>
			<td><a href="./activity.jsp?activeId=<%=activity.getId() %>&option=modify"><%=activity.getName()%></a></td>
			<td><%=activity.getAllCodes().size()%></td>
			<td><%=exchangeNum%></td>
			<td></td>
			<td></td>
		</tr>
		<%
			for (Iterator<String> itor = channelMap.keySet().iterator(); itor.hasNext();) {
						String channel = itor.next();
						int num = channelMap.get(channel);
		%>
		<tr style="background-color: <%=index%2==0?"#C872FC":"#7CF1D7"%>">
			<td></td>
			<td></td>
			<td></td>
			<td></td>
			<td><%=channel%></td>
			<td><%=num%></td>
		</tr>

		<%
			}
		%>
		<%
			}
			}
		%>
	</table>
</body>
</html>