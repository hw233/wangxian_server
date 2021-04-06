<%@page import="com.fy.boss.authorize.service.PassportManager"%>
<%@page import="com.fy.boss.authorize.model.Passport"%>
<%@page import="com.fy.boss.finance.service.*"%>
<%@ page contentType="text/html;charset=utf-8"%>
<%@ page import="java.text.SimpleDateFormat;" %>
<html xmlns="http://www.w3.org/1999/xhtml" lang="zh-cn">
<head>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8">
<title>获得通行证</title>
<link rel="stylesheet" href="../css/style.css"/>
<script language="JavaScript">

	function query(){
		var name = document.getElementById("username").value;
		if(name){
			var str = "?username="+name;
			window.location.replace(str);
		}else{
			alert("用户名不能为空!!");
		}
		
	}
</script>
</head>
<body bgcolor="#c8edcc">
    <table width='60%'>
    <h1>通行证查询</h1>
    	<%
    		String mess = "根据用户名查找通行证";
    		String username = request.getParameter("username");
    		PassportManager pm = PassportManager.getInstance();
    		if(username!=null&&username.trim().length()>0){
				Passport passport = pm.getPassport(username);
    			if(passport!=null){
    				String lastQuestionSetDate = "";
    				String lastChargeDate = "";
    				if(passport.getLastLoginDate()!=null){
    					lastQuestionSetDate = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(passport.getLastLoginDate());
    				}
    				if(passport.getLastChargeDate()!=null){
    					lastChargeDate = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(passport.getLastChargeDate());
    				}
    				
    				out.print("<tr><th>密保问题</th><th>密保答案</th><th>机型</th><th>渠道</th><th>客户端ID</th><th>最后一次登录日期</th><th>最后一次充值日期</th><th>最后一次充值金额</th><th>是否禁止充值</th><th>设备信息</th></tr>");
					out.print("<tr><td>"+passport.getSecretQuestion()+"</td>"
					             +"<td>"+passport.getSecretAnswer()+"</td>"+
								  "<td>"+passport.getLastLoginMobileType()+"</td>"
					             +"<td>"+passport.getRegisterChannel()+"</td>"
								 +"<td>"+passport.getLastLoginClientId()+"</td>"
					             +"<td>"+lastQuestionSetDate+"</td>"
								 +"<td>"+lastChargeDate+"</td>"
					             +"<td>"+passport.getLastChargeAmount()+"</td>"
								 +"<td>"+(SavingForbidManager.getInstance().isForbid(passport,passport.getLastLoginIp())?"是<input type=\"button\" name=\"bt1\" onclick=\"location.replace('forbidSaving.jsp?act=remove&id="+passport.getId()+"')\">":"否")+"</td>"
					             +"<td>"+passport.getLastLoginIp()+"<input type=button name=bt1 value=\"封禁此设备充值\" onclick=\"location.replace('forbidSaving.jsp?act=add&id="+passport.getId()+"')\"></td></tr>");	
    			}else{
    				mess = "通行证不存在！！";
    			}
    			
    		}else{
    			mess = "用户名不能为空！";
    		}
    	%>
		</table>
			<tr><th>用户名</th><td><input type='text' id='username' name='username' value=''/><font color = 'red'><%=mess %></font></td></tr>
			<tr><td><input type='button' value='查询' onclick='query()' /></td></tr>
			
			
	
</body>
</html>
