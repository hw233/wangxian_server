<%@ page contentType="text/html;charset=utf-8"%>
<%@ page import="java.util.*,com.fy.engineserver.smith.waigua.*,com.fy.engineserver.sprite.*,USER_CLIENT_INFO_REQ"%>
<%@page import="com.fy.engineserver.message.USER_CLIENT_INFO_REQ;"%>
<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN">
<html>
	<head>
	<title></title>
	<meta http-equiv="pragma" content="no-cache">
	<meta http-equiv="cache-control" content="no-cache">
	<meta http-equiv="expires" content="0">
	<link rel="stylesheet" href="css/style.css" />
	<link rel="stylesheet" href="css/atalk.css" />
	<script type="text/javascript">
	
	</script>
	</head>

	<body>
		<%     
		WaiguaManager wm = WaiguaManager.getInstance();
		String idstr[] = request.getParameterValues("playerId");
		if(idstr != null && idstr.length > 0) {
			List<Long> flist = new ArrayList<Long>();
			for(int i=0; i<idstr.length; i++) {
				flist.add(Long.valueOf(idstr[i]));
			}
			wm.addForbidList(flist);
		}
	    %>                                              
	    <h2 style="font-weight:bold;">          
	    	玩家属性判断
	    </h2> 
		<center>
			<form action="waigua.jsp" name=f1 method="post">
			<table id="test1" align="center" width="80%" cellpadding="0"
				cellspacing="0" border="0"
				class="sortable-onload-5-6r rowstyle-alt colstyle-alt no-arrow">
				<tr>
					<th width=10%>
						<b>用户名</b>
					</th>
					<th width=20%>
						<b>角色</b>
					</th>
					<th width=10%>
						<b>级别</b>
					</th>
					<th width=20%>
						<b>充值</b>
					</th>
					<th width=20%>
						<b>IP</b>
					</th>
					<th width=20%>
						<b>机型</b>
					</th>
					<th width=10%>
						<b>类型判断</b>
					</th>
					<th width=15%>
						<b>操作</b>
					</th>
				</tr>
				<%
				Long players[] = wm.getRecordPlayers();
				for(Long pid : players) {
					Player p = PlayerManager.getInstance().getPlayer(pid);
					USER_CLIENT_INFO_REQ uc = null;
					try {
						if(p.getConn() != null) {
							uc = (USER_CLIENT_INFO_REQ)p.getConn().getAttachmentData("USER_CLIENT_INFO_REQ");
						}
					} catch(Exception e) {
						e.printStackTrace();
					}
				%>
				<tr>
					<td><%=p.getUsername() %></td>
					<td><%=p.getName() %></td>
					<td><%=p.getSoulLevel() %></td>
					<td><%=p.getRMB()/100+"元, VIP:" + p.getVipLevel() %></td>
					<td><%=(uc!=null?p.getConn().getRemoteAddress():"offline") %></td>
					<td><%=(uc!=null?uc.getPhoneType():"&nbsp;") %></td>
					<td><%=wm.isWaigua(p.getId())?"外挂":"&nbsp;" %></td>
					<td>
						<input type="checkbox" name=playerId value="<%=p.getId() %>"<%if(wm.isWaigua(p.getId())) out.print(" checked"); %>>
					</td>
				</tr>
				<%} %>
			</table><br><br>
			<input type="submit" name=bt1 value="封禁选中的">
			</form>
			<center>
		<br>
		<br>
	</body>
</html>