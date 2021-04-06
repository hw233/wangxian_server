<%@ page language="java" contentType="text/html; charset=utf-8"
    pageEncoding="utf-8"%>
<%@ page import="com.fy.engineserver.warehouse.service.*,com.fy.engineserver.warehouse.*,com.fy.engineserver.datasource.props.*,com.google.gson.*,java.util.*,com.fy.engineserver.sprite.*,
com.fy.engineserver.sprite.concrete.*,com.fy.engineserver.datasource.career.*,com.fy.engineserver.mail.*,
com.fy.engineserver.mail.service.*,com.xuanzhi.tools.text.*,com.fy.engineserver.closebetatest.*" %>
<%@include file="IPManager.jsp" %><html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8">
<title>发送邮件</title>
<link rel="stylesheet" type="text/css" href="../css/common.css" />
<link rel="stylesheet" type="text/css" href="../css/table.css" />
<style type="text/css">
td{
text-align:center;
}
.titlecolor{
background-color:#C2CAF5;
}
#showdiv{
background-color:#DCE0EB;
text-align:left;
margin:0px 0px;
}
</style>
</head>
<body>
<% 
FCAccountInfoManager fcm = FCAccountInfoManager.getInstance();
GMSendPresent gm = GMSendPresent.getInstance();
String stype = request.getParameter("stype"); 
String message = null; 
List<Mail> mails = null;
int PAGE_ROW_NO = 60;
String pageindex = request.getParameter("pageindex");
String[] userNames = request.getParameterValues("username");
String[] playerNames = request.getParameterValues("playername");
if(userNames != null){
	for(String username : userNames){
		if(username != null){
			gm.sendAccount(username);
		}
	}
}
if(playerNames != null){
	for(String pName : playerNames){
		if(pName != null){
			gm.sendPresent(pName);
		}
	}
}
int m = 0;
int index = 0;
if(pageindex == null){
	pageindex = "0";
}
m = Integer.parseInt(pageindex)*PAGE_ROW_NO;
index = Integer.parseInt(pageindex);
PlayerManager pmanager = PlayerManager.getInstance();
String playerName = request.getParameter("playerName");
%>
<form action="" name=f1>
	玩家名:<input type=text name=playerName size=20 value="<%=playerName %>"><br>
	<input type=hidden name=stype value="sub1"><br>
	<input type=submit name=sub1 value=" 提 交 ">
