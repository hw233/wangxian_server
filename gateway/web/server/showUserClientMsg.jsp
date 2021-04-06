<%@page import="com.xuanzhi.tools.text.DateUtil"%>
<%@page import="com.fy.gamegateway.mieshi.waigua.NewLoginHandler"%>
<%@page import="java.util.Date"%>
<%@page import="java.text.SimpleDateFormat"%>
<%@page import="java.util.ArrayList"%>
<%@page import="com.fy.gamegateway.mieshi.waigua.*"%>
<%@page import="java.util.List"%>
<%@page import="com.fy.gamegateway.mieshi.waigua.AuthorizeManager"%>
<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title>Insert title here</title>
</head>
<body>
	<%
		List<ClientAuthorization> cas = new ArrayList<ClientAuthorization>();
		String action = request.getParameter("action");
		int chaxuntype = 0;
		if (action != null) {
			if ("chaxun".equals(action)) {
				String user = request.getParameter("username");
				chaxuntype = Integer.parseInt(request.getParameter("chaxuntype"));
				if(chaxuntype == 0)
					cas = AuthorizeManager.getInstance().getUsernameClientAuthorization(user.trim());
				else
					cas = AuthorizeManager.getInstance().em_ca.query(ClientAuthorization.class,"clientID='"+user.trim()+"'","authorizeTime desc",1,10000);
			}
			else if ("shanchuCa".equals(action)) {
				String caIndex = request.getParameter("caIndex");
				int index = Integer.parseInt(caIndex);
				String user = request.getParameter("username");
				cas = AuthorizeManager.getInstance().getUsernameClientAuthorization(user);
				ClientAuthorization ca = cas.remove(index);
				ca.setLastLoginTime(0);
				AuthorizeManager.getInstance().em_ca.flush(ca);
				AuthorizeManager.logger.warn("[从页面删除一个CA] ["+ca.getId()+"] ["+ca.getClientID()+"] ["+ca.getAuthorizeState()+"] ["+ca.getUsername()+"] ["+ca.getAuthorizeTime()+"]");
			}else if ("kaiqishouquan".equals(action)) {
				AuthorizeManager.isTestNeedAuthorize = !AuthorizeManager.isTestNeedAuthorize;
			}else if ("ceshi100".equals(action)) {
				NewLoginHandler.textIndex += 1;
				if (NewLoginHandler.textIndex >= 100) {
					NewLoginHandler.textIndex = -1;
				}
			}
		}
	%>
	<form>
		<input type="hidden" name="action" value="chaxun">
		<input type='radio' name="chaxuntype" value="0" <%= chaxuntype==0?"checked":"" %> >按用户名 &nbsp;	
		<input type='radio' name="chaxuntype" value="1" <%= chaxuntype==1?"checked":"" %> >按ClientID
		<input name="username" type="text">
		<input type="submit" value="确定">
	</form>
	<table border="1">
		<tr>
			<td>用户名</td>
			<td>授权状态</td>
			<td>授权方式</td>
			<td>授权时间</td>
			<td>最后登录时间</td>
			<td>登录次数</td>
			<td>请求授权次数</td>
			<td>客户端ID</td>
			<td>IP地址</td>
			<td>MAC地址</td>
			<td>平台</td>
			<td>机型</td>
			<td>GPU型号</td>
			<td>客户端创建时间</td>
			<td>授权客户端ID</td>
			<td>删除</td>
		</tr>
		<%
			String aa[] = new String[]{"等待", "正常", "被替代", "异常", "拒绝", "等待过期"};
			String bb[] = new String[]{"自动授权", "手动授权或拒绝"};
			for (int i = 0 ; i< cas.size(); i++) {
				ClientAuthorization ca = cas.get(i);
				
				ClientEntity ce = AuthorizeManager.getInstance().getClientEntity(ca.getClientID());
								
				String mac = "-";							//mac地址
				
				String platform = "-";					//平台
				
				String phoneType = "-";					//机型
				
				String gpuType = "-";						//GPU类型
				
				long createTime = 0;		//创建时间
				
				if(ce != null){
					mac = ce.getMac();
					platform = ce.getPlatform();
					phoneType = ce.getPhoneType();
					gpuType = ce.getGpuType();
					createTime = ce.getCreateTime();
				}
		%>
			<tr>
				<td><%=ca.getUsername() %></td>
				<td><%=aa[ca.getAuthorizeState()] %></td>
				<td><%=bb[ca.getAuthorizeType()] %></td>
				<td><%=DateUtil.formatDate(new Date(ca.getAuthorizeTime()), "yyyy-MM-dd HH:mm:ss") %></td>
				<td><%=DateUtil.formatDate(new Date(ca.getLastLoginTime()), "yyyy-MM-dd HH:mm:ss") %></td>
				<td><%=ca.getIp() %></td>
				<td><%=ca.getLoginNum() %></td>
				<td><%=ca.getQueryNum() %></td>
				<td><%=ca.getClientID() %></td>
				<td><%=mac %></td>
				<td><%=platform %></td>
				<td><%=phoneType %></td>
				<td><%=gpuType %></td>
				<td><%= DateUtil.formatDate(new Date(createTime), "yyyy-MM-dd HH:mm:ss")  %></td>
				<td><%=ca.getAuthorizeClientId() %></td>
				<td></td>
			</tr>
		<%
			}
		%>
	</table>
	
	
</body>
</html>
