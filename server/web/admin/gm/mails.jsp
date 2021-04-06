<%@ page language="java" contentType="text/html; charset=utf-8"
    pageEncoding="utf-8"%>
<%@ page import="com.google.gson.*,java.util.*,com.fy.engineserver.sprite.*,
com.fy.engineserver.sprite.concrete.*,com.fy.engineserver.datasource.career.*,com.fy.engineserver.mail.*,
com.fy.engineserver.mail.service.*,com.xuanzhi.tools.text.*" %>
<%@include file="../IPManager.jsp" %>
<%@page import="com.fy.engineserver.datasource.article.manager.ArticleEntityManager"%>
<%@page import="com.fy.engineserver.datasource.article.manager.ArticleManager"%>
<%@page import="com.fy.engineserver.datasource.article.data.props.Knapsack"%>
<%@page import="com.fy.engineserver.datasource.article.data.props.Cell"%>
<%@page import="com.fy.engineserver.datasource.article.data.entity.ArticleEntity"%>
<%@page import="com.fy.engineserver.datasource.article.data.articles.Article"%>
<%@page import="com.fy.engineserver.core.MailSubSystem"%>
<%@page import="com.fy.engineserver.message.MAIL_GETOUT_ARTICLE_REQ"%>
<%@page import="com.fy.gamegateway.message.GameMessageFactory"%>
<%@page import="com.fy.engineserver.message.MAIL_DELETE_REQ"%><html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8">
<title></title>
<script language="JavaScript">
<!--
-->
</script>
<link rel="stylesheet" type="text/css" href="../../css/common.css">
<link rel="stylesheet" type="text/css" href="../../css/table.css">
<script type="text/javascript">
function tiqufujian(mailId){
	document.getElementById("mailId").value = mailId;
	document.f3.submit();
}
function shanchufujian(mailId){
	document.getElementById("deleteMailId").value = mailId;
	document.f4.submit();
}
</script>
</head>
<body>
<% 
String stype = request.getParameter("stype"); 
String message = null; 
List<Mail> mails = null;
PlayerManager pmanager = PlayerManager.getInstance();
MailManager mmanager = MailManager.getInstance();
Player p = null;
try{
if(stype != null && stype.equals("sub1")) {
	String name = request.getParameter("playerName");
	p = pmanager.getPlayer(name);
}else if(stype != null && stype.equals("sub2")){
	String id = request.getParameter("playerId");
	p = pmanager.getPlayer(Long.valueOf(id));
}}catch(Exception e){
	e.printStackTrace();
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
	mails = mmanager.getAllMails(p);
	String mailId = request.getParameter("mailId");
	if(mailId != null && !mailId.trim().equals("")){
		Mail mail = mmanager.getMail(Long.parseLong(mailId));
		if(mail != null && mail.getReceiver() == p.getId()){

		}
	}
	String deleteMailId = request.getParameter("deleteMailId");
	if(deleteMailId != null && !deleteMailId.trim().equals("")){
		Mail mail = mmanager.getMail(Long.parseLong(deleteMailId));
		if(mail != null && mail.getReceiver() == p.getId()){
			MailSubSystem mss = MailSubSystem.getInstance();
			MAIL_DELETE_REQ req = new MAIL_DELETE_REQ(GameMessageFactory.nextSequnceNum(),Long.parseLong(deleteMailId));
			mss.handle_MAIL_DELETE_REQ(null,req,p);
		}
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
if(mails != null) {
ArticleEntityManager aemanager = ArticleEntityManager.getInstance();
ArticleManager amanager = ArticleManager.getInstance();
 %>
<form name="f3">
<input type="hidden" id="mailId" name="mailId">
</form>
<form name="f4">
<input type="hidden" id="deleteMailId" name="deleteMailId">
</form>
	<table id="test1" align="center" width="90%" cellpadding="2" cellspacing="1" border="0" bgcolor="#000000">
		<tr>
			<th bgcolor="#ffffff" align=center width=10%><b>邮件id</b></th>
			<th bgcolor="#ffffff" align=center width=10%><b>发件人</b></th>
			<th bgcolor="#ffffff" align=center width=15%><b>标题</b></th>
			<th bgcolor="#ffffff" align=center width=40%><b>内容</b></th>
			<th bgcolor="#ffffff" align=center width=6%><b>银子</b></th>
			<th bgcolor="#ffffff" align=center width=6%><b>付费</b></th>
			<th bgcolor="#ffffff" align=center width=10%><b>发送时间</b></th>
			<th bgcolor="#ffffff" align=center width=5%><b>状态</b></th>
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
		long price = mail.getPrice();
		Date sendTime = mail.getCreateDate();
		int status = mail.getStatus();
		Cell cells[] = mail.getCells();
		%>
		<tr>
			<td bgcolor="#ffffff" align="center" rowspan=2>
				<%=mail.getId() %>
			</td>
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
			<%=price %>
			</td>
			<td bgcolor="#ffffff" align="left">
				<%=DateUtil.formatDate(sendTime,"yyyy-MM-dd HH:mm:ss") %>
			</td>
			<td bgcolor="#ffffff" align="left">
				<%=Mail.STATUS_DESP[status] %>
			</td>
		 </tr>
		 <tr>
			<td colspan="7" bgcolor="#ffffff" align="left">附件：
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
		</tr>
	<%} %>
	</table>
<%} %>
</body>
</html>
