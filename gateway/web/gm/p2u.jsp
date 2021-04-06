<%@ page contentType="text/html;charset=utf-8" import="java.util.*,
com.xuanzhi.tools.text.*,com.fy.boss.authorize.model.*,com.fy.boss.client.*,
com.xuanzhi.tools.transport.*,java.nio.channels.*,com.fy.gamegateway.mieshi.server.*"%>
<%@page import="com.xuanzhi.tools.text.ParamUtils"%>
<%@page import="com.fy.gamegateway.mieshi.waigua.*"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">
<head>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
<title>账号信息查询</title>
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
<script>
</script>
</head>
<%
MieshiPlayerInfoManager mpm = MieshiPlayerInfoManager.getInstance();
String playername = request.getParameter("playername");
List<MieshiPlayerInfo> infolist = null;
if(playername != null) {
	infolist = mpm.getMieshiPlayerInfoByPlayernameFromDB(playername);
}
%>
<body>
<form action="p2u.jsp" method=post>
<table width="1157" cellpadding="2" cellspacing="2">
  <tr>
    <td width="430"><label for="textfield">角色：</label>
      <input type="text" name="playername" id="playername"/>
      <input type="submit" name="button" id="button" value="查询"/>
    </td>
  </tr>
</table>
</form>
<hr />
<%if(infolist != null) { 
%>
<p>所有服务器上的角色信息：</p>
<table width="1162" height="73" border="1" align="center">
  <tr>
    <td height="21">用户名</td>
	<td>服务器</td>
	<td>角色ID</td>
	<td>名称</td>
	<td>职业</td>
	<td>等级</td>
	<td>充值</td>
	<td>VIP</td>
	<td>最后在线时间</td>
  </tr>
  <%
  for(MieshiPlayerInfo info : infolist) {
		%>
			<tr>
				<td><a href="javascript:window.open('account.jsp?username=<%=info.getUserName() %>','_blank')"><%=info.getUserName() %></a></td>
				<td><%=info.getServerRealName() %></td>
				<td><%=info.getPlayerId() %></td>
				<td><%=info.getPlayerName() %></td>
				<td><%=getCareerName(info.getCareer()) %></td>
				<td><%=info.getLevel() %></td>
				<td><%=info.getPlayerRMB() %></td>
				<td><%=info.getPlayerVIP() %></td>
				<td><%=DateUtil.formatDate(new Date(info.getLastAccessTime()),"yyyy-MM-dd HH:mm:ss") %></td>
			</tr>
		<%
			}
		%>
</table>
<p>&nbsp;</p>
<script type="text/javascript">
var TabbedPanels1 = new Spry.Widget.TabbedPanels("TabbedPanels1");
</script>
<%} %>
</body>
</html>
<%!
String[] careerNames = new String[]{"通用","斗罗","鬼煞","灵尊","巫皇"};
public String getCareerName(int career) {
	return careerNames[career];
}
%>