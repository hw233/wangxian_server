<%@ page import="com.fy.boss.game.model.Server"%>
<%@ page import="com.fy.boss.game.service.ServerManager"%>
<%@ page import="java.util.*,java.io.*,
				com.xuanzhi.tools.text.*"
				%>
<%@ page contentType="text/html;charset=utf-8"%>

<html xmlns="http://www.w3.org/1999/xhtml" lang="zh-cn">
<head>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8">
<title>更新服务器</title>
<link rel="stylesheet" href="css/style.css"/>
<script language="JavaScript">
<!--
function subcheck() {
	f1.submit();
}
-->
</script>
</head>
<body bgcolor="#FFFFFF" leftmargin="0" topmargin="0">
	<h1>更新服务器</h1>
	<%
	ServerManager smanager = ServerManager.getInstance();
	long id=Long.valueOf(request.getParameter("id"));
	Server server=smanager.getServer(id);
	 String a=request.getParameter("a");
	if(a!=null) {
	    String name=request.getParameter("name");
	    String gameipaddr=request.getParameter("gameipaddr");
	    String gameport=request.getParameter("gameport");
	    String dburi=request.getParameter("dburi");
	    String dbusername=request.getParameter("dbusername");
	    String dbpassword=request.getParameter("dbpassword");
	    String resinhome=request.getParameter("resinhome");
	    String savingNotifyUrl = request.getParameter("callbackurl");
	    String serverId = request.getParameter("serverid");
	    if(!"null".equals(name))
	    server.setName(name);
	    if(!"null".equals(gameipaddr))
	    server.setGameipaddr(gameipaddr);
	    if(!"null".equals(gameport))
	    server.setGameport(Integer.parseInt(gameport));
	    if(!"null".equals(dburi))
	    server.setDburi(dburi);
	    if(!"null".equals(dbusername))
	    server.setDbusername(dbusername);
	    if(!"null".equals(dbpassword))
	    server.setDbpassword(dbpassword);
	    if(!"null".equals(resinhome))
	    server.setResinhome(resinhome);
	    server.setServerid(serverId);
	    if(!"null".equals(savingNotifyUrl))
	   		server.setSavingNotifyUrl(savingNotifyUrl);
	    smanager.updateServer(server);
		response.sendRedirect("server_list.jsp?" );
		return;
	}
	%>
	 <form action="server_update.jsp" method="post">
	 <table id="test1" align="center" width="70%" cellpadding="0" cellspacing="0" border="0" class="sortable-onload-5-6r rowstyle-alt colstyle-alt no-arrow">
	 <tr>
	  <th align=left width=20%><b>服务器名称</b></th>
	  <td align="left" class="top"><input type="text" size="60" name="name" value="<%=server.getName()%>"/></td>
	 </tr>
	 <tr>
	  <th align=left width=20%><b>服务器序列</b></th>
	  <td align="left" class="top"><input id="name" type="text" name="seq" value="" onfocus="clean()"/>(0开始，递增)</td>
	 </tr>
	 <tr>
	  <th align=center width=20%><b>主服务器地址</b></th>
	  <td align="left"><input type="text" size="60" name="gameipaddr" value="<%=server.getGameipaddr() %>"/></td>
	 </tr>
	 <tr>
	  <th align=center width=20%><b>主服务器端口</b></th>
	  <td align="left"><input type="text" size="60" name="gameport" value="<%=server.getGameport() %>"/></td>
	 </tr>
	 <tr>
	  <th align=center width=20%><b>数据库URI</b></th>
	  <td align="left"><input type="text" size="60" name="dburi" value="<%=server.getDburi() %>"/></td>
	 </tr>
	 <tr>
	  <th align=center width=20%><b>数据库用户名</b></th>
	  <td align="left"><input type="text" size="60"  name="dbusername" value="<%=server.getDbusername() %>"/></td>
	 </tr>
	 <tr>
	  <th align=center width=20%><b>数据库密码</b></th>
	  <td align="left"><input type="text" size="60" name="dbpassword" value="<%=server.getDbpassword() %>"/></td>
	 </tr>
	<tr>
	  <th align=center width=20%><b>ServerId</b></th>
	  <td align="left"><input type="text" size="60" name="serverid" value="<%=server.getServerid() %>"/></td>
	 </tr>
	 <tr>
	  <th align=center width=20%><b>RESIN_HOME</b></th>
	  <td align="left"><input type="text" size="60" name="resinhome" value="<%=server.getResinhome() %>"/></td>
	 </tr>
	<tr>
	  <th align=center width=20%><b>充值通知地址</b></th>
	  <td align="left"><input type="text" size="60" name="callbackurl" value="<%=server.getSavingNotifyUrl() %>"/></td>
	 </tr>
	 <tr > <td colspan="2" align=center><input type="submit" value="确定" />
	 <input type="hidden" size="60" name="a" value="1111">
	 <input type="hidden" size="60" name="id" value="<%=id %>">
	 <input type="button" onclick="window.location.replace('server_list.jsp')" value="返 回" /> 
	 </td> </tr>
    </table>
	</form>
</body>
</html>
