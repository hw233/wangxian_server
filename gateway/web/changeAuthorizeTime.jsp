
<%@page import="com.fy.gamegateway.mieshi.waigua.ClientAuthorization"%>
<%@page import="java.util.List"%><%@page import="com.fy.gamegateway.mieshi.waigua.AuthorizeManager"%>
<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
	<%
	//Object o = session.getAttribute("authorize.username");
	String userName = request.getParameter("userName");
	String clientID = request.getParameter("clientID");
	String time = request.getParameter("time");
	String pwd = request.getParameter("pwd");
	AuthorizeManager.getInstance().testLog = true;
	SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
	if(time == null){
		time = sdf.format(System.currentTimeMillis());
	}
	if(pwd == null || !pwd.equals("yanglei123")){
		if(pwd == null){
		}else{
			out.print("<font color='red'>密码不正确</font>");
		}
	}else{
		if(userName != null && clientID != null){
			List<ClientAuthorization> caList = null;
			try {
				caList = AuthorizeManager.getInstance().getUsernameClientAuthorization(userName);
			} catch (Exception e) {
				out.print("[授权检查是否可以修改vip信息] [异常] ["+userName+"] ["+e+"]");
				return;
			}
			if(caList.size() == 0){
				out.print("有效授权为空<br>");
				return;
			}
			ClientAuthorization currAuthor = null;
			for (ClientAuthorization ca : caList) {
				if(ca == null) continue;
				if(ca.getClientID().equals(clientID)){
					currAuthor = ca;
				}
			}
			if(currAuthor == null){
				out.print("账号和clientID对应关系不正确<br>");
				return;
			}
			
			out.print("[修改授权时间] [修改前] [授权列表长度:"+caList.size()+"] [授权时间:"+sdf.format(currAuthor.getAuthorizeTime())+"] [账号:"+userName+"] [clientID:"+clientID+"]<br>");
			String bTimeStr = sdf.format(currAuthor.getAuthorizeTime());
			long lTime = sdf.parse(time).getTime();
			String timeStr = sdf.format(lTime);
			currAuthor.setAuthorizeTime(lTime);
			out.print("[修改授权时间] [修改后] [授权列表长度:"+caList.size()+"] [授权时间:"+sdf.format(currAuthor.getAuthorizeTime())+"] [账号:"+userName+"] [clientID:"+clientID+"] [lTime:"+lTime+"] <br>");
			AuthorizeManager.logger.error("[后台修改授权时间] [账号:"+userName+"] [clientID:"+clientID+"] [修改前授权时间:"+bTimeStr+"] [修改后授权时间:"+timeStr+"]");
		}
	}
	%>
	<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">

<%@page import="java.text.SimpleDateFormat"%>
<%@page import="com.xuanzhi.boss.game.GameConstants"%><html xmlns="http://www.w3.org/1999/xhtml">
<head>
<meta http-equiv="Content-Type" content="text/html; charset=GBK" />
<title>Error</title>
</head>
	<form>
	<body>
	<table>
	<h1>设置xx账号下对应clientID的授权时间</h1>
  <tr>
    <th>账号:</th><td><input type='text' name='userName' value='<%=(userName==null?"":userName) %>'> </td>
  </tr>
  <tr>
    <th>clientID:</th><td><input type='text' name='clientID' value='<%=(clientID==null?"":clientID) %>'> </td>
  </tr>
  <tr>
    <th>授权时间:</th><td><input type='text' name='time' value='<%=(time==null?"":time) %>'> </td>
  </tr>
  <tr>
    <th>页面操作密码:</th><td><input type="password" name='pwd'> </td>
  </tr>
  <input type='submit' name='设置'>
</table>
</body>
</form>
	</html>

