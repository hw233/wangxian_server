<%@page import="com.xuanzhi.boss.game.GameConstants"%>
<%@page import="java.io.ByteArrayOutputStream"%>
<%@page import="java.io.OutputStream"%>
<%@page import="java.io.DataOutputStream"%>
<%@page import="com.xuanzhi.tools.text.JsonUtil"%>
<%@page import="java.util.ArrayList"%>
<%@page import="com.fy.engineserver.enterlimit.EnterLimitManager"%>
<%@page import="com.fy.engineserver.enterlimit.EnterLimitManager.PlayerRecordData"%>
<%@page import="com.fy.engineserver.sprite.SpriteSubSystem"%>
<%@page import="com.fy.boss.authorize.model.Passport"%>
<%@page import="java.io.Serializable"%>
<%@page import="com.fy.engineserver.sprite.Player"%>
<%@page import="com.fy.engineserver.sprite.PlayerManager"%>
<%@page import="com.fy.engineserver.enterlimit.AndroidMsgData"%>
<%@page import="com.fy.engineserver.enterlimit.AndroidMsgManager"%>
<%@ page contentType="text/html;charset=utf-8" import="java.util.*"%>

<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title>Insert title here</title>
</head>
<body>
<BR/>
<hr>
	<table border="1">
	<tr>
	<td>用户</td>
	<td>ID</td>
	<td>名字</td>
	<td>机型</td>
	<td>平台--------</td>
	<td>loginCH</td>
	<td>RegisterCH</td>
	</tr>
	<%
	Player[] ps = PlayerManager.getInstance().getOnlinePlayers();
		for (Player p : ps) {
			Integer num = EnterLimitManager.getInstance().NO_PROCESS_REQNUM.get(p.getId());
			int unBackNum = 0;
			if (num != null) {
				unBackNum = num.intValue();
			}
			%>
			<tr>
			<td><%=p.getUsername() %></td>
			<td><%=p.getId() %></td>
			<td><%=p.getName() %></td>
			<td><%=p.getPassport().getLastLoginMobileType() %></td>
			<td><%=p.getPassport().getLastLoginMobileOs() %></td>
			<td><%=p.getPassport().getLastLoginChannel() %></td>
			<td><%=p.getPassport().getRegisterChannel() %></td>
			<td><%=unBackNum %></td>
			</tr>
			<%
			
		}
	%>
	</table>
</body>
</html>