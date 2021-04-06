<%@page import="org.apache.commons.lang.StringUtils"%>
<%@ page import="com.fy.boss.game.model.Server"%>
<%@ page import="com.fy.boss.game.service.ServerManager"%>
<%@ page import="java.util.*,java.io.*,
				com.xuanzhi.tools.text.*"
				%>
<%@ page contentType="text/html;charset=utf-8"%>
<%@include file="header.jsp"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Strict//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-strict.dtd">
<html xmlns="http://www.w3.org/1999/xhtml" lang="zh-cn">
<head>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8">
<title>新增服务器</title>
<link rel="stylesheet" href="css/style.css"/>
<script language="JavaScript">
<!--
function subcheck() {
 var name=document.getElementById("name").value;
 var server=document.getElementById("server").value;
    if(!name||!server){
       document.getElementById("message").innerHTML = "请输入正确的游戏名";
    }{
	f1.submit();}
}
function clean(){
    document.getElementById("message").innerHTML = "";
 }
-->
</script>
</head>
<body bgcolor="#FFFFFF" leftmargin="0" topmargin="0">
	<h1>新增服务器</h1>
	<div id="message"></div>
	<%
	ServerManager smanager = ServerManager.getInstance();
	String name = request.getParameter("name");
	String message = null;
	if(name != null) {
	    String gameipaddr=request.getParameter("gameipaddr");
	    String gameport=request.getParameter("gameport");
	    String dburi=request.getParameter("dburi");
	    String dbusername=request.getParameter("dbusername");
	    String dbpassword=request.getParameter("dbpassword");
	    String resinhome=request.getParameter("resinhome");
		String savingNotifyUrl = request.getParameter("callbackurl");
		String serverId = request.getParameter("serverid");
		//查询server中的数据看是否有相同的，有相同的则不让提交
		List<Server> serverLst = ServerManager.getInstance().getServers();
		
		for(Server s : serverLst)
		{
				
			if(!StringUtils.isEmpty(name)){
				if(s.getName().trim().equalsIgnoreCase(name)){
					out.println("<script>alert('和现有服务器名字相同，故禁止提交');history.go(-1);</script>");
					return;
				}
			}
			if(!StringUtils.isEmpty(s.getGameipaddr()))
				if(s.getGameipaddr().trim().equalsIgnoreCase(gameipaddr.trim()))
				{
					if(s.getGameport() == Integer.parseInt(gameport))
					{
						out.println("<script>alert('和现有服务器ip和端口地址相同，故禁止提交');history.go(-1);</script>");
						return;
					}
					
					if(s.getResinhome().trim().equalsIgnoreCase(resinhome.trim()))
					{
						out.println("<script>alert('和现有服务器ip和resin地址相同，故禁止提交');history.go(-1);</script>");
						return;
					}
				}
			if(!StringUtils.isEmpty(s.getDburi()))
				if(s.getDburi().trim().equalsIgnoreCase(dburi.trim()))
				{
					if(!s.getDburi().toLowerCase().contains("oracle"))
					{
						out.println("<script>alert('非oracle数据库不能和现有数据库地址相同，故禁止提交');history.go(-1);</script>");
						return;
					}
					else
					{
						if(s.getDbusername().equalsIgnoreCase(dbusername))
						{
							out.println("<script>alert('oracle中dburl和现有数据库地址相同并且用户名一样，故禁止提交');history.go(-1);</script>");
							return;
						}
					}
				}
			
			if(!StringUtils.isEmpty(s.getServerid()))
				if(s.getServerid().trim().equalsIgnoreCase(serverId.trim()))
				{
					out.println("<script>alert('和现有ServerID相同，故禁止提交');history.go(-1);</script>");
					return;
				}
			if(!StringUtils.isEmpty(s.getSavingNotifyUrl()))
				if(s.getSavingNotifyUrl().trim().equalsIgnoreCase(savingNotifyUrl.trim()))
				{
					out.println("<script>alert('和现有充值地址相同，故禁止提交');history.go(-1);</script>");
					return;
				}
		}
		
		
		try {
			 Server server = new Server();
			 server.setName(name);
	         server.setGameipaddr(gameipaddr);
	         server.setGameport(Integer.parseInt(gameport));
	         server.setSavingNotifyUrl(savingNotifyUrl);
	         server.setDburi(dburi);
	         server.setDbusername(dbusername);
	         server.setDbpassword(dbpassword);
	         server.setServerid(serverId); 
	         server.setResinhome(resinhome);
			 server = smanager.createServer(server);
				if(server != null) {
					message = "创建成功!";
				} else {
					message = "创建失败，插入库失败";
				}
			} catch(Exception e) {
			e.printStackTrace();
			message = "失败:"+e.getMessage();
		}
	}
	%>
	<%if(message != null) {%>
	<span style="padding-left:50px;font-size:14px;color:red"><%=message%></span>
	<%}%>
	<form action="" method=post name=f1>
	 <table id="test1" align="center" width="70%" cellpadding="0" cellspacing="0" border="0" class="sortable-onload-5-6r rowstyle-alt colstyle-alt no-arrow">
	 	 <tr>
	  <th align=center width=20%><b>游戏</b></th>
	  <td align="left" class="top">飘渺寻仙曲</td>
	 </tr>
	 <tr>
	  <th align=left width=20%><b>服务器名称</b></th>
	  <td align="left" class="top"><input size=50  id="name" type="text" name="name" value="" onfocus="clean()"/></td>
	 </tr>
	 <tr>
	  <th align=center width=20%><b>主服务器地址</b></th>
	  <td align="left"><input size=50  id="server" type="text" name="gameipaddr" value="" onfocus="clean()"/></td>
	 </tr>
	 <tr>
	  <th align=center width=20%><b>主服务器端口</b></th>
	  <td align="left"><input size=50  type="text" name="gameport" value=""/></td>
	 </tr>
	 <tr>
	  <th align=center width=20%>服务器serverId</th>
	  <td align="left"><input size=50  type="text" name="serverid" value=""/></td>
	 </tr>
	 <tr>
	  <th align=left width=20%><b>充值通知接收地址</b></th>
	  <td align="left" class="top"><input size=50  id="callbackurl" type="text" name="callbackurl" value="" onfocus="clean()" size="30"/></td>
	 </tr>
	 <tr>
	  <th align=center width=20%><b>数据库uri</b></th>
	  <td align="left"><input size=50  type="text" name="dburi" value=""/></td>
	 </tr>
	 <tr>
	  <th align=center width=20%><b>数据库用户名</b></th>
	  <td align="left"><input size=50  type="text" name="dbusername" value=""/></td>
	 </tr>
	 <tr>
	  <th align=center width=20%><b>数据库密码</b></th>
	  <td align="left"><input size=50  type="text" name="dbpassword" value=""/></td>
	 </tr>
	 <tr>
	  <th align=center width=20%><b>RESIN_HOME</b></th>
	  <td align="left"><input size=50  type="text" name="resinhome" value="/home/game/resin_server"/></td>
	 </tr>
	 </table>
		<input type=button name=sub1 value=" 提 交 " onclick="subcheck()"/> |
		<input type="button" onclick="window.location.replace('server_list.jsp')" value="返回上一页" /> 
	</form>
</body>
</html>
