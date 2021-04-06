<%@page
	import="com.fy.engineserver.septstation.service.SeptStationManager"%>
<%@page import="com.fy.engineserver.septstation.SeptStation"%>
<%@page import="com.fy.engineserver.core.JiazuSubSystem"%>
<%@page import="com.fy.engineserver.jiazu.service.JiazuManager"%>
<%@page import="com.fy.engineserver.jiazu.Jiazu"%>
<%@page import="com.fy.engineserver.sprite.Player"%>
<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<%
	String jiazuName = request.getParameter("jiazuName");
	String jiazuPwd = request.getParameter("jiazuPwd");
	String pwdHit = request.getParameter("pwdHit");
	if (jiazuName != null) {
		Jiazu jiazu = JiazuManager.getInstance().getJiazu(jiazuName);
		if (jiazu == null) {
			out.print("家族不存在:" + jiazuName);
		} else {
			if (jiazu.getBaseID() <= 0) {
				out.print("家族:[" + jiazuName + "]还没有驻地,不必生成密码A");
			} else {
				SeptStation septStation = SeptStationManager.getInstance().getSeptStationById(jiazu.getBaseID());
				if (septStation == null) {
					out.print("家族:[" + jiazuName + "]还没有驻地,不必生成密码B");
				} else {
					if (jiazu.getJiazuPassword() == null || "".equals(jiazu.getJiazuPassword().trim())) {
						if (jiazuPwd == null || "".equals(jiazuPwd.trim()) || pwdHit == null || "".equals(pwdHit.trim())) {
							out.print("输入的密码或者提示不能为空,密码:[" + jiazuPwd + "]密码提示:[" + pwdHit + "]");
						} else {
							jiazu.setJiazuPassword(jiazuPwd);
							jiazu.setJiazuPasswordHint(pwdHit);
							out.print("设置密码完成,家族:[" + jiazuName + "]密码:[" + jiazuPwd + "]密码提示:[" + pwdHit + "]");
							JiazuSubSystem.logger.error("[JSP]设置密码完成,家族:[" + jiazuName + "]密码:[" + jiazuPwd + "]密码提示:[" + pwdHit + "]");
						}
					} else {
						out.print("家族原来有密码:[" + jiazu.getJiazuPassword() + "]密码提示[" + jiazu.getJiazuPasswordHint() + "]");
					}
				}
			}
		}
	} else {
		jiazuName = "";
		jiazuPwd = "";
		pwdHit = "";
	}
%>
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title>设置家族密码</title>
</head>
<body>
	<form action="./setJiazuPassword.jsp">
		家族名字:<input name="jiazuName" type="text" value="<%=jiazuName%>">
		家族密码:<input name="jiazuPwd" type="text" value="<%=jiazuPwd%>">
		密码提示:<input name="pwdHit" type="text" value="<%=pwdHit%>">
		<input type="submit" value="提交">
	</form>
</body>
</html>