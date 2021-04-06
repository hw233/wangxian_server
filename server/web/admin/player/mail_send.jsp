<%@ page language="java" contentType="text/html; charset=utf-8"
    pageEncoding="utf-8"%>
<%@ page import="com.google.gson.*,java.util.*,com.fy.engineserver.sprite.*,com.fy.engineserver.sprite.concrete.*,com.fy.engineserver.datasource.career.*,com.fy.engineserver.mail.*,com.fy.engineserver.mail.service.*,com.xuanzhi.tools.text.*" %>
<%@include file="../IPManager.jsp" %>
<%@page import="com.fy.engineserver.datasource.article.manager.ArticleEntityManager"%>
<%@page import="com.fy.engineserver.datasource.article.manager.ArticleManager"%>
<%@page import="com.fy.engineserver.datasource.article.data.props.Knapsack"%>
<%@page import="com.fy.engineserver.datasource.article.data.props.Cell"%>
<%@page import="com.fy.engineserver.datasource.article.data.entity.ArticleEntity"%>
<%@page import="com.fy.engineserver.datasource.article.data.articles.Article"%>
<%@page import="com.fy.engineserver.core.MailSubSystem"%>
<%@page import="com.fy.engineserver.message.MAIL_CREATE_REQ"%>
<%@page import="com.fy.gamegateway.message.GameMessageFactory"%><html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8">
<title></title>
<script language="JavaScript">
<!--
-->
</script>
<link rel="stylesheet" type="text/css" href="../../css/common.css">
<link rel="stylesheet" type="text/css" href="../../css/table.css">
</head>
<body>
<% 
MailManager mmanager = MailManager.getInstance();
String stype = request.getParameter("stype");
String receiveId = request.getParameter("receiveId");


String message = null; 
List<Mail> mails = null;
PlayerManager pmanager = PlayerManager.getInstance();
Player p = null;
try{
if(stype != null && stype.equals("sub1")) {
	String name = request.getParameter("playerName");
	p = pmanager.getPlayer(name);

}else if(stype != null && stype.equals("sub2")){
	String id = request.getParameter("playerId");
	p = pmanager.getPlayer(Integer.parseInt(id));
}}catch(Exception e){
	out.println("没有这个角色");
}
if(p == null){
	long playerId = -1;
	String playerName = null;
	Object obj = session.getAttribute("playerid");
	
	if(obj != null){
		playerId = Long.parseLong(obj.toString());
		p = pmanager.getPlayer(playerId);
	}else{
		Object obj2 = session.getAttribute("playerName");
		if(obj2 != null){
			playerName = obj2.toString();
			p = pmanager.getPlayer(playerName);
		}
	}
}
if(p != null){
	session.setAttribute("playerid",p.getId());
	session.setAttribute("playerName",p.getName());
	if(receiveId != null && !receiveId.trim().equals("")){
		int[] ks = new int[6];
		for(int i = 0; i < ks.length; i++){
			ks[i] = -1;
		}
		int[] cs = new int[6];
		for(int i = 0; i < cs.length; i++){
			cs[i] = -1;
		}
		int[] counts = new int[6];
		String title = request.getParameter("title");
		String content = request.getParameter("content");
		int money = 0;
		try{
			money = Integer.parseInt(request.getParameter("money"));
		}catch(Exception ex){
			
		}
		int price = 0;
		try{
			price = Integer.parseInt(request.getParameter("price"));
		}catch(Exception ex){
			
		}
		try{
			ks[0] = Integer.parseInt(request.getParameter("k0"));
		}catch(Exception ex){
			
		}
		try{
			ks[1] = Integer.parseInt(request.getParameter("k1"));
		}catch(Exception ex){
			
		}
		try{
			ks[2] = Integer.parseInt(request.getParameter("k2"));
		}catch(Exception ex){
			
		}
		try{
			ks[3] = Integer.parseInt(request.getParameter("k3"));
		}catch(Exception ex){
			
		}
		try{
			ks[4] = Integer.parseInt(request.getParameter("k4"));
		}catch(Exception ex){
			
		}
		try{
			ks[5] = Integer.parseInt(request.getParameter("k5"));
		}catch(Exception ex){
			
		}
		try{
			cs[0] = Integer.parseInt(request.getParameter("c0"));
		}catch(Exception ex){
			
		}
		try{
			cs[1] = Integer.parseInt(request.getParameter("c1"));
		}catch(Exception ex){
			
		}
		try{
			cs[2] = Integer.parseInt(request.getParameter("c2"));
		}catch(Exception ex){
			
		}
		try{
			cs[3] = Integer.parseInt(request.getParameter("c3"));
		}catch(Exception ex){
			
		}
		try{
			cs[4] = Integer.parseInt(request.getParameter("c4"));
		}catch(Exception ex){
			
		}
		try{
			cs[5] = Integer.parseInt(request.getParameter("c5"));
		}catch(Exception ex){
			
		}
		try{
			counts[0] = Integer.parseInt(request.getParameter("count0"));
		}catch(Exception ex){
			
		}
		try{
			counts[1] = Integer.parseInt(request.getParameter("count1"));
		}catch(Exception ex){
			
		}
		try{
			counts[2] = Integer.parseInt(request.getParameter("count2"));
		}catch(Exception ex){
			
		}
		try{
			counts[3] = Integer.parseInt(request.getParameter("count3"));
		}catch(Exception ex){
			
		}
		try{
			counts[4] = Integer.parseInt(request.getParameter("count4"));
		}catch(Exception ex){
			
		}
		try{
			counts[5] = Integer.parseInt(request.getParameter("count5"));
		}catch(Exception ex){
			
		}
		MailSubSystem mss = MailSubSystem.getInstance();
//		MAIL_CREATE_REQ req = new MAIL_CREATE_REQ(GameMessageFactory.nextSequnceNum(),receiveId,title,content,ks,cs,counts,money,price);
//		mss.confirmCreateMail(req,p);
	}
}
if(message != null) { 
	out.println(message); 
} 
%>
<form action="" name=f1>
	玩家名:<input type=text name=playerName size=20 value="<%=(p != null ? p.getName():"") %>"><br>
	<input type=hidden name=stype value="sub1"><br>
	<input type=submit name=sub1 value=" 提 交 ">
