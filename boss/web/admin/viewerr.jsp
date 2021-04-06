<%@page import="com.xuanzhi.tools.text.ParamUtils"%>
<%@page import="com.fy.boss.deploy.DeployBoundry"%>
<%@ page language="java" contentType="text/html; charset=utf-8"
    pageEncoding="utf-8"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8">
<title>查看发布平台错误</title>
</head>
<body>
<%
	for(String errStr :  DeployBoundry.errinfoList)
	{
		out.println(errStr+"<br/>");
	}

	boolean isDo = ParamUtils.getBooleanParameter(request, "isDo");
	if(isDo)
	{
		DeployBoundry.errinfoList.clear();
		out.println("错误信息清理完毕!<br/>");
	}

%>
<form>
<input type="hidden" name="isDo" value="true" />
<input type="submit" name="clear" value="清除错误信息" />
</form>

</body>
</html>