</form>
<%
if(stype != null) {
	FCAccountInfo[] fcs = null;
	if(fcm != null){
		fcs = fcm.getFCArray();
	}

	Player p = pmanager.getPlayer(playerName);
	MailManager mmanager = MailManager.getInstance();
	if(message != null) { 
		out.println(message); 
	} 
	
	int n = 0;
	if(mmanager.getAllMails(p) != null){
		n = mmanager.getAllMails(p).size();
	}
	if(n > 0){
		//每页60条数据
		int pageCount = 0;
		pageCount = n/PAGE_ROW_NO;
		if(n%PAGE_ROW_NO != 0){
			pageCount+=1;
		}
		out.println("&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;<b>当前页为第<font color='red'>"+(index+1)+"</font>页&nbsp;&nbsp;总共<font color='#2D20CA'>"+pageCount+"</font>页&nbsp;&nbsp;"+n+"条数据&nbsp;&nbsp;每页"+PAGE_ROW_NO+"条数据</b><br><br>");
		out.println("<a href='gmmails.jsp?stype=sub1&playerName="+playerName+"&pageindex="+0+"'>首页  </a>");
		if(index > 0){
			out.println("<a href='gmmails.jsp?stype=sub1&playerName="+playerName+"&pageindex="+(index-1)+"'><<上一页<<  </a>");	
		}
		for(int i = 0; i < 10; i++){
			if(index+i < pageCount){
			out.println("<a href='gmmails.jsp?stype=sub1&playerName="+playerName+"&pageindex="+(index+i)+"'>第"+(index+i+1)+"页  </a>");
			}
		}
		if(index <(pageCount-1)){
			out.println("<a href='gmmails.jsp?stype=sub1&playerName="+playerName+"&pageindex="+(index+1)+"'>>>下一页>>  </a>");	
		}
		out.println("<a href='gmmails.jsp?stype=sub1&playerName="+playerName+"&pageindex="+(pageCount-1)+"'>末页  </a>");
	}
	
	if((m+PAGE_ROW_NO)>n){
		mails = mmanager.getAllMails(p,m,(n-m));
	}else{
		mails = mmanager.getAllMails(p,m,PAGE_ROW_NO);
	}
	
	%>
	<%
	if(mails != null) {
	ArticleEntityManager aemanager = ArticleEntityManager.getInstance();
	ArticleManager amanager = ArticleManager.getInstance();
	 %>
	 <form action="" method="post"><input type="hidden" name=playerName value="<%=playerName %>"><input type=hidden name=stype value="sub1">
	 <table>
		 <tr class="titlecolor">
		  <td align=center width=10%><b>发件人<b></td>
		  <td align=center width=15%><b>标题<b></td>
		  <td align=center width=40%><b>内容<b></td>
		  <td align=center width=10%><b>发送时间<b></td>
		  <td align=center width=10%><b>给内测号<b></td>
		  <td align=center width=10%><b>给封测礼包<b></td>
		  <td align=center width=5%><b>此人已得封测号数<b></td>
		 </tr>
		 <%for(int i = 0; i < mails.size(); i++) {
			 Mail mail = mails.get(i);
		 	int senderId = mail.getPoster();
		 	String sender = "系统";
		 	Player player = null;
		 	if(senderId > 0) {
		 		sender = pmanager.getPlayer(senderId).getName();
		 		player = pmanager.getPlayer(senderId);
		 	}
		 	String title = mail.getTitle();
		 	String content = mail.getContent();
		 	int coins = mail.getCoins();
		 	Date sendTime = mail.getCreateDate();
		 	int status = mail.getStatus();
		 	Knapsack.Cell cells[] = mail.getCells();
		 %>
		 <tr>
		  <td align="center">
		  	<%=sender %>
		  </td>
		  <td align="left">
		  	<%=title %>
		  </td>
		  <td align="left">
		  	<%=content %>
		  </td>
		  <td align="left">
		  	<%=DateUtil.formatDate(sendTime,"yyyy-MM-dd HH:mm:ss") %>
		  </td>
		  <td align="left">
		  <%
		  if(player != null){
			  %>
			  <input type="checkbox" name="username" value="<%=player.getName() %>">给内测号
			  <%
		  }
		  %>
		  </td>
		  <td align="left">
		  <%
		  if(player != null){
			  %>
		  	<input type="checkbox" name="playername" value="<%=player.getName() %>">给封测礼包
		  	<%
		  } 
		  	%>
		  	</td>
		  
		  	<%
		  	if(fcs != null){
		  		boolean exist = false;
		  		for(FCAccountInfo fc : fcs){
		  			if(fc != null && player != null){
		  				if(fc.getFcAccount() != null && player.getUsername() != null && player.getUsername().equals(fc.getFcAccount())){
		  					exist = true;
		  					if(fc.getCount() == 5){
		  						out.println("<td bgcolor='red' align='left'>"+fc.getCount()+"</td>");
		  					}else if(fc.getCount() > 2){
		  						out.println("<td bgcolor='orange' align='left'>"+fc.getCount()+"</td>");
		  					}else if(fc.getCount() > 0){
		  						out.println("<td bgcolor='#C2CAF5' align='left'>"+fc.getCount()+"</td>");
		  					}else{
		  						out.println("<td align='left'>"+fc.getCount()+"</td>");
		  					}
		  					
		  				}
		  			}
		  		}
		  		if(!exist){
		  			%>
		  			<td>0</td>
		  			<%
		  		}
		  	}else{
		  		%>
		  		<td>0</td>
		  		<%
		  	}
		  	%>
		 
		 </tr>
		 <%} %>
		 <tr>
		 <td colspan="3"><input type="submit" value="发奖"></td>
		 <td colspan="4"><input type="reset" value="清空"></td>
		 </tr>
	    </table>
	    </form>
	<%} 
}
%>
</body>
</html>