</form>
<form action="" name=f2>
	玩家id:<input type=text name=playerId size=20 value="<%=(p != null ? p.getId():"") %>"><br>
	<input type=hidden name=stype value="sub2"><br>
	<input type=submit name=sub2 value=" 提 交 ">
</form>
<%
ArticleEntityManager aemanager = ArticleEntityManager.getInstance();
ArticleManager amanager = ArticleManager.getInstance();
 %>
 <form method="post">
 <table>
	 <tr>
	  <th bgcolor="#ffffff" align=center width=10%><b>收件人</b></th>
	  <th bgcolor="#ffffff" align=center width=15%><b>标题</b></th>
	  <th bgcolor="#ffffff" align=center width=40%><b>内容</b></th>
	  <th bgcolor="#ffffff" align=center width=6%><b>金钱</b></th>
	  <th bgcolor="#ffffff" align=center width=6%><b>对方付费</b></th>
	   <th bgcolor="#ffffff" align=center width=6%></th>
	 </tr>
	 <tr>
	  <td bgcolor="#ffffff" align="center">
	  	<input id="receiveId" name="receiveId">
	  </td>
	  <td bgcolor="#ffffff" align="left">
	  	<input id="title" name="title">
	  </td>
	  <td bgcolor="#ffffff" align="left">
	  	<input id="content" name="content">
	  </td>
	  <td bgcolor="#ffffff" align="left">
	  	<input id="money" name="money">
	  </td>
	  <td>
	  <input id="price" name="price">
	  </td>
	  <td rowspan="2">
	  	<input type="submit" value="发送">
	  </td>
	  </tr>
	  <tr>
	  <td colspan="5">
	  	背包:<input id="k0" name="k0">格子位置:<input id="c0" name="c0">数量:<input id="count0" name="count0"><br/>
	  	背包:<input id="k1" name="k1">格子位置:<input id="c1" name="c1">数量:<input id="count1" name="count1"><br/>
	  	背包:<input id="k2" name="k2">格子位置:<input id="c2" name="c2">数量:<input id="count2" name="count2"><br/>
	  	背包:<input id="k3" name="k3">格子位置:<input id="c3" name="c3">数量:<input id="count3" name="count3"><br/>
	  	背包:<input id="k4" name="k4">格子位置:<input id="c4" name="c4">数量:<input id="count4" name="count4"><br/>
	  	背包:<input id="k5" name="k5">格子位置:<input id="c5" name="c5">数量:<input id="count5" name="count5"><br/>
	  </td>
</tr>
</table>
</form>
</body>
</html>
