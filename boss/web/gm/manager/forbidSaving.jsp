<%@ page contentType="text/html;charset=utf-8"%><%@page import="com.fy.boss.authorize.service.PassportManager"%>
<%@page import="com.fy.boss.authorize.model.Passport"%>
<%@page import="com.fy.boss.finance.service.*"%>
<%
String message = null;
String id = request.getParameter("id");
if(id != null) {
	String act = request.getParameter("act");
	if(act != null) {
		if(act.equals("add")) {
			Passport passport = PassportManager.getInstance().getPassport(Long.valueOf(id));
			if(passport != null) {
				String code = passport.getLastLoginIp();
				if(code != null && !code.equals("116.213.192.216")) {
					SavingForbidManager.getInstance().addForbid(code, 1000);
					message = "封禁充值成功";
				} else {
					message = "设备号为“"+code+"”非法，无法封禁";
				}
			} else {
				message = "帐号不存在";
			}
		} else if(act.equals("remove")) {
			Passport passport = PassportManager.getInstance().getPassport(Long.valueOf(id));
			if(passport != null) {
				String code = passport.getLastLoginIp();
				if(code != null) {
					SavingForbidManager.getInstance().removeForbid(code);
					message = "充值解禁成功";
				} else {
					message = "设备号为空，无法解禁";
				}
			} else {
				message = "帐号不存在";
			}
		}
	}
}
%>
<html>
<br><br><br>
<center>
<%=message %><br><br>
		<input type=button name=bt1 value="返回" onclick="location.replace('getPassport.jsp')">
	</center>
</html>