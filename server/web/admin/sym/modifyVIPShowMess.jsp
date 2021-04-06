<%@page import="com.fy.engineserver.activity.loginActivity.ActivityManagers"%>
<%@page import="com.fy.engineserver.activity.VipHelpMess"%>
<%@ page language="java" contentType="text/html; charset=utf-8"
	pageEncoding="utf-8"%>
</html>
<head>
<title>test</title>
</head>
<body>

<%
for (VipHelpMess vm : ActivityManagers.getInstance().vipMesses) {
	if (vm.viplevel >= 8) {
		vm.content = "<f color='0xff0000'>各位仙友，更多飘渺寻仙曲VIP专属活动请您联系企业QQ4000860066与您的VIP客户经理了解详情，还有更多福利、更多优惠尊享~</f>";
	}
}
out.println("修改成功");
%>
</table>
</body>
</html>
