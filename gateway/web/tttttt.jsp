
<%@page import="com.fy.gamegateway.mieshi.waigua.ClientAuthorization"%>
<%@page import="java.util.List"%><%@page import="com.fy.gamegateway.mieshi.waigua.AuthorizeManager"%>
<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
	<%
	String userName = request.getParameter("userName");
	String clientID = request.getParameter("clientID");
	AuthorizeManager.getInstance().testLog = true;
	if(userName != null && clientID != null){
		List<ClientAuthorization> caList = null;
		try {
			caList = AuthorizeManager.getInstance().getUsernameClientAuthorization(userName);
		} catch (Exception e) {
			out.print("[授权检查是否可以修改vip信息] [异常] ["+userName+"] ["+e+"]");
		}
		//上次登录时间最久的授权信息
		ClientAuthorization oldLoginAuthor = null;
		//当前clientID登录的授权信息
		ClientAuthorization currAuthor = null;
		for (ClientAuthorization ca : caList) {
			if(ca == null) continue;
			if (ca.getAuthorizeType() == 0) {
				if(oldLoginAuthor == null){
					oldLoginAuthor = ca;
				}else if(oldLoginAuthor.getLastLoginTime() > ca.getLastLoginTime()){
					oldLoginAuthor = ca;
				}
			}
			if(ca.getClientID().equals(clientID)){
				currAuthor = ca;
			}
		}
		 long THIRTY_DAY = 30 * 24 * 60 * 60 * 1000L;
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		out.print("[修改vip授权检查] [授权列表长度:"+caList.size()+"] [账号:"+userName+"] [clientID:"+clientID+"] [oldLoginAuthor:"+(oldLoginAuthor!=null?oldLoginAuthor.getClientID():"null")+"] [currAuthor:"+(currAuthor!=null?sdf.format(currAuthor.getLastLoginTime()):"null")+"]<br>");
		if(oldLoginAuthor != null && oldLoginAuthor.getClientID().equals(clientID)){
			out.print("结果：是主设备<br>");
		}else{
			out.print("结果：不是主设备<br>");
		}
		out.print(currAuthor.getLastLoginTime() + THIRTY_DAY - System.currentTimeMillis()+"<br>");
		out.print(currAuthor.getLastLoginTime()+"<br>");
		out.print("授权时间:"+sdf.format(currAuthor.getAuthorizeTime())+"<br>");
		out.print(THIRTY_DAY+"<br>");
		if(currAuthor != null && ((currAuthor.getAuthorizeTime()+ THIRTY_DAY )<= System.currentTimeMillis())){
			out.print("结果：授权超过30天");
		}else{
			out.print("结果：授权没超过30天");
		}
	}
	%>
	<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">

<%@page import="java.text.SimpleDateFormat"%><html xmlns="http://www.w3.org/1999/xhtml">
<head>
<meta http-equiv="Content-Type" content="text/html; charset=GBK" />
<title>Error</title>
</head>
	<form>
	<body>
	<table>
  <tr>
    <th>账号:</th><td><input type='text' name='userName' value='<%=(userName==null?"":userName) %>'> </td>
  </tr>
  <tr>
    <th>clientID:</th><td><input type='text' name='clientID' value='<%=(clientID==null?"":clientID) %>'> </td>
  </tr>
  <input type='submit' name='查询'>
</table>
</body>
</form>
	</html>

