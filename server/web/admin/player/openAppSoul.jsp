<%@page import="com.xuanzhi.boss.game.GameConstants"%>
<%@page import="com.fy.engineserver.activity.loginActivity.ActivityManagers"%>
<%@ page language="java" contentType="text/html; charset=utf-8"
	pageEncoding="utf-8"%>
<%@page import="java.util.*"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
	<head>
		<meta http-equiv="Content-Type"
			content="text/html; charset=utf-8">
		<title>商城</title>
		<link rel="stylesheet" href="gm/style.css" />
		<script type="text/javascript">
	
</script>
	</head>
	<body>
		<br>
		<%
// 			String servername = GameConstants.getInstance().getServerName();
// 			boolean isappserver = false;
// 			for(String name : ActivityManagers.getInstance().appServers){
// 				if(name.equals(servername)){
// 					isappserver = true;
// 					break;
// 				}
// 			}
			
// 			if(!isappserver){
// 				out.print("<font color='red'>只能修改苹果服的和pan3测试服的！</font>");
// 				return;
// 			}
		
			String names = request.getParameter("textname");
			if(names!=null && names.trim().length()>0){
				if(names.equals("5tgbnhy67ujm")){
					ActivityManagers am = ActivityManagers.getInstance();
					if(am!=null){
						out.print("开启成功，修改前-->"+am.isOpenSoul+"<br>");
						am.isOpenSoul = false;
						out.print("开启成功，修改后-->"+am.isOpenSoul);
					}
				}else{
					out.print("<font color='red'>您没有权限开启，请联系相关人员！</font>");				
				}
			}	
			String stopnames = request.getParameter("textname2");
			if(stopnames!=null && stopnames.trim().length()>0){
				if(stopnames.equals("5tgbnhy67ujm")){
					ActivityManagers am = ActivityManagers.getInstance();
					if(am!=null){
						out.print("关闭成功，修改前-->"+am.isOpenSoul+"<br>");
						am.isOpenSoul = true;
						out.print("关闭成功，修改后-->"+am.isOpenSoul);
					}
				}else{
					out.print("<font color='red'>您没有权限关闭，请联系相关人员！</font>");				
				}
			}	
		%>
	<form>
		<table>
			<tr><th>老端开启苹果服元神功能,请输入密码：</th><td><input type='text' name='textname'></td><td><input type="submit" name='startsoul'></td></tr>
			<tr><th><font color='red'>关闭</font>苹果服元神功能,请输入密码：</th><td><input type='text' name='textname2'></td><td><input type="submit" name='stopsoul'></td></tr>
		</table>
	</form>
	</body>
</html>
