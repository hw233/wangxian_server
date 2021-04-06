<%@ page language="java" contentType="text/html; charset=utf-8"
    pageEncoding="utf-8"%>

<%@include file="IPManager.jsp" %>
<%@page import="com.fy.engineserver.datasource.article.manager.ArticleEntityManager"%>
<%@page import="com.fy.engineserver.datasource.article.manager.ArticleManager"%>
<%@page import="com.fy.engineserver.datasource.article.data.props.Knapsack"%>
<%@page import="com.fy.engineserver.datasource.article.data.props.Cell"%>
<%@page import="com.fy.engineserver.datasource.article.data.entity.ArticleEntity"%>
<%@page import="com.fy.engineserver.datasource.article.data.articles.Article"%>
<%@page import="java.util.List"%>
<%@page import="com.fy.engineserver.mail.Mail"%>
<%@page import="com.fy.engineserver.sprite.PlayerManager"%>
<%@page import="com.fy.engineserver.sprite.Player"%>
<%@page import="com.fy.engineserver.mail.service.MailManager"%>
<%@page import="java.util.Date"%>
<%@page import="com.xuanzhi.tools.text.DateUtil"%><html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8">
<title>给玩家发邮件</title>
<script language="JavaScript">
<!--
-->
</script>
</head>
<body>
<% 
String stype = request.getParameter("stype"); 
String message = null; 
List<Mail> mails = null;
PlayerManager pmanager = PlayerManager.getInstance();
try{
if(stype != null && stype.equals("sub1")) {
	String name = request.getParameter("playerName");
	Player p = pmanager.getPlayer(name);
	MailManager mmanager = MailManager.getInstance();
	mails = mmanager.getAllMails(p);
}else if(stype != null && stype.equals("sub2")){
	String id = request.getParameter("playerId");
	Player p = pmanager.getPlayer(Integer.parseInt(id));
	MailManager mmanager = MailManager.getInstance();
	mails = mmanager.getAllMails(p);
}}catch(Exception e){
	out.println("没有这个角色");
}
if(message != null) { 
	out.println(message); 
} 
%>
<form action="" name=f1>
	玩家名:<input type=text name=playerName size=20><br>
	<input type=hidden name=stype value="sub1"><br>
	<input type=submit name=sub1 value=" 提 交 ">
</form>
<form action="" name=f2>
	玩家id:<input type=text name=playerId size=20><br>
	<input type=hidden name=stype value="sub2"><br>
	<input type=submit name=sub2 value=" 提 交 ">
</form>
<%
if(mails != null) {
ArticleEntityManager aemanager = ArticleEntityManager.getInstance();
ArticleManager amanager = ArticleManager.getInstance();
 %>
 <table id="test1" align="center" width="90%" cellpadding="2" cellspacing="1" border="0" bgcolor="#000000">
	 <tr>
	  <th bgcolor="#ffffff" galign=center width=10%><b>发件人<b></th>
	  <th bgcolor="#ffffff" align=center width=15%><b>标题<b></th>
	  <th bgcolor="#ffffff" align=center width=40%><b>内容<b></th>
	  <th bgcolor="#ffffff" align=center width=6%><b>金钱<b></th>
	  <th bgcolor="#ffffff" align=center width=10%><b>发送时间<b></th>
	  <th bgcolor="#ffffff" align=center width=5%><b>状态<b></th>
	  <th bgcolor="#ffffff" align=center width=14%><b>附件<b></th>
	 </tr>
	 <%for(Mail mail:mails) {
	 	long senderId = mail.getPoster();
	 	String sender = "系统";
	 	if(senderId > 0) {
	 		try {
	 			sender = pmanager.getPlayer(senderId).getName();
	 		} catch(Exception e) {
	 			e.printStackTrace();
	 			sender = "未知(角色不存在)";
	 		}
	 	}
	 	String title = mail.getTitle();
	 	String content = mail.getContent();
	 	long coins = mail.getCoins();
	 	Date sendTime = mail.getCreateDate();
	 	int status = mail.getStatus();
	 	Cell cells[] = mail.getCells();
	 %>
	 <tr>
	  <td bgcolor="#ffffff" align="center">
	  	<%=sender %>
	  </td>
	  <td bgcolor="#ffffff" align="left">
	  	<%=title %>
	  </td>
	  <td bgcolor="#ffffff" align="left">
	  	<%=content %>
	  </td>
	  <td bgcolor="#ffffff" align="left">
	  	<%=coins %>
	  </td>
	  <td bgcolor="#ffffff" align="left">
	  	<%=DateUtil.formatDate(sendTime,"yyyy-MM-dd HH:mm:ss") %>
	  </td>
	  <td bgcolor="#ffffff" align="left">
	  	<%=Mail.STATUS_DESP[status] %>
	  </td>
	  <td bgcolor="#ffffff" align="left">
	  	<%for(Cell cell:cells) {
	  		long aid = cell.getEntityId();
	  		if(aid > 0) {
	  		int count = cell.getCount();
	  		ArticleEntity ae = aemanager.getEntity(aid);
	  		Article a = amanager.getArticle(ae.getArticleName());
	  	%>
	  	<%=a.getName() %>(<%=count %>)<br>	
	  	<%} else {%>
	  	此格空<br>
	  	<%} }%>
	  </td>
	 </tr>
	 <%} %>
    </table>
<%} %>
</body>
</html>
