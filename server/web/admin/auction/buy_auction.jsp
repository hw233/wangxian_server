<%@ page contentType="text/html;charset=utf-8" import="java.util.*,
											com.fy.engineserver.auction.*,
											com.fy.engineserver.auction.service.*,
											com.fy.engineserver.sprite.*,
											com.fy.engineserver.datasource.props.*,
											com.xuanzhi.tools.text.*"%>
<%@include file="IPManager.jsp" %><html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8">
<title></title>
<script language="JavaScript">
<!-- 
-->
</script>
</head>
<body>
<%
String aid = request.getParameter("aid");
String bname = request.getParameter("bname");
PlayerManager pmanager = PlayerManager.getInstance();
AuctionManager acmanager = AuctionManager.getInstance();
Auction auc = null;
String message = null;
if(bname != null) {
	auc = acmanager.getAuction(Long.parseLong(aid));
	Player bplayer = pmanager.getPlayer(bname);
	try{
		acmanager.buyAuction(auc, bplayer);
		message = "成功购买";
	} catch(Exception e) {
		e.printStackTrace();
		message = e.getMessage();
	}
} else if(aid != null) {
	auc = acmanager.getAuction(Long.parseLong(aid));
}
if(message != null) {
	out.println(message);
}
if(auc != null && bname == null) {
 %>
<h2>拍卖物品</h2>
<form action="" name=f1>
	物  品:<%=auc.getName() %><br>
	数  量:<%=auc.getCount() %><br>
	当前价:<%=auc.getNowPrice() %><br>
	起始价:<%=auc.getStartPrice() %><br>
	一口价:<%=auc.getBuyPrice() %><br>
	购买人name:<input type=text name=bname size=10><br>
	<input type=hidden name=aid value="<%=aid %>">
	<input type=submit name=sub1 value=" 确定以一口价购买 ">
</form>
<%} %>
</body>
</html>
