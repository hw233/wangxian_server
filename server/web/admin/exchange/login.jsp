<%@ page contentType="text/html;charset=utf-8" import="java.util.*,com.xuanzhi.tools.text.*,
com.fy.engineserver.sprite.*,
com.fy.engineserver.sprite.concrete.*,
com.xuanzhi.tools.transport.*,
com.fy.engineserver.exchange.*,
com.fy.engineserver.exchange.concrete.* "%><%
	DefaultExchangeService des = DefaultExchangeService.getInstance();
	ExchangeManager em = des.getExchangeManager();

	String url = (String)session.getAttribute("exchange.url");
	
	GamePlayerManager pm = (GamePlayerManager)GamePlayerManager.getInstance();
	
	String action = request.getParameter("action");
	
	Player players[] = null;
	if(action != null && action.equals("login")){
		String username = request.getParameter("username");
		String password = request.getParameter("password");
		try{
			players = pm.getPlayerByUser(username);
		}catch(Exception e){}
		
	}else if(action != null && action.equals("select_player")){
		int pid = Integer.parseInt(request.getParameter("pid"));
		Player player = pm.getPlayer(pid);
		session.setAttribute("exchange.player",player);
		response.sendRedirect("./index.jsp");
		return;
	}
%>
<%@include file="../IPManager.jsp" %><html><head>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8">
</HEAD>
<BODY>
<h2>元宝交易所登录界面</h2><br>

<% if(action == null){ %>
<form id="f">
<input type='hidden' name='action' value='login'>
<table border="0" cellpadding="0" cellspacing="0" bgcolor="#FFFFFF" align="center">
<tr align='center' bgcolor='#FFFFFF'><td><b>请输入您的帐号名称</b></td><td><input type='text' name='username' size='10' value=''></td></tr>
<tr align='center' bgcolor='#FFFFFF'><td><b>请输入您的帐号密码</b></td><td><input type='password' name='password' size='10' value=''></td></tr>
<tr align='center' bgcolor='#FFFFFF'><td><input type='submit' value='提交'></td><td></td></tr>
</table>
</form>
<%
	}else if(action.equals("login")){ 
	if(players ==  null){
		out.println("<b>玩家【"+request.getParameter("username")+"】不存在</b>");
	}else{
		out.println("<table border='0' cellpadding='0' cellspacing='1' bgcolor='#000000' align='center'>");
		out.println("<tr align='center' bgcolor='#FFFFFF'><td>角色ID</td><td>角色名称</td><td>元宝</td><td>金币</td></tr>");
		for(int i = 0 ; i < players.length ; i++){
			out.println("<tr align='center' bgcolor='#FFFFFF'><td><a href='./login.jsp?pid="+players[i].getId()+"&action=select_player'>"+players[i].getId()+"</a></td><td>"+players[i].getName()+"</td><td>"+players[i].getRmbyuanbao()+"</td><td>"+players[i].getBindSilver()+"</td></tr>");
		}
		out.println("</table>");
	}

 }%>
</BODY></html>
