<%@page import="org.apache.commons.lang.StringUtils"%>
<%@page import="com.xuanzhi.tools.servlet.HttpUtils"%>
<%@page import="java.util.HashMap"%>
<%@page import="java.net.URL"%>
<%@page import="com.fy.gamegateway.mieshi.server.MieshiServerInfoManager"%>
<%@page import="com.fy.gamegateway.mieshi.server.MieshiServerInfo"%>
<%@page import="com.xuanzhi.tools.text.ParamUtils"%>
<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%
boolean isDo = ParamUtils.getBooleanParameter(request, "isDo");

if(isDo)
{

	String fromserver = ParamUtils.getParameter(request, "fromserver", "");
	String toserver = ParamUtils.getParameter(request, "toserver", "");
	long playerId = ParamUtils.getLongParameter(request, "pid", -1);
	String username = ParamUtils.getParameter(request, "username", "");
	
	MieshiServerInfo info = MieshiServerInfoManager.getInstance().getServerInfoByName(fromserver);
	MieshiServerInfo toinfo = MieshiServerInfoManager.getInstance().getServerInfoByName(toserver);
	
	
	/* if (info.getServerType() == MieshiServerInfo.SERVERTYPE_WIN8专用服务器 || 
			info.getServerType() == MieshiServerInfo.SERVERTYPE_内部测试服务器 ||
			info.getServerType() == MieshiServerInfo.SERVERTYPE_对外测试的服务器 ||
			info.getServerType() == MieshiServerInfo.SERVERTYPE_文化部测试服务器 ||
			info.getServerType() == MieshiServerInfo.SERVERTYPE_移动专服) {
	} */
	//out.println("["+info.getName()+"] ["+info.getHttpPort()+"]");
	//out.println("<br>");
	try {
		String serverIp = info.getServerUrl();
		byte[] b = HttpUtils.webGet(new URL(serverIp+"/admin/yunying/convertplayerInfos.jsp?pid="+playerId+"&authorize.username=caolei&authorize.password=212006"), new HashMap(), 60000, 60000);
		if(b != null && b.length > 0){
			String json = new String(b);
			
			String toserverip = toinfo.getServerUrl();
			String content = "json="+json+(StringUtils.isEmpty(username)?"" : "&username="+username)+"&authorize.username=caolei&authorize.password=212006";
			byte[] bs =  HttpUtils.webPost(new URL(toserverip+"/admin/yunying/reiceiveplayer.jsp"),content.getBytes("utf-8"),new HashMap(), 60000, 60000);
			String result = new String(bs,"utf-8");
			if(result.contains("success"))
			{
				out.print("<script>alert('转移成功!');</script>");
			}
			else
			{
				out.print("content:"+content+"<br/>");
				out.print("result:"+result+"<br/>");
				out.print("<script>alert('转移失败!');</script>");
			}
			
		}
	} catch(Exception e) {
		throw e;
	}

}

	
%>

<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title>转移player</title>
</head>
<body>
<form method="post">
<input type="hidden" name="isDo" value="true">
	目标角色所在的服务器名称:<input type="text" name="fromserver" value="" ><br/>
	要转移到的服务器名称:<input type="text" name="toserver" value="" ><br/>
	角色id:<input type="text" name="pid"  value="" ><br/>
	要改成的账号名:<input type="text" name="username"  value="" >
	<input type="submit" value="提交" >
</form>

</body>
</html>

